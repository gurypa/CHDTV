package androidx.core.util;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class AtomicFile {
    private static final String LOG_TAG = "AtomicFile";
    private final File mBaseName;
    private final File mLegacyBackupName;
    private final File mNewName;

    public AtomicFile(File r4) {
        this.mBaseName = r4;
        this.mNewName = new File(r4.getPath() + ".new");
        this.mLegacyBackupName = new File(r4.getPath() + ".bak");
    }

    private static void rename(File r3, File r4) {
        if (r4.isDirectory() == false) goto L8;
        if (r4.delete() == true) goto L8;
        Log.e(LOG_TAG, "Failed to delete file which is a directory " + r4);
    L8:
        if (r3.renameTo(r4) == true) goto L11;
        Log.e(LOG_TAG, "Failed to rename " + r3 + " to " + r4);
        return;
    }

    private static boolean sync(FileOutputStream r1) {
        r1.getFD().sync();     // Catch: IOException -> L5
        return true;
    L6:
        return false;
    }

    public void delete() {
        this.mBaseName.delete();
        this.mNewName.delete();
        this.mLegacyBackupName.delete();
    }

    public void failWrite(FileOutputStream r4) {
        if (r4 != null) goto L5;
        return;
    L5:
        if (sync(r4) == true) goto L15;
        Log.e(LOG_TAG, "Failed to sync file output stream");
    L15:
        r4.close();     // Catch: IOException -> L9
    L12:
        if (this.mNewName.delete() == true) goto L17;
        Log.e(LOG_TAG, "Failed to delete new file " + this.mNewName);
        return;
    L17:
        return;
    L9:
        e = move-exception;
        Log.e(LOG_TAG, "Failed to close file output stream", e);
        goto L12
    }

    public void finishWrite(FileOutputStream r4) {
        if (r4 != null) goto L5;
        return;
    L5:
        if (sync(r4) == true) goto L13;
        Log.e(LOG_TAG, "Failed to sync file output stream");
    L13:
        r4.close();     // Catch: IOException -> L9
    L11:
        rename(this.mNewName, this.mBaseName);
        return;
    L9:
        e = move-exception;
        Log.e(LOG_TAG, "Failed to close file output stream", e);
        goto L11
    }

    public File getBaseFile() {
        return this.mBaseName;
    }

    public FileInputStream openRead() throws FileNotFoundException {
        if (this.mLegacyBackupName.exists() == false) goto L6;
        rename(this.mLegacyBackupName, this.mBaseName);
    L6:
        if (this.mNewName.exists() == false) goto L13;
        if (this.mBaseName.exists() == false) goto L13;
        if (this.mNewName.delete() == true) goto L13;
        Log.e(LOG_TAG, "Failed to delete outdated new file " + this.mNewName);
    L13:
        return new FileInputStream(this.mBaseName);
    }

    public byte[] readFully() throws IOException {
        FileInputStream r0 = openRead();
        int r1 = 0;
        byte[] r3 = new byte[r0.available()];     // Catch: Throwable -> L14
    L4:
        int r4 = r0.read(r3, r1, r3.length - r1);     // Catch: Throwable -> L14
        if (r4 <= 0) goto L6;
        r1 = r1 + r4;
        int r5 = r0.available();     // Catch: Throwable -> L14
        if (r5 <= (r3.length - r1)) goto L4;
        byte[] r6 = new byte[r1 + r5];     // Catch: Throwable -> L14
        System.arraycopy(r3, 0, r6, 0, r1);     // Catch: Throwable -> L14
        r3 = r6;
        goto L4
    L6:
        r0.close();
        return r3;
    L14:
        th = move-exception;
        r0.close();
        throw th;
    }

    public FileOutputStream startWrite() throws IOException {
        if (this.mLegacyBackupName.exists() == false) goto L17;
        rename(this.mLegacyBackupName, this.mBaseName);
    L17:
        return new FileOutputStream(this.mNewName);
    L9:
        if (this.mNewName.getParentFile().mkdirs() == false) goto L16;
        return new FileOutputStream(this.mNewName);
    L12:
        e = move-exception;
        throw new IOException("Failed to create new file " + this.mNewName, e);
    L16:
        throw new IOException("Failed to create directory for " + this.mNewName);
    }
}
