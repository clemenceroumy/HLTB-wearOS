package com.croumy.hltb_wearos.presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val surface = Color(0xFF242728)
val shimmerColor = Color(0xFFBABABA)
val red = Color(0xFFE53935)
val primaryGradiant = Color(0xFF45CF9F)
val secondaryGradiant = Color(0xFF93F9B9)
val gradiant = Brush.horizontalGradient(
    colors = listOf(
        primaryGradiant,
        secondaryGradiant
    )
)
val primary = Color(0xFF10A470)

internal val wearColorPalette: Colors = Colors(
    surface = surface,
    primary = primary,
)