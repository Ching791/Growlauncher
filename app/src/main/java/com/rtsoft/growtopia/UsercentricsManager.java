package com.rtsoft.growtopia;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import java.util.ArrayList;
import java.util.List;

public class UsercentricsManager {
    private Activity baseContext;

    public UsercentricsManager(Activity activity) {
        this.baseContext = activity;
    }

    public void CheckConsentState() {
        final List<Object> result = new ArrayList();
        // Post callback to GL thread on next frame (like real Usercentrics SDK would)
        if (SharedActivity.mGLView != null) {
            SharedActivity.mGLView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    try {
                        OnConsentFetchedSuccess(result);
                    } catch (UnsatisfiedLinkError e) {
                        // Native method not in official libgrowtopia.so — use nativeSendGUIEx fallback
                        android.util.Log.w("UsercentricsManager", "OnConsentFetchedSuccess native not found, using fallback");
                        SharedActivity.nativeSendGUIEx(0, 0, 0, 0);
                    }
                }
            });
        } else {
            try {
                OnConsentFetchedSuccess(result);
            } catch (UnsatisfiedLinkError e) {
                android.util.Log.w("UsercentricsManager", "OnConsentFetchedSuccess native not found");
            }
        }
    }

    public void FetchUserConsent(List<Object> list) {
        final List<Object> result = list;
        if (SharedActivity.mGLView != null) {
            SharedActivity.mGLView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    try {
                        OnConsentFetchedSuccess(result);
                    } catch (UnsatisfiedLinkError e) {
                        android.util.Log.w("UsercentricsManager", "OnConsentFetchedSuccess native not found");
                    }
                }
            });
        } else {
            try {
                OnConsentFetchedSuccess(result);
            } catch (UnsatisfiedLinkError e) {
                android.util.Log.w("UsercentricsManager", "OnConsentFetchedSuccess native not found");
            }
        }
    }

    public native void OnConsentFetchedFail(int code, String message);
    public native void OnConsentFetchedSuccess(List<Object> list);

    public void RequestConsentSettings() {}
    public void ShowConsentSettings() {}
    public void InitWithRuleSet(String ruleSet) {}
    public void InitWithSettings(String settings) {}
}
