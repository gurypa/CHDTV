package androidx.core.content.p000pm;

import android.content.pm.PermissionInfo;
import android.os.Build;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public final class PermissionInfoCompat {

    static class Api28Impl {
        private Api28Impl() {
        }

        static int getProtection(PermissionInfo r1) {
            return r1.getProtection();
        }

        static int getProtectionFlags(PermissionInfo r1) {
            return r1.getProtectionFlags();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Protection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ProtectionFlags {
    }

    private PermissionInfoCompat() {
    }

    public static int getProtection(PermissionInfo r2) {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return Api28Impl.getProtection(r2);
    L7:
        return r2.protectionLevel & 15;
    }

    public static int getProtectionFlags(PermissionInfo r2) {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return Api28Impl.getProtectionFlags(r2);
    L7:
        return r2.protectionLevel & (-16);
    }
}
