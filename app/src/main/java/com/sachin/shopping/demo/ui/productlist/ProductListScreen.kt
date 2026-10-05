package com.sachin.shopping.demo.ui.productlist

import android.annotation.SuppressLint
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
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
            TopBar(
                navigateToSearch = navigateToSearch,
                navigateToCart = navigateToCart,
                cartCount = state.cartCount
            )
        }
    ) { innerPadding ->
        ProductGrid(
            items = productListingItems,
            modifier = Modifier.padding(innerPadding),
            onProductClick = onProductClick
        )
    }
}

@Composable
private fun ProductGrid(
    items: LazyPagingItems<ProductListing>,
    onProductClick: (ProductListing) -> Unit,
    modifier: Modifier = Modifier
) {
    val states = items.loadState
    val isEmpty = items.itemCount == 0

    val showError = isEmpty && states.refresh is LoadState.Error
    val showEmpty = isEmpty &&
            states.refresh is LoadState.NotLoading &&
            states.source.refresh is LoadState.NotLoading &&
            states.append.endOfPaginationReached
    val showLoader = isEmpty && !showError && !showEmpty

    Box(modifier = modifier.fillMaxSize()) {
        when {
            showLoader -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            showError -> {
                Column(
                    Modifier
                        .align(Alignment.Center)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Couldn't load products")
                    TextButton(onClick = { items.retry() }) { Text("Retry") }
                }
            }

            showEmpty -> {
                Text(
                    text = "No products found",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(items.itemCount, key = items.itemKey { it.id }) { i ->
                        items[i]?.let { product ->
                            ProductListingCard(
                                product = product,
                                modifier = Modifier.clickable {
                                    onProductClick(product)
                                }
                            )
                        }
                    }

                    when (items.loadState.append) {
                        is LoadState.Loading -> item(span = StaggeredGridItemSpan.FullLine) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is LoadState.Error -> item(span = StaggeredGridItemSpan.FullLine) {
                            TextButton(onClick = { items.retry() }) { Text("Retry") }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    navigateToCart: () -> Unit,
    navigateToSearch: () -> Unit,
    cartCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Shopping",
                    style = MaterialTheme.typography.titleLarge
                )

                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Text(
                                text = cartCount.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier
                                    .offset(x = (-8).dp, y = (8).dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }) {
                    Card(
                        onClick = navigateToCart,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cart),
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            SearchBarPlaceholder(onClick = navigateToSearch)
            Spacer(modifier = Modifier.size(16.dp))
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
                        MaterialTheme.colorScheme.surfaceContainerHighest,
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