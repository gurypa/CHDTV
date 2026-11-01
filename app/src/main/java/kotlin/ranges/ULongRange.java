package kotlin.ranges;

import com.google.common.primitives.C0871x7f2744bd;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ULong;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: ULongRange.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u0000 \u001c2\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u00022\b\u0012\u0004\u0012\u00020\u00030\u0004:\u0001\u001cB\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0003H\u0096\u0002¢\u0006\u0004\b\u0012\u0010\u0013J\u0013\u0010\u0014\u001a\u00020\u00102\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0096\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0010H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u001d\u0010\b\u001a\u00020\u00038VX\u0097\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\u0006\u001a\u00020\u00038VX\u0096\u0004ø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\r\u0010\fR\u0017\u0010\u0005\u001a\u00020\u00038VX\u0096\u0004ø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\u000e\u0010\f\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001d"}, m421d2 = {"Lkotlin/ranges/ULongRange;", "Lkotlin/ranges/ULongProgression;", "Lkotlin/ranges/ClosedRange;", "Lkotlin/ULong;", "Lkotlin/ranges/OpenEndRange;", "start", "endInclusive", "(JJLkotlin/jvm/internal/DefaultConstructorMarker;)V", "endExclusive", "getEndExclusive-s-VKNKU$annotations", "()V", "getEndExclusive-s-VKNKU", "()J", "getEndInclusive-s-VKNKU", "getStart-s-VKNKU", "contains", "", "value", "contains-VKZWuLQ", "(J)Z", "equals", "other", "", "hashCode", "", "isEmpty", "toString", "", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class ULongRange extends ULongProgression implements ClosedRange<ULong>, OpenEndRange<ULong> {
    public static final Companion Companion = null;
    private static final ULongRange EMPTY = null;

    /* JADX INFO: compiled from: ULongRange.kt */
    @Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, m421d2 = {"Lkotlin/ranges/ULongRange$Companion;", "", "()V", "EMPTY", "Lkotlin/ranges/ULongRange;", "getEMPTY", "()Lkotlin/ranges/ULongRange;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        public final ULongRange getEMPTY() {
            return ULongRange.access$getEMPTY$cp();
        }
    }

    static {
        Companion = new Companion(null);
        long r3 = -1;
        EMPTY = new ULongRange(r3, 0, null);
    }

    private ULongRange(long r9, long r11) {
        super(r9, r11, 1, null);
    }

    public /* synthetic */ ULongRange(long r1, long r3, DefaultConstructorMarker r5) {
        this(r1, r3);
    }

    public static final /* synthetic */ ULongRange access$getEMPTY$cp() {
        return EMPTY;
    }

    @Deprecated(message = "Can throw an exception when it's impossible to represent the value with ULong type, for example, when the range includes MAX_VALUE. It's recommended to use 'endInclusive' property that doesn't throw.")
    /* JADX INFO: renamed from: getEndExclusive-s-VKNKU$annotations, reason: not valid java name */
    public static /* synthetic */ void m2055getEndExclusivesVKNKU$annotations() {
    }

    @Override // kotlin.ranges.ClosedRange
    public /* bridge */ /* synthetic */ boolean contains(Comparable r3) {
        return m2056containsVKZWuLQ(((ULong) r3).m1045unboximpl());
    }

    /* JADX INFO: renamed from: contains-VKZWuLQ, reason: not valid java name */
    public boolean m2056containsVKZWuLQ(long r3) {
        if (C0871x7f2744bd.m371m(m2051getFirstsVKNKU(), r3) <= 0) goto L5;
    L7:
        return false;
    L5:
        if (C0871x7f2744bd.m371m(r3, m2052getLastsVKNKU()) > 0) goto L7;
        return true;
    }

    @Override // kotlin.ranges.ULongProgression
    public boolean equals(Object r5) {
        if ((r5 instanceof ULongRange) == true) goto L5;
    L13:
        return false;
    L5:
        if (isEmpty() == false) goto L9;
        if (((ULongRange) r5).isEmpty() == false) goto L9;
    L12:
        return true;
    L9:
        if (m2051getFirstsVKNKU() != ((ULongRange) r5).m2051getFirstsVKNKU()) goto L13;
        if (m2052getLastsVKNKU() != ((ULongRange) r5).m2052getLastsVKNKU()) goto L13;
        goto L12
    }

    @Override // kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ Comparable getEndExclusive() {
        return ULong.m987boximpl(m2057getEndExclusivesVKNKU());
    }

    /* JADX INFO: renamed from: getEndExclusive-s-VKNKU, reason: not valid java name */
    public long m2057getEndExclusivesVKNKU() {
        if (m2052getLastsVKNKU() == (-1)) goto L7;
        return ULong.m993constructorimpl(m2052getLastsVKNKU() + ULong.m993constructorimpl(((long) 1) & 4294967295L));
    L7:
        throw new IllegalStateException("Cannot return the exclusive upper bound of a range that includes MAX_VALUE.".toString());
    }

    @Override // kotlin.ranges.ClosedRange
    public /* bridge */ /* synthetic */ Comparable getEndInclusive() {
        return ULong.m987boximpl(m2058getEndInclusivesVKNKU());
    }

    /* JADX INFO: renamed from: getEndInclusive-s-VKNKU, reason: not valid java name */
    public long m2058getEndInclusivesVKNKU() {
        return m2052getLastsVKNKU();
    }

    @Override // kotlin.ranges.ClosedRange
    public /* bridge */ /* synthetic */ Comparable getStart() {
        return ULong.m987boximpl(m2059getStartsVKNKU());
    }

    /* JADX INFO: renamed from: getStart-s-VKNKU, reason: not valid java name */
    public long m2059getStartsVKNKU() {
        return m2051getFirstsVKNKU();
    }

    @Override // kotlin.ranges.ULongProgression
    public int hashCode() {
        if (isEmpty() == false) goto L6;
        return -1;
    L6:
        return (((int) ULong.m993constructorimpl(m2051getFirstsVKNKU() ^ ULong.m993constructorimpl(m2051getFirstsVKNKU() >>> 32))) * 31) + ((int) ULong.m993constructorimpl(m2052getLastsVKNKU() ^ ULong.m993constructorimpl(m2052getLastsVKNKU() >>> 32)));
    }

    @Override // kotlin.ranges.ULongProgression, kotlin.ranges.ClosedRange
    public boolean isEmpty() {
        if (C0871x7f2744bd.m371m(m2051getFirstsVKNKU(), m2052getLastsVKNKU()) <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // kotlin.ranges.ULongProgression
    public String toString() {
        return ULong.m1039toStringimpl(m2051getFirstsVKNKU()) + ".." + ULong.m1039toStringimpl(m2052getLastsVKNKU());
    }
}
