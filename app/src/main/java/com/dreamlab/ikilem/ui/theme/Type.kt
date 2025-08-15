package com.dreamlab.ikilem.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight


private val BaseTypography = Typography()

val Typography = BaseTypography.copy(
    headlineSmall = BaseTypography.headlineSmall.copy(fontWeight = FontWeight.Bold),
    titleLarge    = BaseTypography.titleLarge.copy(fontWeight   = FontWeight.SemiBold)
)