package androidx.media3.exoplayer.upstream;

import android.net.Uri;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceInputStream;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.StatsDataSource;
import androidx.media3.exoplayer.source.LoadEventInfo;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class ParsingLoadable<T> implements Loader.Loadable {
    private final StatsDataSource dataSource;
    public final DataSpec dataSpec;
    public final long loadTaskId;
    private final Parser<? extends T> parser;
    private volatile T result;
    public final int type;

    public interface Parser<T> {
        T parse(Uri r1, InputStream r2) throws IOException;
    }

    public ParsingLoadable(DataSource r3, Uri r4, int r5, Parser<? extends T> r6) {
        this(r3, new DataSpec.Builder().setUri(r4).setFlags(1).build(), r5, r6);
    }

    public ParsingLoadable(DataSource r3, DataSpec r4, int r5, Parser<? extends T> r6) {
        this.dataSource = new StatsDataSource(r3);
        this.dataSpec = r4;
        this.type = r5;
        this.parser = r6;
        this.loadTaskId = LoadEventInfo.getNewId();
    }

    public static <T> T load(DataSource r2, Parser<? extends T> r3, Uri r4, int r5) throws IOException {
        ParsingLoadable r0 = new ParsingLoadable(r2, r4, r5, r3);
        r0.load();
        return (T) Assertions.checkNotNull(r0.getResult());
    }

    public static <T> T load(DataSource r2, Parser<? extends T> r3, DataSpec r4, int r5) throws IOException {
        ParsingLoadable r0 = new ParsingLoadable(r2, r4, r5, r3);
        r0.load();
        return (T) Assertions.checkNotNull(r0.getResult());
    }

    public long bytesLoaded() {
        return this.dataSource.getBytesRead();
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
    public final void cancelLoad() {
    }

    public Map<String, List<String>> getResponseHeaders() {
        return this.dataSource.getLastResponseHeaders();
    }

    public final T getResult() {
        return this.result;
    }

    public Uri getUri() {
        return this.dataSource.getLastOpenedUri();
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
    public final void load() throws IOException {
        this.dataSource.resetBytesRead();
        DataSourceInputStream r0 = new DataSourceInputStream(this.dataSource, this.dataSpec);
        r0.open();     // Catch: Throwable -> L6
        Uri r1 = (Uri) Assertions.checkNotNull(this.dataSource.getUri());     // Catch: Throwable -> L6
        this.result = this.parser.parse(r1, r0);     // Catch: Throwable -> L6
        Util.closeQuietly(r0);
        return;
    L6:
        th = move-exception;
        Util.closeQuietly(r0);
        throw th;
    }
}
