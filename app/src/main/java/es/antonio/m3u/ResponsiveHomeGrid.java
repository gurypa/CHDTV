package es.antonio.m3u;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes3.dex */
final class ResponsiveHomeGrid extends ResponsiveCatalogGrid {
    private final Runnable resizeCards;

    public static /* synthetic */ void $r8$lambda$Fxd3Y4TsfYxlSAP0UdIunOJ79pY(ResponsiveHomeGrid r0) {
        r0.lambda$new$0();
    }

    ResponsiveHomeGrid(Context r2) {
        super(r2);
        this.resizeCards = new ResponsiveHomeGrid$$ExternalSyntheticLambda0(this);
    }

    private /* synthetic */ void lambda$new$0() {
        int r0 = cardHeight();
        int r1 = 0;
    L4:
        if (r1 >= getChildCount()) goto L11;
        View r2 = getChildAt(r1);
        ViewGroup.LayoutParams r3 = r2.getLayoutParams();
        if (r3 == null) goto L10;
        if (r3.height == r0) goto L10;
        r3.height = r0;
        r2.setLayoutParams(r3);
    L10:
        r1 = r1 + 1;
        goto L4
    }

    int cardHeight() {
        if (getAdapter() != null) goto L5;
        int r0 = 5;
    L6:
        int r1 = (int) Math.ceil(((double) r0) / ((double) Math.max(1, getNumColumns())));
        return Math.max(Math.max(Math.round(getResources().getDisplayMetrics().density * 132.0f), Math.round((getResources().getDisplayMetrics().scaledDensity * 104.0f) + (getResources().getDisplayMetrics().density * 20.0f))), (((getHeight() - getPaddingTop()) - getPaddingBottom()) - (getVerticalSpacing() * (r1 - 1))) / Math.max(1, r1));
    L5:
        r0 = getAdapter().getCount();
        goto L6
    }

    @Override // es.antonio.m3u.ResponsiveCatalogGrid
    protected int columnsForWidth(float r2) {
        if (r2 < 568.0f) goto L6;
        return 3;
    L6:
        if (r2 < 280.0f) goto L8;
        return 2;
    L8:
        return 1;
    }

    @Override // es.antonio.m3u.ResponsiveCatalogGrid, android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        removeCallbacks(this.resizeCards);
        super.onDetachedFromWindow();
    }

    @Override // android.widget.AbsListView, android.view.View
    protected void onSizeChanged(int r2, int r3, int r4, int r5) {
        super.onSizeChanged(r2, r3, r4, r5);
        if (r3 != r5) goto L5;
        if (r2 != r4) goto L5;
        return;
    L5:
        removeCallbacks(this.resizeCards);
        post(this.resizeCards);
    }
}
