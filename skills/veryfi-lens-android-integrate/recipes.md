# Recipes

Apply on a fresh `VeryfiLensSettings()` (Kotlin `apply { }`). Always **base hardening**, then **one** recipe, then optional overlays from [settings-groups.md](settings-groups.md). Skip flags the host did not ask for. Enum values: `VeryfiLensSettings.ExtractionEngine.VeryfiCloudAPI | VeryfiInApp | None`; `DocumentType.RECEIPT`, etc.

## Base hardening (every client)

```kotlin
val settings = VeryfiLensSettings().apply {
    ignoreRemoteSettings = true            // unless the host wants dashboard lens_settings overrides
    backupDocsToGallery = false            // default true: every scan copied to the device gallery
    moreMenuIsOn = false                   // default true: user overrides persist and beat host values
    moreSettingsMenuIsOn = false
    autoCaptureOtherIsOn = false           // default true
    showNoDocumentDetectedWarning = false  // usual choice
    dictateIsOn = false                    // obsolete; default true
    locationServicesIsOn = false
    glareDetectionIsOn = false             // Android default true; suboptimal detector
    enableScreenshots = true               // device screenshots of Lens screens; imported files are allowSubmittingScreenshots
}
```

If the host displays captured files after callbacks, or keeps several packages in flight:

```kotlin
settings.autoDeleteLocalResourcesAfterProcessing = false  // host owns File.delete() / cleanLensStorage()
```

## CGP / expense (`cgp-expense`)

**Groups:** CloudProcess, ReviewThenSubmit, CameraPlusGallery, Balanced, StitchToPdf (optional), Minimal. **Artifact:** Receipts or Full; Field Detection / Remote Models only for `documentFieldsToDetect`.

```kotlin
settings.apply {
    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiCloudAPI
    documentTypes = arrayListOf(DocumentType.RECEIPT, DocumentType.INVOICE, DocumentType.OTHER)
    showDocumentTypes = true               // needed to switch when more than one type
    defaultSelectedDocumentType = DocumentType.RECEIPT
    autoSubmitDocumentOnCapture = false
    batchUploadOnCamera = false
    imagePreviewIsOn = true
    galleryIsOn = true
    browseIsOn = false                     // only matters with the More menu
    showBrowserButton = false
    autoCaptureIsOn = false
    autoDocDetectionAndCropIsOn = true
    autoRotateIsOn = true                  // Android default false
    autoSkewCorrectionIsOn = true          // Android default false
    cleanBorderIsOn = true                 // Android default false
    stitchIsOn = true
    stitchOtherIsOn = false
    multipleDocumentsIsOn = true
    multiplePagesCaptureIsOn = true
    singleScanPackages = false
    returnStitchedPDF = true
    packageMaxScans = 10                   // Android default 5
    closeCameraOnSubmit = true
    sameFileUploadPreventionPolicy = VeryfiLensSettings.SameFileUploadPreventionPolicy.Session
    webhookIsOn = false
}
```

Paid Field Detection (entitlement + Field Detection / Remote Models / Lite artifact):

```kotlin
settings.documentFieldsToDetect = arrayListOf(
    VeryfiLensSettings.DocumentFields.Vendor, VeryfiLensSettings.DocumentFields.Date,
    VeryfiLensSettings.DocumentFields.Time, VeryfiLensSettings.DocumentFields.Total,
    VeryfiLensSettings.DocumentFields.Tax,
)
settings.allowCorrectingLocalExtractedFields = true
settings.allowSubmittingDocumentWithoutFields = true
```

**Do not set:** check/ID/OCR flags, `ocrRegex`, `cameraProcessingMode`, `uploadWithoutProcessingIsOn`.

## Banking / checks (`banking-checks`)

**Groups:** CloudProcess, ReviewThenSubmit, StrictBanking, SingleFrame, CheckBack, CheckValidation, Minimal. **Artifact:** `veryfi-lens-cheques-sdk` for capture; Remote Models for TIFF or when size matters.

```kotlin
settings.apply {
    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiCloudAPI
    documentTypes = arrayListOf(DocumentType.CHECK)
    showDocumentTypes = false
    stitchIsOn = false
    autoCaptureIsOn = false
    galleryIsOn = true
    checksBackIsOn = true
    checksBackIsRequired = false          // true if both sides always required
    checkSequenceMode = true              // front-then-back UI (Demo default)
    forceLandscapeImage = true
    autoSelectManualCropIsOn = true
    autoRotateIsOn = true
    captureMarginRatio = 0.08
    checksCropMargin = 2
    checksMinAspectRatio = 1.9
    checksMaxAspectRatio = 2.85
    forceCropLayoutOnRetake = true        // show the guide right after a failed detection
    cropLayoutShowDelay = 15.0            // show the guide after 15 s on camera
    cropLayoutAspectRatio = 2.18
    cropLayoutWidth = 60
    cropLayoutCornerRadius = 5
    cropLayoutOverlayAlpha = 0.3
    validateCheckSize = true
    validateCheckSides = true
    validateCheckCorners = false          // Demo sets true; algorithmic and can be aggressive — customer choice
    allowSubmitCheckSideNotDetected = true
    allowSubmitCheckCornersNotDetected = true
    allowSubmitUndetectedDocsIsOn = false
    returnStitchedPDF = true
}
```

