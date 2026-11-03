# Implemented Features & Functionality

This document details every feature implemented in **OfflinePay**, explaining its operation and implementation status.

---

## 1. Implemented Features

### 📷 Offline CameraX & ML Kit QR Scanner
* **Status**: **Fully Implemented**
* **Description**: Real-time camera barcode scanner utilizing CameraX Camera2 API and Google ML Kit Barcode Scanning.
* **Key Functionality**:
  * Camera permission request handling.
  * Live camera preview feed inside Compose `AndroidView`.
  * Prominent floating **Flashlight / Torch Toggle Button** for low-light scanning environments.
  * Instant QR string extraction and navigation to Payment Details.

### 🔍 Pure JVM UPI URI Parser
* **Status**: **Fully Implemented**
* **Description**: 100% offline Query Decoder implemented in `com.offlinepay.app.domain.UpiParser`.
* **Key Functionality**:
  * Extracts Payee Name (`pn`), UPI ID (`pa`), Amount (`am`), Currency (`cu`), Transaction Reference (`tr`), Note (`tn`), and Merchant Code (`mc`).
  * Handles URL-encoded characters (`John%20Doe`, `Tea%20%26%20Snacks`).
  * Validates non-negative amounts and checks for required `@` symbol in UPI IDs.
  * Pure JVM implementation allowing fast unit testing without Android emulator overhead.

### 📱 USSD *99# Payment Assistant
* **Status**: **Fully Implemented**
* **Description**: Guided offline payment workflow for NPCI's `*99#` USSD banking protocol.
* **Key Functionality**:
  * Automatic SIM operator detection using `TelephonyManager`.
  * Preferred **Dual-SIM Slot Selector** (SIM 1 / SIM 2) in Settings.
  * Displays merchant summary, UPI ID, and payment instructions.
  * Launches official `tel:*99%23` system dialer.

### 🔒 Biometric Security Authorization
* **Status**: **Fully Implemented**
* **Description**: On-device biometric unlock using `androidx.biometric.BiometricPrompt`.
* **Key Functionality**:
  * Requires fingerprint, face unlock, or device PIN before launching payment flows.
  * Can be toggled on/off in Settings.

### 🧾 On-Device Image Receipt Generator
* **Status**: **Fully Implemented**
* **Description**: Canvas-rendered PNG receipt image creator.
* **Key Functionality**:
  * Draws branded OfflinePay receipt bitmap with merchant name, UPI ID, amount, and timestamp.
  * Saves PNG image to app cache.
  * Shares receipt via Android `FileProvider` (`Intent.ACTION_SEND`) to WhatsApp, SMS, or Email.

### 📊 Room Database & Transaction History
* **Status**: **Fully Implemented**
* **Description**: Local SQLite database compiled with KSP (`com.google.devtools.ksp`).
* **Key Functionality**:
  * Real-time search by merchant name or UPI ID.
  * Status filter chips (**All**, **Success**, **Failed**, **Pending**).
  * CSV transaction export.

### 🖤 True Black AMOLED Theme & Adaptive Icon
* **Status**: **Fully Implemented**
* **Description**: Material 3 dark/light theme engine.
* **Key Functionality**:
  * True Black AMOLED (`#000000`) theme with Google Green accents (`#00B887`).
  * Android 13+ `<monochrome>` themed launcher icon.

---

## 2. Feature Implementation Summary Table

| Feature | Implementation Status | Implementation Details |
| :--- | :--- | :--- |
| QR Code Scanning | **Implemented** | CameraX + ML Kit Barcode Scanning + Torch Toggle |
| UPI Query Parsing | **Implemented** | Pure JVM URLDecoder |
| USSD *99# Dialer | **Implemented** | System Intent `tel:*99%23` |
| Dual-SIM Configuration | **Implemented** | Preferred SIM 1 / SIM 2 slot toggle |
| Biometric Lock | **Implemented** | `androidx.biometric:biometric:1.1.0` |
| PNG Receipt Sharing | **Implemented** | Canvas Bitmap + `FileProvider` |
| Transaction History | **Implemented** | Room Database + KSP + Search & Filter |
| CSV Data Export | **Implemented** | On-device CSV exporter |
| Local Notifications | **Implemented** | `NotificationHelper` channel |
| BLE / NFC Peer-to-Peer | **Planned** | Future enhancement |
