package com.gentz.launcher

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameLaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_launch)

        findViewById<TextView>(R.id.tvLoadingInfo).text = getString(
            R.string.loading_info,
            intent.getStringExtra("EXTRA_USER_EMAIL") ?: "guest@gentz.com"
        )

        Handler(Looper.getMainLooper()).postDelayed({
            launchGrowtopia()
        }, 600)
    }

    private fun launchGrowtopia() {
        val growtopiaIntent = Intent().apply {
            setClassName(this@GameLaunchActivity, "com.rtsoft.growtopia.Main")
            putExtra("EXTRA_USER_EMAIL", intent.getStringExtra("EXTRA_USER_EMAIL"))
            putExtra("EXTRA_USER_ROLE", intent.getStringExtra("EXTRA_USER_ROLE"))
            putExtra("EXTRA_GROW_VERSION", intent.getStringExtra("EXTRA_GROW_VERSION"))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            startActivity(growtopiaIntent)
            finish()
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(this, R.string.growtopia_not_packaged, Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
