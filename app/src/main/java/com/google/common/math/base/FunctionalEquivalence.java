package com.google.common.base;

import java.io.Serializable;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class FunctionalEquivalence<F, T> extends Equivalence<F> implements Serializable {
    private static final long serialVersionUID = 0;
    private final Function<? super F, ? extends T> function;
    private final Equivalence<T> resultEquivalence;

    FunctionalEquivalence(Function<? super F, ? extends T> r2, Equivalence<T> r3) {
        this.function = (Function) Preconditions.checkNotNull(r2);
        this.resultEquivalence = (Equivalence) Preconditions.checkNotNull(r3);
    }

    @Override // com.google.common.base.Equivalence
    protected boolean doEquivalent(F r4, F r5) {
        return this.resultEquivalence.equivalent(this.function.apply(r4), this.function.apply(r5));
    }

    @Override // com.google.common.base.Equivalence
    protected int doHash(F r3) {
        return this.resultEquivalence.hash(this.function.apply(r3));
    }

    public boolean equals(@CheckForNull Object r6) {
        if (r6 != this) goto L6;
        return true;
    L6:
        if ((r6 instanceof FunctionalEquivalence) == false) goto L14;
        FunctionalEquivalence r1 = (FunctionalEquivalence) r6;
        if (this.function.equals(r1.function) == false) goto L13;
        if (this.resultEquivalence.equals(r1.resultEquivalence) == false) goto L13;
        return true;
    L13:
        return false;
    L14:
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.function, this.resultEquivalence});
    }

    public String toString() {
        return this.resultEquivalence + ".onResultOf(" + this.function + ")";
    }
}
