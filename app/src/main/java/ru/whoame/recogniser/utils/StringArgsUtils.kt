package ru.whoame.recogniser.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Resolves the [StringArgs] instance into a final, displayable [String].
 *
 * This function must be called within a `@Composable` context because it uses
 * [stringResource] to resolve [ResourceArgs].
 *
 * @return The formatted [String].
 **/
@Composable
fun StringArgs.format(): String = when (this) {
  is ResourceArgs -> stringResource(resourceId, *args)
  is CharSequenceArgs -> value.toString()
}

/**
 * A convenience function to convert an string resource ID into a [ResourceArgs] instance.
 *
 * @param args The optional format arguments for the string resource.
 *
 * @return A [ResourceArgs] instance wrapping the resource ID and its arguments.
 **/
fun Int.toStringArgs(vararg args: Any): StringArgs = ResourceArgs(this, arrayOf(*args))

/**
 * A convenience function to convert any [CharSequence] into a [CharSequenceArgs] instance.
 *
 * This allows for a more fluent way of creating a [StringArgs] object from a raw string.
 *
 * @return A [CharSequenceArgs] instance wrapping this CharSequence.
 **/
fun CharSequence.toStringArgs(): StringArgs = CharSequenceArgs(this)
