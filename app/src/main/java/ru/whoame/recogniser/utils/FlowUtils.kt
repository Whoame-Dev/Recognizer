package ru.whoame.recogniser.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

inline fun <T> flowOf(crossinline block: suspend () -> T): Flow<T> = flow { emit(block.invoke()) }

fun <T> Flow<T>.asResource(): Flow<Resource<T>> = map { value -> value.toResource() }
    .onStart { emit(Resource.Loading()) }
    .catch { throwable -> emit(Resource.Error(throwable)) }

inline fun <T, R> Flow<Resource<T>>.mapResource(crossinline block: (T) -> R): Flow<Resource<R>> = map { resource ->
    resource.map { value -> block.invoke(value) }
}

inline fun <T, R> Flow<Resource<T>>.mapResourceWithDelay(
    delay: Long,
    crossinline block: (T) -> R,
): Flow<Resource<R>> = map { resource ->
    delay(delay)
    resource.map { value -> block.invoke(value) }
}
