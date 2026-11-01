package androidx.collection;

/* JADX INFO: loaded from: classes.dex */
public final class CircularIntArray {
    private int mCapacityBitmask;
    private int[] mElements;
    private int mHead;
    private int mTail;

    public CircularIntArray() {
        this(8);
    }

    public CircularIntArray(int r3) {
        if (r3 < 1) goto L15;
        if (r3 > 1073741824) goto L13;
        if (Integer.bitCount(r3) == 1) goto L9;
        int r0 = Integer.highestOneBit(r3 - 1) << 1;
    L10:
        this.mCapacityBitmask = r0 - 1;
        this.mElements = new int[r0];
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
        int[] r3 = new int[r2];
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

    public void addFirst(int r3) {
        this.mHead = (this.mHead - 1) & this.mCapacityBitmask;
        this.mElements[this.mHead] = r3;
        if (this.mHead != this.mTail) goto L6;
        doubleCapacity();
        return;
    }

    public void addLast(int r3) {
        this.mElements[this.mTail] = r3;
        this.mTail = (this.mTail + 1) & this.mCapacityBitmask;
        if (this.mTail != this.mHead) goto L6;
        doubleCapacity();
        return;
    }

    public void clear() {
        this.mTail = this.mHead;
    }

    public int get(int r4) {
        if (r4 < 0) goto L8;
        if (r4 >= size()) goto L8;
        return this.mElements[(this.mHead + r4) & this.mCapacityBitmask];
    L8:
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getFirst() {
        if (this.mHead == this.mTail) goto L7;
        return this.mElements[this.mHead];
    L7:
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getLast() {
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

    public int popFirst() {
        if (this.mHead == this.mTail) goto L7;
        int r0 = this.mElements[this.mHead];
        this.mHead = (this.mHead + 1) & this.mCapacityBitmask;
        return r0;
    L7:
        throw new ArrayIndexOutOfBoundsException();
    }

    public int popLast() {
        if (this.mHead == this.mTail) goto L7;
        int r0 = (this.mTail - 1) & this.mCapacityBitmask;
        int r1 = this.mElements[r0];
        this.mTail = r0;
        return r1;
    L7:
        throw new ArrayIndexOutOfBoundsException();
    }

    public void removeFromEnd(int r3) {
        if (r3 > 0) goto L5;
        return;
    L5:
        if (r3 > size()) goto L9;
        this.mTail = (this.mTail - r3) & this.mCapacityBitmask;
        return;
    L9:
        throw new ArrayIndexOutOfBoundsException();
    }

    public void removeFromStart(int r3) {
        if (r3 > 0) goto L5;
        return;
    L5:
        if (r3 > size()) goto L9;
        this.mHead = (this.mHead + r3) & this.mCapacityBitmask;
        return;
    L9:
        throw new ArrayIndexOutOfBoundsException();
    }

    public int size() {
        return (this.mTail - this.mHead) & this.mCapacityBitmask;
    }
}
