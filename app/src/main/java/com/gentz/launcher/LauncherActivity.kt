package com.gentz.launcher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gentz.launcher.databinding.ActivityLauncherBinding

class LauncherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.launchButton.setOnClickListener { launchGrowtopia() }
        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, PermissionActivity::class.java))
        }
    }

    private fun launchGrowtopia() {
        val gameIntent = Intent().setClassName(this, "com.rtsoft.growtopia.Main")
            .putExtra("growtopia_version", "Official package")
            .putExtra("user_email", "user@example.com")
            .putExtra("user_role", "Launcher User")
            .putExtra("auth_token", "local-session")
        startActivity(gameIntent)
    }
}
