package launcher.powerkuy.growlauncher.api;

public class JNICall {
    public static final Companion Companion = new Companion();

    public static class Companion {
        public void notifyValueChanged(int type, String key, String value) {}
    }
}
