# Installation & Setup Guide

This guide describes how to set up, build, test, and install **OfflinePay** on your local development environment or Android device.

---

## Environment Requirements

* **Operating System**: Windows 10/11, macOS, or Linux
* **Android Studio**: Ladybug (2024.2.1+) or Jellyfish (2024.1.1+)
* **Java Development Kit (JDK)**: JDK 17
* **Android SDK**:
  * `compileSdk`: 35 (Android 15)
  * `minSdk`: 23 (Android 6.0 Marshmallow)
  * `targetSdk`: 35 (Android 15)
* **Build System**: Gradle 8.x with Android Gradle Plugin 9.5.0

---

## Step-by-Step Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/suhail-tdr/OfflinePay.git
cd OfflinePay
```

### 2. Open Project in Android Studio
1. Open Android Studio.
2. Select **File > Open** and choose the `OfflinePay` directory.
3. Allow Gradle to sync dependencies automatically.

### 3. Build Debug APK
Run the Gradle assemble task:
* **Windows (Command Prompt / PowerShell)**:
  ```cmd
  gradlew.bat assembleDebug
  ```
* **macOS / Linux**:
  ```bash
  ./gradlew assembleDebug
  ```

The generated debug APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## Running Automated Tests

### 1. Execute Unit Tests
```bash
./gradlew testDebugUnitTest
```

### 2. Execute Android Lint Analysis
```bash
./gradlew lintDebug
```

---

## Installing on Physical Device

1. Enable **Developer Options** and **USB Debugging** on your Android smartphone.
2. Connect your device via USB or Wireless ADB.
3. Verify device connection:
   ```bash
   adb devices
   ```
4. Install and launch the application:
   ```bash
   ./gradlew installDebug
   ```
