# Troubleshooting (Android)

Symptom → likely cause → fix. Check [conflicts.md](conflicts.md) for flag combinations. Lens logs use `LogHelper` (logcat); error callbacks also upload SDK logs.

## Build / install

| Symptom | Cause | Fix |
|---------|-------|-----|
| `Could not find com.veryfi.lens:…` | Nexus repo missing or no access | Add `https://nexus.veryfi.com/repository/maven-releases/`; ask Veryfi for access; credentials from Gradle properties |
| Duplicate class / resource merge errors | Two Lens UI flavors, or Core + a UI flavor | Keep exactly one UI artifact |
| Manifest merger: provider authority conflict | Host declares `${applicationId}.veryfi.lens.fileprovider` or re-declares Lens providers | Remove the host duplicate |
| `Manifest merger failed` on `application@allowBackup` (`true` from the app, `false` from `com.veryfi.lens`) | Android Studio’s empty-app template sets `android:allowBackup="true"`. Lens UI, helpers, and headless manifests set `false` | On `<application>`, set `android:allowBackup="false"` and `tools:replace="android:allowBackup"`. Declare `xmlns:tools` on `<manifest>`. Append `android:allowBackup` if `tools:replace` already lists other attributes. Keep `true` only when the host must enable Auto Backup |
| `UnsatisfiedLinkError` on emulator | x86 image with Lite (ARM-only) or missing ABI split | ARM image / real device; keep `armeabi-v7a`, `arm64-v8a` in `abiFilters` |
| Lite crash on first `configure` | Split not installed, or emulated split without `NativeLibraryLoader.setSplitName` | Install the module first; set split name before configure |
| Play Console warns about location/storage permissions | Merged from the SDK manifest | `tools:node="remove"` + keep the related settings off (Unconfirmed full tolerance) |

## Configure / launch

| Symptom | Cause | Fix |
|---------|-------|-----|
| `showCamera()` does nothing, no callback | Called before configure, or debounced second tap | Present inside `configure { ok -> }`; avoid double taps |
| Toast "Please use only valid document types" | Artifact does not allow a type | [install.md](install.md#flavor--artifact--allowed-documenttypes) |
| Toast "Please specify the document types" | `documentTypes == null` | Set it |
| Toast "The URL [..] is not valid." | Bad `credentials.url` | Remove or fix `url` |
| `configure` callback `false` | No network on first run with no cached region, or invalid credentials | Retry with network; verify `clientId` / `username` / `apiKey` |
| Toast "Device Nexus 5X not supported" | Device blocklist in SDK | Unsupported device |
| Settings changes in code are ignored for existing users | `moreMenuIsOn = true` loads stored prefs over host values | `moreMenuIsOn = false` |
| Settings differ from code on some accounts | Dashboard `lens_settings` merged | `ignoreRemoteSettings = true` |
| Credit card screen ignores Cloud engine / auto light | `configure` forces InApp + auto light off for credit cards | Expected |

## Capture

| Symptom | Cause | Fix |
|---------|-------|-----|
| OTHER auto-captures unexpectedly | `autoCaptureOtherIsOn` default true | Set false |
| Scans appear in the phone gallery | `backupDocsToGallery` default true (or user toggled it in More) | false + More menu off |
| Glare dialog blocks users | `glareDetectionIsOn` default true on Android | false, or `allowSubmitUndetectedDocsIsOn = true` |
| Blur dialog loop in low light | Blur gate without allow-anyway | Torch / `tryAgainLightIsOn` / `allowSubmitBlurredDocsIsOn = true` |
| Crop frame not visible / ML crop off | Crop-layout flag mismatch | [recipes.md](recipes.md#croplayout-android-semantics) |
| Driver license / insurance card asks for back | `driverLicenseBackIsOn` / `insuranceCardBackIsOn` default true | Set false if not wanted |
| Any-document back not prompted | `packageMaxScans != 2` | Exactly 2 |
| Location prompt | `locationServicesIsOn = true` | false |

## Upload / callbacks

| Symptom | Cause | Fix |
|---------|-------|-----|
| No delegate calls at all | `setDelegate` never called, or `removeDelegate` called; delegate object recreated | Set once on an app-scoped object |
| Host checks `status == "fail"` and never matches | Android uses `"failed"` | Compare `"failed"` |
| `data` is a string in iOS code, object here | Android parses `data` into `JSONObject` when possible | `optJSONObject("data")`, fallback `optString` |
| Files missing when the host opens them later | `autoDeleteLocalResourcesAfterProcessing = true` | false, or copy in the callback |
| `uploadImages(paths)` with PDF → nothing | Paths overload decodes bitmaps only | `uploadImages(uris)` |
| `storage_permission_missing` | `uploadImages(uris)` on API < 33 without permission | Request storage first |
| `image_is_screenshot` | `allowSubmittingScreenshots = false` | Expected policy |
| Some images silently dropped in `uploadImages` | Blurred without `nonInteractiveBlurCheckIsOn` (Unconfirmed) | `nonInteractiveBlurCheckIsOn = true` to get `blurred_image` |
| Duplicates not prevented | `Always` without Cloud engine | Cloud engine or `Session` |
| Upload notification icon broken (Lite) | `notificationSmallIcon` points into the split | 0 or a base-module drawable |
| Uploads stuck | Queue waiting (network) | `packagesInQueue()`, `retryUpload(id)` / `retryAll()`; `discardAll()` to drop |
| InApp success has no fields for receipts | InApp does not extract receipts | `VeryfiCloudAPI` |
| OCR success has no `data` | Local OCR returns top-level `ocr_text` etc. | Read top-level keys |

## Headless

| Symptom | Cause | Fix |
|---------|-------|-----|
| No callbacks | Headless `setDelegate` not called on the headless object (different package) | Use the matching `VeryfiLensHeadless` import |
| Rectangles offset from preview | Coordinates are in frame (buffer) space | Map with rotation and scale yourself |
| Receipts never auto-capture | `autoCaptureIsOn = false` | true, or call `captureFrame` |
| Expecting cloud extraction from Headless Receipts | Headless does not upload images | Host uploads `file_paths` via its backend |
| Credit card back side not merged | State not reset | `reset()` between sides; merge on host |
