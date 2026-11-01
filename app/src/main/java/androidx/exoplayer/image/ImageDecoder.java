package androidx.media3.exoplayer.image;

import androidx.media3.common.Format;
import androidx.media3.decoder.Decoder;
import androidx.media3.decoder.DecoderException;
import androidx.media3.decoder.DecoderInputBuffer;

/* JADX INFO: loaded from: classes.dex */
public interface ImageDecoder extends Decoder<DecoderInputBuffer, ImageOutputBuffer, ImageDecoderException> {

    /* JADX INFO: renamed from: androidx.media3.exoplayer.image.ImageDecoder$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ Object $default$dequeueOutputBuffer(ImageDecoder r1) throws DecoderException {
            return r1.dequeueOutputBuffer();
        }

        public static /* bridge */ /* synthetic */ void $default$queueInputBuffer(ImageDecoder r0, Object r1) throws DecoderException {
            r0.queueInputBuffer((DecoderInputBuffer) r1);
        }
    }

    public interface Factory {
        public static final Factory DEFAULT = null;

        static {
            DEFAULT = new BitmapFactoryImageDecoder.Factory();
        }

        ImageDecoder createImageDecoder();

        int supportsFormat(Format r1);
    }

    @Override // androidx.media3.decoder.Decoder
    ImageOutputBuffer dequeueOutputBuffer() throws ImageDecoderException;

    void queueInputBuffer(DecoderInputBuffer r1) throws ImageDecoderException;
}
