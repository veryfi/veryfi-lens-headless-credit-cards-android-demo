# Implementation (Lens UI SDK)

Everything here is the **UI SDK** (`com.veryfi.lens.VeryfiLens`, any UI flavor artifact). The Headless SDKs are separate: [headless.md](headless.md).

## Imports

```kotlin
import com.veryfi.lens.VeryfiLens
import com.veryfi.lens.VeryfiLensDelegate
import com.veryfi.lens.helpers.DocumentType
import com.veryfi.lens.helpers.VeryfiLensCredentials
import com.veryfi.lens.helpers.VeryfiLensSettings
import org.json.JSONObject
```

## Credentials

```kotlin
val credentials = VeryfiLensCredentials().apply {
    clientId = BuildConfig.VERYFI_CLIENT_ID   // from your secrets, never hard-coded in VCS
    username = BuildConfig.VERYFI_USERNAME
    apiKey = BuildConfig.VERYFI_API_KEY
    // url = "..."  // only if Veryfi gave you a URL; the Demo does not set it
}
```

- All four are `var String` (default `""`).
- A non-empty invalid `url` shows the Toast "The URL [..] is not valid." (configure continues).
- `hasEmptyCredentialFields()` returns true only when **all** fields are blank. Credentialless scanning is armed only for the Demo package — partners always need credentials.
- There is **no** `invalid_credentials` delegate error on Android (iOS has one). Bad or missing credentials surface as `configure` callback `false` and later upload errors.

## Configure

Signatures (both `@JvmStatic`):

```kotlin
VeryfiLens.configure(application: Application, veryfiLensCredentials: VeryfiLensCredentials, settings: VeryfiLensSettings?)
VeryfiLens.configure(application: Application, veryfiLensCredentials: VeryfiLensCredentials, settings: VeryfiLensSettings?, callback: (Boolean) -> Unit)
```

Always use the callback overload and present UI only after `true`:

```kotlin
VeryfiLens.setDelegate(lensDelegate)
VeryfiLens.configure(application, credentials, settings) { ok ->
    if (ok) VeryfiLens.showCamera() else showConfigError()
}
```

What `configure` does, in order (from `VeryfiLens.kt`):

1. Loads native libraries (`NativeLibraryLoader.install`), sets preferences, validates `url`, stores credentials and **the same settings object** (`settings ?: VeryfiLensSettings()`).
2. `checkCreditCardSettings()`: if `documentTypes[0] == CREDIT_CARD`, forces `dataExtractionEngine = VeryfiInApp` and `autoLightDetectionIsOn = false`.
3. `checkUserUISettings()`: if the user has stored preferences **and** `moreMenuIsOn`, stored values override host values of `autoDocDetectionAndCropIsOn`, `blurDetectionIsOn`, `glareDetectionIsOn`, `autoLightDetectionIsOn`, `autoCaptureIsOn`, `backupDocsToGallery`, `closeCameraOnSubmit`, `batchUploadOnCamera`. Otherwise host values are written to preferences. While loading, `galleryIsOn` is forced false for `CREDIT_CARD` / `CODE` and `autoCaptureIsOn` false for `LONG_RECEIPT`.
4. Nexus 5X: Toast "Device Nexus 5X not supported".
5. Callback overload: `PartnerHelper.validateClientId` — returns `true` immediately (no network) if a valid region is cached; with no network and no cache returns `false` and shows a partner message. The `lens_settings` remote merge mutates your settings object unless `ignoreRemoteSettings = true`. Signed v3 verification runs only when the active host is `api.veryfi.com` or `devapi.veryfi.com`. Any other `overrideBaseUrl` uses unsigned v2. The host does not implement signature checks.

Keep a reference to the settings you pass: Lens mutates it (steps 2–3, remote merge, and during capture — e.g. `CREDIT_CARD` sets `autoCaptureIsOn = true`). Rebuild a fresh `VeryfiLensSettings()` and call `configure` again to change configuration.

## Delegate

```kotlin
interface VeryfiLensDelegate {
    fun veryfiLensClose(json: JSONObject)
    fun veryfiLensUpdate(json: JSONObject)
    fun veryfiLensError(json: JSONObject)
    fun veryfiLensSuccess(json: JSONObject)
    fun onHelpButtonClicked(parentFragment: Fragment) {}   // optional
}
```

- `setDelegate` stores a **strong** reference in the `VeryfiLens` singleton and registers an EventBus subscriber; `removeDelegate()` clears both. Prefer an Application-scoped delegate (or call `removeDelegate()` in `onDestroy`) to avoid leaking an Activity.
- Callbacks arrive on the **main thread**.
- Success: if `data` is a JSON string, it is parsed into a nested `JSONObject` before delivery.
- Update: `detected_objects` and `lcd_scores` are parsed into `JSONArray`s.
- Error: the SDK also appends/uploads its logs.

Minimal delegate:

