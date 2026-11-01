package androidx.core.content;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public final class IntentCompat {
    public static final String ACTION_CREATE_REMINDER = "android.intent.action.CREATE_REMINDER";
    public static final String CATEGORY_LEANBACK_LAUNCHER = "android.intent.category.LEANBACK_LAUNCHER";
    public static final String EXTRA_HTML_TEXT = "android.intent.extra.HTML_TEXT";
    public static final String EXTRA_START_PLAYBACK = "android.intent.extra.START_PLAYBACK";
    public static final String EXTRA_TIME = "android.intent.extra.TIME";

    static class Api15Impl {
        private Api15Impl() {
        }

        static Intent makeMainSelectorActivity(String r1, String r2) {
            return Intent.makeMainSelectorActivity(r1, r2);
        }
    }

    private IntentCompat() {
    }

    public static Intent createManageUnusedAppRestrictionsIntent(Context r4, String r5) {
        if (PackageManagerCompat.areUnusedAppRestrictionsAvailable(r4.getPackageManager()) == false) goto L14;
        if (Build.VERSION.SDK_INT >= 31) goto L7;
        Intent r0 = new Intent(PackageManagerCompat.ACTION_PERMISSION_REVOCATION_SETTINGS).setData(Uri.fromParts("package", r5, null));
        if (Build.VERSION.SDK_INT < 30) goto L12;
        return r0;
    L12:
        return r0.setPackage((String) Preconditions.checkNotNull(PackageManagerCompat.getPermissionRevocationVerifierApp(r4.getPackageManager())));
    L7:
        return new Intent("android.settings.APPLICATION_DETAILS_SETTINGS").setData(Uri.fromParts("package", r5, null));
    L14:
        throw new UnsupportedOperationException("Unused App Restriction features are not available on this device");
    }

    public static Intent makeMainSelectorActivity(String r1, String r2) {
        return Api15Impl.makeMainSelectorActivity(r1, r2);
    }
}
