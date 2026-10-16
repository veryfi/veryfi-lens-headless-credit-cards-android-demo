# Template: Long receipts

**Artifact:** `com.veryfi.lens:veryfi-lens-long-receipts-sdk:2.7.0.6`
**Allowed types:** LONG_RECEIPT, RECEIPT.
**Engine:** `VeryfiCloudAPI`. Wiring: [full.md](full.md#wiring-canonical).

```kotlin
fun longReceiptsSettings() = VeryfiLensSettings().apply {
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
    documentTypes = arrayListOf(DocumentType.LONG_RECEIPT)  // add RECEIPT + showDocumentTypes = true for both
    showDocumentTypes = false
    autoCaptureIsOn = false                    // SDK forces false for LONG_RECEIPT anyway
    stitchIsOn = true
    multiplePagesCaptureIsOn = true
    returnStitchedPDF = true                   // img_stitched_pdf_path
    showGuideCounter = 2                       // long-receipt guide animation, at most twice
    galleryIsOn = true
}
```

Optional: `originalLongImageMaxSizeInMB` (0 = use `originalImageMaxSizeInMB`), `brandImage` (drawable res id shown in the stitching UI).
