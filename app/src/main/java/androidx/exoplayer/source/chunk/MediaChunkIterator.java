package androidx.media3.exoplayer.source.chunk;

import androidx.media3.datasource.DataSpec;

/* JADX INFO: loaded from: classes.dex */
public interface MediaChunkIterator {
    public static final MediaChunkIterator EMPTY = null;

    static {
        EMPTY = new C03511();
    }

    long getChunkEndTimeUs();

    long getChunkStartTimeUs();

    DataSpec getDataSpec();

    boolean isEnded();

    boolean next();

    void reset();
}
