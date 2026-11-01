package androidx.media3.exoplayer.image;

import android.graphics.Bitmap;
import android.net.Uri;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.decoder.DecoderException;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.RendererCapabilities;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/* JADX INFO: loaded from: classes.dex */
public final class ExternallyLoadedImageDecoder implements ImageDecoder {
    private final BitmapResolver bitmapResolver;
    private final DecoderInputBuffer inputBuffer;
    private final ImageOutputBuffer outputBuffer;
    private ListenableFuture<Bitmap> pendingDecode;
    private long pendingDecodeTimeUs;
    private boolean pendingEndOfStream;

    public interface BitmapResolver {
        ListenableFuture<Bitmap> resolve(ExternalImageRequest r1);
    }

    public static final class ExternalImageRequest {
        public final Uri uri;

        public ExternalImageRequest(Uri r1) {
            this.uri = r1;
        }
    }

    public static final class Factory implements ImageDecoder.Factory {
        private final BitmapResolver bitmapResolver;

        public Factory(BitmapResolver r1) {
            this.bitmapResolver = r1;
        }

        @Override // androidx.media3.exoplayer.image.ImageDecoder.Factory
        public ExternallyLoadedImageDecoder createImageDecoder() {
            return new ExternallyLoadedImageDecoder(this.bitmapResolver, null);
        }

        @Override // androidx.media3.exoplayer.image.ImageDecoder.Factory
        public /* bridge */ /* synthetic */ ImageDecoder createImageDecoder() {
            return createImageDecoder();
        }

        @Override // androidx.media3.exoplayer.image.ImageDecoder.Factory
        public int supportsFormat(Format r3) {
            if (Objects.equals(r3.sampleMimeType, MimeTypes.APPLICATION_EXTERNALLY_LOADED_IMAGE) == false) goto L6;
            int r1 = 4;
        L10:
            return RendererCapabilities.CC.create(r1);
        L6:
            if (MimeTypes.isImage(r3.sampleMimeType) == false) goto L8;
            r1 = 1;
            goto L10
        L8:
            r1 = 0;
            goto L10
        }
    }

    private ExternallyLoadedImageDecoder(BitmapResolver r3) {
        this.bitmapResolver = r3;
        this.inputBuffer = new DecoderInputBuffer(1);
        this.outputBuffer = new C03021(this);
    }

    /* synthetic */ ExternallyLoadedImageDecoder(BitmapResolver r1, C03021 r2) {
        this(r1);
    }

    private void resetState() {
        if (this.pendingDecode == null) goto L5;
        this.pendingDecode.cancel(false);
        this.pendingDecode = null;
    L5:
        this.pendingEndOfStream = false;
        this.outputBuffer.release();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.media3.decoder.Decoder
    public DecoderInputBuffer dequeueInputBuffer() {
        if (this.pendingDecode == null) goto L7;
        return null;
    L7:
        return this.inputBuffer;
    }

    @Override // androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ DecoderInputBuffer dequeueInputBuffer() throws DecoderException {
        return dequeueInputBuffer();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.media3.exoplayer.image.ImageDecoder, androidx.media3.decoder.Decoder
    public ImageOutputBuffer dequeueOutputBuffer() throws ImageDecoderException {
        if (this.pendingEndOfStream == false) goto L7;
        this.outputBuffer.addFlag(4);
        this.pendingEndOfStream = false;
        return this.outputBuffer;
    L7:
        if (this.pendingDecode != null) goto L25;
    L24:
        return null;
    L25:
        if (this.pendingDecode.isDone() == false) goto L24;
        ImageOutputBuffer r0 = this.outputBuffer;     // Catch: Throwable -> L14 CancellationException -> L16 ExecutionException -> L19
        r0.bitmap = (Bitmap) Futures.getDone(this.pendingDecode);     // Catch: Throwable -> L14 CancellationException -> L16 ExecutionException -> L19
        ImageOutputBuffer r1 = this.outputBuffer;     // Catch: Throwable -> L14 CancellationException -> L16 ExecutionException -> L19
        r1.timeUs = this.pendingDecodeTimeUs;     // Catch: Throwable -> L14 CancellationException -> L16 ExecutionException -> L19
        ImageOutputBuffer r2 = this.outputBuffer;     // Catch: Throwable -> L14 CancellationException -> L16 ExecutionException -> L19
        this.pendingDecode = null;
        return r2;
    L16:
        e = move-exception;
        throw new ImageDecoderException(e);     // Catch: Throwable -> L14
    L19:
        e = move-exception;
        throw new ImageDecoderException(e.getCause());     // Catch: Throwable -> L14
    L14:
        th = move-exception;
        this.pendingDecode = null;
        throw th;
    }

    @Override // androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ ImageOutputBuffer dequeueOutputBuffer() throws DecoderException {
        return dequeueOutputBuffer();
    }

    @Override // androidx.media3.decoder.Decoder
    public void flush() {
        resetState();
    }

    @Override // androidx.media3.decoder.Decoder
    public String getName() {
        return "externallyLoadedImageDecoder";
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.media3.exoplayer.image.ImageDecoder
    public void queueInputBuffer(DecoderInputBuffer r7) {
        if (r7.isEndOfStream() == false) goto L6;
        this.pendingEndOfStream = true;
        r7.clear();
        return;
    L6:
        ByteBuffer r0 = (ByteBuffer) Assertions.checkNotNull(r7.data);
        Assertions.checkState(r0.hasArray());
        Uri r1 = Uri.parse(new String(r0.array(), r0.arrayOffset(), r0.remaining(), StandardCharsets.UTF_8));
        this.pendingDecode = this.bitmapResolver.resolve(new ExternalImageRequest(r1));
        this.pendingDecodeTimeUs = r7.timeUs;
        r7.clear();
    }

    @Override // androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ void queueInputBuffer(DecoderInputBuffer r1) throws DecoderException {
        queueInputBuffer(r1);
    }

    @Override // androidx.media3.decoder.Decoder
    public void release() {
        resetState();
    }

    @Override // androidx.media3.decoder.Decoder
    public void setOutputStartTimeUs(long r1) {
    }
}
