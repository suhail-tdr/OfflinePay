# Contributing to OfflinePay

Thank you for your interest in contributing to **OfflinePay**! Contributions from the open-source community help make offline payments safer, faster, and more accessible for everyone across India.

---

## How to Contribute

### 1. Reporting Issues
* Search existing GitHub Issues before opening a new one to avoid duplicates.
* Describe the bug or feature request clearly with steps to reproduce, expected behavior, and device/Android version information.

### 2. Submitting Pull Requests
1. Fork the repository and create a new branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. Follow Kotlin Coding Conventions and Jetpack Compose best practices.
3. Ensure all unit tests pass locally:
   ```bash
   ./gradlew testDebugUnitTest
   ```
4. Verify Android lint analysis produces 0 errors:
   ```bash
   ./gradlew lintDebug
   ```
5. Commit your changes with clear, descriptive commit messages:
   ```bash
   git commit -m "Add feature: your feature description"
   ```
6. Push to your branch and open a Pull Request against the `main` branch.

---

## Security Guidelines

* **Never submit code that logs, stores, or intercepts UPI PINs.**
* Do not hardcode API keys or private tokens in source code or resources.
* Ensure all database interactions utilize parameterized Room queries to prevent injection vulnerabilities.

---

## Code Style

* Follow standard [Kotlin Style Guidelines](https://kotlinlang.org/docs/coding-conventions.html).
* Use Compose Material 3 design components where applicable.
* Maintain clean MVVM separation between UI Composables, `TransactionViewModel`, and `TransactionRepository`.
