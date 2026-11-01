package com.google.common.util.concurrent;

import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class LazyLogger {
    private final Object lock;
    private volatile Logger logger;
    private final String loggerName;

    LazyLogger(Class<?> r2) {
        this.lock = new Object();
        this.loggerName = r2.getName();
    }

    Logger get() {
        Logger r0 = this.logger;
        if (r0 == null) goto L5;
        return r0;
    L5:
        Object r1 = this.lock;
        monitor-enter(r1);
        Logger r2 = this.logger;     // Catch: Throwable -> L14
        if (r2 == null) goto L11;
        monitor-exit(r1);     // Catch: Throwable -> L14
        return r2;
    L11:
        Logger r3 = Logger.getLogger(this.loggerName);     // Catch: Throwable -> L14
        this.logger = r3;     // Catch: Throwable -> L14
        monitor-exit(r1);     // Catch: Throwable -> L14
        return r3;
    L14:
        th = move-exception;
        throw th;
    }
}
