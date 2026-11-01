package com.google.common.base;

import java.time.Duration;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class Internal {
    private Internal() {
    }

    static long toNanosSaturated(Duration r3) {
        return r3.toNanos();
    L6:
        if (r3.isNegative() == false) goto L8;
        return Long.MIN_VALUE;
    L8:
        return Long.MAX_VALUE;
    }
}
