package androidx.core.app;

import android.app.Dialog;
import android.os.Build;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class DialogCompat {

    static class Api28Impl {
        private Api28Impl() {
        }

        static <T> T requireViewById(Dialog r1, int r2) {
            return (T) r1.requireViewById(r2);
        }
    }

    private DialogCompat() {
    }

    public static View requireViewById(Dialog r3, int r4) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        View r0 = r3.findViewById(r4);
        if (r0 == null) goto L10;
        return r0;
    L10:
        throw new IllegalArgumentException("ID does not reference a View inside this Dialog");
    L5:
        return (View) Api28Impl.requireViewById(r3, r4);
    }
}
