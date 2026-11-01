package androidx.core.graphics.drawable;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes.dex */
public class IconCompat extends CustomVersionedParcelable {
    private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25f;
    private static final int AMBIENT_SHADOW_ALPHA = 30;
    private static final float BLUR_FACTOR = 0.010416667f;
    static final PorterDuff.Mode DEFAULT_TINT_MODE = null;
    private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
    static final String EXTRA_INT1 = "int1";
    static final String EXTRA_INT2 = "int2";
    static final String EXTRA_OBJ = "obj";
    static final String EXTRA_STRING1 = "string1";
    static final String EXTRA_TINT_LIST = "tint_list";
    static final String EXTRA_TINT_MODE = "tint_mode";
    static final String EXTRA_TYPE = "type";
    private static final float ICON_DIAMETER_FACTOR = 0.9166667f;
    private static final int KEY_SHADOW_ALPHA = 61;
    private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334f;
    private static final String TAG = "IconCompat";
    public static final int TYPE_ADAPTIVE_BITMAP = 5;
    public static final int TYPE_BITMAP = 1;
    public static final int TYPE_DATA = 3;
    public static final int TYPE_RESOURCE = 2;
    public static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_URI = 4;
    public static final int TYPE_URI_ADAPTIVE_BITMAP = 6;
    public byte[] mData;
    public int mInt1;
    public int mInt2;
    Object mObj1;
    public Parcelable mParcelable;
    public String mString1;
    public ColorStateList mTintList;
    PorterDuff.Mode mTintMode;
    public String mTintModeStr;
    public int mType;

    static class Api23Impl {
        private Api23Impl() {
        }

        static IconCompat createFromIcon(Context r4, Icon r5) {
            switch(getType(r5)) {
                case 2: goto L10;
                case 3: goto L4;
                case 4: goto L9;
                case 5: goto L4;
                case 6: goto L7;
                default: goto L4;
            };
        L4:
            IconCompat r0 = new IconCompat(-1);
            r0.mObj1 = r5;
            return r0;
        L10:
            String r1 = getResPackage(r5);
            return IconCompat.createWithResource(IconCompat.getResources(r4, r1), r1, getResId(r5));
        L15:
            throw new IllegalArgumentException("Icon resource cannot be found");
        L7:
            return IconCompat.createWithAdaptiveBitmapContentUri(getUri(r5));
        L9:
            return IconCompat.createWithContentUri(getUri(r5));
        }

        static IconCompat createFromIconInner(Object r3) {
            Preconditions.checkNotNull(r3);
            switch(getType(r3)) {
                case 2: goto L11;
                case 3: goto L4;
                case 4: goto L9;
                case 5: goto L4;
                case 6: goto L7;
                default: goto L4;
            };
        L4:
            IconCompat r0 = new IconCompat(-1);
            r0.mObj1 = r3;
            return r0;
        L7:
            return IconCompat.createWithAdaptiveBitmapContentUri(getUri(r3));
        L9:
            return IconCompat.createWithContentUri(getUri(r3));
        L11:
            return IconCompat.createWithResource(null, getResPackage(r3), getResId(r3));
        }

        static int getResId(Object r6) {
            if (Build.VERSION.SDK_INT < 28) goto L18;
            return Api28Impl.getResId(r6);
        L18:
            return ((Integer) r6.getClass().getMethod("getResId", new Class[0]).invoke(r6, new Object[0])).intValue();
        L15:
            e = move-exception;
            Log.e(IconCompat.TAG, "Unable to get icon resource", e);
            return 0;
        L9:
            e = move-exception;
            Log.e(IconCompat.TAG, "Unable to get icon resource", e);
            return 0;
        L12:
            e = move-exception;
            Log.e(IconCompat.TAG, "Unable to get icon resource", e);
            return 0;
        }

        static String getResPackage(Object r7) {
            if (Build.VERSION.SDK_INT < 28) goto L18;
            return Api28Impl.getResPackage(r7);
        L18:
            return (String) r7.getClass().getMethod("getResPackage", new Class[0]).invoke(r7, new Object[0]);
        L15:
            e = move-exception;
            Log.e(IconCompat.TAG, "Unable to get icon package", e);
            return null;
        L9:
            e = move-exception;
            Log.e(IconCompat.TAG, "Unable to get icon package", e);
            return null;
        L12:
            e = move-exception;
            Log.e(IconCompat.TAG, "Unable to get icon package", e);
            return null;
        }

