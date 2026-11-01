package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.common.util.Assertions;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class ResolvingDataSource implements DataSource {
    private final Resolver resolver;
    private final DataSource upstreamDataSource;
    private boolean upstreamOpened;

    public static final class Factory implements DataSource.Factory {
        private final Resolver resolver;
        private final DataSource.Factory upstreamFactory;

        public Factory(DataSource.Factory r1, Resolver r2) {
            this.upstreamFactory = r1;
            this.resolver = r2;
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        public /* bridge */ /* synthetic */ DataSource createDataSource() {
            return createDataSource();
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        public ResolvingDataSource createDataSource() {
            return new ResolvingDataSource(this.upstreamFactory.createDataSource(), this.resolver);
        }
    }

    public interface Resolver {

        /* JADX INFO: renamed from: androidx.media3.datasource.ResolvingDataSource$Resolver$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static Uri $default$resolveReportedUri(Resolver r0, Uri r1) {
                return r1;
            }
        }

        DataSpec resolveDataSpec(DataSpec r1) throws IOException;

        Uri resolveReportedUri(Uri r1);
    }

    public ResolvingDataSource(DataSource r1, Resolver r2) {
        this.upstreamDataSource = r1;
        this.resolver = r2;
    }

    @Override // androidx.media3.datasource.DataSource
    public void addTransferListener(TransferListener r2) {
        Assertions.checkNotNull(r2);
        this.upstreamDataSource.addTransferListener(r2);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws IOException {
        if (this.upstreamOpened == false) goto L6;
        this.upstreamOpened = false;
        this.upstreamDataSource.close();
        return;
    }

    @Override // androidx.media3.datasource.DataSource
    public Map<String, List<String>> getResponseHeaders() {
        return this.upstreamDataSource.getResponseHeaders();
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        Uri r0 = this.upstreamDataSource.getUri();
        if (r0 != null) goto L6;
        return null;
    L6:
        return this.resolver.resolveReportedUri(r0);
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r4) throws IOException {
        DataSpec r0 = this.resolver.resolveDataSpec(r4);
        this.upstreamOpened = true;
        return this.upstreamDataSource.open(r0);
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r2, int r3, int r4) throws IOException {
        return this.upstreamDataSource.read(r2, r3, r4);
    }
}
