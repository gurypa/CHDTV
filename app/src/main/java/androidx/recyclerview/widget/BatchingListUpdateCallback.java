package androidx.recyclerview.widget;

/* JADX INFO: loaded from: classes.dex */
public class BatchingListUpdateCallback implements ListUpdateCallback {
    private static final int TYPE_ADD = 1;
    private static final int TYPE_CHANGE = 3;
    private static final int TYPE_NONE = 0;
    private static final int TYPE_REMOVE = 2;
    int mLastEventCount;
    Object mLastEventPayload;
    int mLastEventPosition;
    int mLastEventType;
    final ListUpdateCallback mWrapped;

    public BatchingListUpdateCallback(ListUpdateCallback r2) {
        this.mLastEventType = 0;
        this.mLastEventPosition = -1;
        this.mLastEventCount = -1;
        this.mLastEventPayload = null;
        this.mWrapped = r2;
    }

    public void dispatchLastEvent() {
        if (this.mLastEventType != 0) goto L6;
        return;
    L6:
        switch(this.mLastEventType) {
            case 1: goto L10;
            case 2: goto L9;
            case 3: goto L8;
            default: goto L11;
        };
    L8:
        this.mWrapped.onChanged(this.mLastEventPosition, this.mLastEventCount, this.mLastEventPayload);
        goto L11
    L9:
        this.mWrapped.onRemoved(this.mLastEventPosition, this.mLastEventCount);
        goto L11
    L10:
        this.mWrapped.onInserted(this.mLastEventPosition, this.mLastEventCount);
    L11:
        this.mLastEventPayload = null;
        this.mLastEventType = 0;
    }

    @Override // androidx.recyclerview.widget.ListUpdateCallback
    public void onChanged(int r4, int r5, Object r6) {
        if (this.mLastEventType == 3) goto L5;
    L12:
        dispatchLastEvent();
        this.mLastEventPosition = r4;
        this.mLastEventCount = r5;
        this.mLastEventPayload = r6;
        this.mLastEventType = 3;
        return;
    L5:
        if (r4 > (this.mLastEventPosition + this.mLastEventCount)) goto L12;
        if ((r4 + r5) < this.mLastEventPosition) goto L12;
        if (this.mLastEventPayload != r6) goto L12;
        int r0 = this.mLastEventPosition + this.mLastEventCount;
        this.mLastEventPosition = Math.min(r4, this.mLastEventPosition);
        this.mLastEventCount = Math.max(r0, r4 + r5) - this.mLastEventPosition;
    }

    @Override // androidx.recyclerview.widget.ListUpdateCallback
    public void onInserted(int r4, int r5) {
        if (this.mLastEventType == 1) goto L5;
    L10:
        dispatchLastEvent();
        this.mLastEventPosition = r4;
        this.mLastEventCount = r5;
        this.mLastEventType = 1;
        return;
    L5:
        if (r4 < this.mLastEventPosition) goto L10;
        if (r4 > (this.mLastEventPosition + this.mLastEventCount)) goto L10;
        this.mLastEventCount += r5;
        this.mLastEventPosition = Math.min(r4, this.mLastEventPosition);
    }

    @Override // androidx.recyclerview.widget.ListUpdateCallback
    public void onMoved(int r2, int r3) {
        dispatchLastEvent();
        this.mWrapped.onMoved(r2, r3);
    }

    @Override // androidx.recyclerview.widget.ListUpdateCallback
    public void onRemoved(int r4, int r5) {
        if (this.mLastEventType == 2) goto L5;
    L10:
        dispatchLastEvent();
        this.mLastEventPosition = r4;
        this.mLastEventCount = r5;
        this.mLastEventType = 2;
        return;
    L5:
        if (this.mLastEventPosition < r4) goto L10;
        if (this.mLastEventPosition > (r4 + r5)) goto L10;
        this.mLastEventCount += r5;
        this.mLastEventPosition = r4;
    }
}
