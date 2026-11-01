package androidx.recyclerview.widget;

import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class ChildHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = "ChildrenHelper";
    final Bucket mBucket;
    final Callback mCallback;
    final List<View> mHiddenViews;

    static class Bucket {
        static final int BITS_PER_WORD = 64;
        static final long LAST_BIT = Long.MIN_VALUE;
        long mData;
        Bucket mNext;

        Bucket() {
            this.mData = 0;
        }

        private void ensureNext() {
            if (this.mNext != null) goto L6;
            this.mNext = new Bucket();
            return;
        }

        void clear(int r5) {
            if (r5 >= 64) goto L5;
            this.mData &= ~(1 << r5);
            return;
        L5:
            if (this.mNext == null) goto L9;
            this.mNext.clear(r5 - 64);
            return;
        }

        int countOnesBefore(int r7) {
            if (this.mNext != null) goto L9;
            if (r7 < 64) goto L8;
            return Long.bitCount(this.mData);
        L8:
            return Long.bitCount(this.mData & ((1 << r7) - 1));
        L9:
            if (r7 >= 64) goto L13;
            return Long.bitCount(this.mData & ((1 << r7) - 1));
        L13:
            return this.mNext.countOnesBefore(r7 - 64) + Long.bitCount(this.mData);
        }

        boolean get(int r5) {
            if (r5 < 64) goto L7;
            ensureNext();
            return this.mNext.get(r5 - 64);
        L7:
            if ((this.mData & (1 << r5)) == 0) goto L9;
            return true;
        L9:
            return false;
        }

        void insert(int r12, boolean r13) {
            if (r12 < 64) goto L6;
            ensureNext();
            this.mNext.insert(r12 - 64, r13);
            return;
        L6:
            if ((this.mData & Long.MIN_VALUE) == 0) goto L8;
            boolean r0 = true;
        L9:
            long r5 = (1 << r12) - 1;
            this.mData = (this.mData & r5) | ((this.mData & (~r5)) << 1);
            if (r13 == false) goto L12;
            set(r12);
        L13:
            if (r0 == false) goto L15;
        L16:
            ensureNext();
            this.mNext.insert(0, r0);
            return;
        L15:
            if (this.mNext != null) goto L16;
            return;
        L12:
            clear(r12);
            goto L13
        L8:
            r0 = false;
            goto L9
        }

        boolean remove(int r12) {
            if (r12 < 64) goto L6;
            ensureNext();
            return this.mNext.remove(r12 - 64);
        L6:
            long r2 = 1 << r12;
            if ((this.mData & r2) == 0) goto L9;
            boolean r4 = true;
        L10:
            this.mData &= ~r2;
            long r3 = r2 - 1;
            this.mData = (this.mData & r3) | Long.rotateRight(this.mData & (~r3), 1);
            if (this.mNext != null) goto L13;
        L16:
            return r4;
        L13:
            if (this.mNext.get(0) == false) goto L15;
            set(63);
        L15:
            this.mNext.remove(0);
            goto L16
        L9:
            r4 = false;
            goto L10
        }

        void reset() {
            this.mData = 0;
            if (this.mNext == null) goto L6;
            this.mNext.reset();
            return;
        }

        void set(int r5) {
            if (r5 < 64) goto L5;
            ensureNext();
            this.mNext.set(r5 - 64);
            return;
        L5:
            this.mData |= 1 << r5;
        }

        public String toString() {
            if (this.mNext != null) goto L6;
            return Long.toBinaryString(this.mData);
        L6:
            return this.mNext.toString() + "xx" + Long.toBinaryString(this.mData);
        }
    }

    interface Callback {
        void addView(View r1, int r2);

        void attachViewToParent(View r1, int r2, ViewGroup.LayoutParams r3);

        void detachViewFromParent(int r1);

        View getChildAt(int r1);

        int getChildCount();

        RecyclerView.ViewHolder getChildViewHolder(View r1);

        int indexOfChild(View r1);

        void onEnteredHiddenState(View r1);

        void onLeftHiddenState(View r1);

        void removeAllViews();

        void removeViewAt(int r1);
    }

    ChildHelper(Callback r2) {
        this.mCallback = r2;
        this.mBucket = new Bucket();
        this.mHiddenViews = new ArrayList();
    }

    private int getOffset(int r6) {
        if (r6 >= 0) goto L5;
        return -1;
    L5:
        int r1 = this.mCallback.getChildCount();
        int r2 = r6;
    L6:
        if (r2 >= r1) goto L14;
        int r4 = r6 - (r2 - this.mBucket.countOnesBefore(r2));
        if (r4 == 0) goto L10;
        r2 = r2 + r4;
    L10:
        if (this.mBucket.get(r2) == false) goto L12;
        r2 = r2 + 1;
        goto L10
    L12:
        return r2;
    L14:
        return -1;
    }

    private void hideViewInternal(View r2) {
        this.mHiddenViews.add(r2);
        this.mCallback.onEnteredHiddenState(r2);
    }

    private boolean unhideViewInternal(View r2) {
        if (this.mHiddenViews.remove(r2) == false) goto L6;
        this.mCallback.onLeftHiddenState(r2);
        return true;
    L6:
        return false;
    }

    void addView(View r3, int r4, boolean r5) {
        if (r4 >= 0) goto L4;
        int r0 = this.mCallback.getChildCount();
    L5:
        this.mBucket.insert(r0, r5);
        if (r5 == false) goto L8;
        hideViewInternal(r3);
    L8:
        this.mCallback.addView(r3, r0);
        return;
    L4:
        r0 = getOffset(r4);
        goto L5
    }

    void addView(View r2, boolean r3) {
        addView(r2, -1, r3);
    }

    void attachViewToParent(View r3, int r4, ViewGroup.LayoutParams r5, boolean r6) {
        if (r4 >= 0) goto L4;
        int r0 = this.mCallback.getChildCount();
    L5:
        this.mBucket.insert(r0, r6);
        if (r6 == false) goto L8;
        hideViewInternal(r3);
    L8:
        this.mCallback.attachViewToParent(r3, r0, r5);
        return;
    L4:
        r0 = getOffset(r4);
        goto L5
    }

    void detachViewFromParent(int r3) {
        int r0 = getOffset(r3);
        this.mBucket.remove(r0);
        this.mCallback.detachViewFromParent(r0);
    }

    View findHiddenNonRemovedView(int r6) {
        int r0 = this.mHiddenViews.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L12;
        View r2 = this.mHiddenViews.get(r1);
        RecyclerView.ViewHolder r3 = this.mCallback.getChildViewHolder(r2);
        if (r3.getLayoutPosition() != r6) goto L11;
        if (r3.isInvalid() == true) goto L11;
        if (r3.isRemoved() == true) goto L11;
        return r2;
    L11:
        r1 = r1 + 1;
        goto L3
    L12:
        return null;
    }

    View getChildAt(int r3) {
        int r0 = getOffset(r3);
        return this.mCallback.getChildAt(r0);
    }

    int getChildCount() {
        return this.mCallback.getChildCount() - this.mHiddenViews.size();
    }

    View getUnfilteredChildAt(int r2) {
        return this.mCallback.getChildAt(r2);
    }

    int getUnfilteredChildCount() {
        return this.mCallback.getChildCount();
    }

    void hide(View r5) {
        int r0 = this.mCallback.indexOfChild(r5);
        if (r0 < 0) goto L7;
        this.mBucket.set(r0);
        hideViewInternal(r5);
        return;
    L7:
        throw new IllegalArgumentException("view is not a child, cannot hide " + r5);
    }

    int indexOfChild(View r4) {
        int r0 = this.mCallback.indexOfChild(r4);
        if (r0 != (-1)) goto L6;
        return -1;
    L6:
        if (this.mBucket.get(r0) == false) goto L9;
        return -1;
    L9:
        return r0 - this.mBucket.countOnesBefore(r0);
    }

    boolean isHidden(View r2) {
        return this.mHiddenViews.contains(r2);
    }

    void removeAllViewsUnfiltered() {
        this.mBucket.reset();
        int r0 = this.mHiddenViews.size() - 1;
    L3:
        if (r0 < 0) goto L5;
        this.mCallback.onLeftHiddenState(this.mHiddenViews.get(r0));
        this.mHiddenViews.remove(r0);
        r0 = r0 - 1;
        goto L3
    L5:
        this.mCallback.removeAllViews();
    }

    void removeView(View r3) {
        int r0 = this.mCallback.indexOfChild(r3);
        if (r0 >= 0) goto L6;
        return;
    L6:
        if (this.mBucket.remove(r0) == false) goto L8;
        unhideViewInternal(r3);
    L8:
        this.mCallback.removeViewAt(r0);
    }

    void removeViewAt(int r4) {
        int r0 = getOffset(r4);
        View r1 = this.mCallback.getChildAt(r0);
        if (r1 != null) goto L6;
        return;
    L6:
        if (this.mBucket.remove(r0) == false) goto L8;
        unhideViewInternal(r1);
    L8:
        this.mCallback.removeViewAt(r0);
    }

    boolean removeViewIfHidden(View r4) {
        int r0 = this.mCallback.indexOfChild(r4);
        if (r0 != (-1)) goto L7;
        unhideViewInternal(r4);
        return true;
    L7:
        if (this.mBucket.get(r0) == false) goto L10;
        this.mBucket.remove(r0);
        unhideViewInternal(r4);
        this.mCallback.removeViewAt(r0);
        return true;
    L10:
        return false;
    }

    public String toString() {
        return this.mBucket.toString() + ", hidden list:" + this.mHiddenViews.size();
    }

    void unhide(View r5) {
        int r0 = this.mCallback.indexOfChild(r5);
        if (r0 < 0) goto L11;
        if (this.mBucket.get(r0) == false) goto L9;
        this.mBucket.clear(r0);
        unhideViewInternal(r5);
        return;
    L9:
        throw new RuntimeException("trying to unhide a view that was not hidden" + r5);
    L11:
        throw new IllegalArgumentException("view is not a child, cannot hide " + r5);
    }
}