```kotlin
object LensDelegate : VeryfiLensDelegate {
    override fun veryfiLensClose(json: JSONObject) { /* status "close"; queued uploads may continue */ }
    override fun veryfiLensUpdate(json: JSONObject) {
        when (json.optString("status")) {
            "start" -> { /* package created: package_id, img_thumbnail_path(s) */ }
            "inprogress" -> { /* progress */ }
            "failed" -> { /* upload failed: json.optString("error") */ }
        }
    }
    override fun veryfiLensError(json: JSONObject) { /* json.optString("error") / "msg" */ }
    override fun veryfiLensSuccess(json: JSONObject) {
        val packageId = json.optString("package_id")
        val data = json.optJSONObject("data")   // cloud result, in-app card/barcode JSON, or decoupled payload
    }
}
```

## Callback JSON

Keys (`PackageUploadEvent` / `NotifyServerHelper`, snake_case): `package_id`, `upload_id`, `receipt_id`, `document_type`, `data`, `source`, `status`, `connectivity`, `progress`, `exception`, `error`, `msg`, `img_original_data`, `img_original_path`, `img_original_paths`, `img_thumbnail_path`, `img_thumbnail_paths`, `img_thumbnail_data`, `img_stitched_pdf_path`, `img_check_front_tiff_path`, `img_check_back_tiff_path`, `file_hashes`, `img_paths`, `images`, `framework-version`, `framework-build`, `session_scan_count`, `scan_session_counter`, `queue_count`, `scan_to_pdf_pages`, `device_id`, `type`, `empty_fields`, `local_package_path`.

| Status | Where | Meaning |
|--------|-------|---------|
| `start` | update | package created |
| `inprogress` | update | uploading; progress also arrives as `msg = "progress"`, `data = <progress>` |
| `failed` | update/error | **Android spelling** (iOS: `fail`) |
| `error` | error | error |
| `clear_package` | update | local package cleaned |
| `close` | close | Lens UI closed |
| `done` | success | finished |

Notes:

- Checks: `img_original_paths` order is reversed relative to capture (Unconfirmed which side is first — verify with a device test).
- File validation messages: `invalid_file`, `empty_file`, `image_too_small`, `invalid_pdf_file`. Other: `image_is_screenshot`, `blurred_image`, `storage_permission_missing`.
- Success is not guaranteed to be a single event per camera session: each package produces its own events.

Per-engine `success` payload:

| Engine / type | `data` / top-level content |
|---------------|----------------------------|
| `VeryfiCloudAPI` | Veryfi API result in `data` |
| `VeryfiInApp` + `CREDIT_CARD` | on-device card JSON string (parsed into `data`) |
| `VeryfiInApp` + `BARCODE` | on-device barcodes JSON (parsed into `data`) |
| `VeryfiInApp` + `CODE` (OCR) | top-level `ocr_uuid`, `ocr_text`, `ocr_score`, `ocr_image` (local path), `ocr_type`; progress updates carry `ocr_uuid`/`ocr_text`/`ocr_score` |
| `VeryfiInApp` + `identityVerificationIsOn` | `similarity_score`, `face1_path`, `face2_path`, `is_match` |
| `VeryfiInApp` + any other type | plain success; **no extraction** |
| `None` + `uploadWithoutProcessingIsOn = true` | remote package: `bucket`, `package_path` (plus `document_type`) |
| `None` + `uploadWithoutProcessingIsOn = false` | local: `data.local_package_path` when local resources are retained |

InApp and None still post a doc-ready **metadata** notification (document information, no image) to Veryfi.

## Launchers

| Call | Effect |
|------|--------|
| `VeryfiLens.showCamera()` | Flavor/type check, then starts `CaptureActivity` (`FLAG_ACTIVITY_NEW_TASK`). Debounced. |
| `VeryfiLens.showGallery()` | Lens gallery flow; re-validates in background. Debounced. |
| `VeryfiLens.showDocumentBrowser()` | System file picker flow; re-validates in background. Debounced. |
| `VeryfiLens.closeCamera()` | Closes the capture activity. |

Only `showCamera()` runs the flavor check. Do not start two Lens flows at once.

Camera import entry points (independent): More menu Import card (`moreMenuIsOn` + `browseIsOn`), top-right browse icon (`showBrowserButton`, also shown whenever `showInfoButton != null`), gallery button Photos/Files menu (`galleryIsOn` + `galleryButtonWithFilesIsOn`).

## No-UI upload (`uploadImages`) — UI SDK, not Headless

```kotlin
VeryfiLens.uploadImages(uris: Array<Uri>?, isSameTransaction: Boolean = true)
VeryfiLens.uploadImages(paths: Array<String>?, isSameTransaction: Boolean = true)
```

Requires a completed `configure` with the same credentials/settings/delegate. Uses the same foreground `UploadDocumentsService` and the same callbacks as the camera.

| | `Uri` overload | `paths` overload |
|---|---|---|
| Permission | API < 33: `READ/WRITE_EXTERNAL_STORAGE` must be granted, else error `storage_permission_missing` | none checked |
| Images | decoded, optional auto-crop (`autoCropGalleryIsOn`) | `BitmapFactory.decodeFile`; optional auto-crop |
| PDFs / other files | copied to temp and packaged as file scans typed as `documentTypes[0]` (or RECEIPT) | **silently ignored** (not decodable) |
| `null` | — | uses the default Pictures `/image.jpg` |
| Nothing usable | — | no callback at all |

