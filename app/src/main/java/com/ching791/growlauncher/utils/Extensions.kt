package com.ching791.growlauncher.utils

import android.content.Context

fun Context.getPackageVersion(packageName: String): String =
    try {
        packageManager.getPackageInfo(packageName, 0).versionName ?: "Unknown"
    } catch (_: Exception) {
        "Not installed"
    }
