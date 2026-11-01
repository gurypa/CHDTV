package androidx.collection;

/* JADX INFO: loaded from: classes.dex */
public class SparseArrayCompat<E> implements Cloneable {
    private static final Object DELETED = null;
    private boolean mGarbage;
    private int[] mKeys;
    private int mSize;
    private Object[] mValues;

    static {
        DELETED = new Object();
    }

    public SparseArrayCompat() {
        this(10);
    }

    public SparseArrayCompat(int r2) {
        this.mGarbage = false;
        if (r2 != 0) goto L5;
        this.mKeys = ContainerHelpers.EMPTY_INTS;
        this.mValues = ContainerHelpers.EMPTY_OBJECTS;
        return;
    L5:
        int r3 = ContainerHelpers.idealIntArraySize(r2);
        this.mKeys = new int[r3];
        this.mValues = new Object[r3];
    }

    /* JADX INFO: renamed from: gc */
    private void m3gc() {
        int r0 = this.mSize;
        int r1 = 0;
        int[] r2 = this.mKeys;
        Object[] r3 = this.mValues;
        int r4 = 0;
    L3:
        if (r4 >= r0) goto L10;
        Object r5 = r3[r4];
        if (r5 == DELETED) goto L9;
        if (r4 == r1) goto L8;
        r2[r1] = r2[r4];
        r3[r1] = r5;
        r3[r4] = null;
    L8:
        r1 = r1 + 1;
    L9:
        r4 = r4 + 1;
        goto L3
    L10:
        this.mGarbage = false;
        this.mSize = r1;
    }

    public void append(int r8, E r9) {
        if (this.mSize == 0) goto L9;
        if (r8 > this.mKeys[this.mSize - 1]) goto L9;
        put(r8, r9);
        return;
    L9:
        if (this.mGarbage == true) goto L11;
    L13:
        int r0 = this.mSize;
        if (r0 < this.mKeys.length) goto L16;
        int r1 = ContainerHelpers.idealIntArraySize(r0 + 1);
        int[] r2 = new int[r1];
        Object[] r3 = new Object[r1];
        System.arraycopy(this.mKeys, 0, r2, 0, this.mKeys.length);
        System.arraycopy(this.mValues, 0, r3, 0, this.mValues.length);
        this.mKeys = r2;
        this.mValues = r3;
    L16:
        this.mKeys[r0] = r8;
        this.mValues[r0] = r9;
        this.mSize = r0 + 1;
        return;
    L11:
        if (this.mSize < this.mKeys.length) goto L13;
        m3gc();
        goto L13
    }

    public void clear() {
        int r0 = this.mSize;
        Object[] r1 = this.mValues;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        r1[r2] = null;
        r2 = r2 + 1;
        goto L3
    L5:
        this.mSize = 0;
        this.mGarbage = false;
    }

    public SparseArrayCompat<E> clone() {
        SparseArrayCompat<E> r0 = (SparseArrayCompat) super.clone();     // Catch: CloneNotSupportedException -> L4
        r0.mKeys = (int[]) this.mKeys.clone();     // Catch: CloneNotSupportedException -> L4
        r0.mValues = (Object[]) this.mValues.clone();     // Catch: CloneNotSupportedException -> L4
        return r0;
    L4:
        e = move-exception;
        throw new AssertionError(e);
    }

    /* JADX INFO: renamed from: clone, reason: collision with other method in class */
    public /* bridge */ /* synthetic */ Object m451clone() throws CloneNotSupportedException {
        return clone();
    }

