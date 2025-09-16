package ru.whoame.recogniser.ui.base.mock

import ru.whoame.recogniser.ui.base.StateMachineViewModel
import ru.whoame.recogniser.ui.base.mock.factory.TestDefaultsFactory
import ru.whoame.recogniser.ui.base.mock.factory.TestStateFactory
import ru.whoame.recogniser.ui.base.mock.handler.TestDomainEventHandler
import ru.whoame.recogniser.ui.base.mock.handler.TestUiEventHandler
import ru.whoame.recogniser.ui.base.mock.model.*
import ru.whoame.state_machine.logger.LoggerType

class TestViewModel(
    defaultsFactory: TestDefaultsFactory,
) : StateMachineViewModel<TestUiState, TestState, TestSideEffect, TestUiEvent, TestDomainEvent>(
    defaultsFactory = defaultsFactory,
    stateFactory = TestStateFactory(),
    uiEventHandler = TestUiEventHandler(),
    domainEventHandler = TestDomainEventHandler(),
    loggerType = LoggerType.FULL,
) {

    fun uiEvent() = launchEvent {
        TestUiEvent.Event
    }

    fun uiEventWithSideEffect() = launchEvent(TestUiEvent.EventWithSideEffect)

    fun uiEventWithSideEffectInvokeDomainEventWithSideEffect() = launchEvent {
        TestUiEvent.EventWithSideEffectInvokeDomainEventWithSideEffect
    }

}
