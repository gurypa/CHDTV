package androidx.core.widget;

import android.widget.ListView;

/* JADX INFO: loaded from: classes.dex */
public final class ListViewCompat {

    static class Api19Impl {
        private Api19Impl() {
        }

        static boolean canScrollList(ListView r1, int r2) {
            return r1.canScrollList(r2);
        }

        static void scrollListBy(ListView r0, int r1) {
            r0.scrollListBy(r1);
        }
    }

    private ListViewCompat() {
    }

    public static boolean canScrollList(ListView r1, int r2) {
        return Api19Impl.canScrollList(r1, r2);
    }

    public static void scrollListBy(ListView r0, int r1) {
        Api19Impl.scrollListBy(r0, r1);
    }
}
