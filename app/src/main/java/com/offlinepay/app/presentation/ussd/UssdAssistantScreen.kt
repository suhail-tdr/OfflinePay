package com.offlinepay.app.presentation.ussd

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.offlinepay.app.presentation.TransactionViewModel
import com.offlinepay.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UssdAssistantScreen(
    viewModel: TransactionViewModel,
    transactionId: Long,
    onFinishedUssd: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val transactions by viewModel.allTransactions.collectAsState()
    val preferredSim by viewModel.preferredSimSlot.collectAsState()

    val transaction = remember(transactions, transactionId) {
        transactions.find { it.transactionId == transactionId }
    }

    val merchantName = transaction?.merchantName ?: "Merchant"
    val upiId = transaction?.upiId ?: ""
    val amount = transaction?.amount ?: "0"

    // SIM / Operator Detection
    val carrierName = remember {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            val name = tm?.networkOperatorName
            if (!name.isNullOrBlank()) name else "Cellular SIM"
        } catch (_: Exception) {
            "Cellular SIM"
        }
    }

    val simLabel = "$carrierName (SIM $preferredSim)"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("USSD Payment Assistant (*99#)", color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Payment Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Payment Summary", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.SimCard, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(12.dp))
                                    Text(text = simLabel, color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Text(text = "Merchant: $merchantName", color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                        Text(text = "UPI ID: $upiId", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                        Text(text = "Amount: ₹$amount", color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Step-by-Step Instructions
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(text = "Step-by-Step USSD Instructions", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(text = "1. Active Line: $simLabel", color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                        Text(text = "2. Tap below to launch *99# USSD dialer.", color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                        Text(text = "3. Select Send Money -> UPI ID.", color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                        Text(text = "4. Enter Payee UPI ID: $upiId", color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                        Text(text = "5. Enter Amount: ₹$amount", color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                        Text(text = "6. Enter your UPI PIN *only* in the official bank USSD screen.", color = ErrorRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        viewModel.updateTransactionStatus(transactionId, "INITIATED")
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:*99%23"))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Could not open dialer", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Open Dialer (*99#)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        viewModel.updateTransactionStatus(transactionId, "PENDING_VERIFICATION")
                        onFinishedUssd()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "I Have Completed USSD Session", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
