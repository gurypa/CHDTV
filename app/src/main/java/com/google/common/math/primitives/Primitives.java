package com.google.common.primitives;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Primitives {
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE = null;
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE = null;

    static {
        LinkedHashMap r0 = new LinkedHashMap(16);
        LinkedHashMap r2 = new LinkedHashMap(16);
        add(r0, r2, Boolean.TYPE, Boolean.class);
        add(r0, r2, Byte.TYPE, Byte.class);
        add(r0, r2, Character.TYPE, Character.class);
        add(r0, r2, Double.TYPE, Double.class);
        add(r0, r2, Float.TYPE, Float.class);
        add(r0, r2, Integer.TYPE, Integer.class);
        add(r0, r2, Long.TYPE, Long.class);
        add(r0, r2, Short.TYPE, Short.class);
        add(r0, r2, Void.TYPE, Void.class);
        PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(r0);
        WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(r2);
    }

    private Primitives() {
    }

    private static void add(Map<Class<?>, Class<?>> r0, Map<Class<?>, Class<?>> r1, Class<?> r2, Class<?> r3) {
        r0.put(r2, r3);
        r1.put(r3, r2);
    }

    public static Set<Class<?>> allPrimitiveTypes() {
        return PRIMITIVE_TO_WRAPPER_TYPE.keySet();
    }

    public static Set<Class<?>> allWrapperTypes() {
        return WRAPPER_TO_PRIMITIVE_TYPE.keySet();
    }

    public static boolean isWrapperType(Class<?> r2) {
        return WRAPPER_TO_PRIMITIVE_TYPE.containsKey(Preconditions.checkNotNull(r2));
    }

    public static <T> Class<T> unwrap(Class<T> r2) {
        Preconditions.checkNotNull(r2);
        Class<T> r0 = (Class) WRAPPER_TO_PRIMITIVE_TYPE.get(r2);
        if (r0 != null) goto L6;
        return r2;
    L6:
        return r0;
    }

    public static <T> Class<T> wrap(Class<T> r2) {
        Preconditions.checkNotNull(r2);
        Class<T> r0 = (Class) PRIMITIVE_TO_WRAPPER_TYPE.get(r2);
        if (r0 != null) goto L6;
        return r2;
    L6:
        return r0;
    }
}
