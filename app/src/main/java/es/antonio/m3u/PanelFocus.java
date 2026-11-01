package es.antonio.m3u;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
final class PanelFocus {
    private PanelFocus() {
    }

    static List<View> buttons(View r1) {
        ArrayList r0 = new ArrayList();
        collect(r1, r0);
        return r0;
    }

    private static void collect(View r3, List<View> r4) {
        if (r3.getVisibility() == 0) goto L5;
        return;
    L5:
        if (r3.isEnabled() == true) goto L8;
        return;
    L8:
        if ((r3 instanceof Button) == false) goto L13;
        if (r3.isFocusable() == false) goto L13;
        r4.add(r3);
    L13:
        if ((r3 instanceof ViewGroup) == false) goto L20;
        ViewGroup r0 = (ViewGroup) r3;
        int r1 = 0;
    L16:
        if (r1 >= r0.getChildCount()) goto L22;
        collect(r0.getChildAt(r1), r4);
        r1 = r1 + 1;
        goto L16
    L22:
        return;
    }

    static void move(View r3, View r4, int r5) {
        List<View> r0 = buttons(r3);
        int r1 = next(r0.indexOf(r4), r0.size(), r5);
        if (r1 < 0) goto L6;
        r0.get(r1).requestFocus();
        return;
    }

    static int next(int r3, int r4, int r5) {
        if (r4 != 0) goto L6;
        return -1;
    L6:
        if (r3 >= 0) goto L11;
        if (r5 < 0) goto L9;
        return 0;
    L9:
        return r4 - 1;
    L11:
        return Math.max(0, Math.min(r4 - 1, r3 + r5));
    }
}
