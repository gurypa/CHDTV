package androidx.recyclerview.widget;

import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public abstract class SimpleItemAnimator extends RecyclerView.ItemAnimator {
    private static final boolean DEBUG = false;
    private static final String TAG = "SimpleItemAnimator";
    boolean mSupportsChangeAnimations;

    public SimpleItemAnimator() {
        this.mSupportsChangeAnimations = true;
    }

    public abstract boolean animateAdd(RecyclerView.ViewHolder r1);

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public boolean animateAppearance(RecyclerView.ViewHolder r9, RecyclerView.ItemAnimator.ItemHolderInfo r10, RecyclerView.ItemAnimator.ItemHolderInfo r11) {
        if (r10 == null) goto L13;
        if (r10.left != r11.left) goto L10;
        if (r10.top == r11.top) goto L13;
    L10:
        return animateMove(r9, r10.left, r10.top, r11.left, r11.top);
    L13:
        return animateAdd(r9);
    }

    public abstract boolean animateChange(RecyclerView.ViewHolder r1, RecyclerView.ViewHolder r2, int r3, int r4, int r5, int r6);

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public boolean animateChange(RecyclerView.ViewHolder r8, RecyclerView.ViewHolder r9, RecyclerView.ItemAnimator.ItemHolderInfo r10, RecyclerView.ItemAnimator.ItemHolderInfo r11) {
        int r3 = r10.left;
        int r4 = r10.top;
        if (r9.shouldIgnore() == false) goto L5;
        int r0 = r10.left;
        int r5 = r0;
        int r6 = r10.top;
    L7:
        return animateChange(r8, r9, r3, r4, r5, r6);
    L5:
        int r1 = r11.left;
        r5 = r1;
        r6 = r11.top;
        goto L7
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public boolean animateDisappearance(RecyclerView.ViewHolder r8, RecyclerView.ItemAnimator.ItemHolderInfo r9, RecyclerView.ItemAnimator.ItemHolderInfo r10) {
        int r2 = r9.left;
        int r3 = r9.top;
        View r6 = r8.itemView;
        if (r10 != null) goto L5;
        int r0 = r6.getLeft();
    L6:
        int r4 = r0;
        if (r10 != null) goto L9;
        int r1 = r6.getTop();
    L10:
        int r5 = r1;
        if (r8.isRemoved() == true) goto L20;
        if (r2 != r4) goto L16;
        if (r3 == r5) goto L20;
    L16:
        r6.layout(r4, r5, r6.getWidth() + r4, r6.getHeight() + r5);
        return animateMove(r8, r2, r3, r4, r5);
    L20:
        return animateRemove(r8);
    L9:
        r1 = r10.top;
        goto L10
    L5:
        r0 = r10.left;
        goto L6
    }

    public abstract boolean animateMove(RecyclerView.ViewHolder r1, int r2, int r3, int r4, int r5);

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public boolean animatePersistence(RecyclerView.ViewHolder r8, RecyclerView.ItemAnimator.ItemHolderInfo r9, RecyclerView.ItemAnimator.ItemHolderInfo r10) {
        if (r9.left != r10.left) goto L10;
        if (r9.top != r10.top) goto L10;
        dispatchMoveFinished(r8);
        return false;
    L10:
        return animateMove(r8, r9.left, r9.top, r10.left, r10.top);
    }

    public abstract boolean animateRemove(RecyclerView.ViewHolder r1);

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder r2) {
        if (this.mSupportsChangeAnimations == true) goto L5;
    L8:
        return true;
    L5:
        if (r2.isInvalid() == true) goto L8;
        return false;
    }

    public final void dispatchAddFinished(RecyclerView.ViewHolder r1) {
        onAddFinished(r1);
        dispatchAnimationFinished(r1);
    }

    public final void dispatchAddStarting(RecyclerView.ViewHolder r1) {
        onAddStarting(r1);
    }

    public final void dispatchChangeFinished(RecyclerView.ViewHolder r1, boolean r2) {
        onChangeFinished(r1, r2);
        dispatchAnimationFinished(r1);
    }

    public final void dispatchChangeStarting(RecyclerView.ViewHolder r1, boolean r2) {
        onChangeStarting(r1, r2);
    }

    public final void dispatchMoveFinished(RecyclerView.ViewHolder r1) {
        onMoveFinished(r1);
        dispatchAnimationFinished(r1);
    }

    public final void dispatchMoveStarting(RecyclerView.ViewHolder r1) {
        onMoveStarting(r1);
    }

    public final void dispatchRemoveFinished(RecyclerView.ViewHolder r1) {
        onRemoveFinished(r1);
        dispatchAnimationFinished(r1);
    }

    public final void dispatchRemoveStarting(RecyclerView.ViewHolder r1) {
        onRemoveStarting(r1);
    }

    public boolean getSupportsChangeAnimations() {
        return this.mSupportsChangeAnimations;
    }

    public void onAddFinished(RecyclerView.ViewHolder r1) {
    }

    public void onAddStarting(RecyclerView.ViewHolder r1) {
    }

    public void onChangeFinished(RecyclerView.ViewHolder r1, boolean r2) {
    }

    public void onChangeStarting(RecyclerView.ViewHolder r1, boolean r2) {
    }

    public void onMoveFinished(RecyclerView.ViewHolder r1) {
    }

    public void onMoveStarting(RecyclerView.ViewHolder r1) {
    }

    public void onRemoveFinished(RecyclerView.ViewHolder r1) {
    }

    public void onRemoveStarting(RecyclerView.ViewHolder r1) {
    }

    public void setSupportsChangeAnimations(boolean r1) {
        this.mSupportsChangeAnimations = r1;
    }
}