        static int getType(Object r7) {
            if (Build.VERSION.SDK_INT < 28) goto L18;
            return Api28Impl.getType(r7);
        L18:
            return ((Integer) r7.getClass().getMethod("getType", new Class[0]).invoke(r7, new Object[0])).intValue();
        L15:
            e = move-exception;
            Log.e(IconCompat.TAG, "Unable to get icon type " + r7, e);
            return -1;
        L9:
            e = move-exception;
            Log.e(IconCompat.TAG, "Unable to get icon type " + r7, e);
            return -1;
        L12:
            e = move-exception;
            Log.e(IconCompat.TAG, "Unable to get icon type " + r7, e);
            return -1;
        }

        static Uri getUri(Object r7) {
            if (Build.VERSION.SDK_INT < 28) goto L18;
            return Api28Impl.getUri(r7);
        L18:
            return (Uri) r7.getClass().getMethod("getUri", new Class[0]).invoke(r7, new Object[0]);
        L15:
            e = move-exception;
            Log.e(IconCompat.TAG, "Unable to get icon uri", e);
            return null;
        L9:
            e = move-exception;
            Log.e(IconCompat.TAG, "Unable to get icon uri", e);
            return null;
        L12:
            e = move-exception;
            Log.e(IconCompat.TAG, "Unable to get icon uri", e);
            return null;
        }

        static Drawable loadDrawable(Icon r1, Context r2) {
            return r1.loadDrawable(r2);
        }

        static Icon toIcon(IconCompat r4, Context r5) {
            switch(r4.mType) {
                case -1: goto L36;
                case 0: goto L5;
                case 1: goto L27;
                case 2: goto L26;
                case 3: goto L25;
                case 4: goto L24;
                case 5: goto L21;
                case 6: goto L7;
                default: goto L5;
            };
        L24:
            Icon r0 = Icon.createWithContentUri((String) r4.mObj1);
        L29:
            if (r4.mTintList == null) goto L32;
            r0.setTintList(r4.mTintList);
        L32:
            if (r4.mTintMode == IconCompat.DEFAULT_TINT_MODE) goto L34;
            r0.setTintMode(r4.mTintMode);
        L34:
            return r0;
        L25:
            r0 = Icon.createWithData((byte[]) r4.mObj1, r4.mInt1, r4.mInt2);
            goto L29
        L26:
            r0 = Icon.createWithResource(r4.getResPackage(), r4.mInt1);
            goto L29
        L27:
            r0 = Icon.createWithBitmap((Bitmap) r4.mObj1);
            goto L29
        L5:
            throw new IllegalArgumentException("Unknown type");
        L7:
            if (Build.VERSION.SDK_INT < 30) goto L9;
            r0 = Api30Impl.createWithAdaptiveBitmapContentUri(r4.getUri());
            goto L29
        L9:
            if (r5 == null) goto L19;
            InputStream r1 = r4.getUriInputStream(r5);
            if (r1 == null) goto L17;
            if (Build.VERSION.SDK_INT < 26) goto L15;
            r0 = Api26Impl.createWithAdaptiveBitmap(BitmapFactory.decodeStream(r1));
            goto L29
        L15:
            r0 = Icon.createWithBitmap(IconCompat.createLegacyIconFromAdaptiveIcon(BitmapFactory.decodeStream(r1), false));
            goto L29
        L17:
            throw new IllegalStateException("Cannot load adaptive icon from uri: " + r4.getUri());
        L19:
            throw new IllegalArgumentException("Context is required to resolve the file uri of the icon: " + r4.getUri());
        L21:
            if (Build.VERSION.SDK_INT < 26) goto L23;
            r0 = Api26Impl.createWithAdaptiveBitmap((Bitmap) r4.mObj1);
            goto L29
        L23:
            r0 = Icon.createWithBitmap(IconCompat.createLegacyIconFromAdaptiveIcon((Bitmap) r4.mObj1, false));
            goto L29
        L36:
            return (Icon) r4.mObj1;
        }
    }

    static class Api26Impl {
        private Api26Impl() {
        }

        static Drawable createAdaptiveIconDrawable(Drawable r1, Drawable r2) {
            return new AdaptiveIconDrawable(r1, r2);
        }

