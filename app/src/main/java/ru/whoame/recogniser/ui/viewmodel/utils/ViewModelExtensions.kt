package ru.whoame.recogniser.ui.viewmodel.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import ru.whoame.recogniser.ui.viewmodel.StateMachineViewModel

/**
 * Collects and processes side effects emitted by the state machine view model, respecting the active lifecycle state.
 *
 * @param lifecycleState The lifecycle state to start collecting side effects.
 * @param sideEffect Callback for processing each side effect emitted from the flow.
 **/
@Composable
@SuppressLint("ComposableNaming")
fun <SideEffect : Any> StateMachineViewModel<*, *, SideEffect, *, *>.collectSideEffect(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit),
) {
    val sideEffectFlow = sideEffectFlow
    val lifecycleOwner = LocalLifecycleOwner.current

    val callback by rememberUpdatedState(newValue = sideEffect)

    LaunchedEffect(sideEffectFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            sideEffectFlow.collect { callback(it) }
        }
    }
}

/**
 * Collects the UI state as [State]<T>, respecting the active lifecycle state.
 *
 * @param lifecycleState The lifecycle state to start collecting the UI state flow.
 **/
@Composable
fun <UiState : Any> StateMachineViewModel<UiState, *, *, *, *>.collectAsState(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
): State<UiState> = uiStateFlow.collectAsStateWithLifecycle(minActiveState = lifecycleState)
