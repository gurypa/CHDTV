package com.google.common.base;

import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Defaults {
    private static final Double DOUBLE_DEFAULT = null;
    private static final Float FLOAT_DEFAULT = null;

    static {
        DOUBLE_DEFAULT = Double.valueOf(0.0d);
        FLOAT_DEFAULT = Float.valueOf(0.0f);
    }

    private Defaults() {
    }

    @CheckForNull
    public static <T> T defaultValue(Class<T> r2) {
        Preconditions.checkNotNull(r2);
        if (r2.isPrimitive() == true) goto L5;
        return null;
    L5:
        if (r2 != Boolean.TYPE) goto L9;
        return (T) Boolean.FALSE;
    L9:
        if (r2 != Character.TYPE) goto L13;
        return (T) (char) 0;
    L13:
        if (r2 != Byte.TYPE) goto L17;
        return (T) (byte) 0;
    L17:
        if (r2 != Short.TYPE) goto L21;
        return (T) (short) 0;
    L21:
        if (r2 != Integer.TYPE) goto L25;
        return (T) 0;
    L25:
        if (r2 != Long.TYPE) goto L29;
        return (T) 0L;
    L29:
        if (r2 != Float.TYPE) goto L33;
        return (T) FLOAT_DEFAULT;
    L33:
        if (r2 == Double.TYPE) goto L35;
        return null;
    L35:
        return (T) DOUBLE_DEFAULT;
    }
}
