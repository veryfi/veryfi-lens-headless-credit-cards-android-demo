# Conflicts and support footguns (Android)

Run after applying a recipe. Prefer fixing by **unsetting** extras rather than adding flags.

## Hard failures / silent bad UX

| Combination | Symptom | Fix |
|-------------|---------|-----|
| Artifact flavor does not allow a `documentTypes` entry | `showCamera()` shows Toast "Please use only valid document types"; camera never opens; no callback | Match artifact ([install.md](install.md#flavor--artifact--allowed-documenttypes)) or narrow types |
| `documentTypes` left `null` | Toast "Please specify the document types" | Always set `documentTypes` |
| `CREDIT_CARD` or `CODE` with Full / Remote Models / Field Detection / Lite | Flavor check rejects | Credit Cards / OCR artifacts |
| `W2`, `W9`, `BANK_STATEMENT`, `PRESCRIPTION_LABEL` on Lite | Flavor check rejects | Dedicated or Full artifact |
| Two Lens UI artifacts on the classpath | Duplicate classes / resource clashes | Exactly one UI flavor |
| `showCamera()` before `configure` callback `true` | Silently logged "application not initialized", or validation still pending | Present only inside `configure { ok -> if (ok) … }` |
| `VeryfiInApp` on receipts/checks/other types | Plain success, no extraction | `VeryfiCloudAPI` or `None` |
| `CREDIT_CARD` first type with Cloud/None | `configure` forces `VeryfiInApp` anyway | Set `VeryfiInApp` explicitly for clarity |
| `CODE` with `VeryfiCloudAPI` | Uploads to the cloud OCR endpoint; response shape differs from local `ocr_text` | Use `VeryfiInApp` for local `ocr_text` (Demo) |
| `sameFileUploadPreventionPolicy = Always` without `VeryfiCloudAPI` | Hashes never persisted → no prevention | `Session`, or Cloud engine |
| `documentFieldsToDetect` without entitlement or supporting artifact | Local fields stay off | Field Detection / Remote Models / Lite + entitlement |
| `fraudDetectionIsOn` without backend `detect_fraud_on` | Fraud detection off | Ask account owner |
| `retrieveCheckTiffIsOn` on `veryfi-lens-cheques-sdk` | No TIFF model bundled | Remote Models artifact |
| `browseIsOn = true` with `moreMenuIsOn = false` expecting a Browse entry | Nothing appears (Import card is in the More sheet) | `showBrowserButton`, `galleryButtonWithFilesIsOn`, or `showDocumentBrowser()` |
| `uploadImages(paths)` with PDFs | PDFs silently ignored, maybe no callback | `uploadImages(uris)` |
| `uploadImages(uris)` on API < 33 without storage permission | Error `storage_permission_missing` | Request `READ/WRITE_EXTERNAL_STORAGE` first |
| `autoDeleteLocalResourcesAfterProcessing = true` + host reads files later | Paths no longer exist | Set false, or copy inside the callback |
| Lite configured before the split is installed | Native load failure / crash | Configure after install; `NativeLibraryLoader.setSplitName` for emulated splits |
| Lite + `shareLogsIsOn = true` | No FileProvider in Lite | Leave false |
| Lite on x86 / x86_64 emulator | No native libs | ARM device or ARM emulator image |
| Delegate held by an Activity, never removed | Activity leak (singleton strong ref) | App-scoped delegate or `removeDelegate()` in `onDestroy` |

## More menu (Android-specific persistence)

| Combination | Problem | Fix |
|-------------|---------|-----|
| `moreMenuIsOn = true` (default) on a returning user | Stored prefs override host `autoDocDetectionAndCropIsOn`, `blurDetectionIsOn`, `glareDetectionIsOn`, `autoLightDetectionIsOn`, `autoCaptureIsOn`, `backupDocsToGallery`, `closeCameraOnSubmit`, `batchUploadOnCamera` | `moreMenuIsOn = false` |
| `moreMenuIsOn = true` + `moreSettingsMenuIsOn = true` | Submit/gallery close behavior follows stored `afterCaptureCloseView`, not `closeCameraOnSubmit` | Turn one off, or accept user control |
| `showSupermanModeIsOn` / `afterScanCloseCameraIsOn` / `browseIsOn` | Only hide rows inside the More sheet | Irrelevant with `moreMenuIsOn = false` |
| `backupDocsToGallery = false` with More menu on | User can switch backup back on | Hide the More menu |

## Competing submit / session behavior

| Combination | Problem | Fix |
|-------------|---------|-----|
| `autoSubmitDocumentOnCapture` + `batchUploadOnCamera` | Two exit policies | Pick one |
| `closeCameraOnSubmit` + `submitWithoutClose` | Contradictory | Pick one (winner Unconfirmed) |
| `clearSessionOnBack = true` (default) | Back button drops the current session | Set false if users should keep scans on back |

## Detection vs user can proceed

| Combination | Problem | Fix |
|-------------|---------|-----|
| `allowSubmitUndetectedDocsIsOn = false` + check validators without allow flags | Users trapped | Keep `allowSubmitCheck*NotDetected = true` |
| `blurDetectionIsOn` + `allowSubmitUndetectedDocsIsOn = false` + `allowSubmitBlurredDocsIsOn = false` | Blur dialog has no continue | Torch, `tryAgainLightIsOn`, `blurThreshold`, or allow blurred |
| `allowSubmitUndetectedDocsIsOn = true` while blocking blur via `allowSubmitBlurredDocsIsOn = false` | Blur still allowed (umbrella) | Set both false |
| `glareDetectionIsOn = true` (Android default) + `allowSubmitUndetectedDocsIsOn = false` | Glare dialog cannot be dismissed with OK | Turn glare off or allow undetected |
| `cropLayoutIsOn = true` + `autoDocDetectionAndCropIsOn = true` + `cropLayoutWithAutoDetection = false` | ML crop is disabled (crop = frame). For RECEIPT/OTHER the frame is force-shown; for other types the frame view is not added by `addCropLayoutView` (Unconfirmed end-to-end) | Be explicit — fixed: detection off; hybrid: add `cropLayoutWithAutoDetection = true` |
| `cropLayoutWithAutoDetection = true` without `cropLayoutIsOn` | Frame is drawn, but crop logic uses full-frame detection, not the frame | Set both (Demo credit-card preset) |
| Circular layout expected to produce a circular image | Saved crop is rectangular | Visual framing only |
| `showDefaultScanningGuide = true` + `showHelpButton = false` | No control to replay | Turn Help on |
| `showHelpButton` + `showInfoButton` expecting a clean layout | Separate controls; fit Unconfirmed; info also shows browse icon | Pick one |
| `showInfoButton != null` expecting only an info control | Also shows the browse icon and changes the close icon | Accept or use Help/`cameraInfoView` |
| `enableScreenshots` used as a file policy, or reverse | Wrong switch | `allowSubmittingScreenshots` for imports; `enableScreenshots` for device screenshots |
| `pdfPreviewIsOn` without deciding `pdfEditIsOn` | Preview vs edit copy | Decide per client |

## Types and UI

| Combination | Problem | Fix |
|-------------|---------|-----|
| `showDocumentTypes = false` with several `documentTypes` | Cannot switch | Picker on, or one type |
| `OTHER` + `autoCaptureOtherIsOn = true` (default) | OTHER auto-captures | `autoCaptureOtherIsOn = false` |
| `LONG_RECEIPT` + `autoCaptureIsOn = true` | Forced false when prefs load; auto-capture row hidden | Leave false |
| `CREDIT_CARD` + `galleryIsOn = true` | Forced false when prefs load | Leave false |
| `CREDIT_CARD` + `autoCaptureIsOn = false` | Forced true on the camera | Leave true |
| `cameraProcessingMode` without `ANY_DOCUMENT` | KDoc: only valid for any document | Set only for `ANY_DOCUMENT` |
| `anyDocumentBackIsOn` with `packageMaxScans != 2` | No back prompt | `packageMaxScans = 2` |
| Leaving `driverLicenseBackIsOn` / `insuranceCardBackIsOn` default true when back not wanted | Back prompt appears | Set false |
| `stitchOtherIsOn` without `OTHER` | Dead flag | Skip |

## Packaging

| Combination | Problem | Fix |
|-------------|---------|-----|
| `stitchIsOn` + `singleScanPackages` + `returnStitchedPDF` | Mixed semantics | StitchToPdf **or** SeparatePackages |
| `stitchIsOn = true` on checks/cards | Wrong UX | false |
| `packageMaxScans = 1` + back required | Cannot capture back | ≥ 2 |
| `galleryMultipleSelectionIsOn` / `browserMultipleSelectionIsOn` with `packageMaxScans = 1` | Capped | Raise max |

## Remote, privacy

| Combination | Problem | Fix |
|-------------|---------|-----|
| `ignoreRemoteSettings = false` (default) | Dashboard `lens_settings` overwrite your object on configure | true |
| `backupDocsToGallery = true` (default) | Scans copied to gallery; storage permission prompt on API < 33 | false |
| `locationServicesIsOn = true` | Runtime location prompt | false unless needed |
| `dictateIsOn = true` (default) | Obsolete control | false |
| `shareLogsIsOn = true` in production | Users can export logs | false |

## Engine pairing

| Combination | Problem | Fix |
|-------------|---------|-----|
| `None` expecting extracted JSON | No fields | Cloud or InApp |
| `None` + `uploadWithoutProcessingIsOn = false` + auto-delete true | No retained `local_package_path` | `autoDeleteLocalResourcesAfterProcessing = false` |
| `uploadWithoutProcessingIsOn = true` without `None` | Flag ignored | `None` |
| `None` + cloud response flags | No effect | Put options on the customer's API call |
| `webhookIsOn = true` + host waits for immediate `success` data | Async results missed | Handle webhook or turn off |
| `anyDocumentType` set | Deprecated local regex OCR | Use `anyDocumentTemplate` |
| `ocrRegex` / `ocrType` outside `CODE` | Ignored | OCR artifact + `CODE` |
