package com.sachin.shopping.demo.ui.shoppingcart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sachin.shopping.demo.R
import com.sachin.shopping.demo.ui.component.BrandText
import com.sachin.shopping.demo.ui.component.QuantitySelector
import com.sachin.shopping.demo.util.formatPrice
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navigateBack: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.onClearCartClick()
                    }) {
                        Text(
                            text = "Clear all",
                            color = Color(0xFFFB2C36)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Proceed to Checkout")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            items(state.items) {
                Card(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        AsyncImage(
                            model = it.thumbnail,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    MaterialTheme.shapes.medium
                                )
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        ) {
                            it.brand?.let { brand ->
                                BrandText(brand = brand)
                                Spacer(modifier = Modifier.size(4.dp))
                            }

                            Text(
                                text = it.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                QuantitySelector(
                                    quantity = it.quantity,
                                    onQuantityChange = { qty ->
                                        viewModel.onQuantityChange(it.productId, qty)
                                    }
                                )

                                Text(
                                    text = formatPrice(it.lineTotal),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                }
            }

            item {
                OrderSummaryCard(
                    summary = state.summary,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun OrderSummaryCard(
    summary: CartSummary,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        Spacer(Modifier.size(16.dp))

        TextRow(
            text = pluralStringResource(
                R.plurals.subtotal_items, summary.unitCount, summary.unitCount
            ),
            amount = formatPrice(summary.subtotal),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        if (summary.hasDiscount) {
            Spacer(Modifier.size(8.dp))
            TextRow(
                text = "Discount",
                amount = "-" + formatPrice(summary.discount),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        Spacer(Modifier.size(8.dp))
        HorizontalDivider(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 16.dp
            )
        )

        TextRow(
            text = "Total",
            amount = formatPrice(summary.total),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.size(16.dp))
    }
}

@Composable
private fun TextRow(
    text: String,
    amount: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = amount,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium
        )
    }
}