package androidx.media3.exoplayer.video.spherical;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.BaseRenderer;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.exoplayer.audio.SilenceSkippingAudioProcessor;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public final class CameraMotionRenderer extends BaseRenderer {
    private static final int SAMPLE_WINDOW_DURATION_US = 100000;
    private static final String TAG = "CameraMotionRenderer";
    private final DecoderInputBuffer buffer;
    private long lastTimestampUs;
    private CameraMotionListener listener;
    private final ParsableByteArray scratch;

    public CameraMotionRenderer() {
        super(6);
        this.buffer = new DecoderInputBuffer(1);
        this.scratch = new ParsableByteArray();
    }

    private float[] parseMetadata(ByteBuffer r5) {
        if (r5.remaining() == 16) goto L6;
        return null;
    L6:
        this.scratch.reset(r5.array(), r5.limit());
        this.scratch.setPosition(r5.arrayOffset() + 4);
        float[] r1 = new float[3];
        int r2 = 0;
    L7:
        if (r2 >= 3) goto L9;
        r1[r2] = Float.intBitsToFloat(this.scratch.readLittleEndianInt());
        r2 = r2 + 1;
        goto L7
    L9:
        return r1;
    }

    private void resetListener() {
        if (this.listener == null) goto L6;
        this.listener.onCameraMotionReset();
        return;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public String getName() {
        return TAG;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.PlayerMessage.Target
    public void handleMessage(int r2, Object r3) throws ExoPlaybackException {
        if (r2 != 8) goto L5;
        this.listener = (CameraMotionListener) r3;
        return;
    L5:
        super.handleMessage(r2, r3);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isEnded() {
        return hasReadStreamToEnd();
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isReady() {
        return true;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onDisabled() {
        resetListener();
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onPositionReset(long r3, boolean r5) {
        this.lastTimestampUs = Long.MIN_VALUE;
        resetListener();
    }

    @Override // androidx.media3.exoplayer.Renderer
    public void render(long r10, long r12) {
    L3:
        if (hasReadStreamToEnd() == true) goto L23;
        if (this.lastTimestampUs >= (SilenceSkippingAudioProcessor.DEFAULT_MINIMUM_SILENCE_DURATION_US + r10)) goto L37;
        this.buffer.clear();
        boolean r2 = false;
        if (readSource(getFormatHolder(), this.buffer, 0) != (-4)) goto L22;
        if (this.buffer.isEndOfStream() == true) goto L36;
        this.lastTimestampUs = this.buffer.timeUs;
        if (this.lastTimestampUs >= getLastResetPositionUs()) goto L15;
        r2 = true;
    L15:
        if (this.listener == null) goto L3;
        if (r2 == true) goto L3;
        this.buffer.flip();
        float[] r3 = parseMetadata((ByteBuffer) Util.castNonNull(this.buffer.data));
        if (r3 == null) goto L3;
        ((CameraMotionListener) Util.castNonNull(this.listener)).onCameraMotion(this.lastTimestampUs - getStreamOffsetUs(), r3);
        goto L3
    L36:
        return;
    L22:
        return;
    L37:
        return;
    }

    @Override // androidx.media3.exoplayer.RendererCapabilities
    public int supportsFormat(Format r3) {
        if (MimeTypes.APPLICATION_CAMERA_MOTION.equals(r3.sampleMimeType) == false) goto L6;
        return RendererCapabilities.CC.create(4);
    L6:
        return RendererCapabilities.CC.create(0);
    }
}
