package androidx.core.util;

import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public interface Predicate<T> {

    /* JADX INFO: renamed from: androidx.core.util.Predicate$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static Predicate $default$and(final Predicate r1, final Predicate r2) {
            Objects.requireNonNull(r2);
            return new Predicate$$ExternalSyntheticLambda4(r1, r2);
        }

        public static Predicate $default$negate(final Predicate r1) {
            return new Predicate$$ExternalSyntheticLambda5(r1);
        }

        public static Predicate $default$or(final Predicate r1, final Predicate r2) {
            Objects.requireNonNull(r2);
            return new Predicate$$ExternalSyntheticLambda1(r1, r2);
        }

        public static /* synthetic */ boolean $private$lambda$and$0(Predicate r1, Predicate r2, Object r3) {
            if (r1.test(r3) == true) goto L5;
        L7:
            return false;
        L5:
            if (r2.test(r3) == false) goto L7;
            return true;
        }

        public static /* synthetic */ boolean $private$lambda$negate$1(Predicate r1, Object r2) {
            return !r1.test(r2);
        }

        public static /* synthetic */ boolean $private$lambda$or$2(Predicate r1, Predicate r2, Object r3) {
            if (r1.test(r3) == false) goto L5;
        L8:
            return true;
        L5:
            if (r2.test(r3) == true) goto L8;
            return false;
        }

        public static <T> Predicate<T> isEqual(final Object r1) {
            if (r1 != null) goto L5;
            return new Predicate$$ExternalSyntheticLambda2();
        L5:
            return new Predicate$$ExternalSyntheticLambda3(r1);
        }

        public static /* synthetic */ boolean lambda$isEqual$3(Object r1) {
            return Predicate$$ExternalSyntheticBackport0.m31m(r1);
        }

        public static /* synthetic */ boolean lambda$isEqual$4(Object r1, Object r2) {
            return r1.equals(r2);
        }

        public static <T> Predicate<T> not(Predicate<? super T> r1) {
            Objects.requireNonNull(r1);
            return r1.negate();
        }
    }

    Predicate<T> and(Predicate<? super T> r1);

    Predicate<T> negate();

    /* JADX INFO: renamed from: or */
    Predicate<T> mo5or(Predicate<? super T> r1);

    boolean test(T r1);
}
