package com.google.common.util.concurrent;

import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class UncheckedTimeoutException extends RuntimeException {
    private static final long serialVersionUID = 0;

    public UncheckedTimeoutException() {
    }

    public UncheckedTimeoutException(@CheckForNull String r1) {
        super(r1);
    }

    public UncheckedTimeoutException(@CheckForNull String r1, @CheckForNull Throwable r2) {
        super(r1, r2);
    }

    public UncheckedTimeoutException(@CheckForNull Throwable r1) {
        super(r1);
    }
}
