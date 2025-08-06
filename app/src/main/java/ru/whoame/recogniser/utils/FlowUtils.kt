package ru.whoame.recogniser.utils

import kotlinx.coroutines.flow.*

fun <T> flowOf(block: suspend () -> T): Flow<T> = flow { emit(block.invoke()) }

fun <T> Flow<T>.asResource(): Flow<Resource<T>> = map { value -> value.toResource() }
    .onStart { emit(Resource.Loading()) }

fun <T, R> Flow<Resource<T>>.mapResource(block: (T) -> R): Flow<Resource<R>> = map { resource ->
    resource.map { value -> block.invoke(value) }
}
