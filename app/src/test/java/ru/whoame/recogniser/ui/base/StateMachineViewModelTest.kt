package ru.whoame.recogniser.ui.base

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import ru.whoame.recogniser.ui.base.mock.TestViewModel
import ru.whoame.recogniser.ui.base.mock.factory.TestDefaultsFactory
import ru.whoame.recogniser.ui.base.mock.model.TestDomainEvent
import ru.whoame.recogniser.ui.base.mock.model.TestSideEffect
import ru.whoame.recogniser.ui.base.mock.model.TestUiState
import kotlin.test.assertContains
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class StateMachineViewModelTest {

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun defaultState() = runTest {
        val defaultsFactory = TestDefaultsFactory()
        val viewModel = TestViewModel(defaultsFactory)

        assertEquals("Default", viewModel.uiStateFlow.value.value)
    }

    @Test
    fun defaultDomainEvent() = runTest {
        val defaultsFactory = TestDefaultsFactory(TestDomainEvent.Default)
        val viewModel = TestViewModel(defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiStateFlow.toList(states)
        }

        while (states.lastOrNull()?.value != "Default_DefaultDomainEvent") {
            delay(100)
        }
    }

    @Test
    fun defaultDomainEventWithSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory(TestDomainEvent.DefaultWithSideEffect)
        val viewModel = TestViewModel(defaultsFactory)

        val states = mutableListOf<TestUiState>()
        val sideEffects = mutableListOf<TestSideEffect>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiStateFlow.toList(states)
        }
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.sideEffectFlow.toList(sideEffects)
        }

        while (sideEffects.isEmpty() || states.lastOrNull()?.value != "Default_DefaultDomainEvent") {
            delay(100)
        }

        assertContains(sideEffects, TestSideEffect.FromDomainHandler)
    }

    @Test
    fun uiEvent() = runTest {
        val defaultsFactory = TestDefaultsFactory()
        val viewModel = TestViewModel(defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiStateFlow.toList(states)
        }

        viewModel.uiEvent()

        while (states.lastOrNull()?.value != "Default_UiEvent") {
            delay(100)
        }
    }

    @Test
    fun uiEventWithSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory()
        val viewModel = TestViewModel(defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiStateFlow.toList(states)
        }

        val sideEffects = mutableListOf<TestSideEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.sideEffectFlow.toList(sideEffects)
        }

        viewModel.uiEventWithSideEffect()

        while (sideEffects.isEmpty() || states.lastOrNull()?.value != "Default_UiEvent") {
            delay(100)
        }

        assertEquals(TestSideEffect.FromUiHandler, sideEffects.lastOrNull())
    }

    @Test
    fun twoUiEventsWithOneSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory()
        val viewModel = TestViewModel(defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiStateFlow.toList(states)
        }

        val sideEffects = mutableListOf<TestSideEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.sideEffectFlow.toList(sideEffects)
        }

        viewModel.uiEvent()
        viewModel.uiEventWithSideEffect()

        while (sideEffects.isEmpty() || states.lastOrNull()?.value != "Default_UiEvent_UiEvent") {
            delay(100)
        }

        assertEquals(TestSideEffect.FromUiHandler, sideEffects.lastOrNull())
    }

    @Test
    fun stateIsSingleSourceOfTruth() = runTest {
        // Given
        val defaultsFactory = TestDefaultsFactory()
        val viewModel = TestViewModel(defaultsFactory)

        // When - Multiple collectors should see the same state
        val state1 = viewModel.uiStateFlow.value
        val state2 = viewModel.uiStateFlow.value

        // Then
        assertEquals(state1.value, state2.value)
        assertEquals("Default", state1.value)
    }

}
