# Template: Receipts

**Artifact:** `com.veryfi.lens:veryfi-lens-receipts-sdk:2.7.0.6`
**Allowed types:** RECEIPT, OTHER, INVOICE. (LONG_RECEIPT needs the Long Receipts or Full artifact.)
**Engine:** `VeryfiCloudAPI`. Wiring: [full.md](full.md#wiring-canonical).

```kotlin
fun receiptsSettings() = VeryfiLensSettings().apply {
    ignoreRemoteSettings = true
    backupDocsToGallery = false
    moreMenuIsOn = false
    moreSettingsMenuIsOn = false
    autoCaptureOtherIsOn = false
    showNoDocumentDetectedWarning = false
    dictateIsOn = false
    locationServicesIsOn = false
    glareDetectionIsOn = false

    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiCloudAPI
    documentTypes = arrayListOf(DocumentType.RECEIPT)   // add INVOICE / OTHER + showDocumentTypes = true if needed
    showDocumentTypes = false
    autoCaptureIsOn = false
    autoSubmitDocumentOnCapture = false
    imagePreviewIsOn = true
    galleryIsOn = true
    autoRotateIsOn = true
    autoSkewCorrectionIsOn = true
    cleanBorderIsOn = true
    stitchIsOn = true
    multipleDocumentsIsOn = true
    returnStitchedPDF = true
    packageMaxScans = 10
    sameFileUploadPreventionPolicy = VeryfiLensSettings.SameFileUploadPreventionPolicy.Session
}
```

Launch after `configure { ok -> }`: `VeryfiLens.showCamera()`.
Success: `data` is the Veryfi API receipt result.

Optional: batch overlay, Files overlay, guide animation — [recipes.md](../recipes.md#overlays). Paid local field detection requires the Field Detection / Remote Models artifact, not this one.
