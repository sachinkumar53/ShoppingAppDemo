package com.sachin.shopping.demo.ui.search

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sachin.shopping.demo.ui.productlist.SearchBarPlaceholder

@Composable
fun SearchScreen() {
    Scaffold { innerPadding ->
        SearchBarPlaceholder(
            modifier = Modifier.padding(innerPadding),
            onClick = {

            }
        )
    }
}