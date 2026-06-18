package com.rtsoft.growtopia;

import android.app.Activity;

public class IAPManager {
    private Activity mainActivity;
    private boolean isReady = false;

    public IAPManager(Activity activity) {
        this.mainActivity = activity;
    }

    public void IAPPurchase(String str) {}
    public void RequestAIPPurchasedList() {}
    public void RequestItemDetails(String str) {}
    public void ConsumeItem(String str) {}
}
