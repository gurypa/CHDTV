package androidx.recyclerview.widget;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
public class SortedList<T> {
    private static final int CAPACITY_GROWTH = 10;
    private static final int DELETION = 2;
    private static final int INSERTION = 1;
    public static final int INVALID_POSITION = -1;
    private static final int LOOKUP = 4;
    private static final int MIN_CAPACITY = 10;
    private BatchedCallback mBatchedCallback;
    private Callback mCallback;
    T[] mData;
    private int mNewDataStart;
    private T[] mOldData;
    private int mOldDataSize;
    private int mOldDataStart;
    private int mSize;
    private final Class<T> mTClass;

    public static class BatchedCallback<T2> extends Callback<T2> {
        private final BatchingListUpdateCallback mBatchingListUpdateCallback;
        final Callback<T2> mWrappedCallback;

        public BatchedCallback(Callback<T2> r3) {
            this.mWrappedCallback = r3;
            this.mBatchingListUpdateCallback = new BatchingListUpdateCallback(this.mWrappedCallback);
        }

        @Override // androidx.recyclerview.widget.SortedList.Callback
        public boolean areContentsTheSame(T2 r2, T2 r3) {
            return this.mWrappedCallback.areContentsTheSame(r2, r3);
        }

        @Override // androidx.recyclerview.widget.SortedList.Callback
        public boolean areItemsTheSame(T2 r2, T2 r3) {
            return this.mWrappedCallback.areItemsTheSame(r2, r3);
        }

        @Override // androidx.recyclerview.widget.SortedList.Callback, java.util.Comparator
        public int compare(T2 r2, T2 r3) {
            return this.mWrappedCallback.compare(r2, r3);
        }

        public void dispatchLastEvent() {
            this.mBatchingListUpdateCallback.dispatchLastEvent();
        }

        @Override // androidx.recyclerview.widget.SortedList.Callback
        public Object getChangePayload(T2 r2, T2 r3) {
            return this.mWrappedCallback.getChangePayload(r2, r3);
        }

        @Override // androidx.recyclerview.widget.SortedList.Callback
        public void onChanged(int r3, int r4) {
            this.mBatchingListUpdateCallback.onChanged(r3, r4, null);
        }

        @Override // androidx.recyclerview.widget.SortedList.Callback, androidx.recyclerview.widget.ListUpdateCallback
        public void onChanged(int r2, int r3, Object r4) {
            this.mBatchingListUpdateCallback.onChanged(r2, r3, r4);
        }

        @Override // androidx.recyclerview.widget.ListUpdateCallback
        public void onInserted(int r2, int r3) {
            this.mBatchingListUpdateCallback.onInserted(r2, r3);
        }

        @Override // androidx.recyclerview.widget.ListUpdateCallback
        public void onMoved(int r2, int r3) {
            this.mBatchingListUpdateCallback.onMoved(r2, r3);
        }

        @Override // androidx.recyclerview.widget.ListUpdateCallback
        public void onRemoved(int r2, int r3) {
            this.mBatchingListUpdateCallback.onRemoved(r2, r3);
        }
    }

    public static abstract class Callback<T2> implements Comparator<T2>, ListUpdateCallback {
        public Callback() {
        }

        public abstract boolean areContentsTheSame(T2 r1, T2 r2);

        public abstract boolean areItemsTheSame(T2 r1, T2 r2);

        @Override // java.util.Comparator
        public abstract int compare(T2 r1, T2 r2);

        public Object getChangePayload(T2 r2, T2 r3) {
            return null;
        }

        public abstract void onChanged(int r1, int r2);

        public void onChanged(int r1, int r2, Object r3) {
            onChanged(r1, r2);
        }
    }

    public SortedList(Class<T> r2, Callback<T> r3) {
        this(r2, r3, 10);
    }

    public SortedList(Class<T> r2, Callback<T> r3, int r4) {
        this.mTClass = r2;
        this.mData = (T[]) ((Object[]) Array.newInstance(r2, r4));
        this.mCallback = r3;
        this.mSize = 0;
    }

