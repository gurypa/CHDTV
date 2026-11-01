package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
class TypefaceCompatBaseImpl {
    private static final int INVALID_KEY = 0;
    private static final String TAG = "TypefaceCompatBaseImpl";
    private ConcurrentHashMap<Long, FontResourcesParserCompat.FontFamilyFilesResourceEntry> mFontFamilies;

    private interface StyleExtractor<T> {
        int getWeight(T r1);

        boolean isItalic(T r1);
    }

    TypefaceCompatBaseImpl() {
        this.mFontFamilies = new ConcurrentHashMap();
    }

    private void addFontFamily(Typeface r5, FontResourcesParserCompat.FontFamilyFilesResourceEntry r6) {
        long r0 = getUniqueKey(r5);
        if (r0 == 0) goto L6;
        this.mFontFamilies.put(Long.valueOf(r0), r6);
        return;
    }

    private FontResourcesParserCompat.FontFileResourceEntry findBestEntry(FontResourcesParserCompat.FontFamilyFilesResourceEntry r3, int r4) {
        return (FontResourcesParserCompat.FontFileResourceEntry) findBestFont(r3.getEntries(), r4, new C00622(this));
    }

    private FontResourcesParserCompat.FontFileResourceEntry findBestEntry(FontResourcesParserCompat.FontFamilyFilesResourceEntry r3, int r4, boolean r5) {
        return (FontResourcesParserCompat.FontFileResourceEntry) findBestFont(r3.getEntries(), r4, r5, new C00633(this));
    }

    private static <T> T findBestFont(T[] r3, int r4, StyleExtractor<T> r5) {
        if ((r4 & 1) != 0) goto L5;
        int r0 = 400;
    L7:
        if ((r4 & 2) == 0) goto L9;
        boolean r1 = true;
    L11:
        return (T) findBestFont(r3, r0, r1, r5);
    L9:
        r1 = false;
        goto L11
    L5:
        r0 = 700;
        goto L7
    }

    private static <T> T findBestFont(T[] r8, int r9, boolean r10, StyleExtractor<T> r11) {
        T r0 = null;
        int r1 = Integer.MAX_VALUE;
        int r2 = r8.length;
        int r4 = 0;
    L3:
        if (r4 >= r2) goto L13;
        T r5 = r8[r4];
        int r6 = Math.abs(r11.getWeight(r5) - r9) * 2;
        if (r11.isItalic(r5) != r10) goto L7;
        int r7 = 0;
    L8:
        int r12 = r6 + r7;
        if (r0 == null) goto L11;
        if (r1 > r12) goto L11;
    L12:
        r4 = r4 + 1;
    L11:
        r0 = r5;
        r1 = r12;
        goto L12
    L7:
        r7 = 1;
        goto L8
    L13:
        return r0;
    }

    private static long getUniqueKey(Typeface r6) {
        if (r6 != null) goto L13;
        return 0;
    L13:
        Field r4 = Typeface.class.getDeclaredField("native_instance");     // Catch: IllegalAccessException -> L7 NoSuchFieldException -> L10
        r4.setAccessible(true);     // Catch: IllegalAccessException -> L7 NoSuchFieldException -> L10
        return ((Number) r4.get(r6)).longValue();
    L7:
        e = move-exception;
        Log.e(TAG, "Could not retrieve font from family.", e);
        return 0;
    L10:
        e = move-exception;
        Log.e(TAG, "Could not retrieve font from family.", e);
        return 0;
    }

    public Typeface createFromFontFamilyFilesResourceEntry(Context r9, FontResourcesParserCompat.FontFamilyFilesResourceEntry r10, Resources r11, int r12) {
        FontResourcesParserCompat.FontFileResourceEntry r0 = findBestEntry(r10, r12);
        if (r0 != null) goto L6;
        return null;
    L6:
        Typeface r13 = TypefaceCompat.createFromResourcesFontFile(r9, r11, r0.getResourceId(), r0.getFileName(), 0, r12);
        addFontFamily(r13, r10);
        return r13;
    }

    Typeface createFromFontFamilyFilesResourceEntry(Context r9, FontResourcesParserCompat.FontFamilyFilesResourceEntry r10, Resources r11, int r12, boolean r13) {
        FontResourcesParserCompat.FontFileResourceEntry r0 = findBestEntry(r10, r12, r13);
        if (r0 != null) goto L6;
        return null;
    L6:
        Typeface r14 = TypefaceCompat.createFromResourcesFontFile(r9, r11, r0.getResourceId(), r0.getFileName(), 0, 0);
        addFontFamily(r14, r10);
        return r14;
    }

    public Typeface createFromFontInfo(Context r6, CancellationSignal r7, FontsContractCompat.FontInfo[] r8, int r9) {
        if (r8.length >= 1) goto L5;
        return null;
    L5:
        FontsContractCompat.FontInfo r0 = findBestInfo(r8, r9);
        InputStream r1 = null;
        r1 = r6.getContentResolver().openInputStream(r0.getUri());     // Catch: Throwable -> L9 IOException -> L12
        Typeface r2 = createFromInputStream(r6, r1);     // Catch: Throwable -> L9 IOException -> L12
        TypefaceCompatUtil.closeQuietly(r1);
        return r2;
    L9:
        th = move-exception;
        TypefaceCompatUtil.closeQuietly(r1);
        throw th;
    L13:
        TypefaceCompatUtil.closeQuietly(r1);
        return null;
    }

    protected Typeface createFromInputStream(Context r4, InputStream r5) {
        File r0 = TypefaceCompatUtil.getTempFile(r4);
        if (r0 != null) goto L18;
        return null;
    L18:
    L12:
        th = move-exception;
        r0.delete();
        throw th;
    L6:
        if (TypefaceCompatUtil.copyToFile(r0, r5) == true) goto L9;
        r0.delete();
        return null;
    L9:
        Typeface r1 = Typeface.createFromFile(r0.getPath());     // Catch: Throwable -> L12 RuntimeException -> L15
        r0.delete();
        return r1;
    L16:
        r0.delete();
        return null;
    }

    public Typeface createFromResourcesFontFile(Context r4, Resources r5, int r6, String r7, int r8) {
        File r0 = TypefaceCompatUtil.getTempFile(r4);
        if (r0 != null) goto L18;
        return null;
    L18:
    L12:
        th = move-exception;
        r0.delete();
        throw th;
    L6:
        if (TypefaceCompatUtil.copyToFile(r0, r5, r6) == true) goto L9;
        r0.delete();
        return null;
    L9:
        Typeface r1 = Typeface.createFromFile(r0.getPath());     // Catch: Throwable -> L12 RuntimeException -> L15
        r0.delete();
        return r1;
    L16:
        r0.delete();
        return null;
    }

    Typeface createWeightStyle(Context r3, Typeface r4, int r5, boolean r6) {
        Typeface r0 = null;
        r0 = WeightTypefaceApi14.createWeightStyle(this, r3, r4, r5, r6);     // Catch: RuntimeException -> L5
    L6:
        if (r0 == null) goto L8;
        return r0;
    L8:
        return r4;
    }

    protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] r2, int r3) {
        return (FontsContractCompat.FontInfo) findBestFont(r2, r3, new C00611(this));
    }

    FontResourcesParserCompat.FontFamilyFilesResourceEntry getFontFamily(Typeface r5) {
        long r0 = getUniqueKey(r5);
        if (r0 != 0) goto L7;
        return null;
    L7:
        return this.mFontFamilies.get(Long.valueOf(r0));
    }
}
