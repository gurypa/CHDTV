package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class StaggeredGridLayoutManager extends RecyclerView.LayoutManager implements RecyclerView.SmoothScroller.ScrollVectorProvider {
    static final boolean DEBUG = false;

    @Deprecated
    public static final int GAP_HANDLING_LAZY = 1;
    public static final int GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS = 2;
    public static final int GAP_HANDLING_NONE = 0;
    public static final int HORIZONTAL = 0;
    static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private static final float MAX_SCROLL_FACTOR = 0.33333334f;
    private static final String TAG = "StaggeredGridLManager";
    public static final int VERTICAL = 1;
    private final AnchorInfo mAnchorInfo;
    private final Runnable mCheckForGapsRunnable;
    private int mFullSizeSpec;
    private int mGapStrategy;
    private boolean mLaidOutInvalidFullSpan;
    private boolean mLastLayoutFromEnd;
    private boolean mLastLayoutRTL;
    private final LayoutState mLayoutState;
    LazySpanLookup mLazySpanLookup;
    private int mOrientation;
    private SavedState mPendingSavedState;
    int mPendingScrollPosition;
    int mPendingScrollPositionOffset;
    private int[] mPrefetchDistances;
    OrientationHelper mPrimaryOrientation;
    private BitSet mRemainingSpans;
    boolean mReverseLayout;
    OrientationHelper mSecondaryOrientation;
    boolean mShouldReverseLayout;
    private int mSizePerSpan;
    private boolean mSmoothScrollbarEnabled;
    private int mSpanCount;
    Span[] mSpans;
    private final Rect mTmpRect;

    class AnchorInfo {
        boolean mInvalidateOffsets;
        boolean mLayoutFromEnd;
        int mOffset;
        int mPosition;
        int[] mSpanReferenceLines;
        boolean mValid;
        final /* synthetic */ StaggeredGridLayoutManager this$0;

        AnchorInfo(StaggeredGridLayoutManager r1) {
            this.this$0 = r1;
            reset();
        }

        void assignCoordinateFromPadding() {
            if (this.mLayoutFromEnd == false) goto L5;
            int r0 = this.this$0.mPrimaryOrientation.getEndAfterPadding();
        L6:
            this.mOffset = r0;
            return;
        L5:
            r0 = this.this$0.mPrimaryOrientation.getStartAfterPadding();
            goto L6
        }

        void assignCoordinateFromPadding(int r2) {
            if (this.mLayoutFromEnd == false) goto L5;
            this.mOffset = this.this$0.mPrimaryOrientation.getEndAfterPadding() - r2;
            return;
        L5:
            this.mOffset = this.this$0.mPrimaryOrientation.getStartAfterPadding() + r2;
        }

        void reset() {
            this.mPosition = -1;
            this.mOffset = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
            this.mInvalidateOffsets = false;
            this.mValid = false;
            if (this.mSpanReferenceLines == null) goto L6;
            Arrays.fill(this.mSpanReferenceLines, -1);
            return;
        }

        void saveSpanReferenceLines(Span[] r6) {
            int r0 = r6.length;
            if (this.mSpanReferenceLines != null) goto L5;
        L6:
            this.mSpanReferenceLines = new int[this.this$0.mSpans.length];
        L7:
            int r1 = 0;
        L8:
            if (r1 >= r0) goto L10;
            this.mSpanReferenceLines[r1] = r6[r1].getStartLine(Integer.MIN_VALUE);
            r1 = r1 + 1;
            goto L8
        L10:
            return;
        L5:
            if (this.mSpanReferenceLines.length >= r0) goto L7;
            goto L6
        }
    }

    public static class LayoutParams extends RecyclerView.LayoutParams {
        public static final int INVALID_SPAN_ID = -1;
        boolean mFullSpan;
        Span mSpan;

        public LayoutParams(int r1, int r2) {
            super(r1, r2);
        }

        public LayoutParams(Context r1, AttributeSet r2) {
            super(r1, r2);
        }

        public LayoutParams(ViewGroup.LayoutParams r1) {
            super(r1);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams r1) {
            super(r1);
        }

        public LayoutParams(RecyclerView.LayoutParams r1) {
            super(r1);
        }

        public final int getSpanIndex() {
            if (this.mSpan != null) goto L7;
            return -1;
        L7:
            return this.mSpan.mIndex;
        }

        public boolean isFullSpan() {
            return this.mFullSpan;
        }

        public void setFullSpan(boolean r1) {
            this.mFullSpan = r1;
        }
    }

    static class LazySpanLookup {
        private static final int MIN_SIZE = 10;
        int[] mData;
        List<FullSpanItem> mFullSpanItems;

        static class FullSpanItem implements Parcelable {
            public static final Parcelable.Creator<FullSpanItem> CREATOR = null;
            int mGapDir;
            int[] mGapPerSpan;
            boolean mHasUnwantedGapAfter;
            int mPosition;

            static {
                CREATOR = new C05191();
            }

            FullSpanItem() {
            }

            FullSpanItem(Parcel r3) {
                this.mPosition = r3.readInt();
                this.mGapDir = r3.readInt();
                boolean r1 = true;
                if (r3.readInt() == 1) goto L6;
                r1 = false;
            L6:
                this.mHasUnwantedGapAfter = r1;
                int r0 = r3.readInt();
                if (r0 <= 0) goto L10;
                this.mGapPerSpan = new int[r0];
                r3.readIntArray(this.mGapPerSpan);
                return;
            }

            @Override // android.os.Parcelable
            public int describeContents() {
                return 0;
            }

            int getGapForSpan(int r2) {
                if (this.mGapPerSpan != null) goto L6;
                return 0;
            L6:
                return this.mGapPerSpan[r2];
            }

            public String toString() {
                return "FullSpanItem{mPosition=" + this.mPosition + ", mGapDir=" + this.mGapDir + ", mHasUnwantedGapAfter=" + this.mHasUnwantedGapAfter + ", mGapPerSpan=" + Arrays.toString(this.mGapPerSpan) + '}';
            }

            @Override // android.os.Parcelable
            public void writeToParcel(Parcel r2, int r3) {
                r2.writeInt(this.mPosition);
                r2.writeInt(this.mGapDir);
                r2.writeInt(this.mHasUnwantedGapAfter ? 1 : 0);
                if (this.mGapPerSpan != null) goto L5;
            L7:
                r2.writeInt(0);
                return;
            L5:
                if (this.mGapPerSpan.length <= 0) goto L7;
                r2.writeInt(this.mGapPerSpan.length);
                r2.writeIntArray(this.mGapPerSpan);
            }
        }

        LazySpanLookup() {
        }

        private int invalidateFullSpansAfter(int r8) {
            if (this.mFullSpanItems != null) goto L5;
            return -1;
        L5:
            FullSpanItem r0 = getFullSpanItem(r8);
            if (r0 == null) goto L8;
            this.mFullSpanItems.remove(r0);
        L8:
            int r2 = -1;
            int r3 = this.mFullSpanItems.size();
            int r4 = 0;
        L9:
            if (r4 >= r3) goto L14;
            if (this.mFullSpanItems.get(r4).mPosition >= r8) goto L12;
            r4 = r4 + 1;
            goto L9
        L12:
            r2 = r4;
        L14:
            if (r2 == (-1)) goto L17;
            FullSpanItem r1 = this.mFullSpanItems.get(r2);
            this.mFullSpanItems.remove(r2);
            return r1.mPosition;
        L17:
            return -1;
        }

        private void offsetFullSpansForAddition(int r4, int r5) {
            if (this.mFullSpanItems != null) goto L5;
            return;
        L5:
            int r0 = this.mFullSpanItems.size() - 1;
        L6:
            if (r0 < 0) goto L12;
            FullSpanItem r1 = this.mFullSpanItems.get(r0);
            if (r1.mPosition < r4) goto L11;
            r1.mPosition += r5;
        L11:
            r0 = r0 - 1;
            goto L6
        }

        private void offsetFullSpansForRemoval(int r5, int r6) {
            if (this.mFullSpanItems != null) goto L5;
            return;
        L5:
            int r0 = r5 + r6;
            int r1 = this.mFullSpanItems.size() - 1;
        L6:
            if (r1 < 0) goto L15;
            FullSpanItem r2 = this.mFullSpanItems.get(r1);
            if (r2.mPosition < r5) goto L14;
            if (r2.mPosition >= r0) goto L13;
            this.mFullSpanItems.remove(r1);
            goto L14
        L13:
            r2.mPosition -= r6;
        L14:
            r1 = r1 - 1;
            goto L6
        }

        public void addFullSpanItem(FullSpanItem r6) {
            if (this.mFullSpanItems != null) goto L5;
            this.mFullSpanItems = new ArrayList();
        L5:
            int r0 = this.mFullSpanItems.size();
            int r1 = 0;
        L6:
            if (r1 >= r0) goto L15;
            FullSpanItem r2 = this.mFullSpanItems.get(r1);
            if (r2.mPosition != r6.mPosition) goto L11;
            this.mFullSpanItems.remove(r1);
        L11:
            if (r2.mPosition >= r6.mPosition) goto L12;
            r1 = r1 + 1;
            goto L6
        L12:
            this.mFullSpanItems.add(r1, r6);
            return;
        L15:
            this.mFullSpanItems.add(r6);
        }

        void clear() {
            if (this.mData == null) goto L5;
            Arrays.fill(this.mData, -1);
        L5:
            this.mFullSpanItems = null;
        }

        void ensureSize(int r6) {
            if (this.mData != null) goto L6;
            this.mData = new int[Math.max(r6, 10) + 1];
            Arrays.fill(this.mData, -1);
            return;
        L6:
            if (r6 < this.mData.length) goto L10;
            int[] r0 = this.mData;
            this.mData = new int[sizeForPosition(r6)];
            System.arraycopy(r0, 0, this.mData, 0, r0.length);
            Arrays.fill(this.mData, r0.length, this.mData.length, -1);
            return;
        }

        int forceInvalidateAfter(int r4) {
            if (this.mFullSpanItems == null) goto L11;
            int r0 = this.mFullSpanItems.size() - 1;
        L5:
            if (r0 < 0) goto L11;
            if (this.mFullSpanItems.get(r0).mPosition < r4) goto L9;
            this.mFullSpanItems.remove(r0);
        L9:
            r0 = r0 - 1;
        L11:
            return invalidateAfter(r4);
        }

        public FullSpanItem getFirstFullSpanItemInRange(int r6, int r7, int r8, boolean r9) {
            if (this.mFullSpanItems != null) goto L5;
            return null;
        L5:
            int r0 = this.mFullSpanItems.size();
            int r2 = 0;
        L6:
            if (r2 >= r0) goto L20;
            FullSpanItem r3 = this.mFullSpanItems.get(r2);
            if (r3.mPosition >= r7) goto L9;
            if (r3.mPosition < r6) goto L19;
            if (r8 == 0) goto L18;
            if (r3.mGapDir == r8) goto L18;
            if (r9 == false) goto L19;
            if (r3.mHasUnwantedGapAfter == false) goto L19;
        L18:
            return r3;
        L19:
            r2 = r2 + 1;
            goto L6
        L9:
            return null;
        L20:
            return null;
        }

        public FullSpanItem getFullSpanItem(int r5) {
            if (this.mFullSpanItems != null) goto L5;
            return null;
        L5:
            int r0 = this.mFullSpanItems.size() - 1;
        L6:
            if (r0 < 0) goto L11;
            FullSpanItem r2 = this.mFullSpanItems.get(r0);
            if (r2.mPosition == r5) goto L9;
            r0 = r0 - 1;
            goto L6
        L9:
            return r2;
        L11:
            return null;
        }

        int getSpan(int r2) {
            if (this.mData != null) goto L5;
            return -1;
        L5:
            if (r2 < this.mData.length) goto L8;
            return -1;
        L8:
            return this.mData[r2];
        }

        int invalidateAfter(int r5) {
            if (this.mData != null) goto L6;
            return -1;
        L6:
            if (r5 < this.mData.length) goto L8;
            return -1;
        L8:
            int r0 = invalidateFullSpansAfter(r5);
            if (r0 != (-1)) goto L12;
            Arrays.fill(this.mData, r5, this.mData.length, -1);
            return this.mData.length;
        L12:
            int r2 = Math.min(r0 + 1, this.mData.length);
            Arrays.fill(this.mData, r5, r2, -1);
            return r2;
        }

        void offsetForAddition(int r5, int r6) {
            if (this.mData != null) goto L5;
            return;
        L5:
            if (r5 >= this.mData.length) goto L10;
            ensureSize(r5 + r6);
            System.arraycopy(this.mData, r5, this.mData, r5 + r6, (this.mData.length - r5) - r6);
            Arrays.fill(this.mData, r5, r5 + r6, -1);
            offsetFullSpansForAddition(r5, r6);
            return;
        }

        void offsetForRemoval(int r5, int r6) {
            if (this.mData != null) goto L5;
            return;
        L5:
            if (r5 >= this.mData.length) goto L10;
            ensureSize(r5 + r6);
            System.arraycopy(this.mData, r5 + r6, this.mData, r5, (this.mData.length - r5) - r6);
            Arrays.fill(this.mData, this.mData.length - r6, this.mData.length, -1);
            offsetFullSpansForRemoval(r5, r6);
            return;
        }

        void setSpan(int r3, Span r4) {
            ensureSize(r3);
            this.mData[r3] = r4.mIndex;
        }

        int sizeForPosition(int r2) {
            int r0 = this.mData.length;
        L3:
            if (r0 > r2) goto L5;
            r0 = r0 * 2;
            goto L3
        L5:
            return r0;
        }
    }

    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = null;
        boolean mAnchorLayoutFromEnd;
        int mAnchorPosition;
        List<LazySpanLookup.FullSpanItem> mFullSpanItems;
        boolean mLastLayoutRTL;
        boolean mReverseLayout;
        int[] mSpanLookup;
        int mSpanLookupSize;
        int[] mSpanOffsets;
        int mSpanOffsetsSize;
        int mVisibleAnchorPosition;

        static {
            CREATOR = new C05201();
        }

        public SavedState() {
        }

        SavedState(Parcel r4) {
            this.mAnchorPosition = r4.readInt();
            this.mVisibleAnchorPosition = r4.readInt();
            this.mSpanOffsetsSize = r4.readInt();
            if (this.mSpanOffsetsSize <= 0) goto L5;
            this.mSpanOffsets = new int[this.mSpanOffsetsSize];
            r4.readIntArray(this.mSpanOffsets);
        L5:
            this.mSpanLookupSize = r4.readInt();
            if (this.mSpanLookupSize <= 0) goto L8;
            this.mSpanLookup = new int[this.mSpanLookupSize];
            r4.readIntArray(this.mSpanLookup);
        L8:
            boolean r1 = false;
            if (r4.readInt() != 1) goto L11;
            boolean r0 = true;
        L12:
            this.mReverseLayout = r0;
            if (r4.readInt() != 1) goto L15;
            boolean r2 = true;
        L16:
            this.mAnchorLayoutFromEnd = r2;
            if (r4.readInt() != 1) goto L19;
            r1 = true;
        L19:
            this.mLastLayoutRTL = r1;
            this.mFullSpanItems = r4.readArrayList(LazySpanLookup.FullSpanItem.class.getClassLoader());
            return;
        L15:
            r2 = false;
            goto L16
        L11:
            r0 = false;
            goto L12
        }

        public SavedState(SavedState r2) {
            this.mSpanOffsetsSize = r2.mSpanOffsetsSize;
            this.mAnchorPosition = r2.mAnchorPosition;
            this.mVisibleAnchorPosition = r2.mVisibleAnchorPosition;
            this.mSpanOffsets = r2.mSpanOffsets;
            this.mSpanLookupSize = r2.mSpanLookupSize;
            this.mSpanLookup = r2.mSpanLookup;
            this.mReverseLayout = r2.mReverseLayout;
            this.mAnchorLayoutFromEnd = r2.mAnchorLayoutFromEnd;
            this.mLastLayoutRTL = r2.mLastLayoutRTL;
            this.mFullSpanItems = r2.mFullSpanItems;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        void invalidateAnchorPositionInfo() {
            this.mSpanOffsets = null;
            this.mSpanOffsetsSize = 0;
            this.mAnchorPosition = -1;
            this.mVisibleAnchorPosition = -1;
        }

        void invalidateSpanInfo() {
            this.mSpanOffsets = null;
            this.mSpanOffsetsSize = 0;
            this.mSpanLookupSize = 0;
            this.mSpanLookup = null;
            this.mFullSpanItems = null;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel r2, int r3) {
            r2.writeInt(this.mAnchorPosition);
            r2.writeInt(this.mVisibleAnchorPosition);
            r2.writeInt(this.mSpanOffsetsSize);
            if (this.mSpanOffsetsSize <= 0) goto L5;
            r2.writeIntArray(this.mSpanOffsets);
        L5:
            r2.writeInt(this.mSpanLookupSize);
            if (this.mSpanLookupSize <= 0) goto L8;
            r2.writeIntArray(this.mSpanLookup);
        L8:
            r2.writeInt(this.mReverseLayout ? 1 : 0);
            r2.writeInt(this.mAnchorLayoutFromEnd ? 1 : 0);
            r2.writeInt(this.mLastLayoutRTL ? 1 : 0);
            r2.writeList(this.mFullSpanItems);
        }
    }

    class Span {
        static final int INVALID_LINE = Integer.MIN_VALUE;
        int mCachedEnd;
        int mCachedStart;
        int mDeletedSize;
        final int mIndex;
        ArrayList<View> mViews;
        final /* synthetic */ StaggeredGridLayoutManager this$0;

        Span(StaggeredGridLayoutManager r2, int r3) {
            this.this$0 = r2;
            this.mViews = new ArrayList();
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
            this.mDeletedSize = 0;
            this.mIndex = r3;
        }

        void appendToSpan(View r5) {
            LayoutParams r0 = getLayoutParams(r5);
            r0.mSpan = this;
            this.mViews.add(r5);
            this.mCachedEnd = Integer.MIN_VALUE;
            if (this.mViews.size() != 1) goto L6;
            this.mCachedStart = Integer.MIN_VALUE;
        L6:
            if (r0.isItemRemoved() == false) goto L8;
        L9:
            this.mDeletedSize += this.this$0.mPrimaryOrientation.getDecoratedMeasurement(r5);
            return;
        L8:
            if (r0.isItemChanged() == true) goto L9;
        }

        void cacheReferenceLineAndClear(boolean r4, int r5) {
            if (r4 == false) goto L5;
            int r1 = getEndLine(Integer.MIN_VALUE);
        L6:
            clear();
            if (r1 != Integer.MIN_VALUE) goto L9;
            return;
        L9:
            if (r4 == true) goto L11;
        L12:
            if (r4 == false) goto L14;
        L16:
            if (r5 == Integer.MIN_VALUE) goto L18;
            r1 = r1 + r5;
        L18:
            this.mCachedEnd = r1;
            this.mCachedStart = r1;
            return;
        L14:
            if (r1 <= this.this$0.mPrimaryOrientation.getStartAfterPadding()) goto L16;
            return;
        L11:
            if (r1 >= this.this$0.mPrimaryOrientation.getEndAfterPadding()) goto L12;
            return;
        L5:
            r1 = getStartLine(Integer.MIN_VALUE);
            goto L6
        }

        void calculateCachedEnd() {
            View r0 = this.mViews.get(this.mViews.size() - 1);
            LayoutParams r1 = getLayoutParams(r0);
            this.mCachedEnd = this.this$0.mPrimaryOrientation.getDecoratedEnd(r0);
            if (r1.mFullSpan == false) goto L10;
            LazySpanLookup.FullSpanItem r3 = this.this$0.mLazySpanLookup.getFullSpanItem(r1.getViewLayoutPosition());
            if (r3 != null) goto L7;
            return;
        L7:
            if (r3.mGapDir != 1) goto L12;
            this.mCachedEnd += r3.getGapForSpan(this.mIndex);
            return;
        L12:
            return;
        }

        void calculateCachedStart() {
            View r0 = this.mViews.get(0);
            LayoutParams r1 = getLayoutParams(r0);
            this.mCachedStart = this.this$0.mPrimaryOrientation.getDecoratedStart(r0);
            if (r1.mFullSpan == false) goto L10;
            LazySpanLookup.FullSpanItem r2 = this.this$0.mLazySpanLookup.getFullSpanItem(r1.getViewLayoutPosition());
            if (r2 != null) goto L7;
            return;
        L7:
            if (r2.mGapDir != (-1)) goto L12;
            this.mCachedStart -= r2.getGapForSpan(this.mIndex);
            return;
        L12:
            return;
        }

        void clear() {
            this.mViews.clear();
            invalidateCache();
            this.mDeletedSize = 0;
        }

        public int findFirstCompletelyVisibleItemPosition() {
            if (this.this$0.mReverseLayout == false) goto L6;
            return findOneVisibleChild(this.mViews.size() - 1, -1, true);
        L6:
            return findOneVisibleChild(0, this.mViews.size(), true);
        }

        public int findFirstPartiallyVisibleItemPosition() {
            if (this.this$0.mReverseLayout == false) goto L6;
            return findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true);
        L6:
            return findOnePartiallyVisibleChild(0, this.mViews.size(), true);
        }

        public int findFirstVisibleItemPosition() {
            if (this.this$0.mReverseLayout == false) goto L6;
            return findOneVisibleChild(this.mViews.size() - 1, -1, false);
        L6:
            return findOneVisibleChild(0, this.mViews.size(), false);
        }

        public int findLastCompletelyVisibleItemPosition() {
            if (this.this$0.mReverseLayout == false) goto L6;
            return findOneVisibleChild(0, this.mViews.size(), true);
        L6:
            return findOneVisibleChild(this.mViews.size() - 1, -1, true);
        }

        public int findLastPartiallyVisibleItemPosition() {
            if (this.this$0.mReverseLayout == false) goto L6;
            return findOnePartiallyVisibleChild(0, this.mViews.size(), true);
        L6:
            return findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true);
        }

        public int findLastVisibleItemPosition() {
            if (this.this$0.mReverseLayout == false) goto L6;
            return findOneVisibleChild(0, this.mViews.size(), false);
        L6:
            return findOneVisibleChild(this.mViews.size() - 1, -1, false);
        }

        int findOnePartiallyOrCompletelyVisibleChild(int r12, int r13, boolean r14, boolean r15, boolean r16) {
            int r0 = this.this$0.mPrimaryOrientation.getStartAfterPadding();
            int r1 = this.this$0.mPrimaryOrientation.getEndAfterPadding();
            if (r13 <= r12) goto L5;
            int r4 = 1;
        L6:
            int r5 = r12;
        L7:
            if (r5 == r13) goto L39;
            View r6 = this.mViews.get(r5);
            int r7 = this.this$0.mPrimaryOrientation.getDecoratedStart(r6);
            int r8 = this.this$0.mPrimaryOrientation.getDecoratedEnd(r6);
            boolean r9 = false;
            if (r16 == false) goto L13;
            if (r7 > r1) goto L12;
            boolean r10 = true;
        L17:
            if (r16 == false) goto L20;
            if (r8 < r0) goto L23;
            r9 = true;
        L23:
            if (r10 == false) goto L38;
            if (r9 == false) goto L38;
            if (r14 == false) goto L31;
            if (r15 == false) goto L31;
            if (r7 < r0) goto L38;
            if (r8 > r1) goto L38;
            return this.this$0.getPosition(r6);
        L31:
            if (r15 == true) goto L33;
            if (r7 < r0) goto L37;
            if (r8 <= r1) goto L38;
        L37:
            return this.this$0.getPosition(r6);
        L33:
            return this.this$0.getPosition(r6);
        L38:
            r5 = r5 + r4;
            goto L7
        L20:
            if (r8 <= r0) goto L23;
            r9 = true;
            goto L23
        L12:
            r10 = false;
            goto L17
        L13:
            if (r7 >= r1) goto L15;
            r10 = true;
            goto L17
        L15:
            r10 = false;
            goto L17
        L39:
            return -1;
        L5:
            r4 = -1;
            goto L6
        }

        int findOnePartiallyVisibleChild(int r7, int r8, boolean r9) {
            return findOnePartiallyOrCompletelyVisibleChild(r7, r8, false, false, r9);
        }

        int findOneVisibleChild(int r7, int r8, boolean r9) {
            return findOnePartiallyOrCompletelyVisibleChild(r7, r8, r9, true, false);
        }

        public int getDeletedSize() {
            return this.mDeletedSize;
        }

        int getEndLine() {
            if (this.mCachedEnd != Integer.MIN_VALUE) goto L5;
            calculateCachedEnd();
            return this.mCachedEnd;
        L5:
            return this.mCachedEnd;
        }

        int getEndLine(int r3) {
            if (this.mCachedEnd == Integer.MIN_VALUE) goto L7;
            return this.mCachedEnd;
        L7:
            if (this.mViews.size() != 0) goto L9;
            return r3;
        L9:
            calculateCachedEnd();
            return this.mCachedEnd;
        }

        public View getFocusableViewAfter(int r6, int r7) {
            View r0 = null;
            if (r7 != (-1)) goto L19;
            int r1 = this.mViews.size();
            int r2 = 0;
        L5:
            if (r2 >= r1) goto L33;
            View r3 = this.mViews.get(r2);
            if (this.this$0.mReverseLayout == false) goto L11;
            if (this.this$0.getPosition(r3) <= r6) goto L33;
        L11:
            if (this.this$0.mReverseLayout == true) goto L16;
            if (this.this$0.getPosition(r3) >= r6) goto L33;
        L16:
            if (r3.hasFocusable() == false) goto L33;
            r0 = r3;
            r2 = r2 + 1;
        L33:
            return r0;
        L19:
            int r4 = this.mViews.size() - 1;
        L20:
            if (r4 < 0) goto L33;
            View r5 = this.mViews.get(r4);
            if (this.this$0.mReverseLayout == false) goto L26;
            if (this.this$0.getPosition(r5) >= r6) goto L33;
        L26:
            if (this.this$0.mReverseLayout == true) goto L31;
            if (this.this$0.getPosition(r5) <= r6) goto L33;
        L31:
            if (r5.hasFocusable() == false) goto L33;
            r0 = r5;
            r4 = r4 - 1;
            goto L20
        }

        LayoutParams getLayoutParams(View r2) {
            return (LayoutParams) r2.getLayoutParams();
        }

        int getStartLine() {
            if (this.mCachedStart != Integer.MIN_VALUE) goto L5;
            calculateCachedStart();
            return this.mCachedStart;
        L5:
            return this.mCachedStart;
        }

        int getStartLine(int r3) {
            if (this.mCachedStart == Integer.MIN_VALUE) goto L7;
            return this.mCachedStart;
        L7:
            if (this.mViews.size() != 0) goto L9;
            return r3;
        L9:
            calculateCachedStart();
            return this.mCachedStart;
        }

        void invalidateCache() {
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
        }

        void onOffset(int r3) {
            if (this.mCachedStart == Integer.MIN_VALUE) goto L6;
            this.mCachedStart += r3;
        L6:
            if (this.mCachedEnd == Integer.MIN_VALUE) goto L9;
            this.mCachedEnd += r3;
            return;
        }

        void popEnd() {
            int r0 = this.mViews.size();
            View r1 = this.mViews.remove(r0 - 1);
            LayoutParams r2 = getLayoutParams(r1);
            r2.mSpan = null;
            if (r2.isItemRemoved() == false) goto L5;
        L6:
            this.mDeletedSize -= this.this$0.mPrimaryOrientation.getDecoratedMeasurement(r1);
        L8:
            if (r0 != 1) goto L10;
            this.mCachedStart = Integer.MIN_VALUE;
        L10:
            this.mCachedEnd = Integer.MIN_VALUE;
            return;
        L5:
            if (r2.isItemChanged() == false) goto L8;
            goto L6
        }

        void popStart() {
            View r0 = this.mViews.remove(0);
            LayoutParams r1 = getLayoutParams(r0);
            r1.mSpan = null;
            if (this.mViews.size() != 0) goto L6;
            this.mCachedEnd = Integer.MIN_VALUE;
        L6:
            if (r1.isItemRemoved() == false) goto L8;
        L9:
            this.mDeletedSize -= this.this$0.mPrimaryOrientation.getDecoratedMeasurement(r0);
        L10:
            this.mCachedStart = Integer.MIN_VALUE;
            return;
        L8:
            if (r1.isItemChanged() == false) goto L10;
            goto L9
        }

        void prependToSpan(View r5) {
            LayoutParams r0 = getLayoutParams(r5);
            r0.mSpan = this;
            this.mViews.add(0, r5);
            this.mCachedStart = Integer.MIN_VALUE;
            if (this.mViews.size() != 1) goto L6;
            this.mCachedEnd = Integer.MIN_VALUE;
        L6:
            if (r0.isItemRemoved() == false) goto L8;
        L9:
            this.mDeletedSize += this.this$0.mPrimaryOrientation.getDecoratedMeasurement(r5);
            return;
        L8:
            if (r0.isItemChanged() == true) goto L9;
        }

        void setLine(int r1) {
            this.mCachedStart = r1;
            this.mCachedEnd = r1;
        }
    }

    public StaggeredGridLayoutManager(int r3, int r4) {
        this.mSpanCount = -1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mLazySpanLookup = new LazySpanLookup();
        this.mGapStrategy = 2;
        this.mTmpRect = new Rect();
        this.mAnchorInfo = new AnchorInfo(this);
        this.mLaidOutInvalidFullSpan = false;
        this.mSmoothScrollbarEnabled = true;
        this.mCheckForGapsRunnable = new RunnableC05181(this);
        this.mOrientation = r4;
        setSpanCount(r3);
        this.mLayoutState = new LayoutState();
        createOrientationHelpers();
    }

    public StaggeredGridLayoutManager(Context r3, AttributeSet r4, int r5, int r6) {
        this.mSpanCount = -1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mLazySpanLookup = new LazySpanLookup();
        this.mGapStrategy = 2;
        this.mTmpRect = new Rect();
        this.mAnchorInfo = new AnchorInfo(this);
        this.mLaidOutInvalidFullSpan = false;
        this.mSmoothScrollbarEnabled = true;
        this.mCheckForGapsRunnable = new RunnableC05181(this);
        RecyclerView.LayoutManager.Properties r0 = getProperties(r3, r4, r5, r6);
        setOrientation(r0.orientation);
        setSpanCount(r0.spanCount);
        setReverseLayout(r0.reverseLayout);
        this.mLayoutState = new LayoutState();
        createOrientationHelpers();
    }

    private void appendViewToAllSpans(View r3) {
        int r0 = this.mSpanCount - 1;
    L3:
        if (r0 < 0) goto L5;
        this.mSpans[r0].appendToSpan(r3);
        r0 = r0 - 1;
        goto L3
    }

    private void applyPendingSavedState(AnchorInfo r4) {
        if (this.mPendingSavedState.mSpanOffsetsSize > 0) goto L5;
    L18:
        this.mLastLayoutRTL = this.mPendingSavedState.mLastLayoutRTL;
        setReverseLayout(this.mPendingSavedState.mReverseLayout);
        resolveShouldLayoutReverse();
        if (this.mPendingSavedState.mAnchorPosition == (-1)) goto L21;
        this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
        r4.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
    L23:
        if (this.mPendingSavedState.mSpanLookupSize <= 1) goto L30;
        LazySpanLookup r0 = this.mLazySpanLookup;
        r0.mData = this.mPendingSavedState.mSpanLookup;
        LazySpanLookup r1 = this.mLazySpanLookup;
        r1.mFullSpanItems = this.mPendingSavedState.mFullSpanItems;
        return;
    L30:
        return;
    L21:
        r4.mLayoutFromEnd = this.mShouldReverseLayout;
        goto L23
    L5:
        if (this.mPendingSavedState.mSpanOffsetsSize != this.mSpanCount) goto L17;
        int r2 = 0;
    L8:
        if (r2 >= this.mSpanCount) goto L18;
        this.mSpans[r2].clear();
        int r3 = this.mPendingSavedState.mSpanOffsets[r2];
        if (r3 == Integer.MIN_VALUE) goto L15;
        if (this.mPendingSavedState.mAnchorLayoutFromEnd == false) goto L14;
        r3 = r3 + this.mPrimaryOrientation.getEndAfterPadding();
        goto L15
    L14:
        r3 = r3 + this.mPrimaryOrientation.getStartAfterPadding();
    L15:
        this.mSpans[r2].setLine(r3);
        r2 = r2 + 1;
        goto L8
    L17:
        this.mPendingSavedState.invalidateSpanInfo();
        SavedState r5 = this.mPendingSavedState;
        r5.mAnchorPosition = this.mPendingSavedState.mVisibleAnchorPosition;
        goto L18
    }

    private void attachViewToSpans(View r3, LayoutParams r4, LayoutState r5) {
        if (r5.mLayoutDirection != 1) goto L9;
        if (r4.mFullSpan == false) goto L7;
        appendViewToAllSpans(r3);
        return;
    L7:
        r4.mSpan.appendToSpan(r3);
        return;
    L9:
        if (r4.mFullSpan == false) goto L11;
        prependViewToAllSpans(r3);
        return;
    L11:
        r4.mSpan.prependToSpan(r3);
    }

    private int calculateScrollDirectionForPosition(int r6) {
        if (getChildCount() != 0) goto L9;
        if (this.mShouldReverseLayout == true) goto L7;
        return -1;
    L7:
        return 1;
    L9:
        if (r6 >= getFirstChildPosition()) goto L11;
        boolean r3 = true;
    L13:
        if (r3 == this.mShouldReverseLayout) goto L16;
        return -1;
    L16:
        return 1;
    L11:
        r3 = false;
        goto L13
    }

    private boolean checkSpanForGap(Span r4) {
        if (this.mShouldReverseLayout == false) goto L9;
        if (r4.getEndLine() < this.mPrimaryOrientation.getEndAfterPadding()) goto L7;
    L12:
        return false;
    L7:
        return !r4.getLayoutParams(r4.mViews.get(r4.mViews.size() - 1)).mFullSpan;
    L9:
        if (r4.getStartLine() <= this.mPrimaryOrientation.getStartAfterPadding()) goto L12;
        return !r4.getLayoutParams(r4.mViews.get(0)).mFullSpan;
    }

    private int computeScrollExtent(RecyclerView.State r8) {
        if (getChildCount() != 0) goto L7;
        return 0;
    L7:
        return ScrollbarHelper.computeScrollExtent(r8, this.mPrimaryOrientation, findFirstVisibleItemClosestToStart(!this.mSmoothScrollbarEnabled), findFirstVisibleItemClosestToEnd(!this.mSmoothScrollbarEnabled), this, this.mSmoothScrollbarEnabled);
    }

    private int computeScrollOffset(RecyclerView.State r9) {
        if (getChildCount() != 0) goto L7;
        return 0;
    L7:
        return ScrollbarHelper.computeScrollOffset(r9, this.mPrimaryOrientation, findFirstVisibleItemClosestToStart(!this.mSmoothScrollbarEnabled), findFirstVisibleItemClosestToEnd(!this.mSmoothScrollbarEnabled), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    }

    private int computeScrollRange(RecyclerView.State r8) {
        if (getChildCount() != 0) goto L7;
        return 0;
    L7:
        return ScrollbarHelper.computeScrollRange(r8, this.mPrimaryOrientation, findFirstVisibleItemClosestToStart(!this.mSmoothScrollbarEnabled), findFirstVisibleItemClosestToEnd(!this.mSmoothScrollbarEnabled), this, this.mSmoothScrollbarEnabled);
    }

    private int convertFocusDirectionToLayoutDirection(int r5) {
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

    private LazySpanLookup.FullSpanItem createFullSpanItemFromEnd(int r5) {
        LazySpanLookup.FullSpanItem r0 = new LazySpanLookup.FullSpanItem();
        r0.mGapPerSpan = new int[this.mSpanCount];
        int r1 = 0;
    L4:
        if (r1 >= this.mSpanCount) goto L6;
        r0.mGapPerSpan[r1] = r5 - this.mSpans[r1].getEndLine(r5);
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    private LazySpanLookup.FullSpanItem createFullSpanItemFromStart(int r5) {
        LazySpanLookup.FullSpanItem r0 = new LazySpanLookup.FullSpanItem();
        r0.mGapPerSpan = new int[this.mSpanCount];
        int r1 = 0;
    L4:
        if (r1 >= this.mSpanCount) goto L6;
        r0.mGapPerSpan[r1] = this.mSpans[r1].getStartLine(r5) - r5;
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    private void createOrientationHelpers() {
        this.mPrimaryOrientation = OrientationHelper.createOrientationHelper(this, this.mOrientation);
        this.mSecondaryOrientation = OrientationHelper.createOrientationHelper(this, 1 - this.mOrientation);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [androidx.recyclerview.widget.StaggeredGridLayoutManager] */
    /* JADX WARN: Type inference failed for: r0v7 */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference failed for: r16v0 */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v23 */
    /* JADX WARN: Type inference failed for: r3v9 */
    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r8v18 */
    /* JADX WARN: Type inference failed for: r8v3 */
    /* JADX WARN: Type inference failed for: r9v0 */
    /* JADX WARN: Type inference failed for: r9v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r9v6 */
    private int fill(RecyclerView.Recycler r21, LayoutState r22, RecyclerView.State r23) {
        StaggeredGridLayoutManager r0 = this;
        ?? r8 = 0;
        ?? r9 = 1;
        r0.mRemainingSpans.set(0, r0.mSpanCount, true);
        if (r0.mLayoutState.mInfinite == false) goto L9;
        if (r22.mLayoutDirection != 1) goto L7;
        int r10 = Integer.MAX_VALUE;
    L12:
        r0.updateAllRemainingSpans(r22.mLayoutDirection, r10);
        if (r0.mShouldReverseLayout == false) goto L15;
        int r1 = r0.mPrimaryOrientation.getEndAfterPadding();
    L16:
        int r11 = r1;
        boolean r12 = false;
        ?? r2 = r0;
    L18:
        if (r22.hasMore(r23) == false) goto L109;
        if (r2.mLayoutState.mInfinite == false) goto L22;
    L25:
        View r3 = r22.next(r21);
        LayoutParams r13 = (LayoutParams) r3.getLayoutParams();
        int r14 = r13.getViewLayoutPosition();
        int r15 = r2.mLazySpanLookup.getSpan(r14);
        if (r15 != (-1)) goto L28;
        ?? r4 = r9;
    L29:
        ?? r16 = r4;
        if (r16 != 0) goto L32;
        Span r5 = r2.mSpans[r15];
    L37:
        r13.mSpan = r5;
        if (r22.mLayoutDirection != r9) goto L40;
        r2.addView(r3);
    L41:
        r2.measureChildWithDecorationsAndMargin(r3, r13, r8);
        if (r22.mLayoutDirection != r9) goto L53;
        if (r13.mFullSpan == false) goto L46;
        int r6 = r2.getMaxEnd(r11);
    L47:
        int r7 = r2.mPrimaryOrientation.getDecoratedMeasurement(r3) + r6;
        if (r16 != 0) goto L50;
    L61:
        int r17 = r6;
        int r18 = r7;
        if (r13.mFullSpan == true) goto L64;
    L76:
        r2.attachViewToSpans(r3, r13, r22);
        if (r2.isLayoutRTL() == false) goto L86;
        if (r2.mOrientation != r9) goto L86;
        if (r13.mFullSpan == false) goto L83;
        int r19 = r2.mSecondaryOrientation.getEndAfterPadding();
    L84:
        int r20 = r19 - r2.mSecondaryOrientation.getDecoratedMeasurement(r3);
    L90:
        int r110 = r20;
        if (r2.mOrientation != 1) goto L93;
        int r24 = r110;
        r2.layoutDecoratedWithMargins(r3, r24, r17, r19, r18);
        StaggeredGridLayoutManager r25 = this;
    L95:
        if (r13.mFullSpan == false) goto L97;
        r25.updateAllRemainingSpans(r25.mLayoutState.mLayoutDirection, r10);
    L98:
        r25.recycle(r21, r25.mLayoutState);
        if (r25.mLayoutState.mStopInFocusable == true) goto L101;
    L106:
        boolean r26 = false;
    L107:
        r12 = true;
        r8 = r26;
        r9 = 1;
        r2 = r25;
        goto L18
    L101:
        if (r3.hasFocusable() == false) goto L106;
        if (r13.mFullSpan == false) goto L105;
        r25.mRemainingSpans.clear();
        r26 = false;
        goto L107
    L105:
        r26 = false;
        r25.mRemainingSpans.set(r5.mIndex, false);
        goto L107
    L97:
        r25.updateRemainingSpans(r5, r25.mLayoutState.mLayoutDirection, r10);
        goto L98
    L93:
        StaggeredGridLayoutManager r27 = this;
        r27.layoutDecoratedWithMargins(r3, r17, r110, r18, r19);
        r24 = r110;
        r25 = r27;
        goto L95
    L83:
        r19 = r2.mSecondaryOrientation.getEndAfterPadding() - (((r2.mSpanCount - r9) - r5.mIndex) * r2.mSizePerSpan);
    L86:
        if (r13.mFullSpan == false) goto L88;
        int r28 = r2.mSecondaryOrientation.getStartAfterPadding();
    L89:
        r20 = r28;
        r19 = r2.mSecondaryOrientation.getDecoratedMeasurement(r3) + r20;
        goto L90
    L88:
        r28 = (r5.mIndex * r2.mSizePerSpan) + r2.mSecondaryOrientation.getStartAfterPadding();
        goto L89
    L64:
        if (r22.mItemDirection != (-1)) goto L76;
        if (r16 == 0) goto L68;
        r2.mLaidOutInvalidFullSpan = r9;
        goto L76
    L68:
        if (r22.mLayoutDirection != r9) goto L70;
        int r29 = (r2.areAllEndsEqual() ? 1 : 0) ^ r9;
    L71:
        if (r29 == 0) goto L76;
        LazySpanLookup.FullSpanItem r30 = r2.mLazySpanLookup.getFullSpanItem(r14);
        if (r30 == null) goto L75;
        r30.mHasUnwantedGapAfter = r9;
    L75:
        r2.mLaidOutInvalidFullSpan = r9;
        goto L76
    L70:
        r29 = (r2.areAllStartsEqual() ? 1 : 0) ^ r9;
        goto L71
    L50:
        if (r13.mFullSpan == false) goto L61;
        LazySpanLookup.FullSpanItem r31 = r2.createFullSpanItemFromEnd(r6);
        r31.mGapDir = -1;
        r31.mPosition = r14;
        r2.mLazySpanLookup.addFullSpanItem(r31);
        goto L61
    L46:
        r6 = r5.getEndLine(r11);
        goto L47
    L53:
        if (r13.mFullSpan == false) goto L55;
        int r32 = r2.getMinStart(r11);
    L56:
        r7 = r32;
        r6 = r7 - r2.mPrimaryOrientation.getDecoratedMeasurement(r3);
        if (r16 == 0) goto L61;
        if (r13.mFullSpan == false) goto L61;
        LazySpanLookup.FullSpanItem r33 = r2.createFullSpanItemFromStart(r7);
        r33.mGapDir = r9;
        r33.mPosition = r14;
        r2.mLazySpanLookup.addFullSpanItem(r33);
        goto L61
    L55:
        r32 = r5.getStartLine(r11);
        goto L56
    L40:
        r2.addView(r3, r8);
        goto L41
    L32:
        if (r13.mFullSpan == false) goto L34;
        r5 = r2.mSpans[r8];
    L35:
        r2.mLazySpanLookup.setSpan(r14, r5);
        goto L37
    L34:
        r5 = r2.getNextSpan(r22);
        goto L35
    L28:
        r4 = r8;
        goto L29
    L22:
        if (r2.mRemainingSpans.isEmpty() == false) goto L25;
    L109:
        if (r12 == true) goto L112;
        r2.recycle(r21, r2.mLayoutState);
    L112:
        if (r2.mLayoutState.mLayoutDirection != (-1)) goto L114;
        int r34 = r2.mPrimaryOrientation.getStartAfterPadding() - r2.getMinStart(r2.mPrimaryOrientation.getStartAfterPadding());
    L115:
        if (r34 <= 0) goto L118;
        return Math.min(r22.mAvailable, r34);
    L118:
        return r8 == true ? 1 : 0;
    L114:
        r34 = r2.getMaxEnd(r2.mPrimaryOrientation.getEndAfterPadding()) - r2.mPrimaryOrientation.getEndAfterPadding();
        goto L115
    L15:
        r1 = r0.mPrimaryOrientation.getStartAfterPadding();
        goto L16
    L7:
        r10 = Integer.MIN_VALUE;
        goto L12
    L9:
        if (r22.mLayoutDirection != 1) goto L11;
        r10 = r22.mEndLine + r22.mAvailable;
        goto L12
    L11:
        r10 = r22.mStartLine - r22.mAvailable;
        goto L12
    }

    private int findFirstReferenceChildPosition(int r5) {
        int r0 = getChildCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L9;
        int r3 = getPosition(getChildAt(r1));
        if (r3 < 0) goto L8;
        if (r3 >= r5) goto L8;
        return r3;
    L8:
        r1 = r1 + 1;
        goto L3
    L9:
        return 0;
    }

    private int findLastReferenceChildPosition(int r4) {
        int r0 = getChildCount() - 1;
    L3:
        if (r0 < 0) goto L9;
        int r2 = getPosition(getChildAt(r0));
        if (r2 < 0) goto L8;
        if (r2 >= r4) goto L8;
        return r2;
    L8:
        r0 = r0 - 1;
        goto L3
    L9:
        return 0;
    }

    private void fixEndGap(RecyclerView.Recycler r5, RecyclerView.State r6, boolean r7) {
        int r1 = getMaxEnd(Integer.MIN_VALUE);
        if (r1 != Integer.MIN_VALUE) goto L5;
        return;
    L5:
        int r0 = this.mPrimaryOrientation.getEndAfterPadding() - r1;
        if (r0 <= 0) goto L12;
        int r2 = r0 - (-scrollBy(-r0, r5, r6));
        if (r7 == false) goto L13;
        if (r2 <= 0) goto L14;
        this.mPrimaryOrientation.offsetChildren(r2);
        return;
    L14:
        return;
    L13:
        return;
    }

    private void fixStartGap(RecyclerView.Recycler r6, RecyclerView.State r7, boolean r8) {
        int r1 = getMinStart(Integer.MAX_VALUE);
        if (r1 != Integer.MAX_VALUE) goto L5;
        return;
    L5:
        int r0 = r1 - this.mPrimaryOrientation.getStartAfterPadding();
        if (r0 <= 0) goto L12;
        int r2 = r0 - scrollBy(r0, r6, r7);
        if (r8 == false) goto L13;
        if (r2 <= 0) goto L14;
        this.mPrimaryOrientation.offsetChildren(-r2);
        return;
    L14:
        return;
    L13:
        return;
    }

    private int getMaxEnd(int r4) {
        int r0 = this.mSpans[0].getEndLine(r4);
        int r1 = 1;
    L4:
        if (r1 >= this.mSpanCount) goto L9;
        int r2 = this.mSpans[r1].getEndLine(r4);
        if (r2 <= r0) goto L8;
        r0 = r2;
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        return r0;
    }

    private int getMaxStart(int r4) {
        int r0 = this.mSpans[0].getStartLine(r4);
        int r1 = 1;
    L4:
        if (r1 >= this.mSpanCount) goto L9;
        int r2 = this.mSpans[r1].getStartLine(r4);
        if (r2 <= r0) goto L8;
        r0 = r2;
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        return r0;
    }

    private int getMinEnd(int r4) {
        int r0 = this.mSpans[0].getEndLine(r4);
        int r1 = 1;
    L4:
        if (r1 >= this.mSpanCount) goto L9;
        int r2 = this.mSpans[r1].getEndLine(r4);
        if (r2 >= r0) goto L8;
        r0 = r2;
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        return r0;
    }

    private int getMinStart(int r4) {
        int r0 = this.mSpans[0].getStartLine(r4);
        int r1 = 1;
    L4:
        if (r1 >= this.mSpanCount) goto L9;
        int r2 = this.mSpans[r1].getStartLine(r4);
        if (r2 >= r0) goto L8;
        r0 = r2;
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        return r0;
    }

    private Span getNextSpan(LayoutState r11) {
        if (preferLastSpan(r11.mLayoutDirection) == false) goto L5;
        int r2 = this.mSpanCount - 1;
        int r3 = -1;
        int r4 = -1;
    L7:
        if (r11.mLayoutDirection != 1) goto L15;
        Span r1 = null;
        int r5 = Integer.MAX_VALUE;
        int r6 = this.mPrimaryOrientation.getStartAfterPadding();
        int r7 = r2;
    L9:
        if (r7 == r3) goto L14;
        Span r8 = this.mSpans[r7];
        int r9 = r8.getEndLine(r6);
        if (r9 >= r5) goto L13;
        r1 = r8;
        r5 = r9;
    L13:
        r7 = r7 + r4;
        goto L9
    L14:
        return r1;
    L15:
        Span r10 = null;
        int r12 = Integer.MIN_VALUE;
        int r13 = this.mPrimaryOrientation.getEndAfterPadding();
        int r14 = r2;
    L16:
        if (r14 == r3) goto L21;
        Span r15 = this.mSpans[r14];
        int r16 = r15.getStartLine(r13);
        if (r16 <= r12) goto L20;
        r10 = r15;
        r12 = r16;
    L20:
        r14 = r14 + r4;
        goto L16
    L21:
        return r10;
    L5:
        r2 = 0;
        r3 = this.mSpanCount;
        r4 = 1;
        goto L7
    }

    private void handleUpdate(int r6, int r7, int r8) {
        if (this.mShouldReverseLayout == false) goto L5;
        int r0 = getLastChildPosition();
    L7:
        if (r8 != 8) goto L11;
        if (r6 >= r7) goto L10;
        int r1 = r7 + 1;
        int r2 = r6;
    L12:
        this.mLazySpanLookup.invalidateAfter(r2);
        switch(r8) {
            case 1: goto L17;
            case 2: goto L16;
            case 8: goto L15;
            default: goto L18;
        };
    L15:
        this.mLazySpanLookup.offsetForRemoval(r6, 1);
        this.mLazySpanLookup.offsetForAddition(r7, 1);
        goto L18
    L16:
        this.mLazySpanLookup.offsetForRemoval(r6, r7);
        goto L18
    L17:
        this.mLazySpanLookup.offsetForAddition(r6, r7);
    L18:
        if (r1 > r0) goto L21;
        return;
    L21:
        if (this.mShouldReverseLayout == false) goto L23;
        int r3 = getFirstChildPosition();
    L24:
        if (r2 > r3) goto L27;
        requestLayout();
        return;
    L27:
        return;
    L23:
        r3 = getLastChildPosition();
        goto L24
    L10:
        r1 = r6 + 1;
        r2 = r7;
        goto L12
    L11:
        r2 = r6;
        r1 = r6 + r7;
        goto L12
    L5:
        r0 = getFirstChildPosition();
        goto L7
    }

    private void measureChildWithDecorationsAndMargin(View r5, int r6, int r7, boolean r8) {
        calculateItemDecorationsForChild(r5, this.mTmpRect);
        LayoutParams r0 = (LayoutParams) r5.getLayoutParams();
        int r9 = updateSpecWithExtra(r6, r0.leftMargin + this.mTmpRect.left, r0.rightMargin + this.mTmpRect.right);
        int r10 = updateSpecWithExtra(r7, r0.topMargin + this.mTmpRect.top, r0.bottomMargin + this.mTmpRect.bottom);
        if (r8 == false) goto L5;
        boolean r1 = shouldReMeasureChild(r5, r9, r10, r0);
    L7:
        if (r1 == false) goto L10;
        r5.measure(r9, r10);
        return;
    L10:
        return;
    L5:
        r1 = shouldMeasureChild(r5, r9, r10, r0);
        goto L7
    }

    private void measureChildWithDecorationsAndMargin(View r7, LayoutParams r8, boolean r9) {
        if (r8.mFullSpan == false) goto L9;
        if (this.mOrientation != 1) goto L7;
        measureChildWithDecorationsAndMargin(r7, this.mFullSizeSpec, getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom(), r8.height, true), r9);
        return;
    L7:
        measureChildWithDecorationsAndMargin(r7, getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight(), r8.width, true), this.mFullSizeSpec, r9);
        return;
    L9:
        if (this.mOrientation != 1) goto L11;
        measureChildWithDecorationsAndMargin(r7, getChildMeasureSpec(this.mSizePerSpan, getWidthMode(), 0, r8.width, false), getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom(), r8.height, true), r9);
        return;
    L11:
        measureChildWithDecorationsAndMargin(r7, getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight(), r8.width, true), getChildMeasureSpec(this.mSizePerSpan, getHeightMode(), 0, r8.height, false), r9);
    }

    private void onLayoutChildren(RecyclerView.Recycler r10, RecyclerView.State r11, boolean r12) {
        AnchorInfo r0 = this.mAnchorInfo;
        if (this.mPendingSavedState != null) goto L7;
        if (this.mPendingScrollPosition != (-1)) goto L7;
    L10:
        boolean r4 = true;
        if (r0.mValid == true) goto L13;
    L18:
        boolean r1 = true;
    L19:
        if (r1 == false) goto L26;
        r0.reset();
        if (this.mPendingSavedState == null) goto L23;
        applyPendingSavedState(r0);
    L24:
        updateAnchorInfoForLayout(r11, r0);
        r0.mValid = true;
        goto L26
    L23:
        resolveShouldLayoutReverse();
        r0.mLayoutFromEnd = this.mShouldReverseLayout;
    L26:
        if (this.mPendingSavedState != null) goto L35;
        if (this.mPendingScrollPosition != (-1)) goto L35;
        if (r0.mLayoutFromEnd == this.mLastLayoutFromEnd) goto L32;
    L33:
        this.mLazySpanLookup.clear();
        r0.mInvalidateOffsets = true;
        goto L35
    L32:
        if (isLayoutRTL() != this.mLastLayoutRTL) goto L33;
    L35:
        if (getChildCount() > 0) goto L37;
    L63:
        detachAndScrapAttachedViews(r10);
        this.mLayoutState.mRecycle = false;
        this.mLaidOutInvalidFullSpan = false;
        updateMeasureSpecs(this.mSecondaryOrientation.getTotalSpace());
        updateLayoutState(r0.mPosition, r11);
        if (r0.mLayoutFromEnd == false) goto L66;
        setLayoutStateDirection(-1);
        fill(r10, this.mLayoutState, r11);
        setLayoutStateDirection(1);
        this.mLayoutState.mCurrentPosition = r0.mPosition + this.mLayoutState.mItemDirection;
        fill(r10, this.mLayoutState, r11);
    L67:
        repositionToWrapContentIfNecessary();
        if (getChildCount() > 0) goto L70;
    L73:
        boolean r2 = false;
        if (r12 == false) goto L92;
        if (r11.isPreLayout() == true) goto L92;
        if (this.mGapStrategy != 0) goto L80;
    L86:
        r4 = false;
    L87:
        if (r4 == false) goto L92;
        removeCallbacks(this.mCheckForGapsRunnable);
        if (checkForGaps() == false) goto L92;
        r2 = true;
        goto L92
    L80:
        if (getChildCount() <= 0) goto L86;
        if (this.mLaidOutInvalidFullSpan == true) goto L87;
        if (hasGapsToFix() == null) goto L86;
    L92:
        if (r11.isPreLayout() == false) goto L94;
        this.mAnchorInfo.reset();
    L94:
        this.mLastLayoutFromEnd = r0.mLayoutFromEnd;
        this.mLastLayoutRTL = isLayoutRTL();
        if (r2 == false) goto L103;
        this.mAnchorInfo.reset();
        onLayoutChildren(r10, r11, false);
        return;
    L103:
        return;
    L70:
        if (this.mShouldReverseLayout == false) goto L72;
        fixEndGap(r10, r11, true);
        fixStartGap(r10, r11, false);
        goto L73
    L72:
        fixStartGap(r10, r11, true);
        fixEndGap(r10, r11, false);
        goto L73
    L66:
        setLayoutStateDirection(1);
        fill(r10, this.mLayoutState, r11);
        setLayoutStateDirection(-1);
        this.mLayoutState.mCurrentPosition = r0.mPosition + this.mLayoutState.mItemDirection;
        fill(r10, this.mLayoutState, r11);
        goto L67
    L37:
        if (this.mPendingSavedState == null) goto L41;
        if (this.mPendingSavedState.mSpanOffsetsSize >= 1) goto L63;
    L41:
        if (r0.mInvalidateOffsets == false) goto L50;
        int r5 = 0;
    L44:
        if (r5 >= this.mSpanCount) goto L63;
        this.mSpans[r5].clear();
        if (r0.mOffset == Integer.MIN_VALUE) goto L48;
        this.mSpans[r5].setLine(r0.mOffset);
    L48:
        r5 = r5 + 1;
        goto L44
    L50:
        if (r1 == false) goto L52;
    L58:
        int r6 = 0;
    L60:
        if (r6 >= this.mSpanCount) goto L62;
        this.mSpans[r6].cacheReferenceLineAndClear(this.mShouldReverseLayout, r0.mOffset);
        r6 = r6 + 1;
        goto L60
    L62:
        this.mAnchorInfo.saveSpanReferenceLines(this.mSpans);
        goto L63
    L52:
        if (this.mAnchorInfo.mSpanReferenceLines == null) goto L58;
        int r7 = 0;
    L56:
        if (r7 >= this.mSpanCount) goto L63;
        Span r8 = this.mSpans[r7];
        r8.clear();
        r8.setLine(this.mAnchorInfo.mSpanReferenceLines[r7]);
        r7 = r7 + 1;
        goto L56
    L13:
        if (this.mPendingScrollPosition != (-1)) goto L18;
        if (this.mPendingSavedState != null) goto L18;
        r1 = false;
    L7:
        if (r11.getItemCount() != 0) goto L10;
        removeAndRecycleAllViews(r10);
        r0.reset();
    }

    private boolean preferLastSpan(int r5) {
        if (this.mOrientation != 0) goto L12;
        if (r5 != (-1)) goto L6;
        boolean r0 = true;
    L8:
        if (r0 == this.mShouldReverseLayout) goto L11;
        return true;
    L11:
        return false;
    L6:
        r0 = false;
        goto L8
    L12:
        if (r5 != (-1)) goto L14;
        boolean r1 = true;
    L16:
        if (r1 != this.mShouldReverseLayout) goto L18;
        boolean r2 = true;
    L20:
        if (r2 != isLayoutRTL()) goto L23;
        return true;
    L23:
        return false;
    L18:
        r2 = false;
        goto L20
    L14:
        r1 = false;
        goto L16
    }

    private void prependViewToAllSpans(View r3) {
        int r0 = this.mSpanCount - 1;
    L3:
        if (r0 < 0) goto L5;
        this.mSpans[r0].prependToSpan(r3);
        r0 = r0 - 1;
        goto L3
    }

    private void recycle(RecyclerView.Recycler r4, LayoutState r5) {
        if (r5.mRecycle == true) goto L5;
        return;
    L5:
        if (r5.mInfinite == false) goto L8;
        return;
    L8:
        if (r5.mAvailable != 0) goto L14;
        if (r5.mLayoutDirection != (-1)) goto L12;
        recycleFromEnd(r4, r5.mEndLine);
        return;
    L12:
        recycleFromStart(r4, r5.mStartLine);
        return;
    L14:
        if (r5.mLayoutDirection != (-1)) goto L20;
        int r0 = r5.mStartLine - getMaxStart(r5.mStartLine);
        if (r0 >= 0) goto L18;
        int r1 = r5.mEndLine;
    L19:
        recycleFromEnd(r4, r1);
        return;
    L18:
        r1 = r5.mEndLine - Math.min(r0, r5.mAvailable);
        goto L19
    L20:
        int r2 = getMinEnd(r5.mEndLine) - r5.mEndLine;
        if (r2 >= 0) goto L23;
        int r3 = r5.mStartLine;
    L24:
        recycleFromStart(r4, r3);
        return;
    L23:
        r3 = r5.mStartLine + Math.min(r2, r5.mAvailable);
        goto L24
    }

    private void recycleFromEnd(RecyclerView.Recycler r8, int r9) {
        int r1 = getChildCount() - 1;
    L3:
        if (r1 < 0) goto L28;
        View r2 = getChildAt(r1);
        if (this.mPrimaryOrientation.getDecoratedStart(r2) < r9) goto L27;
        if (this.mPrimaryOrientation.getTransformedStartWithDecoration(r2) < r9) goto L37;
        LayoutParams r3 = (LayoutParams) r2.getLayoutParams();
        if (r3.mFullSpan == false) goto L23;
        int r4 = 0;
    L12:
        if (r4 >= this.mSpanCount) goto L17;
        if (this.mSpans[r4].mViews.size() == 1) goto L15;
        r4 = r4 + 1;
        goto L12
    L15:
        return;
    L17:
        int r5 = 0;
    L19:
        if (r5 >= this.mSpanCount) goto L26;
        this.mSpans[r5].popEnd();
        r5 = r5 + 1;
    L26:
        removeAndRecycleView(r2, r8);
        r1 = r1 - 1;
        goto L3
    L23:
        if (r3.mSpan.mViews.size() == 1) goto L24;
        r3.mSpan.popEnd();
        goto L26
    L24:
        return;
    L37:
        return;
    L27:
        return;
    }

    private void recycleFromStart(RecyclerView.Recycler r6, int r7) {
    L3:
        if (getChildCount() <= 0) goto L28;
        View r0 = getChildAt(0);
        if (this.mPrimaryOrientation.getDecoratedEnd(r0) > r7) goto L27;
        if (this.mPrimaryOrientation.getTransformedEndWithDecoration(r0) > r7) goto L37;
        LayoutParams r1 = (LayoutParams) r0.getLayoutParams();
        if (r1.mFullSpan == false) goto L23;
        int r2 = 0;
    L12:
        if (r2 >= this.mSpanCount) goto L17;
        if (this.mSpans[r2].mViews.size() == 1) goto L15;
        r2 = r2 + 1;
        goto L12
    L15:
        return;
    L17:
        int r3 = 0;
    L19:
        if (r3 >= this.mSpanCount) goto L26;
        this.mSpans[r3].popStart();
        r3 = r3 + 1;
    L26:
        removeAndRecycleView(r0, r6);
        goto L3
    L23:
        if (r1.mSpan.mViews.size() == 1) goto L24;
        r1.mSpan.popStart();
        goto L26
    L24:
        return;
    L37:
        return;
    L27:
        return;
    }

    private void repositionToWrapContentIfNecessary() {
        if (this.mSecondaryOrientation.getMode() != 1073741824) goto L5;
        return;
    L5:
        float r0 = 0.0f;
        int r1 = getChildCount();
        int r2 = 0;
    L6:
        if (r2 >= r1) goto L15;
        View r3 = getChildAt(r2);
        float r4 = this.mSecondaryOrientation.getDecoratedMeasurement(r3);
        if (r4 < r0) goto L14;
        if (((LayoutParams) r3.getLayoutParams()).isFullSpan() == false) goto L13;
        r4 = (1.0f * r4) / this.mSpanCount;
    L13:
        r0 = Math.max(r0, r4);
    L14:
        r2 = r2 + 1;
        goto L6
    L15:
        int r5 = this.mSizePerSpan;
        int r6 = Math.round(this.mSpanCount * r0);
        if (this.mSecondaryOrientation.getMode() != Integer.MIN_VALUE) goto L18;
        r6 = Math.min(r6, this.mSecondaryOrientation.getTotalSpace());
    L18:
        updateMeasureSpecs(r6);
        if (this.mSizePerSpan != r5) goto L21;
        return;
    L21:
        int r7 = 0;
    L22:
        if (r7 >= r1) goto L36;
        View r8 = getChildAt(r7);
        LayoutParams r9 = (LayoutParams) r8.getLayoutParams();
        if (r9.mFullSpan == true) goto L35;
        if (isLayoutRTL() == true) goto L29;
    L31:
        int r10 = r9.mSpan.mIndex * this.mSizePerSpan;
        int r11 = r9.mSpan.mIndex * r5;
        if (this.mOrientation != 1) goto L34;
        r8.offsetLeftAndRight(r10 - r11);
        goto L35
    L34:
        r8.offsetTopAndBottom(r10 - r11);
        goto L35
    L29:
        if (this.mOrientation != 1) goto L31;
        r8.offsetLeftAndRight(((-((this.mSpanCount - 1) - r9.mSpan.mIndex)) * this.mSizePerSpan) - ((-((this.mSpanCount - 1) - r9.mSpan.mIndex)) * r5));
    L35:
        r7 = r7 + 1;
        goto L22
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

    private void setLayoutStateDirection(int r6) {
        this.mLayoutState.mLayoutDirection = r6;
        LayoutState r0 = this.mLayoutState;
        boolean r1 = this.mShouldReverseLayout;
        int r2 = 1;
        if (r6 != (-1)) goto L5;
        boolean r4 = true;
    L6:
        if (r1 == r4) goto L9;
        r2 = -1;
    L9:
        r0.mItemDirection = r2;
        return;
    L5:
        r4 = false;
        goto L6
    }

    private void updateAllRemainingSpans(int r3, int r4) {
        int r0 = 0;
    L4:
        if (r0 >= this.mSpanCount) goto L10;
        if (this.mSpans[r0].mViews.isEmpty() == true) goto L9;
        updateRemainingSpans(this.mSpans[r0], r3, r4);
    L9:
        r0 = r0 + 1;
        goto L4
    }

    private boolean updateAnchorFromChildren(RecyclerView.State r2, AnchorInfo r3) {
        if (this.mLastLayoutFromEnd == false) goto L5;
        int r0 = findLastReferenceChildPosition(r2.getItemCount());
    L6:
        r3.mPosition = r0;
        r3.mOffset = Integer.MIN_VALUE;
        return true;
    L5:
        r0 = findFirstReferenceChildPosition(r2.getItemCount());
        goto L6
    }

    private void updateLayoutState(int r8, RecyclerView.State r9) {
        boolean r1 = false;
        this.mLayoutState.mAvailable = 0;
        this.mLayoutState.mCurrentPosition = r8;
        int r0 = 0;
        int r2 = 0;
        if (isSmoothScrolling() == false) goto L14;
        int r3 = r9.getTargetScrollPosition();
        if (r3 == (-1)) goto L14;
        boolean r5 = this.mShouldReverseLayout;
        if (r3 >= r8) goto L9;
        boolean r6 = true;
    L10:
        if (r5 != r6) goto L12;
        r2 = this.mPrimaryOrientation.getTotalSpace();
        goto L14
    L12:
        r0 = this.mPrimaryOrientation.getTotalSpace();
        goto L14
    L9:
        r6 = false;
    L14:
        if (getClipToPadding() == false) goto L16;
        this.mLayoutState.mStartLine = this.mPrimaryOrientation.getStartAfterPadding() - r0;
        this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEndAfterPadding() + r2;
    L17:
        this.mLayoutState.mStopInFocusable = false;
        this.mLayoutState.mRecycle = true;
        LayoutState r7 = this.mLayoutState;
        if (this.mPrimaryOrientation.getMode() == 0) goto L20;
    L23:
        r7.mInfinite = r1;
        return;
    L20:
        if (this.mPrimaryOrientation.getEnd() != 0) goto L23;
        r1 = true;
        goto L23
    L16:
        this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEnd() + r2;
        this.mLayoutState.mStartLine = -r0;
        goto L17
    }

    private void updateRemainingSpans(Span r6, int r7, int r8) {
        int r0 = r6.getDeletedSize();
        if (r7 != (-1)) goto L9;
        if ((r6.getStartLine() + r0) > r8) goto L14;
        this.mRemainingSpans.set(r6.mIndex, false);
        return;
    L14:
        return;
    L9:
        if ((r6.getEndLine() - r0) < r8) goto L12;
        this.mRemainingSpans.set(r6.mIndex, false);
        return;
    }

    private int updateSpecWithExtra(int r4, int r5, int r6) {
        if (r5 != 0) goto L5;
        if (r6 != 0) goto L5;
        return r4;
    L5:
        int r0 = View.MeasureSpec.getMode(r4);
        if (r0 == Integer.MIN_VALUE) goto L12;
        if (r0 == 1073741824) goto L12;
        return r4;
    L12:
        return View.MeasureSpec.makeMeasureSpec(Math.max(0, (View.MeasureSpec.getSize(r4) - r5) - r6), r0);
    }

    boolean areAllEndsEqual() {
        int r0 = this.mSpans[0].getEndLine(Integer.MIN_VALUE);
        int r3 = 1;
    L4:
        if (r3 >= this.mSpanCount) goto L9;
        if (this.mSpans[r3].getEndLine(Integer.MIN_VALUE) != r0) goto L7;
        r3 = r3 + 1;
        goto L4
    L7:
        return false;
    L9:
        return true;
    }

    boolean areAllStartsEqual() {
        int r0 = this.mSpans[0].getStartLine(Integer.MIN_VALUE);
        int r3 = 1;
    L4:
        if (r3 >= this.mSpanCount) goto L9;
        if (this.mSpans[r3].getStartLine(Integer.MIN_VALUE) != r0) goto L7;
        r3 = r3 + 1;
        goto L4
    L7:
        return false;
    L9:
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void assertNotInLayoutOrScroll(String r2) {
        if (this.mPendingSavedState != null) goto L6;
        super.assertNotInLayoutOrScroll(r2);
        return;
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

    boolean checkForGaps() {
        if (getChildCount() != 0) goto L5;
    L36:
        return false;
    L5:
        if (this.mGapStrategy == 0) goto L36;
        if (isAttachedToWindow() == false) goto L36;
        if (this.mShouldReverseLayout == false) goto L12;
        int r0 = getLastChildPosition();
        int r2 = getFirstChildPosition();
    L14:
        if (r0 != 0) goto L20;
        if (hasGapsToFix() == null) goto L20;
        this.mLazySpanLookup.clear();
        requestSimpleAnimationsInNextLayout();
        requestLayout();
        return true;
    L20:
        if (this.mLaidOutInvalidFullSpan == true) goto L23;
        return false;
    L23:
        if (this.mShouldReverseLayout == false) goto L25;
        int r4 = -1;
    L26:
        LazySpanLookup.FullSpanItem r5 = this.mLazySpanLookup.getFirstFullSpanItemInRange(r0, r2 + 1, r4, true);
        if (r5 != null) goto L30;
        this.mLaidOutInvalidFullSpan = false;
        this.mLazySpanLookup.forceInvalidateAfter(r2 + 1);
        return false;
    L30:
        LazySpanLookup.FullSpanItem r1 = this.mLazySpanLookup.getFirstFullSpanItemInRange(r0, r5.mPosition, r4 * (-1), true);
        if (r1 != null) goto L33;
        this.mLazySpanLookup.forceInvalidateAfter(r5.mPosition);
    L34:
        requestSimpleAnimationsInNextLayout();
        requestLayout();
        return true;
    L33:
        this.mLazySpanLookup.forceInvalidateAfter(r1.mPosition + 1);
        goto L34
    L25:
        r4 = 1;
        goto L26
    L12:
        r0 = getFirstChildPosition();
        r2 = getLastChildPosition();
        goto L14
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean checkLayoutParams(RecyclerView.LayoutParams r2) {
        return r2 instanceof LayoutParams;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void collectAdjacentPrefetchPositions(int r7, int r8, RecyclerView.State r9, RecyclerView.LayoutManager.LayoutPrefetchRegistry r10) {
        if (this.mOrientation != 0) goto L5;
        int r0 = r7;
    L7:
        if (getChildCount() == 0) goto L32;
        if (r0 == 0) goto L39;
        prepareLayoutStateForDelta(r0, r9);
        if (this.mPrefetchDistances != null) goto L13;
    L14:
        this.mPrefetchDistances = new int[this.mSpanCount];
    L15:
        int r1 = 0;
        int r2 = 0;
    L17:
        if (r2 >= this.mSpanCount) goto L26;
        if (this.mLayoutState.mItemDirection != (-1)) goto L21;
        int r3 = this.mLayoutState.mStartLine - this.mSpans[r2].getStartLine(this.mLayoutState.mStartLine);
    L23:
        if (r3 < 0) goto L25;
        this.mPrefetchDistances[r1] = r3;
        r1 = r1 + 1;
    L25:
        r2 = r2 + 1;
        goto L17
    L21:
        r3 = this.mSpans[r2].getEndLine(this.mLayoutState.mEndLine) - this.mLayoutState.mEndLine;
        goto L23
    L26:
        Arrays.sort(this.mPrefetchDistances, 0, r1);
        int r4 = 0;
    L27:
        if (r4 >= r1) goto L31;
        if (this.mLayoutState.hasMore(r9) == false) goto L38;
        r10.addPosition(this.mLayoutState.mCurrentPosition, this.mPrefetchDistances[r4]);
        this.mLayoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
        r4 = r4 + 1;
        goto L27
    L38:
        return;
    L31:
        return;
    L13:
        if (this.mPrefetchDistances.length >= this.mSpanCount) goto L15;
    L39:
        return;
    L32:
        return;
    L5:
        r0 = r8;
        goto L7
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
    public PointF computeScrollVectorForPosition(int r5) {
        int r0 = calculateScrollDirectionForPosition(r5);
        PointF r1 = new PointF();
        if (r0 != 0) goto L7;
        return null;
    L7:
        if (this.mOrientation != 0) goto L9;
        r1.x = r0;
        r1.y = 0.0f;
    L10:
        return r1;
    L9:
        r1.x = 0.0f;
        r1.y = r0;
        goto L10
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

    public int[] findFirstCompletelyVisibleItemPositions(int[] r4) {
        if (r4 != null) goto L5;
        r4 = new int[this.mSpanCount];
    L6:
        int r0 = 0;
    L8:
        if (r0 >= this.mSpanCount) goto L10;
        r4[r0] = this.mSpans[r0].findFirstCompletelyVisibleItemPosition();
        r0 = r0 + 1;
        goto L8
    L10:
        return r4;
    L5:
        if (r4.length >= this.mSpanCount) goto L6;
        throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + r4.length);
    }

    View findFirstVisibleItemClosestToEnd(boolean r8) {
        int r0 = this.mPrimaryOrientation.getStartAfterPadding();
        int r1 = this.mPrimaryOrientation.getEndAfterPadding();
        View r2 = null;
        int r3 = getChildCount() - 1;
    L3:
        if (r3 < 0) goto L15;
        View r4 = getChildAt(r3);
        int r5 = this.mPrimaryOrientation.getDecoratedStart(r4);
        int r6 = this.mPrimaryOrientation.getDecoratedEnd(r4);
        if (r6 <= r0) goto L14;
        if (r5 >= r1) goto L14;
        if (r6 <= r1) goto L13;
        if (r8 == false) goto L13;
        if (r2 != null) goto L14;
        r2 = r4;
    L13:
        return r4;
    L14:
        r3 = r3 - 1;
        goto L3
    L15:
        return r2;
    }

    View findFirstVisibleItemClosestToStart(boolean r9) {
        int r0 = this.mPrimaryOrientation.getStartAfterPadding();
        int r1 = this.mPrimaryOrientation.getEndAfterPadding();
        int r2 = getChildCount();
        View r3 = null;
        int r4 = 0;
    L3:
        if (r4 >= r2) goto L15;
        View r5 = getChildAt(r4);
        int r6 = this.mPrimaryOrientation.getDecoratedStart(r5);
        if (this.mPrimaryOrientation.getDecoratedEnd(r5) <= r0) goto L14;
        if (r6 >= r1) goto L14;
        if (r6 >= r0) goto L13;
        if (r9 == false) goto L13;
        if (r3 != null) goto L14;
        r3 = r5;
    L13:
        return r5;
    L14:
        r4 = r4 + 1;
        goto L3
    L15:
        return r3;
    }

    int findFirstVisibleItemPositionInt() {
        if (this.mShouldReverseLayout == false) goto L5;
        View r0 = findFirstVisibleItemClosestToEnd(true);
    L7:
        if (r0 != null) goto L10;
        return -1;
    L10:
        return getPosition(r0);
    L5:
        r0 = findFirstVisibleItemClosestToStart(true);
        goto L7
    }

    public int[] findFirstVisibleItemPositions(int[] r4) {
        if (r4 != null) goto L5;
        r4 = new int[this.mSpanCount];
    L6:
        int r0 = 0;
    L8:
        if (r0 >= this.mSpanCount) goto L10;
        r4[r0] = this.mSpans[r0].findFirstVisibleItemPosition();
        r0 = r0 + 1;
        goto L8
    L10:
        return r4;
    L5:
        if (r4.length >= this.mSpanCount) goto L6;
        throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + r4.length);
    }

    public int[] findLastCompletelyVisibleItemPositions(int[] r4) {
        if (r4 != null) goto L5;
        r4 = new int[this.mSpanCount];
    L6:
        int r0 = 0;
    L8:
        if (r0 >= this.mSpanCount) goto L10;
        r4[r0] = this.mSpans[r0].findLastCompletelyVisibleItemPosition();
        r0 = r0 + 1;
        goto L8
    L10:
        return r4;
    L5:
        if (r4.length >= this.mSpanCount) goto L6;
        throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + r4.length);
    }

    public int[] findLastVisibleItemPositions(int[] r4) {
        if (r4 != null) goto L5;
        r4 = new int[this.mSpanCount];
    L6:
        int r0 = 0;
    L8:
        if (r0 >= this.mSpanCount) goto L10;
        r4[r0] = this.mSpans[r0].findLastVisibleItemPosition();
        r0 = r0 + 1;
        goto L8
    L10:
        return r4;
    L5:
        if (r4.length >= this.mSpanCount) goto L6;
        throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + r4.length);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
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

    int getFirstChildPosition() {
        if (getChildCount() != 0) goto L6;
        return 0;
    L6:
        return getPosition(getChildAt(0));
    }

    public int getGapStrategy() {
        return this.mGapStrategy;
    }

    int getLastChildPosition() {
        int r0 = getChildCount();
        if (r0 != 0) goto L6;
        return 0;
    L6:
        return getPosition(getChildAt(r0 - 1));
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public boolean getReverseLayout() {
        return this.mReverseLayout;
    }

    public int getSpanCount() {
        return this.mSpanCount;
    }

    View hasGapsToFix() {
        int r2 = getChildCount() - 1;
        BitSet r4 = new BitSet(this.mSpanCount);
        r4.set(0, this.mSpanCount, true);
        int r7 = -1;
        if (this.mOrientation == 1) goto L5;
    L7:
        byte r5 = -1;
    L9:
        if (this.mShouldReverseLayout == false) goto L11;
        int r8 = r2;
        int r9 = 0 - 1;
    L12:
        if (r8 >= r9) goto L14;
        r7 = 1;
    L14:
        int r10 = r8;
    L15:
        if (r10 == r9) goto L51;
        View r11 = getChildAt(r10);
        LayoutParams r12 = (LayoutParams) r11.getLayoutParams();
        if (r4.get(r12.mSpan.mIndex) == false) goto L23;
        if (checkSpanForGap(r12.mSpan) == true) goto L20;
        r4.clear(r12.mSpan.mIndex);
        goto L23
    L20:
        return r11;
    L23:
        if (r12.mFullSpan == true) goto L50;
        if ((r10 + r7) == r9) goto L50;
        View r13 = getChildAt(r10 + r7);
        boolean r14 = false;
        if (this.mShouldReverseLayout == false) goto L35;
        int r15 = this.mPrimaryOrientation.getDecoratedEnd(r11);
        int r3 = this.mPrimaryOrientation.getDecoratedEnd(r13);
        if (r15 < r3) goto L31;
        if (r15 != r3) goto L40;
        r14 = true;
    L40:
        if (r14 == false) goto L50;
        if ((r12.mSpan.mIndex - ((LayoutParams) r13.getLayoutParams()).mSpan.mIndex) >= 0) goto L44;
        boolean r6 = true;
    L45:
        if (r5 >= 0) goto L47;
        boolean r16 = true;
    L48:
        if (r6 == r16) goto L50;
        return r11;
    L47:
        r16 = false;
        goto L48
    L44:
        r6 = false;
        goto L45
    L31:
        return r11;
    L35:
        int r17 = this.mPrimaryOrientation.getDecoratedStart(r11);
        int r18 = this.mPrimaryOrientation.getDecoratedStart(r13);
        if (r17 > r18) goto L37;
        if (r17 != r18) goto L40;
        r14 = true;
        goto L40
    L37:
        return r11;
    L50:
        r10 = r10 + r7;
        goto L15
    L51:
        return null;
    L11:
        r8 = 0;
        r9 = r2 + 1;
        goto L12
    L5:
        if (isLayoutRTL() == false) goto L7;
        r5 = 1;
        goto L9
    }

    public void invalidateSpanAssignments() {
        this.mLazySpanLookup.clear();
        requestLayout();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean isAutoMeasureEnabled() {
        if (this.mGapStrategy == 0) goto L5;
        return true;
    L5:
        return false;
    }

    boolean isLayoutRTL() {
        if (getLayoutDirection() == 1) goto L7;
        return false;
    L7:
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void offsetChildrenHorizontal(int r3) {
        super.offsetChildrenHorizontal(r3);
        int r0 = 0;
    L4:
        if (r0 >= this.mSpanCount) goto L6;
        this.mSpans[r0].onOffset(r3);
        r0 = r0 + 1;
        goto L4
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void offsetChildrenVertical(int r3) {
        super.offsetChildrenVertical(r3);
        int r0 = 0;
    L4:
        if (r0 >= this.mSpanCount) goto L6;
        this.mSpans[r0].onOffset(r3);
        r0 = r0 + 1;
        goto L4
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onAdapterChanged(RecyclerView.Adapter r3, RecyclerView.Adapter r4) {
        this.mLazySpanLookup.clear();
        int r0 = 0;
    L4:
        if (r0 >= this.mSpanCount) goto L6;
        this.mSpans[r0].clear();
        r0 = r0 + 1;
        goto L4
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onDetachedFromWindow(RecyclerView r3, RecyclerView.Recycler r4) {
        super.onDetachedFromWindow(r3, r4);
        removeCallbacks(this.mCheckForGapsRunnable);
        int r0 = 0;
    L4:
        if (r0 >= this.mSpanCount) goto L6;
        this.mSpans[r0].clear();
        r0 = r0 + 1;
        goto L4
    L6:
        r3.requestLayout();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public View onFocusSearchFailed(View r12, int r13, RecyclerView.Recycler r14, RecyclerView.State r15) {
        if (getChildCount() != 0) goto L5;
        return null;
    L5:
        View r0 = findContainingItemView(r12);
        if (r0 != null) goto L8;
        return null;
    L8:
        resolveShouldLayoutReverse();
        int r2 = convertFocusDirectionToLayoutDirection(r13);
        if (r2 != Integer.MIN_VALUE) goto L11;
        return null;
    L11:
        LayoutParams r3 = (LayoutParams) r0.getLayoutParams();
        boolean r4 = r3.mFullSpan;
        Span r5 = r3.mSpan;
        if (r2 != 1) goto L14;
        int r7 = getLastChildPosition();
    L15:
        updateLayoutState(r7, r15);
        setLayoutStateDirection(r2);
        this.mLayoutState.mCurrentPosition = this.mLayoutState.mItemDirection + r7;
        this.mLayoutState.mAvailable = (int) (this.mPrimaryOrientation.getTotalSpace() * MAX_SCROLL_FACTOR);
        this.mLayoutState.mStopInFocusable = true;
        boolean r9 = false;
        this.mLayoutState.mRecycle = false;
        fill(r14, this.mLayoutState, r15);
        this.mLastLayoutFromEnd = this.mShouldReverseLayout;
        if (r4 == true) goto L22;
        View r8 = r5.getFocusableViewAfter(r7, r2);
        if (r8 == null) goto L22;
        if (r8 == r0) goto L22;
        return r8;
    L22:
        if (preferLastSpan(r2) == false) goto L31;
        int r10 = this.mSpanCount - 1;
    L24:
        if (r10 < 0) goto L39;
        View r11 = this.mSpans[r10].getFocusableViewAfter(r7, r2);
        if (r11 == null) goto L29;
        if (r11 == r0) goto L29;
        return r11;
    L29:
        r10 = r10 - 1;
    L39:
        boolean r16 = !this.mReverseLayout;
        if (r2 != (-1)) goto L42;
        boolean r17 = true;
    L43:
        if (r16 != r17) goto L46;
        r9 = true;
    L46:
        if (r4 == true) goto L55;
        if (r9 == false) goto L49;
        int r18 = r5.findFirstPartiallyVisibleItemPosition();
    L50:
        View r19 = findViewByPosition(r18);
        if (r19 == null) goto L55;
        if (r19 == r0) goto L55;
        return r19;
    L49:
        r18 = r5.findLastPartiallyVisibleItemPosition();
    L55:
        if (preferLastSpan(r2) == false) goto L71;
        int r110 = this.mSpanCount - 1;
    L57:
        if (r110 < 0) goto L82;
        if (r110 == r5.mIndex) goto L69;
        if (r9 == false) goto L63;
        int r6 = this.mSpans[r110].findFirstPartiallyVisibleItemPosition();
    L64:
        View r20 = findViewByPosition(r6);
        if (r20 == null) goto L69;
        if (r20 == r0) goto L69;
        return r20;
    L63:
        r6 = this.mSpans[r110].findLastPartiallyVisibleItemPosition();
    L69:
        r110 = r110 - 1;
    L82:
        return null;
    L71:
        int r21 = 0;
    L73:
        if (r21 >= this.mSpanCount) goto L82;
        if (r9 == false) goto L76;
        int r111 = this.mSpans[r21].findFirstPartiallyVisibleItemPosition();
    L77:
        View r22 = findViewByPosition(r111);
        if (r22 == null) goto L81;
        if (r22 == r0) goto L81;
        return r22;
    L81:
        r21 = r21 + 1;
        goto L73
    L76:
        r111 = this.mSpans[r21].findLastPartiallyVisibleItemPosition();
        goto L77
    L42:
        r17 = false;
        goto L43
    L31:
        int r23 = 0;
    L33:
        if (r23 >= this.mSpanCount) goto L39;
        View r112 = this.mSpans[r23].getFocusableViewAfter(r7, r2);
        if (r112 == null) goto L38;
        if (r112 == r0) goto L38;
        return r112;
    L38:
        r23 = r23 + 1;
        goto L33
    L14:
        r7 = getFirstChildPosition();
        goto L15
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onInitializeAccessibilityEvent(AccessibilityEvent r5) {
        super.onInitializeAccessibilityEvent(r5);
        if (getChildCount() <= 0) goto L13;
        View r1 = findFirstVisibleItemClosestToStart(false);
        View r0 = findFirstVisibleItemClosestToEnd(false);
        if (r1 == null) goto L12;
        if (r0 == null) goto L14;
        int r2 = getPosition(r1);
        int r3 = getPosition(r0);
        if (r2 >= r3) goto L11;
        r5.setFromIndex(r2);
        r5.setToIndex(r3);
        return;
    L11:
        r5.setFromIndex(r3);
        r5.setToIndex(r2);
        return;
    L14:
        return;
    L12:
        return;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsAdded(RecyclerView r2, int r3, int r4) {
        handleUpdate(r3, r4, 1);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsChanged(RecyclerView r2) {
        this.mLazySpanLookup.clear();
        requestLayout();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsMoved(RecyclerView r2, int r3, int r4, int r5) {
        handleUpdate(r3, r4, 8);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsRemoved(RecyclerView r2, int r3, int r4) {
        handleUpdate(r3, r4, 2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsUpdated(RecyclerView r2, int r3, int r4, Object r5) {
        handleUpdate(r3, r4, 4);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(RecyclerView.Recycler r2, RecyclerView.State r3) {
        onLayoutChildren(r2, r3, true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutCompleted(RecyclerView.State r2) {
        super.onLayoutCompleted(r2);
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
        this.mAnchorInfo.reset();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onRestoreInstanceState(Parcelable r3) {
        if ((r3 instanceof SavedState) == false) goto L9;
        this.mPendingSavedState = (SavedState) r3;
        if (this.mPendingScrollPosition == (-1)) goto L7;
        this.mPendingSavedState.invalidateAnchorPositionInfo();
        this.mPendingSavedState.invalidateSpanInfo();
    L7:
        requestLayout();
        return;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public Parcelable onSaveInstanceState() {
        if (this.mPendingSavedState != null) goto L5;
        SavedState r0 = new SavedState();
        r0.mReverseLayout = this.mReverseLayout;
        r0.mAnchorLayoutFromEnd = this.mLastLayoutFromEnd;
        r0.mLastLayoutRTL = this.mLastLayoutRTL;
        if (this.mLazySpanLookup != null) goto L9;
    L11:
        r0.mSpanLookupSize = 0;
    L13:
        if (getChildCount() > 0) goto L15;
        r0.mAnchorPosition = -1;
        r0.mVisibleAnchorPosition = -1;
        r0.mSpanOffsetsSize = 0;
    L32:
        return r0;
    L15:
        if (this.mLastLayoutFromEnd == false) goto L17;
        int r1 = getLastChildPosition();
    L18:
        r0.mAnchorPosition = r1;
        r0.mVisibleAnchorPosition = findFirstVisibleItemPositionInt();
        r0.mSpanOffsetsSize = this.mSpanCount;
        r0.mSpanOffsets = new int[this.mSpanCount];
        int r2 = 0;
    L20:
        if (r2 >= this.mSpanCount) goto L32;
        if (this.mLastLayoutFromEnd == false) goto L26;
        int r3 = this.mSpans[r2].getEndLine(Integer.MIN_VALUE);
        if (r3 == Integer.MIN_VALUE) goto L29;
        r3 = r3 - this.mPrimaryOrientation.getEndAfterPadding();
    L29:
        r0.mSpanOffsets[r2] = r3;
        r2 = r2 + 1;
        goto L20
    L26:
        r3 = this.mSpans[r2].getStartLine(Integer.MIN_VALUE);
        if (r3 == Integer.MIN_VALUE) goto L29;
        r3 = r3 - this.mPrimaryOrientation.getStartAfterPadding();
        goto L29
    L17:
        r1 = getFirstChildPosition();
        goto L18
    L9:
        if (this.mLazySpanLookup.mData == null) goto L11;
        r0.mSpanLookup = this.mLazySpanLookup.mData;
        r0.mSpanLookupSize = r0.mSpanLookup.length;
        r0.mFullSpanItems = this.mLazySpanLookup.mFullSpanItems;
        goto L13
    L5:
        return new SavedState(this.mPendingSavedState);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onScrollStateChanged(int r1) {
        if (r1 != 0) goto L5;
        checkForGaps();
        return;
    }

    void prepareLayoutStateForDelta(int r5, RecyclerView.State r6) {
        if (r5 <= 0) goto L4;
        int r0 = 1;
        int r1 = getLastChildPosition();
    L5:
        this.mLayoutState.mRecycle = true;
        updateLayoutState(r1, r6);
        setLayoutStateDirection(r0);
        LayoutState r2 = this.mLayoutState;
        r2.mCurrentPosition = this.mLayoutState.mItemDirection + r1;
        LayoutState r3 = this.mLayoutState;
        r3.mAvailable = Math.abs(r5);
        return;
    L4:
        r0 = -1;
        r1 = getFirstChildPosition();
        goto L5
    }

    int scrollBy(int r7, RecyclerView.Recycler r8, RecyclerView.State r9) {
        if (getChildCount() == 0) goto L14;
        if (r7 == 0) goto L14;
        prepareLayoutStateForDelta(r7, r9);
        int r0 = fill(r8, this.mLayoutState, r9);
        if (this.mLayoutState.mAvailable >= r0) goto L9;
        int r3 = r7;
    L12:
        this.mPrimaryOrientation.offsetChildren(-r3);
        this.mLastLayoutFromEnd = this.mShouldReverseLayout;
        this.mLayoutState.mAvailable = 0;
        recycle(r8, this.mLayoutState);
        return r3;
    L9:
        if (r7 >= 0) goto L11;
        r3 = -r0;
        goto L12
    L11:
        r3 = r0;
    L14:
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollHorizontallyBy(int r2, RecyclerView.Recycler r3, RecyclerView.State r4) {
        return scrollBy(r2, r3, r4);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void scrollToPosition(int r2) {
        if (this.mPendingSavedState != null) goto L5;
    L7:
        this.mPendingScrollPosition = r2;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        requestLayout();
        return;
    L5:
        if (this.mPendingSavedState.mAnchorPosition == r2) goto L7;
        this.mPendingSavedState.invalidateAnchorPositionInfo();
        goto L7
    }

    public void scrollToPositionWithOffset(int r2, int r3) {
        if (this.mPendingSavedState == null) goto L5;
        this.mPendingSavedState.invalidateAnchorPositionInfo();
    L5:
        this.mPendingScrollPosition = r2;
        this.mPendingScrollPositionOffset = r3;
        requestLayout();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollVerticallyBy(int r2, RecyclerView.Recycler r3, RecyclerView.State r4) {
        return scrollBy(r2, r3, r4);
    }

    public void setGapStrategy(int r3) {
        assertNotInLayoutOrScroll(null);
        if (r3 != this.mGapStrategy) goto L5;
        return;
    L5:
        if (r3 != 0) goto L7;
    L11:
        this.mGapStrategy = r3;
        requestLayout();
        return;
    L7:
        if (r3 == 2) goto L11;
        throw new IllegalArgumentException("invalid gap strategy. Must be GAP_HANDLING_NONE or GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void setMeasuredDimension(Rect r7, int r8, int r9) {
        int r0 = getPaddingLeft() + getPaddingRight();
        int r1 = getPaddingTop() + getPaddingBottom();
        if (this.mOrientation != 1) goto L5;
        int r3 = chooseSize(r9, r7.height() + r1, getMinimumHeight());
        int r2 = chooseSize(r8, (this.mSizePerSpan * this.mSpanCount) + r0, getMinimumWidth());
    L6:
        setMeasuredDimension(r2, r3);
        return;
    L5:
        int r4 = chooseSize(r8, r7.width() + r0, getMinimumWidth());
        r2 = r4;
        r3 = chooseSize(r9, (this.mSizePerSpan * this.mSpanCount) + r1, getMinimumHeight());
        goto L6
    }

    public void setOrientation(int r3) {
        if (r3 != 0) goto L4;
    L8:
        assertNotInLayoutOrScroll(null);
        if (r3 != this.mOrientation) goto L11;
        return;
    L11:
        this.mOrientation = r3;
        OrientationHelper r0 = this.mPrimaryOrientation;
        this.mPrimaryOrientation = this.mSecondaryOrientation;
        this.mSecondaryOrientation = r0;
        requestLayout();
        return;
    L4:
        if (r3 == 1) goto L8;
        throw new IllegalArgumentException("invalid orientation.");
    }

    public void setReverseLayout(boolean r2) {
        assertNotInLayoutOrScroll(null);
        if (this.mPendingSavedState != null) goto L5;
    L7:
        this.mReverseLayout = r2;
        requestLayout();
        return;
    L5:
        if (this.mPendingSavedState.mReverseLayout == r2) goto L7;
        this.mPendingSavedState.mReverseLayout = r2;
        goto L7
    }

    public void setSpanCount(int r4) {
        assertNotInLayoutOrScroll(null);
        if (r4 == this.mSpanCount) goto L11;
        invalidateSpanAssignments();
        this.mSpanCount = r4;
        this.mRemainingSpans = new BitSet(this.mSpanCount);
        this.mSpans = new Span[this.mSpanCount];
        int r0 = 0;
    L6:
        if (r0 >= this.mSpanCount) goto L8;
        this.mSpans[r0] = new Span(this, r0);
        r0 = r0 + 1;
        goto L6
    L8:
        requestLayout();
        return;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void smoothScrollToPosition(RecyclerView r3, RecyclerView.State r4, int r5) {
        LinearSmoothScroller r0 = new LinearSmoothScroller(r3.getContext());
        r0.setTargetPosition(r5);
        startSmoothScroll(r0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean supportsPredictiveItemAnimations() {
        if (this.mPendingSavedState != null) goto L5;
        return true;
    L5:
        return false;
    }

    boolean updateAnchorFromPendingData(RecyclerView.State r8, AnchorInfo r9) {
        boolean r1 = false;
        if (r8.isPreLayout() == false) goto L5;
    L62:
        return false;
    L5:
        if (this.mPendingScrollPosition == (-1)) goto L62;
        if (this.mPendingScrollPosition >= 0) goto L10;
    L60:
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        return false;
    L10:
        if (this.mPendingScrollPosition >= r8.getItemCount()) goto L60;
        if (this.mPendingSavedState != null) goto L15;
    L20:
        View r0 = findViewByPosition(this.mPendingScrollPosition);
        if (r0 != null) goto L23;
        r9.mPosition = this.mPendingScrollPosition;
        if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) goto L53;
        r9.assignCoordinateFromPadding(this.mPendingScrollPositionOffset);
    L57:
        r9.mInvalidateOffsets = true;
    L59:
        return true;
    L53:
        if (calculateScrollDirectionForPosition(r9.mPosition) != 1) goto L55;
        r1 = true;
    L55:
        r9.mLayoutFromEnd = r1;
        r9.assignCoordinateFromPadding();
        goto L57
    L23:
        if (this.mShouldReverseLayout == false) goto L25;
        int r2 = getLastChildPosition();
    L26:
        r9.mPosition = r2;
        if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) goto L34;
        if (r9.mLayoutFromEnd == false) goto L31;
        r9.mOffset = (this.mPrimaryOrientation.getEndAfterPadding() - this.mPendingScrollPositionOffset) - this.mPrimaryOrientation.getDecoratedEnd(r0);
    L32:
        return true;
    L31:
        r9.mOffset = (this.mPrimaryOrientation.getStartAfterPadding() + this.mPendingScrollPositionOffset) - this.mPrimaryOrientation.getDecoratedStart(r0);
        goto L32
    L34:
        if (this.mPrimaryOrientation.getDecoratedMeasurement(r0) > this.mPrimaryOrientation.getTotalSpace()) goto L36;
        int r3 = this.mPrimaryOrientation.getDecoratedStart(r0) - this.mPrimaryOrientation.getStartAfterPadding();
        if (r3 >= 0) goto L45;
        r9.mOffset = -r3;
        return true;
    L45:
        int r5 = this.mPrimaryOrientation.getEndAfterPadding() - this.mPrimaryOrientation.getDecoratedEnd(r0);
        if (r5 >= 0) goto L49;
        r9.mOffset = r5;
        return true;
    L49:
        r9.mOffset = Integer.MIN_VALUE;
        goto L59
    L36:
        if (r9.mLayoutFromEnd == false) goto L38;
        int r4 = this.mPrimaryOrientation.getEndAfterPadding();
    L39:
        r9.mOffset = r4;
        return true;
    L38:
        r4 = this.mPrimaryOrientation.getStartAfterPadding();
        goto L39
    L25:
        r2 = getFirstChildPosition();
        goto L26
    L15:
        if (this.mPendingSavedState.mAnchorPosition == (-1)) goto L20;
        if (this.mPendingSavedState.mSpanOffsetsSize < 1) goto L20;
        r9.mOffset = Integer.MIN_VALUE;
        r9.mPosition = this.mPendingScrollPosition;
        goto L59
    }

    void updateAnchorInfoForLayout(RecyclerView.State r2, AnchorInfo r3) {
        if (updateAnchorFromPendingData(r2, r3) == false) goto L6;
        return;
    L6:
        if (updateAnchorFromChildren(r2, r3) == false) goto L8;
        return;
    L8:
        r3.assignCoordinateFromPadding();
        r3.mPosition = 0;
    }

    void updateMeasureSpecs(int r2) {
        this.mSizePerSpan = r2 / this.mSpanCount;
        this.mFullSizeSpec = View.MeasureSpec.makeMeasureSpec(r2, this.mSecondaryOrientation.getMode());
    }
}
