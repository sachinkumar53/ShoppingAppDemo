package com.sachin.shopping.demo.ui.shoppingcart

import androidx.lifecycle.ViewModel
import com.sachin.shopping.demo.data.local.projection.CartLine
import com.sachin.shopping.demo.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel(), OrbitContainerHost<CartUiState, CartUiState, Nothing> {
    private val quantityMutex = Mutex()
    override val container = orbitContainer<CartUiState, Nothing>(CartUiState()) {
        intent {
            cartRepository.lines.collect { lines ->
                reduce {
                    state.copy(
                        items = lines,
                        summary = lines.toSummary(),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onClearCartClick() = intent {
        cartRepository.clear()
    }

    fun onQuantityChange(productId: Int, qty: Int) = intent {
        quantityMutex.withLock {
            cartRepository.setQuantity(productId, qty)
        }
    }
}

data class CartUiState(
    val isLoading: Boolean = true,
    val items: List<CartLine> = emptyList(),
    val summary: CartSummary = CartSummary(0, 0.0, 0.0)
)

data class CartSummary(
    val unitCount: Int,
    val subtotal: Double,    // sum of original prices x qty
    val discount: Double     // sum of savings x qty
) {
    val total: Double get() = (subtotal - discount).round2()
    val hasDiscount: Boolean get() = discount > 0.0
}

private fun Double.round2(): Double = Math.round(this * 100) / 100.0

fun List<CartLine>.toSummary(): CartSummary {
    var units = 0
    var subtotal = 0.0
    var discount = 0.0
    for (line in this) {
        val unitPrice = (line.price * (1 - line.discountPercentage / 100)).round2()
        units += line.quantity
        subtotal += line.price * line.quantity
        discount += (line.price - unitPrice) * line.quantity
    }
    return CartSummary(units, subtotal.round2(), discount.round2())
}
