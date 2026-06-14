package com.rtsoft.growtopia;

import android.content.Context;

public class AppsFlyerManager {
    private Context baseContext;
    private volatile boolean isStoped = false;
    private volatile boolean isStarted = false;

    public AppsFlyerManager(Context context) {
        this.baseContext = context;
    }

    public String GetAppsFlyerId() { return ""; }
    public void Init(String key) {}
    public void Start(String key) {}
    public void LogEvent(String event, String value) {}
    public void LogPurchase(String a, String b, String c) {}
    public void SetUserConsent(boolean consent) {}
    public void SetCustomerUserId(String userId) {}
    public void Stop() { this.isStoped = true; }
}
