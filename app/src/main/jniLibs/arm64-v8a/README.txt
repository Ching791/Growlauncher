Place the Growtopia 5.54 arm64-v8a native libraries here:

- libgrowtopia.so   (required - the Proton engine; the game activity refuses to start without it)

- libcurl.so, libssl.so (only if the engine build links them dynamically)

They must come from a v5.54 build: the Java signatures in com.rtsoft.growtopia match that
version's JNI bindings, and a mismatched engine crashes during the consent/age gate.

Note that .so files are ignored by .gitignore except under this directory.
