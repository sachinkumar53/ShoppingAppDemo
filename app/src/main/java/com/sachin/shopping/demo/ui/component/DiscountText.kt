package com.sachin.shopping.demo.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sachin.shopping.demo.ui.theme.ShoppingDemoTheme

@Composable
fun DiscountText(
    discountPercentage: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelMedium
) {
    Text(
        modifier = modifier
            .background(
                color = Color(0xFF00c950).copy(0.25f),
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        text = "${discountPercentage}% OFF",
        color = Color(0xFF00c950),
        style = style,
        lineHeight = style.fontSize
    )
}

@Preview(showBackground = true)
@Composable
private fun DiscountTextPreview() {
    ShoppingDemoTheme {
        DiscountText(discountPercentage = 20, modifier = Modifier)
    }
}