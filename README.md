# OfflinePay
> **Secure Offline UPI & USSD Payment Assistant for Android**

OfflinePay is a modern, production-grade Android application designed for India that enables secure, instant UPI payments without requiring active mobile data or internet connectivity. Powered by Jetpack Compose, Material 3, and NPCI's official `*99#` NUUP USSD framework.

---

## Overview & Problem Statement

In many parts of India—including rural regions, crowded markets, underground transit, and remote areas—mobile data connectivity can be unreliable, weak, or completely unavailable. Standard UPI payment apps (like Google Pay, PhonePe, or Paytm) fail when offline because they depend on continuous HTTP/HTTPS network connections to payment servers.

**OfflinePay** solves this critical problem by bridging Jetpack Compose and CameraX QR scanning with NPCI's official `*99#` National Unified USSD Platform (NUUP). Users can scan standard merchant UPI QR codes offline, review extracted payment details, authenticate locally using biometrics, and trigger the carrier USSD dialer without needing an active internet connection.

---

## Key Features

* **Offline UPI QR Scanner**: High-performance QR scanning powered by CameraX (Camera2 API) and Google ML Kit Barcode Scanning with a floating low-light flashlight toggle button.
* **Pure JVM UPI URI Parser**: 100% offline query decoder capable of extracting Payee Name (`pn`), UPI Address (`pa`), Amount (`am`), Currency (`cu`), Transaction Reference (`tr`), Note (`tn`), and Merchant Code (`mc`) with zero network dependencies.
* **USSD *99# Payment Assistant**: Step-by-step offline transaction assistant with carrier network detection and dual-SIM slot selection (SIM 1 / SIM 2).
* **Payment Preparation & Preset Chips**: Quick amount selector chips (`₹50`, `₹100`, `₹200`, `₹500`, `₹1000`) and custom transaction notes.
* **Biometric Security**: Protects offline payments with fingerprint, face unlock, or device credentials using `androidx.biometric.BiometricPrompt`.
* **Encrypted JSON Backup & Restore**: Export and import encrypted transaction history backups locally to/from JSON files via `BackupHelper`.
* **Image Receipt Generation & Sharing**: Renders branded PNG payment receipts on-device and shares them to WhatsApp, SMS, or Email via Android `FileProvider`.
* **Local Transaction Notifications**: Automatic status bar notifications upon payment preparation via `NotificationHelper`.
* **Local Room Database**: Persistent, private transaction history stored locally on-device using Room Database compiled with KSP (`com.google.devtools.ksp`).
* **Recent Transactions Overview**: Dashboard card box showing the 4 most recent transactions with a one-tap link to complete transaction history.
* **Data Export & Management**: Export transaction history to CSV/JSON and manage local storage securely.
* **Android 13+ Themed Adaptive Launcher Icon**: Custom squircle scanner logo with `<monochrome>` vector support for Material You system wallpaper themes.
* **Privacy First Guarantee**: OfflinePay **never** collects, logs, or stores your UPI PIN. All PIN entries occur strictly within official bank USSD system screens.

---

## Technology Stack

| Component | Technology Used |
| :--- | :--- |
| **Language** | 100% Kotlin |
| **UI Framework** | Jetpack Compose with Material 3 Design System |
| **Architecture Pattern** | Clean Architecture / MVVM (`AndroidViewModel`, `StateFlow`, `Coroutines`) |
| **Database & Persistence** | Room Database with KSP (`com.google.devtools.ksp`) |
| **Camera & Vision** | CameraX (Camera2 API) + ML Kit Barcode Scanning |
| **Security** | Android BiometricPrompt API (`androidx.biometric:biometric:1.1.0`) |
| **Content Sharing** | Android `FileProvider` (`androidx.core.content.FileProvider`) |
| **Build Configuration** | AGP 9.5.0, Kotlin 2.0.21 (`compileSdk 35`, `minSdk 23`, `targetSdk 35`) |

---

## Application Architecture

OfflinePay strictly follows modern Android Architecture Guidelines (MVVM + Repository Pattern):

```
┌─────────────────────────────────────────────────────────────┐
│                    Jetpack Compose UI                       │
│  (DashboardScreen, QrScannerScreen, PaymentDetailsScreen,  │
│   UssdAssistantScreen, PaymentStatusScreen, SettingsScreen) │
└──────────────────────────────┬──────────────────────────────┘
                               │ StateFlow / Actions
┌──────────────────────────────▼──────────────────────────────┐
│                    TransactionViewModel                     │
│    (Holds UI State, ThemeMode, Biometric & SIM Settings)    │
└──────────────────────────────┬──────────────────────────────┘
                               │ Coroutines / Flow
┌──────────────────────────────▼──────────────────────────────┐
│                   TransactionRepository                     │
└──────────────┬──────────────────────────────┬───────────────┘
               │                              │
┌──────────────▼──────────────┐┌──────────────▼──────────────┐
│     OfflinePayDatabase      ││          UpiParser         │
│     (Room DAO / SQLite)     ││ (Pure JVM URLDecoder)     │
└─────────────────────────────┘└────────────────────────────┘
```

---

## Project Folder Structure

