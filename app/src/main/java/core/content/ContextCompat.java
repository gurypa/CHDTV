package androidx.core.content;

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.job.JobScheduler;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.RestrictionsManager;
import android.content.pm.LauncherApps;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.hardware.ConsumerIrManager;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.media.tv.TvInputManager;
import android.net.ConnectivityManager;
import android.net.nsd.NsdManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Process;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.print.PrintManager;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.TextServicesManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.os.BuildCompat;
import androidx.core.os.ExecutorCompat;
import androidx.core.util.ObjectsCompat;
import androidx.media3.common.MimeTypes;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class ContextCompat {
    private static final String DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION_SUFFIX = ".DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION";
    public static final int RECEIVER_EXPORTED = 2;
    public static final int RECEIVER_NOT_EXPORTED = 4;
    public static final int RECEIVER_VISIBLE_TO_INSTANT_APPS = 1;
    private static final String TAG = "ContextCompat";
    private static final Object sLock = null;
    private static final Object sSync = null;
    private static TypedValue sTempValue;

    static class Api16Impl {
        private Api16Impl() {
        }

        static void startActivities(Context r0, Intent[] r1, Bundle r2) {
            r0.startActivities(r1, r2);
        }

        static void startActivity(Context r0, Intent r1, Bundle r2) {
            r0.startActivity(r1, r2);
        }
    }

    static class Api19Impl {
        private Api19Impl() {
        }

        static File[] getExternalCacheDirs(Context r1) {
            return r1.getExternalCacheDirs();
        }

        static File[] getExternalFilesDirs(Context r1, String r2) {
            return r1.getExternalFilesDirs(r2);
        }

        static File[] getObbDirs(Context r1) {
            return r1.getObbDirs();
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static File getCodeCacheDir(Context r1) {
            return r1.getCodeCacheDir();
        }

        static Drawable getDrawable(Context r1, int r2) {
            return r1.getDrawable(r2);
        }

        static File getNoBackupFilesDir(Context r1) {
            return r1.getNoBackupFilesDir();
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static int getColor(Context r1, int r2) {
            return r1.getColor(r2);
        }

        static <T> T getSystemService(Context r1, Class<T> r2) {
            return (T) r1.getSystemService(r2);
        }

        static String getSystemServiceName(Context r1, Class<?> r2) {
            return r1.getSystemServiceName(r2);
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static Context createDeviceProtectedStorageContext(Context r1) {
            return r1.createDeviceProtectedStorageContext();
        }

        static File getDataDir(Context r1) {
            return r1.getDataDir();
        }

        static boolean isDeviceProtectedStorage(Context r1) {
            return r1.isDeviceProtectedStorage();
        }
    }

    static class Api26Impl {
        private Api26Impl() {
        }

        static Intent registerReceiver(Context r2, BroadcastReceiver r3, IntentFilter r4, String r5, Handler r6, int r7) {
            if ((r7 & 4) == 0) goto L8;
            if (r5 != null) goto L8;
            return r2.registerReceiver(r3, r4, ContextCompat.obtainAndCheckReceiverPermission(r2), r6);
        L8:
            return r2.registerReceiver(r3, r4, r5, r6, r7 & 1);
        }

        static ComponentName startForegroundService(Context r1, Intent r2) {
            return r1.startForegroundService(r2);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static Executor getMainExecutor(Context r1) {
            return r1.getMainExecutor();
        }
    }

    static class Api30Impl {
        private Api30Impl() {
        }

        static String getAttributionTag(Context r1) {
            return r1.getAttributionTag();
        }
    }

    static class Api33Impl {
        private Api33Impl() {
        }

        static Intent registerReceiver(Context r1, BroadcastReceiver r2, IntentFilter r3, String r4, Handler r5, int r6) {
            return r1.registerReceiver(r2, r3, r4, r5, r6);
        }
    }

    private static final class LegacyServiceMapHolder {
        static final HashMap<Class<?>, String> SERVICES = null;

        static {
            SERVICES = new HashMap();
            SERVICES.put(SubscriptionManager.class, "telephony_subscription_service");
            SERVICES.put(UsageStatsManager.class, "usagestats");
            SERVICES.put(AppWidgetManager.class, "appwidget");
            SERVICES.put(BatteryManager.class, "batterymanager");
            SERVICES.put(CameraManager.class, "camera");
            SERVICES.put(JobScheduler.class, "jobscheduler");
            SERVICES.put(LauncherApps.class, "launcherapps");
            SERVICES.put(MediaProjectionManager.class, "media_projection");
            SERVICES.put(MediaSessionManager.class, "media_session");
            SERVICES.put(RestrictionsManager.class, "restrictions");
            SERVICES.put(TelecomManager.class, "telecom");
            SERVICES.put(TvInputManager.class, "tv_input");
            SERVICES.put(AppOpsManager.class, "appops");
            SERVICES.put(CaptioningManager.class, "captioning");
            SERVICES.put(ConsumerIrManager.class, "consumer_ir");
            SERVICES.put(PrintManager.class, "print");
            SERVICES.put(BluetoothManager.class, "bluetooth");
            SERVICES.put(DisplayManager.class, "display");
            SERVICES.put(UserManager.class, "user");
            SERVICES.put(InputManager.class, "input");
            SERVICES.put(MediaRouter.class, "media_router");
            SERVICES.put(NsdManager.class, "servicediscovery");
            SERVICES.put(AccessibilityManager.class, "accessibility");
            SERVICES.put(AccountManager.class, "account");
            SERVICES.put(ActivityManager.class, "activity");
            SERVICES.put(AlarmManager.class, NotificationCompat.CATEGORY_ALARM);
            SERVICES.put(AudioManager.class, MimeTypes.BASE_TYPE_AUDIO);
            SERVICES.put(ClipboardManager.class, "clipboard");
            SERVICES.put(ConnectivityManager.class, "connectivity");
            SERVICES.put(DevicePolicyManager.class, "device_policy");
            SERVICES.put(DownloadManager.class, "download");
            SERVICES.put(DropBoxManager.class, "dropbox");
            SERVICES.put(InputMethodManager.class, "input_method");
            SERVICES.put(KeyguardManager.class, "keyguard");
            SERVICES.put(LayoutInflater.class, "layout_inflater");
            SERVICES.put(LocationManager.class, "location");
            SERVICES.put(NfcManager.class, "nfc");
            SERVICES.put(NotificationManager.class, "notification");
            SERVICES.put(PowerManager.class, "power");
            SERVICES.put(SearchManager.class, "search");
            SERVICES.put(SensorManager.class, "sensor");
            SERVICES.put(StorageManager.class, "storage");
            SERVICES.put(TelephonyManager.class, "phone");
            SERVICES.put(TextServicesManager.class, "textservices");
            SERVICES.put(UiModeManager.class, "uimode");
            SERVICES.put(UsbManager.class, "usb");
            SERVICES.put(Vibrator.class, "vibrator");
            SERVICES.put(WallpaperManager.class, "wallpaper");
            SERVICES.put(WifiP2pManager.class, "wifip2p");
            SERVICES.put(WifiManager.class, "wifi");
            SERVICES.put(WindowManager.class, "window");
        }

        private LegacyServiceMapHolder() {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RegisterReceiverFlags {
    }

    static {
        sLock = new Object();
        sSync = new Object();
    }

    protected ContextCompat() {
    }

    public static int checkSelfPermission(Context r2, String r3) {
        ObjectsCompat.requireNonNull(r3, "permission must be non-null");
        if (BuildCompat.isAtLeastT() == true) goto L12;
        if (TextUtils.equals("android.permission.POST_NOTIFICATIONS", r3) == false) goto L12;
        if (NotificationManagerCompat.from(r2).areNotificationsEnabled() == false) goto L9;
        return 0;
    L9:
        return -1;
    L12:
        return r2.checkPermission(r3, Process.myPid(), Process.myUid());
    }

    public static Context createDeviceProtectedStorageContext(Context r2) {
        if (Build.VERSION.SDK_INT >= 24) goto L5;
        return null;
    L5:
        return Api24Impl.createDeviceProtectedStorageContext(r2);
    }

    private static File createFilesDir(File r4) {
        Object r0 = sSync;
        monitor-enter(r0);
    L13:
        th = move-exception;
        throw th;
    L5:
        if (r4.exists() == false) goto L7;
    L11:
        monitor-exit(r0);     // Catch: Throwable -> L13
        return r4;
    L7:
        if (r4.mkdirs() == false) goto L10;
        monitor-exit(r0);     // Catch: Throwable -> L13
        return r4;
    L10:
        Log.w(TAG, "Unable to create files subdir " + r4.getPath());     // Catch: Throwable -> L13
        goto L11
    }

    public static String getAttributionTag(Context r2) {
        if (Build.VERSION.SDK_INT >= 30) goto L5;
        return null;
    L5:
        return Api30Impl.getAttributionTag(r2);
    }

    public static File getCodeCacheDir(Context r1) {
        return Api21Impl.getCodeCacheDir(r1);
    }

    public static int getColor(Context r1, int r2) {
        return Api23Impl.getColor(r1, r2);
    }

    public static ColorStateList getColorStateList(Context r2, int r3) {
        return ResourcesCompat.getColorStateList(r2.getResources(), r3, r2.getTheme());
    }

    public static File getDataDir(Context r2) {
        if (Build.VERSION.SDK_INT >= 24) goto L5;
        String r0 = r2.getApplicationInfo().dataDir;
        if (r0 != null) goto L11;
        return null;
    L11:
        return new File(r0);
    L5:
        return Api24Impl.getDataDir(r2);
    }

    public static Drawable getDrawable(Context r1, int r2) {
        return Api21Impl.getDrawable(r1, r2);
    }

    public static File[] getExternalCacheDirs(Context r1) {
        return Api19Impl.getExternalCacheDirs(r1);
    }

    public static File[] getExternalFilesDirs(Context r1, String r2) {
        return Api19Impl.getExternalFilesDirs(r1, r2);
    }

    public static Executor getMainExecutor(Context r2) {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return Api28Impl.getMainExecutor(r2);
    L7:
        return ExecutorCompat.create(new Handler(r2.getMainLooper()));
    }

    public static File getNoBackupFilesDir(Context r1) {
        return Api21Impl.getNoBackupFilesDir(r1);
    }

    public static File[] getObbDirs(Context r1) {
        return Api19Impl.getObbDirs(r1);
    }

    public static <T> T getSystemService(Context r1, Class<T> r2) {
        return (T) Api23Impl.getSystemService(r1, r2);
    }

    public static String getSystemServiceName(Context r1, Class<?> r2) {
        return Api23Impl.getSystemServiceName(r1, r2);
    }

    public static boolean isDeviceProtectedStorage(Context r2) {
        if (Build.VERSION.SDK_INT >= 24) goto L5;
        return false;
    L5:
        return Api24Impl.isDeviceProtectedStorage(r2);
    }

    static String obtainAndCheckReceiverPermission(Context r4) {
        String r0 = r4.getPackageName() + DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION_SUFFIX;
        if (PermissionChecker.checkSelfPermission(r4, r0) != 0) goto L6;
        return r0;
    L6:
        throw new RuntimeException("Permission " + r0 + " is required by your application to receive broadcasts, please add it to your manifest");
    }

    public static Intent registerReceiver(Context r6, BroadcastReceiver r7, IntentFilter r8, int r9) {
        return registerReceiver(r6, r7, r8, null, null, r9);
    }

    public static Intent registerReceiver(Context r6, BroadcastReceiver r7, IntentFilter r8, String r9, Handler r10, int r11) {
        if ((r11 & 1) == 0) goto L10;
        if ((r11 & 4) == 0) goto L10;
        throw new IllegalArgumentException("Cannot specify both RECEIVER_VISIBLE_TO_INSTANT_APPS and RECEIVER_NOT_EXPORTED");
    L10:
        if ((r11 & 1) == 0) goto L12;
        int r5 = r11 | 2;
    L14:
        if ((r5 & 2) != 0) goto L21;
        if ((r5 & 4) != 0) goto L21;
        throw new IllegalArgumentException("One of either RECEIVER_EXPORTED or RECEIVER_NOT_EXPORTED is required");
    L21:
        if ((r5 & 2) == 0) goto L28;
        if ((r5 & 4) == 0) goto L28;
        throw new IllegalArgumentException("Cannot specify both RECEIVER_EXPORTED and RECEIVER_NOT_EXPORTED");
    L28:
        if (BuildCompat.isAtLeastT() == false) goto L32;
        return Api33Impl.registerReceiver(r6, r7, r8, r9, r10, r5);
    L32:
        if (Build.VERSION.SDK_INT < 26) goto L36;
        return Api26Impl.registerReceiver(r6, r7, r8, r9, r10, r5);
    L36:
        if ((r5 & 4) == 0) goto L41;
        if (r9 != null) goto L41;
        return r6.registerReceiver(r7, r8, obtainAndCheckReceiverPermission(r6), r10);
    L41:
        return r6.registerReceiver(r7, r8, r9, r10);
    L12:
        r5 = r11;
        goto L14
    }

    public static boolean startActivities(Context r1, Intent[] r2) {
        return startActivities(r1, r2, null);
    }

    public static boolean startActivities(Context r1, Intent[] r2, Bundle r3) {
        Api16Impl.startActivities(r1, r2, r3);
        return true;
    }

    public static void startActivity(Context r0, Intent r1, Bundle r2) {
        Api16Impl.startActivity(r0, r1, r2);
    }

    public static void startForegroundService(Context r2, Intent r3) {
        if (Build.VERSION.SDK_INT < 26) goto L5;
        Api26Impl.startForegroundService(r2, r3);
        return;
    L5:
        r2.startService(r3);
    }
}
