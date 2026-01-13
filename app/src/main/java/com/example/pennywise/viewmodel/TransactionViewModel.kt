package com.example.pennywise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennywise.data.Transaction
import com.example.pennywise.data.repository.TransactionRepository
import com.example.pennywise.data.toCreateRequest
import com.example.pennywise.data.toTransaction
import com.example.pennywise.data.toUpdateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class TransactionViewModel : ViewModel() {
    private val repository = TransactionRepository()
    
    private val _transactions = MutableStateFlow<UiState<List<Transaction>>>(UiState.Idle)
    val transactions: StateFlow<UiState<List<Transaction>>> = _transactions.asStateFlow()
    
    private val _operationState = MutableStateFlow<UiState<Boolean>>(UiState.Idle)
    val operationState: StateFlow<UiState<Boolean>> = _operationState.asStateFlow()
    
    init {
        loadTransactions()
    }
    
    fun loadTransactions() {
        viewModelScope.launch {
            _transactions.value = UiState.Loading
            val result = repository.getAllTransactions()
            _transactions.value = result.fold(
                onSuccess = { response ->
                    UiState.Success(response.map { it.toTransaction() })
                },
                onFailure = { error ->
                    UiState.Error(error.message ?: "Gagal memuat data")
                }
            )
        }
    }
    
    fun createTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _operationState.value = UiState.Loading
            val result = repository.createTransaction(transaction.toCreateRequest())
            _operationState.value = result.fold(
                onSuccess = {
                    loadTransactions() // Reload data
                    UiState.Success(true)
                },
                onFailure = { error ->
                    UiState.Error(error.message ?: "Gagal menyimpan transaksi")
                }
            )
        }
    }
    
    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _operationState.value = UiState.Loading
            val result = repository.updateTransaction(transaction.toUpdateRequest())
            _operationState.value = result.fold(
                onSuccess = {
                    loadTransactions() // Reload data
                    UiState.Success(true)
                },
                onFailure = { error ->
                    UiState.Error(error.message ?: "Gagal update transaksi")
                }
            )
        }
    }
    
    fun deleteTransaction(id: String) {
        viewModelScope.launch {
            _operationState.value = UiState.Loading
            val result = repository.deleteTransaction(id)
            _operationState.value = result.fold(
                onSuccess = {
                    loadTransactions() // Reload data
                    UiState.Success(true)
                },
                onFailure = { error ->
                    UiState.Error(error.message ?: "Gagal menghapus transaksi")
                }
            )
        }
    }
    
    fun resetOperationState() {
        _operationState.value = UiState.Idle
    }
    
    fun getTransactionById(id: String): Transaction? {
        return (_transactions.value as? UiState.Success)?.data?.find { it.id == id }
    }
}
