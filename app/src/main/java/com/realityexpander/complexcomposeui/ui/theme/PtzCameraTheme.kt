package com.realityexpander.complexcomposeui.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.realityexpander.complexcomposeui.R

@Composable
fun PtzCameraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val dynamicColor = false

    val darkColorScheme = darkColorScheme(
        primary = colorResource(R.color.primaryColor),
        secondary = colorResource(R.color.colorAccent),
        tertiary = Color.Yellow,
        onPrimary = Color.White,
        surface = Color.Black.copy(alpha = 0.7f),
    )

    val lightColorScheme = lightColorScheme(
        primary = colorResource(R.color.primaryColor),
        secondary = colorResource(R.color.colorAccent),
        tertiary = Color.Yellow,
        onPrimary = Color.White,
        surface = Color.Black.copy(alpha = 0.7f),

        /* Other default colors to override
        background = Color(0xFFFFFBFE),
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color(0xFF1C1B1F),
        onSurface = Color(0xFF1C1B1F),
        */
    )

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

val LocalIsTablet = staticCompositionLocalOf { false }

object AppDimens {
    val fontScale: Float @Composable get() = if (LocalIsTablet.current) 1f else 0.78f
    val iconScale: Float @Composable get() = if (LocalIsTablet.current) 1f else 0.70f
    val roundedCornerSize: Dp @Composable get() = if (LocalIsTablet.current) 15.dp else 6.dp
}

val LocalOnErrorMessage = staticCompositionLocalOf {
    { onErrorMessage: String -> Unit }
}
