package androidx.media3.exoplayer.image;

import android.graphics.Bitmap;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.TraceUtil;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.BaseRenderer;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.exoplayer.source.MediaSource;
import java.util.ArrayDeque;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public class ImageRenderer extends BaseRenderer {
    private static final long IMAGE_PRESENTATION_WINDOW_THRESHOLD_US = 30000;
    private static final int REINITIALIZATION_STATE_NONE = 0;
    private static final int REINITIALIZATION_STATE_SIGNAL_END_OF_STREAM_THEN_WAIT = 2;
    private static final int REINITIALIZATION_STATE_WAIT_END_OF_STREAM = 3;
    private static final String TAG = "ImageRenderer";
    private int currentTileIndex;
    private ImageDecoder decoder;
    private final ImageDecoder.Factory decoderFactory;
    private int decoderReinitializationState;
    private int firstFrameState;
    private final DecoderInputBuffer flagsOnlyBuffer;
    private ImageOutput imageOutput;
    private DecoderInputBuffer inputBuffer;
    private Format inputFormat;
    private boolean inputStreamEnded;
    private long largestQueuedPresentationTimeUs;
    private long lastProcessedOutputBufferTimeUs;
    private TileInfo nextTileInfo;
    private Bitmap outputBitmap;
    private boolean outputStreamEnded;
    private OutputStreamInfo outputStreamInfo;
    private final ArrayDeque<OutputStreamInfo> pendingOutputStreamChanges;
    private boolean readyToOutputTiles;
    private TileInfo tileInfo;

    private static final class OutputStreamInfo {
        public static final OutputStreamInfo UNSET = null;
        public final long previousStreamLastBufferTimeUs;
        public final long streamOffsetUs;

        static {
            UNSET = new OutputStreamInfo(C0180C.TIME_UNSET, C0180C.TIME_UNSET);
        }

        public OutputStreamInfo(long r1, long r3) {
            this.previousStreamLastBufferTimeUs = r1;
            this.streamOffsetUs = r3;
        }
    }

    private static class TileInfo {
        private final long presentationTimeUs;
        private Bitmap tileBitmap;
        private final int tileIndex;

        public TileInfo(int r1, long r2) {
            this.tileIndex = r1;
            this.presentationTimeUs = r2;
        }

        public long getPresentationTimeUs() {
            return this.presentationTimeUs;
        }

        public Bitmap getTileBitmap() {
            return this.tileBitmap;
        }

        public int getTileIndex() {
            return this.tileIndex;
        }

        public boolean hasTileBitmap() {
            if (this.tileBitmap == null) goto L5;
            return true;
        L5:
            return false;
        }

        public void setTileBitmap(Bitmap r1) {
            this.tileBitmap = r1;
        }
    }

    public ImageRenderer(ImageDecoder.Factory r3, ImageOutput r4) {
        super(4);
        this.decoderFactory = r3;
        this.imageOutput = getImageOutput(r4);
        this.flagsOnlyBuffer = DecoderInputBuffer.newNoDataInstance();
        this.outputStreamInfo = OutputStreamInfo.UNSET;
        this.pendingOutputStreamChanges = new ArrayDeque();
        this.largestQueuedPresentationTimeUs = C0180C.TIME_UNSET;
        this.lastProcessedOutputBufferTimeUs = C0180C.TIME_UNSET;
        this.decoderReinitializationState = 0;
        this.firstFrameState = 1;
    }

    private boolean canCreateDecoderForFormat(Format r3) {
        int r0 = this.decoderFactory.supportsFormat(r3);
        if (r0 != RendererCapabilities.CC.create(4)) goto L5;
    L8:
        return true;
    L5:
        if (r0 == RendererCapabilities.CC.create(3)) goto L8;
        return false;
    }

    private Bitmap cropTileFromImageGrid(int r6) {
        Assertions.checkStateNotNull(this.outputBitmap);
        int r0 = this.outputBitmap.getWidth() / ((Format) Assertions.checkStateNotNull(this.inputFormat)).tileCountHorizontal;
        int r1 = this.outputBitmap.getHeight() / ((Format) Assertions.checkStateNotNull(this.inputFormat)).tileCountVertical;
        int r2 = (r6 % this.inputFormat.tileCountHorizontal) * r0;
        int r3 = (r6 / this.inputFormat.tileCountHorizontal) * r1;
        return Bitmap.createBitmap(this.outputBitmap, r2, r3, r0, r1);
    }

    private boolean drainOutput(long r13, long r15) throws ImageDecoderException, ExoPlaybackException {
        if (this.outputBitmap == null) goto L8;
        if (this.tileInfo != null) goto L8;
        return false;
    L8:
        if (this.firstFrameState != 0) goto L13;
        if (getState() == 2) goto L13;
        return false;
    L13:
        if (this.outputBitmap != null) goto L28;
        Assertions.checkStateNotNull(this.decoder);
        ImageOutputBuffer r1 = this.decoder.dequeueOutputBuffer();
        if (r1 != null) goto L18;
        return false;
    L18:
        if (((ImageOutputBuffer) Assertions.checkStateNotNull(r1)).isEndOfStream() == true) goto L20;
        Assertions.checkStateNotNull(r1.bitmap, "Non-EOS buffer came back from the decoder without bitmap.");
        this.outputBitmap = r1.bitmap;
        ((ImageOutputBuffer) Assertions.checkStateNotNull(r1)).release();
        goto L28
    L20:
        if (this.decoderReinitializationState != 3) goto L22;
        releaseDecoderResources();
        Assertions.checkStateNotNull(this.inputFormat);
        initDecoder();
    L25:
        return false;
    L22:
        ((ImageOutputBuffer) Assertions.checkStateNotNull(r1)).release();
        if (this.pendingOutputStreamChanges.isEmpty() == false) goto L25;
        this.outputStreamEnded = true;
    L28:
        if (this.readyToOutputTiles == true) goto L30;
    L60:
        return false;
    L30:
        if (this.outputBitmap == null) goto L60;
        if (this.tileInfo == null) goto L60;
        Assertions.checkStateNotNull(this.inputFormat);
        if (this.inputFormat.tileCountHorizontal != 1) goto L38;
        if (this.inputFormat.tileCountVertical != 1) goto L38;
    L42:
        boolean r2 = false;
    L43:
        boolean r11 = r2;
        if (this.tileInfo.hasTileBitmap() == true) goto L51;
        TileInfo r3 = this.tileInfo;
        if (r11 == false) goto L48;
        Bitmap r4 = cropTileFromImageGrid(this.tileInfo.getTileIndex());
    L49:
        r3.setTileBitmap(r4);
        goto L51
    L48:
        r4 = (Bitmap) Assertions.checkStateNotNull(this.outputBitmap);
    L51:
        if (processOutputBuffer(r13, r15, (Bitmap) Assertions.checkStateNotNull(this.tileInfo.getTileBitmap()), this.tileInfo.getPresentationTimeUs()) == true) goto L53;
        return false;
    L53:
        onProcessedOutputBuffer(((TileInfo) Assertions.checkStateNotNull(this.tileInfo)).getPresentationTimeUs());
        this.firstFrameState = 3;
        if (r11 == true) goto L56;
    L57:
        this.outputBitmap = null;
    L58:
        this.tileInfo = this.nextTileInfo;
        this.nextTileInfo = null;
        return true;
    L56:
        if (((TileInfo) Assertions.checkStateNotNull(this.tileInfo)).getTileIndex() != ((((Format) Assertions.checkStateNotNull(this.inputFormat)).tileCountVertical * ((Format) Assertions.checkStateNotNull(this.inputFormat)).tileCountHorizontal) - 1)) goto L58;
    L38:
        if (this.inputFormat.tileCountHorizontal == (-1)) goto L42;
        if (this.inputFormat.tileCountVertical == (-1)) goto L42;
        r2 = true;
        goto L43
    }

    private boolean feedInputBuffer(long r11) throws ImageDecoderException {
        if (this.readyToOutputTiles == true) goto L5;
    L7:
        FormatHolder r0 = getFormatHolder();
        if (this.decoder != null) goto L10;
    L50:
        return false;
    L10:
        if (this.decoderReinitializationState == 3) goto L50;
        if (this.inputStreamEnded == true) goto L50;
        if (this.inputBuffer != null) goto L20;
        this.inputBuffer = this.decoder.dequeueInputBuffer();
        if (this.inputBuffer != null) goto L20;
        return false;
    L20:
        if (this.decoderReinitializationState != 2) goto L24;
        Assertions.checkStateNotNull(this.inputBuffer);
        this.inputBuffer.setFlags(4);
        ((ImageDecoder) Assertions.checkStateNotNull(this.decoder)).queueInputBuffer(this.inputBuffer);
        this.inputBuffer = null;
        this.decoderReinitializationState = 3;
        return false;
    L24:
        switch(readSource(r0, this.inputBuffer, 0)) {
            case -5: goto L48;
            case -4: goto L28;
            case -3: goto L27;
            default: goto L26;
        };
    L27:
        return false;
    L28:
        this.inputBuffer.flip();
        if (this.inputBuffer.data == null) goto L33;
        if (this.inputBuffer.data.remaining() <= 0) goto L33;
    L34:
        boolean r2 = true;
    L36:
        if (r2 == false) goto L38;
        ((ImageDecoder) Assertions.checkStateNotNull(this.decoder)).queueInputBuffer((DecoderInputBuffer) Assertions.checkStateNotNull(this.inputBuffer));
        this.currentTileIndex = 0;
    L38:
        maybeAdvanceTileInfo(r11, (DecoderInputBuffer) Assertions.checkStateNotNull(this.inputBuffer));
        if (((DecoderInputBuffer) Assertions.checkStateNotNull(this.inputBuffer)).isEndOfStream() == false) goto L42;
        this.inputStreamEnded = true;
        this.inputBuffer = null;
        return false;
    L42:
        this.largestQueuedPresentationTimeUs = Math.max(this.largestQueuedPresentationTimeUs, ((DecoderInputBuffer) Assertions.checkStateNotNull(this.inputBuffer)).timeUs);
        if (r2 == false) goto L45;
        this.inputBuffer = null;
    L47:
        return !this.readyToOutputTiles;
    L45:
        ((DecoderInputBuffer) Assertions.checkStateNotNull(this.inputBuffer)).clear();
    L33:
        if (((DecoderInputBuffer) Assertions.checkStateNotNull(this.inputBuffer)).isEndOfStream() == true) goto L34;
        r2 = false;
        goto L36
    L48:
        this.inputFormat = (Format) Assertions.checkStateNotNull(r0.format);
        this.decoderReinitializationState = 2;
        return true;
    L26:
        throw new IllegalStateException();
    L5:
        if (this.tileInfo == null) goto L7;
        return false;
    }

    private static ImageOutput getImageOutput(ImageOutput r1) {
        if (r1 != null) goto L5;
        return ImageOutput.NO_OP;
    L5:
        return r1;
    }

    @EnsuresNonNull({"decoder"})
    @RequiresNonNull({"inputFormat"})
    private void initDecoder() throws ExoPlaybackException {
        if (canCreateDecoderForFormat(this.inputFormat) == false) goto L10;
        if (this.decoder == null) goto L7;
        this.decoder.release();
    L7:
        this.decoder = this.decoderFactory.createImageDecoder();
        return;
    L10:
        throw createRendererException(new ImageDecoderException("Provided decoder factory can't create decoder for format."), this.inputFormat, PlaybackException.ERROR_CODE_DECODING_FORMAT_UNSUPPORTED);
    }

    private boolean isTileLastInGrid(TileInfo r5) {
        if (((Format) Assertions.checkStateNotNull(this.inputFormat)).tileCountHorizontal != (-1)) goto L5;
        return true;
    L5:
        if (this.inputFormat.tileCountVertical != (-1)) goto L7;
        return true;
    L7:
        if (r5.getTileIndex() == ((((Format) Assertions.checkStateNotNull(this.inputFormat)).tileCountVertical * this.inputFormat.tileCountHorizontal) - 1)) goto L14;
        return false;
    L14:
        return true;
    }

    private void lowerFirstFrameState(int r2) {
        this.firstFrameState = Math.min(this.firstFrameState, r2);
    }

    private void maybeAdvanceTileInfo(long r9, DecoderInputBuffer r11) {
        boolean r1 = true;
        if (r11.isEndOfStream() == false) goto L6;
        this.readyToOutputTiles = true;
        return;
    L6:
        this.nextTileInfo = new TileInfo(this.currentTileIndex, r11.timeUs);
        this.currentTileIndex++;
        if (this.readyToOutputTiles == true) goto L32;
        long r2 = this.nextTileInfo.getPresentationTimeUs();
        if ((r2 - 30000) <= r9) goto L11;
    L13:
        boolean r0 = false;
    L15:
        if (this.tileInfo != null) goto L17;
    L21:
        boolean r4 = false;
    L22:
        boolean r5 = isTileLastInGrid((TileInfo) Assertions.checkStateNotNull(this.nextTileInfo));
        if (r0 == true) goto L28;
        if (r4 == true) goto L28;
        if (r5 == true) goto L28;
        r1 = false;
    L28:
        this.readyToOutputTiles = r1;
        if (r4 == false) goto L32;
        if (r0 == true) goto L32;
        return;
    L17:
        if (this.tileInfo.getPresentationTimeUs() > r9) goto L21;
        if (r9 >= r2) goto L21;
        r4 = true;
        goto L22
    L11:
        if (r9 > (30000 + r2)) goto L13;
        r0 = true;
    L32:
        this.tileInfo = this.nextTileInfo;
        this.nextTileInfo = null;
    }

    private void onProcessedOutputBuffer(long r3) {
        this.lastProcessedOutputBufferTimeUs = r3;
    L4:
        if (this.pendingOutputStreamChanges.isEmpty() == true) goto L8;
        if (r3 < this.pendingOutputStreamChanges.peek().previousStreamLastBufferTimeUs) goto L11;
        this.outputStreamInfo = this.pendingOutputStreamChanges.removeFirst();
        goto L4
    L11:
        return;
    }

    private void releaseDecoderResources() {
        this.inputBuffer = null;
        this.decoderReinitializationState = 0;
        this.largestQueuedPresentationTimeUs = C0180C.TIME_UNSET;
        if (this.decoder == null) goto L6;
        this.decoder.release();
        this.decoder = null;
        return;
    }

    private void setImageOutput(ImageOutput r2) {
        this.imageOutput = getImageOutput(r2);
    }

    private boolean shouldForceRender() {
        if (getState() != 2) goto L5;
        boolean r0 = true;
    L7:
        switch(this.firstFrameState) {
            case 0: goto L12;
            case 1: goto L11;
            case 2: goto L9;
            case 3: goto L10;
            default: goto L9;
        };
    L10:
        return false;
    L11:
        return true;
    L12:
        return r0;
    L9:
        throw new IllegalStateException();
    L5:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.exoplayer.Renderer
    public String getName() {
        return TAG;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.PlayerMessage.Target
    public void handleMessage(int r2, Object r3) throws ExoPlaybackException {
        switch(r2) {
            case 15: goto L5;
            default: goto L3;
        };
    L3:
        super.handleMessage(r2, r3);
        return;
    L5:
        if ((r3 instanceof ImageOutput) == false) goto L7;
        ImageOutput r0 = (ImageOutput) r3;
    L8:
        setImageOutput(r0);
        return;
    L7:
        r0 = null;
        goto L8
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isEnded() {
        return this.outputStreamEnded;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isReady() {
        if (this.firstFrameState != 3) goto L5;
    L10:
        return true;
    L5:
        if (this.firstFrameState == 0) goto L7;
    L9:
        return false;
    L7:
        if (this.readyToOutputTiles == false) goto L9;
        goto L9
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onDisabled() {
        this.inputFormat = null;
        this.outputStreamInfo = OutputStreamInfo.UNSET;
        this.pendingOutputStreamChanges.clear();
        releaseDecoderResources();
        this.imageOutput.onDisabled();
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onEnabled(boolean r2, boolean r3) throws ExoPlaybackException {
        if (r3 == false) goto L4;
        int r0 = 1;
    L5:
        this.firstFrameState = r0;
        return;
    L4:
        r0 = 0;
        goto L5
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onPositionReset(long r3, boolean r5) throws ExoPlaybackException {
        lowerFirstFrameState(1);
        this.outputStreamEnded = false;
        this.inputStreamEnded = false;
        this.outputBitmap = null;
        this.tileInfo = null;
        this.nextTileInfo = null;
        this.readyToOutputTiles = false;
        this.inputBuffer = null;
        if (this.decoder == null) goto L5;
        this.decoder.flush();
    L5:
        this.pendingOutputStreamChanges.clear();
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onRelease() {
        releaseDecoderResources();
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onReset() {
        releaseDecoderResources();
        lowerFirstFrameState(1);
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onStreamChanged(Format[] r8, long r9, long r11, MediaSource.MediaPeriodId r13) throws ExoPlaybackException {
        super.onStreamChanged(r8, r9, r11, r13);
        if (this.outputStreamInfo.streamOffsetUs != C0180C.TIME_UNSET) goto L5;
    L14:
        this.outputStreamInfo = new OutputStreamInfo(C0180C.TIME_UNSET, r11);
        return;
    L5:
        if (this.pendingOutputStreamChanges.isEmpty() == true) goto L7;
    L13:
        this.pendingOutputStreamChanges.add(new OutputStreamInfo(this.largestQueuedPresentationTimeUs, r11));
        return;
    L7:
        if (this.largestQueuedPresentationTimeUs == C0180C.TIME_UNSET) goto L14;
        if (this.lastProcessedOutputBufferTimeUs == C0180C.TIME_UNSET) goto L13;
        if (this.lastProcessedOutputBufferTimeUs < this.largestQueuedPresentationTimeUs) goto L13;
        goto L13
    }

    protected boolean processOutputBuffer(long r6, long r8, Bitmap r10, long r11) throws ExoPlaybackException {
        long r0 = r11 - r6;
        if (shouldForceRender() == false) goto L5;
    L9:
        this.imageOutput.onImageAvailable(r11 - this.outputStreamInfo.streamOffsetUs, r10);
        return true;
    L5:
        if (r0 < 30000) goto L9;
        return false;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public void render(long r4, long r6) throws ExoPlaybackException {
        if (this.outputStreamEnded == false) goto L6;
        return;
    L6:
        if (this.inputFormat != null) goto L27;
        FormatHolder r0 = getFormatHolder();
        this.flagsOnlyBuffer.clear();
        int r1 = readSource(r0, this.flagsOnlyBuffer, 2);
        if (r1 != (-5)) goto L11;
        this.inputFormat = (Format) Assertions.checkStateNotNull(r0.format);
        initDecoder();
        goto L27
    L11:
        if (r1 != (-4)) goto L14;
        Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
        this.inputStreamEnded = true;
        this.outputStreamEnded = true;
        return;
    L14:
        return;
    L27:
        TraceUtil.beginSection("drainAndFeedDecoder");     // Catch: ImageDecoderException -> L24
    L17:
        if (drainOutput(r4, r6) == true) goto L17;
    L20:
        if (feedInputBuffer(r4) == true) goto L20;
        TraceUtil.endSection();     // Catch: ImageDecoderException -> L24
        return;
    L24:
        e = move-exception;
        throw createRendererException(e, null, PlaybackException.ERROR_CODE_DECODING_FAILED);
    }

    @Override // androidx.media3.exoplayer.RendererCapabilities
    public int supportsFormat(Format r2) {
        return this.decoderFactory.supportsFormat(r2);
    }
}
