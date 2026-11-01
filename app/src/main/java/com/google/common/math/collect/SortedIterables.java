package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.SortedSet;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class SortedIterables {
    private SortedIterables() {
    }

    public static <E> Comparator<? super E> comparator(SortedSet<E> r1) {
        Comparator<? super E> r0 = r1.comparator();
        if (r0 == null) goto L5;
        return r0;
    L5:
        return Ordering.natural();
    }

    public static boolean hasSameComparator(Comparator<?> r2, Iterable<?> r3) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        if ((r3 instanceof SortedSet) == false) goto L6;
        Comparator r0 = comparator((SortedSet) r3);
    L9:
        return r2.equals(r0);
    L6:
        if ((r3 instanceof SortedIterable) == false) goto L10;
        r0 = ((SortedIterable) r3).comparator();
        goto L9
    L10:
        return false;
    }
}
