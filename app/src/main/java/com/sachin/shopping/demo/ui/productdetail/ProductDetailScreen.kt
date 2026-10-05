package com.sachin.shopping.demo.ui.productdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sachin.shopping.demo.R
import com.sachin.shopping.demo.ui.component.CenteredMessage
import com.sachin.shopping.demo.ui.component.QuantitySelector
import com.sachin.shopping.demo.ui.component.loadingIndicator
import com.sachin.shopping.demo.ui.productdetail.component.additionalInfoSection
import com.sachin.shopping.demo.ui.productdetail.component.detailSection
import com.sachin.shopping.demo.ui.productdetail.component.imageSection
import com.sachin.shopping.demo.ui.productdetail.component.reviewsSection
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ProductDetailScreen(
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    var descExpanded by remember { mutableStateOf(false) }

    ProductDetailScreen(
        navigateBack = navigateBack,
        navigateToCart = navigateToCart,
        onDescExpandedChange = { descExpanded = it },
        onQuantityChange = { viewModel.onQuantityChange(it) },
        onRetry = { viewModel.loadProduct() },
        descExpanded = descExpanded,
        state = state
    )
}

@Composable
private fun ProductDetailScreen(
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit,
    onDescExpandedChange: (Boolean) -> Unit,
    onQuantityChange: (Int) -> Unit,
    onRetry: () -> Unit,
    descExpanded: Boolean,
    state: ProductDetailState,
) {
    Scaffold(
        topBar = {
            BackButtonTopBar(onBackClick = navigateBack)
        },
        bottomBar = {
            if (state.product != null) {
                BottomBar(
                    onGoToCartClick = navigateToCart,
                    onQuantityChange = onQuantityChange,
                    quantity = state.quantity,
                    stock = state.product.stock
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            when {
                state.isLoading -> {
                    loadingIndicator()
                }

                state.error != null -> {
                    item {
                        CenteredMessage(
                            text = state.error,
                            modifier = Modifier.fillParentMaxSize(),
                            actionLabel = "Retry",
                            onAction = onRetry
                        )
                    }
                }

                state.product != null -> {
                    val product = state.product
                    imageSection(images = product.images)

                    detailSection(
                        brand = product.brand,
                        title = product.title,
                        rating = product.rating,
                        reviewsCount = product.reviews.size,
                        discountedPrice = product.discountedPrice,
                        originalPrice = product.price,
                        description = product.description,
                        discountPercentage = product.discountPercentage,
                        descExpanded = descExpanded,
                        onDescExpandedChange = onDescExpandedChange
                    )

                    additionalInfoSection(
                        shippingInfo = product.shippingInformation,
                        returnPolicy = product.returnPolicy,
                        warrantyInfo = product.warrantyInformation
                    )

                    if (product.reviews.isNotEmpty()) {
                        reviewsSection(reviews = product.reviews)
                    }

                }
            }

        }

    }
}


@Composable
private fun BackButtonTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .fillMaxWidth()
            .height(56.dp)
    ) {
        IconButton(
            onClick = onBackClick,
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
}

@Composable
private fun BottomBar(
    onGoToCartClick: () -> Unit,
    modifier: Modifier = Modifier,
    onQuantityChange: (Int) -> Unit,
    quantity: Int,
    stock: Int
) {
    Surface(
        shadowElevation = 12.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(16.dp)
        ) {
            AnimatedVisibility(
                visible = quantity > 0,
                enter = fadeIn() + expandHorizontally(
                    expandFrom = Alignment.Start
                ),
                exit = fadeOut() + shrinkHorizontally(
                    shrinkTowards = Alignment.Start
                )
            ) {
                QuantitySelector(
                    quantity = quantity,
                    onQuantityChange = onQuantityChange,
                    maxQuantity = stock,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    if (quantity > 0) {
                        onGoToCartClick()
                    } else {
                        onQuantityChange(1)
                    }
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (quantity > 0)
                            R.drawable.ic_cart
                        else
                            R.drawable.ic_add_to_cart
                    ),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    if (quantity > 0) "Go to Cart" else "Add to Cart"
                )
            }
        }
    }
}

