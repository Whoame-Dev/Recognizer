package ru.whoame.recogniser.utils

import androidx.annotation.IntRange

/**
 * Represents a generic resource that can be in one of three states:
 * [Loading], [Data], or [Error].
 * Each state carries a timestamp indicating when the state was created.
 *
 * This interface is commonly used for managing data from data or domain layers.
 *
 * @param T The type of value held by the resource in its [Data] state.
 **/
sealed interface Resource<out T> {

    val timestamp: Long

    /**
     * Indicates the resource is currently loading.
     *
     * @property timestamp The time when the loading state was created.
     **/
    data class Loading<out T>(
        @get:IntRange(from = 0L) override val timestamp: Long = System.currentTimeMillis(),
    ) : Resource<T>

    /**
     * Represents a successful data result.
     *
     * @property value The contained data value.
     * @property timestamp The time when the data state was created.
     **/
    data class Data<out T>(
        val value: T,
        @get:IntRange(from = 0L) override val timestamp: Long = System.currentTimeMillis(),
    ) : Resource<T>

    /**
     * Represents an error state with an associated exception.
     *
     * @property throwable The thrown exception causing this error.
     * @property timestamp The time when the error state was created.
     **/
    data class Error<out T>(
        val throwable: Throwable,
        @get:IntRange(from = 0L) override val timestamp: Long = System.currentTimeMillis(),
    ) : Resource<T>

}

/**
 * Wraps value to [Resource] state.
 **/
fun <T> T.toResource(): Resource<T> = Resource.Data(this)

/**
 * Transforms the value inside [Resource.Data] using the provided block.
 * Either returns [Resource.Loading] or [Resource.Error] if no data is present
 **/
inline fun <T, R> Resource<T>.map(crossinline block: (T) -> R): Resource<R> = when (this) {
    is Resource.Loading -> Resource.Loading(timestamp)
    is Resource.Data -> Resource.Data(
        value = block.invoke(value),
        timestamp = timestamp,
    )

    is Resource.Error -> Resource.Loading(timestamp)
}

/**
 * Filters the elements of the list inside [Resource.Data] using the provided predicate.
 * If the Resource is in [Resource.Data] state, only the elements that match the predicate are retained.
 *
 * @param predicate Function that returns true for elements to keep.
 *
 * @return A [Resource] containing the filtered list or corresponding loading state.
 **/
inline fun <T> Resource<List<T>>.filter(crossinline predicate: (T) -> Boolean): Resource<List<T>> = map { values ->
    values.filter(predicate)
}

/**
 * Returns the value if this is [Resource.Data], otherwise returns the defaultValue.
 **/
fun <T> Resource<T>.valueOrDefault(defaultValue: T): T = (this as? Resource.Data)?.value ?: defaultValue

/**
 * Returns the value if this is [Resource.Data], otherwise returns the defaultValue or null.
 **/
fun <T> Resource<T>.valueOrNull(defaultValue: T? = null): T? = (this as? Resource.Data)?.value ?: defaultValue

/**
 * Returns true if the Resource is in Loading state.
 **/
fun Resource<*>.isLoading(): Boolean = this is Resource.Loading
