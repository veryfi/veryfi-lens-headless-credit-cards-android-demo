# Install, flavors, permissions

Source of truth: `veryfilens/build.gradle` (flavors + `publishing`), `FlavorCheckerHelper.kt`, module manifests, `AI_CONTEXT.md`. SDK version at inspection: **`2.7.0.6`**.

## Requirements

| Item | Value |
|------|-------|
| minSdk | 23 |
| compileSdk / targetSdk used by SDK | 36 |
| Camera stack | CameraX (bundled transitively) |
| Language | Kotlin façade; callable from Java via `@JvmStatic` |

## Maven repository

Artifacts are published to Veryfi's Nexus: `https://nexus.veryfi.com/repository/maven-releases/`. Access details come from Veryfi. If the repository requires credentials for your account, read them from Gradle properties — **never commit them** and never copy any credential found in the SDK repo.

```groovy
// settings.gradle (or root build.gradle allprojects.repositories)
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url "https://nexus.veryfi.com/repository/maven-releases/"
            // Only if Veryfi gave you credentials:
            credentials {
                username = providers.gradleProperty("veryfiMavenUser").getOrNull()
                password = providers.gradleProperty("veryfiMavenPassword").getOrNull()
            }
        }
    }
}
```

```groovy
// app/build.gradle — exactly ONE Lens UI flavor
dependencies {
    implementation "com.veryfi.lens:veryfi-lens-receipts-sdk:2.7.0.6"
}
```

Transitive dependencies (`com.veryfi.lens.cpp:veryfi-cpp-*-sdk`, `com.veryfi.lens.helpers:veryfi-helpers-sdk`) are resolved automatically; do not add them by hand.

## Flavor → artifact → allowed `documentTypes`

