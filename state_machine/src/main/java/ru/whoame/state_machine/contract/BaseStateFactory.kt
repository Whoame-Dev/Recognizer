package ru.whoame.state_machine.contract

/**
 * Base factory for converting internal state to UI state.
 **/
interface BaseStateFactory<State : Any, UiState : Any> {

    /**
     * Converts internal state to UI state.
     **/
    fun convert(state: State): UiState

}