    private int add(T r7, boolean r8) {
        int r9 = findIndexOf(r7, this.mData, 0, this.mSize, 1);
        if (r9 != (-1)) goto L6;
        r9 = 0;
    L15:
        addToData(r9, r7);
        if (r8 == false) goto L18;
        this.mCallback.onInserted(r9, 1);
    L18:
        return r9;
    L6:
        if (r9 >= this.mSize) goto L15;
        T r2 = this.mData[r9];
        if (this.mCallback.areItemsTheSame(r2, r7) == false) goto L15;
        if (this.mCallback.areContentsTheSame(r2, r7) == false) goto L13;
        this.mData[r9] = r7;
        return r9;
    L13:
        this.mData[r9] = r7;
        this.mCallback.onChanged(r9, 1, this.mCallback.getChangePayload(r2, r7));
        return r9;
    }

    private void addAllInternal(T[] r4) {
        if (r4.length >= 1) goto L5;
        return;
    L5:
        int r0 = sortAndDedup(r4);
        if (this.mSize != 0) goto L8;
        this.mData = r4;
        this.mSize = r0;
        this.mCallback.onInserted(0, r0);
        return;
    L8:
        merge(r4, r0);
    }

    private void addToData(int r5, T r6) {
        if (r5 > this.mSize) goto L11;
        if (this.mSize != this.mData.length) goto L7;
        T[] r0 = (T[]) ((Object[]) Array.newInstance(this.mTClass, this.mData.length + 10));
        System.arraycopy(this.mData, 0, r0, 0, r5);
        r0[r5] = r6;
        System.arraycopy(this.mData, r5, r0, r5 + 1, this.mSize - r5);
        this.mData = r0;
    L8:
        this.mSize++;
        return;
    L7:
        System.arraycopy(this.mData, r5, this.mData, r5 + 1, this.mSize - r5);
        this.mData[r5] = r6;
        goto L8
    L11:
        throw new IndexOutOfBoundsException("cannot add item to " + r5 + " because size is " + this.mSize);
    }

    private T[] copyArray(T[] r4) {
        T[] r0 = (T[]) ((Object[]) Array.newInstance(this.mTClass, r4.length));
        System.arraycopy(r4, 0, r0, 0, r4.length);
        return r0;
    }

    private int findIndexOf(T r7, T[] r8, int r9, int r10, int r11) {
    L3:
        if (r9 >= r10) goto L20;
        int r2 = (r9 + r10) / 2;
        T r3 = r8[r2];
        int r4 = this.mCallback.compare(r3, r7);
        if (r4 < 0) goto L6;
        if (r4 == 0) goto L9;
        r10 = r2;
        goto L3
    L9:
        if (this.mCallback.areItemsTheSame(r3, r7) == false) goto L11;
        return r2;
    L11:
        int r5 = linearEqualitySearch(r7, r2, r9, r10);
        if (r11 != 1) goto L17;
        if (r5 != (-1)) goto L16;
        return r2;
    L16:
        return r5;
    L17:
        return r5;
    L6:
        r9 = r2 + 1;
        goto L3
    L20:
        if (r11 == 1) goto L22;
        return -1;
    L22:
        return r9;
    }

    private int findSameItem(T r4, T[] r5, int r6, int r7) {
        int r0 = r6;
    L3:
        if (r0 >= r7) goto L8;
        if (this.mCallback.areItemsTheSame(r5[r0], r4) == true) goto L6;
        r0 = r0 + 1;
        goto L3
    L6:
        return r0;
    L8:
        return -1;
    }

    private int linearEqualitySearch(T r5, int r6, int r7, int r8) {
        int r0 = r6 - 1;
    L3:
        if (r0 < r7) goto L11;
        T r1 = this.mData[r0];
        if (this.mCallback.compare(r1, r5) != 0) goto L11;
        if (this.mCallback.areItemsTheSame(r1, r5) == true) goto L9;
        r0 = r0 - 1;
        goto L3
    L9:
        return r0;
    L11:
        int r2 = r6 + 1;
    L12:
        if (r2 >= r8) goto L20;
        T r3 = this.mData[r2];
        if (this.mCallback.compare(r3, r5) != 0) goto L28;
        if (this.mCallback.areItemsTheSame(r3, r5) == true) goto L18;
        r2 = r2 + 1;
        goto L12
    L18:
        return r2;
    L28:
        return -1;
    L20:
        return -1;
    }

