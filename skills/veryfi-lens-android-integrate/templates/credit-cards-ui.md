# Template: Credit cards (Lens UI)

**Artifact:** `com.veryfi.lens:veryfi-lens-credit-cards-sdk:2.7.0.6` — the only UI artifact that allows CREDIT_CARD.
**Allowed types:** CREDIT_CARD.
**Engine:** `VeryfiInApp`. Card data is read on device and returned to the host; `configure` forces InApp and turns auto light off when the first type is CREDIT_CARD. Wiring: [full.md](full.md#wiring-canonical).

For an app-owned camera use [headless-credit-cards.md](headless-credit-cards.md) instead.

```kotlin
fun creditCardSettings() = VeryfiLensSettings().apply {
    ignoreRemoteSettings = true
    backupDocsToGallery = false
    moreMenuIsOn = false
    moreSettingsMenuIsOn = false
    showNoDocumentDetectedWarning = false
    dictateIsOn = false
    locationServicesIsOn = false
    glareDetectionIsOn = false

    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiInApp
    documentTypes = arrayListOf(DocumentType.CREDIT_CARD)
    showDocumentTypes = false
    autoCaptureIsOn = true                     // forced true on the card camera
    galleryIsOn = false
    stitchIsOn = false
    autoLightDetectionIsOn = false
    autoRotateIsOn = true
    cropLayoutIsOn = true                      // Android hybrid needs both flags (Demo preset)
    cropLayoutWithAutoDetection = true
    cropLayoutWidth = 80
    cropLayoutHeight = 25
    cropLayoutCornerRadius = 15
    cropLayoutStroke = 16
    cropLayoutBorderColor = "#FFFFFFFF"
    creditCardMarginTop = 100
    creditCardMarginBottom = 0
    creditCardDetectCardNumber = true
    creditCardDetectCardHolderName = true
    creditCardDetectCardDate = true
    creditCardDetectCardCVC = true
    creditCardAutoCaptureMode = VeryfiLensSettings.CreditCardAutoCaptureMode.Normal
}
```

Success: `data` = on-device card JSON produced by `CardExtraction.getCardData` (parsed to `JSONObject`). The exact key set of the UI flow is Unconfirmed — log one real payload; Headless uses `card_number`, `card_dates`, `card_name`, `card_type`, `card_cvc`, `card_uuid`.

Do not: use the Full / Remote Models / Lite artifacts; set `VeryfiCloudAPI`; enable gallery; enable `fraudDetectionIsOn` without backend entitlement.
