package com.ching791.growlauncher

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GrowlauncherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // TODO: Re-enable Firebase analytics/crashlytics after google-services.json is configured.
    }
}
