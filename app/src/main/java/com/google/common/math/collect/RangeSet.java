package com.google.common.collect;

import com.google.errorprone.annotations.DoNotMock;
import java.lang.Comparable;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use ImmutableRangeSet or TreeRangeSet")
@ElementTypesAreNonnullByDefault
public interface RangeSet<C extends Comparable> {
    void add(Range<C> r1);

    void addAll(RangeSet<C> r1);

    void addAll(Iterable<Range<C>> r1);

    Set<Range<C>> asDescendingSetOfRanges();

    Set<Range<C>> asRanges();

    void clear();

    RangeSet<C> complement();

    boolean contains(C r1);

    boolean encloses(Range<C> r1);

    boolean enclosesAll(RangeSet<C> r1);

    boolean enclosesAll(Iterable<Range<C>> r1);

    boolean equals(@CheckForNull Object r1);

    int hashCode();

    boolean intersects(Range<C> r1);

    boolean isEmpty();

    @CheckForNull
    Range<C> rangeContaining(C r1);

    void remove(Range<C> r1);

    void removeAll(RangeSet<C> r1);

    void removeAll(Iterable<Range<C>> r1);

    Range<C> span();

    RangeSet<C> subRangeSet(Range<C> r1);

    String toString();
}
