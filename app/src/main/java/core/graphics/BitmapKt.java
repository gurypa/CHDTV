package androidx.core.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorSpace;
import android.graphics.Point;
import android.graphics.PointF;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.arthenica.ffmpegkit.StreamInformation;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Bitmap.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000D\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u001a#\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0086\b\u001a7\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a)\u0010\u000b\u001a\u00020\u0001*\u00020\u00012\u0017\u0010\f\u001a\u0013\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f0\r¢\u0006\u0002\b\u0010H\u0086\bø\u0001\u0000\u001a\u0015\u0010\u0011\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u0013H\u0086\n\u001a\u0015\u0010\u0011\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u0014H\u0086\n\u001a\u001d\u0010\u0015\u001a\u00020\u0003*\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u0003H\u0086\n\u001a'\u0010\u0018\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0019\u001a\u00020\bH\u0086\b\u001a'\u0010\u001a\u001a\u00020\u000f*\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u00032\b\b\u0001\u0010\u001b\u001a\u00020\u0003H\u0086\n\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001c"}, m421d2 = {"createBitmap", "Landroid/graphics/Bitmap;", StreamInformation.KEY_WIDTH, "", StreamInformation.KEY_HEIGHT, "config", "Landroid/graphics/Bitmap$Config;", "hasAlpha", "", "colorSpace", "Landroid/graphics/ColorSpace;", "applyCanvas", "block", "Lkotlin/Function1;", "Landroid/graphics/Canvas;", "", "Lkotlin/ExtensionFunctionType;", "contains", TtmlNode.TAG_P, "Landroid/graphics/Point;", "Landroid/graphics/PointF;", "get", "x", "y", "scale", "filter", "set", TtmlNode.ATTR_TTS_COLOR, "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class BitmapKt {
    public static final Bitmap applyCanvas(Bitmap r2, Function1<? super Canvas, Unit> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "block");
        r3.invoke(new Canvas(r2));
        return r2;
    }

    public static final boolean contains(Bitmap r5, Point r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, TtmlNode.TAG_P);
        int r1 = r5.getWidth();
        int r2 = r6.x;
        if (r2 < 0) goto L6;
        if (r2 >= r1) goto L6;
        boolean r3 = true;
    L7:
        if (r3 == false) goto L14;
        if (r6.y < 0) goto L14;
        if (r6.y >= r5.getHeight()) goto L14;
        return true;
    L14:
        return false;
    L6:
        r3 = false;
        goto L7
    }

    public static final boolean contains(Bitmap r4, PointF r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, TtmlNode.TAG_P);
        if (r5.x >= 0.0f) goto L5;
    L11:
        return false;
    L5:
        if (r5.x >= r4.getWidth()) goto L11;
        if (r5.y < 0.0f) goto L11;
        if (r5.y >= r4.getHeight()) goto L11;
        return true;
    }

    public static final Bitmap createBitmap(int r3, int r4, Bitmap.Config r5) {
        Intrinsics.checkNotNullParameter(r5, "config");
        Bitmap r1 = Bitmap.createBitmap(r3, r4, r5);
        Intrinsics.checkNotNullExpressionValue(r1, "createBitmap(width, height, config)");
        return r1;
    }

    public static final Bitmap createBitmap(int r3, int r4, Bitmap.Config r5, boolean r6, ColorSpace r7) {
        Intrinsics.checkNotNullParameter(r5, "config");
        Intrinsics.checkNotNullParameter(r7, "colorSpace");
        Bitmap r1 = Bitmap.createBitmap(r3, r4, r5, r6, r7);
        Intrinsics.checkNotNullExpressionValue(r1, "createBitmap(width, heig…ig, hasAlpha, colorSpace)");
        return r1;
    }

    public static /* synthetic */ Bitmap createBitmap$default(int r1, int r2, Bitmap.Config r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L5;
        r3 = Bitmap.Config.ARGB_8888;
    L5:
        Intrinsics.checkNotNullParameter(r3, "config");
        Bitmap r6 = Bitmap.createBitmap(r1, r2, r3);
        Intrinsics.checkNotNullExpressionValue(r6, "createBitmap(width, height, config)");
        return r6;
    }

    public static /* synthetic */ Bitmap createBitmap$default(int r1, int r2, Bitmap.Config r3, boolean r4, ColorSpace r5, int r6, Object r7) {
        if ((r6 & 4) == 0) goto L6;
        r3 = Bitmap.Config.ARGB_8888;
    L6:
        if ((r6 & 8) == 0) goto L9;
        r4 = true;
    L9:
        if ((r6 & 16) == 0) goto L11;
        ColorSpace r8 = ColorSpace.get(ColorSpace.Named.SRGB);
        Intrinsics.checkNotNullExpressionValue(r8, "get(ColorSpace.Named.SRGB)");
        r5 = r8;
    L11:
        Intrinsics.checkNotNullParameter(r3, "config");
        Intrinsics.checkNotNullParameter(r5, "colorSpace");
        Bitmap r9 = Bitmap.createBitmap(r1, r2, r3, r4, r5);
        Intrinsics.checkNotNullExpressionValue(r9, "createBitmap(width, heig…ig, hasAlpha, colorSpace)");
        return r9;
    }

    public static final int get(Bitmap r2, int r3, int r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.getPixel(r3, r4);
    }

    public static final Bitmap scale(Bitmap r3, int r4, int r5, boolean r6) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Bitmap r1 = Bitmap.createScaledBitmap(r3, r4, r5, r6);
        Intrinsics.checkNotNullExpressionValue(r1, "createScaledBitmap(this, width, height, filter)");
        return r1;
    }

    public static /* synthetic */ Bitmap scale$default(Bitmap r1, int r2, int r3, boolean r4, int r5, Object r6) {
        if ((r5 & 4) == 0) goto L5;
        r4 = true;
    L5:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Bitmap r7 = Bitmap.createScaledBitmap(r1, r2, r3, r4);
        Intrinsics.checkNotNullExpressionValue(r7, "createScaledBitmap(this, width, height, filter)");
        return r7;
    }

    public static final void set(Bitmap r1, int r2, int r3, int r4) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.setPixel(r2, r3, r4);
    }
}
