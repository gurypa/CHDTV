package androidx.core.graphics;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.media3.extractor.text.ttml.TtmlNode;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Canvas.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000>\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\u001a1\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\u0086\bø\u0001\u0000\u001a1\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\b\u001a\u00020\t2\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\u0086\bø\u0001\u0000\u001a1\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\b\u001a\u00020\n2\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\u0086\bø\u0001\u0000\u001aI\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\f2\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\u0086\bø\u0001\u0000\u001aI\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00102\u0006\u0010\r\u001a\u00020\u00102\u0006\u0010\u000e\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u00102\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\u0086\bø\u0001\u0000\u001a3\u0010\u0011\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u00132\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\u0086\bø\u0001\u0000\u001aG\u0010\u0014\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0015\u001a\u00020\f2\b\b\u0002\u0010\u0016\u001a\u00020\f2\b\b\u0002\u0010\u0017\u001a\u00020\f2\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\u0086\bø\u0001\u0000\u001a)\u0010\u0018\u001a\u00020\u0001*\u00020\u00022\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\u0086\bø\u0001\u0000\u001aQ\u0010\u0019\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u001a\u001a\u00020\f2\b\b\u0002\u0010\u001b\u001a\u00020\f2\b\b\u0002\u0010\u0016\u001a\u00020\f2\b\b\u0002\u0010\u0017\u001a\u00020\f2\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\u0086\bø\u0001\u0000\u001a=\u0010\u001c\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u001a\u001a\u00020\f2\b\b\u0002\u0010\u001b\u001a\u00020\f2\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\u0086\bø\u0001\u0000\u001a=\u0010\u001d\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u001a\u001a\u00020\f2\b\b\u0002\u0010\u001b\u001a\u00020\f2\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\u0086\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001e"}, m421d2 = {"withClip", "", "Landroid/graphics/Canvas;", "clipPath", "Landroid/graphics/Path;", "block", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "clipRect", "Landroid/graphics/Rect;", "Landroid/graphics/RectF;", TtmlNode.LEFT, "", "top", TtmlNode.RIGHT, "bottom", "", "withMatrix", "matrix", "Landroid/graphics/Matrix;", "withRotation", "degrees", "pivotX", "pivotY", "withSave", "withScale", "x", "y", "withSkew", "withTranslation", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class CanvasKt {
    public static final void withClip(Canvas r3, float r4, float r5, float r6, float r7, Function1<? super Canvas, Unit> r8) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r8, "block");
        int r1 = r3.save();
        r3.clipRect(r4, r5, r6, r7);
        r8.invoke(r3);     // Catch: Throwable -> L6
        r3.restoreToCount(r1);
        return;
    L6:
        th = move-exception;
        r3.restoreToCount(r1);
        throw th;
    }

    public static final void withClip(Canvas r3, int r4, int r5, int r6, int r7, Function1<? super Canvas, Unit> r8) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r8, "block");
        int r1 = r3.save();
        r3.clipRect(r4, r5, r6, r7);
        r8.invoke(r3);     // Catch: Throwable -> L6
        r3.restoreToCount(r1);
        return;
    L6:
        th = move-exception;
        r3.restoreToCount(r1);
        throw th;
    }

    public static final void withClip(Canvas r3, Path r4, Function1<? super Canvas, Unit> r5) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "clipPath");
        Intrinsics.checkNotNullParameter(r5, "block");
        int r1 = r3.save();
        r3.clipPath(r4);
        r5.invoke(r3);     // Catch: Throwable -> L6
        r3.restoreToCount(r1);
        return;
    L6:
        th = move-exception;
        r3.restoreToCount(r1);
        throw th;
    }

    public static final void withClip(Canvas r3, Rect r4, Function1<? super Canvas, Unit> r5) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "clipRect");
        Intrinsics.checkNotNullParameter(r5, "block");
        int r1 = r3.save();
        r3.clipRect(r4);
        r5.invoke(r3);     // Catch: Throwable -> L6
        r3.restoreToCount(r1);
        return;
    L6:
        th = move-exception;
        r3.restoreToCount(r1);
        throw th;
    }

    public static final void withClip(Canvas r3, RectF r4, Function1<? super Canvas, Unit> r5) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "clipRect");
        Intrinsics.checkNotNullParameter(r5, "block");
        int r1 = r3.save();
        r3.clipRect(r4);
        r5.invoke(r3);     // Catch: Throwable -> L6
        r3.restoreToCount(r1);
        return;
    L6:
        th = move-exception;
        r3.restoreToCount(r1);
        throw th;
    }

    public static final void withMatrix(Canvas r3, Matrix r4, Function1<? super Canvas, Unit> r5) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "matrix");
        Intrinsics.checkNotNullParameter(r5, "block");
        int r1 = r3.save();
        r3.concat(r4);
        r5.invoke(r3);     // Catch: Throwable -> L6
        r3.restoreToCount(r1);
        return;
    L6:
        th = move-exception;
        r3.restoreToCount(r1);
        throw th;
    }

    public static /* synthetic */ void withMatrix$default(Canvas r1, Matrix r2, Function1 r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L5;
        r2 = new Matrix();
    L5:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "matrix");
        Intrinsics.checkNotNullParameter(r3, "block");
        int r6 = r1.save();
        r1.concat(r2);
        r3.invoke(r1);     // Catch: Throwable -> L9
        r1.restoreToCount(r6);
        return;
    L9:
        th = move-exception;
        r1.restoreToCount(r6);
        throw th;
    }

    public static final void withRotation(Canvas r3, float r4, float r5, float r6, Function1<? super Canvas, Unit> r7) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r7, "block");
        int r1 = r3.save();
        r3.rotate(r4, r5, r6);
        r7.invoke(r3);     // Catch: Throwable -> L6
        r3.restoreToCount(r1);
        return;
    L6:
        th = move-exception;
        r3.restoreToCount(r1);
        throw th;
    }

    public static /* synthetic */ void withRotation$default(Canvas r1, float r2, float r3, float r4, Function1 r5, int r6, Object r7) {
        if ((r6 & 1) == 0) goto L6;
        r2 = 0.0f;
    L6:
        if ((r6 & 2) == 0) goto L9;
        r3 = 0.0f;
    L9:
        if ((r6 & 4) == 0) goto L11;
        r4 = 0.0f;
    L11:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r5, "block");
        int r8 = r1.save();
        r1.rotate(r2, r3, r4);
        r5.invoke(r1);     // Catch: Throwable -> L15
        r1.restoreToCount(r8);
        return;
    L15:
        th = move-exception;
        r1.restoreToCount(r8);
        throw th;
    }

    public static final void withSave(Canvas r3, Function1<? super Canvas, Unit> r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "block");
        int r1 = r3.save();
        r4.invoke(r3);     // Catch: Throwable -> L6
        r3.restoreToCount(r1);
        return;
    L6:
        th = move-exception;
        r3.restoreToCount(r1);
        throw th;
    }

    public static final void withScale(Canvas r3, float r4, float r5, float r6, float r7, Function1<? super Canvas, Unit> r8) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r8, "block");
        int r1 = r3.save();
        r3.scale(r4, r5, r6, r7);
        r8.invoke(r3);     // Catch: Throwable -> L6
        r3.restoreToCount(r1);
        return;
    L6:
        th = move-exception;
        r3.restoreToCount(r1);
        throw th;
    }

    public static /* synthetic */ void withScale$default(Canvas r1, float r2, float r3, float r4, float r5, Function1 r6, int r7, Object r8) {
        if ((r7 & 1) == 0) goto L6;
        r2 = 1.0f;
    L6:
        if ((r7 & 2) == 0) goto L9;
        r3 = 1.0f;
    L9:
        if ((r7 & 4) == 0) goto L12;
        r4 = 0.0f;
    L12:
        if ((r7 & 8) == 0) goto L14;
        r5 = 0.0f;
    L14:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r6, "block");
        int r9 = r1.save();
        r1.scale(r2, r3, r4, r5);
        r6.invoke(r1);     // Catch: Throwable -> L18
        r1.restoreToCount(r9);
        return;
    L18:
        th = move-exception;
        r1.restoreToCount(r9);
        throw th;
    }

    public static final void withSkew(Canvas r3, float r4, float r5, Function1<? super Canvas, Unit> r6) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r6, "block");
        int r1 = r3.save();
        r3.skew(r4, r5);
        r6.invoke(r3);     // Catch: Throwable -> L6
        r3.restoreToCount(r1);
        return;
    L6:
        th = move-exception;
        r3.restoreToCount(r1);
        throw th;
    }

    public static /* synthetic */ void withSkew$default(Canvas r1, float r2, float r3, Function1 r4, int r5, Object r6) {
        if ((r5 & 1) == 0) goto L6;
        r2 = 0.0f;
    L6:
        if ((r5 & 2) == 0) goto L8;
        r3 = 0.0f;
    L8:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r4, "block");
        int r7 = r1.save();
        r1.skew(r2, r3);
        r4.invoke(r1);     // Catch: Throwable -> L12
        r1.restoreToCount(r7);
        return;
    L12:
        th = move-exception;
        r1.restoreToCount(r7);
        throw th;
    }

    public static final void withTranslation(Canvas r3, float r4, float r5, Function1<? super Canvas, Unit> r6) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r6, "block");
        int r1 = r3.save();
        r3.translate(r4, r5);
        r6.invoke(r3);     // Catch: Throwable -> L6
        r3.restoreToCount(r1);
        return;
    L6:
        th = move-exception;
        r3.restoreToCount(r1);
        throw th;
    }

    public static /* synthetic */ void withTranslation$default(Canvas r1, float r2, float r3, Function1 r4, int r5, Object r6) {
        if ((r5 & 1) == 0) goto L6;
        r2 = 0.0f;
    L6:
        if ((r5 & 2) == 0) goto L8;
        r3 = 0.0f;
    L8:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r4, "block");
        int r7 = r1.save();
        r1.translate(r2, r3);
        r4.invoke(r1);     // Catch: Throwable -> L12
        r1.restoreToCount(r7);
        return;
    L12:
        th = move-exception;
        r1.restoreToCount(r7);
        throw th;
    }
}
