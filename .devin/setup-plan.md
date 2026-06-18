# Growlauncher environment setup plan

To build/test this Android app in future sessions, the blueprint will:

1. Install Gradle 8.9 (no wrapper committed) to `/home/ubuntu/gradle-8.9` and add to PATH.
2. Install Android command-line tools to `/home/ubuntu/android-sdk` and set `ANDROID_HOME`/`ANDROID_SDK_ROOT`.
3. Accept SDK licenses and install: `platform-tools`, `platforms;android-35`, `build-tools;34.0.0`, `ndk;27.0.12077973` (matches `app/build.gradle.kts`).
4. Write `local.properties` with `sdk.dir`.

Verified this session: `gradle :app:assembleDebug` -> BUILD SUCCESSFUL.

Note: `app/src/main/jniLibs/arm64-v8a/libgrowtopia.so` is a placeholder README in the repo; full runtime launch requires the real native library.
