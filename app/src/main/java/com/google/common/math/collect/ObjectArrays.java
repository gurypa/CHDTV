package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class ObjectArrays {
    private ObjectArrays() {
    }

    static Object checkElementNotNull(@CheckForNull Object r3, int r4) {
        if (r3 == null) goto L5;
        return r3;
    L5:
        throw new NullPointerException("at index " + r4);
    }

    static Object[] checkElementsNotNull(Object... r1) {
        checkElementsNotNull(r1, r1.length);
        return r1;
    }

    static Object[] checkElementsNotNull(Object[] r2, int r3) {
        int r0 = 0;
    L3:
        if (r0 >= r3) goto L5;
        checkElementNotNull(r2[r0], r0);
        r0 = r0 + 1;
        goto L3
    L5:
        return r2;
    }

    public static <T> T[] concat(@ParametricNullness T r4, T[] r5) {
        T[] r0 = (T[]) newArray(r5, r5.length + 1);
        r0[0] = r4;
        System.arraycopy(r5, 0, r0, 1, r5.length);
        return r0;
    }

    public static <T> T[] concat(T[] r2, @ParametricNullness T r3) {
        T[] r0 = (T[]) Arrays.copyOf(r2, r2.length + 1);
        r0[r2.length] = r3;
        return r0;
    }

    public static <T> T[] concat(T[] r4, T[] r5, Class<T> r6) {
        T[] r0 = (T[]) newArray(r6, r4.length + r5.length);
        System.arraycopy(r4, 0, r0, 0, r4.length);
        System.arraycopy(r5, 0, r0, r4.length, r5.length);
        return r0;
    }

    static Object[] copyAsObjectArray(Object[] r2, int r3, int r4) {
        Preconditions.checkPositionIndexes(r3, r3 + r4, r2.length);
        if (r4 == 0) goto L5;
        Object[] r1 = new Object[r4];
        System.arraycopy(r2, r3, r1, 0, r4);
        return r1;
    L5:
        return new Object[0];
    }

    private static Object[] fillArray(Iterable<?> r4, Object[] r5) {
        int r0 = 0;
        Iterator<?> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r5[r0] = r1.next();
        r0 = r0 + 1;
        goto L4
    L6:
        return r5;
    }

    public static <T> T[] newArray(Class<T> r1, int r2) {
        return (T[]) ((Object[]) Array.newInstance(r1, r2));
    }

    public static <T> T[] newArray(T[] r1, int r2) {
        return (T[]) Platform.newArray(r1, r2);
    }

    static void swap(Object[] r2, int r3, int r4) {
        Object r0 = r2[r3];
        r2[r3] = r2[r4];
        r2[r4] = r0;
    }

    static Object[] toArrayImpl(Collection<?> r1) {
        return fillArray(r1, new Object[r1.size()]);
    }

    static <T> T[] toArrayImpl(Collection<?> r3, T[] r4) {
        int r0 = r3.size();
        if (r4.length >= r0) goto L5;
        r4 = (T[]) newArray(r4, r0);
    L5:
        fillArray(r3, r4);
        if (r4.length <= r0) goto L8;
        r4[r0] = null;
    L8:
        return r4;
    }

    static <T> T[] toArrayImpl(Object[] r2, int r3, int r4, T[] r5) {
        Preconditions.checkPositionIndexes(r3, r3 + r4, r2.length);
        if (r5.length >= r4) goto L6;
        r5 = (T[]) newArray(r5, r4);
    L8:
        System.arraycopy(r2, r3, r5, 0, r4);
        return r5;
    L6:
        if (r5.length <= r4) goto L8;
        r5[r4] = null;
        goto L8
    }
}
