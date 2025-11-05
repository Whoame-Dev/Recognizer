package ru.whoame.recogniser.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Convenience annotation that combines two Compose previews for light and dark modes.
 *
 * @see [Preview]
 **/
@Preview(
    name = "Dark mode",
    group = "UI mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Preview(
    name = "Light mode",
    group = "UI mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
annotation class DarkLightPreviews

@Preview(
    name = "Dark mode compact",
    group = "Screen UI mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Preview(
    name = "Light mode compact",
    group = "Screen UI mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Preview(
    name = "Dark mode medium",
    group = "Screen UI mode",
    widthDp = 600,
    heightDp = 480,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Preview(
    name = "Light mode medium",
    group = "Screen UI mode",
    widthDp = 600,
    heightDp = 480,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Preview(
    name = "Dark mode expanded",
    group = "Screen UI mode",
    widthDp = 840,
    heightDp = 900,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Preview(
    name = "Light mode expanded",
    group = "Screen UI mode",
    widthDp = 840,
    heightDp = 900,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
annotation class DarkLightScreenPreviews
