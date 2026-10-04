package com.sachin.shopping.demo.util

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(price: Double): String =
    NumberFormat.getCurrencyInstance(Locale.US).format(price)