package ru.whoame.recogniser.ui.viewmodel.handler

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.whoame.recogniser.ui.viewmodel.contracts.Transaction
import ru.whoame.recogniser.ui.viewmodel.handler.mock.TestUiEvent
import ru.whoame.recogniser.ui.viewmodel.handler.mock.TestUiEventHandler
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BaseUiEventHandlerTest {

    @Test
    fun updateState() = runTest {
        val handler = TestUiEventHandler()

        val values = mutableListOf<Transaction<Int, String, TestUiEvent, String>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            handler.transactionFlow.toList(values)
        }

        handler.handleEvent(TestUiEvent.UpdateState).take(1).collect()

        val transaction = values[0]
        assertTrue(transaction is Transaction.UpdateState)
        assertEquals(2, transaction.block.invoke(1))
    }

    @Test
    fun sideEffect() = runTest {
        val handler = TestUiEventHandler()

        val values = mutableListOf<Transaction<Int, String, TestUiEvent, String>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            handler.transactionFlow.toList(values)
        }

        handler.handleEvent(TestUiEvent.SideEffect).take(1).collect()

        val transaction = values[0]
        assertTrue(transaction is Transaction.SendSideEffect)
        assertEquals("Side effect", transaction.sideEffect)
    }

    @Test
    fun sideEffectLambda() = runTest {
        val handler = TestUiEventHandler()

        val values = mutableListOf<Transaction<Int, String, TestUiEvent, String>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            handler.transactionFlow.toList(values)
        }

        handler.handleEvent(TestUiEvent.SideEffectLambda).take(1).collect()

        val transaction = values[0]
        assertTrue(transaction is Transaction.SendSideEffect)
        assertEquals("Side effect lambda", transaction.sideEffect)
    }

    @Test
    fun updateStateWithSideEffect() = runTest {
        val handler = TestUiEventHandler()

        val values = mutableListOf<Transaction<Int, String, TestUiEvent, String>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            handler.transactionFlow.toList(values)
        }

        handler.handleEvent(TestUiEvent.UpdateStateWithSideEffect).take(1).collect()

        val firstTransaction = values[0]
        assertTrue(firstTransaction is Transaction.UpdateState)
        assertEquals(2, firstTransaction.block.invoke(1))

        val secondTransaction = values[1]
        assertTrue(secondTransaction is Transaction.SendSideEffect)
        assertEquals("Side effect", secondTransaction.sideEffect)
    }

    @Test
    fun twoStateUpdatesWithDelay() = runTest {
        val handler = TestUiEventHandler()

        val values = mutableListOf<Transaction<Int, String, TestUiEvent, String>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            handler.transactionFlow.toList(values)
        }

        handler.handleEvent(TestUiEvent.TwoStateUpdatesWithDelay).take(1).collect()

        val firstTransaction = values[0]
        assertTrue(firstTransaction is Transaction.UpdateState)
        assertEquals(2, firstTransaction.block.invoke(1))

        val secondTransaction = values[1]
        assertTrue(secondTransaction is Transaction.UpdateState)
        assertEquals(0, secondTransaction.block.invoke(1))
    }

    @Test
    fun sendDomainEvent() = runTest {
        val handler = TestUiEventHandler()

        val values = mutableListOf<Transaction<Int, String, TestUiEvent, String>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            handler.transactionFlow.toList(values)
        }

        handler.handleEvent(TestUiEvent.DomainEvent).take(1).collect()

        val transaction = values[0]
        assertTrue(transaction is Transaction.SendDomainEvent)
        assertEquals("Domain event", transaction.event)
    }

    @Test
    fun sendDomainEventLambda() = runTest {
        val handler = TestUiEventHandler()

        val values = mutableListOf<Transaction<Int, String, TestUiEvent, String>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            handler.transactionFlow.toList(values)
        }

        handler.handleEvent(TestUiEvent.DomainEventLambda).take(1).collect()

        val transaction = values[0]
        assertTrue(transaction is Transaction.SendDomainEvent)
        assertEquals("Domain event lambda", transaction.event)
    }

}
