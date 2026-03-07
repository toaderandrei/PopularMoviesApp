package com.ant.shared

/** Returns "Android" followed by the SDK version integer (e.g., "Android 34"). */
actual fun platformName(): String {
    return "Android ${android.os.Build.VERSION.SDK_INT}"
}
