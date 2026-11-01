package androidx.media3.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
public final class AtomicFile {
    private static final String TAG = "AtomicFile";
    private final File backupName;
    private final File baseName;

    private static final class AtomicFileOutputStream extends OutputStream {
        private boolean closed;
        private final FileOutputStream fileOutputStream;

        public AtomicFileOutputStream(File r2) throws FileNotFoundException {
            this.closed = false;
            this.fileOutputStream = new FileOutputStream(r2);
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.closed == false) goto L5;
            return;
        L5:
            this.closed = true;
            flush();
            this.fileOutputStream.getFD().sync();     // Catch: IOException -> L8
        L10:
            this.fileOutputStream.close();
            return;
        L8:
            e = move-exception;
            Log.m49w(AtomicFile.TAG, "Failed to sync file descriptor:", e);
            goto L10
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            this.fileOutputStream.flush();
        }

        @Override // java.io.OutputStream
        public void write(int r2) throws IOException {
            this.fileOutputStream.write(r2);
        }

        @Override // java.io.OutputStream
        public void write(byte[] r2) throws IOException {
            this.fileOutputStream.write(r2);
        }

        @Override // java.io.OutputStream
        public void write(byte[] r2, int r3, int r4) throws IOException {
            this.fileOutputStream.write(r2, r3, r4);
        }
    }

    public AtomicFile(File r4) {
        this.baseName = r4;
        this.backupName = new File(r4.getPath() + ".bak");
    }

    private void restoreBackup() {
        if (this.backupName.exists() == false) goto L6;
        this.baseName.delete();
        this.backupName.renameTo(this.baseName);
        return;
    }

    public void delete() {
        this.baseName.delete();
        this.backupName.delete();
    }

    public void endWrite(OutputStream r2) throws IOException {
        r2.close();
        this.backupName.delete();
    }

    public boolean exists() {
        if (this.baseName.exists() == false) goto L5;
    L8:
        return true;
    L5:
        if (this.backupName.exists() == true) goto L8;
        return false;
    }

    public InputStream openRead() throws FileNotFoundException {
        restoreBackup();
        return new FileInputStream(this.baseName);
    }

    public OutputStream startWrite() throws IOException {
        if (this.baseName.exists() == true) goto L5;
    L25:
        return new AtomicFileOutputStream(this.baseName);
    L12:
        e = move-exception;
        File r1 = this.baseName.getParentFile();
        if (r1 == null) goto L24;
        if (r1.mkdirs() == false) goto L24;
        return new AtomicFileOutputStream(this.baseName);
    L20:
        e = move-exception;
        throw new IOException("Couldn't create " + this.baseName, e);
    L24:
        throw new IOException("Couldn't create " + this.baseName, e);
    L5:
        if (this.backupName.exists() == false) goto L7;
        this.baseName.delete();
        goto L25
    L7:
        if (this.baseName.renameTo(this.backupName) == true) goto L25;
        Log.m48w(TAG, "Couldn't rename file " + this.baseName + " to backup file " + this.backupName);
        goto L25
    }
}
