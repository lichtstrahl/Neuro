package root.iv.neuro.app;

import android.app.Application;
import android.util.Log;

import root.iv.neuro.BuildConfig;

public class App extends Application {
    public static void logI(String msg) {
        Log.i(BuildConfig.GLOBAL_TAG, msg);
    }

    public static void logW(String msg) {
        Log.w(BuildConfig.GLOBAL_TAG, msg);
    }

    public static void logE(String msg) {
        Log.e(BuildConfig.GLOBAL_TAG, msg);
    }
}
