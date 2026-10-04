package com.sachin.shopping.demo.ui.component

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun BrandText(
    brand: String,
    fontSize: TextUnit = 11.sp,
    lineHeight: TextUnit = 16.sp,
    letterSpacing: TextUnit = 2.5.sp,
    color: Color = LocalContentColor.current
) {
    Text(
        text = brand.map { it.uppercase() }.joinToString(""),
        fontSize = fontSize,
        lineHeight = lineHeight,
        letterSpacing = letterSpacing,
        color = color
    )
}