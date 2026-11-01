package com.google.common.util.concurrent;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Atomics {
    private Atomics() {
    }

    public static <V> AtomicReference<V> newReference() {
        return new AtomicReference();
    }

    public static <V> AtomicReference<V> newReference(@ParametricNullness V r1) {
        return new AtomicReference(r1);
    }

    public static <E> AtomicReferenceArray<E> newReferenceArray(int r1) {
        return new AtomicReferenceArray(r1);
    }

    public static <E> AtomicReferenceArray<E> newReferenceArray(E[] r1) {
        return new AtomicReferenceArray(r1);
    }
}
