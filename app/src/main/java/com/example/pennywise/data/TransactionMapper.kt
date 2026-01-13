package com.example.pennywise.data

import com.example.pennywise.data.remote.ItemRequest
import com.example.pennywise.data.remote.ItemResponse
import com.example.pennywise.data.remote.TransactionResponse
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// Extension functions untuk konversi
fun Transaction.toCreateRequest(): com.example.pennywise.data.remote.CreateTransactionRequest {
    return com.example.pennywise.data.remote.CreateTransactionRequest(
        title = title,
        transactionDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        transactionTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
        totalAmount = totalAmount,
        items = items.map { ItemRequest(it.name, it.amount) }
    )
}

fun Transaction.toUpdateRequest(): com.example.pennywise.data.remote.UpdateTransactionRequest {
    return com.example.pennywise.data.remote.UpdateTransactionRequest(
        id = id,
        title = title,
        transactionDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        transactionTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
        totalAmount = totalAmount,
        items = items.map { ItemRequest(it.name, it.amount) }
    )
}

fun TransactionResponse.toTransaction(): Transaction {
    return Transaction(
        id = id,
        title = title,
        items = items.map { ExpenseItem(it.itemName, it.price) },
        date = LocalDate.parse(transactionDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        time = LocalTime.parse(transactionTime, DateTimeFormatter.ofPattern("HH:mm:ss")),
        note = note ?: ""
    )
}
