package com.offlinepay.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.offlinepay.app.navigation.OfflinePayNavGraph
import com.offlinepay.app.presentation.AppTheme
import com.offlinepay.app.presentation.TransactionViewModel
import com.offlinepay.app.ui.theme.OfflinePayTheme

class MainActivity : FragmentActivity() {
    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by viewModel.currentTheme.collectAsState()
            val isDark = when (themeMode) {
                AppTheme.DARK -> true
                AppTheme.LIGHT -> false
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }
            val navController = rememberNavController()

            OfflinePayTheme(darkTheme = isDark) {
                OfflinePayNavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
