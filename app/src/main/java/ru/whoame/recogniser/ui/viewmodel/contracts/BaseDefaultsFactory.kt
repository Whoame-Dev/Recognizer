package ru.whoame.recogniser.ui.viewmodel.contracts

/**
 * Provides default initial state and domain events for a state machine.
 *
 * @param State type representing the state.
 * @param DomainEvent type representing domain events.
 *
 * @see [state] Returns the default initial state for the state machine.
 * @see [domainEvents] Returns the default list of domain events for the state machine, empty by default.
 **/
interface BaseDefaultsFactory<State : Any, DomainEvent : Any> {

    fun state(): State

    fun domainEvents(): List<DomainEvent> = emptyList()

}
