package ru.whoame.state_machine.mock.handler

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.whoame.state_machine.handler.BaseUiEventHandler
import ru.whoame.state_machine.mock.model.*

class TestUiEventHandler : BaseUiEventHandler<TestState, TestSideEffect, TestUiEvent, TestDomainEvent>() {

    override fun handleEvent(event: TestUiEvent) = when (event) {
        TestUiEvent.Event -> event()
        TestUiEvent.EventWithSideEffect -> eventWithSideEffect()
        TestUiEvent.EventWithSideEffectInvokeDomainEventWithSideEffect ->
            eventWithSideEffectInvokeDomainEventWithSideEffect()
    }

    private fun event() = flow { emit(Unit) }
        .map {
            reduceState { state ->
                state.copy(state.value + "_UiEvent")
            }
        }

    private fun eventWithSideEffect() = event()
        .map {
            reduceSideEffect(TestSideEffect.FromUiHandler)
        }

    private fun eventWithSideEffectInvokeDomainEventWithSideEffect() = eventWithSideEffect()
        .map {
            reduceEvent {
                TestDomainEvent.FromUiHandlerWithSideEffect
            }
        }

}
