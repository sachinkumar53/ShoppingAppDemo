package com.sachin.shopping.demo.ui.productlist

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.sachin.shopping.demo.data.model.ProductListing
import com.sachin.shopping.demo.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel(), OrbitContainerHost<ProductListUiState, ProductListUiState, Nothing> {
    override val container = orbitContainer<ProductListUiState, Nothing>(
        initialState = ProductListUiState()
    ) {
        intent {
            reduce { state.copy(isLoading = true) }
            val products = repository.getAllProducts()
            reduce {
                state.copy(
                    isLoading = false,
                    products = products
                )
            }
        }
    }
}


@Immutable
data class ProductListUiState(
    val isLoading: Boolean = false,
    val products: List<ProductListing> = emptyList()
)