package androidx.recyclerview.widget;

import android.util.Log;
import android.util.Pair;
import android.view.ViewGroup;
import androidx.core.util.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class ConcatAdapterController implements NestedAdapterWrapper.Callback {
    private List<WeakReference<RecyclerView>> mAttachedRecyclerViews;
    private final IdentityHashMap<RecyclerView.ViewHolder, NestedAdapterWrapper> mBinderLookup;
    private final ConcatAdapter mConcatAdapter;
    private WrapperAndLocalPosition mReusableHolder;
    private final ConcatAdapter.Config.StableIdMode mStableIdMode;
    private final StableIdStorage mStableIdStorage;
    private final ViewTypeStorage mViewTypeStorage;
    private List<NestedAdapterWrapper> mWrappers;

    static class WrapperAndLocalPosition {
        boolean mInUse;
        int mLocalPosition;
        NestedAdapterWrapper mWrapper;

        WrapperAndLocalPosition() {
        }
    }

    ConcatAdapterController(ConcatAdapter r3, ConcatAdapter.Config r4) {
        this.mAttachedRecyclerViews = new ArrayList();
        this.mBinderLookup = new IdentityHashMap();
        this.mWrappers = new ArrayList();
        this.mReusableHolder = new WrapperAndLocalPosition();
        this.mConcatAdapter = r3;
        if (r4.isolateViewTypes == false) goto L5;
        this.mViewTypeStorage = new ViewTypeStorage.IsolatedViewTypeStorage();
    L6:
        this.mStableIdMode = r4.stableIdMode;
        if (r4.stableIdMode != ConcatAdapter.Config.StableIdMode.NO_STABLE_IDS) goto L10;
        this.mStableIdStorage = new StableIdStorage.NoStableIdStorage();
        return;
    L10:
        if (r4.stableIdMode != ConcatAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS) goto L13;
        this.mStableIdStorage = new StableIdStorage.IsolatedStableIdStorage();
        return;
    L13:
        if (r4.stableIdMode != ConcatAdapter.Config.StableIdMode.SHARED_STABLE_IDS) goto L17;
        this.mStableIdStorage = new StableIdStorage.SharedPoolStableIdStorage();
        return;
    L17:
        throw new IllegalArgumentException("unknown stable id mode");
    L5:
        this.mViewTypeStorage = new ViewTypeStorage.SharedIdRangeViewTypeStorage();
        goto L6
    }

    private void calculateAndUpdateStateRestorationPolicy() {
        RecyclerView.Adapter.StateRestorationPolicy r0 = computeStateRestorationPolicy();
        if (r0 == this.mConcatAdapter.getStateRestorationPolicy()) goto L6;
        this.mConcatAdapter.internalSetStateRestorationPolicy(r0);
        return;
    }

    private RecyclerView.Adapter.StateRestorationPolicy computeStateRestorationPolicy() {
        Iterator<NestedAdapterWrapper> r0 = this.mWrappers.iterator();
    L4:
        if (r0.hasNext() == false) goto L17;
        NestedAdapterWrapper r1 = r0.next();
        RecyclerView.Adapter.StateRestorationPolicy r2 = r1.adapter.getStateRestorationPolicy();
        if (r2 == RecyclerView.Adapter.StateRestorationPolicy.PREVENT) goto L8;
        if (r2 != RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY) goto L4;
        if (r1.getCachedItemCount() != 0) goto L4;
        return RecyclerView.Adapter.StateRestorationPolicy.PREVENT;
    L8:
        return RecyclerView.Adapter.StateRestorationPolicy.PREVENT;
    L17:
        return RecyclerView.Adapter.StateRestorationPolicy.ALLOW;
    }

    private int countItemsBefore(NestedAdapterWrapper r5) {
        int r0 = 0;
        Iterator<NestedAdapterWrapper> r1 = this.mWrappers.iterator();
    L4:
        if (r1.hasNext() == false) goto L8;
        NestedAdapterWrapper r2 = r1.next();
        if (r2 == r5) goto L8;
        r0 = r0 + r2.getCachedItemCount();
    L8:
        return r0;
    }

    private WrapperAndLocalPosition findWrapperAndLocalPosition(int r6) {
        if (this.mReusableHolder.mInUse == false) goto L5;
        WrapperAndLocalPosition r0 = new WrapperAndLocalPosition();
    L6:
        int r1 = r6;
        Iterator<NestedAdapterWrapper> r2 = this.mWrappers.iterator();
    L8:
        if (r2.hasNext() == false) goto L14;
        NestedAdapterWrapper r3 = r2.next();
        if (r3.getCachedItemCount() > r1) goto L11;
        r1 = r1 - r3.getCachedItemCount();
        goto L8
    L11:
        r0.mWrapper = r3;
        r0.mLocalPosition = r1;
    L14:
        if (r0.mWrapper == null) goto L17;
        return r0;
    L17:
        throw new IllegalArgumentException("Cannot find wrapper for " + r6);
    L5:
        this.mReusableHolder.mInUse = true;
        r0 = this.mReusableHolder;
        goto L6
    }

    private NestedAdapterWrapper findWrapperFor(RecyclerView.Adapter<RecyclerView.ViewHolder> r3) {
        int r0 = indexOfWrapper(r3);
        if (r0 != (-1)) goto L7;
        return null;
    L7:
        return this.mWrappers.get(r0);
    }

    private NestedAdapterWrapper getWrapper(RecyclerView.ViewHolder r5) {
        NestedAdapterWrapper r0 = this.mBinderLookup.get(r5);
        if (r0 == null) goto L6;
        return r0;
    L6:
        throw new IllegalStateException("Cannot find wrapper for " + r5 + ", seems like it is not bound by this adapter: " + this);
    }

    private int indexOfWrapper(RecyclerView.Adapter<RecyclerView.ViewHolder> r4) {
        int r0 = this.mWrappers.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L8;
        if (this.mWrappers.get(r1).adapter == r4) goto L6;
        r1 = r1 + 1;
        goto L3
    L6:
        return r1;
    L8:
        return -1;
    }

    private boolean isAttachedTo(RecyclerView r4) {
        Iterator<WeakReference<RecyclerView>> r0 = this.mAttachedRecyclerViews.iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (r0.next().get() != r4) goto L4;
        return true;
    L10:
        return false;
    }

    private void releaseWrapperAndLocalPosition(WrapperAndLocalPosition r2) {
        r2.mInUse = false;
        r2.mWrapper = null;
        r2.mLocalPosition = -1;
        this.mReusableHolder = r2;
    }

    boolean addAdapter(int r6, RecyclerView.Adapter<RecyclerView.ViewHolder> r7) {
        if (r6 < 0) goto L28;
        if (r6 > this.mWrappers.size()) goto L28;
        if (hasStableIds() == false) goto L9;
        Preconditions.checkArgument(r7.hasStableIds(), "All sub adapters must have stable ids when stable id mode is ISOLATED_STABLE_IDS or SHARED_STABLE_IDS");
    L12:
        if (findWrapperFor(r7) == null) goto L15;
        return false;
    L15:
        NestedAdapterWrapper r1 = new NestedAdapterWrapper(r7, this, this.mViewTypeStorage, this.mStableIdStorage.createStableIdLookup());
        this.mWrappers.add(r6, r1);
        Iterator<WeakReference<RecyclerView>> r2 = this.mAttachedRecyclerViews.iterator();
    L17:
        if (r2.hasNext() == false) goto L23;
        RecyclerView r4 = r2.next().get();
        if (r4 == null) goto L17;
        r7.onAttachedToRecyclerView(r4);
        goto L17
    L23:
        if (r1.getCachedItemCount() <= 0) goto L25;
        this.mConcatAdapter.notifyItemRangeInserted(countItemsBefore(r1), r1.getCachedItemCount());
    L25:
        calculateAndUpdateStateRestorationPolicy();
        return true;
    L9:
        if (r7.hasStableIds() == false) goto L12;
        Log.w("ConcatAdapter", "Stable ids in the adapter will be ignored as the ConcatAdapter is configured not to have stable ids");
    L28:
        throw new IndexOutOfBoundsException("Index must be between 0 and " + this.mWrappers.size() + ". Given:" + r6);
    }

    boolean addAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> r2) {
        return addAdapter(this.mWrappers.size(), r2);
    }

    public boolean canRestoreState() {
        Iterator<NestedAdapterWrapper> r0 = this.mWrappers.iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (r0.next().adapter.canRestoreState() == true) goto L4;
        return false;
    L10:
        return true;
    }

    public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getBoundAdapter(RecyclerView.ViewHolder r3) {
        NestedAdapterWrapper r0 = this.mBinderLookup.get(r3);
        if (r0 != null) goto L7;
        return null;
    L7:
        return r0.adapter;
    }

    public List<RecyclerView.Adapter<? extends RecyclerView.ViewHolder>> getCopyOfAdapters() {
        if (this.mWrappers.isEmpty() == true) goto L5;
        ArrayList r0 = new ArrayList(this.mWrappers.size());
        Iterator<NestedAdapterWrapper> r1 = this.mWrappers.iterator();
    L8:
        if (r1.hasNext() == false) goto L10;
        r0.add(r1.next().adapter);
        goto L8
    L10:
        return r0;
    L5:
        return Collections.emptyList();
    }

    public long getItemId(int r4) {
        WrapperAndLocalPosition r0 = findWrapperAndLocalPosition(r4);
        long r1 = r0.mWrapper.getItemId(r0.mLocalPosition);
        releaseWrapperAndLocalPosition(r0);
        return r1;
    }

    public int getItemViewType(int r4) {
        WrapperAndLocalPosition r0 = findWrapperAndLocalPosition(r4);
        int r1 = r0.mWrapper.getItemViewType(r0.mLocalPosition);
        releaseWrapperAndLocalPosition(r0);
        return r1;
    }

    public int getLocalAdapterPosition(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> r8, RecyclerView.ViewHolder r9, int r10) {
        NestedAdapterWrapper r0 = this.mBinderLookup.get(r9);
        if (r0 != null) goto L6;
        return -1;
    L6:
        int r2 = r10 - countItemsBefore(r0);
        int r3 = r0.adapter.getItemCount();
        if (r2 < 0) goto L12;
        if (r2 >= r3) goto L12;
        return r0.adapter.findRelativeAdapterPositionIn(r8, r9, r2);
    L12:
        throw new IllegalStateException("Detected inconsistent adapter updates. The local position of the view holder maps to " + r2 + " which is out of bounds for the adapter with size " + r3 + ".Make sure to immediately call notify methods in your adapter when you change the backing dataviewHolder:" + r9 + "adapter:" + r8);
    }

    public int getTotalCount() {
        int r0 = 0;
        Iterator<NestedAdapterWrapper> r1 = this.mWrappers.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0 = r0 + r1.next().getCachedItemCount();
        goto L4
    L6:
        return r0;
    }

    public Pair<RecyclerView.Adapter<? extends RecyclerView.ViewHolder>, Integer> getWrappedAdapterAndPosition(int r5) {
        WrapperAndLocalPosition r0 = findWrapperAndLocalPosition(r5);
        Pair<RecyclerView.Adapter<? extends RecyclerView.ViewHolder>, Integer> r1 = new Pair(r0.mWrapper.adapter, Integer.valueOf(r0.mLocalPosition));
        releaseWrapperAndLocalPosition(r0);
        return r1;
    }

    public boolean hasStableIds() {
        if (this.mStableIdMode == ConcatAdapter.Config.StableIdMode.NO_STABLE_IDS) goto L5;
        return true;
    L5:
        return false;
    }

    public void onAttachedToRecyclerView(RecyclerView r4) {
        if (isAttachedTo(r4) == false) goto L5;
        return;
    L5:
        this.mAttachedRecyclerViews.add(new WeakReference(r4));
        Iterator<NestedAdapterWrapper> r0 = this.mWrappers.iterator();
    L7:
        if (r0.hasNext() == false) goto L9;
        r0.next().adapter.onAttachedToRecyclerView(r4);
        goto L7
    }

    public void onBindViewHolder(RecyclerView.ViewHolder r4, int r5) {
        WrapperAndLocalPosition r0 = findWrapperAndLocalPosition(r5);
        this.mBinderLookup.put(r4, r0.mWrapper);
        r0.mWrapper.onBindViewHolder(r4, r0.mLocalPosition);
        releaseWrapperAndLocalPosition(r0);
    }

    @Override // androidx.recyclerview.widget.NestedAdapterWrapper.Callback
    public void onChanged(NestedAdapterWrapper r2) {
        this.mConcatAdapter.notifyDataSetChanged();
        calculateAndUpdateStateRestorationPolicy();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup r3, int r4) {
        return this.mViewTypeStorage.getWrapperForGlobalType(r4).onCreateViewHolder(r3, r4);
    }

    public void onDetachedFromRecyclerView(RecyclerView r4) {
        int r0 = this.mAttachedRecyclerViews.size() - 1;
    L3:
        if (r0 < 0) goto L11;
        WeakReference<RecyclerView> r1 = this.mAttachedRecyclerViews.get(r0);
        if (r1.get() != null) goto L8;
        this.mAttachedRecyclerViews.remove(r0);
    L10:
        r0 = r0 - 1;
        goto L3
    L8:
        if (r1.get() != r4) goto L10;
        this.mAttachedRecyclerViews.remove(r0);
    L11:
        Iterator<NestedAdapterWrapper> r2 = this.mWrappers.iterator();
    L13:
        if (r2.hasNext() == false) goto L15;
        r2.next().adapter.onDetachedFromRecyclerView(r4);
        goto L13
    }

    public boolean onFailedToRecycleView(RecyclerView.ViewHolder r5) {
        NestedAdapterWrapper r0 = this.mBinderLookup.get(r5);
        if (r0 == null) goto L7;
        boolean r1 = r0.adapter.onFailedToRecycleView(r5);
        this.mBinderLookup.remove(r5);
        return r1;
    L7:
        throw new IllegalStateException("Cannot find wrapper for " + r5 + ", seems like it is not bound by this adapter: " + this);
    }

    @Override // androidx.recyclerview.widget.NestedAdapterWrapper.Callback
    public void onItemRangeChanged(NestedAdapterWrapper r4, int r5, int r6) {
        int r0 = countItemsBefore(r4);
        this.mConcatAdapter.notifyItemRangeChanged(r5 + r0, r6);
    }

    @Override // androidx.recyclerview.widget.NestedAdapterWrapper.Callback
    public void onItemRangeChanged(NestedAdapterWrapper r4, int r5, int r6, Object r7) {
        int r0 = countItemsBefore(r4);
        this.mConcatAdapter.notifyItemRangeChanged(r5 + r0, r6, r7);
    }

    @Override // androidx.recyclerview.widget.NestedAdapterWrapper.Callback
    public void onItemRangeInserted(NestedAdapterWrapper r4, int r5, int r6) {
        int r0 = countItemsBefore(r4);
        this.mConcatAdapter.notifyItemRangeInserted(r5 + r0, r6);
    }

    @Override // androidx.recyclerview.widget.NestedAdapterWrapper.Callback
    public void onItemRangeMoved(NestedAdapterWrapper r5, int r6, int r7) {
        int r0 = countItemsBefore(r5);
        this.mConcatAdapter.notifyItemMoved(r6 + r0, r7 + r0);
    }

    @Override // androidx.recyclerview.widget.NestedAdapterWrapper.Callback
    public void onItemRangeRemoved(NestedAdapterWrapper r4, int r5, int r6) {
        int r0 = countItemsBefore(r4);
        this.mConcatAdapter.notifyItemRangeRemoved(r5 + r0, r6);
    }

    @Override // androidx.recyclerview.widget.NestedAdapterWrapper.Callback
    public void onStateRestorationPolicyChanged(NestedAdapterWrapper r1) {
        calculateAndUpdateStateRestorationPolicy();
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder r3) {
        getWrapper(r3).adapter.onViewAttachedToWindow(r3);
    }

    public void onViewDetachedFromWindow(RecyclerView.ViewHolder r3) {
        getWrapper(r3).adapter.onViewDetachedFromWindow(r3);
    }

    public void onViewRecycled(RecyclerView.ViewHolder r5) {
        NestedAdapterWrapper r0 = this.mBinderLookup.get(r5);
        if (r0 == null) goto L7;
        r0.adapter.onViewRecycled(r5);
        this.mBinderLookup.remove(r5);
        return;
    L7:
        throw new IllegalStateException("Cannot find wrapper for " + r5 + ", seems like it is not bound by this adapter: " + this);
    }

    boolean removeAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> r7) {
        int r0 = indexOfWrapper(r7);
        if (r0 != (-1)) goto L6;
        return false;
    L6:
        NestedAdapterWrapper r1 = this.mWrappers.get(r0);
        int r2 = countItemsBefore(r1);
        this.mWrappers.remove(r0);
        this.mConcatAdapter.notifyItemRangeRemoved(r2, r1.getCachedItemCount());
        Iterator<WeakReference<RecyclerView>> r3 = this.mAttachedRecyclerViews.iterator();
    L8:
        if (r3.hasNext() == false) goto L13;
        RecyclerView r5 = r3.next().get();
        if (r5 == null) goto L8;
        r7.onDetachedFromRecyclerView(r5);
        goto L8
    L13:
        r1.dispose();
        calculateAndUpdateStateRestorationPolicy();
        return true;
    }
}
