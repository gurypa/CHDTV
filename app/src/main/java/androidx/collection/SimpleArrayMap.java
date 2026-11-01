package androidx.collection;

import java.util.ConcurrentModificationException;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SimpleArrayMap<K, V> {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean CONCURRENT_MODIFICATION_EXCEPTIONS = true;
    private static final boolean DEBUG = false;
    private static final String TAG = "ArrayMap";
    static Object[] mBaseCache;
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;
    static int mTwiceBaseCacheSize;
    Object[] mArray;
    int[] mHashes;
    int mSize;

    public SimpleArrayMap() {
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        this.mSize = 0;
    }

    public SimpleArrayMap(int r2) {
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
    public SimpleArrayMap(SimpleArrayMap<K, V> r1) {
        this();
        if (r1 == 0) goto L6;
        putAll(r1);
        return;
    }

    private void allocArrays(int r7) {
        if (r7 != 8) goto L17;
        monitor-enter(SimpleArrayMap.class);
    L13:
        th = move-exception;
        throw th;
    L7:
        if (mTwiceBaseCache == null) goto L11;
        Object[] r4 = mTwiceBaseCache;     // Catch: Throwable -> L13
        this.mArray = r4;     // Catch: Throwable -> L13
        mTwiceBaseCache = (Object[]) r4[0];     // Catch: Throwable -> L13
        this.mHashes = (int[]) r4[1];     // Catch: Throwable -> L13
        r4[1] = null;     // Catch: Throwable -> L13
        r4[0] = null;     // Catch: Throwable -> L13
        mTwiceBaseCacheSize--;     // Catch: Throwable -> L13
        monitor-exit(SimpleArrayMap.class);     // Catch: Throwable -> L13
        return;
    L11:
        monitor-exit(SimpleArrayMap.class);     // Catch: Throwable -> L13
    L30:
        this.mHashes = new int[r7];
        this.mArray = new Object[r7 << 1];
        return;
    L17:
        if (r7 != 4) goto L30;
        monitor-enter(SimpleArrayMap.class);
    L27:
        th = move-exception;
        throw th;
    L21:
        if (mBaseCache == null) goto L25;
        Object[] r5 = mBaseCache;     // Catch: Throwable -> L27
        this.mArray = r5;     // Catch: Throwable -> L27
        mBaseCache = (Object[]) r5[0];     // Catch: Throwable -> L27
        this.mHashes = (int[]) r5[1];     // Catch: Throwable -> L27
        r5[1] = null;     // Catch: Throwable -> L27
        r5[0] = null;     // Catch: Throwable -> L27
        mBaseCacheSize--;     // Catch: Throwable -> L27
        monitor-exit(SimpleArrayMap.class);     // Catch: Throwable -> L27
        return;
    L25:
        monitor-exit(SimpleArrayMap.class);     // Catch: Throwable -> L27
        goto L30
    }

    private static int binarySearchHashes(int[] r2, int r3, int r4) {
        return ContainerHelpers.binarySearch(r2, r3, r4);
    L6:
        throw new ConcurrentModificationException();
    }

    private static void freeArrays(int[] r7, Object[] r8, int r9) {
        if (r7.length != 8) goto L18;
        monitor-enter(SimpleArrayMap.class);
    L14:
        th = move-exception;
        throw th;
    L7:
        if (mTwiceBaseCacheSize >= 10) goto L12;
        r8[0] = mTwiceBaseCache;     // Catch: Throwable -> L14
        r8[1] = r7;     // Catch: Throwable -> L14
        int r1 = (r9 << 1) - 1;     // Catch: Throwable -> L14
    L9:
        if (r1 < 2) goto L11;
        r8[r1] = null;     // Catch: Throwable -> L14
        r1 = r1 - 1;     // Catch: Throwable -> L14
        goto L9
    L11:
        mTwiceBaseCache = r8;     // Catch: Throwable -> L14
        mTwiceBaseCacheSize++;     // Catch: Throwable -> L14
    L12:
        monitor-exit(SimpleArrayMap.class);     // Catch: Throwable -> L14
        return;
    L18:
        if (r7.length == 4) goto L20;
        return;
    L20:
        monitor-enter(SimpleArrayMap.class);
    L29:
        th = move-exception;
        throw th;
    L22:
        if (mBaseCacheSize >= 10) goto L27;
        r8[0] = mBaseCache;     // Catch: Throwable -> L29
        r8[1] = r7;     // Catch: Throwable -> L29
        int r2 = (r9 << 1) - 1;     // Catch: Throwable -> L29
    L24:
        if (r2 < 2) goto L26;
        r8[r2] = null;     // Catch: Throwable -> L29
        r2 = r2 - 1;     // Catch: Throwable -> L29
        goto L24
    L26:
        mBaseCache = r8;     // Catch: Throwable -> L29
        mBaseCacheSize++;     // Catch: Throwable -> L29
    L27:
        monitor-exit(SimpleArrayMap.class);     // Catch: Throwable -> L29
    }

    public void clear() {
        if (this.mSize <= 0) goto L6;
        int[] r0 = this.mHashes;
        Object[] r1 = this.mArray;
        int r2 = this.mSize;
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        this.mSize = 0;
        freeArrays(r0, r1, r2);
    L6:
        if (this.mSize > 0) goto L9;
        return;
    L9:
        throw new ConcurrentModificationException();
    }

    public boolean containsKey(Object r2) {
        if (indexOfKey(r2) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean containsValue(Object r2) {
        if (indexOfValue(r2) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public void ensureCapacity(int r7) {
        int r0 = this.mSize;
        if (this.mHashes.length >= r7) goto L9;
        int[] r1 = this.mHashes;
        Object[] r2 = this.mArray;
        allocArrays(r7);
        if (this.mSize <= 0) goto L7;
        System.arraycopy(r1, 0, this.mHashes, 0, r0);
        System.arraycopy(r2, 0, this.mArray, 0, r0 << 1);
    L7:
        freeArrays(r1, r2, r0);
    L9:
        if (this.mSize != r0) goto L12;
        return;
    L12:
        throw new ConcurrentModificationException();
    }

    public boolean equals(Object r9) {
        if (this != r9) goto L49;
        return true;
    L49:
        if ((r9 instanceof SimpleArrayMap) == false) goto L26;
        SimpleArrayMap r2 = (SimpleArrayMap) r9;     // Catch: ClassCastException -> L44 NullPointerException -> L46
        if (size() == r2.size()) goto L11;
        return false;
    L11:
        int r3 = 0;
    L13:
        if (r3 >= this.mSize) goto L24;
        K r4 = keyAt(r3);     // Catch: ClassCastException -> L44 NullPointerException -> L46
        V r5 = valueAt(r3);     // Catch: ClassCastException -> L44 NullPointerException -> L46
        Object r6 = r2.get(r4);     // Catch: ClassCastException -> L44 NullPointerException -> L46
        if (r5 != null) goto L21;
        if (r6 != null) goto L19;
        if (r2.containsKey(r4) == false) goto L19;
    L23:
        r3 = r3 + 1;     // Catch: ClassCastException -> L44 NullPointerException -> L46
    L19:
        return false;
    L21:
        if (r5.equals(r6) == true) goto L23;
        return false;
    L24:
        return true;
    L26:
        if ((r9 instanceof Map) == false) goto L48;
        Map r7 = (Map) r9;     // Catch: ClassCastException -> L44 NullPointerException -> L46
        if (size() == r7.size()) goto L30;
        return false;
    L30:
        int r8 = 0;
    L32:
        if (r8 >= this.mSize) goto L43;
        K r10 = keyAt(r8);     // Catch: ClassCastException -> L44 NullPointerException -> L46
        V r11 = valueAt(r8);     // Catch: ClassCastException -> L44 NullPointerException -> L46
        Object r12 = r7.get(r10);     // Catch: ClassCastException -> L44 NullPointerException -> L46
        if (r11 != null) goto L40;
        if (r12 != null) goto L38;
        if (r7.containsKey(r10) == false) goto L38;
    L42:
        r8 = r8 + 1;
    L38:
        return false;
    L40:
        if (r11.equals(r12) == true) goto L42;
        return false;
    L43:
        return true;
    L48:
        return false;
    }

    public V get(Object r2) {
        return getOrDefault(r2, null);
    }

    public V getOrDefault(Object r4, V r5) {
        int r0 = indexOfKey(r4);
        if (r0 < 0) goto L6;
        return (V) this.mArray[(r0 << 1) + 1];
    L6:
        return r5;
    }

    public int hashCode() {
        int[] r0 = this.mHashes;
        Object[] r1 = this.mArray;
        int r2 = 0;
        int r3 = 0;
        int r4 = 1;
        int r5 = this.mSize;
    L3:
        if (r3 >= r5) goto L9;
        Object r6 = r1[r4];
        int r7 = r0[r3];
        if (r6 != null) goto L7;
        int r8 = 0;
    L8:
        r2 = r2 + (r7 ^ r8);
        r3 = r3 + 1;
        r4 = r4 + 2;
        goto L3
    L7:
        r8 = r6.hashCode();
        goto L8
    L9:
        return r2;
    }

    int indexOf(Object r7, int r8) {
        int r0 = this.mSize;
        if (r0 != 0) goto L6;
        return -1;
    L6:
        int r1 = binarySearchHashes(this.mHashes, r0, r8);
        if (r1 >= 0) goto L10;
        return r1;
    L10:
        if (r7.equals(this.mArray[r1 << 1]) == false) goto L12;
        return r1;
    L12:
        int r2 = r1 + 1;
    L13:
        if (r2 >= r0) goto L20;
        if (this.mHashes[r2] != r8) goto L20;
        if (r7.equals(this.mArray[r2 << 1]) == true) goto L18;
        r2 = r2 + 1;
        goto L13
    L18:
        return r2;
    L20:
        int r3 = r1 - 1;
    L21:
        if (r3 < 0) goto L29;
        if (this.mHashes[r3] != r8) goto L29;
        if (r7.equals(this.mArray[r3 << 1]) == true) goto L26;
        r3 = r3 - 1;
        goto L21
    L26:
        return r3;
    L29:
        return ~r2;
    }

    public int indexOfKey(Object r2) {
        if (r2 != null) goto L5;
        return indexOfNull();
    L5:
        return indexOf(r2, r2.hashCode());
    }

    int indexOfNull() {
        int r0 = this.mSize;
        if (r0 != 0) goto L6;
        return -1;
    L6:
        int r1 = binarySearchHashes(this.mHashes, r0, 0);
        if (r1 >= 0) goto L10;
        return r1;
    L10:
        if (this.mArray[r1 << 1] != null) goto L12;
        return r1;
    L12:
        int r2 = r1 + 1;
    L13:
        if (r2 >= r0) goto L20;
        if (this.mHashes[r2] != 0) goto L20;
        if (this.mArray[r2 << 1] == null) goto L18;
        r2 = r2 + 1;
        goto L13
    L18:
        return r2;
    L20:
        int r3 = r1 - 1;
    L21:
        if (r3 < 0) goto L29;
        if (this.mHashes[r3] != 0) goto L29;
        if (this.mArray[r3 << 1] == null) goto L26;
        r3 = r3 - 1;
        goto L21
    L26:
        return r3;
    L29:
        return ~r2;
    }

    int indexOfValue(Object r5) {
        int r0 = this.mSize * 2;
        Object[] r1 = this.mArray;
        if (r5 != null) goto L12;
        int r2 = 1;
    L5:
        if (r2 >= r0) goto L19;
        if (r1[r2] == null) goto L9;
        r2 = r2 + 2;
        goto L5
    L9:
        return r2 >> 1;
    L19:
        return -1;
    L12:
        int r3 = 1;
    L13:
        if (r3 >= r0) goto L25;
        if (r5.equals(r1[r3]) == true) goto L17;
        r3 = r3 + 2;
        goto L13
    L17:
        return r3 >> 1;
    L25:
        return -1;
    }

    public boolean isEmpty() {
        if (this.mSize > 0) goto L5;
        return true;
    L5:
        return false;
    }

    public K keyAt(int r3) {
        return (K) this.mArray[r3 << 1];
    }

    public V put(K r10, V r11) {
        int r0 = this.mSize;
        if (r10 != null) goto L5;
        int r1 = 0;
        int r2 = indexOfNull();
    L6:
        if (r2 < 0) goto L9;
        int r3 = (r2 << 1) + 1;
        V r4 = (V) this.mArray[r3];
        this.mArray[r3] = r11;
        return r4;
    L9:
        int r5 = ~r2;
        if (r0 < this.mHashes.length) goto L26;
        int r6 = 8;
        if (r0 < 8) goto L15;
        r6 = (r0 >> 1) + r0;
    L18:
        int[] r7 = this.mHashes;
        Object[] r8 = this.mArray;
        allocArrays(r6);
        if (r0 != this.mSize) goto L25;
        if (this.mHashes.length <= 0) goto L23;
        System.arraycopy(r7, 0, this.mHashes, 0, r7.length);
        System.arraycopy(r8, 0, this.mArray, 0, r8.length);
    L23:
        freeArrays(r7, r8, r0);
        goto L26
    L25:
        throw new ConcurrentModificationException();
    L15:
        if (r0 >= 4) goto L18;
        r6 = 4;
    L26:
        if (r5 >= r0) goto L29;
        System.arraycopy(this.mHashes, r5, this.mHashes, r5 + 1, r0 - r5);
        System.arraycopy(this.mArray, r5 << 1, this.mArray, (r5 + 1) << 1, (this.mSize - r5) << 1);
    L29:
        if (r0 != this.mSize) goto L35;
        if (r5 >= this.mHashes.length) goto L35;
        this.mHashes[r5] = r1;
        this.mArray[r5 << 1] = r10;
        this.mArray[(r5 << 1) + 1] = r11;
        this.mSize++;
        return null;
    L35:
        throw new ConcurrentModificationException();
    L5:
        r1 = r10.hashCode();
        r2 = indexOf(r10, r1);
        goto L6
    }

    public void putAll(SimpleArrayMap<? extends K, ? extends V> r6) {
        int r0 = r6.mSize;
        ensureCapacity(this.mSize + r0);
        if (this.mSize != 0) goto L6;
        if (r0 <= 0) goto L9;
        System.arraycopy(r6.mHashes, 0, this.mHashes, 0, r0);
        System.arraycopy(r6.mArray, 0, this.mArray, 0, r0 << 1);
        this.mSize = r0;
        return;
    L9:
        return;
    L6:
        int r1 = 0;
    L7:
        if (r1 >= r0) goto L12;
        put(r6.keyAt(r1), r6.valueAt(r1));
        r1 = r1 + 1;
        goto L7
    }

    public V putIfAbsent(K r2, V r3) {
        V r0 = get(r2);
        if (r0 == null) goto L5;
        return r0;
    L5:
        return put(r2, r3);
    }

    public V remove(Object r3) {
        int r0 = indexOfKey(r3);
        if (r0 >= 0) goto L5;
        return null;
    L5:
        return removeAt(r0);
    }

    public boolean remove(Object r4, Object r5) {
        int r0 = indexOfKey(r4);
        if (r0 < 0) goto L11;
        V r1 = valueAt(r0);
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

    public V removeAt(int r12) {
        V r0 = (V) this.mArray[(r12 << 1) + 1];
        int r1 = this.mSize;
        if (r1 > 1) goto L5;
        clear();
    L26:
        return r0;
    L5:
        int r3 = r1 - 1;
        int r5 = 8;
        if (this.mHashes.length > 8) goto L8;
    L20:
        if (r12 >= r3) goto L22;
        System.arraycopy(this.mHashes, r12 + 1, this.mHashes, r12, r3 - r12);
        System.arraycopy(this.mArray, (r12 + 1) << 1, this.mArray, r12 << 1, (r3 - r12) << 1);
    L22:
        this.mArray[r3 << 1] = null;
        this.mArray[(r3 << 1) + 1] = null;
    L24:
        if (r1 != this.mSize) goto L28;
        this.mSize = r3;
        goto L26
    L28:
        throw new ConcurrentModificationException();
    L8:
        if (r1 >= (this.mHashes.length / 3)) goto L20;
        if (r1 <= 8) goto L11;
        r5 = r1 + (r1 >> 1);
    L11:
        int[] r4 = this.mHashes;
        Object[] r6 = this.mArray;
        allocArrays(r5);
        if (r1 != this.mSize) goto L19;
        if (r12 <= 0) goto L15;
        System.arraycopy(r4, 0, this.mHashes, 0, r12);
        System.arraycopy(r6, 0, this.mArray, 0, r12 << 1);
    L15:
        if (r12 >= r3) goto L24;
        System.arraycopy(r4, r12 + 1, this.mHashes, r12, r3 - r12);
        System.arraycopy(r6, (r12 + 1) << 1, this.mArray, r12 << 1, (r3 - r12) << 1);
        goto L24
    L19:
        throw new ConcurrentModificationException();
    }

    public V replace(K r3, V r4) {
        int r0 = indexOfKey(r3);
        if (r0 >= 0) goto L5;
        return null;
    L5:
        return setValueAt(r0, r4);
    }

    public boolean replace(K r4, V r5, V r6) {
        int r0 = indexOfKey(r4);
        if (r0 < 0) goto L11;
        V r1 = valueAt(r0);
        if (r1 == r5) goto L9;
        if (r5 != null) goto L8;
        return false;
    L8:
        if (r5.equals(r1) == true) goto L9;
        return false;
    L9:
        setValueAt(r0, r6);
        return true;
    L11:
        return false;
    }

    public V setValueAt(int r3, V r4) {
        int r0 = (r3 << 1) + 1;
        V r5 = (V) this.mArray[r0];
        this.mArray[r0] = r4;
        return r5;
    }

    public int size() {
        return this.mSize;
    }

    public String toString() {
        if (isEmpty() == false) goto L6;
        return "{}";
    L6:
        StringBuilder r0 = new StringBuilder(this.mSize * 28);
        r0.append('{');
        int r1 = 0;
    L8:
        if (r1 >= this.mSize) goto L20;
        if (r1 <= 0) goto L11;
        r0.append(", ");
    L11:
        K r2 = keyAt(r1);
        if (r2 == this) goto L14;
        r0.append(r2);
    L15:
        r0.append('=');
        V r4 = valueAt(r1);
        if (r4 == this) goto L18;
        r0.append(r4);
    L19:
        r1 = r1 + 1;
        goto L8
    L18:
        r0.append("(this Map)");
        goto L19
    L14:
        r0.append("(this Map)");
        goto L15
    L20:
        r0.append('}');
        return r0.toString();
    }

    public V valueAt(int r3) {
        return (V) this.mArray[(r3 << 1) + 1];
    }
}
