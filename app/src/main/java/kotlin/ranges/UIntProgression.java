package kotlin.ranges;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.UByte$$ExternalSyntheticBackport4;
import kotlin.UInt;
import kotlin.internal.UProgressionUtilKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: UIntRange.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010(\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0017\u0018\u0000 \u00192\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0019B\u001f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0006H\u0016J\b\u0010\u0014\u001a\u00020\u0010H\u0016J\u000f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00020\u0016H\u0086\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u0016\u0010\b\u001a\u00020\u0002ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\nR\u0016\u0010\f\u001a\u00020\u0002ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001a"}, m421d2 = {"Lkotlin/ranges/UIntProgression;", "", "Lkotlin/UInt;", "start", "endInclusive", "step", "", "(IIILkotlin/jvm/internal/DefaultConstructorMarker;)V", "first", "getFirst-pVg5ArA", "()I", "I", "last", "getLast-pVg5ArA", "getStep", "equals", "", "other", "", "hashCode", "isEmpty", "iterator", "", "toString", "", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public class UIntProgression implements Iterable<UInt>, KMappedMarker {
    public static final Companion Companion = null;
    private final int first;
    private final int last;
    private final int step;

    /* JADX INFO: compiled from: UIntRange.kt */
    @Metadata(m420d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J%\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000b¨\u0006\f"}, m421d2 = {"Lkotlin/ranges/UIntProgression$Companion;", "", "()V", "fromClosedRange", "Lkotlin/ranges/UIntProgression;", "rangeStart", "Lkotlin/UInt;", "rangeEnd", "step", "", "fromClosedRange-Nkh28Cs", "(III)Lkotlin/ranges/UIntProgression;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        /* JADX INFO: renamed from: fromClosedRange-Nkh28Cs, reason: not valid java name */
        public final UIntProgression m2044fromClosedRangeNkh28Cs(int r3, int r4, int r5) {
            return new UIntProgression(r3, r4, r5, null);
        }
    }

    static {
        Companion = new Companion(null);
    }

    private UIntProgression(int r3, int r4, int r5) {
        if (r5 == 0) goto L11;
        if (r5 == Integer.MIN_VALUE) goto L9;
        this.first = r3;
        this.last = UProgressionUtilKt.m2028getProgressionLastElementNkh28Cs(r3, r4, r5);
        this.step = r5;
        return;
    L9:
        throw new IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.");
    L11:
        throw new IllegalArgumentException("Step must be non-zero.");
    }

    public /* synthetic */ UIntProgression(int r1, int r2, int r3, DefaultConstructorMarker r4) {
        this(r1, r2, r3);
    }

    public boolean equals(Object r3) {
        if ((r3 instanceof UIntProgression) == true) goto L5;
    L15:
        return false;
    L5:
        if (isEmpty() == false) goto L9;
        if (((UIntProgression) r3).isEmpty() == false) goto L9;
    L14:
        return true;
    L9:
        if (this.first != ((UIntProgression) r3).first) goto L15;
        if (this.last != ((UIntProgression) r3).last) goto L15;
        if (this.step != ((UIntProgression) r3).step) goto L15;
        goto L14
    }

    /* JADX INFO: renamed from: getFirst-pVg5ArA, reason: not valid java name */
    public final int m2042getFirstpVg5ArA() {
        return this.first;
    }

    /* JADX INFO: renamed from: getLast-pVg5ArA, reason: not valid java name */
    public final int m2043getLastpVg5ArA() {
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
        if (UByte$$ExternalSyntheticBackport4.m433m(this.first, this.last) <= 0) goto L11;
        return true;
    L11:
        return false;
    L8:
        if (UByte$$ExternalSyntheticBackport4.m433m(this.first, this.last) >= 0) goto L11;
        return true;
    }

    @Override // java.lang.Iterable
    public final Iterator<UInt> iterator() {
        return new UIntProgressionIterator(this.first, this.last, this.step, null);
    }

    public String toString() {
        if (this.step <= 0) goto L5;
        StringBuilder r0 = new StringBuilder().append(UInt.m960toStringimpl(this.first)).append("..").append(UInt.m960toStringimpl(this.last)).append(" step ");
        int r1 = this.step;
    L7:
        return r0.append(r1).toString();
    L5:
        r0 = new StringBuilder().append(UInt.m960toStringimpl(this.first)).append(" downTo ").append(UInt.m960toStringimpl(this.last)).append(" step ");
        r1 = -this.step;
        goto L7
    }
}
