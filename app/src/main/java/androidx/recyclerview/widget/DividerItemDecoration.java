package androidx.recyclerview.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = null;
    public static final int HORIZONTAL = 0;
    private static final String TAG = "DividerItem";
    public static final int VERTICAL = 1;
    private final Rect mBounds;
    private Drawable mDivider;
    private int mOrientation;

    static {
        ATTRS = new int[]{R.attr.listDivider};
    }

    public DividerItemDecoration(Context r4, int r5) {
        this.mBounds = new Rect();
        TypedArray r0 = r4.obtainStyledAttributes(ATTRS);
        this.mDivider = r0.getDrawable(0);
        if (this.mDivider != null) goto L5;
        Log.w(TAG, "@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute all call setDrawable()");
    L5:
        r0.recycle();
        setOrientation(r5);
    }

    private void drawHorizontal(Canvas r9, RecyclerView r10) {
        r9.save();
        if (r10.getClipToPadding() == false) goto L5;
        int r0 = r10.getPaddingTop();
        int r1 = r10.getHeight() - r10.getPaddingBottom();
        r9.clipRect(r10.getPaddingLeft(), r0, r10.getWidth() - r10.getPaddingRight(), r1);
    L6:
        int r2 = r10.getChildCount();
        int r3 = 0;
    L7:
        if (r3 >= r2) goto L9;
        View r4 = r10.getChildAt(r3);
        r10.getLayoutManager().getDecoratedBoundsWithMargins(r4, this.mBounds);
        int r5 = this.mBounds.right + Math.round(r4.getTranslationX());
        int r6 = r5 - this.mDivider.getIntrinsicWidth();
        this.mDivider.setBounds(r6, r0, r5, r1);
        this.mDivider.draw(r9);
        r3 = r3 + 1;
        goto L7
    L9:
        r9.restore();
        return;
    L5:
        r0 = 0;
        r1 = r10.getHeight();
        goto L6
    }

    private void drawVertical(Canvas r9, RecyclerView r10) {
        r9.save();
        if (r10.getClipToPadding() == false) goto L5;
        int r0 = r10.getPaddingLeft();
        int r1 = r10.getWidth() - r10.getPaddingRight();
        r9.clipRect(r0, r10.getPaddingTop(), r1, r10.getHeight() - r10.getPaddingBottom());
    L6:
        int r2 = r10.getChildCount();
        int r3 = 0;
    L7:
        if (r3 >= r2) goto L9;
        View r4 = r10.getChildAt(r3);
        r10.getDecoratedBoundsWithMargins(r4, this.mBounds);
        int r5 = this.mBounds.bottom + Math.round(r4.getTranslationY());
        int r6 = r5 - this.mDivider.getIntrinsicHeight();
        this.mDivider.setBounds(r0, r6, r1, r5);
        this.mDivider.draw(r9);
        r3 = r3 + 1;
        goto L7
    L9:
        r9.restore();
        return;
    L5:
        r0 = 0;
        r1 = r10.getWidth();
        goto L6
    }

    public Drawable getDrawable() {
        return this.mDivider;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect r4, View r5, RecyclerView r6, RecyclerView.State r7) {
        if (this.mDivider != null) goto L7;
        r4.set(0, 0, 0, 0);
        return;
    L7:
        if (this.mOrientation != 1) goto L9;
        r4.set(0, 0, 0, this.mDivider.getIntrinsicHeight());
        return;
    L9:
        r4.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDraw(Canvas r3, RecyclerView r4, RecyclerView.State r5) {
        if (r4.getLayoutManager() != null) goto L5;
        return;
    L5:
        if (this.mDivider != null) goto L8;
        return;
    L8:
        if (this.mOrientation != 1) goto L10;
        drawVertical(r3, r4);
        return;
    L10:
        drawHorizontal(r3, r4);
    }

    public void setDrawable(Drawable r3) {
        if (r3 == null) goto L6;
        this.mDivider = r3;
        return;
    L6:
        throw new IllegalArgumentException("Drawable cannot be null.");
    }

    public void setOrientation(int r3) {
        if (r3 != 0) goto L4;
    L8:
        this.mOrientation = r3;
        return;
    L4:
        if (r3 == 1) goto L8;
        throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
    }
}
