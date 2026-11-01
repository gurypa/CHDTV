package com.google.common.reflect;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Reflection {
    private Reflection() {
    }

    public static String getPackageName(Class<?> r1) {
        return getPackageName(r1.getName());
    }

    public static String getPackageName(String r2) {
        int r0 = r2.lastIndexOf(46);
        if (r0 >= 0) goto L6;
        return "";
    L6:
        return r2.substring(0, r0);
    }

    public static void initialize(Class<?>... r6) {
        int r0 = r6.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L10;
        Class<?> r2 = r6[r1];
        Class.forName(r2.getName(), true, r2.getClassLoader());     // Catch: ClassNotFoundException -> L7
        r1 = r1 + 1;
    L7:
        e = move-exception;
        throw new AssertionError(e);
    }

    public static <T> T newProxy(Class<T> r3, InvocationHandler r4) {
        Preconditions.checkNotNull(r4);
        Preconditions.checkArgument(r3.isInterface(), "%s is not an interface", r3);
        return r3.cast(Proxy.newProxyInstance(r3.getClassLoader(), new Class[]{r3}, r4));
    }
}
