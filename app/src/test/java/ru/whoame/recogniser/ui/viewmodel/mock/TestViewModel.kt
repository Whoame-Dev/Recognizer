package ru.whoame.recogniser.ui.viewmodel

import ru.whoame.recogniser.ui.viewmodel.mock.factory.TestDefaultsFactory
import ru.whoame.recogniser.ui.viewmodel.mock.factory.TestStateFactory
import ru.whoame.recogniser.ui.viewmodel.mock.handler.TestDomainEventHandler
import ru.whoame.recogniser.ui.viewmodel.mock.handler.TestUiEventHandler
import ru.whoame.recogniser.ui.viewmodel.mock.model.*

class TestViewModel(
    defaultsFactory: TestDefaultsFactory,
) : StateMachineViewModel<TestUiState, TestState, TestSideEffect, TestUiEvent, TestDomainEvent>(
    defaultsFactory = defaultsFactory,
    stateFactory = TestStateFactory(),
    uiEventHandler = TestUiEventHandler(),
    domainEventHandler = TestDomainEventHandler(),
) {

    fun uiEvent() = launchEvent {
        TestUiEvent.Event
    }

    fun uiEventWithSideEffect() = launchEvent(TestUiEvent.EventWithSideEffect)

    fun uiEventWithSideEffectInvokeDomainEventWithSideEffect() = launchEvent {
        TestUiEvent.EventWithSideEffectInvokeDomainEventWithSideEffect
    }

}
