package androidx.collection;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class ArraySet<E> implements Collection<E>, Set<E> {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final String TAG = "ArraySet";
    private static Object[] sBaseCache;
    private static final Object sBaseCacheLock = null;
    private static int sBaseCacheSize;
    private static Object[] sTwiceBaseCache;
    private static final Object sTwiceBaseCacheLock = null;
    private static int sTwiceBaseCacheSize;
    Object[] mArray;
    private int[] mHashes;
    int mSize;

    private class ElementIterator extends IndexBasedArrayIterator<E> {
        final /* synthetic */ ArraySet this$0;

        ElementIterator(ArraySet r1) {
            this.this$0 = r1;
            super(r1.mSize);
        }

        @Override // androidx.collection.IndexBasedArrayIterator
        protected E elementAt(int r2) {
            return (E) this.this$0.valueAt(r2);
        }

        @Override // androidx.collection.IndexBasedArrayIterator
        protected void removeAt(int r2) {
            this.this$0.removeAt(r2);
        }
    }

    static {
        sBaseCacheLock = new Object();
        sTwiceBaseCacheLock = new Object();
    }

    public ArraySet() {
        this(0);
    }

    public ArraySet(int r2) {
        if (r2 != 0) goto L5;
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
    L6:
        this.mSize = 0;
        return;
    L5:
        allocArrays(r2);
        goto L6
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ArraySet(ArraySet<E> r1) {
        this();
        if (r1 == 0) goto L6;
        addAll(r1);
        return;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ArraySet(Collection<E> r1) {
        this();
        if (r1 == 0) goto L6;
        addAll(r1);
        return;
    }

    public ArraySet(E[] r4) {
        this();
        if (r4 == null) goto L7;
        int r0 = r4.length;
        int r1 = 0;
    L5:
        if (r1 >= r0) goto L9;
        add(r4[r1]);
        r1 = r1 + 1;
        goto L5
    L9:
        return;
    }

    private void allocArrays(int r9) {
        if (r9 != 8) goto L23;
        Object r0 = sTwiceBaseCacheLock;
        monitor-enter(r0);
    L19:
        th = move-exception;
        throw th;
    L7:
        if (sTwiceBaseCache == null) goto L17;
        Object[] r4 = sTwiceBaseCache;     // Catch: Throwable -> L19
        this.mArray = r4;     // Catch: ClassCastException -> L15 Throwable -> L19
        sTwiceBaseCache = (Object[]) r4[0];     // Catch: ClassCastException -> L15 Throwable -> L19
        this.mHashes = (int[]) r4[1];     // Catch: ClassCastException -> L15 Throwable -> L19
        if (this.mHashes == null) goto L16;
        r4[1] = null;     // Catch: ClassCastException -> L15 Throwable -> L19
        r4[0] = null;     // Catch: ClassCastException -> L15 Throwable -> L19
        sTwiceBaseCacheSize--;     // Catch: ClassCastException -> L15 Throwable -> L19
        monitor-exit(r0);     // Catch: Throwable -> L19
        return;
    L16:
        System.out.println("ArraySet Found corrupt ArraySet cache: [0]=" + r4[0] + " [1]=" + r4[1]);     // Catch: Throwable -> L19
        sTwiceBaseCache = null;     // Catch: Throwable -> L19
        sTwiceBaseCacheSize = 0;     // Catch: Throwable -> L19
    L17:
        monitor-exit(r0);     // Catch: Throwable -> L19
    L42:
        this.mHashes = new int[r9];
        this.mArray = new Object[r9];
        return;
    L23:
        if (r9 != 4) goto L42;
        Object r1 = sBaseCacheLock;
        monitor-enter(r1);
    L39:
        th = move-exception;
        throw th;
    L27:
        if (sBaseCache == null) goto L37;
        Object[] r5 = sBaseCache;     // Catch: Throwable -> L39
        this.mArray = r5;     // Catch: ClassCastException -> L35 Throwable -> L39
        sBaseCache = (Object[]) r5[0];     // Catch: ClassCastException -> L35 Throwable -> L39
        this.mHashes = (int[]) r5[1];     // Catch: ClassCastException -> L35 Throwable -> L39
        if (this.mHashes == null) goto L36;
        r5[1] = null;     // Catch: ClassCastException -> L35 Throwable -> L39
        r5[0] = null;     // Catch: ClassCastException -> L35 Throwable -> L39
        sBaseCacheSize--;     // Catch: ClassCastException -> L35 Throwable -> L39
        monitor-exit(r1);     // Catch: Throwable -> L39
        return;
    L36:
        System.out.println("ArraySet Found corrupt ArraySet cache: [0]=" + r5[0] + " [1]=" + r5[1]);     // Catch: Throwable -> L39
        sBaseCache = null;     // Catch: Throwable -> L39
        sBaseCacheSize = 0;     // Catch: Throwable -> L39
    L37:
        monitor-exit(r1);     // Catch: Throwable -> L39
        goto L42
    }

    private int binarySearch(int r3) {
        return ContainerHelpers.binarySearch(this.mHashes, this.mSize, r3);
    L6:
        throw new ConcurrentModificationException();
    }

    private static void freeArrays(int[] r7, Object[] r8, int r9) {
        if (r7.length != 8) goto L18;
        Object r0 = sTwiceBaseCacheLock;
        monitor-enter(r0);
    L14:
        th = move-exception;
        throw th;
    L7:
        if (sTwiceBaseCacheSize >= 10) goto L12;
        r8[0] = sTwiceBaseCache;     // Catch: Throwable -> L14
        r8[1] = r7;     // Catch: Throwable -> L14
        int r1 = r9 - 1;     // Catch: Throwable -> L14
    L9:
        if (r1 < 2) goto L11;
        r8[r1] = null;     // Catch: Throwable -> L14
        r1 = r1 - 1;     // Catch: Throwable -> L14
        goto L9
    L11:
        sTwiceBaseCache = r8;     // Catch: Throwable -> L14
        sTwiceBaseCacheSize++;     // Catch: Throwable -> L14
    L12:
        monitor-exit(r0);     // Catch: Throwable -> L14
        return;
    L18:
        if (r7.length != 4) goto L39;
        Object r2 = sBaseCacheLock;
        monitor-enter(r2);
    L29:
        th = move-exception;
        throw th;
    L22:
        if (sBaseCacheSize >= 10) goto L27;
        r8[0] = sBaseCache;     // Catch: Throwable -> L29
        r8[1] = r7;     // Catch: Throwable -> L29
        int r3 = r9 - 1;     // Catch: Throwable -> L29
    L24:
        if (r3 < 2) goto L26;
        r8[r3] = null;     // Catch: Throwable -> L29
        r3 = r3 - 1;     // Catch: Throwable -> L29
        goto L24
    L26:
        sBaseCache = r8;     // Catch: Throwable -> L29
        sBaseCacheSize++;     // Catch: Throwable -> L29
    L27:
        monitor-exit(r2);     // Catch: Throwable -> L29
        return;
    }

    private int indexOf(Object r6, int r7) {
        int r0 = this.mSize;
        if (r0 != 0) goto L6;
        return -1;
    L6:
        int r1 = binarySearch(r7);
        if (r1 >= 0) goto L10;
        return r1;
    L10:
        if (r6.equals(this.mArray[r1]) == false) goto L12;
        return r1;
    L12:
        int r2 = r1 + 1;
    L13:
        if (r2 >= r0) goto L20;
        if (this.mHashes[r2] != r7) goto L20;
        if (r6.equals(this.mArray[r2]) == true) goto L18;
        r2 = r2 + 1;
        goto L13
    L18:
        return r2;
    L20:
        int r3 = r1 - 1;
    L21:
        if (r3 < 0) goto L29;
        if (this.mHashes[r3] != r7) goto L29;
        if (r6.equals(this.mArray[r3]) == true) goto L26;
        r3 = r3 - 1;
        goto L21
    L26:
        return r3;
    L29:
        return ~r2;
    }

    private int indexOfNull() {
        int r0 = this.mSize;
        if (r0 != 0) goto L6;
        return -1;
    L6:
        int r1 = binarySearch(0);
        if (r1 >= 0) goto L10;
        return r1;
    L10:
        if (this.mArray[r1] != null) goto L12;
        return r1;
    L12:
        int r2 = r1 + 1;
    L13:
        if (r2 >= r0) goto L20;
        if (this.mHashes[r2] != 0) goto L20;
        if (this.mArray[r2] == null) goto L18;
        r2 = r2 + 1;
        goto L13
    L18:
        return r2;
    L20:
        int r3 = r1 - 1;
    L21:
        if (r3 < 0) goto L29;
        if (this.mHashes[r3] != 0) goto L29;
        if (this.mArray[r3] == null) goto L26;
        r3 = r3 - 1;
        goto L21
    L26:
        return r3;
    L29:
        return ~r2;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean add(E r10) {
        int r0 = this.mSize;
        if (r10 != null) goto L5;
        int r1 = 0;
        int r2 = indexOfNull();
    L7:
        if (r2 < 0) goto L9;
        return false;
    L9:
        int r3 = ~r2;
        if (r0 < this.mHashes.length) goto L26;
        int r4 = 8;
        if (r0 < 8) goto L15;
        r4 = (r0 >> 1) + r0;
    L18:
        int[] r5 = this.mHashes;
        Object[] r6 = this.mArray;
        allocArrays(r4);
        if (r0 != this.mSize) goto L25;
        if (this.mHashes.length <= 0) goto L23;
        System.arraycopy(r5, 0, this.mHashes, 0, r5.length);
        System.arraycopy(r6, 0, this.mArray, 0, r6.length);
    L23:
        freeArrays(r5, r6, r0);
        goto L26
    L25:
        throw new ConcurrentModificationException();
    L15:
        if (r0 >= 4) goto L18;
        r4 = 4;
    L26:
        if (r3 >= r0) goto L29;
        System.arraycopy(this.mHashes, r3, this.mHashes, r3 + 1, r0 - r3);
        System.arraycopy(this.mArray, r3, this.mArray, r3 + 1, r0 - r3);
    L29:
        if (r0 != this.mSize) goto L35;
        if (r3 >= this.mHashes.length) goto L35;
        this.mHashes[r3] = r1;
        this.mArray[r3] = r10;
        this.mSize++;
        return true;
    L35:
        throw new ConcurrentModificationException();
    L5:
        r1 = r10.hashCode();
        r2 = indexOf(r10, r1);
        goto L7
    }

    public void addAll(ArraySet<? extends E> r5) {
        int r0 = r5.mSize;
        ensureCapacity(this.mSize + r0);
        if (this.mSize != 0) goto L10;
        if (r0 <= 0) goto L13;
        System.arraycopy(r5.mHashes, 0, this.mHashes, 0, r0);
        System.arraycopy(r5.mArray, 0, this.mArray, 0, r0);
        if (this.mSize != 0) goto L9;
        this.mSize = r0;
        return;
    L9:
        throw new ConcurrentModificationException();
    L13:
        return;
    L10:
        int r1 = 0;
    L11:
        if (r1 >= r0) goto L16;
        add(r5.valueAt(r1));
        r1 = r1 + 1;
        goto L11
    }

    @Override // java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends E> r5) {
        ensureCapacity(this.mSize + r5.size());
        boolean r0 = false;
        Iterator<? extends E> r1 = r5.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0 = r0 | add(r1.next());
        goto L4
    L6:
        return r0;
    }

    @Override // java.util.Collection, java.util.Set
    public void clear() {
        if (this.mSize == 0) goto L6;
        int[] r0 = this.mHashes;
        Object[] r1 = this.mArray;
        int r2 = this.mSize;
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        this.mSize = 0;
        freeArrays(r0, r1, r2);
    L6:
        if (this.mSize != 0) goto L9;
        return;
    L9:
        throw new ConcurrentModificationException();
    }

    @Override // java.util.Collection, java.util.Set
    public boolean contains(Object r2) {
        if (indexOf(r2) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> r4) {
        Iterator<?> r0 = r4.iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (contains(r0.next()) == true) goto L4;
        return false;
    L10:
        return true;
    }

    public void ensureCapacity(int r7) {
        int r0 = this.mSize;
        if (this.mHashes.length >= r7) goto L9;
        int[] r1 = this.mHashes;
        Object[] r2 = this.mArray;
        allocArrays(r7);
        if (this.mSize <= 0) goto L7;
        System.arraycopy(r1, 0, this.mHashes, 0, this.mSize);
        System.arraycopy(r2, 0, this.mArray, 0, this.mSize);
    L7:
        freeArrays(r1, r2, this.mSize);
    L9:
        if (this.mSize != r0) goto L12;
        return;
    L12:
        throw new ConcurrentModificationException();
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object r7) {
        if (this != r7) goto L6;
        return true;
    L6:
        if ((r7 instanceof Set) == false) goto L23;
        Set r1 = (Set) r7;
        if (size() == r1.size()) goto L10;
        return false;
    L10:
        int r3 = 0;
    L24:
        if (r3 >= this.mSize) goto L18;
        if (r1.contains(valueAt(r3)) == false) goto L15;
        r3 = r3 + 1;
        goto L24
    L15:
        return false;
    L18:
        return true;
    L20:
        return false;
    L22:
        return false;
    L23:
        return false;
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        int[] r0 = this.mHashes;
        int r1 = 0;
        int r2 = 0;
        int r3 = this.mSize;
    L3:
        if (r2 >= r3) goto L5;
        r1 = r1 + r0[r2];
        r2 = r2 + 1;
        goto L3
    L5:
        return r1;
    }

    public int indexOf(Object r2) {
        if (r2 != null) goto L5;
        return indexOfNull();
    L5:
        return indexOf(r2, r2.hashCode());
    }

    @Override // java.util.Collection, java.util.Set
    public boolean isEmpty() {
        if (this.mSize > 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return new ElementIterator(this);
    }

    @Override // java.util.Collection, java.util.Set
    public boolean remove(Object r3) {
        int r0 = indexOf(r3);
        if (r0 < 0) goto L6;
        removeAt(r0);
        return true;
    L6:
        return false;
    }

    public boolean removeAll(ArraySet<? extends E> r5) {
        int r0 = r5.mSize;
        int r1 = this.mSize;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L6;
        remove(r5.valueAt(r2));
        r2 = r2 + 1;
        goto L3
    L6:
        if (r1 == this.mSize) goto L8;
        return true;
    L8:
        return false;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean removeAll(Collection<?> r5) {
        boolean r0 = false;
        Iterator<?> r1 = r5.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0 = r0 | remove(r1.next());
        goto L4
    L6:
        return r0;
    }

    public E removeAt(int r10) {
        int r0 = this.mSize;
        E r1 = (E) this.mArray[r10];
        if (r0 > 1) goto L5;
        clear();
    L24:
        return r1;
    L5:
        int r3 = r0 - 1;
        int r5 = 8;
        if (this.mHashes.length > 8) goto L8;
    L18:
        if (r10 >= r3) goto L20;
        System.arraycopy(this.mHashes, r10 + 1, this.mHashes, r10, r3 - r10);
        System.arraycopy(this.mArray, r10 + 1, this.mArray, r10, r3 - r10);
    L20:
        this.mArray[r3] = null;
    L22:
        if (r0 != this.mSize) goto L26;
        this.mSize = r3;
        goto L24
    L26:
        throw new ConcurrentModificationException();
    L8:
        if (this.mSize >= (this.mHashes.length / 3)) goto L18;
        if (this.mSize <= 8) goto L12;
        r5 = this.mSize + (this.mSize >> 1);
    L12:
        int[] r2 = this.mHashes;
        Object[] r4 = this.mArray;
        allocArrays(r5);
        if (r10 <= 0) goto L15;
        System.arraycopy(r2, 0, this.mHashes, 0, r10);
        System.arraycopy(r4, 0, this.mArray, 0, r10);
    L15:
        if (r10 >= r3) goto L22;
        System.arraycopy(r2, r10 + 1, this.mHashes, r10, r3 - r10);
        System.arraycopy(r4, r10 + 1, this.mArray, r10, r3 - r10);
        goto L22
    }

    @Override // java.util.Collection, java.util.Set
    public boolean retainAll(Collection<?> r4) {
        boolean r0 = false;
        int r1 = this.mSize - 1;
    L3:
        if (r1 < 0) goto L8;
        if (r4.contains(this.mArray[r1]) == true) goto L7;
        removeAt(r1);
        r0 = true;
    L7:
        r1 = r1 - 1;
        goto L3
    L8:
        return r0;
    }

    @Override // java.util.Collection, java.util.Set
    public int size() {
        return this.mSize;
    }

    @Override // java.util.Collection, java.util.Set
    public Object[] toArray() {
        Object[] r0 = new Object[this.mSize];
        System.arraycopy(this.mArray, 0, r0, 0, this.mSize);
        return r0;
    }

    @Override // java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] r4) {
        if (r4.length >= this.mSize) goto L5;
        r4 = (T[]) ((Object[]) Array.newInstance(r4.getClass().getComponentType(), this.mSize));
    L5:
        T[] r5 = r4;
        System.arraycopy(this.mArray, 0, r5, 0, this.mSize);
        if (r4.length <= this.mSize) goto L8;
        T[] r6 = r4;
        r6[this.mSize] = null;
    L8:
        return r4;
    }

    public String toString() {
        if (isEmpty() == false) goto L6;
        return "{}";
    L6:
        StringBuilder r0 = new StringBuilder(this.mSize * 14);
        r0.append('{');
        int r1 = 0;
    L8:
        if (r1 >= this.mSize) goto L16;
        if (r1 <= 0) goto L11;
        r0.append(", ");
    L11:
        E r2 = valueAt(r1);
        if (r2 == this) goto L14;
        r0.append(r2);
    L15:
        r1 = r1 + 1;
        goto L8
    L14:
        r0.append("(this Set)");
        goto L15
    L16:
        r0.append('}');
        return r0.toString();
    }

    public E valueAt(int r2) {
        return (E) this.mArray[r2];
    }
}
