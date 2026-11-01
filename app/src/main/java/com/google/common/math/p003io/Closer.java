package com.google.common.p003io;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Closer implements Closeable {
    private static final Suppressor SUPPRESSING_SUPPRESSOR = null;
    private final Deque<Closeable> stack;
    final Suppressor suppressor;

    @CheckForNull
    private Throwable thrown;

    interface Suppressor {
        void suppress(Closeable r1, Throwable r2, Throwable r3);
    }

    static {
        SUPPRESSING_SUPPRESSOR = new Closer$$ExternalSyntheticLambda0();
    }

    Closer(Suppressor r3) {
        this.stack = new ArrayDeque(4);
        this.suppressor = (Suppressor) Preconditions.checkNotNull(r3);
    }

    public static Closer create() {
        return new Closer(SUPPRESSING_SUPPRESSOR);
    }

    static /* synthetic */ void lambda$static$0(Closeable r5, Throwable r6, Throwable r7) {
        if (r6 != r7) goto L9;
        return;
    L9:
        r6.addSuppressed(r7);     // Catch: Throwable -> L6
        return;
    L7:
        Closeables.logger.log(Level.WARNING, "Suppressing exception thrown when closing " + r5, r7);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        Throwable r0 = this.thrown;
    L4:
        if (this.stack.isEmpty() == true) goto L14;
        Closeable r1 = this.stack.removeFirst();
        r1.close();     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        if (r0 == null) goto L10;
        this.suppressor.suppress(r1, r0, th);
        goto L4
    L10:
        r0 = th;
        goto L4
    L14:
        if (this.thrown != null) goto L19;
        if (r0 == null) goto L29;
        Throwables.throwIfInstanceOf(r0, IOException.class);
        Throwables.throwIfUnchecked(r0);
        throw new AssertionError(r0);
    L29:
        return;
    }

    @ParametricNullness
    public <C extends Closeable> C register(@ParametricNullness C r2) {
        if (r2 == null) goto L4;
        this.stack.addFirst(r2);
    L4:
        return r2;
    }

    public RuntimeException rethrow(Throwable r2) throws IOException {
        Preconditions.checkNotNull(r2);
        this.thrown = r2;
        Throwables.throwIfInstanceOf(r2, IOException.class);
        Throwables.throwIfUnchecked(r2);
        throw new RuntimeException(r2);
    }

    public <X extends Exception> RuntimeException rethrow(Throwable r2, Class<X> r3) throws IOException, Exception {
        Preconditions.checkNotNull(r2);
        this.thrown = r2;
        Throwables.throwIfInstanceOf(r2, IOException.class);
        Throwables.throwIfInstanceOf(r2, r3);
        Throwables.throwIfUnchecked(r2);
        throw new RuntimeException(r2);
    }

    public <X1 extends Exception, X2 extends Exception> RuntimeException rethrow(Throwable r2, Class<X1> r3, Class<X2> r4) throws IOException, Exception, Exception {
        Preconditions.checkNotNull(r2);
        this.thrown = r2;
        Throwables.throwIfInstanceOf(r2, IOException.class);
        Throwables.throwIfInstanceOf(r2, r3);
        Throwables.throwIfInstanceOf(r2, r4);
        Throwables.throwIfUnchecked(r2);
        throw new RuntimeException(r2);
    }
}
