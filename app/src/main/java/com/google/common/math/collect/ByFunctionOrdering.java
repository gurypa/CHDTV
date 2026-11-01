package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class ByFunctionOrdering<F, T> extends Ordering<F> implements Serializable {
    private static final long serialVersionUID = 0;
    final Function<F, ? extends T> function;
    final Ordering<T> ordering;

    ByFunctionOrdering(Function<F, ? extends T> r2, Ordering<T> r3) {
        this.function = (Function) Preconditions.checkNotNull(r2);
        this.ordering = (Ordering) Preconditions.checkNotNull(r3);
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(@ParametricNullness F r4, @ParametricNullness F r5) {
        return this.ordering.compare(this.function.apply(r4), this.function.apply(r5));
    }

    @Override // java.util.Comparator
    public boolean equals(@CheckForNull Object r6) {
        if (r6 != this) goto L6;
        return true;
    L6:
        if ((r6 instanceof ByFunctionOrdering) == false) goto L14;
        ByFunctionOrdering r1 = (ByFunctionOrdering) r6;
        if (this.function.equals(r1.function) == false) goto L13;
        if (this.ordering.equals(r1.ordering) == false) goto L13;
        return true;
    L13:
        return false;
    L14:
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.function, this.ordering});
    }

    public String toString() {
        return this.ordering + ".onResultOf(" + this.function + ")";
    }
}
