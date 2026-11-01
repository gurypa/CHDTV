package androidx.core.os;

import android.os.Build;
import android.os.Process;
import android.os.UserHandle;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public final class ProcessCompat {

    static class Api16Impl {
        private static Method sMethodUserIdIsAppMethod;
        private static boolean sResolved;
        private static final Object sResolvedLock = null;

        static {
            sResolvedLock = new Object();
        }

        private Api16Impl() {
        }

        static boolean isApplicationUid(int r7) {
            Object r1 = sResolvedLock;     // Catch: Exception -> L21
            monitor-enter(r1);     // Catch: Exception -> L21
        L18:
            th = move-exception;
            throw th;     // Catch: Exception -> L21
        L6:
            if (sResolved == true) goto L8;
            sResolved = true;     // Catch: Throwable -> L18
            sMethodUserIdIsAppMethod = Class.forName("android.os.UserId").getDeclaredMethod("isApp", new Class[]{Integer.TYPE});     // Catch: Throwable -> L18
        L8:
            monitor-exit(r1);     // Catch: Throwable -> L18
        L10:
            if (sMethodUserIdIsAppMethod == null) goto L23;
            Boolean r2 = (Boolean) sMethodUserIdIsAppMethod.invoke(null, new Object[]{Integer.valueOf(r7)});     // Catch: Exception -> L21
            if (r2 == null) goto L16;
            return r2.booleanValue();
        L16:
            throw new NullPointerException();     // Catch: Exception -> L21
        L23:
            return true;
        L21:
            e = move-exception;
            e.printStackTrace();
            goto L23
        }
    }

    static class Api17Impl {
        private static Method sMethodUserHandleIsAppMethod;
        private static boolean sResolved;
        private static final Object sResolvedLock = null;

        static {
            sResolvedLock = new Object();
        }

        private Api17Impl() {
        }

        static boolean isApplicationUid(int r7) {
            Object r1 = sResolvedLock;     // Catch: Exception -> L21
            monitor-enter(r1);     // Catch: Exception -> L21
        L18:
            th = move-exception;
            throw th;     // Catch: Exception -> L21
        L6:
            if (sResolved == true) goto L8;
            sResolved = true;     // Catch: Throwable -> L18
            sMethodUserHandleIsAppMethod = UserHandle.class.getDeclaredMethod("isApp", new Class[]{Integer.TYPE});     // Catch: Throwable -> L18
        L8:
            monitor-exit(r1);     // Catch: Throwable -> L18
        L10:
            if (sMethodUserHandleIsAppMethod == null) goto L23;
            Boolean r2 = (Boolean) sMethodUserHandleIsAppMethod.invoke(null, new Object[]{Integer.valueOf(r7)});     // Catch: Exception -> L21
            if (r2 == null) goto L16;
            return r2.booleanValue();
        L16:
            throw new NullPointerException();     // Catch: Exception -> L21
        L23:
            return true;
        L21:
            e = move-exception;
            e.printStackTrace();
            goto L23
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static boolean isApplicationUid(int r1) {
            return Process.isApplicationUid(r1);
        }
    }

    private ProcessCompat() {
    }

    public static boolean isApplicationUid(int r2) {
        if (Build.VERSION.SDK_INT < 24) goto L7;
        return Api24Impl.isApplicationUid(r2);
    L7:
        return Api17Impl.isApplicationUid(r2);
    }
}
