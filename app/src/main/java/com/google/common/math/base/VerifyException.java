package com.google.common.base;

import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class VerifyException extends RuntimeException {
    public VerifyException() {
    }

    public VerifyException(@CheckForNull String r1) {
        super(r1);
    }

    public VerifyException(@CheckForNull String r1, @CheckForNull Throwable r2) {
        super(r1, r2);
    }

    public VerifyException(@CheckForNull Throwable r1) {
        super(r1);
    }
}
