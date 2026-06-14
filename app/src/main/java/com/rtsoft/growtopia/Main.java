package com.rtsoft.growtopia;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.inputmethod.InputMethodManager;

import com.rtsoft.growtopia.HeightProvider;
import com.ubisoft.bridge.a;

import launcher.powerkuy.growlauncher.api.JNICall;
import launcher.powerkuy.growlauncher.api.JavaForNative;
import launcher.powerkuy.growlauncher.luamanager.LuaManager;

import java.net.URLEncoder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Main extends SharedActivity {
    public static boolean OriginalKeyboard = false;
    public static boolean block_pause;
    public static HelpShiftManager helpshiftManager;
    public static Main mainApp;
    public static GLSurfaceView mygl;
    private HeightProvider heightProvider;

    public NativeAppInterface nativeAppInterface = new NativeAppInterface();
    public AppsFlyerManager appsflyerManager = new AppsFlyerManager(this);
    public IronSourceManager ironSourceManager = new IronSourceManager(this);
    public WebViewManager webViewManager = new WebViewManager(this);
    public AppReviewManager appReviewManager = new AppReviewManager(this);
    public FirebaseCrashlyticsManager firebaseCrashlyticsManager = new FirebaseCrashlyticsManager();
    public FirebaseCloudMessageManager firebaseCloudMessageManager = new FirebaseCloudMessageManager();
    public GoogleSignInHelper googleSignInHelper = new GoogleSignInHelper(this);
    public MAFManager mafManager = new MAFManager(this);
    public UsercentricsManager usercentricsManager = null;

    // === Static getters the native engine calls via JNI ===
    public static AppReviewManager GetAppReviewManager() { return mainApp.appReviewManager; }
    public static AppsFlyerManager GetAppsflyerManager() { return mainApp.appsflyerManager; }
    public static FirebaseCloudMessageManager GetFirebaseCloudMessageManager() { return mainApp.firebaseCloudMessageManager; }
    public static FirebaseCrashlyticsManager GetFirebaseCrashlyticsManager() { return new FirebaseCrashlyticsManager(); }
    public static GoogleSignInHelper GetGoogleSignInHelper() { return mainApp.googleSignInHelper; }
    public static Object GetHelpShiftManager() { return helpshiftManager; }
    public static Object GetIronSourceManager() { return mainApp.ironSourceManager; }
    public static MAFManager GetMAFManager() { return mainApp.mafManager; }
    public static UsercentricsManager GetUsercentricsManager() {
        if (mainApp == null || mainApp.usercentricsManager == null) return new UsercentricsManager(null);
        return mainApp.usercentricsManager;
    }
    public static WebViewManager GetWebViewManager() { return mainApp.webViewManager; }

    public static boolean HandleDeeplink(Intent intent) {
        final Uri data = intent.getData();
        if (data == null) return false;
        Log.d("URL host", "" + data.getHost());
        Log.d("URL data", data.toString());
        SharedActivity.mGLView.post(() -> {
            NativeAppInterface.OnDeepLinkProcess(data.getSchemeSpecificPart());
        });
        return true;
    }

    // Native methods declared here (engine expects these on Main class)
    public static native void nativeOnKey(int state, int virtualKey, int unicodeChar);
    public static native boolean nativeOnTouch(float x, float y, int action);

    private void applyImmersiveFullscreen() {
        if (Build.VERSION.SDK_INT >= 30) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void handleIntent(Intent intent) {
        if (intent == null) return;
        String action = intent.getAction();
        Uri data = intent.getData();
        if (!"android.intent.action.VIEW".equals(action) || data == null) return;
        try {
            JNICall.Companion.notifyValueChanged(5, "google_redirect_callback",
                "info=" + URLEncoder.encode(data.getQueryParameter("info"), "UTF-8") +
                "&token=" + URLEncoder.encode(data.getQueryParameter("token"), "UTF-8"));
        } catch (Exception e) {
            Log.e("Main", "handleIntent error: " + e.getMessage());
        }
    }

    @Override
    public String GetAppsflyerUID() { return ""; }

    public void OnKeyboardHeightChanged(int height) {
        if (OriginalKeyboard) {
            if (this.webViewManager.IsVisible()) {
                this.webViewManager.MoveView(height);
                return;
            }
            SharedActivity.m_KeyBoardHeight = height;
            boolean keyboardOpen = height > 0;
            if (keyboardOpen && !SharedActivity.m_editText.isFocused()) {
                UpdateEditBoxInView(true, false);
            } else if (!keyboardOpen && SharedActivity.m_editText.isFocused()) {
                OriginalKeyboard = false;
                SharedActivity.nativeOnInputText(SharedActivity.m_editText.getText().toString());
                if (!SharedActivity.passwordField) {
                    SharedActivity.nativeOnKey(1, 500000, 0);
                }
                SharedActivity.nativeCancelBtnPressed();
                UpdateEditBoxInView(false, false);
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    SharedActivity.nativeUpdateConsoleLogPos(SharedActivity.m_KeyBoardHeight);
                }
            }
            if (SharedActivity.m_editText.isFocused()) {
                UpdateEditBoxRootViewPosition();
            }
        }
    }

    public void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void initialize(Bundle bundle) {
        // Placeholder for mod menu or additional UI overlay
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        int h = config.screenHeightDp;
        int w = config.screenWidthDp;
        if (h > w) {
            config.screenHeightDp = w;
            config.screenWidthDp = h;
        }
        super.onConfigurationChanged(config);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainApp = this;
        helpshiftManager = new HelpShiftManager(this);
        SharedActivity.dllname = "growtopia";
        this.BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArv12FD/xxuAJ3/B8Jgx78985UN/FitcQD5C21eIS5D+98yr7dy9sw8R2fSTFZKExBZVAfatgDH7s6fb9vfHi43szfpdXs3ZL2hsa7DeCWRyVSTD6o/i14vgwInv1S/dgLAwQth3PDXWF+zYXOlL+umOt9K9eqQo5CZhkwl9JAmMHlazvbhSGAldV5QsdY3pK5wmg/w2873abgYsGdI3B9wL75kgZW9tV2O6efiIbXlevktGOMup3Ql2H4Rcpa3ZeDtGl+YTQbEUQTYiYBDtFGCyqksXeM6+kCnaF97Ss5wA0w5ID9WJLkziXI4iGBMRd0a7s+vVniwpx771oGcJxewIDAQAB";
        SharedActivity.securityEnabled = false;
        SharedActivity.IAPEnabled = true;
        SharedActivity.HookedEnabled = false;
        SharedActivity.PackageName = BuildConfig.APPLICATION_ID;
        this.usercentricsManager = new UsercentricsManager(this);
        System.loadLibrary("growtopia");

        super.onCreate(savedInstanceState);

        Configuration config = getResources().getConfiguration();
        int h = config.screenHeightDp;
        int w = config.screenWidthDp;
        if (h > w) {
            config.screenHeightDp = w;
            config.screenWidthDp = h;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }

        a.a(this); // Ubisoft bridge init (matching real Growlauncher)

        this.heightProvider = new HeightProvider(this).setHeightListener(height -> {
            OnKeyboardHeightChanged(height);
        });

        initialize(savedInstanceState);
        getWindow().addFlags(128); // FLAG_KEEP_SCREEN_ON

        applyImmersiveFullscreen();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.heightProvider != null) this.heightProvider.OnPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.heightProvider != null) this.heightProvider.OnResume();
    }

    @Override
    public void onStart() { super.onStart(); }

    @Override
    public void onStop() {
        JavaForNative.shutdown();
        super.onStop();
    }

    // PowerKuyRootRenderer: engine calls into this for the powerkuy overlay
    public static class PowerKuyRootRenderer implements GLSurfaceView.Renderer {
        public static native void nativeDrawFrame();
        public static native int nativeGetMessagePowerKuy();
        public static native void nativeSurfaceChanged(int width, int height);

        @Override
        public void onDrawFrame(GL10 gl) {
            try { nativeDrawFrame(); } catch (UnsatisfiedLinkError e) {}
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            try { nativeSurfaceChanged(width, height); } catch (UnsatisfiedLinkError e) {}
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {}
    }
}
