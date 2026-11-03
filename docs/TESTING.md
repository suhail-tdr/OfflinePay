# Testing & Verification Report

OfflinePay includes automated unit testing, static lint analysis, and physical device test execution.

---

## 1. Automated Test Execution Results

### Unit Test Summary (`:app:testDebugUnitTest`)
* **Execution Command**: `./gradlew testDebugUnitTest`
* **Test Outcome**: **12 Passed, 0 Skipped, 0 Failed**
* **Execution Time**: ~6 seconds

```
Task :app:testDebugUnitTest
12 passed, 0 skipped, 0 failed.
BUILD SUCCESSFUL
```

---

## 2. Test Suite Details

### `UpiParserTest` (`app/src/test/java/com/offlinepay/app/domain/UpiParserTest.kt`)
* `parse_validUpiUri_returnsSuccess`: Validates parsing of complete `upi://pay` URIs with `pa`, `pn`, `am`, `cu`, `tn`, and `tr`.
* `parse_urlEncodedParameters_decodesCorrectly`: Verifies URL decoding for spaces and special characters (`pn=John%20Doe`, `tn=Tea%20%26%20Snacks`).
* `parse_missingPayeeAddress_returnsFailure`: Rejects URIs lacking required `pa` parameter.
* `parse_invalidUpiId_returnsFailure`: Rejects UPI IDs missing the `@` character.
* `parse_invalidAmount_returnsFailure`: Rejects negative or non-numeric payment amounts.
* `parse_nonUpiScheme_returnsFailure`: Rejects non-UPI URI schemes (`https://`).
* `parse_nullOrBlank_returnsFailure`: Handles null or empty input safely without crashing.

### `TransactionStatusTest` (`app/src/test/java/com/offlinepay/app/data/TransactionStatusTest.kt`)
* `transactionEntity_defaultStatus_isPrepared`: Confirms default transaction state.
* `transactionEntity_userReportedStatus_distinguishesFromBankVerified`: Ensures user-reported statuses are explicitly labeled.
* `transactionEntity_validStatuses_areRecognized`: Verifies all valid lifecycle status strings.

---

## 3. Static Lint Analysis Results

* **Execution Command**: `./gradlew lintDebug`
* **Lint Outcome**: **PASS (0 Errors)**
* **Key Fixes**:
  * Corrected API 23 `registerDefaultNetworkCallback` lint requirement in `ConnectivityHelper.kt`.
  * Configured adaptive launcher icon directory to `mipmap-anydpi-v26`.
