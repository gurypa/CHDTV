package com.google.common.collect;

import java.io.Serializable;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class NullsLastOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final Ordering<? super T> ordering;

    NullsLastOrdering(Ordering<? super T> r1) {
        this.ordering = r1;
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(@CheckForNull T r2, @CheckForNull T r3) {
        if (r2 != r3) goto L5;
        return 0;
    L5:
        if (r2 != null) goto L8;
        return 1;
    L8:
        if (r3 != null) goto L12;
        return -1;
    L12:
        return this.ordering.compare(r2, r3);
    }

    @Override // java.util.Comparator
    public boolean equals(@CheckForNull Object r4) {
        if (r4 != this) goto L6;
        return true;
    L6:
        if ((r4 instanceof NullsLastOrdering) == true) goto L8;
        return false;
    L8:
        return this.ordering.equals(((NullsLastOrdering) r4).ordering);
    }

    public int hashCode() {
        return this.ordering.hashCode() ^ (-921210296);
    }

    @Override // com.google.common.collect.Ordering
    public <S extends T> Ordering<S> nullsFirst() {
        return this.ordering.nullsFirst();
    }

    @Override // com.google.common.collect.Ordering
    public <S extends T> Ordering<S> nullsLast() {
        return this;
    }

    @Override // com.google.common.collect.Ordering
    public <S extends T> Ordering<S> reverse() {
        return this.ordering.reverse().nullsFirst();
    }

    public String toString() {
        return this.ordering + ".nullsLast()";
    }
}
