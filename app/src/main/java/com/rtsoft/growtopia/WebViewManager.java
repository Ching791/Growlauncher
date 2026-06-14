package com.rtsoft.growtopia;

import android.app.Activity;

public class WebViewManager {
    private Activity baseActivity;
    public boolean needed_to_render = false;
    public String to_render = "";
    public String last_packet = "";
    public String last_url = "";

    public WebViewManager(Activity activity) {
        this.baseActivity = activity;
    }

    public void HideWebView() {}
    public boolean IsVisible() { return false; }
    public void LoadURL(String url, boolean allowExternal) {}
    public void LoadURLPost(String url, byte[] postData, boolean allowExternal) {}
    public void MoveView(int height) {}
    public void SetBgColor(int r, int g, int b, int a) {}
    public void SetFrame(float x, float y, float w, float h) {}
    public void ShowWebView() {}
    public void requestPageSource() {}

    public native void nativeOnErrorOccurred(int errorCode);
    public native void nativeOnPageContent(String content);
    public native void nativeOnPageLoaded(String url);
    public native void nativeOnScriptCall(String func, String param);
}
