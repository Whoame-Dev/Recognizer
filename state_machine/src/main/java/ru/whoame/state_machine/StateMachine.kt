package ru.whoame.state_machine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import ru.whoame.state_machine.contract.BaseDefaultsFactory
import ru.whoame.state_machine.contract.BaseStateFactory
import ru.whoame.state_machine.contract.Transaction
import ru.whoame.state_machine.handler.BaseAnalyticsEventHandler
import ru.whoame.state_machine.handler.BaseDomainEventHandler
import ru.whoame.state_machine.handler.BaseUiEventHandler
import ru.whoame.state_machine.logger.LoggerSettings
import ru.whoame.state_machine.logger.StateMachineLogger
import kotlin.coroutines.CoroutineContext

/**
 * A reactive state machine that manages application state, UI events, domain events, and side effects.
 *
 * This state machine follows a unidirectional data flow pattern, processing events through handlers
 * and updating state accordingly. It provides separate flows for UI state observation and side effect handling.
 *
 * @param UiState The type representing the UI state exposed to the presentation layer
 * @param State The type representing the internal domain state
 * @param SideEffect The type representing side effects (e.g., navigation, notifications)
 * @param UiEvent The type representing events triggered from the UI layer
 * @param DomainEvent The type representing events from the domain/business logic layer
 *
 * @param loggerSettings Configuration settings for the state machine logger
 * @param scope The coroutine scope used for launching internal coroutines
 * @param defaultsFactory Factory providing initial state and default domain events
 * @param stateFactory Factory for converting internal state to UI state
 * @param uiEventHandler Optional handler for processing UI events
 * @param domainEventHandler Optional handler for processing domain events
 * @param analyticsEventHandler Optional handler for processing all events, side effects and errors for analytics purposes
 * @param context The coroutine context used for internal operations (defaults to [Dispatchers.Default])
 * @param onCreated Callback invoked after the state machine is fully initialized
 **/
