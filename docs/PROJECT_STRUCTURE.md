# Project Structure & Directory Guide

This document provides a complete guide to the codebase organization of **OfflinePay**.

```
OfflinePay/
├── app/                                 # Main Android Application Module
│   ├── build.gradle.kts                 # App-level build script (dependencies, minSdk 23, compileSdk 35)
│   ├── proguard-rules.pro               # ProGuard keep rules
│   └── src/
│       ├── androidTest/                 # Instrumentation UI tests
│       ├── main/
│       │   ├── AndroidManifest.xml      # App permissions, activities, file provider declarations
│       │   ├── java/com/offlinepay/app/
│       │   │   ├── MainActivity.kt      # Single Activity entry point
│       │   │   ├── data/
│       │   │   │   ├── database/        # Room Database (OfflinePayDatabase, TransactionDao, TransactionEntity)
│       │   │   │   └── repository/      # TransactionRepository implementation
│       │   │   ├── domain/              # Pure JVM UpiParser & UpiPaymentData model
│       │   │   ├── navigation/          # Navigation destinations & OfflinePayNavGraph
│       │   │   ├── presentation/        # ViewModels & Feature Screens
│       │   │   │   ├── history/         # TransactionHistoryScreen
│       │   │   │   ├── payment/         # PaymentDetailsScreen
│       │   │   │   ├── scanner/         # QrScannerScreen
│       │   │   │   ├── settings/        # SettingsScreen
│       │   │   │   ├── splash/          # SplashScreen
│       │   │   │   ├── status/          # PaymentStatusScreen
│       │   │   │   ├── ussd/            # UssdAssistantScreen
│       │   │   │   └── TransactionViewModel.kt
│       │   │   ├── ui/
│       │   │   │   ├── components/      # Reusable Compose widgets (DashboardComponents, OfflinePayLogoIcon)
│       │   │   │   ├── dashboard/       # DashboardScreen
│       │   │   │   └── theme/           # True Black AMOLED theme (Color, Theme, Type)
│       │   │   └── util/                # BiometricHelper, ConnectivityHelper, NotificationHelper
│       │   └── res/                     # Drawables, mipmaps, values, and XML provider paths
│       └── test/                        # Unit tests (UpiParserTest, TransactionStatusTest)
├── docs/                                # Technical documentation directory
│   ├── ARCHITECTURE.md                  # System architecture specification
│   ├── FEATURES.md                      # Detailed feature implementation log
│   ├── INSTALLATION.md                  # Development environment setup guide
│   ├── PROJECT_STRUCTURE.md             # Codebase layout guide
│   ├── SECURITY.md                      # OWASP MASVS security & privacy guidelines
│   └── TESTING.md                       # Automated test & lint verification report
├── build.gradle.kts                     # Root build configuration
├── gradle.properties                    # Gradle JVM properties & flags
├── settings.gradle.kts                  # Gradle module settings
├── CONTRIBUTING.md                      # Community contribution guidelines
├── LICENSE                              # MIT Open Source License
├── README.md                            # GitHub Repository Landing Page
└── SECURITY.md                          # Root security disclosure policy
```
