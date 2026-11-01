package kotlin.ranges;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: Progressions.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0016\u0018\u0000 \u00172\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0017B\u001f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0002¢\u0006\u0002\u0010\u0006J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0002H\u0016J\b\u0010\u0012\u001a\u00020\u000eH\u0016J\t\u0010\u0013\u001a\u00020\u0014H\u0096\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0016R\u0011\u0010\u0007\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t¨\u0006\u0018"}, m421d2 = {"Lkotlin/ranges/IntProgression;", "", "", "start", "endInclusive", "step", "(III)V", "first", "getFirst", "()I", "last", "getLast", "getStep", "equals", "", "other", "", "hashCode", "isEmpty", "iterator", "Lkotlin/collections/IntIterator;", "toString", "", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public class IntProgression implements Iterable<Integer>, KMappedMarker {
    public static final Companion Companion = null;
    private final int first;
    private final int last;
    private final int step;

    /* JADX INFO: compiled from: Progressions.kt */
    @Metadata(m420d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0006¨\u0006\t"}, m421d2 = {"Lkotlin/ranges/IntProgression$Companion;", "", "()V", "fromClosedRange", "Lkotlin/ranges/IntProgression;", "rangeStart", "", "rangeEnd", "step", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        public final IntProgression fromClosedRange(int r2, int r3, int r4) {
            return new IntProgression(r2, r3, r4);
        }
    }

    static {
        Companion = new Companion(null);
    }

    public IntProgression(int r3, int r4, int r5) {
        if (r5 == 0) goto L11;
        if (r5 == Integer.MIN_VALUE) goto L9;
        this.first = r3;
        this.last = ProgressionUtilKt.getProgressionLastElement(r3, r4, r5);
        this.step = r5;
        return;
    L9:
        throw new IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.");
    L11:
        throw new IllegalArgumentException("Step must be non-zero.");
    }

    public boolean equals(Object r3) {
        if ((r3 instanceof IntProgression) == true) goto L5;
    L15:
        return false;
    L5:
        if (isEmpty() == false) goto L9;
        if (((IntProgression) r3).isEmpty() == false) goto L9;
    L14:
        return true;
    L9:
        if (this.first != ((IntProgression) r3).first) goto L15;
        if (this.last != ((IntProgression) r3).last) goto L15;
        if (this.step != ((IntProgression) r3).step) goto L15;
        goto L14
    }

    public final int getFirst() {
        return this.first;
    }

    public final int getLast() {
        return this.last;
    }

    public final int getStep() {
        return this.step;
    }

    public int hashCode() {
        if (isEmpty() == false) goto L6;
        return -1;
    L6:
        return (((this.first * 31) + this.last) * 31) + this.step;
    }

    public boolean isEmpty() {
        if (this.step <= 0) goto L8;
        if (this.first <= this.last) goto L11;
        return true;
    L11:
        return false;
    L8:
        if (this.first >= this.last) goto L11;
        return true;
    }

    @Override // java.lang.Iterable
    public /* bridge */ /* synthetic */ Iterator<Integer> iterator() {
        return iterator2();
    }

    @Override // java.lang.Iterable
    /* JADX INFO: renamed from: iterator, reason: avoid collision after fix types in other method */
    public Iterator<Integer> iterator2() {
        return new IntProgressionIterator(this.first, this.last, this.step);
    }

    public String toString() {
        if (this.step <= 0) goto L5;
        StringBuilder r0 = new StringBuilder().append(this.first).append("..").append(this.last).append(" step ");
        int r1 = this.step;
    L7:
        return r0.append(r1).toString();
    L5:
        r0 = new StringBuilder().append(this.first).append(" downTo ").append(this.last).append(" step ");
        r1 = -this.step;
        goto L7
    }
}
