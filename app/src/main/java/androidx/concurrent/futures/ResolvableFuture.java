package androidx.concurrent.futures;

import com.google.common.util.concurrent.ListenableFuture;

/* JADX INFO: loaded from: classes.dex */
public final class ResolvableFuture<V> extends AbstractResolvableFuture<V> {
    private ResolvableFuture() {
    }

    public static <V> ResolvableFuture<V> create() {
        return new ResolvableFuture();
    }

    @Override // androidx.concurrent.futures.AbstractResolvableFuture
    public boolean set(V r2) {
        return super.set(r2);
    }

    @Override // androidx.concurrent.futures.AbstractResolvableFuture
    public boolean setException(Throwable r2) {
        return super.setException(r2);
    }

    @Override // androidx.concurrent.futures.AbstractResolvableFuture
    public boolean setFuture(ListenableFuture<? extends V> r2) {
        return super.setFuture(r2);
    }
}
