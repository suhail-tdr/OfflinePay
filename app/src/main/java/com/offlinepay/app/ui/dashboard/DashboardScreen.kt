package com.offlinepay.app.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.offlinepay.app.presentation.TransactionViewModel
import com.offlinepay.app.ui.components.*
import com.offlinepay.app.ui.theme.*
import com.offlinepay.app.util.rememberIsConnected
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: TransactionViewModel,
    onScanClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onTransactionClick: (Long) -> Unit
) {
    val isConnected = rememberIsConnected()
    val transactions by viewModel.allTransactions.collectAsState()
    val totalCount = transactions.size
    val dateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    OfflinePayHeader(
                        isConnected = isConnected,
                        onSettingsClick = onSettingsClick,
                        onProfileClick = onSettingsClick
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Balance / Vault Overview Card
            item {
                BalanceCard(
                    balance = "$totalCount Transactions",
                    offlineKeyStatus = "Active (USSD *99#)"
                )
            }

            // Primary Scan QR Button
            item {
                ScanQrButton(onClick = onScanClick)
            }

            // Recent Transactions Header with View All action
            item {
                Spacer(modifier = Modifier.height(4.dp))
                SectionHeader(
                    title = "Recent Transactions",
                    actionText = "View All",
                    onActionClick = onHistoryClick
                )
            }

            if (transactions.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "No offline payments recorded yet",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Scan a QR code or enter a UPI ID to make a secure offline payment.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                // Box / Card container displaying the last 4 transactions with decreased corner radius
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            transactions.take(4).forEachIndexed { index, tx ->
                                TransactionRow(
                                    transaction = TransactionItemData(
                                        id = tx.transactionId.toString(),
                                        merchantOrName = tx.merchantName,
                                        category = "UPI Offline",
                                        amount = "₹${tx.amount ?: "0"}",
                                        timestamp = dateFormat.format(Date(tx.createdAt)),
                                        isIncoming = false,
                                        isOfflineVerified = tx.transactionStatus == "SUCCESS_REPORTED"
                                    ),
                                    onClick = { onTransactionClick(tx.transactionId) }
                                )
                                if (index < transactions.take(4).size - 1) {
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
