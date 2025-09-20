package ru.whoame.recogniser.utils

/**
 * Represents a string value that can be resolved in two ways:
 * either from a static [CharSequence] or from an Android string resource with format arguments.
 * This sealed interface provides a type-safe way to handle different string sources.
 **/
sealed interface StringArgs

/**
 * Represents a string that is defined as an Android resource.
 *
 * @property resourceId The ID of the string resource (e.g., `R.string.example`).
 * @property args The optional format arguments to be used with the string resource.
 **/
data class ResourceArgs(
  val resourceId: Int,
  val args: Array<Any> = emptyArray(),
) : StringArgs {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ResourceArgs

    if (resourceId != other.resourceId) return false
    if (!args.contentEquals(other.args)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = resourceId
    result = 31 * result + args.contentHashCode()
    return result
  }

}

/**
 * Represents a string that is provided as a direct [CharSequence].
 *
 * @property value The concrete string value.
 **/
data class CharSequenceArgs(val value: CharSequence) : StringArgs
