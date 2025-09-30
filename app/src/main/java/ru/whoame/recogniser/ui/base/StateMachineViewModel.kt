package ru.whoame.recogniser.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.whoame.recogniser.BuildConfig
import ru.whoame.state_machine.StateMachine
import ru.whoame.state_machine.contract.BaseDefaultsFactory
import ru.whoame.state_machine.contract.BaseStateFactory
import ru.whoame.state_machine.handler.BaseAnalyticsEventHandler
import ru.whoame.state_machine.handler.BaseDomainEventHandler
import ru.whoame.state_machine.handler.BaseUiEventHandler
import ru.whoame.state_machine.logger.LoggerSettings
import ru.whoame.state_machine.logger.LoggerType
import ru.whoame.state_machine.utils.TransactionSenderDslMarker
import kotlin.coroutines.CoroutineContext

/**
 * Abstract base ViewModel implementing a state machine architecture for managing UI logic in a reactive and scalable way.
 *
 * Responsibilities:
 * - Maintains internal state, exposes UI state as a [StateFlow] for reactive consumption.
 * - Handles side effects, exposing them as a [SharedFlow] (such as navigation, dialogs, messages).
 * - Processes UI events and domain events through corresponding event handlers (if provided),
 *   using transactions for all modifications/events.
 * - Supports easy composition/extension for screen or feature ViewModel implementations.
 *
 * Usage:
 * 1. Extend this [ViewModel] class for your screen.
 * 2. Provide factories and handlers for state, events, and side effects.
 * 3. Collect [uiStateFlow] and [sideEffectFlow] in your UI composable screen or fragment.
 *
 * All event/state mutations are channelled through transactions,
 * ensuring single-source-of-truth state management and serialization of side effects/events.
 *
 * @param UiState Type representing the externally exposed UI state (for consumption in UI).
 * @param State Internal feature state type (encapsulating feature data and transient properties).
 * @param SideEffect Type for transient side effects (e.g., navigation intents, toasts).
 * @param UiEvent Event type for UI-driven actions (such as button clicks, gestures).
 * @param DomainEvent Event type for domain-driven actions or external events.
 *
 * @param defaultsFactory Factory producing initial State and default DomainEvents.
 * @param stateFactory Factory converting internal State to externally-consumed UiState.
 * @param uiEventHandler Handler for incoming UI events (may be null if not needed).
 * @param domainEventHandler Handler for incoming Domain events (may be null if not needed).
 * @param analyticsEventHandler Optional handler for processing all events, side effects and errors for analytics purposes
 * @param loggerType Type of logger to use for state machine logging
 *  (default is [LoggerType.NONE] for release builds and [LoggerType.ERROR] for debug builds).
 **/
abstract class StateMachineViewModel<UiState : Any, State : Any, SideEffect : Any, UiEvent : Any, DomainEvent : Any>(
    defaultsFactory: BaseDefaultsFactory<State, DomainEvent>,
    stateFactory: BaseStateFactory<State, UiState>,
    uiEventHandler: BaseUiEventHandler<State, SideEffect, UiEvent, DomainEvent>? = null,
    domainEventHandler: BaseDomainEventHandler<State, SideEffect, DomainEvent>? = null,
    analyticsEventHandler: BaseAnalyticsEventHandler<State, SideEffect, UiEvent, DomainEvent>? = null,
    loggerType: LoggerType = if (BuildConfig.DEBUG) LoggerType.ERROR else LoggerType.NONE,
) : ViewModel() {

    private val stateMachine = StateMachine(
        loggerSettings = LoggerSettings(this.javaClass.simpleName, loggerType),
        scope = viewModelScope,
        defaultsFactory = defaultsFactory,
        stateFactory = stateFactory,
        uiEventHandler = uiEventHandler,
        domainEventHandler = domainEventHandler,
        analyticsEventHandler = analyticsEventHandler,
        onCreated = { onCreated() },
    )

    /**
     * Exposes the current UI state as a [StateFlow] for reactive consumption by the UI layer.
     **/
    val uiStateFlow: StateFlow<UiState> get() = stateMachine.uiStateFlow
    /**
     * Exposes side effects as a [SharedFlow] for one-time consumption by the UI layer.
     * Side effects include navigation events, dialogs, toasts, etc.
     **/
    val sideEffectFlow: SharedFlow<SideEffect> get() = stateMachine.sideEffectFlow

    /**
     * Called after the state machine is started and initialized.
     * Override this method to perform any additional initialization logic.
     **/
    protected open fun onCreated() {}

    /**
     * Launches a UI event by sending it to the state machine.
     *
     * @param event UI event to send.
     * @param context Coroutine context for event processing (defaults to [Dispatchers.Default]).
     **/
    @TransactionSenderDslMarker
    protected fun launchEvent(
        event: UiEvent,
        context: CoroutineContext = Dispatchers.Default,
    ) {
        viewModelScope.launch(context) {
            stateMachine.sendUiEvent(event)
        }
    }

    /**
     * Launches a UI event built using a lambda function.
     *
     * @param context Coroutine context for event processing (defaults to [Dispatchers.Default]).
     * @param block Lambda function that generates the UI event to send.
     **/
    @TransactionSenderDslMarker
    protected fun launchEvent(
        context: CoroutineContext = Dispatchers.Default,
        block: () -> UiEvent,
    ) {
        viewModelScope.launch(context) {
            stateMachine.sendUiEvent(block.invoke())
        }
    }

}
