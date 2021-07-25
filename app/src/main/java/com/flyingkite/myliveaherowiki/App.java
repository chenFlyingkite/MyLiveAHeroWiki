package com.flyingkite.myliveaherowiki;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.StrictMode;
import android.widget.Toast;

import com.flyingkite.myliveaherowiki.lah.LiveAHeroWiki;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.AnyRes;
import androidx.annotation.StringRes;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {
    public static App me;
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh_mm_ss_SSS", Locale.US);
    private static Thread.UncaughtExceptionHandler defaultHandler;
    private static final boolean DEBUG = BuildConfig.DEBUG;
    //private static final boolean DEBUG = false;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        me = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        strictMode();

        LiveAHeroWiki.init(this);

//        CrashReport.init(this, DEBUG);
//        FirebaseApp.initializeApp(this);
//        RemoteConfig.init(R.xml.remote_config_default);
//        FabricAnswers.logAppOnCreate(null);
//        TosWiki.init(this);
        //initCrashHandler();
    }

    private void strictMode() {
        if (DEBUG) {
            // http://developer.android.com/intl/zh-tw/training/articles/perf-anr.html
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectNetwork()
                    .detectCustomSlowCalls()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectActivityLeaks()
                    .detectLeakedRegistrationObjects()
                    .penaltyLog()
                    .build());
        }
    }

    public static void showToast(@StringRes int id) {
        Toast.makeText(me, id, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(@StringRes int id) {
        Toast.makeText(me, id, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String s) {
        Toast.makeText(me, s, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(String s) {
        Toast.makeText(me, s, Toast.LENGTH_SHORT).show();
    }

    public static Uri getUriOfResource(@AnyRes int resId) {
        return getUriOfResource(res(), resId);
    }

    public static Uri getUriOfResource(Resources r, @AnyRes int resId) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + r.getResourcePackageName(resId)
                + '/' + r.getResourceTypeName(resId)
                + '/' + r.getResourceEntryName(resId));
    }

    public static Resources res() {
        return me.getResources();
    }

    public static boolean isNetworkConnected() {
        if (me == null) return false;

        ConnectivityManager c = (ConnectivityManager) me.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo n = c.getActiveNetworkInfo();
        return n != null && n.isConnected();
    }
}
