package androidx.media3.exoplayer.source.chunk;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceUtil;
import androidx.media3.datasource.DataSpec;
import java.io.IOException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public abstract class DataChunk extends Chunk {
    private static final int READ_GRANULARITY = 16384;
    private byte[] data;
    private volatile boolean loadCanceled;

    public DataChunk(DataSource r12, DataSpec r13, int r14, Format r15, int r16, Object r17, byte[] r18) {
        super(r12, r13, r14, r15, r16, r17, C0180C.TIME_UNSET, C0180C.TIME_UNSET);
        if (r18 != null) goto L5;
        byte[] r1 = Util.EMPTY_BYTE_ARRAY;
    L6:
        this.data = r1;
        return;
    L5:
        r1 = r18;
        goto L6
    }

    private void maybeExpandData(int r3) {
        if (this.data.length >= (r3 + 16384)) goto L6;
        this.data = Arrays.copyOf(this.data, this.data.length + 16384);
        return;
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
    public final void cancelLoad() {
        this.loadCanceled = true;
    }

    protected abstract void consume(byte[] r1, int r2) throws IOException;

    public byte[] getDataHolder() {
        return this.data;
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
    public final void load() throws IOException {
        this.dataSource.open(this.dataSpec);     // Catch: Throwable -> L15
        int r0 = 0;
        int r1 = 0;
    L4:
        if (r1 == (-1)) goto L11;
        if (this.loadCanceled == true) goto L11;
        maybeExpandData(r0);     // Catch: Throwable -> L15
        r1 = this.dataSource.read(this.data, r0, 16384);     // Catch: Throwable -> L15
        if (r1 == (-1)) goto L4;
        r0 = r0 + r1;     // Catch: Throwable -> L15
    L11:
        if (this.loadCanceled == true) goto L13;
        consume(this.data, r0);     // Catch: Throwable -> L15
    L13:
        DataSourceUtil.closeQuietly(this.dataSource);
        return;
    L15:
        th = move-exception;
        DataSourceUtil.closeQuietly(this.dataSource);
        throw th;
    }
}
