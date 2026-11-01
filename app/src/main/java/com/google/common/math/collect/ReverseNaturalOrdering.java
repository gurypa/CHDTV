package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class ReverseNaturalOrdering extends Ordering<Comparable<?>> implements Serializable {
    static final ReverseNaturalOrdering INSTANCE = null;
    private static final long serialVersionUID = 0;

    static {
        INSTANCE = new ReverseNaturalOrdering();
    }

    private ReverseNaturalOrdering() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public int compare(Comparable<?> r2, Comparable<?> r3) {
        Preconditions.checkNotNull(r2);
        if (r2 != r3) goto L7;
        return 0;
    L7:
        return r3.compareTo(r2);
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public /* bridge */ /* synthetic */ int compare(Object r1, Object r2) {
        return compare((Comparable) r1, (Comparable) r2);
    }

    public <E extends Comparable<?>> E max(E r2, E r3) {
        return (E) NaturalOrdering.INSTANCE.min(r2, r3);
    }

    public <E extends Comparable<?>> E max(E r2, E r3, E r4, E... r5) {
        return (E) NaturalOrdering.INSTANCE.min(r2, r3, r4, r5);
    }

    @Override // com.google.common.collect.Ordering
    public <E extends Comparable<?>> E max(Iterable<E> r2) {
        return (E) NaturalOrdering.INSTANCE.min(r2);
    }

    @Override // com.google.common.collect.Ordering
    public <E extends Comparable<?>> E max(Iterator<E> r2) {
        return (E) NaturalOrdering.INSTANCE.min(r2);
    }

    @Override // com.google.common.collect.Ordering
    public /* bridge */ /* synthetic */ Object max(Iterable r1) {
        return max(r1);
    }

    @Override // com.google.common.collect.Ordering
    public /* bridge */ /* synthetic */ Object max(Object r1, Object r2) {
        return max((Comparable) r1, (Comparable) r2);
    }

    @Override // com.google.common.collect.Ordering
    public /* bridge */ /* synthetic */ Object max(Object r1, Object r2, Object r3, Object[] r4) {
        return max((Comparable) r1, (Comparable) r2, (Comparable) r3, (Comparable[]) r4);
    }

    @Override // com.google.common.collect.Ordering
    public /* bridge */ /* synthetic */ Object max(Iterator r1) {
        return max(r1);
    }

    public <E extends Comparable<?>> E min(E r2, E r3) {
        return (E) NaturalOrdering.INSTANCE.max(r2, r3);
    }

    public <E extends Comparable<?>> E min(E r2, E r3, E r4, E... r5) {
        return (E) NaturalOrdering.INSTANCE.max(r2, r3, r4, r5);
    }

    @Override // com.google.common.collect.Ordering
    public <E extends Comparable<?>> E min(Iterable<E> r2) {
        return (E) NaturalOrdering.INSTANCE.max(r2);
    }

    @Override // com.google.common.collect.Ordering
    public <E extends Comparable<?>> E min(Iterator<E> r2) {
        return (E) NaturalOrdering.INSTANCE.max(r2);
    }

    @Override // com.google.common.collect.Ordering
    public /* bridge */ /* synthetic */ Object min(Iterable r1) {
        return min(r1);
    }

    @Override // com.google.common.collect.Ordering
    public /* bridge */ /* synthetic */ Object min(Object r1, Object r2) {
        return min((Comparable) r1, (Comparable) r2);
    }

    @Override // com.google.common.collect.Ordering
    public /* bridge */ /* synthetic */ Object min(Object r1, Object r2, Object r3, Object[] r4) {
        return min((Comparable) r1, (Comparable) r2, (Comparable) r3, (Comparable[]) r4);
    }

    @Override // com.google.common.collect.Ordering
    public /* bridge */ /* synthetic */ Object min(Iterator r1) {
        return min(r1);
    }

    @Override // com.google.common.collect.Ordering
    public <S extends Comparable<?>> Ordering<S> reverse() {
        return Ordering.natural();
    }

    public String toString() {
        return "Ordering.natural().reverse()";
    }
}
