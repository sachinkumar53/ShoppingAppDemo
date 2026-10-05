package com.sachin.shopping.demo.ui.productlist

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.sachin.shopping.demo.R
import com.sachin.shopping.demo.data.model.ProductListing
import com.sachin.shopping.demo.util.formatPrice
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ProductListScreen(
    navigateToSearch: () -> Unit,
    navigateToCart: () -> Unit,
    onProductClick: (ProductListing) -> Unit,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val paginatedProducts = viewModel.products.collectAsLazyPagingItems()

    ProductListScreen(
        navigateToSearch = navigateToSearch,
        navigateToCart = navigateToCart,
        onProductClick = onProductClick,
        productListingItems = paginatedProducts,
        state = state
    )
}


@Composable
private fun ProductListScreen(
    navigateToSearch: () -> Unit,
    navigateToCart: () -> Unit,
    onProductClick: (ProductListing) -> Unit,
    productListingItems: LazyPagingItems<ProductListing>,
    state: ProductListUiState
) {
    Scaffold(
        topBar = {
            SearchBarPlaceholder(
                onClick = navigateToSearch,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 16.dp)
                    .padding(top = 32.dp)
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = state.cartCount > 0,
                enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
            ) {
                OutlinedCard(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column {
                            Text(
                                text = pluralStringResource(R.plurals.cart_items, state.cartCount, state.cartCount),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(
                                text = formatPrice(state.cartTotal),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Button(onClick = navigateToCart) {
                            Text("View cart")
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_right),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp)
                                    .offset(x = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(
                productListingItems.itemCount,
                key = productListingItems.itemKey { it.id }
            ) { index ->
                productListingItems[index]?.let {
                    ProductListingCard(
                        product = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onProductClick(it)
                            }
                    )
                }
            }

            // Initial loading
            when (productListingItems.loadState.refresh) {
                is LoadState.Error -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Button(onClick = {
                            // TODO: Implement retry logic
                        }) {
                            Text("Retry")
                        }
                    }
                }

                LoadState.Loading -> {
                    item(
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            CircularProgressIndicator()
                        }
                    }
                }

                else -> {

                }
            }

            when (productListingItems.loadState.append) {
                is LoadState.Error -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Button(onClick = {
                            // TODO: Implement retry logic
                        }) {
                            Text("Retry")
                        }
                    }
                }

                LoadState.Loading -> {
                    item(
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {

                            CircularProgressIndicator()
                        }
                    }
                }

                else -> {

                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun ProductListingCard(
    product: ProductListing,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            AsyncImage(
                model = product.thumbnail,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Inside
            )

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
                    .background(
                        MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                        CircleShape
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = null,
                    modifier = Modifier.size(10.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = String.format("%.1f", product.rating),
                    fontSize = 10.sp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }

        Column(modifier = Modifier.padding(12.dp)) {
            product.brand?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            Text(
                text = product.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = formatPrice(product.discountedPrice ?: product.price),
                    style = MaterialTheme.typography.titleSmall
                )
                if (product.discountedPrice != null) {
                    Text(
                        text = formatPrice(product.price),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        text = "${product.discountPercentage}% OFF",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF00c950)
                    )
                }
            }
        }
    }
}