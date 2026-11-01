package com.google.common.cache;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class CacheLoader<K, V> {

    /* JADX INFO: renamed from: com.google.common.cache.CacheLoader$1 */
    class C05631 extends CacheLoader<K, V> {
        final /* synthetic */ Executor val$executor;
        final /* synthetic */ CacheLoader val$loader;

        C05631(CacheLoader r1, Executor r2) {
            this.val$loader = r1;
            this.val$executor = r2;
        }

        static /* synthetic */ Object lambda$reload$0(CacheLoader r1, Object r2, Object r3) throws Exception {
            return r1.reload(r2, r3).get();
        }

        @Override // com.google.common.cache.CacheLoader
        public V load(K r2) throws Exception {
            return (V) this.val$loader.load(r2);
        }

        @Override // com.google.common.cache.CacheLoader
        public Map<K, V> loadAll(Iterable<? extends K> r2) throws Exception {
            return this.val$loader.loadAll(r2);
        }

        @Override // com.google.common.cache.CacheLoader
        public ListenableFuture<V> reload(final K r3, final V r4) {
            final CacheLoader r0 = this.val$loader;
            ListenableFutureTask r1 = ListenableFutureTask.create(new CacheLoader$1$$ExternalSyntheticLambda0(r0, r3, r4));
            this.val$executor.execute(r1);
            return r1;
        }
    }

    private static final class FunctionToCacheLoader<K, V> extends CacheLoader<K, V> implements Serializable {
        private static final long serialVersionUID = 0;
        private final Function<K, V> computingFunction;

        public FunctionToCacheLoader(Function<K, V> r2) {
            this.computingFunction = (Function) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.cache.CacheLoader
        public V load(K r3) {
            return this.computingFunction.apply((K) Preconditions.checkNotNull(r3));
        }
    }

    public static final class InvalidCacheLoadException extends RuntimeException {
        public InvalidCacheLoadException(String r1) {
            super(r1);
        }
    }

    private static final class SupplierToCacheLoader<V> extends CacheLoader<Object, V> implements Serializable {
        private static final long serialVersionUID = 0;
        private final Supplier<V> computingSupplier;

        public SupplierToCacheLoader(Supplier<V> r2) {
            this.computingSupplier = (Supplier) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.cache.CacheLoader
        public V load(Object r2) {
            Preconditions.checkNotNull(r2);
            return this.computingSupplier.get();
        }
    }

    public static final class UnsupportedLoadingOperationException extends UnsupportedOperationException {
        UnsupportedLoadingOperationException() {
        }
    }

    protected CacheLoader() {
    }

    public static <K, V> CacheLoader<K, V> asyncReloading(CacheLoader<K, V> r1, Executor r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new C05631(r1, r2);
    }

    public static <K, V> CacheLoader<K, V> from(Function<K, V> r1) {
        return new FunctionToCacheLoader(r1);
    }

    public static <V> CacheLoader<Object, V> from(Supplier<V> r1) {
        return new SupplierToCacheLoader(r1);
    }

    public abstract V load(K r1) throws Exception;

    public Map<K, V> loadAll(Iterable<? extends K> r2) throws Exception {
        throw new UnsupportedLoadingOperationException();
    }

    public ListenableFuture<V> reload(K r2, V r3) throws Exception {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        return Futures.immediateFuture(load(r2));
    }
}
