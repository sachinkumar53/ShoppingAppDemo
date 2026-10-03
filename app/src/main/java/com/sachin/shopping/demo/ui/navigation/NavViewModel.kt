package com.sachin.shopping.demo.ui.navigation

import androidx.lifecycle.ViewModel

class NavViewModel : ViewModel() {

    val backStack = mutableListOf<Screen>(Screen.ProductList)

}