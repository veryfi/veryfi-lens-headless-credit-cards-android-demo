# Validation checklist

Run this before delivering host code. Fix conflicts by removing extra flags.

- [ ] Dependency is `com.veryfi.lens.headless:credit-cards-sdk:2.7.0.6`
- [ ] CAMERA requested by the host; CameraX bound after configure returns true
- [ ] Analyzer calls `processCreditCard` and closes the ImageProxy
- [ ] `close()` and `unbindAll()` run when the screen is destroyed
- [ ] Delegate reads card_* keys; PAN is not described as uploaded
- [ ] `reset()` documented between sides; no VeryfiLensHelper writes
- [ ] No `uploadImages` or `showCamera` calls
- [ ] Any-Documents and receipt field detection not promised
- [ ] Installed app size, not AAR size
- [ ] `card_dates` doc-ready boolean marked **Unconfirmed**

- [ ] Every relative link in the generated host notes stays inside this skill folder
- [ ] Unconfirmed behavior the host depends on is labeled **Unconfirmed** in the delivery note
- [ ] Host `<application>` sets `android:allowBackup="false"` and `tools:replace="android:allowBackup"` so a new-project `true` does not fail the manifest merger
- [ ] No repository password, Demo `SettingsHolder` preset, or `VeryfiLensHelper` mutation is in the host code
