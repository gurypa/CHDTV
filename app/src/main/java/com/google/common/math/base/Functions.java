package com.google.common.base;

import java.io.Serializable;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Functions {

    /* JADX INFO: renamed from: com.google.common.base.Functions$1 */
    static /* synthetic */ class C05381 {
    }

    private static class ConstantFunction<E> implements Function<Object, E>, Serializable {
        private static final long serialVersionUID = 0;

        @ParametricNullness
        private final E value;

        public ConstantFunction(@ParametricNullness E r1) {
            this.value = r1;
        }

        @Override // com.google.common.base.Function
        @ParametricNullness
        public E apply(@CheckForNull Object r2) {
            return this.value;
        }

        @Override // com.google.common.base.Function
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof ConstantFunction) == true) goto L5;
            return false;
        L5:
            return Objects.equal(this.value, ((ConstantFunction) r4).value);
        }

        public int hashCode() {
            if (this.value != null) goto L6;
            return 0;
        L6:
            return this.value.hashCode();
        }

        public String toString() {
            return "Functions.constant(" + this.value + ")";
        }
    }

    private static class ForMapWithDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0;

        @ParametricNullness
        final V defaultValue;
        final Map<K, ? extends V> map;

        ForMapWithDefault(Map<K, ? extends V> r2, @ParametricNullness V r3) {
            this.map = (Map) Preconditions.checkNotNull(r2);
            this.defaultValue = r3;
        }

        @Override // com.google.common.base.Function
        @ParametricNullness
        public V apply(@ParametricNullness K r3) {
            V r0 = this.map.get(r3);
            if (r0 != null) goto L9;
            if (this.map.containsKey(r3) == true) goto L9;
            return this.defaultValue;
        L9:
            return (V) NullnessCasts.uncheckedCastNullableTToT(r0);
        }

        @Override // com.google.common.base.Function
        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof ForMapWithDefault) == false) goto L10;
            ForMapWithDefault r0 = (ForMapWithDefault) r5;
            if (this.map.equals(r0.map) == true) goto L7;
            return false;
        L7:
            if (Objects.equal(this.defaultValue, r0.defaultValue) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{this.map, this.defaultValue});
        }

        public String toString() {
            return "Functions.forMap(" + this.map + ", defaultValue=" + this.defaultValue + ")";
        }
    }

    private static class FunctionComposition<A, B, C> implements Function<A, C>, Serializable {
        private static final long serialVersionUID = 0;

        /* JADX INFO: renamed from: f */
        private final Function<A, ? extends B> f93f;

        /* JADX INFO: renamed from: g */
        private final Function<B, C> f94g;

        public FunctionComposition(Function<B, C> r2, Function<A, ? extends B> r3) {
            this.f94g = (Function) Preconditions.checkNotNull(r2);
            this.f93f = (Function) Preconditions.checkNotNull(r3);
        }

        @Override // com.google.common.base.Function
        @ParametricNullness
        public C apply(@ParametricNullness A r3) {
            return (C) this.f94g.apply(this.f93f.apply(r3));
        }

        @Override // com.google.common.base.Function
        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof FunctionComposition) == false) goto L10;
            FunctionComposition r0 = (FunctionComposition) r5;
            if (this.f93f.equals(r0.f93f) == true) goto L7;
            return false;
        L7:
            if (this.f94g.equals(r0.f94g) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        public int hashCode() {
            return this.f93f.hashCode() ^ this.f94g.hashCode();
        }

        public String toString() {
            return this.f94g + "(" + this.f93f + ")";
        }
    }

    private static class FunctionForMapNoDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        final Map<K, V> map;

        FunctionForMapNoDefault(Map<K, V> r2) {
            this.map = (Map) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.base.Function
        @ParametricNullness
        public V apply(@ParametricNullness K r4) {
            V r0 = this.map.get(r4);
            if (r0 == null) goto L5;
        L8:
            boolean r1 = true;
        L9:
            Preconditions.checkArgument(r1, "Key '%s' not present in map", r4);
            return (V) NullnessCasts.uncheckedCastNullableTToT(r0);
        L5:
            if (this.map.containsKey(r4) == true) goto L8;
            r1 = false;
            goto L9
        }

        @Override // com.google.common.base.Function
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof FunctionForMapNoDefault) == true) goto L5;
            return false;
        L5:
            return this.map.equals(((FunctionForMapNoDefault) r4).map);
        }

        public int hashCode() {
            return this.map.hashCode();
        }

        public String toString() {
            return "Functions.forMap(" + this.map + ")";
        }
    }

    private static final enum IdentityFunction extends Enum<IdentityFunction> implements Function<Object, Object> {
        private static final /* synthetic */ IdentityFunction[] $VALUES = null;
        public static final IdentityFunction INSTANCE = null;

        private static /* synthetic */ IdentityFunction[] $values() {
            return new IdentityFunction[]{INSTANCE};
        }

        static {
            INSTANCE = new IdentityFunction("INSTANCE", 0);
            $VALUES = $values();
        }

        private IdentityFunction(String r1, int r2) {
            super(r1, r2);
        }

        public static IdentityFunction valueOf(String r1) {
            return (IdentityFunction) Enum.valueOf(IdentityFunction.class, r1);
        }

        public static IdentityFunction[] values() {
            return (IdentityFunction[]) $VALUES.clone();
        }

        @Override // com.google.common.base.Function
        @CheckForNull
        public Object apply(@CheckForNull Object r1) {
            return r1;
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Functions.identity()";
        }
    }

    private static class PredicateFunction<T> implements Function<T, Boolean>, Serializable {
        private static final long serialVersionUID = 0;
        private final Predicate<T> predicate;

        private PredicateFunction(Predicate<T> r2) {
            this.predicate = (Predicate) Preconditions.checkNotNull(r2);
        }

        /* synthetic */ PredicateFunction(Predicate r1, C05381 r2) {
            this(r1);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.base.Function
        public Boolean apply(@ParametricNullness T r2) {
            return Boolean.valueOf(this.predicate.apply(r2));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.base.Function
        public /* bridge */ /* synthetic */ Boolean apply(@ParametricNullness Object r1) {
            return apply(r1);
        }

        @Override // com.google.common.base.Function
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof PredicateFunction) == true) goto L5;
            return false;
        L5:
            return this.predicate.equals(((PredicateFunction) r4).predicate);
        }

        public int hashCode() {
            return this.predicate.hashCode();
        }

        public String toString() {
            return "Functions.forPredicate(" + this.predicate + ")";
        }
    }

    private static class SupplierFunction<F, T> implements Function<F, T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Supplier<T> supplier;

        private SupplierFunction(Supplier<T> r2) {
            this.supplier = (Supplier) Preconditions.checkNotNull(r2);
        }

        /* synthetic */ SupplierFunction(Supplier r1, C05381 r2) {
            this(r1);
        }

        @Override // com.google.common.base.Function
        @ParametricNullness
        public T apply(@ParametricNullness F r2) {
            return this.supplier.get();
        }

        @Override // com.google.common.base.Function
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof SupplierFunction) == true) goto L5;
            return false;
        L5:
            return this.supplier.equals(((SupplierFunction) r4).supplier);
        }

        public int hashCode() {
            return this.supplier.hashCode();
        }

        public String toString() {
            return "Functions.forSupplier(" + this.supplier + ")";
        }
    }

    private static final enum ToStringFunction extends Enum<ToStringFunction> implements Function<Object, String> {
        private static final /* synthetic */ ToStringFunction[] $VALUES = null;
        public static final ToStringFunction INSTANCE = null;

        private static /* synthetic */ ToStringFunction[] $values() {
            return new ToStringFunction[]{INSTANCE};
        }

        static {
            INSTANCE = new ToStringFunction("INSTANCE", 0);
            $VALUES = $values();
        }

        private ToStringFunction(String r1, int r2) {
            super(r1, r2);
        }

        public static ToStringFunction valueOf(String r1) {
            return (ToStringFunction) Enum.valueOf(ToStringFunction.class, r1);
        }

        public static ToStringFunction[] values() {
            return (ToStringFunction[]) $VALUES.clone();
        }

        @Override // com.google.common.base.Function
        public /* bridge */ /* synthetic */ String apply(Object r1) {
            return apply2(r1);
        }

        @Override // com.google.common.base.Function
        /* JADX INFO: renamed from: apply, reason: avoid collision after fix types in other method */
        public String apply2(Object r2) {
            Preconditions.checkNotNull(r2);
            return r2.toString();
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Functions.toStringFunction()";
        }
    }

    private Functions() {
    }

    public static <A, B, C> Function<A, C> compose(Function<B, C> r1, Function<A, ? extends B> r2) {
        return new FunctionComposition(r1, r2);
    }

    public static <E> Function<Object, E> constant(@ParametricNullness E r1) {
        return new ConstantFunction(r1);
    }

    public static <K, V> Function<K, V> forMap(Map<K, V> r1) {
        return new FunctionForMapNoDefault(r1);
    }

    public static <K, V> Function<K, V> forMap(Map<K, ? extends V> r1, @ParametricNullness V r2) {
        return new ForMapWithDefault(r1, r2);
    }

    public static <T> Function<T, Boolean> forPredicate(Predicate<T> r2) {
        return new PredicateFunction(r2, null);
    }

    public static <F, T> Function<F, T> forSupplier(Supplier<T> r2) {
        return new SupplierFunction(r2, null);
    }

    public static <E> Function<E, E> identity() {
        return IdentityFunction.INSTANCE;
    }

    public static Function<Object, String> toStringFunction() {
        return ToStringFunction.INSTANCE;
    }
}
