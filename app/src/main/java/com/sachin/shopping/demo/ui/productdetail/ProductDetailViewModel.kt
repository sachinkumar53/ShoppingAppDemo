package com.sachin.shopping.demo.ui.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.sachin.shopping.demo.data.model.Product
import com.sachin.shopping.demo.data.repository.CartRepository
import com.sachin.shopping.demo.data.repository.ProductRepository
import com.sachin.shopping.demo.ui.navigation.ProductDetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel(), OrbitContainerHost<ProductDetailState, ProductDetailState, Nothing> {
    private val args = savedStateHandle.toRoute<ProductDetailRoute>()
    private val quantityMutex = Mutex()

    override val container = orbitContainer<ProductDetailState, Nothing>(
        ProductDetailState(isLoading = true)
    ) {
        intent {
            productRepository.observeProduct(args.productId).collectLatest { product ->
                if (product == null) {
                    reduce {
                        state.copy(
                            product = null,
                            error = "No product found with id ${args.productId}"
                        )
                    }
                } else {
                    reduce { state.copy(product = product, error = null) }
                }
            }
        }
        intent {
            val quantity = cartRepository.getQuantity(args.productId)
            reduce {
                state.copy(
                    quantity = quantity ?: 0,
                    isLoading = false
                )
            }

        }

        intent {
            cartRepository.observeQuantity(args.productId).collectLatest { quantity ->
                reduce {
                    state.copy(quantity = quantity ?: 0)
                }
            }
        }

        intent {
            // Refresh the product from server in background
            productRepository.refreshProduct(args.productId)
        }
    }

    fun onQuantityChange(quantity: Int) = intent {
        quantityMutex.withLock {
            cartRepository.setQuantity(args.productId, quantity)
        }
    }

    fun loadProduct() {
        intent {
            reduce { state.copy(error = null, isLoading = true) }
            productRepository.refreshProduct(args.productId).onSuccess {
                reduce { state.copy(error = null, isLoading = false) }
            }.onFailure {
                reduce { state.copy(error = "Failed to load product", isLoading = false) }
            }
        }
    }
}

data class ProductDetailState(
    val product: Product? = null,
    val quantity: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
