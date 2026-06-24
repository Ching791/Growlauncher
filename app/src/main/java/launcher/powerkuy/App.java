package launcher.powerkuy;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.provider.Settings;

import java.util.UUID;

public class App extends Application {
    public static App f10088p;
    private static SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        f10088p = this;
        prefs = getSharedPreferences("powerkuy_data", Context.MODE_PRIVATE);
    }

    public static AssetManager a() {
        return f10088p.getAssets();
    }

    public static String getData(String key) {
        if (f10088p == null) return "";
        if (prefs == null) {
            prefs = f10088p.getSharedPreferences("powerkuy_data", Context.MODE_PRIVATE);
        }
        String value = prefs.getString(key, null);
        if (value == null) {
            if ("mac".equals(key)) {
                value = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
            } else if ("gid".equals(key)) {
                try {
                    value = Settings.Secure.getString(f10088p.getContentResolver(), Settings.Secure.ANDROID_ID);
                } catch (Exception e) {
                    value = UUID.randomUUID().toString();
                }
            } else {
                value = "";
            }
            prefs.edit().putString(key, value).apply();
        }
        return value;
    }
}
