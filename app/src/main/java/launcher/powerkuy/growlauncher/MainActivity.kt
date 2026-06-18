package launcher.powerkuy.growlauncher

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNativeBridge()
        val loaded = loadGrowtopiaLibraries()
        Log.i(TAG, "Growtopia native libraries loaded: $loaded")
    }

    private external fun initNativeBridge(): Boolean
    private external fun loadGrowtopiaLibraries(): Boolean

    companion object {
        private const val TAG = "Growlauncher"

        init {
            System.loadLibrary("native-lib")
        }
    }
}
