# Veryfi Lens Android — Headless Credit Cards — implementation

Source of truth: Veryfi Lens Android SDK **2.7.0.6**, commit `7f33726c`. Names below were read from that tree. Items not fully confirmed are marked **Unconfirmed**. Do not copy repository passwords, keystores, or Demo-only types into the host app.


Façade: `com.veryfi.lens.headless.creditcards.VeryfiLensHeadless`.  
Delegate: `com.veryfi.lens.headless.creditcards.VeryfiLensHeadlessDelegate` (four methods).  
`Frame`, `VeryfiLensCredentials`, and `VeryfiLensSettings` live in `com.veryfi.lens.helpers`.

Card values are read on device and delivered to the delegate. The doc-ready request (`NotifyServerHelper.notifyServerOfCreditCardReady`) sends booleans for which fields were non-empty (`card_number`, `card_dates`, `card_name`, `card_cvc`, `card_type`), `data_extraction_engine` = `mobile`, `document_type` = `credit_card`, and `sdk` = `lens-headless-credit-card`. It does not send the PAN string. `card_dates` in the detector JSON is a `JSONArray`, while the notify method calls `getString` on that key — whether the dates boolean is actually posted is **Unconfirmed**.

This object has no `uploadImages`. The Lens UI method `VeryfiLens.uploadImages` is a different SDK (`com.veryfi.lens:veryfi-lens-credit-cards-sdk` uses `com.veryfi.lens.VeryfiLens` and still keeps card images off the Cloud image-upload branch; see that product's own notes). Do not route card frames through `uploadImages`.

## Credentials, delegate, configure

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

VeryfiLensHeadless.setDelegate(object : VeryfiLensHeadlessDelegate {
    override fun veryfiLensClose(json: JSONObject) {}
    override fun veryfiLensError(json: JSONObject) {}
    override fun veryfiLensUpdate(json: JSONObject) { /* partial card fields */ }
    override fun veryfiLensSuccess(json: JSONObject) {
        val number = json.optString("card_number")
        val dates = json.optJSONArray("card_dates")
        val name = json.optString("card_name")
        val type = json.optString("card_type")
        val cvc = json.optString("card_cvc")
        val id = json.optString("card_uuid")
    }
})

VeryfiLensHeadless.configure(application, credentials, headlessCardSettings()) { ok ->
    if (ok) bindCamera() else showConfigError()
}
```

There is no public `removeDelegate`. Call `VeryfiLensHeadless.close()` from `onDestroy` to release the detector, and unbind CameraX.

## CameraX lifecycle

Request `CAMERA` before binding. Preview plus `ImageAnalysis` is the reader. `processCreditCard` expects NV21 bytes from `ImageAnalysis`.

```kotlin
private var cameraProvider: ProcessCameraProvider? = null
private val analysisExecutor = Executors.newSingleThreadExecutor()

fun bindCamera() {
    val future = ProcessCameraProvider.getInstance(context)
    future.addListener({
        val provider = future.get()
        cameraProvider = provider
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
        val analysis = ImageAnalysis.Builder()
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        analysis.setAnalyzer(analysisExecutor) { image ->
            try {
                VeryfiLensHeadless.processCreditCard(
                    Frame(image.toNv21(), image.width, image.height)
                )
            } finally {
                image.close()
            }
        }
        provider.unbindAll()
        provider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            analysis,
        )
    }, ContextCompat.getMainExecutor(context))
}

fun onDestroyScreen() {
    VeryfiLensHeadless.close()
    cameraProvider?.unbindAll()
    analysisExecutor.shutdown()
}
```

`toNv21()` is host code. The Demo `ImageProxyUtils` / `CameraAnalyzer` types are not SDK API.

```kotlin
import androidx.camera.core.ImageProxy

fun ImageProxy.toNv21(): ByteArray {
    val y = planes[0]
    val u = planes[1]
    val v = planes[2]
    val out = ByteArray(width * height * 3 / 2)
    var pos = 0
    val yBuf = y.buffer.duplicate()
    val uBuf = u.buffer.duplicate()
    val vBuf = v.buffer.duplicate()
    for (row in 0 until height) {
        for (col in 0 until width) {
            out[pos++] = yBuf.get(row * y.rowStride + col * y.pixelStride)
        }
    }
    for (row in 0 until height / 2) {
        for (col in 0 until width / 2) {
            out[pos++] = vBuf.get(row * v.rowStride + col * v.pixelStride)
            out[pos++] = uBuf.get(row * u.rowStride + col * u.pixelStride)
        }
    }
    return out
}
```

`Frame` is `com.veryfi.lens.helpers.Frame(data: ByteArray, width: Int, height: Int, frameRate: Int = 30)`. Both headless SDKs drop a frame when `imageProcessorBusy` is true, so calling on every analyzer frame is safe. Rectangle and card coordinates are in buffer space. Map them onto the preview yourself (rotation and scale).


## Front and back

Keep the merged card on the host. After the first side, call `VeryfiLensHeadless.reset()` and scan the other side. Fill only missing fields. The Demo flips `VeryfiLensHelper.settings.creditCardDetectCard*` between sides. `VeryfiLensHelper` is internal. That mutation is **Unconfirmed** as a client contract.
