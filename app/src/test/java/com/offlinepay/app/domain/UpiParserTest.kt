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
    fun parse_uppercaseParameters_sbiQr_returnsSuccess() {
        val uri = "UPI://PAY?PA=sbi.store@sbi&PN=SBI%20Merchant%20Store&AM=250.00&CU=INR"
        val result = UpiParser.parse(uri)

        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertEquals("sbi.store@sbi", data?.payeeAddress)
        assertEquals("SBI Merchant Store", data?.payeeName)
        assertEquals("250.00", data?.amount)
    }

    @Test
    fun parse_embeddedUpiInBankAppWrapper_hdfcOrGpay_returnsSuccess() {
        val uri = "gpay://upi://pay?pa=store.hdfc@okhdfcbank&pn=HDFC%20Supermarket&am=500"
        val result = UpiParser.parse(uri)

        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertEquals("store.hdfc@okhdfcbank", data?.payeeAddress)
        assertEquals("HDFC Supermarket", data?.payeeName)
        assertEquals("500", data?.amount)
    }

    @Test
    fun parse_bharatQrEmvCoFormat_sbiPos_returnsSuccess() {
        val emvcoPayload = "00020101021226480009sbi.merchant@sbi01031235204581253033565406100.005802IN5912SBI%20Merchant6007MUMBAI63041234"
        val result = UpiParser.parse(emvcoPayload)

        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertEquals("sbi.merchant@sbi", data?.payeeAddress)
        assertEquals("100.00", data?.amount)
    }

    @Test
    fun parse_directVpaString_returnsSuccess() {
        val directVpa = "shop.owner@okicici"
        val result = UpiParser.parse(directVpa)

        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertEquals("shop.owner@okicici", data?.payeeAddress)
        assertEquals("Shop Owner", data?.payeeName)
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
        assertTrue(result.exceptionOrNull()?.message?.contains("Missing payee address") == true)
    }

    @Test
    fun parse_invalidUpiId_returnsFailure() {
        val uri = "upi://pay?pa=invalidupi&pn=TestMerchant"
        val result = UpiParser.parse(uri)

        assertTrue(result.isFailure)
    }

    @Test
    fun parse_invalidAmount_returnsFailure() {
        val uri = "upi://pay?pa=merchant@okicici&am=-50"
        val result = UpiParser.parse(uri)

        assertTrue(result.isFailure)
    }

    @Test
    fun parse_nullOrBlank_returnsFailure() {
        assertTrue(UpiParser.parse(null).isFailure)
        assertTrue(UpiParser.parse("   ").isFailure)
    }
}
