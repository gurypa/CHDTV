package androidx.media3.extractor.jpeg;

import androidx.media3.common.util.Assertions;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ForwardingExtractorInput;

/* JADX INFO: loaded from: classes.dex */
final class StartOffsetExtractorInput extends ForwardingExtractorInput {
    private final long startOffset;

    public StartOffsetExtractorInput(ExtractorInput r3, long r4) {
        super(r3);
        if (r3.getPosition() < r4) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        this.startOffset = r4;
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.extractor.ForwardingExtractorInput, androidx.media3.extractor.ExtractorInput
    public long getLength() {
        return super.getLength() - this.startOffset;
    }

    @Override // androidx.media3.extractor.ForwardingExtractorInput, androidx.media3.extractor.ExtractorInput
    public long getPeekPosition() {
        return super.getPeekPosition() - this.startOffset;
    }

    @Override // androidx.media3.extractor.ForwardingExtractorInput, androidx.media3.extractor.ExtractorInput
    public long getPosition() {
        return super.getPosition() - this.startOffset;
    }

    @Override // androidx.media3.extractor.ForwardingExtractorInput, androidx.media3.extractor.ExtractorInput
    public <E extends Throwable> void setRetryPosition(long r3, E r5) throws Throwable {
        super.setRetryPosition(this.startOffset + r3, r5);
    }
}