Remote Models artifact only (TIFF model not bundled in Cheques):

```kotlin
settings.retrieveCheckTiffIsOn = true      // success: img_check_front_tiff_path / img_check_back_tiff_path
settings.rejectInvalidCheckTIFF = true     // retake if TIFF cannot be built
```

**Do not set:** `autoSubmitDocumentOnCapture`, `batchUploadOnCamera`, receipt stitch, `useLegacyModels` (the Demo sets it false; no public contract).

## Any-Documents (`any-documents`)

**Artifact:** Full, Any Docs, Business Cards (for ID/nutrition/shipping shortcuts), Identity Verification, or Prescription Labels — check [install.md](install.md#flavor--artifact--allowed-documenttypes).

```kotlin
settings.apply {
    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiCloudAPI
    documentTypes = arrayListOf(DocumentType.ANY_DOCUMENT)
    anyDocumentTemplate = "us_passport"   // template confirmed by support
    showDocumentTypes = false
    cameraProcessingMode = VeryfiLensSettings.CameraProcessingMode.Card // Document for page-shaped docs
    stitchIsOn = false
    galleryIsOn = false
    autoCaptureIsOn = false
    autoRotateIsOn = true
    cleanBorderIsOn = true
    returnStitchedPDF = true
}
```

Back side: `anyDocumentBackIsOn = true`, `anyDocumentBackIsRequired = false`, **`packageMaxScans = 2`** (exactly 2, else no prompt). `DRIVER_LICENSE` / `INSURANCE_CARD` use `driverLicenseBackIsOn` / `insuranceCardBackIsOn` (both **default true** on Android) and ignore `anyDocumentBackIsOn`.

## Decoupled capture (`decoupled-capture`)

```kotlin
settings.apply {
    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.None
    uploadWithoutProcessingIsOn = false          // local zip; customer uploads + processes
    autoDeleteLocalResourcesAfterProcessing = false
    returnStitchedPDF = true
    autoSubmitDocumentOnCapture = false
    batchUploadOnCamera = false
    imagePreviewIsOn = true
    autoDocDetectionAndCropIsOn = true
    autoRotateIsOn = true
    autoSkewCorrectionIsOn = true
    cleanBorderIsOn = true
    multiplePagesCaptureIsOn = true
    multipleDocumentsIsOn = true
    singleScanPackages = false
    stitchIsOn = true
    packageMaxScans = 10
    galleryIsOn = true
    showBrowserButton = true
    webhookIsOn = false
    sameFileUploadPreventionPolicy = VeryfiLensSettings.SameFileUploadPreventionPolicy.Session
}
```

In `veryfiLensSuccess`, read `json.optJSONObject("data")?.optString("local_package_path")` (the SDK merges it into `data`). Delete it yourself after upload.

### Overlay: remote package (`decoupled-remote-package`)

```kotlin
settings.dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.None  // required
settings.uploadWithoutProcessingIsOn = true   // Lens uploads; does not process
```

Success `data` carries `bucket` and `package_path` for the customer's backend.

## Visual scan-to-PDF (`visual-scan-to-pdf`)

```kotlin
settings.apply {
    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.None
    uploadWithoutProcessingIsOn = false
    autoSubmitDocumentOnCapture = true
    returnStitchedPDF = true                     // img_stitched_pdf_path
    autoDeleteLocalResourcesAfterProcessing = false
    autoCaptureIsOn = false
    autoDocDetectionAndCropIsOn = true
    autoSkewCorrectionIsOn = true
    multiplePagesCaptureIsOn = true
    multipleDocumentsIsOn = true
    stitchIsOn = true
    showDocumentTypes = false
    documentTypes = arrayListOf(DocumentType.RECEIPT)   // detection model only
    galleryIsOn = true
    showBrowserButton = true
}
```

Credentials are still required for partners (credentialless scan-to-PDF is Demo-only).

## CPG / barcode (`cpg-barcode`)

```kotlin
settings.apply {
    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiInApp
    documentTypes = arrayListOf(DocumentType.BARCODE)
    showDocumentTypes = false
    autoCaptureIsOn = true
    galleryIsOn = false
    stitchIsOn = false
    autoLightDetectionIsOn = false
    continuousAutoFocus = true
}
```

## CPG / code OCR (`cpg-code`)

```kotlin
settings.apply {
    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiInApp  // local OCR result
    documentTypes = arrayListOf(DocumentType.CODE)
    showDocumentTypes = false
    autoCaptureIsOn = true
    galleryIsOn = false                        // forced off for CODE when prefs are loaded anyway
    stitchIsOn = false
    autoLightDetectionIsOn = false
    continuousAutoFocus = true
    ocrType = VeryfiLensSettings.OcrType.Strips  // Caps for bottle caps
    ocrRegex = "[A-Z0-9]{10}"                  // replace with the customer's pattern
}
```

## Credit cards (`credit-cards`)

```kotlin
settings.apply {
    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiInApp  // forced anyway by configure
    documentTypes = arrayListOf(DocumentType.CREDIT_CARD)
    showDocumentTypes = false
    autoCaptureIsOn = true                     // forced true when the card type is active
    galleryIsOn = false
    stitchIsOn = false
    autoLightDetectionIsOn = false             // forced false by configure
    autoRotateIsOn = true
    cropLayoutIsOn = true                      // Android hybrid needs BOTH layout flags
    cropLayoutWithAutoDetection = true
    cropLayoutWidth = 80
    cropLayoutHeight = 25
    cropLayoutCornerRadius = 15
    cropLayoutStroke = 16
    cropLayoutBorderColor = "#FFFFFFFF"
    creditCardMarginTop = 100
    creditCardMarginBottom = 0
}
```

## Tax / statements (W-2, W-9, bank statement)

```kotlin
settings.apply {
    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiCloudAPI
    documentTypes = arrayListOf(DocumentType.W2)   // W9, BANK_STATEMENT
    showDocumentTypes = false
    autoCaptureIsOn = false
    stitchIsOn = false                             // true for multi-page bank statements
    galleryIsOn = true
    autoRotateIsOn = true
    cleanBorderIsOn = true
    returnStitchedPDF = true
}
```

## Long receipts

```kotlin
settings.apply {
    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiCloudAPI
    documentTypes = arrayListOf(DocumentType.LONG_RECEIPT)
    showDocumentTypes = false
    stitchIsOn = true
    multiplePagesCaptureIsOn = true
    returnStitchedPDF = true
    // autoCaptureIsOn is forced false for LONG_RECEIPT when stored prefs are loaded
}
```

## Overlays

### CaptureFlow.BatchToMaxThenReview

```kotlin
settings.batchUploadOnCamera = true
settings.autoSubmitDocumentOnCapture = false
settings.packageMaxScans = 10
```

### CaptureFlow.AutoSubmit

```kotlin
settings.autoSubmitDocumentOnCapture = true
settings.batchUploadOnCamera = false
```

### CaptureSources.Files

```kotlin
settings.showBrowserButton = true                 // top-right browse icon
// or: gallery button opens a Photos/Files choice
settings.galleryIsOn = true
settings.galleryButtonWithFilesIsOn = true
// or: More menu Import card (only with moreMenuIsOn = true)
settings.browseIsOn = true
```

Standalone file flow without camera: `VeryfiLens.showDocumentBrowser()`.

### CropLayout (Android semantics)

From `CameraManager.shouldCrop` / `CaptureFragmentUIManager.addCropLayoutView`:

```kotlin
// Pure fixed frame: layout visible, hard crop to the frame, no ML crop.
settings.cropLayoutIsOn = true
settings.autoDocDetectionAndCropIsOn = false
```

```kotlin
// Hybrid: frame visible + detection/green box inside it. Android needs BOTH flags.
settings.cropLayoutIsOn = true
settings.cropLayoutWithAutoDetection = true
settings.autoDocDetectionAndCropIsOn = true
settings.docDetectFillUIColor = "#6600FF00"
settings.docDetectStrokeUIColor = "#FF00FF00"
```

Shape: `cropLayoutWidth` / `cropLayoutHeight` (percent) or `cropLayoutAspectRatio`; `cropLayoutCornerRadius`; circle when `cropLayoutAspectRatio == 1.0` and `cropLayoutCornerRadius >= 1000`. Styling: `cropLayoutBorderColor`, `cropLayoutStroke`, `cropLayoutOverlayColor`, `cropLayoutOverlayAlpha`, `cropLayoutTipMessage`.

### GuideAnimation

```kotlin
settings.showGuideShortCounter = 2        // receipt
settings.showGuideCounter = 2             // long receipt
settings.showPrescriptionLabelGuideCounter = 2
settings.animationDisplayDuration = 10.0
settings.showHelpButton = true
settings.showDefaultScanningGuide = true  // false → onHelpButtonClicked(parentFragment)
```

### Compliance.Strict

```kotlin
settings.allowSubmittingScreenshots = false
settings.fraudDetectionIsOn = true          // needs backend detect_fraud_on
settings.showLCDIsNotAllowed = true
settings.deviceBlockListIsOn = true
settings.sameFileUploadPreventionPolicy = VeryfiLensSettings.SameFileUploadPreventionPolicy.Always // CloudAPI only
```

Resubmission under `Always`: `VeryfiLens.allowReuploadingFiles(uri)` / `(file)` / `(fingerprint)`.

### KeepLocalResources

```kotlin
settings.autoDeleteLocalResourcesAfterProcessing = false  // host deletes; see implementation.md
```
