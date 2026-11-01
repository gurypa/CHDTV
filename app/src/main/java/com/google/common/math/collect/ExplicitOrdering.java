package com.google.common.collect;

import java.io.Serializable;
import java.util.List;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class ExplicitOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final ImmutableMap<T, Integer> rankMap;

    ExplicitOrdering(ImmutableMap<T, Integer> r1) {
        this.rankMap = r1;
    }

    ExplicitOrdering(List<T> r2) {
        this(Maps.indexMap(r2));
    }

    private int rank(T r3) {
        Integer r0 = this.rankMap.get(r3);
        if (r0 == null) goto L7;
        return r0.intValue();
    L7:
        throw new Ordering.IncomparableValueException(r3);
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(T r3, T r4) {
        return rank(r3) - rank(r4);
    }

    @Override // java.util.Comparator
    public boolean equals(@CheckForNull Object r4) {
        if ((r4 instanceof ExplicitOrdering) == true) goto L5;
        return false;
    L5:
        return this.rankMap.equals(((ExplicitOrdering) r4).rankMap);
    }

    public int hashCode() {
        return this.rankMap.hashCode();
    }

    public String toString() {
        return "Ordering.explicit(" + this.rankMap.keySet() + ")";
    }
}
