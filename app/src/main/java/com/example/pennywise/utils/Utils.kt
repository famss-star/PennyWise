package com.example.pennywise.utils

fun formatCurrency(amount: Double): String {
    return "Rp ${"%,.0f".format(amount).replace(",", ".")}"
}