    private void merge(T[] r11, int r12) {
        boolean r0 = !(this.mCallback instanceof BatchedCallback);
        if (r0 == false) goto L5;
        beginBatchedUpdates();
    L5:
        this.mOldData = this.mData;
        this.mOldDataStart = 0;
        this.mOldDataSize = this.mSize;
        this.mData = (T[]) ((Object[]) Array.newInstance(this.mTClass, (this.mSize + r12) + 10));
        this.mNewDataStart = 0;
        int r2 = 0;
    L7:
        if (this.mOldDataStart < this.mOldDataSize) goto L10;
        if (r2 < r12) goto L10;
    L14:
        this.mOldData = null;
        if (r0 == false) goto L43;
        endBatchedUpdates();
        return;
    L43:
        return;
    L10:
        if (this.mOldDataStart == this.mOldDataSize) goto L11;
        if (r2 == r12) goto L13;
        T r4 = this.mOldData[this.mOldDataStart];
        T r5 = r11[r2];
        int r6 = this.mCallback.compare(r4, r5);
        if (r6 > 0) goto L20;
        if (r6 != 0) goto L27;
        if (this.mCallback.areItemsTheSame(r4, r5) == false) goto L27;
        T[] r7 = this.mData;
        int r8 = this.mNewDataStart;
        this.mNewDataStart = r8 + 1;
        r7[r8] = r5;
        r2 = r2 + 1;
        this.mOldDataStart++;
        if (this.mCallback.areContentsTheSame(r4, r5) == true) goto L7;
        this.mCallback.onChanged(this.mNewDataStart - 1, 1, this.mCallback.getChangePayload(r4, r5));
    L27:
        T[] r9 = this.mData;
        int r10 = this.mNewDataStart;
        this.mNewDataStart = r10 + 1;
        r9[r10] = r4;
        this.mOldDataStart++;
        goto L7
    L20:
        T[] r13 = this.mData;
        int r14 = this.mNewDataStart;
        this.mNewDataStart = r14 + 1;
        r13[r14] = r5;
        this.mSize++;
        r2 = r2 + 1;
        this.mCallback.onInserted(this.mNewDataStart - 1, 1);
        goto L7
    L13:
        int r1 = this.mOldDataSize - this.mOldDataStart;
        System.arraycopy(this.mOldData, this.mOldDataStart, this.mData, this.mNewDataStart, r1);
        this.mNewDataStart += r1;
        goto L14
    L11:
        int r3 = r12 - r2;
        System.arraycopy(r11, r2, this.mData, this.mNewDataStart, r3);
        this.mNewDataStart += r3;
        this.mSize += r3;
        this.mCallback.onInserted(this.mNewDataStart - r3, r3);
        goto L14
    }

    private boolean remove(T r7, boolean r8) {
        int r9 = findIndexOf(r7, this.mData, 0, this.mSize, 2);
        if (r9 != (-1)) goto L6;
        return false;
    L6:
        removeItemAtIndex(r9, r8);
        return true;
    }

    private void removeItemAtIndex(int r6, boolean r7) {
        System.arraycopy(this.mData, r6 + 1, this.mData, r6, (this.mSize - r6) - 1);
        this.mSize--;
        this.mData[this.mSize] = null;
        if (r7 == false) goto L6;
        this.mCallback.onRemoved(r6, 1);
        return;
    }

    private void replaceAllInsert(T r4) {
        this.mData[this.mNewDataStart] = r4;
        this.mNewDataStart++;
        this.mSize++;
        this.mCallback.onInserted(this.mNewDataStart - 1, 1);
    }

