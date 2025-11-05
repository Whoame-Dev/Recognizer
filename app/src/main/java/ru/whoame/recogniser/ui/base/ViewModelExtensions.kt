package ru.whoame.recogniser.ui.base

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Collects and processes side effects emitted by the state machine view model, respecting the active lifecycle state.
 *
 * @param block Callback for processing each side effect emitted from the flow.
 **/
@Composable
@SuppressLint("ComposableNaming")
fun <SideEffect : Any> StateMachineViewModel<*, *, SideEffect, *, *>.collectSideEffect(
    block: @Composable (sideEffect: SideEffect) -> Unit,
) {
    val sideEffect by sideEffectFlow.collectAsStateWithLifecycle(initialValue = null)

    sideEffect?.let { effect ->
        block.invoke(effect)
    }
}
