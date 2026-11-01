package androidx.core.view;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class DragStartHelper {
    private boolean mDragging;
    private int mLastTouchX;
    private int mLastTouchY;
    private final OnDragStartListener mListener;
    private final View.OnLongClickListener mLongClickListener;
    private final View.OnTouchListener mTouchListener;
    private final View mView;

    public interface OnDragStartListener {
        boolean onDragStart(View r1, DragStartHelper r2);
    }

    public DragStartHelper(View r2, OnDragStartListener r3) {
        this.mLongClickListener = new DragStartHelper$$ExternalSyntheticLambda0(this);
        this.mTouchListener = new DragStartHelper$$ExternalSyntheticLambda1(this);
        this.mView = r2;
        this.mListener = r3;
    }

    public void attach() {
        this.mView.setOnLongClickListener(this.mLongClickListener);
        this.mView.setOnTouchListener(this.mTouchListener);
    }

    public void detach() {
        this.mView.setOnLongClickListener(null);
        this.mView.setOnTouchListener(null);
    }

    public void getTouchPosition(Point r3) {
        r3.set(this.mLastTouchX, this.mLastTouchY);
    }

    public boolean onLongClick(View r2) {
        return this.mListener.onDragStart(r2, this);
    }

    public boolean onTouch(View r5, MotionEvent r6) {
        int r0 = (int) r6.getX();
        int r1 = (int) r6.getY();
        switch(r6.getAction()) {
            case 0: goto L21;
            case 1: goto L20;
            case 2: goto L6;
            case 3: goto L20;
            default: goto L22;
        };
    L20:
        this.mDragging = false;
        goto L22
    L21:
        this.mLastTouchX = r0;
        this.mLastTouchY = r1;
    L22:
        return false;
    L6:
        if (MotionEventCompat.isFromSource(r6, InputDeviceCompat.SOURCE_MOUSE) == false) goto L22;
        if ((r6.getButtonState() & 1) == 0) goto L22;
        if (this.mDragging == true) goto L22;
        if (this.mLastTouchX == r0) goto L16;
    L18:
        this.mLastTouchX = r0;
        this.mLastTouchY = r1;
        this.mDragging = this.mListener.onDragStart(r5, this);
        return this.mDragging;
    L16:
        if (this.mLastTouchY != r1) goto L18;
        goto L18
    }
}
