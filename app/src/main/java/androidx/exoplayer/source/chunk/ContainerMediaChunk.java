package androidx.media3.exoplayer.source.chunk;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceUtil;
import androidx.media3.datasource.DataSpec;
import androidx.media3.extractor.DefaultExtractorInput;
import androidx.media3.extractor.TrackOutput;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class ContainerMediaChunk extends BaseMediaChunk {
    private final int chunkCount;
    private final ChunkExtractor chunkExtractor;
    private volatile boolean loadCanceled;
    private boolean loadCompleted;
    private long nextLoadPosition;
    private final long sampleOffsetUs;

    public ContainerMediaChunk(DataSource r5, DataSpec r6, Format r7, int r8, Object r9, long r10, long r12, long r14, long r16, long r18, int r20, long r21, ChunkExtractor r23) {
        super(r5, r6, r7, r8, r9, r10, r12, r14, r16, r18);
        this.chunkCount = r20;
        this.sampleOffsetUs = r21;
        this.chunkExtractor = r23;
    }

    private void maybeWriteEmptySamples(BaseMediaChunkOutput r13) {
        if (MimeTypes.isImage(this.trackFormat.containerMimeType) == true) goto L6;
        return;
    L6:
        if (this.trackFormat.tileCountHorizontal > 1) goto L10;
        if (this.trackFormat.tileCountVertical > 1) goto L10;
        return;
    L10:
        if (this.trackFormat.tileCountHorizontal != (-1)) goto L12;
        return;
    L12:
        if (this.trackFormat.tileCountVertical == (-1)) goto L21;
        TrackOutput r2 = r13.track(0, 4);
        int r0 = this.trackFormat.tileCountHorizontal * this.trackFormat.tileCountVertical;
        long r9 = (this.endTimeUs - this.startTimeUs) / ((long) r0);
        int r11 = 1;
    L15:
        if (r11 >= r0) goto L17;
        r2.sampleData(new ParsableByteArray(), 0);
        r2.sampleMetadata(((long) r11) * r9, 0, 0, 0, null);
        r11 = r11 + 1;
        goto L15
    L17:
        return;
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
    public final void cancelLoad() {
        this.loadCanceled = true;
    }

    @Override // androidx.media3.exoplayer.source.chunk.MediaChunk
    public long getNextChunkIndex() {
        return this.chunkIndex + ((long) this.chunkCount);
    }

    protected ChunkExtractor.TrackOutputProvider getTrackOutputProvider(BaseMediaChunkOutput r1) {
        return r1;
    }

    @Override // androidx.media3.exoplayer.source.chunk.MediaChunk
    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
    public final void load() throws IOException {
        BaseMediaChunkOutput r1 = getOutput();
        if (this.nextLoadPosition != 0) goto L29;
        r1.setSampleOffsetUs(this.sampleOffsetUs);
        ChunkExtractor r4 = this.chunkExtractor;
        ChunkExtractor.TrackOutputProvider r5 = getTrackOutputProvider(r1);
        long r2 = this.clippedStartTimeUs;
        long r6 = C0180C.TIME_UNSET;
        if (r2 != C0180C.TIME_UNSET) goto L7;
        long r3 = -9223372036854775807L;
    L9:
        if (this.clippedEndTimeUs == C0180C.TIME_UNSET) goto L12;
        r6 = this.clippedEndTimeUs - this.sampleOffsetUs;
    L12:
        r4.init(r5, r3, r6);
        goto L29
    L7:
        r3 = this.clippedStartTimeUs - this.sampleOffsetUs;
    L29:
        DataSpec r0 = this.dataSpec.subrange(this.nextLoadPosition);     // Catch: Throwable -> L26
        DefaultExtractorInput r7 = new DefaultExtractorInput(this.dataSource, r0.position, this.dataSource.open(r0));     // Catch: Throwable -> L26
    L30:
    L23:
        th = move-exception;
        this.nextLoadPosition = r7.getPosition() - this.dataSpec.position;     // Catch: Throwable -> L26
        throw th;     // Catch: Throwable -> L26
    L15:
        if (this.loadCanceled == true) goto L19;
        if (this.chunkExtractor.read(r7) == true) goto L30;
    L19:
        maybeWriteEmptySamples(r1);     // Catch: Throwable -> L23
        this.nextLoadPosition = r7.getPosition() - this.dataSpec.position;     // Catch: Throwable -> L26
        DataSourceUtil.closeQuietly(this.dataSource);
        this.loadCompleted = !this.loadCanceled;
        return;
    L26:
        th = move-exception;
        DataSourceUtil.closeQuietly(this.dataSource);
        throw th;
    }
}
