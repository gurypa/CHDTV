package com.google.common.p003io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Closeables {
    static final Logger logger = null;

    static {
        logger = Logger.getLogger(Closeables.class.getName());
    }

    private Closeables() {
    }

    public static void close(@CheckForNull Closeable r4, boolean r5) throws IOException {
        if (r4 != null) goto L11;
        return;
    L11:
        r4.close();     // Catch: IOException -> L6
        return;
    L6:
        e = move-exception;
        if (r5 == false) goto L10;
        logger.log(Level.WARNING, "IOException thrown while closing Closeable.", e);
        return;
    L10:
        throw e;
    }

    public static void closeQuietly(@CheckForNull InputStream r2) {
        close(r2, true);     // Catch: IOException -> L5
        return;
    L5:
        e = move-exception;
        throw new AssertionError(e);
    }

    public static void closeQuietly(@CheckForNull Reader r2) {
        close(r2, true);     // Catch: IOException -> L5
        return;
    L5:
        e = move-exception;
        throw new AssertionError(e);
    }
}
