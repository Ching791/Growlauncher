package com.rtsoft.growtopia;

import android.app.Activity;

public class GoogleSignInHelper {
    Activity mainActivity;

    public GoogleSignInHelper(Activity activity) {
        this.mainActivity = activity;
    }

    public native void OnSignIn(int code, String token);

    public void Init() {}
    public void SignIn() {}
    public void SignOut() {}
    public void handleSignInResult(int requestCode, int resultCode, android.content.Intent data) {}
}
