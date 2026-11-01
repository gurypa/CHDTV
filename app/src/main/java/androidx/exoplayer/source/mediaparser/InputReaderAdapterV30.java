package androidx.media3.exoplayer.source.mediaparser;

import android.media.MediaParser;
import androidx.media3.common.DataReader;
import androidx.media3.common.util.Util;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class InputReaderAdapterV30 implements MediaParser.SeekableInputReader {
    private long currentPosition;
    private DataReader dataReader;
    private long lastSeekPosition;
    private long resourceLength;

    public InputReaderAdapterV30() {
    }

    public long getAndResetSeekPosition() {
        long r0 = this.lastSeekPosition;
        this.lastSeekPosition = -1;
        return r0;
    }

    @Override // android.media.MediaParser.InputReader
    public long getLength() {
        return this.resourceLength;
    }

    @Override // android.media.MediaParser.InputReader
    public long getPosition() {
        return this.currentPosition;
    }

    @Override // android.media.MediaParser.InputReader
    public int read(byte[] r6, int r7, int r8) throws IOException {
        int r0 = ((DataReader) Util.castNonNull(this.dataReader)).read(r6, r7, r8);
        this.currentPosition += (long) r0;
        return r0;
    }

    @Override // android.media.MediaParser.SeekableInputReader
    public void seekToPosition(long r1) {
        this.lastSeekPosition = r1;
    }

    public void setCurrentPosition(long r1) {
        this.currentPosition = r1;
    }

    public void setDataReader(DataReader r3, long r4) {
        this.dataReader = r3;
        this.resourceLength = r4;
        this.lastSeekPosition = -1;
    }
}
