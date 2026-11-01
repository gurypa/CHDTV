package androidx.recyclerview.widget;

import androidx.core.os.TraceCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
final class GapWorker implements Runnable {
    static final ThreadLocal<GapWorker> sGapWorker = null;
    static Comparator<Task> sTaskComparator;
    long mFrameIntervalNs;
    long mPostTimeNs;
    ArrayList<RecyclerView> mRecyclerViews;
    private ArrayList<Task> mTasks;

    static class LayoutPrefetchRegistryImpl implements RecyclerView.LayoutManager.LayoutPrefetchRegistry {
        int mCount;
        int[] mPrefetchArray;
        int mPrefetchDx;
        int mPrefetchDy;

        LayoutPrefetchRegistryImpl() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager.LayoutPrefetchRegistry
        public void addPosition(int r6, int r7) {
            if (r6 < 0) goto L15;
            if (r7 < 0) goto L13;
            int r0 = this.mCount * 2;
            if (this.mPrefetchArray != null) goto L8;
            this.mPrefetchArray = new int[4];
            Arrays.fill(this.mPrefetchArray, -1);
        L10:
            this.mPrefetchArray[r0] = r6;
            this.mPrefetchArray[r0 + 1] = r7;
            this.mCount++;
            return;
        L8:
            if (r0 < this.mPrefetchArray.length) goto L10;
            int[] r1 = this.mPrefetchArray;
            this.mPrefetchArray = new int[r0 * 2];
            System.arraycopy(r1, 0, this.mPrefetchArray, 0, r1.length);
            goto L10
        L13:
            throw new IllegalArgumentException("Pixel distance must be non-negative");
        L15:
            throw new IllegalArgumentException("Layout positions must be non-negative");
        }

        void clearPrefetchPositions() {
            if (this.mPrefetchArray == null) goto L5;
            Arrays.fill(this.mPrefetchArray, -1);
        L5:
            this.mCount = 0;
        }

        void collectPrefetchPositionsFromView(RecyclerView r5, boolean r6) {
            this.mCount = 0;
            if (this.mPrefetchArray == null) goto L5;
            Arrays.fill(this.mPrefetchArray, -1);
        L5:
            RecyclerView.LayoutManager r0 = r5.mLayout;
            if (r5.mAdapter == null) goto L21;
            if (r0 != null) goto L9;
            return;
        L9:
            if (r0.isItemPrefetchEnabled() == false) goto L23;
            if (r6 == false) goto L15;
            if (r5.mAdapterHelper.hasPendingUpdates() == true) goto L18;
            r0.collectInitialPrefetchPositions(r5.mAdapter.getItemCount(), this);
        L18:
            if (this.mCount <= r0.mPrefetchMaxCountObserved) goto L24;
            r0.mPrefetchMaxCountObserved = this.mCount;
            r0.mPrefetchMaxObservedInInitialPrefetch = r6;
            r5.mRecycler.updateViewCacheSize();
            return;
        L24:
            return;
        L15:
            if (r5.hasPendingAdapterUpdates() == true) goto L18;
            r0.collectAdjacentPrefetchPositions(this.mPrefetchDx, this.mPrefetchDy, r5.mState, this);
            goto L18
        L23:
            return;
        }

        boolean lastPrefetchIncludedPosition(int r4) {
            if (this.mPrefetchArray == null) goto L11;
            int r0 = this.mCount * 2;
            int r1 = 0;
        L5:
            if (r1 >= r0) goto L15;
            if (this.mPrefetchArray[r1] == r4) goto L8;
            r1 = r1 + 2;
            goto L5
        L8:
            return true;
        L15:
            return false;
        L11:
            return false;
        }

        void setPrefetchVector(int r1, int r2) {
            this.mPrefetchDx = r1;
            this.mPrefetchDy = r2;
        }
    }

    static class Task {
        public int distanceToItem;
        public boolean immediate;
        public int position;
        public RecyclerView view;
        public int viewVelocity;

        Task() {
        }

        public void clear() {
            this.immediate = false;
            this.viewVelocity = 0;
            this.distanceToItem = 0;
            this.view = null;
            this.position = 0;
        }
    }

