package com.google.common.collect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class LinkedHashMultimap<K, V> extends LinkedHashMultimapGwtSerializationDependencies<K, V> {
    private static final int DEFAULT_KEY_CAPACITY = 16;
    private static final int DEFAULT_VALUE_SET_CAPACITY = 2;
    static final double VALUE_SET_LOAD_FACTOR = 1.0d;
    private static final long serialVersionUID = 1;
    private transient ValueEntry<K, V> multimapHeaderEntry;
    transient int valueSetCapacity;

    static final class ValueEntry<K, V> extends ImmutableEntry<K, V> implements ValueSetLink<K, V> {

        @CheckForNull
        ValueEntry<K, V> nextInValueBucket;

        @CheckForNull
        private ValueEntry<K, V> predecessorInMultimap;

        @CheckForNull
        private ValueSetLink<K, V> predecessorInValueSet;
        final int smearedValueHash;

        @CheckForNull
        private ValueEntry<K, V> successorInMultimap;

        @CheckForNull
        private ValueSetLink<K, V> successorInValueSet;

        ValueEntry(@ParametricNullness K r1, @ParametricNullness V r2, int r3, @CheckForNull ValueEntry<K, V> r4) {
            super(r1, r2);
            this.smearedValueHash = r3;
            this.nextInValueBucket = r4;
        }

        static <K, V> ValueEntry<K, V> newHeader() {
            return new ValueEntry(null, null, 0, null);
        }

        public ValueEntry<K, V> getPredecessorInMultimap() {
            return (ValueEntry) Objects.requireNonNull(this.predecessorInMultimap);
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return (ValueSetLink) Objects.requireNonNull(this.predecessorInValueSet);
        }

        public ValueEntry<K, V> getSuccessorInMultimap() {
            return (ValueEntry) Objects.requireNonNull(this.successorInMultimap);
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return (ValueSetLink) Objects.requireNonNull(this.successorInValueSet);
        }

        boolean matchesValue(@CheckForNull Object r2, int r3) {
            if (this.smearedValueHash == r3) goto L5;
        L7:
            return false;
        L5:
            if (com.google.common.base.Objects.equal(getValue(), r2) == false) goto L7;
            return true;
        }

        public void setPredecessorInMultimap(ValueEntry<K, V> r1) {
            this.predecessorInMultimap = r1;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public void setPredecessorInValueSet(ValueSetLink<K, V> r1) {
            this.predecessorInValueSet = r1;
        }

        public void setSuccessorInMultimap(ValueEntry<K, V> r1) {
            this.successorInMultimap = r1;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public void setSuccessorInValueSet(ValueSetLink<K, V> r1) {
            this.successorInValueSet = r1;
        }
    }

    final class ValueSet extends Sets.ImprovedAbstractSet<V> implements ValueSetLink<K, V> {
        private ValueSetLink<K, V> firstEntry;
        ValueEntry<K, V>[] hashTable;

        @ParametricNullness
        private final K key;
        private ValueSetLink<K, V> lastEntry;
        private int modCount;
        private int size;
        final /* synthetic */ LinkedHashMultimap this$0;

        ValueSet(@ParametricNullness LinkedHashMultimap r3, K r4, int r5) {
            this.this$0 = r3;
            this.size = 0;
            this.modCount = 0;
            this.key = r4;
            this.firstEntry = this;
            this.lastEntry = this;
            this.hashTable = new ValueEntry[Hashing.closedTableSize(r5, LinkedHashMultimap.VALUE_SET_LOAD_FACTOR)];
        }

        static /* synthetic */ ValueSetLink access$000(ValueSet r1) {
            return r1.firstEntry;
        }

        static /* synthetic */ int access$100(ValueSet r1) {
            return r1.modCount;
        }

        private int mask() {
            return this.hashTable.length - 1;
        }

        private void rehashIfNecessary() {
            if (Hashing.needsResizing(this.size, this.hashTable.length, LinkedHashMultimap.VALUE_SET_LOAD_FACTOR) == false) goto L7;
            ValueEntry<K, V>[] r0 = new ValueEntry[this.hashTable.length * 2];
            this.hashTable = r0;
            int r1 = r0.length - 1;
            ValueSetLink<K, V> r2 = this.firstEntry;
        L5:
            if (r2 == this) goto L9;
            ValueEntry<K, V> r3 = (ValueEntry) r2;
            int r4 = r3.smearedValueHash & r1;
            r3.nextInValueBucket = r0[r4];
            r0[r4] = r3;
            r2 = r2.getSuccessorInValueSet();
            goto L5
        L9:
            return;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(@ParametricNullness V r7) {
            int r0 = Hashing.smearedHash(r7);
            int r1 = mask() & r0;
            ValueEntry<K, V> r2 = this.hashTable[r1];
            ValueEntry<K, V> r3 = r2;
        L3:
            if (r3 == null) goto L9;
            if (r3.matchesValue(r7, r0) == true) goto L6;
            r3 = r3.nextInValueBucket;
            goto L3
        L6:
            return false;
        L9:
            ValueEntry<K, V> r4 = new ValueEntry(this.key, r7, r0, r2);
            LinkedHashMultimap.access$200(this.lastEntry, r4);
            LinkedHashMultimap.access$200(r4, this);
            LinkedHashMultimap.access$400(LinkedHashMultimap.access$300(this.this$0).getPredecessorInMultimap(), r4);
            LinkedHashMultimap.access$400(r4, LinkedHashMultimap.access$300(this.this$0));
            this.hashTable[r1] = r4;
            this.size++;
            this.modCount++;
            rehashIfNecessary();
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Arrays.fill(this.hashTable, null);
            this.size = 0;
            ValueSetLink<K, V> r0 = this.firstEntry;
        L3:
            if (r0 == this) goto L5;
            LinkedHashMultimap.access$600((ValueEntry) r0);
            r0 = r0.getSuccessorInValueSet();
            goto L3
        L5:
            LinkedHashMultimap.access$200(this, this);
            this.modCount++;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r4) {
            int r0 = Hashing.smearedHash(r4);
            ValueEntry<K, V> r1 = this.hashTable[mask() & r0];
        L3:
            if (r1 == null) goto L9;
            if (r1.matchesValue(r4, r0) == true) goto L6;
            r1 = r1.nextInValueBucket;
            goto L3
        L6:
            return true;
        L9:
            return false;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return this.lastEntry;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return this.firstEntry;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<V> iterator() {
            return new C06651(this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r7) {
            int r0 = Hashing.smearedHash(r7);
            int r1 = mask() & r0;
            ValueEntry<K, V> r2 = null;
            ValueEntry<K, V> r3 = this.hashTable[r1];
        L3:
            if (r3 == null) goto L12;
            if (r3.matchesValue(r7, r0) == true) goto L6;
            r2 = r3;
            r3 = r3.nextInValueBucket;
            goto L3
        L6:
            if (r2 != null) goto L8;
            this.hashTable[r1] = r3.nextInValueBucket;
        L9:
            LinkedHashMultimap.access$500(r3);
            LinkedHashMultimap.access$600(r3);
            this.size--;
            this.modCount++;
            return true;
        L8:
            r2.nextInValueBucket = r3.nextInValueBucket;
            goto L9
        L12:
            return false;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public void setPredecessorInValueSet(ValueSetLink<K, V> r1) {
            this.lastEntry = r1;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public void setSuccessorInValueSet(ValueSetLink<K, V> r1) {
            this.firstEntry = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.size;
        }
    }

    private interface ValueSetLink<K, V> {
        ValueSetLink<K, V> getPredecessorInValueSet();

        ValueSetLink<K, V> getSuccessorInValueSet();

        void setPredecessorInValueSet(ValueSetLink<K, V> r1);

        void setSuccessorInValueSet(ValueSetLink<K, V> r1);
    }

    private LinkedHashMultimap(int r3, int r4) {
        super(Platform.newLinkedHashMapWithExpectedSize(r3));
        this.valueSetCapacity = 2;
        CollectPreconditions.checkNonnegative(r4, "expectedValuesPerKey");
        this.valueSetCapacity = r4;
        this.multimapHeaderEntry = ValueEntry.newHeader();
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
    }

    static /* synthetic */ void access$200(ValueSetLink r0, ValueSetLink r1) {
        succeedsInValueSet(r0, r1);
    }

    static /* synthetic */ ValueEntry access$300(LinkedHashMultimap r1) {
        return r1.multimapHeaderEntry;
    }

    static /* synthetic */ void access$400(ValueEntry r0, ValueEntry r1) {
        succeedsInMultimap(r0, r1);
    }

    static /* synthetic */ void access$500(ValueSetLink r0) {
        deleteFromValueSet(r0);
    }

    static /* synthetic */ void access$600(ValueEntry r0) {
        deleteFromMultimap(r0);
    }

    public static <K, V> LinkedHashMultimap<K, V> create() {
        return new LinkedHashMultimap(16, 2);
    }

    public static <K, V> LinkedHashMultimap<K, V> create(int r3, int r4) {
        return new LinkedHashMultimap(Maps.capacity(r3), Maps.capacity(r4));
    }

    public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> r2) {
        LinkedHashMultimap<K, V> r0 = create(r2.keySet().size(), 2);
        r0.putAll(r2);
        return r0;
    }

    private static <K, V> void deleteFromMultimap(ValueEntry<K, V> r2) {
        succeedsInMultimap(r2.getPredecessorInMultimap(), r2.getSuccessorInMultimap());
    }

    private static <K, V> void deleteFromValueSet(ValueSetLink<K, V> r2) {
        succeedsInValueSet(r2.getPredecessorInValueSet(), r2.getSuccessorInValueSet());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream r8) throws IOException, ClassNotFoundException {
        r8.defaultReadObject();
        this.multimapHeaderEntry = ValueEntry.newHeader();
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
        this.valueSetCapacity = 2;
        int r0 = r8.readInt();
        Map r1 = Platform.newLinkedHashMapWithExpectedSize(12);
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        Object r3 = r8.readObject();
        r1.put(r3, createCollection(r3));
        r2 = r2 + 1;
        goto L3
    L5:
        int r4 = r8.readInt();
        int r5 = 0;
    L6:
        if (r5 >= r4) goto L8;
        Object r6 = r8.readObject();
        Object r7 = r8.readObject();
        ((Collection) Objects.requireNonNull((Collection) r1.get(r6))).add(r7);
        r5 = r5 + 1;
        goto L6
    L8:
        setMap(r1);
    }

    private static <K, V> void succeedsInMultimap(ValueEntry<K, V> r0, ValueEntry<K, V> r1) {
        r0.setSuccessorInMultimap(r1);
        r1.setPredecessorInMultimap(r0);
    }

    private static <K, V> void succeedsInValueSet(ValueSetLink<K, V> r0, ValueSetLink<K, V> r1) {
        r0.setSuccessorInValueSet(r1);
        r1.setPredecessorInValueSet(r0);
    }

    private void writeObject(ObjectOutputStream r4) throws IOException {
        r4.defaultWriteObject();
        r4.writeInt(keySet().size());
        Iterator<K> r0 = keySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r4.writeObject(r0.next());
        goto L4
    L6:
        r4.writeInt(size());
        Iterator<Map.Entry<K, V>> r1 = entries().iterator();
    L8:
        if (r1.hasNext() == false) goto L10;
        Map.Entry<K, V> r2 = r1.next();
        r4.writeObject(r2.getKey());
        r4.writeObject(r2.getValue());
        goto L8
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ Map asMap() {
        return super.asMap();
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public void clear() {
        super.clear();
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean containsEntry(@CheckForNull Object r1, @CheckForNull Object r2) {
        return super.containsEntry(r1, r2);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean containsKey(@CheckForNull Object r1) {
        return super.containsKey(r1);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean containsValue(@CheckForNull Object r1) {
        return super.containsValue(r1);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
    /* bridge */ /* synthetic */ Collection createCollection() {
        return createCollection();
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap
    Collection<V> createCollection(@ParametricNullness K r3) {
        return new ValueSet(this, r3, this.valueSetCapacity);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
    Set<V> createCollection() {
        return Platform.newLinkedHashSetWithExpectedSize(this.valueSetCapacity);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection entries() {
        return entries();
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Set<Map.Entry<K, V>> entries() {
        return super.entries();
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
    Iterator<Map.Entry<K, V>> entryIterator() {
        return new C06641(this);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ boolean equals(@CheckForNull Object r1) {
        return super.equals(r1);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Set get(@ParametricNullness Object r1) {
        return super.get(r1);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Set<K> keySet() {
        return super.keySet();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Multiset keys() {
        return super.keys();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean put(@ParametricNullness Object r1, @ParametricNullness Object r2) {
        return super.put(r1, r2);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean putAll(Multimap r1) {
        return super.putAll(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean putAll(@ParametricNullness Object r1, Iterable r2) {
        return super.putAll(r1, r2);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean remove(@CheckForNull Object r1, @CheckForNull Object r2) {
        return super.remove(r1, r2);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Set removeAll(@CheckForNull Object r1) {
        return super.removeAll(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection replaceValues(@ParametricNullness Object r1, Iterable r2) {
        return replaceValues(r1, r2);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Set<V> replaceValues(@ParametricNullness K r2, Iterable<? extends V> r3) {
        return super.replaceValues(r2, r3);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    @Override // com.google.common.collect.AbstractMultimap
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
    Iterator<V> valueIterator() {
        return Maps.valueIterator(entryIterator());
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Collection<V> values() {
        return super.values();
    }
}
