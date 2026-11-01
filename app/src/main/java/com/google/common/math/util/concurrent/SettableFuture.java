package com.google.common.util.concurrent;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class SettableFuture<V> extends AbstractFuture.TrustedFuture<V> {
    private SettableFuture() {
    }

    public static <V> SettableFuture<V> create() {
        return new SettableFuture();
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    public boolean set(@ParametricNullness V r2) {
        return super.set(r2);
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    public boolean setException(Throwable r2) {
        return super.setException(r2);
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    public boolean setFuture(ListenableFuture<? extends V> r2) {
        return super.setFuture(r2);
    }
}
