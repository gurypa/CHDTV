package androidx.core.graphics;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import androidx.media3.extractor.text.ttml.TtmlNode;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Rect.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000<\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0000\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f\u001a\r\u0010\u0004\u001a\u00020\u0005*\u00020\u0001H\u0086\n\u001a\r\u0010\u0004\u001a\u00020\u0006*\u00020\u0003H\u0086\n\u001a\r\u0010\u0007\u001a\u00020\u0005*\u00020\u0001H\u0086\n\u001a\r\u0010\u0007\u001a\u00020\u0006*\u00020\u0003H\u0086\n\u001a\r\u0010\b\u001a\u00020\u0005*\u00020\u0001H\u0086\n\u001a\r\u0010\b\u001a\u00020\u0006*\u00020\u0003H\u0086\n\u001a\r\u0010\t\u001a\u00020\u0005*\u00020\u0001H\u0086\n\u001a\r\u0010\t\u001a\u00020\u0006*\u00020\u0003H\u0086\n\u001a\u0015\u0010\n\u001a\u00020\u000b*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0086\n\u001a\u0015\u0010\n\u001a\u00020\u000b*\u00020\u00032\u0006\u0010\f\u001a\u00020\u000eH\u0086\n\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\rH\u0086\n\u001a\u0015\u0010\u000f\u001a\u00020\u0011*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0086\n\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0005H\u0086\n\u001a\u0015\u0010\u000f\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u000eH\u0086\n\u001a\u0015\u0010\u000f\u001a\u00020\u0011*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0086\n\u001a\u0015\u0010\u000f\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0006H\u0086\n\u001a\u0015\u0010\u0012\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0086\f\u001a\u0015\u0010\u0012\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0086\f\u001a\u0015\u0010\u0013\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\rH\u0086\n\u001a\u0015\u0010\u0013\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0086\n\u001a\u0015\u0010\u0013\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0005H\u0086\n\u001a\u0015\u0010\u0013\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u000eH\u0086\n\u001a\u0015\u0010\u0013\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0086\n\u001a\u0015\u0010\u0013\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0006H\u0086\n\u001a\u0015\u0010\u0014\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u0005H\u0086\n\u001a\u0015\u0010\u0014\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0006H\u0086\n\u001a\u0015\u0010\u0014\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0005H\u0086\n\u001a\r\u0010\u0016\u001a\u00020\u0001*\u00020\u0003H\u0086\b\u001a\r\u0010\u0017\u001a\u00020\u0003*\u00020\u0001H\u0086\b\u001a\r\u0010\u0018\u001a\u00020\u0011*\u00020\u0001H\u0086\b\u001a\r\u0010\u0018\u001a\u00020\u0011*\u00020\u0003H\u0086\b\u001a\u0015\u0010\u0019\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u001bH\u0086\b\u001a\u0015\u0010\u001c\u001a\u00020\u0011*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0086\f\u001a\u0015\u0010\u001c\u001a\u00020\u0011*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0086\f¨\u0006\u001d"}, m421d2 = {"and", "Landroid/graphics/Rect;", "r", "Landroid/graphics/RectF;", "component1", "", "", "component2", "component3", "component4", "contains", "", TtmlNode.TAG_P, "Landroid/graphics/Point;", "Landroid/graphics/PointF;", "minus", "xy", "Landroid/graphics/Region;", "or", "plus", "times", "factor", "toRect", "toRectF", "toRegion", "transform", "m", "Landroid/graphics/Matrix;", "xor", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class RectKt {
    public static final Rect and(Rect r4, Rect r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "r");
        Rect r1 = new Rect(r4);
        r1.intersect(r5);
        return r1;
    }

    public static final RectF and(RectF r4, RectF r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "r");
        RectF r1 = new RectF(r4);
        r1.intersect(r5);
        return r1;
    }

    public static final float component1(RectF r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.left;
    }

    public static final int component1(Rect r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.left;
    }

    public static final float component2(RectF r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.top;
    }

    public static final int component2(Rect r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.top;
    }

    public static final float component3(RectF r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.right;
    }

    public static final int component3(Rect r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.right;
    }

    public static final float component4(RectF r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.bottom;
    }

    public static final int component4(Rect r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.bottom;
    }

    public static final boolean contains(Rect r3, Point r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, TtmlNode.TAG_P);
        return r3.contains(r4.x, r4.y);
    }

    public static final boolean contains(RectF r3, PointF r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, TtmlNode.TAG_P);
        return r3.contains(r4.x, r4.y);
    }

    public static final Rect minus(Rect r6, int r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Rect r1 = new Rect(r6);
        r1.offset(-r7, -r7);
        return r1;
    }

    public static final Rect minus(Rect r6, Point r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "xy");
        Rect r1 = new Rect(r6);
        r1.offset(-r7.x, -r7.y);
        return r1;
    }

    public static final RectF minus(RectF r6, float r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        RectF r1 = new RectF(r6);
        r1.offset(-r7, -r7);
        return r1;
    }

    public static final RectF minus(RectF r6, PointF r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "xy");
        RectF r1 = new RectF(r6);
        r1.offset(-r7.x, -r7.y);
        return r1;
    }

    public static final Region minus(Rect r5, Rect r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "r");
        Region r1 = new Region(r5);
        r1.op(r6, Region.Op.DIFFERENCE);
        return r1;
    }

    public static final Region minus(RectF r7, RectF r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "r");
        Rect r4 = new Rect();
        r7.roundOut(r4);
        Region r1 = new Region(r4);
        Rect r6 = new Rect();
        r8.roundOut(r6);
        r1.op(r6, Region.Op.DIFFERENCE);
        return r1;
    }

    /* JADX INFO: renamed from: or */
    public static final Rect m12or(Rect r6, Rect r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "r");
        Rect r3 = new Rect(r6);
        r3.union(r7);
        return r3;
    }

    /* JADX INFO: renamed from: or */
    public static final RectF m13or(RectF r6, RectF r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "r");
        RectF r3 = new RectF(r6);
        r3.union(r7);
        return r3;
    }

    public static final Rect plus(Rect r4, int r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Rect r1 = new Rect(r4);
        r1.offset(r5, r5);
        return r1;
    }

    public static final Rect plus(Rect r6, Point r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "xy");
        Rect r1 = new Rect(r6);
        r1.offset(r7.x, r7.y);
        return r1;
    }

    public static final Rect plus(Rect r4, Rect r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "r");
        Rect r1 = new Rect(r4);
        r1.union(r5);
        return r1;
    }

    public static final RectF plus(RectF r4, float r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        RectF r1 = new RectF(r4);
        r1.offset(r5, r5);
        return r1;
    }

    public static final RectF plus(RectF r6, PointF r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "xy");
        RectF r1 = new RectF(r6);
        r1.offset(r7.x, r7.y);
        return r1;
    }

    public static final RectF plus(RectF r4, RectF r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "r");
        RectF r1 = new RectF(r4);
        r1.union(r5);
        return r1;
    }

    public static final Rect times(Rect r5, int r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Rect r1 = new Rect(r5);
        r1.top *= r6;
        r1.left *= r6;
        r1.right *= r6;
        r1.bottom *= r6;
        return r1;
    }

    public static final RectF times(RectF r5, float r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        RectF r1 = new RectF(r5);
        r1.top *= r6;
        r1.left *= r6;
        r1.right *= r6;
        r1.bottom *= r6;
        return r1;
    }

    public static final RectF times(RectF r8, int r9) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        float r1 = r9;
        RectF r4 = new RectF(r8);
        r4.top *= r1;
        r4.left *= r1;
        r4.right *= r1;
        r4.bottom *= r1;
        return r4;
    }

    public static final Rect toRect(RectF r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Rect r1 = new Rect();
        r2.roundOut(r1);
        return r1;
    }

    public static final RectF toRectF(Rect r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return new RectF(r2);
    }

    public static final Region toRegion(Rect r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return new Region(r2);
    }

    public static final Region toRegion(RectF r5) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Rect r4 = new Rect();
        r5.roundOut(r4);
        return new Region(r4);
    }

    public static final RectF transform(RectF r3, Matrix r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "m");
        r4.mapRect(r3);
        return r3;
    }

    public static final Region xor(Rect r5, Rect r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "r");
        Region r1 = new Region(r5);
        r1.op(r6, Region.Op.XOR);
        return r1;
    }

    public static final Region xor(RectF r7, RectF r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "r");
        Rect r4 = new Rect();
        r7.roundOut(r4);
        Region r1 = new Region(r4);
        Rect r6 = new Rect();
        r8.roundOut(r6);
        r1.op(r6, Region.Op.XOR);
        return r1;
    }
}
