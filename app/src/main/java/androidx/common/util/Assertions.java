package androidx.media3.common.util;

import android.os.Looper;
import android.text.TextUtils;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.dataflow.qual.Pure;

/* JADX INFO: loaded from: classes.dex */
public final class Assertions {
    private Assertions() {
    }

    @Pure
    public static void checkArgument(boolean r1) {
        if (r1 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException();
    }

    @Pure
    public static void checkArgument(boolean r2, Object r3) {
        if (r2 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(String.valueOf(r3));
    }

    @Pure
    public static int checkIndex(int r1, int r2, int r3) {
        if (r1 < r2) goto L6;
        if (r1 >= r3) goto L6;
        return r1;
    L6:
        throw new IndexOutOfBoundsException();
    }

    @Pure
    public static void checkMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) goto L6;
        return;
    L6:
        throw new IllegalStateException("Not in applications main thread");
    }

    @EnsuresNonNull({"#1"})
    @Pure
    public static String checkNotEmpty(String r1) {
        if (TextUtils.isEmpty(r1) == true) goto L6;
        return r1;
    L6:
        throw new IllegalArgumentException();
    }

    @EnsuresNonNull({"#1"})
    @Pure
    public static String checkNotEmpty(String r2, Object r3) {
        if (TextUtils.isEmpty(r2) == true) goto L6;
        return r2;
    L6:
        throw new IllegalArgumentException(String.valueOf(r3));
    }

    @EnsuresNonNull({"#1"})
    @Pure
    public static <T> T checkNotNull(T r1) {
        if (r1 == null) goto L5;
        return r1;
    L5:
        throw new NullPointerException();
    }

    @EnsuresNonNull({"#1"})
    @Pure
    public static <T> T checkNotNull(T r2, Object r3) {
        if (r2 == null) goto L5;
        return r2;
    L5:
        throw new NullPointerException(String.valueOf(r3));
    }

    @Pure
    public static void checkState(boolean r1) {
        if (r1 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException();
    }

    @Pure
    public static void checkState(boolean r2, Object r3) {
        if (r2 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(String.valueOf(r3));
    }

    @EnsuresNonNull({"#1"})
    @Pure
    public static <T> T checkStateNotNull(T r1) {
        if (r1 == null) goto L5;
        return r1;
    L5:
        throw new IllegalStateException();
    }

    @EnsuresNonNull({"#1"})
    @Pure
    public static <T> T checkStateNotNull(T r2, Object r3) {
        if (r2 == null) goto L5;
        return r2;
    L5:
        throw new IllegalStateException(String.valueOf(r3));
    }
}
