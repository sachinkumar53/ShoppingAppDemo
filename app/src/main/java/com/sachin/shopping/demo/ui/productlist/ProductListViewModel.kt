package com.sachin.shopping.demo.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sachin.shopping.demo.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    repository: ProductRepository
) : ViewModel() {
    val products = repository.getAllProducts().cachedIn(viewModelScope)
}