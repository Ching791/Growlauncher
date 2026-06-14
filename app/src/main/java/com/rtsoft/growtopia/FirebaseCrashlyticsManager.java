package com.rtsoft.growtopia;

import android.util.Log;

public class FirebaseCrashlyticsManager {
    public void RecordException(String str, int i) {
        Log.e("FirebaseCrashlytics", str + i);
    }
}
