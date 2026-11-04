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

        val raw = uriString.trim().replace("\uFEFF", "").replace("\u200B", "")

        // 1. Standard or embedded upi:// parsing
        val upiMatch = findUpiUri(raw)
        if (upiMatch != null) {
            return parseUpiUri(upiMatch)
        }

        // 2. EMVCo / BharatQR parsing (used by SBI, HDFC, ICICI POS terminals)
        if (raw.startsWith("000201") || raw.contains("000201")) {
            val emvcoParsed = parseEmvcoBharatQr(raw)
            if (emvcoParsed.isSuccess) return emvcoParsed
        }

        // 3. Direct VPA Check (e.g. "merchant.store@sbi")
        val directVpa = extractDirectVpa(raw)
        if (directVpa != null) {
            return Result.success(
                UpiPaymentData(
                    payeeAddress = directVpa,
                    payeeName = extractMerchantNameFromVpa(directVpa),
                    amount = null,
                    currency = "INR",
                    transactionRef = null,
                    transactionNote = "Direct UPI Payment",
                    merchantCode = null,
                    rawUri = raw
                )
            )
        }

        return Result.failure(IllegalArgumentException("Not a valid UPI payment URI"))
    }

    private fun findUpiUri(input: String): String? {
        val lower = input.lowercase()
        val index = lower.indexOf("upi://")
        if (index != -1) {
            return input.substring(index)
        }
        return null
    }

    private fun parseUpiUri(upiUri: String): Result<UpiPaymentData> {
        val queryParams = mutableMapOf<String, String>()
        try {
            val queryIndex = upiUri.indexOf('?')
            if (queryIndex != -1 && queryIndex < upiUri.length - 1) {
                val query = upiUri.substring(queryIndex + 1)
                for (param in query.split("&")) {
                    val pair = param.split("=", limit = 2)
                    if (pair.isNotEmpty() && pair[0].isNotBlank()) {
                        val key = safeDecode(pair[0]).lowercase().trim()
                        val value = if (pair.size == 2) safeDecode(pair[1]).trim() else ""
                        queryParams[key] = value
                    }
                }
            }
        } catch (e: Exception) {
            return Result.failure(IllegalArgumentException("Invalid UPI query format", e))
        }

        val pa = queryParams["pa"]
        if (pa.isNullOrBlank()) {
            return Result.failure(IllegalArgumentException("Missing payee address (pa)"))
        }

        if (!pa.contains("@")) {
            return Result.failure(IllegalArgumentException("Invalid UPI ID format: $pa"))
        }

        val pn = queryParams["pn"]?.takeIf { it.isNotBlank() } ?: extractMerchantNameFromVpa(pa)
        val am = queryParams["am"]?.takeIf { it.isNotBlank() }
        val cu = queryParams["cu"]?.takeIf { it.isNotBlank() } ?: "INR"
        val tr = queryParams["tr"]?.takeIf { it.isNotBlank() }
        val tn = queryParams["tn"]?.takeIf { it.isNotBlank() }
        val mc = queryParams["mc"]?.takeIf { it.isNotBlank() }

        if (!am.isNullOrBlank()) {
            val amountVal = am.toDoubleOrNull()
            if (amountVal == null || amountVal < 0) {
                return Result.failure(IllegalArgumentException("Invalid payment amount: $am"))
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
                rawUri = upiUri
            )
        )
    }

    private fun parseEmvcoBharatQr(raw: String): Result<UpiPaymentData> {
        var pa: String? = null
        var pn: String? = null
        var am: String? = null

        try {
            // Find embedded VPA with letter start matching
            val vpaPattern = Regex("""([a-zA-Z][a-zA-Z0-9.\-_]{1,50}@[a-zA-Z]+)""")
            val match = vpaPattern.find(raw)
            if (match != null) {
                pa = match.value
            }

            // Extract merchant name if available (tag 59)
            val namePattern = Regex("""59(\d{2})([A-Za-z0-9\s.\-_]+)""")
            val nameMatch = namePattern.find(raw)
            if (nameMatch != null) {
                val len = nameMatch.groupValues[1].toIntOrNull() ?: 0
                val fullName = nameMatch.groupValues[2]
                if (fullName.length >= len) {
                    pn = fullName.substring(0, len).trim()
                }
            }

            // Extract amount if available (tag 54)
            val amountPattern = Regex("""54(\d{2})([\d.]+)""")
            val amountMatch = amountPattern.find(raw)
            if (amountMatch != null) {
                val len = amountMatch.groupValues[1].toIntOrNull() ?: 0
                val fullAmount = amountMatch.groupValues[2]
                if (fullAmount.length >= len) {
                    am = fullAmount.substring(0, len).trim()
                }
            }
        } catch (_: Exception) {}

        if (!pa.isNullOrBlank() && pa.contains("@")) {
            return Result.success(
                UpiPaymentData(
                    payeeAddress = pa,
                    payeeName = pn ?: extractMerchantNameFromVpa(pa),
                    amount = am,
                    currency = "INR",
                    transactionRef = null,
                    transactionNote = "BharatQR / Bank POS Payment",
                    merchantCode = null,
                    rawUri = raw
                )
            )
        }

        return Result.failure(IllegalArgumentException("Could not extract UPI VPA from BharatQR"))
    }

    private fun extractDirectVpa(raw: String): String? {
        val trimmed = raw.trim()
        val vpaRegex = Regex("""^[a-zA-Z0-9.\-_]+@[a-zA-Z0-9]+$""")
        if (vpaRegex.matches(trimmed)) {
            return trimmed
        }
        return null
    }

    private fun extractMerchantNameFromVpa(vpa: String): String {
        val handle = vpa.substringBefore("@").replace(".", " ").replace("-", " ").replace("_", " ")
        return handle.split(" ").joinToString(" ") { part ->
            part.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }.takeIf { it.isNotBlank() } ?: "Bank Merchant"
    }

    private fun safeDecode(input: String): String {
        return try {
            URLDecoder.decode(input.replace("+", "%20"), StandardCharsets.UTF_8.name())
        } catch (_: Exception) {
            input
        }
    }
}
