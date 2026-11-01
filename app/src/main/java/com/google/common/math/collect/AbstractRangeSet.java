package com.google.common.collect;

import java.lang.Comparable;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractRangeSet<C extends Comparable> implements RangeSet<C> {
    AbstractRangeSet() {
    }

    @Override // com.google.common.collect.RangeSet
    public void add(Range<C> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.RangeSet
    public void addAll(RangeSet<C> r2) {
        addAll(r2.asRanges());
    }

    @Override // com.google.common.collect.RangeSet
    public void addAll(Iterable<Range<C>> r3) {
        Iterator<Range<C>> r0 = r3.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        add(r0.next());
        goto L4
    }

    @Override // com.google.common.collect.RangeSet
    public void clear() {
        remove(Range.all());
    }

    @Override // com.google.common.collect.RangeSet
    public boolean contains(C r2) {
        if (rangeContaining(r2) == null) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // com.google.common.collect.RangeSet
    public abstract boolean encloses(Range<C> r1);

    @Override // com.google.common.collect.RangeSet
    public boolean enclosesAll(RangeSet<C> r2) {
        return enclosesAll(r2.asRanges());
    }

    @Override // com.google.common.collect.RangeSet
    public boolean enclosesAll(Iterable<Range<C>> r4) {
        Iterator<Range<C>> r0 = r4.iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (encloses(r0.next()) == true) goto L4;
        return false;
    L10:
        return true;
    }

    @Override // com.google.common.collect.RangeSet
    public boolean equals(@CheckForNull Object r4) {
        if (r4 != this) goto L6;
        return true;
    L6:
        if ((r4 instanceof RangeSet) == true) goto L8;
        return false;
    L8:
        return asRanges().equals(((RangeSet) r4).asRanges());
    }

    @Override // com.google.common.collect.RangeSet
    public final int hashCode() {
        return asRanges().hashCode();
    }

    @Override // com.google.common.collect.RangeSet
    public boolean intersects(Range<C> r2) {
        return !subRangeSet(r2).isEmpty();
    }

    @Override // com.google.common.collect.RangeSet
    public boolean isEmpty() {
        return asRanges().isEmpty();
    }

    @Override // com.google.common.collect.RangeSet
    @CheckForNull
    public abstract Range<C> rangeContaining(C r1);

    @Override // com.google.common.collect.RangeSet
    public void remove(Range<C> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.RangeSet
    public void removeAll(RangeSet<C> r2) {
        removeAll(r2.asRanges());
    }

    @Override // com.google.common.collect.RangeSet
    public void removeAll(Iterable<Range<C>> r3) {
        Iterator<Range<C>> r0 = r3.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        remove(r0.next());
        goto L4
    }

    @Override // com.google.common.collect.RangeSet
    public final String toString() {
        return asRanges().toString();
    }
}