    static {
        sGapWorker = new ThreadLocal();
        sTaskComparator = new C04871();
    }

    GapWorker() {
        this.mRecyclerViews = new ArrayList();
        this.mTasks = new ArrayList();
    }

    private void buildTaskList() {
        int r0 = this.mRecyclerViews.size();
        int r1 = 0;
        int r2 = 0;
    L4:
        if (r2 >= r0) goto L9;
        RecyclerView r4 = this.mRecyclerViews.get(r2);
        if (r4.getWindowVisibility() != 0) goto L8;
        r4.mPrefetchRegistry.collectPrefetchPositionsFromView(r4, false);
        r1 = r1 + r4.mPrefetchRegistry.mCount;
    L8:
        r2 = r2 + 1;
        goto L4
    L9:
        this.mTasks.ensureCapacity(r1);
        int r3 = 0;
        int r5 = 0;
    L10:
        if (r5 >= r0) goto L27;
        RecyclerView r6 = this.mRecyclerViews.get(r5);
        if (r6.getWindowVisibility() != 0) goto L26;
        LayoutPrefetchRegistryImpl r7 = r6.mPrefetchRegistry;
        int r8 = Math.abs(r7.mPrefetchDx) + Math.abs(r7.mPrefetchDy);
        int r9 = 0;
    L16:
        if (r9 >= (r7.mCount * 2)) goto L26;
        if (r3 < this.mTasks.size()) goto L20;
        Task r10 = new Task();
        this.mTasks.add(r10);
    L21:
        int r11 = r7.mPrefetchArray[r9 + 1];
        if (r11 > r8) goto L24;
        boolean r12 = true;
    L25:
        r10.immediate = r12;
        r10.viewVelocity = r8;
        r10.distanceToItem = r11;
        r10.view = r6;
        r10.position = r7.mPrefetchArray[r9];
        r3 = r3 + 1;
        r9 = r9 + 2;
        goto L16
    L24:
        r12 = false;
        goto L25
    L20:
        r10 = this.mTasks.get(r3);
    L26:
        r5 = r5 + 1;
        goto L10
    L27:
        Collections.sort(this.mTasks, sTaskComparator);
    }

    private void flushTaskWithDeadline(Task r5, long r6) {
        if (r5.immediate == false) goto L5;
        long r0 = Long.MAX_VALUE;
    L6:
        RecyclerView.ViewHolder r2 = prefetchPositionWithDeadline(r5.view, r5.position, r0);
        if (r2 != null) goto L9;
        return;
    L9:
        if (r2.mNestedRecyclerView != null) goto L11;
        return;
    L11:
        if (r2.isBound() == true) goto L13;
        return;
    L13:
        if (r2.isInvalid() == true) goto L19;
        prefetchInnerRecyclerViewWithDeadline(r2.mNestedRecyclerView.get(), r6);
        return;
    L19:
        return;
    L5:
        r0 = r6;
        goto L6
    }

    private void flushTasksWithDeadline(long r4) {
        int r0 = 0;
    L4:
        if (r0 >= this.mTasks.size()) goto L9;
        Task r1 = this.mTasks.get(r0);
        if (r1.view == null) goto L12;
        flushTaskWithDeadline(r1, r4);
        r1.clear();
        r0 = r0 + 1;
        goto L4
    L12:
        return;
    }

    static boolean isPrefetchPositionAttached(RecyclerView r5, int r6) {
        int r0 = r5.mChildHelper.getUnfilteredChildCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L11;
        RecyclerView.ViewHolder r3 = RecyclerView.getChildViewHolderInt(r5.mChildHelper.getUnfilteredChildAt(r1));
        if (r3.mPosition != r6) goto L10;
        if (r3.isInvalid() == true) goto L10;
        return true;
    L10:
        r1 = r1 + 1;
        goto L3
    L11:
        return false;
    }

