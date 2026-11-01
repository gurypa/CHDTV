package androidx.core.graphics;

import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;

/* JADX INFO: loaded from: classes.dex */
public final class BitmapCompat {

    static class Api17Impl {
        private Api17Impl() {
        }

        static boolean hasMipMap(Bitmap r1) {
            return r1.hasMipMap();
        }

        static void setHasMipMap(Bitmap r0, boolean r1) {
            r0.setHasMipMap(r1);
        }
    }

    static class Api19Impl {
        private Api19Impl() {
        }

        static int getAllocationByteCount(Bitmap r1) {
            return r1.getAllocationByteCount();
        }
    }

    static class Api27Impl {
        private Api27Impl() {
        }

        static Bitmap copyBitmapIfHardware(Bitmap r3) {
            if (r3.getConfig() != Bitmap.Config.HARDWARE) goto L9;
            Bitmap.Config r0 = Bitmap.Config.ARGB_8888;
            if (Build.VERSION.SDK_INT < 31) goto L8;
            r0 = Api31Impl.getHardwareBitmapConfig(r3);
        L8:
            return r3.copy(r0, true);
        L9:
            return r3;
        }

        static Bitmap createBitmapWithSourceColorspace(int r5, int r6, Bitmap r7, boolean r8) {
            Bitmap.Config r0 = r7.getConfig();
            ColorSpace r1 = r7.getColorSpace();
            ColorSpace r2 = ColorSpace.get(ColorSpace.Named.LINEAR_EXTENDED_SRGB);
            if (r8 == false) goto L8;
            if (r7.getColorSpace().equals(r2) == true) goto L8;
            r0 = Bitmap.Config.RGBA_F16;
            r1 = r2;
        L13:
            return Bitmap.createBitmap(r5, r6, r0, r7.hasAlpha(), r1);
        L8:
            if (r7.getConfig() != Bitmap.Config.HARDWARE) goto L13;
            r0 = Bitmap.Config.ARGB_8888;
            if (Build.VERSION.SDK_INT < 31) goto L13;
            r0 = Api31Impl.getHardwareBitmapConfig(r7);
            goto L13
        }

        static boolean isAlreadyF16AndLinear(Bitmap r3) {
            ColorSpace r0 = ColorSpace.get(ColorSpace.Named.LINEAR_EXTENDED_SRGB);
            if (r3.getConfig() == Bitmap.Config.RGBA_F16) goto L5;
        L7:
            return false;
        L5:
            if (r3.getColorSpace().equals(r0) == false) goto L7;
            return true;
        }
    }

    static class Api29Impl {
        private Api29Impl() {
        }

        static void setPaintBlendMode(Paint r1) {
            r1.setBlendMode(BlendMode.SRC);
        }
    }

    static class Api31Impl {
        private Api31Impl() {
        }

        static Bitmap.Config getHardwareBitmapConfig(Bitmap r2) {
            if (r2.getHardwareBuffer().getFormat() != 22) goto L7;
            return Bitmap.Config.RGBA_F16;
        L7:
            return Bitmap.Config.ARGB_8888;
        }
    }

