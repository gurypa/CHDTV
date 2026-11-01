package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class CompactHashSet<E> extends AbstractSet<E> implements Serializable {
    static final double HASH_FLOODING_FPP = 0.001d;
    private static final int MAX_HASH_BUCKET_LENGTH = 9;

    @CheckForNull
    transient Object[] elements;

    @CheckForNull
    private transient int[] entries;
    private transient int metadata;
    private transient int size;

    @CheckForNull
    private transient Object table;

    CompactHashSet() {
        init(3);
    }

    CompactHashSet(int r1) {
        init(r1);
    }

    static /* synthetic */ int access$000(CompactHashSet r1) {
        return r1.metadata;
    }

    static /* synthetic */ Object access$100(CompactHashSet r1, int r2) {
        return r1.element(r2);
    }

    public static <E> CompactHashSet<E> create() {
        return new CompactHashSet();
    }

    public static <E> CompactHashSet<E> create(Collection<? extends E> r1) {
        CompactHashSet<E> r0 = createWithExpectedSize(r1.size());
        r0.addAll(r1);
        return r0;
    }

    @SafeVarargs
    public static <E> CompactHashSet<E> create(E... r1) {
        CompactHashSet<E> r0 = createWithExpectedSize(r1.length);
        Collections.addAll(r0, r1);
        return r0;
    }

    private Set<E> createHashFloodingResistantDelegate(int r3) {
        return new LinkedHashSet(r3, 1.0f);
    }

    public static <E> CompactHashSet<E> createWithExpectedSize(int r1) {
        return new CompactHashSet(r1);
    }

    private E element(int r2) {
        return (E) requireElements()[r2];
    }

    private int entry(int r2) {
        return requireEntries()[r2];
    }

    private int hashTableMask() {
        return (1 << (this.metadata & 31)) - 1;
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
        add(r5.readObject());
        r1 = r1 + 1;
        goto L5
    L7:
        return;
    L9:
        throw new InvalidObjectException("Invalid size: " + r0);
    }

    private Object[] requireElements() {
        return (Object[]) Objects.requireNonNull(this.elements);
    }

    private int[] requireEntries() {
        return (int[]) Objects.requireNonNull(this.entries);
    }

    private Object requireTable() {
        return Objects.requireNonNull(this.table);
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

    private void setElement(int r2, E r3) {
        requireElements()[r2] = r3;
    }

    private void setEntry(int r2, int r3) {
        requireEntries()[r2] = r3;
    }

    private void setHashTableMask(int r4) {
        int r0 = 32 - Integer.numberOfLeadingZeros(r4);
        this.metadata = CompactHashing.maskCombine(this.metadata, r0, 31);
    }

    private void writeObject(ObjectOutputStream r3) throws IOException {
        r3.defaultWriteObject();
        r3.writeInt(size());
        Iterator<E> r0 = iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r3.writeObject(r0.next());
        goto L4
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(@ParametricNullness E r15) {
        if (needsAllocArrays() == false) goto L5;
        allocArrays();
    L5:
        Set<E> r0 = delegateOrNull();
        if (r0 != null) goto L8;
        int[] r1 = requireEntries();
        Object[] r2 = requireElements();
        int r3 = this.size;
        int r4 = r3 + 1;
        int r5 = Hashing.smearedHash(r15);
        int r6 = hashTableMask();
        int r7 = r5 & r6;
        int r8 = CompactHashing.tableGet(requireTable(), r7);
        if (r8 != 0) goto L14;
        if (r4 <= r6) goto L13;
        r6 = resizeTable(r6, CompactHashing.newCapacity(r6), r5, r3);
    L30:
        resizeMeMaybe(r4);
        insertEntry(r3, r15, r5, r6);
        this.size = r4;
        incrementModCount();
        return true;
    L13:
        CompactHashing.tableSet(requireTable(), r7, r3 + 1);
        goto L30
    L14:
        int r9 = CompactHashing.getHashPrefix(r5, r6);
        int r10 = 0;
    L15:
        int r11 = r8 - 1;
        int r12 = r1[r11];
        if (CompactHashing.getHashPrefix(r12, r6) == r9) goto L18;
    L21:
        r8 = CompactHashing.getNext(r12, r6);
        r10 = r10 + 1;
        if (r8 != 0) goto L15;
        if (r10 >= 9) goto L26;
        if (r4 <= r6) goto L29;
        r6 = resizeTable(r6, CompactHashing.newCapacity(r6), r5, r3);
        goto L30
    L29:
        r1[r11] = CompactHashing.maskCombine(r12, r3 + 1, r6);
        goto L30
    L26:
        return convertToHashFloodingResistantImplementation().add(r15);
    L18:
        if (com.google.common.base.Objects.equal(r15, r2[r11]) == false) goto L21;
        return false;
    L8:
        return r0.add(r15);
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
        this.elements = new Object[r0];
        return r0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        if (needsAllocArrays() == false) goto L5;
        return;
    L5:
        incrementModCount();
        Set<E> r0 = delegateOrNull();
        if (r0 == null) goto L8;
        this.metadata = Ints.constrainToRange(size(), 3, 1073741823);
        r0.clear();
        this.table = null;
        this.size = 0;
        return;
    L8:
        Arrays.fill(requireElements(), 0, this.size, null);
        CompactHashing.tableClear(requireTable());
        Arrays.fill(requireEntries(), 0, this.size, 0);
        this.size = 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@CheckForNull Object r10) {
        if (needsAllocArrays() == false) goto L5;
        return false;
    L5:
        Set<E> r0 = delegateOrNull();
        if (r0 != null) goto L8;
        int r2 = Hashing.smearedHash(r10);
        int r3 = hashTableMask();
        int r4 = CompactHashing.tableGet(requireTable(), r2 & r3);
        if (r4 != 0) goto L12;
        return false;
    L12:
        int r5 = CompactHashing.getHashPrefix(r2, r3);
    L13:
        int r6 = r4 - 1;
        int r7 = entry(r6);
        if (CompactHashing.getHashPrefix(r7, r3) == r5) goto L16;
    L19:
        r4 = CompactHashing.getNext(r7, r3);
        if (r4 != 0) goto L13;
        return false;
    L16:
        if (com.google.common.base.Objects.equal(r10, element(r6)) == false) goto L19;
        return true;
    L8:
        return r0.contains(r10);
    }

    Set<E> convertToHashFloodingResistantImplementation() {
        Set<E> r0 = createHashFloodingResistantDelegate(hashTableMask() + 1);
        int r1 = firstEntryIndex();
    L3:
        if (r1 < 0) goto L5;
        r0.add(element(r1));
        r1 = getSuccessor(r1);
        goto L3
    L5:
        this.table = r0;
        this.entries = null;
        this.elements = null;
        incrementModCount();
        return r0;
    }

    @CheckForNull
    Set<E> delegateOrNull() {
        if ((this.table instanceof Set) == true) goto L5;
        return null;
    L5:
        return (Set) this.table;
    }

    int firstEntryIndex() {
        if (isEmpty() == false) goto L5;
        return -1;
    L5:
        return 0;
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

    void insertEntry(int r2, @ParametricNullness E r3, int r4, int r5) {
        setEntry(r2, CompactHashing.maskCombine(r4, 0, r5));
        setElement(r2, r3);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        if (size() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    boolean isUsingHashFloodingResistance() {
        if (delegateOrNull() == null) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        Set<E> r0 = delegateOrNull();
        if (r0 == null) goto L7;
        return r0.iterator();
    L7:
        return new C06081(this);
    }

    void moveLastEntry(int r12, int r13) {
        Object r0 = requireTable();
        int[] r1 = requireEntries();
        Object[] r2 = requireElements();
        int r3 = size() - 1;
        if (r12 >= r3) goto L11;
        Object r6 = r2[r3];
        r2[r12] = r6;
        r2[r3] = null;
        r1[r12] = r1[r3];
        r1[r3] = 0;
        int r7 = Hashing.smearedHash(r6) & r13;
        int r4 = CompactHashing.tableGet(r0, r7);
        int r8 = r3 + 1;
        if (r4 != r8) goto L7;
        CompactHashing.tableSet(r0, r7, r12 + 1);
        return;
    L7:
        int r5 = r4 - 1;
        int r9 = r1[r5];
        r4 = CompactHashing.getNext(r9, r13);
        if (r4 != r8) goto L7;
        r1[r5] = CompactHashing.maskCombine(r9, r12 + 1, r13);
        return;
    L11:
        r2[r12] = null;
        r1[r12] = 0;
    }

    boolean needsAllocArrays() {
        if (this.table != null) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(@CheckForNull Object r10) {
        if (needsAllocArrays() == false) goto L5;
        return false;
    L5:
        Set<E> r0 = delegateOrNull();
        if (r0 != null) goto L8;
        int r4 = hashTableMask();
        int r11 = CompactHashing.remove(r10, null, r4, requireTable(), requireEntries(), requireElements(), null);
        if (r11 != (-1)) goto L12;
        return false;
    L12:
        moveLastEntry(r11, r4);
        this.size--;
        incrementModCount();
        return true;
    L8:
        return r0.remove(r10);
    }

    void resizeEntries(int r2) {
        this.entries = Arrays.copyOf(requireEntries(), r2);
        this.elements = Arrays.copyOf(requireElements(), r2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        Set<E> r0 = delegateOrNull();
        if (r0 == null) goto L6;
        return r0.size();
    L6:
        return this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public Object[] toArray() {
        if (needsAllocArrays() == true) goto L5;
        Set<E> r0 = delegateOrNull();
        if (r0 == null) goto L10;
        return r0.toArray();
    L10:
        return Arrays.copyOf(requireElements(), this.size);
    L5:
        return new Object[0];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] r5) {
        if (needsAllocArrays() == true) goto L5;
        Set<E> r0 = delegateOrNull();
        if (r0 == null) goto L12;
        return (T[]) r0.toArray(r5);
    L12:
        return (T[]) ObjectArrays.toArrayImpl(requireElements(), 0, this.size, r5);
    L5:
        if (r5.length <= 0) goto L7;
        r5[0] = null;
    L7:
        return r5;
    }

    public void trimToSize() {
        if (needsAllocArrays() == false) goto L5;
        return;
    L5:
        Set<E> r0 = delegateOrNull();
        if (r0 == null) goto L9;
        Set<E> r1 = createHashFloodingResistantDelegate(size());
        r1.addAll(r0);
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
}
