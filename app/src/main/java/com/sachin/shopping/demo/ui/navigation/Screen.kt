package com.sachin.shopping.demo.ui.navigation

import kotlinx.serialization.Serializable


@Serializable
data object ProductListRoute

@Serializable
data class ProductDetailRoute(val productId: Int)

@Serializable
data object CartRoute