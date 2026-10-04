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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sachin.shopping.demo.R
import com.sachin.shopping.demo.data.model.Review
import com.sachin.shopping.demo.util.RelativeTimeFormatter
import com.sachin.shopping.demo.util.formatPrice
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ProductDetailScreen(
    navigateBack: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val product = state.product

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
                    Box(
                        modifier = Modifier.fillParentMaxSize()

                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            } else if (product != null) {
                item {
                    ImageSlider(
                        images = product.images,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .fillMaxHeight(0.5f)
                    )
                }

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
                        product.brand?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Text(
                            text = product.title,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Row {
                            if (product.discountPercentage != null) {
                                Text(
                                    text = formatPrice(product.price),
                                    style = MaterialTheme.typography.bodyMedium,
                                    textDecoration = TextDecoration.LineThrough,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            Text(
                                text = formatPrice(product.discountedPrice ?: product.price),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            if (product.discountPercentage > 0) {
                                Text(
                                    text = "${product.discountPercentage}% OFF",
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 6.dp
                                    ),
                                    color = Color(0xFF00c950),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            "Description",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        Text(
                            text = product.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }

                item {
                    HorizontalDivider()
                    Text(
                        text = "Dimensions: ${product.dimensions.width} x ${product.dimensions.height} x ${product.dimensions.depth}",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(text = "Weight: ${product.weight} Kg")
                    Text(text = "Return Policy: ${product.returnPolicy}")
                    Text(text = "Shipping Information: ${product.shippingInformation}")
                    Text(text = "Warranty Information: ${product.warrantyInformation}")
                    HorizontalDivider()
                }

                if (product.reviews.isNotEmpty()) {
                    reviewsSection(reviews = product.reviews)
                }

            }

        }

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

private fun LazyListScope.reviewsSection(
    reviews: List<Review>
) {
    item {
        Text(
            "Reviews (${reviews.size})",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier
                .fillParentMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        )
    }
    items(reviews) { review ->
        Column(
            modifier = Modifier
                .fillParentMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = review.reviewerName,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = RelativeTimeFormatter.format(review.date),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            StarRating(rating = review.rating)
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(16.dp))
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
    rating: Int,
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
                tint = if (it < rating) MaterialTheme.colorScheme.primary else Color.Gray,
                modifier = Modifier.size(12.dp)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "${rating}.0",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
