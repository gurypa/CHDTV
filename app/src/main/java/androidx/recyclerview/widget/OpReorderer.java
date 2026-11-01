package androidx.recyclerview.widget;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class OpReorderer {
    final Callback mCallback;

    interface Callback {
        AdapterHelper.UpdateOp obtainUpdateOp(int r1, int r2, int r3, Object r4);

        void recycleUpdateOp(AdapterHelper.UpdateOp r1);
    }

    OpReorderer(Callback r1) {
        this.mCallback = r1;
    }

    private int getLastMoveOutOfOrder(List<AdapterHelper.UpdateOp> r6) {
        boolean r0 = false;
        int r1 = r6.size() - 1;
    L3:
        if (r1 < 0) goto L10;
        if (r6.get(r1).cmd != 8) goto L8;
        if (r0 == false) goto L9;
        return r1;
    L9:
        r1 = r1 - 1;
        goto L3
    L8:
        r0 = true;
        goto L9
    L10:
        return -1;
    }

    private void swapMoveAdd(List<AdapterHelper.UpdateOp> r4, int r5, AdapterHelper.UpdateOp r6, int r7, AdapterHelper.UpdateOp r8) {
        int r0 = 0;
        if (r6.itemCount >= r8.positionStart) goto L6;
        r0 = 0 - 1;
    L6:
        if (r6.positionStart >= r8.positionStart) goto L9;
        r0 = r0 + 1;
    L9:
        if (r8.positionStart > r6.positionStart) goto L12;
        r6.positionStart += r8.itemCount;
    L12:
        if (r8.positionStart > r6.itemCount) goto L14;
        r6.itemCount += r8.itemCount;
    L14:
        r8.positionStart += r0;
        r4.set(r5, r8);
        r4.set(r7, r6);
    }

    private void swapMoveOp(List<AdapterHelper.UpdateOp> r8, int r9, int r10) {
        AdapterHelper.UpdateOp r4 = r8.get(r9);
        AdapterHelper.UpdateOp r6 = r8.get(r10);
        switch(r6.cmd) {
            case 1: goto L7;
            case 2: goto L6;
            case 3: goto L9;
            case 4: goto L5;
            default: goto L9;
        };
    L5:
        swapMoveUpdate(r8, r9, r4, r10, r6);
        return;
    L6:
        swapMoveRemove(r8, r9, r4, r10, r6);
        return;
    L7:
        swapMoveAdd(r8, r9, r4, r10, r6);
        return;
    }

    void reorderOps(List<AdapterHelper.UpdateOp> r4) {
    L2:
        int r0 = getLastMoveOutOfOrder(r4);
        if (r0 == (-1)) goto L5;
        swapMoveOp(r4, r0, r0 + 1);
        goto L2
    }

    void swapMoveRemove(List<AdapterHelper.UpdateOp> r9, int r10, AdapterHelper.UpdateOp r11, int r12, AdapterHelper.UpdateOp r13) {
        AdapterHelper.UpdateOp r0 = null;
        boolean r1 = false;
        if (r11.positionStart >= r11.itemCount) goto L9;
        boolean r2 = false;
        if (r13.positionStart != r11.positionStart) goto L15;
        if (r13.itemCount != (r11.itemCount - r11.positionStart)) goto L15;
        r1 = true;
    L15:
        if (r11.itemCount >= r13.positionStart) goto L18;
        r13.positionStart--;
    L24:
        if (r11.positionStart > r13.positionStart) goto L27;
        r13.positionStart++;
    L29:
        if (r1 == false) goto L32;
        r9.set(r10, r13);
        r9.remove(r12);
        this.mCallback.recycleUpdateOp(r11);
        return;
    L32:
        if (r2 == false) goto L46;
        if (r0 == null) goto L41;
        if (r11.positionStart <= r0.positionStart) goto L38;
        r11.positionStart -= r0.itemCount;
    L38:
        if (r11.itemCount <= r0.positionStart) goto L41;
        r11.itemCount -= r0.itemCount;
    L41:
        if (r11.positionStart <= r13.positionStart) goto L44;
        r11.positionStart -= r13.itemCount;
    L44:
        if (r11.itemCount <= r13.positionStart) goto L59;
        r11.itemCount -= r13.itemCount;
    L59:
        r9.set(r10, r13);
        if (r11.positionStart == r11.itemCount) goto L62;
        r9.set(r12, r11);
    L63:
        if (r0 == null) goto L67;
        r9.add(r10, r0);
        return;
    L67:
        return;
    L62:
        r9.remove(r12);
        goto L63
    L46:
        if (r0 == null) goto L54;
        if (r11.positionStart < r0.positionStart) goto L51;
        r11.positionStart -= r0.itemCount;
    L51:
        if (r11.itemCount < r0.positionStart) goto L54;
        r11.itemCount -= r0.itemCount;
    L54:
        if (r11.positionStart < r13.positionStart) goto L57;
        r11.positionStart -= r13.itemCount;
    L57:
        if (r11.itemCount < r13.positionStart) goto L59;
        r11.itemCount -= r13.itemCount;
        goto L59
    L27:
        if (r11.positionStart >= (r13.positionStart + r13.itemCount)) goto L29;
        r0 = this.mCallback.obtainUpdateOp(2, r11.positionStart + 1, (r13.positionStart + r13.itemCount) - r11.positionStart, null);
        r13.itemCount = r11.positionStart - r13.positionStart;
        goto L29
    L18:
        if (r11.itemCount >= (r13.positionStart + r13.itemCount)) goto L24;
        r13.itemCount--;
        r11.cmd = 2;
        r11.itemCount = 1;
        if (r13.itemCount != 0) goto L66;
        r9.remove(r12);
        this.mCallback.recycleUpdateOp(r13);
        return;
    L66:
        return;
    L9:
        r2 = true;
        if (r13.positionStart != (r11.itemCount + 1)) goto L15;
        if (r13.itemCount != (r11.positionStart - r11.itemCount)) goto L15;
        r1 = true;
        goto L15
    }

    void swapMoveUpdate(List<AdapterHelper.UpdateOp> r8, int r9, AdapterHelper.UpdateOp r10, int r11, AdapterHelper.UpdateOp r12) {
        AdapterHelper.UpdateOp r0 = null;
        AdapterHelper.UpdateOp r1 = null;
        if (r10.itemCount >= r12.positionStart) goto L6;
        r12.positionStart--;
    L9:
        if (r10.positionStart > r12.positionStart) goto L12;
        r12.positionStart++;
    L14:
        r8.set(r11, r10);
        if (r12.itemCount <= 0) goto L17;
        r8.set(r9, r12);
    L18:
        if (r0 == null) goto L20;
        r8.add(r9, r0);
    L20:
        if (r1 == null) goto L23;
        r8.add(r9, r1);
        return;
    L23:
        return;
    L17:
        r8.remove(r9);
        this.mCallback.recycleUpdateOp(r12);
        goto L18
    L12:
        if (r10.positionStart >= (r12.positionStart + r12.itemCount)) goto L14;
        int r2 = (r12.positionStart + r12.itemCount) - r10.positionStart;
        r1 = this.mCallback.obtainUpdateOp(4, r10.positionStart + 1, r2, r12.payload);
        r12.itemCount -= r2;
        goto L14
    L6:
        if (r10.itemCount >= (r12.positionStart + r12.itemCount)) goto L9;
        r12.itemCount--;
        r0 = this.mCallback.obtainUpdateOp(4, r10.positionStart, 1, r12.payload);
        goto L9
    }
}
