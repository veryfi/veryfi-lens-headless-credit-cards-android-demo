# Template: Full (multiple document types)

**Artifact:** `com.veryfi.lens:veryfi-lens-sdk:2.7.0.6` (or `veryfi-lens-remote-models-sdk` when size matters — same allowed types).
**Allowed types:** RECEIPT, LONG_RECEIPT, OTHER, ANY_DOCUMENT, CHECK, BUSINESS_CARD, INVOICE, W2, W9, BARCODE, BANK_STATEMENT, INSURANCE_CARD, PASSPORT, DRIVER_LICENSE, NUTRITION_FACT, SHIPPING_LABEL, PRESCRIPTION_LABEL. **Not** CREDIT_CARD or CODE (use their dedicated artifacts).

This file also holds the canonical wiring used by every UI template.

## Gradle

```groovy
dependencies {
    implementation "com.veryfi.lens:veryfi-lens-sdk:2.7.0.6"
}
```

Repository setup: [install.md](../install.md#maven-repository).

## Wiring (canonical)

```kotlin
import android.app.Application
import com.veryfi.lens.VeryfiLens
import com.veryfi.lens.VeryfiLensDelegate
import com.veryfi.lens.helpers.DocumentType
import com.veryfi.lens.helpers.VeryfiLensCredentials
import com.veryfi.lens.helpers.VeryfiLensSettings
import org.json.JSONObject

object LensController : VeryfiLensDelegate {

    fun start(application: Application, settings: VeryfiLensSettings, onReady: () -> Unit) {
        val credentials = VeryfiLensCredentials().apply {
            clientId = BuildConfig.VERYFI_CLIENT_ID
            username = BuildConfig.VERYFI_USERNAME
            apiKey = BuildConfig.VERYFI_API_KEY
        }
        VeryfiLens.setDelegate(this)
        VeryfiLens.configure(application, credentials, settings) { ok ->
            if (ok) onReady() // e.g. VeryfiLens.showCamera()
        }
    }

    override fun veryfiLensClose(json: JSONObject) {}
    override fun veryfiLensUpdate(json: JSONObject) {
        // status: start | inprogress | failed | clear_package
    }
    override fun veryfiLensError(json: JSONObject) {}
    override fun veryfiLensSuccess(json: JSONObject) {
        val packageId = json.optString("package_id")
        val documentType = json.optString("document_type")
        val data = json.optJSONObject("data")
    }
}
```

Usage: `LensController.start(application, fullSettings()) { VeryfiLens.showCamera() }`.

## Settings

```kotlin
fun fullSettings() = VeryfiLensSettings().apply {
    // base hardening
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
    documentTypes = arrayListOf(
        DocumentType.RECEIPT, DocumentType.INVOICE, DocumentType.OTHER,
        DocumentType.LONG_RECEIPT, DocumentType.CHECK, DocumentType.BUSINESS_CARD,
    )                                          // only the types the host needs
    showDocumentTypes = true                   // switcher for multiple types
    defaultSelectedDocumentType = DocumentType.RECEIPT
    galleryIsOn = true
    autoRotateIsOn = true
    autoSkewCorrectionIsOn = true
    cleanBorderIsOn = true
    stitchIsOn = true
    returnStitchedPDF = true
    packageMaxScans = 10
    driverLicenseBackIsOn = false              // Android default true; enable per product
    insuranceCardBackIsOn = false
}
```

Per-type flags (check backs, TIFF, any-document templates) from [recipes.md](../recipes.md) only if that type is included.

## Do not

- Add CREDIT_CARD or CODE here (flavor check blocks the camera).
- Leave every type enabled "just in case" — more types means more UI and larger model downloads.
