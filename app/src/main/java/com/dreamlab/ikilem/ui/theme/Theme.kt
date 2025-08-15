package com.dreamlab.ikilem.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val ColorScheme = lightColorScheme()


@Composable
fun IkilemTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else {
            if (darkTheme) darkColorScheme(
                primary = PurpleStart,
                secondary = TealEnd,
                background = DarkBg,
                surface = DarkCard
            ) else lightColorScheme(
                primary = PurpleStart,
                secondary = TealEnd,
                background = LightBg
            )
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes(
            extraSmall = RoundedCornerShape(8.dp),
            small      = RoundedCornerShape(12.dp),
            medium     = RoundedCornerShape(16.dp),
            large      = RoundedCornerShape(20.dp),
            extraLarge = RoundedCornerShape(28.dp)
        ),
        content = content
    )
}