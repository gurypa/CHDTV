package androidx.media3.extractor;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class ForwardingExtractorInput implements ExtractorInput {
    private final ExtractorInput input;

    public ForwardingExtractorInput(ExtractorInput r1) {
        this.input = r1;
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public void advancePeekPosition(int r2) throws IOException {
        this.input.advancePeekPosition(r2);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public boolean advancePeekPosition(int r2, boolean r3) throws IOException {
        return this.input.advancePeekPosition(r2, r3);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public long getLength() {
        return this.input.getLength();
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public long getPeekPosition() {
        return this.input.getPeekPosition();
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public long getPosition() {
        return this.input.getPosition();
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public int peek(byte[] r2, int r3, int r4) throws IOException {
        return this.input.peek(r2, r3, r4);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public void peekFully(byte[] r2, int r3, int r4) throws IOException {
        this.input.peekFully(r2, r3, r4);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public boolean peekFully(byte[] r2, int r3, int r4, boolean r5) throws IOException {
        return this.input.peekFully(r2, r3, r4, r5);
    }

    @Override // androidx.media3.extractor.ExtractorInput, androidx.media3.common.DataReader
    public int read(byte[] r2, int r3, int r4) throws IOException {
        return this.input.read(r2, r3, r4);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public void readFully(byte[] r2, int r3, int r4) throws IOException {
        this.input.readFully(r2, r3, r4);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public boolean readFully(byte[] r2, int r3, int r4, boolean r5) throws IOException {
        return this.input.readFully(r2, r3, r4, r5);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public void resetPeekPosition() {
        this.input.resetPeekPosition();
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public <E extends Throwable> void setRetryPosition(long r2, E r4) throws Throwable {
        this.input.setRetryPosition(r2, r4);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public int skip(int r2) throws IOException {
        return this.input.skip(r2);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public void skipFully(int r2) throws IOException {
        this.input.skipFully(r2);
    }

    @Override // androidx.media3.extractor.ExtractorInput
    public boolean skipFully(int r2, boolean r3) throws IOException {
        return this.input.skipFully(r2, r3);
    }
}
