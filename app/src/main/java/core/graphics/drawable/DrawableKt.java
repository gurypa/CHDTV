package androidx.core.graphics.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.arthenica.ffmpegkit.StreamInformation;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Drawable.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000$\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\u001a*\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0003\u0010\u0003\u001a\u00020\u00042\b\b\u0003\u0010\u0005\u001a\u00020\u00042\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u001a,\u0010\b\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\b\b\u0003\u0010\u0003\u001a\u00020\u00042\b\b\u0003\u0010\u0005\u001a\u00020\u00042\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u001a2\u0010\t\u001a\u00020\n*\u00020\u00022\b\b\u0003\u0010\u000b\u001a\u00020\u00042\b\b\u0003\u0010\f\u001a\u00020\u00042\b\b\u0003\u0010\r\u001a\u00020\u00042\b\b\u0003\u0010\u000e\u001a\u00020\u0004¨\u0006\u000f"}, m421d2 = {"toBitmap", "Landroid/graphics/Bitmap;", "Landroid/graphics/drawable/Drawable;", StreamInformation.KEY_WIDTH, "", StreamInformation.KEY_HEIGHT, "config", "Landroid/graphics/Bitmap$Config;", "toBitmapOrNull", "updateBounds", "", TtmlNode.LEFT, "top", TtmlNode.RIGHT, "bottom", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class DrawableKt {
    public static final Bitmap toBitmap(Drawable r7, int r8, int r9, Bitmap.Config r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        if ((r7 instanceof BitmapDrawable) == true) goto L5;
    L19:
        Rect r0 = r7.getBounds();
        Intrinsics.checkNotNullExpressionValue(r0, "bounds");
        int r2 = r0.left;
        int r3 = r0.top;
        int r4 = r0.right;
        int r1 = r0.bottom;
        if (r10 != null) goto L22;
        Bitmap.Config r5 = Bitmap.Config.ARGB_8888;
    L23:
        Bitmap r6 = Bitmap.createBitmap(r8, r9, r5);
        r7.setBounds(0, 0, r8, r9);
        r7.draw(new Canvas(r6));
        r7.setBounds(r2, r3, r4, r1);
        Intrinsics.checkNotNullExpressionValue(r6, "bitmap");
        return r6;
    L22:
        r5 = r10;
        goto L23
    L5:
        if (((BitmapDrawable) r7).getBitmap() == null) goto L18;
        if (r10 == null) goto L10;
        if (((BitmapDrawable) r7).getBitmap().getConfig() != r10) goto L19;
    L10:
        if (r8 == ((BitmapDrawable) r7).getBitmap().getWidth()) goto L12;
    L15:
        Bitmap r11 = Bitmap.createScaledBitmap(((BitmapDrawable) r7).getBitmap(), r8, r9, true);
        Intrinsics.checkNotNullExpressionValue(r11, "createScaledBitmap(bitmap, width, height, true)");
        return r11;
    L12:
        if (r9 != ((BitmapDrawable) r7).getBitmap().getHeight()) goto L15;
        Bitmap r12 = ((BitmapDrawable) r7).getBitmap();
        Intrinsics.checkNotNullExpressionValue(r12, "bitmap");
        return r12;
    L18:
        throw new IllegalArgumentException("bitmap is null");
    }

    public static /* synthetic */ Bitmap toBitmap$default(Drawable r0, int r1, int r2, Bitmap.Config r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = r0.getIntrinsicWidth();
    L6:
        if ((r4 & 2) == 0) goto L9;
        r2 = r0.getIntrinsicHeight();
    L9:
        if ((r4 & 4) == 0) goto L12;
        r3 = null;
    L12:
        return toBitmap(r0, r1, r2, r3);
    }

    public static final Bitmap toBitmapOrNull(Drawable r1, int r2, int r3, Bitmap.Config r4) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if ((r1 instanceof BitmapDrawable) == false) goto L9;
        if (((BitmapDrawable) r1).getBitmap() != null) goto L9;
        return null;
    L9:
        return toBitmap(r1, r2, r3, r4);
    }

    public static /* synthetic */ Bitmap toBitmapOrNull$default(Drawable r0, int r1, int r2, Bitmap.Config r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = r0.getIntrinsicWidth();
    L6:
        if ((r4 & 2) == 0) goto L9;
        r2 = r0.getIntrinsicHeight();
    L9:
        if ((r4 & 4) == 0) goto L12;
        r3 = null;
    L12:
        return toBitmapOrNull(r0, r1, r2, r3);
    }

    public static final void updateBounds(Drawable r1, int r2, int r3, int r4, int r5) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.setBounds(r2, r3, r4, r5);
    }

    public static /* synthetic */ void updateBounds$default(Drawable r0, int r1, int r2, int r3, int r4, int r5, Object r6) {
        if ((r5 & 1) == 0) goto L6;
        r1 = r0.getBounds().left;
    L6:
        if ((r5 & 2) == 0) goto L9;
        r2 = r0.getBounds().top;
    L9:
        if ((r5 & 4) == 0) goto L12;
        r3 = r0.getBounds().right;
    L12:
        if ((r5 & 8) == 0) goto L14;
        r4 = r0.getBounds().bottom;
    L14:
        updateBounds(r0, r1, r2, r3, r4);
    }
}