        static Icon createWithAdaptiveBitmap(Bitmap r1) {
            return Icon.createWithAdaptiveBitmap(r1);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static int getResId(Object r1) {
            return ((Icon) r1).getResId();
        }

        static String getResPackage(Object r1) {
            return ((Icon) r1).getResPackage();
        }

        static int getType(Object r1) {
            return ((Icon) r1).getType();
        }

        static Uri getUri(Object r1) {
            return ((Icon) r1).getUri();
        }
    }

    static class Api30Impl {
        private Api30Impl() {
        }

        static Icon createWithAdaptiveBitmapContentUri(Uri r1) {
            return Icon.createWithAdaptiveBitmapContentUri(r1);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IconType {
    }

    static {
        DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
    }

    public IconCompat() {
        this.mType = -1;
        this.mData = null;
        this.mParcelable = null;
        this.mInt1 = 0;
        this.mInt2 = 0;
        this.mTintList = null;
        this.mTintMode = DEFAULT_TINT_MODE;
        this.mTintModeStr = null;
    }

    IconCompat(int r3) {
        this.mType = -1;
        this.mData = null;
        this.mParcelable = null;
        this.mInt1 = 0;
        this.mInt2 = 0;
        this.mTintList = null;
        this.mTintMode = DEFAULT_TINT_MODE;
        this.mTintModeStr = null;
        this.mType = r3;
    }

    public static IconCompat createFromBundle(Bundle r4) {
        int r0 = r4.getInt(EXTRA_TYPE);
        IconCompat r1 = new IconCompat(r0);
        r1.mInt1 = r4.getInt(EXTRA_INT1);
        r1.mInt2 = r4.getInt(EXTRA_INT2);
        r1.mString1 = r4.getString(EXTRA_STRING1);
        if (r4.containsKey(EXTRA_TINT_LIST) == false) goto L6;
        r1.mTintList = (ColorStateList) r4.getParcelable(EXTRA_TINT_LIST);
    L6:
        if (r4.containsKey(EXTRA_TINT_MODE) == false) goto L9;
        r1.mTintMode = PorterDuff.Mode.valueOf(r4.getString(EXTRA_TINT_MODE));
    L9:
        switch(r0) {
            case -1: goto L14;
            case 0: goto L10;
            case 1: goto L14;
            case 2: goto L13;
            case 3: goto L12;
            case 4: goto L13;
            case 5: goto L14;
            case 6: goto L13;
            default: goto L10;
        };
    L10:
        Log.w(TAG, "Unknown type " + r0);
        return null;
    L12:
        r1.mObj1 = r4.getByteArray(EXTRA_OBJ);
    L15:
        return r1;
    L13:
        r1.mObj1 = r4.getString(EXTRA_OBJ);
        goto L15
    L14:
        r1.mObj1 = r4.getParcelable(EXTRA_OBJ);
        goto L15
    }

    public static IconCompat createFromIcon(Context r1, Icon r2) {
        Preconditions.checkNotNull(r2);
        return Api23Impl.createFromIcon(r1, r2);
    }

    public static IconCompat createFromIcon(Icon r1) {
        return Api23Impl.createFromIconInner(r1);
    }

    public static IconCompat createFromIconOrNullIfZeroResId(Icon r2) {
        if (Api23Impl.getType(r2) != 2) goto L9;
        if (Api23Impl.getResId(r2) != 0) goto L9;
        return null;
    L9:
        return Api23Impl.createFromIconInner(r2);
    }

    static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap r11, boolean r12) {
        int r0 = (int) (Math.min(r11.getWidth(), r11.getHeight()) * DEFAULT_VIEW_PORT_SCALE);
        Bitmap r1 = Bitmap.createBitmap(r0, r0, Bitmap.Config.ARGB_8888);
        Canvas r2 = new Canvas(r1);
        Paint r3 = new Paint(3);
        float r4 = r0 * 0.5f;
        float r5 = ICON_DIAMETER_FACTOR * r4;
        if (r12 == false) goto L5;
        float r7 = r0 * BLUR_FACTOR;
        r3.setColor(0);
        r3.setShadowLayer(r7, 0.0f, r0 * KEY_SHADOW_OFFSET_FACTOR, 1023410176);
        r2.drawCircle(r4, r4, r5, r3);
        r3.setShadowLayer(r7, 0.0f, 0.0f, 503316480);
        r2.drawCircle(r4, r4, r5, r3);
        r3.clearShadowLayer();
    L5:
        r3.setColor(ViewCompat.MEASURED_STATE_MASK);
        BitmapShader r6 = new BitmapShader(r11, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix r8 = new Matrix();
        r8.setTranslate((-(r11.getWidth() - r0)) / 2.0f, (-(r11.getHeight() - r0)) / 2.0f);
        r6.setLocalMatrix(r8);
        r3.setShader(r6);
        r2.drawCircle(r4, r4, r5, r3);
        r2.setBitmap(null);
        return r1;
    }

    public static IconCompat createWithAdaptiveBitmap(Bitmap r2) {
        ObjectsCompat.requireNonNull(r2);
        IconCompat r0 = new IconCompat(5);
        r0.mObj1 = r2;
        return r0;
    }

    public static IconCompat createWithAdaptiveBitmapContentUri(Uri r1) {
        ObjectsCompat.requireNonNull(r1);
        return createWithAdaptiveBitmapContentUri(r1.toString());
    }

    public static IconCompat createWithAdaptiveBitmapContentUri(String r2) {
        ObjectsCompat.requireNonNull(r2);
        IconCompat r0 = new IconCompat(6);
        r0.mObj1 = r2;
        return r0;
    }

    public static IconCompat createWithBitmap(Bitmap r2) {
        ObjectsCompat.requireNonNull(r2);
        IconCompat r0 = new IconCompat(1);
        r0.mObj1 = r2;
        return r0;
    }

    public static IconCompat createWithContentUri(Uri r1) {
        ObjectsCompat.requireNonNull(r1);
        return createWithContentUri(r1.toString());
    }

    public static IconCompat createWithContentUri(String r2) {
        ObjectsCompat.requireNonNull(r2);
        IconCompat r0 = new IconCompat(4);
        r0.mObj1 = r2;
        return r0;
    }

    public static IconCompat createWithData(byte[] r2, int r3, int r4) {
        ObjectsCompat.requireNonNull(r2);
        IconCompat r0 = new IconCompat(3);
        r0.mObj1 = r2;
        r0.mInt1 = r3;
        r0.mInt2 = r4;
        return r0;
    }

    public static IconCompat createWithResource(Context r2, int r3) {
        ObjectsCompat.requireNonNull(r2);
        return createWithResource(r2.getResources(), r2.getPackageName(), r3);
    }

    public static IconCompat createWithResource(Resources r4, String r5, int r6) {
        ObjectsCompat.requireNonNull(r5);
        if (r6 == 0) goto L15;
        IconCompat r0 = new IconCompat(2);
        r0.mInt1 = r6;
        if (r4 != null) goto L16;
        r0.mObj1 = r5;
    L12:
        r0.mString1 = r5;
        return r0;
    L16:
        r0.mObj1 = r4.getResourceName(r6);     // Catch: Resources.NotFoundException -> L8
    L10:
        throw new IllegalArgumentException("Icon resource cannot be found");
    L15:
        throw new IllegalArgumentException("Drawable resource ID must not be 0");
    }

    static Resources getResources(Context r5, String r6) {
        if ("android".equals(r6) == true) goto L5;
        PackageManager r0 = r5.getPackageManager();
        ApplicationInfo r1 = r0.getApplicationInfo(r6, 8192);     // Catch: PackageManager.NameNotFoundException -> L12
        if (r1 == null) goto L11;
        return r0.getResourcesForApplication(r1);
    L11:
        return null;
    L12:
        e = move-exception;
        Log.e(TAG, String.format("Unable to find pkg=%s for icon", new Object[]{r6}), e);
        return null;
    L5:
        return Resources.getSystem();
    }

    private Drawable loadDrawableInner(Context r9) {
        switch(this.mType) {
            case 1: goto L30;
            case 2: goto L21;
            case 3: goto L20;
            case 4: goto L15;
            case 5: goto L14;
            case 6: goto L5;
            default: goto L31;
        };
    L5:
        InputStream r0 = getUriInputStream(r9);
        if (r0 == null) goto L31;
        if (Build.VERSION.SDK_INT < 26) goto L12;
        return Api26Impl.createAdaptiveIconDrawable(null, new BitmapDrawable(r9.getResources(), BitmapFactory.decodeStream(r0)));
    L12:
        return new BitmapDrawable(r9.getResources(), createLegacyIconFromAdaptiveIcon(BitmapFactory.decodeStream(r0), false));
    L15:
        InputStream r1 = getUriInputStream(r9);
        if (r1 == null) goto L31;
        return new BitmapDrawable(r9.getResources(), BitmapFactory.decodeStream(r1));
    L21:
        String r2 = getResPackage();
        if (TextUtils.isEmpty(r2) == false) goto L32;
        r2 = r9.getPackageName();
    L32:
        return ResourcesCompat.getDrawable(getResources(r9, r2), this.mInt1, r9.getTheme());
    L27:
        e = move-exception;
        Log.e(TAG, String.format("Unable to load resource 0x%08x from pkg=%s", new Object[]{Integer.valueOf(this.mInt1), this.mObj1}), e);
    L31:
        return null;
    L14:
        return new BitmapDrawable(r9.getResources(), createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, false));
    L20:
        return new BitmapDrawable(r9.getResources(), BitmapFactory.decodeByteArray((byte[]) this.mObj1, this.mInt1, this.mInt2));
    L30:
        return new BitmapDrawable(r9.getResources(), (Bitmap) this.mObj1);
    }

