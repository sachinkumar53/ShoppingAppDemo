package com.sachin.shopping.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sachin.shopping.demo.ui.navigation.NavViewModel
import com.sachin.shopping.demo.ui.navigation.Screen
import com.sachin.shopping.demo.ui.productlist.ProductListScreen
import com.sachin.shopping.demo.ui.theme.ShoppingDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingDemoTheme {
                val navViewModel = viewModel<NavViewModel>()
                NavDisplay(
                    backStack = navViewModel.backStack,
                    entryProvider = entryProvider {
                        entry<Screen.ProductList> {
                            ProductListScreen()
                        }

                    }
                )
            }
        }
    }
}

