package androidx.core.telephony;

import android.os.Build;
import android.telephony.TelephonyManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyManagerCompat {
    private static Method sGetDeviceIdMethod;
    private static Method sGetSubIdMethod;

    private static class Api23Impl {
        private Api23Impl() {
        }

        static String getDeviceId(TelephonyManager r1, int r2) {
            return r1.getDeviceId(r2);
        }
    }

    private static class Api26Impl {
        private Api26Impl() {
        }

        static String getImei(TelephonyManager r1) {
            return r1.getImei();
        }
    }

    private static class Api30Impl {
        private Api30Impl() {
        }

        static int getSubscriptionId(TelephonyManager r1) {
            return r1.getSubscriptionId();
        }
    }

    private TelephonyManagerCompat() {
    }

    public static String getImei(TelephonyManager r3) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        int r0 = getSubscriptionId(r3);
        if (r0 == Integer.MAX_VALUE) goto L13;
        if (r0 == (-1)) goto L13;
        return Api23Impl.getDeviceId(r3, SubscriptionManagerCompat.getSlotIndex(r0));
    L13:
        return r3.getDeviceId();
    L5:
        return Api26Impl.getImei(r3);
    }

    public static int getSubscriptionId(TelephonyManager r4) {
        if (Build.VERSION.SDK_INT >= 30) goto L5;
        if (sGetSubIdMethod != null) goto L10;
        sGetSubIdMethod = TelephonyManager.class.getDeclaredMethod("getSubId", new Class[0]);     // Catch: NoSuchMethodException -> L16 IllegalAccessException -> L18 InvocationTargetException -> L20
        sGetSubIdMethod.setAccessible(true);     // Catch: NoSuchMethodException -> L16 IllegalAccessException -> L18 InvocationTargetException -> L20
    L10:
        Integer r0 = (Integer) sGetSubIdMethod.invoke(r4, new Object[0]);     // Catch: NoSuchMethodException -> L16 IllegalAccessException -> L18 InvocationTargetException -> L20
        if (r0 != null) goto L13;
        return Integer.MAX_VALUE;
    L13:
        if (r0.intValue() == (-1)) goto L28;
        return r0.intValue();
    L28:
        return Integer.MAX_VALUE;
    L29:
        return Integer.MAX_VALUE;
    L22:
        return Integer.MAX_VALUE;
    L26:
        return Integer.MAX_VALUE;
    L5:
        return Api30Impl.getSubscriptionId(r4);
    }
}
