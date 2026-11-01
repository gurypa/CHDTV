package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class TypefaceCompatApi24Impl extends TypefaceCompatBaseImpl {
    private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String TAG = "TypefaceCompatApi24Impl";
    private static final Method sAddFontWeightStyle = null;
    private static final Method sCreateFromFamiliesWithDefault = null;
    private static final Class<?> sFontFamily = null;
    private static final Constructor<?> sFontFamilyCtor = null;

    static {
        Class<?> r0 = Class.forName(FONT_FAMILY_CLASS);     // Catch: Throwable -> L11
        Constructor<?> r2 = r0.getConstructor(new Class[0]);     // Catch: Throwable -> L11
        Class<?> r5 = Integer.TYPE;     // Catch: NoSuchMethodException -> L9 Throwable -> L11
        Method r3 = r0.getMethod(ADD_FONT_WEIGHT_STYLE_METHOD, new Class[]{ByteBuffer.class, r5, List.class, r5, Boolean.TYPE});     // Catch: Throwable -> L11 Throwable -> L11
        Method r1 = Typeface.class.getMethod(CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD, new Class[]{Array.newInstance(r0, 1).getClass()});     // Catch: Throwable -> L11 Throwable -> L11
    L13:
        sFontFamilyCtor = r2;
        sFontFamily = r0;
        sAddFontWeightStyle = r3;
        sCreateFromFamiliesWithDefault = r1;
        return;
    L9:
        e = e;
    L12:
        Log.e(TAG, e.getClass().getName(), e);
        r2 = null;
        r3 = null;
        r0 = null;
        r1 = null;
    L11:
        e = e;
        goto L12
    }

    TypefaceCompatApi24Impl() {
    }

    private static boolean addFontWeightStyle(Object r7, ByteBuffer r8, int r9, int r10, boolean r11) {
        return ((Boolean) sAddFontWeightStyle.invoke(r7, new Object[]{r8, Integer.valueOf(r9), null, Integer.valueOf(r10), Boolean.valueOf(r11)})).booleanValue();
    L8:
        return false;
    }

    private static Typeface createFromFamiliesWithDefault(Object r5) {
        Object r1 = Array.newInstance(sFontFamily, 1);     // Catch: InvocationTargetException -> L5 Throwable -> L7
        Array.set(r1, 0, r5);     // Catch: InvocationTargetException -> L5 Throwable -> L7
        return (Typeface) sCreateFromFamiliesWithDefault.invoke(null, new Object[]{r1});
    L8:
        return null;
    }

    public static boolean isUsable() {
        if (sAddFontWeightStyle != null) goto L6;
        Log.w(TAG, "Unable to collect necessary private methods.Fallback to legacy implementation.");
    L6:
        if (sAddFontWeightStyle == null) goto L8;
        return true;
    L8:
        return false;
    }

    private static Object newFamily() {
        return sFontFamilyCtor.newInstance(new Object[0]);
    L5:
        return null;
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromFontFamilyFilesResourceEntry(Context r11, FontResourcesParserCompat.FontFamilyFilesResourceEntry r12, Resources r13, int r14) {
        Object r0 = newFamily();
        if (r0 != null) goto L5;
        return null;
    L5:
        FontResourcesParserCompat.FontFileResourceEntry[] r2 = r12.getEntries();
        int r3 = r2.length;
        int r4 = 0;
    L6:
        if (r4 >= r3) goto L15;
        FontResourcesParserCompat.FontFileResourceEntry r5 = r2[r4];
        ByteBuffer r6 = TypefaceCompatUtil.copyToDirectBuffer(r11, r13, r5.getResourceId());
        if (r6 == null) goto L9;
        if (addFontWeightStyle(r0, r6, r5.getTtcIndex(), r5.getWeight(), r5.isItalic()) == false) goto L12;
        r4 = r4 + 1;
        goto L6
    L12:
        return null;
    L9:
        return null;
    L15:
        return createFromFamiliesWithDefault(r0);
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromFontInfo(Context r12, CancellationSignal r13, FontsContractCompat.FontInfo[] r14, int r15) {
        Object r0 = newFamily();
        if (r0 != null) goto L5;
        return null;
    L5:
        SimpleArrayMap r2 = new SimpleArrayMap();
        int r3 = r14.length;
        int r4 = 0;
    L6:
        if (r4 >= r3) goto L16;
        FontsContractCompat.FontInfo r5 = r14[r4];
        Uri r6 = r5.getUri();
        ByteBuffer r7 = (ByteBuffer) r2.get(r6);
        if (r7 != null) goto L10;
        r7 = TypefaceCompatUtil.mmap(r12, r13, r6);
        r2.put(r6, r7);
    L10:
        if (r7 == null) goto L11;
        if (addFontWeightStyle(r0, r7, r5.getTtcIndex(), r5.getWeight(), r5.isItalic()) == false) goto L14;
        r4 = r4 + 1;
        goto L6
    L14:
        return null;
    L11:
        return null;
    L16:
        Typeface r8 = createFromFamiliesWithDefault(r0);
        if (r8 != null) goto L20;
        return null;
    L20:
        return Typeface.create(r8, r15);
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    Typeface createWeightStyle(Context r3, Typeface r4, int r5, boolean r6) {
        Typeface r0 = null;
        r0 = WeightTypefaceApi21.createWeightStyle(r4, r5, r6);     // Catch: RuntimeException -> L5
    L6:
        if (r0 == null) goto L8;
        return r0;
    L8:
        return super.createWeightStyle(r3, r4, r5, r6);
    }
}
