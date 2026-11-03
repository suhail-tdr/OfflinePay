package com.offlinepay.app.presentation.status

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.offlinepay.app.presentation.TransactionViewModel
import com.offlinepay.app.ui.theme.*
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentStatusScreen(
    viewModel: TransactionViewModel,
    transactionId: Long,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    val transactions by viewModel.allTransactions.collectAsState()

    val transaction = remember(transactions, transactionId) {
        transactions.find { it.transactionId == transactionId }
    }

    val merchantName = transaction?.merchantName ?: "Merchant"
    val upiId = transaction?.upiId ?: ""
    val amount = transaction?.amount ?: "0"

    fun shareImageReceipt() {
        try {
            val uri = createReceiptImageUri(context, merchantName, upiId, amount)
            if (uri != null) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share Receipt Image Via"))
            } else {
                Toast.makeText(context, "Could not generate receipt image", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error sharing receipt image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment Status", color = MaterialTheme.colorScheme.onBackground) },
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
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Record Payment Outcome",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = merchantName, color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "UPI: $upiId", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                        Text(text = "₹$amount", color = MaterialTheme.colorScheme.primary, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.updateTransactionStatus(transactionId, "SUCCESS_REPORTED", "User Reported")
                        onDone()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen, contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(text = "Mark Success Reported", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        viewModel.updateTransactionStatus(transactionId, "FAILED_REPORTED", "User Reported")
                        onDone()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed, contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(text = "Mark Failed Reported", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { shareImageReceipt() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Share Receipt Image", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Text(
                text = "Note: OfflinePay does not independently verify bank transactions. Please verify SMS or bank confirmation.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun createReceiptImageUri(context: Context, merchant: String, upi: String, amount: String): Uri? {
    val width = 800
    val height = 900
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Dark Background
    val bgPaint = Paint().apply { color = android.graphics.Color.parseColor("#000000") }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

    // Card Background
    val cardPaint = Paint().apply { color = android.graphics.Color.parseColor("#121212") }
    canvas.drawRoundRect(40f, 40f, 760f, 860f, 32f, 32f, cardPaint)

    // Title
    val titlePaint = Paint().apply {
        color = android.graphics.Color.parseColor("#34A853")
        textSize = 44f
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }
    canvas.drawText("OfflinePay Receipt", 80f, 130f, titlePaint)

    // Line
    val linePaint = Paint().apply {
        color = android.graphics.Color.parseColor("#26262C")
        strokeWidth = 3f
    }
    canvas.drawLine(80f, 160f, 720f, 160f, linePaint)

    // Labels & Values
    val labelPaint = Paint().apply {
        color = android.graphics.Color.parseColor("#94A3B8")
        textSize = 28f
        isAntiAlias = true
    }
    val valuePaint = Paint().apply {
        color = android.graphics.Color.parseColor("#F8FAFC")
        textSize = 36f
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }

    canvas.drawText("MERCHANT", 80f, 240f, labelPaint)
    canvas.drawText(merchant, 80f, 290f, valuePaint)

    canvas.drawText("UPI ID", 80f, 380f, labelPaint)
    canvas.drawText(upi, 80f, 430f, valuePaint)

    val amountPaint = Paint().apply {
        color = android.graphics.Color.parseColor("#34A853")
        textSize = 56f
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }
    canvas.drawText("AMOUNT", 80f, 530f, labelPaint)
    canvas.drawText("₹$amount", 80f, 600f, amountPaint)

    canvas.drawText("METHOD", 80f, 700f, labelPaint)
    canvas.drawText("Offline USSD (*99#)", 80f, 750f, valuePaint)

    // Save
    val cachePath = File(context.cacheDir, "images")
    cachePath.mkdirs()
    val file = File(cachePath, "payment_receipt_${System.currentTimeMillis()}.png")
    FileOutputStream(file).use { stream ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    }

    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}
