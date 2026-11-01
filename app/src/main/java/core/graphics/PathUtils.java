package androidx.core.graphics;

import android.graphics.Path;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.Collection;

/* JADX INFO: loaded from: classes.dex */
public final class PathUtils {

    static class Api26Impl {
        private Api26Impl() {
        }

        static float[] approximate(Path r1, float r2) {
            return r1.approximate(r2);
        }
    }

    private PathUtils() {
    }

    public static Collection<PathSegment> flatten(Path r1) {
        return flatten(r1, 0.5f);
    }

    public static Collection<PathSegment> flatten(Path r15, float r16) {
        float[] r0 = Api26Impl.approximate(r15, r16);
        int r1 = r0.length / 3;
        ArrayList r2 = new ArrayList(r1);
        int r3 = 1;
    L3:
        if (r3 >= r1) goto L12;
        int r4 = r3 * 3;
        int r5 = (r3 - 1) * 3;
        float r6 = r0[r4];
        float r7 = r0[r4 + 1];
        float r8 = r0[r4 + 2];
        float r9 = r0[r5];
        float r10 = r0[r5 + 1];
        float r11 = r0[r5 + 2];
        if (r6 == r9) goto L11;
        if (r7 == r10) goto L9;
    L10:
        r2.add(new PathSegment(new PointF(r10, r11), r9, new PointF(r7, r8), r6));
        goto L11
    L9:
        if (r8 != r11) goto L10;
    L11:
        r3 = r3 + 1;
        goto L3
    L12:
        return r2;
    }
}