@OptIn(ExperimentalCoroutinesApi::class)
class StateMachine<UiState : Any, State : Any, SideEffect : Any, UiEvent : Any, DomainEvent : Any>(
    loggerSettings: LoggerSettings,
    scope: CoroutineScope,
    defaultsFactory: BaseDefaultsFactory<State, DomainEvent>,
    stateFactory: BaseStateFactory<State, UiState>,
    private val uiEventHandler: BaseUiEventHandler<State, SideEffect, UiEvent, DomainEvent>? = null,
    private val domainEventHandler: BaseDomainEventHandler<State, SideEffect, DomainEvent>? = null,
    analyticsEventHandler: BaseAnalyticsEventHandler<State, SideEffect, UiEvent, DomainEvent>? = null,
    context: CoroutineContext = Dispatchers.Default,
    onCreated: () -> Unit = {},
) {

    private val logger = StateMachineLogger(
        tag = this.javaClass.simpleName,
        parentTag = loggerSettings.tag,
        type = loggerSettings.type,
        errorListener = { throwable -> analyticsEventHandler?.sendError(throwable) },
    )

    private val mutableStateFlow: MutableStateFlow<State> = MutableStateFlow(value = defaultsFactory.state())

    private val uiMutableStateFlow: MutableStateFlow<UiState> = MutableStateFlow(
        value = stateFactory.convert(mutableStateFlow.value),
    )
    /**
     * This flow emits the current UI state whenever the internal state changes.
     * UI components should collect from this flow to observe state changes and update accordingly.
     * The flow is hot, meaning it will immediately emit the current state to new collectors.
     **/
    val uiStateFlow: StateFlow<UiState> get() = uiMutableStateFlow.asStateFlow()

    private val sideEffectSharedFlow: MutableSharedFlow<SideEffect> = MutableSharedFlow()
    /**
     * This flow emits side effects such as navigation commands, error messages, or other
     * one-time events that should be handled by the UI.
     * Unlike state, side effects are consumed once and not replayed to new collectors.
     **/
    val sideEffectFlow: SharedFlow<SideEffect> get() = sideEffectSharedFlow.asSharedFlow()

    private val transactionChannel: Channel<Transaction<State, SideEffect, UiEvent, DomainEvent>> = Channel()

    init {
        logger.starting()
        uiEventHandler?.setStateFlow(mutableStateFlow)
        domainEventHandler?.setStateFlow(mutableStateFlow)
        analyticsEventHandler?.setStateFlow(mutableStateFlow)

        scope.launch(context) {
            mutableStateFlow.collect { state ->
                try {
                    logger.newState(state)
                    val uiState = stateFactory.convert(state)
                    logger.newUiState(uiState)
                    uiMutableStateFlow.value = uiState
                } catch (e: Throwable) {
                    logger.convertStateThrowable(stateFactory.javaClass.simpleName, e)
                }
            }
        }

        val defaultDomainEventTransactionsFlow = defaultsFactory.domainEvents()
            .asFlow()
            .map { event ->
                logger.defaultDomainEvent(event)
                Transaction.DomainEvent<State, SideEffect, UiEvent, DomainEvent>(event)
            }

        val uiEventHandlerTransactionFlow = uiEventHandler?.transactionFlow
            ?.catch { throwable ->
                logger.handleEventThrowable(uiEventHandler.javaClass.simpleName, throwable)
            }
            ?: emptyFlow()

        val domainEventHandlerTransactionFlow = domainEventHandler?.transactionFlow
            ?.catch { throwable ->
                logger.handleEventThrowable(domainEventHandler.javaClass.simpleName, throwable)
            }
            ?: emptyFlow()

        val transactionsFlow = merge(
            defaultDomainEventTransactionsFlow,
            transactionChannel.receiveAsFlow(),
            uiEventHandlerTransactionFlow,
            domainEventHandlerTransactionFlow,
        )
            .onEach { transaction ->
                logger.newTransaction(transaction)
                analyticsEventHandler?.sendTransaction(transaction)
            }
            .flatMapMerge(transform = ::handleTransaction)
            .buffer()
            .catch { throwable ->
                logger.handleTransactionThrowable(throwable)
            }

        scope.launch(context) {
            transactionsFlow.collect()
        }

        val analyticsFlow = analyticsEventHandler?.analyticsFlow?.buffer()
            ?.catch { throwable ->
                logger.handleAnalyticsThrowable(throwable)
            }

        analyticsFlow?.let { flow ->
            scope.launch(context) {
                flow.collect()
            }
        }

        onCreated.invoke()
        logger.started()
    }

    private suspend fun handleTransaction(
        transaction: Transaction<State, SideEffect, out UiEvent, DomainEvent>,
    ): Flow<Any?> = when (transaction) {
        is Transaction.StateUpdate -> transaction.block.let { block ->
            logger.newStateUpdate(block)
            mutableStateFlow.update(block)
            emptyFlow()
        }

        is Transaction.SideEffect -> transaction.sideEffect.let { sideEffect ->
            logger.newSideEffect(sideEffect)
            sideEffectSharedFlow.emit(sideEffect)
            emptyFlow()
        }

        is Transaction.UiEvent -> transaction.event.let { event ->
            if (uiEventHandler != null) {
                logger.newUiEvent(event)
                uiEventHandler.handleEvent(event)
                    .catch { throwable ->
                        logger.handleEventThrowable(uiEventHandler.javaClass.simpleName, throwable)
                    }
            } else {
                logger.uiHandlerIsNull(event)
                emptyFlow()
            }
        }

        is Transaction.DomainEvent -> transaction.event.let { event ->
            if (domainEventHandler != null) {
                logger.newDomainEvent(event)
                domainEventHandler.handleEvent(event)
                    .catch { throwable ->
                        logger.handleEventThrowable(domainEventHandler.javaClass.simpleName, throwable)
                    }
            } else {
                logger.domainHandlerIsNull(event)
                emptyFlow()
            }
        }
    }

    /**
     * Sends a UI event to the state machine for processing.
     *
     * @param event The UI event to process
     **/
    suspend fun sendUiEvent(event: UiEvent) {
        transactionChannel.send(Transaction.UiEvent(event))
    }

}
