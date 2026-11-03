# OfflinePay

> **Secure Offline UPI & USSD Payment Assistant for Android**

OfflinePay is a modern, production-grade Android application designed for India that enables secure, instant UPI payments without requiring active mobile data or internet connectivity. Powered by Jetpack Compose, Material 3, and NPCI's official `*99#` NUUP USSD framework.

---

## Key Features

* **Offline UPI QR Scanner**: Instant QR parsing powered by CameraX and Google ML Kit Barcode Scanning with a floating low-light flashlight toggle button.
* **Pure JVM UPI URI Parser**: 100% offline query decoder capable of extracting Payee Name (`pn`), UPI Address (`pa`), Amount (`am`), Currency (`cu`), Transaction Reference (`tr`), and Note (`tn`) with zero network dependencies.
* **USSD *99# Payment Assistant**: Guided step-by-step offline transaction workflow with SIM operator detection and dual-SIM slot selection (SIM 1 / SIM 2).
* **Payment Preparation & Amount Presets**: Quick amount selector chips (`₹50`, `₹100`, `₹200`, `₹500`, `₹1000`) and customizable transaction notes.
* **Biometric Security**: Protects offline payments with fingerprint, face unlock, or device credentials using `androidx.biometric.BiometricPrompt`.
* **Image Receipt Generation & Sharing**: Generates branded PNG payment receipts on-device and shares them to WhatsApp, SMS, or Email via Android `FileProvider`.
* **Local Transaction Notifications**: Automatic status bar notifications upon payment preparation via `NotificationHelper`.
* **Local Room Database**: Persistent, private transaction history stored locally on-device using Room Database compiled with KSP (`com.google.devtools.ksp`).
* **Recent Transactions Overview**: Dashboard card box showing the 4 most recent transactions with a one-tap link to complete transaction history.
* **Data Export & Management**: Export transaction history to CSV and manage local storage securely.
* **Android 13+ Themed Adaptive Launcher Icon**: Custom squircle scanner logo with `<monochrome>` vector support for Material You system wallpaper themes.
* **Privacy First**: OfflinePay **never** collects, logs, or stores your UPI PIN. All PIN entries occur strictly within official bank USSD system screens.

---

## Tech Stack & Architecture

* **Language**: 100% Kotlin
* **UI Toolkit**: Jetpack Compose with Material 3 Design System
* **Architecture**: Clean Architecture / MVVM (`AndroidViewModel`, `StateFlow`, `Coroutines`)
* **Database**: Room Database with KSP (`com.google.devtools.ksp`)
* **Camera & ML**: CameraX (Camera2 API) + ML Kit Barcode Scanning
* **Security**: Android BiometricPrompt API (`androidx.biometric`)
* **Build System**: Android Gradle Plugin 9.5.0 (`compileSdk 35`, `minSdk 23`, `targetSdk 35`)

---

## Project Structure

```
app/src/main/java/com/offlinepay/app/
├── data/
│   ├── database/
│   │   ├── OfflinePayDatabase.kt     # Room database setup
│   │   ├── TransactionDao.kt         # Data Access Object
│   │   └── TransactionEntity.kt      # Transaction Room Entity
│   └── repository/
│       └── TransactionRepository.kt  # Repository pattern
├── domain/
│   ├── UpiParser.kt                  # Pure JVM URLDecoder-based UPI parser
│   └── UpiPaymentData.kt             # Data class model for parsed UPI params
├── presentation/
│   ├── history/
│   │   └── TransactionHistoryScreen.kt # History screen with search & status filters
│   ├── payment/
│   │   └── PaymentDetailsScreen.kt     # Merchant details & amount preset chips
│   ├── scanner/
│   │   └── QrScannerScreen.kt         # CameraX scanner with floating torch button
│   ├── settings/
│   │   └── SettingsScreen.kt          # Biometrics, Dual-SIM settings
│   ├── splash/
│   │   └── SplashScreen.kt            # App launch splash
│   ├── status/
│   │   └── PaymentStatusScreen.kt      # Outcome confirmation & PNG image receipt
│   ├── ussd/
│   │   └── UssdAssistantScreen.kt     # *99# dialer assistant & SIM carrier display
│   └── TransactionViewModel.kt        # Centralized StateFlow ViewModel
├── ui/
│   ├── components/
│   │   └── DashboardComponents.kt     # Header, vault overview, logo icon, buttons
│   └── dashboard/
│       └── DashboardScreen.kt         # Main home screen with recent 4 transactions box
└── util/
    ├── BiometricHelper.kt            # Android BiometricPrompt helper
    ├── ConnectivityHelper.kt         # Network connectivity monitor
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

## Getting Started

### Prerequisites
* **Android Studio**: Ladybug / Jellyfish or newer
* **JDK**: Version 17
* **Android Device / Emulator**: Running Android 6.0 (API level 23) or higher

### Build & Installation Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/OfflinePay.git
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

4. **Run Android Lint**:
   ```bash
   ./gradlew lintDebug
   ```

5. **Install on Connected Device**:
   ```bash
   ./gradlew installDebug
   ```

---

## Security & Privacy Notice

OfflinePay is designed with strict privacy principles:
1. **Zero PIN Access**: The application does **not** contain any text field or prompt for UPI PINs.
2. **Local Storage Only**: All transaction logs are stored exclusively in an encrypted Room database on your physical device.
3. **Official USSD Gateway**: All offline transactions are routed through NPCI's official `*99#` USSD telecommunication protocol provided by telecom operators in India.

---

## License

This project is licensed under the [MIT License](LICENSE).
=======
Offline QR Payment Assistant – Android Application
>>>>>>> 7acf9f727a97bb942c006fe47e2c50aa3f44d3cd