Group `com.veryfi.lens`, version `2.7.0.6`. "Allowed" is enforced by `FlavorCheckerHelper` in `showCamera()` only (see [SKILL.md](SKILL.md#1-flavor-must-match-documenttypes)).

| Gradle flavor | Artifact ID | Allowed `DocumentType`s |
|---------------|-------------|-------------------------|
| lensFull | `veryfi-lens-sdk` | RECEIPT, LONG_RECEIPT, OTHER, ANY_DOCUMENT, CHECK, BUSINESS_CARD, INVOICE, W2, W9, BARCODE, BANK_STATEMENT, INSURANCE_CARD, PASSPORT, DRIVER_LICENSE, NUTRITION_FACT, SHIPPING_LABEL, PRESCRIPTION_LABEL — **not** CREDIT_CARD, CODE |
| lensRemoteModels | `veryfi-lens-remote-models-sdk` | same as Full (not CREDIT_CARD, CODE). Downloads heavy models after install. |
| lensFieldDetection | `veryfi-lens-field-detection-sdk` | same as Full (not CREDIT_CARD, CODE). Enables local field detection (paid). |
| lensReceipts | `veryfi-lens-receipts-sdk` | RECEIPT, OTHER, INVOICE |
| lensLongReceipts | `veryfi-lens-long-receipts-sdk` | LONG_RECEIPT, RECEIPT |
| lensCheques | `veryfi-lens-cheques-sdk` | CHECK |
| lensCreditCards | `veryfi-lens-credit-cards-sdk` | CREDIT_CARD |
| lensBusinessCards | `veryfi-lens-business-cards-sdk` | BUSINESS_CARD, INSURANCE_CARD, PASSPORT, DRIVER_LICENSE, NUTRITION_FACT, SHIPPING_LABEL |
| lensBarcodes | `veryfi-lens-barcodes-sdk` | BARCODE |
| lensOCR | `veryfi-lens-ocr-sdk` | CODE |
| lensW2 | `veryfi-lens-w2-sdk` | W2 |
| lensW9 | `veryfi-lens-w9-sdk` | W9 |
| lensBankStatements | `veryfi-lens-bank-statements-sdk` | BANK_STATEMENT |
| lensAnyDocs | `veryfi-lens-any-docs-sdk` | RECEIPT, OTHER, INVOICE, LONG_RECEIPT, CHECK, DRIVER_LICENSE, ANY_DOCUMENT |
| lensIdentityVerification | `veryfi-lens-identity-verification-sdk` | DRIVER_LICENSE, PASSPORT, INSURANCE_CARD |
| lensPrescriptionLabels | `veryfi-lens-prescription-labels-sdk` | PRESCRIPTION_LABEL |
| lensLite | `veryfi-lens-lite-sdk` | RECEIPT, OTHER, INVOICE, LONG_RECEIPT, ANY_DOCUMENT, CHECK, BUSINESS_CARD, INSURANCE_CARD, PASSPORT, DRIVER_LICENSE, NUTRITION_FACT, SHIPPING_LABEL, BARCODE — **not** CREDIT_CARD, CODE, W2, W9, BANK_STATEMENT, PRESCRIPTION_LABEL. ABIs `armeabi-v7a`, `arm64-v8a` only. |
| lensDemo | (not published) | Demo package only — never use |

Other artifacts:

| Artifact | Use |
|----------|-----|
| `com.veryfi.lens.headless:receipts-sdk` | Headless Receipts (headless-specific native lib) |
| `com.veryfi.lens.headless:receipts-sdk-full` | Headless Receipts built on the full native lib (larger). Pick `receipts-sdk` unless Veryfi says otherwise (Unconfirmed trade-off) |
| `com.veryfi.lens.headless:credit-cards-sdk` | Headless Credit Cards (headless-specific native lib) |
| `com.veryfi.lens.headless:credit-cards-sdk-full` | Headless Credit Cards on the full native lib (larger); same caveat |
| `com.veryfi.lens.core:veryfi-lens-core-sdk` | Veryfi-owned Core (email + obsolete dictation). Built on Field Detection; adds `WRITE_CONTACTS`, `RECORD_AUDIO`. **Not for partners.** |

Any-Documents: nutrition facts, passport, driver license, shipping label, insurance card, and prescription label are Any-Documents shortcuts, not separate products. Contact support for another `anyDocumentTemplate`.

### Size advice

- Recommend `veryfi-lens-remote-models-sdk` when size matters and the types are allowed there (not credit cards or codes). Remote model download is enabled only for Remote Models and Lite (`RemoteModelConfig.isRemoteModelEnabled`).
- Check TIFF (`retrieveCheckTiffIsOn`) needs the `PPOcrDet` model, which is not among the Cheques flavor's bundled resources → use Remote Models. (Code-inspection only; Unconfirmed end-to-end on Android.)
- Measure the installed app with the SDK linked and removed. [SDK binary sizes](https://docs.veryfi.com/lens/mobile/settings/sdk_binary_sizes/).

## Permissions

The SDK manifest **merges** these into the host (no need to redeclare): `CAMERA`, `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `FLASHLIGHT`, `VIBRATE`, `FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_DATA_SYNC`, `POST_NOTIFICATIONS`, `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` (plus non-standard `READ/WRITE_INTERNAL_STORAGE`, which have no effect). Camera `uses-feature` entries are `required="false"`.

Runtime requests are made **by Lens UI** (`CaptureActivity`), not by the host:

| Permission | When requested |
|------------|----------------|
| CAMERA | Always when the camera opens |
| Location | Only if `locationServicesIsOn = true` |
| Storage (API < 33) | Non-Lite flavors, if `galleryIsOn`, `browseIsOn`, or `backupDocsToGallery` |
| POST_NOTIFICATIONS (API 33+) | Optional; upload runs in a `dataSync` foreground service with a notification |

Host responsibilities:

- `uploadImages(uris)` on API < 33 requires storage permissions already granted, otherwise `veryfiLensError` with `storage_permission_missing`. The paths overload does no check.
- Headless SDKs: the host requests `CAMERA` and runs CameraX itself.
- If the Play listing must not declare location or storage, remove them with `tools:node="remove"` in the host manifest **and** keep `locationServicesIsOn = false`, `backupDocsToGallery = false`, and gallery/browse off on API < 33. (Standard manifest-merger behavior; Unconfirmed that every Lens path tolerates removal.)
- The SDK declares a FileProvider with authority `${applicationId}.veryfi.lens.fileprovider` (not in Lite). Do not declare a provider with the same authority.
- New projects set `android:allowBackup="true"`. Lens manifests (`veryfilens`, `veryfihelpers`, and the headless modules) set `false`, so the merger fails: `Attribute application@allowBackup value=(true) ... is also present at [com.veryfi.lens:…] value=(false)`. On the host `<application>`, set `android:allowBackup="false"` and `tools:replace="android:allowBackup"`, and declare `xmlns:tools="http://schemas.android.com/tools"` on `<manifest>`. If `tools:replace` already lists other attributes, append `android:allowBackup`. Leave `dataExtractionRules` and `fullBackupContent` in place. Use `allowBackup="true"` with `tools:replace` only when the host must keep Auto Backup; that overrides the library.

## Lite (on-demand Dynamic Feature Module)

`veryfi-lens-lite-sdk` is meant to live in a Play Feature Delivery module. Checklist (from the SDK's `AI_CONTEXT.md`):

1. Put the Lite dependency in the feature module; install the module via Play Feature Delivery.
2. Call `SplitCompat.install(context)` once in `Application.attachBaseContext`. Lens activities install themselves.
3. When Play delivers the module as an emulated split (Android 10 redirect install, internal app sharing, `bundletool --local-testing`), call `com.veryfi.lens.cpp.dfm.NativeLibraryLoader.setSplitName("your_feature_module")` **before** `configure`.
4. Call `VeryfiLens.configure` only **after** the split is installed, and before any capture.
5. Keep `notificationSmallIcon = 0` or point it to a drawable in the **base** module.
6. Lite declares no ContentProviders: do not re-declare `CropFileProvider`; `shareLogsIsOn` is unavailable.
7. No stub `VeryfiLensTheme` is needed in the base module.
8. Only `armeabi-v7a` / `arm64-v8a` (no x86 emulators).

See [templates/lite.md](templates/lite.md).
