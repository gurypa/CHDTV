package com.google.common.p003io;

import java.io.Flushable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Flushables {
    private static final Logger logger = null;

    static {
        logger = Logger.getLogger(Flushables.class.getName());
    }

    private Flushables() {
    }

    public static void flush(Flushable r4, boolean r5) throws IOException {
        r4.flush();     // Catch: IOException -> L4
        return;
    L4:
        e = move-exception;
        if (r5 == false) goto L8;
        logger.log(Level.WARNING, "IOException thrown while flushing Flushable.", e);
        return;
    L8:
        throw e;
    }

    public static void flushQuietly(Flushable r4) {
        flush(r4, true);     // Catch: IOException -> L5
        return;
    L5:
        e = move-exception;
        logger.log(Level.SEVERE, "IOException should not have been thrown.", e);
    }
}
