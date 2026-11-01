package com.google.common.collect;

import com.arthenica.ffmpegkit.MediaInformation;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.DoNotMock;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ImmutableMultimap<K, V> extends BaseImmutableMultimap<K, V> implements Serializable {
    private static final long serialVersionUID = 0;
    final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
    final transient int size;

    @DoNotMock
    public static class Builder<K, V> {

        @CheckForNull
        Map<K, ImmutableCollection.Builder<V>> builderMap;
        int expectedValuesPerKey;

        @CheckForNull
        Comparator<? super K> keyComparator;

        @CheckForNull
        Comparator<? super V> valueComparator;

        public Builder() {
            this.expectedValuesPerKey = 4;
        }

        Builder(int r2) {
            this.expectedValuesPerKey = 4;
            if (r2 <= 0) goto L6;
            this.builderMap = Platform.preservesInsertionOrderOnPutsMapWithExpectedSize(r2);
            return;
        }

        public ImmutableMultimap<K, V> build() {
            if (this.builderMap == null) goto L5;
            Collection r0 = this.builderMap.entrySet();
            if (this.keyComparator == null) goto L10;
            r0 = Ordering.from(this.keyComparator).onKeys().immutableSortedCopy(r0);
        L10:
            return ImmutableListMultimap.fromMapBuilderEntries(r0, this.valueComparator);
        L5:
            return ImmutableListMultimap.m225of();
        }

        Builder<K, V> combine(Builder<K, V> r5) {
            if (r5.builderMap == null) goto L8;
            Iterator<Map.Entry<K, ImmutableCollection.Builder<V>>> r0 = r5.builderMap.entrySet().iterator();
        L6:
            if (r0.hasNext() == false) goto L8;
            Map.Entry<K, ImmutableCollection.Builder<V>> r1 = r0.next();
            putAll(r1.getKey(), r1.getValue().build());
        L8:
            return this;
        }

        Map<K, ImmutableCollection.Builder<V>> ensureBuilderMapNonNull() {
            Map<K, ImmutableCollection.Builder<V>> r0 = this.builderMap;
            if (r0 != null) goto L6;
            Map<K, ImmutableCollection.Builder<V>> r1 = Platform.preservesInsertionOrderOnPutsMap();
            this.builderMap = r1;
            return r1;
        L6:
            return r0;
        }

        int expectedValueCollectionSize(int r3, Iterable<?> r4) {
            if ((r4 instanceof Collection) == true) goto L5;
            return r3;
        L5:
            return Math.max(r3, ((Collection) r4).size());
        }

        public Builder<K, V> expectedValuesPerKey(int r2) {
            CollectPreconditions.checkNonnegative(r2, "expectedValuesPerKey");
            this.expectedValuesPerKey = Math.max(r2, 1);
            return this;
        }

        ImmutableCollection.Builder<V> newValueCollectionBuilderWithExpectedSize(int r2) {
            return ImmutableList.builderWithExpectedSize(r2);
        }

        public Builder<K, V> orderKeysBy(Comparator<? super K> r2) {
            this.keyComparator = (Comparator) Preconditions.checkNotNull(r2);
            return this;
        }

        public Builder<K, V> orderValuesBy(Comparator<? super V> r2) {
            this.valueComparator = (Comparator) Preconditions.checkNotNull(r2);
            return this;
        }

        public Builder<K, V> put(K r3, V r4) {
            CollectPreconditions.checkEntryNotNull(r3, r4);
            ImmutableCollection.Builder<V> r0 = ensureBuilderMapNonNull().get(r3);
            if (r0 != null) goto L5;
            r0 = newValueCollectionBuilderWithExpectedSize(this.expectedValuesPerKey);
            ensureBuilderMapNonNull().put(r3, r0);
        L5:
            r0.add(r4);
            return this;
        }

        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> r3) {
            return put(r3.getKey(), r3.getValue());
        }

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

        public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> r3) {
            Iterator<? extends Map.Entry<? extends K, ? extends V>> r0 = r3.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            put(r0.next());
            goto L4
        L6:
            return this;
        }

        public Builder<K, V> putAll(K r4, Iterable<? extends V> r5) {
            if (r4 == null) goto L14;
            Iterator<? extends V> r0 = r5.iterator();
            if (r0.hasNext() == true) goto L6;
            return this;
        L6:
            ImmutableCollection.Builder<V> r1 = ensureBuilderMapNonNull().get(r4);
            if (r1 != null) goto L10;
            r1 = newValueCollectionBuilderWithExpectedSize(expectedValueCollectionSize(this.expectedValuesPerKey, r5));
            ensureBuilderMapNonNull().put(r4, r1);
        L10:
            if (r0.hasNext() == false) goto L12;
            V r2 = r0.next();
            CollectPreconditions.checkEntryNotNull(r4, r2);
            r1.add(r2);
            goto L10
        L12:
            return this;
        L14:
            throw new NullPointerException("null key in entry: null=" + Iterables.toString(r5));
        }

        public Builder<K, V> putAll(K r2, V... r3) {
            return putAll(r2, Arrays.asList(r3));
        }
    }

    private static class EntryCollection<K, V> extends ImmutableCollection<Map.Entry<K, V>> {
        private static final long serialVersionUID = 0;
        final ImmutableMultimap<K, V> multimap;

        EntryCollection(ImmutableMultimap<K, V> r1) {
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
            return this.multimap.isPartialView();
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return this.multimap.entryIterator();
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.multimap.size();
        }

        @Override // com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    static class FieldSettersHolder {
        static final Serialization.FieldSetter<? super ImmutableMultimap<?, ?>> MAP_FIELD_SETTER = null;
        static final Serialization.FieldSetter<? super ImmutableMultimap<?, ?>> SIZE_FIELD_SETTER = null;

        static {
            MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
            SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, MediaInformation.KEY_SIZE);
        }

        FieldSettersHolder() {
        }
    }

    class Keys extends ImmutableMultiset<K> {
        final /* synthetic */ ImmutableMultimap this$0;

        Keys(ImmutableMultimap r1) {
            this.this$0 = r1;
        }

        private void readObject(ObjectInputStream r3) throws InvalidObjectException {
            throw new InvalidObjectException("Use KeysSerializedForm");
        }

        @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r2) {
            return this.this$0.containsKey(r2);
        }

        @Override // com.google.common.collect.Multiset
        public int count(@CheckForNull Object r3) {
            ImmutableCollection<V> r0 = this.this$0.map.get(r3);
            if (r0 != null) goto L6;
            return 0;
        L6:
            return r0.size();
        }

        @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
        public ImmutableSet<K> elementSet() {
            return this.this$0.keySet();
        }

        @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
        public /* bridge */ /* synthetic */ Set elementSet() {
            return elementSet();
        }

        @Override // com.google.common.collect.ImmutableMultiset
        Multiset.Entry<K> getEntry(int r4) {
            Map.Entry<K, ? extends ImmutableCollection<V>> r0 = this.this$0.map.entrySet().asList().get(r4);
            return Multisets.immutableEntry(r0.getKey(), r0.getValue().size());
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
        public int size() {
            return this.this$0.size();
        }

        @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return new KeysSerializedForm(this.this$0);
        }
    }

    private static final class KeysSerializedForm implements Serializable {
        final ImmutableMultimap<?, ?> multimap;

        KeysSerializedForm(ImmutableMultimap<?, ?> r1) {
            this.multimap = r1;
        }

        Object readResolve() {
            return this.multimap.keys();
        }
    }

    private static final class Values<K, V> extends ImmutableCollection<V> {
        private static final long serialVersionUID = 0;
        private final transient ImmutableMultimap<K, V> multimap;

        Values(ImmutableMultimap<K, V> r1) {
            this.multimap = r1;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r2) {
            return this.multimap.containsValue(r2);
        }

        @Override // com.google.common.collect.ImmutableCollection
        int copyIntoArray(Object[] r3, int r4) {
            UnmodifiableIterator<? extends ImmutableCollection<V>> r0 = this.multimap.map.values().iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            r4 = r0.next().copyIntoArray(r3, r4);
            goto L4
        L6:
            return r4;
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return true;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public UnmodifiableIterator<V> iterator() {
            return this.multimap.valueIterator();
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.multimap.size();
        }

        @Override // com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> r1, int r2) {
        this.map = r1;
        this.size = r2;
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> Builder<K, V> builderWithExpectedKeys(int r1) {
        CollectPreconditions.checkNonnegative(r1, "expectedKeys");
        return new Builder(r1);
    }

    public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> r2) {
        if ((r2 instanceof ImmutableMultimap) == false) goto L8;
        ImmutableMultimap<K, V> r0 = (ImmutableMultimap) r2;
        if (r0.isPartialView() == true) goto L8;
        return r0;
    L8:
        return ImmutableListMultimap.copyOf(r2);
    }

    public static <K, V> ImmutableMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> r1) {
        return ImmutableListMultimap.copyOf(r1);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMultimap<K, V> m242of() {
        return ImmutableListMultimap.m225of();
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMultimap<K, V> m243of(K r1, V r2) {
        return ImmutableListMultimap.m226of(r1, r2);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMultimap<K, V> m244of(K r1, V r2, K r3, V r4) {
        return ImmutableListMultimap.m227of(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMultimap<K, V> m245of(K r1, V r2, K r3, V r4, K r5, V r6) {
        return ImmutableListMultimap.m228of(r1, r2, r3, r4, r5, r6);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMultimap<K, V> m246of(K r1, V r2, K r3, V r4, K r5, V r6, K r7, V r8) {
        return ImmutableListMultimap.m229of(r1, r2, r3, r4, r5, r6, r7, r8);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMultimap<K, V> m247of(K r1, V r2, K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10) {
        return ImmutableListMultimap.m230of(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public ImmutableMap<K, Collection<V>> asMap() {
        return this.map;
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ Map asMap() {
        return asMap();
    }

    @Override // com.google.common.collect.Multimap
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean containsEntry(@CheckForNull Object r1, @CheckForNull Object r2) {
        return super.containsEntry(r1, r2);
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsKey(@CheckForNull Object r2) {
        return this.map.containsKey(r2);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public boolean containsValue(@CheckForNull Object r2) {
        if (r2 != null) goto L4;
    L6:
        return false;
    L4:
        if (super.containsValue(r2) == false) goto L6;
        return true;
    }

    @Override // com.google.common.collect.AbstractMultimap
    Map<K, Collection<V>> createAsMap() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.AbstractMultimap
    ImmutableCollection<Map.Entry<K, V>> createEntries() {
        return new EntryCollection(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    /* bridge */ /* synthetic */ Collection createEntries() {
        return createEntries();
    }

    @Override // com.google.common.collect.AbstractMultimap
    Set<K> createKeySet() {
        throw new AssertionError("unreachable");
    }

    @Override // com.google.common.collect.AbstractMultimap
    ImmutableMultiset<K> createKeys() {
        return new Keys(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    /* bridge */ /* synthetic */ Multiset createKeys() {
        return createKeys();
    }

    @Override // com.google.common.collect.AbstractMultimap
    ImmutableCollection<V> createValues() {
        return new Values(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    /* bridge */ /* synthetic */ Collection createValues() {
        return createValues();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public ImmutableCollection<Map.Entry<K, V>> entries() {
        return (ImmutableCollection) super.entries();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection entries() {
        return entries();
    }

    @Override // com.google.common.collect.AbstractMultimap
    UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
        return new C06341(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    /* bridge */ /* synthetic */ Iterator entryIterator() {
        return entryIterator();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ boolean equals(@CheckForNull Object r1) {
        return super.equals(r1);
    }

    @Override // com.google.common.collect.Multimap
    public abstract ImmutableCollection<V> get(K r1);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection get(Object r1) {
        return get(r1);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public abstract ImmutableMultimap<V, K> inverse();

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    boolean isPartialView() {
        return this.map.isPartialView();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public ImmutableSet<K> keySet() {
        return this.map.keySet();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Set keySet() {
        return keySet();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public ImmutableMultiset<K> keys() {
        return (ImmutableMultiset) super.keys();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Multiset keys() {
        return keys();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public final boolean put(K r2, V r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public final boolean putAll(Multimap<? extends K, ? extends V> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public final boolean putAll(K r2, Iterable<? extends V> r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public final boolean remove(@CheckForNull Object r2, @CheckForNull Object r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multimap
    @Deprecated
    public ImmutableCollection<V> removeAll(@CheckForNull Object r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ Collection removeAll(@CheckForNull Object r1) {
        return removeAll(r1);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public ImmutableCollection<V> replaceValues(K r2, Iterable<? extends V> r3) {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ Collection replaceValues(Object r1, Iterable r2) {
        return replaceValues(r1, r2);
    }

    @Override // com.google.common.collect.Multimap
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.AbstractMultimap
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.google.common.collect.AbstractMultimap
    UnmodifiableIterator<V> valueIterator() {
        return new C06352(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    /* bridge */ /* synthetic */ Iterator valueIterator() {
        return valueIterator();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public ImmutableCollection<V> values() {
        return (ImmutableCollection) super.values();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection values() {
        return values();
    }
}
