package ru.whoame.state_machine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.whoame.state_machine.contract.BaseDefaultsFactory
import ru.whoame.state_machine.logger.LoggerSettings
import ru.whoame.state_machine.logger.LoggerType
import ru.whoame.state_machine.mock.factory.TestDefaultsFactory
import ru.whoame.state_machine.mock.factory.TestStateFactory
import ru.whoame.state_machine.mock.handler.TestDomainEventHandler
import ru.whoame.state_machine.mock.handler.TestUiEventHandler
import ru.whoame.state_machine.mock.model.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class StateMachineTest {

    private fun getTestStateMachine(
        scope: CoroutineScope,
        defaultsFactory: BaseDefaultsFactory<TestState, TestDomainEvent>,
    ) = StateMachine(
        loggerSettings = LoggerSettings(this.javaClass.simpleName, LoggerType.FULL),
        scope = scope,
        defaultsFactory = defaultsFactory,
        stateFactory = TestStateFactory(),
        uiEventHandler = TestUiEventHandler(),
        domainEventHandler = TestDomainEventHandler(),
    )

    @Test
    fun onCreateCalled() = runTest {
        var onCreatedCalled = false

        StateMachine<TestUiState, TestState, TestSideEffect, TestUiEvent, TestDomainEvent>(
            loggerSettings = LoggerSettings(this.javaClass.simpleName, LoggerType.FULL),
            scope = backgroundScope,
            defaultsFactory = TestDefaultsFactory(),
            stateFactory = TestStateFactory(),
            onCreated = {
                onCreatedCalled = true
            },
        )

        assertTrue(onCreatedCalled)
    }

    @Test
    fun defaultUiState() = runTest {
        val defaultsFactory = TestDefaultsFactory()
        val stateMachine = getTestStateMachine(backgroundScope, defaultsFactory)

        var uiState: TestUiState? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            uiState = stateMachine.uiStateFlow.value
        }

        assertEquals("Default", uiState?.value)
    }

    @Test
    fun defaultDomainEvent() = runTest {
        val defaultsFactory = TestDefaultsFactory(TestDomainEvent.Default)
        val stateMachine = getTestStateMachine(backgroundScope, defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.uiStateFlow.toList(states)
        }

        while (states.lastOrNull()?.value != "Default_DefaultDomainEvent") {
            delay(100)
        }
    }

    @Test
    fun defaultDomainEventWithSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory(TestDomainEvent.DefaultWithSideEffect)
        val stateMachine = getTestStateMachine(backgroundScope, defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.uiStateFlow.toList(states)
        }

        val sideEffects = mutableListOf<TestSideEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.sideEffectFlow.toList(sideEffects)
        }

        while (sideEffects.isEmpty() || states.lastOrNull()?.value != "Default_DefaultDomainEvent") {
            delay(100)
        }

        assertContains(sideEffects, TestSideEffect.FromDomainHandler)
    }

    @Test
    fun twoDefaultDomainEventsWithOneSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory(TestDomainEvent.Default, TestDomainEvent.DefaultWithSideEffect)
        val stateMachine = getTestStateMachine(backgroundScope, defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.uiStateFlow.toList(states)
        }

        val sideEffects = mutableListOf<TestSideEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.sideEffectFlow.toList(sideEffects)
        }

        while (sideEffects.isEmpty() || states.lastOrNull()?.value != "Default_DefaultDomainEvent_DefaultDomainEvent") {
            delay(100)
        }

        assertEquals(TestSideEffect.FromDomainHandler, sideEffects.lastOrNull())
    }

    @Test
    fun uiEvent() = runTest {
        val defaultsFactory = TestDefaultsFactory()
        val stateMachine = getTestStateMachine(backgroundScope, defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.uiStateFlow.toList(states)
        }

        stateMachine.sendUiEvent(TestUiEvent.Event)

        while (states.lastOrNull()?.value != "Default_UiEvent") {
            delay(100)
        }
    }

    @Test
    fun uiEventWithSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory()
        val stateMachine = getTestStateMachine(backgroundScope, defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.uiStateFlow.toList(states)
        }

        val sideEffects = mutableListOf<TestSideEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.sideEffectFlow.toList(sideEffects)
        }

        stateMachine.sendUiEvent(TestUiEvent.EventWithSideEffect)

        while (sideEffects.isEmpty() || states.lastOrNull()?.value != "Default_UiEvent") {
            delay(100)
        }

        assertEquals(TestSideEffect.FromUiHandler, sideEffects.lastOrNull())
    }

    @Test
    fun twoUiEventsWithOneSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory()
        val stateMachine = getTestStateMachine(backgroundScope, defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.uiStateFlow.toList(states)
        }

        val sideEffects = mutableListOf<TestSideEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.sideEffectFlow.toList(sideEffects)
        }

        stateMachine.sendUiEvent(TestUiEvent.Event)
        stateMachine.sendUiEvent(TestUiEvent.EventWithSideEffect)

        while (sideEffects.isEmpty() || states.lastOrNull()?.value != "Default_UiEvent_UiEvent") {
            delay(100)
        }

        assertEquals(TestSideEffect.FromUiHandler, sideEffects.lastOrNull())
    }

    @Test
    fun uiEventWithSideEffectAndDefaultDomainEventWithSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory(TestDomainEvent.DefaultWithSideEffect)
        val stateMachine = getTestStateMachine(backgroundScope, defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.uiStateFlow.toList(states)
        }

        val sideEffects = mutableListOf<TestSideEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.sideEffectFlow.toList(sideEffects)
        }

        stateMachine.sendUiEvent(TestUiEvent.EventWithSideEffect)

        val expectedValues = listOf("Default", "DefaultDomainEvent", "UiEvent")
        while (true) {
            val values = states.lastOrNull()?.value?.split('_', ignoreCase = true).orEmpty()

            if (sideEffects.size == 2 && values.containsAll(expectedValues)) break else delay(100)
        }

        assertContains(sideEffects, TestSideEffect.FromUiHandler)
        assertContains(sideEffects, TestSideEffect.FromDomainHandler)
    }

    @Test
    fun uiEventWithSideEffectInvokeDomainEventWithSideEffectAndDefaultDomainEventWithSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory(TestDomainEvent.DefaultWithSideEffect)
        val stateMachine = getTestStateMachine(backgroundScope, defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.uiStateFlow.toList(states)
        }

        val sideEffects = mutableListOf<TestSideEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateMachine.sideEffectFlow.toList(sideEffects)
        }

        stateMachine.sendUiEvent(TestUiEvent.EventWithSideEffectInvokeDomainEventWithSideEffect)

        val expectedValues = listOf("Default", "DefaultDomainEvent", "UiEvent", "DomainEvent")
        while (true) {
            val values = states.lastOrNull()?.value?.split('_', ignoreCase = true).orEmpty()

            if (sideEffects.size == 3 && values.containsAll(expectedValues)) break else delay(100)
        }

        assertContains(sideEffects, TestSideEffect.FromUiHandler)
        assertContains(sideEffects, TestSideEffect.FromDomainHandler)
        assertContains(sideEffects, TestSideEffect.FromUiAndDomainHandler)
    }

}
