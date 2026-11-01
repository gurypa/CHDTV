package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl {
    private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String TAG = "TypefaceCompatApi21Impl";
    private static Method sAddFontWeightStyle;
    private static Method sCreateFromFamiliesWithDefault;
    private static Class<?> sFontFamily;
    private static Constructor<?> sFontFamilyCtor;
    private static boolean sHasInitBeenCalled;

    static {
        sHasInitBeenCalled = false;
    }

    TypefaceCompatApi21Impl() {
    }

    private static boolean addFontWeightStyle(Object r5, String r6, int r7, boolean r8) {
        init();
        return ((Boolean) sAddFontWeightStyle.invoke(r5, new Object[]{r6, Integer.valueOf(r7), Boolean.valueOf(r8)})).booleanValue();
    L5:
        e = move-exception;
        throw new RuntimeException(e);
    }

    private static Typeface createFromFamiliesWithDefault(Object r4) {
        init();
        Object r0 = Array.newInstance(sFontFamily, 1);     // Catch: Throwable -> L5 IllegalAccessException -> L7
        Array.set(r0, 0, r4);     // Catch: Throwable -> L5 IllegalAccessException -> L7
        return (Typeface) sCreateFromFamiliesWithDefault.invoke(null, new Object[]{r0});
    L5:
        e = move-exception;
        throw new RuntimeException(e);
    }

    private File getFile(ParcelFileDescriptor r4) {
        String r1 = Os.readlink("/proc/self/fd/" + r4.getFd());     // Catch: ErrnoException -> L8
        if (OsConstants.S_ISREG(Os.stat(r1).st_mode) == false) goto L7;
        return new File(r1);
    L7:
        return null;
    L9:
        return null;
    }

    private static void init() {
        if (sHasInitBeenCalled == false) goto L5;
        return;
    L5:
        sHasInitBeenCalled = true;
        Class<?> r1 = Class.forName(FONT_FAMILY_CLASS);     // Catch: Throwable -> L15
        Constructor<?> r3 = r1.getConstructor(new Class[0]);     // Catch: Throwable -> L15
    L13:
        e = e;
    L16:
        Log.e(TAG, e.getClass().getName(), e);
        r1 = null;
        r3 = null;
        Method r4 = null;
        Method r0 = null;
    L17:
        sFontFamilyCtor = r3;
        sFontFamily = r1;
        sAddFontWeightStyle = r4;
        sCreateFromFamiliesWithDefault = r0;
        return;
    L9:
        r4 = r1.getMethod(ADD_FONT_WEIGHT_STYLE_METHOD, new Class[]{String.class, Integer.TYPE, Boolean.TYPE});     // Catch: Throwable -> L15 Throwable -> L15
        r0 = Typeface.class.getMethod(CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD, new Class[]{Array.newInstance(r1, 1).getClass()});     // Catch: Throwable -> L15 Throwable -> L15
    L15:
        e = e;
        goto L16
    }

    private static Object newFamily() {
        init();
        return sFontFamilyCtor.newInstance(new Object[0]);
    L5:
        e = move-exception;
        throw new RuntimeException(e);
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromFontFamilyFilesResourceEntry(Context r11, FontResourcesParserCompat.FontFamilyFilesResourceEntry r12, Resources r13, int r14) {
        Object r0 = newFamily();
        FontResourcesParserCompat.FontFileResourceEntry[] r1 = r12.getEntries();
        int r2 = r1.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L23;
        FontResourcesParserCompat.FontFileResourceEntry r4 = r1[r3];
        File r5 = TypefaceCompatUtil.getTempFile(r11);
        if (r5 == null) goto L6;
    L16:
        th = move-exception;
        r5.delete();
        throw th;
    L8:
        if (TypefaceCompatUtil.copyToFile(r5, r13, r4.getResourceId()) == false) goto L9;
        if (addFontWeightStyle(r0, r5.getPath(), r4.getWeight(), r4.isItalic()) == false) goto L13;
        r5.delete();
        r3 = r3 + 1;
        goto L3
    L13:
        r5.delete();
        return null;
        goto L20
    L9:
        r5.delete();
        return null;
    L20:
        r5.delete();
        return null;
    L6:
        return null;
    L23:
        return createFromFamiliesWithDefault(r0);
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromFontInfo(Context r9, CancellationSignal r10, FontsContractCompat.FontInfo[] r11, int r12) {
        if (r11.length >= 1) goto L5;
        return null;
    L5:
        FontsContractCompat.FontInfo r0 = findBestInfo(r11, r12);
        ContentResolver r1 = r9.getContentResolver();
        ParcelFileDescriptor r3 = r1.openFileDescriptor(r0.getUri(), "r", r10);     // Catch: IOException -> L39
        if (r3 != null) goto L46;
        if (r3 == null) goto L10;
        r3.close();     // Catch: IOException -> L39
    L10:
        return null;
    L46:
        File r4 = getFile(r3);     // Catch: Throwable -> L32
        if (r4 != null) goto L14;
    L20:
        FileInputStream r5 = new FileInputStream(r3.getFileDescriptor());     // Catch: Throwable -> L32
        Typeface r6 = super.createFromInputStream(r9, r5);     // Catch: Throwable -> L26
        r5.close();     // Catch: Throwable -> L32
        if (r3 == null) goto L25;
        r3.close();     // Catch: IOException -> L39
    L25:
        return r6;
    L26:
        th = move-exception;
        r5.close();     // Catch: Throwable -> L29
    L31:
        throw th;     // Catch: Throwable -> L32
    L29:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L32
        goto L31
    L14:
        if (r4.canRead() == false) goto L20;
        Typeface r7 = Typeface.createFromFile(r4);     // Catch: Throwable -> L32
        if (r3 == null) goto L19;
        r3.close();     // Catch: IOException -> L39
    L19:
        return r7;
    L32:
        th = move-exception;
        if (r3 != null) goto L41;
    L38:
        throw th;     // Catch: IOException -> L39
    L41:
        r3.close();     // Catch: Throwable -> L36
    L36:
        th = move-exception;
        th.addSuppressed(th);     // Catch: IOException -> L39
    L40:
        return null;
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
