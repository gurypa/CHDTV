package com.google.common.base;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Enums {
    private static final Map<Class<? extends Enum<?>>, Map<String, WeakReference<? extends Enum<?>>>> enumConstantCache = null;

    private static final class StringConverter<T extends Enum<T>> extends Converter<String, T> implements Serializable {
        private static final long serialVersionUID = 0;
        private final Class<T> enumClass;

        StringConverter(Class<T> r2) {
            this.enumClass = (Class) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.base.Converter
        protected /* bridge */ /* synthetic */ String doBackward(Object r1) {
            return doBackward((Enum) r1);
        }

        protected String doBackward(T r2) {
            return r2.name();
        }

        /* JADX INFO: renamed from: doForward, reason: avoid collision after fix types in other method */
        protected T doForward2(String r2) {
            return (T) Enum.valueOf(this.enumClass, r2);
        }

        @Override // com.google.common.base.Converter
        protected /* bridge */ /* synthetic */ Object doForward(String r1) {
            return doForward2(r1);
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof StringConverter) == true) goto L5;
            return false;
        L5:
            return this.enumClass.equals(((StringConverter) r4).enumClass);
        }

        public int hashCode() {
            return this.enumClass.hashCode();
        }

        public String toString() {
            return "Enums.stringConverter(" + this.enumClass.getName() + ".class)";
        }
    }

    static {
        enumConstantCache = new WeakHashMap();
    }

    private Enums() {
    }

    static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> getEnumConstants(Class<T> r3) {
        Map<Class<? extends Enum<?>>, Map<String, WeakReference<? extends Enum<?>>>> r0 = enumConstantCache;
        monitor-enter(r0);
        Map<String, WeakReference<? extends Enum<?>>> r1 = enumConstantCache.get(r3);     // Catch: Throwable -> L9
        if (r1 != null) goto L7;
        r1 = populateCache(r3);     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
        return r1;
    L9:
        th = move-exception;
        throw th;
    }

    public static Field getField(Enum<?> r3) {
        Class<E> r0 = r3.getDeclaringClass();
        return r0.getDeclaredField(r3.name());
    L5:
        e = move-exception;
        throw new AssertionError(e);
    }

    public static <T extends Enum<T>> Optional<T> getIfPresent(Class<T> r1, String r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return Platform.getEnumIfPresent(r1, r2);
    }

    private static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> populateCache(Class<T> r5) {
        HashMap r0 = new HashMap();
        Iterator r1 = EnumSet.allOf(r5).iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        Enum r2 = (Enum) r1.next();
        r0.put(r2.name(), new WeakReference(r2));
        goto L4
    L6:
        enumConstantCache.put(r5, r0);
        return r0;
    }

    public static <T extends Enum<T>> Converter<String, T> stringConverter(Class<T> r1) {
        return new StringConverter(r1);
    }
}