    private void replaceAllInternal(T[] r10) {
        boolean r0 = !(this.mCallback instanceof BatchedCallback);
        if (r0 == false) goto L5;
        beginBatchedUpdates();
    L5:
        this.mOldDataStart = 0;
        this.mOldDataSize = this.mSize;
        this.mOldData = this.mData;
        this.mNewDataStart = 0;
        int r2 = sortAndDedup(r10);
        this.mData = (T[]) ((Object[]) Array.newInstance(this.mTClass, r2));
    L7:
        if (this.mNewDataStart < r2) goto L11;
        if (this.mOldDataStart < this.mOldDataSize) goto L11;
    L16:
        this.mOldData = null;
        if (r0 == false) goto L48;
        endBatchedUpdates();
        return;
    L48:
        return;
    L11:
        if (this.mOldDataStart >= this.mOldDataSize) goto L12;
        if (this.mNewDataStart >= r2) goto L15;
        T r3 = this.mOldData[this.mOldDataStart];
        T r4 = r10[this.mNewDataStart];
        int r5 = this.mCallback.compare(r3, r4);
        if (r5 < 0) goto L22;
        if (r5 > 0) goto L24;
        if (this.mCallback.areItemsTheSame(r3, r4) == false) goto L27;
        this.mData[this.mNewDataStart] = r4;
        this.mOldDataStart++;
        this.mNewDataStart++;
        if (this.mCallback.areContentsTheSame(r3, r4) == true) goto L7;
        this.mCallback.onChanged(this.mNewDataStart - 1, 1, this.mCallback.getChangePayload(r3, r4));
        goto L7
    L27:
        replaceAllRemove();
        replaceAllInsert(r4);
        goto L7
    L24:
        replaceAllInsert(r4);
        goto L7
    L22:
        replaceAllRemove();
        goto L7
    L15:
        int r1 = this.mOldDataSize - this.mOldDataStart;
        this.mSize -= r1;
        this.mCallback.onRemoved(this.mNewDataStart, r1);
        goto L16
    L12:
        int r6 = this.mNewDataStart;
        int r7 = r2 - this.mNewDataStart;
        System.arraycopy(r10, r6, this.mData, r6, r7);
        this.mNewDataStart += r7;
        this.mSize += r7;
        this.mCallback.onInserted(r6, r7);
        goto L16
    }

    private void replaceAllRemove() {
        this.mSize--;
        this.mOldDataStart++;
        this.mCallback.onRemoved(this.mNewDataStart, 1);
    }

    private int sortAndDedup(T[] r8) {
        if (r8.length != 0) goto L6;
        return 0;
    L6:
        Arrays.sort(r8, this.mCallback);
        int r0 = 0;
        int r1 = 1;
        int r2 = 1;
    L8:
        if (r2 >= r8.length) goto L22;
        T r3 = r8[r2];
        if (this.mCallback.compare(r8[r0], r3) != 0) goto L18;
        int r5 = findSameItem(r3, r8, r0, r1);
        if (r5 == (-1)) goto L14;
        r8[r5] = r3;
    L21:
        r2 = r2 + 1;
        goto L8
    L14:
        if (r1 == r2) goto L16;
        r8[r1] = r3;
    L16:
        r1 = r1 + 1;
        goto L21
    L18:
        if (r1 == r2) goto L20;
        r8[r1] = r3;
    L20:
        r0 = r1;
        r1 = r1 + 1;
        goto L21
    L22:
        return r1;
    }

    private void throwIfInMutationOperation() {
        if (this.mOldData != null) goto L6;
        return;
    L6:
        throw new IllegalStateException("Data cannot be mutated in the middle of a batch update operation such as addAll or replaceAll.");
    }

