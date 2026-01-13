package com.example.pennywise.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Dashboard.route,
        icon = Icons.Default.Home,
        label = "Dashboard"
    ),
    BottomNavItem(
        route = Screen.Transactions.route,
        icon = Icons.Default.Receipt,
        label = "Transaksi"
    ),
    BottomNavItem(
        route = Screen.Reports.route,
        icon = Icons.Default.BarChart,
        label = "Laporan"
    ),
    BottomNavItem(
        route = Screen.Settings.route,
        icon = Icons.Default.Settings,
        label = "Pengaturan"
    )
)
