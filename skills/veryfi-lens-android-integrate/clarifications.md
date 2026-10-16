# Clarifications, Unconfirmed items, iOS ↔ Android differences

Everything in this pack was checked against Android source at `2.7.0.6` (commit `7f33726c`). Items below are **not** fully confirmed. Say so when a host relies on them; do not present them as contract.

## Unconfirmed

| Item | What the code shows | Status |
|------|---------------------|--------|
| `cleanLensStorage()` and `veryfi/zip/` | Clears Lens Pictures and base-dir zips via `FilesHelper.clearLocalResources()` | Unconfirmed that retained `veryfi/zip/` packages are removed — delete `local_package_path` yourself |
| Blurred images in `uploadImages` | With `nonInteractiveBlurCheckIsOn = false`, blurred images are not added to the session | Unconfirmed whether this also depends on `blurDetectionIsOn`, and whether any callback fires |
| `attachDocument(documentId)` | Starts capture in attach mode with `receiptId` | Veryfi-owned flow; unconfirmed for partners |
| OCR with `VeryfiCloudAPI` | Uploads to the partner OCR endpoint; Demo reads `data.id`, `data.code`, `data.score`, `data.img_url` | Response shape unconfirmed as contract; InApp keys (`ocr_uuid`, `ocr_text`, `ocr_score`, `ocr_image`, `ocr_type`) are confirmed in code |
| `showNativeAlerts` | Mentioned in KDoc only; no property exists | Treat as unsupported |
| Remote settings when region cached | `validateClientId` returns true without a network call | Unconfirmed whether `lens_settings` are re-merged from cache on that path |
| Headless Receipts upload | No image upload / Process a Document in code; only doc-ready metadata | Host owns upload. Unconfirmed whether the SDK deletes `file_paths` |
| Headless `-full` vs non-full artifacts | `-full` depends on the full native lib | Size/feature trade-off unconfirmed; default to non-full |
| Mutating `VeryfiLensHelper.settings` (Headless CC Demo) | Demo flips `creditCardDetectCard*` between sides | Internal helper; not a documented contract |
| Check TIFF on Android | `retrieveCheckTiffIsOn` requires `PPOcrDet`; not in Cheques bundled resources; remote models enabled for Remote Models / Lite | Unconfirmed end-to-end on device; Lite + TIFF unconfirmed |
| `img_original_paths` order for checks | Reversed in code | Verify which side is index 0 on device |
| `closeCameraOnSubmit` + `submitWithoutClose` both true | Two code paths | Winner unconfirmed |
| `showHelpButton` + `showInfoButton` together | Separate views | Visual fit unconfirmed |
| String overrides | Library string resources merge into host | Resource names are not a documented contract |
| Removing merged permissions (`tools:node="remove"`) | Standard manifest merger | Unconfirmed that every Lens path handles missing permissions |
| `useLegacyModels` | Default true; Demo checks preset sets false | No public contract; leave default |
| `cameraProcessingMode` outside `ANY_DOCUMENT` | KDoc: "only valid" for any document | Behavior otherwise unconfirmed |
| `cropLayoutIsOn` alone for non-receipt types | Frame view not added unless forced | Unconfirmed; use explicit fixed/hybrid combos |

## Unsupported on Android (do not generate)

- iOS-only APIs: `VeryfiLens.shared()`, `utils`, `lensVC()`/embedding a view controller, `showCrop`, `headlessUpload(images:)`/`(url:)`, SwiftUI helpers, `shareExtensionModeIsOn`, `customLensStrings`, `backupDocsToiCloud`, `showiCloudBackupOption`, `VeryfiInfoView`.
- Embedding Lens as a `Fragment` in the host layout (Lens always starts its own Activities).
- `DocumentType` values `dictation` / `bank_letter_and_check` (not in the Android enum).
- `CREDIT_CARD` / `CODE` on Full, Remote Models, Field Detection, Lite; `W2` / `W9` / `BANK_STATEMENT` / `PRESCRIPTION_LABEL` on Lite.
- Credentialless scanning for partners (Demo package only).
- `invalid_credentials` delegate error (Android reports via `configure` callback `false`).
- Veryfi Core (`VeryfiLensCore`, email/dictation) for partners.

## iOS ↔ Android differences that change code

| Topic | iOS | Android |
|-------|-----|---------|
| Façade | `VeryfiLens.shared()` | `VeryfiLens` object, `@JvmStatic` |
| Delegate | weak `delegate` property | `setDelegate(...)` strong ref; `removeDelegate()` |
| Configure | `configure(with:settings:completion:)` | `configure(application, credentials, settings) { ok -> }` |
| Failure status | `fail` | `failed` |
| `documentTypes` | `[String]` | `ArrayList<DocumentType>?`, default `null` |
| `defaultSelectedDocumentType` | `String` | `DocumentType?` |
| Enums | `.cloudAPI` / `.inApp` / `.none` | `ExtractionEngine.VeryfiCloudAPI` / `VeryfiInApp` / `None` |
| Custom views | `VeryfiInfoView` | `Fragment` (`showInfoButton`, `cameraInfoView`) |
| Colors | `UIColor` / strings | hex `String` |
| No-UI files | `uploadImages(paths:)`, `headlessUpload` | `uploadImages(uris / paths, isSameTransaction)` |
| More menu | overrides dropped when menu hidden | same, but overrides also **replace host values on configure** while the menu is on |
| Defaults that differ | — | `packageMaxScans = 5`, `glareDetectionIsOn = true`, `autoRotateIsOn = false`, `autoSkewCorrectionIsOn = false`, `cleanBorderIsOn = false`, `zoomIsOn = true`, `batchUploadAnimationDuration = 0.8`, `driverLicenseBackIsOn = true`, `insuranceCardBackIsOn = true`, `galleryOtherIsOn = true`, `alertSheetStyleIsOn = true` |
| Hybrid crop layout | `cropLayoutWithAutoDetection` alone | `cropLayoutIsOn` **and** `cropLayoutWithAutoDetection` |
| Lite | App Clip | Play Feature Delivery module |
| Check TIFF fields | — | `img_check_front_tiff_path`, `img_check_back_tiff_path` |
