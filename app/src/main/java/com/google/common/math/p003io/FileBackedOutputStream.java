package com.google.common.p003io;

import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class FileBackedOutputStream extends OutputStream {

    @CheckForNull
    private File file;
    private final int fileThreshold;

    @CheckForNull
    private MemoryOutput memory;
    private OutputStream out;
    private final boolean resetOnFinalize;
    private final ByteSource source;

    private static class MemoryOutput extends ByteArrayOutputStream {
        private MemoryOutput() {
        }

        /* synthetic */ MemoryOutput(C08481 r1) {
            this();
        }

        byte[] getBuffer() {
            return this.buf;
        }

        int getCount() {
            return this.count;
        }
    }

    public FileBackedOutputStream(int r2) {
        this(r2, false);
    }

    public FileBackedOutputStream(int r3, boolean r4) {
        if (r3 < 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "fileThreshold must be non-negative, but was %s", r3);
        this.fileThreshold = r3;
        this.resetOnFinalize = r4;
        this.memory = new MemoryOutput(null);
        this.out = this.memory;
        if (r4 == false) goto L9;
        this.source = new C08481(this);
        return;
    L9:
        this.source = new C08492(this);
        return;
    L5:
        r0 = false;
        goto L6
    }

    static /* synthetic */ InputStream access$100(FileBackedOutputStream r1) throws IOException {
        return r1.openInputStream();
    }

    private synchronized InputStream openInputStream() throws IOException {
        monitor-enter(this);
    L11:
        th = move-exception;
        throw th;
    L4:
        if (this.file == null) goto L8;
        FileInputStream r0 = new FileInputStream(this.file);     // Catch: Throwable -> L11
        monitor-exit(this);
        return r0;
    L8:
        Objects.requireNonNull(this.memory);     // Catch: Throwable -> L11
        ByteArrayInputStream r1 = new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount());     // Catch: Throwable -> L11
        monitor-exit(this);
        return r1;
    }

    private void update(int r6) throws IOException {
        if (this.memory != null) goto L5;
        return;
    L5:
        if ((this.memory.getCount() + r6) <= this.fileThreshold) goto L17;
        File r0 = TempFileCreator.INSTANCE.createTempFile("FileBackedOutputStream");
        if (this.resetOnFinalize == false) goto L15;
        r0.deleteOnExit();
    L15:
        FileOutputStream r1 = new FileOutputStream(r0);     // Catch: IOException -> L11
        r1.write(this.memory.getBuffer(), 0, this.memory.getCount());     // Catch: IOException -> L11
        r1.flush();     // Catch: IOException -> L11
        this.out = r1;     // Catch: IOException -> L11
        this.file = r0;
        this.memory = null;
        return;
    L11:
        e = move-exception;
        r0.delete();
        throw e;
    }

    public ByteSource asByteSource() {
        return this.source;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        monitor-enter(this);
        this.out.close();     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public synchronized void flush() throws IOException {
        monitor-enter(this);
        this.out.flush();     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    @CheckForNull
    synchronized File getFile() {
        monitor-enter(this);
        File r0 = this.file;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void reset() throws IOException {
        monitor-enter(this);
        C08481 r0 = null;
        close();     // Catch: Throwable -> L20
    L6:
        if (this.memory != null) goto L8;
        this.memory = new MemoryOutput(r0);     // Catch: Throwable -> L18
    L9:
        this.out = this.memory;     // Catch: Throwable -> L18
        if (this.file == null) goto L16;
        File r1 = this.file;     // Catch: Throwable -> L18
        this.file = null;     // Catch: Throwable -> L18
        if (r1.delete() == true) goto L16;
        throw new IOException("Could not delete: " + r1);     // Catch: Throwable -> L18
    L16:
        monitor-exit(this);
        return;
    L8:
        this.memory.reset();     // Catch: Throwable -> L18
    L18:
        th = move-exception;
        throw th;
    L20:
        th = move-exception;
        if (this.memory != null) goto L24;
        this.memory = new MemoryOutput(r0);     // Catch: Throwable -> L18
    L25:
        this.out = this.memory;     // Catch: Throwable -> L18
        if (this.file == null) goto L31;
        File r2 = this.file;     // Catch: Throwable -> L18
        this.file = null;     // Catch: Throwable -> L18
        if (r2.delete() == true) goto L31;
        throw new IOException("Could not delete: " + r2);     // Catch: Throwable -> L18
    L31:
        throw th;     // Catch: Throwable -> L18
    L24:
        this.memory.reset();     // Catch: Throwable -> L18
        goto L25
    }

    @Override // java.io.OutputStream
    public synchronized void write(int r2) throws IOException {
        monitor-enter(this);
        update(1);     // Catch: Throwable -> L7
        this.out.write(r2);     // Catch: Throwable -> L7
        monitor-exit(this);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] r3) throws IOException {
        monitor-enter(this);
        write(r3, 0, r3.length);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] r2, int r3, int r4) throws IOException {
        monitor-enter(this);
        update(r4);     // Catch: Throwable -> L6
        this.out.write(r2, r3, r4);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }
}
