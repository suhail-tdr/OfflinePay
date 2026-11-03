package com.offlinepay.app.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.offlinepay.app.R

object NotificationHelper {
    private const val CHANNEL_ID = "offlinepay_transactions"
    private const val CHANNEL_NAME = "Offline Transactions"

    @SuppressLint("MissingPermission")
    fun showPaymentPreparedNotification(context: Context, merchantName: String, amount: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for offline UPI payment preparations"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Payment Prepared: ₹$amount")
            .setContentText("Offline payment to $merchantName is ready for USSD *99# dialer")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        } catch (_: Exception) {
            // Ignore if notification permission is not granted
        }
    }
}
