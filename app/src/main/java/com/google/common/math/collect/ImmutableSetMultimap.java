package com.google.common.collect;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class ImmutableSetMultimap<K, V> extends ImmutableMultimap<K, V> implements SetMultimap<K, V> {
    private static final long serialVersionUID = 0;
    private final transient ImmutableSet<V> emptySet;

    @CheckForNull
    @LazyInit
    private transient ImmutableSet<Map.Entry<K, V>> entries;

    @CheckForNull
    @LazyInit
    private transient ImmutableSetMultimap<V, K> inverse;

    public static final class Builder<K, V> extends ImmutableMultimap.Builder<K, V> {
        public Builder() {
        }

        Builder(int r1) {
            super(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap build() {
            return build();
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public ImmutableSetMultimap<K, V> build() {
            if (this.builderMap == null) goto L5;
            Collection r0 = this.builderMap.entrySet();
            if (this.keyComparator == null) goto L10;
            r0 = Ordering.from(this.keyComparator).onKeys().immutableSortedCopy(r0);
        L10:
            return ImmutableSetMultimap.fromMapBuilderEntries(r0, this.valueComparator);
        L5:
            return ImmutableSetMultimap.m266of();
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        /* bridge */ /* synthetic */ ImmutableMultimap.Builder combine(ImmutableMultimap.Builder r1) {
            return combine(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        Builder<K, V> combine(ImmutableMultimap.Builder<K, V> r1) {
            super.combine(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        int expectedValueCollectionSize(int r3, Iterable<?> r4) {
            if ((r4 instanceof Set) == true) goto L5;
            return r3;
        L5:
            return Math.max(r3, ((Set) r4).size());
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder expectedValuesPerKey(int r1) {
            return expectedValuesPerKey(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> expectedValuesPerKey(int r1) {
            super.expectedValuesPerKey(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        ImmutableCollection.Builder<V> newValueCollectionBuilderWithExpectedSize(int r3) {
            if (this.valueComparator != null) goto L6;
            return ImmutableSet.builderWithExpectedSize(r3);
        L6:
            return new ImmutableSortedSet.Builder(this.valueComparator, r3);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder orderKeysBy(Comparator r1) {
            return orderKeysBy(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> orderKeysBy(Comparator<? super K> r1) {
            super.orderKeysBy(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder orderValuesBy(Comparator r1) {
            return orderValuesBy(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> orderValuesBy(Comparator<? super V> r1) {
            super.orderValuesBy(r1);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder put(Object r1, Object r2) {
            return put(r1, r2);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder put(Map.Entry r1) {
            return put(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> put(K r1, V r2) {
            super.put(r1, r2);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> r1) {
            super.put(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder putAll(Multimap r1) {
            return putAll(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder putAll(Iterable r1) {
            return putAll(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder putAll(Object r1, Iterable r2) {
            return putAll(r1, r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder putAll(Object r1, Object[] r2) {
            return putAll(r1, r2);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> putAll(Multimap<? extends K, ? extends V> r5) {
            Iterator<Map.Entry<? extends K, Collection<? extends V>>> r0 = r5.asMap().entrySet().iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            Map.Entry<? extends K, Collection<? extends V>> r1 = r0.next();
            putAll(r1.getKey(), r1.getValue());
            goto L4
        L6:
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> r1) {
            super.putAll(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> putAll(K r1, Iterable<? extends V> r2) {
            super.putAll(r1, r2);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> putAll(K r2, V... r3) {
            return putAll(r2, Arrays.asList(r3));
        }
    }

    private static final class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>> {
        private final transient ImmutableSetMultimap<K, V> multimap;

        EntrySet(ImmutableSetMultimap<K, V> r1) {
            this.multimap = r1;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r5) {
            if ((r5 instanceof Map.Entry) == false) goto L6;
            Map.Entry r0 = (Map.Entry) r5;
            return this.multimap.containsEntry(r0.getKey(), r0.getValue());
        L6:
            return false;
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return false;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return this.multimap.entryIterator();
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.multimap.size();
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    private static final class SetFieldSettersHolder {
        static final Serialization.FieldSetter<? super ImmutableSetMultimap<?, ?>> EMPTY_SET_FIELD_SETTER = null;

        static {
            EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
        }

        private SetFieldSettersHolder() {
        }
    }

    ImmutableSetMultimap(ImmutableMap<K, ImmutableSet<V>> r2, int r3, @CheckForNull Comparator<? super V> r4) {
        super(r2, r3);
        this.emptySet = emptySet(r4);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> Builder<K, V> builderWithExpectedKeys(int r1) {
        CollectPreconditions.checkNonnegative(r1, "expectedKeys");
        return new Builder(r1);
    }

    public static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> r1) {
        return copyOf(r1, null);
    }

    private static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> r2, @CheckForNull Comparator<? super V> r3) {
        Preconditions.checkNotNull(r2);
        if (r2.isEmpty() == false) goto L8;
        if (r3 != null) goto L8;
        return m266of();
    L8:
        if ((r2 instanceof ImmutableSetMultimap) == false) goto L13;
        ImmutableSetMultimap<K, V> r0 = (ImmutableSetMultimap) r2;
        if (r0.isPartialView() == true) goto L13;
        return r0;
    L13:
        return fromMapEntries(r2.asMap().entrySet(), r3);
    }

    public static <K, V> ImmutableSetMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> r1) {
        return new Builder().putAll(r1).build();
    }

    private static <V> ImmutableSet<V> emptySet(@CheckForNull Comparator<? super V> r1) {
        if (r1 != null) goto L5;
        return ImmutableSet.m259of();
    L5:
        return ImmutableSortedSet.emptySet(r1);
    }

    public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(Function<? super T, ? extends K> r1, Function<? super T, ? extends Stream<? extends V>> r2) {
        return CollectCollectors.flatteningToImmutableSetMultimap(r1, r2);
    }

    static <K, V> ImmutableSetMultimap<K, V> fromMapBuilderEntries(Collection<? extends Map.Entry<K, ImmutableCollection.Builder<V>>> r8, @CheckForNull Comparator<? super V> r9) {
        if (r8.isEmpty() == true) goto L5;
        ImmutableMap.Builder r0 = new ImmutableMap.Builder(r8.size());
        int r1 = 0;
        Iterator<? extends Map.Entry<K, ImmutableCollection.Builder<V>>> r2 = r8.iterator();
    L8:
        if (r2.hasNext() == false) goto L14;
        Map.Entry<K, ImmutableCollection.Builder<V>> r3 = r2.next();
        K r4 = r3.getKey();
        ImmutableSet r6 = valueSet(r9, ((ImmutableSet.Builder) r3.getValue()).build());
        if (r6.isEmpty() == true) goto L8;
        r0.put(r4, r6);
        r1 = r1 + r6.size();
        goto L8
    L14:
        return new ImmutableSetMultimap(r0.buildOrThrow(), r1, r9);
    L5:
        return m266of();
    }

    static <K, V> ImmutableSetMultimap<K, V> fromMapEntries(Collection<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>> r8, @CheckForNull Comparator<? super V> r9) {
        if (r8.isEmpty() == true) goto L5;
        ImmutableMap.Builder r0 = new ImmutableMap.Builder(r8.size());
        int r1 = 0;
        Iterator<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>> r2 = r8.iterator();
    L8:
        if (r2.hasNext() == false) goto L14;
        Map.Entry<? extends K, ? extends Collection<? extends V>> r3 = r2.next();
        K r4 = r3.getKey();
        ImmutableSet r6 = valueSet(r9, r3.getValue());
        if (r6.isEmpty() == true) goto L8;
        r0.put(r4, r6);
        r1 = r1 + r6.size();
        goto L8
    L14:
        return new ImmutableSetMultimap(r0.buildOrThrow(), r1, r9);
    L5:
        return m266of();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ImmutableSetMultimap<V, K> invert() {
        Builder r0 = builder();
        UnmodifiableIterator r1 = entries().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        Map.Entry r2 = (Map.Entry) r1.next();
        r0.put(r2.getValue(), r2.getKey());
        goto L4
    L6:
        ImmutableSetMultimap<V, K> r3 = r0.build();
        r3.inverse = this;
        return r3;
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSetMultimap<K, V> m266of() {
        return EmptyImmutableSetMultimap.INSTANCE;
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSetMultimap<K, V> m267of(K r2, V r3) {
        Builder r0 = builder();
        r0.put(r2, r3);
        return r0.build();
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSetMultimap<K, V> m268of(K r2, V r3, K r4, V r5) {
        Builder r0 = builder();
        r0.put(r2, r3);
        r0.put(r4, r5);
        return r0.build();
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSetMultimap<K, V> m269of(K r2, V r3, K r4, V r5, K r6, V r7) {
        Builder r0 = builder();
        r0.put(r2, r3);
        r0.put(r4, r5);
        r0.put(r6, r7);
        return r0.build();
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSetMultimap<K, V> m270of(K r2, V r3, K r4, V r5, K r6, V r7, K r8, V r9) {
        Builder r0 = builder();
        r0.put(r2, r3);
        r0.put(r4, r5);
        r0.put(r6, r7);
        r0.put(r8, r9);
        return r0.build();
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSetMultimap<K, V> m271of(K r2, V r3, K r4, V r5, K r6, V r7, K r8, V r9, K r10, V r11) {
        Builder r0 = builder();
        r0.put(r2, r3);
        r0.put(r4, r5);
        r0.put(r6, r7);
        r0.put(r8, r9);
        r0.put(r10, r11);
        return r0.build();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream r13) throws IOException, ClassNotFoundException {
        r13.defaultReadObject();
        Comparator r0 = (Comparator) r13.readObject();
        int r1 = r13.readInt();
        if (r1 < 0) goto L25;
        ImmutableMap.Builder r2 = ImmutableMap.builder();
        int r3 = 0;
        int r4 = 0;
    L5:
        if (r4 >= r1) goto L26;
        Object r5 = Objects.requireNonNull(r13.readObject());
        int r6 = r13.readInt();
        if (r6 <= 0) goto L17;
        ImmutableSet.Builder r7 = valuesBuilder(r0);
        int r8 = 0;
    L9:
        if (r8 >= r6) goto L11;
        r7.add(Objects.requireNonNull(r13.readObject()));
        r8 = r8 + 1;
        goto L9
    L11:
        ImmutableSet r9 = r7.build();
        if (r9.size() != r6) goto L15;
        r2.put(r5, r9);
        r3 = r3 + r6;
        r4 = r4 + 1;
        goto L5
    L15:
        throw new InvalidObjectException("Duplicate key-value pairs exist for key " + r5);
    L17:
        throw new InvalidObjectException("Invalid value count " + r6);
    L26:
        ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, r2.buildOrThrow());
        ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, r3);
        SetFieldSettersHolder.EMPTY_SET_FIELD_SETTER.set(this, emptySet(r0));
        return;
    L21:
        e = move-exception;
        throw ((InvalidObjectException) new InvalidObjectException(e.getMessage()).initCause(e));
    L25:
        throw new InvalidObjectException("Invalid key count " + r1);
    }

    public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(Function<? super T, ? extends K> r1, Function<? super T, ? extends V> r2) {
        return CollectCollectors.toImmutableSetMultimap(r1, r2);
    }

    private static <V> ImmutableSet<V> valueSet(@CheckForNull Comparator<? super V> r1, Collection<? extends V> r2) {
        if (r1 != null) goto L5;
        return ImmutableSet.copyOf(r2);
    L5:
        return ImmutableSortedSet.copyOf(r1, r2);
    }

    private static <V> ImmutableSet.Builder<V> valuesBuilder(@CheckForNull Comparator<? super V> r1) {
        if (r1 != null) goto L5;
        return new ImmutableSet.Builder();
    L5:
        return new ImmutableSortedSet.Builder(r1);
    }

    private void writeObject(ObjectOutputStream r2) throws IOException {
        r2.defaultWriteObject();
        r2.writeObject(valueComparator());
        Serialization.writeMultimap(this, r2);
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ ImmutableCollection entries() {
        return entries();
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public ImmutableSet<Map.Entry<K, V>> entries() {
        ImmutableSet<Map.Entry<K, V>> r0 = this.entries;
        if (r0 != null) goto L6;
        EntrySet r1 = new EntrySet(this);
        this.entries = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection entries() {
        return entries();
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Set entries() {
        return entries();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ ImmutableCollection get(Object r1) {
        return get(r1);
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    public ImmutableSet<V> get(K r3) {
        return (ImmutableSet) MoreObjects.firstNonNull((ImmutableSet) this.map.get(r3), this.emptySet);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection get(Object r1) {
        return get(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Set get(Object r1) {
        return get(r1);
    }

    @Override // com.google.common.collect.ImmutableMultimap
    public /* bridge */ /* synthetic */ ImmutableMultimap inverse() {
        return inverse();
    }

    @Override // com.google.common.collect.ImmutableMultimap
    public ImmutableSetMultimap<V, K> inverse() {
        ImmutableSetMultimap<V, K> r0 = this.inverse;
        if (r0 != null) goto L6;
        ImmutableSetMultimap<V, K> r1 = invert();
        this.inverse = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ ImmutableCollection removeAll(@CheckForNull Object r1) {
        return removeAll(r1);
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    @Deprecated
    public final ImmutableSet<V> removeAll(@CheckForNull Object r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ Collection removeAll(@CheckForNull Object r1) {
        return removeAll(r1);
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ Set removeAll(@CheckForNull Object r1) {
        return removeAll(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ ImmutableCollection replaceValues(Object r1, Iterable r2) {
        return replaceValues(r1, r2);
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public final ImmutableSet<V> replaceValues(K r2, Iterable<? extends V> r3) {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ Collection replaceValues(Object r1, Iterable r2) {
        return replaceValues(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ Set replaceValues(Object r1, Iterable r2) {
        return replaceValues(r1, r2);
    }

    @CheckForNull
    Comparator<? super V> valueComparator() {
        if ((this.emptySet instanceof ImmutableSortedSet) == true) goto L7;
        return null;
    L7:
        return ((ImmutableSortedSet) this.emptySet).comparator();
    }
}
