package androidx.recyclerview.widget;

import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;
import androidx.core.util.Pools;

/* JADX INFO: loaded from: classes.dex */
class ViewInfoStore {
    private static final boolean DEBUG = false;
    final SimpleArrayMap<RecyclerView.ViewHolder, InfoRecord> mLayoutHolderMap;
    final LongSparseArray<RecyclerView.ViewHolder> mOldChangedHolders;

    static class InfoRecord {
        static final int FLAG_APPEAR = 2;
        static final int FLAG_APPEAR_AND_DISAPPEAR = 3;
        static final int FLAG_APPEAR_PRE_AND_POST = 14;
        static final int FLAG_DISAPPEARED = 1;
        static final int FLAG_POST = 8;
        static final int FLAG_PRE = 4;
        static final int FLAG_PRE_AND_POST = 12;
        static Pools.Pool<InfoRecord> sPool;
        int flags;
        RecyclerView.ItemAnimator.ItemHolderInfo postInfo;
        RecyclerView.ItemAnimator.ItemHolderInfo preInfo;

        static {
            sPool = new Pools.SimplePool(20);
        }

        private InfoRecord() {
        }

        static void drainCache() {
        L3:
            if (sPool.acquire() != null) goto L3;
        }

        static InfoRecord obtain() {
            InfoRecord r0 = sPool.acquire();
            if (r0 != null) goto L6;
            return new InfoRecord();
        L6:
            return r0;
        }

        static void recycle(InfoRecord r1) {
            r1.flags = 0;
            r1.preInfo = null;
            r1.postInfo = null;
            sPool.release(r1);
        }
    }

    interface ProcessCallback {
        void processAppeared(RecyclerView.ViewHolder r1, RecyclerView.ItemAnimator.ItemHolderInfo r2, RecyclerView.ItemAnimator.ItemHolderInfo r3);

        void processDisappeared(RecyclerView.ViewHolder r1, RecyclerView.ItemAnimator.ItemHolderInfo r2, RecyclerView.ItemAnimator.ItemHolderInfo r3);

        void processPersistent(RecyclerView.ViewHolder r1, RecyclerView.ItemAnimator.ItemHolderInfo r2, RecyclerView.ItemAnimator.ItemHolderInfo r3);

        void unused(RecyclerView.ViewHolder r1);
    }

    ViewInfoStore() {
        this.mLayoutHolderMap = new SimpleArrayMap();
        this.mOldChangedHolders = new LongSparseArray();
    }

    private RecyclerView.ItemAnimator.ItemHolderInfo popFromLayoutStep(RecyclerView.ViewHolder r5, int r6) {
        int r0 = this.mLayoutHolderMap.indexOfKey(r5);
        if (r0 >= 0) goto L5;
        return null;
    L5:
        InfoRecord r2 = this.mLayoutHolderMap.valueAt(r0);
        if (r2 != null) goto L8;
    L21:
        return null;
    L8:
        if ((r2.flags & r6) == 0) goto L21;
        r2.flags &= ~r6;
        if (r6 != 4) goto L13;
        RecyclerView.ItemAnimator.ItemHolderInfo r1 = r2.preInfo;
    L16:
        if ((r2.flags & 12) != 0) goto L18;
        this.mLayoutHolderMap.removeAt(r0);
        InfoRecord.recycle(r2);
    L18:
        return r1;
    L13:
        if (r6 != 8) goto L20;
        r1 = r2.postInfo;
        goto L16
    L20:
        throw new IllegalArgumentException("Must provide flag PRE or POST");
    }

    void addToAppearedInPreLayoutHolders(RecyclerView.ViewHolder r3, RecyclerView.ItemAnimator.ItemHolderInfo r4) {
        InfoRecord r0 = this.mLayoutHolderMap.get(r3);
        if (r0 != null) goto L5;
        r0 = InfoRecord.obtain();
        this.mLayoutHolderMap.put(r3, r0);
    L5:
        r0.flags |= 2;
        r0.preInfo = r4;
    }

    void addToDisappearedInLayout(RecyclerView.ViewHolder r3) {
        InfoRecord r0 = this.mLayoutHolderMap.get(r3);
        if (r0 != null) goto L5;
        r0 = InfoRecord.obtain();
        this.mLayoutHolderMap.put(r3, r0);
    L5:
        r0.flags |= 1;
    }

    void addToOldChangeHolders(long r2, RecyclerView.ViewHolder r4) {
        this.mOldChangedHolders.put(r2, r4);
    }

