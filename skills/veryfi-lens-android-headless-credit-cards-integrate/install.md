# Veryfi Lens Android — Headless Credit Cards — install

Source of truth: Veryfi Lens Android SDK **2.7.0.6**, commit `7f33726c`. Names below were read from that tree. Items not fully confirmed are marked **Unconfirmed**. Do not copy repository passwords, keystores, or Demo-only types into the host app.


## Requirements

| Item | Value |
|------|--------|
| minSdk | 23 |
| compileSdk / targetSdk used by the SDK | 36 |
| Language | Kotlin façade, callable from Java via `@JvmStatic` |
| SDK version | `2.7.0.6` (commit `7f33726c`) |

Camera stack: Host-owned CameraX (`Preview` + `ImageAnalysis`). `processCreditCard` consumes NV21 frames. The SDK does not start `CaptureActivity`.

## Maven

Repository: `https://nexus.veryfi.com/repository/maven-releases/`

```groovy
// settings.gradle — dependencyResolutionManagement.repositories
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url "https://nexus.veryfi.com/repository/maven-releases/"
            // Only when Veryfi issued repository credentials. Read them from Gradle properties.
            // Never commit them, and never copy a password out of the SDK repository.
            credentials {
                username = providers.gradleProperty("veryfiMavenUser").getOrNull()
                password = providers.gradleProperty("veryfiMavenPassword").getOrNull()
            }
        }
    }
}
```

```groovy
// app/build.gradle
dependencies {
    implementation "com.veryfi.lens.headless:credit-cards-sdk:2.7.0.6"
    implementation "androidx.camera:camera-camera2:<host CameraX version>"
    implementation "androidx.camera:camera-lifecycle:<host CameraX version>"
    implementation "androidx.camera:camera-view:<host CameraX version>"
}
```

Transitive `com.veryfi.lens.cpp` and `com.veryfi.lens.helpers` artifacts resolve with the Lens dependency. Do not add them by hand.


## Flavor check

`FlavorCheckerHelper` reads `BuildConfig.FLAVOR` (`lensHeadlessCreditCards`). Allowed types: Card capture only. There is no `documentTypes` list and no `showCamera()`.

No UI flavor check. Do not also depend on `veryfi-lens-credit-cards-sdk` in the same module. `credit-cards-sdk-full` is the larger native publication. Prefer `credit-cards-sdk` unless Veryfi says otherwise (**Unconfirmed** trade-off).

## Permissions

The host owns the camera. Declare `android.permission.CAMERA` and request it at runtime before `ProcessCameraProvider.bindToLifecycle`. These headless artifacts do not open `CaptureActivity`, so they do not request `CAMERA` for you.

The headless credit-cards module manifest also merges `INTERNET`, `ACCESS_NETWORK_STATE`, `FOREGROUND_SERVICE`, and `FOREGROUND_SERVICE_DATA_SYNC`, plus a `TransferService` with `foregroundServiceType="dataSync"`. `uses-feature` `glEsVersion="0x00030000"` is `required="true"` on that module.

`veryfihelpers` merges `ACCESS_COARSE_LOCATION` and `ACCESS_NETWORK_STATE`. This product does not turn on `locationServicesIsOn`. If the Play listing must not mention location, remove `ACCESS_COARSE_LOCATION` with `tools:node="remove"`. Whether every code path tolerates that removal is **Unconfirmed**.

New projects set `android:allowBackup="true"`. This module and `veryfihelpers` set `false`, so the merger fails on `application@allowBackup`. On the host `<application>`, set `android:allowBackup="false"` and `tools:replace="android:allowBackup"`, with `xmlns:tools` on `<manifest>`. Append `android:allowBackup` when `tools:replace` already lists other attributes. Leave `dataExtractionRules` and `fullBackupContent` in place. Keep `allowBackup="true"` only when the host must enable Auto Backup.

New projects set `android:allowBackup="true"`. This module and `veryfihelpers` set `false`, so the merger fails on `application@allowBackup`. On the host `<application>`, set `android:allowBackup="false"` and `tools:replace="android:allowBackup"`, with `xmlns:tools` on `<manifest>`. Append `android:allowBackup` when `tools:replace` already lists other attributes. Leave `dataExtractionRules` and `fullBackupContent` in place. Keep `allowBackup="true"` only when the host must enable Auto Backup.


## App size

Compare the **installed app**, with this dependency linked and with it removed (release APK/AAB, Android Studio APK Analyzer, or the Play Console size report). The published AAR is a fat artifact. Do not quote AAR size as the user's install delta. Background: [SDK binary sizes](https://docs.veryfi.com/lens/mobile/settings/sdk_binary_sizes/).

Coordinates: `com.veryfi.lens.headless:credit-cards-sdk:2.7.0.6` and `credit-cards-sdk-full`. Use `credit-cards-sdk` unless Veryfi specifies `-full` (**Unconfirmed** delta). The module manifest sets `glEsVersion` 3.0 required. Compare the installed app. Remote Models and Lite cannot replace this SDK: their UI checkers reject `CREDIT_CARD`, and they are not this headless façade. Do not quote AAR size.



