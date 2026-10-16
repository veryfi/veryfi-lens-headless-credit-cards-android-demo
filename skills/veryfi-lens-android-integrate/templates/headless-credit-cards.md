# Template: Headless Credit Cards (app-owned camera)

**Artifact:** `com.veryfi.lens.headless:credit-cards-sdk:2.7.0.6` — separate from the Lens UI artifacts. Details: [headless.md](../headless.md).

Card data is read on device and returned to the host; the SDK sends Veryfi only booleans for which fields were found. No images are uploaded.

## Gradle

```groovy
dependencies {
    implementation "com.veryfi.lens.headless:credit-cards-sdk:2.7.0.6"
    implementation "androidx.camera:camera-camera2:<version>"
    implementation "androidx.camera:camera-lifecycle:<version>"
    implementation "androidx.camera:camera-view:<version>"
}
```

Host requests `CAMERA` at runtime.

## Configure + delegate

```kotlin
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadless
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadlessDelegate
import com.veryfi.lens.helpers.Frame
import com.veryfi.lens.helpers.VeryfiLensCredentials
import com.veryfi.lens.helpers.VeryfiLensSettings
import org.json.JSONObject

val credentials = VeryfiLensCredentials().apply {
    clientId = BuildConfig.VERYFI_CLIENT_ID
    username = BuildConfig.VERYFI_USERNAME
    apiKey = BuildConfig.VERYFI_API_KEY
}
val settings = VeryfiLensSettings().apply {
    ignoreRemoteSettings = true
    creditCardDetectCardNumber = true
    creditCardDetectCardHolderName = true
    creditCardDetectCardDate = true
    creditCardDetectCardCVC = true
    creditCardAutoCaptureMode = VeryfiLensSettings.CreditCardAutoCaptureMode.Normal // Speed | Accuracy
    creditCardMarginTop = 100
    creditCardMarginBottom = 0
}

VeryfiLensHeadless.setDelegate(object : VeryfiLensHeadlessDelegate {
    override fun veryfiLensClose(json: JSONObject) {}
    override fun veryfiLensError(json: JSONObject) {}
    override fun veryfiLensUpdate(json: JSONObject) { showPartial(json) }   // same keys as success, partial
    override fun veryfiLensSuccess(json: JSONObject) {
        val number = json.optString("card_number")
        val dates = json.optJSONArray("card_dates")
        val name = json.optString("card_name")
        val type = json.optString("card_type")
        val cvc = json.optString("card_cvc")
        val id = json.optString("card_uuid")
    }
})

VeryfiLensHeadless.configure(application, credentials, settings) { ok ->
    if (ok) bindCamera()
}
```

## Frames

```kotlin
val analysis = ImageAnalysis.Builder()
    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
    .build()
analysis.setAnalyzer(executor) { image ->
    VeryfiLensHeadless.processCreditCard(Frame(image.toNv21(), image.width, image.height))
    image.close()
}
```

`toNv21()`: host code — see [headless-receipts.md](headless-receipts.md#frames).

## Front and back

Keep the merged result on the host. After the first side, call `VeryfiLensHeadless.reset()` and scan the other side; fill only missing fields. (The Demo instead flips `VeryfiLensHelper.settings.creditCardDetectCard*` for fields already found — internal helper, Unconfirmed as contract.)

## Lifecycle

- `VeryfiLensHeadless.close()` in `onDestroy`.
- For a Lens-provided camera UI instead, use [credit-cards-ui.md](credit-cards-ui.md).