    void addToPostLayout(RecyclerView.ViewHolder r3, RecyclerView.ItemAnimator.ItemHolderInfo r4) {
        InfoRecord r0 = this.mLayoutHolderMap.get(r3);
        if (r0 != null) goto L5;
        r0 = InfoRecord.obtain();
        this.mLayoutHolderMap.put(r3, r0);
    L5:
        r0.postInfo = r4;
        r0.flags |= 8;
    }

    void addToPreLayout(RecyclerView.ViewHolder r3, RecyclerView.ItemAnimator.ItemHolderInfo r4) {
        InfoRecord r0 = this.mLayoutHolderMap.get(r3);
        if (r0 != null) goto L5;
        r0 = InfoRecord.obtain();
        this.mLayoutHolderMap.put(r3, r0);
    L5:
        r0.preInfo = r4;
        r0.flags |= 4;
    }

    void clear() {
        this.mLayoutHolderMap.clear();
        this.mOldChangedHolders.clear();
    }

    RecyclerView.ViewHolder getFromOldChangeHolders(long r2) {
        return this.mOldChangedHolders.get(r2);
    }

    boolean isDisappearing(RecyclerView.ViewHolder r4) {
        InfoRecord r0 = this.mLayoutHolderMap.get(r4);
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if ((r0.flags & 1) == 0) goto L7;
        return true;
    }

    boolean isInPreLayout(RecyclerView.ViewHolder r3) {
        InfoRecord r0 = this.mLayoutHolderMap.get(r3);
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if ((r0.flags & 4) == 0) goto L7;
        return true;
    }

    void onDetach() {
        InfoRecord.drainCache();
    }

    public void onViewDetached(RecyclerView.ViewHolder r1) {
        removeFromDisappearedInLayout(r1);
    }

    RecyclerView.ItemAnimator.ItemHolderInfo popFromPostLayout(RecyclerView.ViewHolder r2) {
        return popFromLayoutStep(r2, 8);
    }

    RecyclerView.ItemAnimator.ItemHolderInfo popFromPreLayout(RecyclerView.ViewHolder r2) {
        return popFromLayoutStep(r2, 4);
    }

    void process(ProcessCallback r6) {
        int r0 = this.mLayoutHolderMap.size() - 1;
    L3:
        if (r0 < 0) goto L27;
        RecyclerView.ViewHolder r1 = this.mLayoutHolderMap.keyAt(r0);
        InfoRecord r2 = this.mLayoutHolderMap.removeAt(r0);
        if ((r2.flags & 3) != 3) goto L8;
        r6.unused(r1);
    L26:
        InfoRecord.recycle(r2);
        r0 = r0 - 1;
        goto L3
    L8:
        if ((r2.flags & 1) == 0) goto L14;
        if (r2.preInfo != null) goto L12;
        r6.unused(r1);
        goto L26
    L12:
        r6.processDisappeared(r1, r2.preInfo, r2.postInfo);
        goto L26
    L14:
        if ((r2.flags & 14) != 14) goto L17;
        r6.processAppeared(r1, r2.preInfo, r2.postInfo);
        goto L26
    L17:
        if ((r2.flags & 12) != 12) goto L20;
        r6.processPersistent(r1, r2.preInfo, r2.postInfo);
        goto L26
    L20:
        if ((r2.flags & 4) == 0) goto L23;
        r6.processDisappeared(r1, r2.preInfo, null);
        goto L26
    L23:
        if ((r2.flags & 8) == 0) goto L25;
        r6.processAppeared(r1, r2.preInfo, r2.postInfo);
        goto L26
    L25:
        int r3 = r2.flags;
        goto L26
    }

    void removeFromDisappearedInLayout(RecyclerView.ViewHolder r3) {
        InfoRecord r0 = this.mLayoutHolderMap.get(r3);
        if (r0 != null) goto L5;
        return;
    L5:
        r0.flags &= -2;
    }

    void removeViewHolder(RecyclerView.ViewHolder r3) {
        int r0 = this.mOldChangedHolders.size() - 1;
    L3:
        if (r0 < 0) goto L8;
        if (r3 == this.mOldChangedHolders.valueAt(r0)) goto L6;
        r0 = r0 - 1;
        goto L3
    L6:
        this.mOldChangedHolders.removeAt(r0);
    L8:
        InfoRecord r1 = this.mLayoutHolderMap.remove(r3);
        if (r1 == null) goto L14;
        InfoRecord.recycle(r1);
        return;
    }
}
