package com.google.common.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class AbstractInvocationHandler implements InvocationHandler {
    private static final Object[] NO_ARGS = null;

    static {
        NO_ARGS = new Object[0];
    }

    public AbstractInvocationHandler() {
    }

    private static boolean isProxyOfSameInterfaces(Object r2, Class<?> r3) {
        if (r3.isInstance(r2) == false) goto L5;
    L10:
        return true;
    L5:
        if (Proxy.isProxyClass(r2.getClass()) == true) goto L7;
    L9:
        return false;
    L7:
        if (Arrays.equals(r2.getClass().getInterfaces(), r3.getInterfaces()) == false) goto L9;
        goto L9
    }

    public boolean equals(@CheckForNull Object r2) {
        return super.equals(r2);
    }

    @CheckForNull
    protected abstract Object handleInvocation(Object r1, Method r2, Object[] r3) throws Throwable;

    public int hashCode() {
        return super.hashCode();
    }

    @Override // java.lang.reflect.InvocationHandler
    @CheckForNull
    public final Object invoke(Object r5, Method r6, @CheckForNull Object[] r7) throws Throwable {
        if (r7 != null) goto L5;
        r7 = NO_ARGS;
    L5:
        if (r7.length == 0) goto L7;
    L10:
        boolean r1 = true;
        if (r7.length != 1) goto L32;
        if (r6.getName().equals("equals") == false) goto L32;
        if (r6.getParameterTypes()[0] != Object.class) goto L32;
        Object r0 = r7[0];
        if (r0 == null) goto L19;
        if (r5 != r0) goto L24;
        return true;
    L24:
        if (isProxyOfSameInterfaces(r0, r5.getClass()) == true) goto L26;
    L28:
        r1 = false;
    L30:
        return Boolean.valueOf(r1);
    L26:
        if (equals(Proxy.getInvocationHandler(r0)) == false) goto L28;
    L19:
        return false;
    L32:
        if (r7.length != 0) goto L38;
        if (r6.getName().equals("toString") == false) goto L38;
        return toString();
    L38:
        return handleInvocation(r5, r6, r7);
    L7:
        if (r6.getName().equals("hashCode") == false) goto L10;
        return Integer.valueOf(hashCode());
    }

    public String toString() {
        return super.toString();
    }
}
