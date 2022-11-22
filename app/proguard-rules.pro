# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-verbose

# -libraryjars ../app/libs
# -dontpreverify
# -dontobfuscate
# -dontshrink
# -dontoptimize

-keepattributes LineNumberTable,SourceFile

# keep class BuildConfig
-keep public class **.BuildConfig { *; }

# keep class members of R
-keepclassmembers class **.R$* {public static <fields>;}

# keep all public and protected method names,
# which could be used by Java reflection.
-keepclassmembernames class * {
    public protected <methods>;
}

#android X
-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }
-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# Class names are needed in reflection
-keepnames class com.amazonaws.**
-keepnames class com.amazon.**
# Request handlers defined in request.handlers
-keep class com.amazonaws.services.**.*Handler
# The following are referenced but aren't required to run
-dontwarn com.fasterxml.jackson.**
-dontwarn org.apache.commons.logging.**
-dontwarn com.amazonaws.mobile.auth.**
-dontwarn com.amazonaws.mobileconnectors.**
# Android 6.0 release removes support for the Apache HTTP client
-dontwarn org.apache.http.**
# The SDK has several references of Apache HTTP client
-dontwarn com.amazonaws.http.**
-dontwarn com.amazonaws.metrics.**
# Class names needed for Veryfi Lens
-keep class org.opencv.android.**
-keep class org.apache.harmony.**

#room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

-keep class com.veryfi.lens.VeryfiLens
-keep interface com.veryfi.lens.VeryfiLensDelegate
-keep class com.veryfi.lens.helpers.VeryfiLensSettings
-keep class com.veryfi.lens.helpers.DocumentType
-keep class com.veryfi.lens.helpers.VeryfiLensCredentials
-keep enum com.veryfi.lens.helpers.VeryfiLensSettings$ExtractionEngine
-keep class com.veryfi.lens.helpers.ExportLogsHelper

-keep class com.veryfi.lens.VeryfiLens {public *;}
-keep interface com.veryfi.lens.VeryfiLensDelegate {public *;}
-keep class com.veryfi.lens.helpers.VeryfiLensSettings {public *;}
-keepclassmembers class com.veryfi.lens.helpers.VeryfiLensSettings { *; }
-keep class com.veryfi.lens.helpers.DocumentType {public *;}
-keepclassmembers class com.veryfi.lens.helpers.DocumentType { *; }
-keep class com.veryfi.lens.helpers.VeryfiLensCredentials {public *;}
-keepclassmembers class com.veryfi.lens.helpers.VeryfiLensCredentials { *; }
-keep class com.veryfi.lens.helpers.ExportLogsHelper {public *;}
-keepclassmembers class com.veryfi.lens.helpers.ExportLogsHelper { *; }
-keep class com.veryfi.lens.helpers.VeryfiLensSettings {public *;}
-keepclassmembers class com.veryfi.lens.helpers.VeryfiLensSettings { *; }
-keep class com.veryfi.lens.helpers.VeryfiLensSettings {public *;}
-keepclassmembers class com.veryfi.lens.helpers.VeryfiLensSettings { *; }

-keep class com.veryfi.lens.helpers.models.Category {public *;}
-keepclassmembers class com.veryfi.lens.helpers.models.Category { *; }

-keep class com.veryfi.lens.helpers.models.CustomerProject {public *;}
-keepclassmembers class com.veryfi.lens.helpers.models.CustomerProject { *; }

-keep class com.veryfi.lens.helpers.models.Job {public *;}
-keepclassmembers class com.veryfi.lens.helpers.models.Job { *; }

-keep class com.veryfi.lens.helpers.models.Tag {public *;}
-keepclassmembers class com.veryfi.lens.helpers.models.Tag { *; }

-keep class com.veryfi.lens.helpers.models.DocumentInformation {public *;}
-keepclassmembers class com.veryfi.lens.helpers.models.DocumentInformation { *; }

-keep class com.veryfi.lens.helpers.UploadingProcessHelper {public *;}
-keepclassmembers class com.veryfi.lens.helpers.UploadingProcessHelper { *; }


-keep class com.veryfi.lens.service.UploadDocumentsService {public *;}
-keep class com.veryfi.lens.service.UploadDocumentsService$Companion { *; }
-keepclassmembers class com.veryfi.lens.service.UploadDocumentsService {
    public static ** Companion;
}

-keep enum com.veryfi.lens.helpers.PackageUploadEvent$UploadEventType
-keep class com.veryfi.lens.helpers.PackageUploadEvent {public *;}
-keepclassmembers class com.veryfi.lens.helpers.PackageUploadEvent { *; }
-keepclassmembers class com.veryfi.lens.helpers.PackageUploadEvent {
    public static ** Companion;
}

-keep class com.veryfi.lens.helpers.database.ProcessData {public *;}
-keepclassmembers class com.veryfi.lens.helpers.database.ProcessData { *; }

-keep class com.veryfi.lens.cpp.ExportLogsCpp
-keep class com.veryfi.lens.cpp.ExportLogsCpp {public *;}
-keepclassmembers class com.veryfi.lens.cpp.ExportLogsCpp { *; }

#-keep class com.veryfi.lens.cpp.** { *; }
#-keepnames class com.veryfi.lens.cpp.** { *; }

# eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

#gson
-keep class com.google.** { *; }
-keepattributes *Annotation*
-keepattributes Signature
-keepclassmembers enum * { public static **[] values(); public static ** valueOf(java.lang.String); }
-keepclassmembers enum * { *; }

-keep class androidx.databinding.** { *; }