package com.example.pennywise.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Transactions : Screen("transactions")
    object AddTransaction : Screen("add_transaction?id={transactionId}") {
        fun createRoute(transactionId: String? = null): String {
            return if (transactionId != null) "add_transaction?id=$transactionId" else "add_transaction"
        }
    }
    object Reports : Screen("reports")
    object Settings : Screen("settings")
}
