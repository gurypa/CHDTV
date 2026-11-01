package com.google.common.cache;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class RemovalListeners {
    private RemovalListeners() {
    }

    public static <K, V> RemovalListener<K, V> asynchronous(final RemovalListener<K, V> r1, final Executor r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new RemovalListeners$$ExternalSyntheticLambda1(r2, r1);
    }

    static /* synthetic */ void lambda$asynchronous$0(RemovalListener r0, RemovalNotification r1) {
        r0.onRemoval(r1);
    }

    static /* synthetic */ void lambda$asynchronous$1(Executor r1, final RemovalListener r2, final RemovalNotification r3) {
        r1.execute(new RemovalListeners$$ExternalSyntheticLambda0(r2, r3));
    }
}
