package com.sachin.shopping.demo.ui.productdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val product = state.product
    Scaffold { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else if (product != null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                HorizontalPager(
                    state = rememberPagerState { product.images.size }
                ) { page ->
                    AsyncImage(
                        model = product.images[page],
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Text(
                    text = product.title,
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    text = product.description,
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    text = "$${product.price}",
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    text = "Rating: ${product.rating}",
                    modifier = Modifier.padding(16.dp)
                )

            }
        }
    }
}