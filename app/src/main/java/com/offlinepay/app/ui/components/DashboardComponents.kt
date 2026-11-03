package com.offlinepay.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.offlinepay.app.ui.theme.*

data class TransactionItemData(
    val id: String,
    val merchantOrName: String,
    val category: String,
    val amount: String,
    val timestamp: String,
    val isIncoming: Boolean,
    val isOfflineVerified: Boolean
)

enum class BottomTab { HOME, TRANSACTIONS, SETTINGS }

val NavBgColor = Color(0xFF10251B) // Deep green-black
val NavSelectedColor = Color(0xFF00B887) // Emerald green (#00B887)
val NavUnselectedColor = Color(0xFF64748B) // Muted blue-grey
val NavBorderColor = Color(0xFF1C3B2B) // Subtle top border

@Composable
fun OfflinePayLogoIcon(
    modifier: Modifier = Modifier,
    size: Dp = 42.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF00B887)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.55f)) {
            val strokeWidth = 3.dp.toPx()
            val color = Color(0xFF0B1220)
            val w = size.toPx() * 0.55f
            val h = size.toPx() * 0.55f
            val cornerLen = w * 0.28f

            // Top-Left Corner
            drawLine(color, start = Offset(0f, 0f), end = Offset(cornerLen, 0f), strokeWidth = strokeWidth, cap = StrokeCap.Round)
            drawLine(color, start = Offset(0f, 0f), end = Offset(0f, cornerLen), strokeWidth = strokeWidth, cap = StrokeCap.Round)

            // Top-Right Corner
            drawLine(color, start = Offset(w, 0f), end = Offset(w - cornerLen, 0f), strokeWidth = strokeWidth, cap = StrokeCap.Round)
            drawLine(color, start = Offset(w, 0f), end = Offset(w, cornerLen), strokeWidth = strokeWidth, cap = StrokeCap.Round)

            // Bottom-Left Corner
            drawLine(color, start = Offset(0f, h), end = Offset(cornerLen, h), strokeWidth = strokeWidth, cap = StrokeCap.Round)
            drawLine(color, start = Offset(0f, h), end = Offset(0f, h - cornerLen), strokeWidth = strokeWidth, cap = StrokeCap.Round)

            // Bottom-Right Corner
            drawLine(color, start = Offset(w, h), end = Offset(w - cornerLen, h), strokeWidth = strokeWidth, cap = StrokeCap.Round)
            drawLine(color, start = Offset(w, h), end = Offset(w, h - cornerLen), strokeWidth = strokeWidth, cap = StrokeCap.Round)

            // Center Dash Line
            val dashWidth = w * 0.45f
            val startX = (w - dashWidth) / 2f
            drawLine(color, start = Offset(startX, h / 2f), end = Offset(startX + dashWidth, h / 2f), strokeWidth = strokeWidth, cap = StrokeCap.Round)
        }
    }
}

@Composable
fun OfflinePayBottomBar(
    currentTab: BottomTab,
    onHomeClick: () -> Unit,
    onTransactionsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        color = NavBgColor,
        tonalElevation = 8.dp,
        border = BorderStroke(1.dp, NavBorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBar(
            containerColor = NavBgColor,
            tonalElevation = 0.dp,
            windowInsets = NavigationBarDefaults.windowInsets
        ) {
            NavigationBarItem(
                selected = currentTab == BottomTab.HOME,
                onClick = onHomeClick,
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home", fontSize = 11.sp, fontWeight = if (currentTab == BottomTab.HOME) FontWeight.Bold else FontWeight.Medium) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NavSelectedColor,
                    selectedTextColor = NavSelectedColor,
                    indicatorColor = NavSelectedColor.copy(alpha = 0.2f),
                    unselectedIconColor = NavUnselectedColor,
                    unselectedTextColor = NavUnselectedColor
                )
            )
            NavigationBarItem(
                selected = currentTab == BottomTab.TRANSACTIONS,
                onClick = onTransactionsClick,
                icon = { Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = "Transactions") },
                label = { Text("Transactions", fontSize = 11.sp, fontWeight = if (currentTab == BottomTab.TRANSACTIONS) FontWeight.Bold else FontWeight.Medium) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NavSelectedColor,
                    selectedTextColor = NavSelectedColor,
                    indicatorColor = NavSelectedColor.copy(alpha = 0.2f),
                    unselectedIconColor = NavUnselectedColor,
                    unselectedTextColor = NavUnselectedColor
                )
            )
            NavigationBarItem(
                selected = currentTab == BottomTab.SETTINGS,
                onClick = onSettingsClick,
                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                label = { Text("Settings", fontSize = 11.sp, fontWeight = if (currentTab == BottomTab.SETTINGS) FontWeight.Bold else FontWeight.Medium) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NavSelectedColor,
                    selectedTextColor = NavSelectedColor,
                    indicatorColor = NavSelectedColor.copy(alpha = 0.2f),
                    unselectedIconColor = NavUnselectedColor,
                    unselectedTextColor = NavUnselectedColor
                )
            )
        }
    }
}

