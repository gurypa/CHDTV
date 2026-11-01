package es.antonio.m3u;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes3.dex */
final class DialogHandoff {
    private static final Map<Activity, List<Dialog>> windows = null;

    static {
        windows = new WeakHashMap();
    }

    private DialogHandoff() {
    }

    static void close(Activity r4) {
        List<Dialog> r0 = windows.remove(r4);
        if (r0 == null) goto L10;
        Iterator r1 = new ArrayList(r0).iterator();
    L6:
        if (r1.hasNext() == false) goto L16;
        Dialog r2 = (Dialog) r1.next();
        if (r2.isShowing() == false) goto L6;
        r2.dismiss();
        goto L6
    L16:
        return;
    }

    static /* synthetic */ void lambda$track$0(Activity r2, Dialog r3, DialogInterface r4) {
        List<Dialog> r0 = windows.get(r2);
        if (r0 == null) goto L8;
        r0.remove(r3);
        if (r0.isEmpty() == false) goto L9;
        windows.remove(r2);
        return;
    L9:
        return;
    }

    static void track(final Activity r2, final Dialog r3) {
        r3.setOwnerActivity(r2);
        List<Dialog> r0 = windows.get(r2);
        if (r0 != null) goto L5;
        r0 = new ArrayList();
        windows.put(r2, r0);
    L5:
        r0.add(r3);
        r3.setOnDismissListener(new DialogHandoff$$ExternalSyntheticLambda0(r2, r3));
    }
}
