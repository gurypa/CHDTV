package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class CompactHashMap<K, V> extends AbstractMap<K, V> implements Serializable {
    static final double HASH_FLOODING_FPP = 0.001d;
    private static final int MAX_HASH_BUCKET_LENGTH = 9;
    private static final Object NOT_FOUND = null;

    @CheckForNull
    transient int[] entries;

    @CheckForNull
    @LazyInit
    private transient Set<Map.Entry<K, V>> entrySetView;

    @CheckForNull
    @LazyInit
    private transient Set<K> keySetView;

    @CheckForNull
    transient Object[] keys;
    private transient int metadata;
    private transient int size;

    @CheckForNull
    private transient Object table;

    @CheckForNull
    transient Object[] values;

    @CheckForNull
    @LazyInit
    private transient Collection<V> valuesView;

    class EntrySetView extends AbstractSet<Map.Entry<K, V>> {
        final /* synthetic */ CompactHashMap this$0;

        EntrySetView(CompactHashMap r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r7) {
            Map<K, V> r0 = this.this$0.delegateOrNull();
            if (r0 == null) goto L7;
            return r0.entrySet().contains(r7);
        L7:
            if ((r7 instanceof Map.Entry) == false) goto L14;
            Map.Entry r1 = (Map.Entry) r7;
            int r3 = CompactHashMap.access$500(this.this$0, r1.getKey());
            if (r3 != (-1)) goto L11;
            return false;
        L11:
            if (Objects.equal(CompactHashMap.access$600(this.this$0, r3), r1.getValue()) == false) goto L16;
            return true;
        L16:
            return false;
        L14:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return this.this$0.entrySetIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r12) {
            Map<K, V> r0 = this.this$0.delegateOrNull();
            if (r0 == null) goto L7;
            return r0.entrySet().remove(r12);
        L7:
            if ((r12 instanceof Map.Entry) == false) goto L16;
            Map.Entry r1 = (Map.Entry) r12;
            if (this.this$0.needsAllocArrays() == false) goto L11;
            return false;
        L11:
            int r6 = CompactHashMap.access$700(this.this$0);
            int r3 = CompactHashing.remove(r1.getKey(), r1.getValue(), r6, CompactHashMap.access$800(this.this$0), CompactHashMap.access$900(this.this$0), CompactHashMap.access$1000(this.this$0), CompactHashMap.access$1100(this.this$0));
            if (r3 != (-1)) goto L14;
            return false;
        L14:
            this.this$0.moveLastEntry(r3, r6);
            CompactHashMap.access$1210(this.this$0);
            this.this$0.incrementModCount();
            return true;
        L16:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.size();
        }
    }

    private abstract class Itr<T> implements Iterator<T> {
        int currentIndex;
        int expectedMetadata;
        int indexToRemove;
        final /* synthetic */ CompactHashMap this$0;

        private Itr(CompactHashMap r1) {
            this.this$0 = r1;
            this.expectedMetadata = CompactHashMap.access$000(this.this$0);
            this.currentIndex = this.this$0.firstEntryIndex();
            this.indexToRemove = -1;
        }

        /* synthetic */ Itr(CompactHashMap r1, C06051 r2) {
            this(r1);
        }

        private void checkForConcurrentModification() {
            if (CompactHashMap.access$000(this.this$0) != this.expectedMetadata) goto L6;
            return;
        L6:
            throw new ConcurrentModificationException();
        }

        @ParametricNullness
        abstract T getOutput(int r1);

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.currentIndex < 0) goto L5;
            return true;
        L5:
            return false;
        }

        void incrementExpectedModCount() {
            this.expectedMetadata += 32;
        }

        @Override // java.util.Iterator
        @ParametricNullness
        public T next() {
            checkForConcurrentModification();
            if (hasNext() == false) goto L7;
            this.indexToRemove = this.currentIndex;
            T r0 = getOutput(this.currentIndex);
            this.currentIndex = this.this$0.getSuccessor(this.currentIndex);
            return r0;
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            checkForConcurrentModification();
            if (this.indexToRemove < 0) goto L5;
            boolean r0 = true;
        L6:
            CollectPreconditions.checkRemove(r0);
            incrementExpectedModCount();
            this.this$0.remove(CompactHashMap.access$100(this.this$0, this.indexToRemove));
            this.currentIndex = this.this$0.adjustAfterRemove(this.currentIndex, this.indexToRemove);
            this.indexToRemove = -1;
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    class KeySetView extends AbstractSet<K> {
        final /* synthetic */ CompactHashMap this$0;

        KeySetView(CompactHashMap r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r2) {
            return this.this$0.containsKey(r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return this.this$0.keySetIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r4) {
            Map<K, V> r0 = this.this$0.delegateOrNull();
            if (r0 == null) goto L6;
            return r0.keySet().remove(r4);
        L6:
            if (CompactHashMap.access$200(this.this$0, r4) == CompactHashMap.access$300()) goto L8;
            return true;
        L8:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.size();
        }
    }

    final class MapEntry extends AbstractMapEntry<K, V> {

        @ParametricNullness
        private final K key;
        private int lastKnownIndex;
        final /* synthetic */ CompactHashMap this$0;

        MapEntry(CompactHashMap r2, int r3) {
            this.this$0 = r2;
            this.key = (K) CompactHashMap.access$100(r2, r3);
            this.lastKnownIndex = r3;
        }

        private void updateLastKnownIndex() {
            if (this.lastKnownIndex != (-1)) goto L5;
        L8:
            this.lastKnownIndex = CompactHashMap.access$500(this.this$0, this.key);
            return;
        L5:
            if (this.lastKnownIndex >= this.this$0.size()) goto L8;
            if (Objects.equal(this.key, CompactHashMap.access$100(this.this$0, this.lastKnownIndex)) == false) goto L8;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public V getValue() {
            Map<K, V> r0 = this.this$0.delegateOrNull();
            if (r0 != null) goto L5;
            updateLastKnownIndex();
            if (this.lastKnownIndex != (-1)) goto L10;
            return (V) NullnessCasts.unsafeNull();
        L10:
            return (V) CompactHashMap.access$600(this.this$0, this.lastKnownIndex);
        L5:
            return (V) NullnessCasts.uncheckedCastNullableTToT(r0.get(this.key));
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @ParametricNullness
        public V setValue(@ParametricNullness V r5) {
            Map<K, V> r0 = this.this$0.delegateOrNull();
            if (r0 != null) goto L5;
            updateLastKnownIndex();
            if (this.lastKnownIndex != (-1)) goto L10;
            this.this$0.put(this.key, r5);
            return (V) NullnessCasts.unsafeNull();
        L10:
            V r1 = (V) CompactHashMap.access$600(this.this$0, this.lastKnownIndex);
            CompactHashMap.access$1300(this.this$0, this.lastKnownIndex, r5);
            return r1;
        L5:
            return (V) NullnessCasts.uncheckedCastNullableTToT(r0.put(this.key, r5));
        }
    }

    class ValuesView extends AbstractCollection<V> {
        final /* synthetic */ CompactHashMap this$0;

        ValuesView(CompactHashMap r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return this.this$0.valuesIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.this$0.size();
        }
    }

    static {
        NOT_FOUND = new Object();
    }

    CompactHashMap() {
        init(3);
    }

    CompactHashMap(int r1) {
        init(r1);
    }

    static /* synthetic */ int access$000(CompactHashMap r1) {
        return r1.metadata;
    }

    static /* synthetic */ Object access$100(CompactHashMap r1, int r2) {
        return r1.key(r2);
    }

    static /* synthetic */ Object[] access$1000(CompactHashMap r1) {
        return r1.requireKeys();
    }

    static /* synthetic */ Object[] access$1100(CompactHashMap r1) {
        return r1.requireValues();
    }

    static /* synthetic */ int access$1210(CompactHashMap r2) {
        int r0 = r2.size;
        r2.size = r0 - 1;
        return r0;
    }

    static /* synthetic */ void access$1300(CompactHashMap r0, int r1, Object r2) {
        r0.setValue(r1, r2);
    }

    static /* synthetic */ Object access$200(CompactHashMap r1, Object r2) {
        return r1.removeHelper(r2);
    }

    static /* synthetic */ Object access$300() {
        return NOT_FOUND;
    }

    static /* synthetic */ int access$500(CompactHashMap r1, Object r2) {
        return r1.indexOf(r2);
    }

    static /* synthetic */ Object access$600(CompactHashMap r1, int r2) {
        return r1.value(r2);
    }

    static /* synthetic */ int access$700(CompactHashMap r1) {
        return r1.hashTableMask();
    }

    static /* synthetic */ Object access$800(CompactHashMap r1) {
        return r1.requireTable();
    }

    static /* synthetic */ int[] access$900(CompactHashMap r1) {
        return r1.requireEntries();
    }

    public static <K, V> CompactHashMap<K, V> create() {
        return new CompactHashMap();
    }

    public static <K, V> CompactHashMap<K, V> createWithExpectedSize(int r1) {
        return new CompactHashMap(r1);
    }

    private int entry(int r2) {
        return requireEntries()[r2];
    }

    private int hashTableMask() {
        return (1 << (this.metadata & 31)) - 1;
    }

    private int indexOf(@CheckForNull Object r9) {
        if (needsAllocArrays() == false) goto L5;
        return -1;
    L5:
        int r0 = Hashing.smearedHash(r9);
        int r2 = hashTableMask();
        int r3 = CompactHashing.tableGet(requireTable(), r0 & r2);
        if (r3 != 0) goto L8;
        return -1;
    L8:
        int r4 = CompactHashing.getHashPrefix(r0, r2);
    L9:
        int r5 = r3 - 1;
        int r6 = entry(r5);
        if (CompactHashing.getHashPrefix(r6, r2) == r4) goto L12;
    L14:
        r3 = CompactHashing.getNext(r6, r2);
        if (r3 != 0) goto L9;
        return -1;
    L12:
        if (Objects.equal(r9, key(r5)) == false) goto L14;
        return r5;
    }

    private K key(int r2) {
        return (K) requireKeys()[r2];
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream r5) throws IOException, ClassNotFoundException {
        r5.defaultReadObject();
        int r0 = r5.readInt();
        if (r0 < 0) goto L9;
        init(r0);
        int r1 = 0;
    L5:
        if (r1 >= r0) goto L7;
        put(r5.readObject(), r5.readObject());
        r1 = r1 + 1;
        goto L5
    L7:
        return;
    L9:
        throw new InvalidObjectException("Invalid size: " + r0);
    }

    private Object removeHelper(@CheckForNull Object r9) {
        if (needsAllocArrays() == true) goto L5;
        int r3 = hashTableMask();
        int r10 = CompactHashing.remove(r9, null, r3, requireTable(), requireEntries(), requireKeys(), null);
        if (r10 == (-1)) goto L9;
        V r0 = value(r10);
        moveLastEntry(r10, r3);
        this.size--;
        incrementModCount();
        return r0;
    L9:
        return NOT_FOUND;
    L5:
        return NOT_FOUND;
    }

    private int[] requireEntries() {
        return (int[]) java.util.Objects.requireNonNull(this.entries);
    }

    private Object[] requireKeys() {
        return (Object[]) java.util.Objects.requireNonNull(this.keys);
    }

    private Object requireTable() {
        return java.util.Objects.requireNonNull(this.table);
    }

    private Object[] requireValues() {
        return (Object[]) java.util.Objects.requireNonNull(this.values);
    }

    private void resizeMeMaybe(int r4) {
        int r0 = requireEntries().length;
        if (r4 <= r0) goto L8;
        int r1 = Math.min(1073741823, (Math.max(1, r0 >>> 1) + r0) | 1);
        if (r1 == r0) goto L9;
        resizeEntries(r1);
        return;
    L9:
        return;
    }

    private int resizeTable(int r13, int r14, int r15, int r16) {
        Object r0 = CompactHashing.createTable(r14);
        int r1 = r14 - 1;
        if (r16 == 0) goto L5;
        CompactHashing.tableSet(r0, r15 & r1, r16 + 1);
    L5:
        Object r2 = requireTable();
        int[] r3 = requireEntries();
        int r4 = 0;
    L6:
        if (r4 > r13) goto L11;
        int r5 = CompactHashing.tableGet(r2, r4);
    L8:
        if (r5 == 0) goto L10;
        int r6 = r5 - 1;
        int r7 = r3[r6];
        int r8 = CompactHashing.getHashPrefix(r7, r13) | r4;
        int r9 = r8 & r1;
        int r10 = CompactHashing.tableGet(r0, r9);
        CompactHashing.tableSet(r0, r9, r5);
        r3[r6] = CompactHashing.maskCombine(r8, r10, r1);
        r5 = CompactHashing.getNext(r7, r13);
        goto L8
    L10:
        r4 = r4 + 1;
        goto L6
    L11:
        this.table = r0;
        setHashTableMask(r1);
        return r1;
    }

    private void setEntry(int r2, int r3) {
        requireEntries()[r2] = r3;
    }

    private void setHashTableMask(int r4) {
        int r0 = 32 - Integer.numberOfLeadingZeros(r4);
        this.metadata = CompactHashing.maskCombine(this.metadata, r0, 31);
    }

    private void setKey(int r2, K r3) {
        requireKeys()[r2] = r3;
    }

    private void setValue(int r2, V r3) {
        requireValues()[r2] = r3;
    }

    private V value(int r2) {
        return (V) requireValues()[r2];
    }

    private void writeObject(ObjectOutputStream r4) throws IOException {
        r4.defaultWriteObject();
        r4.writeInt(size());
        Iterator<Map.Entry<K, V>> r0 = entrySetIterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Map.Entry<K, V> r1 = r0.next();
        r4.writeObject(r1.getKey());
        r4.writeObject(r1.getValue());
        goto L4
    }

    void accessEntry(int r1) {
    }

    int adjustAfterRemove(int r2, int r3) {
        return r2 - 1;
    }

    int allocArrays() {
        Preconditions.checkState(needsAllocArrays(), "Arrays already allocated");
        int r0 = this.metadata;
        int r1 = CompactHashing.tableSize(r0);
        this.table = CompactHashing.createTable(r1);
        setHashTableMask(r1 - 1);
        this.entries = new int[r0];
        this.keys = new Object[r0];
        this.values = new Object[r0];
        return r0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        if (needsAllocArrays() == false) goto L5;
        return;
    L5:
        incrementModCount();
        Map<K, V> r0 = delegateOrNull();
        if (r0 == null) goto L8;
        this.metadata = Ints.constrainToRange(size(), 3, 1073741823);
        r0.clear();
        this.table = null;
        this.size = 0;
        return;
    L8:
        Arrays.fill(requireKeys(), 0, this.size, null);
        Arrays.fill(requireValues(), 0, this.size, null);
        CompactHashing.tableClear(requireTable());
        Arrays.fill(requireEntries(), 0, this.size, 0);
        this.size = 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(@CheckForNull Object r4) {
        Map<K, V> r0 = delegateOrNull();
        if (r0 == null) goto L6;
        return r0.containsKey(r4);
    L6:
        if (indexOf(r4) == (-1)) goto L8;
        return true;
    L8:
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(@CheckForNull Object r4) {
        Map<K, V> r0 = delegateOrNull();
        if (r0 != null) goto L5;
        int r1 = 0;
    L8:
        if (r1 >= this.size) goto L14;
        if (Objects.equal(r4, value(r1)) == true) goto L11;
        r1 = r1 + 1;
        goto L8
    L11:
        return true;
    L14:
        return false;
    L5:
        return r0.containsValue(r4);
    }

    Map<K, V> convertToHashFloodingResistantImplementation() {
        Map<K, V> r0 = createHashFloodingResistantDelegate(hashTableMask() + 1);
        int r1 = firstEntryIndex();
    L3:
        if (r1 < 0) goto L5;
        r0.put(key(r1), value(r1));
        r1 = getSuccessor(r1);
        goto L3
    L5:
        this.table = r0;
        this.entries = null;
        this.keys = null;
        this.values = null;
        incrementModCount();
        return r0;
    }

    Set<Map.Entry<K, V>> createEntrySet() {
        return new EntrySetView(this);
    }

    Map<K, V> createHashFloodingResistantDelegate(int r3) {
        return new LinkedHashMap(r3, 1.0f);
    }

    Set<K> createKeySet() {
        return new KeySetView(this);
    }

    Collection<V> createValues() {
        return new ValuesView(this);
    }

    @CheckForNull
    Map<K, V> delegateOrNull() {
        if ((this.table instanceof Map) == true) goto L5;
        return null;
    L5:
        return (Map) this.table;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.entrySetView != null) goto L6;
        Set<Map.Entry<K, V>> r0 = createEntrySet();
        this.entrySetView = r0;
        return r0;
    L6:
        return this.entrySetView;
    }

    Iterator<Map.Entry<K, V>> entrySetIterator() {
        Map<K, V> r0 = delegateOrNull();
        if (r0 == null) goto L7;
        return r0.entrySet().iterator();
    L7:
        return new C06062(this);
    }

    int firstEntryIndex() {
        if (isEmpty() == false) goto L5;
        return -1;
    L5:
        return 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CheckForNull
    public V get(@CheckForNull Object r4) {
        Map<K, V> r0 = delegateOrNull();
        if (r0 != null) goto L5;
        int r1 = indexOf(r4);
        if (r1 != (-1)) goto L10;
        return null;
    L10:
        accessEntry(r1);
        return value(r1);
    L5:
        return r0.get(r4);
    }

    int getSuccessor(int r3) {
        if ((r3 + 1) < this.size) goto L7;
        return -1;
    L7:
        return r3 + 1;
    }

    void incrementModCount() {
        this.metadata += 32;
    }

    void init(int r4) {
        if (r4 < 0) goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkArgument(r1, "Expected size must be >= 0");
        this.metadata = Ints.constrainToRange(r4, 1, 1073741823);
        return;
    L5:
        r1 = false;
        goto L6
    }

    void insertEntry(int r2, @ParametricNullness K r3, @ParametricNullness V r4, int r5, int r6) {
        setEntry(r2, CompactHashing.maskCombine(r5, 0, r6));
        setKey(r2, r3);
        setValue(r2, r4);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        if (size() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        if (this.keySetView != null) goto L6;
        Set<K> r0 = createKeySet();
        this.keySetView = r0;
        return r0;
    L6:
        return this.keySetView;
    }

    Iterator<K> keySetIterator() {
        Map<K, V> r0 = delegateOrNull();
        if (r0 == null) goto L7;
        return r0.keySet().iterator();
    L7:
        return new C06051(this);
    }

    void moveLastEntry(int r13, int r14) {
        Object r0 = requireTable();
        int[] r1 = requireEntries();
        Object[] r2 = requireKeys();
        Object[] r3 = requireValues();
        int r4 = size() - 1;
        if (r13 >= r4) goto L11;
        Object r7 = r2[r4];
        r2[r13] = r7;
        r3[r13] = r3[r4];
        r2[r4] = null;
        r3[r4] = null;
        r1[r13] = r1[r4];
        r1[r4] = 0;
        int r8 = Hashing.smearedHash(r7) & r14;
        int r5 = CompactHashing.tableGet(r0, r8);
        int r9 = r4 + 1;
        if (r5 != r9) goto L7;
        CompactHashing.tableSet(r0, r8, r13 + 1);
        return;
    L7:
        int r6 = r5 - 1;
        int r10 = r1[r6];
        r5 = CompactHashing.getNext(r10, r14);
        if (r5 != r9) goto L7;
        r1[r6] = CompactHashing.maskCombine(r10, r13 + 1, r14);
        return;
    L11:
        r2[r13] = null;
        r3[r13] = null;
        r1[r13] = 0;
    }

    boolean needsAllocArrays() {
        if (this.table != null) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CheckForNull
    public V put(@ParametricNullness K r19, @ParametricNullness V r20) {
        K r2 = r19;
        V r3 = r20;
        if (needsAllocArrays() == false) goto L5;
        allocArrays();
    L5:
        Map<K, V> r6 = delegateOrNull();
        if (r6 != null) goto L8;
        int[] r7 = requireEntries();
        Object[] r8 = requireKeys();
        Object[] r9 = requireValues();
        int r1 = this.size;
        int r10 = r1 + 1;
        int r4 = Hashing.smearedHash(r2);
        int r5 = hashTableMask();
        int r11 = r4 & r5;
        int r12 = CompactHashing.tableGet(requireTable(), r11);
        if (r12 != 0) goto L14;
        if (r10 <= r5) goto L13;
        r5 = resizeTable(r5, CompactHashing.newCapacity(r5), r4, r1);
    L30:
        resizeMeMaybe(r10);
        insertEntry(r1, r2, r3, r4, r5);
        this.size = r10;
        incrementModCount();
        return null;
    L13:
        CompactHashing.tableSet(requireTable(), r11, r1 + 1);
        goto L30
    L14:
        int r13 = CompactHashing.getHashPrefix(r4, r5);
        int r14 = 0;
    L15:
        int r15 = r12 - 1;
        Map<K, V> r16 = r6;
        int r17 = r7[r15];
        int[] r18 = r7;
        if (CompactHashing.getHashPrefix(r17, r5) == r13) goto L18;
    L21:
        r12 = CompactHashing.getNext(r17, r5);
        r14 = r14 + 1;
        if (r12 == 0) goto L24;
        r2 = r19;
        r3 = r20;
        r6 = r16;
        r7 = r18;
        goto L15
    L24:
        if (r14 >= 9) goto L26;
        if (r10 <= r5) goto L29;
        r5 = resizeTable(r5, CompactHashing.newCapacity(r5), r4, r1);
        goto L30
    L29:
        r18[r15] = CompactHashing.maskCombine(r17, r1 + 1, r5);
        goto L30
    L26:
        return convertToHashFloodingResistantImplementation().put(r2, r3);
    L18:
        if (Objects.equal(r2, r8[r15]) == false) goto L21;
        V r21 = (V) r9[r15];
        r9[r15] = r3;
        accessEntry(r15);
        return r21;
    L8:
        return r6.put(r2, r3);
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CheckForNull
    public V remove(@CheckForNull Object r4) {
        Map<K, V> r0 = delegateOrNull();
        if (r0 != null) goto L5;
        V r1 = (V) removeHelper(r4);
        if (r1 != NOT_FOUND) goto L10;
        return null;
    L10:
        return r1;
    L5:
        return r0.remove(r4);
    }

    void resizeEntries(int r2) {
        this.entries = Arrays.copyOf(requireEntries(), r2);
        this.keys = Arrays.copyOf(requireKeys(), r2);
        this.values = Arrays.copyOf(requireValues(), r2);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        Map<K, V> r0 = delegateOrNull();
        if (r0 == null) goto L6;
        return r0.size();
    L6:
        return this.size;
    }

    public void trimToSize() {
        if (needsAllocArrays() == false) goto L5;
        return;
    L5:
        Map<K, V> r0 = delegateOrNull();
        if (r0 == null) goto L9;
        Map<K, V> r1 = createHashFloodingResistantDelegate(size());
        r1.putAll(r0);
        this.table = r1;
        return;
    L9:
        int r2 = this.size;
        if (r2 >= requireEntries().length) goto L12;
        resizeEntries(r2);
    L12:
        int r3 = CompactHashing.tableSize(r2);
        int r4 = hashTableMask();
        if (r3 >= r4) goto L16;
        resizeTable(r4, r3, 0, 0);
        return;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        if (this.valuesView != null) goto L6;
        Collection<V> r0 = createValues();
        this.valuesView = r0;
        return r0;
    L6:
        return this.valuesView;
    }

    Iterator<V> valuesIterator() {
        Map<K, V> r0 = delegateOrNull();
        if (r0 == null) goto L7;
        return r0.values().iterator();
    L7:
        return new C06073(this);
    }
}
