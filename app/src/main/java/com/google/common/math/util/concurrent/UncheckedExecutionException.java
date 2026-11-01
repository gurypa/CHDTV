package com.google.common.util.concurrent;

import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class UncheckedExecutionException extends RuntimeException {
    private static final long serialVersionUID = 0;

    @Deprecated
    protected UncheckedExecutionException() {
    }

    @Deprecated
    protected UncheckedExecutionException(@CheckForNull String r1) {
        super(r1);
    }

    public UncheckedExecutionException(@CheckForNull String r1, @CheckForNull Throwable r2) {
        super(r1, r2);
    }

    public UncheckedExecutionException(@CheckForNull Throwable r1) {
        super(r1);
    }
}
