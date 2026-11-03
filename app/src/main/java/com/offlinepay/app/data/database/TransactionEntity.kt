package com.offlinepay.app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Long = 0L,
    val merchantName: String,
    val upiId: String,
    val amount: String?,
    val currency: String = "INR",
    val transactionReference: String? = null,
    val transactionNote: String? = null,
    val transactionStatus: String, // PREPARED, INITIATED, PENDING_VERIFICATION, SUCCESS_REPORTED, FAILED_REPORTED, UNKNOWN
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val verificationSource: String? = null
)
