package ru.whoame.recogniser.ui.viewmodel.contracts

/**
 * Represents a transaction to be processed by the state machine, including state updates, side effects, and events.
 **/
sealed interface Transaction<State : Any, SideEffect : Any, UiEvent : Any, DomainEvent : Any> {

    /**
     * Transaction for updating the current state using the provided block.
     **/
    data class UpdateState<State : Any, SideEffect : Any, UiEvent : Any, DomainEvent : Any>(
        val block: (State) -> State,
    ) : Transaction<State, SideEffect, UiEvent, DomainEvent>

    /**
     * Transaction for sending a side effect.
     **/
    data class SendSideEffect<State : Any, SideEffect : Any, UiEvent : Any, DomainEvent : Any>(
        val sideEffect: SideEffect,
    ) : Transaction<State, SideEffect, UiEvent, DomainEvent>

    /**
     * Transaction for sending a UI event.
     **/
    data class SendUiEvent<State : Any, SideEffect : Any, UiEvent : Any, DomainEvent : Any>(
        val event: UiEvent,
    ) : Transaction<State, SideEffect, UiEvent, DomainEvent>

    /**
     * Transaction for sending a domain event.
     **/
    data class SendDomainEvent<State : Any, SideEffect : Any, UiEvent : Any, DomainEvent : Any>(
        val event: DomainEvent,
    ) : Transaction<State, SideEffect, UiEvent, DomainEvent>

}
