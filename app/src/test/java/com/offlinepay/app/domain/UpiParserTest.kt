package com.offlinepay.app.domain

import org.junit.Assert.*
import org.junit.Test

class UpiParserTest {

    @Test
    fun parse_validUpiUri_returnsSuccess() {
        val uri = "upi://pay?pa=merchant@upi&pn=TestMerchant&am=150.00&cu=INR&tn=Coffee&tr=TR123"
        val result = UpiParser.parse(uri)

        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertEquals("merchant@upi", data?.payeeAddress)
        assertEquals("TestMerchant", data?.payeeName)
        assertEquals("150.00", data?.amount)
        assertEquals("INR", data?.currency)
        assertEquals("Coffee", data?.transactionNote)
        assertEquals("TR123", data?.transactionRef)
    }

    @Test
    fun parse_urlEncodedParameters_decodesCorrectly() {
        val uri = "upi://pay?pa=shop%40ybl&pn=John%20Doe&tn=Tea%20%26%20Snacks&am=50"
        val result = UpiParser.parse(uri)

        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertEquals("shop@ybl", data?.payeeAddress)
        assertEquals("John Doe", data?.payeeName)
        assertEquals("Tea & Snacks", data?.transactionNote)
        assertEquals("50", data?.amount)
    }

    @Test
    fun parse_missingPayeeAddress_returnsFailure() {
        val uri = "upi://pay?pn=TestMerchant&am=100.00"
        val result = UpiParser.parse(uri)

        assertTrue(result.isFailure)
        assertEquals("Missing payee address (pa)", result.exceptionOrNull()?.message)
    }

    @Test
    fun parse_invalidUpiId_returnsFailure() {
        val uri = "upi://pay?pa=invalidupi&pn=TestMerchant"
        val result = UpiParser.parse(uri)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Invalid UPI ID format") == true)
    }

    @Test
    fun parse_invalidAmount_returnsFailure() {
        val uri = "upi://pay?pa=merchant@okicici&am=-50"
        val result = UpiParser.parse(uri)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Invalid amount") == true)
    }

    @Test
    fun parse_nonNumericAmount_returnsFailure() {
        val uri = "upi://pay?pa=merchant@okicici&am=abc"
        val result = UpiParser.parse(uri)

        assertTrue(result.isFailure)
    }

    @Test
    fun parse_nonUpiScheme_returnsFailure() {
        val uri = "https://example.com/pay?pa=merchant@upi"
        val result = UpiParser.parse(uri)

        assertTrue(result.isFailure)
        assertEquals("Not a valid UPI payment URI", result.exceptionOrNull()?.message)
    }

    @Test
    fun parse_nullOrBlank_returnsFailure() {
        assertTrue(UpiParser.parse(null).isFailure)
        assertTrue(UpiParser.parse("   ").isFailure)
    }
}
