#include <jni.h>
#include <android/log.h>
#include <dlfcn.h>
#include <mutex>

namespace {
constexpr const char* kTag = "GrowlauncherNative";
std::once_flag g_init_flag;

void LogInfo(const char* msg) {
    __android_log_print(ANDROID_LOG_INFO, kTag, "%s", msg);
}

bool TryLoad(const char* library_name) {
    void* handle = dlopen(library_name, RTLD_NOW | RTLD_LOCAL);
    if (handle == nullptr) {
        __android_log_print(
            ANDROID_LOG_WARN,
            kTag,
            "Failed to load %s: %s",
            library_name,
            dlerror() == nullptr ? "unknown error" : dlerror());
        return false;
    }

    LogInfo(library_name);
    return true;
}
}  // namespace

extern "C" JNIEXPORT jboolean JNICALL
Java_launcher_powerkuy_growlauncher_MainActivity_initNativeBridge(
        JNIEnv* /* env */, jobject /* thiz */) {
    std::call_once(g_init_flag, []() { LogInfo("Native bridge initialized"); });
    return JNI_TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_launcher_powerkuy_growlauncher_MainActivity_loadGrowtopiaLibraries(
        JNIEnv* /* env */, jobject /* thiz */) {
    const bool growtopia_loaded = TryLoad("libgrowtopia.so");
    const bool hooks_loaded = TryLoad("libPowerKuy.so");
    return (growtopia_loaded || hooks_loaded) ? JNI_TRUE : JNI_FALSE;
}
