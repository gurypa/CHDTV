package androidx.media3.extractor.text;

import androidx.media3.common.util.Assertions;
import androidx.media3.decoder.DecoderException;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.decoder.DecoderOutputBuffer;
import androidx.media3.decoder.SimpleDecoder;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public abstract class SimpleSubtitleDecoder extends SimpleDecoder<SubtitleInputBuffer, SubtitleOutputBuffer, SubtitleDecoderException> implements SubtitleDecoder {
    private final String name;

    protected SimpleSubtitleDecoder(String r3) {
        super(new SubtitleInputBuffer[2], new SubtitleOutputBuffer[2]);
        this.name = r3;
        setInitialInputBufferSize(1024);
    }

    static /* synthetic */ void access$000(SimpleSubtitleDecoder r0, DecoderOutputBuffer r1) {
        r0.releaseOutputBuffer(r1);
    }

    @Override // androidx.media3.decoder.SimpleDecoder
    protected /* bridge */ /* synthetic */ DecoderInputBuffer createInputBuffer() {
        return createInputBuffer();
    }

    @Override // androidx.media3.decoder.SimpleDecoder
    protected final SubtitleInputBuffer createInputBuffer() {
        return new SubtitleInputBuffer();
    }

    @Override // androidx.media3.decoder.SimpleDecoder
    protected /* bridge */ /* synthetic */ DecoderOutputBuffer createOutputBuffer() {
        return createOutputBuffer();
    }

    @Override // androidx.media3.decoder.SimpleDecoder
    protected final SubtitleOutputBuffer createOutputBuffer() {
        return new C04421(this);
    }

    @Override // androidx.media3.decoder.SimpleDecoder
    protected /* bridge */ /* synthetic */ DecoderException createUnexpectedDecodeException(Throwable r1) {
        return createUnexpectedDecodeException(r1);
    }

    @Override // androidx.media3.decoder.SimpleDecoder
    protected final SubtitleDecoderException createUnexpectedDecodeException(Throwable r3) {
        return new SubtitleDecoderException("Unexpected decode error", r3);
    }

    @Override // androidx.media3.decoder.SimpleDecoder
    protected /* bridge */ /* synthetic */ DecoderException decode(DecoderInputBuffer r1, DecoderOutputBuffer r2, boolean r3) {
        return decode((SubtitleInputBuffer) r1, (SubtitleOutputBuffer) r2, r3);
    }

    protected abstract Subtitle decode(byte[] r1, int r2, boolean r3) throws SubtitleDecoderException;

    protected final SubtitleDecoderException decode(SubtitleInputBuffer r9, SubtitleOutputBuffer r10, boolean r11) {
        ByteBuffer r0 = (ByteBuffer) Assertions.checkNotNull(r9.data);     // Catch: SubtitleDecoderException -> L9
        Subtitle r1 = decode(r0.array(), r0.limit(), r11);     // Catch: SubtitleDecoderException -> L9
        r10.setContent(r9.timeUs, r1, r9.subsampleOffsetUs);     // Catch: SubtitleDecoderException -> L7
        r10.shouldBeSkipped = false;     // Catch: SubtitleDecoderException -> L7
        return null;
    L7:
        e = move-exception;
        return e;
    L9:
        e = move-exception;
        return e;
    }

    @Override // androidx.media3.decoder.Decoder
    public final String getName() {
        return this.name;
    }

    @Override // androidx.media3.extractor.text.SubtitleDecoder
    public void setPositionUs(long r1) {
    }
}
