package com.sachin.shopping.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sachin.shopping.demo.ui.ShoppingApp
import com.sachin.shopping.demo.ui.theme.ShoppingDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingDemoTheme {
                ShoppingApp()
            }
        }
    }
}

