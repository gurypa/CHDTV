package com.google.common.util.concurrent;

import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class ExecutionError extends Error {
    private static final long serialVersionUID = 0;

    @Deprecated
    protected ExecutionError() {
    }

    public ExecutionError(@CheckForNull Error r1) {
        super(r1);
    }

    @Deprecated
    protected ExecutionError(@CheckForNull String r1) {
        super(r1);
    }

    public ExecutionError(@CheckForNull String r1, @CheckForNull Error r2) {
        super(r1, r2);
    }
}
