package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class ReverseOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final Ordering<? super T> forwardOrder;

    ReverseOrdering(Ordering<? super T> r2) {
        this.forwardOrder = (Ordering) Preconditions.checkNotNull(r2);
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(@ParametricNullness T r2, @ParametricNullness T r3) {
        return this.forwardOrder.compare(r3, r2);
    }

    @Override // java.util.Comparator
    public boolean equals(@CheckForNull Object r4) {
        if (r4 != this) goto L6;
        return true;
    L6:
        if ((r4 instanceof ReverseOrdering) == true) goto L8;
        return false;
    L8:
        return this.forwardOrder.equals(((ReverseOrdering) r4).forwardOrder);
    }

    public int hashCode() {
        return -this.forwardOrder.hashCode();
    }

    @Override // com.google.common.collect.Ordering
    public <E extends T> E max(Iterable<E> r2) {
        return (E) this.forwardOrder.min(r2);
    }

    @Override // com.google.common.collect.Ordering
    public <E extends T> E max(@ParametricNullness E r2, @ParametricNullness E r3) {
        return (E) this.forwardOrder.min(r2, r3);
    }

    @Override // com.google.common.collect.Ordering
    public <E extends T> E max(@ParametricNullness E r2, @ParametricNullness E r3, @ParametricNullness E r4, E... r5) {
        return (E) this.forwardOrder.min(r2, r3, r4, r5);
    }

    @Override // com.google.common.collect.Ordering
    public <E extends T> E max(Iterator<E> r2) {
        return (E) this.forwardOrder.min(r2);
    }

    @Override // com.google.common.collect.Ordering
    public <E extends T> E min(Iterable<E> r2) {
        return (E) this.forwardOrder.max(r2);
    }

    @Override // com.google.common.collect.Ordering
    public <E extends T> E min(@ParametricNullness E r2, @ParametricNullness E r3) {
        return (E) this.forwardOrder.max(r2, r3);
    }

    @Override // com.google.common.collect.Ordering
    public <E extends T> E min(@ParametricNullness E r2, @ParametricNullness E r3, @ParametricNullness E r4, E... r5) {
        return (E) this.forwardOrder.max(r2, r3, r4, r5);
    }

    @Override // com.google.common.collect.Ordering
    public <E extends T> E min(Iterator<E> r2) {
        return (E) this.forwardOrder.max(r2);
    }

    @Override // com.google.common.collect.Ordering
    public <S extends T> Ordering<S> reverse() {
        return this.forwardOrder;
    }

    public String toString() {
        return this.forwardOrder + ".reverse()";
    }
}
