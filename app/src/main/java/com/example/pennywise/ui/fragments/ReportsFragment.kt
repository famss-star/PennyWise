package com.example.pennywise.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.pennywise.R
import com.example.pennywise.databinding.FragmentReportsBinding
import com.example.pennywise.data.Transaction
import com.example.pennywise.utils.formatCurrency
import com.example.pennywise.viewmodel.TransactionViewModel
import com.example.pennywise.viewmodel.UiState
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReportsFragment : Fragment() {
    
    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: TransactionViewModel by activityViewModels()
    private var allTransactions: List<Transaction> = emptyList()
    private var selectedPeriod: Period = Period.WEEK
    
    enum class Period {
        WEEK, MONTH, YEAR
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupChipGroup()
        observeTransactions()
    }
    
    private fun setupChipGroup() {
        binding.chipWeek.setOnClickListener {
            selectPeriod(Period.WEEK)
        }
        
        binding.chipMonth.setOnClickListener {
            selectPeriod(Period.MONTH)
        }
        
        binding.chipYear.setOnClickListener {
            selectPeriod(Period.YEAR)
        }
    }
    
    private fun selectPeriod(period: Period) {
        selectedPeriod = period
        
        // Update chip selection
        binding.chipWeek.isChecked = period == Period.WEEK
        binding.chipMonth.isChecked = period == Period.MONTH
        binding.chipYear.isChecked = period == Period.YEAR
        
        updateStatistics()
    }
    
    private fun observeTransactions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.transactions.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is UiState.Success -> {
                        binding.progressBar.isVisible = false
                        allTransactions = state.data
                        updateStatistics()
                    }
                    is UiState.Error -> {
                        binding.progressBar.isVisible = false
                    }
                    else -> {}
                }
            }
        }
    }
    
    private fun updateStatistics() {
        val filteredTransactions = filterTransactionsByPeriod()
        
        if (filteredTransactions.isEmpty()) {
            binding.tvTotalExpense.text = formatCurrency(0.0)
            binding.tvTransactionCount.text = "0"
            binding.tvAveragePerTransaction.text = formatCurrency(0.0)
            binding.tvAverageValue.text = formatCurrency(0.0)
            binding.barChart.setData(emptyList())
            return
        }
        
        val totalExpense = filteredTransactions.sumOf { it.totalAmount }
        val transactionCount = filteredTransactions.size
        val averageExpense = totalExpense / transactionCount
        
        binding.tvTotalExpense.text = formatCurrency(totalExpense)
        binding.tvTransactionCount.text = transactionCount.toString()
        binding.tvAveragePerTransaction.text = formatCurrency(averageExpense)
        
        // Calculate daily average based on period
        val days = when (selectedPeriod) {
            Period.WEEK -> 7
            Period.MONTH -> 30
            Period.YEAR -> 365
        }
        val dailyAverage = totalExpense / days
        binding.tvAverageValue.text = formatCurrency(dailyAverage)
        
        // Update chart
        updateChart(filteredTransactions)
    }
    
    private fun updateChart(transactions: List<Transaction>) {
        val chartData = when (selectedPeriod) {
            Period.WEEK -> getWeeklyChartData(transactions)
            Period.MONTH -> getMonthlyChartData(transactions)
            Period.YEAR -> getYearlyChartData(transactions)
        }
        
        // Update chart title
        val title = when (selectedPeriod) {
            Period.WEEK -> "Pengeluaran 7 Hari Terakhir"
            Period.MONTH -> "Pengeluaran Per Minggu"
            Period.YEAR -> "Pengeluaran Per Bulan"
        }
        binding.tvChartTitle.text = title
        binding.barChart.setData(chartData)
    }
    
    private fun getWeeklyChartData(transactions: List<Transaction>): List<Pair<String, Double>> {
        val currentDate = LocalDate.now()
        val data = mutableListOf<Pair<String, Double>>()
        
        for (i in 6 downTo 0) {
            val date = currentDate.minusDays(i.toLong())
            val dayTransactions = transactions.filter { it.date == date }
            val total = dayTransactions.sumOf { it.totalAmount }
            val label = date.format(DateTimeFormatter.ofPattern("EEE"))
            data.add(label to total)
        }
        
        return data
    }
    
    private fun getMonthlyChartData(transactions: List<Transaction>): List<Pair<String, Double>> {
        val currentDate = LocalDate.now()
        val data = mutableListOf<Pair<String, Double>>()
        
        for (i in 3 downTo 0) {
            val weekStart = currentDate.minusWeeks((i + 1).toLong())
            val weekEnd = currentDate.minusWeeks(i.toLong())
            val weekTransactions = transactions.filter { 
                !it.date.isBefore(weekStart) && it.date.isBefore(weekEnd)
            }
            val total = weekTransactions.sumOf { it.totalAmount }
            data.add("W${4-i}" to total)
        }
        
        return data
    }
    
    private fun getYearlyChartData(transactions: List<Transaction>): List<Pair<String, Double>> {
        val currentDate = LocalDate.now()
        val data = mutableListOf<Pair<String, Double>>()
        
        for (i in 11 downTo 0) {
            val monthDate = currentDate.minusMonths(i.toLong())
            val monthTransactions = transactions.filter { 
                it.date.year == monthDate.year && it.date.monthValue == monthDate.monthValue
            }
            val total = monthTransactions.sumOf { it.totalAmount }
            val label = monthDate.format(DateTimeFormatter.ofPattern("MMM"))
            data.add(label to total)
        }
        
        return data
    }
    
    private fun filterTransactionsByPeriod(): List<Transaction> {
        val currentDate = LocalDate.now()
        val startDate = when (selectedPeriod) {
            Period.WEEK -> currentDate.minusDays(7)
            Period.MONTH -> currentDate.minusMonths(1)
            Period.YEAR -> currentDate.minusYears(1)
        }
        
        return allTransactions.filter { transaction ->
            !transaction.date.isBefore(startDate) && !transaction.date.isAfter(currentDate)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
