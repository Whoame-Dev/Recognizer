package ru.whoame.state_machine.handler

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.whoame.state_machine.contract.Transaction
import ru.whoame.state_machine.handler.mock.TestDomainEvent
import ru.whoame.state_machine.handler.mock.TestDomainEventHandler
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BaseDomainEventHandlerTest {

    @Test
    fun updateState() = runTest {
        val handler = TestDomainEventHandler()

        val values = mutableListOf<Transaction<Int, String, *, TestDomainEvent>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            handler.transactionFlow.toList(values)
        }

        handler.handleEvent(TestDomainEvent.UpdateState).take(1).collect()

        val transaction = values[0]
        assertTrue(transaction is Transaction.StateUpdate)
        assertEquals(2, transaction.block.invoke(1))
    }

    @Test
    fun sideEffect() = runTest {
        val handler = TestDomainEventHandler()

        val values = mutableListOf<Transaction<Int, String, *, TestDomainEvent>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            handler.transactionFlow.toList(values)
        }

        handler.handleEvent(TestDomainEvent.SideEffect).take(1).collect()

        val transaction = values[0]
        assertTrue(transaction is Transaction.SideEffect)
        assertEquals("Side effect", transaction.sideEffect)
    }

    @Test
    fun sideEffectLambda() = runTest {
        val handler = TestDomainEventHandler()

        val values = mutableListOf<Transaction<Int, String, *, TestDomainEvent>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            handler.transactionFlow.toList(values)
        }

        handler.handleEvent(TestDomainEvent.SideEffectLambda).take(1).collect()

        val transaction = values[0]
        assertTrue(transaction is Transaction.SideEffect)
        assertEquals("Side effect lambda", transaction.sideEffect)
    }

    @Test
    fun updateStateWithSideEffect() = runTest {
        val handler = TestDomainEventHandler()

        val values = mutableListOf<Transaction<Int, String, *, TestDomainEvent>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            handler.transactionFlow.toList(values)
        }

        handler.handleEvent(TestDomainEvent.UpdateStateWithSideEffect).take(1).collect()

        val firstTransaction = values[0]
        assertTrue(firstTransaction is Transaction.StateUpdate)
        assertEquals(2, firstTransaction.block.invoke(1))

        val secondTransaction = values[1]
        assertTrue(secondTransaction is Transaction.SideEffect)
        assertEquals("Side effect", secondTransaction.sideEffect)
    }

    @Test
    fun twoStateUpdatesWithDelay() = runTest {
        val handler = TestDomainEventHandler()

        val values = mutableListOf<Transaction<Int, String, *, TestDomainEvent>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            handler.transactionFlow.toList(values)
        }

        handler.handleEvent(TestDomainEvent.TwoStateUpdatesWithDelay).take(1).collect()

        val firstTransaction = values[0]
        assertTrue(firstTransaction is Transaction.StateUpdate)
        assertEquals(2, firstTransaction.block.invoke(1))

        val secondTransaction = values[1]
        assertTrue(secondTransaction is Transaction.StateUpdate)
        assertEquals(0, secondTransaction.block.invoke(1))
    }

}
