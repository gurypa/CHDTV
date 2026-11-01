package androidx.core.app;

import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public final class BundleCompat {

    static class Api18Impl {
        private Api18Impl() {
        }

        static IBinder getBinder(Bundle r1, String r2) {
            return r1.getBinder(r2);
        }

        static void putBinder(Bundle r0, String r1, IBinder r2) {
            r0.putBinder(r1, r2);
        }
    }

    static class BeforeApi18Impl {
        private static final String TAG = "BundleCompatBaseImpl";
        private static Method sGetIBinderMethod;
        private static boolean sGetIBinderMethodFetched;
        private static Method sPutIBinderMethod;
        private static boolean sPutIBinderMethodFetched;

        private BeforeApi18Impl() {
        }

        public static IBinder getBinder(Bundle r7, String r8) {
            if (sGetIBinderMethodFetched == true) goto L10;
            sGetIBinderMethod = Bundle.class.getMethod("getIBinder", new Class[]{String.class});     // Catch: NoSuchMethodException -> L6
            sGetIBinderMethod.setAccessible(true);     // Catch: NoSuchMethodException -> L6
        L8:
            sGetIBinderMethodFetched = true;
        L6:
            e = move-exception;
            Log.i(TAG, "Failed to retrieve getIBinder method", e);
        L10:
            if (sGetIBinderMethod != null) goto L22;
        L19:
            return null;
        L22:
            return (IBinder) sGetIBinderMethod.invoke(r7, new Object[]{r8});
        L13:
            e = move-exception;
            Log.i(TAG, "Failed to invoke getIBinder via reflection", e);
            sGetIBinderMethod = null;
            goto L19
        }

        public static void putBinder(Bundle r8, String r9, IBinder r10) {
            if (sPutIBinderMethodFetched == true) goto L10;
            sPutIBinderMethod = Bundle.class.getMethod("putIBinder", new Class[]{String.class, IBinder.class});     // Catch: NoSuchMethodException -> L6
            sPutIBinderMethod.setAccessible(true);     // Catch: NoSuchMethodException -> L6
        L8:
            sPutIBinderMethodFetched = true;
        L6:
            e = move-exception;
            Log.i(TAG, "Failed to retrieve putIBinder method", e);
        L10:
            if (sPutIBinderMethod == null) goto L24;
            sPutIBinderMethod.invoke(r8, new Object[]{r9, r10});     // Catch: IllegalArgumentException -> L13 Throwable -> L15 InvocationTargetException -> L17
            return;
        L15:
            e = move-exception;
            Log.i(TAG, "Failed to invoke putIBinder via reflection", e);
            sPutIBinderMethod = null;
            return;
        }
    }

    private BundleCompat() {
    }

    public static IBinder getBinder(Bundle r1, String r2) {
        return Api18Impl.getBinder(r1, r2);
    }

    public static void putBinder(Bundle r0, String r1, IBinder r2) {
        Api18Impl.putBinder(r0, r1, r2);
    }
}
