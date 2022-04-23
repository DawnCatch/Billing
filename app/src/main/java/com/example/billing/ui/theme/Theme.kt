package com.example.billing.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.billing.activitys.Billing
import java.util.zip.Deflater

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

private val BlackPalette = darkColors(
    primary = Color.Black,
    primaryVariant = Color(38, 38, 38),
    secondary = Color(113, 113, 113),
    onSurface = Color.Black,
    onPrimary = Color(113, 113, 113),
)

private val PurplePalette = lightColors(
    primary = Color(98, 0, 238),
    primaryVariant = Color(241, 241, 241),
    secondary = Color(3, 218, 197),
    secondaryVariant = Color(3,218, 197),
    onPrimary = Color.White,
)

private val RedPalette = lightColors(
    primary = Color(180, 41, 41),
    primaryVariant = Color(241, 241, 241),
    secondary = Color(255, 201, 201),
    secondaryVariant = Color(255, 201, 201),
    onPrimary = Color.White,
)

val Colors.focusedColor:Color
    get() = when(Billing.sSettings.theme.getState().value) {
        0 -> {
            onBackground
        }

        else -> {
            primary
        }
    }

val Colors.surfaceColor:Color
    get() = when(Billing.sSettings.theme.getState().value) {
        0 -> {
            primaryVariant
        }

        else -> {
            Color.White
        }
    }

val Colors.itemBackgroud:Color
    get() = when(Billing.sSettings.theme.getState().value) {
        0 -> {
            Color(113, 113, 113)
        }

        1 -> {
            Color(242, 243, 245)
        }

        else -> {
            Color(242, 243, 245)
        }
    }

val Colors.itemSelectedBackgroud:Color
    get() = when(Billing.sSettings.theme.getState().value) {
        0 -> {
            Color.Black
        }

        1 -> {
            Color.Yellow
        }

        else -> {
            Color.Yellow
        }
    }

val Colors.keyboard:Color
    get() = when(Billing.sSettings.theme.getState().value) {
        0 -> {
            Color.Black
        }

        1 -> {
            Color(213, 213, 215)
        }

        else -> {
            Color(213, 213, 215)
        }
    }

val Colors.keyboardTime:Color
    get() = when(Billing.sSettings.theme.getState().value) {
        0 -> {
            Color.Black
        }

        1 -> {
            Color(248, 238, 235)
        }

        else -> {
            Color(248, 238, 235)
        }
    }

val Colors.keyboardMov:Color
    get() = when(Billing.sSettings.theme.getState().value) {
        0 -> {
            Color.Black
        }

        1 -> {
            Color(144, 218, 228)
        }

        else -> {
            Color(144, 218, 228)
        }
    }

@Composable
fun BillingTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val color = when (Billing.sSettings.theme.getState().value) {
        0 -> {
            BlackPalette
        }

        1 -> {
            PurplePalette
        }

        2 -> {
            RedPalette
        }

        else -> {
            PurplePalette
        }
    }


    MaterialTheme(
        colors = color,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}