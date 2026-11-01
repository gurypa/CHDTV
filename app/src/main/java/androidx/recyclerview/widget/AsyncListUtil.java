package androidx.recyclerview.widget;

import android.util.Log;
import android.util.SparseIntArray;

/* JADX INFO: loaded from: classes.dex */
public class AsyncListUtil<T> {
    static final boolean DEBUG = false;
    static final String TAG = "AsyncListUtil";
    boolean mAllowScrollHints;
    private final ThreadUtil.BackgroundCallback<T> mBackgroundCallback;
    final ThreadUtil.BackgroundCallback<T> mBackgroundProxy;
    final DataCallback<T> mDataCallback;
    int mDisplayedGeneration;
    int mItemCount;
    private final ThreadUtil.MainThreadCallback<T> mMainThreadCallback;
    final ThreadUtil.MainThreadCallback<T> mMainThreadProxy;
    final SparseIntArray mMissingPositions;
    final int[] mPrevRange;
    int mRequestedGeneration;
    private int mScrollHint;
    final Class<T> mTClass;
    final TileList<T> mTileList;
    final int mTileSize;
    final int[] mTmpRange;
    final int[] mTmpRangeExtended;
    final ViewCallback mViewCallback;

    public static abstract class DataCallback<T> {
        public DataCallback() {
        }

        public abstract void fillData(T[] r1, int r2, int r3);

        public int getMaxCachedTiles() {
            return 10;
        }

        public void recycleData(T[] r1, int r2) {
        }

        public abstract int refreshData();
    }

    public static abstract class ViewCallback {
        public static final int HINT_SCROLL_ASC = 2;
        public static final int HINT_SCROLL_DESC = 1;
        public static final int HINT_SCROLL_NONE = 0;

        public ViewCallback() {
        }

        public void extendRangeInto(int[] r7, int[] r8, int r9) {
            int r1 = (r7[1] - r7[0]) + 1;
            int r3 = r1 / 2;
            int r4 = r7[0];
            if (r9 != 1) goto L5;
            int r5 = r1;
        L6:
            r8[0] = r4 - r5;
            int r2 = r7[1];
            if (r9 != 2) goto L9;
            int r6 = r1;
        L10:
            r8[1] = r2 + r6;
            return;
        L9:
            r6 = r3;
            goto L10
        L5:
            r5 = r3;
            goto L6
        }

        public abstract void getItemRangeInto(int[] r1);

        public abstract void onDataRefresh();

        public abstract void onItemLoaded(int r1);
    }

    public AsyncListUtil(Class<T> r3, int r4, DataCallback<T> r5, ViewCallback r6) {
        this.mTmpRange = new int[2];
        this.mPrevRange = new int[2];
        this.mTmpRangeExtended = new int[2];
        this.mScrollHint = 0;
        this.mItemCount = 0;
        this.mDisplayedGeneration = 0;
        this.mRequestedGeneration = this.mDisplayedGeneration;
        this.mMissingPositions = new SparseIntArray();
        this.mMainThreadCallback = new C04741(this);
        this.mBackgroundCallback = new C04752(this);
        this.mTClass = r3;
        this.mTileSize = r4;
        this.mDataCallback = r5;
        this.mViewCallback = r6;
        this.mTileList = new TileList(this.mTileSize);
        MessageThreadUtil r0 = new MessageThreadUtil();
        this.mMainThreadProxy = r0.getMainThreadProxy(this.mMainThreadCallback);
        this.mBackgroundProxy = r0.getBackgroundProxy(this.mBackgroundCallback);
        refresh();
    }

    private boolean isRefreshPending() {
        if (this.mRequestedGeneration == this.mDisplayedGeneration) goto L5;
        return true;
    L5:
        return false;
    }

    public T getItem(int r4) {
        if (r4 < 0) goto L12;
        if (r4 >= this.mItemCount) goto L12;
        T r0 = this.mTileList.getItemAt(r4);
        if (r0 == null) goto L8;
    L10:
        return r0;
    L8:
        if (isRefreshPending() == true) goto L10;
        this.mMissingPositions.put(r4, 0);
    L12:
        throw new IndexOutOfBoundsException(r4 + " is not within 0 and " + this.mItemCount);
    }

    public int getItemCount() {
        return this.mItemCount;
    }

    void log(String r3, Object... r4) {
        Log.d(TAG, "[MAIN] " + String.format(r3, r4));
    }

    public void onRangeChanged() {
        if (isRefreshPending() == false) goto L5;
        return;
    L5:
        updateRange();
        this.mAllowScrollHints = true;
    }

    public void refresh() {
        this.mMissingPositions.clear();
        ThreadUtil.BackgroundCallback<T> r0 = this.mBackgroundProxy;
        int r1 = this.mRequestedGeneration + 1;
        this.mRequestedGeneration = r1;
        r0.refresh(r1);
    }

    void updateRange() {
        this.mViewCallback.getItemRangeInto(this.mTmpRange);
        if (this.mTmpRange[0] <= this.mTmpRange[1]) goto L5;
        return;
    L5:
        if (this.mTmpRange[0] >= 0) goto L8;
        return;
    L8:
        if (this.mTmpRange[1] < this.mItemCount) goto L11;
        return;
    L11:
        if (this.mAllowScrollHints == true) goto L14;
        this.mScrollHint = 0;
    L25:
        this.mPrevRange[0] = this.mTmpRange[0];
        this.mPrevRange[1] = this.mTmpRange[1];
        this.mViewCallback.extendRangeInto(this.mTmpRange, this.mTmpRangeExtended, this.mScrollHint);
        this.mTmpRangeExtended[0] = Math.min(this.mTmpRange[0], Math.max(this.mTmpRangeExtended[0], 0));
        this.mTmpRangeExtended[1] = Math.max(this.mTmpRange[1], Math.min(this.mTmpRangeExtended[1], this.mItemCount - 1));
        this.mBackgroundProxy.updateRange(this.mTmpRange[0], this.mTmpRange[1], this.mTmpRangeExtended[0], this.mTmpRangeExtended[1], this.mScrollHint);
        return;
    L14:
        if (this.mTmpRange[0] <= this.mPrevRange[1]) goto L16;
    L24:
        this.mScrollHint = 0;
        goto L25
    L16:
        if (this.mPrevRange[0] > this.mTmpRange[1]) goto L24;
        if (this.mTmpRange[0] >= this.mPrevRange[0]) goto L22;
        this.mScrollHint = 1;
        goto L25
    L22:
        if (this.mTmpRange[0] <= this.mPrevRange[0]) goto L25;
        this.mScrollHint = 2;
        goto L25
    }
}
