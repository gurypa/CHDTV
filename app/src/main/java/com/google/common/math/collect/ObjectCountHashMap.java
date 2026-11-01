package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class ObjectCountHashMap<K> {
    static final float DEFAULT_LOAD_FACTOR = 1.0f;
    static final int DEFAULT_SIZE = 3;
    private static final long HASH_MASK = -4294967296L;
    private static final int MAXIMUM_CAPACITY = 1073741824;
    private static final long NEXT_MASK = 4294967295L;
    static final int UNSET = -1;
    transient long[] entries;
    transient Object[] keys;
    private transient float loadFactor;
    transient int modCount;
    transient int size;
    private transient int[] table;
    private transient int threshold;
    transient int[] values;

    class MapEntry extends Multisets.AbstractEntry<K> {

        @ParametricNullness
        final K key;
        int lastKnownIndex;
        final /* synthetic */ ObjectCountHashMap this$0;

        MapEntry(ObjectCountHashMap r2, int r3) {
            this.this$0 = r2;
            this.key = (K) r2.keys[r3];
            this.lastKnownIndex = r3;
        }

        @Override // com.google.common.collect.Multiset.Entry
        public int getCount() {
            updateLastKnownIndex();
            if (this.lastKnownIndex != (-1)) goto L6;
            return 0;
        L6:
            return this.this$0.values[this.lastKnownIndex];
        }

        @Override // com.google.common.collect.Multiset.Entry
        @ParametricNullness
        public K getElement() {
            return this.key;
        }

        public int setCount(int r4) {
            updateLastKnownIndex();
            if (this.lastKnownIndex != (-1)) goto L6;
            this.this$0.put(this.key, r4);
            return 0;
        L6:
            int r0 = this.this$0.values[this.lastKnownIndex];
            this.this$0.values[this.lastKnownIndex] = r4;
            return r0;
        }

        void updateLastKnownIndex() {
            if (this.lastKnownIndex != (-1)) goto L5;
        L8:
            this.lastKnownIndex = this.this$0.indexOf(this.key);
            return;
        L5:
            if (this.lastKnownIndex >= this.this$0.size()) goto L8;
            if (Objects.equal(this.key, this.this$0.keys[this.lastKnownIndex]) == false) goto L8;
        }
    }

    ObjectCountHashMap() {
        init(3, 1.0f);
    }

    ObjectCountHashMap(int r2) {
        this(r2, 1.0f);
    }

    ObjectCountHashMap(int r1, float r2) {
        init(r1, r2);
    }

    ObjectCountHashMap(ObjectCountHashMap<? extends K> r4) {
        init(r4.size(), 1.0f);
        int r0 = r4.firstIndex();
    L4:
        if (r0 == (-1)) goto L6;
        put(r4.getKey(r0), r4.getValue(r0));
        r0 = r4.nextIndex(r0);
        goto L4
    }

    static <K> ObjectCountHashMap<K> create() {
        return new ObjectCountHashMap();
    }

    static <K> ObjectCountHashMap<K> createWithExpectedSize(int r1) {
        return new ObjectCountHashMap(r1);
    }

    private static int getHash(long r2) {
        return (int) (r2 >>> 32);
    }

    private static int getNext(long r1) {
        return (int) r1;
    }

    private int hashTableMask() {
        return this.table.length - 1;
    }

    private static long[] newEntries(int r3) {
        long[] r0 = new long[r3];
        Arrays.fill(r0, -1);
        return r0;
    }

    private static int[] newTable(int r2) {
        int[] r0 = new int[r2];
        Arrays.fill(r0, -1);
        return r0;
    }

    private int remove(@CheckForNull Object r11, int r12) {
        int r0 = hashTableMask() & r12;
        int r1 = this.table[r0];
        if (r1 != (-1)) goto L5;
        return 0;
    L5:
        int r4 = -1;
    L7:
        if (getHash(this.entries[r1]) == r12) goto L9;
    L16:
        r4 = r1;
        r1 = getNext(this.entries[r1]);
        if (r1 != (-1)) goto L7;
        return 0;
    L9:
        if (Objects.equal(r11, this.keys[r1]) == false) goto L16;
        int r2 = this.values[r1];
        if (r4 != (-1)) goto L13;
        this.table[r0] = getNext(this.entries[r1]);
    L14:
        moveLastEntry(r1);
        this.size--;
        this.modCount++;
        return r2;
    L13:
        this.entries[r4] = swapNext(this.entries[r4], getNext(this.entries[r1]));
        goto L14
    }

    private void resizeMeMaybe(int r4) {
        int r0 = this.entries.length;
        if (r4 <= r0) goto L10;
        int r1 = Math.max(1, r0 >>> 1) + r0;
        if (r1 >= 0) goto L7;
        r1 = Integer.MAX_VALUE;
    L7:
        if (r1 == r0) goto L11;
        resizeEntries(r1);
        return;
    L11:
        return;
    }

    private void resizeTable(int r21) {
        int[] r1 = this.table;
        int r2 = r1.length;
        if (r2 < 1073741824) goto L6;
        this.threshold = Integer.MAX_VALUE;
        return;
    L6:
        int r4 = ((int) (r21 * this.loadFactor)) + 1;
        int[] r5 = newTable(r21);
        long[] r6 = this.entries;
        int r7 = r5.length - 1;
        int r8 = 0;
    L8:
        if (r8 >= this.size) goto L10;
        int r11 = getHash(r6[r8]);
        int r12 = r11 & r7;
        int r13 = r5[r12];
        r5[r12] = r8;
        int[] r18 = r1;
        r6[r8] = (((long) r13) & NEXT_MASK) | (((long) r11) << 32);
        r8 = r8 + 1;
        r1 = r18;
        r2 = r2;
        goto L8
    L10:
        this.threshold = r4;
        this.table = r5;
    }

    private static long swapNext(long r6, int r8) {
        return (HASH_MASK & r6) | (NEXT_MASK & ((long) r8));
    }

    public void clear() {
        this.modCount++;
        Arrays.fill(this.keys, 0, this.size, null);
        Arrays.fill(this.values, 0, this.size, 0);
        Arrays.fill(this.table, -1);
        Arrays.fill(this.entries, -1);
        this.size = 0;
    }

    public boolean containsKey(@CheckForNull Object r3) {
        if (indexOf(r3) == (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    void ensureCapacity(int r3) {
        if (r3 <= this.entries.length) goto L6;
        resizeEntries(r3);
    L6:
        if (r3 < this.threshold) goto L9;
        resizeTable(Math.max(2, Integer.highestOneBit(r3 - 1) << 1));
        return;
    }

    int firstIndex() {
        if (this.size != 0) goto L5;
        return -1;
    L5:
        return 0;
    }

    public int get(@CheckForNull Object r3) {
        int r0 = indexOf(r3);
        if (r0 != (-1)) goto L6;
        return 0;
    L6:
        return this.values[r0];
    }

    Multiset.Entry<K> getEntry(int r2) {
        Preconditions.checkElementIndex(r2, this.size);
        return new MapEntry(this, r2);
    }

    @ParametricNullness
    K getKey(int r2) {
        Preconditions.checkElementIndex(r2, this.size);
        return (K) this.keys[r2];
    }

    int getValue(int r2) {
        Preconditions.checkElementIndex(r2, this.size);
        return this.values[r2];
    }

    int indexOf(@CheckForNull Object r6) {
        int r0 = Hashing.smearedHash(r6);
        int r1 = this.table[hashTableMask() & r0];
    L4:
        if (r1 == (-1)) goto L11;
        long r3 = this.entries[r1];
        if (getHash(r3) != r0) goto L10;
        if (Objects.equal(r6, this.keys[r1]) == false) goto L10;
        return r1;
    L10:
        r1 = getNext(r3);
        goto L4
    L11:
        return -1;
    }

    void init(int r5, float r6) {
        boolean r0 = false;
        if (r5 < 0) goto L5;
        boolean r2 = true;
    L6:
        Preconditions.checkArgument(r2, "Initial capacity must be non-negative");
        if (r6 <= 0.0f) goto L9;
        r0 = true;
    L9:
        Preconditions.checkArgument(r0, "Illegal load factor");
        int r1 = Hashing.closedTableSize(r5, r6);
        this.table = newTable(r1);
        this.loadFactor = r6;
        this.keys = new Object[r5];
        this.values = new int[r5];
        this.entries = newEntries(r5);
        this.threshold = Math.max(1, (int) (r1 * r6));
        return;
    L5:
        r2 = false;
        goto L6
    }

    void insertEntry(int r6, @ParametricNullness K r7, int r8, int r9) {
        this.entries[r6] = (((long) r9) << 32) | NEXT_MASK;
        this.keys[r6] = r7;
        this.values[r6] = r8;
    }

    void moveLastEntry(int r12) {
        int r0 = size() - 1;
        if (r12 >= r0) goto L11;
        this.keys[r12] = this.keys[r0];
        this.values[r12] = this.values[r0];
        this.keys[r0] = null;
        this.values[r0] = 0;
        long r5 = this.entries[r0];
        this.entries[r12] = r5;
        this.entries[r0] = -1;
        int r7 = getHash(r5) & hashTableMask();
        int r1 = this.table[r7];
        if (r1 != r0) goto L7;
        this.table[r7] = r12;
        return;
    L7:
        int r2 = r1;
        long r8 = this.entries[r1];
        r1 = getNext(r8);
        if (r1 != r0) goto L7;
        this.entries[r2] = swapNext(r8, r12);
        return;
    L11:
        this.keys[r12] = null;
        this.values[r12] = 0;
        this.entries[r12] = -1;
    }

    int nextIndex(int r3) {
        if ((r3 + 1) < this.size) goto L7;
        return -1;
    L7:
        return r3 + 1;
    }

    int nextIndexAfterRemove(int r2, int r3) {
        return r2 - 1;
    }

    public int put(@ParametricNullness K r14, int r15) {
        CollectPreconditions.checkPositive(r15, "count");
        long[] r0 = this.entries;
        Object[] r1 = this.keys;
        int[] r2 = this.values;
        int r3 = Hashing.smearedHash(r14);
        int r4 = hashTableMask() & r3;
        int r5 = this.size;
        int r6 = this.table[r4];
        if (r6 != (-1)) goto L5;
        this.table[r4] = r5;
    L15:
        if (r5 == Integer.MAX_VALUE) goto L22;
        int r7 = r5 + 1;
        resizeMeMaybe(r7);
        insertEntry(r5, r14, r15, r3);
        this.size = r7;
        if (r5 < this.threshold) goto L19;
        resizeTable(this.table.length * 2);
    L19:
        this.modCount++;
        return 0;
    L22:
        throw new IllegalStateException("Cannot contain more than Integer.MAX_VALUE elements!");
    L5:
        int r8 = r6;
        long r9 = r0[r6];
        if (getHash(r9) == r3) goto L8;
    L11:
        r6 = getNext(r9);
        if (r6 != (-1)) goto L5;
        r0[r8] = swapNext(r9, r5);
        goto L15
    L8:
        if (Objects.equal(r14, r1[r6]) == false) goto L11;
        int r10 = r2[r6];
        r2[r6] = r15;
        return r10;
    }

    public int remove(@CheckForNull Object r2) {
        return remove(r2, Hashing.smearedHash(r2));
    }

    int removeEntry(int r5) {
        return remove(this.keys[r5], getHash(this.entries[r5]));
    }

    void resizeEntries(int r5) {
        this.keys = Arrays.copyOf(this.keys, r5);
        this.values = Arrays.copyOf(this.values, r5);
        long[] r0 = this.entries;
        int r1 = r0.length;
        long[] r2 = Arrays.copyOf(r0, r5);
        if (r5 <= r1) goto L5;
        Arrays.fill(r2, r1, r5, -1);
    L5:
        this.entries = r2;
    }

    void setValue(int r2, int r3) {
        Preconditions.checkElementIndex(r2, this.size);
        this.values[r2] = r3;
    }

    int size() {
        return this.size;
    }
}
