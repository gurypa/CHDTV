package com.google.common.collect;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class Platform {
    private Platform() {
    }

    static <T> T[] copy(Object[] r1, int r2, int r3, T[] r4) {
        return (T[]) Arrays.copyOfRange(r1, r2, r3, r4.getClass());
    }

    static <E extends Enum<E>> Class<E> getDeclaringClassOrObjectForJ2cl(E r1) {
        return r1.getDeclaringClass();
    }

    static <T> T[] newArray(T[] r2, int r3) {
        if (r2.length != 0) goto L5;
        Object[] r0 = r2;
    L7:
        return (T[]) Arrays.copyOf(r0, r3);
    L5:
        r0 = Arrays.copyOf(r2, 0);
        goto L7
    }

    static <K, V> Map<K, V> newHashMapWithExpectedSize(int r1) {
        return CompactHashMap.createWithExpectedSize(r1);
    }

    static <E> Set<E> newHashSetWithExpectedSize(int r1) {
        return CompactHashSet.createWithExpectedSize(r1);
    }

    static <K, V> Map<K, V> newLinkedHashMapWithExpectedSize(int r1) {
        return CompactLinkedHashMap.createWithExpectedSize(r1);
    }

    static <E> Set<E> newLinkedHashSetWithExpectedSize(int r1) {
        return CompactLinkedHashSet.createWithExpectedSize(r1);
    }

    static <E> Set<E> preservesInsertionOrderOnAddsSet() {
        return CompactHashSet.create();
    }

    static <K, V> Map<K, V> preservesInsertionOrderOnPutsMap() {
        return CompactHashMap.create();
    }

    static <K, V> Map<K, V> preservesInsertionOrderOnPutsMapWithExpectedSize(int r1) {
        return Maps.newLinkedHashMapWithExpectedSize(r1);
    }

    static int reduceExponentIfGwt(int r0) {
        return r0;
    }

    static int reduceIterationsIfGwt(int r0) {
        return r0;
    }

    static MapMaker tryWeakKeys(MapMaker r1) {
        return r1.weakKeys();
    }
}
