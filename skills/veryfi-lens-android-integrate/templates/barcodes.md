# Template: Barcodes

**Artifact:** `com.veryfi.lens:veryfi-lens-barcodes-sdk:2.7.0.6` (BARCODE is also allowed on Full, Remote Models, Lite).
**Allowed types:** BARCODE.
**Engine:** `VeryfiInApp` — success `data` is the on-device `BarcodesDetector` JSON. Wiring: [full.md](full.md#wiring-canonical).

```kotlin
fun barcodeSettings() = VeryfiLensSettings().apply {
    ignoreRemoteSettings = true
    backupDocsToGallery = false
    moreMenuIsOn = false
    moreSettingsMenuIsOn = false
    showNoDocumentDetectedWarning = false
    dictateIsOn = false
    locationServicesIsOn = false
    glareDetectionIsOn = false

    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiInApp
    documentTypes = arrayListOf(DocumentType.BARCODE)
    showDocumentTypes = false
    autoCaptureIsOn = true
    galleryIsOn = false
    stitchIsOn = false
    autoLightDetectionIsOn = false
    continuousAutoFocus = true                 // small codes held close
}
```

Barcode JSON key set is not documented in code comments — log one payload before parsing (Unconfirmed shape).

Not the same as `barcodeExtractionIsOn`, which asks the Cloud API to read barcodes on receipts/bills.
