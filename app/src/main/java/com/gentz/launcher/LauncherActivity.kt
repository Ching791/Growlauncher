package com.gentz.launcher

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        findViewById<TextView>(R.id.tvVersion).text =
            getString(R.string.growtopia_version, BuildConfig.GROWTOPIA_VERSION)
        findViewById<TextView>(R.id.tvUser).text = getString(R.string.user_email, "guest@gentz.com")
        findViewById<TextView>(R.id.tvRole).text = getString(R.string.user_role, "Member")

        findViewById<Button>(R.id.btnLaunch).setOnClickListener {
            val launchIntent = Intent(this, GameLaunchActivity::class.java).apply {
                putExtra("EXTRA_USER_EMAIL", "guest@gentz.com")
                putExtra("EXTRA_USER_ROLE", "Member")
                putExtra("EXTRA_GROW_VERSION", BuildConfig.GROWTOPIA_VERSION)
            }
            startActivity(launchIntent)
        }

        findViewById<Button>(R.id.btnScriptHub).setOnClickListener {
            Toast.makeText(this, R.string.script_hub_placeholder, Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, PermissionActivity::class.java))
        }

        findViewById<Button>(R.id.btnSwitchAccount).setOnClickListener {
            Toast.makeText(this, R.string.switch_account_placeholder, Toast.LENGTH_SHORT).show()
        }
    }
}
