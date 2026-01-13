package com.example.pennywise.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    
    @POST("api_pennywise.php")
    suspend fun createTransaction(
        @Query("action") action: String = "create",
        @Body request: CreateTransactionRequest
    ): Response<ApiResponse<TransactionResponse>>
    
    @GET("api_pennywise.php")
    suspend fun getAllTransactions(
        @Query("action") action: String = "read_all"
    ): Response<List<TransactionResponse>>
    
    @POST("api_pennywise.php")
    suspend fun updateTransaction(
        @Query("action") action: String = "update",
        @Body request: UpdateTransactionRequest
    ): Response<ApiResponse<TransactionResponse>>
    
    @POST("api_pennywise.php")
    suspend fun deleteTransaction(
        @Query("action") action: String = "delete",
        @Body request: DeleteTransactionRequest
    ): Response<ApiResponse<Any>>
}
