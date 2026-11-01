package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@Immutable(containerOf = {"C"})
@ElementTypesAreNonnullByDefault
public final class Range<C extends Comparable> extends RangeGwtSerializationDependencies implements Predicate<C>, Serializable {
    private static final Range<Comparable> ALL = null;
    private static final long serialVersionUID = 0;
    final Cut<C> lowerBound;
    final Cut<C> upperBound;

    /* JADX INFO: renamed from: com.google.common.collect.Range$1 */
    static /* synthetic */ class C07161 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$collect$BoundType = null;

        static {
            $SwitchMap$com$google$common$collect$BoundType = new int[BoundType.values().length];
            $SwitchMap$com$google$common$collect$BoundType[BoundType.OPEN.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L12:
            $SwitchMap$com$google$common$collect$BoundType[BoundType.CLOSED.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
            return;
        }
    }

    private static class RangeLexOrdering extends Ordering<Range<?>> implements Serializable {
        static final Ordering<?> INSTANCE = null;
        private static final long serialVersionUID = 0;

        static {
            INSTANCE = new RangeLexOrdering();
        }

        private RangeLexOrdering() {
        }

        public int compare(Range<?> r4, Range<?> r5) {
            return ComparisonChain.start().compare(r4.lowerBound, r5.lowerBound).compare(r4.upperBound, r5.upperBound).result();
        }

        @Override // com.google.common.collect.Ordering, java.util.Comparator
        public /* bridge */ /* synthetic */ int compare(Object r1, Object r2) {
            return compare((Range) r1, (Range) r2);
        }
    }

    static {
        ALL = new Range(Cut.belowAll(), Cut.aboveAll());
    }

    private Range(Cut<C> r4, Cut<C> r5) {
        this.lowerBound = (Cut) Preconditions.checkNotNull(r4);
        this.upperBound = (Cut) Preconditions.checkNotNull(r5);
        if (r4.compareTo(r5) > 0) goto L10;
        if (r4 == Cut.aboveAll()) goto L10;
        if (r5 == Cut.belowAll()) goto L10;
        return;
    L10:
        throw new IllegalArgumentException("Invalid range: " + toString(r4, r5));
    }

    public static <C extends Comparable<?>> Range<C> all() {
        return (Range<C>) ALL;
    }

    public static <C extends Comparable<?>> Range<C> atLeast(C r2) {
        return create(Cut.belowValue(r2), Cut.aboveAll());
    }

    public static <C extends Comparable<?>> Range<C> atMost(C r2) {
        return create(Cut.belowAll(), Cut.aboveValue(r2));
    }

    public static <C extends Comparable<?>> Range<C> closed(C r2, C r3) {
        return create(Cut.belowValue(r2), Cut.aboveValue(r3));
    }

    public static <C extends Comparable<?>> Range<C> closedOpen(C r2, C r3) {
        return create(Cut.belowValue(r2), Cut.belowValue(r3));
    }

    static int compareOrThrow(Comparable r1, Comparable r2) {
        return r1.compareTo(r2);
    }

    static <C extends Comparable<?>> Range<C> create(Cut<C> r1, Cut<C> r2) {
        return new Range(r1, r2);
    }

    public static <C extends Comparable<?>> Range<C> downTo(C r2, BoundType r3) {
        switch(C07161.$SwitchMap$com$google$common$collect$BoundType[r3.ordinal()]) {
            case 1: goto L9;
            case 2: goto L7;
            default: goto L5;
        };
    L5:
        throw new AssertionError();
    L7:
        return atLeast(r2);
    L9:
        return greaterThan(r2);
    }

    public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> r5) {
        Preconditions.checkNotNull(r5);
        if ((r5 instanceof SortedSet) == false) goto L9;
        SortedSet r0 = (SortedSet) r5;
        Comparator r1 = r0.comparator();
        if (Ordering.natural().equals(r1) == true) goto L8;
        if (r1 != null) goto L9;
    L8:
        return closed((Comparable) r0.first(), (Comparable) r0.last());
    L9:
        Iterator<C> r2 = r5.iterator();
        Comparable r3 = (Comparable) Preconditions.checkNotNull(r2.next());
        Comparable r4 = r3;
    L11:
        if (r2.hasNext() == false) goto L14;
        Comparable r6 = (Comparable) Preconditions.checkNotNull(r2.next());
        r3 = (Comparable) Ordering.natural().min(r3, r6);
        r4 = (Comparable) Ordering.natural().max(r4, r6);
        goto L11
    L14:
        return closed(r3, r4);
    }

