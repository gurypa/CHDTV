package com.google.common.collect;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class CompactLinkedHashSet<E> extends CompactHashSet<E> {
    private static final int ENDPOINT = -2;
    private transient int firstEntry;
    private transient int lastEntry;

    @CheckForNull
    private transient int[] predecessor;

    @CheckForNull
    private transient int[] successor;

    CompactLinkedHashSet() {
    }

    CompactLinkedHashSet(int r1) {
        super(r1);
    }

    public static <E> CompactLinkedHashSet<E> create() {
        return new CompactLinkedHashSet();
    }

    public static <E> CompactLinkedHashSet<E> create(Collection<? extends E> r1) {
        CompactLinkedHashSet<E> r0 = createWithExpectedSize(r1.size());
        r0.addAll(r1);
        return r0;
    }

    @SafeVarargs
    public static <E> CompactLinkedHashSet<E> create(E... r1) {
        CompactLinkedHashSet<E> r0 = createWithExpectedSize(r1.length);
        Collections.addAll(r0, r1);
        return r0;
    }

    public static <E> CompactLinkedHashSet<E> createWithExpectedSize(int r1) {
        return new CompactLinkedHashSet(r1);
    }

    private int getPredecessor(int r2) {
        return requirePredecessors()[r2] - 1;
    }

    private int[] requirePredecessors() {
        return (int[]) Objects.requireNonNull(this.predecessor);
    }

    private int[] requireSuccessors() {
        return (int[]) Objects.requireNonNull(this.successor);
    }

    private void setPredecessor(int r3, int r4) {
        requirePredecessors()[r3] = r4 + 1;
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

    private void setSuccessor(int r3, int r4) {
        requireSuccessors()[r3] = r4 + 1;
    }

    @Override // com.google.common.collect.CompactHashSet
    int adjustAfterRemove(int r2, int r3) {
        if (r2 < size()) goto L6;
        return r3;
    L6:
        return r2;
    }

    @Override // com.google.common.collect.CompactHashSet
    int allocArrays() {
        int r0 = super.allocArrays();
        this.predecessor = new int[r0];
        this.successor = new int[r0];
        return r0;
    }

    @Override // com.google.common.collect.CompactHashSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        if (needsAllocArrays() == false) goto L5;
        return;
    L5:
        this.firstEntry = -2;
        this.lastEntry = -2;
        if (this.predecessor != null) goto L8;
    L10:
        super.clear();
        return;
    L8:
        if (this.successor == null) goto L10;
        Arrays.fill(this.predecessor, 0, size(), 0);
        Arrays.fill(this.successor, 0, size(), 0);
        goto L10
    }

    @Override // com.google.common.collect.CompactHashSet
    Set<E> convertToHashFloodingResistantImplementation() {
        Set<E> r0 = super.convertToHashFloodingResistantImplementation();
        this.predecessor = null;
        this.successor = null;
        return r0;
    }

    @Override // com.google.common.collect.CompactHashSet
    int firstEntryIndex() {
        return this.firstEntry;
    }

    @Override // com.google.common.collect.CompactHashSet
    int getSuccessor(int r2) {
        return requireSuccessors()[r2] - 1;
    }

    @Override // com.google.common.collect.CompactHashSet
    void init(int r2) {
        super.init(r2);
        this.firstEntry = -2;
        this.lastEntry = -2;
    }

    @Override // com.google.common.collect.CompactHashSet
    void insertEntry(int r2, @ParametricNullness E r3, int r4, int r5) {
        super.insertEntry(r2, r3, r4, r5);
        setSucceeds(this.lastEntry, r2);
        setSucceeds(r2, -2);
    }

    @Override // com.google.common.collect.CompactHashSet
    void moveLastEntry(int r4, int r5) {
        int r0 = size() - 1;
        super.moveLastEntry(r4, r5);
        setSucceeds(getPredecessor(r4), getSuccessor(r4));
        if (r4 >= r0) goto L5;
        setSucceeds(getPredecessor(r0), r4);
        setSucceeds(r4, getSuccessor(r0));
    L5:
        requirePredecessors()[r0] = 0;
        requireSuccessors()[r0] = 0;
    }

    @Override // com.google.common.collect.CompactHashSet
    void resizeEntries(int r2) {
        super.resizeEntries(r2);
        this.predecessor = Arrays.copyOf(requirePredecessors(), r2);
        this.successor = Arrays.copyOf(requireSuccessors(), r2);
    }

    @Override // com.google.common.collect.CompactHashSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public Object[] toArray() {
        return ObjectArrays.toArrayImpl(this);
    }

    @Override // com.google.common.collect.CompactHashSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] r2) {
        return (T[]) ObjectArrays.toArrayImpl(this, r2);
    }
}
