package com.google.common.collect;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class CompactLinkedHashMap<K, V> extends CompactHashMap<K, V> {
    private static final int ENDPOINT = -2;
    private final boolean accessOrder;
    private transient int firstEntry;
    private transient int lastEntry;

    @CheckForNull
    transient long[] links;

    CompactLinkedHashMap() {
        this(3);
    }

    CompactLinkedHashMap(int r2) {
        this(r2, false);
    }

    CompactLinkedHashMap(int r1, boolean r2) {
        super(r1);
        this.accessOrder = r2;
    }

    public static <K, V> CompactLinkedHashMap<K, V> create() {
        return new CompactLinkedHashMap();
    }

    public static <K, V> CompactLinkedHashMap<K, V> createWithExpectedSize(int r1) {
        return new CompactLinkedHashMap(r1);
    }

    private int getPredecessor(int r4) {
        return ((int) (link(r4) >>> 32)) - 1;
    }

    private long link(int r4) {
        return requireLinks()[r4];
    }

    private long[] requireLinks() {
        return (long[]) Objects.requireNonNull(this.links);
    }

    private void setLink(int r2, long r3) {
        requireLinks()[r2] = r3;
    }

    private void setPredecessor(int r8, int r9) {
        setLink(r8, (link(r8) & (~(-4294967296L))) | (((long) (r9 + 1)) << 32));
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

    private void setSuccessor(int r7, int r8) {
        setLink(r7, (link(r7) & (~4294967295L)) | (((long) (r8 + 1)) & 4294967295L));
    }

    @Override // com.google.common.collect.CompactHashMap
    void accessEntry(int r3) {
        if (this.accessOrder == false) goto L6;
        setSucceeds(getPredecessor(r3), getSuccessor(r3));
        setSucceeds(this.lastEntry, r3);
        setSucceeds(r3, -2);
        incrementModCount();
        return;
    }

    @Override // com.google.common.collect.CompactHashMap
    int adjustAfterRemove(int r2, int r3) {
        if (r2 < size()) goto L6;
        return r3;
    L6:
        return r2;
    }

    @Override // com.google.common.collect.CompactHashMap
    int allocArrays() {
        int r0 = super.allocArrays();
        this.links = new long[r0];
        return r0;
    }

    @Override // com.google.common.collect.CompactHashMap, java.util.AbstractMap, java.util.Map
    public void clear() {
        if (needsAllocArrays() == false) goto L5;
        return;
    L5:
        this.firstEntry = -2;
        this.lastEntry = -2;
        if (this.links == null) goto L8;
        Arrays.fill(this.links, 0, size(), 0);
    L8:
        super.clear();
    }

    @Override // com.google.common.collect.CompactHashMap
    Map<K, V> convertToHashFloodingResistantImplementation() {
        Map<K, V> r0 = super.convertToHashFloodingResistantImplementation();
        this.links = null;
        return r0;
    }

    @Override // com.google.common.collect.CompactHashMap
    Map<K, V> createHashFloodingResistantDelegate(int r4) {
        return new LinkedHashMap(r4, 1.0f, this.accessOrder);
    }

    @Override // com.google.common.collect.CompactHashMap
    int firstEntryIndex() {
        return this.firstEntry;
    }

    @Override // com.google.common.collect.CompactHashMap
    int getSuccessor(int r3) {
        return ((int) link(r3)) - 1;
    }

    @Override // com.google.common.collect.CompactHashMap
    void init(int r2) {
        super.init(r2);
        this.firstEntry = -2;
        this.lastEntry = -2;
    }

    @Override // com.google.common.collect.CompactHashMap
    void insertEntry(int r3, @ParametricNullness K r4, @ParametricNullness V r5, int r6, int r7) {
        super.insertEntry(r3, r4, r5, r6, r7);
        setSucceeds(this.lastEntry, r3);
        setSucceeds(r3, -2);
    }

    @Override // com.google.common.collect.CompactHashMap
    void moveLastEntry(int r4, int r5) {
        int r0 = size() - 1;
        super.moveLastEntry(r4, r5);
        setSucceeds(getPredecessor(r4), getSuccessor(r4));
        if (r4 >= r0) goto L5;
        setSucceeds(getPredecessor(r0), r4);
        setSucceeds(r4, getSuccessor(r0));
    L5:
        setLink(r0, 0);
    }

    @Override // com.google.common.collect.CompactHashMap
    void resizeEntries(int r2) {
        super.resizeEntries(r2);
        this.links = Arrays.copyOf(requireLinks(), r2);
    }
}
