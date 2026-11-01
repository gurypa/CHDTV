package androidx.core.graphics;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseArray;
import androidx.collection.LongSparseArray;
import androidx.core.content.res.FontResourcesParserCompat;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class WeightTypefaceApi14 {
    private static final String NATIVE_INSTANCE_FIELD = "native_instance";
    private static final String TAG = "WeightTypeface";
    private static final Field sNativeInstance = null;
    private static final Object sWeightCacheLock = null;
    private static final LongSparseArray<SparseArray<Typeface>> sWeightTypefaceCache = null;

    static {
        Field r0 = Typeface.class.getDeclaredField(NATIVE_INSTANCE_FIELD);     // Catch: Exception -> L4
        r0.setAccessible(true);     // Catch: Exception -> L4
    L6:
        sNativeInstance = r0;
        sWeightTypefaceCache = new LongSparseArray(3);
        sWeightCacheLock = new Object();
        return;
    L4:
        e = move-exception;
        Log.e(TAG, e.getClass().getName(), e);
        r0 = null;
        goto L6
    }

    private WeightTypefaceApi14() {
    }

    static Typeface createWeightStyle(TypefaceCompatBaseImpl r7, Context r8, Typeface r9, int r10, boolean r11) {
        if (isPrivateApiAvailable() == true) goto L6;
        return null;
    L6:
        int r0 = (r10 << 1) | (r11 ? 1 : 0);
        Object r1 = sWeightCacheLock;
        monitor-enter(r1);
        long r2 = getNativeInstance(r9);     // Catch: Throwable -> L21
        SparseArray<Typeface> r4 = sWeightTypefaceCache.get(r2);     // Catch: Throwable -> L21
        if (r4 != null) goto L11;
        r4 = new SparseArray(4);     // Catch: Throwable -> L21
        sWeightTypefaceCache.put(r2, r4);     // Catch: Throwable -> L21
    L15:
        Typeface r5 = getBestFontFromFamily(r7, r8, r9, r10, r11);     // Catch: Throwable -> L21
        if (r5 != null) goto L18;
        r5 = platformTypefaceCreate(r9, r10, r11);     // Catch: Throwable -> L21
    L18:
        r4.put(r0 == true ? 1 : 0, r5);     // Catch: Throwable -> L21
        monitor-exit(r1);     // Catch: Throwable -> L21
        return r5;
    L11:
        Typeface r6 = r4.get(r0 == true ? 1 : 0);     // Catch: Throwable -> L21
        if (r6 == null) goto L15;
        monitor-exit(r1);     // Catch: Throwable -> L21
        return r6;
    L21:
        th = move-exception;
        throw th;
    }

    private static Typeface getBestFontFromFamily(TypefaceCompatBaseImpl r6, Context r7, Typeface r8, int r9, boolean r10) {
        FontResourcesParserCompat.FontFamilyFilesResourceEntry r2 = r6.getFontFamily(r8);
        if (r2 != null) goto L7;
        return null;
    L7:
        return r6.createFromFontFamilyFilesResourceEntry(r7, r2, r7.getResources(), r9, r10);
    }

    private static long getNativeInstance(Typeface r3) {
        return ((Number) sNativeInstance.get(r3)).longValue();
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

    private static Typeface platformTypefaceCreate(Typeface r3, int r4, boolean r5) {
        if (r4 < 600) goto L5;
        boolean r0 = true;
    L7:
        if (r0 == true) goto L10;
        if (r5 == true) goto L10;
        int r1 = 0;
    L16:
        return Typeface.create(r3, r1);
    L10:
        if (r0 == true) goto L12;
        r1 = 2;
        goto L16
    L12:
        if (r5 == true) goto L14;
        r1 = 1;
        goto L16
    L14:
        r1 = 3;
        goto L16
    L5:
        r0 = false;
        goto L7
    }
}
