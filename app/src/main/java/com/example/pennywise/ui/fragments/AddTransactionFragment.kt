package com.example.pennywise.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pennywise.R
import com.example.pennywise.databinding.FragmentAddTransactionBinding
import com.example.pennywise.databinding.ItemExpenseInputBinding
import com.example.pennywise.data.ExpenseItem
import com.example.pennywise.data.Transaction
import com.example.pennywise.utils.formatCurrency
import com.example.pennywise.viewmodel.TransactionViewModel
import com.example.pennywise.viewmodel.UiState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class AddTransactionFragment : Fragment() {
    
    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: TransactionViewModel by activityViewModels()
    private val expenseItems = mutableListOf<View>()
    private var selectedDate: LocalDate = LocalDate.now()
    private var selectedTime: LocalTime = LocalTime.now()
    private var transactionId: String? = null
    private var editingTransaction: Transaction? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        transactionId = arguments?.getString("transactionId")
        
        if (transactionId != null) {
            // Edit mode
            loadTransactionData()
        } else {
            // Add mode
            addExpenseItem()
        }
        
        setupButtons()
        updateDateTimeDisplay()
    }
    
    private fun loadTransactionData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.transactions.collect { state ->
                if (state is UiState.Success) {
                    editingTransaction = state.data.find { it.id == transactionId }
                    editingTransaction?.let { transaction ->
                        binding.etTitle.setText(transaction.title)
                        binding.etNote.setText(transaction.note)
                        selectedDate = transaction.date
                        selectedTime = transaction.time
                        updateDateTimeDisplay()
                        
                        // Load items
                        transaction.items.forEach { item ->
                            addExpenseItem(item.name, item.amount)
                        }
                    }
                }
            }
        }
    }
    
    private fun setupButtons() {
        binding.btnAddItem.setOnClickListener {
            addExpenseItem()
        }
        
        binding.btnDate.setOnClickListener {
            showDatePicker()
        }
        
        binding.btnTime.setOnClickListener {
            showTimePicker()
        }
        
        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.btnSave.setOnClickListener {
            saveTransaction()
        }
    }
    
    private fun addExpenseItem(name: String = "", amount: Double = 0.0) {
        val itemBinding = ItemExpenseInputBinding.inflate(
            LayoutInflater.from(requireContext()),
            binding.layoutItems,
            false
        )
        
        if (name.isNotEmpty()) {
            itemBinding.etItemName.setText(name)
            itemBinding.etItemAmount.setText(amount.toString())
        }
        
        // Add text watchers for auto-calculation
        itemBinding.etItemAmount.addTextChangedListener {
            calculateTotal()
        }
        
        itemBinding.btnRemove.setOnClickListener {
            binding.layoutItems.removeView(itemBinding.root)
            expenseItems.remove(itemBinding.root)
            calculateTotal()
        }
        
        binding.layoutItems.addView(itemBinding.root)
        expenseItems.add(itemBinding.root)
        
        calculateTotal()
    }
    
    private fun calculateTotal() {
        var total = 0.0
        
        for (i in 0 until binding.layoutItems.childCount) {
            val itemView = binding.layoutItems.getChildAt(i)
            val itemBinding = ItemExpenseInputBinding.bind(itemView)
            val amountText = itemBinding.etItemAmount.text.toString()
            
            if (amountText.isNotEmpty()) {
                total += amountText.toDoubleOrNull() ?: 0.0
            }
        }
        
        binding.tvTotal.text = formatCurrency(total)
    }
    
    private fun showDatePicker() {
        val calendar = Calendar.getInstance().apply {
            set(selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth)
        }
        
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                updateDateTimeDisplay()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    
    private fun showTimePicker() {
        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                selectedTime = LocalTime.of(hourOfDay, minute)
                updateDateTimeDisplay()
            },
            selectedTime.hour,
            selectedTime.minute,
            true
        ).show()
    }
    
    private fun updateDateTimeDisplay() {
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
        
        binding.btnDate.text = selectedDate.format(dateFormat)
        binding.btnTime.text = selectedTime.format(timeFormat)
    }
    
    private fun saveTransaction() {
        val title = binding.etTitle.text.toString()
        val note = binding.etNote.text.toString()
        
        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Title tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Collect items
        val items = mutableListOf<ExpenseItem>()
        var hasEmptyItem = false
        
        for (i in 0 until binding.layoutItems.childCount) {
            val itemView = binding.layoutItems.getChildAt(i)
            val itemBinding = ItemExpenseInputBinding.bind(itemView)
            
            val itemName = itemBinding.etItemName.text.toString()
            val amountText = itemBinding.etItemAmount.text.toString()
            
            if (itemName.isEmpty() || amountText.isEmpty()) {
                hasEmptyItem = true
                break
            }
            
            val amount = amountText.toDoubleOrNull() ?: 0.0
            items.add(ExpenseItem(name = itemName, amount = amount))
        }
        
        if (hasEmptyItem) {
            Toast.makeText(requireContext(), "Lengkapi semua item", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (items.isEmpty()) {
            Toast.makeText(requireContext(), "Tambahkan minimal 1 item", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Calculate total
        val totalAmount = items.sumOf { it.amount }
        
        viewLifecycleOwner.lifecycleScope.launch {
            if (transactionId != null) {
                // Update existing
                val updatedTransaction = Transaction(
                    id = transactionId!!,
                    title = title,
                    items = items,
                    date = selectedDate,
                    time = selectedTime,
                    note = note
                )
                viewModel.updateTransaction(updatedTransaction)
            } else {
                // Create new
                val newTransaction = Transaction(
                    id = "", // Will be set by API
                    title = title,
                    items = items,
                    date = selectedDate,
                    time = selectedTime,
                    note = note
                )
                viewModel.createTransaction(newTransaction)
            }
            
            Toast.makeText(requireContext(), "Transaksi berhasil disimpan", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

