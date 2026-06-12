package com.rtsoft.growtopia

import android.app.Service
import android.content.Intent
import android.os.IBinder

class GrowtopiaBackgroundService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}
