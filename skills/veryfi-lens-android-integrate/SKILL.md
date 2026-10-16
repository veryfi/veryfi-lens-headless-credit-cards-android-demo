---
name: veryfi-lens-android-integrate
description: >-
  Integrates the Veryfi Lens Android SDK (Kotlin/Java): Maven flavor artifact, permissions,
  VeryfiLensCredentials, VeryfiLens.configure, VeryfiLensSettings recipes, VeryfiLensDelegate
  callbacks, showCamera/showGallery/showDocumentBrowser, no-UI uploadImages, queue utilities,
  receipts, long receipts, checks, credit cards, business cards, barcodes, OCR strips/caps,
  W2/W9, Lite (Play Feature Delivery), and the separate Headless Receipts / Headless Credit
  Cards SDKs. Use when adding Veryfi Lens to an Android app, choosing a flavor template,
  wiring the delegate, or reducing Lens support tickets about capture, upload, or extraction.
---

# Veryfi Lens Android integration

Walk a host Android app from Gradle to a working first scan. Prefer changing the **fewest** flags. Do not invent SDK APIs. Facts here were read from the Android SDK source at version `2.7.0.6` (commit `7f33726c`). Anything not confirmed in code is marked **Unconfirmed** — see [clarifications.md](clarifications.md).

**Public entry points** (all Kotlin `object`s with `@JvmStatic` methods):

| Product | Façade | Delegate |
|---------|--------|----------|
| Lens UI SDK (every UI flavor) | `com.veryfi.lens.VeryfiLens` | `com.veryfi.lens.VeryfiLensDelegate` |
| Headless Receipts | `com.veryfi.lens.headless.receipts.VeryfiLensHeadless` | `com.veryfi.lens.headless.receipts.VeryfiLensHeadlessDelegate` |
| Headless Credit Cards | `com.veryfi.lens.headless.creditcards.VeryfiLensHeadless` | `com.veryfi.lens.headless.creditcards.VeryfiLensHeadlessDelegate` |

Shared types live in `com.veryfi.lens.helpers`: `VeryfiLensCredentials`, `VeryfiLensSettings`, `DocumentType`, `Frame`.

**Settings type:** always a fresh `VeryfiLensSettings()` (also used by both Headless SDKs). Android has no `VeryfiLens*Settings` subclasses; never copy the Demo app's `SettingsHolder` presets wholesale — they carry Demo branding, Demo-only flavors, and paid features.

**No SDK changes.** Recipes are property assignments only. The Android repo is read-only reference.

## Pick a workflow

- **Quick** — the host already said which product and document type. Ask nothing else; apply the matching template with base hardening, then state the assumptions you made.
- **Guided** — anything important is missing. Ask **only** the missing questions below (max 4, in one message), then continue.

### Quick workflow

```
Lens Android quick:
- [ ] Map request → template (table below)
- [ ] Add Maven repo + ONE artifact (install.md)
- [ ] Manifest/permissions check (install.md#permissions)
- [ ] Credentials + template settings + base hardening
- [ ] setDelegate → configure { ok -> } → show* only after ok
- [ ] Handle close / update / success / error
- [ ] Run conflicts.md against the final settings
```

### Guided workflow (minimal questions)

Ask only what is unknown, in this order, and stop as soon as a template is determined:

1. **Documents** — which types? (receipts/invoices, long receipts, checks, credit cards, business cards, barcodes, codes on strips or bottle caps, W-2, W-9, other)
2. **UI** — Lens camera UI, no-UI upload of files the app already has, or the app owns the camera (Headless)?
3. **Extraction** — Veryfi Cloud processes it (default), on-device only (credit card / code / barcode), or the customer runs Process a Document later (decoupled)?
4. **Size / delivery** — is SDK size a concern, or is this an on-demand Play Feature module (Lite)?

Defaults if the user does not answer: Lens UI, Cloud API, review-then-submit, camera + gallery, no gallery backup, More menu off.

### Templates

