package androidx.core.graphics;

import android.graphics.Point;
import android.graphics.PointF;
import androidx.media3.extractor.text.ttml.TtmlNode;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Point.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0002\b\t\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0086\n\u001a\r\u0010\u0000\u001a\u00020\u0003*\u00020\u0004H\u0086\n\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0002H\u0086\n\u001a\r\u0010\u0005\u001a\u00020\u0003*\u00020\u0004H\u0086\n\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0086\n\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\b\u001a\u00020\u0001H\u0086\n\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0086\n\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\b\u001a\u00020\u0003H\u0086\n\u001a\u0015\u0010\t\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0086\n\u001a\u0015\u0010\t\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\b\u001a\u00020\u0001H\u0086\n\u001a\u0015\u0010\t\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0086\n\u001a\u0015\u0010\t\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\b\u001a\u00020\u0003H\u0086\n\u001a\r\u0010\n\u001a\u00020\u0002*\u00020\u0004H\u0086\b\u001a\r\u0010\u000b\u001a\u00020\u0004*\u00020\u0002H\u0086\b\u001a\r\u0010\f\u001a\u00020\u0002*\u00020\u0002H\u0086\n\u001a\r\u0010\f\u001a\u00020\u0004*\u00020\u0004H\u0086\n¨\u0006\r"}, m421d2 = {"component1", "", "Landroid/graphics/Point;", "", "Landroid/graphics/PointF;", "component2", "minus", TtmlNode.TAG_P, "xy", "plus", "toPoint", "toPointF", "unaryMinus", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class PointKt {
    public static final float component1(PointF r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.x;
    }

    public static final int component1(Point r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.x;
    }

    public static final float component2(PointF r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.y;
    }

    public static final int component2(Point r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.y;
    }

    public static final Point minus(Point r6, int r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Point r1 = new Point(r6.x, r6.y);
        r1.offset(-r7, -r7);
        return r1;
    }

    public static final Point minus(Point r6, Point r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, TtmlNode.TAG_P);
        Point r1 = new Point(r6.x, r6.y);
        r1.offset(-r7.x, -r7.y);
        return r1;
    }

    public static final PointF minus(PointF r6, float r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        PointF r1 = new PointF(r6.x, r6.y);
        r1.offset(-r7, -r7);
        return r1;
    }

    public static final PointF minus(PointF r6, PointF r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, TtmlNode.TAG_P);
        PointF r1 = new PointF(r6.x, r6.y);
        r1.offset(-r7.x, -r7.y);
        return r1;
    }

    public static final Point plus(Point r4, int r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Point r1 = new Point(r4.x, r4.y);
        r1.offset(r5, r5);
        return r1;
    }

    public static final Point plus(Point r6, Point r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, TtmlNode.TAG_P);
        Point r1 = new Point(r6.x, r6.y);
        r1.offset(r7.x, r7.y);
        return r1;
    }

    public static final PointF plus(PointF r4, float r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        PointF r1 = new PointF(r4.x, r4.y);
        r1.offset(r5, r5);
        return r1;
    }

    public static final PointF plus(PointF r6, PointF r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, TtmlNode.TAG_P);
        PointF r1 = new PointF(r6.x, r6.y);
        r1.offset(r7.x, r7.y);
        return r1;
    }

    public static final Point toPoint(PointF r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        return new Point((int) r4.x, (int) r4.y);
    }

    public static final PointF toPointF(Point r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return new PointF(r2);
    }

    public static final Point unaryMinus(Point r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        return new Point(-r4.x, -r4.y);
    }

    public static final PointF unaryMinus(PointF r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        return new PointF(-r4.x, -r4.y);
    }
}
