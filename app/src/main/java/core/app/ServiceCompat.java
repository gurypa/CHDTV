package androidx.core.app;

import android.app.Service;
import android.os.Build;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public final class ServiceCompat {
    public static final int START_STICKY = 1;
    public static final int STOP_FOREGROUND_DETACH = 2;
    public static final int STOP_FOREGROUND_REMOVE = 1;

    static class Api24Impl {
        private Api24Impl() {
        }

        static void stopForeground(Service r0, int r1) {
            r0.stopForeground(r1);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface StopForegroundFlags {
    }

    private ServiceCompat() {
    }

    public static void stopForeground(Service r2, int r3) {
        if (Build.VERSION.SDK_INT < 24) goto L6;
        Api24Impl.stopForeground(r2, r3);
        return;
    L6:
        if ((r3 & 1) == 0) goto L8;
        boolean r0 = true;
    L9:
        r2.stopForeground(r0);
        return;
    L8:
        r0 = false;
        goto L9
    }
}
