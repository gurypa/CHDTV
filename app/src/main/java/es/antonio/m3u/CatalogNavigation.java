package es.antonio.m3u;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
final class CatalogNavigation {
    CatalogNavigation() {
    }

    static void bind(final AbsListView r3, final int r4) {
        r3.setDescendantFocusability(393216);
        r3.setFocusable(true);
        r3.setFocusableInTouchMode(false);
        if ((r3 instanceof ListView) == false) goto L5;
        ((ListView) r3).setItemsCanFocus(false);
    L5:
        final int[] r0 = {0};
        final HashSet r1 = new HashSet();
        r3.setOnKeyListener(new CatalogNavigation$$ExternalSyntheticLambda0(r1, r3, r4, r0));
        r3.post(new CatalogNavigation$$ExternalSyntheticLambda1(r3));
    }

    static /* synthetic */ boolean lambda$bind$0(Set r13, AbsListView r14, int r15, int[] r16, View r17, int r18, KeyEvent r19) {
        if (r18 == 19) goto L10;
        if (r18 == 20) goto L10;
        if (r18 == 21) goto L10;
        if (r18 == 22) goto L10;
        boolean r6 = false;
    L12:
        if (r18 != 23) goto L14;
    L21:
        boolean r7 = true;
    L22:
        if (r6 == true) goto L26;
        if (r7 == true) goto L26;
        return false;
    L26:
        if (r19.getAction() != 1) goto L30;
        return r13.remove(Integer.valueOf(r18));
    L30:
        if (r19.getAction() == 0) goto L33;
        return false;
    L33:
        if ((r14 instanceof GridView) == false) goto L35;
        int r8 = Math.max(1, ((GridView) r14).getNumColumns());
    L36:
        int r9 = r14.getCount();
        if (r9 != 0) goto L39;
        return false;
    L39:
        int r10 = r14.getSelectedItemPosition();
        if (r10 >= 0) goto L42;
        int r11 = Math.max(r16[0], r14.getFirstVisiblePosition());
    L43:
        int r12 = Math.max(0, Math.min(r9 - 1, r11));
        if (r7 == false) goto L49;
        r13.add(Integer.valueOf(r18));
        if (r19.getRepeatCount() != 0) goto L48;
        r14.performItemClick(r14.getSelectedView(), r12, ((ListAdapter) r14.getAdapter()).getItemId(r12));
    L48:
        return true;
    L49:
        int r110 = r12;
        if (r18 != 19) goto L54;
        if (r12 >= r8) goto L53;
        return false;
    L53:
        r110 = r12 - r8;
    L65:
        r13.add(Integer.valueOf(r18));
        r16[0] = r110;
        r14.setSelection(r110);
        return true;
    L54:
        if (r18 != 20) goto L56;
        r110 = Math.min(r9 - 1, r12 + r8);
        goto L65
    L56:
        if (r8 != 1) goto L58;
        return false;
    L58:
        if (r18 != 21) goto L63;
        if ((r12 % r8) <= 0) goto L65;
        r110 = r12 - 1;
        goto L65
    L63:
        if ((r12 % r8) >= (r8 - 1)) goto L65;
        r110 = Math.min(r9 - 1, r12 + 1);
        goto L65
    L42:
        r11 = r10;
        goto L43
    L35:
        r8 = r15;
        goto L36
    L14:
        if (r18 == 66) goto L21;
        if (r18 == 160) goto L21;
        if (r18 == 96) goto L21;
        r7 = false;
    L10:
        r6 = true;
        goto L12
    }

    static /* synthetic */ void lambda$bind$1(AbsListView r1) {
        if (r1.isAttachedToWindow() == true) goto L5;
        return;
    L5:
        if (r1.isInTouchMode() == false) goto L7;
        return;
    L7:
        if ((r1 instanceof ResponsiveCatalogGrid) == true) goto L9;
    L10:
        r1.requestFocus();
        if (r1.getSelectedItemPosition() >= 0) goto L17;
        r1.setSelection(r1.getFirstVisiblePosition());
        return;
    L17:
        return;
    L9:
        if (((ResponsiveCatalogGrid) r1).hasTouchBrowsing() == false) goto L10;
    }
}
