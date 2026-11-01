package androidx.media3.exoplayer.source.chunk;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceUtil;
import androidx.media3.datasource.DataSpec;
import androidx.media3.extractor.ChunkIndex;
import androidx.media3.extractor.DefaultExtractorInput;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class InitializationChunk extends Chunk {
    private final ChunkExtractor chunkExtractor;
    private ChunkIndex chunkIndex;
    private volatile boolean loadCanceled;
    private long nextLoadPosition;
    private ChunkExtractor.TrackOutputProvider trackOutputProvider;

    public InitializationChunk(DataSource r12, DataSpec r13, Format r14, int r15, Object r16, ChunkExtractor r17) {
        super(r12, r13, 2, r14, r15, r16, C0180C.TIME_UNSET, C0180C.TIME_UNSET);
        this.chunkExtractor = r17;
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
    public void cancelLoad() {
        this.loadCanceled = true;
    }

    public ChunkIndex getChunkIndex() {
        return this.chunkIndex;
    }

    public void init(ChunkExtractor.TrackOutputProvider r1) {
        this.trackOutputProvider = r1;
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
    public void load() throws IOException {
        if (this.nextLoadPosition != 0) goto L22;
        this.chunkExtractor.init(this.trackOutputProvider, C0180C.TIME_UNSET, C0180C.TIME_UNSET);
    L22:
        DataSpec r0 = this.dataSpec.subrange(this.nextLoadPosition);     // Catch: Throwable -> L17
        DefaultExtractorInput r2 = new DefaultExtractorInput(this.dataSource, r0.position, this.dataSource.open(r0));     // Catch: Throwable -> L17
    L20:
    L14:
        th = move-exception;
        this.nextLoadPosition = r2.getPosition() - this.dataSpec.position;     // Catch: Throwable -> L17
        this.chunkIndex = this.chunkExtractor.getChunkIndex();     // Catch: Throwable -> L17
        throw th;     // Catch: Throwable -> L17
    L7:
        if (this.loadCanceled == true) goto L11;
        if (this.chunkExtractor.read(r2) == false) goto L11;
    L11:
        this.nextLoadPosition = r2.getPosition() - this.dataSpec.position;     // Catch: Throwable -> L17
        this.chunkIndex = this.chunkExtractor.getChunkIndex();     // Catch: Throwable -> L17
        DataSourceUtil.closeQuietly(this.dataSource);
        return;
    L17:
        th = move-exception;
        DataSourceUtil.closeQuietly(this.dataSource);
        throw th;
    }
}
