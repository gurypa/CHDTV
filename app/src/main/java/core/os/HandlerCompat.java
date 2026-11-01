package androidx.core.os;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public final class HandlerCompat {
    private static final String TAG = "HandlerCompat";

    private static class Api28Impl {
        private Api28Impl() {
        }

        public static Handler createAsync(Looper r1) {
            return Handler.createAsync(r1);
        }

        public static Handler createAsync(Looper r1, Handler.Callback r2) {
            return Handler.createAsync(r1, r2);
        }

        public static boolean postDelayed(Handler r1, Runnable r2, Object r3, long r4) {
            return r1.postDelayed(r2, r3, r4);
        }
    }

    private static class Api29Impl {
        private Api29Impl() {
        }

        public static boolean hasCallbacks(Handler r1, Runnable r2) {
            return r1.hasCallbacks(r2);
        }
    }

    private HandlerCompat() {
    }

    public static Handler createAsync(Looper r7) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        return (Handler) Handler.class.getDeclaredConstructor(new Class[]{Looper.class, Handler.Callback.class, Boolean.TYPE}).newInstance(new Object[]{r7, null, true});
    L24:
        e = e;
    L25:
        Log.w(TAG, "Unable to invoke Handler(Looper, Callback, boolean) constructor", e);
        return new Handler(r7);
    L22:
        e = e;
    L20:
        e = e;
    L9:
        e = move-exception;
        Throwable r1 = e.getCause();
        if ((r1 instanceof RuntimeException) == true) goto L19;
        if ((r1 instanceof Error) == false) goto L17;
        throw ((Error) r1);
    L17:
        throw new RuntimeException(r1);
    L19:
        throw ((RuntimeException) r1);
    L5:
        return Api28Impl.createAsync(r7);
    }

    public static Handler createAsync(Looper r7, Handler.Callback r8) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        return (Handler) Handler.class.getDeclaredConstructor(new Class[]{Looper.class, Handler.Callback.class, Boolean.TYPE}).newInstance(new Object[]{r7, r8, true});
    L24:
        e = e;
    L25:
        Log.w(TAG, "Unable to invoke Handler(Looper, Callback, boolean) constructor", e);
        return new Handler(r7, r8);
    L22:
        e = e;
    L20:
        e = e;
    L9:
        e = move-exception;
        Throwable r1 = e.getCause();
        if ((r1 instanceof RuntimeException) == true) goto L19;
        if ((r1 instanceof Error) == false) goto L17;
        throw ((Error) r1);
    L17:
        throw new RuntimeException(r1);
    L19:
        throw ((RuntimeException) r1);
    L5:
        return Api28Impl.createAsync(r7, r8);
    }

    public static boolean hasCallbacks(Handler r7, Runnable r8) {
        if (Build.VERSION.SDK_INT >= 29) goto L5;
        return ((Boolean) Handler.class.getMethod("hasCallbacks", new Class[]{Runnable.class}).invoke(r7, new Object[]{r8})).booleanValue();
    L13:
        e = move-exception;
        Throwable r0 = e;
    L17:
        throw new UnsupportedOperationException("Failed to call Handler.hasCallbacks(), but there is no safe failure mode for this method. Raising exception.", r0);
    L11:
        e = move-exception;
        r0 = e;
    L9:
        e = move-exception;
        r0 = e;
    L18:
        e = move-exception;
        Throwable r2 = e.getCause();
        if ((r2 instanceof RuntimeException) == true) goto L28;
        if ((r2 instanceof Error) == false) goto L26;
        throw ((Error) r2);
    L26:
        throw new RuntimeException(r2);
    L28:
        throw ((RuntimeException) r2);
    L5:
        return Api29Impl.hasCallbacks(r7, r8);
    }

    public static boolean postDelayed(Handler r2, Runnable r3, Object r4, long r5) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        Message r0 = Message.obtain(r2, r3);
        r0.obj = r4;
        return r2.sendMessageDelayed(r0, r5);
    L5:
        return Api28Impl.postDelayed(r2, r3, r4, r5);
    }
}
