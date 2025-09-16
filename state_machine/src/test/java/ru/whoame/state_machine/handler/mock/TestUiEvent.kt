package ru.whoame.state_machine.handler.mock

enum class TestUiEvent {

    UpdateState,
    SideEffect,
    SideEffectLambda,
    UpdateStateWithSideEffect,
    TwoStateUpdatesWithDelay,
    DomainEvent,
    DomainEventLambda,

}
