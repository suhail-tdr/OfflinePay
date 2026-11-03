package com.offlinepay.app.data.repository

import com.offlinepay.app.data.database.TransactionDao
import com.offlinepay.app.data.database.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val totalCount: Flow<Int> = transactionDao.getTotalCount()

    suspend fun getTransactionById(id: Long): TransactionEntity? {
        return transactionDao.getTransactionById(id)
    }

    fun getTransactionsByStatus(status: String): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByStatus(status)
    }

    fun searchTransactions(query: String): Flow<List<TransactionEntity>> {
        return transactionDao.searchTransactions(query)
    }

    suspend fun insertTransaction(transaction: TransactionEntity): Long {
        return transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun updateStatus(id: Long, status: String, verificationSource: String? = null) {
        val tx = transactionDao.getTransactionById(id)
        if (tx != null) {
            transactionDao.updateTransaction(tx.copy(transactionStatus = status, verificationSource = verificationSource, updatedAt = System.currentTimeMillis()))
        }
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun deleteTransaction(id: Long) {
        transactionDao.deleteTransactionById(id)
    }

    suspend fun deleteAllTransactions() {
        transactionDao.deleteAllTransactions()
    }

    suspend fun clearAll() {
        transactionDao.deleteAllTransactions()
    }
}
