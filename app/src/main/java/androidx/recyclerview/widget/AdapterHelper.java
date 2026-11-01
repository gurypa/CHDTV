package androidx.recyclerview.widget;

import androidx.core.util.Pools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class AdapterHelper implements OpReorderer.Callback {
    private static final boolean DEBUG = false;
    static final int POSITION_TYPE_INVISIBLE = 0;
    static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
    private static final String TAG = "AHT";
    final Callback mCallback;
    final boolean mDisableRecycler;
    private int mExistingUpdateTypes;
    Runnable mOnItemProcessedCallback;
    final OpReorderer mOpReorderer;
    final ArrayList<UpdateOp> mPendingUpdates;
    final ArrayList<UpdateOp> mPostponedList;
    private Pools.Pool<UpdateOp> mUpdateOpPool;

    interface Callback {
        RecyclerView.ViewHolder findViewHolder(int r1);

        void markViewHoldersUpdated(int r1, int r2, Object r3);

        void offsetPositionsForAdd(int r1, int r2);

        void offsetPositionsForMove(int r1, int r2);

        void offsetPositionsForRemovingInvisible(int r1, int r2);

        void offsetPositionsForRemovingLaidOutOrNewView(int r1, int r2);

        void onDispatchFirstPass(UpdateOp r1);

        void onDispatchSecondPass(UpdateOp r1);
    }

    static final class UpdateOp {
        static final int ADD = 1;
        static final int MOVE = 8;
        static final int POOL_SIZE = 30;
        static final int REMOVE = 2;
        static final int UPDATE = 4;
        int cmd;
        int itemCount;
        Object payload;
        int positionStart;

        UpdateOp(int r1, int r2, int r3, Object r4) {
            this.cmd = r1;
            this.positionStart = r2;
            this.itemCount = r3;
            this.payload = r4;
        }

        String cmdToString() {
            switch(this.cmd) {
                case 1: goto L12;
                case 2: goto L10;
                case 4: goto L8;
                case 8: goto L6;
                default: goto L4;
            };
        L4:
            return "??";
        L6:
            return "mv";
        L8:
            return "up";
        L10:
            return "rm";
        L12:
            return "add";
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof UpdateOp) == true) goto L8;
            return false;
        L8:
            UpdateOp r1 = (UpdateOp) r6;
            if (this.cmd == r1.cmd) goto L12;
            return false;
        L12:
            if (this.cmd != 8) goto L21;
            if (Math.abs(this.itemCount - this.positionStart) != 1) goto L21;
            if (this.itemCount != r1.positionStart) goto L21;
            if (this.positionStart != r1.itemCount) goto L21;
            return true;
        L21:
            if (this.itemCount == r1.itemCount) goto L24;
            return false;
        L24:
            if (this.positionStart == r1.positionStart) goto L27;
            return false;
        L27:
            if (this.payload == null) goto L32;
            if (this.payload.equals(r1.payload) == true) goto L34;
            return false;
        L34:
            return true;
        L32:
            if (r1.payload == null) goto L34;
            return false;
        }

        public int hashCode() {
            return (((this.cmd * 31) + this.positionStart) * 31) + this.itemCount;
        }

        public String toString() {
            return Integer.toHexString(System.identityHashCode(this)) + "[" + cmdToString() + ",s:" + this.positionStart + "c:" + this.itemCount + ",p:" + this.payload + "]";
        }
    }

    AdapterHelper(Callback r2) {
        this(r2, false);
    }

    AdapterHelper(Callback r3, boolean r4) {
        this.mUpdateOpPool = new Pools.SimplePool(30);
        this.mPendingUpdates = new ArrayList();
        this.mPostponedList = new ArrayList();
        this.mExistingUpdateTypes = 0;
        this.mCallback = r3;
        this.mDisableRecycler = r4;
        this.mOpReorderer = new OpReorderer(this);
    }

    private void applyAdd(UpdateOp r1) {
        postponeAndUpdateViewHolders(r1);
    }

    private void applyMove(UpdateOp r1) {
        postponeAndUpdateViewHolders(r1);
    }

    private void applyRemove(UpdateOp r12) {
        int r0 = r12.positionStart;
        int r1 = 0;
        int r2 = r12.positionStart + r12.itemCount;
        byte r3 = -1;
        int r4 = r12.positionStart;
    L4:
        if (r4 >= r2) goto L21;
        boolean r7 = false;
        if (this.mCallback.findViewHolder(r4) == null) goto L8;
    L13:
        if (r3 != 0) goto L15;
        dispatchAndUpdateViewHolders(obtainUpdateOp(2, r0, r1, null));
        r7 = true;
    L15:
        r3 = 1;
    L16:
        if (r7 == false) goto L18;
        r4 = r4 - r1;
        r2 = r2 - r1;
        r1 = 1;
    L19:
        r4 = r4 + 1;
        goto L4
    L18:
        r1 = r1 + 1;
        goto L19
    L8:
        if (canFindInPreLayout(r4) == true) goto L13;
        if (r3 != 1) goto L12;
        postponeAndUpdateViewHolders(obtainUpdateOp(2, r0, r1, null));
        r7 = true;
    L12:
        r3 = 0;
        goto L16
    L21:
        if (r1 == r12.itemCount) goto L23;
        recycleUpdateOp(r12);
        r12 = obtainUpdateOp(2, r0, r1, null);
    L23:
        if (r3 != 0) goto L25;
        dispatchAndUpdateViewHolders(r12);
        return;
    L25:
        postponeAndUpdateViewHolders(r12);
    }

    private void applyUpdate(UpdateOp r10) {
        int r0 = r10.positionStart;
        int r1 = 0;
        int r2 = r10.positionStart + r10.itemCount;
        byte r3 = -1;
        int r4 = r10.positionStart;
    L4:
        if (r4 >= r2) goto L18;
        if (this.mCallback.findViewHolder(r4) == null) goto L8;
    L13:
        if (r3 != 0) goto L15;
        dispatchAndUpdateViewHolders(obtainUpdateOp(4, r0, r1, r10.payload));
        r1 = 0;
        r0 = r4;
    L15:
        r3 = 1;
    L16:
        r1 = r1 + 1;
        r4 = r4 + 1;
        goto L4
    L8:
        if (canFindInPreLayout(r4) == true) goto L13;
        if (r3 != 1) goto L12;
        postponeAndUpdateViewHolders(obtainUpdateOp(4, r0, r1, r10.payload));
        r1 = 0;
        r0 = r4;
    L12:
        r3 = 0;
        goto L16
    L18:
        if (r1 == r10.itemCount) goto L20;
        Object r5 = r10.payload;
        recycleUpdateOp(r10);
        r10 = obtainUpdateOp(4, r0, r1, r5);
    L20:
        if (r3 != 0) goto L22;
        dispatchAndUpdateViewHolders(r10);
        return;
    L22:
        postponeAndUpdateViewHolders(r10);
    }

    private boolean canFindInPreLayout(int r8) {
        int r0 = this.mPostponedList.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L18;
        UpdateOp r2 = this.mPostponedList.get(r1);
        if (r2.cmd != 8) goto L10;
        if (findPositionOffset(r2.itemCount, r1 + 1) != r8) goto L17;
        return true;
    L17:
        r1 = r1 + 1;
        goto L3
    L10:
        if (r2.cmd != 1) goto L17;
        int r3 = r2.positionStart + r2.itemCount;
        int r4 = r2.positionStart;
    L12:
        if (r4 >= r3) goto L17;
        if (findPositionOffset(r4, r1 + 1) == r8) goto L15;
        r4 = r4 + 1;
        goto L12
    L15:
        return true;
    L18:
        return false;
    }

    private void dispatchAndUpdateViewHolders(UpdateOp r13) {
        if (r13.cmd == 1) goto L37;
        if (r13.cmd == 8) goto L37;
        int r0 = updatePositionWithPostponed(r13.positionStart, r13.cmd);
        int r2 = 1;
        int r3 = r13.positionStart;
        switch(r13.cmd) {
            case 2: goto L11;
            case 3: goto L9;
            case 4: goto L10;
            default: goto L9;
        };
    L10:
        int r4 = 1;
    L12:
        int r5 = 1;
    L14:
        if (r5 >= r13.itemCount) goto L32;
        int r7 = updatePositionWithPostponed(r13.positionStart + (r4 * r5), r13.cmd);
        boolean r8 = false;
        boolean r10 = false;
        switch(r13.cmd) {
            case 2: goto L22;
            case 3: goto L25;
            case 4: goto L19;
            default: goto L25;
        };
    L22:
        if (r7 != r0) goto L24;
        r10 = true;
    L24:
        r8 = r10;
    L25:
        if (r8 == false) goto L27;
        r2 = r2 + 1;
    L31:
        r5 = r5 + 1;
        goto L14
    L27:
        UpdateOp r9 = obtainUpdateOp(r13.cmd, r0, r2, r13.payload);
        dispatchFirstPassAndUpdateViewHolders(r9, r3);
        recycleUpdateOp(r9);
        if (r13.cmd != 4) goto L30;
        r3 = r3 + r2;
    L30:
        r0 = r7;
        r2 = 1;
        goto L31
    L19:
        if (r7 != (r0 + 1)) goto L21;
        r10 = true;
    L21:
        r8 = r10;
        goto L25
    L32:
        Object r1 = r13.payload;
        recycleUpdateOp(r13);
        if (r2 <= 0) goto L41;
        UpdateOp r6 = obtainUpdateOp(r13.cmd, r0, r2, r1);
        dispatchFirstPassAndUpdateViewHolders(r6, r3);
        recycleUpdateOp(r6);
        return;
    L41:
        return;
    L11:
        r4 = 0;
        goto L12
    L9:
        throw new IllegalArgumentException("op should be remove or update." + r13);
    L37:
        throw new IllegalArgumentException("should not dispatch add or move for pre layout");
    }

    private void postponeAndUpdateViewHolders(UpdateOp r5) {
        this.mPostponedList.add(r5);
        switch(r5.cmd) {
            case 1: goto L9;
            case 2: goto L8;
            case 4: goto L7;
            case 8: goto L6;
            default: goto L5;
        };
    L6:
        this.mCallback.offsetPositionsForMove(r5.positionStart, r5.itemCount);
        return;
    L7:
        this.mCallback.markViewHoldersUpdated(r5.positionStart, r5.itemCount, r5.payload);
        return;
    L8:
        this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(r5.positionStart, r5.itemCount);
        return;
    L9:
        this.mCallback.offsetPositionsForAdd(r5.positionStart, r5.itemCount);
        return;
    L5:
        throw new IllegalArgumentException("Unknown update op type for " + r5);
    }

    private int updatePositionWithPostponed(int r9, int r10) {
        int r1 = this.mPostponedList.size() - 1;
    L4:
        if (r1 < 0) goto L45;
        UpdateOp r4 = this.mPostponedList.get(r1);
        if (r4.cmd != 8) goto L33;
        if (r4.positionStart >= r4.itemCount) goto L10;
        int r2 = r4.positionStart;
        int r5 = r4.itemCount;
    L11:
        if (r9 < r2) goto L26;
        if (r9 > r5) goto L26;
        if (r2 != r4.positionStart) goto L20;
        if (r10 != 1) goto L17;
        r4.itemCount++;
    L19:
        r9 = r9 + 1;
    L44:
        r1 = r1 - 1;
        goto L4
    L17:
        if (r10 != 2) goto L19;
        r4.itemCount--;
        goto L19
    L20:
        if (r10 != 1) goto L22;
        r4.positionStart++;
    L24:
        r9 = r9 - 1;
        goto L44
    L22:
        if (r10 != 2) goto L24;
        r4.positionStart--;
    L26:
        if (r9 >= r4.positionStart) goto L44;
        if (r10 != 1) goto L29;
        r4.positionStart++;
        r4.itemCount++;
        goto L44
    L29:
        if (r10 != 2) goto L44;
        r4.positionStart--;
        r4.itemCount--;
        goto L44
    L10:
        r2 = r4.itemCount;
        r5 = r4.positionStart;
        goto L11
    L33:
        if (r4.positionStart <= r9) goto L35;
        if (r10 != 1) goto L42;
        r4.positionStart++;
        goto L44
    L42:
        if (r10 != 2) goto L44;
        r4.positionStart--;
        goto L44
    L35:
        if (r4.cmd != 1) goto L38;
        r9 = r9 - r4.itemCount;
        goto L44
    L38:
        if (r4.cmd != 2) goto L44;
        r9 = r9 + r4.itemCount;
        goto L44
    L45:
        int r3 = this.mPostponedList.size() - 1;
    L46:
        if (r3 < 0) goto L58;
        UpdateOp r6 = this.mPostponedList.get(r3);
        if (r6.cmd != 8) goto L55;
        if (r6.itemCount != r6.positionStart) goto L52;
    L53:
        this.mPostponedList.remove(r3);
        recycleUpdateOp(r6);
    L57:
        r3 = r3 - 1;
        goto L46
    L52:
        if (r6.itemCount >= 0) goto L57;
    L55:
        if (r6.itemCount > 0) goto L57;
        this.mPostponedList.remove(r3);
        recycleUpdateOp(r6);
        goto L57
    L58:
        return r9;
    }

    AdapterHelper addUpdateOp(UpdateOp... r2) {
        Collections.addAll(this.mPendingUpdates, r2);
        return this;
    }

    public int applyPendingUpdatesToPosition(int r6) {
        int r0 = this.mPendingUpdates.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L27;
        UpdateOp r2 = this.mPendingUpdates.get(r1);
        switch(r2.cmd) {
            case 1: goto L24;
            case 2: goto L17;
            case 8: goto L8;
            default: goto L26;
        };
    L8:
        if (r2.positionStart != r6) goto L11;
        r6 = r2.itemCount;
        goto L26
    L11:
        if (r2.positionStart >= r6) goto L14;
        r6 = r6 - 1;
    L14:
        if (r2.itemCount > r6) goto L26;
        r6 = r6 + 1;
        goto L26
    L17:
        if (r2.positionStart > r6) goto L26;
        if ((r2.positionStart + r2.itemCount) > r6) goto L20;
        r6 = r6 - r2.itemCount;
        goto L26
    L20:
        return -1;
    L24:
        if (r2.positionStart > r6) goto L26;
        r6 = r6 + r2.itemCount;
    L26:
        r1 = r1 + 1;
        goto L3
    L27:
        return r6;
    }

    void consumePostponedUpdates() {
        int r0 = this.mPostponedList.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        this.mCallback.onDispatchSecondPass(this.mPostponedList.get(r1));
        r1 = r1 + 1;
        goto L3
    L5:
        recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }

    void consumeUpdatesInOnePass() {
        consumePostponedUpdates();
        int r0 = this.mPendingUpdates.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L15;
        UpdateOp r2 = this.mPendingUpdates.get(r1);
        switch(r2.cmd) {
            case 1: goto L10;
            case 2: goto L9;
            case 4: goto L8;
            case 8: goto L7;
            default: goto L12;
        };
    L7:
        this.mCallback.onDispatchSecondPass(r2);
        this.mCallback.offsetPositionsForMove(r2.positionStart, r2.itemCount);
        goto L12
    L8:
        this.mCallback.onDispatchSecondPass(r2);
        this.mCallback.markViewHoldersUpdated(r2.positionStart, r2.itemCount, r2.payload);
        goto L12
    L9:
        this.mCallback.onDispatchSecondPass(r2);
        this.mCallback.offsetPositionsForRemovingInvisible(r2.positionStart, r2.itemCount);
        goto L12
    L10:
        this.mCallback.onDispatchSecondPass(r2);
        this.mCallback.offsetPositionsForAdd(r2.positionStart, r2.itemCount);
    L12:
        if (this.mOnItemProcessedCallback == null) goto L14;
        this.mOnItemProcessedCallback.run();
    L14:
        r1 = r1 + 1;
        goto L3
    L15:
        recycleUpdateOpsAndClearList(this.mPendingUpdates);
        this.mExistingUpdateTypes = 0;
    }

    void dispatchFirstPassAndUpdateViewHolders(UpdateOp r4, int r5) {
        this.mCallback.onDispatchFirstPass(r4);
        switch(r4.cmd) {
            case 2: goto L7;
            case 3: goto L5;
            case 4: goto L6;
            default: goto L5;
        };
    L6:
        this.mCallback.markViewHoldersUpdated(r5, r4.itemCount, r4.payload);
        return;
    L7:
        this.mCallback.offsetPositionsForRemovingInvisible(r5, r4.itemCount);
        return;
    L5:
        throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
    }

    int findPositionOffset(int r2) {
        return findPositionOffset(r2, 0);
    }

    int findPositionOffset(int r6, int r7) {
        int r0 = this.mPostponedList.size();
        int r1 = r7;
    L3:
        if (r1 >= r0) goto L28;
        UpdateOp r2 = this.mPostponedList.get(r1);
        if (r2.cmd != 8) goto L16;
        if (r2.positionStart != r6) goto L10;
        r6 = r2.itemCount;
    L27:
        r1 = r1 + 1;
        goto L3
    L10:
        if (r2.positionStart >= r6) goto L13;
        r6 = r6 - 1;
    L13:
        if (r2.itemCount > r6) goto L27;
        r6 = r6 + 1;
        goto L27
    L16:
        if (r2.positionStart > r6) goto L27;
        if (r2.cmd != 2) goto L25;
        if (r6 < (r2.positionStart + r2.itemCount)) goto L21;
        r6 = r6 - r2.itemCount;
        goto L27
    L21:
        return -1;
    L25:
        if (r2.cmd != 1) goto L27;
        r6 = r6 + r2.itemCount;
        goto L27
    L28:
        return r6;
    }

    boolean hasAnyUpdateTypes(int r2) {
        if ((this.mExistingUpdateTypes & r2) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    boolean hasPendingUpdates() {
        if (this.mPendingUpdates.size() <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    boolean hasUpdates() {
        if (this.mPostponedList.isEmpty() == false) goto L5;
    L7:
        return false;
    L5:
        if (this.mPendingUpdates.isEmpty() == true) goto L7;
        return true;
    }

    @Override // androidx.recyclerview.widget.OpReorderer.Callback
    public UpdateOp obtainUpdateOp(int r3, int r4, int r5, Object r6) {
        UpdateOp r0 = this.mUpdateOpPool.acquire();
        if (r0 == null) goto L7;
        r0.cmd = r3;
        r0.positionStart = r4;
        r0.itemCount = r5;
        r0.payload = r6;
        return r0;
    L7:
        return new UpdateOp(r3, r4, r5, r6);
    }

    boolean onItemRangeChanged(int r6, int r7, Object r8) {
        if (r7 >= 1) goto L5;
        return false;
    L5:
        this.mPendingUpdates.add(obtainUpdateOp(4, r6, r7, r8));
        this.mExistingUpdateTypes |= 4;
        if (this.mPendingUpdates.size() == 1) goto L8;
        return false;
    L8:
        return true;
    }

    boolean onItemRangeInserted(int r5, int r6) {
        if (r6 >= 1) goto L5;
        return false;
    L5:
        this.mPendingUpdates.add(obtainUpdateOp(1, r5, r6, null));
        this.mExistingUpdateTypes |= 1;
        if (this.mPendingUpdates.size() == 1) goto L8;
        return false;
    L8:
        return true;
    }

    boolean onItemRangeMoved(int r6, int r7, int r8) {
        if (r6 != r7) goto L6;
        return false;
    L6:
        if (r8 != 1) goto L12;
        this.mPendingUpdates.add(obtainUpdateOp(8, r6, r7, null));
        this.mExistingUpdateTypes |= 8;
        if (this.mPendingUpdates.size() == 1) goto L10;
        return false;
    L10:
        return true;
    L12:
        throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
    }

    boolean onItemRangeRemoved(int r6, int r7) {
        if (r7 >= 1) goto L5;
        return false;
    L5:
        this.mPendingUpdates.add(obtainUpdateOp(2, r6, r7, null));
        this.mExistingUpdateTypes |= 2;
        if (this.mPendingUpdates.size() == 1) goto L8;
        return false;
    L8:
        return true;
    }

    void preProcess() {
        this.mOpReorderer.reorderOps(this.mPendingUpdates);
        int r0 = this.mPendingUpdates.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L15;
        UpdateOp r2 = this.mPendingUpdates.get(r1);
        switch(r2.cmd) {
            case 1: goto L10;
            case 2: goto L9;
            case 4: goto L8;
            case 8: goto L7;
            default: goto L12;
        };
    L7:
        applyMove(r2);
        goto L12
    L8:
        applyUpdate(r2);
        goto L12
    L9:
        applyRemove(r2);
        goto L12
    L10:
        applyAdd(r2);
    L12:
        if (this.mOnItemProcessedCallback == null) goto L14;
        this.mOnItemProcessedCallback.run();
    L14:
        r1 = r1 + 1;
        goto L3
    L15:
        this.mPendingUpdates.clear();
    }

    @Override // androidx.recyclerview.widget.OpReorderer.Callback
    public void recycleUpdateOp(UpdateOp r2) {
        if (this.mDisableRecycler == true) goto L6;
        r2.payload = null;
        this.mUpdateOpPool.release(r2);
        return;
    }

    void recycleUpdateOpsAndClearList(List<UpdateOp> r4) {
        int r0 = r4.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        recycleUpdateOp(r4.get(r1));
        r1 = r1 + 1;
        goto L3
    L5:
        r4.clear();
    }

    void reset() {
        recycleUpdateOpsAndClearList(this.mPendingUpdates);
        recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }
}
