package androidx.core.graphics;

import android.graphics.Path;
import androidx.media3.extractor.text.ttml.TtmlNode;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Path.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u001c\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004*\u00020\u00012\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u000b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f¨\u0006\f"}, m421d2 = {"and", "Landroid/graphics/Path;", TtmlNode.TAG_P, "flatten", "", "Landroidx/core/graphics/PathSegment;", "error", "", "minus", "or", "plus", "xor", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class PathKt {
    public static final Path and(Path r5, Path r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, TtmlNode.TAG_P);
        Path r1 = new Path();
        r1.op(r5, r6, Path.Op.INTERSECT);
        return r1;
    }

    public static final Iterable<PathSegment> flatten(Path r2, float r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Collection<PathSegment> r0 = PathUtils.flatten(r2, r3);
        Intrinsics.checkNotNullExpressionValue(r0, "flatten(this, error)");
        return r0;
    }

    public static /* synthetic */ Iterable flatten$default(Path r0, float r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = 0.5f;
    L6:
        return flatten(r0, r1);
    }

    public static final Path minus(Path r5, Path r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, TtmlNode.TAG_P);
        Path r1 = new Path(r5);
        r1.op(r6, Path.Op.DIFFERENCE);
        return r1;
    }

    /* JADX INFO: renamed from: or */
    public static final Path m11or(Path r7, Path r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, TtmlNode.TAG_P);
        Path r3 = new Path(r7);
        r3.op(r8, Path.Op.UNION);
        return r3;
    }

    public static final Path plus(Path r5, Path r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, TtmlNode.TAG_P);
        Path r1 = new Path(r5);
        r1.op(r6, Path.Op.UNION);
        return r1;
    }

    public static final Path xor(Path r5, Path r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, TtmlNode.TAG_P);
        Path r1 = new Path(r5);
        r1.op(r6, Path.Op.XOR);
        return r1;
    }
}
