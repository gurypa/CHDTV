package androidx.collection;

/* JADX INFO: loaded from: classes.dex */
public final class CircularArray<E> {
    private int mCapacityBitmask;
    private E[] mElements;
    private int mHead;
    private int mTail;

    public CircularArray() {
        this(8);
    }

    public CircularArray(int r3) {
        if (r3 < 1) goto L15;
        if (r3 > 1073741824) goto L13;
        if (Integer.bitCount(r3) == 1) goto L9;
        int r0 = Integer.highestOneBit(r3 - 1) << 1;
    L10:
        this.mCapacityBitmask = r0 - 1;
        this.mElements = (E[]) new Object[r0];
        return;
    L9:
        r0 = r3;
        goto L10
    L13:
        throw new IllegalArgumentException("capacity must be <= 2^30");
    L15:
        throw new IllegalArgumentException("capacity must be >= 1");
    }

    private void doubleCapacity() {
        int r0 = this.mElements.length;
        int r1 = r0 - this.mHead;
        int r2 = r0 << 1;
        if (r2 < 0) goto L7;
        E[] r3 = (E[]) new Object[r2];
        System.arraycopy(this.mElements, this.mHead, r3, 0, r1);
        System.arraycopy(this.mElements, 0, r3, r1, this.mHead);
        this.mElements = r3;
        this.mHead = 0;
        this.mTail = r0;
        this.mCapacityBitmask = r2 - 1;
        return;
    L7:
        throw new RuntimeException("Max array capacity exceeded");
    }

    public void addFirst(E r3) {
        this.mHead = (this.mHead - 1) & this.mCapacityBitmask;
        this.mElements[this.mHead] = r3;
        if (this.mHead != this.mTail) goto L6;
        doubleCapacity();
        return;
    }

    public void addLast(E r3) {
        this.mElements[this.mTail] = r3;
        this.mTail = (this.mTail + 1) & this.mCapacityBitmask;
        if (this.mTail != this.mHead) goto L6;
        doubleCapacity();
        return;
    }

    public void clear() {
        removeFromStart(size());
    }

    public E get(int r4) {
        if (r4 < 0) goto L8;
        if (r4 >= size()) goto L8;
        return this.mElements[(this.mHead + r4) & this.mCapacityBitmask];
    L8:
        throw new ArrayIndexOutOfBoundsException();
    }

    public E getFirst() {
        if (this.mHead == this.mTail) goto L7;
        return this.mElements[this.mHead];
    L7:
        throw new ArrayIndexOutOfBoundsException();
    }

    public E getLast() {
        if (this.mHead == this.mTail) goto L7;
        return this.mElements[(this.mTail - 1) & this.mCapacityBitmask];
    L7:
        throw new ArrayIndexOutOfBoundsException();
    }

    public boolean isEmpty() {
        if (this.mHead != this.mTail) goto L5;
        return true;
    L5:
        return false;
    }

    public E popFirst() {
        if (this.mHead == this.mTail) goto L7;
        E r0 = this.mElements[this.mHead];
        this.mElements[this.mHead] = null;
        this.mHead = (this.mHead + 1) & this.mCapacityBitmask;
        return r0;
    L7:
        throw new ArrayIndexOutOfBoundsException();
    }

    public E popLast() {
        if (this.mHead == this.mTail) goto L7;
        int r0 = (this.mTail - 1) & this.mCapacityBitmask;
        E r1 = this.mElements[r0];
        this.mElements[r0] = null;
        this.mTail = r0;
        return r1;
    L7:
        throw new ArrayIndexOutOfBoundsException();
    }

    public void removeFromEnd(int r7) {
        if (r7 > 0) goto L5;
        return;
    L5:
        if (r7 > size()) goto L22;
        int r0 = 0;
        if (r7 >= this.mTail) goto L9;
        r0 = this.mTail - r7;
    L9:
        int r1 = r0;
    L11:
        if (r1 >= this.mTail) goto L13;
        this.mElements[r1] = null;
        r1 = r1 + 1;
        goto L11
    L13:
        int r2 = this.mTail - r0;
        int r8 = r7 - r2;
        this.mTail -= r2;
        if (r8 <= 0) goto L25;
        this.mTail = this.mElements.length;
        int r3 = this.mTail - r8;
        int r4 = r3;
    L17:
        if (r4 >= this.mTail) goto L19;
        this.mElements[r4] = null;
        r4 = r4 + 1;
        goto L17
    L19:
        this.mTail = r3;
        return;
    L25:
        return;
    L22:
        throw new ArrayIndexOutOfBoundsException();
    }

    public void removeFromStart(int r6) {
        if (r6 > 0) goto L5;
        return;
    L5:
        if (r6 > size()) goto L21;
        int r0 = this.mElements.length;
        if (r6 >= (r0 - this.mHead)) goto L9;
        r0 = this.mHead + r6;
    L9:
        int r1 = this.mHead;
    L11:
        if (r1 >= r0) goto L13;
        this.mElements[r1] = null;
        r1 = r1 + 1;
        goto L11
    L13:
        int r2 = r0 - this.mHead;
        int r7 = r6 - r2;
        this.mHead = (this.mHead + r2) & this.mCapacityBitmask;
        if (r7 <= 0) goto L24;
        int r3 = 0;
    L16:
        if (r3 >= r7) goto L18;
        this.mElements[r3] = null;
        r3 = r3 + 1;
        goto L16
    L18:
        this.mHead = r7;
        return;
    L24:
        return;
    L21:
        throw new ArrayIndexOutOfBoundsException();
    }

    public int size() {
        return (this.mTail - this.mHead) & this.mCapacityBitmask;
    }
}
