package com.example.pennywise.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pennywise.ui.screens.AddTransactionScreen
import com.example.pennywise.ui.screens.DashboardScreen
import com.example.pennywise.ui.screens.ReportsScreen
import com.example.pennywise.ui.screens.SettingsScreen
import com.example.pennywise.ui.screens.TransactionsScreen
import com.example.pennywise.viewmodel.TransactionViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: TransactionViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Screen.Transactions.route) {
            TransactionsScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Screen.AddTransaction.route) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId")
            AddTransactionScreen(
                navController = navController,
                viewModel = viewModel,
                transactionId = transactionId
            )
        }
        composable(Screen.Reports.route) {
            ReportsScreen(viewModel = viewModel)
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
