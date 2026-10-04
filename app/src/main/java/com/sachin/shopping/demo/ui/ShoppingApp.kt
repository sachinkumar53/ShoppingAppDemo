package com.sachin.shopping.demo.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sachin.shopping.demo.ui.navigation.ProductDetailRoute
import com.sachin.shopping.demo.ui.navigation.ProductListRoute
import com.sachin.shopping.demo.ui.productdetail.ProductDetailScreen
import com.sachin.shopping.demo.ui.productlist.ProductListScreen

@Composable
fun ShoppingApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ProductListRoute,
    ) {
        composable<ProductListRoute> {
            ProductListScreen(
                onProductClick = { product ->
                    navController.navigate(ProductDetailRoute(productId = product.id))
                }
            )
        }

        composable<ProductDetailRoute> {
            ProductDetailScreen()
        }
    }
}