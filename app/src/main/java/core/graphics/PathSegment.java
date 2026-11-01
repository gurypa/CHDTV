package androidx.core.graphics;

import android.graphics.PointF;
import androidx.core.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public final class PathSegment {
    private final PointF mEnd;
    private final float mEndFraction;
    private final PointF mStart;
    private final float mStartFraction;

    public PathSegment(PointF r2, float r3, PointF r4, float r5) {
        this.mStart = (PointF) Preconditions.checkNotNull(r2, "start == null");
        this.mStartFraction = r3;
        this.mEnd = (PointF) Preconditions.checkNotNull(r4, "end == null");
        this.mEndFraction = r5;
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof PathSegment) == true) goto L8;
        return false;
    L8:
        PathSegment r1 = (PathSegment) r6;
        if (Float.compare(this.mStartFraction, r1.mStartFraction) != 0) goto L18;
        if (Float.compare(this.mEndFraction, r1.mEndFraction) != 0) goto L18;
        if (this.mStart.equals(r1.mStart) == false) goto L18;
        if (this.mEnd.equals(r1.mEnd) == false) goto L18;
        return true;
    L18:
        return false;
    }

    public PointF getEnd() {
        return this.mEnd;
    }

    public float getEndFraction() {
        return this.mEndFraction;
    }

    public PointF getStart() {
        return this.mStart;
    }

    public float getStartFraction() {
        return this.mStartFraction;
    }

    public int hashCode() {
        int r1 = this.mStart.hashCode() * 31;
        int r4 = 0;
        if (this.mStartFraction == 0.0f) goto L5;
        int r2 = Float.floatToIntBits(this.mStartFraction);
    L6:
        int r3 = (((r1 + r2) * 31) + this.mEnd.hashCode()) * 31;
        if (this.mEndFraction == 0.0f) goto L10;
        r4 = Float.floatToIntBits(this.mEndFraction);
    L10:
        return r3 + r4;
    L5:
        r2 = 0;
        goto L6
    }

    public String toString() {
        return "PathSegment{start=" + this.mStart + ", startFraction=" + this.mStartFraction + ", end=" + this.mEnd + ", endFraction=" + this.mEndFraction + '}';
    }
}
