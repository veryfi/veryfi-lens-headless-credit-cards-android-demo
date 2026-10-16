# Template: Business cards

**Artifact:** `com.veryfi.lens:veryfi-lens-business-cards-sdk:2.7.0.6`
**Allowed types:** BUSINESS_CARD (the artifact also allows INSURANCE_CARD, PASSPORT, DRIVER_LICENSE, NUTRITION_FACT, SHIPPING_LABEL).
**Engine:** `VeryfiCloudAPI` (Demo). Wiring: [full.md](full.md#wiring-canonical).

```kotlin
fun businessCardSettings() = VeryfiLensSettings().apply {
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
    documentTypes = arrayListOf(DocumentType.BUSINESS_CARD)
    showDocumentTypes = false
    stitchIsOn = false
    autoCaptureIsOn = false
    galleryIsOn = true
    autoRotateIsOn = true
    cleanBorderIsOn = true
}
```

Success: `data` = Veryfi business-card result.

If the same app also scans IDs with this artifact, those are Any-Documents shortcuts: `DRIVER_LICENSE` / `INSURANCE_CARD` back prompts default **on** (`driverLicenseBackIsOn`, `insuranceCardBackIsOn`); set them explicitly.
