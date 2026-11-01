package androidx.recyclerview.widget;

import android.view.ViewGroup;

/* JADX INFO: loaded from: classes.dex */
class NestedAdapterWrapper {
    public final RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private RecyclerView.AdapterDataObserver mAdapterObserver;
    int mCachedItemCount;
    final Callback mCallback;
    private final StableIdStorage.StableIdLookup mStableIdLookup;
    private final ViewTypeStorage.ViewTypeLookup mViewTypeLookup;

    interface Callback {
        void onChanged(NestedAdapterWrapper r1);

        void onItemRangeChanged(NestedAdapterWrapper r1, int r2, int r3);

        void onItemRangeChanged(NestedAdapterWrapper r1, int r2, int r3, Object r4);

        void onItemRangeInserted(NestedAdapterWrapper r1, int r2, int r3);

        void onItemRangeMoved(NestedAdapterWrapper r1, int r2, int r3);

        void onItemRangeRemoved(NestedAdapterWrapper r1, int r2, int r3);

        void onStateRestorationPolicyChanged(NestedAdapterWrapper r1);
    }

    NestedAdapterWrapper(RecyclerView.Adapter<RecyclerView.ViewHolder> r3, Callback r4, ViewTypeStorage r5, StableIdStorage.StableIdLookup r6) {
        this.mAdapterObserver = new C05001(this);
        this.adapter = r3;
        this.mCallback = r4;
        this.mViewTypeLookup = r5.createViewTypeWrapper(this);
        this.mStableIdLookup = r6;
        this.mCachedItemCount = this.adapter.getItemCount();
        this.adapter.registerAdapterDataObserver(this.mAdapterObserver);
    }

    void dispose() {
        this.adapter.unregisterAdapterDataObserver(this.mAdapterObserver);
        this.mViewTypeLookup.dispose();
    }

    int getCachedItemCount() {
        return this.mCachedItemCount;
    }

    public long getItemId(int r5) {
        long r0 = this.adapter.getItemId(r5);
        return this.mStableIdLookup.localToGlobal(r0);
    }

    int getItemViewType(int r3) {
        return this.mViewTypeLookup.localToGlobal(this.adapter.getItemViewType(r3));
    }

    void onBindViewHolder(RecyclerView.ViewHolder r2, int r3) {
        this.adapter.bindViewHolder(r2, r3);
    }

    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup r3, int r4) {
        int r0 = this.mViewTypeLookup.globalToLocal(r4);
        return this.adapter.onCreateViewHolder(r3, r0);
    }
}
