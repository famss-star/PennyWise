package com.example.pennywise.data.remote

import com.google.gson.annotations.SerializedName

// Request Models
data class CreateTransactionRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("transaction_date")
    val transactionDate: String, // Format: DD-MM-YYYY
    @SerializedName("transaction_time")
    val transactionTime: String, // Format: HH:mm:ss
    @SerializedName("total_amount")
    val totalAmount: Double,
    @SerializedName("items")
    val items: List<ItemRequest>
)

data class UpdateTransactionRequest(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("transaction_date")
    val transactionDate: String,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("total_amount")
    val totalAmount: Double,
    @SerializedName("items")
    val items: List<ItemRequest>
)

data class DeleteTransactionRequest(
    @SerializedName("id")
    val id: String
)

data class ItemRequest(
    @SerializedName("item_name")
    val itemName: String,
    @SerializedName("price")
    val price: Double
)

// Response Models
data class ApiResponse<T>(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T?
)

data class TransactionResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("transaction_date")
    val transactionDate: String,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("total_amount")
    val totalAmount: Double,
    @SerializedName("note")
    val note: String?,
    @SerializedName("items")
    val items: List<ItemResponse>
)

data class ItemResponse(
    @SerializedName("item_name")
    val itemName: String,
    @SerializedName("price")
    val price: Double
)
