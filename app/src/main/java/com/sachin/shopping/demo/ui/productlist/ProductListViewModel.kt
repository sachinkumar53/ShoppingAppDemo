package com.sachin.shopping.demo.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.sachin.shopping.demo.data.mapper.toProductListing
import com.sachin.shopping.demo.data.model.ProductListing
import com.sachin.shopping.demo.data.repository.CartRepository
import com.sachin.shopping.demo.data.repository.ProductRepository
import com.sachin.shopping.demo.ui.shoppingcart.toSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    repository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel(), OrbitContainerHost<ProductListUiState, ProductListUiState, Nothing> {

    val products: Flow<PagingData<ProductListing>> = repository.getAllProducts()
        .map { data -> data.map { it.toProductListing() } }
        .cachedIn(viewModelScope)


    override val container = orbitContainer<ProductListUiState, Nothing>(ProductListUiState()) {
        intent {
            cartRepository.lines.collect { lines ->
                reduce {
                    state.copy(
                        cartCount = lines.sumOf { it.quantity },
                        cartTotal = lines.toSummary().total
                    )
                }
            }
        }
    }
}

data class ProductListUiState(
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val cartCount: Int = 0,
    val cartTotal: Double = 0.0
)