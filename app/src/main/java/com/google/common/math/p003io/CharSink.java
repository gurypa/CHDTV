package com.google.common.p003io;

import com.google.common.base.Preconditions;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class CharSink {
    protected CharSink() {
    }

    public Writer openBufferedStream() throws IOException {
        Writer r0 = openStream();
        if ((r0 instanceof BufferedWriter) == false) goto L6;
        return (BufferedWriter) r0;
    L6:
        return new BufferedWriter(r0);
    }

    public abstract Writer openStream() throws IOException;

    public void write(CharSequence r4) throws IOException {
        Preconditions.checkNotNull(r4);
        Closer r0 = Closer.create();
        Writer r1 = (Writer) r0.register(openStream());     // Catch: Throwable -> L6
        r1.append(r4);     // Catch: Throwable -> L6
        r1.flush();     // Catch: Throwable -> L6
        r0.close();
        return;
    L6:
        th = move-exception;
    L9:
        th = move-exception;
        r0.close();
        throw th;
    L8:
        throw r0.rethrow(th);     // Catch: Throwable -> L9
    }

    public long writeFrom(Readable r5) throws IOException {
        Preconditions.checkNotNull(r5);
        Closer r0 = Closer.create();
        Writer r1 = (Writer) r0.register(openStream());     // Catch: Throwable -> L6
        long r2 = CharStreams.copy(r5, r1);     // Catch: Throwable -> L6
        r1.flush();     // Catch: Throwable -> L6
        r0.close();
        return r2;
    L6:
        th = move-exception;
    L9:
        th = move-exception;
        r0.close();
        throw th;
    L8:
        throw r0.rethrow(th);     // Catch: Throwable -> L9
    }

    public void writeLines(Iterable<? extends CharSequence> r2) throws IOException {
        writeLines(r2, System.getProperty("line.separator"));
    }

    public void writeLines(Iterable<? extends CharSequence> r6, String r7) throws IOException {
        Preconditions.checkNotNull(r6);
        Preconditions.checkNotNull(r7);
        Closer r0 = Closer.create();
        Writer r1 = (Writer) r0.register(openBufferedStream());     // Catch: Throwable -> L10
        Iterator<? extends CharSequence> r2 = r6.iterator();     // Catch: Throwable -> L10
    L5:
        if (r2.hasNext() == false) goto L7;
        r1.append(r2.next()).append(r7);     // Catch: Throwable -> L10
        goto L5
    L7:
        r1.flush();     // Catch: Throwable -> L10
        r0.close();
        return;
    L10:
        th = move-exception;
    L13:
        th = move-exception;
        r0.close();
        throw th;
    L12:
        throw r0.rethrow(th);     // Catch: Throwable -> L13
    }
}
