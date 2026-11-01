package androidx.core.graphics;

import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseArray;
import androidx.collection.LongSparseArray;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
final class WeightTypefaceApi21 {
    private static final String NATIVE_CREATE_FROM_TYPEFACE_METHOD = "nativeCreateFromTypeface";
    private static final String NATIVE_CREATE_WEIGHT_ALIAS_METHOD = "nativeCreateWeightAlias";
    private static final String NATIVE_INSTANCE_FIELD = "native_instance";
    private static final String TAG = "WeightTypeface";
    private static final Constructor<Typeface> sConstructor = null;
    private static final Method sNativeCreateFromTypeface = null;
    private static final Method sNativeCreateWeightAlias = null;
    private static final Field sNativeInstance = null;
    private static final Object sWeightCacheLock = null;
    private static final LongSparseArray<SparseArray<Typeface>> sWeightTypefaceCache = null;

    static {
        Field r0 = Typeface.class.getDeclaredField(NATIVE_INSTANCE_FIELD);     // Catch: Throwable -> L13
    L11:
        e = e;
    L14:
        Log.e(TAG, e.getClass().getName(), e);
        r0 = null;
        Method r1 = null;
        Method r2 = null;
        Constructor<Typeface> r3 = null;
    L15:
        sNativeInstance = r0;
        sNativeCreateFromTypeface = r1;
        sNativeCreateWeightAlias = r2;
        sConstructor = r3;
        sWeightTypefaceCache = new LongSparseArray(3);
        sWeightCacheLock = new Object();
        return;
    L5:
        r1 = Typeface.class.getDeclaredMethod(NATIVE_CREATE_FROM_TYPEFACE_METHOD, new Class[]{Long.TYPE, Integer.TYPE});     // Catch: Throwable -> L13 Throwable -> L13
        r1.setAccessible(true);     // Catch: Throwable -> L13 Throwable -> L13
        r2 = Typeface.class.getDeclaredMethod(NATIVE_CREATE_WEIGHT_ALIAS_METHOD, new Class[]{Long.TYPE, Integer.TYPE});     // Catch: Throwable -> L13 Throwable -> L13
        r2.setAccessible(true);     // Catch: Throwable -> L13 Throwable -> L13
        r3 = Typeface.class.getDeclaredConstructor(new Class[]{Long.TYPE});     // Catch: Throwable -> L13 Throwable -> L13
        r3.setAccessible(true);     // Catch: Throwable -> L13 Throwable -> L13
    L13:
        e = e;
        goto L14
    }

    private WeightTypefaceApi21() {
    }

    private static Typeface create(long r5) {
        return sConstructor.newInstance(new Object[]{Long.valueOf(r5)});
    L10:
        return null;
    L8:
        return null;
    L6:
        return null;
    }

    static Typeface createWeightStyle(Typeface r7, int r8, boolean r9) {
        if (isPrivateApiAvailable() == true) goto L6;
        return null;
    L6:
        int r0 = (r8 << 1) | (r9 ? 1 : 0);
        Object r1 = sWeightCacheLock;
        monitor-enter(r1);
        long r2 = getNativeInstance(r7);     // Catch: Throwable -> L22
        SparseArray<Typeface> r4 = sWeightTypefaceCache.get(r2);     // Catch: Throwable -> L22
        if (r4 != null) goto L11;
        r4 = new SparseArray(4);     // Catch: Throwable -> L22
        sWeightTypefaceCache.put(r2, r4);     // Catch: Throwable -> L22
    L16:
        if (r9 != r7.isItalic()) goto L18;
        Typeface r5 = create(nativeCreateWeightAlias(r2, r8));     // Catch: Throwable -> L22
    L19:
        r4.put(r0 == true ? 1 : 0, r5);     // Catch: Throwable -> L22
        monitor-exit(r1);     // Catch: Throwable -> L22
        return r5;
    L18:
        r5 = create(nativeCreateFromTypefaceWithExactStyle(r2, r8, r9));     // Catch: Throwable -> L22
        goto L19
    L11:
        Typeface r6 = r4.get(r0 == true ? 1 : 0);     // Catch: Throwable -> L22
        if (r6 == null) goto L16;
        monitor-exit(r1);     // Catch: Throwable -> L22
        return r6;
    L22:
        th = move-exception;
        throw th;
    }

    private static long getNativeInstance(Typeface r2) {
        return sNativeInstance.getLong(r2);
    L4:
        e = move-exception;
        throw new RuntimeException(e);
    }

    private static boolean isPrivateApiAvailable() {
        if (sNativeInstance == null) goto L5;
        return true;
    L5:
        return false;
    }

    private static long nativeCreateFromTypefaceWithExactStyle(long r8, int r10, boolean r11) {
        if (r11 == false) goto L5;
        int r2 = 2;
    L14:
        return ((Long) sNativeCreateWeightAlias.invoke(null, new Object[]{Long.valueOf(((Long) sNativeCreateFromTypeface.invoke(null, new Object[]{Long.valueOf(r8), Integer.valueOf(r2)})).longValue()), Integer.valueOf(r10)})).longValue();
    L11:
        e = move-exception;
        throw new RuntimeException(e);
    L8:
        e = move-exception;
        throw new RuntimeException(e);
    L5:
        r2 = 0;
        goto L14
    }

    private static long nativeCreateWeightAlias(long r5, int r7) {
        return ((Long) sNativeCreateWeightAlias.invoke(null, new Object[]{Long.valueOf(r5), Integer.valueOf(r7)})).longValue();
    L7:
        e = move-exception;
        throw new RuntimeException(e);
    L4:
        e = move-exception;
        throw new RuntimeException(e);
    }
}
