package com.ant.shared

import platform.UIKit.UIDevice

/**
 * iOS implementation of platformName().
 */
actual fun platformName(): String {
    val device = UIDevice.currentDevice
    return "iOS ${device.systemVersion}"
}
