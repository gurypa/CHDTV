package com.google.common.p003io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class MultiReader extends Reader {

    @CheckForNull
    private Reader current;

    /* JADX INFO: renamed from: it */
    private final Iterator<? extends CharSource> f162it;

    MultiReader(Iterator<? extends CharSource> r1) throws IOException {
        this.f162it = r1;
        advance();
    }

    private void advance() throws IOException {
        close();
        if (this.f162it.hasNext() == false) goto L6;
        this.current = this.f162it.next().openStream();
        return;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.current != null) goto L11;
        return;
    L11:
        this.current.close();     // Catch: Throwable -> L7
        this.current = null;
        return;
    L7:
        th = move-exception;
        this.current = null;
        throw th;
    }

    @Override // java.io.Reader
    public int read(char[] r3, int r4, int r5) throws IOException {
        Preconditions.checkNotNull(r3);
        if (this.current != null) goto L5;
        return -1;
    L5:
        int r0 = this.current.read(r3, r4, r5);
        if (r0 != (-1)) goto L9;
        advance();
        return read(r3, r4, r5);
    L9:
        return r0;
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        if (this.current != null) goto L5;
    L7:
        return false;
    L5:
        if (this.current.ready() == false) goto L7;
        return true;
    }

    @Override // java.io.Reader
    public long skip(long r6) throws IOException {
        if (r6 < 0) goto L5;
        boolean r2 = true;
    L6:
        Preconditions.checkArgument(r2, "n is negative");
        if (r6 > 0) goto L9;
    L14:
        return 0;
    L9:
        if (this.current == null) goto L14;
        long r3 = this.current.skip(r6);
        if (r3 > 0) goto L12;
        advance();
        goto L9
    L12:
        return r3;
    L5:
        r2 = false;
        goto L6
    }
}
