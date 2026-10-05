package com.sachin.shopping.demo.ui.productdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sachin.shopping.demo.ui.component.BrandText
import com.sachin.shopping.demo.ui.component.DiscountText
import com.sachin.shopping.demo.util.formatPrice
import com.webtoonscorp.android.readmore.material3.ReadMoreText

fun LazyListScope.detailSection(
    brand: String?,
    title: String,
    rating: Double,
    reviewsCount: Int,
    discountedPrice: Double?,
    originalPrice: Double,
    discountPercentage: Int,
    description: String,
    descExpanded: Boolean,
    onDescExpandedChange: (Boolean) -> Unit
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.size(16.dp))
            brand?.let { brand ->
                BrandText(brand = brand)
            }
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                StarRating(rating)
                Text(
                    " (${reviewsCount})",
                    style = MaterialTheme.typography.labelMedium,
                    lineHeight = MaterialTheme.typography.labelMedium.fontSize
                )
            }
            Spacer(modifier = Modifier.size(8.dp))

            PriceSection(
                discountedPrice = discountedPrice,
                originalPrice = originalPrice,
                discountPercentage = discountPercentage
            )

            Spacer(modifier = Modifier.size(16.dp))

            ReadMoreText(
                text = description,
                expanded = descExpanded,
                onExpandedChange = onDescExpandedChange,
                style = MaterialTheme.typography.bodyMedium,
                readMoreText = "more",
                readMoreStyle = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                ),
                readMoreMaxLines = 3
            )
        }
    }
}


@Composable
private fun PriceSection(
    originalPrice: Double,
    discountedPrice: Double?,
    discountPercentage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = formatPrice(discountedPrice ?: originalPrice),
            style = TextStyle(
                fontSize = 30.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        if (discountedPrice != null) {
            Text(
                text = formatPrice(originalPrice),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textDecoration = TextDecoration.LineThrough
                )
            )

            DiscountText(discountPercentage = discountPercentage)
        }
    }
}