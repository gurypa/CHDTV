package androidx.core.os;

import android.os.Message;

/* JADX INFO: loaded from: classes.dex */
public final class MessageCompat {
    private static boolean sTryIsAsynchronous;
    private static boolean sTrySetAsynchronous;

    static class Api22Impl {
        private Api22Impl() {
        }

        static boolean isAsynchronous(Message r1) {
            return r1.isAsynchronous();
        }

        static void setAsynchronous(Message r0, boolean r1) {
            r0.setAsynchronous(r1);
        }
    }

    static {
        sTrySetAsynchronous = true;
        sTryIsAsynchronous = true;
    }

    private MessageCompat() {
    }

    public static boolean isAsynchronous(Message r1) {
        return Api22Impl.isAsynchronous(r1);
    }

    public static void setAsynchronous(Message r0, boolean r1) {
        Api22Impl.setAsynchronous(r0, r1);
    }
}
