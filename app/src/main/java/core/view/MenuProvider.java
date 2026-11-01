package androidx.core.view;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/* JADX INFO: loaded from: classes.dex */
public interface MenuProvider {

    /* JADX INFO: renamed from: androidx.core.view.MenuProvider$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onMenuClosed(MenuProvider r0, Menu r1) {
        }

        public static void $default$onPrepareMenu(MenuProvider r0, Menu r1) {
        }
    }

    void onCreateMenu(Menu r1, MenuInflater r2);

    void onMenuClosed(Menu r1);

    boolean onMenuItemSelected(MenuItem r1);

    void onPrepareMenu(Menu r1);
}