Both overloads:

- `isSameTransaction = true` → one package; `false` → one package per image.
- Screenshot + `allowSubmittingScreenshots = false` → `failed` / `image_is_screenshot`.
- `nonInteractiveBlurCheckIsOn = true` + blurred → `blurred_image`. Otherwise blurred images may be dropped from the session without a callback (Unconfirmed; see [clarifications.md](clarifications.md)).
- Share-intent use: pass the received `Uri`s from the main app process after `configure`. Android has no share-extension mode.

## Queue and storage utilities

| Call | Effect |
|------|--------|
| `packagesInQueue(): ArrayList<String>` | package IDs still in the upload queue |
| `retryUpload(packageId)` / `retryAll()` | restart upload via the foreground service |
| `discardUpload(packageId)` / `discardAll()` | drop queued packages |
| `cleanLensStorage()` | `discardAll()` + clear local Lens resources |
| `allowReuploadingFiles(fingerprint: String)` / `(file: File)` / `(uri: Uri)` | clear a hash from `sameFileUploadPreventionPolicy` tracking |
| `updateCameraInfoView(fragment)` | replace `cameraInfoView` and re-deliver to the open `CaptureActivity` |
| `updateScanSessionCounter(count)` | set the camera badge when `scanSessionCounterIsOn` is true and `autoScanSessionCounterIsOn` is false; negative values clamp to 0; other modes no-op |
| `shareOCRFeedback()` | upload OCR feedback logs (OCR flavor) |
| `startCrashLogger()` / `stopCrashLogger()` | SDK crash logging |

The badge stays hidden unless `scanSessionCounterIsOn` is true. With `autoScanSessionCounterIsOn` true (the default), Lens updates it from successful submissions. With that flag false, the host calls `updateScanSessionCounter`. Both flags off, or the counter off, is a no-op.

Submit moves the package into a processing session and starts a new capture session before upload. Opening the camera again does not show or drop a package already handed to upload.

Public but not for partners: `attachDocument(documentId)` (Veryfi-owned attach mode — Unconfirmed), `delegateLensSuccess` / `delegateLensUpdate` (internal), `removeHandsFree()`, `checkCloseCamera()`, `disableViaVoiceAnimation()`, `disableBlurDetection()` (they mutate stored user preferences).

## Local resources and who deletes them

- Default `autoDeleteLocalResourcesAfterProcessing = true`: images/zip are removed after processing. Copy anything you need **inside** the callback.
- Set `false` when the host shows captured images later, keeps several packages in flight, or uses local decoupled capture. The host then owns deletion.
- Retained zips live under `<getExternalFilesDir(null) or filesDir>/veryfi/zip/`. `cleanLensStorage()` clears Lens Pictures and base-dir package zips; whether it also clears `veryfi/zip/` is **Unconfirmed** — delete `local_package_path` yourself with `File(path).delete()` after upload.

## Decoupled capture (customer-owned Process a Document)

`dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.None`:

- **Local package** (`uploadWithoutProcessingIsOn = false`, `autoDeleteLocalResourcesAfterProcessing = false`): success carries `data.local_package_path`; host uploads the zip and calls Process a Document.
- **Remote package** (`uploadWithoutProcessingIsOn = true`): Lens uploads to Veryfi storage without processing; success carries `bucket` and `package_path` for the customer's backend.

Cloud-only response flags (`boundingBoxesIsOn`, `confidenceDetailsIsOn`, `parseAddressIsOn`, `detectBlurResponseIsOn`, `boostModeIsOn`, `computeIsOn`, `webhookIsOn`) do not shape the customer's later request.

## Help, info view, camera banner

- `showHelpButton = true` shows a Help control. With `showDefaultScanningGuide = true` Lens replays its guide; otherwise Lens calls `onHelpButtonClicked(parentFragment)` so the host can show its own UI (e.g. a `DialogFragment` via `parentFragment.childFragmentManager`).
- `showInfoButton: Fragment?` — non-null shows an info control that opens the fragment in Lens `InfoActivity`; it **also** shows the browse icon and switches the close icon. Help and info are separate views on Android; whether both fit together is Unconfirmed — pick one.
- `cameraInfoView: Fragment?` — banner at the top of the camera; replace while open with `VeryfiLens.updateCameraInfoView(fragment)`.

## Strings and branding

Android has no `customLensStrings`. Lens strings are Android string resources in the AAR (with translations). Overriding a library string by redeclaring the same resource name in the host's `res/values*/strings.xml` is standard resource merging, but resource names are not a documented contract (Unconfirmed). See [customizable strings](https://docs.veryfi.com/lens/mobile/settings/customizable_strings/). Colors are hex strings (`"#RRGGBB"` / `"#AARRGGBB"`); fonts are `String?` names; `brandImage` / `notificationSmallIcon` are drawable resource IDs.

## Core (not for partners)

`com.veryfi.lens.core.VeryfiLensCore.showEmail()` / `showDictation()` require `veryfi-lens-core-sdk`. Dictation is obsolete. Do not recommend Core to partners.
