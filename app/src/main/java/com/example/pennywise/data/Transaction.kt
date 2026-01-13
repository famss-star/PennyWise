package com.example.pennywise.data

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

data class ExpenseItem(
    val name: String,
    val amount: Double
)

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val items: List<ExpenseItem>,
    val date: LocalDate,
    val time: LocalTime,
    val note: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    val totalAmount: Double
        get() = items.sumOf { it.amount }
}
