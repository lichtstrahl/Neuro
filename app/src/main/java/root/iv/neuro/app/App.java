package root.iv.neuro.app;

import android.app.Application;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import root.iv.neuro.BuildConfig;
import timber.log.Timber;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

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
