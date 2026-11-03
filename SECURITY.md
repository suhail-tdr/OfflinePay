# Security Policy

## Reporting Vulnerabilities

The security and privacy of OfflinePay users are our highest priority. If you discover a security vulnerability or potential privacy defect in this project, please report it responsibly.

### How to Report

* **Email**: Contact the maintainer directly or open a confidential security advisory on GitHub.
* Do **NOT** open public GitHub issues for critical security vulnerabilities.

Please include the following information in your report:
1. Description of the vulnerability and its potential impact.
2. Step-by-step instructions or proof-of-concept code to reproduce the issue.
3. Affected app version, Android OS version, and device details.

---

## Security Principles & Architecture

OfflinePay adheres to OWASP Mobile Application Security Verification Standard (MASVS) principles:

1. **Zero PIN Collection**: OfflinePay contains no text fields or prompts for capturing UPI PINs. All PIN authentication is handled strictly by the official telecommunications USSD framework (`*99#`).
2. **Local Data Persistence**: All transaction logs are retained locally in an encrypted Room SQLite database on your physical device. No remote tracking or analytics servers are used.
3. **Biometric Safeguard**: Optional fingerprint/face unlock authorization before launching payment flows.
4. **Transparent Verification**: Payment outcomes explicitly distinguish user-reported status from bank-verified status.

---

## Supported Versions

| Version | Supported |
| :--- | :--- |
| `1.0.x` | Yes |
