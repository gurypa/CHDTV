package androidx.media3.datasource;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class RawResourceDataSource extends BaseDataSource {

    @Deprecated
    public static final String RAW_RESOURCE_SCHEME = "rawresource";
    private final Context applicationContext;
    private AssetFileDescriptor assetFileDescriptor;
    private long bytesRemaining;
    private DataSpec dataSpec;
    private InputStream inputStream;
    private boolean opened;

    public static class RawResourceDataSourceException extends DataSourceException {
        @Deprecated
        public RawResourceDataSourceException(String r3) {
            super(r3, null, 2000);
        }

        public RawResourceDataSourceException(String r1, Throwable r2, int r3) {
            super(r1, r2, r3);
        }

        @Deprecated
        public RawResourceDataSourceException(Throwable r2) {
            super(r2, 2000);
        }
    }

    public RawResourceDataSource(Context r2) {
        super(false);
        this.applicationContext = r2.getApplicationContext();
    }

    @Deprecated
    public static Uri buildRawResourceUri(int r2) {
        return Uri.parse("rawresource:///" + r2);
    }

    private static AssetFileDescriptor openAssetFileDescriptor(Context r9, DataSpec r10) throws RawResourceDataSourceException {
        Uri r0 = r10.uri.normalizeScheme();
        if (TextUtils.equals(RAW_RESOURCE_SCHEME, r0.getScheme()) == false) goto L10;
        Resources r1 = r9.getResources();
        List<String> r5 = r0.getPathSegments();
        if (r5.size() != 1) goto L8;
        int r2 = parseResourceId(r5.get(0));
    L47:
        AssetFileDescriptor r3 = r1.openRawResourceFd(r2);     // Catch: Resources.NotFoundException -> L35
        if (r3 == null) goto L34;
        return r3;
    L34:
        throw new RawResourceDataSourceException("Resource is compressed: " + r0, null, 2000);
    L35:
        e = move-exception;
        throw new RawResourceDataSourceException(null, e, PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND);
    L8:
        throw new RawResourceDataSourceException("rawresource:// URI must have exactly one path element, found " + r5.size());
    L10:
        if (TextUtils.equals("android.resource", r0.getScheme()) == false) goto L44;
        String r4 = (String) Assertions.checkNotNull(r0.getPath());
        if (r4.startsWith("/") == false) goto L15;
        r4 = r4.substring(1);
    L15:
        if (TextUtils.isEmpty(r0.getHost()) == false) goto L17;
        String r6 = r9.getPackageName();
    L19:
        if (r6.equals(r9.getPackageName()) == false) goto L45;
        Resources r7 = r9.getResources();
    L24:
        if (r4.matches("\\d+") == false) goto L26;
        r2 = parseResourceId(r4);
    L29:
        r1 = r7;
        goto L47
    L26:
        int r8 = r7.getIdentifier(r6 + ":" + r4, "raw", null);
        if (r8 == 0) goto L39;
        r2 = r8;
        goto L29
    L39:
        throw new RawResourceDataSourceException("Resource not found.", null, PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND);
    L45:
        r7 = r9.getPackageManager().getResourcesForApplication(r6);     // Catch: PackageManager.NameNotFoundException -> L40
    L40:
        e = move-exception;
        throw new RawResourceDataSourceException("Package in android.resource:// URI not found. Check http://g.co/dev/packagevisibility.", e, PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND);
    L17:
        r6 = r0.getHost();
        goto L19
    L44:
        throw new RawResourceDataSourceException("Unsupported URI scheme (" + r0.getScheme() + "). Only android.resource is supported.", null, 1004);
    }

    private static int parseResourceId(String r5) throws RawResourceDataSourceException {
        return Integer.parseInt(r5);
    L6:
        throw new RawResourceDataSourceException("Resource identifier must be an integer.", null, 1004);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws RawResourceDataSourceException {
        this.dataSpec = null;
    L24:
        th = move-exception;
        this.inputStream = null;
    L37:
        th = move-exception;
        this.assetFileDescriptor = null;
        if (this.opened == false) goto L45;
        this.opened = false;
        transferEnded();
    L45:
        throw th;
    L39:
        e = move-exception;
        throw new RawResourceDataSourceException(null, e, 2000);     // Catch: Throwable -> L37
    L31:
        if (this.assetFileDescriptor == null) goto L33;
        this.assetFileDescriptor.close();     // Catch: Throwable -> L37 IOException -> L39
    L33:
        this.assetFileDescriptor = null;
        if (this.opened == false) goto L36;
        this.opened = false;
        transferEnded();
    L36:
        throw th;
    L26:
        e = move-exception;
        throw new RawResourceDataSourceException(null, e, 2000);     // Catch: Throwable -> L24
    L4:
        if (this.inputStream == null) goto L6;
        this.inputStream.close();     // Catch: Throwable -> L24 IOException -> L26
    L6:
        this.inputStream = null;
    L17:
        e = move-exception;
        throw new RawResourceDataSourceException(null, e, 2000);     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        this.assetFileDescriptor = null;
        if (this.opened == false) goto L23;
        this.opened = false;
        transferEnded();
    L23:
        throw th;
    L8:
        if (this.assetFileDescriptor == null) goto L10;
        this.assetFileDescriptor.close();     // Catch: Throwable -> L15 IOException -> L17
    L10:
        this.assetFileDescriptor = null;
        if (this.opened == false) goto L53;
        this.opened = false;
        transferEnded();
        return;
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        if (this.dataSpec != null) goto L7;
        return null;
    L7:
        return this.dataSpec.uri;
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r23) throws RawResourceDataSourceException {
        this.dataSpec = r23;
        transferInitializing(r23);
        this.assetFileDescriptor = openAssetFileDescriptor(this.applicationContext, r23);
        long r3 = this.assetFileDescriptor.getLength();
        FileInputStream r0 = new FileInputStream(this.assetFileDescriptor.getFileDescriptor());
        this.inputStream = r0;
        long r6 = -1;
        if (r3 != (-1)) goto L46;
    L9:
        long r10 = this.assetFileDescriptor.getStartOffset();     // Catch: IOException -> L41 RawResourceDataSourceException -> L44
        long r12 = r0.skip(r23.position + r10) - r10;     // Catch: IOException -> L41 RawResourceDataSourceException -> L44
        if (r12 != r23.position) goto L40;
        if (r3 != (-1)) goto L21;
        FileChannel r1 = r0.getChannel();     // Catch: IOException -> L41 RawResourceDataSourceException -> L44
        if (r1.size() != 0) goto L16;
        this.bytesRemaining = -1;     // Catch: IOException -> L41 RawResourceDataSourceException -> L44
    L26:
        if (r23.length != r6) goto L28;
    L32:
        this.opened = true;
        transferStarted(r23);
        if (r23.length == r6) goto L36;
        return r23.length;
    L36:
        return this.bytesRemaining;
    L28:
        if (this.bytesRemaining != r6) goto L30;
        long r7 = r23.length;
    L31:
        this.bytesRemaining = r7;
        goto L32
    L30:
        r7 = Math.min(this.bytesRemaining, r23.length);
        goto L31
    L16:
        this.bytesRemaining = r1.size() - r1.position();     // Catch: IOException -> L41 RawResourceDataSourceException -> L44
        if (this.bytesRemaining >= 0) goto L26;
        throw new RawResourceDataSourceException(null, null, 2008);     // Catch: IOException -> L41 RawResourceDataSourceException -> L44
    L21:
        r6 = -1;
        this.bytesRemaining = r3 - r12;     // Catch: IOException -> L41 RawResourceDataSourceException -> L44
        if (this.bytesRemaining >= 0) goto L26;
        throw new DataSourceException(2008);     // Catch: IOException -> L41 RawResourceDataSourceException -> L44
    L40:
        throw new RawResourceDataSourceException(null, null, 2008);     // Catch: IOException -> L41 RawResourceDataSourceException -> L44
    L46:
    L44:
        e = move-exception;
        throw e;
    L41:
        e = move-exception;
        throw new RawResourceDataSourceException(null, e, 2000);
    L5:
        if (r23.position <= r3) goto L9;
        throw new RawResourceDataSourceException(null, null, 2008);     // Catch: IOException -> L41 RawResourceDataSourceException -> L44
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r9, int r10, int r11) throws RawResourceDataSourceException {
        if (r11 != 0) goto L6;
        return 0;
    L6:
        if (this.bytesRemaining != 0) goto L28;
        return -1;
    L28:
    L25:
        e = move-exception;
        throw new RawResourceDataSourceException(null, e, 2000);
    L10:
        if (this.bytesRemaining != (-1)) goto L12;
        int r2 = r11;
    L13:
        int r3 = ((InputStream) Util.castNonNull(this.inputStream)).read(r9, r10, r2);     // Catch: IOException -> L25
        if (r3 != (-1)) goto L21;
        if (this.bytesRemaining != (-1)) goto L19;
        return -1;
    L19:
        throw new RawResourceDataSourceException("End of stream reached having not read sufficient data.", new EOFException(), 2000);
    L21:
        if (this.bytesRemaining == (-1)) goto L23;
        this.bytesRemaining -= (long) r3;
    L23:
        bytesTransferred(r3);
        return r3;
    L12:
        r2 = (int) Math.min(this.bytesRemaining, r11);     // Catch: IOException -> L25
        goto L13
    }
}
