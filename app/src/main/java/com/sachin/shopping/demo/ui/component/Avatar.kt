package com.sachin.shopping.demo.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue

@Composable
fun Avatar(
    name: String,
    modifier: Modifier = Modifier
) {
    val initials = remember(name) { name.split(" ").joinToString("") { it.take(1).uppercase() } }
    val backgroundColor = avatarColor(name)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(backgroundColor)
    ) {
        Text(
            text = initials,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.contentColorFor(backgroundColor)
        )
    }
}

private val avatarColors = listOf(
    Color(0xFF4F46E5),
    Color(0xFF7C3AED),
    Color(0xFFA21CAF),
    Color(0xFFBE123C),
    Color(0xFFC2410C),
    Color(0xFFB45309),
    Color(0xFF047857),
    Color(0xFF0F766E),
    Color(0xFF0369A1)
)

private fun avatarColor(name: String): Color {
    val index = (name.hashCode().absoluteValue) % avatarColors.size
    return avatarColors[index]
}
