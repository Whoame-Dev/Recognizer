package ru.whoame.state_machine.contract

/**
 * This factory interface is responsible for supplying the default values
 * that a state machine needs to initialize properly.
 *
 * @param State type representing the state.
 * @param DomainEvent type representing domain events.
 **/
interface BaseDefaultsFactory<State : Any, DomainEvent : Any> {

    /**
     * Returns the default initial state for the state machine.
     *
     * @return The initial [State] that the state machine should start with
     **/
    fun state(): State

    /**
     * The method returns the list of domain events that should be executed immediately
     * after the start of the state machine. For example, to request data to be displayed.
     * By default it returns an empty list.
     *
     * @return a list of domain events, empty by default
     **/
    fun domainEvents(): List<DomainEvent> = emptyList()

}
