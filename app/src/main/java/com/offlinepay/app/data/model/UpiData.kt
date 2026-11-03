package com.offlinepay.app.data.model

data class UpiData(
    val payeeAddress: String, // pa
    val payeeName: String,    // pn
    val amount: String?,      // am
    val currency: String?,    // cu
    val transactionNote: String?, // tn
    val transactionRef: String?,  // tr
    val merchantCode: String?,    // mc
    val rawUri: String
)
