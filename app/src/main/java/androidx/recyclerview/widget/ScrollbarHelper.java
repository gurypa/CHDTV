package androidx.recyclerview.widget;

import android.view.View;

/* JADX INFO: loaded from: classes.dex */
class ScrollbarHelper {
    private ScrollbarHelper() {
    }

    static int computeScrollExtent(RecyclerView.State r2, OrientationHelper r3, View r4, View r5, RecyclerView.LayoutManager r6, boolean r7) {
        if (r6.getChildCount() != 0) goto L5;
        return 0;
    L5:
        if (r2.getItemCount() == 0) goto L16;
        if (r4 == null) goto L17;
        if (r5 == null) goto L18;
        if (r7 == false) goto L11;
        int r0 = r3.getDecoratedEnd(r5) - r3.getDecoratedStart(r4);
        return Math.min(r3.getTotalSpace(), r0);
    L11:
        return Math.abs(r6.getPosition(r4) - r6.getPosition(r5)) + 1;
    L18:
        return 0;
    L17:
        return 0;
    L16:
        return 0;
    }

    static int computeScrollOffset(RecyclerView.State r9, OrientationHelper r10, View r11, View r12, RecyclerView.LayoutManager r13, boolean r14, boolean r15) {
        if (r13.getChildCount() != 0) goto L5;
    L18:
        return 0;
    L5:
        if (r9.getItemCount() == 0) goto L18;
        if (r11 == null) goto L18;
        if (r12 == null) goto L18;
        int r0 = Math.min(r13.getPosition(r11), r13.getPosition(r12));
        int r2 = Math.max(r13.getPosition(r11), r13.getPosition(r12));
        if (r15 == false) goto L12;
        int r1 = Math.max(0, (r9.getItemCount() - r2) - 1);
    L14:
        if (r14 == true) goto L17;
        return r1;
    L17:
        return Math.round((r1 * (Math.abs(r10.getDecoratedEnd(r12) - r10.getDecoratedStart(r11)) / (Math.abs(r13.getPosition(r11) - r13.getPosition(r12)) + 1))) + (r10.getStartAfterPadding() - r10.getDecoratedStart(r11)));
    L12:
        r1 = Math.max(0, r0);
        goto L14
    }

    static int computeScrollRange(RecyclerView.State r4, OrientationHelper r5, View r6, View r7, RecyclerView.LayoutManager r8, boolean r9) {
        if (r8.getChildCount() != 0) goto L5;
        return 0;
    L5:
        if (r4.getItemCount() == 0) goto L16;
        if (r6 == null) goto L17;
        if (r7 == null) goto L18;
        if (r9 == true) goto L13;
        return r4.getItemCount();
    L13:
        return (int) (((r5.getDecoratedEnd(r7) - r5.getDecoratedStart(r6)) / (Math.abs(r8.getPosition(r6) - r8.getPosition(r7)) + 1)) * r4.getItemCount());
    L18:
        return 0;
    L17:
        return 0;
    L16:
        return 0;
    }
}
