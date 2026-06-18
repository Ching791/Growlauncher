package com.rtsoft.growtopia

import android.app.NativeActivity

/**
 * Hosts the packaged Growtopia native engine.
 *
 * Extending [NativeActivity] lets Android's native activity glue load
 * libgrowtopia.so (declared via the android.app.lib_name meta-data in the
 * manifest) and invoke its ANativeActivity_onCreate entry point, which starts
 * the game. A plain Activity that only calls System.loadLibrary never runs the
 * native entry point, so the game never starts.
 */
class Main : NativeActivity()
