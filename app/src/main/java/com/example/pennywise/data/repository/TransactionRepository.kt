package com.example.pennywise.data.repository

import com.example.pennywise.data.remote.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionRepository {
    private val apiService = RetrofitClient.apiService
    
    suspend fun createTransaction(request: CreateTransactionRequest): Result<TransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.createTransaction(request = request)
                if (response.isSuccessful && response.body()?.status == "success") {
                    // Server tidak mengembalikan data untuk create, jadi kita buat dummy response
                    val dummyResponse = TransactionResponse(
                        id = "",
                        title = request.title,
                        transactionDate = request.transactionDate,
                        transactionTime = request.transactionTime,
                        totalAmount = request.totalAmount,
                        note = null,
                        items = request.items.map { ItemResponse(it.itemName, it.price) }
                    )
                    Result.success(dummyResponse)
                } else {
                    Result.failure(Exception(response.body()?.message ?: "Gagal membuat transaksi"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun getAllTransactions(): Result<List<TransactionResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllTransactions()
                println("DEBUG - Response code: ${response.code()}")
                println("DEBUG - Response body: ${response.body()}")
                
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        println("DEBUG - Data size: ${data.size}")
                        Result.success(data)
                    } else {
                        println("DEBUG - Data is null, returning empty list")
                        Result.success(emptyList())
                    }
                } else {
                    val errorMsg = "Gagal mengambil data: ${response.code()}"
                    println("DEBUG - Error: $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                println("DEBUG - Exception: ${e.message}")
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }
    
    suspend fun updateTransaction(request: UpdateTransactionRequest): Result<TransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.updateTransaction(request = request)
                if (response.isSuccessful && response.body()?.status == "success") {
                    // Server tidak mengembalikan data untuk update, jadi kita buat dummy response
                    val dummyResponse = TransactionResponse(
                        id = request.id,
                        title = request.title,
                        transactionDate = request.transactionDate,
                        transactionTime = request.transactionTime,
                        totalAmount = request.totalAmount,
                        note = null,
                        items = request.items.map { ItemResponse(it.itemName, it.price) }
                    )
                    Result.success(dummyResponse)
                } else {
                    Result.failure(Exception(response.body()?.message ?: "Gagal update transaksi"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun deleteTransaction(id: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteTransaction(request = DeleteTransactionRequest(id))
                if (response.isSuccessful && response.body()?.status == "success") {
                    Result.success(true)
                } else {
                    Result.failure(Exception(response.body()?.message ?: "Gagal menghapus transaksi"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
