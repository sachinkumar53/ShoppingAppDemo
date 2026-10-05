package com.sachin.shopping.demo.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sachin.shopping.demo.ui.navigation.CartRoute
import com.sachin.shopping.demo.ui.navigation.ProductDetailRoute
import com.sachin.shopping.demo.ui.navigation.ProductListRoute
import com.sachin.shopping.demo.ui.navigation.SearchRoute
import com.sachin.shopping.demo.ui.productdetail.ProductDetailScreen
import com.sachin.shopping.demo.ui.productlist.ProductListScreen
import com.sachin.shopping.demo.ui.search.SearchScreen
import com.sachin.shopping.demo.ui.shoppingcart.CartScreen

@Composable
fun ShoppingApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ProductListRoute,
    ) {
        composable<ProductListRoute> {
            ProductListScreen(
                navigateToSearch = {
                    navController.navigate(SearchRoute)
                },
                navigateToCart = {
                    navController.navigate(CartRoute)
                },
                onProductClick = { product ->
                    navController.navigate(ProductDetailRoute(productId = product.id))
                }
            )
        }

        composable<ProductDetailRoute> {
            ProductDetailScreen(
                navigateBack = navController::navigateUp,
                navigateToCart = {
                    navController.navigate(CartRoute)
                }
            )
        }

        composable<CartRoute> {
            CartScreen(
                navigateBack = navController::navigateUp
            )
        }

        composable<SearchRoute> {
            SearchScreen(
                onBack = navController::navigateUp,
                onProductClick = { productId ->
                    navController.navigate(ProductDetailRoute(productId = productId))
                }
            )
        }
    }
}