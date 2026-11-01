package com.google.common.collect;

import com.google.errorprone.annotations.DoNotMock;
import java.lang.Comparable;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use ImmutableRangeMap or TreeRangeMap")
@ElementTypesAreNonnullByDefault
public interface RangeMap<K extends Comparable, V> {
    Map<Range<K>, V> asDescendingMapOfRanges();

    Map<Range<K>, V> asMapOfRanges();

    void clear();

    boolean equals(@CheckForNull Object r1);

    @CheckForNull
    V get(K r1);

    @CheckForNull
    Map.Entry<Range<K>, V> getEntry(K r1);

    int hashCode();

    void put(Range<K> r1, V r2);

    void putAll(RangeMap<K, ? extends V> r1);

    void putCoalescing(Range<K> r1, V r2);

    void remove(Range<K> r1);

    Range<K> span();

    RangeMap<K, V> subRangeMap(Range<K> r1);

    String toString();
}
