# Template: OCR bottle caps

**Artifact:** `com.veryfi.lens:veryfi-lens-ocr-sdk:2.7.0.6`
**Allowed types:** CODE.
**Engine:** `VeryfiInApp`. Same wiring and result handling as [ocr-code-strips.md](ocr-code-strips.md) (`ocr_type` = `"caps"`).

```kotlin
fun ocrCapsSettings() = VeryfiLensSettings().apply {
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
    galleryIsOn = false
    stitchIsOn = false
    autoLightDetectionIsOn = false
    continuousAutoFocus = true
    ocrType = VeryfiLensSettings.OcrType.Caps  // circular view; caps models
    ocrRadius = 60                             // circle radius (caps only)
    ocrBorderStroke = 10                       // circle border width (caps only)
    ocrBorderColor = "#48E195"
    ocrRegex = "[A-Z0-9]{10}"                  // replace with the customer's pattern
}
```

`ocrViewWidth` / `ocrViewHeight` / `ocrViewCornerRadius` shape the strips rectangle; with Caps the view shape is a circle (`FocusView.Shape.Circle`).
