package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class Platform {
    private Platform() {
    }

    static boolean isInstanceOfThrowableClass(@CheckForNull Throwable r1, Class<? extends Throwable> r2) {
        return r2.isInstance(r1);
    }

    static void restoreInterruptIfIsInterruptedException(Throwable r1) {
        Preconditions.checkNotNull(r1);
        if ((r1 instanceof InterruptedException) == false) goto L6;
        Thread.currentThread().interrupt();
        return;
    }
}
