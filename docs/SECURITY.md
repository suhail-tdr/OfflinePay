# Security Practices & Vulnerability Governance

OfflinePay is architected with strict mobile application security principles aligned with the **OWASP Mobile Application Security Verification Standard (MASVS)**.

---

## Security Practices

### 1. Zero UPI PIN Access
* **Architecture**: OfflinePay **never** contains text fields, code, or prompts for collecting or intercepting UPI PINs.
* **Enforcement**: All PIN entries occur exclusively inside official telecommunications USSD system screens provided by cellular carriers and banks.

### 2. Local-Only Encrypted Storage
* **Architecture**: Transaction history is retained strictly within a local Room SQLite database on your device.
* **No Remote Analytics**: The app makes zero external network telemetry calls and transmits no personal payment data.

### 3. Biometric Authorization
* **Architecture**: Uses `androidx.biometric.BiometricPrompt` to enforce fingerprint, face unlock, or device PIN authentication before initiating offline payments.

### 4. Secure Content Sharing
* **FileProvider Isolation**: Receipt sharing uses Android `FileProvider` (`androidx.core.content.FileProvider`) with temporary read permissions (`grantUriPermissions="true"`) to prevent intent redirection vulnerabilities.

### 5. Input Sanitization
* **QR URI Parsing**: `UpiParser` sanitizes input, validates required parameters, enforces URL decoding, and checks numeric amount boundaries to prevent injection or malformed QR crashes.

---

## Vulnerability Reporting

To report security vulnerabilities, please email the maintainer or open a private security disclosure on GitHub. Do not submit public issues for zero-day security reports.
