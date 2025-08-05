package com.example.composetutorial.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Definir o esquema de cores para o tema escuro
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF6600), // Cor laranja para botão e texto "Rápida"
    onPrimary = Color.White, // Texto branco no botão
    background = Color.Black, // Fundo preto
    onSurface = Color.White, // Texto branco em fundo escuro
   
)

// Definir uma tipografia personalizada
private val AppTypography = Typography(
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    )
)

// Definir formas (usando padrão do Material3)
private val AppShapes = Shapes()

@Composable
fun ComposeTutorialTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography, // Usar tipografia personalizada
        shapes = AppShapes, // Usar formas padrão
        content = content
    )
}