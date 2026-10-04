package com.sachin.shopping.demo.ui.productdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sachin.shopping.demo.R
import com.sachin.shopping.demo.data.model.Review
import com.sachin.shopping.demo.ui.component.Avatar
import com.sachin.shopping.demo.ui.component.DiscountText
import com.sachin.shopping.demo.util.RelativeTimeFormatter
import com.sachin.shopping.demo.util.formatPrice
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import com.webtoonscorp.android.readmore.material3.ReadMoreText
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ProductDetailScreen(
    navigateBack: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val product = state.product

    var descExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                IconButton(
                    onClick = navigateBack,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = null
                    )
                }
            }
        },
        bottomBar = {
            if (product != null) {
                Surface(
                    shadowElevation = 12.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .padding(16.dp),
                        onClick = {
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_add_to_cart),
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Add to Cart")
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            if (state.isLoading) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            } else if (product != null) {
                heroSection(images = product.images)

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
                        product.brand?.let { brand ->
                            Text(
                                text = brand.map { it.uppercase() }.joinToString(""),
                                style = TextStyle(
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp,
                                    letterSpacing = 2.5.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = product.title,
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            StarRating(product.rating)
                            VerticalDivider(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .height(16.dp)
                            )
                            Text(
                                "${product.reviews.size} reviews",
                                style = MaterialTheme.typography.labelMedium,
                                lineHeight = MaterialTheme.typography.labelMedium.fontSize
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))

                        PriceSection(
                            discountedPrice = product.discountedPrice,
                            originalPrice = product.price,
                            discountPercentage = product.discountPercentage
                        )

                        Spacer(modifier = Modifier.size(16.dp))

                        ReadMoreText(
                            text = product.description,
                            expanded = descExpanded,
                            onExpandedChange = { descExpanded = it },
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

                item {
                    Surface(
                        modifier = Modifier.fillParentMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .padding(horizontal = 16.dp)
                        ) {
                            //HorizontalDivider()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                InfoBox(
                                    iconResId = R.drawable.ic_shipping,
                                    text = product.shippingInformation,
                                    modifier = Modifier.weight(1f)
                                )
                                VerticalDivider(
                                    modifier = Modifier
                                        .height(32.dp)
                                        .padding(horizontal = 8.dp)
                                        .align(Alignment.CenterVertically)
                                )
                                InfoBox(
                                    iconResId = R.drawable.ic_return,
                                    text = product.returnPolicy,
                                    modifier = Modifier.weight(1f)
                                )
                                VerticalDivider(
                                    modifier = Modifier
                                        .height(32.dp)
                                        .padding(horizontal = 8.dp)
                                        .align(Alignment.CenterVertically)
                                )
                                InfoBox(
                                    iconResId = R.drawable.ic_warranty,
                                    text = product.warrantyInformation,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            //HorizontalDivider()
                        }
                    }
                }

                if (product.reviews.isNotEmpty()) {
                    reviewsSection(reviews = product.reviews)
                }

            }

        }

    }
}

@Composable
private fun InfoBox(
    iconResId: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }

}

@Composable
private fun ImageSlider(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { images.size }
    Box(modifier = modifier) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        if (pagerState.pageCount > 1) {
            DotsIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                dotCount = pagerState.pageCount,
                type = ShiftIndicatorType(
                    dotsGraphic = DotGraphic(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        size = 6.dp
                    )
                ),
                pagerState = pagerState
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

private fun LazyListScope.heroSection(
    images: List<String>
) {
    item {
        ImageSlider(
            images = images,
            modifier = Modifier
                .fillParentMaxWidth()
                .fillMaxHeight(0.5f)
        )
    }
}

private fun LazyListScope.reviewsSection(
    reviews: List<Review>
) {
    item {
        HorizontalDivider()
        Text(
            "Customer Reviews (${reviews.size})",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .fillParentMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        )
    }
    items(reviews) { review ->
        Surface(
            modifier = Modifier.fillParentMaxWidth(),
        ) {
            OutlinedCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp)
            ) {
                Spacer(modifier = Modifier.size(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Avatar(name = review.reviewerName)
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = review.reviewerName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        StarRating(rating = review.rating.toDouble())
                    }

                    Text(
                        text = RelativeTimeFormatter.format(review.date),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.align(Alignment.Top)
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = review.comment,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
private fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minQuantity: Int = 0,
    maxQuantity: Int = Int.MAX_VALUE,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = accentColor,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = {
                if (quantity > minQuantity) {
                    onQuantityChange(quantity - 1)
                }
            },
            enabled = quantity > minQuantity,
            modifier = Modifier.size(36.dp)
        ) {
            Text(
                text = "−",
                fontSize = 22.sp,
                color = if (quantity > minQuantity)
                    accentColor else Color.Gray,
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = quantity.toString(),
            modifier = Modifier.widthIn(min = 28.dp),
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(
            onClick = {
                if (quantity < maxQuantity) {
                    onQuantityChange(quantity + 1)
                }
            },
            enabled = quantity < maxQuantity,
            modifier = Modifier.size(36.dp)
        ) {
            Text(
                text = "+",
                fontSize = 22.sp,
                color = if (quantity < maxQuantity)
                    accentColor else Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
private fun StarRating(
    rating: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(5) {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = if (it < rating) Color(0xFFFFB900) else Color.Gray,
                modifier = Modifier.size(14.dp)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = String.format("%.1f", rating),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = MaterialTheme.typography.labelMedium.fontSize
        )
    }
}
