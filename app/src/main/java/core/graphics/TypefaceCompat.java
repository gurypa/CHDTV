package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import androidx.collection.LruCache;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.provider.FontsContractCompat;
import androidx.core.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public class TypefaceCompat {
    private static final LruCache<String, Typeface> sTypefaceCache = null;
    private static final TypefaceCompatBaseImpl sTypefaceCompatImpl = null;

    public static class ResourcesCallbackAdapter extends FontsContractCompat.FontRequestCallback {
        private ResourcesCompat.FontCallback mFontCallback;

        public ResourcesCallbackAdapter(ResourcesCompat.FontCallback r1) {
            this.mFontCallback = r1;
        }

        @Override // androidx.core.provider.FontsContractCompat.FontRequestCallback
        public void onTypefaceRequestFailed(int r2) {
            if (this.mFontCallback == null) goto L6;
            this.mFontCallback.onFontRetrievalFailed(r2);
            return;
        }

        @Override // androidx.core.provider.FontsContractCompat.FontRequestCallback
        public void onTypefaceRetrieved(Typeface r2) {
            if (this.mFontCallback == null) goto L6;
            this.mFontCallback.onFontRetrieved(r2);
            return;
        }
    }

    static {
        if (Build.VERSION.SDK_INT < 29) goto L6;
        sTypefaceCompatImpl = new TypefaceCompatApi29Impl();
    L17:
        sTypefaceCache = new LruCache(16);
        return;
    L6:
        if (Build.VERSION.SDK_INT < 28) goto L9;
        sTypefaceCompatImpl = new TypefaceCompatApi28Impl();
        goto L17
    L9:
        if (Build.VERSION.SDK_INT < 26) goto L12;
        sTypefaceCompatImpl = new TypefaceCompatApi26Impl();
        goto L17
    L12:
        if (Build.VERSION.SDK_INT >= 24) goto L14;
    L16:
        sTypefaceCompatImpl = new TypefaceCompatApi21Impl();
        goto L17
    L14:
        if (TypefaceCompatApi24Impl.isUsable() == false) goto L16;
        sTypefaceCompatImpl = new TypefaceCompatApi24Impl();
        goto L17
    }

    private TypefaceCompat() {
    }

    public static void clearCache() {
        sTypefaceCache.evictAll();
    }

    public static Typeface create(Context r2, Typeface r3, int r4) {
        if (r2 == null) goto L6;
        return Typeface.create(r3, r4);
    L6:
        throw new IllegalArgumentException("Context cannot be null");
    }

    public static Typeface create(Context r3, Typeface r4, int r5, boolean r6) {
        if (r3 == null) goto L9;
        Preconditions.checkArgumentInRange(r5, 1, 1000, "weight");
        if (r4 != null) goto L7;
        r4 = Typeface.DEFAULT;
    L7:
        return sTypefaceCompatImpl.createWeightStyle(r3, r4, r5, r6);
    L9:
        throw new IllegalArgumentException("Context cannot be null");
    }

    public static Typeface createFromFontInfo(Context r1, CancellationSignal r2, FontsContractCompat.FontInfo[] r3, int r4) {
        return sTypefaceCompatImpl.createFromFontInfo(r1, r2, r3, r4);
    }

    @Deprecated
    public static Typeface createFromResourcesFamilyXml(Context r10, FontResourcesParserCompat.FamilyResourceEntry r11, Resources r12, int r13, int r14, ResourcesCompat.FontCallback r15, Handler r16, boolean r17) {
        return createFromResourcesFamilyXml(r10, r11, r12, r13, null, 0, r14, r15, r16, r17);
    }

    public static Typeface createFromResourcesFamilyXml(Context r12, FontResourcesParserCompat.FamilyResourceEntry r13, Resources r14, int r15, String r16, int r17, int r18, ResourcesCompat.FontCallback r19, Handler r20, boolean r21) {
        if ((r13 instanceof FontResourcesParserCompat.ProviderResourceEntry) == false) goto L23;
        FontResourcesParserCompat.ProviderResourceEntry r2 = (FontResourcesParserCompat.ProviderResourceEntry) r13;
        Typeface r3 = getSystemFontFamily(r2.getSystemFontFamilyName());
        if (r3 == null) goto L9;
        if (r19 == null) goto L8;
        r19.callbackSuccessAsync(r3, r20);
    L8:
        return r3;
    L9:
        boolean r4 = true;
        if (r21 == true) goto L12;
        if (r19 == null) goto L18;
        r4 = false;
    L18:
        boolean r8 = r4;
        if (r21 == false) goto L21;
        int r9 = r2.getTimeout();
    L22:
        Typeface r5 = FontsContractCompat.requestFont(r12, r2.getRequest(), r18, r8, r9, ResourcesCompat.FontCallback.getHandler(r20), new ResourcesCallbackAdapter(r19));
    L28:
        if (r5 == null) goto L30;
        sTypefaceCache.put(createResourceUid(r14, r15, r16, r17, r18), r5);
    L30:
        return r5;
    L21:
        r9 = -1;
        goto L22
    L12:
        if (r2.getFetchStrategy() == 0) goto L18;
        r4 = false;
        goto L18
    L23:
        r5 = sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(r12, (FontResourcesParserCompat.FontFamilyFilesResourceEntry) r13, r14, r18);
        if (r19 == null) goto L28;
        if (r5 == null) goto L27;
        r19.callbackSuccessAsync(r5, r20);
        goto L28
    L27:
        r19.callbackFailAsync(-3, r20);
        goto L28
    }

    @Deprecated
    public static Typeface createFromResourcesFontFile(Context r6, Resources r7, int r8, String r9, int r10) {
        return createFromResourcesFontFile(r6, r7, r8, r9, 0, r10);
    }

    public static Typeface createFromResourcesFontFile(Context r6, Resources r7, int r8, String r9, int r10, int r11) {
        Typeface r12 = sTypefaceCompatImpl.createFromResourcesFontFile(r6, r7, r8, r9, r11);
        if (r12 == null) goto L5;
        String r13 = createResourceUid(r7, r8, r9, r10, r11);
        sTypefaceCache.put(r13, r12);
    L5:
        return r12;
    }

    private static String createResourceUid(Resources r2, int r3, String r4, int r5, int r6) {
        return r2.getResourcePackageName(r3) + '-' + r4 + '-' + r5 + '-' + r3 + '-' + r6;
    }

    @Deprecated
    public static Typeface findFromCache(Resources r2, int r3, int r4) {
        return findFromCache(r2, r3, null, 0, r4);
    }

    public static Typeface findFromCache(Resources r2, int r3, String r4, int r5, int r6) {
        return sTypefaceCache.get(createResourceUid(r2, r3, r4, r5, r6));
    }

    private static Typeface getBestFontFromFamily(Context r3, Typeface r4, int r5) {
        FontResourcesParserCompat.FontFamilyFilesResourceEntry r0 = sTypefaceCompatImpl.getFontFamily(r4);
        if (r0 != null) goto L7;
        return null;
    L7:
        return sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(r3, r0, r3.getResources(), r5);
    }

    private static Typeface getSystemFontFamily(String r4) {
        if (r4 != null) goto L5;
    L13:
        return null;
    L5:
        if (r4.isEmpty() == true) goto L13;
        Typeface r2 = Typeface.create(r4, 0);
        Typeface r1 = Typeface.create(Typeface.DEFAULT, 0);
        if (r2 != null) goto L10;
        return null;
    L10:
        if (r2.equals(r1) == false) goto L12;
        return null;
    L12:
        return r2;
    }
}
