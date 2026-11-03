package com.offlinepay.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.offlinepay.app.presentation.TransactionViewModel
import com.offlinepay.app.presentation.history.TransactionHistoryScreen
import com.offlinepay.app.presentation.payment.PaymentDetailsScreen
import com.offlinepay.app.presentation.scanner.QrScannerScreen
import com.offlinepay.app.presentation.settings.SettingsScreen
import com.offlinepay.app.presentation.splash.SplashScreen
import com.offlinepay.app.presentation.status.PaymentStatusScreen
import com.offlinepay.app.presentation.ussd.UssdAssistantScreen
import com.offlinepay.app.ui.dashboard.DashboardScreen

@Composable
fun OfflinePayNavGraph(
    navController: NavHostController,
    viewModel: TransactionViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = viewModel,
                onScanClick = { navController.navigate(Screen.QrScanner.route) },
                onHistoryClick = { navController.navigate(Screen.TransactionHistory.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onTransactionClick = { txId ->
                    navController.navigate(Screen.PaymentStatus.route.replace("{transactionId}", txId.toString()))
                }
            )
        }

        composable(Screen.QrScanner.route) {
            QrScannerScreen(
                viewModel = viewModel,
                onQrScanned = {
                    navController.navigate(Screen.PaymentDetails.route)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.PaymentDetails.route) {
            PaymentDetailsScreen(
                viewModel = viewModel,
                onProceedToUssd = { txId ->
                    navController.navigate(Screen.UssdAssistant.route.replace("{transactionId}", txId.toString())) {
                        popUpTo(Screen.Dashboard.route)
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.UssdAssistant.route,
            arguments = listOf(navArgument("transactionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: 0L
            UssdAssistantScreen(
                viewModel = viewModel,
                transactionId = transactionId,
                onFinishedUssd = {
                    navController.navigate(Screen.PaymentStatus.route.replace("{transactionId}", transactionId.toString())) {
                        popUpTo(Screen.UssdAssistant.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.PaymentStatus.route,
            arguments = listOf(navArgument("transactionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: 0L
            PaymentStatusScreen(
                viewModel = viewModel,
                transactionId = transactionId,
                onDone = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.TransactionHistory.route) {
            TransactionHistoryScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
