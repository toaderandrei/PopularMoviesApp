package com.ant.shared

import platform.UIKit.UIDevice

/** Returns the iOS system name and version (e.g., "iOS 17.0"). */
actual fun platformName(): String {
    return UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}
