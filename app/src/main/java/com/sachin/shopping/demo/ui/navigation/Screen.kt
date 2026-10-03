package com.sachin.shopping.demo.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {

    @Serializable
    data object ProductList : Screen
}