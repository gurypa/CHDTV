package androidx.media3.exoplayer.image;

import android.graphics.Bitmap;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.BitmapUtil;
import androidx.media3.decoder.DecoderException;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.decoder.DecoderOutputBuffer;
import androidx.media3.decoder.SimpleDecoder;
import androidx.media3.exoplayer.RendererCapabilities;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public final class BitmapFactoryImageDecoder extends SimpleDecoder<DecoderInputBuffer, ImageOutputBuffer, ImageDecoderException> implements ImageDecoder {
    private final BitmapDecoder bitmapDecoder;

    public interface BitmapDecoder {
        Bitmap decode(byte[] r1, int r2) throws ImageDecoderException;
    }

    public static final class Factory implements ImageDecoder.Factory {
        private final BitmapDecoder bitmapDecoder;

        public Factory() {
            this.bitmapDecoder = new BitmapFactoryImageDecoder$Factory$$ExternalSyntheticLambda0();
        }

        public Factory(BitmapDecoder r1) {
            this.bitmapDecoder = r1;
        }

        static /* synthetic */ Bitmap lambda$new$0(byte[] r1, int r2) throws ImageDecoderException {
            return BitmapFactoryImageDecoder.access$100(r1, r2);
        }

        @Override // androidx.media3.exoplayer.image.ImageDecoder.Factory
        public BitmapFactoryImageDecoder createImageDecoder() {
            return new BitmapFactoryImageDecoder(this.bitmapDecoder, null);
        }

        @Override // androidx.media3.exoplayer.image.ImageDecoder.Factory
        public /* bridge */ /* synthetic */ ImageDecoder createImageDecoder() {
            return createImageDecoder();
        }

        @Override // androidx.media3.exoplayer.image.ImageDecoder.Factory
        public int supportsFormat(Format r2) {
            if (r2.sampleMimeType == null) goto L13;
            if (MimeTypes.isImage(r2.sampleMimeType) == false) goto L13;
            if (Util.isBitmapFactorySupportedMimeType(r2.sampleMimeType) == false) goto L11;
            return RendererCapabilities.CC.create(4);
        L11:
            return RendererCapabilities.CC.create(1);
        L13:
            return RendererCapabilities.CC.create(0);
        }
    }

    private BitmapFactoryImageDecoder(BitmapDecoder r3) {
        super(new DecoderInputBuffer[1], new ImageOutputBuffer[1]);
        this.bitmapDecoder = r3;
    }

    /* synthetic */ BitmapFactoryImageDecoder(BitmapDecoder r1, C03011 r2) {
        this(r1);
    }

    static /* synthetic */ Bitmap access$100(byte[] r1, int r2) throws ImageDecoderException {
        return decode(r1, r2);
    }

    static /* synthetic */ void access$200(BitmapFactoryImageDecoder r0, DecoderOutputBuffer r1) {
        r0.releaseOutputBuffer(r1);
    }

    private static Bitmap decode(byte[] r4, int r5) throws ImageDecoderException {
        return BitmapUtil.decode(r4, r5, null, -1);
    L8:
        e = move-exception;
        throw new ImageDecoderException("Could not decode image data with BitmapFactory. (data.length = " + r4.length + ", input length = " + r5 + ")", e);
    L5:
        e = move-exception;
        throw new ImageDecoderException(e);
    }

    @Override // androidx.media3.decoder.SimpleDecoder
    protected DecoderInputBuffer createInputBuffer() {
        return new DecoderInputBuffer(1);
    }

    @Override // androidx.media3.decoder.SimpleDecoder
    protected /* bridge */ /* synthetic */ DecoderOutputBuffer createOutputBuffer() {
        return createOutputBuffer();
    }

    @Override // androidx.media3.decoder.SimpleDecoder
    protected ImageOutputBuffer createOutputBuffer() {
        return new C03011(this);
    }

    @Override // androidx.media3.decoder.SimpleDecoder
    protected /* bridge */ /* synthetic */ DecoderException createUnexpectedDecodeException(Throwable r1) {
        return createUnexpectedDecodeException(r1);
    }

    @Override // androidx.media3.decoder.SimpleDecoder
    protected ImageDecoderException createUnexpectedDecodeException(Throwable r3) {
        return new ImageDecoderException("Unexpected decode error", r3);
    }

    @Override // androidx.media3.decoder.SimpleDecoder
    protected /* bridge */ /* synthetic */ DecoderException decode(DecoderInputBuffer r1, DecoderOutputBuffer r2, boolean r3) {
        return decode(r1, (ImageOutputBuffer) r2, r3);
    }

    protected ImageDecoderException decode(DecoderInputBuffer r5, ImageOutputBuffer r6, boolean r7) {
        ByteBuffer r0 = (ByteBuffer) Assertions.checkNotNull(r5.data);     // Catch: ImageDecoderException -> L9
        Assertions.checkState(r0.hasArray());     // Catch: ImageDecoderException -> L9
        if (r0.arrayOffset() != 0) goto L5;
        boolean r1 = true;
    L6:
        Assertions.checkArgument(r1);     // Catch: ImageDecoderException -> L9
        r6.bitmap = this.bitmapDecoder.decode(r0.array(), r0.remaining());     // Catch: ImageDecoderException -> L9
        r6.timeUs = r5.timeUs;     // Catch: ImageDecoderException -> L9
        return null;
    L5:
        r1 = false;
    L9:
        e = move-exception;
        return e;
    }

    @Override // androidx.media3.decoder.SimpleDecoder, androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ ImageOutputBuffer dequeueOutputBuffer() throws ImageDecoderException {
        return (ImageOutputBuffer) super.dequeueOutputBuffer();
    }

    @Override // androidx.media3.decoder.Decoder
    public String getName() {
        return "BitmapFactoryImageDecoder";
    }
}
