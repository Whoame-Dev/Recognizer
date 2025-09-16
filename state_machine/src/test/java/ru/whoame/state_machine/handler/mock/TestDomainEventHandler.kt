package ru.whoame.state_machine.handler.mock

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.whoame.state_machine.handler.BaseDomainEventHandler

class TestDomainEventHandler : BaseDomainEventHandler<Int, String, TestDomainEvent>() {

    override fun handleEvent(event: TestDomainEvent): Flow<*> = when (event) {
        TestDomainEvent.UpdateState -> increase()
        TestDomainEvent.SideEffect -> sideEffect()
        TestDomainEvent.SideEffectLambda -> sideEffectLambda()
        TestDomainEvent.UpdateStateWithSideEffect -> increaseWithSideEffect()
        TestDomainEvent.TwoStateUpdatesWithDelay -> twoStateUpdatesWithDelay()
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

}
