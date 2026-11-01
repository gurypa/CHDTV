package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class LinearLayoutManager extends RecyclerView.LayoutManager implements ItemTouchHelper.ViewDropHandler, RecyclerView.SmoothScroller.ScrollVectorProvider {
    static final boolean DEBUG = false;
    public static final int HORIZONTAL = 0;
    public static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private static final float MAX_SCROLL_FACTOR = 0.33333334f;
    private static final String TAG = "LinearLayoutManager";
    public static final int VERTICAL = 1;
    final AnchorInfo mAnchorInfo;
    private int mInitialPrefetchItemCount;
    private boolean mLastStackFromEnd;
    private final LayoutChunkResult mLayoutChunkResult;
    private LayoutState mLayoutState;
    int mOrientation;
    OrientationHelper mOrientationHelper;
    SavedState mPendingSavedState;
    int mPendingScrollPosition;
    int mPendingScrollPositionOffset;
    private boolean mRecycleChildrenOnDetach;
    private int[] mReusableIntPair;
    private boolean mReverseLayout;
    boolean mShouldReverseLayout;
    private boolean mSmoothScrollbarEnabled;
    private boolean mStackFromEnd;

    static class AnchorInfo {
        int mCoordinate;
        boolean mLayoutFromEnd;
        OrientationHelper mOrientationHelper;
        int mPosition;
        boolean mValid;

        AnchorInfo() {
            reset();
        }

        void assignCoordinateFromPadding() {
            if (this.mLayoutFromEnd == false) goto L5;
            int r0 = this.mOrientationHelper.getEndAfterPadding();
        L6:
            this.mCoordinate = r0;
            return;
        L5:
            r0 = this.mOrientationHelper.getStartAfterPadding();
            goto L6
        }

        public void assignFromView(View r3, int r4) {
            if (this.mLayoutFromEnd == false) goto L5;
            this.mCoordinate = this.mOrientationHelper.getDecoratedEnd(r3) + this.mOrientationHelper.getTotalSpaceChange();
        L6:
            this.mPosition = r4;
            return;
        L5:
            this.mCoordinate = this.mOrientationHelper.getDecoratedStart(r3);
            goto L6
        }

        public void assignFromViewAndKeepVisibleRect(View r13, int r14) {
            int r0 = this.mOrientationHelper.getTotalSpaceChange();
            if (r0 < 0) goto L6;
            assignFromView(r13, r14);
            return;
        L6:
            this.mPosition = r14;
            if (this.mLayoutFromEnd == false) goto L14;
            int r4 = (this.mOrientationHelper.getEndAfterPadding() - r0) - this.mOrientationHelper.getDecoratedEnd(r13);
            this.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - r4;
            if (r4 <= 0) goto L23;
            int r6 = this.mCoordinate - this.mOrientationHelper.getDecoratedMeasurement(r13);
            int r7 = this.mOrientationHelper.getStartAfterPadding();
            int r9 = r6 - (Math.min(this.mOrientationHelper.getDecoratedStart(r13) - r7, 0) + r7);
            if (r9 >= 0) goto L24;
            this.mCoordinate += Math.min(r4, -r9);
            return;
        L24:
            return;
        L23:
            return;
        L14:
            int r1 = this.mOrientationHelper.getDecoratedStart(r13);
            int r3 = r1 - this.mOrientationHelper.getStartAfterPadding();
            this.mCoordinate = r1;
            if (r3 <= 0) goto L20;
            int r2 = (this.mOrientationHelper.getEndAfterPadding() - Math.min(0, (this.mOrientationHelper.getEndAfterPadding() - r0) - this.mOrientationHelper.getDecoratedEnd(r13))) - (this.mOrientationHelper.getDecoratedMeasurement(r13) + r1);
            if (r2 >= 0) goto L21;
            this.mCoordinate -= Math.min(r3, -r2);
            return;
        L21:
            return;
        }

        boolean isViewValidAsAnchor(View r4, RecyclerView.State r5) {
            RecyclerView.LayoutParams r0 = (RecyclerView.LayoutParams) r4.getLayoutParams();
            if (r0.isItemRemoved() == false) goto L5;
        L9:
            return false;
        L5:
            if (r0.getViewLayoutPosition() < 0) goto L9;
            if (r0.getViewLayoutPosition() >= r5.getItemCount()) goto L9;
            return true;
        }

        void reset() {
            this.mPosition = -1;
            this.mCoordinate = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
            this.mValid = false;
        }

        public String toString() {
            return "AnchorInfo{mPosition=" + this.mPosition + ", mCoordinate=" + this.mCoordinate + ", mLayoutFromEnd=" + this.mLayoutFromEnd + ", mValid=" + this.mValid + '}';
        }
    }

    protected static class LayoutChunkResult {
        public int mConsumed;
        public boolean mFinished;
        public boolean mFocusable;
        public boolean mIgnoreConsumed;

        protected LayoutChunkResult() {
        }

        void resetInternal() {
            this.mConsumed = 0;
            this.mFinished = false;
            this.mIgnoreConsumed = false;
            this.mFocusable = false;
        }
    }

    static class LayoutState {
        static final int INVALID_LAYOUT = Integer.MIN_VALUE;
        static final int ITEM_DIRECTION_HEAD = -1;
        static final int ITEM_DIRECTION_TAIL = 1;
        static final int LAYOUT_END = 1;
        static final int LAYOUT_START = -1;
        static final int SCROLLING_OFFSET_NaN = Integer.MIN_VALUE;
        static final String TAG = "LLM#LayoutState";
        int mAvailable;
        int mCurrentPosition;
        int mExtraFillSpace;
        boolean mInfinite;
        boolean mIsPreLayout;
        int mItemDirection;
        int mLastScrollDelta;
        int mLayoutDirection;
        int mNoRecycleSpace;
        int mOffset;
        boolean mRecycle;
        List<RecyclerView.ViewHolder> mScrapList;
        int mScrollingOffset;

        LayoutState() {
            this.mRecycle = true;
            this.mExtraFillSpace = 0;
            this.mNoRecycleSpace = 0;
            this.mIsPreLayout = false;
            this.mScrapList = null;
        }

        private View nextViewFromScrapList() {
            int r0 = this.mScrapList.size();
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L12;
            View r2 = this.mScrapList.get(r1).itemView;
            RecyclerView.LayoutParams r3 = (RecyclerView.LayoutParams) r2.getLayoutParams();
            if (r3.isItemRemoved() == true) goto L11;
            if (this.mCurrentPosition != r3.getViewLayoutPosition()) goto L11;
            assignPositionFromScrapList(r2);
            return r2;
        L11:
            r1 = r1 + 1;
            goto L3
        L12:
            return null;
        }

        public void assignPositionFromScrapList() {
            assignPositionFromScrapList(null);
        }

        public void assignPositionFromScrapList(View r3) {
            View r0 = nextViewInLimitedList(r3);
            if (r0 != null) goto L5;
            this.mCurrentPosition = -1;
            return;
        L5:
            this.mCurrentPosition = ((RecyclerView.LayoutParams) r0.getLayoutParams()).getViewLayoutPosition();
        }

        boolean hasMore(RecyclerView.State r3) {
            if (this.mCurrentPosition >= 0) goto L5;
        L7:
            return false;
        L5:
            if (this.mCurrentPosition >= r3.getItemCount()) goto L7;
            return true;
        }

        void log() {
            Log.d(TAG, "avail:" + this.mAvailable + ", ind:" + this.mCurrentPosition + ", dir:" + this.mItemDirection + ", offset:" + this.mOffset + ", layoutDir:" + this.mLayoutDirection);
        }

        View next(RecyclerView.Recycler r4) {
            if (this.mScrapList != null) goto L5;
            View r0 = r4.getViewForPosition(this.mCurrentPosition);
            this.mCurrentPosition += this.mItemDirection;
            return r0;
        L5:
            return nextViewFromScrapList();
        }

        public View nextViewInLimitedList(View r9) {
            int r0 = this.mScrapList.size();
            View r1 = null;
            int r2 = Integer.MAX_VALUE;
            int r3 = 0;
        L3:
            if (r3 >= r0) goto L17;
            View r4 = this.mScrapList.get(r3).itemView;
            RecyclerView.LayoutParams r5 = (RecyclerView.LayoutParams) r4.getLayoutParams();
            if (r4 == r9) goto L16;
            if (r5.isItemRemoved() == true) goto L16;
            int r6 = (r5.getViewLayoutPosition() - this.mCurrentPosition) * this.mItemDirection;
            if (r6 < 0) goto L16;
            if (r6 >= r2) goto L16;
            r1 = r4;
            r2 = r6;
            if (r6 == 0) goto L17;
        L16:
            r3 = r3 + 1;
        L17:
            return r1;
        }
    }

    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = null;
        boolean mAnchorLayoutFromEnd;
        int mAnchorOffset;
        int mAnchorPosition;

        static {
            CREATOR = new C04961();
        }

        public SavedState() {
        }

        SavedState(Parcel r3) {
            this.mAnchorPosition = r3.readInt();
            this.mAnchorOffset = r3.readInt();
            boolean r1 = true;
            if (r3.readInt() == 1) goto L6;
            r1 = false;
        L6:
            this.mAnchorLayoutFromEnd = r1;
        }

        public SavedState(SavedState r2) {
            this.mAnchorPosition = r2.mAnchorPosition;
            this.mAnchorOffset = r2.mAnchorOffset;
            this.mAnchorLayoutFromEnd = r2.mAnchorLayoutFromEnd;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        boolean hasValidAnchor() {
            if (this.mAnchorPosition < 0) goto L5;
            return true;
        L5:
            return false;
        }

        void invalidateAnchor() {
            this.mAnchorPosition = -1;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel r2, int r3) {
            r2.writeInt(this.mAnchorPosition);
            r2.writeInt(this.mAnchorOffset);
            r2.writeInt(this.mAnchorLayoutFromEnd ? 1 : 0);
        }
    }

    public LinearLayoutManager(Context r3) {
        this(r3, 1, false);
    }

    public LinearLayoutManager(Context r3, int r4, boolean r5) {
        this.mOrientation = 1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mStackFromEnd = false;
        this.mSmoothScrollbarEnabled = true;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
        this.mAnchorInfo = new AnchorInfo();
        this.mLayoutChunkResult = new LayoutChunkResult();
        this.mInitialPrefetchItemCount = 2;
        this.mReusableIntPair = new int[2];
        setOrientation(r4);
        setReverseLayout(r5);
    }

    public LinearLayoutManager(Context r3, AttributeSet r4, int r5, int r6) {
        this.mOrientation = 1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mStackFromEnd = false;
        this.mSmoothScrollbarEnabled = true;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
        this.mAnchorInfo = new AnchorInfo();
        this.mLayoutChunkResult = new LayoutChunkResult();
        this.mInitialPrefetchItemCount = 2;
        this.mReusableIntPair = new int[2];
        RecyclerView.LayoutManager.Properties r0 = getProperties(r3, r4, r5, r6);
        setOrientation(r0.orientation);
        setReverseLayout(r0.reverseLayout);
        setStackFromEnd(r0.stackFromEnd);
    }

    private int computeScrollExtent(RecyclerView.State r8) {
        if (getChildCount() != 0) goto L6;
        return 0;
    L6:
        ensureLayoutState();
        return ScrollbarHelper.computeScrollExtent(r8, this.mOrientationHelper, findFirstVisibleChildClosestToStart(!this.mSmoothScrollbarEnabled, true), findFirstVisibleChildClosestToEnd(!this.mSmoothScrollbarEnabled, true), this, this.mSmoothScrollbarEnabled);
    }

    private int computeScrollOffset(RecyclerView.State r9) {
        if (getChildCount() != 0) goto L6;
        return 0;
    L6:
        ensureLayoutState();
        return ScrollbarHelper.computeScrollOffset(r9, this.mOrientationHelper, findFirstVisibleChildClosestToStart(!this.mSmoothScrollbarEnabled, true), findFirstVisibleChildClosestToEnd(!this.mSmoothScrollbarEnabled, true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    }

    private int computeScrollRange(RecyclerView.State r8) {
        if (getChildCount() != 0) goto L6;
        return 0;
    L6:
        ensureLayoutState();
        return ScrollbarHelper.computeScrollRange(r8, this.mOrientationHelper, findFirstVisibleChildClosestToStart(!this.mSmoothScrollbarEnabled, true), findFirstVisibleChildClosestToEnd(!this.mSmoothScrollbarEnabled, true), this, this.mSmoothScrollbarEnabled);
    }

    private View findFirstPartiallyOrCompletelyInvisibleChild() {
        return findOnePartiallyOrCompletelyInvisibleChild(0, getChildCount());
    }

    private View findLastPartiallyOrCompletelyInvisibleChild() {
        return findOnePartiallyOrCompletelyInvisibleChild(getChildCount() - 1, -1);
    }

    private View findPartiallyOrCompletelyInvisibleChildClosestToEnd() {
        if (this.mShouldReverseLayout == false) goto L6;
        return findFirstPartiallyOrCompletelyInvisibleChild();
    L6:
        return findLastPartiallyOrCompletelyInvisibleChild();
    }

    private View findPartiallyOrCompletelyInvisibleChildClosestToStart() {
        if (this.mShouldReverseLayout == false) goto L6;
        return findLastPartiallyOrCompletelyInvisibleChild();
    L6:
        return findFirstPartiallyOrCompletelyInvisibleChild();
    }

    private int fixLayoutEndGap(int r4, RecyclerView.Recycler r5, RecyclerView.State r6, boolean r7) {
        int r0 = this.mOrientationHelper.getEndAfterPadding() - r4;
        if (r0 <= 0) goto L11;
        int r1 = -scrollBy(-r0, r5, r6);
        int r8 = r4 + r1;
        if (r7 == false) goto L10;
        int r2 = this.mOrientationHelper.getEndAfterPadding() - r8;
        if (r2 <= 0) goto L10;
        this.mOrientationHelper.offsetChildren(r2);
        return r2 + r1;
    L10:
        return r1;
    L11:
        return 0;
    }

    private int fixLayoutStartGap(int r5, RecyclerView.Recycler r6, RecyclerView.State r7, boolean r8) {
        int r0 = r5 - this.mOrientationHelper.getStartAfterPadding();
        if (r0 <= 0) goto L11;
        int r1 = -scrollBy(r0, r6, r7);
        int r9 = r5 + r1;
        if (r8 == false) goto L10;
        int r2 = r9 - this.mOrientationHelper.getStartAfterPadding();
        if (r2 <= 0) goto L10;
        this.mOrientationHelper.offsetChildren(-r2);
        return r1 - r2;
    L10:
        return r1;
    L11:
        return 0;
    }

    private View getChildClosestToEnd() {
        if (this.mShouldReverseLayout == false) goto L5;
        int r0 = 0;
    L7:
        return getChildAt(r0);
    L5:
        r0 = getChildCount() - 1;
        goto L7
    }

    private View getChildClosestToStart() {
        if (this.mShouldReverseLayout == false) goto L5;
        int r0 = getChildCount() - 1;
    L7:
        return getChildAt(r0);
    L5:
        r0 = 0;
        goto L7
    }

    private void layoutForPredictiveAnimations(RecyclerView.Recycler r17, RecyclerView.State r18, int r19, int r20) {
        if (r18.willRunPredictiveAnimations() == false) goto L38;
        if (getChildCount() == 0) goto L38;
        if (r18.isPreLayout() == true) goto L38;
        if (supportsPredictiveItemAnimations() == false) goto L43;
        int r3 = 0;
        int r4 = 0;
        List<RecyclerView.ViewHolder> r5 = r17.getScrapList();
        int r6 = r5.size();
        int r8 = getPosition(getChildAt(0));
        int r9 = 0;
    L12:
        if (r9 >= r6) goto L28;
        RecyclerView.ViewHolder r10 = r5.get(r9);
        if (r10.isRemoved() == true) goto L27;
        byte r12 = 1;
        if (r10.getLayoutPosition() >= r8) goto L19;
        boolean r13 = true;
    L21:
        if (r13 == this.mShouldReverseLayout) goto L24;
        r12 = -1;
    L24:
        if (r12 != (-1)) goto L26;
        r3 = r3 + this.mOrientationHelper.getDecoratedMeasurement(r10.itemView);
        goto L27
    L26:
        r4 = r4 + this.mOrientationHelper.getDecoratedMeasurement(r10.itemView);
        goto L27
    L19:
        r13 = false;
    L27:
        r9 = r9 + 1;
        goto L12
    L28:
        this.mLayoutState.mScrapList = r5;
        if (r3 <= 0) goto L32;
        updateLayoutStateToFillStart(getPosition(getChildClosestToStart()), r19);
        this.mLayoutState.mExtraFillSpace = r3;
        this.mLayoutState.mAvailable = 0;
        this.mLayoutState.assignPositionFromScrapList();
        fill(r17, this.mLayoutState, r18, false);
    L32:
        if (r4 <= 0) goto L35;
        updateLayoutStateToFillEnd(getPosition(getChildClosestToEnd()), r20);
        this.mLayoutState.mExtraFillSpace = r4;
        this.mLayoutState.mAvailable = 0;
        this.mLayoutState.assignPositionFromScrapList();
        fill(r17, this.mLayoutState, r18, false);
    L35:
        this.mLayoutState.mScrapList = null;
        return;
    L43:
        return;
    }

    private void logChildren() {
        Log.d(TAG, "internal representation of views on the screen");
        int r0 = 0;
    L4:
        if (r0 >= getChildCount()) goto L6;
        View r2 = getChildAt(r0);
        Log.d(TAG, "item " + getPosition(r2) + ", coord:" + this.mOrientationHelper.getDecoratedStart(r2));
        r0 = r0 + 1;
        goto L4
    L6:
        Log.d(TAG, "==============");
    }

    private void recycleByLayoutState(RecyclerView.Recycler r5, LayoutState r6) {
        if (r6.mRecycle == true) goto L5;
        return;
    L5:
        if (r6.mInfinite == true) goto L14;
        int r0 = r6.mScrollingOffset;
        int r1 = r6.mNoRecycleSpace;
        if (r6.mLayoutDirection != (-1)) goto L10;
        recycleViewsFromEnd(r5, r0, r1);
        return;
    L10:
        recycleViewsFromStart(r5, r0, r1);
        return;
    }

    private void recycleChildren(RecyclerView.Recycler r2, int r3, int r4) {
        if (r3 != r4) goto L4;
        return;
    L4:
        if (r4 <= r3) goto L9;
        int r0 = r4 - 1;
    L6:
        if (r0 < r3) goto L12;
        removeAndRecycleViewAt(r0, r2);
        r0 = r0 - 1;
        goto L6
    L12:
        return;
    L9:
        int r1 = r3;
    L10:
        if (r1 <= r4) goto L15;
        removeAndRecycleViewAt(r1, r2);
        r1 = r1 - 1;
        goto L10
    }

    private void recycleViewsFromEnd(RecyclerView.Recycler r6, int r7, int r8) {
        int r0 = getChildCount();
        if (r7 >= 0) goto L5;
        return;
    L5:
        int r1 = (this.mOrientationHelper.getEnd() - r7) + r8;
        if (this.mShouldReverseLayout == false) goto L18;
        int r2 = 0;
    L8:
        if (r2 >= r0) goto L28;
        View r3 = getChildAt(r2);
        if (this.mOrientationHelper.getDecoratedStart(r3) < r1) goto L15;
        if (this.mOrientationHelper.getTransformedStartWithDecoration(r3) < r1) goto L15;
        r2 = r2 + 1;
    L15:
        recycleChildren(r6, 0, r2);
        return;
    L28:
        return;
    L18:
        int r4 = r0 - 1;
    L19:
        if (r4 < 0) goto L35;
        View r5 = getChildAt(r4);
        if (this.mOrientationHelper.getDecoratedStart(r5) < r1) goto L26;
        if (this.mOrientationHelper.getTransformedStartWithDecoration(r5) < r1) goto L26;
        r4 = r4 - 1;
    L26:
        recycleChildren(r6, r0 - 1, r4);
        return;
    }

    private void recycleViewsFromStart(RecyclerView.Recycler r6, int r7, int r8) {
        if (r7 >= 0) goto L4;
        return;
    L4:
        int r0 = r7 - r8;
        int r1 = getChildCount();
        if (this.mShouldReverseLayout == false) goto L17;
        int r2 = r1 - 1;
    L7:
        if (r2 < 0) goto L27;
        View r3 = getChildAt(r2);
        if (this.mOrientationHelper.getDecoratedEnd(r3) > r0) goto L14;
        if (this.mOrientationHelper.getTransformedEndWithDecoration(r3) > r0) goto L14;
        r2 = r2 - 1;
    L14:
        recycleChildren(r6, r1 - 1, r2);
        return;
    L27:
        return;
    L17:
        int r4 = 0;
    L18:
        if (r4 >= r1) goto L34;
        View r5 = getChildAt(r4);
        if (this.mOrientationHelper.getDecoratedEnd(r5) > r0) goto L25;
        if (this.mOrientationHelper.getTransformedEndWithDecoration(r5) > r0) goto L25;
        r4 = r4 + 1;
    L25:
        recycleChildren(r6, 0, r4);
        return;
    }

    private void resolveShouldLayoutReverse() {
        if (this.mOrientation != 1) goto L5;
    L8:
        this.mShouldReverseLayout = this.mReverseLayout;
        return;
    L5:
        if (isLayoutRTL() == false) goto L8;
        this.mShouldReverseLayout = !this.mReverseLayout;
    }

    private boolean updateAnchorFromChildren(RecyclerView.Recycler r11, RecyclerView.State r12, AnchorInfo r13) {
        boolean r1 = false;
        if (getChildCount() != 0) goto L5;
        return false;
    L5:
        View r0 = getFocusedChild();
        if (r0 == null) goto L12;
        if (r13.isViewValidAsAnchor(r0, r12) == false) goto L12;
        r13.assignFromViewAndKeepVisibleRect(r0, getPosition(r0));
        return true;
    L12:
        if (this.mLastStackFromEnd == this.mStackFromEnd) goto L14;
        return false;
    L14:
        View r3 = findReferenceChild(r11, r12, r13.mLayoutFromEnd, this.mStackFromEnd);
        if (r3 == null) goto L36;
        r13.assignFromView(r3, getPosition(r3));
        if (r12.isPreLayout() == false) goto L19;
    L35:
        return true;
    L19:
        if (supportsPredictiveItemAnimations() == false) goto L35;
        int r4 = this.mOrientationHelper.getDecoratedStart(r3);
        int r5 = this.mOrientationHelper.getDecoratedEnd(r3);
        int r6 = this.mOrientationHelper.getStartAfterPadding();
        int r7 = this.mOrientationHelper.getEndAfterPadding();
        if (r5 > r6) goto L24;
        if (r4 >= r6) goto L24;
        boolean r8 = true;
    L25:
        if (r4 < r7) goto L28;
        if (r5 <= r7) goto L28;
        r1 = true;
    L28:
        if (r8 == true) goto L31;
        if (r1 == false) goto L35;
    L31:
        if (r13.mLayoutFromEnd == false) goto L33;
        int r9 = r7;
    L34:
        r13.mCoordinate = r9;
        goto L35
    L33:
        r9 = r6;
    L24:
        r8 = false;
        goto L25
    L36:
        return false;
    }

    private boolean updateAnchorFromPendingData(RecyclerView.State r8, AnchorInfo r9) {
        boolean r1 = false;
        if (r8.isPreLayout() == false) goto L5;
    L61:
        return false;
    L5:
        if (this.mPendingScrollPosition == (-1)) goto L61;
        if (this.mPendingScrollPosition >= 0) goto L10;
    L59:
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        return false;
    L10:
        if (this.mPendingScrollPosition >= r8.getItemCount()) goto L59;
        r9.mPosition = this.mPendingScrollPosition;
        if (this.mPendingSavedState == null) goto L22;
        if (this.mPendingSavedState.hasValidAnchor() == false) goto L22;
        r9.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
        if (r9.mLayoutFromEnd == false) goto L19;
        r9.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingSavedState.mAnchorOffset;
    L20:
        return true;
    L19:
        r9.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingSavedState.mAnchorOffset;
    L22:
        if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) goto L54;
        View r0 = findViewByPosition(this.mPendingScrollPosition);
        if (r0 == null) goto L43;
        if (this.mOrientationHelper.getDecoratedMeasurement(r0) <= this.mOrientationHelper.getTotalSpace()) goto L30;
        r9.assignCoordinateFromPadding();
        return true;
    L30:
        if ((this.mOrientationHelper.getDecoratedStart(r0) - this.mOrientationHelper.getStartAfterPadding()) >= 0) goto L34;
        r9.mCoordinate = this.mOrientationHelper.getStartAfterPadding();
        r9.mLayoutFromEnd = false;
        return true;
    L34:
        if ((this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(r0)) >= 0) goto L38;
        r9.mCoordinate = this.mOrientationHelper.getEndAfterPadding();
        r9.mLayoutFromEnd = true;
        return true;
    L38:
        if (r9.mLayoutFromEnd == false) goto L40;
        int r5 = this.mOrientationHelper.getDecoratedEnd(r0) + this.mOrientationHelper.getTotalSpaceChange();
    L41:
        r9.mCoordinate = r5;
    L53:
        return true;
    L40:
        r5 = this.mOrientationHelper.getDecoratedStart(r0);
        goto L41
    L43:
        if (getChildCount() > 0) goto L45;
    L52:
        r9.assignCoordinateFromPadding();
        goto L53
    L45:
        if (this.mPendingScrollPosition >= getPosition(getChildAt(0))) goto L47;
        boolean r4 = true;
    L49:
        if (r4 != this.mShouldReverseLayout) goto L51;
        r1 = true;
    L51:
        r9.mLayoutFromEnd = r1;
        goto L52
    L47:
        r4 = false;
        goto L49
    L54:
        r9.mLayoutFromEnd = this.mShouldReverseLayout;
        if (this.mShouldReverseLayout == false) goto L57;
        r9.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingScrollPositionOffset;
    L58:
        return true;
    L57:
        r9.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingScrollPositionOffset;
        goto L58
    }

    private void updateAnchorInfoForLayout(RecyclerView.Recycler r2, RecyclerView.State r3, AnchorInfo r4) {
        if (updateAnchorFromPendingData(r3, r4) == false) goto L6;
        return;
    L6:
        if (updateAnchorFromChildren(r2, r3, r4) == false) goto L8;
        return;
    L8:
        r4.assignCoordinateFromPadding();
        if (this.mStackFromEnd == false) goto L11;
        int r0 = r3.getItemCount() - 1;
    L12:
        r4.mPosition = r0;
        return;
    L11:
        r0 = 0;
        goto L12
    }

    private void updateLayoutState(int r10, int r11, boolean r12, RecyclerView.State r13) {
        this.mLayoutState.mInfinite = resolveIsInfinite();
        this.mLayoutState.mLayoutDirection = r10;
        boolean r1 = false;
        this.mReusableIntPair[0] = 0;
        int r2 = 1;
        this.mReusableIntPair[1] = 0;
        calculateExtraLayoutSpace(r13, this.mReusableIntPair);
        int r0 = Math.max(0, this.mReusableIntPair[0]);
        int r3 = Math.max(0, this.mReusableIntPair[1]);
        if (r10 != 1) goto L5;
        r1 = true;
    L5:
        LayoutState r4 = this.mLayoutState;
        if (r1 == false) goto L8;
        int r5 = r3;
    L9:
        r4.mExtraFillSpace = r5;
        LayoutState r6 = this.mLayoutState;
        if (r1 == false) goto L12;
        int r7 = r0;
    L13:
        r6.mNoRecycleSpace = r7;
        if (r1 == false) goto L20;
        this.mLayoutState.mExtraFillSpace += this.mOrientationHelper.getEndPadding();
        View r8 = getChildClosestToEnd();
        LayoutState r9 = this.mLayoutState;
        if (this.mShouldReverseLayout == false) goto L19;
        r2 = -1;
    L19:
        r9.mItemDirection = r2;
        this.mLayoutState.mCurrentPosition = getPosition(r8) + this.mLayoutState.mItemDirection;
        this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedEnd(r8);
        int r14 = this.mOrientationHelper.getDecoratedEnd(r8) - this.mOrientationHelper.getEndAfterPadding();
    L25:
        this.mLayoutState.mAvailable = r11;
        if (r12 == false) goto L28;
        this.mLayoutState.mAvailable -= r14;
    L28:
        this.mLayoutState.mScrollingOffset = r14;
        return;
    L20:
        View r15 = getChildClosestToStart();
        this.mLayoutState.mExtraFillSpace += this.mOrientationHelper.getStartAfterPadding();
        LayoutState r16 = this.mLayoutState;
        if (this.mShouldReverseLayout == true) goto L24;
        r2 = -1;
    L24:
        r16.mItemDirection = r2;
        this.mLayoutState.mCurrentPosition = getPosition(r15) + this.mLayoutState.mItemDirection;
        this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedStart(r15);
        r14 = (-this.mOrientationHelper.getDecoratedStart(r15)) + this.mOrientationHelper.getStartAfterPadding();
        goto L25
    L12:
        r7 = r3;
        goto L13
    L8:
        r5 = r0;
        goto L9
    }

    private void updateLayoutStateToFillEnd(int r4, int r5) {
        this.mLayoutState.mAvailable = this.mOrientationHelper.getEndAfterPadding() - r5;
        LayoutState r0 = this.mLayoutState;
        if (this.mShouldReverseLayout == false) goto L5;
        int r1 = -1;
    L6:
        r0.mItemDirection = r1;
        this.mLayoutState.mCurrentPosition = r4;
        this.mLayoutState.mLayoutDirection = 1;
        this.mLayoutState.mOffset = r5;
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
        return;
    L5:
        r1 = 1;
        goto L6
    }

    private void updateLayoutStateToFillEnd(AnchorInfo r3) {
        updateLayoutStateToFillEnd(r3.mPosition, r3.mCoordinate);
    }

    private void updateLayoutStateToFillStart(int r4, int r5) {
        this.mLayoutState.mAvailable = r5 - this.mOrientationHelper.getStartAfterPadding();
        this.mLayoutState.mCurrentPosition = r4;
        LayoutState r0 = this.mLayoutState;
        if (this.mShouldReverseLayout == false) goto L5;
        int r1 = 1;
    L6:
        r0.mItemDirection = r1;
        this.mLayoutState.mLayoutDirection = -1;
        this.mLayoutState.mOffset = r5;
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
        return;
    L5:
        r1 = -1;
        goto L6
    }

    private void updateLayoutStateToFillStart(AnchorInfo r3) {
        updateLayoutStateToFillStart(r3.mPosition, r3.mCoordinate);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void assertNotInLayoutOrScroll(String r2) {
        if (this.mPendingSavedState != null) goto L6;
        super.assertNotInLayoutOrScroll(r2);
        return;
    }

    protected void calculateExtraLayoutSpace(RecyclerView.State r6, int[] r7) {
        int r0 = 0;
        int r1 = 0;
        int r2 = getExtraLayoutSpace(r6);
        if (this.mLayoutState.mLayoutDirection != (-1)) goto L5;
        r0 = r2;
    L6:
        r7[0] = r0;
        r7[1] = r1;
        return;
    L5:
        r1 = r2;
        goto L6
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollHorizontally() {
        if (this.mOrientation != 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollVertically() {
        if (this.mOrientation == 1) goto L7;
        return false;
    L7:
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void collectAdjacentPrefetchPositions(int r5, int r6, RecyclerView.State r7, RecyclerView.LayoutManager.LayoutPrefetchRegistry r8) {
        if (this.mOrientation != 0) goto L5;
        int r0 = r5;
    L7:
        if (getChildCount() == 0) goto L16;
        if (r0 == 0) goto L17;
        ensureLayoutState();
        if (r0 <= 0) goto L13;
        int r2 = 1;
    L14:
        updateLayoutState(r2, Math.abs(r0), true, r7);
        collectPrefetchPositionsForLayoutState(r7, this.mLayoutState, r8);
        return;
    L13:
        r2 = -1;
        goto L14
    L17:
        return;
    L16:
        return;
    L5:
        r0 = r6;
        goto L7
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void collectInitialPrefetchPositions(int r8, RecyclerView.LayoutManager.LayoutPrefetchRegistry r9) {
        int r2 = -1;
        if (this.mPendingSavedState != null) goto L5;
    L7:
        resolveShouldLayoutReverse();
        boolean r0 = this.mShouldReverseLayout;
        if (this.mPendingScrollPosition != (-1)) goto L13;
        if (r0 == false) goto L11;
        int r3 = r8 - 1;
    L14:
        if (r0 == true) goto L17;
        r2 = 1;
    L17:
        int r4 = r3;
        int r5 = 0;
    L19:
        if (r5 >= this.mInitialPrefetchItemCount) goto L23;
        if (r4 < 0) goto L27;
        if (r4 >= r8) goto L28;
        r9.addPosition(r4, 0);
        r4 = r4 + r2;
        r5 = r5 + 1;
        goto L19
    L28:
        return;
    L27:
        return;
    L23:
        return;
    L11:
        r3 = 0;
        goto L14
    L13:
        r3 = this.mPendingScrollPosition;
        goto L14
    L5:
        if (this.mPendingSavedState.hasValidAnchor() == false) goto L7;
        r0 = this.mPendingSavedState.mAnchorLayoutFromEnd;
        r3 = this.mPendingSavedState.mAnchorPosition;
        goto L14
    }

    void collectPrefetchPositionsForLayoutState(RecyclerView.State r4, LayoutState r5, RecyclerView.LayoutManager.LayoutPrefetchRegistry r6) {
        int r0 = r5.mCurrentPosition;
        if (r0 >= 0) goto L5;
        return;
    L5:
        if (r0 >= r4.getItemCount()) goto L9;
        r6.addPosition(r0, Math.max(0, r5.mScrollingOffset));
        return;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeHorizontalScrollExtent(RecyclerView.State r2) {
        return computeScrollExtent(r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeHorizontalScrollOffset(RecyclerView.State r2) {
        return computeScrollOffset(r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeHorizontalScrollRange(RecyclerView.State r2) {
        return computeScrollRange(r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller.ScrollVectorProvider
    public PointF computeScrollVectorForPosition(int r6) {
        if (getChildCount() != 0) goto L6;
        return null;
    L6:
        boolean r0 = false;
        int r2 = 1;
        if (r6 >= getPosition(getChildAt(0))) goto L10;
        r0 = true;
    L10:
        if (r0 == this.mShouldReverseLayout) goto L13;
        r2 = -1;
    L13:
        if (this.mOrientation != 0) goto L17;
        return new PointF(r2, 0.0f);
    L17:
        return new PointF(0.0f, r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeVerticalScrollExtent(RecyclerView.State r2) {
        return computeScrollExtent(r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeVerticalScrollOffset(RecyclerView.State r2) {
        return computeScrollOffset(r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeVerticalScrollRange(RecyclerView.State r2) {
        return computeScrollRange(r2);
    }

    int convertFocusDirectionToLayoutDirection(int r5) {
        switch(r5) {
            case 1: goto L33;
            case 2: goto L26;
            case 17: goto L21;
            case 33: goto L16;
            case 66: goto L11;
            case 130: goto L6;
            default: goto L4;
        };
    L4:
        return Integer.MIN_VALUE;
    L6:
        if (this.mOrientation == 1) goto L9;
        return Integer.MIN_VALUE;
    L9:
        return 1;
    L11:
        if (this.mOrientation == 0) goto L14;
        return Integer.MIN_VALUE;
    L14:
        return 1;
    L16:
        if (this.mOrientation != 1) goto L19;
        return -1;
    L19:
        return Integer.MIN_VALUE;
    L21:
        if (this.mOrientation != 0) goto L24;
        return -1;
    L24:
        return Integer.MIN_VALUE;
    L26:
        if (this.mOrientation != 1) goto L29;
        return 1;
    L29:
        if (isLayoutRTL() == false) goto L31;
        return -1;
    L31:
        return 1;
    L33:
        if (this.mOrientation != 1) goto L36;
        return -1;
    L36:
        if (isLayoutRTL() == false) goto L38;
        return 1;
    L38:
        return -1;
    }

    LayoutState createLayoutState() {
        return new LayoutState();
    }

    void ensureLayoutState() {
        if (this.mLayoutState != null) goto L6;
        this.mLayoutState = createLayoutState();
        return;
    }

    int fill(RecyclerView.Recycler r8, LayoutState r9, RecyclerView.State r10, boolean r11) {
        int r0 = r9.mAvailable;
        if (r9.mScrollingOffset != Integer.MIN_VALUE) goto L5;
    L8:
        int r1 = r9.mAvailable + r9.mExtraFillSpace;
        LayoutChunkResult r3 = this.mLayoutChunkResult;
    L10:
        if (r9.mInfinite == true) goto L13;
        if (r1 > 0) goto L13;
    L34:
        return r0 - r9.mAvailable;
    L13:
        if (r9.hasMore(r10) == false) goto L34;
        r3.resetInternal();
        layoutChunk(r8, r10, r9, r3);
        if (r3.mFinished == true) goto L34;
        r9.mOffset += r3.mConsumed * r9.mLayoutDirection;
        if (r3.mIgnoreConsumed == true) goto L20;
    L23:
        r9.mAvailable -= r3.mConsumed;
        r1 = r1 - r3.mConsumed;
    L25:
        if (r9.mScrollingOffset == Integer.MIN_VALUE) goto L30;
        r9.mScrollingOffset += r3.mConsumed;
        if (r9.mAvailable >= 0) goto L29;
        r9.mScrollingOffset += r9.mAvailable;
    L29:
        recycleByLayoutState(r8, r9);
    L30:
        if (r11 == false) goto L10;
        if (r3.mFocusable == false) goto L10;
    L20:
        if (r9.mScrapList != null) goto L23;
        if (r10.isPreLayout() == true) goto L25;
    L5:
        if (r9.mAvailable >= 0) goto L7;
        r9.mScrollingOffset += r9.mAvailable;
    L7:
        recycleByLayoutState(r8, r9);
        goto L8
    }

    public int findFirstCompletelyVisibleItemPosition() {
        View r0 = findOneVisibleChild(0, getChildCount(), true, false);
        if (r0 != null) goto L6;
        return -1;
    L6:
        return getPosition(r0);
    }

    View findFirstVisibleChildClosestToEnd(boolean r3, boolean r4) {
        if (this.mShouldReverseLayout == false) goto L7;
        return findOneVisibleChild(0, getChildCount(), r3, r4);
    L7:
        return findOneVisibleChild(getChildCount() - 1, -1, r3, r4);
    }

    View findFirstVisibleChildClosestToStart(boolean r3, boolean r4) {
        if (this.mShouldReverseLayout == false) goto L7;
        return findOneVisibleChild(getChildCount() - 1, -1, r3, r4);
    L7:
        return findOneVisibleChild(0, getChildCount(), r3, r4);
    }

    public int findFirstVisibleItemPosition() {
        View r0 = findOneVisibleChild(0, getChildCount(), false, true);
        if (r0 != null) goto L6;
        return -1;
    L6:
        return getPosition(r0);
    }

    public int findLastCompletelyVisibleItemPosition() {
        View r0 = findOneVisibleChild(getChildCount() - 1, -1, true, false);
        if (r0 != null) goto L6;
        return -1;
    L6:
        return getPosition(r0);
    }

    public int findLastVisibleItemPosition() {
        View r0 = findOneVisibleChild(getChildCount() - 1, -1, false, true);
        if (r0 != null) goto L6;
        return -1;
    L6:
        return getPosition(r0);
    }

    View findOnePartiallyOrCompletelyInvisibleChild(int r6, int r7) {
        ensureLayoutState();
        if (r7 <= r6) goto L5;
        byte r0 = 1;
    L8:
        if (r0 != 0) goto L12;
        return getChildAt(r6);
    L12:
        if (this.mOrientationHelper.getDecoratedStart(getChildAt(r6)) >= this.mOrientationHelper.getStartAfterPadding()) goto L14;
        int r1 = 16644;
        int r2 = 16388;
    L16:
        if (this.mOrientation != 0) goto L19;
        return this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(r6, r7, r1, r2);
    L19:
        return this.mVerticalBoundCheck.findOneViewWithinBoundFlags(r6, r7, r1, r2);
    L14:
        r1 = 4161;
        r2 = 4097;
        goto L16
    L5:
        if (r7 >= r6) goto L7;
        r0 = -1;
        goto L8
    L7:
        r0 = 0;
        goto L8
    }

    View findOneVisibleChild(int r4, int r5, boolean r6, boolean r7) {
        ensureLayoutState();
        int r1 = 0;
        if (r6 == false) goto L5;
        int r0 = 24579;
    L6:
        if (r7 == false) goto L9;
        r1 = 320;
    L9:
        if (this.mOrientation != 0) goto L12;
        return this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(r4, r5, r0, r1);
    L12:
        return this.mVerticalBoundCheck.findOneViewWithinBoundFlags(r4, r5, r0, r1);
    L5:
        r0 = 320;
        goto L6
    }

    View findReferenceChild(RecyclerView.Recycler r19, RecyclerView.State r20, boolean r21, boolean r22) {
        ensureLayoutState();
        int r1 = 0;
        int r2 = getChildCount();
        int r3 = 1;
        if (r22 == false) goto L5;
        r1 = getChildCount() - 1;
        r2 = -1;
        r3 = -1;
    L5:
        int r5 = r20.getItemCount();
        int r6 = this.mOrientationHelper.getStartAfterPadding();
        int r7 = this.mOrientationHelper.getEndAfterPadding();
        View r8 = null;
        View r9 = null;
        View r10 = null;
        int r11 = r1;
    L6:
        if (r11 == r2) goto L36;
        View r12 = getChildAt(r11);
        int r13 = getPosition(r12);
        int r14 = this.mOrientationHelper.getDecoratedStart(r12);
        int r15 = this.mOrientationHelper.getDecoratedEnd(r12);
        if (r13 < 0) goto L35;
        if (r13 >= r5) goto L35;
        if (((RecyclerView.LayoutParams) r12.getLayoutParams()).isItemRemoved() == false) goto L14;
        if (r8 != null) goto L35;
        r8 = r12;
        goto L35
    L14:
        boolean r16 = false;
        if (r15 > r6) goto L18;
        if (r14 >= r6) goto L18;
        boolean r17 = true;
    L19:
        if (r14 < r7) goto L22;
        if (r15 <= r7) goto L22;
        r16 = true;
    L22:
        if (r17 == true) goto L26;
        if (r16 == true) goto L26;
        return r12;
    L26:
        if (r21 == false) goto L31;
        if (r16 == false) goto L29;
        r9 = r12;
        goto L35
    L29:
        if (r10 != null) goto L35;
        r10 = r12;
        goto L35
    L31:
        if (r17 == false) goto L33;
        r9 = r12;
        goto L35
    L33:
        if (r10 != null) goto L35;
        r10 = r12;
    L18:
        r17 = false;
    L35:
        r11 = r11 + r3;
        goto L6
    L36:
        if (r10 != null) goto L54;
        if (r9 == null) goto L41;
        return r9;
    L41:
        return r8;
    L54:
        return r10;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public View findViewByPosition(int r6) {
        int r0 = getChildCount();
        if (r0 != 0) goto L6;
        return null;
    L6:
        int r2 = r6 - getPosition(getChildAt(0));
        if (r2 < 0) goto L13;
        if (r2 >= r0) goto L13;
        View r3 = getChildAt(r2);
        if (getPosition(r3) != r6) goto L13;
        return r3;
    L13:
        return super.findViewByPosition(r6);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-2, -2);
    }

    @Deprecated
    protected int getExtraLayoutSpace(RecyclerView.State r2) {
        if (r2.hasTargetScrollPosition() == true) goto L5;
        return 0;
    L5:
        return this.mOrientationHelper.getTotalSpace();
    }

    public int getInitialPrefetchItemCount() {
        return this.mInitialPrefetchItemCount;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public boolean getRecycleChildrenOnDetach() {
        return this.mRecycleChildrenOnDetach;
    }

    public boolean getReverseLayout() {
        return this.mReverseLayout;
    }

    public boolean getStackFromEnd() {
        return this.mStackFromEnd;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    protected boolean isLayoutRTL() {
        if (getLayoutDirection() == 1) goto L7;
        return false;
    L7:
        return true;
    }

    public boolean isSmoothScrollbarEnabled() {
        return this.mSmoothScrollbarEnabled;
    }

    void layoutChunk(RecyclerView.Recycler r9, RecyclerView.State r10, LayoutState r11, LayoutChunkResult r12) {
        View r1 = r11.next(r9);
        if (r1 != null) goto L6;
        r12.mFinished = true;
        return;
    L6:
        RecyclerView.LayoutParams r7 = (RecyclerView.LayoutParams) r1.getLayoutParams();
        if (r11.mScrapList != null) goto L15;
        boolean r0 = this.mShouldReverseLayout;
        if (r11.mLayoutDirection != (-1)) goto L11;
        boolean r4 = true;
    L12:
        if (r0 != r4) goto L14;
        addView(r1);
    L22:
        measureChildWithMargins(r1, 0, 0);
        r12.mConsumed = this.mOrientationHelper.getDecoratedMeasurement(r1);
        if (this.mOrientation == 1) goto L25;
        int r5 = getPaddingTop();
        int r2 = this.mOrientationHelper.getDecoratedMeasurementInOther(r1) + r5;
        if (r11.mLayoutDirection != (-1)) goto L35;
        int r6 = r11.mOffset;
        int r3 = r11.mOffset - r12.mConsumed;
        int r8 = r5;
        int r13 = r2;
    L36:
        layoutDecoratedWithMargins(r1, r3, r8, r6, r13);
        if (r7.isItemRemoved() == false) goto L39;
    L40:
        r12.mIgnoreConsumed = true;
    L41:
        r12.mFocusable = r1.hasFocusable();
        return;
    L39:
        if (r7.isItemChanged() == false) goto L41;
    L35:
        int r14 = r11.mOffset;
        r6 = r11.mOffset + r12.mConsumed;
        r3 = r14;
        r8 = r5;
        r13 = r2;
        goto L36
    L25:
        if (isLayoutRTL() == false) goto L27;
        int r15 = getWidth() - getPaddingRight();
        int r16 = r15 - this.mOrientationHelper.getDecoratedMeasurementInOther(r1);
    L29:
        if (r11.mLayoutDirection != (-1)) goto L31;
        int r17 = r11.mOffset;
        r13 = r17;
        r3 = r16;
        r8 = r11.mOffset - r12.mConsumed;
        r6 = r15;
        goto L36
    L31:
        int r18 = r11.mOffset;
        r13 = r11.mOffset + r12.mConsumed;
        r3 = r16;
        r8 = r18;
        r6 = r15;
        goto L36
    L27:
        r16 = getPaddingLeft();
        r15 = this.mOrientationHelper.getDecoratedMeasurementInOther(r1) + r16;
        goto L29
    L14:
        addView(r1, 0);
        goto L22
    L11:
        r4 = false;
        goto L12
    L15:
        boolean r19 = this.mShouldReverseLayout;
        if (r11.mLayoutDirection != (-1)) goto L18;
        boolean r20 = true;
    L19:
        if (r19 != r20) goto L21;
        addDisappearingView(r1);
        goto L22
    L21:
        addDisappearingView(r1, 0);
        goto L22
    L18:
        r20 = false;
        goto L19
    }

    void onAnchorReady(RecyclerView.Recycler r1, RecyclerView.State r2, AnchorInfo r3, int r4) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onDetachedFromWindow(RecyclerView r2, RecyclerView.Recycler r3) {
        super.onDetachedFromWindow(r2, r3);
        if (this.mRecycleChildrenOnDetach == false) goto L6;
        removeAndRecycleAllViews(r3);
        r3.clear();
        return;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public View onFocusSearchFailed(View r7, int r8, RecyclerView.Recycler r9, RecyclerView.State r10) {
        resolveShouldLayoutReverse();
        if (getChildCount() != 0) goto L5;
        return null;
    L5:
        int r0 = convertFocusDirectionToLayoutDirection(r8);
        if (r0 != Integer.MIN_VALUE) goto L8;
        return null;
    L8:
        ensureLayoutState();
        updateLayoutState(r0, (int) (this.mOrientationHelper.getTotalSpace() * MAX_SCROLL_FACTOR), false, r10);
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
        this.mLayoutState.mRecycle = false;
        fill(r9, this.mLayoutState, r10, true);
        if (r0 != (-1)) goto L11;
        View r4 = findPartiallyOrCompletelyInvisibleChildClosestToStart();
    L12:
        if (r0 != (-1)) goto L14;
        View r2 = getChildClosestToStart();
    L16:
        if (r2.hasFocusable() == false) goto L20;
        if (r4 != null) goto L19;
        return null;
    L19:
        return r2;
    L20:
        return r4;
    L14:
        r2 = getChildClosestToEnd();
        goto L16
    L11:
        r4 = findPartiallyOrCompletelyInvisibleChildClosestToEnd();
        goto L12
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onInitializeAccessibilityEvent(AccessibilityEvent r2) {
        super.onInitializeAccessibilityEvent(r2);
        if (getChildCount() <= 0) goto L6;
        r2.setFromIndex(findFirstVisibleItemPosition());
        r2.setToIndex(findLastVisibleItemPosition());
        return;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(RecyclerView.Recycler r12, RecyclerView.State r13) {
        int r1 = -1;
        if (this.mPendingSavedState != null) goto L7;
        if (this.mPendingScrollPosition != (-1)) goto L7;
    L11:
        if (this.mPendingSavedState != null) goto L13;
    L15:
        ensureLayoutState();
        this.mLayoutState.mRecycle = false;
        resolveShouldLayoutReverse();
        View r0 = getFocusedChild();
        if (this.mAnchorInfo.mValid == true) goto L18;
    L28:
        this.mAnchorInfo.reset();
        this.mAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayout ^ this.mStackFromEnd;
        updateAnchorInfoForLayout(r12, r13, this.mAnchorInfo);
        this.mAnchorInfo.mValid = true;
    L29:
        LayoutState r3 = this.mLayoutState;
        if (this.mLayoutState.mLastScrollDelta < 0) goto L32;
        int r5 = 1;
    L33:
        r3.mLayoutDirection = r5;
        this.mReusableIntPair[0] = 0;
        this.mReusableIntPair[1] = 0;
        calculateExtraLayoutSpace(r13, this.mReusableIntPair);
        int r4 = Math.max(0, this.mReusableIntPair[0]) + this.mOrientationHelper.getStartAfterPadding();
        int r6 = Math.max(0, this.mReusableIntPair[1]) + this.mOrientationHelper.getEndPadding();
        if (r13.isPreLayout() == false) goto L49;
        if (this.mPendingScrollPosition == (-1)) goto L49;
        if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) goto L49;
        View r7 = findViewByPosition(this.mPendingScrollPosition);
        if (r7 == null) goto L49;
        if (this.mShouldReverseLayout == false) goto L44;
        int r8 = (this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(r7)) - this.mPendingScrollPositionOffset;
    L45:
        if (r8 <= 0) goto L47;
        r4 = r4 + r8;
        goto L49
    L47:
        r6 = r6 - r8;
        goto L49
    L44:
        r8 = this.mPendingScrollPositionOffset - (this.mOrientationHelper.getDecoratedStart(r7) - this.mOrientationHelper.getStartAfterPadding());
    L49:
        if (this.mAnchorInfo.mLayoutFromEnd == false) goto L56;
        if (this.mShouldReverseLayout == false) goto L60;
        r1 = 1;
    L60:
        onAnchorReady(r12, r13, this.mAnchorInfo, r1);
        detachAndScrapAttachedViews(r12);
        this.mLayoutState.mInfinite = resolveIsInfinite();
        this.mLayoutState.mIsPreLayout = r13.isPreLayout();
        this.mLayoutState.mNoRecycleSpace = 0;
        if (this.mAnchorInfo.mLayoutFromEnd == false) goto L69;
        updateLayoutStateToFillStart(this.mAnchorInfo);
        this.mLayoutState.mExtraFillSpace = r4;
        fill(r12, this.mLayoutState, r13, false);
        int r9 = this.mLayoutState.mOffset;
        int r10 = this.mLayoutState.mCurrentPosition;
        if (this.mLayoutState.mAvailable <= 0) goto L65;
        r6 = r6 + this.mLayoutState.mAvailable;
    L65:
        updateLayoutStateToFillEnd(this.mAnchorInfo);
        this.mLayoutState.mExtraFillSpace = r6;
        this.mLayoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
        fill(r12, this.mLayoutState, r13, false);
        int r11 = this.mLayoutState.mOffset;
        if (this.mLayoutState.mAvailable <= 0) goto L77;
        int r14 = this.mLayoutState.mAvailable;
        updateLayoutStateToFillStart(r10, r9);
        this.mLayoutState.mExtraFillSpace = r14;
        fill(r12, this.mLayoutState, r13, false);
        r9 = this.mLayoutState.mOffset;
    L77:
        if (getChildCount() > 0) goto L79;
    L82:
        layoutForPredictiveAnimations(r12, r13, r9, r11);
        if (r13.isPreLayout() == true) goto L85;
        this.mOrientationHelper.onLayoutComplete();
    L86:
        this.mLastStackFromEnd = this.mStackFromEnd;
        return;
    L85:
        this.mAnchorInfo.reset();
        goto L86
    L79:
        if ((this.mShouldReverseLayout ^ this.mStackFromEnd) == false) goto L81;
        int r15 = fixLayoutEndGap(r11, r12, r13, true);
        int r16 = r9 + r15;
        int r2 = fixLayoutStartGap(r16, r12, r13, false);
        r9 = r16 + r2;
        r11 = (r11 + r15) + r2;
        goto L82
    L81:
        int r17 = fixLayoutStartGap(r9, r12, r13, true);
        int r18 = r11 + r17;
        int r19 = fixLayoutEndGap(r18, r12, r13, false);
        r9 = (r9 + r17) + r19;
        r11 = r18 + r19;
        goto L82
    L69:
        updateLayoutStateToFillEnd(this.mAnchorInfo);
        this.mLayoutState.mExtraFillSpace = r6;
        fill(r12, this.mLayoutState, r13, false);
        r11 = this.mLayoutState.mOffset;
        int r20 = this.mLayoutState.mCurrentPosition;
        if (this.mLayoutState.mAvailable <= 0) goto L72;
        r4 = r4 + this.mLayoutState.mAvailable;
    L72:
        updateLayoutStateToFillStart(this.mAnchorInfo);
        this.mLayoutState.mExtraFillSpace = r4;
        this.mLayoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
        fill(r12, this.mLayoutState, r13, false);
        int r21 = this.mLayoutState.mOffset;
        if (this.mLayoutState.mAvailable <= 0) goto L75;
        int r22 = this.mLayoutState.mAvailable;
        updateLayoutStateToFillEnd(r20, r11);
        this.mLayoutState.mExtraFillSpace = r22;
        fill(r12, this.mLayoutState, r13, false);
        r11 = this.mLayoutState.mOffset;
        r9 = r21;
        goto L77
    L75:
        r9 = r21;
        goto L77
    L56:
        if (this.mShouldReverseLayout == true) goto L60;
        r1 = 1;
        goto L60
    L32:
        r5 = -1;
        goto L33
    L18:
        if (this.mPendingScrollPosition != (-1)) goto L28;
        if (this.mPendingSavedState != null) goto L28;
        if (r0 == null) goto L29;
        if (this.mOrientationHelper.getDecoratedStart(r0) < this.mOrientationHelper.getEndAfterPadding()) goto L26;
    L27:
        this.mAnchorInfo.assignFromViewAndKeepVisibleRect(r0, getPosition(r0));
        goto L29
    L26:
        if (this.mOrientationHelper.getDecoratedEnd(r0) > this.mOrientationHelper.getStartAfterPadding()) goto L29;
    L13:
        if (this.mPendingSavedState.hasValidAnchor() == false) goto L15;
        this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
    L7:
        if (r13.getItemCount() != 0) goto L11;
        removeAndRecycleAllViews(r12);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutCompleted(RecyclerView.State r2) {
        super.onLayoutCompleted(r2);
        this.mPendingSavedState = null;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mAnchorInfo.reset();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onRestoreInstanceState(Parcelable r3) {
        if ((r3 instanceof SavedState) == false) goto L9;
        this.mPendingSavedState = (SavedState) r3;
        if (this.mPendingScrollPosition == (-1)) goto L7;
        this.mPendingSavedState.invalidateAnchor();
    L7:
        requestLayout();
        return;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public Parcelable onSaveInstanceState() {
        if (this.mPendingSavedState != null) goto L5;
        SavedState r0 = new SavedState();
        if (getChildCount() <= 0) goto L13;
        ensureLayoutState();
        boolean r1 = this.mLastStackFromEnd ^ this.mShouldReverseLayout;
        r0.mAnchorLayoutFromEnd = r1;
        if (r1 == false) goto L11;
        View r2 = getChildClosestToEnd();
        r0.mAnchorOffset = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(r2);
        r0.mAnchorPosition = getPosition(r2);
    L14:
        return r0;
    L11:
        View r3 = getChildClosestToStart();
        r0.mAnchorPosition = getPosition(r3);
        r0.mAnchorOffset = this.mOrientationHelper.getDecoratedStart(r3) - this.mOrientationHelper.getStartAfterPadding();
        goto L14
    L13:
        r0.invalidateAnchor();
        goto L14
    L5:
        return new SavedState(this.mPendingSavedState);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.ViewDropHandler
    public void prepareForDrop(View r7, View r8, int r9, int r10) {
        assertNotInLayoutOrScroll("Cannot drop a view during a scroll or layout calculation");
        ensureLayoutState();
        resolveShouldLayoutReverse();
        int r0 = getPosition(r7);
        int r1 = getPosition(r8);
        if (r0 >= r1) goto L5;
        byte r4 = 1;
    L7:
        if (this.mShouldReverseLayout == false) goto L11;
        if (r4 != 1) goto L10;
        scrollToPositionWithOffset(r1, this.mOrientationHelper.getEndAfterPadding() - (this.mOrientationHelper.getDecoratedStart(r8) + this.mOrientationHelper.getDecoratedMeasurement(r7)));
        return;
    L10:
        scrollToPositionWithOffset(r1, this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(r8));
        return;
    L11:
        if (r4 != (-1)) goto L13;
        scrollToPositionWithOffset(r1, this.mOrientationHelper.getDecoratedStart(r8));
        return;
    L13:
        scrollToPositionWithOffset(r1, this.mOrientationHelper.getDecoratedEnd(r8) - this.mOrientationHelper.getDecoratedMeasurement(r7));
        return;
    L5:
        r4 = -1;
        goto L7
    }

    boolean resolveIsInfinite() {
        if (this.mOrientationHelper.getMode() == 0) goto L5;
    L7:
        return false;
    L5:
        if (this.mOrientationHelper.getEnd() != 0) goto L7;
        return true;
    }

    int scrollBy(int r7, RecyclerView.Recycler r8, RecyclerView.State r9) {
        if (getChildCount() == 0) goto L18;
        if (r7 == 0) goto L18;
        ensureLayoutState();
        this.mLayoutState.mRecycle = true;
        if (r7 <= 0) goto L9;
        int r0 = 1;
    L10:
        int r3 = Math.abs(r7);
        updateLayoutState(r0, r3, true, r9);
        int r2 = this.mLayoutState.mScrollingOffset + fill(r8, this.mLayoutState, r9, false);
        if (r2 >= 0) goto L13;
        return 0;
    L13:
        if (r3 <= r2) goto L15;
        int r1 = r0 * r2;
    L16:
        this.mOrientationHelper.offsetChildren(-r1);
        this.mLayoutState.mLastScrollDelta = r1;
        return r1;
    L15:
        r1 = r7;
        goto L16
    L9:
        r0 = -1;
    L18:
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollHorizontallyBy(int r3, RecyclerView.Recycler r4, RecyclerView.State r5) {
        if (this.mOrientation != 1) goto L7;
        return 0;
    L7:
        return scrollBy(r3, r4, r5);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void scrollToPosition(int r2) {
        this.mPendingScrollPosition = r2;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        if (this.mPendingSavedState == null) goto L5;
        this.mPendingSavedState.invalidateAnchor();
    L5:
        requestLayout();
    }

    public void scrollToPositionWithOffset(int r2, int r3) {
        this.mPendingScrollPosition = r2;
        this.mPendingScrollPositionOffset = r3;
        if (this.mPendingSavedState == null) goto L5;
        this.mPendingSavedState.invalidateAnchor();
    L5:
        requestLayout();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollVerticallyBy(int r2, RecyclerView.Recycler r3, RecyclerView.State r4) {
        if (this.mOrientation != 0) goto L7;
        return 0;
    L7:
        return scrollBy(r2, r3, r4);
    }

    public void setInitialPrefetchItemCount(int r1) {
        this.mInitialPrefetchItemCount = r1;
    }

    public void setOrientation(int r4) {
        if (r4 != 0) goto L4;
    L8:
        assertNotInLayoutOrScroll(null);
        if (r4 == this.mOrientation) goto L11;
    L13:
        this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, r4);
        this.mAnchorInfo.mOrientationHelper = this.mOrientationHelper;
        this.mOrientation = r4;
        requestLayout();
        return;
    L11:
        if (this.mOrientationHelper == null) goto L13;
        return;
    L4:
        if (r4 == 1) goto L8;
        throw new IllegalArgumentException("invalid orientation:" + r4);
    }

    public void setRecycleChildrenOnDetach(boolean r1) {
        this.mRecycleChildrenOnDetach = r1;
    }

    public void setReverseLayout(boolean r2) {
        assertNotInLayoutOrScroll(null);
        if (r2 != this.mReverseLayout) goto L5;
        return;
    L5:
        this.mReverseLayout = r2;
        requestLayout();
    }

    public void setSmoothScrollbarEnabled(boolean r1) {
        this.mSmoothScrollbarEnabled = r1;
    }

    public void setStackFromEnd(boolean r2) {
        assertNotInLayoutOrScroll(null);
        if (this.mStackFromEnd != r2) goto L5;
        return;
    L5:
        this.mStackFromEnd = r2;
        requestLayout();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    boolean shouldMeasureTwice() {
        if (getHeightMode() != 1073741824) goto L5;
    L9:
        return false;
    L5:
        if (getWidthMode() == 1073741824) goto L9;
        if (hasFlexibleChildInBothOrientations() == false) goto L9;
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void smoothScrollToPosition(RecyclerView r3, RecyclerView.State r4, int r5) {
        LinearSmoothScroller r0 = new LinearSmoothScroller(r3.getContext());
        r0.setTargetPosition(r5);
        startSmoothScroll(r0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean supportsPredictiveItemAnimations() {
        if (this.mPendingSavedState == null) goto L5;
    L7:
        return false;
    L5:
        if (this.mLastStackFromEnd != this.mStackFromEnd) goto L7;
        return true;
    }

    void validateChildOrder() {
        Log.d(TAG, "validating child count " + getChildCount());
        boolean r1 = true;
        if (getChildCount() >= 1) goto L5;
        return;
    L5:
        int r2 = getPosition(getChildAt(0));
        int r3 = this.mOrientationHelper.getDecoratedStart(getChildAt(0));
        if (this.mShouldReverseLayout == false) goto L23;
        int r4 = 1;
    L9:
        if (r4 >= getChildCount()) goto L38;
        View r7 = getChildAt(r4);
        int r8 = getPosition(r7);
        int r9 = this.mOrientationHelper.getDecoratedStart(r7);
        if (r8 < r2) goto L12;
        if (r9 > r3) goto L20;
        r4 = r4 + 1;
        goto L9
    L20:
        logChildren();
        throw new RuntimeException("detected invalid location");
    L12:
        logChildren();
        StringBuilder r6 = new StringBuilder().append("detected invalid position. loc invalid? ");
        if (r9 < r3) goto L17;
        r1 = false;
    L17:
        throw new RuntimeException(r6.append(r1).toString());
    L38:
        return;
    L23:
        int r5 = 1;
    L25:
        if (r5 >= getChildCount()) goto L45;
        View r10 = getChildAt(r5);
        int r11 = getPosition(r10);
        int r12 = this.mOrientationHelper.getDecoratedStart(r10);
        if (r11 < r2) goto L28;
        if (r12 < r3) goto L36;
        r5 = r5 + 1;
        goto L25
    L36:
        logChildren();
        throw new RuntimeException("detected invalid location");
    L28:
        logChildren();
        StringBuilder r13 = new StringBuilder().append("detected invalid position. loc invalid? ");
        if (r12 < r3) goto L33;
        r1 = false;
    L33:
        throw new RuntimeException(r13.append(r1).toString());
    }
}
