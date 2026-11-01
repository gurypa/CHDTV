package androidx.core.app;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;

/* JADX INFO: loaded from: classes.dex */
public final class AppOpsManagerCompat {
    public static final int MODE_ALLOWED = 0;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_ERRORED = 2;
    public static final int MODE_IGNORED = 1;

    static class Api19Impl {
        private Api19Impl() {
        }

        static int noteOp(AppOpsManager r1, String r2, int r3, String r4) {
            return r1.noteOp(r2, r3, r4);
        }

        static int noteOpNoThrow(AppOpsManager r1, String r2, int r3, String r4) {
            return r1.noteOpNoThrow(r2, r3, r4);
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static <T> T getSystemService(Context r1, Class<T> r2) {
            return (T) r1.getSystemService(r2);
        }

        static int noteProxyOp(AppOpsManager r1, String r2, String r3) {
            return r1.noteProxyOp(r2, r3);
        }

        static int noteProxyOpNoThrow(AppOpsManager r1, String r2, String r3) {
            return r1.noteProxyOpNoThrow(r2, r3);
        }

        static String permissionToOp(String r1) {
            return AppOpsManager.permissionToOp(r1);
        }
    }

    static class Api29Impl {
        private Api29Impl() {
        }

        static int checkOpNoThrow(AppOpsManager r1, String r2, int r3, String r4) {
            if (r1 != null) goto L6;
            return 1;
        L6:
            return r1.checkOpNoThrow(r2, r3, r4);
        }

        static String getOpPackageName(Context r1) {
            return r1.getOpPackageName();
        }

        static AppOpsManager getSystemService(Context r1) {
            return (AppOpsManager) r1.getSystemService(AppOpsManager.class);
        }
    }

    private AppOpsManagerCompat() {
    }

    public static int checkOrNoteProxyOp(Context r5, int r6, String r7, String r8) {
        if (Build.VERSION.SDK_INT < 29) goto L10;
        AppOpsManager r0 = Api29Impl.getSystemService(r5);
        int r2 = Api29Impl.checkOpNoThrow(r0, r7, Binder.getCallingUid(), r8);
        if (r2 == 0) goto L8;
        return r2;
    L8:
        return Api29Impl.checkOpNoThrow(r0, r7, r6, Api29Impl.getOpPackageName(r5));
    L10:
        return noteProxyOpNoThrow(r5, r7, r8);
    }

    public static int noteOp(Context r2, String r3, int r4, String r5) {
        return Api19Impl.noteOp((AppOpsManager) r2.getSystemService("appops"), r3, r4, r5);
    }

    public static int noteOpNoThrow(Context r2, String r3, int r4, String r5) {
        return Api19Impl.noteOpNoThrow((AppOpsManager) r2.getSystemService("appops"), r3, r4, r5);
    }

    public static int noteProxyOp(Context r2, String r3, String r4) {
        return Api23Impl.noteProxyOp((AppOpsManager) Api23Impl.getSystemService(r2, AppOpsManager.class), r3, r4);
    }

    public static int noteProxyOpNoThrow(Context r2, String r3, String r4) {
        return Api23Impl.noteProxyOpNoThrow((AppOpsManager) Api23Impl.getSystemService(r2, AppOpsManager.class), r3, r4);
    }

    public static String permissionToOp(String r1) {
        return Api23Impl.permissionToOp(r1);
    }
}
