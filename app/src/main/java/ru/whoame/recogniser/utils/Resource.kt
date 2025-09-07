package ru.whoame.recogniser.utils

import androidx.annotation.IntRange

sealed interface Resource<out T> {

    val timestamp: Long

    data class Loading<out T>(
        @get:IntRange(from = 0L) override val timestamp: Long = System.currentTimeMillis(),
    ) : Resource<T>

    data class Data<out T>(
        val value: T,
        @get:IntRange(from = 0L) override val timestamp: Long = System.currentTimeMillis(),
    ) : Resource<T>

    data class Error<out T>(
        val throwable: Throwable,
        @get:IntRange(from = 0L) override val timestamp: Long = System.currentTimeMillis(),
    ) : Resource<T>

}

fun <T> T.toResource(): Resource<T> = Resource.Data(this)

fun <T, R> Resource<T>.map(block: (T) -> R): Resource<R> = when (this) {
    is Resource.Loading -> Resource.Loading(timestamp)
    is Resource.Data -> Resource.Data(
        value = block.invoke(value),
        timestamp = timestamp,
    )

    is Resource.Error -> Resource.Loading(timestamp)
}