    private BitmapCompat() {
    }

    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r3v7 */
    public static Bitmap createScaledBitmap(Bitmap r30, int r31, int r32, Rect r33, boolean r34) {
        int r1 = r31;
        int r2 = r32;
        if (r1 <= 0) goto L144;
        if (r2 <= 0) goto L144;
        if (r33 != null) goto L7;
    L19:
        Bitmap r4 = r30;
        if (Build.VERSION.SDK_INT < 27) goto L22;
        r4 = Api27Impl.copyBitmapIfHardware(r30);
    L22:
        if (r33 == null) goto L24;
        int r5 = r33.width();
    L25:
        if (r33 == null) goto L27;
        int r7 = r33.height();
    L28:
        float r8 = r1 / r5;
        float r9 = r2 / r7;
        if (r33 == null) goto L31;
        int r11 = r33.left;
    L32:
        if (r33 == null) goto L34;
        int r12 = r33.top;
    L36:
        if (r11 != 0) goto L48;
        if (r12 != 0) goto L48;
        if (r1 != r30.getWidth()) goto L48;
        if (r2 != r30.getHeight()) goto L48;
        if (r30.isMutable() == false) goto L47;
        if (r30 != r4) goto L47;
        return r30.copy(r30.getConfig(), true);
    L47:
        return r4;
    L48:
        Paint r14 = new Paint(1);
        r14.setFilterBitmap(true);
        if (Build.VERSION.SDK_INT < 29) goto L51;
        Api29Impl.setPaintBlendMode(r14);
    L52:
        if (r5 != r1) goto L56;
        if (r7 != r2) goto L56;
        Bitmap r6 = Bitmap.createBitmap(r1, r2, r4.getConfig());
        new Canvas(r6).drawBitmap(r4, -r11, -r12, r14);
        return r6;
    L56:
        double r17 = Math.log(2.0d);
        if (r8 <= 1.0f) goto L59;
        int r10 = (int) Math.ceil(Math.log(r8) / r17);
    L61:
        if (r9 <= 1065353216(0x3f800000, float:1.0)) goto L63;
        int r13 = (int) Math.ceil(Math.log(r9) / r17);
    L64:
        int r15 = r10;
        int r20 = r13;
        Bitmap r21 = null;
        int r22 = 0;
        if (r34 == true) goto L67;
        int r16 = r20;
    L80:
        Rect r3 = new Rect(r11, r12, r5, r7);
        Rect r18 = new Rect();
        Bitmap r19 = r21;
        int r110 = r10;
    L81:
        if (r110 != 0) goto L88;
        if (r13 != 0) goto L88;
        if (r19 == r30) goto L87;
        if (r19 == null) goto L87;
        r19.recycle();
    L87:
        return r4;
    L88:
        if (r110 >= 0) goto L90;
        r110 = r110 + 1;
    L92:
        if (r13 >= 0) goto L94;
        r13 = r13 + 1;
    L96:
        int r23 = r12;
        int r24 = r110;
        int r25 = r13;
        r18.set(0, 0, sizeAtStep(r5, r1, r110, r15), sizeAtStep(r7, r2, r13, r16));
        if (r24 != 0) goto L100;
        if (r25 != 0) goto L100;
        boolean r111 = true;
    L101:
        if (r19 != null) goto L103;
    L107:
        boolean r112 = false;
    L108:
        if (r19 == null) goto L123;
        if (r19 == r30) goto L123;
        if (r34 == false) goto L117;
        if (Build.VERSION.SDK_INT < 27) goto L117;
        if (Api27Impl.isAlreadyF16AndLinear(r19) == false) goto L123;
    L117:
        if (r111 == false) goto L121;
        if (r112 == false) goto L123;
        if (r22 != 0) goto L123;
    L121:
        int r29 = r5;
    L142:
        new Canvas(r19).drawBitmap(r4, r3, r18, r14);
        Bitmap r26 = r4;
        r4 = r19;
        r19 = r26;
        r3.set(r18);
        r1 = r31;
        r2 = r32;
        r12 = r23;
        r13 = r25;
        r5 = r29;
        r110 = r24;
    L123:
        if (r19 == r30) goto L126;
        if (r19 == null) goto L126;
        r19.recycle();
    L126:
        int r113 = r22;
        if (r24 <= 0) goto L129;
        int r114 = r113;
    L130:
        int r115 = sizeAtStep(r5, r1, r114, r15);
        if (r25 <= 0) goto L133;
        int r27 = r113;
    L134:
        int r28 = sizeAtStep(r7, r2, r27, r16);
        r29 = r5;
        if (Build.VERSION.SDK_INT < 27) goto L141;
        if (r34 == false) goto L139;
        if (r111 == true) goto L139;
        boolean r35 = true;
    L140:
        r19 = Api27Impl.createBitmapWithSourceColorspace(r115, r28, r30, r35);
    L139:
        r35 = false;
        goto L140
    L141:
        r19 = Bitmap.createBitmap(r115, r28, r4.getConfig());
        goto L142
    L133:
        r27 = r25;
        goto L134
    L129:
        r114 = r24;
        goto L130
    L103:
        if (r19.getWidth() != r1) goto L107;
        if (r19.getHeight() != r2) goto L107;
        r112 = true;
    L100:
        r111 = false;
        goto L101
    L94:
        if (r13 <= 0) goto L96;
        r13 = r13 - 1;
        goto L96
    L90:
        if (r110 <= 0) goto L92;
        r110 = r110 - 1;
        goto L92
    L67:
        if (Build.VERSION.SDK_INT >= 27) goto L69;
    L78:
        r16 = r20;
        goto L80
    L69:
        if (Api27Impl.isAlreadyF16AndLinear(r30) == true) goto L78;
        if (r10 <= 0) goto L72;
        ?? r36 = 1;
        int r116 = sizeAtStep(r5, r1, 1, r15);
    L73:
        int r210 = r116;
        if (r13 <= 0) goto L76;
        r16 = r20;
        int r211 = sizeAtStep(r7, r2, r36, r16);
    L77:
        Bitmap r117 = Api27Impl.createBitmapWithSourceColorspace(r210, r211, r30, r36);
        new Canvas(r117).drawBitmap(r4, -r11, -r12, r14);
        r12 = 0;
        r21 = r4;
        r4 = r117;
        r22 = 1;
        r11 = 0;
        goto L80
    L76:
        r16 = r20;
        r211 = r7;
        goto L77
    L72:
        r36 = 1;
        r116 = r5;
        goto L73
    L63:
        r13 = (int) Math.floor(Math.log(r9) / r17);
        goto L64
    L59:
        r10 = (int) Math.floor(Math.log(r8) / r17);
        goto L61
    L51:
        r14.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        goto L52
    L34:
        r12 = 0;
        goto L36
    L31:
        r11 = 0;
        goto L32
    L27:
        r7 = r30.getHeight();
        goto L28
    L24:
        r5 = r30.getWidth();
        goto L25
    L7:
        if (r33.isEmpty() == true) goto L18;
        if (r33.left < 0) goto L18;
        if (r33.right > r30.getWidth()) goto L18;
        if (r33.top < 0) goto L18;
        if (r33.bottom <= r30.getHeight()) goto L19;
    L18:
        throw new IllegalArgumentException("srcRect must be contained by srcBm!");
    L144:
        throw new IllegalArgumentException("dstW and dstH must be > 0!");
    }

    public static int getAllocationByteCount(Bitmap r1) {
        return Api19Impl.getAllocationByteCount(r1);
    }

    public static boolean hasMipMap(Bitmap r1) {
        return Api17Impl.hasMipMap(r1);
    }

    public static void setHasMipMap(Bitmap r0, boolean r1) {
        Api17Impl.setHasMipMap(r0, r1);
    }

    public static int sizeAtStep(int r2, int r3, int r4, int r5) {
        if (r4 != 0) goto L5;
        return r3;
    L5:
        if (r4 <= 0) goto L9;
        return (1 << (r5 - r4)) * r2;
    L9:
        return r3 << ((-r4) - 1);
    }
}