    private static String typeToString(int r1) {
        switch(r1) {
            case 1: goto L15;
            case 2: goto L13;
            case 3: goto L11;
            case 4: goto L9;
            case 5: goto L7;
            case 6: goto L5;
            default: goto L3;
        };
    L3:
        return "UNKNOWN";
    L5:
        return "URI_MASKABLE";
    L7:
        return "BITMAP_MASKABLE";
    L9:
        return "URI";
    L11:
        return "DATA";
    L13:
        return "RESOURCE";
    L15:
        return "BITMAP";
    }

    public void addToShortcutIntent(Intent r7, Drawable r8, Context r9) {
        checkResource(r9);
        switch(this.mType) {
            case 1: goto L23;
            case 2: goto L30;
            case 3: goto L5;
            case 4: goto L5;
            case 5: goto L6;
            default: goto L5;
        };
    L6:
        Bitmap r0 = createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, true);
    L26:
        if (r8 == null) goto L28;
        int r1 = r0.getWidth();
        int r2 = r0.getHeight();
        r8.setBounds(r1 / 2, r2 / 2, r1, r2);
        r8.draw(new Canvas(r0));
    L28:
        r7.putExtra("android.intent.extra.shortcut.ICON", r0);
        return;
    L23:
        r0 = (Bitmap) this.mObj1;
        if (r8 == null) goto L26;
        r0 = r0.copy(r0.getConfig(), true);
        goto L26
    L30:
        Context r3 = r9.createPackageContext(getResPackage(), 0);     // Catch: PackageManager.NameNotFoundException -> L20
        if (r8 != null) goto L11;
        r7.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(r3, this.mInt1));     // Catch: PackageManager.NameNotFoundException -> L20
        return;
    L11:
        Drawable r4 = ContextCompat.getDrawable(r3, this.mInt1);     // Catch: PackageManager.NameNotFoundException -> L20
        if (r4.getIntrinsicWidth() > 0) goto L14;
    L17:
        int r5 = ((ActivityManager) r3.getSystemService("activity")).getLauncherLargeIconSize();     // Catch: PackageManager.NameNotFoundException -> L20
        Bitmap r6 = Bitmap.createBitmap(r5, r5, Bitmap.Config.ARGB_8888);     // Catch: PackageManager.NameNotFoundException -> L20
    L18:
        r4.setBounds(0, 0, r6.getWidth(), r6.getHeight());     // Catch: PackageManager.NameNotFoundException -> L20
        r4.draw(new Canvas(r6));     // Catch: PackageManager.NameNotFoundException -> L20
        r0 = r6;
        goto L26
    L14:
        if (r4.getIntrinsicHeight() <= 0) goto L17;
        r6 = Bitmap.createBitmap(r4.getIntrinsicWidth(), r4.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);     // Catch: PackageManager.NameNotFoundException -> L20
    L20:
        e = move-exception;
        throw new IllegalArgumentException("Can't find package " + this.mObj1, e);
    L5:
        throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
    }

