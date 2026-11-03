package com.offlinepay.app.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")
    object QrScanner : Screen("qr_scanner")
    object PaymentDetails : Screen("payment_details")
    object UssdAssistant : Screen("ussd_assistant/{transactionId}") {
        fun createRoute(transactionId: Long) = "ussd_assistant/$transactionId"
    }
    object PaymentStatus : Screen("payment_status/{transactionId}") {
        fun createRoute(transactionId: Long) = "payment_status/$transactionId"
    }
    object TransactionHistory : Screen("transaction_history")
    object Settings : Screen("settings")
}