    private void prefetchInnerRecyclerViewWithDeadline(RecyclerView r4, long r5) {
        if (r4 != null) goto L5;
        return;
    L5:
        if (r4.mDataSetHasChangedAfterLayout == true) goto L7;
    L9:
        LayoutPrefetchRegistryImpl r0 = r4.mPrefetchRegistry;
        r0.collectPrefetchPositionsFromView(r4, true);
        if (r0.mCount != 0) goto L21;
        return;
    L21:
        TraceCompat.beginSection("RV Nested Prefetch");     // Catch: Throwable -> L17
        r4.mState.prepareForNestedPrefetch(r4.mAdapter);     // Catch: Throwable -> L17
        int r1 = 0;
    L13:
        if (r1 >= (r0.mCount * 2)) goto L16;
        prefetchPositionWithDeadline(r4, r0.mPrefetchArray[r1], r5);     // Catch: Throwable -> L17
        r1 = r1 + 2;
        goto L13
    L16:
        TraceCompat.endSection();
        return;
    L17:
        th = move-exception;
        TraceCompat.endSection();
        throw th;
    L7:
        if (r4.mChildHelper.getUnfilteredChildCount() == 0) goto L9;
        r4.removeAndRecycleViews();
        goto L9
    }

    private RecyclerView.ViewHolder prefetchPositionWithDeadline(RecyclerView r5, int r6, long r7) {
        if (isPrefetchPositionAttached(r5, r6) == false) goto L6;
        return null;
    L6:
        RecyclerView.Recycler r0 = r5.mRecycler;
        r5.onEnterLayoutOrScroll();     // Catch: Throwable -> L17
        RecyclerView.ViewHolder r2 = r0.tryGetViewHolderForPositionByDeadline(r6, false, r7);     // Catch: Throwable -> L17
        if (r2 != null) goto L10;
    L15:
        r5.onExitLayoutOrScroll(false);
        return r2;
    L10:
        if (r2.isBound() == true) goto L12;
    L14:
        r0.addViewHolderToRecycledViewPool(r2, false);     // Catch: Throwable -> L17
        goto L15
    L12:
        if (r2.isInvalid() == true) goto L14;
        r0.recycleView(r2.itemView);     // Catch: Throwable -> L17
    L17:
        th = move-exception;
        r5.onExitLayoutOrScroll(false);
        throw th;
    }

    public void add(RecyclerView r2) {
        this.mRecyclerViews.add(r2);
    }

    void postFromTraversal(RecyclerView r5, int r6, int r7) {
        if (r5.isAttachedToWindow() == true) goto L5;
    L7:
        r5.mPrefetchRegistry.setPrefetchVector(r6, r7);
        return;
    L5:
        if (this.mPostTimeNs != 0) goto L7;
        this.mPostTimeNs = r5.getNanoTime();
        r5.post(this);
        goto L7
    }

    void prefetch(long r1) {
        buildTaskList();
        flushTasksWithDeadline(r1);
    }

    public void remove(RecyclerView r2) {
        this.mRecyclerViews.remove(r2);
    }

    @Override // java.lang.Runnable
    public void run() {
        TraceCompat.beginSection("RV Prefetch");     // Catch: Throwable -> L21
        if (this.mRecyclerViews.isEmpty() == false) goto L7;
        this.mPostTimeNs = 0;
        TraceCompat.endSection();
        return;
    L7:
        int r2 = this.mRecyclerViews.size();     // Catch: Throwable -> L21
        long r3 = 0;
        int r5 = 0;
    L8:
        if (r5 >= r2) goto L15;
        RecyclerView r6 = this.mRecyclerViews.get(r5);     // Catch: Throwable -> L21
        if (r6.getWindowVisibility() != 0) goto L13;
        r3 = Math.max(r6.getDrawingTime(), r3);     // Catch: Throwable -> L21
    L13:
        r5 = r5 + 1;
        goto L8
    L15:
        if (r3 != 0) goto L18;
        this.mPostTimeNs = 0;
        TraceCompat.endSection();
        return;
    L18:
        prefetch(TimeUnit.MILLISECONDS.toNanos(r3) + this.mFrameIntervalNs);     // Catch: Throwable -> L21
        this.mPostTimeNs = 0;
        TraceCompat.endSection();
        return;
    L21:
        th = move-exception;
        this.mPostTimeNs = 0;
        TraceCompat.endSection();
        throw th;
    }
}
