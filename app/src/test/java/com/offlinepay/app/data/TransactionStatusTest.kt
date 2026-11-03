package com.offlinepay.app.data

import com.offlinepay.app.data.database.TransactionEntity
import org.junit.Assert.*
import org.junit.Test

class TransactionStatusTest {

    @Test
    fun transactionEntity_defaultStatus_isPrepared() {
        val entity = TransactionEntity(
            merchantName = "Cafe Coffee Day",
            upiId = "ccd@upi",
            amount = "120.00",
            transactionStatus = "PREPARED"
        )

        assertEquals("PREPARED", entity.transactionStatus)
        assertNull(entity.verificationSource)
    }

    @Test
    fun transactionEntity_userReportedStatus_distinguishesFromBankVerified() {
        val entity = TransactionEntity(
            merchantName = "Supermarket",
            upiId = "store@okicici",
            amount = "450.00",
            transactionStatus = "SUCCESS_REPORTED",
            verificationSource = "User Reported"
        )

        assertEquals("SUCCESS_REPORTED", entity.transactionStatus)
        assertEquals("User Reported", entity.verificationSource)
        assertNotEquals("BANK_VERIFIED", entity.verificationSource)
    }

    @Test
    fun transactionEntity_validStatuses_areRecognized() {
        val validStatuses = listOf(
            "PREPARED",
            "INITIATED",
            "PENDING_VERIFICATION",
            "SUCCESS_REPORTED",
            "FAILED_REPORTED",
            "UNKNOWN"
        )

        validStatuses.forEach { status ->
            val entity = TransactionEntity(
                merchantName = "Test Merchant",
                upiId = "test@upi",
                amount = "10.00",
                transactionStatus = status
            )
            assertEquals(status, entity.transactionStatus)
        }
    }
}
