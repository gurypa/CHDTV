package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class GridLayoutManager extends LinearLayoutManager {
    private static final boolean DEBUG = false;
    public static final int DEFAULT_SPAN_COUNT = -1;
    private static final String TAG = "GridLayoutManager";
    int[] mCachedBorders;
    final Rect mDecorInsets;
    boolean mPendingSpanCountChange;
    final SparseIntArray mPreLayoutSpanIndexCache;
    final SparseIntArray mPreLayoutSpanSizeCache;
    View[] mSet;
    int mSpanCount;
    SpanSizeLookup mSpanSizeLookup;
    private boolean mUsingSpansToEstimateScrollBarDimensions;

    public static final class DefaultSpanSizeLookup extends SpanSizeLookup {
        public DefaultSpanSizeLookup() {
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public int getSpanIndex(int r2, int r3) {
            return r2 % r3;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public int getSpanSize(int r2) {
            return 1;
        }
    }

    public static class LayoutParams extends RecyclerView.LayoutParams {
        public static final int INVALID_SPAN_ID = -1;
        int mSpanIndex;
        int mSpanSize;

        public LayoutParams(int r2, int r3) {
            super(r2, r3);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }

        public LayoutParams(Context r2, AttributeSet r3) {
            super(r2, r3);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }

        public LayoutParams(ViewGroup.LayoutParams r2) {
            super(r2);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams r2) {
            super(r2);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }

        public LayoutParams(RecyclerView.LayoutParams r2) {
            super(r2);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }

        public int getSpanIndex() {
            return this.mSpanIndex;
        }

        public int getSpanSize() {
            return this.mSpanSize;
        }
    }

    public static abstract class SpanSizeLookup {
        private boolean mCacheSpanGroupIndices;
        private boolean mCacheSpanIndices;
        final SparseIntArray mSpanGroupIndexCache;
        final SparseIntArray mSpanIndexCache;

        public SpanSizeLookup() {
            this.mSpanIndexCache = new SparseIntArray();
            this.mSpanGroupIndexCache = new SparseIntArray();
            this.mCacheSpanIndices = false;
            this.mCacheSpanGroupIndices = false;
        }

        static int findFirstKeyLessThan(SparseIntArray r4, int r5) {
            int r0 = 0;
            int r1 = r4.size() - 1;
        L3:
            if (r0 > r1) goto L9;
            int r2 = (r0 + r1) >>> 1;
            if (r4.keyAt(r2) < r5) goto L6;
            r1 = r2 - 1;
            goto L3
        L6:
            r0 = r2 + 1;
            goto L3
        L9:
            int r3 = r0 - 1;
            if (r3 >= 0) goto L12;
            return -1;
        L12:
            if (r3 < r4.size()) goto L14;
            return -1;
        L14:
            return r4.keyAt(r3);
        }

        int getCachedSpanGroupIndex(int r4, int r5) {
            if (this.mCacheSpanGroupIndices == false) goto L5;
            int r0 = this.mSpanGroupIndexCache.get(r4, -1);
            if (r0 == (-1)) goto L9;
            return r0;
        L9:
            int r1 = getSpanGroupIndex(r4, r5);
            this.mSpanGroupIndexCache.put(r4, r1);
            return r1;
        L5:
            return getSpanGroupIndex(r4, r5);
        }

        int getCachedSpanIndex(int r4, int r5) {
            if (this.mCacheSpanIndices == false) goto L5;
            int r0 = this.mSpanIndexCache.get(r4, -1);
            if (r0 == (-1)) goto L9;
            return r0;
        L9:
            int r1 = getSpanIndex(r4, r5);
            this.mSpanIndexCache.put(r4, r1);
            return r1;
        L5:
            return getSpanIndex(r4, r5);
        }

        public int getSpanGroupIndex(int r7, int r8) {
            int r0 = 0;
            int r1 = 0;
            int r2 = 0;
            if (this.mCacheSpanGroupIndices == false) goto L9;
            int r3 = findFirstKeyLessThan(this.mSpanGroupIndexCache, r7);
            if (r3 == (-1)) goto L9;
            r1 = this.mSpanGroupIndexCache.get(r3);
            r2 = r3 + 1;
            r0 = getCachedSpanIndex(r3, r8) + getSpanSize(r3);
            if (r0 != r8) goto L9;
            r0 = 0;
            r1 = r1 + 1;
        L9:
            int r4 = getSpanSize(r7);
            int r5 = r2;
        L10:
            if (r5 >= r7) goto L18;
            int r6 = getSpanSize(r5);
            r0 = r0 + r6;
            if (r0 != r8) goto L14;
            r0 = 0;
            r1 = r1 + 1;
        L16:
            r5 = r5 + 1;
            goto L10
        L14:
            if (r0 <= r8) goto L16;
            r0 = r6;
            r1 = r1 + 1;
            goto L16
        L18:
            if ((r0 + r4) > r8) goto L20;
            return r1;
        L20:
            return r1 + 1;
        }

        public int getSpanIndex(int r8, int r9) {
            int r0 = getSpanSize(r8);
            if (r0 != r9) goto L5;
            return 0;
        L5:
            int r2 = 0;
            int r3 = 0;
            if (this.mCacheSpanIndices == false) goto L10;
            int r4 = findFirstKeyLessThan(this.mSpanIndexCache, r8);
            if (r4 < 0) goto L10;
            r2 = this.mSpanIndexCache.get(r4) + getSpanSize(r4);
            r3 = r4 + 1;
        L10:
            int r5 = r3;
        L11:
            if (r5 >= r8) goto L19;
            int r6 = getSpanSize(r5);
            r2 = r2 + r6;
            if (r2 != r9) goto L15;
            r2 = 0;
        L17:
            r5 = r5 + 1;
            goto L11
        L15:
            if (r2 <= r9) goto L17;
            r2 = r6;
            goto L17
        L19:
            if ((r2 + r0) > r9) goto L21;
            return r2;
        L21:
            return 0;
        }

        public abstract int getSpanSize(int r1);

        public void invalidateSpanGroupIndexCache() {
            this.mSpanGroupIndexCache.clear();
        }

        public void invalidateSpanIndexCache() {
            this.mSpanIndexCache.clear();
        }

        public boolean isSpanGroupIndexCacheEnabled() {
            return this.mCacheSpanGroupIndices;
        }

        public boolean isSpanIndexCacheEnabled() {
            return this.mCacheSpanIndices;
        }

        public void setSpanGroupIndexCacheEnabled(boolean r2) {
            if (r2 == true) goto L4;
            this.mSpanGroupIndexCache.clear();
        L4:
            this.mCacheSpanGroupIndices = r2;
        }

        public void setSpanIndexCacheEnabled(boolean r2) {
            if (r2 == true) goto L4;
            this.mSpanGroupIndexCache.clear();
        L4:
            this.mCacheSpanIndices = r2;
        }
    }

    public GridLayoutManager(Context r2, int r3) {
        super(r2);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        setSpanCount(r3);
    }

    public GridLayoutManager(Context r2, int r3, int r4, boolean r5) {
        super(r2, r4, r5);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        setSpanCount(r3);
    }

    public GridLayoutManager(Context r3, AttributeSet r4, int r5, int r6) {
        super(r3, r4, r5, r6);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        setSpanCount(getProperties(r3, r4, r5, r6).spanCount);
    }

    private void assignSpans(RecyclerView.Recycler r9, RecyclerView.State r10, int r11, boolean r12) {
        if (r12 == false) goto L4;
        int r0 = 0;
        int r1 = r11;
        int r2 = 1;
    L5:
        int r3 = 0;
        int r4 = r0;
    L6:
        if (r4 == r1) goto L8;
        View r5 = this.mSet[r4];
        LayoutParams r6 = (LayoutParams) r5.getLayoutParams();
        r6.mSpanSize = getSpanSize(r9, r10, getPosition(r5));
        r6.mSpanIndex = r3;
        r3 = r3 + r6.mSpanSize;
        r4 = r4 + r2;
        goto L6
    L8:
        return;
    L4:
        r0 = r11 - 1;
        r1 = -1;
        r2 = -1;
        goto L5
    }

    private void cachePreLayoutSpanMapping() {
        int r0 = getChildCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        LayoutParams r2 = (LayoutParams) getChildAt(r1).getLayoutParams();
        int r3 = r2.getViewLayoutPosition();
        this.mPreLayoutSpanSizeCache.put(r3, r2.getSpanSize());
        this.mPreLayoutSpanIndexCache.put(r3, r2.getSpanIndex());
        r1 = r1 + 1;
        goto L3
    }

    private void calculateItemBorders(int r3) {
        this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, r3);
    }

    static int[] calculateItemBorders(int[] r7, int r8, int r9) {
        if (r7 != null) goto L4;
    L7:
        r7 = new int[r8 + 1];
    L8:
        r7[0] = 0;
        int r0 = r9 / r8;
        int r1 = r9 % r8;
        int r2 = 0;
        int r3 = 0;
        int r4 = 1;
    L9:
        if (r4 > r8) goto L16;
        int r5 = r0;
        r3 = r3 + r1;
        if (r3 <= 0) goto L15;
        if ((r8 - r3) >= r1) goto L15;
        r5 = r5 + 1;
        r3 = r3 - r8;
    L15:
        r2 = r2 + r5;
        r7[r4] = r2;
        r4 = r4 + 1;
        goto L9
    L16:
        return r7;
    L4:
        if (r7.length != (r8 + 1)) goto L7;
        if (r7[r7.length - 1] == r9) goto L8;
        goto L7
    }

    private void clearPreLayoutSpanMappingCache() {
        this.mPreLayoutSpanSizeCache.clear();
        this.mPreLayoutSpanIndexCache.clear();
    }

    private int computeScrollOffsetWithSpanInfo(RecyclerView.State r19) {
        if (getChildCount() != 0) goto L5;
    L22:
        return 0;
    L5:
        if (r19.getItemCount() == 0) goto L22;
        ensureLayoutState();
        boolean r1 = isSmoothScrollbarEnabled();
        View r3 = findFirstVisibleChildClosestToStart(!r1, true);
        View r5 = findFirstVisibleChildClosestToEnd(!r1, true);
        if (r3 == null) goto L21;
        if (r5 == null) goto L21;
        int r6 = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(r3), this.mSpanCount);
        int r7 = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(r5), this.mSpanCount);
        int r8 = Math.min(r6, r7);
        int r9 = Math.max(r6, r7);
        int r10 = this.mSpanSizeLookup.getCachedSpanGroupIndex(r19.getItemCount() - 1, this.mSpanCount) + 1;
        if (this.mShouldReverseLayout == false) goto L14;
        int r2 = Math.max(0, (r10 - r9) - 1);
    L16:
        if (r1 == true) goto L19;
        return r2;
    L19:
        return Math.round((r2 * (Math.abs(this.mOrientationHelper.getDecoratedEnd(r5) - this.mOrientationHelper.getDecoratedStart(r3)) / ((this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(r5), this.mSpanCount) - this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(r3), this.mSpanCount)) + 1))) + (this.mOrientationHelper.getStartAfterPadding() - this.mOrientationHelper.getDecoratedStart(r3)));
    L14:
        r2 = Math.max(0, r8);
    L21:
        return 0;
    }

    private int computeScrollRangeWithSpanInfo(RecyclerView.State r10) {
        if (getChildCount() != 0) goto L5;
    L18:
        return 0;
    L5:
        if (r10.getItemCount() == 0) goto L18;
        ensureLayoutState();
        View r0 = findFirstVisibleChildClosestToStart(!isSmoothScrollbarEnabled(), true);
        View r3 = findFirstVisibleChildClosestToEnd(!isSmoothScrollbarEnabled(), true);
        if (r0 == null) goto L17;
        if (r3 == null) goto L17;
        if (isSmoothScrollbarEnabled() == false) goto L14;
        int r1 = this.mOrientationHelper.getDecoratedEnd(r3) - this.mOrientationHelper.getDecoratedStart(r0);
        int r4 = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(r0), this.mSpanCount);
        int r5 = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(r3), this.mSpanCount);
        return (int) ((r1 / ((r5 - r4) + 1)) * (this.mSpanSizeLookup.getCachedSpanGroupIndex(r10.getItemCount() - 1, this.mSpanCount) + 1));
    L14:
        return this.mSpanSizeLookup.getCachedSpanGroupIndex(r10.getItemCount() - 1, this.mSpanCount) + 1;
    L17:
        return 0;
    }

    private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler r7, RecyclerView.State r8, LinearLayoutManager.AnchorInfo r9, int r10) {
        if (r10 != 1) goto L5;
        boolean r1 = true;
    L6:
        int r2 = getSpanIndex(r7, r8, r9.mPosition);
        if (r1 == false) goto L12;
    L8:
        if (r2 <= 0) goto L23;
        if (r9.mPosition <= 0) goto L24;
        r9.mPosition--;
        r2 = getSpanIndex(r7, r8, r9.mPosition);
        goto L8
    L24:
        return;
    L23:
        return;
    L12:
        int r3 = r8.getItemCount() - 1;
        int r0 = r9.mPosition;
        int r4 = r2;
    L13:
        if (r0 >= r3) goto L17;
        int r5 = getSpanIndex(r7, r8, r0 + 1);
        if (r5 <= r4) goto L17;
        r0 = r0 + 1;
        r4 = r5;
    L17:
        r9.mPosition = r0;
        return;
    L5:
        r1 = false;
        goto L6
    }

    private void ensureViewSet() {
        if (this.mSet != null) goto L5;
    L6:
        this.mSet = new View[this.mSpanCount];
        return;
    L5:
        if (this.mSet.length != this.mSpanCount) goto L6;
    }

    private int getSpanGroupIndex(RecyclerView.Recycler r4, RecyclerView.State r5, int r6) {
        if (r5.isPreLayout() == false) goto L5;
        int r0 = r4.convertPreLayoutPositionToPostLayout(r6);
        if (r0 != (-1)) goto L11;
        Log.w(TAG, "Cannot find span size for pre layout position. " + r6);
        return 0;
    L11:
        return this.mSpanSizeLookup.getCachedSpanGroupIndex(r0, this.mSpanCount);
    L5:
        return this.mSpanSizeLookup.getCachedSpanGroupIndex(r6, this.mSpanCount);
    }

    private int getSpanIndex(RecyclerView.Recycler r5, RecyclerView.State r6, int r7) {
        if (r6.isPreLayout() == false) goto L5;
        int r0 = this.mPreLayoutSpanIndexCache.get(r7, -1);
        if (r0 == (-1)) goto L9;
        return r0;
    L9:
        int r2 = r5.convertPreLayoutPositionToPostLayout(r7);
        if (r2 != (-1)) goto L14;
        Log.w(TAG, "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + r7);
        return 0;
    L14:
        return this.mSpanSizeLookup.getCachedSpanIndex(r2, this.mSpanCount);
    L5:
        return this.mSpanSizeLookup.getCachedSpanIndex(r7, this.mSpanCount);
    }

    private int getSpanSize(RecyclerView.Recycler r5, RecyclerView.State r6, int r7) {
        if (r6.isPreLayout() == false) goto L5;
        int r0 = this.mPreLayoutSpanSizeCache.get(r7, -1);
        if (r0 == (-1)) goto L9;
        return r0;
    L9:
        int r2 = r5.convertPreLayoutPositionToPostLayout(r7);
        if (r2 != (-1)) goto L14;
        Log.w(TAG, "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + r7);
        return 1;
    L14:
        return this.mSpanSizeLookup.getSpanSize(r2);
    L5:
        return this.mSpanSizeLookup.getSpanSize(r7);
    }

    private void guessMeasurement(float r3, int r4) {
        calculateItemBorders(Math.max(Math.round(this.mSpanCount * r3), r4));
    }

    private void measureChild(View r11, int r12, boolean r13) {
        LayoutParams r0 = (LayoutParams) r11.getLayoutParams();
        Rect r1 = r0.mDecorInsets;
        int r2 = ((r1.top + r1.bottom) + r0.topMargin) + r0.bottomMargin;
        int r3 = ((r1.left + r1.right) + r0.leftMargin) + r0.rightMargin;
        int r4 = getSpaceForSpanRange(r0.mSpanIndex, r0.mSpanSize);
        if (this.mOrientation != 1) goto L5;
        int r5 = getChildMeasureSpec(r4, r12, r3, r0.width, false);
        int r6 = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), r2, r0.height, true);
    L6:
        measureChildWithDecorationsAndMargin(r11, r5, r6, r13);
        return;
    L5:
        r6 = getChildMeasureSpec(r4, r12, r2, r0.height, false);
        r5 = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getWidthMode(), r3, r0.width, true);
        goto L6
    }

    private void measureChildWithDecorationsAndMargin(View r3, int r4, int r5, boolean r6) {
        RecyclerView.LayoutParams r0 = (RecyclerView.LayoutParams) r3.getLayoutParams();
        if (r6 == false) goto L5;
        boolean r1 = shouldReMeasureChild(r3, r4, r5, r0);
    L6:
        if (r1 == false) goto L9;
        r3.measure(r4, r5);
        return;
    L9:
        return;
    L5:
        r1 = shouldMeasureChild(r3, r4, r5, r0);
        goto L6
    }

    private void updateMeasurements() {
        if (getOrientation() != 1) goto L5;
        int r0 = (getWidth() - getPaddingRight()) - getPaddingLeft();
    L6:
        calculateItemBorders(r0);
        return;
    L5:
        r0 = (getHeight() - getPaddingBottom()) - getPaddingTop();
        goto L6
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean checkLayoutParams(RecyclerView.LayoutParams r2) {
        return r2 instanceof LayoutParams;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    void collectPrefetchPositionsForLayoutState(RecyclerView.State r7, LinearLayoutManager.LayoutState r8, RecyclerView.LayoutManager.LayoutPrefetchRegistry r9) {
        int r0 = this.mSpanCount;
        int r1 = 0;
    L4:
        if (r1 >= this.mSpanCount) goto L9;
        if (r8.hasMore(r7) == false) goto L13;
        if (r0 <= 0) goto L14;
        int r2 = r8.mCurrentPosition;
        r9.addPosition(r2, Math.max(0, r8.mScrollingOffset));
        r0 = r0 - this.mSpanSizeLookup.getSpanSize(r2);
        r8.mCurrentPosition += r8.mItemDirection;
        r1 = r1 + 1;
        goto L4
    L14:
        return;
    L13:
        return;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeHorizontalScrollOffset(RecyclerView.State r2) {
        if (this.mUsingSpansToEstimateScrollBarDimensions == false) goto L7;
        return computeScrollOffsetWithSpanInfo(r2);
    L7:
        return super.computeHorizontalScrollOffset(r2);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeHorizontalScrollRange(RecyclerView.State r2) {
        if (this.mUsingSpansToEstimateScrollBarDimensions == false) goto L7;
        return computeScrollRangeWithSpanInfo(r2);
    L7:
        return super.computeHorizontalScrollRange(r2);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeVerticalScrollOffset(RecyclerView.State r2) {
        if (this.mUsingSpansToEstimateScrollBarDimensions == false) goto L7;
        return computeScrollOffsetWithSpanInfo(r2);
    L7:
        return super.computeVerticalScrollOffset(r2);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeVerticalScrollRange(RecyclerView.State r2) {
        if (this.mUsingSpansToEstimateScrollBarDimensions == false) goto L7;
        return computeScrollRangeWithSpanInfo(r2);
    L7:
        return super.computeVerticalScrollRange(r2);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    View findReferenceChild(RecyclerView.Recycler r17, RecyclerView.State r18, boolean r19, boolean r20) {
        int r1 = 0;
        int r2 = getChildCount();
        int r3 = 1;
        if (r20 == false) goto L5;
        r1 = getChildCount() - 1;
        r2 = -1;
        r3 = -1;
    L5:
        int r4 = r18.getItemCount();
        ensureLayoutState();
        View r5 = null;
        View r6 = null;
        int r7 = this.mOrientationHelper.getStartAfterPadding();
        int r8 = this.mOrientationHelper.getEndAfterPadding();
        int r9 = r1;
    L6:
        if (r9 == r2) goto L28;
        View r10 = getChildAt(r9);
        int r11 = getPosition(r10);
        if (r11 < 0) goto L26;
        if (r11 >= r4) goto L26;
        if (getSpanIndex(r17, r18, r11) != 0) goto L26;
        if (((RecyclerView.LayoutParams) r10.getLayoutParams()).isItemRemoved() == false) goto L18;
        if (r5 != null) goto L26;
        r5 = r10;
        goto L26
    L18:
        if (this.mOrientationHelper.getDecoratedStart(r10) < r8) goto L20;
    L23:
        if (r6 != null) goto L26;
        r6 = r10;
        goto L26
    L20:
        if (this.mOrientationHelper.getDecoratedEnd(r10) < r7) goto L23;
        return r10;
    L26:
        r9 = r9 + r3;
        goto L6
    L28:
        if (r6 == null) goto L31;
        return r6;
    L31:
        return r5;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation != 0) goto L7;
        return new LayoutParams(-2, -1);
    L7:
        return new LayoutParams(-1, -2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateLayoutParams(Context r2, AttributeSet r3) {
        return new LayoutParams(r2, r3);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams r3) {
        if ((r3 instanceof ViewGroup.MarginLayoutParams) == false) goto L7;
        return new LayoutParams((ViewGroup.MarginLayoutParams) r3);
    L7:
        return new LayoutParams(r3);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getColumnCountForAccessibility(RecyclerView.Recycler r3, RecyclerView.State r4) {
        if (this.mOrientation != 1) goto L7;
        return this.mSpanCount;
    L7:
        if (r4.getItemCount() >= 1) goto L11;
        return 0;
    L11:
        return getSpanGroupIndex(r3, r4, r4.getItemCount() - 1) + 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getRowCountForAccessibility(RecyclerView.Recycler r3, RecyclerView.State r4) {
        if (this.mOrientation != 0) goto L7;
        return this.mSpanCount;
    L7:
        if (r4.getItemCount() >= 1) goto L11;
        return 0;
    L11:
        return getSpanGroupIndex(r3, r4, r4.getItemCount() - 1) + 1;
    }

    int getSpaceForSpanRange(int r4, int r5) {
        if (this.mOrientation != 1) goto L9;
        if (isLayoutRTL() == false) goto L9;
        return this.mCachedBorders[this.mSpanCount - r4] - this.mCachedBorders[(this.mSpanCount - r4) - r5];
    L9:
        return this.mCachedBorders[r4 + r5] - this.mCachedBorders[r4];
    }

    public int getSpanCount() {
        return this.mSpanCount;
    }

    public SpanSizeLookup getSpanSizeLookup() {
        return this.mSpanSizeLookup;
    }

    public boolean isUsingSpansToEstimateScrollbarDimensions() {
        return this.mUsingSpansToEstimateScrollBarDimensions;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    void layoutChunk(RecyclerView.Recycler r25, RecyclerView.State r26, LinearLayoutManager.LayoutState r27, LinearLayoutManager.LayoutChunkResult r28) {
        int r10 = this.mOrientationHelper.getModeInOther();
        if (r10 == 1073741824) goto L5;
        boolean r3 = true;
    L6:
        boolean r12 = r3;
        if (getChildCount() <= 0) goto L9;
        int r4 = this.mCachedBorders[this.mSpanCount];
    L10:
        int r13 = r4;
        if (r12 == false) goto L14;
        updateMeasurements();
    L14:
        if (r27.mItemDirection != 1) goto L16;
        boolean r5 = true;
    L17:
        boolean r14 = r5;
        int r6 = 0;
        int r7 = this.mSpanCount;
        if (r14 == true) goto L22;
        r7 = getSpanIndex(r25, r26, r27.mCurrentPosition) + getSpanSize(r25, r26, r27.mCurrentPosition);
    L22:
        if (r6 >= this.mSpanCount) goto L38;
        if (r27.hasMore(r26) == false) goto L38;
        if (r7 <= 0) goto L38;
        int r8 = r27.mCurrentPosition;
        int r9 = getSpanSize(r25, r26, r8);
        if (r9 > this.mSpanCount) goto L36;
        r7 = r7 - r9;
        if (r7 < 0) goto L38;
        View r2 = r27.next(r25);
        if (r2 == null) goto L38;
        this.mSet[r6] = r2;
        r6 = r6 + 1;
        goto L22
    L36:
        throw new IllegalArgumentException("Item at position " + r8 + " requires " + r9 + " spans but GridLayoutManager has only " + this.mSpanCount + " spans.");
    L38:
        if (r6 != 0) goto L41;
        r28.mFinished = true;
        return;
    L41:
        int r1 = 0;
        float r11 = 0.0f;
        assignSpans(r25, r26, r6, r14);
        int r15 = 0;
    L42:
        if (r15 >= r6) goto L59;
        View r16 = this.mSet[r15];
        if (r27.mScrapList != null) goto L48;
        if (r14 == false) goto L47;
        addView(r16);
        boolean r17 = false;
    L52:
        calculateItemDecorationsForChild(r16, this.mDecorInsets);
        measureChild(r16, r10, r17);
        int r18 = this.mOrientationHelper.getDecoratedMeasurement(r16);
        if (r18 <= r1) goto L55;
        r1 = r18;
    L55:
        int r19 = r1;
        float r20 = (this.mOrientationHelper.getDecoratedMeasurementInOther(r16) * 1.0f) / ((LayoutParams) r16.getLayoutParams()).mSpanSize;
        if (r20 <= r11) goto L58;
        r11 = r20;
    L58:
        r15 = r15 + 1;
        r1 = r19;
        goto L42
    L47:
        r17 = false;
        addView(r16, 0);
        goto L52
    L48:
        r17 = false;
        if (r14 == false) goto L51;
        addDisappearingView(r16);
        goto L52
    L51:
        addDisappearingView(r16, 0);
        goto L52
    L59:
        if (r12 == false) goto L67;
        guessMeasurement(r11, r13);
        int r21 = 0;
        int r22 = 0;
    L61:
        if (r22 >= r6) goto L66;
        View r23 = this.mSet[r22];
        measureChild(r23, 1073741824, true);
        int r24 = this.mOrientationHelper.getDecoratedMeasurement(r23);
        if (r24 <= r21) goto L65;
        r21 = r24;
    L65:
        r22 = r22 + 1;
        goto L61
    L66:
        int r110 = r21;
    L68:
        int r29 = 0;
    L69:
        if (r29 >= r6) goto L79;
        View r30 = this.mSet[r29];
        if (this.mOrientationHelper.getDecoratedMeasurement(r30) == r110) goto L77;
        LayoutParams r31 = (LayoutParams) r30.getLayoutParams();
        Rect r32 = r31.mDecorInsets;
        int r33 = ((r32.top + r32.bottom) + r31.topMargin) + r31.bottomMargin;
        int r34 = ((r32.left + r32.right) + r31.leftMargin) + r31.rightMargin;
        int r35 = getSpaceForSpanRange(r31.mSpanIndex, r31.mSpanSize);
        if (this.mOrientation != 1) goto L75;
        int r36 = getChildMeasureSpec(r35, 1073741824, r34, r31.width, false);
        int r37 = View.MeasureSpec.makeMeasureSpec(r110 - r33, 1073741824);
    L76:
        measureChildWithDecorationsAndMargin(r30, r36, r37, true);
    L78:
        r29 = r29 + 1;
        r10 = r10;
        r11 = r11;
        goto L69
    L75:
        r36 = View.MeasureSpec.makeMeasureSpec(r110 - r34, 1073741824);
        r37 = getChildMeasureSpec(r35, 1073741824, r33, r31.height, false);
        goto L76
    L77:
        r10 = r10;
        goto L78
    L79:
        r28.mConsumed = r110;
        int r38 = 0;
        int r39 = 0;
        int r40 = 0;
        int r41 = 0;
        if (this.mOrientation != 1) goto L86;
        if (r27.mLayoutDirection != (-1)) goto L84;
        r41 = r27.mOffset;
        r40 = r41 - r110;
    L89:
        int r42 = 0;
    L90:
        if (r42 >= r6) goto L106;
        View r43 = this.mSet[r42];
        LayoutParams r44 = (LayoutParams) r43.getLayoutParams();
        int r111 = r38;
        if (this.mOrientation == 1) goto L94;
        r40 = getPaddingTop() + this.mCachedBorders[r44.mSpanIndex];
        int r45 = r40 + this.mOrientationHelper.getDecoratedMeasurementInOther(r43);
        int r46 = r39;
        int r47 = r111;
    L98:
        layoutDecoratedWithMargins(r43, r47, r40, r46, r45);
        if (r44.isItemRemoved() == false) goto L101;
    L104:
        r28.mIgnoreConsumed = true;
    L105:
        r28.mFocusable |= r43.hasFocusable();
        r42 = r42 + 1;
        r38 = r47;
        r39 = r46;
        r41 = r45;
        goto L90
    L101:
        if (r44.isItemChanged() == false) goto L105;
    L94:
        if (isLayoutRTL() == false) goto L96;
        int r48 = getPaddingLeft() + this.mCachedBorders[this.mSpanCount - r44.mSpanIndex];
        r47 = r48 - this.mOrientationHelper.getDecoratedMeasurementInOther(r43);
        r45 = r41;
        r46 = r48;
        goto L98
    L96:
        int r49 = getPaddingLeft() + this.mCachedBorders[r44.mSpanIndex];
        int r50 = r49 + this.mOrientationHelper.getDecoratedMeasurementInOther(r43);
        r47 = r49;
        r45 = r41;
        r46 = r50;
        goto L98
    L106:
        Arrays.fill(this.mSet, null);
        return;
    L84:
        r40 = r27.mOffset;
        r41 = r40 + r110;
        goto L89
    L86:
        if (r27.mLayoutDirection != (-1)) goto L88;
        r39 = r27.mOffset;
        r38 = r39 - r110;
        goto L89
    L88:
        r38 = r27.mOffset;
        r39 = r38 + r110;
        goto L89
    L67:
        r110 = r1;
        goto L68
    L16:
        r5 = false;
        goto L17
    L9:
        r4 = 0;
        goto L10
    L5:
        r3 = false;
        goto L6
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    void onAnchorReady(RecyclerView.Recycler r2, RecyclerView.State r3, LinearLayoutManager.AnchorInfo r4, int r5) {
        super.onAnchorReady(r2, r3, r4, r5);
        updateMeasurements();
        if (r3.getItemCount() > 0) goto L5;
    L7:
        ensureViewSet();
        return;
    L5:
        if (r3.isPreLayout() == true) goto L7;
        ensureAnchorIsInCorrectSpan(r2, r3, r4, r5);
        goto L7
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public View onFocusSearchFailed(View r33, int r34, RecyclerView.Recycler r35, RecyclerView.State r36) {
        RecyclerView.Recycler r1 = r35;
        RecyclerView.State r2 = r36;
        View r3 = findContainingItemView(r33);
        if (r3 != null) goto L5;
        return null;
    L5:
        LayoutParams r5 = (LayoutParams) r3.getLayoutParams();
        int r6 = r5.mSpanIndex;
        int r7 = r5.mSpanIndex + r5.mSpanSize;
        if (super.onFocusSearchFailed(r33, r34, r35, r36) != null) goto L9;
        return null;
    L9:
        if (convertFocusDirectionToLayoutDirection(r34) != 1) goto L11;
        boolean r12 = true;
    L13:
        if (r12 == this.mShouldReverseLayout) goto L15;
        boolean r13 = true;
    L16:
        if (r13 == false) goto L18;
        int r14 = getChildCount() - 1;
        int r15 = -1;
        int r16 = -1;
    L20:
        if (this.mOrientation == 1) goto L22;
    L24:
        boolean r10 = false;
    L25:
        View r17 = null;
        View r20 = null;
        int r11 = getSpanGroupIndex(r1, r2, r14);
        int r4 = -1;
        int r8 = 0;
        int r9 = -1;
        int r18 = 0;
        int r19 = r14;
    L26:
        if (r19 == r16) goto L81;
        int r24 = r14;
        int r110 = getSpanGroupIndex(r1, r2, r19);
        View r21 = getChildAt(r19);
        if (r21 == r3) goto L81;
        if (r21.hasFocusable() == false) goto L36;
        if (r110 == r11) goto L36;
        if (r17 != null) goto L81;
        View r25 = r3;
        int r31 = r4;
        int r111 = r8;
        int r26 = r11;
        int r30 = r19;
    L78:
        r4 = r31;
    L79:
        r19 = r30 + r15;
        r1 = r35;
        r2 = r36;
        r8 = r111;
        r14 = r24;
        r3 = r25;
        r11 = r26;
    L36:
        LayoutParams r22 = (LayoutParams) r21.getLayoutParams();
        r25 = r3;
        int r23 = r22.mSpanIndex;
        r26 = r11;
        int r112 = r22.mSpanIndex + r22.mSpanSize;
        if (r21.hasFocusable() == false) goto L41;
        if (r23 != r6) goto L41;
        if (r112 != r7) goto L41;
        return r21;
    L41:
        boolean r27 = false;
        if (r21.hasFocusable() == false) goto L45;
        if (r17 != null) goto L45;
    L47:
        r27 = true;
        r31 = r4;
        r111 = r8;
        r30 = r19;
    L73:
        if (r27 == false) goto L78;
        if (r21.hasFocusable() == false) goto L77;
        int r113 = r22.mSpanIndex;
        r111 = Math.min(r112, r7) - Math.max(r23, r6);
        r17 = r21;
        r4 = r113;
        goto L79
    L77:
        r9 = r22.mSpanIndex;
        r20 = r21;
        r18 = Math.min(r112, r7) - Math.max(r23, r6);
        r4 = r31;
    L45:
        if (r21.hasFocusable() == true) goto L48;
        if (r20 == null) goto L47;
    L48:
        r30 = r19;
        int r114 = Math.min(r112, r7) - Math.max(r23, r6);
        if (r21.hasFocusable() == false) goto L61;
        if (r114 <= r8) goto L52;
        r27 = true;
        r31 = r4;
        r111 = r8;
        goto L73
    L52:
        if (r114 != r8) goto L59;
        r31 = r4;
        if (r23 <= r4) goto L56;
        boolean r28 = true;
    L57:
        if (r10 != r28) goto L60;
        r27 = true;
        r111 = r8;
    L60:
        r111 = r8;
        goto L73
    L56:
        r28 = false;
        goto L57
    L59:
        r31 = r4;
        goto L60
    L61:
        r31 = r4;
        if (r17 != null) goto L72;
        r111 = r8;
        boolean r29 = false;
        if (isViewPartiallyVisible(r21, false, true) == false) goto L73;
        if (r114 <= r18) goto L67;
        r27 = true;
        goto L73
    L67:
        if (r114 != r18) goto L73;
        if (r23 <= r9) goto L70;
        r29 = true;
    L70:
        if (r10 != r29) goto L73;
        r27 = true;
        goto L73
    L72:
        r111 = r8;
    L81:
        if (r17 == null) goto L84;
        return r17;
    L84:
        return r20;
    L22:
        if (isLayoutRTL() == false) goto L24;
        r10 = true;
        goto L25
    L18:
        r14 = 0;
        r15 = 1;
        r16 = getChildCount();
        goto L20
    L15:
        r13 = false;
        goto L16
    L11:
        r12 = false;
        goto L13
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler r2, RecyclerView.State r3, AccessibilityNodeInfoCompat r4) {
        super.onInitializeAccessibilityNodeInfo(r2, r3, r4);
        r4.setClassName(GridView.class.getName());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler r10, RecyclerView.State r11, View r12, AccessibilityNodeInfoCompat r13) {
        ViewGroup.LayoutParams r0 = r12.getLayoutParams();
        if ((r0 instanceof LayoutParams) == true) goto L6;
        super.onInitializeAccessibilityNodeInfoForItem(r12, r13);
        return;
    L6:
        LayoutParams r1 = (LayoutParams) r0;
        int r3 = getSpanGroupIndex(r10, r11, r1.getViewLayoutPosition());
        if (this.mOrientation != 0) goto L9;
        r13.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(r1.getSpanIndex(), r1.getSpanSize(), r3, 1, false, false));
        return;
    L9:
        r13.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(r3, 1, r1.getSpanIndex(), r1.getSpanSize(), false, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsAdded(RecyclerView r2, int r3, int r4) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsChanged(RecyclerView r2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsMoved(RecyclerView r2, int r3, int r4, int r5) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsRemoved(RecyclerView r2, int r3, int r4) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsUpdated(RecyclerView r2, int r3, int r4, Object r5) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(RecyclerView.Recycler r2, RecyclerView.State r3) {
        if (r3.isPreLayout() == false) goto L5;
        cachePreLayoutSpanMapping();
    L5:
        super.onLayoutChildren(r2, r3);
        clearPreLayoutSpanMappingCache();
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutCompleted(RecyclerView.State r2) {
        super.onLayoutCompleted(r2);
        this.mPendingSpanCountChange = false;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollHorizontallyBy(int r2, RecyclerView.Recycler r3, RecyclerView.State r4) {
        updateMeasurements();
        ensureViewSet();
        return super.scrollHorizontallyBy(r2, r3, r4);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollVerticallyBy(int r2, RecyclerView.Recycler r3, RecyclerView.State r4) {
        updateMeasurements();
        ensureViewSet();
        return super.scrollVerticallyBy(r2, r3, r4);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void setMeasuredDimension(Rect r8, int r9, int r10) {
        if (this.mCachedBorders != null) goto L5;
        super.setMeasuredDimension(r8, r9, r10);
    L5:
        int r0 = getPaddingLeft() + getPaddingRight();
        int r1 = getPaddingTop() + getPaddingBottom();
        if (this.mOrientation != 1) goto L8;
        int r4 = chooseSize(r10, r8.height() + r1, getMinimumHeight());
        int r2 = chooseSize(r9, this.mCachedBorders[this.mCachedBorders.length - 1] + r0, getMinimumWidth());
    L9:
        setMeasuredDimension(r2, r4);
        return;
    L8:
        int r5 = chooseSize(r9, r8.width() + r0, getMinimumWidth());
        r2 = r5;
        r4 = chooseSize(r10, this.mCachedBorders[this.mCachedBorders.length - 1] + r1, getMinimumHeight());
        goto L9
    }

    public void setSpanCount(int r4) {
        if (r4 != this.mSpanCount) goto L5;
        return;
    L5:
        this.mPendingSpanCountChange = true;
        if (r4 < 1) goto L10;
        this.mSpanCount = r4;
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        requestLayout();
        return;
    L10:
        throw new IllegalArgumentException("Span count should be at least 1. Provided " + r4);
    }

    public void setSpanSizeLookup(SpanSizeLookup r1) {
        this.mSpanSizeLookup = r1;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    public void setStackFromEnd(boolean r3) {
        if (r3 == true) goto L6;
        super.setStackFromEnd(false);
        return;
    L6:
        throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
    }

    public void setUsingSpansToEstimateScrollbarDimensions(boolean r1) {
        this.mUsingSpansToEstimateScrollBarDimensions = r1;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean supportsPredictiveItemAnimations() {
        if (this.mPendingSavedState == null) goto L5;
    L7:
        return false;
    L5:
        if (this.mPendingSpanCountChange == true) goto L7;
        return true;
    }
}
