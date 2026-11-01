package com.google.common.collect;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class CompoundOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final Comparator<? super T>[] comparators;

    CompoundOrdering(Iterable<? extends Comparator<? super T>> r2) {
        this.comparators = (Comparator[]) Iterables.toArray(r2, new Comparator[0]);
    }

    CompoundOrdering(Comparator<? super T> r3, Comparator<? super T> r4) {
        this.comparators = new Comparator[]{r3, r4};
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(@ParametricNullness T r3, @ParametricNullness T r4) {
        int r0 = 0;
    L4:
        if (r0 >= this.comparators.length) goto L9;
        int r1 = this.comparators[r0].compare(r3, r4);
        if (r1 != 0) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r1;
    L9:
        return 0;
    }

    @Override // java.util.Comparator
    public boolean equals(@CheckForNull Object r4) {
        if (r4 != this) goto L6;
        return true;
    L6:
        if ((r4 instanceof CompoundOrdering) == true) goto L8;
        return false;
    L8:
        return Arrays.equals(this.comparators, ((CompoundOrdering) r4).comparators);
    }

    public int hashCode() {
        return Arrays.hashCode(this.comparators);
    }

    public String toString() {
        return "Ordering.compound(" + Arrays.toString(this.comparators) + ")";
    }
}
