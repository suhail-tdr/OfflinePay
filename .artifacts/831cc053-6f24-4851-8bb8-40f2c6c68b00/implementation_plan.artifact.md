# Implementation Plan - OfflinePay

OfflinePay is an offline UPI payment assistant for India that allows users to scan merchant UPI QR codes without internet, extract payment details, review them, and prepare/guide USSD banking (`*99#`) sessions while storing transaction history securely in a local Room database.

## User Review Required

> [!IMPORTANT]
> - **Security & Privacy:** OfflinePay never collects, stores, or transmits user UPI PINs.
> - **USSD / Network Limitations:** USSD payments (`*99#`) require cellular network connectivity and participating banks. OfflinePay does not automate USSD banking menus or claim independent transaction verification.

## Proposed Changes

### 1. Domain & Data Layers (Room Database & UPI Parser)
- **[NEW]** [UpiParser.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/domain/UpiParser.kt): UPI URI parser supporting `pa`, `pn`, `am`, `cu`, `tr`, `tn`, `mc` parameters with validation and unit tests.
- **[NEW]** [TransactionEntity.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/data/database/TransactionEntity.kt): Room entity for storing payment records.
- **[NEW]** [TransactionDao.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/data/database/TransactionDao.kt): DAO for CRUD operations and filtering.
- **[NEW]** [OfflinePayDatabase.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/data/database/OfflinePayDatabase.kt): Room database instance.
- **[NEW]** [TransactionRepository.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/data/repository/TransactionRepository.kt): Repository wrapper around TransactionDao.

### 2. ViewModels & State Management
- **[NEW]** [TransactionViewModel.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/presentation/TransactionViewModel.kt): ViewModel for managing transactions, scanning states, and history.

### 3. QR Scanner & CameraX
- **[NEW]** [QrScannerScreen.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/presentation/scanner/QrScannerScreen.kt): CameraX preview and ML Kit Barcode scanning integration.

### 4. UI Screens & Navigation
- **[NEW]** [Screen.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/navigation/Screen.kt): Navigation routes.
- **[NEW]** [OfflinePayNavGraph.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/navigation/OfflinePayNavGraph.kt): Jetpack Compose Navigation graph connecting all 9 screens (Splash, Onboarding, Dashboard, QR Scanner, Payment Details, USSD Assistant, Payment Status, Transaction History, Settings).
- **[NEW]** [SplashScreen.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/presentation/splash/SplashScreen.kt)
- **[NEW]** [OnboardingScreen.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/presentation/onboarding/OnboardingScreen.kt)
- **[MODIFY]** [DashboardScreen.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/ui/dashboard/DashboardScreen.kt): Update dashboard to connect with navigation and ViewModels.
- **[NEW]** [PaymentDetailsScreen.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/presentation/payment/PaymentDetailsScreen.kt)
- **[NEW]** [UssdAssistantScreen.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/presentation/ussd/UssdAssistantScreen.kt)
- **[NEW]** [PaymentStatusScreen.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/presentation/status/PaymentStatusScreen.kt)
- **[NEW]** [TransactionHistoryScreen.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/presentation/history/TransactionHistoryScreen.kt)
- **[NEW]** [SettingsScreen.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/main/java/com/offlinepay/app/presentation/settings/SettingsScreen.kt)

### 5. Testing
- **[NEW]** [UpiParserTest.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/test/java/com/offlinepay/app/domain/UpiParserTest.kt): Unit tests for UPI URI parser.
- **[NEW]** [TransactionDaoTest.kt](file:///C:/Users/suhai/AndroidStudioProjects/OfflinePay/app/src/test/java/com/offlinepay/app/data/database/TransactionDaoTest.kt): Room DAO tests.

## Verification Plan

### Automated Tests
- Run `./gradlew test` to execute unit tests for UPI parser and Room database.
- Run `./gradlew assembleDebug` to verify successful compilation.

### Manual Verification
- Deploy to emulator / physical device.
- Test QR scanning, payment preparation, USSD dialer handoff (`*99#`), local transaction history, and offline behavior.
