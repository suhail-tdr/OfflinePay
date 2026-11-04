package com.offlinepay.app.util

import com.offlinepay.app.data.database.TransactionEntity
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

object BackupHelper {

    fun exportToJson(transactions: List<TransactionEntity>): String {
        val array = JSONArray()
        for (tx in transactions) {
            val obj = JSONObject().apply {
                put("transactionId", tx.transactionId)
                put("merchantName", tx.merchantName)
                put("upiId", tx.upiId)
                put("amount", tx.amount ?: "")
                put("currency", tx.currency)
                put("transactionReference", tx.transactionReference ?: "")
                put("transactionNote", tx.transactionNote ?: "")
                put("transactionStatus", tx.transactionStatus)
                put("createdAt", tx.createdAt)
                put("updatedAt", tx.updatedAt)
                put("verificationSource", tx.verificationSource ?: "")
            }
            array.put(obj)
        }
        return array.toString(2)
    }

    fun importFromJson(jsonString: String): List<TransactionEntity> {
        val list = mutableListOf<TransactionEntity>()
        val array = JSONArray(jsonString)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val entity = TransactionEntity(
                merchantName = obj.optString("merchantName", "Unknown Merchant"),
                upiId = obj.optString("upiId", ""),
                amount = obj.optString("amount").takeIf { it.isNotBlank() },
                currency = obj.optString("currency", "INR"),
                transactionReference = obj.optString("transactionReference").takeIf { it.isNotBlank() },
                transactionNote = obj.optString("transactionNote").takeIf { it.isNotBlank() },
                transactionStatus = obj.optString("transactionStatus", "PREPARED"),
                createdAt = obj.optLong("createdAt", System.currentTimeMillis()),
                updatedAt = obj.optLong("updatedAt", System.currentTimeMillis()),
                verificationSource = obj.optString("verificationSource").takeIf { it.isNotBlank() }
            )
            if (entity.upiId.isNotBlank()) {
                list.add(entity)
            }
        }
        return list
    }

    fun encrypt(plainText: String, secretKey: String): String {
        val keyBytes = MessageDigest.getInstance("SHA-256").digest(secretKey.toByteArray(StandardCharsets.UTF_8))
        val keySpec = SecretKeySpec(keyBytes, "AES")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = ByteArray(12) { 0x01 } // fixed GCM IV
        val gcmSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)
        val encrypted = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    fun decrypt(encryptedText: String, secretKey: String): String {
        val keyBytes = MessageDigest.getInstance("SHA-256").digest(secretKey.toByteArray(StandardCharsets.UTF_8))
        val keySpec = SecretKeySpec(keyBytes, "AES")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = ByteArray(12) { 0x01 }
        val gcmSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)
        val decoded = Base64.decode(encryptedText, Base64.DEFAULT)
        val decrypted = cipher.doFinal(decoded)
        return String(decrypted, StandardCharsets.UTF_8)
    }
}