    public boolean containsKey(int r2) {
        if (indexOfKey(r2) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean containsValue(E r2) {
        if (indexOfValue(r2) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Deprecated
    public void delete(int r1) {
        remove(r1);
    }

    public E get(int r2) {
        return get(r2, null);
    }

    public E get(int r4, E r5) {
        int r0 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, r4);
        if (r0 >= 0) goto L5;
    L9:
        return r5;
    L5:
        if (this.mValues[r0] == DELETED) goto L9;
        return (E) this.mValues[r0];
    }

    public int indexOfKey(int r3) {
        if (this.mGarbage == false) goto L6;
        m3gc();
    L6:
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, r3);
    }

    public int indexOfValue(E r3) {
        if (this.mGarbage == false) goto L5;
        m3gc();
    L5:
        int r0 = 0;
    L7:
        if (r0 >= this.mSize) goto L12;
        if (this.mValues[r0] == r3) goto L10;
        r0 = r0 + 1;
        goto L7
    L10:
        return r0;
    L12:
        return -1;
    }

    public boolean isEmpty() {
        if (size() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    public int keyAt(int r2) {
        if (this.mGarbage == false) goto L6;
        m3gc();
    L6:
        return this.mKeys[r2];
    }

    public void put(int r8, E r9) {
        int r0 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, r8);
        if (r0 < 0) goto L5;
        this.mValues[r0] = r9;
        return;
    L5:
        int r1 = ~r0;
        if (r1 >= this.mSize) goto L12;
        if (this.mValues[r1] != DELETED) goto L12;
        this.mKeys[r1] = r8;
        this.mValues[r1] = r9;
        return;
    L12:
        if (this.mGarbage == false) goto L17;
        if (this.mSize < this.mKeys.length) goto L17;
        m3gc();
        r1 = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, r8);
    L17:
        if (this.mSize < this.mKeys.length) goto L20;
        int r2 = ContainerHelpers.idealIntArraySize(this.mSize + 1);
        int[] r3 = new int[r2];
        Object[] r4 = new Object[r2];
        System.arraycopy(this.mKeys, 0, r3, 0, this.mKeys.length);
        System.arraycopy(this.mValues, 0, r4, 0, this.mValues.length);
        this.mKeys = r3;
        this.mValues = r4;
    L20:
        if ((this.mSize - r1) == 0) goto L22;
        System.arraycopy(this.mKeys, r1, this.mKeys, r1 + 1, this.mSize - r1);
        System.arraycopy(this.mValues, r1, this.mValues, r1 + 1, this.mSize - r1);
    L22:
        this.mKeys[r1] = r8;
        this.mValues[r1] = r9;
        this.mSize++;
    }

    public void putAll(SparseArrayCompat<? extends E> r5) {
        int r0 = 0;
        int r1 = r5.size();
    L3:
        if (r0 >= r1) goto L5;
        put(r5.keyAt(r0), r5.valueAt(r0));
        r0 = r0 + 1;
        goto L3
    }

    public E putIfAbsent(int r2, E r3) {
        E r0 = get(r2);
        if (r0 != null) goto L5;
        put(r2, r3);
    L5:
        return r0;
    }

    public void remove(int r4) {
        int r0 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, r4);
        if (r0 >= 0) goto L5;
        return;
    L5:
        if (this.mValues[r0] == DELETED) goto L9;
        this.mValues[r0] = DELETED;
        this.mGarbage = true;
        return;
    }

    public boolean remove(int r4, Object r5) {
        int r0 = indexOfKey(r4);
        if (r0 < 0) goto L11;
        E r1 = valueAt(r0);
        if (r5 == r1) goto L9;
        if (r5 != null) goto L8;
        return false;
    L8:
        if (r5.equals(r1) == true) goto L9;
        return false;
    L9:
        removeAt(r0);
        return true;
    L11:
        return false;
    }

    public void removeAt(int r3) {
        if (this.mValues[r3] == DELETED) goto L6;
        this.mValues[r3] = DELETED;
        this.mGarbage = true;
        return;
    }

    public void removeAtRange(int r3, int r4) {
        int r0 = Math.min(this.mSize, r3 + r4);
        int r1 = r3;
    L3:
        if (r1 >= r0) goto L5;
        removeAt(r1);
        r1 = r1 + 1;
        goto L3
    }

    public E replace(int r4, E r5) {
        int r0 = indexOfKey(r4);
        if (r0 < 0) goto L6;
        E r1 = (E) this.mValues[r0];
        this.mValues[r0] = r5;
        return r1;
    L6:
        return null;
    }

    public boolean replace(int r4, E r5, E r6) {
        int r0 = indexOfKey(r4);
        if (r0 < 0) goto L11;
        Object r1 = this.mValues[r0];
        if (r1 == r5) goto L9;
        if (r5 != null) goto L8;
        return false;
    L8:
        if (r5.equals(r1) == true) goto L9;
        return false;
    L9:
        this.mValues[r0] = r6;
        return true;
    L11:
        return false;
    }

    public void setValueAt(int r2, E r3) {
        if (this.mGarbage == false) goto L5;
        m3gc();
    L5:
        this.mValues[r2] = r3;
    }

    public int size() {
        if (this.mGarbage == false) goto L6;
        m3gc();
    L6:
        return this.mSize;
    }

    public String toString() {
        if (size() > 0) goto L6;
        return "{}";
    L6:
        StringBuilder r0 = new StringBuilder(this.mSize * 28);
        r0.append('{');
        int r1 = 0;
    L8:
        if (r1 >= this.mSize) goto L16;
        if (r1 <= 0) goto L11;
        r0.append(", ");
    L11:
        r0.append(keyAt(r1));
        r0.append('=');
        E r3 = valueAt(r1);
        if (r3 == this) goto L14;
        r0.append(r3);
    L15:
        r1 = r1 + 1;
        goto L8
    L14:
        r0.append("(this Map)");
        goto L15
    L16:
        r0.append('}');
        return r0.toString();
    }

    public E valueAt(int r2) {
        if (this.mGarbage == false) goto L6;
        m3gc();
    L6:
        return (E) this.mValues[r2];
    }
}
