package kotlin.ranges;

import androidx.media3.exoplayer.upstream.CmcdData;
import kotlin.Metadata;

/* JADX INFO: compiled from: Ranges.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002¢\u0006\u0002\u0010\u0005J\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0002H\u0096\u0002J\u0013\u0010\u000e\u001a\u00020\f2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\fH\u0016J\u0018\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u0002H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u000e\u0010\u0006\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u00020\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\u0003\u001a\u00020\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\t¨\u0006\u0019"}, m421d2 = {"Lkotlin/ranges/ClosedDoubleRange;", "Lkotlin/ranges/ClosedFloatingPointRange;", "", "start", "endInclusive", "(DD)V", "_endInclusive", "_start", "getEndInclusive", "()Ljava/lang/Double;", "getStart", "contains", "", "value", "equals", "other", "", "hashCode", "", "isEmpty", "lessThanOrEquals", CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, "b", "toString", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class ClosedDoubleRange implements ClosedFloatingPointRange<Double> {
    private final double _endInclusive;
    private final double _start;

    public ClosedDoubleRange(double r1, double r3) {
        this._start = r1;
        this._endInclusive = r3;
    }

    public boolean contains(double r3) {
        if (r3 >= this._start) goto L5;
    L7:
        return false;
    L5:
        if (r3 > this._endInclusive) goto L7;
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.ranges.ClosedFloatingPointRange, kotlin.ranges.ClosedRange
    public /* bridge */ /* synthetic */ boolean contains(Comparable r3) {
        return contains(((Number) r3).doubleValue());
    }

    public boolean equals(Object r8) {
        if ((r8 instanceof ClosedDoubleRange) == true) goto L5;
        return false;
    L5:
        if (isEmpty() == false) goto L9;
        if (((ClosedDoubleRange) r8).isEmpty() == false) goto L9;
    L20:
        return true;
    L9:
        if (this._start != ((ClosedDoubleRange) r8)._start) goto L11;
        boolean r0 = true;
    L12:
        if (r0 == true) goto L14;
        return false;
    L14:
        if (this._endInclusive != ((ClosedDoubleRange) r8)._endInclusive) goto L16;
        boolean r1 = true;
    L17:
        if (r1 == true) goto L20;
        return false;
    L16:
        r1 = false;
        goto L17
    L11:
        r0 = false;
        goto L12
    }

    @Override // kotlin.ranges.ClosedRange
    public /* bridge */ /* synthetic */ Comparable getEndInclusive() {
        return getEndInclusive();
    }

    @Override // kotlin.ranges.ClosedRange
    public Double getEndInclusive() {
        return Double.valueOf(this._endInclusive);
    }

    @Override // kotlin.ranges.ClosedRange
    public /* bridge */ /* synthetic */ Comparable getStart() {
        return getStart();
    }

    @Override // kotlin.ranges.ClosedRange
    public Double getStart() {
        return Double.valueOf(this._start);
    }

    public int hashCode() {
        if (isEmpty() == false) goto L6;
        return -1;
    L6:
        return (ClosedDoubleRange$$ExternalSyntheticBackport0.m449m(this._start) * 31) + ClosedDoubleRange$$ExternalSyntheticBackport0.m449m(this._endInclusive);
    }

    @Override // kotlin.ranges.ClosedFloatingPointRange, kotlin.ranges.ClosedRange
    public boolean isEmpty() {
        if (this._start <= this._endInclusive) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean lessThanOrEquals(double r2, double r4) {
        if (r2 > r4) goto L5;
        return true;
    L5:
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.ranges.ClosedFloatingPointRange
    public /* bridge */ /* synthetic */ boolean lessThanOrEquals(Comparable r5, Comparable r6) {
        return lessThanOrEquals(((Number) r5).doubleValue(), ((Number) r6).doubleValue());
    }

    public String toString() {
        return this._start + ".." + this._endInclusive;
    }
}
