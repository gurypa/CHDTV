package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class NaturalOrdering extends Ordering<Comparable<?>> implements Serializable {
    static final NaturalOrdering INSTANCE = null;
    private static final long serialVersionUID = 0;

    @CheckForNull
    @LazyInit
    private transient Ordering<Comparable<?>> nullsFirst;

    @CheckForNull
    @LazyInit
    private transient Ordering<Comparable<?>> nullsLast;

    static {
        INSTANCE = new NaturalOrdering();
    }

    private NaturalOrdering() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public int compare(Comparable<?> r2, Comparable<?> r3) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        return r2.compareTo(r3);
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public /* bridge */ /* synthetic */ int compare(Object r1, Object r2) {
        return compare((Comparable) r1, (Comparable) r2);
    }

    @Override // com.google.common.collect.Ordering
    public <S extends Comparable<?>> Ordering<S> nullsFirst() {
        Ordering<S> r0 = (Ordering<S>) this.nullsFirst;
        if (r0 != null) goto L6;
        Ordering<S> r1 = super.nullsFirst();
        this.nullsFirst = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.Ordering
    public <S extends Comparable<?>> Ordering<S> nullsLast() {
        Ordering<S> r0 = (Ordering<S>) this.nullsLast;
        if (r0 != null) goto L6;
        Ordering<S> r1 = super.nullsLast();
        this.nullsLast = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.Ordering
    public <S extends Comparable<?>> Ordering<S> reverse() {
        return ReverseNaturalOrdering.INSTANCE;
    }

    public String toString() {
        return "Ordering.natural()";
    }
}