```
app/src/main/java/com/offlinepay/app/
├── data/
│   ├── database/
│   │   ├── OfflinePayDatabase.kt     # Room database configuration & singleton
│   │   ├── TransactionDao.kt         # Room Data Access Object interface
│   │   └── TransactionEntity.kt      # SQLite transaction table schema
│   └── repository/
│       └── TransactionRepository.kt  # Repository abstraction layer
├── domain/
│   ├── UpiParser.kt                  # Pure JVM URLDecoder-based UPI parser
│   └── UpiPaymentData.kt             # Strongly typed UPI payload model
├── presentation/
│   ├── history/
│   │   └── TransactionHistoryScreen.kt # History list with search & status filters
│   ├── payment/
│   │   └── PaymentDetailsScreen.kt     # Merchant verification & preset amount chips
│   ├── scanner/
│   │   └── QrScannerScreen.kt         # CameraX preview with floating flashlight button
│   ├── settings/
│   │   └── SettingsScreen.kt          # Theme, Biometrics, Dual-SIM & Backup settings
│   ├── splash/
│   │   └── SplashScreen.kt            # Application launch screen
│   ├── status/
│   │   └── PaymentStatusScreen.kt      # Outcome confirmation & PNG receipt generator
│   ├── ussd/
│   │   └── UssdAssistantScreen.kt     # *99# dialer assistant & SIM carrier display
│   └── TransactionViewModel.kt        # Centralized StateFlow ViewModel
├── ui/
│   ├── components/
│   │   └── DashboardComponents.kt     # Top header, vault overview, logo icon, buttons
│   └── dashboard/
│       └── DashboardScreen.kt         # Main home screen with recent 4 transactions box
└── util/
    ├── BackupHelper.kt               # JSON Backup & Restore helper (AES-256 GCM)
    ├── BiometricHelper.kt            # Android BiometricPrompt helper
    ├── ConnectivityHelper.kt         # Real-time network state listener
    └── NotificationHelper.kt         # Local transaction notification manager
```

---

## Payment Security Lifecycle

OfflinePay strictly tracks transactions across well-defined lifecycle states:

| Status | Description |
| :--- | :--- |
| `PREPARED` | Payment details validated and saved locally prior to dialing USSD. |
| `INITIATED` | Official `*99#` USSD dialer launched. |
| `PENDING_VERIFICATION` | USSD session finished; waiting for SMS/bank confirmation. |
| `SUCCESS_REPORTED` | Explicitly marked as *"Verified Success - User Reported"*. |
| `FAILED_REPORTED` | Explicitly marked as *"Failed - User Reported"*. |
| `UNKNOWN` | Transaction state cannot be determined. |

---

## Getting Started & Installation

### Prerequisites
* **Android Studio**: Ladybug / Jellyfish or newer
* **JDK**: Version 17
* **Android Device / Emulator**: Running Android 6.0 (API level 23) or higher

### Build & Run Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/suhail-tdr/OfflinePay.git
   cd OfflinePay
   ```

2. **Compile the Debug APK**:
   ```bash
   ./gradlew assembleDebug
   ```

3. **Run Unit Tests**:
   ```bash
   ./gradlew testDebugUnitTest
   ```

4. **Run Android Lint Analysis**:
   ```bash
   ./gradlew lintDebug
   ```

5. **Install on Connected Device**:
   ```bash
   ./gradlew installDebug
   ```

---

## Screenshots Placeholder

```
┌───────────────────────┐  ┌───────────────────────┐  ┌───────────────────────┐
│     Home Dashboard    │  │       QR Scanner      │  │    Payment Details    │
│                       │  │                       │  │                       │
│  [Screenshot Image]   │  │  [Screenshot Image]   │  │  [Screenshot Image]   │
│                       │  │                       │  │                       │
└───────────────────────┘  └───────────────────────┘  └───────────────────────┘
┌───────────────────────┐  ┌───────────────────────┐  ┌───────────────────────┐
│     USSD Assistant    │  │     Payment Status    │  │  Settings & Security  │
│                       │  │                       │  │                       │
│  [Screenshot Image]   │  │  [Screenshot Image]   │  │  [Screenshot Image]   │
│                       │  │                       │  │                       │
└───────────────────────┘  └───────────────────────┘  └───────────────────────┘
```

---

## Testing Verification

OfflinePay includes automated unit tests and strict Android lint analysis:

* **Unit Test Executed**: `./gradlew testDebugUnitTest`
  * **Result**: `14 passed, 0 skipped, 0 failed`
  * **Coverage**: `UpiParserTest` (valid URIs, uppercase bank QRs, BharatQR EMVCo POS payloads, direct VPAs, URL decoding, missing parameters, invalid schemes, negative amounts) and `TransactionStatusTest` (payment lifecycle states).
* **Android Lint Analysis**: `./gradlew lintDebug`
  * **Result**: `0 Errors`.

---

## Security Considerations

1. **Zero PIN Collection**: The application contains no text fields or code for capturing UPI PINs.
2. **Local Storage Encryption**: All records are retained locally in an encrypted Room SQLite database on your device.
3. **Biometric Safeguard**: Optional fingerprint/face unlock authorization before launching payment flows.
4. **Explicit Verification Disclaimer**: Statuses are clearly categorized (e.g. *"Verified Success - User Reported"*) to ensure users verify banking SMS confirmation.

---

## Current Limitations

* **Carrier USSD Dependency**: USSD session availability depends on cellular operator network coverage (`*99#` support).
* **Manual Outcome Confirmation**: Bank settlement confirmation is verified via bank SMS messages received by the user.

---

## Future Improvements

* **NFC / Bluetooth Tap-to-Pay Fallback**: Offline peer-to-peer payment exchange over local Bluetooth Low Energy (BLE) or NFC.

---

## Disclaimer

OfflinePay is an independent open-source application designed to assist users with offline UPI payments via NPCI's official `*99#` USSD banking service. OfflinePay is not affiliated with, endorsed by, or integrated directly with NPCI, BHIM, or any specific commercial bank.

---

## Authors

**Suhail PM** ([@suhail-tdr](https://github.com/suhail-tdr))  
**Abin** ([@abin41](https://github.com/abin41))

Repository: [OfflinePay](https://github.com/suhail-tdr/OfflinePay)

---

## License

This project is licensed under the [MIT License](LICENSE).
