package com.ubisoft.bridge;

import com.rtsoft.growtopia.Main;

public abstract class a {
    static {
        try {
            System.loadLibrary("ubiservices");
        } catch (UnsatisfiedLinkError e10) {
            System.err.println("Loading library failed: " + e10);
        }
    }

    public static void a(Main main) {
        try {
            NativeInterface.injectActivity(main, 0, new String[0]);
        } catch (UnsatisfiedLinkError e) {
            // libubiservices.so not present - ignore
        }
    }
}
