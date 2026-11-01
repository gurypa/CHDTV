package com.google.common.base;

import java.io.Serializable;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class Equivalence<T> {

    /* JADX INFO: renamed from: com.google.common.base.Equivalence$1 */
    static /* synthetic */ class C05371 {
    }

    static final class Equals extends Equivalence<Object> implements Serializable {
        static final Equals INSTANCE = null;
        private static final long serialVersionUID = 1;

        static {
            INSTANCE = new Equals();
        }

        Equals() {
        }

        private Object readResolve() {
            return INSTANCE;
        }

        @Override // com.google.common.base.Equivalence
        protected boolean doEquivalent(Object r2, Object r3) {
            return r2.equals(r3);
        }

        @Override // com.google.common.base.Equivalence
        protected int doHash(Object r2) {
            return r2.hashCode();
        }
    }

    private static final class EquivalentToPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Equivalence<T> equivalence;

        @CheckForNull
        private final T target;

        EquivalentToPredicate(Equivalence<T> r2, @CheckForNull T r3) {
            this.equivalence = (Equivalence) Preconditions.checkNotNull(r2);
            this.target = r3;
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@CheckForNull T r3) {
            return this.equivalence.equivalent(r3, this.target);
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof EquivalentToPredicate) == false) goto L14;
            EquivalentToPredicate r1 = (EquivalentToPredicate) r6;
            if (this.equivalence.equals(r1.equivalence) == false) goto L13;
            if (Objects.equal(this.target, r1.target) == false) goto L13;
            return true;
        L13:
            return false;
        L14:
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{this.equivalence, this.target});
        }

        public String toString() {
            return this.equivalence + ".equivalentTo(" + this.target + ")";
        }
    }

    static final class Identity extends Equivalence<Object> implements Serializable {
        static final Identity INSTANCE = null;
        private static final long serialVersionUID = 1;

        static {
            INSTANCE = new Identity();
        }

        Identity() {
        }

        private Object readResolve() {
            return INSTANCE;
        }

        @Override // com.google.common.base.Equivalence
        protected boolean doEquivalent(Object r2, Object r3) {
            return false;
        }

        @Override // com.google.common.base.Equivalence
        protected int doHash(Object r2) {
            return System.identityHashCode(r2);
        }
    }

    public static final class Wrapper<T> implements Serializable {
        private static final long serialVersionUID = 0;
        private final Equivalence<? super T> equivalence;

        @ParametricNullness
        private final T reference;

        private Wrapper(Equivalence<? super T> r2, @ParametricNullness T r3) {
            this.equivalence = (Equivalence) Preconditions.checkNotNull(r2);
            this.reference = r3;
        }

        /* synthetic */ Wrapper(Equivalence r1, Object r2, C05371 r3) {
            this(r1, r2);
        }

        public boolean equals(@CheckForNull Object r5) {
            if (r5 != this) goto L6;
            return true;
        L6:
            if ((r5 instanceof Wrapper) == false) goto L11;
            Wrapper r0 = (Wrapper) r5;
            if (this.equivalence.equals(r0.equivalence) == true) goto L10;
            return false;
        L10:
            return this.equivalence.equivalent(this.reference, r0.reference);
        L11:
            return false;
        }

        @ParametricNullness
        public T get() {
            return this.reference;
        }

        public int hashCode() {
            return this.equivalence.hash(this.reference);
        }

        public String toString() {
            return this.equivalence + ".wrap(" + this.reference + ")";
        }
    }

    protected Equivalence() {
    }

    public static Equivalence<Object> equals() {
        return Equals.INSTANCE;
    }

    public static Equivalence<Object> identity() {
        return Identity.INSTANCE;
    }

    protected abstract boolean doEquivalent(T r1, T r2);

    protected abstract int doHash(T r1);

    public final boolean equivalent(@CheckForNull T r2, @CheckForNull T r3) {
        if (r2 != r3) goto L5;
        return true;
    L5:
        if (r2 == null) goto L10;
        if (r3 != null) goto L9;
        return false;
    L9:
        return doEquivalent(r2, r3);
    L10:
        return false;
    }

    public final Predicate<T> equivalentTo(@CheckForNull T r2) {
        return new EquivalentToPredicate(this, r2);
    }

    public final int hash(@CheckForNull T r2) {
        if (r2 != null) goto L6;
        return 0;
    L6:
        return doHash(r2);
    }

    public final <F> Equivalence<F> onResultOf(Function<? super F, ? extends T> r2) {
        return new FunctionalEquivalence(r2, this);
    }

    public final <S extends T> Equivalence<Iterable<S>> pairwise() {
        return new PairwiseEquivalence(this);
    }

    public final <S extends T> Wrapper<S> wrap(@ParametricNullness S r3) {
        return new Wrapper(this, r3, null);
    }
}
