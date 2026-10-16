# Settings groups (conceptual)

Not public SDK types. Each group mutates `VeryfiLensSettings` only. IDs match the iOS pack so cross-platform helpers can share names; flag values below are **Android** semantics.

Compose: **ExtractionMode** + **CaptureFlow** + **CaptureSources** + **QualityGate** + **MultiPage** + optional extras + **Chrome** + **Branding**.

## ExtractionMode

| ID | Flags | Use |
|----|-------|-----|
| `CloudProcess` | `dataExtractionEngine = VeryfiCloudAPI`, `uploadWithoutProcessingIsOn = false` | Veryfi API processing |
| `InApp` | `dataExtractionEngine = VeryfiInApp` | `CREDIT_CARD`, `BARCODE`, `CODE`, identity verification. Other types get a plain success with no extraction. |
| `DecoupledLocalPackage` | `None`, `uploadWithoutProcessingIsOn = false`, `autoDeleteLocalResourcesAfterProcessing = false` | Retain `data.local_package_path`; customer uploads + processes |
| `DecoupledRemotePackage` | `None`, `uploadWithoutProcessingIsOn = true` | Lens uploads without processing; `data.bucket` / `data.package_path` |
| `VisualOnly` | `None`, `uploadWithoutProcessingIsOn = false` | Local package / PDF only |

## CaptureFlow

| ID | Flags |
|----|-------|
| `ReviewThenSubmit` | `autoSubmitDocumentOnCapture = false`, `batchUploadOnCamera = false`, `imagePreviewIsOn = true` |
| `AutoSubmit` | `autoSubmitDocumentOnCapture = true`, `batchUploadOnCamera = false` |
| `BatchToMaxThenReview` | `batchUploadOnCamera = true`, `autoSubmitDocumentOnCapture = false`, `packageMaxScans` set |

Do not combine AutoSubmit with BatchToMaxThenReview. `batchUploadOnCamera` is overridden from stored prefs when `moreMenuIsOn = true` (Superman mode toggle).

## CaptureSources

| ID | Flags |
|----|-------|
| `CameraOnly` | `galleryIsOn = false`, `showBrowserButton = false`, `browseIsOn = false`, `dictateIsOn = false` |
| `CameraPlusGallery` | `galleryIsOn = true`, `galleryButtonWithFilesIsOn = false` |
| `CameraPlusFiles` | `showBrowserButton = true`, or `galleryIsOn = true` + `galleryButtonWithFilesIsOn = true`, or `moreMenuIsOn = true` + `browseIsOn = true` |
| `FilesFirst` | `VeryfiLens.showDocumentBrowser()` / `showGallery()` standalone |
| `NoUiUpload` | `VeryfiLens.uploadImages(uris, isSameTransaction)` — UI SDK, no screens |

`galleryOtherIsOn` / `browseOtherIsOn` only when `OTHER` is in `documentTypes`. Import auto-crop: `autoCropGalleryIsOn` / `autoCropBrowserIsOn` also require `autoDocDetectionAndCropIsOn` (camera-side imports check the stored preference) — best effort.

## QualityGate

| ID | Flags |
|----|-------|
| `Lenient` | `allowSubmitUndetectedDocsIsOn = true` (umbrella: also lets blur and glare dialogs continue) |
| `Balanced` | `blurDetectionIsOn = true`, `allowSubmitUndetectedDocsIsOn = false`, `allowSubmitBlurredDocsIsOn = true` |
| `StrictBanking` | `allowSubmitUndetectedDocsIsOn = false`, `validateCheckSides`, `validateCheckSize`, `forceCropLayoutOnRetake = true`, `captureMarginRatio = 0.08` |

Android extra: `allowSubmitLowQualityDocsIsOn` (default true) controls the image-size dialog.

## MultiPage

| ID | Flags |
|----|-------|
| `SingleFrame` | `stitchIsOn = false`, `multiplePagesCaptureIsOn = false` |
| `StitchToPdf` | `stitchIsOn = true`, `multiplePagesCaptureIsOn = true`, `singleScanPackages = false`, `returnStitchedPDF = true` |
| `SeparatePackages` | `multipleDocumentsIsOn = true`, `stitchIsOn = true`, `singleScanPackages = true` |

## Compliance

