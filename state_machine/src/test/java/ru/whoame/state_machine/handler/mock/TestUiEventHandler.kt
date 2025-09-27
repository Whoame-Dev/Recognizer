package ru.whoame.state_machine.handler.mock

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.whoame.state_machine.handler.BaseUiEventHandler

class TestUiEventHandler : BaseUiEventHandler<Int, String, TestUiEvent, String>() {

    override fun handleEvent(event: TestUiEvent): Flow<*> = when (event) {
        TestUiEvent.UpdateState -> increase()
        TestUiEvent.SideEffect -> sideEffect()
        TestUiEvent.SideEffectLambda -> sideEffectLambda()
        TestUiEvent.UpdateStateWithSideEffect -> increaseWithSideEffect()
        TestUiEvent.TwoStateUpdatesWithDelay -> twoStateUpdatesWithDelay()
        TestUiEvent.DomainEvent -> domainEvent()
        TestUiEvent.DomainEventLambda -> domainEventLambda()
    }

    private fun increase() = flow { emit(Unit) }
        .map {
            reduceState { state ->
                state + 1
            }
        }

    private fun sideEffect() = flow { emit(Unit) }
        .map {
            reduceSideEffect("Side effect")
        }

    private fun sideEffectLambda() = flow { emit(Unit) }
        .map {
            reduceSideEffect { "Side effect lambda" }
        }

    private fun increaseWithSideEffect() = flow { emit(Unit) }
        .map {
            reduceState { state ->
                state + 1
            }
            reduceSideEffect("Side effect")
        }

    private fun twoStateUpdatesWithDelay() = flow { emit(Unit) }
        .map {
            reduceState { state ->
                state + 1
            }
            delay(1000)
            reduceState { state ->
                state - 1
            }
        }

    private fun domainEvent() = flow { emit(Unit) }
        .map {
            reduceEvent("Domain event")
        }

    private fun domainEventLambda() = flow { emit(Unit) }
        .map {
            reduceEvent {
                "Domain event lambda"
            }
        }

}