@Composable
fun OfflinePayHeader(
    isConnected: Boolean,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Emerald Squircle Logo Icon with decreased border radius (8.dp)
            OfflinePayLogoIcon(
                modifier = Modifier.clickable { onProfileClick() },
                size = 42.dp
            )

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "OfflinePay",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "SECURE",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "upi://offline@secure",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }

        // Connectivity Badge
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isConnected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else WarningOrange.copy(alpha = 0.15f),
            border = BorderStroke(
                1.dp,
                if (isConnected) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) else WarningOrange.copy(alpha = 0.4f)
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = if (isConnected) Icons.Default.Wifi else Icons.Default.WifiOff,
                    contentDescription = null,
                    tint = if (isConnected) MaterialTheme.colorScheme.primary else WarningOrange,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = if (isConnected) "Online" else "Offline Ready",
                    color = if (isConnected) MaterialTheme.colorScheme.primary else WarningOrange,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun QuickActionGrid(
    onScanClick: () -> Unit,
    onUpiClick: () -> Unit,
    onBeneficiariesClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickActionItem(
                icon = Icons.Default.QrCodeScanner,
                label = "Scan QR",
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                tintColor = MaterialTheme.colorScheme.primary,
                onClick = onScanClick
            )
            QuickActionItem(
                icon = Icons.AutoMirrored.Filled.Send,
                label = "To UPI ID",
                containerColor = Color(0xFF3B82F6).copy(alpha = 0.15f),
                tintColor = Color(0xFF60A5FA),
                onClick = onUpiClick
            )
            QuickActionItem(
                icon = Icons.Default.AccountBalance,
                label = "Bank",
                containerColor = Color(0xFF8B5CF6).copy(alpha = 0.15f),
                tintColor = Color(0xFFA78BFA),
                onClick = onBeneficiariesClick
            )
            QuickActionItem(
                icon = Icons.Default.History,
                label = "History",
                containerColor = Color(0xFFF59E0B).copy(alpha = 0.15f),
                tintColor = Color(0xFFFBBF24),
                onClick = onHistoryClick
            )
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    containerColor: Color,
    tintColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tintColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun BalanceCard(
    balance: String,
    offlineKeyStatus: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Offline Payment Vault",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                text = "USSD *99# Active",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = balance,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Encryption Status",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = offlineKeyStatus,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun ScanQrButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Scan QR Code to Pay",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = actionText,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onActionClick() }
        )
    }
}

@Composable
fun TransactionRow(
    transaction: TransactionItemData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Merchant Initial Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = transaction.merchantOrName.take(1).uppercase(),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        text = transaction.merchantOrName,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = transaction.category,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "•",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                        Text(
                            text = transaction.timestamp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (transaction.isIncoming) "+" else "-"}${transaction.amount}",
                    color = if (transaction.isIncoming) SuccessGreen else MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (transaction.isOfflineVerified) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = if (transaction.isOfflineVerified) "Verified" else "Pending",
                        color = if (transaction.isOfflineVerified) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
