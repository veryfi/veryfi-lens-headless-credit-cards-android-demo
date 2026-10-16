---
name: veryfi-lens-android-headless-credit-cards-integrate
description: "Integrates the Veryfi Lens Headless Credit Cards SDK (com.veryfi.lens.headless:credit-cards-sdk:2.7.0.6) with host-owned CameraX: configure, processCreditCard, reset, close, and VeryfiLensHeadlessDelegate card JSON. Use when the Android app owns the camera and reads cards on device. This is not VeryfiLens.uploadImages and not the Lens UI credit-card activity."
---

# Veryfi Lens Android — Headless Credit Cards

Walk a host Android app from Gradle to a working first scan. Change the fewest flags. Do not invent SDK APIs. Facts were read from the Android SDK at version `2.7.0.6` (commit `7f33726c`). Anything not confirmed there is marked **Unconfirmed**.

**Coordinate:** `com.veryfi.lens.headless:credit-cards-sdk:2.7.0.6`  
**Gradle flavor:** `lensHeadlessCreditCards`  
**Allowed document types:** Card capture only. There is no `documentTypes` list and no `showCamera()`.

Public entry point: `com.veryfi.lens.headless.creditcards.VeryfiLensHeadless` with `com.veryfi.lens.headless.creditcards.VeryfiLensHeadlessDelegate`. Settings type: `com.veryfi.lens.helpers.VeryfiLensSettings`. `Frame` is `com.veryfi.lens.helpers.Frame`.

This folder is complete by itself. Duplicate guidance across products is intentional.

## Pick a workflow

- **Quick** — the host already asked for this product. Ask nothing else. Apply the settings in [settings.md](settings.md), wire [implementation.md](implementation.md), and state the assumptions below.
- **Guided** — something material is missing. Ask only the unknown questions (at most four, one message), then continue.

### Quick workflow

```
veryfi-lens-android-headless-credit-cards-integrate quick:
- [ ] Confirm this artifact (install.md)
- [ ] Add the Maven repository and this one dependency
- [ ] Permissions (install.md)
- [ ] Credentials + settings from settings.md
- [ ] setDelegate, configure { ok -> }, bind CameraX when ok is true, call close() and unbind on destroy
- [ ] Handle the callbacks in callbacks.md
- [ ] Run the conflicts in troubleshooting.md
```

Assumptions when the host was specific about the product and silent on the rest: Host camera, all four card fields on, auto-capture mode Normal, margins 100/0, host merges front and back after reset().

### Guided workflow

1. **UI** — host camera (this SDK) or Lens camera UI (`com.veryfi.lens:veryfi-lens-credit-cards-sdk`)?
2. **Fields** — number, name, date, CVC?
3. **Mode** — `CreditCardAutoCaptureMode` Speed, Normal, or Accuracy?
4. **Sides** — front only, or front and back with `reset()`?

Defaults: host camera, all fields, Normal, host merges sides.


## Rules

1. Types are `com.veryfi.lens.headless.creditcards.VeryfiLensHeadless` and `VeryfiLensHeadlessDelegate`.
2. `processCreditCard(Frame)` on each NV21 analysis frame. `reset()` clears detector state between sides. `close()` releases it.
3. Card values stay in the delegate JSON. `notifyServerOfCreditCardReady` sends booleans for which fields were non-empty, engine `mobile`, `sdk` = `lens-headless-credit-card`. It does not put the PAN string in that body.
4. Do not call `VeryfiLens.uploadImages`. Card frames are not a file upload.
5. Do not mutate `VeryfiLensHelper.settings` between sides. Merge on the host.


## Do not

- Copy a repository password, keystore, or credential from the SDK repo or the Demo app. Maven access comes from Veryfi. Keep it in `~/.gradle/gradle.properties` or CI secrets.
- Depend on a second Lens UI flavor, or on `com.veryfi.lens.core:veryfi-lens-core-sdk` (email and obsolete dictation; adds `WRITE_CONTACTS` and `RECORD_AUDIO`).
- Treat Demo types as the client contract: `SettingsHolder`, Demo analyzers, `ImageProxyUtils`, `VeryfiLensHelper`.
- Set `dictateIsOn = true` or call `VeryfiLensCore.showDictation()`.
- Set `anyDocumentType` (deprecated local regex OCR). A new template id is `anyDocumentTemplate`, and only after Veryfi support confirms it.
- Set `useLegacyModels` (default true; no partner contract).
- Quote AAR size as the installed app size. Compare the final app with the SDK linked and removed. [SDK binary sizes](https://docs.veryfi.com/lens/mobile/settings/sdk_binary_sizes/).
- Treat `allowSubmittingScreenshots` (rejects imported screenshots) and `enableScreenshots` (device screenshots of Lens screens, default true) as one switch.
- Send frames to `VeryfiLens.uploadImages` or `showCamera()`.
- Copy Demo `VeryfiLensHelper` field toggling as a supported API.
- Depend on the UI credit-cards artifact and this one together.
- Promise the `card_dates` boolean in the doc-ready body. The notify method calls `getString` on a `JSONArray` (**Unconfirmed**).
- Quote AAR size as app size.


## What to hand back

1. `implementation "com.veryfi.lens.headless:credit-cards-sdk:2.7.0.6"` and host CameraX.
2. `CAMERA` permission, configure callback, analyzer, `close()` in `onDestroy`.
3. Delegate reading `card_number`, `card_dates`, `card_name`, `card_type`, `card_cvc`, `card_uuid`.
4. Privacy note: PAN to the host; doc-ready carries booleans.
5. `reset()` between front and back, merge on the host.


## Files in this folder

- [install.md](install.md) — Maven coordinate, requirements, permissions, app size
- [implementation.md](implementation.md) — configure, delegate, and launch or camera lifecycle
- [settings.md](settings.md) — `VeryfiLensSettings` for this product, with Android defaults
- [callbacks.md](callbacks.md) — delegate payloads
- [troubleshooting.md](troubleshooting.md) — symptom, cause, fix
- [clarifications.md](clarifications.md) — Unconfirmed items, Any-Documents, fraud, field detection
- [checklist.md](checklist.md) — validation checklist before handing code to the host

