package kotlin.ranges;

import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: PrimitiveRanges.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 \u001a2\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u00022\b\u0012\u0004\u0012\u00020\u00030\u0004:\u0001\u001aB\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u0011\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0003H\u0096\u0002J\u0013\u0010\u0012\u001a\u00020\u00102\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0096\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0010H\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0016R\u001a\u0010\b\u001a\u00020\u00038VX\u0097\u0004¢\u0006\f\u0012\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\u0006\u001a\u00020\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\fR\u0014\u0010\u0005\u001a\u00020\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\f¨\u0006\u001b"}, m421d2 = {"Lkotlin/ranges/LongRange;", "Lkotlin/ranges/LongProgression;", "Lkotlin/ranges/ClosedRange;", "", "Lkotlin/ranges/OpenEndRange;", "start", "endInclusive", "(JJ)V", "endExclusive", "getEndExclusive$annotations", "()V", "getEndExclusive", "()Ljava/lang/Long;", "getEndInclusive", "getStart", "contains", "", "value", "equals", "other", "", "hashCode", "", "isEmpty", "toString", "", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class LongRange extends LongProgression implements ClosedRange<Long>, OpenEndRange<Long> {
    public static final Companion Companion = null;
    private static final LongRange EMPTY = null;

    /* JADX INFO: compiled from: PrimitiveRanges.kt */
    @Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, m421d2 = {"Lkotlin/ranges/LongRange$Companion;", "", "()V", "EMPTY", "Lkotlin/ranges/LongRange;", "getEMPTY", "()Lkotlin/ranges/LongRange;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        public final LongRange getEMPTY() {
            return LongRange.access$getEMPTY$cp();
        }
    }

    static {
        Companion = new Companion(null);
        EMPTY = new LongRange(1, 0);
    }

    public LongRange(long r8, long r10) {
        super(r8, r10, 1);
    }

    public static final /* synthetic */ LongRange access$getEMPTY$cp() {
        return EMPTY;
    }

    @Deprecated(message = "Can throw an exception when it's impossible to represent the value with Long type, for example, when the range includes MAX_VALUE. It's recommended to use 'endInclusive' property that doesn't throw.")
    public static /* synthetic */ void getEndExclusive$annotations() {
    }

    public boolean contains(long r3) {
        if (getFirst() <= r3) goto L5;
    L7:
        return false;
    L5:
        if (r3 > getLast()) goto L7;
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.ranges.ClosedRange
    public /* bridge */ /* synthetic */ boolean contains(Comparable r3) {
        return contains(((Number) r3).longValue());
    }

    @Override // kotlin.ranges.LongProgression
    public boolean equals(Object r5) {
        if ((r5 instanceof LongRange) == true) goto L5;
    L13:
        return false;
    L5:
        if (isEmpty() == false) goto L9;
        if (((LongRange) r5).isEmpty() == false) goto L9;
    L12:
        return true;
    L9:
        if (getFirst() != ((LongRange) r5).getFirst()) goto L13;
        if (getLast() != ((LongRange) r5).getLast()) goto L13;
        goto L12
    }

    @Override // kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ Comparable getEndExclusive() {
        return getEndExclusive();
    }

    @Override // kotlin.ranges.OpenEndRange
    public Long getEndExclusive() {
        if (getLast() == Long.MAX_VALUE) goto L7;
        return Long.valueOf(getLast() + 1);
    L7:
        throw new IllegalStateException("Cannot return the exclusive upper bound of a range that includes MAX_VALUE.".toString());
    }

    @Override // kotlin.ranges.ClosedRange
    public /* bridge */ /* synthetic */ Comparable getEndInclusive() {
        return getEndInclusive();
    }

    @Override // kotlin.ranges.ClosedRange
    public Long getEndInclusive() {
        return Long.valueOf(getLast());
    }

    @Override // kotlin.ranges.ClosedRange
    public /* bridge */ /* synthetic */ Comparable getStart() {
        return getStart();
    }

    @Override // kotlin.ranges.ClosedRange
    public Long getStart() {
        return Long.valueOf(getFirst());
    }

    @Override // kotlin.ranges.LongProgression
    public int hashCode() {
        if (isEmpty() == false) goto L6;
        return -1;
    L6:
        return (int) ((((long) 31) * (getFirst() ^ (getFirst() >>> 32))) + (getLast() ^ (getLast() >>> 32)));
    }

    @Override // kotlin.ranges.LongProgression, kotlin.ranges.ClosedRange
    public boolean isEmpty() {
        if (getFirst() <= getLast()) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // kotlin.ranges.LongProgression
    public String toString() {
        return getFirst() + ".." + getLast();
    }
}
