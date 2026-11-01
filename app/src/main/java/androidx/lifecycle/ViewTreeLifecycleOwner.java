package androidx.lifecycle;

import android.view.View;
import android.view.ViewParent;
import androidx.lifecycle.runtime.C0151R;

/* JADX INFO: loaded from: classes.dex */
public class ViewTreeLifecycleOwner {
    private ViewTreeLifecycleOwner() {
    }

    public static LifecycleOwner get(View r4) {
        LifecycleOwner r0 = (LifecycleOwner) r4.getTag(C0151R.id.view_tree_lifecycle_owner);
        if (r0 == null) goto L5;
        return r0;
    L5:
        ViewParent r1 = r4.getParent();
    L6:
        if (r0 != null) goto L10;
        if ((r1 instanceof View) == false) goto L10;
        View r2 = (View) r1;
        r0 = (LifecycleOwner) r2.getTag(C0151R.id.view_tree_lifecycle_owner);
        r1 = r2.getParent();
    L10:
        return r0;
    }

    public static void set(View r1, LifecycleOwner r2) {
        r1.setTag(C0151R.id.view_tree_lifecycle_owner, r2);
    }
}
