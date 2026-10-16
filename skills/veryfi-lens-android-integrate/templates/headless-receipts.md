# Template: Headless Receipts (app-owned camera)

**Artifact:** `com.veryfi.lens.headless:receipts-sdk:2.7.0.6` — separate from every Lens UI artifact. Details: [headless.md](../headless.md).

Not the same as the UI SDK's `VeryfiLens.uploadImages(...)`: this SDK detects and crops receipts on device and returns local file paths. It does **not** upload images or run extraction; the host owns any upload.

## Gradle

```groovy
dependencies {
    implementation "com.veryfi.lens.headless:receipts-sdk:2.7.0.6"
    // CameraX (host-owned camera), versions per your project
    implementation "androidx.camera:camera-camera2:<version>"
    implementation "androidx.camera:camera-lifecycle:<version>"
    implementation "androidx.camera:camera-view:<version>"
}
```

Host requests `Manifest.permission.CAMERA` at runtime before binding CameraX.

## Configure + delegate

```kotlin
import com.veryfi.lens.headless.receipts.VeryfiLensHeadless
import com.veryfi.lens.headless.receipts.VeryfiLensHeadlessDelegate
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
    autoCaptureIsOn = true              // processFrame auto-captures; false = rectangles only
    autoDocDetectionAndCropIsOn = true
}

VeryfiLensHeadless.setDelegate(object : VeryfiLensHeadlessDelegate {
    override fun veryfiLensClose(json: JSONObject) {}
    override fun veryfiLensError(json: JSONObject) {}
    override fun veryfiLensUpdate(json: JSONObject) {
        val rectangles = json.optJSONArray("rectangles")  // [{corner0:{x,y}, …, corner3:{x,y}}] in frame space
    }
    override fun veryfiLensSuccess(json: JSONObject) {
        val paths = json.optJSONArray("file_paths")      // local cropped images; host uploads/deletes
        val blurred = json.optBoolean("is_blurred")
        val cropped = json.optBoolean("is_cropped")
        val detected = json.optBoolean("is_document_detected")
        val sessionId = json.optString("session_id")
    }
})

VeryfiLensHeadless.configure(application, credentials, settings) { ok ->
    if (ok) bindCamera() else showConfigError()
}
```

## Frames

```kotlin
val analysis = ImageAnalysis.Builder()
    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
    .setTargetRotation(Surface.ROTATION_0)     // Demo uses sensor orientation
    .build()
analysis.setAnalyzer(executor) { image ->
    VeryfiLensHeadless.processFrame(Frame(image.toNv21(), image.width, image.height))
    image.close()
}

// Manual shutter with ImageCapture (JPEG): plane[0] bytes
imageCapture.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
    override fun onCaptureSuccess(image: ImageProxy) {
        val buffer = image.planes[0].buffer
        val jpeg = ByteArray(buffer.remaining()).also { buffer.get(it) }
        VeryfiLensHeadless.captureFrame(Frame(jpeg, image.width, image.height))
        image.close()
    }
})

// Imported bitmap
VeryfiLensHeadless.processGalleryImage(bitmap)   // honors autoCropGalleryIsOn
```

`toNv21()` is **host code** (the Demo's `ImageProxyUtils.yuvToByteArray` is not SDK API). Simple, unoptimized version:

```kotlin
fun ImageProxy.toNv21(): ByteArray {
    val out = ByteArray(width * height * 3 / 2)
    var pos = 0
    val y = planes[0]; val u = planes[1]; val v = planes[2]
    for (row in 0 until height) for (col in 0 until width)
        out[pos++] = y.buffer.get(row * y.rowStride + col * y.pixelStride)
    for (row in 0 until height / 2) for (col in 0 until width / 2) {
        out[pos++] = v.buffer.get(row * v.rowStride + col * v.pixelStride)
        out[pos++] = u.buffer.get(row * u.rowStride + col * u.pixelStride)
    }
    return out
}
```

## Notes

- Frames are dropped while the processor is busy; calling on every analyzer frame is fine.
- Type is always RECEIPT; `documentTypes` is not used.
- Only doc-ready metadata (no image) is sent to Veryfi. Host deletes `file_paths` when done (SDK cleanup Unconfirmed).
- Rectangle coordinates are in unrotated buffer space; map to the preview yourself.
