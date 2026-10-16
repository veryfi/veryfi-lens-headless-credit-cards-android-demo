# Template: Lite (Play Feature Delivery module)

**Artifact:** `com.veryfi.lens:veryfi-lens-lite-sdk:2.7.0.6`, declared in a **dynamic feature module**.
**Allowed types:** RECEIPT, OTHER, INVOICE, LONG_RECEIPT, ANY_DOCUMENT, CHECK, BUSINESS_CARD, INSURANCE_CARD, PASSPORT, DRIVER_LICENSE, NUTRITION_FACT, SHIPPING_LABEL, BARCODE.
**Not supported:** CREDIT_CARD, CODE, W2, W9, BANK_STATEMENT, PRESCRIPTION_LABEL; `shareLogsIsOn`; x86 ABIs.
**Models:** remote model download is enabled on Lite (like Remote Models).

## Module setup

```groovy
// feature module build.gradle (com.android.dynamic-feature)
dependencies {
    implementation project(":app")
    implementation "com.veryfi.lens:veryfi-lens-lite-sdk:2.7.0.6"
}
```

```kotlin
// base module Application
class App : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        SplitCompat.install(this)      // com.google.android.play.core.splitcompat.SplitCompat
    }
}
```

After the module is installed (Play Feature Delivery `SplitInstallManager`), from code inside the feature module:

```kotlin
// Only when Play delivered the module as an emulated split
// (Android 10 redirect install, internal app sharing, bundletool --local-testing):
com.veryfi.lens.cpp.dfm.NativeLibraryLoader.setSplitName("lens_feature")   // your module name

VeryfiLens.setDelegate(delegate)
VeryfiLens.configure(application, credentials, liteSettings()) { ok ->
    if (ok) VeryfiLens.showCamera()
}
```

Whether `setSplitName` is harmless to call on a normally installed split is Unconfirmed; call it only in the emulated cases listed.

## Settings

```kotlin
fun liteSettings() = VeryfiLensSettings().apply {
    ignoreRemoteSettings = true
    backupDocsToGallery = false
    moreMenuIsOn = false
    moreSettingsMenuIsOn = false
    autoCaptureOtherIsOn = false
    showNoDocumentDetectedWarning = false
    dictateIsOn = false
    locationServicesIsOn = false
    glareDetectionIsOn = false

    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiCloudAPI
    documentTypes = arrayListOf(DocumentType.RECEIPT)
    showDocumentTypes = false
    shareLogsIsOn = false                      // no FileProvider in Lite
    notificationSmallIcon = 0                  // or a drawable in the BASE module
    galleryIsOn = true
}
```

## Checklist

- [ ] `SplitCompat.install` in `attachBaseContext`
- [ ] configure only after the split is installed
- [ ] `setSplitName` for emulated splits
- [ ] no re-declared `CropFileProvider`; no stub `VeryfiLensTheme`
- [ ] test on ARM device/emulator
