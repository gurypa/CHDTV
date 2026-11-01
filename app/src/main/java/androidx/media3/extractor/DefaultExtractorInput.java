package androidx.media3.extractor;

import androidx.media3.common.DataReader;
import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultExtractorInput implements ExtractorInput {
    private static final int PEEK_MAX_FREE_SPACE = 524288;
    private static final int PEEK_MIN_FREE_SPACE_AFTER_RESIZE = 65536;
    private static final int SCRATCH_SPACE_SIZE = 4096;
    private final DataReader dataReader;
    private byte[] peekBuffer;
    private int peekBufferLength;
    private int peekBufferPosition;
    private long position;
    private final byte[] scratchSpace;
    private final long streamLength;

    static {
        MediaLibraryInfo.registerModule("media3.extractor");
    }

    public DefaultExtractorInput(DataReader r2, long r3, long r5) {
        this.dataReader = r2;
        this.position = r3;
        this.streamLength = r5;
        this.peekBuffer = new byte[65536];
        this.scratchSpace = new byte[4096];
    }

    private void commitBytesRead(int r5) {
        if (r5 == (-1)) goto L6;
        this.position += (long) r5;
        return;
    }

    private void ensureSpaceForPeek(int r5) {
        int r0 = this.peekBufferPosition + r5;
        if (r0 <= this.peekBuffer.length) goto L6;
        int r1 = Util.constrainValue(this.peekBuffer.length * 2, 65536 + r0, 524288 + r0);
        this.peekBuffer = Arrays.copyOf(this.peekBuffer, r1);
        return;
    }

    private int readFromPeekBuffer(byte[] r4, int r5, int r6) {
        if (this.peekBufferLength != 0) goto L5;
        return 0;
    L5:
        int r0 = Math.min(this.peekBufferLength, r6);
        System.arraycopy(this.peekBuffer, 0, r4, r5, r0);
        updatePeekBuffer(r0);
        return r0;
    }

    private int readFromUpstream(byte[] r4, int r5, int r6, int r7, boolean r8) throws IOException {
        if (Thread.interrupted() == true) goto L14;
        int r0 = this.dataReader.read(r4, r5 + r7, r6 - r7);
        if (r0 != (-1)) goto L12;
        if (r7 != 0) goto L10;
        if (r8 == false) goto L10;
        return -1;
    L10:
        throw new EOFException();
    L12:
        return r7 + r0;
    L14:
        throw new InterruptedIOException();
    }

    private int skipFromPeekBuffer(int r2) {
        int r0 = Math.min(this.peekBufferLength, r2);
        updatePeekBuffer(r0);
        return r0;
    }

    private void updatePeekBuffer(int r6) {
        this.peekBufferLength -= r6;
        this.peekBufferPosition = 0;
        byte[] r1 = this.peekBuffer;
        if (this.peekBufferLength >= (this.peekBuffer.length - 524288)) goto L5;
        r1 = new byte[this.peekBufferLength + 65536];
    L5:
        System.arraycopy(this.peekBuffer, r6, r1, 0, this.peekBufferLength);
        this.peekBuffer = r1;
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public void advancePeekPosition(int r2) throws IOException {
        advancePeekPosition(r2, false);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public boolean advancePeekPosition(int r8, boolean r9) throws IOException {
        ensureSpaceForPeek(r8);
        int r5 = this.peekBufferLength - this.peekBufferPosition;
    L3:
        if (r5 >= r8) goto L9;
        int r4 = r8;
        boolean r6 = r9;
        r5 = readFromUpstream(this.peekBuffer, this.peekBufferPosition, r4, r5, r6);
        if (r5 == (-1)) goto L6;
        this.peekBufferLength = this.peekBufferPosition + r5;
        r8 = r4;
        r9 = r6;
        goto L3
    L6:
        return false;
    L9:
        this.peekBufferPosition += r8;
        return true;
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public long getLength() {
        return this.streamLength;
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public long getPeekPosition() {
        return this.position + ((long) this.peekBufferPosition);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public long getPosition() {
        return this.position;
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public int peek(byte[] r8, int r9, int r10) throws IOException {
        ensureSpaceForPeek(r10);
        int r0 = this.peekBufferLength - this.peekBufferPosition;
        if (r0 != 0) goto L8;
        DefaultExtractorInput r1 = this;
        int r11 = r1.readFromUpstream(this.peekBuffer, this.peekBufferPosition, r10, 0, true);
        if (r11 != (-1)) goto L7;
        return -1;
    L7:
        r1.peekBufferLength += r11;
    L9:
        System.arraycopy(r1.peekBuffer, r1.peekBufferPosition, r8, r9, r11);
        r1.peekBufferPosition += r11;
        return r11;
    L8:
        r1 = this;
        r11 = Math.min(r10, r0);
        goto L9
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public void peekFully(byte[] r2, int r3, int r4) throws IOException {
        peekFully(r2, r3, r4, false);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public boolean peekFully(byte[] r3, int r4, int r5, boolean r6) throws IOException {
        if (advancePeekPosition(r5, r6) == true) goto L6;
        return false;
    L6:
        System.arraycopy(this.peekBuffer, this.peekBufferPosition - r5, r3, r4, r5);
        return true;
    }

    @Override // androidx.media3.extractor.ExtractorInput, androidx.media3.common.DataReader
    public int read(byte[] r8, int r9, int r10) throws IOException {
        int r0 = readFromPeekBuffer(r8, r9, r10);
        if (r0 != 0) goto L6;
        r0 = readFromUpstream(r8, r9, r10, 0, true);
    L6:
        commitBytesRead(r0);
        return r0;
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public void readFully(byte[] r2, int r3, int r4) throws IOException {
        readFully(r2, r3, r4, false);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public boolean readFully(byte[] r8, int r9, int r10, boolean r11) throws IOException {
        int r5 = readFromPeekBuffer(r8, r9, r10);
    L4:
        if (r5 >= r10) goto L7;
        if (r5 == (-1)) goto L7;
        r5 = readFromUpstream(r8, r9, r10, r5, r11);
    L7:
        commitBytesRead(r5);
        if (r5 == (-1)) goto L10;
        return true;
    L10:
        return false;
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public void resetPeekPosition() {
        this.peekBufferPosition = 0;
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public <E extends Throwable> void setRetryPosition(long r3, E r5) throws Throwable {
        if (r3 < 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        this.position = r3;
        throw r5;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public int skip(int r8) throws IOException {
        int r0 = skipFromPeekBuffer(r8);
        if (r0 != 0) goto L6;
        r0 = readFromUpstream(this.scratchSpace, 0, Math.min(r8, this.scratchSpace.length), 0, true);
    L6:
        commitBytesRead(r0);
        return r0;
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public void skipFully(int r2) throws IOException {
        skipFully(r2, false);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public boolean skipFully(int r8, boolean r9) throws IOException {
        int r5 = skipFromPeekBuffer(r8);
    L4:
        if (r5 >= r8) goto L7;
        if (r5 == (-1)) goto L7;
        int r4 = Math.min(r8, this.scratchSpace.length + r5);
        r5 = readFromUpstream(this.scratchSpace, -r5, r4, r5, r9);
    L7:
        commitBytesRead(r5);
        if (r5 == (-1)) goto L10;
        return true;
    L10:
        return false;
    }
}
