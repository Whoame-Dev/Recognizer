package ru.whoame.recogniser.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.whoame.recogniser.ui.viewmodel.contracts.BaseDefaultsFactory
import ru.whoame.recogniser.ui.viewmodel.contracts.BaseStateFactory
import ru.whoame.recogniser.ui.viewmodel.contracts.Transaction
import ru.whoame.recogniser.ui.viewmodel.handler.BaseDomainEventHandler
import ru.whoame.recogniser.ui.viewmodel.handler.BaseUiEventHandler
import ru.whoame.recogniser.ui.viewmodel.utils.TransactionSenderDslMarker
import ru.whoame.recogniser.utils.flowOf
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalCoroutinesApi::class)
/**
 * Abstract base ViewModel implementing a state machine architecture for managing UI logic in a reactive and scalable way.
 *
 * Responsibilities:
 * - Maintains internal state, exposes UI state as a [StateFlow] for reactive consumption.
 * - Handles side effects, exposing them as a [SharedFlow] (such as navigation, dialogs, messages).
 * - Processes UI events and domain events through corresponding event handlers (if provided), using transactions for all modifications/events.
 * - Supports easy composition/extension for screen or feature ViewModel implementations.
 *
 * Generic Parameters:
 * @param UiState Type representing the externally exposed UI state (for consumption in UI).
 * @param State Internal feature state type (encapsulating feature data and transient properties).
 * @param SideEffect Type for transient side effects (e.g., navigation intents, toasts).
 * @param UiEvent Event type for UI-driven actions (such as button clicks, gestures).
 * @param DomainEvent Event type for domain-driven actions or external events.
 *
 * Constructor Params:
 * @param defaultsFactory Factory producing initial State and default DomainEvents.
 * @param stateFactory Factory converting internal State to externally-consumed UiState.
 * @param uiEventHandler Handler for incoming UI events (may be null if not needed).
 * @param domainEventHandler Handler for incoming Domain events (may be null if not needed).
 * @param context Coroutine context for event processing (defaults to [Dispatchers.Default]).
 *
 * Usage:
 * 1. Extend this ViewModel class for your screen.
 * 2. Provide factories and handlers for state, events, and side effects.
 * 3. Collect [uiStateFlow] and [sideEffectFlow] in your UI composable screen or fragment.
 *
 * All event/state mutations are channelled through transactions, ensuring single-source-of-truth state management and serialization of side effects/events.
 **/
abstract class StateMachineViewModel<UiState : Any, State : Any, SideEffect : Any, UiEvent : Any, DomainEvent : Any>(
    defaultsFactory: BaseDefaultsFactory<State, DomainEvent>,
    stateFactory: BaseStateFactory<State, UiState>,
    uiEventHandler: BaseUiEventHandler<State, SideEffect, UiEvent, DomainEvent>? = null,
    domainEventHandler: BaseDomainEventHandler<State, SideEffect, DomainEvent>? = null,
    context: CoroutineContext = Dispatchers.Default,
) : ViewModel() {

    private val mutableStateFlow: MutableStateFlow<State>

    private val uiMutableStateFlow: MutableStateFlow<UiState>
    /**
     * Publicly exposed UI state as a hot StateFlow.
     * Collects in UI to observe state changes.
     **/
    val uiStateFlow: StateFlow<UiState> get() = uiMutableStateFlow.asStateFlow()

    private val sideEffectSharedFlow: MutableSharedFlow<SideEffect> = MutableSharedFlow()
    /**
     * Publicly exposed side effect flow as a hot SharedFlow.
     * Collects in UI to observe side effects.
     **/
    val sideEffectFlow: SharedFlow<SideEffect> get() = sideEffectSharedFlow.asSharedFlow()

    private val transactionChannel: Channel<Transaction<State, SideEffect, UiEvent, DomainEvent>> = Channel()

    init {
        val initialState = defaultsFactory.state()
        mutableStateFlow = MutableStateFlow(initialState)
        uiMutableStateFlow = MutableStateFlow(stateFactory.convert(initialState))

        viewModelScope.launch(context) {
            mutableStateFlow.collect { state ->
                uiMutableStateFlow.emit(stateFactory.convert(state))
            }
        }

        val defaultDomainEventTransactionsFlow = defaultsFactory.domainEvents()
            .asFlow()
            .map { event ->
                Transaction.SendDomainEvent<State, SideEffect, UiEvent, DomainEvent>(event)
            }

        val transactionsFlow = merge(
            defaultDomainEventTransactionsFlow,
            transactionChannel.receiveAsFlow(),
            uiEventHandler?.transactionFlow ?: emptyFlow(),
            domainEventHandler?.transactionFlow ?: emptyFlow(),
        )
            .buffer()
            .flatMapConcat { transaction ->
                when (transaction) {
                    is Transaction.UpdateState -> flowOf { mutableStateFlow.update(transaction.block) }
                    is Transaction.SendSideEffect -> flowOf { sideEffectSharedFlow.emit(transaction.sideEffect) }
                    is Transaction.SendUiEvent ->
                        uiEventHandler?.handleEvent(transaction.event) ?: emptyFlow()

                    is Transaction.SendDomainEvent ->
                        domainEventHandler?.handleEvent(transaction.event) ?: emptyFlow()
                }
            }

        viewModelScope.launch(context) {
            transactionsFlow.collect()
        }
    }

    /**
     * Launches a UI event by sending it through the transaction channel.
     *
     * @param event UI event to send.
     * @param context Coroutine context
     **/
    @TransactionSenderDslMarker
    protected fun launchEvent(
        event: UiEvent,
        context: CoroutineContext = Dispatchers.Default,
    ) {
        viewModelScope.launch(context) {
            transactionChannel.send(Transaction.SendUiEvent(event))
        }
    }

    /**
     * Launches a UI event built using a lambda.
     *
     * @param context Coroutine context
     * @param block Lambda to generate the UI event
     **/
    @TransactionSenderDslMarker
    protected fun launchEvent(
        context: CoroutineContext = Dispatchers.Default,
        block: () -> UiEvent,
    ) {
        viewModelScope.launch(context) {
            transactionChannel.send(Transaction.SendUiEvent(block.invoke()))
        }
    }

}
