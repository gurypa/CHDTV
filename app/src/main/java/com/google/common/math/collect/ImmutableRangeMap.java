package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.DoNotMock;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class ImmutableRangeMap<K extends Comparable<?>, V> implements RangeMap<K, V>, Serializable {
    private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = null;
    private static final long serialVersionUID = 0;
    private final transient ImmutableList<Range<K>> ranges;
    private final transient ImmutableList<V> values;

    @DoNotMock
    public static final class Builder<K extends Comparable<?>, V> {
        private final List<Map.Entry<Range<K>, V>> entries;

        public Builder() {
            this.entries = Lists.newArrayList();
        }

        public ImmutableRangeMap<K, V> build() {
            Collections.sort(this.entries, Range.rangeLexOrdering().onKeys());
            ImmutableList.Builder r0 = new ImmutableList.Builder(this.entries.size());
            ImmutableList.Builder r1 = new ImmutableList.Builder(this.entries.size());
            int r2 = 0;
        L4:
            if (r2 >= this.entries.size()) goto L16;
            Range<K> r3 = this.entries.get(r2).getKey();
            if (r2 <= 0) goto L14;
            Range<K> r4 = this.entries.get(r2 - 1).getKey();
            if (r3.isConnected(r4) == false) goto L14;
            if (r3.intersection(r4).isEmpty() == true) goto L14;
            throw new IllegalArgumentException("Overlapping ranges: range " + r4 + " overlaps with entry " + r3);
        L14:
            r0.add(r3);
            r1.add(this.entries.get(r2).getValue());
            r2 = r2 + 1;
            goto L4
        L16:
            return new ImmutableRangeMap(r0.build(), r1.build());
        }

        Builder<K, V> combine(Builder<K, V> r3) {
            this.entries.addAll(r3.entries);
            return this;
        }

        public Builder<K, V> put(Range<K> r3, V r4) {
            Preconditions.checkNotNull(r3);
            Preconditions.checkNotNull(r4);
            Preconditions.checkArgument(!r3.isEmpty(), "Range must not be empty, but was %s", r3);
            this.entries.add(Maps.immutableEntry(r3, r4));
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Builder<K, V> putAll(RangeMap<K, ? extends V> r5) {
            Iterator r0 = r5.asMapOfRanges().entrySet().iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            Map.Entry r1 = (Map.Entry) r0.next();
            put((Range) r1.getKey(), r1.getValue());
            goto L4
        L6:
            return this;
        }
    }

    private static class SerializedForm<K extends Comparable<?>, V> implements Serializable {
        private static final long serialVersionUID = 0;
        private final ImmutableMap<Range<K>, V> mapOfRanges;

        SerializedForm(ImmutableMap<Range<K>, V> r1) {
            this.mapOfRanges = r1;
        }

        Object createRangeMap() {
            Builder r0 = new Builder();
            UnmodifiableIterator<Map.Entry<Range<K>, V>> r1 = this.mapOfRanges.entrySet().iterator();
        L4:
            if (r1.hasNext() == false) goto L7;
            Map.Entry<Range<K>, V> r2 = r1.next();
            r0.put(r2.getKey(), r2.getValue());
            goto L4
        L7:
            return r0.build();
        }

        Object readResolve() {
            if (this.mapOfRanges.isEmpty() == false) goto L7;
            return ImmutableRangeMap.m255of();
        L7:
            return createRangeMap();
        }
    }

    static {
        EMPTY = new ImmutableRangeMap(ImmutableList.m212of(), ImmutableList.m212of());
    }

    ImmutableRangeMap(ImmutableList<Range<K>> r1, ImmutableList<V> r2) {
        this.ranges = r1;
        this.values = r2;
    }

    static /* synthetic */ ImmutableList access$000(ImmutableRangeMap r1) {
        return r1.ranges;
    }

    public static <K extends Comparable<?>, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> r6) {
        if ((r6 instanceof ImmutableRangeMap) == true) goto L5;
        Map<Range<K>, ? extends V> r0 = r6.asMapOfRanges();
        ImmutableList.Builder r1 = new ImmutableList.Builder(r0.size());
        ImmutableList.Builder r2 = new ImmutableList.Builder(r0.size());
        Iterator r3 = r0.entrySet().iterator();
    L8:
        if (r3.hasNext() == false) goto L11;
        Map.Entry r4 = (Map.Entry) r3.next();
        r1.add((Range) r4.getKey());
        r2.add(r4.getValue());
        goto L8
    L11:
        return new ImmutableRangeMap(r1.build(), r2.build());
    L5:
        return (ImmutableRangeMap) r6;
    }

    /* JADX INFO: renamed from: of */
    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> m255of() {
        return (ImmutableRangeMap<K, V>) EMPTY;
    }

    /* JADX INFO: renamed from: of */
    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> m256of(Range<K> r3, V r4) {
        return new ImmutableRangeMap(ImmutableList.m213of(r3), ImmutableList.m213of(r4));
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    public static <T, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(Function<? super T, Range<K>> r1, Function<? super T, ? extends V> r2) {
        return CollectCollectors.toImmutableRangeMap(r1, r2);
    }

    @Override // com.google.common.collect.RangeMap
    public ImmutableMap<Range<K>, V> asDescendingMapOfRanges() {
        if (this.ranges.isEmpty() == false) goto L7;
        return ImmutableMap.m231of();
    L7:
        return new ImmutableSortedMap(new RegularImmutableSortedSet(this.ranges.reverse(), Range.rangeLexOrdering().reverse()), this.values.reverse());
    }

    @Override // com.google.common.collect.RangeMap
    public /* bridge */ /* synthetic */ Map asDescendingMapOfRanges() {
        return asDescendingMapOfRanges();
    }

    @Override // com.google.common.collect.RangeMap
    public ImmutableMap<Range<K>, V> asMapOfRanges() {
        if (this.ranges.isEmpty() == false) goto L7;
        return ImmutableMap.m231of();
    L7:
        return new ImmutableSortedMap(new RegularImmutableSortedSet(this.ranges, Range.rangeLexOrdering()), this.values);
    }

    @Override // com.google.common.collect.RangeMap
    public /* bridge */ /* synthetic */ Map asMapOfRanges() {
        return asMapOfRanges();
    }

    @Override // com.google.common.collect.RangeMap
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.RangeMap
    public boolean equals(@CheckForNull Object r4) {
        if ((r4 instanceof RangeMap) == true) goto L5;
        return false;
    L5:
        return asMapOfRanges().equals(((RangeMap) r4).asMapOfRanges());
    }

    @Override // com.google.common.collect.RangeMap
    @CheckForNull
    public V get(K r6) {
        int r0 = SortedLists.binarySearch(this.ranges, new ImmutableRangeMap$$ExternalSyntheticLambda1(), Cut.belowValue(r6), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        if (r0 != (-1)) goto L6;
        return null;
    L6:
        if (this.ranges.get(r0).contains(r6) == true) goto L8;
        return null;
    L8:
        return this.values.get(r0);
    }

    @Override // com.google.common.collect.RangeMap
    @CheckForNull
    public Map.Entry<Range<K>, V> getEntry(K r6) {
        int r0 = SortedLists.binarySearch(this.ranges, new ImmutableRangeMap$$ExternalSyntheticLambda1(), Cut.belowValue(r6), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        if (r0 != (-1)) goto L5;
        return null;
    L5:
        Range<K> r1 = this.ranges.get(r0);
        if (r1.contains(r6) == true) goto L8;
        return null;
    L8:
        return Maps.immutableEntry(r1, this.values.get(r0));
    }

    @Override // com.google.common.collect.RangeMap
    public int hashCode() {
        return asMapOfRanges().hashCode();
    }

    @Override // com.google.common.collect.RangeMap
    @Deprecated
    public final void put(Range<K> r2, V r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.RangeMap
    @Deprecated
    public final void putAll(RangeMap<K, ? extends V> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.RangeMap
    @Deprecated
    public final void putCoalescing(Range<K> r2, V r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.RangeMap
    @Deprecated
    public final void remove(Range<K> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.RangeMap
    public Range<K> span() {
        if (this.ranges.isEmpty() == true) goto L7;
        return Range.create(this.ranges.get(0).lowerBound, this.ranges.get(this.ranges.size() - 1).upperBound);
    L7:
        throw new NoSuchElementException();
    }

    @Override // com.google.common.collect.RangeMap
    public ImmutableRangeMap<K, V> subRangeMap(final Range<K> r12) {
        if (((Range) Preconditions.checkNotNull(r12)).isEmpty() == false) goto L7;
        return m255of();
    L7:
        if (this.ranges.isEmpty() == true) goto L18;
        if (r12.encloses(span()) == true) goto L19;
        final int r0 = SortedLists.binarySearch(this.ranges, new ImmutableRangeMap$$ExternalSyntheticLambda0(), r12.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        int r1 = SortedLists.binarySearch(this.ranges, new ImmutableRangeMap$$ExternalSyntheticLambda1(), r12.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        if (r0 >= r1) goto L14;
        final int r3 = r1 - r0;
        final ImmutableList<Range<K>> r4 = new C06371(this, r3, r0, r12);
        final ImmutableList<V> r8 = this.values.subList(r0, r1);
        return (ImmutableRangeMap<K, V>) new C06382(this, r4, r8, r12, this);
    L14:
        return m255of();
    L19:
        return this;
    L18:
        return this;
    }

    @Override // com.google.common.collect.RangeMap
    public /* bridge */ /* synthetic */ RangeMap subRangeMap(Range r1) {
        return subRangeMap(r1);
    }

    @Override // com.google.common.collect.RangeMap
    public String toString() {
        return asMapOfRanges().toString();
    }

    Object writeReplace() {
        return new SerializedForm(asMapOfRanges());
    }
}
