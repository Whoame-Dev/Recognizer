package ru.whoame.state_machine.handler

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import ru.whoame.state_machine.contract.Transaction

/**
 * A base class for creating handlers that listen to state machine events
 * and send them to an analytics service.
 *
 * This listener intercepts all [Transaction]s and errors, allowing for centralized
 * analytics processing. To implement specific logic, you must extend this class
 * and override one or more `handle...` functions to process and send analytics events.
 *
 * For cases where analytics events need to be sent from other handlers (e.g.,
 * [BaseUiEventHandler] or [BaseDomainEventHandler]), the recommended approach is to emit a [SideEffect].
 * This [SideEffect] can then be caught and processed by the [handleSideEffect] method in your
 * concrete implementation of this class. For instance, to track data received from a repository
 * within a [BaseDomainEventHandler], you can dispatch a specific side effect that this handler
 * will convert into an analytics event.
 *
 * @param State The type of the state machine's state.
 * @param SideEffect The type of side effects.
 * @param UiEvent The type of UI events.
 * @param DomainEvent The type of domain events.
 **/
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseAnalyticsEventHandler<State : Any, SideEffect : Any, UiEvent : Any, DomainEvent : Any> {

    private val transactionChannel: Channel<Transaction<State, SideEffect, out UiEvent, DomainEvent>> = Channel()

    private val errorChannel: Channel<Throwable> = Channel()

    /**
     * A flow designed to trigger the execution of the `handle...` methods.
     *
     * Note: The items emitted by this flow are ignored by its collector. The primary responsibility
     * of sending analytics events lies within the overridden `handle...` methods themselves.
     **/
    internal val analyticsFlow: Flow<Any>
        get() = merge(
            transactionChannel.receiveAsFlow().flatMapMerge(transform = ::handleTransaction),
            errorChannel.receiveAsFlow().flatMapMerge(transform = ::handleError),
        )

    private lateinit var stateFlow: StateFlow<State>

    /**
     * Sets the state flow for this handler.
     * This method is intended for internal use by the state machine framework.
     *
     * @param flow The [StateFlow] to be used for state management.
     **/
    internal fun setStateFlow(flow: StateFlow<State>) {
        stateFlow = flow
    }

    /**
     * The current state value.
     * Provides direct synchronous access to the current state without needing to collect from the [stateFlow].
     * Useful for getting the up-to-date state within the `handle...` functions.
     **/
    protected val state: State get() = stateFlow.value

    private fun handleTransaction(
        transaction: Transaction<State, SideEffect, out UiEvent, DomainEvent>,
    ): Flow<Any> = when (transaction) {
        is Transaction.DomainEvent<*, *, *, DomainEvent> -> handleDomainEvent(transaction.event)
        is Transaction.UiEvent<*, *, UiEvent, *> -> handleUiEvent(transaction.event)
        is Transaction.SideEffect<*, SideEffect, *, *> -> handleSideEffect(transaction.sideEffect)
        is Transaction.StateUpdate<*, *, *, *> -> emptyFlow()
    }

    /**
     * Handles a [DomainEvent], which typically represents a business logic event,
     * such as the start of a data loading process.
     *
     * You should implement the logic for sending the analytics event directly within this method.
     *
     * @param domainEvent The domain event to be processed.
     * @return An optional [Flow] that can be used to perform asynchronous work (e.g., fetching additional data)
     * before sending the event. The values emitted by this flow are ignored.
     **/
    open fun handleDomainEvent(domainEvent: DomainEvent): Flow<Any> = emptyFlow()

    /**
     * Handles a [UiEvent] triggered by a user action.
     *
     * You should implement the logic for sending the analytics event directly within this method.
     *
     * @param uiEvent The UI event to be processed.
     * @return An optional [Flow] that can be used to perform asynchronous work (e.g., fetching additional data)
     * before sending the event. The values emitted by this flow are ignored.
     **/
    open fun handleUiEvent(uiEvent: UiEvent): Flow<Any> = emptyFlow()

    /**
     * Handles a [SideEffect], which is an action that occurs outside of the state machine's state,
     * such as showing a toast message or navigating to another screen.
     *
     * You should implement the logic for sending the analytics event directly within this method.
     *
     * @param sideEffect The side effect to be processed.
     * @return An optional [Flow] that can be used to perform asynchronous work (e.g., fetching additional data)
     * before sending the event. The values emitted by this flow are ignored.
     **/
    open fun handleSideEffect(sideEffect: SideEffect): Flow<Any> = emptyFlow()

    /**
     * Handles an error that occurred during state machine processing.
     *
     * You should implement the logic for sending the analytics event directly within this method.
     *
     * @param error The [Throwable] that was caught.
     * @return An optional [Flow] that can be used to perform asynchronous work (e.g., fetching additional data)
     * before sending the event. The values emitted by this flow are ignored.
     **/
    open fun handleError(error: Throwable): Flow<Any> = emptyFlow()

    /**
     * Sends a transaction to the handler for processing.
     * Intended for internal use by the state machine framework.
     **/
    internal suspend fun sendTransaction(
        transaction: Transaction<State, SideEffect, out UiEvent, DomainEvent>,
    ) = transactionChannel.send(transaction)

    /**
     * Sends an error to the handler for processing.
     * Intended for internal use by the state machine framework.
     **/
    internal fun sendError(error: Throwable) {
        errorChannel.trySend(error)
    }

}
