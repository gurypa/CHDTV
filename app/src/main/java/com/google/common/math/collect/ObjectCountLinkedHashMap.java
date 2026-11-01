package com.google.common.collect;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class ObjectCountLinkedHashMap<K> extends ObjectCountHashMap<K> {
    private static final int ENDPOINT = -2;
    private transient int firstEntry;
    private transient int lastEntry;
    transient long[] links;

    ObjectCountLinkedHashMap() {
        this(3);
    }

    ObjectCountLinkedHashMap(int r2) {
        this(r2, 1.0f);
    }

    ObjectCountLinkedHashMap(int r1, float r2) {
        super(r1, r2);
    }

    ObjectCountLinkedHashMap(ObjectCountHashMap<K> r4) {
        init(r4.size(), 1.0f);
        int r0 = r4.firstIndex();
    L4:
        if (r0 == (-1)) goto L6;
        put(r4.getKey(r0), r4.getValue(r0));
        r0 = r4.nextIndex(r0);
        goto L4
    }

    static <K> ObjectCountLinkedHashMap<K> create() {
        return new ObjectCountLinkedHashMap();
    }

    static <K> ObjectCountLinkedHashMap<K> createWithExpectedSize(int r1) {
        return new ObjectCountLinkedHashMap(r1);
    }

    private int getPredecessor(int r4) {
        return (int) (this.links[r4] >>> 32);
    }

    private int getSuccessor(int r4) {
        return (int) this.links[r4];
    }

    private void setPredecessor(int r9, int r10) {
        this.links[r9] = (this.links[r9] & (~(-4294967296L))) | (((long) r10) << 32);
    }

    private void setSucceeds(int r2, int r3) {
        if (r2 != (-2)) goto L5;
        this.firstEntry = r3;
    L6:
        if (r3 != (-2)) goto L8;
        this.lastEntry = r2;
        return;
    L8:
        setPredecessor(r3, r2);
        return;
    L5:
        setSuccessor(r2, r3);
        goto L6
    }

    private void setSuccessor(int r9, int r10) {
        this.links[r9] = (this.links[r9] & (~4294967295L)) | (((long) r10) & 4294967295L);
    }

    @Override // com.google.common.collect.ObjectCountHashMap
    public void clear() {
        super.clear();
        this.firstEntry = -2;
        this.lastEntry = -2;
    }

    @Override // com.google.common.collect.ObjectCountHashMap
    int firstIndex() {
        if (this.firstEntry != (-2)) goto L6;
        return -1;
    L6:
        return this.firstEntry;
    }

    @Override // com.google.common.collect.ObjectCountHashMap
    void init(int r4, float r5) {
        super.init(r4, r5);
        this.firstEntry = -2;
        this.lastEntry = -2;
        this.links = new long[r4];
        Arrays.fill(this.links, -1);
    }

    @Override // com.google.common.collect.ObjectCountHashMap
    void insertEntry(int r2, @ParametricNullness K r3, int r4, int r5) {
        super.insertEntry(r2, r3, r4, r5);
        setSucceeds(this.lastEntry, r2);
        setSucceeds(r2, -2);
    }

    @Override // com.google.common.collect.ObjectCountHashMap
    void moveLastEntry(int r4) {
        int r0 = size() - 1;
        setSucceeds(getPredecessor(r4), getSuccessor(r4));
        if (r4 >= r0) goto L5;
        setSucceeds(getPredecessor(r0), r4);
        setSucceeds(r4, getSuccessor(r0));
    L5:
        super.moveLastEntry(r4);
    }

    @Override // com.google.common.collect.ObjectCountHashMap
    int nextIndex(int r3) {
        int r0 = getSuccessor(r3);
        if (r0 != (-2)) goto L6;
        return -1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.ObjectCountHashMap
    int nextIndexAfterRemove(int r2, int r3) {
        if (r2 != size()) goto L6;
        return r3;
    L6:
        return r2;
    }

    @Override // com.google.common.collect.ObjectCountHashMap
    void resizeEntries(int r5) {
        super.resizeEntries(r5);
        int r0 = this.links.length;
        this.links = Arrays.copyOf(this.links, r5);
        Arrays.fill(this.links, r0, r5, -1);
    }
}
