# Veryfi Lens Android — Headless Credit Cards — settings

Source of truth: Veryfi Lens Android SDK **2.7.0.6**, commit `7f33726c`. Names below were read from that tree. Items not fully confirmed are marked **Unconfirmed**. Do not copy repository passwords, keystores, or Demo-only types into the host app.


Always construct a fresh `VeryfiLensSettings()`. Android has no `VeryfiLens*Settings` subclasses. Pass that instance to `configure`. The SDK mutates it.

```kotlin
import com.veryfi.lens.helpers.DocumentType
import com.veryfi.lens.helpers.VeryfiLensSettings

fun headlessCardSettings() = VeryfiLensSettings().apply {
    ignoreRemoteSettings = true
    creditCardDetectCardNumber = true
    creditCardDetectCardHolderName = true
    creditCardDetectCardDate = true
    creditCardDetectCardCVC = true
    creditCardAutoCaptureMode = VeryfiLensSettings.CreditCardAutoCaptureMode.Normal
    creditCardMarginTop = 100
    creditCardMarginBottom = 0
}

```

## Defaults versus this recipe

Android initializers below are the property defaults in `VeryfiLensSettings`.

| Property | Android default | This recipe | Why |
|----------|-----------------|-------------|-----|
| `ignoreRemoteSettings` | false | true | Keep host settings from being replaced by `lens_settings` |
| `creditCardDetectCardNumber` | true | true | Set false to skip PAN |
| `creditCardDetectCardHolderName` | true | true | Set false to skip the name |
| `creditCardDetectCardDate` | true | true | Set false to skip dates |
| `creditCardDetectCardCVC` | true | true | Set false to skip CVC |
| `creditCardAutoCaptureMode` | `Normal` | `Normal` | `Speed` or `Accuracy` |
| `creditCardMarginTop` | 100 | 100 | Top inset for the reader |
| `creditCardMarginBottom` | 0 | 0 | Bottom inset |


These are the settings the headless credit-card reader reads. UI flags such as `galleryIsOn`, stitch, and `documentTypes` are not how this SDK selects a card flow.

`configure` on this module still calls `VeryfiLensHelper.checkCreditCardSettings()`, which forces `VeryfiInApp` and turns auto light off only when `documentTypes[0]` is `CREDIT_CARD`. This recipe does not set `documentTypes`. Card reading uses `processCreditCard` regardless. Forcing the engine is therefore **Unconfirmed** on the headless path unless `documentTypes` starts with `CREDIT_CARD`.

Do not set `dataExtractionEngine` to `VeryfiCloudAPI` in order to upload card images.


## Suggestions

- Keep detected fields on the host. Call `reset()` before the other side. Fill only missing keys.
- Call `close()` from `onDestroy` and unbind CameraX.
- Lens-provided camera UI is `com.veryfi.lens:veryfi-lens-credit-cards-sdk:2.7.0.6`, a different `configure`.
- Compare installed size of `credit-cards-sdk` and, only if Veryfi asks, `credit-cards-sdk-full`. Do not quote AAR size.

