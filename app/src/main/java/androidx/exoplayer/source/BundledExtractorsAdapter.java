package androidx.media3.exoplayer.source;

import android.net.Uri;
import androidx.media3.common.DataReader;
import androidx.media3.common.util.Assertions;
import androidx.media3.extractor.DefaultExtractorInput;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.mp3.Mp3Extractor;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class BundledExtractorsAdapter implements ProgressiveMediaExtractor {
    private Extractor extractor;
    private ExtractorInput extractorInput;
    private final ExtractorsFactory extractorsFactory;

    public BundledExtractorsAdapter(ExtractorsFactory r1) {
        this.extractorsFactory = r1;
    }

    static /* synthetic */ String lambda$init$0(Extractor r1) {
        return r1.getUnderlyingImplementation().getClass().getSimpleName();
    }

    @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor
    public void disableSeekingOnMp3Streams() {
        if (this.extractor != null) goto L5;
        return;
    L5:
        Extractor r0 = this.extractor.getUnderlyingImplementation();
        if ((r0 instanceof Mp3Extractor) == false) goto L9;
        ((Mp3Extractor) r0).disableSeeking();
        return;
    }

    @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor
    public long getCurrentInputPosition() {
        if (this.extractorInput != null) goto L7;
        return -1;
    L7:
        return this.extractorInput.getPosition();
    }

    @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor
    public void init(DataReader r13, Uri r14, Map<String, List<String>> r15, long r16, long r18, ExtractorOutput r20) throws IOException {
        DefaultExtractorInput r0 = new DefaultExtractorInput(r13, r16, r18);
        this.extractorInput = r0;
        if (this.extractor == null) goto L5;
        return;
    L5:
        Extractor[] r3 = this.extractorsFactory.createExtractors(r14, r15);
        ImmutableList.Builder r4 = ImmutableList.builderWithExpectedSize(r3.length);
        boolean r5 = false;
        if (r3.length != 1) goto L8;
        this.extractor = r3[0];
    L45:
        this.extractor.init(r20);
        return;
    L8:
        int r7 = r3.length;
        int r8 = 0;
    L9:
        if (r8 >= r7) goto L44;
        Extractor r9 = r3[r8];
    L26:
        th = move-exception;
        if (this.extractor == null) goto L30;
    L31:
        r5 = true;
    L32:
        Assertions.checkState(r5);
        r0.resetPeekPosition();
        throw th;
    L30:
        if (r0.getPosition() != r16) goto L32;
    L12:
        if (r9.sniff(r0) == false) goto L20;
        this.extractor = r9;     // Catch: Throwable -> L26 EOFException -> L34
        goto L36
    L15:
        if (this.extractor == null) goto L17;
    L18:
        r5 = true;
    L19:
        Assertions.checkState(r5);
        r0.resetPeekPosition();
        goto L44
    L17:
        if (r0.getPosition() != r16) goto L19;
    L20:
        r4.addAll(r9.getSniffFailureDetails());     // Catch: Throwable -> L26 EOFException -> L34
        if (this.extractor == null) goto L24;
    L41:
        boolean r1 = true;
    L42:
        Assertions.checkState(r1);
        r0.resetPeekPosition();
        r8 = r8 + 1;
        goto L9
    L24:
        if (r0.getPosition() == r16) goto L41;
    L40:
        r1 = false;
    L36:
        if (this.extractor != null) goto L41;
        if (r0.getPosition() != r16) goto L40;
    L44:
        if (this.extractor != null) goto L45;
        throw new UnrecognizedInputFormatException("None of the available extractors (" + Joiner.m184on(", ").join(Lists.transform(ImmutableList.copyOf(r3), new BundledExtractorsAdapter$$ExternalSyntheticLambda0())) + ") could read the stream.", (Uri) Assertions.checkNotNull(r14), r4.build());
    }

    @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor
    public int read(PositionHolder r3) throws IOException {
        return ((Extractor) Assertions.checkNotNull(this.extractor)).read((ExtractorInput) Assertions.checkNotNull(this.extractorInput), r3);
    }

    @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor
    public void release() {
        if (this.extractor == null) goto L5;
        this.extractor.release();
        this.extractor = null;
    L5:
        this.extractorInput = null;
    }

    @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor
    public void seek(long r2, long r4) {
        ((Extractor) Assertions.checkNotNull(this.extractor)).seek(r2, r4);
    }
}
