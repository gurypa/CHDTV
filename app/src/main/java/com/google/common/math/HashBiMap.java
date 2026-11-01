package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class HashBiMap<K, V> extends AbstractMap<K, V> implements BiMap<K, V>, Serializable {
    private static final int ABSENT = -1;
    private static final int ENDPOINT = -2;

    @LazyInit
    private transient Set<Map.Entry<K, V>> entrySet;
    private transient int firstInInsertionOrder;
    private transient int[] hashTableKToV;
    private transient int[] hashTableVToK;

    @CheckForNull
    @LazyInit
    private transient BiMap<V, K> inverse;

    @LazyInit
    private transient Set<K> keySet;
    transient K[] keys;
    private transient int lastInInsertionOrder;
    transient int modCount;
    private transient int[] nextInBucketKToV;
    private transient int[] nextInBucketVToK;
    private transient int[] nextInInsertionOrder;
    private transient int[] prevInInsertionOrder;
    transient int size;

    @LazyInit
    private transient Set<V> valueSet;
    transient V[] values;

    final class EntryForKey extends AbstractMapEntry<K, V> {
        int index;

        @ParametricNullness
        final K key;
        final /* synthetic */ HashBiMap this$0;

        EntryForKey(HashBiMap r2, int r3) {
            this.this$0 = r2;
            this.key = (K) NullnessCasts.uncheckedCastNullableTToT(r2.keys[r3]);
            this.index = r3;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public V getValue() {
            updateIndex();
            if (this.index != (-1)) goto L6;
            return (V) NullnessCasts.unsafeNull();
        L6:
            return (V) NullnessCasts.uncheckedCastNullableTToT(this.this$0.values[this.index]);
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public V setValue(@ParametricNullness V r5) {
            updateIndex();
            if (this.index != (-1)) goto L6;
            this.this$0.put(this.key, r5);
            return (V) NullnessCasts.unsafeNull();
        L6:
            V r0 = (V) NullnessCasts.uncheckedCastNullableTToT(this.this$0.values[this.index]);
            if (Objects.equal(r0, r5) == false) goto L9;
            return r5;
        L9:
            HashBiMap.access$200(this.this$0, this.index, r5, false);
            return r0;
        }

        void updateIndex() {
            if (this.index != (-1)) goto L5;
        L8:
            this.index = this.this$0.findEntryByKey(this.key);
            return;
        L5:
            if (this.index > this.this$0.size) goto L8;
            if (Objects.equal(this.this$0.keys[this.index], this.key) == false) goto L8;
        }
    }

    static final class EntryForValue<K, V> extends AbstractMapEntry<V, K> {
        final HashBiMap<K, V> biMap;
        int index;

        @ParametricNullness
        final V value;

        EntryForValue(HashBiMap<K, V> r2, int r3) {
            this.biMap = r2;
            this.value = (V) NullnessCasts.uncheckedCastNullableTToT(r2.values[r3]);
            this.index = r3;
        }

        private void updateIndex() {
            if (this.index != (-1)) goto L5;
        L8:
            this.index = this.biMap.findEntryByValue(this.value);
            return;
        L5:
            if (this.index > this.biMap.size) goto L8;
            if (Objects.equal(this.value, this.biMap.values[this.index]) == false) goto L8;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public V getKey() {
            return this.value;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public K getValue() {
            updateIndex();
            if (this.index != (-1)) goto L6;
            return (K) NullnessCasts.unsafeNull();
        L6:
            return (K) NullnessCasts.uncheckedCastNullableTToT(this.biMap.keys[this.index]);
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public K setValue(@ParametricNullness K r5) {
            updateIndex();
            if (this.index != (-1)) goto L6;
            this.biMap.putInverse(this.value, r5, false);
            return (K) NullnessCasts.unsafeNull();
        L6:
            K r0 = (K) NullnessCasts.uncheckedCastNullableTToT(this.biMap.keys[this.index]);
            if (Objects.equal(r0, r5) == false) goto L9;
            return r5;
        L9:
            HashBiMap.access$400(this.biMap, this.index, r5, false);
            return r0;
        }
    }

    final class EntrySet extends View<K, V, Map.Entry<K, V>> {
        final /* synthetic */ HashBiMap this$0;

        EntrySet(HashBiMap r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r7) {
            if ((r7 instanceof Map.Entry) == false) goto L10;
            Map.Entry r0 = (Map.Entry) r7;
            Object r2 = r0.getKey();
            Object r3 = r0.getValue();
            int r4 = this.this$0.findEntryByKey(r2);
            if (r4 != (-1)) goto L7;
            return false;
        L7:
            if (Objects.equal(r3, this.this$0.values[r4]) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        @Override // com.google.common.collect.HashBiMap.View
        /* bridge */ /* synthetic */ Object forEntry(int r1) {
            return forEntry(r1);
        }

        @Override // com.google.common.collect.HashBiMap.View
        Map.Entry<K, V> forEntry(int r3) {
            return new EntryForKey(this.this$0, r3);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r7) {
            if ((r7 instanceof Map.Entry) == false) goto L10;
            Map.Entry r0 = (Map.Entry) r7;
            Object r1 = r0.getKey();
            Object r2 = r0.getValue();
            int r3 = Hashing.smearedHash(r1);
            int r4 = this.this$0.findEntryByKey(r1, r3);
            if (r4 != (-1)) goto L7;
            return false;
        L7:
            if (Objects.equal(r2, this.this$0.values[r4]) == false) goto L13;
            this.this$0.removeEntryKeyHashKnown(r4, r3);
            return true;
        L13:
            return false;
        L10:
            return false;
        }
    }

    static class Inverse<K, V> extends AbstractMap<V, K> implements BiMap<V, K>, Serializable {
        private final HashBiMap<K, V> forward;
        private transient Set<Map.Entry<V, K>> inverseEntrySet;

        Inverse(HashBiMap<K, V> r1) {
            this.forward = r1;
        }

        private void readObject(ObjectInputStream r2) throws ClassNotFoundException, IOException {
            r2.defaultReadObject();
            HashBiMap.access$302(this.forward, this);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            this.forward.clear();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            return this.forward.containsValue(r2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(@CheckForNull Object r2) {
            return this.forward.containsKey(r2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<V, K>> entrySet() {
            Set<Map.Entry<V, K>> r0 = this.inverseEntrySet;
            if (r0 != null) goto L6;
            InverseEntrySet r1 = new InverseEntrySet(this.forward);
            this.inverseEntrySet = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // com.google.common.collect.BiMap
        @CheckForNull
        public K forcePut(@ParametricNullness V r3, @ParametricNullness K r4) {
            return this.forward.putInverse(r3, r4, true);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public K get(@CheckForNull Object r2) {
            return this.forward.getInverse(r2);
        }

        @Override // com.google.common.collect.BiMap
        public BiMap<K, V> inverse() {
            return this.forward;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<V> keySet() {
            return this.forward.values();
        }

        @Override // java.util.AbstractMap, java.util.Map, com.google.common.collect.BiMap
        @CheckForNull
        public K put(@ParametricNullness V r3, @ParametricNullness K r4) {
            return this.forward.putInverse(r3, r4, false);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public K remove(@CheckForNull Object r2) {
            return this.forward.removeInverse(r2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.forward.size;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ Collection values() {
            return values();
        }

        @Override // java.util.AbstractMap, java.util.Map, com.google.common.collect.BiMap
        public Set<K> values() {
            return this.forward.keySet();
        }
    }

    static class InverseEntrySet<K, V> extends View<K, V, Map.Entry<V, K>> {
        InverseEntrySet(HashBiMap<K, V> r1) {
            super(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r7) {
            if ((r7 instanceof Map.Entry) == false) goto L10;
            Map.Entry r0 = (Map.Entry) r7;
            Object r2 = r0.getKey();
            Object r3 = r0.getValue();
            int r4 = this.biMap.findEntryByValue(r2);
            if (r4 != (-1)) goto L7;
            return false;
        L7:
            if (Objects.equal(this.biMap.keys[r4], r3) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        @Override // com.google.common.collect.HashBiMap.View
        /* bridge */ /* synthetic */ Object forEntry(int r1) {
            return forEntry(r1);
        }

        @Override // com.google.common.collect.HashBiMap.View
        Map.Entry<V, K> forEntry(int r3) {
            return new EntryForValue(this.biMap, r3);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r7) {
            if ((r7 instanceof Map.Entry) == false) goto L10;
            Map.Entry r0 = (Map.Entry) r7;
            Object r1 = r0.getKey();
            Object r2 = r0.getValue();
            int r3 = Hashing.smearedHash(r1);
            int r4 = this.biMap.findEntryByValue(r1, r3);
            if (r4 != (-1)) goto L7;
            return false;
        L7:
            if (Objects.equal(this.biMap.keys[r4], r2) == false) goto L13;
            this.biMap.removeEntryValueHashKnown(r4, r3);
            return true;
        L13:
            return false;
        L10:
            return false;
        }
    }

    final class KeySet extends View<K, V, K> {
        final /* synthetic */ HashBiMap this$0;

        KeySet(HashBiMap r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r2) {
            return this.this$0.containsKey(r2);
        }

        @Override // com.google.common.collect.HashBiMap.View
        @ParametricNullness
        K forEntry(int r2) {
            return (K) NullnessCasts.uncheckedCastNullableTToT(this.this$0.keys[r2]);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r4) {
            int r0 = Hashing.smearedHash(r4);
            int r1 = this.this$0.findEntryByKey(r4, r0);
            if (r1 == (-1)) goto L6;
            this.this$0.removeEntryKeyHashKnown(r1, r0);
            return true;
        L6:
            return false;
        }
    }

    final class ValueSet extends View<K, V, V> {
        final /* synthetic */ HashBiMap this$0;

        ValueSet(HashBiMap r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r2) {
            return this.this$0.containsValue(r2);
        }

        @Override // com.google.common.collect.HashBiMap.View
        @ParametricNullness
        V forEntry(int r2) {
            return (V) NullnessCasts.uncheckedCastNullableTToT(this.this$0.values[r2]);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r4) {
            int r0 = Hashing.smearedHash(r4);
            int r1 = this.this$0.findEntryByValue(r4, r0);
            if (r1 == (-1)) goto L6;
            this.this$0.removeEntryValueHashKnown(r1, r0);
            return true;
        L6:
            return false;
        }
    }

    static abstract class View<K, V, T> extends AbstractSet<T> {
        final HashBiMap<K, V> biMap;

        View(HashBiMap<K, V> r1) {
            this.biMap = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.biMap.clear();
        }

        @ParametricNullness
        abstract T forEntry(int r1);

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<T> iterator() {
            return new C06261(this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.biMap.size;
        }
    }

    private HashBiMap(int r1) {
        init(r1);
    }

    static /* synthetic */ int access$000(HashBiMap r1) {
        return r1.firstInInsertionOrder;
    }

    static /* synthetic */ int[] access$100(HashBiMap r1) {
        return r1.nextInInsertionOrder;
    }

    static /* synthetic */ void access$200(HashBiMap r0, int r1, Object r2, boolean r3) {
        r0.replaceValueInEntry(r1, r2, r3);
    }

    static /* synthetic */ BiMap access$302(HashBiMap r0, BiMap r1) {
        r0.inverse = r1;
        return r1;
    }

    static /* synthetic */ void access$400(HashBiMap r0, int r1, Object r2, boolean r3) {
        r0.replaceKeyInEntry(r1, r2, r3);
    }

    private int bucket(int r2) {
        return (this.hashTableKToV.length - 1) & r2;
    }

    public static <K, V> HashBiMap<K, V> create() {
        return create(16);
    }

    public static <K, V> HashBiMap<K, V> create(int r1) {
        return new HashBiMap(r1);
    }

    public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> r1) {
        HashBiMap<K, V> r0 = create(r1.size());
        r0.putAll(r1);
        return r0;
    }

    private static int[] createFilledWithAbsent(int r2) {
        int[] r0 = new int[r2];
        Arrays.fill(r0, -1);
        return r0;
    }

    private void deleteFromTableKToV(int r7, int r8) {
        if (r7 == (-1)) goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkArgument(r1);
        int r2 = bucket(r8);
        if (this.hashTableKToV[r2] != r7) goto L10;
        this.hashTableKToV[r2] = this.nextInBucketKToV[r7];
        this.nextInBucketKToV[r7] = -1;
        return;
    L10:
        int r3 = this.hashTableKToV[r2];
        int r4 = this.nextInBucketKToV[r3];
    L11:
        if (r4 == (-1)) goto L17;
        if (r4 == r7) goto L13;
        r3 = r4;
        r4 = this.nextInBucketKToV[r4];
        goto L11
    L13:
        this.nextInBucketKToV[r3] = this.nextInBucketKToV[r7];
        this.nextInBucketKToV[r7] = -1;
        return;
    L17:
        throw new AssertionError("Expected to find entry with key " + this.keys[r7]);
    L5:
        r1 = false;
        goto L6
    }

    private void deleteFromTableVToK(int r7, int r8) {
        if (r7 == (-1)) goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkArgument(r1);
        int r2 = bucket(r8);
        if (this.hashTableVToK[r2] != r7) goto L10;
        this.hashTableVToK[r2] = this.nextInBucketVToK[r7];
        this.nextInBucketVToK[r7] = -1;
        return;
    L10:
        int r3 = this.hashTableVToK[r2];
        int r4 = this.nextInBucketVToK[r3];
    L11:
        if (r4 == (-1)) goto L17;
        if (r4 == r7) goto L13;
        r3 = r4;
        r4 = this.nextInBucketVToK[r4];
        goto L11
    L13:
        this.nextInBucketVToK[r3] = this.nextInBucketVToK[r7];
        this.nextInBucketVToK[r7] = -1;
        return;
    L17:
        throw new AssertionError("Expected to find entry with value " + this.values[r7]);
    L5:
        r1 = false;
        goto L6
    }

    private void ensureCapacity(int r9) {
        if (this.nextInBucketKToV.length >= r9) goto L6;
        int r1 = ImmutableCollection.Builder.expandedCapacity(this.nextInBucketKToV.length, r9);
        this.keys = (K[]) Arrays.copyOf(this.keys, r1);
        this.values = (V[]) Arrays.copyOf(this.values, r1);
        this.nextInBucketKToV = expandAndFillWithAbsent(this.nextInBucketKToV, r1);
        this.nextInBucketVToK = expandAndFillWithAbsent(this.nextInBucketVToK, r1);
        this.prevInInsertionOrder = expandAndFillWithAbsent(this.prevInInsertionOrder, r1);
        this.nextInInsertionOrder = expandAndFillWithAbsent(this.nextInInsertionOrder, r1);
    L6:
        if (this.hashTableKToV.length >= r9) goto L11;
        int r0 = Hashing.closedTableSize(r9, 1.0d);
        this.hashTableKToV = createFilledWithAbsent(r0);
        this.hashTableVToK = createFilledWithAbsent(r0);
        int r2 = 0;
    L9:
        if (r2 >= this.size) goto L13;
        int r3 = bucket(Hashing.smearedHash(this.keys[r2]));
        this.nextInBucketKToV[r2] = this.hashTableKToV[r3];
        this.hashTableKToV[r3] = r2;
        int r5 = bucket(Hashing.smearedHash(this.values[r2]));
        this.nextInBucketVToK[r2] = this.hashTableVToK[r5];
        this.hashTableVToK[r5] = r2;
        r2 = r2 + 1;
        goto L9
    L13:
        return;
    }

    private static int[] expandAndFillWithAbsent(int[] r3, int r4) {
        int r0 = r3.length;
        int[] r1 = Arrays.copyOf(r3, r4);
        Arrays.fill(r1, r0, r4, -1);
        return r1;
    }

    private void insertIntoTableKToV(int r4, int r5) {
        if (r4 == (-1)) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        int r1 = bucket(r5);
        this.nextInBucketKToV[r4] = this.hashTableKToV[r1];
        this.hashTableKToV[r1] = r4;
        return;
    L5:
        r0 = false;
        goto L6
    }

    private void insertIntoTableVToK(int r4, int r5) {
        if (r4 == (-1)) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        int r1 = bucket(r5);
        this.nextInBucketVToK[r4] = this.hashTableVToK[r1];
        this.hashTableVToK[r1] = r4;
        return;
    L5:
        r0 = false;
        goto L6
    }

    private void moveEntryToIndex(int r13, int r14) {
        if (r13 != r14) goto L4;
        return;
    L4:
        int r0 = this.prevInInsertionOrder[r13];
        int r1 = this.nextInInsertionOrder[r13];
        setSucceeds(r0, r14);
        setSucceeds(r14, r1);
        K r2 = this.keys[r13];
        V r3 = this.values[r13];
        this.keys[r14] = r2;
        this.values[r14] = r3;
        int r5 = bucket(Hashing.smearedHash(r2));
        if (this.hashTableKToV[r5] != r13) goto L7;
        this.hashTableKToV[r5] = r14;
    L10:
        this.nextInBucketKToV[r14] = this.nextInBucketKToV[r13];
        this.nextInBucketKToV[r13] = -1;
        int r10 = bucket(Hashing.smearedHash(r3));
        if (this.hashTableVToK[r10] != r13) goto L13;
        this.hashTableVToK[r10] = r14;
    L16:
        this.nextInBucketVToK[r14] = this.nextInBucketVToK[r13];
        this.nextInBucketVToK[r13] = -1;
        return;
    L13:
        int r6 = this.hashTableVToK[r10];
        int r7 = this.nextInBucketVToK[r6];
    L14:
        if (r7 == r13) goto L15;
        r6 = r7;
        r7 = this.nextInBucketVToK[r7];
        goto L14
    L15:
        this.nextInBucketVToK[r6] = r14;
        goto L16
    L7:
        int r8 = this.hashTableKToV[r5];
        int r9 = this.nextInBucketKToV[r8];
    L8:
        if (r9 == r13) goto L9;
        r8 = r9;
        r9 = this.nextInBucketKToV[r9];
        goto L8
    L9:
        this.nextInBucketKToV[r8] = r14;
        goto L10
    }

    private void readObject(ObjectInputStream r3) throws IOException, ClassNotFoundException {
        r3.defaultReadObject();
        int r0 = Serialization.readCount(r3);
        init(16);
        Serialization.populateMap(this, r3, r0);
    }

    private void removeEntry(int r7, int r8, int r9) {
        if (r7 == (-1)) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        deleteFromTableKToV(r7, r8);
        deleteFromTableVToK(r7, r9);
        setSucceeds(this.prevInInsertionOrder[r7], this.nextInInsertionOrder[r7]);
        moveEntryToIndex(this.size - 1, r7);
        this.keys[this.size - 1] = null;
        this.values[this.size - 1] = null;
        this.size--;
        this.modCount++;
        return;
    L5:
        r0 = false;
        goto L6
    }

    private void replaceKeyInEntry(int r8, @ParametricNullness K r9, boolean r10) {
        if (r8 == (-1)) goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkArgument(r1);
        int r2 = Hashing.smearedHash(r9);
        int r3 = findEntryByKey(r9, r2);
        int r4 = this.lastInInsertionOrder;
        int r5 = -2;
        if (r3 == (-1)) goto L14;
        if (r10 == false) goto L13;
        r4 = this.prevInInsertionOrder[r3];
        r5 = this.nextInInsertionOrder[r3];
        removeEntryKeyHashKnown(r3, r2);
        if (r8 != this.size) goto L14;
        r8 = r3;
        goto L14
    L13:
        throw new IllegalArgumentException("Key already present in map: " + r9);
    L14:
        if (r4 != r8) goto L17;
        r4 = this.prevInInsertionOrder[r8];
    L19:
        if (r5 != r8) goto L22;
        r5 = this.nextInInsertionOrder[r8];
    L24:
        setSucceeds(this.prevInInsertionOrder[r8], this.nextInInsertionOrder[r8]);
        deleteFromTableKToV(r8, Hashing.smearedHash(this.keys[r8]));
        this.keys[r8] = r9;
        insertIntoTableKToV(r8, Hashing.smearedHash(r9));
        setSucceeds(r4, r8);
        setSucceeds(r8, r5);
        return;
    L22:
        if (r5 != this.size) goto L24;
        r5 = r3;
        goto L24
    L17:
        if (r4 != this.size) goto L19;
        r4 = r3;
        goto L19
    L5:
        r1 = false;
        goto L6
    }

    private void replaceValueInEntry(int r6, @ParametricNullness V r7, boolean r8) {
        if (r6 == (-1)) goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkArgument(r1);
        int r2 = Hashing.smearedHash(r7);
        int r3 = findEntryByValue(r7, r2);
        if (r3 == (-1)) goto L14;
        if (r8 == false) goto L13;
        removeEntryValueHashKnown(r3, r2);
        if (r6 != this.size) goto L14;
        r6 = r3;
        goto L14
    L13:
        throw new IllegalArgumentException("Value already present in map: " + r7);
    L14:
        deleteFromTableVToK(r6, Hashing.smearedHash(this.values[r6]));
        this.values[r6] = r7;
        insertIntoTableVToK(r6, r2);
        return;
    L5:
        r1 = false;
        goto L6
    }

    private void setSucceeds(int r3, int r4) {
        if (r3 != (-2)) goto L5;
        this.firstInInsertionOrder = r4;
    L6:
        if (r4 != (-2)) goto L8;
        this.lastInInsertionOrder = r3;
        return;
    L8:
        this.prevInInsertionOrder[r4] = r3;
        return;
    L5:
        this.nextInInsertionOrder[r3] = r4;
        goto L6
    }

    private void writeObject(ObjectOutputStream r1) throws IOException {
        r1.defaultWriteObject();
        Serialization.writeMap(this, r1);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        Arrays.fill(this.keys, 0, this.size, null);
        Arrays.fill(this.values, 0, this.size, null);
        Arrays.fill(this.hashTableKToV, -1);
        Arrays.fill(this.hashTableVToK, -1);
        Arrays.fill(this.nextInBucketKToV, 0, this.size, -1);
        Arrays.fill(this.nextInBucketVToK, 0, this.size, -1);
        Arrays.fill(this.prevInInsertionOrder, 0, this.size, -1);
        Arrays.fill(this.nextInInsertionOrder, 0, this.size, -1);
        this.size = 0;
        this.firstInInsertionOrder = -2;
        this.lastInInsertionOrder = -2;
        this.modCount++;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(@CheckForNull Object r3) {
        if (findEntryByKey(r3) == (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(@CheckForNull Object r3) {
        if (findEntryByValue(r3) == (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> r0 = this.entrySet;
        if (r0 != null) goto L6;
        EntrySet r1 = new EntrySet(this);
        this.entrySet = r1;
        return r1;
    L6:
        return r0;
    }

    int findEntry(@CheckForNull Object r3, int r4, int[] r5, int[] r6, Object[] r7) {
        int r0 = r5[bucket(r4)];
    L4:
        if (r0 == (-1)) goto L9;
        if (Objects.equal(r7[r0], r3) == true) goto L7;
        r0 = r6[r0];
        goto L4
    L7:
        return r0;
    L9:
        return -1;
    }

    int findEntryByKey(@CheckForNull Object r2) {
        return findEntryByKey(r2, Hashing.smearedHash(r2));
    }

    int findEntryByKey(@CheckForNull Object r7, int r8) {
        return findEntry(r7, r8, this.hashTableKToV, this.nextInBucketKToV, this.keys);
    }

    int findEntryByValue(@CheckForNull Object r2) {
        return findEntryByValue(r2, Hashing.smearedHash(r2));
    }

    int findEntryByValue(@CheckForNull Object r7, int r8) {
        return findEntry(r7, r8, this.hashTableVToK, this.nextInBucketVToK, this.values);
    }

    @Override // com.google.common.collect.BiMap
    @CheckForNull
    public V forcePut(@ParametricNullness K r2, @ParametricNullness V r3) {
        return put(r2, r3, true);
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CheckForNull
    public V get(@CheckForNull Object r3) {
        int r0 = findEntryByKey(r3);
        if (r0 != (-1)) goto L6;
        return null;
    L6:
        return this.values[r0];
    }

    @CheckForNull
    K getInverse(@CheckForNull Object r3) {
        int r0 = findEntryByValue(r3);
        if (r0 != (-1)) goto L6;
        return null;
    L6:
        return this.keys[r0];
    }

    void init(int r3) {
        CollectPreconditions.checkNonnegative(r3, "expectedSize");
        int r0 = Hashing.closedTableSize(r3, 1.0d);
        this.size = 0;
        this.keys = (K[]) new Object[r3];
        this.values = (V[]) new Object[r3];
        this.hashTableKToV = createFilledWithAbsent(r0);
        this.hashTableVToK = createFilledWithAbsent(r0);
        this.nextInBucketKToV = createFilledWithAbsent(r3);
        this.nextInBucketVToK = createFilledWithAbsent(r3);
        this.firstInInsertionOrder = -2;
        this.lastInInsertionOrder = -2;
        this.prevInInsertionOrder = createFilledWithAbsent(r3);
        this.nextInInsertionOrder = createFilledWithAbsent(r3);
    }

    @Override // com.google.common.collect.BiMap
    public BiMap<V, K> inverse() {
        BiMap<V, K> r0 = this.inverse;
        if (r0 != null) goto L6;
        Inverse r1 = new Inverse(this);
        this.inverse = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> r0 = this.keySet;
        if (r0 != null) goto L6;
        KeySet r1 = new KeySet(this);
        this.keySet = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // java.util.AbstractMap, java.util.Map, com.google.common.collect.BiMap
    @CheckForNull
    public V put(@ParametricNullness K r2, @ParametricNullness V r3) {
        return put(r2, r3, false);
    }

    @CheckForNull
    V put(@ParametricNullness K r8, @ParametricNullness V r9, boolean r10) {
        int r0 = Hashing.smearedHash(r8);
        int r1 = findEntryByKey(r8, r0);
        if (r1 == (-1)) goto L9;
        V r2 = this.values[r1];
        if (Objects.equal(r2, r9) == false) goto L7;
        return r9;
    L7:
        replaceValueInEntry(r1, r9, r10);
        return r2;
    L9:
        int r3 = Hashing.smearedHash(r9);
        int r4 = findEntryByValue(r9, r3);
        if (r10 == false) goto L13;
        if (r4 == (-1)) goto L17;
        removeEntryValueHashKnown(r4, r3);
    L17:
        ensureCapacity(this.size + 1);
        this.keys[this.size] = r8;
        this.values[this.size] = r9;
        insertIntoTableKToV(this.size, r0);
        insertIntoTableVToK(this.size, r3);
        setSucceeds(this.lastInInsertionOrder, this.size);
        setSucceeds(this.size, -2);
        this.size++;
        this.modCount++;
        return null;
    L13:
        if (r4 != (-1)) goto L15;
        boolean r5 = true;
    L16:
        Preconditions.checkArgument(r5, "Value already present: %s", r9);
        goto L17
    L15:
        r5 = false;
        goto L16
    }

    @CheckForNull
    K putInverse(@ParametricNullness V r9, @ParametricNullness K r10, boolean r11) {
        int r0 = Hashing.smearedHash(r9);
        int r1 = findEntryByValue(r9, r0);
        if (r1 == (-1)) goto L9;
        K r2 = this.keys[r1];
        if (Objects.equal(r2, r10) == false) goto L7;
        return r10;
    L7:
        replaceKeyInEntry(r1, r10, r11);
        return r2;
    L9:
        int r3 = this.lastInInsertionOrder;
        int r4 = Hashing.smearedHash(r10);
        int r5 = findEntryByKey(r10, r4);
        if (r11 == false) goto L13;
        if (r5 == (-1)) goto L17;
        r3 = this.prevInInsertionOrder[r5];
        removeEntryKeyHashKnown(r5, r4);
    L17:
        ensureCapacity(this.size + 1);
        this.keys[this.size] = r10;
        this.values[this.size] = r9;
        insertIntoTableKToV(this.size, r4);
        insertIntoTableVToK(this.size, r0);
        if (r3 != (-2)) goto L20;
        int r6 = this.firstInInsertionOrder;
    L21:
        setSucceeds(r3, this.size);
        setSucceeds(this.size, r6);
        this.size++;
        this.modCount++;
        return null;
    L20:
        r6 = this.nextInInsertionOrder[r3];
        goto L21
    L13:
        if (r5 != (-1)) goto L15;
        boolean r7 = true;
    L16:
        Preconditions.checkArgument(r7, "Key already present: %s", r10);
        goto L17
    L15:
        r7 = false;
        goto L16
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CheckForNull
    public V remove(@CheckForNull Object r4) {
        int r0 = Hashing.smearedHash(r4);
        int r1 = findEntryByKey(r4, r0);
        if (r1 != (-1)) goto L6;
        return null;
    L6:
        V r2 = this.values[r1];
        removeEntryKeyHashKnown(r1, r0);
        return r2;
    }

    void removeEntry(int r2) {
        removeEntryKeyHashKnown(r2, Hashing.smearedHash(this.keys[r2]));
    }

    void removeEntryKeyHashKnown(int r2, int r3) {
        removeEntry(r2, r3, Hashing.smearedHash(this.values[r2]));
    }

    void removeEntryValueHashKnown(int r2, int r3) {
        removeEntry(r2, Hashing.smearedHash(this.keys[r2]), r3);
    }

    @CheckForNull
    K removeInverse(@CheckForNull Object r4) {
        int r0 = Hashing.smearedHash(r4);
        int r1 = findEntryByValue(r4, r0);
        if (r1 != (-1)) goto L6;
        return null;
    L6:
        K r2 = this.keys[r1];
        removeEntryValueHashKnown(r1, r0);
        return r2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public /* bridge */ /* synthetic */ Collection values() {
        return values();
    }

    @Override // java.util.AbstractMap, java.util.Map, com.google.common.collect.BiMap
    public Set<V> values() {
        Set<V> r0 = this.valueSet;
        if (r0 != null) goto L6;
        ValueSet r1 = new ValueSet(this);
        this.valueSet = r1;
        return r1;
    L6:
        return r0;
    }
}
