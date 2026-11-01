package kotlin.ranges;

import com.google.common.primitives.C0871x7f2744bd;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.ULong;
import kotlin.internal.UProgressionUtilKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: ULongRange.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0017\u0018\u0000 \u001a2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u001aB\u001f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0010H\u0016J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00020\u0017H\u0086\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0016R\u0016\u0010\b\u001a\u00020\u0002ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\nR\u0016\u0010\f\u001a\u00020\u0002ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001b"}, m421d2 = {"Lkotlin/ranges/ULongProgression;", "", "Lkotlin/ULong;", "start", "endInclusive", "step", "", "(JJJLkotlin/jvm/internal/DefaultConstructorMarker;)V", "first", "getFirst-s-VKNKU", "()J", "J", "last", "getLast-s-VKNKU", "getStep", "equals", "", "other", "", "hashCode", "", "isEmpty", "iterator", "", "toString", "", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public class ULongProgression implements Iterable<ULong>, KMappedMarker {
    public static final Companion Companion = null;
    private final long first;
    private final long last;
    private final long step;

    /* JADX INFO: compiled from: ULongRange.kt */
    @Metadata(m420d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J%\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000b¨\u0006\f"}, m421d2 = {"Lkotlin/ranges/ULongProgression$Companion;", "", "()V", "fromClosedRange", "Lkotlin/ranges/ULongProgression;", "rangeStart", "Lkotlin/ULong;", "rangeEnd", "step", "", "fromClosedRange-7ftBX0g", "(JJJ)Lkotlin/ranges/ULongProgression;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        /* JADX INFO: renamed from: fromClosedRange-7ftBX0g, reason: not valid java name */
        public final ULongProgression m2053fromClosedRange7ftBX0g(long r9, long r11, long r13) {
            return new ULongProgression(r9, r11, r13, null);
        }
    }

    static {
        Companion = new Companion(null);
    }

    private ULongProgression(long r3, long r5, long r7) {
        if (r7 == 0) goto L11;
        if (r7 == Long.MIN_VALUE) goto L9;
        this.first = r3;
        this.last = UProgressionUtilKt.m2027getProgressionLastElement7ftBX0g(r3, r5, r7);
        this.step = r7;
        return;
    L9:
        throw new IllegalArgumentException("Step must be greater than Long.MIN_VALUE to avoid overflow on negation.");
    L11:
        throw new IllegalArgumentException("Step must be non-zero.");
    }

    public /* synthetic */ ULongProgression(long r1, long r3, long r5, DefaultConstructorMarker r7) {
        this(r1, r3, r5);
    }

    public boolean equals(Object r5) {
        if ((r5 instanceof ULongProgression) == true) goto L5;
    L15:
        return false;
    L5:
        if (isEmpty() == false) goto L9;
        if (((ULongProgression) r5).isEmpty() == false) goto L9;
    L14:
        return true;
    L9:
        if (this.first != ((ULongProgression) r5).first) goto L15;
        if (this.last != ((ULongProgression) r5).last) goto L15;
        if (this.step != ((ULongProgression) r5).step) goto L15;
        goto L14
    }

    /* JADX INFO: renamed from: getFirst-s-VKNKU, reason: not valid java name */
    public final long m2051getFirstsVKNKU() {
        return this.first;
    }

    /* JADX INFO: renamed from: getLast-s-VKNKU, reason: not valid java name */
    public final long m2052getLastsVKNKU() {
        return this.last;
    }

    public final long getStep() {
        return this.step;
    }

    public int hashCode() {
        if (isEmpty() == false) goto L6;
        return -1;
    L6:
        return (((((int) ULong.m993constructorimpl(this.first ^ ULong.m993constructorimpl(this.first >>> 32))) * 31) + ((int) ULong.m993constructorimpl(this.last ^ ULong.m993constructorimpl(this.last >>> 32)))) * 31) + ((int) (this.step ^ (this.step >>> 32)));
    }

    public boolean isEmpty() {
        long r0 = this.step;
        long r3 = this.first;
        long r5 = this.last;
        if (r0 <= 0) goto L8;
        if (C0871x7f2744bd.m371m(r3, r5) <= 0) goto L11;
        return true;
    L11:
        return false;
    L8:
        if (C0871x7f2744bd.m371m(r3, r5) >= 0) goto L11;
        return true;
    }

    @Override // java.lang.Iterable
    public final Iterator<ULong> iterator() {
        return new ULongProgressionIterator(this.first, this.last, this.step, null);
    }

    public String toString() {
        if (this.step <= 0) goto L5;
        StringBuilder r0 = new StringBuilder().append(ULong.m1039toStringimpl(this.first)).append("..").append(ULong.m1039toStringimpl(this.last)).append(" step ");
        long r1 = this.step;
    L7:
        return r0.append(r1).toString();
    L5:
        r0 = new StringBuilder().append(ULong.m1039toStringimpl(this.first)).append(" downTo ").append(ULong.m1039toStringimpl(this.last)).append(" step ");
        r1 = -this.step;
        goto L7
    }
}
