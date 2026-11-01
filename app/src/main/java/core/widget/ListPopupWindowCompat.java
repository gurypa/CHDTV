package androidx.core.widget;

import android.view.View;
import android.widget.ListPopupWindow;

/* JADX INFO: loaded from: classes.dex */
public final class ListPopupWindowCompat {

    static class Api19Impl {
        private Api19Impl() {
        }

        static View.OnTouchListener createDragToOpenListener(ListPopupWindow r1, View r2) {
            return r1.createDragToOpenListener(r2);
        }
    }

    private ListPopupWindowCompat() {
    }

    public static View.OnTouchListener createDragToOpenListener(ListPopupWindow r1, View r2) {
        return Api19Impl.createDragToOpenListener(r1, r2);
    }

    @Deprecated
    public static View.OnTouchListener createDragToOpenListener(Object r1, View r2) {
        return createDragToOpenListener((ListPopupWindow) r1, r2);
    }
}
