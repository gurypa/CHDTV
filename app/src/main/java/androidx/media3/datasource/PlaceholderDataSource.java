package androidx.media3.datasource;

import android.net.Uri;
import java.io.IOException;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class PlaceholderDataSource implements DataSource {
    public static final DataSource.Factory FACTORY = null;
    public static final PlaceholderDataSource INSTANCE = null;

    public static /* synthetic */ PlaceholderDataSource $r8$lambda$HDM3559DY8vWpsat317RpDbLVt0() {
        return new PlaceholderDataSource();
    }

    static {
        INSTANCE = new PlaceholderDataSource();
        FACTORY = new PlaceholderDataSource$$ExternalSyntheticLambda0();
    }

    private PlaceholderDataSource() {
    }

    @Override // androidx.media3.datasource.DataSource
    public void addTransferListener(TransferListener r1) {
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() {
    }

    @Override // androidx.media3.datasource.DataSource
    public /* synthetic */ Map getResponseHeaders() {
        return DataSource.CC.$default$getResponseHeaders(this);
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        return null;
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r3) throws IOException {
        throw new IOException("PlaceholderDataSource cannot be opened");
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r2, int r3, int r4) {
        throw new UnsupportedOperationException();
    }
}
