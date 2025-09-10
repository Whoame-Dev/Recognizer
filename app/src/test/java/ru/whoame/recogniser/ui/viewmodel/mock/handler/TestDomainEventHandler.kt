package ru.whoame.recogniser.ui.viewmodel.mock.handler

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.whoame.recogniser.ui.viewmodel.handler.BaseDomainEventHandler
import ru.whoame.recogniser.ui.viewmodel.mock.model.TestDomainEvent
import ru.whoame.recogniser.ui.viewmodel.mock.model.TestSideEffect
import ru.whoame.recogniser.ui.viewmodel.mock.model.TestState

class TestDomainEventHandler : BaseDomainEventHandler<TestState, TestSideEffect, TestDomainEvent>() {

    override fun handleEvent(event: TestDomainEvent) = when (event) {
        TestDomainEvent.Default -> default()
        TestDomainEvent.DefaultWithSideEffect -> defaultWithSideEffect()
        TestDomainEvent.FromUiHandlerWithSideEffect -> eventWithSideEffect()
    }

    private fun default() = flow { emit(Unit) }
        .map {
            reduceState { state ->
                state.copy(state.value + "_DefaultDomainEvent")
            }
        }

    private fun defaultWithSideEffect() = default()
        .map {
            reduceSideEffect {
                TestSideEffect.FromDomainHandler
            }
        }

    private fun eventWithSideEffect() = flow { emit(Unit) }
        .map {
            reduceState { state ->
                state.copy(state.value + "_DomainEvent")
            }
            reduceSideEffect {
                TestSideEffect.FromUiAndDomainHandler
            }
        }

}
