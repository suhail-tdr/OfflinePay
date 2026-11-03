# OfflinePay Architecture Specification

OfflinePay is designed following modern **Android Architecture Guidelines**, adopting the **Model-View-ViewModel (MVVM)** pattern with Unidirectional Data Flow (UDF) powered by Jetpack Compose and Kotlin Coroutines/StateFlow.

---

## High-Level Architectural Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                       UI Layer (View)                       │
│  Jetpack Compose Screens:                                   │
│  - DashboardScreen                                          │
│  - QrScannerScreen                                          │
│  - PaymentDetailsScreen                                     │
│  - UssdAssistantScreen                                      │
│  - PaymentStatusScreen                                      │
│  - TransactionHistoryScreen                                 │
│  - SettingsScreen                                           │
└──────────────────────────────┬──────────────────────────────┘
                               │ User Interactions / Actions
                               │ Observed StateFlows
┌──────────────────────────────▼──────────────────────────────┐
│                   ViewModel Layer                           │
│  TransactionViewModel                                       │
│  - Manages scanned UPI data, theme, biometric lock & SIM    │
│  - Exposes StateFlow<List<TransactionEntity>> & TotalCount  │
└──────────────────────────────┬──────────────────────────────┘
                               │ Coroutines / Repository calls
┌──────────────────────────────▼──────────────────────────────┐
│                   Repository Layer                          │
│  TransactionRepository                                      │
│  - Single source of truth for transaction persistence       │
└──────────────────────────────┬──────────────────────────────┘
                               │
            ┌──────────────────┴──────────────────┐
            │                                     │
┌───────────▼────────────┐            ┌───────────▼────────────┐
│      Data Layer        │            │      Domain Layer      │
│  OfflinePayDatabase    │            │  UpiParser             │
│  (Room DAO / SQLite)   │            │  (Pure JVM URLDecoder) │
└────────────────────────┘            └────────────────────────┘
```

---

## Component Breakdown

### 1. View Layer (Jetpack Compose UI)
* **Declarative Composables**: All UI elements are rendered using Jetpack Compose with Material 3 design tokens.
* **Single Activity**: `MainActivity` serves as the sole activity container, hoisting the Compose `NavHost`.
* **Navigation**: Driven by `OfflinePayNavGraph` via `androidx.navigation.compose`.

### 2. ViewModel Layer
* `TransactionViewModel` holds UI state using `StateFlow` and `asStateFlow()`.
* Coroutines handle database operations off the main thread using `viewModelScope.launch`.

### 3. Domain Layer
* `UpiParser`: Pure JVM URLDecoder-based parser that validates and extracts payment parameters (`pa`, `pn`, `am`, `cu`, `tr`, `tn`, `mc`) from `upi://pay` URI strings without framework dependencies, enabling 100% unit test execution.

### 4. Data & Persistence Layer
* **Room Database**: Local SQLite database compiled with KSP (`com.google.devtools.ksp`).
* **Entities**: `TransactionEntity` captures transaction metadata and lifecycle status.
* **Data Access Object**: `TransactionDao` provides reactive `Flow<List<TransactionEntity>>` queries.

---

## Payment Lifecycle Data Flow

```
[QR Code / Input] -> [UpiParser] -> [PaymentDetailsScreen] -> [Biometric Lock Check]
                                                                      │
                                                                 (Authorized)
                                                                      │
[PaymentStatusScreen] <- [USSD Assistant (*99#)] <- [Room DB: PREPARED]
```

1. **Scan / Parse**: CameraX captures QR -> `UpiParser` extracts payment metadata.
2. **Review & Validate**: User reviews merchant name, UPI ID, and amount preset chips.
3. **Biometric Lock**: Optional `BiometricPrompt` verifies user identity.
4. **Prepare Record**: Saved to Room with status `PREPARED`. Notification posted via `NotificationHelper`.
5. **USSD Assistance**: User dials `*99#` via official phone dialer.
6. **Confirmation & Receipt**: Outcome recorded (`SUCCESS_REPORTED` / `FAILED_REPORTED`) and branded PNG image receipt generated.
