package androidx.core.graphics;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.FontVariationAxis;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class TypefaceCompatApi26Impl extends TypefaceCompatApi21Impl {
    private static final String ABORT_CREATION_METHOD = "abortCreation";
    private static final String ADD_FONT_FROM_ASSET_MANAGER_METHOD = "addFontFromAssetManager";
    private static final String ADD_FONT_FROM_BUFFER_METHOD = "addFontFromBuffer";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String FREEZE_METHOD = "freeze";
    private static final int RESOLVE_BY_FONT_TABLE = -1;
    private static final String TAG = "TypefaceCompatApi26Impl";
    protected final Method mAbortCreation;
    protected final Method mAddFontFromAssetManager;
    protected final Method mAddFontFromBuffer;
    protected final Method mCreateFromFamiliesWithDefault;
    protected final Class<?> mFontFamily;
    protected final Constructor<?> mFontFamilyCtor;
    protected final Method mFreeze;

    public TypefaceCompatApi26Impl() {
        Class<?> r0 = obtainFontFamily();     // Catch: NoSuchMethodException -> L5 Throwable -> L7
        Constructor<?> r1 = obtainFontFamilyCtor(r0);     // Catch: NoSuchMethodException -> L5 Throwable -> L7
        Method r2 = obtainAddFontFromAssetManagerMethod(r0);     // Catch: NoSuchMethodException -> L5 Throwable -> L7
        Method r3 = obtainAddFontFromBufferMethod(r0);     // Catch: NoSuchMethodException -> L5 Throwable -> L7
        Method r4 = obtainFreezeMethod(r0);     // Catch: NoSuchMethodException -> L5 Throwable -> L7
        Method r5 = obtainAbortCreationMethod(r0);     // Catch: NoSuchMethodException -> L5 Throwable -> L7
        Method r6 = obtainCreateFromFamiliesWithDefaultMethod(r0);     // Catch: NoSuchMethodException -> L5 Throwable -> L7
    L9:
        this.mFontFamily = r0;
        this.mFontFamilyCtor = r1;
        this.mAddFontFromAssetManager = r2;
        this.mAddFontFromBuffer = r3;
        this.mFreeze = r4;
        this.mAbortCreation = r5;
        this.mCreateFromFamiliesWithDefault = r6;
        return;
    L7:
        e = move-exception;
        Log.e(TAG, "Unable to collect necessary methods for class " + e.getClass().getName(), e);
        r0 = null;
        r1 = null;
        r2 = null;
        r3 = null;
        r4 = null;
        r5 = null;
        r6 = null;
        goto L9
    }

    private void abortCreation(Object r3) {
        this.mAbortCreation.invoke(r3, new Object[0]);     // Catch: InvocationTargetException -> L4 IllegalAccessException -> L6
    L11:
        return;
    L8:
        return;
    }

    private boolean addFontFromAssetManager(Context r10, Object r11, String r12, int r13, int r14, int r15, FontVariationAxis[] r16) {
        return ((Boolean) this.mAddFontFromAssetManager.invoke(r11, new Object[]{r10.getAssets(), r12, 0, false, Integer.valueOf(r13), Integer.valueOf(r14), Integer.valueOf(r15), r16})).booleanValue();
    L8:
        return false;
    }

    private boolean addFontFromBuffer(Object r8, ByteBuffer r9, int r10, int r11, int r12) {
        return ((Boolean) this.mAddFontFromBuffer.invoke(r8, new Object[]{r9, Integer.valueOf(r10), null, Integer.valueOf(r11), Integer.valueOf(r12)})).booleanValue();
    L8:
        return false;
    }

    private boolean freeze(Object r4) {
        return ((Boolean) this.mFreeze.invoke(r4, new Object[0])).booleanValue();
    L8:
        return false;
    }

    private boolean isFontFamilyPrivateAPIAvailable() {
        if (this.mAddFontFromAssetManager != null) goto L6;
        Log.w(TAG, "Unable to collect necessary private methods. Fallback to legacy implementation.");
    L6:
        if (this.mAddFontFromAssetManager == null) goto L8;
        return true;
    L8:
        return false;
    }

    private Object newFamily() {
        return this.mFontFamilyCtor.newInstance(new Object[0]);
    L9:
        return null;
    }

    protected Typeface createFromFamiliesWithDefault(Object r9) {
        Object r1 = Array.newInstance(this.mFontFamily, 1);     // Catch: Throwable -> L5 IllegalAccessException -> L7
        Array.set(r1, 0, r9);     // Catch: Throwable -> L5 IllegalAccessException -> L7
        return (Typeface) this.mCreateFromFamiliesWithDefault.invoke(null, new Object[]{r1, -1, -1});
    L8:
        return null;
    }

    @Override // androidx.core.graphics.TypefaceCompatApi21Impl, androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromFontFamilyFilesResourceEntry(Context r14, FontResourcesParserCompat.FontFamilyFilesResourceEntry r15, Resources r16, int r17) {
        if (isFontFamilyPrivateAPIAvailable() == false) goto L5;
        Object r3 = newFamily();
        if (r3 != null) goto L9;
        return null;
    L9:
        FontResourcesParserCompat.FontFileResourceEntry[] r9 = r15.getEntries();
        int r10 = r9.length;
        int r11 = 0;
    L10:
        if (r11 >= r10) goto L17;
        FontResourcesParserCompat.FontFileResourceEntry r12 = r9[r11];
        String r4 = r12.getFileName();
        int r5 = r12.getTtcIndex();
        int r6 = r12.getWeight();
        boolean r7 = r12.isItalic();
        if (addFontFromAssetManager(r14, r3, r4, r5, r6, r7 ? 1 : 0, FontVariationAxis.fromFontVariationSettings(r12.getVariationSettings())) == false) goto L13;
        r11 = r11 + 1;
        goto L10
    L13:
        abortCreation(r3);
        return null;
    L17:
        if (freeze(r3) == true) goto L20;
        return null;
    L20:
        return createFromFamiliesWithDefault(r3);
    L5:
        return super.createFromFontFamilyFilesResourceEntry(r14, r15, r16, r17);
    }

    @Override // androidx.core.graphics.TypefaceCompatApi21Impl, androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromFontInfo(Context r17, CancellationSignal r18, FontsContractCompat.FontInfo[] r19, int r20) {
        if (r19.length >= 1) goto L6;
        return null;
    L6:
        if (isFontFamilyPrivateAPIAvailable() == true) goto L27;
        FontsContractCompat.FontInfo r2 = findBestInfo(r19, r20);
        ParcelFileDescriptor r0 = r17.getContentResolver().openFileDescriptor(r2.getUri(), "r", r18);     // Catch: IOException -> L25
        if (r0 != null) goto L52;
        if (r0 == null) goto L12;
        r0.close();     // Catch: IOException -> L25
    L12:
        return null;
    L52:
        Typeface r1 = new Typeface.Builder(r0.getFileDescriptor()).setWeight(r2.getWeight()).setItalic(r2.isItalic()).build();     // Catch: Throwable -> L17
        if (r0 == null) goto L16;
        r0.close();     // Catch: IOException -> L25
    L16:
        return r1;
    L17:
        th = move-exception;
        if (r0 == null) goto L61;
        r0.close();     // Catch: Throwable -> L22
        goto L26
    L62:
        throw th;     // Catch: IOException -> L25
    L22:
        th = move-exception;
        th.addSuppressed(th);     // Catch: IOException -> L25
        throw th;     // Catch: IOException -> L25
    L61:
        throw th;     // Catch: IOException -> L25
    L26:
        return null;
    L27:
        Map<Uri, ByteBuffer> r3 = TypefaceCompatUtil.readFontInfoIntoByteBuffer(r17, r19, r18);
        Object r4 = newFamily();
        if (r4 != null) goto L30;
        return null;
    L30:
        int r12 = r19.length;
        boolean r13 = false;
        int r14 = 0;
    L31:
        if (r14 >= r12) goto L41;
        FontsContractCompat.FontInfo r15 = r19[r14];
        ByteBuffer r5 = r3.get(r15.getUri());
        if (r5 == null) goto L40;
        if (addFontFromBuffer(r4, r5, r15.getTtcIndex(), r15.getWeight(), r15.isItalic() ? 1 : 0) == false) goto L37;
        r13 = true;
        goto L40
    L37:
        abortCreation(r4);
        return null;
    L40:
        r14 = r14 + 1;
        r13 = r13;
        goto L31
    L41:
        if (r13 == true) goto L45;
        abortCreation(r4);
        return null;
    L45:
        if (freeze(r4) == true) goto L47;
        return null;
    L47:
        Typeface r6 = createFromFamiliesWithDefault(r4);
        if (r6 != null) goto L51;
        return null;
    L51:
        return Typeface.create(r6, r20);
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromResourcesFontFile(Context r10, Resources r11, int r12, String r13, int r14) {
        if (isFontFamilyPrivateAPIAvailable() == false) goto L5;
        Object r3 = newFamily();
        if (r3 != null) goto L10;
        return null;
    L10:
        if (addFontFromAssetManager(r10, r3, r13, 0, -1, -1, null) == true) goto L14;
        abortCreation(r3);
        return null;
    L14:
        if (freeze(r3) == true) goto L17;
        return null;
    L17:
        return createFromFamiliesWithDefault(r3);
    L5:
        return super.createFromResourcesFontFile(r10, r11, r12, r13, r14);
    }

    @Override // androidx.core.graphics.TypefaceCompatApi21Impl, androidx.core.graphics.TypefaceCompatBaseImpl
    Typeface createWeightStyle(Context r3, Typeface r4, int r5, boolean r6) {
        Typeface r0 = null;
        r0 = WeightTypefaceApi26.createWeightStyle(r4, r5, r6);     // Catch: RuntimeException -> L5
    L6:
        if (r0 == null) goto L8;
        return r0;
    L8:
        return super.createWeightStyle(r3, r4, r5, r6);
    }

    protected Method obtainAbortCreationMethod(Class<?> r3) throws NoSuchMethodException {
        return r3.getMethod(ABORT_CREATION_METHOD, new Class[0]);
    }

    protected Method obtainAddFontFromAssetManagerMethod(Class<?> r5) throws NoSuchMethodException {
        Class<?> r1 = Integer.TYPE;
        return r5.getMethod(ADD_FONT_FROM_ASSET_MANAGER_METHOD, new Class[]{AssetManager.class, String.class, r1, Boolean.TYPE, r1, r1, r1, FontVariationAxis[].class});
    }

    protected Method obtainAddFontFromBufferMethod(Class<?> r5) throws NoSuchMethodException {
        Class<?> r1 = Integer.TYPE;
        return r5.getMethod(ADD_FONT_FROM_BUFFER_METHOD, new Class[]{ByteBuffer.class, r1, FontVariationAxis[].class, r1, r1});
    }

    protected Method obtainCreateFromFamiliesWithDefaultMethod(Class<?> r7) throws NoSuchMethodException {
        Class r3 = Integer.TYPE;
        Method r2 = Typeface.class.getDeclaredMethod(CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD, new Class[]{Array.newInstance(r7, 1).getClass(), r3, r3});
        r2.setAccessible(true);
        return r2;
    }

    protected Class<?> obtainFontFamily() throws ClassNotFoundException {
        return Class.forName(FONT_FAMILY_CLASS);
    }

    protected Constructor<?> obtainFontFamilyCtor(Class<?> r2) throws NoSuchMethodException {
        return r2.getConstructor(new Class[0]);
    }

    protected Method obtainFreezeMethod(Class<?> r3) throws NoSuchMethodException {
        return r3.getMethod(FREEZE_METHOD, new Class[0]);
    }
}
