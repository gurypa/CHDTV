package com.google.common.base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Suppliers {

    static class ExpiringMemoizingSupplier<T> implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0;
        final Supplier<T> delegate;
        final long durationNanos;
        volatile transient long expirationNanos;
        private transient Object lock;

        @CheckForNull
        volatile transient T value;

        ExpiringMemoizingSupplier(Supplier<T> r2, long r3) {
            this.lock = new Object();
            this.delegate = r2;
            this.durationNanos = r3;
        }

        private void readObject(ObjectInputStream r2) throws IOException, ClassNotFoundException {
            r2.defaultReadObject();
            this.lock = new Object();
        }

        @Override // com.google.common.base.Supplier
        @ParametricNullness
        public T get() {
            long r0 = this.expirationNanos;
            long r2 = System.nanoTime();
            if (r0 != 0) goto L5;
        L6:
            Object r6 = this.lock;
            monitor-enter(r6);
        L20:
            th = move-exception;
            throw th;
        L9:
            if (r0 != this.expirationNanos) goto L17;
            T r7 = this.delegate.get();     // Catch: Throwable -> L20
            this.value = r7;     // Catch: Throwable -> L20
            long r1 = r2 + this.durationNanos;     // Catch: Throwable -> L20
            if (r1 != 0) goto L13;
            long r4 = 1;
        L14:
            this.expirationNanos = r4;     // Catch: Throwable -> L20
            monitor-exit(r6);     // Catch: Throwable -> L20
            return r7;
        L13:
            r4 = r1;
            goto L14
        L17:
            monitor-exit(r6);     // Catch: Throwable -> L20
        L19:
            return (T) NullnessCasts.uncheckedCastNullableTToT(this.value);
        L5:
            if ((r2 - r0) < 0) goto L19;
            goto L6
        }

        public String toString() {
            return "Suppliers.memoizeWithExpiration(" + this.delegate + ", " + this.durationNanos + ", NANOS)";
        }
    }

    static class MemoizingSupplier<T> implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0;
        final Supplier<T> delegate;
        volatile transient boolean initialized;
        private transient Object lock;

        @CheckForNull
        transient T value;

        MemoizingSupplier(Supplier<T> r2) {
            this.lock = new Object();
            this.delegate = (Supplier) Preconditions.checkNotNull(r2);
        }

        private void readObject(ObjectInputStream r2) throws IOException, ClassNotFoundException {
            r2.defaultReadObject();
            this.lock = new Object();
        }

        @Override // com.google.common.base.Supplier
        @ParametricNullness
        public T get() {
            if (this.initialized == true) goto L17;
            Object r0 = this.lock;
            monitor-enter(r0);
        L13:
            th = move-exception;
            throw th;
        L7:
            if (this.initialized == true) goto L11;
            T r1 = this.delegate.get();     // Catch: Throwable -> L13
            this.value = r1;     // Catch: Throwable -> L13
            this.initialized = true;     // Catch: Throwable -> L13
            monitor-exit(r0);     // Catch: Throwable -> L13
            return r1;
        L11:
            monitor-exit(r0);     // Catch: Throwable -> L13
        L17:
            return (T) NullnessCasts.uncheckedCastNullableTToT(this.value);
        }

        public String toString() {
            StringBuilder r0 = new StringBuilder().append("Suppliers.memoize(");
            if (this.initialized == false) goto L5;
            Object r1 = "<supplier that returned " + this.value + ">";
        L7:
            return r0.append(r1).append(")").toString();
        L5:
            r1 = this.delegate;
            goto L7
        }
    }

    static class NonSerializableMemoizingSupplier<T> implements Supplier<T> {
        private static final Supplier<Void> SUCCESSFULLY_COMPUTED = null;
        private volatile Supplier<T> delegate;
        private final Object lock;

        @CheckForNull
        private T value;

        static {
            SUCCESSFULLY_COMPUTED = new C0556xd39da8e3();
        }

        NonSerializableMemoizingSupplier(Supplier<T> r2) {
            this.lock = new Object();
            this.delegate = (Supplier) Preconditions.checkNotNull(r2);
        }

        static /* synthetic */ Void lambda$static$0() {
            throw new IllegalStateException();
        }

        @Override // com.google.common.base.Supplier
        @ParametricNullness
        public T get() {
            if (this.delegate == SUCCESSFULLY_COMPUTED) goto L17;
            Object r0 = this.lock;
            monitor-enter(r0);
        L13:
            th = move-exception;
            throw th;
        L7:
            if (this.delegate == SUCCESSFULLY_COMPUTED) goto L11;
            T r1 = this.delegate.get();     // Catch: Throwable -> L13
            this.value = r1;     // Catch: Throwable -> L13
            this.delegate = (Supplier<T>) SUCCESSFULLY_COMPUTED;     // Catch: Throwable -> L13
            monitor-exit(r0);     // Catch: Throwable -> L13
            return r1;
        L11:
            monitor-exit(r0);     // Catch: Throwable -> L13
        L17:
            return (T) NullnessCasts.uncheckedCastNullableTToT(this.value);
        }

        public String toString() {
            Supplier<T> r0 = this.delegate;
            StringBuilder r1 = new StringBuilder().append("Suppliers.memoize(");
            if (r0 != SUCCESSFULLY_COMPUTED) goto L5;
            Object r2 = "<supplier that returned " + this.value + ">";
        L7:
            return r1.append(r2).append(")").toString();
        L5:
            r2 = r0;
            goto L7
        }
    }

    private static class SupplierComposition<F, T> implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0;
        final Function<? super F, T> function;
        final Supplier<F> supplier;

        SupplierComposition(Function<? super F, T> r2, Supplier<F> r3) {
            this.function = (Function) Preconditions.checkNotNull(r2);
            this.supplier = (Supplier) Preconditions.checkNotNull(r3);
        }

        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof SupplierComposition) == false) goto L10;
            SupplierComposition r0 = (SupplierComposition) r5;
            if (this.function.equals(r0.function) == true) goto L7;
            return false;
        L7:
            if (this.supplier.equals(r0.supplier) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        @Override // com.google.common.base.Supplier
        @ParametricNullness
        public T get() {
            return this.function.apply(this.supplier.get());
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{this.function, this.supplier});
        }

        public String toString() {
            return "Suppliers.compose(" + this.function + ", " + this.supplier + ")";
        }
    }

    private interface SupplierFunction<T> extends Function<Supplier<T>, T> {
    }

    private static final enum SupplierFunctionImpl extends Enum<SupplierFunctionImpl> implements SupplierFunction<Object> {
        private static final /* synthetic */ SupplierFunctionImpl[] $VALUES = null;
        public static final SupplierFunctionImpl INSTANCE = null;

        private static /* synthetic */ SupplierFunctionImpl[] $values() {
            return new SupplierFunctionImpl[]{INSTANCE};
        }

        static {
            INSTANCE = new SupplierFunctionImpl("INSTANCE", 0);
            $VALUES = $values();
        }

        private SupplierFunctionImpl(String r1, int r2) {
            super(r1, r2);
        }

        public static SupplierFunctionImpl valueOf(String r1) {
            return (SupplierFunctionImpl) Enum.valueOf(SupplierFunctionImpl.class, r1);
        }

        public static SupplierFunctionImpl[] values() {
            return (SupplierFunctionImpl[]) $VALUES.clone();
        }

        @CheckForNull
        public Object apply(Supplier<Object> r2) {
            return r2.get();
        }

        @Override // com.google.common.base.Function
        @CheckForNull
        public /* bridge */ /* synthetic */ Object apply(Object r1) {
            return apply((Supplier) r1);
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Suppliers.supplierFunction()";
        }
    }

    private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0;

        @ParametricNullness
        final T instance;

        SupplierOfInstance(@ParametricNullness T r1) {
            this.instance = r1;
        }

        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof SupplierOfInstance) == true) goto L5;
            return false;
        L5:
            return Objects.equal(this.instance, ((SupplierOfInstance) r4).instance);
        }

        @Override // com.google.common.base.Supplier
        @ParametricNullness
        public T get() {
            return this.instance;
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{this.instance});
        }

        public String toString() {
            return "Suppliers.ofInstance(" + this.instance + ")";
        }
    }

    private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0;
        final Supplier<T> delegate;

        ThreadSafeSupplier(Supplier<T> r2) {
            this.delegate = (Supplier) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.base.Supplier
        @ParametricNullness
        public T get() {
            Supplier<T> r0 = this.delegate;
            monitor-enter(r0);
            T r1 = this.delegate.get();     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return r1;
        L7:
            th = move-exception;
            throw th;
        }

        public String toString() {
            return "Suppliers.synchronizedSupplier(" + this.delegate + ")";
        }
    }

    private Suppliers() {
    }

    public static <F, T> Supplier<T> compose(Function<? super F, T> r1, Supplier<F> r2) {
        return new SupplierComposition(r1, r2);
    }

    public static <T> Supplier<T> memoize(Supplier<T> r1) {
        if ((r1 instanceof NonSerializableMemoizingSupplier) == false) goto L5;
    L12:
        return r1;
    L5:
        if ((r1 instanceof MemoizingSupplier) == true) goto L12;
        if ((r1 instanceof Serializable) == false) goto L11;
        return new MemoizingSupplier(r1);
    L11:
        return new NonSerializableMemoizingSupplier(r1);
    }

    public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> r3, long r4, TimeUnit r6) {
        Preconditions.checkNotNull(r3);
        if (r4 <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "duration (%s %s) must be > 0", r4, r6);
        return new ExpiringMemoizingSupplier(r3, r6.toNanos(r4));
    L5:
        r0 = false;
        goto L6
    }

    public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> r3, Duration r4) {
        Preconditions.checkNotNull(r3);
        if (r4.isNegative() == false) goto L5;
    L7:
        boolean r0 = false;
    L8:
        Preconditions.checkArgument(r0, "duration (%s) must be > 0", r4);
        return new ExpiringMemoizingSupplier(r3, Internal.toNanosSaturated(r4));
    L5:
        if (r4.isZero() == true) goto L7;
        r0 = true;
        goto L8
    }

    public static <T> Supplier<T> ofInstance(@ParametricNullness T r1) {
        return new SupplierOfInstance(r1);
    }

    public static <T> Function<Supplier<T>, T> supplierFunction() {
        return SupplierFunctionImpl.INSTANCE;
    }

    public static <T> Supplier<T> synchronizedSupplier(Supplier<T> r1) {
        return new ThreadSafeSupplier(r1);
    }
}