    public int add(T r2) {
        throwIfInMutationOperation();
        return add(r2, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void addAll(Collection<T> r4) {
        addAll(r4.toArray((Object[]) Array.newInstance(this.mTClass, r4.size())), true);
    }

    public void addAll(T... r2) {
        addAll(r2, false);
    }

    public void addAll(T[] r2, boolean r3) {
        throwIfInMutationOperation();
        if (r2.length != 0) goto L5;
        return;
    L5:
        if (r3 == false) goto L7;
        addAllInternal(r2);
        return;
    L7:
        addAllInternal(copyArray(r2));
    }

    public void beginBatchedUpdates() {
        throwIfInMutationOperation();
        if ((this.mCallback instanceof BatchedCallback) == false) goto L6;
        return;
    L6:
        if (this.mBatchedCallback != null) goto L8;
        this.mBatchedCallback = new BatchedCallback(this.mCallback);
    L8:
        this.mCallback = this.mBatchedCallback;
    }

    public void clear() {
        throwIfInMutationOperation();
        if (this.mSize != 0) goto L5;
        return;
    L5:
        int r0 = this.mSize;
        Arrays.fill(this.mData, 0, r0, null);
        this.mSize = 0;
        this.mCallback.onRemoved(0, r0);
    }

    public void endBatchedUpdates() {
        throwIfInMutationOperation();
        if ((this.mCallback instanceof BatchedCallback) == false) goto L6;
        ((BatchedCallback) this.mCallback).dispatchLastEvent();
    L6:
        if (this.mCallback != this.mBatchedCallback) goto L9;
        this.mCallback = this.mBatchedCallback.mWrappedCallback;
        return;
    }

    public T get(int r4) throws IndexOutOfBoundsException {
        if (r4 >= this.mSize) goto L14;
        if (r4 < 0) goto L14;
        if (this.mOldData == null) goto L12;
        if (r4 < this.mNewDataStart) goto L12;
        return this.mOldData[(r4 - this.mNewDataStart) + this.mOldDataStart];
    L12:
        return this.mData[r4];
    L14:
        throw new IndexOutOfBoundsException("Asked to get item at " + r4 + " but size is " + this.mSize);
    }

    public int indexOf(T r8) {
        if (this.mOldData == null) goto L13;
        int r9 = findIndexOf(r8, this.mData, 0, this.mNewDataStart, 4);
        if (r9 == (-1)) goto L7;
        return r9;
    L7:
        int r10 = findIndexOf(r8, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
        if (r10 != (-1)) goto L10;
        return -1;
    L10:
        return (r10 - this.mOldDataStart) + this.mNewDataStart;
    L13:
        return findIndexOf(r8, this.mData, 0, this.mSize, 4);
    }

    public void recalculatePositionOfItemAt(int r4) {
        throwIfInMutationOperation();
        T r0 = get(r4);
        removeItemAtIndex(r4, false);
        int r1 = add(r0, false);
        if (r4 == r1) goto L6;
        this.mCallback.onMoved(r4, r1);
        return;
    }

    public boolean remove(T r2) {
        throwIfInMutationOperation();
        return remove(r2, true);
    }

    public T removeItemAt(int r3) {
        throwIfInMutationOperation();
        T r0 = get(r3);
        removeItemAtIndex(r3, true);
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void replaceAll(Collection<T> r4) {
        replaceAll(r4.toArray((Object[]) Array.newInstance(this.mTClass, r4.size())), true);
    }

    public void replaceAll(T... r2) {
        replaceAll(r2, false);
    }

    public void replaceAll(T[] r2, boolean r3) {
        throwIfInMutationOperation();
        if (r3 == false) goto L5;
        replaceAllInternal(r2);
        return;
    L5:
        replaceAllInternal(copyArray(r2));
    }

    public int size() {
        return this.mSize;
    }

    public void updateItemAt(int r7, T r8) {
        throwIfInMutationOperation();
        T r0 = get(r7);
        if (r0 != r8) goto L5;
    L8:
        boolean r3 = true;
    L9:
        if (r0 != r8) goto L11;
    L16:
        if (r3 == false) goto L18;
        this.mCallback.onChanged(r7, 1, this.mCallback.getChangePayload(r0, r8));
    L18:
        removeItemAtIndex(r7, false);
        int r1 = add(r8, false);
        if (r7 == r1) goto L23;
        this.mCallback.onMoved(r7, r1);
        return;
    L23:
        return;
    L11:
        if (this.mCallback.compare(r0, r8) != 0) goto L16;
        this.mData[r7] = r8;
        if (r3 == false) goto L22;
        this.mCallback.onChanged(r7, 1, this.mCallback.getChangePayload(r0, r8));
        return;
    L22:
        return;
    L5:
        if (this.mCallback.areContentsTheSame(r0, r8) == false) goto L8;
        r3 = false;
        goto L9
    }
}