    public static <C extends Comparable<?>> Range<C> greaterThan(C r2) {
        return create(Cut.aboveValue(r2), Cut.aboveAll());
    }

    public static <C extends Comparable<?>> Range<C> lessThan(C r2) {
        return create(Cut.belowAll(), Cut.belowValue(r2));
    }

    public static <C extends Comparable<?>> Range<C> open(C r2, C r3) {
        return create(Cut.aboveValue(r2), Cut.belowValue(r3));
    }

    public static <C extends Comparable<?>> Range<C> openClosed(C r2, C r3) {
        return create(Cut.aboveValue(r2), Cut.aboveValue(r3));
    }

    public static <C extends Comparable<?>> Range<C> range(C r3, BoundType r4, C r5, BoundType r6) {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r6);
        if (r4 != BoundType.OPEN) goto L5;
        Cut r0 = Cut.aboveValue(r3);
    L7:
        if (r6 != BoundType.OPEN) goto L9;
        Cut r1 = Cut.belowValue(r5);
    L11:
        return create(r0, r1);
    L9:
        r1 = Cut.aboveValue(r5);
        goto L11
    L5:
        r0 = Cut.belowValue(r3);
        goto L7
    }

    static <C extends Comparable<?>> Ordering<Range<C>> rangeLexOrdering() {
        return (Ordering<Range<C>>) RangeLexOrdering.INSTANCE;
    }

    public static <C extends Comparable<?>> Range<C> singleton(C r1) {
        return closed(r1, r1);
    }

    private static String toString(Cut<?> r2, Cut<?> r3) {
        StringBuilder r0 = new StringBuilder(16);
        r2.describeAsLowerBound(r0);
        r0.append("..");
        r3.describeAsUpperBound(r0);
        return r0.toString();
    }

    public static <C extends Comparable<?>> Range<C> upTo(C r2, BoundType r3) {
        switch(C07161.$SwitchMap$com$google$common$collect$BoundType[r3.ordinal()]) {
            case 1: goto L9;
            case 2: goto L7;
            default: goto L5;
        };
    L5:
        throw new AssertionError();
    L7:
        return atMost(r2);
    L9:
        return lessThan(r2);
    }

    @Deprecated
    public boolean apply(C r2) {
        return contains(r2);
    }

    @Override // com.google.common.base.Predicate
    @Deprecated
    public /* bridge */ /* synthetic */ boolean apply(Object r1) {
        return apply((Comparable) r1);
    }

    public Range<C> canonical(DiscreteDomain<C> r4) {
        Preconditions.checkNotNull(r4);
        Cut<C> r0 = this.lowerBound.canonical(r4);
        Cut<C> r1 = this.upperBound.canonical(r4);
        if (r0 != this.lowerBound) goto L8;
        if (r1 != this.upperBound) goto L8;
        return this;
    L8:
        return create(r0, r1);
    }

    public boolean contains(C r2) {
        Preconditions.checkNotNull(r2);
        if (this.lowerBound.isLessThan(r2) == true) goto L5;
    L7:
        return false;
    L5:
        if (this.upperBound.isLessThan(r2) == true) goto L7;
        return true;
    }

    public boolean containsAll(Iterable<? extends C> r6) {
        if (Iterables.isEmpty(r6) == false) goto L6;
        return true;
    L6:
        if ((r6 instanceof SortedSet) == false) goto L17;
        SortedSet r0 = (SortedSet) r6;
        Comparator r3 = r0.comparator();
        if (Ordering.natural().equals(r3) == true) goto L11;
        if (r3 != null) goto L17;
    L11:
        if (contains((Comparable) r0.first()) == false) goto L16;
        if (contains((Comparable) r0.last()) == false) goto L16;
        return true;
    L16:
        return false;
    L17:
        Iterator<? extends C> r1 = r6.iterator();
    L19:
        if (r1.hasNext() == false) goto L24;
        if (contains(r1.next()) == true) goto L19;
        return false;
    L24:
        return true;
    }

    public boolean encloses(Range<C> r3) {
        if (this.lowerBound.compareTo(r3.lowerBound) <= 0) goto L5;
    L7:
        return false;
    L5:
        if (this.upperBound.compareTo(r3.upperBound) < 0) goto L7;
        return true;
    }

    @Override // com.google.common.base.Predicate
    public boolean equals(@CheckForNull Object r5) {
        if ((r5 instanceof Range) == false) goto L10;
        Range r0 = (Range) r5;
        if (this.lowerBound.equals(r0.lowerBound) == true) goto L7;
        return false;
    L7:
        if (this.upperBound.equals(r0.upperBound) == false) goto L12;
        return true;
    L12:
        return false;
    L10:
        return false;
    }

    public Range<C> gap(Range<C> r6) {
        if (this.lowerBound.compareTo(r6.upperBound) >= 0) goto L10;
        if (r6.lowerBound.compareTo(this.upperBound) >= 0) goto L10;
        throw new IllegalArgumentException("Ranges have a nonempty intersection: " + this + ", " + r6);
    L10:
        if (this.lowerBound.compareTo(r6.lowerBound) >= 0) goto L12;
        boolean r0 = true;
    L13:
        if (r0 == false) goto L15;
        Range<C> r1 = this;
    L16:
        if (r0 == false) goto L18;
        Range<C> r2 = r6;
    L20:
        return create(r1.upperBound, r2.lowerBound);
    L18:
        r2 = this;
        goto L20
    L15:
        r1 = r6;
        goto L16
    L12:
        r0 = false;
        goto L13
    }

    public boolean hasLowerBound() {
        if (this.lowerBound == Cut.belowAll()) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean hasUpperBound() {
        if (this.upperBound == Cut.aboveAll()) goto L5;
        return true;
    L5:
        return false;
    }

    public int hashCode() {
        return (this.lowerBound.hashCode() * 31) + this.upperBound.hashCode();
    }

    public Range<C> intersection(Range<C> r7) {
        int r0 = this.lowerBound.compareTo(r7.lowerBound);
        int r1 = this.upperBound.compareTo(r7.upperBound);
        if (r0 < 0) goto L6;
        if (r1 > 0) goto L6;
        return this;
    L6:
        if (r0 > 0) goto L9;
        if (r1 < 0) goto L9;
        return r7;
    L9:
        if (r0 < 0) goto L11;
        Cut<C> r2 = this.lowerBound;
    L12:
        if (r1 > 0) goto L14;
        Cut<C> r3 = this.upperBound;
    L16:
        if (r2.compareTo(r3) > 0) goto L18;
        boolean r4 = true;
    L19:
        Preconditions.checkArgument(r4, "intersection is undefined for disconnected ranges %s and %s", this, r7);
        return create(r2, r3);
    L18:
        r4 = false;
        goto L19
    L14:
        r3 = r7.upperBound;
        goto L16
    L11:
        r2 = r7.lowerBound;
        goto L12
    }

    public boolean isConnected(Range<C> r3) {
        if (this.lowerBound.compareTo(r3.upperBound) <= 0) goto L5;
    L7:
        return false;
    L5:
        if (r3.lowerBound.compareTo(this.upperBound) > 0) goto L7;
        return true;
    }

    public boolean isEmpty() {
        return this.lowerBound.equals(this.upperBound);
    }

    Cut<C> lowerBound() {
        return this.lowerBound;
    }

    public BoundType lowerBoundType() {
        return this.lowerBound.typeAsLowerBound();
    }

    public C lowerEndpoint() {
        return (C) this.lowerBound.endpoint();
    }

    Object readResolve() {
        if (equals(ALL) == true) goto L5;
        return this;
    L5:
        return all();
    }

    public Range<C> span(Range<C> r6) {
        int r0 = this.lowerBound.compareTo(r6.lowerBound);
        int r1 = this.upperBound.compareTo(r6.upperBound);
        if (r0 > 0) goto L6;
        if (r1 < 0) goto L6;
        return this;
    L6:
        if (r0 < 0) goto L9;
        if (r1 > 0) goto L9;
        return r6;
    L9:
        if (r0 > 0) goto L11;
        Cut<C> r2 = this.lowerBound;
    L12:
        if (r1 < 0) goto L14;
        Cut<C> r3 = this.upperBound;
    L16:
        return create(r2, r3);
    L14:
        r3 = r6.upperBound;
        goto L16
    L11:
        r2 = r6.lowerBound;
        goto L12
    }

    public String toString() {
        return toString(this.lowerBound, this.upperBound);
    }

    Cut<C> upperBound() {
        return this.upperBound;
    }

    public BoundType upperBoundType() {
        return this.upperBound.typeAsUpperBound();
    }

    public C upperEndpoint() {
        return (C) this.upperBound.endpoint();
    }
}
