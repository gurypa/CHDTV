package androidx.core.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public final class NavUtils {
    public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
    private static final String TAG = "NavUtils";

    static class Api16Impl {
        private Api16Impl() {
        }

        static Intent getParentActivityIntent(Activity r1) {
            return r1.getParentActivityIntent();
        }

        static boolean navigateUpTo(Activity r1, Intent r2) {
            return r1.navigateUpTo(r2);
        }

        static boolean shouldUpRecreateTask(Activity r1, Intent r2) {
            return r1.shouldUpRecreateTask(r2);
        }
    }

    private NavUtils() {
    }

    public static Intent getParentActivityIntent(Activity r6) {
        Intent r0 = Api16Impl.getParentActivityIntent(r6);
        if (r0 == null) goto L5;
        return r0;
    L5:
        String r1 = getParentActivityName(r6);
        if (r1 != null) goto L8;
        return null;
    L8:
        ComponentName r2 = new ComponentName(r6, r1);
        if (getParentActivityName(r6, r2) != null) goto L13;
        return Intent.makeMainActivity(r2);
    L13:
        return new Intent().setComponent(r2);
    L15:
        Log.e(TAG, "getParentActivityIntent: bad parentActivityName '" + r1 + "' in manifest");
        return null;
    }

    public static Intent getParentActivityIntent(Context r4, ComponentName r5) throws PackageManager.NameNotFoundException {
        String r0 = getParentActivityName(r4, r5);
        if (r0 != null) goto L6;
        return null;
    L6:
        ComponentName r1 = new ComponentName(r5.getPackageName(), r0);
        if (getParentActivityName(r4, r1) != null) goto L11;
        return Intent.makeMainActivity(r1);
    L11:
        return new Intent().setComponent(r1);
    }

    public static Intent getParentActivityIntent(Context r4, Class<?> r5) throws PackageManager.NameNotFoundException {
        String r0 = getParentActivityName(r4, new ComponentName(r4, r5));
        if (r0 != null) goto L6;
        return null;
    L6:
        ComponentName r1 = new ComponentName(r4, r0);
        if (getParentActivityName(r4, r1) != null) goto L11;
        return Intent.makeMainActivity(r1);
    L11:
        return new Intent().setComponent(r1);
    }

    public static String getParentActivityName(Activity r2) {
        return getParentActivityName(r2, r2.getComponentName());
    L4:
        e = move-exception;
        throw new IllegalArgumentException(e);
    }

    public static String getParentActivityName(Context r6, ComponentName r7) throws PackageManager.NameNotFoundException {
        PackageManager r0 = r6.getPackageManager();
        if (Build.VERSION.SDK_INT < 24) goto L5;
        int r1 = 128 | 512;
    L7:
        if (Build.VERSION.SDK_INT < 29) goto L10;
        r1 = r1 | 269221888;
    L12:
        ActivityInfo r2 = r0.getActivityInfo(r7, r1);
        String r3 = r2.parentActivityName;
        if (r3 == null) goto L16;
        return r3;
    L16:
        if (r2.metaData != null) goto L18;
        return null;
    L18:
        String r4 = r2.metaData.getString(PARENT_ACTIVITY);
        if (r4 != null) goto L22;
        return null;
    L22:
        if (r4.charAt(0) == '.') goto L24;
        return r4;
    L24:
        return r6.getPackageName() + r4;
    L10:
        if (Build.VERSION.SDK_INT < 24) goto L12;
        r1 = r1 | 786432;
        goto L12
    L5:
        r1 = 128 | 512;
        goto L7
    }

    public static void navigateUpFromSameTask(Activity r4) {
        Intent r0 = getParentActivityIntent(r4);
        if (r0 == null) goto L7;
        navigateUpTo(r4, r0);
        return;
    L7:
        throw new IllegalArgumentException("Activity " + r4.getClass().getSimpleName() + " does not have a parent activity name specified. (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data>  element in your manifest?)");
    }

    public static void navigateUpTo(Activity r0, Intent r1) {
        Api16Impl.navigateUpTo(r0, r1);
    }

    public static boolean shouldUpRecreateTask(Activity r1, Intent r2) {
        return Api16Impl.shouldUpRecreateTask(r1, r2);
    }
}