| Product | Template | Artifact |
|---------|----------|----------|
| Full / multiple types | [templates/full.md](templates/full.md) | `veryfi-lens-sdk` |
| Receipts | [templates/receipts.md](templates/receipts.md) | `veryfi-lens-receipts-sdk` |
| Long receipts | [templates/long-receipts.md](templates/long-receipts.md) | `veryfi-lens-long-receipts-sdk` |
| Lite (on-demand DFM) | [templates/lite.md](templates/lite.md) | `veryfi-lens-lite-sdk` |
| Checks | [templates/checks.md](templates/checks.md) | `veryfi-lens-cheques-sdk` |
| Credit cards (Lens UI) | [templates/credit-cards-ui.md](templates/credit-cards-ui.md) | `veryfi-lens-credit-cards-sdk` |
| Business cards | [templates/business-cards.md](templates/business-cards.md) | `veryfi-lens-business-cards-sdk` |
| Barcodes | [templates/barcodes.md](templates/barcodes.md) | `veryfi-lens-barcodes-sdk` |
| OCR code strips | [templates/ocr-code-strips.md](templates/ocr-code-strips.md) | `veryfi-lens-ocr-sdk` |
| OCR bottle caps | [templates/ocr-code-caps.md](templates/ocr-code-caps.md) | `veryfi-lens-ocr-sdk` |
| W-2 | [templates/w2.md](templates/w2.md) | `veryfi-lens-w2-sdk` |
| W-9 | [templates/w9.md](templates/w9.md) | `veryfi-lens-w9-sdk` |
| Headless receipts | [templates/headless-receipts.md](templates/headless-receipts.md) | `com.veryfi.lens.headless:receipts-sdk` |
| Headless credit cards | [templates/headless-credit-cards.md](templates/headless-credit-cards.md) | `com.veryfi.lens.headless:credit-cards-sdk` |

Reference files (one level deep):

- [install.md](install.md) — Maven repo, flavor → artifact, allowed `documentTypes` per flavor, permissions, Lite/DFM checklist, size
- [implementation.md](implementation.md) — credentials, configure, delegate, launchers, `uploadImages`, queue/storage utilities, Core, callback JSON, local files, decoupled capture
- [headless.md](headless.md) — Headless Receipts and Headless Credit Cards (separate SDKs, host-owned camera)
- [recipes.md](recipes.md) — base hardening + use-case property sets + overlays
- [settings-groups.md](settings-groups.md) — conceptual groups (ExtractionMode, CaptureFlow, …)
- [conflicts.md](conflicts.md) — incompatible flags and support footguns
- [settings-catalog.md](settings-catalog.md) — every `VeryfiLensSettings` property with Android default
- [troubleshooting.md](troubleshooting.md) — symptom → cause → fix
- [clarifications.md](clarifications.md) — Unconfirmed / unsupported items and iOS ↔ Android differences

## Core rules

### 1. Flavor must match `documentTypes`

