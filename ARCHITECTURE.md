# Architecture – OfflinePay

OfflinePay follows **MVVM (Model-View-ViewModel)** with **Clean Architecture** principles:
- **Presentation Layer:** Jetpack Compose UI screens observing ViewModel state flows.
- **Domain Layer:** Business logic, UPI URI parser (`UpiParser`), and payment data models.
- **Data Layer:** Room database entities (`TransactionEntity`), DAO, and repository (`TransactionRepository`).
- **Navigation:** Navigation Compose (`OfflinePayNavGraph`).
