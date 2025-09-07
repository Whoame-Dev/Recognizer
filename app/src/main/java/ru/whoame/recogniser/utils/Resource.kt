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

inline fun <T, R> Resource<T>.map(crossinline block: (T) -> R): Resource<R> = when (this) {
    is Resource.Loading -> Resource.Loading(timestamp)
    is Resource.Data -> Resource.Data(
        value = block.invoke(value),
        timestamp = timestamp,
    )

    is Resource.Error -> Resource.Loading(timestamp)
}

inline fun <T> Resource<List<T>>.filter(crossinline predicate: (T) -> Boolean): Resource<List<T>> = map { values ->
    values.filter(predicate)
}

fun <T> Resource<T>.valueOrDefault(defaultValue: T): T = (this as? Resource.Data)?.value ?: defaultValue

fun <T> Resource<T>.valueOrNull(defaultValue: T? = null): T? = (this as? Resource.Data)?.value ?: defaultValue

fun Resource<*>.isLoading(): Boolean = this is Resource.Loading
