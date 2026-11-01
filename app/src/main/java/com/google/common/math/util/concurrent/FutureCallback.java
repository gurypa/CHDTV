package com.google.common.util.concurrent;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface FutureCallback<V> {
    void onFailure(Throwable r1);

    void onSuccess(@ParametricNullness V r1);
}
