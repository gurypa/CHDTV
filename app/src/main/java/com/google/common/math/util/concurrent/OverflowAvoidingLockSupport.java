package com.google.common.util.concurrent;

import java.util.concurrent.locks.LockSupport;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class OverflowAvoidingLockSupport {
    static final long MAX_NANOSECONDS_THRESHOLD = 2147483647999999999L;

    private OverflowAvoidingLockSupport() {
    }

    static void parkNanos(@CheckForNull Object r2, long r3) {
        LockSupport.parkNanos(r2, Math.min(r3, MAX_NANOSECONDS_THRESHOLD));
    }
}
