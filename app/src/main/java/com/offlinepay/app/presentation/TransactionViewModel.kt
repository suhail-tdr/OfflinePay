package com.offlinepay.app.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.offlinepay.app.data.database.OfflinePayDatabase
import com.offlinepay.app.data.database.TransactionEntity
import com.offlinepay.app.data.repository.TransactionRepository
import com.offlinepay.app.domain.UpiParser
import com.offlinepay.app.domain.UpiPaymentData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppTheme { DARK, LIGHT, SYSTEM }

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TransactionRepository

    val allTransactions: StateFlow<List<TransactionEntity>>
    val totalCount: StateFlow<Int>

    private val _scannedPaymentData = MutableStateFlow<UpiPaymentData?>(null)
    val scannedPaymentData: StateFlow<UpiPaymentData?> = _scannedPaymentData.asStateFlow()

    private val _parseError = MutableStateFlow<String?>(null)
    val parseError: StateFlow<String?> = _parseError.asStateFlow()

    private val _currentTransactionId = MutableStateFlow<Long?>(null)
    val currentTransactionId: StateFlow<Long?> = _currentTransactionId.asStateFlow()

    private val _currentTheme = MutableStateFlow(AppTheme.DARK)
    val currentTheme: StateFlow<AppTheme> = _currentTheme.asStateFlow()

    private val _biometricEnabled = MutableStateFlow(true)
    val biometricEnabled: StateFlow<Boolean> = _biometricEnabled.asStateFlow()

    private val _preferredSimSlot = MutableStateFlow(1)
    val preferredSimSlot: StateFlow<Int> = _preferredSimSlot.asStateFlow()

    init {
        val transactionDao = OfflinePayDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)

        allTransactions = repository.allTransactions
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        totalCount = repository.totalCount
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    }

    fun setTheme(theme: AppTheme) {
        _currentTheme.value = theme
    }

    fun setBiometricEnabled(enabled: Boolean) {
        _biometricEnabled.value = enabled
    }

    fun setPreferredSimSlot(slot: Int) {
        _preferredSimSlot.value = slot
    }

    fun parseQrCode(uriString: String?) {
        val result = UpiParser.parse(uriString)
        if (result.isSuccess) {
            _scannedPaymentData.value = result.getOrNull()
            _parseError.value = null
        } else {
            _scannedPaymentData.value = null
            _parseError.value = result.exceptionOrNull()?.message ?: "Failed to parse QR code"
        }
    }

    fun clearScannedData() {
        _scannedPaymentData.value = null
        _parseError.value = null
    }

    fun createAndSaveTransaction(
        merchantName: String,
        upiId: String,
        amount: String?,
        currency: String = "INR",
        reference: String?,
        note: String?,
        status: String = "PREPARED",
        onSaved: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val entity = TransactionEntity(
                merchantName = merchantName,
                upiId = upiId,
                amount = amount,
                currency = currency,
                transactionReference = reference,
                transactionNote = note,
                transactionStatus = status
            )
            val id = repository.insertTransaction(entity)
            _currentTransactionId.value = id
            onSaved(id)
        }
    }

    fun updateTransactionStatus(id: Long, status: String, verificationSource: String? = null) {
        viewModelScope.launch {
            val tx = repository.getTransactionById(id)
            if (tx != null) {
                repository.updateTransaction(
                    tx.copy(
                        transactionStatus = status,
                        verificationSource = verificationSource
                    )
                )
            }
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun deleteAllTransactions() {
        viewModelScope.launch {
            repository.deleteAllTransactions()
        }
    }
}
