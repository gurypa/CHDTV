package androidx.core.widget;

import android.widget.ListView;

/* JADX INFO: loaded from: classes.dex */
public class ListViewAutoScrollHelper extends AutoScrollHelper {
    private final ListView mTarget;

    public ListViewAutoScrollHelper(ListView r1) {
        super(r1);
        this.mTarget = r1;
    }

    @Override // androidx.core.widget.AutoScrollHelper
    public boolean canTargetScrollHorizontally(int r2) {
        return false;
    }

    @Override // androidx.core.widget.AutoScrollHelper
    public boolean canTargetScrollVertically(int r10) {
        ListView r0 = this.mTarget;
        int r1 = r0.getCount();
        if (r1 != 0) goto L5;
        return false;
    L5:
        int r3 = r0.getChildCount();
        int r4 = r0.getFirstVisiblePosition();
        int r5 = r4 + r3;
        if (r10 <= 0) goto L12;
        if (r5 >= r1) goto L9;
        return true;
    L9:
        if (r0.getChildAt(r3 - 1).getBottom() > r0.getHeight()) goto L22;
        return false;
    L22:
        return true;
    L12:
        if (r10 >= 0) goto L20;
        if (r4 <= 0) goto L15;
        return true;
    L15:
        if (r0.getChildAt(0).getTop() < 0) goto L23;
        return false;
    L23:
        return true;
    L20:
        return false;
    }

    @Override // androidx.core.widget.AutoScrollHelper
    public void scrollTargetBy(int r2, int r3) {
        ListViewCompat.scrollListBy(this.mTarget, r3);
    }
}
