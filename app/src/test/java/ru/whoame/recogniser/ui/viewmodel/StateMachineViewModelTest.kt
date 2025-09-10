package ru.whoame.recogniser.ui.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.whoame.recogniser.ui.viewmodel.mock.factory.TestDefaultsFactory
import ru.whoame.recogniser.ui.viewmodel.mock.model.TestDomainEvent
import ru.whoame.recogniser.ui.viewmodel.mock.model.TestSideEffect
import ru.whoame.recogniser.ui.viewmodel.mock.model.TestUiState
import kotlin.test.assertContains
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class StateMachineViewModelTest {

    @Test
    fun defaultUiState() = runTest {
        val defaultsFactory = TestDefaultsFactory(emptyList())
        val viewModel = TestViewModel(defaultsFactory)

        var uiState: TestUiState? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            uiState = viewModel.uiStateFlow.value
        }
        assertEquals("Default", uiState?.value)
    }

    @Test
    fun defaultDomainEvent() = runTest {
        val defaultsFactory = TestDefaultsFactory(TestDomainEvent.Default)
        val viewModel = TestViewModel(defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiStateFlow.toList(states)
        }

        var value: String? = null
        repeat(10000) {
            delay(100)
            value = states.lastOrNull()?.value
        }

        assertEquals("Default_DefaultDomainEvent", value)
    }

    @Test
    fun defaultDomainEventWithSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory(TestDomainEvent.DefaultWithSideEffect)
        val viewModel = TestViewModel(defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiStateFlow.toList(states)
        }

        val sideEffects = mutableListOf<TestSideEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.sideEffectFlow.toList(sideEffects)
        }

        var value: String? = null
        repeat(10000) {
            delay(100)
            value = states.lastOrNull()?.value
        }

        assertEquals("Default_DefaultDomainEvent", value)
        assertContains(sideEffects, TestSideEffect.FromDomainHandler)
    }

    @Test
    fun twoDefaultDomainEventsWithOneSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory(TestDomainEvent.Default, TestDomainEvent.DefaultWithSideEffect)
        val viewModel = TestViewModel(defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiStateFlow.toList(states)
        }

        val sideEffects = mutableListOf<TestSideEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.sideEffectFlow.toList(sideEffects)
        }

        var value: String? = null
        var sideEffect: TestSideEffect? = null
        repeat(10000) {
            delay(100)
            value = states.lastOrNull()?.value
            sideEffect = sideEffects.lastOrNull()
        }

        assertEquals("Default_DefaultDomainEvent_DefaultDomainEvent", value)
        assertEquals(TestSideEffect.FromDomainHandler, sideEffect)
    }

    @Test
    fun uiEvent() = runTest {
        val defaultsFactory = TestDefaultsFactory(emptyList())
        val viewModel = TestViewModel(defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiStateFlow.toList(states)
        }

        viewModel.uiEvent()

        var value: String? = null
        repeat(10000) {
            delay(100)
            value = states.lastOrNull()?.value
        }

        assertEquals("Default_UiEvent", value)
    }

    @Test
    fun uiEventWithSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory(emptyList())
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

        var value: String? = null
        var sideEffect: TestSideEffect? = null
        repeat(10000) {
            delay(100)
            value = states.lastOrNull()?.value
            sideEffect = sideEffects.lastOrNull()
        }

        assertEquals("Default_UiEvent", value)
        assertEquals(TestSideEffect.FromUiHandler, sideEffect)
    }

    @Test
    fun twoUiEventsWithOneSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory(emptyList())
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

        var value: String? = null
        var sideEffect: TestSideEffect? = null
        repeat(10000) {
            delay(100)
            value = states.lastOrNull()?.value
            sideEffect = sideEffects.lastOrNull()
        }

        assertEquals("Default_UiEvent_UiEvent", value)
        assertEquals(TestSideEffect.FromUiHandler, sideEffect)
    }

    @Test
    fun uiEventWithSideEffectAndDefaultDomainEventWithSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory(TestDomainEvent.DefaultWithSideEffect)
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

        var value: String? = null
        repeat(10000) {
            delay(100)
            value = states.lastOrNull()?.value
        }

        val values = value?.split('_', ignoreCase = true).orEmpty()
        assertContains(values, "Default")
        assertContains(values, "DefaultDomainEvent")
        assertContains(values, "UiEvent")
        assertContains(sideEffects, TestSideEffect.FromUiHandler)
        assertContains(sideEffects, TestSideEffect.FromDomainHandler)
    }

    @Test
    fun uiEventWithSideEffectInvokeDomainEventWithSideEffectAndDefaultDomainEventWithSideEffect() = runTest {
        val defaultsFactory = TestDefaultsFactory(TestDomainEvent.DefaultWithSideEffect)
        val viewModel = TestViewModel(defaultsFactory)

        val states = mutableListOf<TestUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiStateFlow.toList(states)
        }

        val sideEffects = mutableListOf<TestSideEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.sideEffectFlow.toList(sideEffects)
        }

        viewModel.uiEventWithSideEffectInvokeDomainEventWithSideEffect()

        var value: String? = null
        repeat(10000) {
            delay(100)
            value = states.lastOrNull()?.value
        }

        val values = value?.split('_', ignoreCase = true).orEmpty()
        assertContains(values, "Default")
        assertContains(values, "DefaultDomainEvent")
        assertContains(values, "UiEvent")
        assertContains(values, "DomainEvent")
        assertContains(sideEffects, TestSideEffect.FromUiHandler)
        assertContains(sideEffects, TestSideEffect.FromDomainHandler)
        assertContains(sideEffects, TestSideEffect.FromUiAndDomainHandler)
    }

}
