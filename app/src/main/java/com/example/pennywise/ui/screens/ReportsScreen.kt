package com.example.pennywise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pennywise.data.Transaction
import com.example.pennywise.ui.theme.ExpenseColor
import com.example.pennywise.ui.theme.PennyWiseTheme
import com.example.pennywise.utils.formatCurrency
import com.example.pennywise.viewmodel.TransactionViewModel
import com.example.pennywise.viewmodel.UiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(viewModel: TransactionViewModel = viewModel()) {
    var selectedPeriod by remember { mutableStateOf("Bulan Ini") }
    val transactionsState by viewModel.transactions.collectAsState()
    
    val allTransactions = when (transactionsState) {
        is UiState.Success -> (transactionsState as UiState.Success<List<Transaction>>).data
        else -> emptyList()
    }
    
    // Filter transactions based on selected period
    val transactions = remember(allTransactions, selectedPeriod) {
        val now = LocalDate.now()
        when (selectedPeriod) {
            "Minggu Ini" -> {
                val startOfWeek = now.minusDays(now.dayOfWeek.value.toLong() - 1)
                allTransactions.filter { it.date >= startOfWeek && it.date <= now }
            }
            "Bulan Ini" -> {
                allTransactions.filter { 
                    it.date.year == now.year && it.date.monthValue == now.monthValue 
                }
            }
            "Tahun Ini" -> {
                allTransactions.filter { it.date.year == now.year }
            }
            else -> allTransactions
        }
    }
    
    val isLoading = transactionsState is UiState.Loading
    val totalExpense = transactions.sumOf { it.totalAmount }
    val totalTransactions = transactions.size
    val averagePerTransaction = if (totalTransactions > 0) totalExpense / totalTransactions else 0.0
    
    // Chart data based on period
    val chartData = remember(transactions, selectedPeriod) {
        when (selectedPeriod) {
            "Minggu Ini" -> {
                // Last 7 days
                (0..6).map { daysAgo ->
                    val date = LocalDate.now().minusDays(daysAgo.toLong())
                    val amount = transactions
                        .filter { it.date == date }
                        .sumOf { it.totalAmount }
                    date to amount
                }.reversed()
            }
            "Bulan Ini" -> {
                // Group by week (4 weeks)
                val now = LocalDate.now()
                (0..3).map { weekAgo ->
                    val endDate = now.minusWeeks(weekAgo.toLong())
                    val startDate = endDate.minusDays(6)
                    val amount = transactions
                        .filter { it.date >= startDate && it.date <= endDate }
                        .sumOf { it.totalAmount }
                    endDate to amount
                }.reversed()
            }
            "Tahun Ini" -> {
                // Last 12 months
                val now = LocalDate.now()
                (0..11).map { monthAgo ->
                    val date = now.minusMonths(monthAgo.toLong())
                    val amount = transactions
                        .filter { it.date.year == date.year && it.date.monthValue == date.monthValue }
                        .sumOf { it.totalAmount }
                    date to amount
                }.reversed()
            }
            else -> emptyList()
        }
    }
    
    val chartLabel = when (selectedPeriod) {
        "Minggu Ini" -> "Pengeluaran 7 Hari Terakhir"
        "Bulan Ini" -> "Pengeluaran 4 Minggu Terakhir"
        "Tahun Ini" -> "Pengeluaran 12 Bulan Terakhir"
        else -> "Pengeluaran"
    }
    
    val averageLabel = when (selectedPeriod) {
        "Minggu Ini" -> "Rata-rata Harian"
        "Bulan Ini" -> "Rata-rata Harian"
        "Tahun Ini" -> "Rata-rata Bulanan"
        else -> "Rata-rata"
    }
    
    val averageValue = when (selectedPeriod) {
        "Minggu Ini" -> if (totalExpense > 0) totalExpense / 7 else 0.0
        "Bulan Ini" -> if (totalExpense > 0) totalExpense / 30 else 0.0
        "Tahun Ini" -> if (totalExpense > 0) totalExpense / 12 else 0.0
        else -> 0.0
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Laporan") },
                actions = {
                    IconButton(onClick = { viewModel.loadTransactions() }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Period Selector
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = selectedPeriod == "Minggu Ini",
                                onClick = { selectedPeriod = "Minggu Ini" },
                                label = { Text("Minggu") },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = selectedPeriod == "Bulan Ini",
                                onClick = { selectedPeriod = "Bulan Ini" },
                                label = { Text("Bulan") },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = selectedPeriod == "Tahun Ini",
                                onClick = { selectedPeriod = "Tahun Ini" },
                                label = { Text("Tahun") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                
                // Total Expense Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total Pengeluaran",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = formatCurrency(totalExpense),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.TrendingDown,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
                
                // Daily Expense Chart
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = chartLabel,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            SimpleBarChart(
                                data = chartData,
                                period = selectedPeriod,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }
                    }
                }
                
                // Statistics
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Statistik",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            StatisticItem(
                                label = "Jumlah Transaksi",
                                value = "$totalTransactions transaksi"
                            )
                            StatisticItem(
                                label = "Rata-rata per Transaksi",
                                value = formatCurrency(averagePerTransaction)
                            )
                            StatisticItem(
                                label = averageLabel,
                                value = formatCurrency(averageValue)
                            )
                        }
                    }
                }
            }
            
            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun SimpleBarChart(
    data: List<Pair<LocalDate, Double>>,
    period: String,
    modifier: Modifier = Modifier
) {
    val maxValue = data.maxOfOrNull { it.second } ?: 1.0
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { (date, amount) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                // Bar
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(
                            if (maxValue > 0) 
                                ((amount / maxValue) * 150).dp.coerceAtLeast(4.dp) 
                            else 4.dp
                        )
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(if (amount > 0) ExpenseColor else MaterialTheme.colorScheme.surfaceVariant)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Date label
                val labelFormat = when (period) {
                    "Minggu Ini" -> "EEE"  // Mon, Tue, etc
                    "Bulan Ini" -> "dd/MM"  // 13/01
                    "Tahun Ini" -> "MMM"  // Jan, Feb, etc
                    else -> "dd/MM"
                }
                Text(
                    text = date.format(DateTimeFormatter.ofPattern(labelFormat)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun StatisticItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ReportsScreenPreview() {
    PennyWiseTheme {
        ReportsScreen()
    }
}
