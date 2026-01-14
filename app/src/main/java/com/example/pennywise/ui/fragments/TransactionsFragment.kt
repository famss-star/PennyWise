package com.example.pennywise.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pennywise.R
import com.example.pennywise.data.Transaction
import com.example.pennywise.databinding.FragmentTransactionsBinding
import com.example.pennywise.ui.adapters.TransactionAdapter
import com.example.pennywise.utils.formatCurrency
import com.example.pennywise.viewmodel.TransactionViewModel
import com.example.pennywise.viewmodel.UiState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class TransactionsFragment : Fragment() {
    
    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: TransactionViewModel by activityViewModels()
    private lateinit var adapter: TransactionAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeTransactions()
        
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.addTransactionFragment)
        }
    }
    
    private fun setupRecyclerView() {
        adapter = TransactionAdapter { transaction ->
            showTransactionDetail(transaction)
        }
        
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactions.adapter = adapter
    }
    
    private fun showTransactionDetail(transaction: Transaction) {
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        
        val itemsText = transaction.items.joinToString("\n") { item ->
            "• ${item.name}: ${formatCurrency(item.amount)}"
        }
        
        val message = buildString {
            append("Tanggal: ${transaction.date.format(dateFormatter)}\n")
            append("Waktu: ${transaction.time.format(timeFormatter)}\n\n")
            append("Item:\n$itemsText\n\n")
            append("Total: ${formatCurrency(transaction.totalAmount)}\n")
            if (transaction.note.isNotEmpty()) {
                append("\nCatatan:\n${transaction.note}")
            }
        }
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(transaction.title)
            .setMessage(message)
            .setPositiveButton("Tutup", null)
            .setNegativeButton("Hapus") { _, _ ->
                showDeleteConfirmation(transaction)
            }
            .setNeutralButton("Edit") { _, _ ->
                val bundle = Bundle().apply {
                    putString("transactionId", transaction.id)
                }
                findNavController().navigate(R.id.addTransactionFragment, bundle)
            }
            .show()
    }
    
    private fun showDeleteConfirmation(transaction: Transaction) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Hapus Transaksi")
            .setMessage("Yakin ingin menghapus transaksi \"${transaction.title}\"?")
            .setPositiveButton("Hapus") { _, _ ->
                viewModel.deleteTransaction(transaction.id)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
    
    private fun observeTransactions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.transactions.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.layoutEmpty.isVisible = false
                    }
                    is UiState.Success -> {
                        binding.progressBar.isVisible = false
                        val transactions = state.data
                        
                        if (transactions.isEmpty()) {
                            binding.layoutEmpty.isVisible = true
                            binding.rvTransactions.isVisible = false
                        } else {
                            binding.layoutEmpty.isVisible = false
                            binding.rvTransactions.isVisible = true
                            adapter.submitList(transactions)
                        }
                    }
                    is UiState.Error -> {
                        binding.progressBar.isVisible = false
                        binding.layoutEmpty.isVisible = true
                    }
                    else -> {}
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