| ID | Flags |
|----|-------|
| `NoGalleryBackup` | `backupDocsToGallery = false` (and `moreMenuIsOn = false` so users cannot re-enable it) |
| `KeepLocalResources` | `autoDeleteLocalResourcesAfterProcessing = false` |
| `NoScreenshotSubmit` | `allowSubmittingScreenshots = false` |
| `NoDeviceScreenshots` | `enableScreenshots = false` |
| `DeviceBlocklist` | `deviceBlockListIsOn = true` |
| `SameFileSession` | `sameFileUploadPreventionPolicy = Session` |
| `SameFileAlways` | `Always` + `VeryfiCloudAPI` (hashes are persisted only after a cloud upload) |

Fraud detection needs backend `detect_fraud_on`; field detection needs entitlement and a Field Detection / Remote Models / Lite artifact.

## IdentityExtras

| ID | Flags |
|----|-------|
| `BackOptional` | `driverLicenseBackIsOn` / `insuranceCardBackIsOn` (default **true**) with `*BackIsRequired = false`; any-document: `anyDocumentBackIsOn = true` + `packageMaxScans = 2` |
| `BackRequired` | same + `*BackIsRequired = true` |
| `FaceVerify` | `identityVerificationIsOn = true`, `VeryfiInApp` (Demo), optional `identityVerificationThreshold` |
| `CropLayoutGuide` | Fixed: `cropLayoutIsOn = true` + `autoDocDetectionAndCropIsOn = false`. Hybrid: `cropLayoutIsOn = true` + `cropLayoutWithAutoDetection = true`. |

## BankingExtras

| ID | Flags |
|----|-------|
| `CheckBack` | `checkSequenceMode = true`, `checksBackIsOn = true`, `forceLandscapeImage = true` |
| `CheckValidation` | `validateCheckSides`, `validateCheckSize`, allow-submit check flags; `validateCheckCorners` only if customer wants it |
| `CheckTiff` | `retrieveCheckTiffIsOn`, optional `rejectInvalidCheckTIFF`; Remote Models artifact |

## Chrome

| ID | Flags |
|----|-------|
| `GuideAnimation` | `showGuideShortCounter`, `showGuideCounter`, `showPrescriptionLabelGuideCounter`, `animationDisplayDuration`, `showHelpButton` + `showDefaultScanningGuide` |
| `CameraHelpOrInfo` | Help (`showHelpButton`, built-in guide or `onHelpButtonClicked`) **or** `showInfoButton` fragment; `cameraInfoView` banner is independent |
| `ScanSessionCounter` | `scanSessionCounterIsOn` (default false). `autoScanSessionCounterIsOn` (default true) lets Lens count successful submissions; set it false and call `VeryfiLens.updateScanSessionCounter(count)` for a host-driven badge. Leave both off for a first scan |
| `Minimal` | `moreMenuIsOn = false`, `moreSettingsMenuIsOn = false`, `shareLogsIsOn = false`, `showSupermanModeIsOn = false` |

## Branding

Apply last: `primaryColor`/`accentColor`/`secondaryColor` (+ `Dark`), submit button colors, `dialog*` / `alertDialog*` colors, `toolbarIconsColor`, `docDetectFillUIColor`, crop-layout colors, fonts (`font`, `buttonFont`, `textFont`, `titleFont`), button sizing (`buttonHeight`, `buttonFontSize`, `buttonFullWidth`, …), `brandImage`, `notificationChannelName`, `notificationSmallIcon`. Never required for a first scan.

## Recipes → groups

| Recipe | Groups |
|--------|--------|
| `cgp-expense` | CloudProcess + ReviewThenSubmit + CameraPlusGallery + Balanced + StitchToPdf + Minimal |
| `banking-checks` | CloudProcess + ReviewThenSubmit + StrictBanking + SingleFrame + CheckBack + CheckValidation + Minimal |
| `any-documents` | CloudProcess + ReviewThenSubmit + CameraOnly + Balanced + IdentityExtras + Minimal |
| `decoupled-capture` | DecoupledLocalPackage + ReviewThenSubmit + CameraPlusGallery + Balanced + StitchToPdf + Minimal |
| `decoupled-remote-package` | DecoupledRemotePackage + same |
| `visual-scan-to-pdf` | VisualOnly + AutoSubmit + StitchToPdf + Minimal |
| `cpg-barcode` / `cpg-code` / `credit-cards` | InApp + CameraOnly + Minimal |
