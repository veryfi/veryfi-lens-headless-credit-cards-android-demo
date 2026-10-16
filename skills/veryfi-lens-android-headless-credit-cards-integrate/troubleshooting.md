# Veryfi Lens Android — Headless Credit Cards — troubleshooting

Source of truth: Veryfi Lens Android SDK **2.7.0.6**, commit `7f33726c`. Names below were read from that tree. Items not fully confirmed are marked **Unconfirmed**. Do not copy repository passwords, keystores, or Demo-only types into the host app.


Lens UI logs go through `LogHelper` (logcat). Error callbacks also upload SDK logs.

## Build and launch

| Symptom | Cause | Fix |
|---------|-------|-----|
| `Could not find com.veryfi.lens.headless:credit-cards-sdk` | Nexus repository missing or no access | Add `https://nexus.veryfi.com/repository/maven-releases/`. Ask Veryfi for access. Credentials from Gradle properties |
| Duplicate class or resource merge | Two Lens UI artifacts, or Core plus a UI artifact | Keep this one dependency |
| Manifest provider authority clash | Host redeclares `${applicationId}.veryfi.lens.fileprovider` | Remove the host duplicate |
| `Manifest merger failed` on `application@allowBackup` | New-app template sets `android:allowBackup="true"`; Lens libraries set `false` | On `<application>`, set `android:allowBackup="false"` and `tools:replace="android:allowBackup"` (`xmlns:tools` on `<manifest>`). Append to an existing `tools:replace` list. Keep `true` only if the host must enable Auto Backup |
| `configure` callback false | No network on first launch with no cached region, or credentials rejected | Retry online. Check `clientId`, `username`, `apiKey` |
| Toast `The URL [..] is not valid.` | Bad `credentials.url` | Remove it or use the URL Veryfi issued |
| Settings edits ignored for returning users | `moreMenuIsOn` true loads stored prefs over the host | Set `moreMenuIsOn = false` |
| Settings differ from code on some accounts | Dashboard `lens_settings` merged | `ignoreRemoteSettings = true` |
| Camera permission crash or black preview | `CAMERA` not granted | Request it, then bind |
| `configure` false | Offline first launch or bad credentials | Retry with network |
| GLES requirement | Module manifest requires OpenGL ES 3.0 | A device with GLES 3.0 |


## This product

| Symptom | Cause | Fix |
|---------|-------|-----|
| No callbacks | Wrong `VeryfiLensHeadless` import (receipts vs creditcards) | `com.veryfi.lens.headless.creditcards.VeryfiLensHeadless` |
| Second side repeats the front | No `reset()` | `reset()` and merge on the host |
| PAN appeared in a cloud extraction response | Images were sent through a UI Cloud engine | Stay on this headless reader |
| `Error with credentials` | `configure` not completed or processor not started | Bind only after `ok` |


## Conflicts

Prefer deleting an extra flag over adding another one.

| Combination | What happens | Fix |
|-------------|--------------|-----|
| `VeryfiLens.showCamera()` | Different SDK | `processCreditCard` |
| `uploadImages` for card photos | UI file pipeline; can be the wrong privacy path | Keep frames in `processCreditCard` |
| Back side empty | State not reset | `reset()` and merge on the host |
| Toggling `VeryfiLensHelper.settings.creditCardDetectCard*` | Internal Demo pattern | **Unconfirmed**. Do not generate it |
| UI artifact plus headless artifact | Duplicate classes | One coordinate |
| Cloud engine to "extract the card" | Images must not be uploaded for extraction | On-device JSON |


| `ignoreRemoteSettings` left false | Dashboard overwrites the object | true |
| `backupDocsToGallery` left true | Scans land in the gallery; storage prompt on API < 33 | false, and More menu off so the user cannot turn it back on |
| `moreMenuIsOn` true on a returning user | Stored prefs replace host capture flags | false |
| `glareDetectionIsOn` true (Android default) and `allowSubmitUndetectedDocsIsOn` false | Glare dialog has no OK path | Glare off, or allow undetected |
| `blurDetectionIsOn` true, undetected disallowed, `allowSubmitBlurredDocsIsOn` false | Blur dialog cannot continue | Torch, `tryAgainLightIsOn`, or allow blurred |
| `allowSubmitUndetectedDocsIsOn` true while blur is meant to block | The undetected flag also lets blur and glare continue | Set both false to block |
| `sameFileUploadPreventionPolicy = Always` without `VeryfiCloudAPI` | Hashes are not persisted | `Session`, or Cloud engine |
| `None` plus `uploadWithoutProcessingIsOn = false` plus auto-delete true | No retained `local_package_path` | `autoDeleteLocalResourcesAfterProcessing = false` |
| `uploadWithoutProcessingIsOn = true` without `None` | Flag ignored | Set `ExtractionEngine.None` |
