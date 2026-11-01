package androidx.core.util;

import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class ObjectsCompat {

    static class Api19Impl {
        private Api19Impl() {
        }

        static boolean equals(Object r1, Object r2) {
            return Objects.equals(r1, r2);
        }

        static int hash(Object... r1) {
            return Objects.hash(r1);
        }
    }

    private ObjectsCompat() {
    }

    public static boolean equals(Object r1, Object r2) {
        return Api19Impl.equals(r1, r2);
    }

    public static int hash(Object... r1) {
        return Api19Impl.hash(r1);
    }

    public static int hashCode(Object r1) {
        if (r1 != null) goto L6;
        return 0;
    L6:
        return r1.hashCode();
    }

    public static <T> T requireNonNull(T r1) {
        if (r1 == null) goto L5;
        return r1;
    L5:
        throw new NullPointerException();
    }

    public static <T> T requireNonNull(T r1, String r2) {
        if (r1 == null) goto L5;
        return r1;
    L5:
        throw new NullPointerException(r2);
    }

    public static String toString(Object r1, String r2) {
        if (r1 == null) goto L5;
        return r1.toString();
    L5:
        return r2;
    }
}
