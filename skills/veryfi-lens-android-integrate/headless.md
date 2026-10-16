# Headless SDKs (host-owned camera)

Two **separate** artifacts, each with its own `VeryfiLensHeadless` object and delegate. They share `VeryfiLensCredentials`, `VeryfiLensSettings`, and `Frame` from `com.veryfi.lens.helpers`. They do **not** open Lens UI and do **not** use `VeryfiLens.configure`, `VeryfiLens.setDelegate`, or `uploadImages`.

Not the same as UI-SDK no-UI upload: `VeryfiLens.uploadImages(...)` ([implementation.md](implementation.md#no-ui-upload-uploadimages--ui-sdk-not-headless)) uploads files through the Lens pipeline; the Headless SDKs only detect/crop/read on device.

The host must: request `CAMERA`, run CameraX (`Preview` + `ImageAnalysis`, and `ImageCapture` for receipts), convert frames to bytes, and draw any overlay. The Demo's `ImageProxyUtils.yuvToByteArray` / `CameraAnalyzer` are **Demo code**, not SDK API — write your own conversion.

`Frame(data: ByteArray, width: Int, height: Int, frameRate: Int = 30)`.

Both SDKs drop frames while busy (`imageProcessorBusy`), so it is safe to call on every analyzer frame.

## Headless Receipts

Dependency: `com.veryfi.lens.headless:receipts-sdk:2.7.0.6` (or `receipts-sdk-full`).

```kotlin
import com.veryfi.lens.headless.receipts.VeryfiLensHeadless
import com.veryfi.lens.headless.receipts.VeryfiLensHeadlessDelegate
```

| API | Input | Notes |
|-----|-------|-------|
| `configure(application, credentials, headlessSettings: VeryfiLensSettings, callback: (Boolean) -> Unit)` | — | Only public configure overload |
| `setDelegate(VeryfiLensHeadlessDelegate)` | — | Four methods, no help callback |
| `processFrame(frame)` | live NV21/YUV bytes from `ImageAnalysis` | Detects document; with `autoCaptureIsOn = true` auto-captures, otherwise reports rectangles only |
| `captureFrame(frame)` | JPEG bytes (Demo reads `ImageCapture` plane[0]) | Manual capture + crop |
| `processGalleryImage(image: Bitmap)` | bitmap | Crop an imported image |

Callbacks:

- `veryfiLensUpdate`: `rectangles` = array of `{corner0..corner3: {x, y}}` (frame coordinates — map them to your preview yourself).
- `veryfiLensSuccess`: `session_id`, `file_paths` (array of local cropped image paths), `is_blurred`, `is_cropped`, `is_document_detected`.

Behavior:

- Document type is always RECEIPT.
- It posts a doc-ready **metadata** notification to Veryfi (`sdk = lens_headless_receipts`, engine `mobile`). It does **not** upload images or call Process a Document; the host owns any upload of `file_paths` (e.g. via its backend and the Veryfi API).
- Settings read directly by the headless module: `autoCaptureIsOn`, `autoDocDetectionAndCropIsOn`, `autoCropGalleryIsOn` (for `processGalleryImage`), `originalImageMaxSizeInMB`; plus `ignoreRemoteSettings` during configure validation. Shared native/helper code may read others (e.g. blur) — Unconfirmed.
- Host owns deletion of `file_paths` (Unconfirmed whether the SDK cleans them).

## Headless Credit Cards

Dependency: `com.veryfi.lens.headless:credit-cards-sdk:2.7.0.6` (or `credit-cards-sdk-full`).

```kotlin
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadless
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadlessDelegate
```

| API | Notes |
|-----|-------|
| `configure(application, credentials, headlessSettings, callback)` | only public overload |
| `setDelegate(VeryfiLensHeadlessDelegate)` | four methods |
| `processCreditCard(frame)` | NV21 bytes from `ImageAnalysis` |
| `reset()` | clear accumulated card state (e.g. before reading the back) |
| `close()` | release the detector |

Callbacks (`update` while reading, `success` when done): `card_number`, `card_dates` (array), `card_name`, `card_type`, `card_cvc`, `card_uuid`.

Settings read: `creditCardDetectCardNumber`, `creditCardDetectCardHolderName`, `creditCardDetectCardDate`, `creditCardDetectCardCVC` (all default true), `creditCardAutoCaptureMode` (`Speed` / `Normal` / `Accuracy`, default `Normal`), `creditCardMarginTop` (100), `creditCardMarginBottom` (0).

Behavior:

- Card data stays on device. The doc-ready notification only sends booleans for which fields were found (engine `mobile`).
- Front/back: the Demo, after a partial result, sets `VeryfiLensHelper.settings.creditCardDetectCard* = false` for fields already found and calls `reset()` before scanning the other side. `VeryfiLensHelper` is an SDK-internal helper object; mutating it is Demo practice, not a documented contract (Unconfirmed). Safer: keep the result on the host, call `reset()`, and merge fields yourself.
- Call `close()` when the screen is destroyed.

See [templates/headless-receipts.md](templates/headless-receipts.md) and [templates/headless-credit-cards.md](templates/headless-credit-cards.md).
