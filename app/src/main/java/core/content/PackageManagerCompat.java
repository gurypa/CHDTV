package androidx.core.content;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.core.os.UserManagerCompat;
import com.google.common.util.concurrent.ListenableFuture;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public final class PackageManagerCompat {
    public static final String ACTION_PERMISSION_REVOCATION_SETTINGS = "android.intent.action.AUTO_REVOKE_PERMISSIONS";
    public static final String LOG_TAG = "PackageManagerCompat";

    private static class Api30Impl {
        private Api30Impl() {
        }

        static boolean areUnusedAppRestrictionsEnabled(Context r1) {
            return !r1.getPackageManager().isAutoRevokeWhitelisted();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface UnusedAppRestrictionsStatus {
    }

    private PackageManagerCompat() {
    }

    public static boolean areUnusedAppRestrictionsAvailable(PackageManager r5) {
        if (Build.VERSION.SDK_INT < 30) goto L5;
        boolean r0 = true;
    L7:
        if (Build.VERSION.SDK_INT >= 30) goto L9;
        boolean r3 = true;
    L11:
        if (getPermissionRevocationVerifierApp(r5) == null) goto L13;
        boolean r4 = true;
    L14:
        if (r0 == true) goto L20;
        if (r3 == false) goto L19;
        if (r4 == false) goto L19;
        return true;
    L19:
        return false;
    L20:
        return true;
    L13:
        r4 = false;
        goto L14
    L9:
        r3 = false;
        goto L11
    L5:
        r0 = false;
        goto L7
    }

    public static String getPermissionRevocationVerifierApp(PackageManager r7) {
        String r2 = null;
        Iterator<ResolveInfo> r3 = r7.queryIntentActivities(new Intent(ACTION_PERMISSION_REVOCATION_SETTINGS).setData(Uri.fromParts("package", "com.example", null)), 0).iterator();
    L4:
        if (r3.hasNext() == false) goto L11;
        String r5 = r3.next().activityInfo.packageName;
        if (r7.checkPermission("android.permission.PACKAGE_VERIFICATION_AGENT", r5) != 0) goto L4;
        if (r2 != null) goto L9;
        r2 = r5;
        goto L4
    L9:
        return r2;
    L11:
        return r2;
    }

    public static ListenableFuture<Integer> getUnusedAppRestrictionsStatus(Context r7) {
        ResolvableFuture<Integer> r0 = ResolvableFuture.create();
        if (UserManagerCompat.isUserUnlocked(r7) == true) goto L7;
        r0.set(0);
        Log.e(LOG_TAG, "User is in locked direct boot mode");
        return r0;
    L7:
        if (areUnusedAppRestrictionsAvailable(r7.getPackageManager()) == true) goto L10;
        r0.set(1);
        return r0;
    L10:
        int r1 = r7.getApplicationInfo().targetSdkVersion;
        if (r1 >= 30) goto L14;
        r0.set(0);
        Log.e(LOG_TAG, "Target SDK version below API 30");
        return r0;
    L14:
        int r3 = 4;
        if (Build.VERSION.SDK_INT < 31) goto L24;
        if (Api30Impl.areUnusedAppRestrictionsEnabled(r7) == false) goto L21;
        if (r1 < 31) goto L20;
        r3 = 5;
    L20:
        r0.set(Integer.valueOf(r3));
    L22:
        return r0;
    L21:
        r0.set(2);
        goto L22
    L24:
        if (Build.VERSION.SDK_INT == 30) goto L26;
        final UnusedAppRestrictionsBackportServiceConnection r2 = new UnusedAppRestrictionsBackportServiceConnection(r7);
        Objects.requireNonNull(r2);
        r0.addListener(new PackageManagerCompat$$ExternalSyntheticLambda0(r2), Executors.newSingleThreadExecutor());
        r2.connectAndFetchResult(r0);
        return r0;
    L26:
        if (Api30Impl.areUnusedAppRestrictionsEnabled(r7) == true) goto L29;
        r3 = 2;
    L29:
        r0.set(Integer.valueOf(r3));
        return r0;
    }
}