    public void checkResource(Context r11) {
        if (this.mType == 2) goto L5;
        return;
    L5:
        if (this.mObj1 == null) goto L18;
        String r0 = (String) this.mObj1;
        if (r0.contains(":") == true) goto L9;
        return;
    L9:
        String r3 = r0.split(":", -1)[1];
        String r6 = r3.split("/", -1)[0];
        String r4 = r3.split("/", -1)[1];
        String r1 = r0.split(":", -1)[0];
        if ("0_resource_name_obfuscated".equals(r4) == false) goto L13;
        Log.i(TAG, "Found obfuscated resource, not trying to update resource id for it");
        return;
    L13:
        String r2 = getResPackage();
        int r7 = getResources(r11, r2).getIdentifier(r4, r6, r1);
        if (this.mInt1 == r7) goto L19;
        Log.i(TAG, "Id has changed for " + r2 + " " + r0);
        this.mInt1 = r7;
        return;
    L19:
        return;
    }

    public Bitmap getBitmap() {
        if (this.mType != (-1)) goto L11;
        if ((this.mObj1 instanceof Bitmap) == true) goto L7;
        return null;
    L7:
        return (Bitmap) this.mObj1;
    L11:
        if (this.mType != 1) goto L15;
        return (Bitmap) this.mObj1;
    L15:
        if (this.mType != 5) goto L19;
        return createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, true);
    L19:
        throw new IllegalStateException("called getBitmap() on " + this);
    }

    public int getResId() {
        if (this.mType != (-1)) goto L7;
        return Api23Impl.getResId(this.mObj1);
    L7:
        if (this.mType != 2) goto L11;
        return this.mInt1;
    L11:
        throw new IllegalStateException("called getResId() on " + this);
    }

    public String getResPackage() {
        if (this.mType != (-1)) goto L7;
        return Api23Impl.getResPackage(this.mObj1);
    L7:
        if (this.mType != 2) goto L18;
        if (this.mString1 == null) goto L16;
        if (TextUtils.isEmpty(this.mString1) == true) goto L16;
        return this.mString1;
    L16:
        return ((String) this.mObj1).split(":", -1)[0];
    L18:
        throw new IllegalStateException("called getResPackage() on " + this);
    }

    public int getType() {
        if (this.mType != (-1)) goto L7;
        return Api23Impl.getType(this.mObj1);
    L7:
        return this.mType;
    }

    public Uri getUri() {
        if (this.mType != (-1)) goto L7;
        return Api23Impl.getUri(this.mObj1);
    L7:
        if (this.mType == 4) goto L14;
        if (this.mType == 6) goto L14;
        throw new IllegalStateException("called getUri() on " + this);
    L14:
        return Uri.parse((String) this.mObj1);
    }

    public InputStream getUriInputStream(Context r7) {
        Uri r0 = getUri();
        String r1 = r0.getScheme();
        if ("content".equals(r1) == false) goto L5;
    L17:
        return r7.getContentResolver().openInputStream(r0);
    L13:
        e = move-exception;
        Log.w(TAG, "Unable to load image from URI: " + r0, e);
        return null;
    L5:
        if ("file".equals(r1) == true) goto L17;
        return new FileInputStream(new File((String) this.mObj1));
    L9:
        e = move-exception;
        Log.w(TAG, "Unable to load image from path: " + r0, e);
        return null;
    }

    public Drawable loadDrawable(Context r2) {
        checkResource(r2);
        return Api23Impl.loadDrawable(toIcon(r2), r2);
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void onPostParceling() {
        this.mTintMode = PorterDuff.Mode.valueOf(this.mTintModeStr);
        switch(this.mType) {
            case -1: goto L16;
            case 0: goto L27;
            case 1: goto L12;
            case 2: goto L6;
            case 3: goto L5;
            case 4: goto L6;
            case 5: goto L12;
            case 6: goto L6;
            default: goto L27;
        };
    L5:
        this.mObj1 = this.mData;
        return;
    L6:
        this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
        if (this.mType == 2) goto L9;
        return;
    L9:
        if (this.mString1 != null) goto L22;
        this.mString1 = ((String) this.mObj1).split(":", -1)[0];
        return;
    L22:
        return;
    L27:
        return;
    L12:
        if (this.mParcelable == null) goto L14;
        this.mObj1 = this.mParcelable;
        return;
    L14:
        this.mObj1 = this.mData;
        this.mType = 3;
        this.mInt1 = 0;
        this.mInt2 = this.mData.length;
        return;
    L16:
        if (this.mParcelable == null) goto L19;
        this.mObj1 = this.mParcelable;
        return;
    L19:
        throw new IllegalArgumentException("Invalid icon");
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void onPreParceling(boolean r5) {
        this.mTintModeStr = this.mTintMode.name();
        switch(this.mType) {
            case -1: goto L11;
            case 0: goto L21;
            case 1: goto L8;
            case 2: goto L7;
            case 3: goto L6;
            case 4: goto L5;
            case 5: goto L8;
            case 6: goto L5;
            default: goto L21;
        };
    L5:
        this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
        return;
    L6:
        this.mData = (byte[]) this.mObj1;
        return;
    L7:
        this.mData = ((String) this.mObj1).getBytes(Charset.forName("UTF-16"));
        return;
    L8:
        if (r5 == false) goto L10;
        Bitmap r0 = (Bitmap) this.mObj1;
        ByteArrayOutputStream r1 = new ByteArrayOutputStream();
        r0.compress(Bitmap.CompressFormat.PNG, 90, r1);
        this.mData = r1.toByteArray();
        return;
    L10:
        this.mParcelable = (Parcelable) this.mObj1;
        return;
    L11:
        if (r5 == true) goto L14;
        this.mParcelable = (Parcelable) this.mObj1;
        return;
    L14:
        throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
    }

    public IconCompat setTint(int r2) {
        return setTintList(ColorStateList.valueOf(r2));
    }

    public IconCompat setTintList(ColorStateList r1) {
        this.mTintList = r1;
        return this;
    }

    public IconCompat setTintMode(PorterDuff.Mode r1) {
        this.mTintMode = r1;
        return this;
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        switch(this.mType) {
            case -1: goto L9;
            case 0: goto L5;
            case 1: goto L8;
            case 2: goto L7;
            case 3: goto L6;
            case 4: goto L7;
            case 5: goto L8;
            case 6: goto L7;
            default: goto L5;
        };
    L6:
        r0.putByteArray(EXTRA_OBJ, (byte[]) this.mObj1);
    L10:
        r0.putInt(EXTRA_TYPE, this.mType);
        r0.putInt(EXTRA_INT1, this.mInt1);
        r0.putInt(EXTRA_INT2, this.mInt2);
        r0.putString(EXTRA_STRING1, this.mString1);
        if (this.mTintList == null) goto L14;
        r0.putParcelable(EXTRA_TINT_LIST, this.mTintList);
    L14:
        if (this.mTintMode == DEFAULT_TINT_MODE) goto L16;
        r0.putString(EXTRA_TINT_MODE, this.mTintMode.name());
    L16:
        return r0;
    L7:
        r0.putString(EXTRA_OBJ, (String) this.mObj1);
        goto L10
    L8:
        r0.putParcelable(EXTRA_OBJ, (Bitmap) this.mObj1);
        goto L10
    L9:
        r0.putParcelable(EXTRA_OBJ, (Parcelable) this.mObj1);
        goto L10
    L5:
        throw new IllegalArgumentException("Invalid icon");
    }

    @Deprecated
    public Icon toIcon() {
        return toIcon(null);
    }

    public Icon toIcon(Context r2) {
        return Api23Impl.toIcon(this, r2);
    }

    public String toString() {
        if (this.mType == (-1)) goto L5;
        StringBuilder r0 = new StringBuilder("Icon(typ=").append(typeToString(this.mType));
        switch(this.mType) {
            case 1: goto L14;
            case 2: goto L13;
            case 3: goto L10;
            case 4: goto L9;
            case 5: goto L14;
            case 6: goto L9;
            default: goto L16;
        };
    L9:
        r0.append(" uri=").append(this.mObj1);
        goto L16
    L10:
        r0.append(" len=").append(this.mInt1);
        if (this.mInt2 == 0) goto L16;
        r0.append(" off=").append(this.mInt2);
        goto L16
    L13:
        r0.append(" pkg=").append(this.mString1).append(" id=").append(String.format("0x%08x", new Object[]{Integer.valueOf(getResId())}));
        goto L16
    L14:
        r0.append(" size=").append(((Bitmap) this.mObj1).getWidth()).append("x").append(((Bitmap) this.mObj1).getHeight());
    L16:
        if (this.mTintList == null) goto L19;
        r0.append(" tint=");
        r0.append(this.mTintList);
    L19:
        if (this.mTintMode == DEFAULT_TINT_MODE) goto L21;
        r0.append(" mode=").append(this.mTintMode);
    L21:
        r0.append(")");
        return r0.toString();
    L5:
        return String.valueOf(this.mObj1);
    }
}
