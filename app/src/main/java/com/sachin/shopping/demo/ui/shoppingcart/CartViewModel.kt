package com.sachin.shopping.demo.ui.shoppingcart

import androidx.lifecycle.ViewModel
import com.sachin.shopping.demo.data.model.CartItem
import com.sachin.shopping.demo.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel(), OrbitContainerHost<CartUiState, CartUiState, Nothing> {

    override val container = orbitContainer<CartUiState, Nothing>(CartUiState()) {
        intent {
            cartRepository.observeCart().collectLatest {
                reduce {
                    state.copy(items = it)
                }
            }
        }
    }

    fun onClearCartClick() = intent {
        cartRepository.clear()
    }
}

data class CartUiState(
    val items: List<CartItem> = emptyList(),
)
