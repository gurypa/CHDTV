package androidx.core.telephony;

import android.os.Build;
import android.telephony.SubscriptionManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class SubscriptionManagerCompat {
    private static Method sGetSlotIndexMethod;

    private static class Api29Impl {
        private Api29Impl() {
        }

        static int getSlotIndex(int r1) {
            return SubscriptionManager.getSlotIndex(r1);
        }
    }

    private SubscriptionManagerCompat() {
    }

    public static int getSlotIndex(int r7) {
        if (r7 != (-1)) goto L6;
        return -1;
    L6:
        if (Build.VERSION.SDK_INT >= 29) goto L8;
        if (sGetSlotIndexMethod == null) goto L12;
    L16:
        Integer r1 = (Integer) sGetSlotIndexMethod.invoke(null, new Object[]{Integer.valueOf(r7)});     // Catch: InvocationTargetException -> L21 IllegalAccessException -> L23 NoSuchMethodException -> L25
        if (r1 == null) goto L27;
        return r1.intValue();
        goto L27
    L12:
        if (Build.VERSION.SDK_INT < 26) goto L14;
        sGetSlotIndexMethod = SubscriptionManager.class.getDeclaredMethod("getSlotIndex", new Class[]{Integer.TYPE});     // Catch: InvocationTargetException -> L21 IllegalAccessException -> L23 NoSuchMethodException -> L25
    L15:
        sGetSlotIndexMethod.setAccessible(true);     // Catch: InvocationTargetException -> L21 IllegalAccessException -> L23 NoSuchMethodException -> L25
        goto L16
    L14:
        sGetSlotIndexMethod = SubscriptionManager.class.getDeclaredMethod("getSlotId", new Class[]{Integer.TYPE});     // Catch: InvocationTargetException -> L21 IllegalAccessException -> L23 NoSuchMethodException -> L25
    L27:
        return -1;
    L8:
        return Api29Impl.getSlotIndex(r7);
    }
}