`VeryfiLens.showCamera()` runs `FlavorCheckerHelper`: if any entry of `documentTypes` is not allowed by the linked flavor, a Toast ("Please use only valid document types") is shown, an error is logged, and **the camera does not open** (no delegate callback). `documentTypes = null` shows "Please specify the document types". Full table: [install.md](install.md#flavor--artifact--allowed-documenttypes). Notable: **Full, Remote Models and Field Detection do not allow `CREDIT_CARD` or `CODE`**; Lite does not allow `CREDIT_CARD`, `CODE`, `W2`, `W9`, `BANK_STATEMENT`, `PRESCRIPTION_LABEL`.

When SDK size is a concern recommend **Remote Models** (`veryfi-lens-remote-models-sdk`): heavy models download after install for the enabled features. Remote model download is enabled only on the Remote Models and Lite flavors. Measure the final app with the SDK linked and removed; the AAR size is a fat artifact, not the install delta. [SDK binary sizes](https://docs.veryfi.com/lens/mobile/settings/sdk_binary_sizes/).

### 2. Base hardening (always, unless the host wants the opposite)

```kotlin
val settings = VeryfiLensSettings().apply {
    ignoreRemoteSettings = true            // otherwise dashboard lens_settings overwrite this object during configure
    backupDocsToGallery = false            // Android default true: every scan copied to the device gallery
    moreMenuIsOn = false                   // Android default true; see "More menu" below
    moreSettingsMenuIsOn = false
    autoCaptureOtherIsOn = false           // default true: OTHER auto-captures even with autoCaptureIsOn = false
    showNoDocumentDetectedWarning = false  // usual choice: skip the no-document alert
    dictateIsOn = false                    // obsolete; not partner available (default true)
    locationServicesIsOn = false           // true triggers a runtime location request
    glareDetectionIsOn = false             // Android default true; suboptimal algorithmic gate
}
```

Keep files after callbacks, or several packages in flight: `autoDeleteLocalResourcesAfterProcessing = false` (host owns deletion).

### 3. More menu persists user overrides (Android-specific)

`configure` checks `moreMenuIsOn`. When it is `true` **and** the user has used Lens before, stored user preferences **replace the host values** of `autoDocDetectionAndCropIsOn`, `blurDetectionIsOn`, `glareDetectionIsOn`, `autoLightDetectionIsOn`, `autoCaptureIsOn`, `backupDocsToGallery`, `closeCameraOnSubmit`, `batchUploadOnCamera` on every later `configure`. Changing those in code then has no effect for existing users. With `moreMenuIsOn = false`, host values are written to preferences each time. Keep it `false` unless end users should own those toggles.

### 4. Configure, then present

1. Build `VeryfiLensCredentials()` (`clientId`, `username`, `apiKey`, optional `url`) and settings.
2. `VeryfiLens.setDelegate(delegate)` — strong reference held by the SDK; call `removeDelegate()` when the owner dies.
3. `VeryfiLens.configure(application, credentials, settings) { ok -> ... }`
4. Call `showCamera()` / `showGallery()` / `showDocumentBrowser()` **only after `ok == true`**. (The Demo calls `showCamera()` right after `configure`; do not copy that.) Calling a launcher before configure is silently logged ("application not initialized") — no callback.

Android has **no** `invalid_credentials` delegate error; an unusable configuration yields `ok == false` and/or a Toast. See [implementation.md](implementation.md#configure).

### 5. Choose one entry path

| Need | API |
|------|-----|
| Lens camera UI | `VeryfiLens.showCamera()` |
| Lens gallery / system file picker UI | `VeryfiLens.showGallery()` / `VeryfiLens.showDocumentBrowser()` |
| No UI, images/files the app already has (e.g. share intent) | `VeryfiLens.uploadImages(uris, isSameTransaction)` or `uploadImages(paths, …)` |
| App owns the camera, on-device receipt detection/crop | Headless Receipts SDK ([headless.md](headless.md)) |
| App owns the camera, on-device card reading | Headless Credit Cards SDK ([headless.md](headless.md)) |

`uploadImages` is part of the **UI SDK** (same artifact, same configure, same delegate, same upload pipeline). The Headless SDKs are **separate artifacts** with their own `configure`/delegate and **do not upload images or call Process a Document**. Never mix them up.

### 6. Delegate

Implement all four: `veryfiLensClose`, `veryfiLensUpdate`, `veryfiLensError`, `veryfiLensSuccess` (`org.json.JSONObject`, delivered on the main thread). Optional `onHelpButtonClicked(parentFragment: Fragment)`. Android failure status is **`"failed"`** (iOS uses `"fail"`). Keys: [implementation.md](implementation.md#callback-json).

## Do not

- Copy any repository password, keystore, or credential from the Android repo or Demo. Maven access comes from Veryfi; keep it in `~/.gradle/gradle.properties` or CI secrets.
- Depend on more than one Lens UI flavor artifact, or on `veryfi-lens-core-sdk` for partners (Core = email/dictation, obsolete; adds `WRITE_CONTACTS`/`RECORD_AUDIO`).
- Use `CREDIT_CARD` or `CODE` with the Full/Remote Models/Field Detection artifacts — use the Credit Cards or OCR artifacts.
- Promise W-2/W-9/bank statements/credit cards/codes on Lite.
- Set `dictateIsOn = true` or call `VeryfiLensCore.showDictation()` (obsolete; not partner available).
- Set `anyDocumentType` (deprecated local regex OCR); use `anyDocumentTemplate`.
- Set `useLegacyModels` (no public contract; leave the default — Unconfirmed).
- Use `VeryfiInApp` except for `CREDIT_CARD`, `BARCODE`, `CODE`, or identity verification. For other types InApp only posts metadata and returns a plain success — no extraction.
- Send credit-card images to the cloud: `configure` forces `VeryfiInApp` for a first type of `CREDIT_CARD`; keep it that way.
- Leave `moreMenuIsOn` at its default `true` unless end users should permanently override capture toggles (rule 3).
- Promise that `fraudDetectionIsOn` or `documentFieldsToDetect` work by setting the flag: fraud needs backend `detect_fraud_on`; field detection needs the entitlement **and** the Field Detection / Remote Models / Lite flavor.
- Promise bank-check TIFF on `veryfi-lens-cheques-sdk`: its bundled resources do not include the TIFF model; use Remote Models (Unconfirmed on Android beyond code inspection).
- Quote AAR size as app size.
- Treat `allowSubmittingScreenshots` (rejects imported screenshots) and `enableScreenshots` (device screenshots of Lens screens) as one switch.
- Treat `browseIsOn` as a global import gate — it only adds the Import/Browse card inside the More menu. `showBrowserButton`, `galleryButtonWithFilesIsOn`, and `showDocumentBrowser()` are independent.
- Expect `uploadImages(paths)` to accept PDFs — it decodes bitmaps only; use the `Uri` overload for PDFs/files.
- Reference iOS-only APIs on Android: `utils`, `lensVC`, `showCrop`, `headlessUpload`, SwiftUI helpers, `shareExtensionModeIsOn`, `customLensStrings`, `backupDocsToiCloud`, `showiCloudBackupOption`.

## Output

When generating host code, produce:

1. Gradle: repository block + **one** dependency line (+ Lite DFM notes if applicable)
2. Manifest/permission notes actually needed (usually none beyond what the AAR merges)
3. Delegate implementation + `configure { ok -> }` + one launcher call gated on `ok`
4. The template's `VeryfiLensSettings` mutations with a one-line comment per non-obvious flag
5. A short conflict note for residual risks, and any Unconfirmed item the host relies on
