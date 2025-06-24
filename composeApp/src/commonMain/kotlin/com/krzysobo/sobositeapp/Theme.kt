package com.krzysobo.sobositeapp
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color


//class MoreColors (
//        primary: Color,
//        primaryVariant: Color,
//        secondary: Color,
//        secondaryVariant: Color,
//        background: Color,
//        surface: Color,
//        error: Color,
//        onPrimary: Color,
//        onSecondary: Color,
//        onBackground: Color,
//        onSurface: Color,
//        onError: Color,
//        isLight: Boolean) {
//    val colors = Colors(
//        primary,
//        primaryVariant,
//        secondary,
//        secondaryVariant,
//        background,
//        surface,
//        error,
//        onPrimary,
//        onSecondary,
//        onBackground,
//        onSurface,
//        onError,
//        isLight,
//    )
//}



private val LightColorPalette = lightColors(
    primary = Color(0xFF6200EE),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC5),
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

// Dark color palette
private val DarkColorPalette = darkColors(
    primary = Color(0xFFBB86FC),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC5),
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)



@Composable
fun SoboTheme(
        useDarkTheme: Boolean = isSystemInDarkTheme(),
        colors: Colors = if (useDarkTheme) DarkColorPalette else LightColorPalette,
        typography: Typography = MaterialTheme.typography,
        shapes: Shapes = MaterialTheme.shapes,
        content: @Composable () -> Unit) {
    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

