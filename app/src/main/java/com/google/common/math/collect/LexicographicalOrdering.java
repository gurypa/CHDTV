package com.google.common.collect;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class LexicographicalOrdering<T> extends Ordering<Iterable<T>> implements Serializable {
    private static final long serialVersionUID = 0;
    final Comparator<? super T> elementOrder;

    LexicographicalOrdering(Comparator<? super T> r1) {
        this.elementOrder = r1;
    }

    public int compare(Iterable<T> r6, Iterable<T> r7) {
        Iterator<T> r0 = r6.iterator();
        Iterator<T> r1 = r7.iterator();
    L4:
        if (r0.hasNext() == false) goto L14;
        if (r1.hasNext() == false) goto L7;
        int r2 = this.elementOrder.compare(r0.next(), r1.next());
        if (r2 == 0) goto L4;
        return r2;
    L7:
        return 1;
    L14:
        if (r1.hasNext() == false) goto L17;
        return -1;
    L17:
        return 0;
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public /* bridge */ /* synthetic */ int compare(Object r1, Object r2) {
        return compare((Iterable) r1, (Iterable) r2);
    }

    @Override // java.util.Comparator
    public boolean equals(@CheckForNull Object r4) {
        if (r4 != this) goto L6;
        return true;
    L6:
        if ((r4 instanceof LexicographicalOrdering) == true) goto L8;
        return false;
    L8:
        return this.elementOrder.equals(((LexicographicalOrdering) r4).elementOrder);
    }

    public int hashCode() {
        return this.elementOrder.hashCode() ^ 2075626741;
    }

    public String toString() {
        return this.elementOrder + ".lexicographical()";
    }
}
