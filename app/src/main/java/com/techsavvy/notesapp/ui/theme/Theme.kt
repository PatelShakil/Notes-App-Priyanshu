package com.techsavvy.notesapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.techsavvy.notesapp.helpers.NotesPreferences

private val DarkColorPalette = darkColorScheme(
    primary = ThemeColors.Night.primary,
    onPrimary = ThemeColors.Night.text,
    surface = ThemeColors.Night.surafce,
    background = ThemeColors.Night.bacground
)

private val LightColorPalette = lightColorScheme(
    primary = ThemeColors.Day.primary,
    onPrimary = ThemeColors.Day.text,
    surface = ThemeColors.Day.surafce,
    background = ThemeColors.Day.bacground
)

@Composable
fun NotesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val context = LocalContext.current

    val sharedPreferences = NotesPreferences(context)

    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }



        sharedPreferences.getIsDarkMode() -> DarkColorPalette
        else -> LightColorPalette
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !sharedPreferences.getIsDarkMode()
        }
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}