package com.sachin.shopping.demo.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.sachin.shopping.demo.data.mapper.toProductListing
import com.sachin.shopping.demo.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    repository: ProductRepository
) : ViewModel() {
    val products = repository.getAllProducts().map { data ->
        data.map { entity -> entity.toProductListing() }
    }.cachedIn(viewModelScope)
}