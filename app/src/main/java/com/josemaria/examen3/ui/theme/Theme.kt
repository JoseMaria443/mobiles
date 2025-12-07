package com.josemaria.examen3.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PokemonRed,
    onPrimary = PokemonWhite,
    primaryContainer = PokemonYellowLight,
    onPrimaryContainer = PokemonBlack,
    
    secondary = PokemonYellow,
    onSecondary = PokemonBlack,
    secondaryContainer = PokemonYellowLight,
    onSecondaryContainer = PokemonBlack,
    
    tertiary = PokemonBlue,
    onTertiary = PokemonWhite,
    tertiaryContainer = PokemonBlueLight,
    onTertiaryContainer = PokemonBlack,
    
    background = PokemonWhite,
    onBackground = PokemonBlack,
    
    surface = PokemonWhite,
    onSurface = PokemonBlack,
    surfaceVariant = PokemonGrayLight,
    onSurfaceVariant = PokemonBlack,
    
    error = PokemonRedDark,
    onError = PokemonWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = PokemonRed,
    onPrimary = PokemonWhite,
    primaryContainer = PokemonRedDark,
    onPrimaryContainer = PokemonYellow,
    
    secondary = PokemonYellow,
    onSecondary = PokemonBlack,
    secondaryContainer = PokemonYellowLight,
    onSecondaryContainer = PokemonBlack,
    
    tertiary = PokemonBlueLight,
    onTertiary = PokemonBlack,
    tertiaryContainer = PokemonBlue,
    onTertiaryContainer = PokemonWhite,
    
    background = PokemonBlack,
    onBackground = PokemonWhite,
    
    surface = Color(0xFF1E1E1E),
    onSurface = PokemonWhite,
    surfaceVariant = PokemonGray,
    onSurfaceVariant = PokemonWhite,
    
    error = PokemonRed,
    onError = PokemonBlack
)

@Composable
fun Examen3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
