package androidx.media3.datasource;

import android.net.Uri;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.collect.Sets;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class FileDescriptorDataSource extends BaseDataSource {
    private static final Set<FileDescriptor> inUseFileDescriptors = null;
    private long bytesRemaining;
    private final FileDescriptor fileDescriptor;
    private FileInputStream inputStream;
    private final long length;
    private final long offset;
    private boolean opened;
    private Uri uri;

    static {
        inUseFileDescriptors = Sets.newConcurrentHashSet();
    }

    public FileDescriptorDataSource(FileDescriptor r2, long r3, long r5) {
        super(false);
        this.fileDescriptor = (FileDescriptor) Assertions.checkNotNull(r2);
        this.offset = r3;
        this.length = r5;
    }

    private static void seekFileDescriptor(FileDescriptor r3, long r4) throws DataSourceException {
        Os.lseek(r3, r4, OsConstants.SEEK_SET);     // Catch: ErrnoException -> L4
        return;
    L4:
        e = move-exception;
        throw new DataSourceException(e, 2000);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws DataSourceException {
        this.uri = null;
        inUseFileDescriptors.remove(this.fileDescriptor);
    L10:
        th = move-exception;
        this.inputStream = null;
        if (this.opened == false) goto L18;
        this.opened = false;
        transferEnded();
    L18:
        throw th;
    L12:
        e = move-exception;
        throw new DataSourceException(e, 2000);     // Catch: Throwable -> L10
    L4:
        if (this.inputStream == null) goto L6;
        this.inputStream.close();     // Catch: Throwable -> L10 IOException -> L12
    L6:
        this.inputStream = null;
        if (this.opened == false) goto L21;
        this.opened = false;
        transferEnded();
        return;
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r11) throws DataSourceException {
        this.uri = r11.uri;     // Catch: IOException -> L41 DataSourceException -> L48
        transferInitializing(r11);     // Catch: IOException -> L41 DataSourceException -> L48
        if (inUseFileDescriptors.add(this.fileDescriptor) == false) goto L40;
        if (this.length != (-1)) goto L7;
    L11:
        seekFileDescriptor(this.fileDescriptor, this.offset + r11.position);     // Catch: IOException -> L41 DataSourceException -> L48
        this.inputStream = new FileInputStream(this.fileDescriptor);     // Catch: IOException -> L41 DataSourceException -> L48
        if (this.length != (-1)) goto L21;
        FileChannel r0 = this.inputStream.getChannel();     // Catch: IOException -> L41 DataSourceException -> L48
        long r6 = r0.size();     // Catch: IOException -> L41 DataSourceException -> L48
        if (r6 != 0) goto L16;
        this.bytesRemaining = -1;     // Catch: IOException -> L41 DataSourceException -> L48
    L26:
        if (r11.length != (-1)) goto L28;
    L32:
        this.opened = true;
        transferStarted(r11);
        if (r11.length == (-1)) goto L36;
        return r11.length;
    L36:
        return this.bytesRemaining;
    L28:
        if (this.bytesRemaining != (-1)) goto L30;
        long r1 = r11.length;
    L31:
        this.bytesRemaining = r1;
        goto L32
    L30:
        r1 = Math.min(this.bytesRemaining, r11.length);
        goto L31
    L16:
        this.bytesRemaining = r6 - r0.position();     // Catch: IOException -> L41 DataSourceException -> L48
        if (this.bytesRemaining >= 0) goto L26;
        throw new DataSourceException(2008);     // Catch: IOException -> L41 DataSourceException -> L48
    L21:
        this.bytesRemaining = this.length - r11.position;     // Catch: IOException -> L41 DataSourceException -> L48
        if (this.bytesRemaining >= 0) goto L26;
        throw new DataSourceException(2008);     // Catch: IOException -> L41 DataSourceException -> L48
    L7:
        if (r11.position <= this.length) goto L11;
        throw new DataSourceException(2008);     // Catch: IOException -> L41 DataSourceException -> L48
    L40:
        throw new DataSourceException(new IllegalStateException("Attempted to re-use an already in-use file descriptor"), -2);     // Catch: IOException -> L41 DataSourceException -> L48
    L48:
        e = move-exception;
        throw e;
    L41:
        e = move-exception;
        if ((e instanceof FileNotFoundException) == false) goto L45;
        int r2 = PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND;
    L47:
        throw new DataSourceException(e, r2);
    L45:
        r2 = 2000;
        goto L47
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r9, int r10, int r11) throws DataSourceException {
        if (r11 != 0) goto L6;
        return 0;
    L6:
        if (this.bytesRemaining != 0) goto L9;
        return -1;
    L9:
        if (this.bytesRemaining != (-1)) goto L11;
        int r0 = r11;
    L23:
        int r2 = ((FileInputStream) Util.castNonNull(this.inputStream)).read(r9, r10, r0);     // Catch: IOException -> L20
        if (r2 != (-1)) goto L16;
        return -1;
    L16:
        if (this.bytesRemaining == (-1)) goto L18;
        this.bytesRemaining -= (long) r2;
    L18:
        bytesTransferred(r2);
        return r2;
    L20:
        e = move-exception;
        throw new DataSourceException(e, 2000);
    L11:
        r0 = (int) Math.min(this.bytesRemaining, r11);
        goto L23
    }
}
