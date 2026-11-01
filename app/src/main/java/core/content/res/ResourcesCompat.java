package androidx.core.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public final class ResourcesCompat {
    public static final int ID_NULL = 0;
    private static final String TAG = "ResourcesCompat";
    private static final Object sColorStateCacheLock = null;
    private static final WeakHashMap<ColorStateListCacheKey, SparseArray<ColorStateListCacheEntry>> sColorStateCaches = null;
    private static final ThreadLocal<TypedValue> sTempTypedValue = null;

    static class Api15Impl {
        private Api15Impl() {
        }

        static Drawable getDrawableForDensity(Resources r1, int r2, int r3) {
            return r1.getDrawableForDensity(r2, r3);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static Drawable getDrawable(Resources r1, int r2, Resources.Theme r3) {
            return r1.getDrawable(r2, r3);
        }

        static Drawable getDrawableForDensity(Resources r1, int r2, int r3, Resources.Theme r4) {
            return r1.getDrawableForDensity(r2, r3, r4);
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static int getColor(Resources r1, int r2, Resources.Theme r3) {
            return r1.getColor(r2, r3);
        }

        static ColorStateList getColorStateList(Resources r1, int r2, Resources.Theme r3) {
            return r1.getColorStateList(r2, r3);
        }
    }

    static class Api29Impl {
        private Api29Impl() {
        }

        static float getFloat(Resources r1, int r2) {
            return r1.getFloat(r2);
        }
    }

    private static class ColorStateListCacheEntry {
        final Configuration mConfiguration;
        final int mThemeHash;
        final ColorStateList mValue;

        ColorStateListCacheEntry(ColorStateList r2, Configuration r3, Resources.Theme r4) {
            this.mValue = r2;
            this.mConfiguration = r3;
            if (r4 != null) goto L5;
            int r0 = 0;
        L6:
            this.mThemeHash = r0;
            return;
        L5:
            r0 = r4.hashCode();
            goto L6
        }
    }

    private static final class ColorStateListCacheKey {
        final Resources mResources;
        final Resources.Theme mTheme;

        ColorStateListCacheKey(Resources r1, Resources.Theme r2) {
            this.mResources = r1;
            this.mTheme = r2;
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if (r6 != null) goto L8;
        L17:
            return false;
        L8:
            if (getClass() != r6.getClass()) goto L17;
            ColorStateListCacheKey r2 = (ColorStateListCacheKey) r6;
            if (this.mResources.equals(r2.mResources) == false) goto L16;
            if (ObjectsCompat.equals(this.mTheme, r2.mTheme) == false) goto L16;
            return true;
        L16:
            return false;
        }

        public int hashCode() {
            return ObjectsCompat.hash(new Object[]{this.mResources, this.mTheme});
        }
    }

    public static abstract class FontCallback {
        public FontCallback() {
        }

        public static Handler getHandler(Handler r2) {
            if (r2 != null) goto L5;
            return new Handler(Looper.getMainLooper());
        L5:
            return r2;
        }

        public final void callbackFailAsync(final int r3, Handler r4) {
            getHandler(r4).post(new ResourcesCompat$FontCallback$$ExternalSyntheticLambda1(this, r3));
        }

        public final void callbackSuccessAsync(final Typeface r3, Handler r4) {
            getHandler(r4).post(new ResourcesCompat$FontCallback$$ExternalSyntheticLambda0(this, r3));
        }

        /* JADX INFO: renamed from: lambda$callbackFailAsync$1$androidx-core-content-res-ResourcesCompat$FontCallback */
        /* synthetic */ void m6xb24343b7(int r1) {
            onFontRetrievalFailed(r1);
        }

        /* JADX INFO: renamed from: lambda$callbackSuccessAsync$0$androidx-core-content-res-ResourcesCompat$FontCallback */
        /* synthetic */ void m7x46c88379(Typeface r1) {
            onFontRetrieved(r1);
        }

        public abstract void onFontRetrievalFailed(int r1);

        public abstract void onFontRetrieved(Typeface r1);
    }

    public static final class ThemeCompat {

        static class Api23Impl {
            private static Method sRebaseMethod;
            private static boolean sRebaseMethodFetched;
            private static final Object sRebaseMethodLock = null;

            static {
                sRebaseMethodLock = new Object();
            }

            private Api23Impl() {
            }

            static void rebase(Resources.Theme r6) {
                Object r0 = sRebaseMethodLock;
                monitor-enter(r0);
            L23:
                th = move-exception;
                throw th;
            L6:
                if (sRebaseMethodFetched == true) goto L14;
                sRebaseMethod = Resources.Theme.class.getDeclaredMethod("rebase", new Class[0]);     // Catch: NoSuchMethodException -> L10 Throwable -> L23
                sRebaseMethod.setAccessible(true);     // Catch: NoSuchMethodException -> L10 Throwable -> L23
            L12:
                sRebaseMethodFetched = true;     // Catch: Throwable -> L23
                goto L14
            L10:
                e = move-exception;
                Log.i(ResourcesCompat.TAG, "Failed to retrieve rebase() method", e);     // Catch: Throwable -> L23
            L14:
                if (sRebaseMethod != null) goto L30;
            L21:
                monitor-exit(r0);     // Catch: Throwable -> L23
                return;
            L30:
                sRebaseMethod.invoke(r6, new Object[0]);     // Catch: InvocationTargetException -> L17 Throwable -> L19 Throwable -> L23
            L19:
                e = move-exception;
                Log.i(ResourcesCompat.TAG, "Failed to invoke rebase() method via reflection", e);     // Catch: Throwable -> L23
                sRebaseMethod = null;     // Catch: Throwable -> L23
                goto L21
            }
        }

        static class Api29Impl {
            private Api29Impl() {
            }

            static void rebase(Resources.Theme r0) {
                r0.rebase();
            }
        }

        private ThemeCompat() {
        }

        public static void rebase(Resources.Theme r2) {
            if (Build.VERSION.SDK_INT < 29) goto L5;
            Api29Impl.rebase(r2);
            return;
        L5:
            Api23Impl.rebase(r2);
        }
    }

    static {
        sTempTypedValue = new ThreadLocal();
        sColorStateCaches = new WeakHashMap(0);
        sColorStateCacheLock = new Object();
    }

    private ResourcesCompat() {
    }

    private static void addColorStateListToCache(ColorStateListCacheKey r4, int r5, ColorStateList r6, Resources.Theme r7) {
        Object r0 = sColorStateCacheLock;
        monitor-enter(r0);
        SparseArray<ColorStateListCacheEntry> r1 = sColorStateCaches.get(r4);     // Catch: Throwable -> L10
        if (r1 != null) goto L7;
        r1 = new SparseArray();     // Catch: Throwable -> L10
        sColorStateCaches.put(r4, r1);     // Catch: Throwable -> L10
    L7:
        r1.append(r5, new ColorStateListCacheEntry(r6, r4.mResources.getConfiguration(), r7));     // Catch: Throwable -> L10
        monitor-exit(r0);     // Catch: Throwable -> L10
        return;
    L10:
        th = move-exception;
        throw th;
    }

    public static void clearCachesForTheme(Resources.Theme r4) {
        Object r0 = sColorStateCacheLock;
        monitor-enter(r0);
        Iterator<ColorStateListCacheKey> r1 = sColorStateCaches.keySet().iterator();     // Catch: Throwable -> L15
    L6:
        if (r1.hasNext() == false) goto L13;
        ColorStateListCacheKey r2 = r1.next();     // Catch: Throwable -> L15
        if (r2 == null) goto L6;
        if (r4.equals(r2.mTheme) == false) goto L6;
        r1.remove();     // Catch: Throwable -> L15
        goto L6
    L13:
        monitor-exit(r0);     // Catch: Throwable -> L15
        return;
    L15:
        th = move-exception;
        throw th;
    }

    private static ColorStateList getCachedColorStateList(ColorStateListCacheKey r5, int r6) {
        Object r0 = sColorStateCacheLock;
        monitor-enter(r0);
        SparseArray<ColorStateListCacheEntry> r1 = sColorStateCaches.get(r5);     // Catch: Throwable -> L27
        if (r1 != null) goto L7;
    L24:
        monitor-exit(r0);     // Catch: Throwable -> L27
        return null;
    L7:
        if (r1.size() <= 0) goto L24;
        ColorStateListCacheEntry r2 = r1.get(r6);     // Catch: Throwable -> L27
        if (r2 == null) goto L24;
        if (r2.mConfiguration.equals(r5.mResources.getConfiguration()) == true) goto L13;
    L23:
        r1.remove(r6);     // Catch: Throwable -> L27
        goto L24
    L13:
        if (r5.mTheme != null) goto L17;
        if (r2.mThemeHash != 0) goto L17;
    L20:
        ColorStateList r3 = r2.mValue;     // Catch: Throwable -> L27
        monitor-exit(r0);     // Catch: Throwable -> L27
        return r3;
    L17:
        if (r5.mTheme == null) goto L23;
        if (r2.mThemeHash != r5.mTheme.hashCode()) goto L23;
    L27:
        th = move-exception;
        throw th;
    }

    public static Typeface getCachedFont(Context r9, int r10) throws Resources.NotFoundException {
        if (r9.isRestricted() == false) goto L7;
        return null;
    L7:
        return loadFont(r9, r10, new TypedValue(), 0, null, null, false, true);
    }

    public static int getColor(Resources r1, int r2, Resources.Theme r3) throws Resources.NotFoundException {
        return Api23Impl.getColor(r1, r2, r3);
    }

    public static ColorStateList getColorStateList(Resources r3, int r4, Resources.Theme r5) throws Resources.NotFoundException {
        ColorStateListCacheKey r0 = new ColorStateListCacheKey(r3, r5);
        ColorStateList r1 = getCachedColorStateList(r0, r4);
        if (r1 == null) goto L5;
        return r1;
    L5:
        ColorStateList r2 = inflateColorStateList(r3, r4, r5);
        if (r2 == null) goto L10;
        addColorStateListToCache(r0, r4, r2, r5);
        return r2;
    L10:
        return Api23Impl.getColorStateList(r3, r4, r5);
    }

    public static Drawable getDrawable(Resources r1, int r2, Resources.Theme r3) throws Resources.NotFoundException {
        return Api21Impl.getDrawable(r1, r2, r3);
    }

    public static Drawable getDrawableForDensity(Resources r1, int r2, int r3, Resources.Theme r4) throws Resources.NotFoundException {
        return Api21Impl.getDrawableForDensity(r1, r2, r3, r4);
    }

    public static float getFloat(Resources r4, int r5) {
        if (Build.VERSION.SDK_INT >= 29) goto L5;
        TypedValue r0 = getTypedValue();
        r4.getValue(r5, r0, true);
        if (r0.type != 4) goto L11;
        return r0.getFloat();
    L11:
        throw new Resources.NotFoundException("Resource ID #0x" + Integer.toHexString(r5) + " type #0x" + Integer.toHexString(r0.type) + " is not valid");
    L5:
        return Api29Impl.getFloat(r4, r5);
    }

    public static Typeface getFont(Context r9, int r10) throws Resources.NotFoundException {
        if (r9.isRestricted() == false) goto L7;
        return null;
    L7:
        return loadFont(r9, r10, new TypedValue(), 0, null, null, false, false);
    }

    public static Typeface getFont(Context r9, int r10, TypedValue r11, int r12, FontCallback r13) throws Resources.NotFoundException {
        if (r9.isRestricted() == false) goto L7;
        return null;
    L7:
        return loadFont(r9, r10, r11, r12, r13, null, true, false);
    }

    public static void getFont(Context r9, int r10, FontCallback r11, Handler r12) throws Resources.NotFoundException {
        Preconditions.checkNotNull(r11);
        if (r9.isRestricted() == false) goto L6;
        r11.callbackFailAsync(-4, r12);
        return;
    L6:
        loadFont(r9, r10, new TypedValue(), 0, r11, r12, false, false);
    }

    private static TypedValue getTypedValue() {
        TypedValue r0 = sTempTypedValue.get();
        if (r0 != null) goto L6;
        TypedValue r1 = new TypedValue();
        sTempTypedValue.set(r1);
        return r1;
    L6:
        return r0;
    }

    private static ColorStateList inflateColorStateList(Resources r5, int r6, Resources.Theme r7) {
        if (isColorInt(r5, r6) == false) goto L11;
        return null;
    L11:
        return ColorStateListInflaterCompat.createFromXml(r5, r5.getXml(r6), r7);
    L8:
        e = move-exception;
        Log.w(TAG, "Failed to inflate ColorStateList, leaving it to the framework", e);
        return null;
    }

    private static boolean isColorInt(Resources r4, int r5) {
        TypedValue r0 = getTypedValue();
        r4.getValue(r5, r0, true);
        if (r0.type >= 28) goto L5;
    L7:
        return false;
    L5:
        if (r0.type > 31) goto L7;
        return true;
    }

    private static Typeface loadFont(Context r10, int r11, TypedValue r12, int r13, FontCallback r14, Handler r15, boolean r16, boolean r17) {
        Resources r1 = r10.getResources();
        r1.getValue(r11, r12, true);
        Typeface r9 = loadFont(r10, r1, r12, r11, r13, r14, r15, r16, r17);
        if (r9 != null) goto L9;
        if (r14 != null) goto L9;
        if (r17 == true) goto L9;
        throw new Resources.NotFoundException("Font resource ID #0x" + Integer.toHexString(r11) + " could not be retrieved.");
    L9:
        return r9;
    }

    private static Typeface loadFont(Context r16, Resources r17, TypedValue r18, int r19, int r20, FontCallback r21, Handler r22, boolean r23, boolean r24) {
        if (r18.string == null) goto L53;
        String r4 = r18.string.toString();
        if (r4.startsWith("res/") == true) goto L9;
        if (r21 == null) goto L8;
        r21.callbackFailAsync(-3, r22);
    L8:
        return null;
    L9:
        Typeface r15 = TypefaceCompat.findFromCache(r17, r19, r4, r18.assetCookie, r20);
        if (r15 == null) goto L14;
        if (r21 == null) goto L13;
        r21.callbackSuccessAsync(r15, r22);
    L13:
        return r15;
    L14:
        if (r24 == false) goto L57;
        return null;
    L57:
    L45:
        e = e;
    L46:
        Log.e(TAG, "Failed to read xml resource " + r4, e);
    L49:
        if (r21 == null) goto L51;
        r21.callbackFailAsync(-3, r22);
    L51:
        return null;
    L47:
        e = e;
    L48:
        Log.e(TAG, "Failed to parse xml resource " + r4, e);
        goto L49
    L54:
        if (r4.toLowerCase().endsWith(".xml") == false) goto L35;
        FontResourcesParserCompat.FamilyResourceEntry r1 = FontResourcesParserCompat.parse(r17.getXml(r19), r17);     // Catch: IOException -> L41 XmlPullParserException -> L43
        if (r1 != null) goto L55;
        Log.e(TAG, "Failed to find font-family tag");     // Catch: IOException -> L45 XmlPullParserException -> L47
        if (r21 == null) goto L23;
        r21.callbackFailAsync(-3, r22);     // Catch: IOException -> L45 XmlPullParserException -> L47
    L23:
        return null;
    L55:
    L28:
        return TypefaceCompat.createFromResourcesFamilyXml(r16, r1, r17, r19, r4, r18.assetCookie, r20, r21, r22, r23);
    L29:
        e = e;
        r4 = r4;
    L31:
        e = e;
        r4 = r4;
        goto L48
    L35:
        Typeface r0 = TypefaceCompat.createFromResourcesFontFile(r16, r17, r19, r4, r18.assetCookie, r20);     // Catch: IOException -> L45 XmlPullParserException -> L47
        if (r21 == null) goto L40;
        if (r0 == null) goto L39;
        r21.callbackSuccessAsync(r0, r22);     // Catch: IOException -> L45 XmlPullParserException -> L47
        goto L40
    L39:
        r21.callbackFailAsync(-3, r22);     // Catch: IOException -> L45 XmlPullParserException -> L47
    L40:
        return r0;
    L41:
        e = e;
    L43:
        e = e;
        goto L48
    L53:
        throw new Resources.NotFoundException("Resource \"" + r17.getResourceName(r19) + "\" (" + Integer.toHexString(r19) + ") is not a Font: " + r18);
    }
}
