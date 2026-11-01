package androidx.media3.datasource;

import android.net.Uri;
import android.util.Base64;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: classes.dex */
public final class DataSchemeDataSource extends BaseDataSource {
    public static final String SCHEME_DATA = "data";
    private int bytesRemaining;
    private byte[] data;
    private DataSpec dataSpec;
    private int readPosition;

    public DataSchemeDataSource() {
        super(false);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() {
        if (this.data == null) goto L5;
        this.data = null;
        transferEnded();
    L5:
        this.dataSpec = null;
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        if (this.dataSpec != null) goto L7;
        return null;
    L7:
        return this.dataSpec.uri;
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r11) throws IOException {
        transferInitializing(r11);
        this.dataSpec = r11;
        Uri r0 = r11.uri.normalizeScheme();
        String r1 = r0.getScheme();
        Assertions.checkArgument("data".equals(r1), "Unsupported scheme: " + r1);
        String[] r2 = Util.split(r0.getSchemeSpecificPart(), ",");
        if (r2.length != 2) goto L25;
        String r3 = r2[1];
        if (r2[0].contains(";base64") == true) goto L26;
        this.data = Util.getUtf8Bytes(URLDecoder.decode(r3, StandardCharsets.US_ASCII.name()));
    L13:
        if (r11.position > this.data.length) goto L22;
        this.readPosition = (int) r11.position;
        this.bytesRemaining = this.data.length - this.readPosition;
        if (r11.length == (-1)) goto L17;
        this.bytesRemaining = (int) Math.min(this.bytesRemaining, r11.length);
    L17:
        transferStarted(r11);
        if (r11.length == (-1)) goto L21;
        return r11.length;
    L21:
        return this.bytesRemaining;
    L22:
        this.data = null;
        throw new DataSourceException(2008);
    L26:
        this.data = Base64.decode(r3, 0);     // Catch: IllegalArgumentException -> L8
    L8:
        e = move-exception;
        throw ParserException.createForMalformedDataOfUnknownType("Error while parsing Base64 encoded string: " + r3, e);
    L25:
        throw ParserException.createForMalformedDataOfUnknownType("Unexpected URI format: " + r0, null);
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r3, int r4, int r5) {
        if (r5 != 0) goto L6;
        return 0;
    L6:
        if (this.bytesRemaining != 0) goto L9;
        return -1;
    L9:
        int r6 = Math.min(r5, this.bytesRemaining);
        System.arraycopy(Util.castNonNull(this.data), this.readPosition, r3, r4, r6);
        this.readPosition += r6;
        this.bytesRemaining -= r6;
        bytesTransferred(r6);
        return r6;
    }
}
