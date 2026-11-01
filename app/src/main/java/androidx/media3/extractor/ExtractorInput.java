package androidx.media3.extractor;

import androidx.media3.common.DataReader;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public interface ExtractorInput extends DataReader {
    void advancePeekPosition(int r1) throws IOException;

    boolean advancePeekPosition(int r1, boolean r2) throws IOException;

    long getLength();

    long getPeekPosition();

    long getPosition();

    int peek(byte[] r1, int r2, int r3) throws IOException;

    void peekFully(byte[] r1, int r2, int r3) throws IOException;

    boolean peekFully(byte[] r1, int r2, int r3, boolean r4) throws IOException;

    @Override // androidx.media3.common.DataReader
    int read(byte[] r1, int r2, int r3) throws IOException;

    void readFully(byte[] r1, int r2, int r3) throws IOException;

    boolean readFully(byte[] r1, int r2, int r3, boolean r4) throws IOException;

    void resetPeekPosition();

    <E extends Throwable> void setRetryPosition(long r1, E r3) throws Throwable;

    int skip(int r1) throws IOException;

    void skipFully(int r1) throws IOException;

    boolean skipFully(int r1, boolean r2) throws IOException;
}
