package kotlin.ranges;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: Progressions.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0016\u0018\u0000 \u00182\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0018B\u001f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0002¢\u0006\u0002\u0010\u0006J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u000eH\u0016J\t\u0010\u0014\u001a\u00020\u0015H\u0096\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0016R\u0011\u0010\u0007\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t¨\u0006\u0019"}, m421d2 = {"Lkotlin/ranges/LongProgression;", "", "", "start", "endInclusive", "step", "(JJJ)V", "first", "getFirst", "()J", "last", "getLast", "getStep", "equals", "", "other", "", "hashCode", "", "isEmpty", "iterator", "Lkotlin/collections/LongIterator;", "toString", "", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public class LongProgression implements Iterable<Long>, KMappedMarker {
    public static final Companion Companion = null;
    private final long first;
    private final long last;
    private final long step;

    /* JADX INFO: compiled from: Progressions.kt */
    @Metadata(m420d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0006¨\u0006\t"}, m421d2 = {"Lkotlin/ranges/LongProgression$Companion;", "", "()V", "fromClosedRange", "Lkotlin/ranges/LongProgression;", "rangeStart", "", "rangeEnd", "step", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        public final LongProgression fromClosedRange(long r8, long r10, long r12) {
            return new LongProgression(r8, r10, r12);
        }
    }

    static {
        Companion = new Companion(null);
    }

    public LongProgression(long r3, long r5, long r7) {
        if (r7 == 0) goto L11;
        if (r7 == Long.MIN_VALUE) goto L9;
        this.first = r3;
        this.last = ProgressionUtilKt.getProgressionLastElement(r3, r5, r7);
        this.step = r7;
        return;
    L9:
        throw new IllegalArgumentException("Step must be greater than Long.MIN_VALUE to avoid overflow on negation.");
    L11:
        throw new IllegalArgumentException("Step must be non-zero.");
    }

    public boolean equals(Object r5) {
        if ((r5 instanceof LongProgression) == true) goto L5;
    L15:
        return false;
    L5:
        if (isEmpty() == false) goto L9;
        if (((LongProgression) r5).isEmpty() == false) goto L9;
    L14:
        return true;
    L9:
        if (this.first != ((LongProgression) r5).first) goto L15;
        if (this.last != ((LongProgression) r5).last) goto L15;
        if (this.step != ((LongProgression) r5).step) goto L15;
        goto L14
    }

    public final long getFirst() {
        return this.first;
    }

    public final long getLast() {
        return this.last;
    }

    public final long getStep() {
        return this.step;
    }

    public int hashCode() {
        if (isEmpty() == false) goto L5;
        return -1;
    L5:
        long r0 = 31;
        return (int) ((r0 * (((this.first ^ (this.first >>> 32)) * r0) + (this.last ^ (this.last >>> 32)))) + (this.step ^ (this.step >>> 32)));
    }

    public boolean isEmpty() {
        long r0 = this.step;
        long r3 = this.first;
        long r5 = this.last;
        if (r0 <= 0) goto L8;
        if (r3 <= r5) goto L11;
        return true;
    L11:
        return false;
    L8:
        if (r3 >= r5) goto L11;
        return true;
    }

    @Override // java.lang.Iterable
    public /* bridge */ /* synthetic */ Iterator<Long> iterator() {
        return iterator2();
    }

    @Override // java.lang.Iterable
    /* JADX INFO: renamed from: iterator, reason: avoid collision after fix types in other method */
    public Iterator<Long> iterator2() {
        return new LongProgressionIterator(this.first, this.last, this.step);
    }

    public String toString() {
        if (this.step <= 0) goto L5;
        StringBuilder r0 = new StringBuilder().append(this.first).append("..").append(this.last).append(" step ");
        long r1 = this.step;
    L7:
        return r0.append(r1).toString();
    L5:
        r0 = new StringBuilder().append(this.first).append(" downTo ").append(this.last).append(" step ");
        r1 = -this.step;
        goto L7
    }
}
