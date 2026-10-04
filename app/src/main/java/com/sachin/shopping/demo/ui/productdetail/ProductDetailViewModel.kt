package com.sachin.shopping.demo.ui.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.sachin.shopping.demo.data.model.Product
import com.sachin.shopping.demo.data.repository.ProductRepository
import com.sachin.shopping.demo.ui.navigation.ProductDetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    productRepository: ProductRepository
) : ViewModel(), OrbitContainerHost<ProductDetailState, ProductDetailState, Nothing> {
    private val args = savedStateHandle.toRoute<ProductDetailRoute>()
    override val container = orbitContainer<ProductDetailState, Nothing>(
        ProductDetailState(isLoading = true)
    ) {
        intent {
            val product = productRepository.getProductDetail(args.productId)
            reduce {
                state.copy(
                    product = product,
                    isLoading = false
                )
            }
        }
    }
}

data class ProductDetailState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)