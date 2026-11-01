package androidx.media3.exoplayer.source.chunk;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceUtil;
import androidx.media3.datasource.DataSpec;
import androidx.media3.extractor.DefaultExtractorInput;
import androidx.media3.extractor.TrackOutput;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class SingleSampleMediaChunk extends BaseMediaChunk {
    private boolean loadCompleted;
    private long nextLoadPosition;
    private final Format sampleFormat;
    private final int trackType;

    public SingleSampleMediaChunk(DataSource r17, DataSpec r18, Format r19, int r20, Object r21, long r22, long r24, long r26, int r28, Format r29) {
        super(r17, r18, r19, r20, r21, r22, r24, C0180C.TIME_UNSET, C0180C.TIME_UNSET, r26);
        this.trackType = r28;
        this.sampleFormat = r29;
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
    public void cancelLoad() {
    }

    @Override // androidx.media3.exoplayer.source.chunk.MediaChunk
    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
    public void load() throws IOException {
        BaseMediaChunkOutput r1 = getOutput();
        r1.setSampleOffsetUs(0);
        TrackOutput r3 = r1.track(0, this.trackType);
        r3.format(this.sampleFormat);
        long r4 = this.dataSource.open(this.dataSpec.subrange(this.nextLoadPosition));     // Catch: Throwable -> L14
        if (r4 == (-1)) goto L6;
        long r8 = r4 + this.nextLoadPosition;     // Catch: Throwable -> L14
    L7:
        DefaultExtractorInput r5 = new DefaultExtractorInput(this.dataSource, this.nextLoadPosition, r8);     // Catch: Throwable -> L14
        int r12 = 0;
    L9:
        if (r12 == (-1)) goto L11;
        this.nextLoadPosition += (long) r12;     // Catch: Throwable -> L14
        r12 = r3.sampleData(r5, Integer.MAX_VALUE, true);     // Catch: Throwable -> L14
        goto L9
    L11:
        r3.sampleMetadata(this.startTimeUs, 1, (int) this.nextLoadPosition, 0, null);     // Catch: Throwable -> L14
        DataSourceUtil.closeQuietly(this.dataSource);
        this.loadCompleted = true;
        return;
    L6:
        r8 = r4;
    L14:
        th = move-exception;
        DataSourceUtil.closeQuietly(this.dataSource);
        throw th;
    }
}
