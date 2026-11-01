package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Comparators {
    private Comparators() {
    }

    public static <T> Collector<T, ?, List<T>> greatest(int r1, Comparator<? super T> r2) {
        return least(r1, r2.reversed());
    }

    public static <T> boolean isInOrder(Iterable<? extends T> r4, Comparator<T> r5) {
        Preconditions.checkNotNull(r5);
        Iterator<? extends T> r0 = r4.iterator();
        if (r0.hasNext() == false) goto L12;
        T r1 = r0.next();
    L6:
        if (r0.hasNext() == false) goto L16;
        T r2 = r0.next();
        if (r5.compare(r1, r2) > 0) goto L9;
        r1 = r2;
        goto L6
    L9:
        return false;
    L16:
        return true;
    L12:
        return true;
    }

    public static <T> boolean isInStrictOrder(Iterable<? extends T> r4, Comparator<T> r5) {
        Preconditions.checkNotNull(r5);
        Iterator<? extends T> r0 = r4.iterator();
        if (r0.hasNext() == false) goto L12;
        T r1 = r0.next();
    L6:
        if (r0.hasNext() == false) goto L16;
        T r2 = r0.next();
        if (r5.compare(r1, r2) >= 0) goto L9;
        r1 = r2;
        goto L6
    L9:
        return false;
    L16:
        return true;
    L12:
        return true;
    }

    static /* synthetic */ TopKSelector lambda$least$0(int r1, Comparator r2) {
        return TopKSelector.least(r1, r2);
    }

    public static <T> Collector<T, ?, List<T>> least(final int r7, final Comparator<? super T> r8) {
        CollectPreconditions.checkNonnegative(r7, "k");
        Preconditions.checkNotNull(r8);
        return Collector.of(new Comparators$$ExternalSyntheticLambda0(r7, r8), new Comparators$$ExternalSyntheticLambda1(), new Comparators$$ExternalSyntheticLambda2(), new Comparators$$ExternalSyntheticLambda3(), new Collector.Characteristics[]{Collector.Characteristics.UNORDERED});
    }

    public static <T, S extends T> Comparator<Iterable<S>> lexicographical(Comparator<T> r2) {
        return new LexicographicalOrdering((Comparator) Preconditions.checkNotNull(r2));
    }

    public static <T extends Comparable<? super T>> T max(T r1, T r2) {
        if (r1.compareTo(r2) < 0) goto L6;
        return r1;
    L6:
        return r2;
    }

    @ParametricNullness
    public static <T> T max(@ParametricNullness T r1, @ParametricNullness T r2, Comparator<? super T> r3) {
        if (r3.compare(r1, r2) < 0) goto L6;
        return r1;
    L6:
        return r2;
    }

    public static <T extends Comparable<? super T>> T min(T r1, T r2) {
        if (r1.compareTo(r2) > 0) goto L6;
        return r1;
    L6:
        return r2;
    }

    @ParametricNullness
    public static <T> T min(@ParametricNullness T r1, @ParametricNullness T r2, Comparator<? super T> r3) {
        if (r3.compare(r1, r2) > 0) goto L6;
        return r1;
    L6:
        return r2;
    }
}
