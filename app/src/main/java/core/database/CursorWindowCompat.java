package androidx.core.database;

import android.database.CursorWindow;
import android.os.Build;

/* JADX INFO: loaded from: classes.dex */
public final class CursorWindowCompat {

    static class Api15Impl {
        private Api15Impl() {
        }

        static CursorWindow createCursorWindow(String r1) {
            return new CursorWindow(r1);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static CursorWindow createCursorWindow(String r1, long r2) {
            return new CursorWindow(r1, r2);
        }
    }

    private CursorWindowCompat() {
    }

    public static CursorWindow create(String r2, long r3) {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return Api28Impl.createCursorWindow(r2, r3);
    L7:
        return Api15Impl.createCursorWindow(r2);
    }
}
