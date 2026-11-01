package com.google.common.util.concurrent;

import java.util.Locale;
import java.util.logging.Level;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class UncaughtExceptionHandlers {

    static final class Exiter implements Thread.UncaughtExceptionHandler {
        private static final LazyLogger logger = null;
        private final Runtime runtime;

        static {
            logger = new LazyLogger(Exiter.class);
        }

        Exiter(Runtime r1) {
            this.runtime = r1;
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread r8, Throwable r9) {
            logger.get().log(Level.SEVERE, String.format(Locale.ROOT, "Caught an exception in %s.  Shutting down.", new Object[]{r8}), r9);     // Catch: Throwable -> L5
        L7:
            this.runtime.exit(1);
            return;
        L5:
            th = move-exception;
            System.err.println(r9.getMessage());     // Catch: Throwable -> L9
            System.err.println(th.getMessage());     // Catch: Throwable -> L9
        L9:
            th = move-exception;
            this.runtime.exit(1);
            throw th;
        }
    }

    private UncaughtExceptionHandlers() {
    }

    public static Thread.UncaughtExceptionHandler systemExit() {
        return new Exiter(Runtime.getRuntime());
    }
}
