# Template: OCR code strips

**Artifact:** `com.veryfi.lens:veryfi-lens-ocr-sdk:2.7.0.6` — the only artifact that allows CODE.
**Allowed types:** CODE.
**Engine:** `VeryfiInApp` (Demo) → local OCR result. Wiring: [full.md](full.md#wiring-canonical).

```kotlin
fun ocrStripsSettings() = VeryfiLensSettings().apply {
    ignoreRemoteSettings = true
    backupDocsToGallery = false
    moreMenuIsOn = false
    moreSettingsMenuIsOn = false
    showNoDocumentDetectedWarning = false
    dictateIsOn = false
    locationServicesIsOn = false
    glareDetectionIsOn = false

    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiInApp
    documentTypes = arrayListOf(DocumentType.CODE)
    showDocumentTypes = false
    autoCaptureIsOn = true
    galleryIsOn = false                        // forced off for CODE on prefs load
    stitchIsOn = false
    autoLightDetectionIsOn = false
    continuousAutoFocus = true
    ocrType = VeryfiLensSettings.OcrType.Strips
    ocrRegex = "[A-Z0-9]{10}"                  // replace with the customer's code pattern
    ocrViewWidth = 85                          // dp (Demo)
    ocrViewHeight = 8                          // dp (Demo)
    ocrViewCornerRadius = 20                   // dp
    ocrBorderColor = "#48E195"
    usesAlternativeOCRModel = false
}
```

## Result

```kotlin
override fun veryfiLensUpdate(json: JSONObject) {
    val partial = json.optString("ocr_text")      // live progress: ocr_uuid, ocr_text, ocr_score
}
override fun veryfiLensSuccess(json: JSONObject) {
    val text = json.optString("ocr_text")         // top level, not in data
    val score = json.optDouble("ocr_score")
    val imagePath = json.optString("ocr_image")   // local cropped image
    val id = json.optString("ocr_uuid")
    // json.optString("ocr_type") == "strips"
}
```

With `VeryfiCloudAPI` the image is uploaded to the cloud OCR endpoint instead; the Demo then reads `data.code`, `data.score`, `data.img_url`, `data.id` (Unconfirmed as a contract).

`VeryfiLens.shareOCRFeedback()` uploads OCR feedback logs (optional).
