# Testing Strategy – OfflinePay

- **Unit Tests:** UPI QR parser verification against valid and malformed UPI URIs.
- **Instrumented Tests:** Room Database CRUD operations and ViewModel state emissions.
- **Manual Verification Scenarios:**
  1. Offline QR code scanning with Wi-Fi & Mobile Data disabled.
  2. USSD dialer handoff with `*99#`.
  3. Recording payment status (`SUCCESS_REPORTED`, `FAILED_REPORTED`).
