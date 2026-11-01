package androidx.core.graphics;

import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseArray;
import androidx.collection.LongSparseArray;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
final class WeightTypefaceApi26 {
    private static final String NATIVE_CREATE_FROM_TYPEFACE_WITH_EXACT_STYLE_METHOD = "nativeCreateFromTypefaceWithExactStyle";
    private static final String NATIVE_INSTANCE_FIELD = "native_instance";
    private static final String TAG = "WeightTypeface";
    private static final Constructor<Typeface> sConstructor = null;
    private static final Method sNativeCreateFromTypefaceWithExactStyle = null;
    private static final Field sNativeInstance = null;
    private static final Object sWeightCacheLock = null;
    private static final LongSparseArray<SparseArray<Typeface>> sWeightTypefaceCache = null;

    static {
        Field r1 = Typeface.class.getDeclaredField(NATIVE_INSTANCE_FIELD);     // Catch: NoSuchMethodException -> L5 Throwable -> L7
        Method r2 = Typeface.class.getDeclaredMethod(NATIVE_CREATE_FROM_TYPEFACE_WITH_EXACT_STYLE_METHOD, new Class[]{Long.TYPE, Integer.TYPE, Boolean.TYPE});     // Catch: NoSuchMethodException -> L5 Throwable -> L7
        r2.setAccessible(true);     // Catch: NoSuchMethodException -> L5 Throwable -> L7
        Constructor<Typeface> r3 = Typeface.class.getDeclaredConstructor(new Class[]{Long.TYPE});     // Catch: NoSuchMethodException -> L5 Throwable -> L7
        r3.setAccessible(true);     // Catch: NoSuchMethodException -> L5 Throwable -> L7
    L9:
        sNativeInstance = r1;
        sNativeCreateFromTypefaceWithExactStyle = r2;
        sConstructor = r3;
        sWeightTypefaceCache = new LongSparseArray(3);
        sWeightCacheLock = new Object();
        return;
    L7:
        e = move-exception;
        Log.e(TAG, e.getClass().getName(), e);
        r1 = null;
        r2 = null;
        r3 = null;
        goto L9
    }

    private WeightTypefaceApi26() {
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
        long r2 = getNativeInstance(r7);     // Catch: Throwable -> L18
        SparseArray<Typeface> r4 = sWeightTypefaceCache.get(r2);     // Catch: Throwable -> L18
        if (r4 != null) goto L11;
        r4 = new SparseArray(4);     // Catch: Throwable -> L18
        sWeightTypefaceCache.put(r2, r4);     // Catch: Throwable -> L18
    L15:
        Typeface r5 = create(nativeCreateFromTypefaceWithExactStyle(r2, r8, r9));     // Catch: Throwable -> L18
        r4.put(r0 == true ? 1 : 0, r5);     // Catch: Throwable -> L18
        monitor-exit(r1);     // Catch: Throwable -> L18
        return r5;
    L11:
        Typeface r6 = r4.get(r0 == true ? 1 : 0);     // Catch: Throwable -> L18
        if (r6 == null) goto L15;
        monitor-exit(r1);     // Catch: Throwable -> L18
        return r6;
    L18:
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

    private static long nativeCreateFromTypefaceWithExactStyle(long r6, int r8, boolean r9) {
        return ((Long) sNativeCreateFromTypefaceWithExactStyle.invoke(null, new Object[]{Long.valueOf(r6), Integer.valueOf(r8), Boolean.valueOf(r9)})).longValue();
    L7:
        e = move-exception;
        throw new RuntimeException(e);
    L4:
        e = move-exception;
        throw new RuntimeException(e);
    }
}
