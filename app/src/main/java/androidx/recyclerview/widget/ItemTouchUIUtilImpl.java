package androidx.recyclerview.widget;

import android.graphics.Canvas;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.C0472R;

/* JADX INFO: loaded from: classes.dex */
class ItemTouchUIUtilImpl implements ItemTouchUIUtil {
    static final ItemTouchUIUtil INSTANCE = null;

    static {
        INSTANCE = new ItemTouchUIUtilImpl();
    }

    ItemTouchUIUtilImpl() {
    }

    private static float findMaxElevation(RecyclerView r6, View r7) {
        int r0 = r6.getChildCount();
        float r1 = 0.0f;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L11;
        View r3 = r6.getChildAt(r2);
        if (r3 == r7) goto L10;
        float r4 = ViewCompat.getElevation(r3);
        if (r4 <= r1) goto L10;
        r1 = r4;
    L10:
        r2 = r2 + 1;
        goto L3
    L11:
        return r1;
    }

    @Override // androidx.recyclerview.widget.ItemTouchUIUtil
    public void clearView(View r4) {
        Object r0 = r4.getTag(C0472R.id.item_touch_helper_previous_elevation);
        if ((r0 instanceof Float) == false) goto L5;
        ViewCompat.setElevation(r4, ((Float) r0).floatValue());
    L5:
        r4.setTag(C0472R.id.item_touch_helper_previous_elevation, null);
        r4.setTranslationX(0.0f);
        r4.setTranslationY(0.0f);
    }

    @Override // androidx.recyclerview.widget.ItemTouchUIUtil
    public void onDraw(Canvas r4, RecyclerView r5, View r6, float r7, float r8, int r9, boolean r10) {
        if (r10 == true) goto L4;
    L6:
        r6.setTranslationX(r7);
        r6.setTranslationY(r8);
        return;
    L4:
        if (r6.getTag(C0472R.id.item_touch_helper_previous_elevation) != null) goto L6;
        Float r0 = Float.valueOf(ViewCompat.getElevation(r6));
        ViewCompat.setElevation(r6, findMaxElevation(r5, r6) + 1.0f);
        r6.setTag(C0472R.id.item_touch_helper_previous_elevation, r0);
        goto L6
    }

    @Override // androidx.recyclerview.widget.ItemTouchUIUtil
    public void onDrawOver(Canvas r1, RecyclerView r2, View r3, float r4, float r5, int r6, boolean r7) {
    }

    @Override // androidx.recyclerview.widget.ItemTouchUIUtil
    public void onSelected(View r1) {
    }
}
