# Add project specific ProGuard rules here.
-keepattributes Signature
-keepattributes *Annotation*

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# Gson
-keep class com.quoteexplorer.model.** { *; }
-keep class com.quoteexplorer.data.api.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
