package com.ant.shared

/**
 * Returns the name and version of the current platform (e.g., "Android 34" or "iOS 17.0").
 *
 * This is an `expect` declaration with `actual` implementations in androidMain and iosMain.
 */
expect fun platformName(): String
