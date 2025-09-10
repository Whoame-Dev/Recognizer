package ru.whoame.recogniser.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/**
 * Creates a [Flow] that emits the result of the provided suspend block.
 **/
inline fun <T> flowOf(crossinline block: suspend () -> T): Flow<T> = flow { emit(block.invoke()) }

/**
 * Maps each item emitted by the Flow to a [Resource].
 * Emits a [Resource.Loading] at the start and [Resource.Error] on exception.
 **/
fun <T> Flow<T>.asResource(): Flow<Resource<T>> = map { value -> value.toResource() }
    .onStart { emit(Resource.Loading()) }
    .catch { throwable -> emit(Resource.Error(throwable)) }

/**
 * Transforms the value inside [Resource] using the provided block.
 **/
inline fun <T, R> Flow<Resource<T>>.mapResource(crossinline block: (T) -> R): Flow<Resource<R>> = map { resource ->
    resource.map { value -> block.invoke(value) }
}

/**
 * Transforms the value inside [Resource] using the provided block and adds a delay before mapping.
 * Useful when developing or testing
 *
 * @param delay The delay in milliseconds.
 **/
inline fun <T, R> Flow<Resource<T>>.mapResourceWithDelay(
    delay: Long,
    crossinline block: (T) -> R,
): Flow<Resource<R>> = map { resource ->
    delay(delay)
    resource.map { value -> block.invoke(value) }
}
