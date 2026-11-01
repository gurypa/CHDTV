package kotlin.ranges;

import androidx.media3.exoplayer.upstream.CmcdData;
import kotlin.Metadata;

/* JADX INFO: compiled from: Ranges.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002¢\u0006\u0002\u0010\u0005J\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0002H\u0096\u0002J\u0013\u0010\u000e\u001a\u00020\f2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\fH\u0016J\u0018\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u0002H\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u000e\u0010\u0006\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u00020\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\u0003\u001a\u00020\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\t¨\u0006\u0019"}, m421d2 = {"Lkotlin/ranges/OpenEndFloatRange;", "Lkotlin/ranges/OpenEndRange;", "", "start", "endExclusive", "(FF)V", "_endExclusive", "_start", "getEndExclusive", "()Ljava/lang/Float;", "getStart", "contains", "", "value", "equals", "other", "", "hashCode", "", "isEmpty", "lessThanOrEquals", CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, "b", "toString", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class OpenEndFloatRange implements OpenEndRange<Float> {
    private final float _endExclusive;
    private final float _start;

    public OpenEndFloatRange(float r1, float r2) {
        this._start = r1;
        this._endExclusive = r2;
    }

    private final boolean lessThanOrEquals(float r2, float r3) {
        if (r2 > r3) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean contains(float r2) {
        if (r2 >= this._start) goto L5;
    L7:
        return false;
    L5:
        if (r2 >= this._endExclusive) goto L7;
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ boolean contains(Comparable r2) {
        return contains(((Number) r2).floatValue());
    }

    public boolean equals(Object r5) {
        if ((r5 instanceof OpenEndFloatRange) == true) goto L5;
        return false;
    L5:
        if (isEmpty() == false) goto L9;
        if (((OpenEndFloatRange) r5).isEmpty() == false) goto L9;
    L20:
        return true;
    L9:
        if (this._start != ((OpenEndFloatRange) r5)._start) goto L11;
        boolean r0 = true;
    L12:
        if (r0 == true) goto L14;
        return false;
    L14:
        if (this._endExclusive != ((OpenEndFloatRange) r5)._endExclusive) goto L16;
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

    @Override // kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ Comparable getEndExclusive() {
        return getEndExclusive();
    }

    @Override // kotlin.ranges.OpenEndRange
    public Float getEndExclusive() {
        return Float.valueOf(this._endExclusive);
    }

    @Override // kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ Comparable getStart() {
        return getStart();
    }

    @Override // kotlin.ranges.OpenEndRange
    public Float getStart() {
        return Float.valueOf(this._start);
    }

    public int hashCode() {
        if (isEmpty() == false) goto L6;
        return -1;
    L6:
        return (Float.floatToIntBits(this._start) * 31) + Float.floatToIntBits(this._endExclusive);
    }

    @Override // kotlin.ranges.OpenEndRange
    public boolean isEmpty() {
        if (this._start < this._endExclusive) goto L5;
        return true;
    L5:
        return false;
    }

    public String toString() {
        return this._start + "..<" + this._endExclusive;
    }
}
