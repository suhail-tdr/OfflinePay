package com.offlinepay.app.domain

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

data class UpiPaymentData(
    val payeeAddress: String,
    val payeeName: String,
    val amount: String?,
    val currency: String,
    val transactionRef: String?,
    val transactionNote: String?,
    val merchantCode: String?,
    val rawUri: String
)

object UpiParser {
    fun parse(uriString: String?): Result<UpiPaymentData> {
        if (uriString.isNullOrBlank()) {
            return Result.failure(IllegalArgumentException("URI cannot be null or blank"))
        }

        val trimmed = uriString.trim()
        if (!trimmed.lowercase().startsWith("upi://")) {
            return Result.failure(IllegalArgumentException("Not a valid UPI payment URI"))
        }

        val queryParams = mutableMapOf<String, String>()
        try {
            val queryIndex = trimmed.indexOf('?')
            if (queryIndex != -1 && queryIndex < trimmed.length - 1) {
                val query = trimmed.substring(queryIndex + 1)
                for (param in query.split("&")) {
                    val pair = param.split("=", limit = 2)
                    if (pair.size == 2) {
                        val key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8.name())
                        val value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8.name())
                        queryParams[key] = value
                    } else if (pair.size == 1 && pair[0].isNotBlank()) {
                        queryParams[URLDecoder.decode(pair[0], StandardCharsets.UTF_8.name())] = ""
                    }
                }
            }
        } catch (e: Exception) {
            return Result.failure(IllegalArgumentException("Invalid UPI URI query format", e))
        }

        val pa = queryParams["pa"]?.trim()
        if (pa.isNullOrBlank()) {
            return Result.failure(IllegalArgumentException("Missing payee address (pa)"))
        }

        if (!pa.contains("@")) {
            return Result.failure(IllegalArgumentException("Invalid UPI ID format: $pa"))
        }

        val pn = queryParams["pn"]?.trim() ?: "Unknown Merchant"
        val am = queryParams["am"]?.trim()
        val cu = queryParams["cu"]?.trim() ?: "INR"
        val tr = queryParams["tr"]?.trim()
        val tn = queryParams["tn"]?.trim()
        val mc = queryParams["mc"]?.trim()

        if (!am.isNullOrBlank()) {
            val amountVal = am.toDoubleOrNull()
            if (amountVal == null || amountVal < 0) {
                return Result.failure(IllegalArgumentException("Invalid amount: $am"))
            }
        }

        return Result.success(
            UpiPaymentData(
                payeeAddress = pa,
                payeeName = pn,
                amount = am,
                currency = cu,
                transactionRef = tr,
                transactionNote = tn,
                merchantCode = mc,
                rawUri = uriString
            )
        )
    }
}
