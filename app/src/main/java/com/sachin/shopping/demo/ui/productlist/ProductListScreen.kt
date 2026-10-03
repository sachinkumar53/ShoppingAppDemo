package com.sachin.shopping.demo.ui.productlist

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sachin.shopping.demo.R
import com.sachin.shopping.demo.data.model.ProductListing
import org.orbitmvi.orbit.compose.collectAsState
import kotlin.math.roundToInt

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    ProductListScreen(
        state = state
    )
}


@Composable
private fun ProductListScreen(
    state: ProductListUiState
) {
    Scaffold { innerPadding ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier.padding(innerPadding),
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalItemSpacing = 12.dp,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(state.products.size) { index ->
                val product = state.products[index]
                ProductListingCard(
                    product = product
                )
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
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.titleSmall
                )

                product.discountPercentage?.roundToInt()?.takeIf { it > 0 }?.let {
                    Text(
                        text = "$it% off",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}