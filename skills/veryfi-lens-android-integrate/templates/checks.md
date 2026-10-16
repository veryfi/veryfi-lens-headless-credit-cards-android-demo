# Template: Checks

**Artifact:** `com.veryfi.lens:veryfi-lens-cheques-sdk:2.7.0.6` (capture). Use `veryfi-lens-remote-models-sdk` when check TIFF is needed or size matters.
**Allowed types:** CHECK.
**Engine:** `VeryfiCloudAPI`. Wiring: [full.md](full.md#wiring-canonical).

```kotlin
fun checksSettings() = VeryfiLensSettings().apply {
    ignoreRemoteSettings = true
    backupDocsToGallery = false
    moreMenuIsOn = false
    moreSettingsMenuIsOn = false
    showNoDocumentDetectedWarning = false
    dictateIsOn = false
    locationServicesIsOn = false
    glareDetectionIsOn = false

    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiCloudAPI
    documentTypes = arrayListOf(DocumentType.CHECK)
    showDocumentTypes = false
    stitchIsOn = false
    autoCaptureIsOn = false
    checkSequenceMode = true                   // front-then-back UI
    checksBackIsOn = true
    checksBackIsRequired = false               // true to force both sides
    forceLandscapeImage = true
    autoSelectManualCropIsOn = true
    autoRotateIsOn = true
    captureMarginRatio = 0.08
    checksCropMargin = 2
    checksMinAspectRatio = 1.9
    checksMaxAspectRatio = 2.85
    forceCropLayoutOnRetake = true
    cropLayoutShowDelay = 15.0
    cropLayoutAspectRatio = 2.18
    cropLayoutWidth = 60
    cropLayoutCornerRadius = 5
    cropLayoutOverlayAlpha = 0.3
    validateCheckSize = true
    validateCheckSides = true
    validateCheckCorners = false               // customer choice; can be aggressive
    allowSubmitCheckSideNotDetected = true
    allowSubmitCheckCornersNotDetected = true
    allowSubmitUndetectedDocsIsOn = false
    returnStitchedPDF = true
}
```

TIFF (Remote Models artifact only):

```kotlin
settings.retrieveCheckTiffIsOn = true
settings.rejectInvalidCheckTIFF = true
```

Success: `data` = Veryfi check result; TIFF paths in `img_check_front_tiff_path` / `img_check_back_tiff_path`. `img_original_paths` order is reversed in code — verify side order on a device.

Optional paid: `fraudDetectionIsOn = true` (needs backend `detect_fraud_on`). Do not set `useLegacyModels` (Demo sets it false; no contract).
