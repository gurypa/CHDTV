package com.google.common.base;

import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class PairwiseEquivalence<E, T extends E> extends Equivalence<Iterable<T>> implements Serializable {
    private static final long serialVersionUID = 1;
    final Equivalence<E> elementEquivalence;

    PairwiseEquivalence(Equivalence<E> r2) {
        this.elementEquivalence = (Equivalence) Preconditions.checkNotNull(r2);
    }

    protected boolean doEquivalent(Iterable<T> r7, Iterable<T> r8) {
        Iterator<T> r0 = r7.iterator();
        Iterator<T> r1 = r8.iterator();
    L4:
        if (r0.hasNext() == false) goto L11;
        if (r1.hasNext() == false) goto L11;
        if (this.elementEquivalence.equivalent(r0.next(), r1.next()) == true) goto L4;
        return false;
    L11:
        if (r0.hasNext() == false) goto L13;
        return false;
    L13:
        if (r1.hasNext() == true) goto L21;
        return true;
    L21:
        return false;
    }

    @Override // com.google.common.base.Equivalence
    protected /* bridge */ /* synthetic */ boolean doEquivalent(Object r1, Object r2) {
        return doEquivalent((Iterable) r1, (Iterable) r2);
    }

    protected int doHash(Iterable<T> r6) {
        int r0 = 78721;
        Iterator<T> r1 = r6.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        T r2 = r1.next();
        r0 = (r0 * 24943) + this.elementEquivalence.hash(r2);
        goto L4
    L6:
        return r0;
    }

    @Override // com.google.common.base.Equivalence
    protected /* bridge */ /* synthetic */ int doHash(Object r1) {
        return doHash((Iterable) r1);
    }

    public boolean equals(@CheckForNull Object r4) {
        if ((r4 instanceof PairwiseEquivalence) == true) goto L5;
        return false;
    L5:
        return this.elementEquivalence.equals(((PairwiseEquivalence) r4).elementEquivalence);
    }

    public int hashCode() {
        return this.elementEquivalence.hashCode() ^ 1185147655;
    }

    public String toString() {
        return this.elementEquivalence + ".pairwise()";
    }
}
