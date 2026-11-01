package androidx.media3.exoplayer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.media3.common.util.Log;
import androidx.media3.exoplayer.audio.AudioRendererEventListener;
import androidx.media3.exoplayer.audio.AudioSink;
import androidx.media3.exoplayer.audio.DefaultAudioSink;
import androidx.media3.exoplayer.audio.MediaCodecAudioRenderer;
import androidx.media3.exoplayer.image.ImageDecoder;
import androidx.media3.exoplayer.image.ImageRenderer;
import androidx.media3.exoplayer.mediacodec.DefaultMediaCodecAdapterFactory;
import androidx.media3.exoplayer.mediacodec.MediaCodecAdapter;
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector;
import androidx.media3.exoplayer.metadata.MetadataOutput;
import androidx.media3.exoplayer.metadata.MetadataRenderer;
import androidx.media3.exoplayer.text.TextOutput;
import androidx.media3.exoplayer.text.TextRenderer;
import androidx.media3.exoplayer.video.MediaCodecVideoRenderer;
import androidx.media3.exoplayer.video.VideoRendererEventListener;
import androidx.media3.exoplayer.video.spherical.CameraMotionRenderer;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class DefaultRenderersFactory implements RenderersFactory {
    public static final long DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS = 5000;
    public static final int EXTENSION_RENDERER_MODE_OFF = 0;
    public static final int EXTENSION_RENDERER_MODE_ON = 1;
    public static final int EXTENSION_RENDERER_MODE_PREFER = 2;
    public static final int MAX_DROPPED_VIDEO_FRAME_COUNT_TO_NOTIFY = 50;
    private static final String TAG = "DefaultRenderersFactory";
    private long allowedVideoJoiningTimeMs;
    private final DefaultMediaCodecAdapterFactory codecAdapterFactory;
    private final Context context;
    private boolean enableAudioTrackPlaybackParams;
    private boolean enableDecoderFallback;
    private boolean enableFloatOutput;
    private int extensionRendererMode;
    private MediaCodecSelector mediaCodecSelector;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExtensionRendererMode {
    }

    public DefaultRenderersFactory(Context r3) {
        this.context = r3;
        this.codecAdapterFactory = new DefaultMediaCodecAdapterFactory(r3);
        this.extensionRendererMode = 0;
        this.allowedVideoJoiningTimeMs = 5000;
        this.mediaCodecSelector = MediaCodecSelector.DEFAULT;
    }

    protected void buildAudioRenderers(Context r13, int r14, MediaCodecSelector r15, boolean r16, AudioSink r17, Handler r18, AudioRendererEventListener r19, ArrayList<Renderer> r20) {
        r20.add(new MediaCodecAudioRenderer(r13, getCodecAdapterFactory(), r15, r16, r18, r19, r17));
        if (r14 != 0) goto L5;
        return;
    L5:
        int r0 = r20.size();
        if (r14 != 2) goto L8;
        int r5 = r0 - 1;
    L89:
        int r10 = r5 + 1;
        r20.add(r5, (Renderer) Class.forName("androidx.media3.decoder.midi.MidiRenderer").getConstructor(new Class[]{Context.class}).newInstance(new Object[]{r13}));     // Catch: Exception -> L14 ClassNotFoundException -> L16
        Log.m46i(TAG, "Loaded MidiRenderer.");     // Catch: Exception -> L14 ClassNotFoundException -> L16
    L81:
        int r11 = r10 + 1;
        r20.add(r10, (Renderer) Class.forName("androidx.media3.decoder.opus.LibopusAudioRenderer").getConstructor(new Class[]{Handler.class, AudioRendererEventListener.class, AudioSink.class}).newInstance(new Object[]{r18, r19, r17}));     // Catch: Exception -> L28 ClassNotFoundException -> L30
        Log.m46i(TAG, "Loaded LibopusAudioRenderer.");     // Catch: Exception -> L28 ClassNotFoundException -> L30
    L85:
        int r12 = r11 + 1;
        r20.add(r11, (Renderer) Class.forName("androidx.media3.decoder.flac.LibflacAudioRenderer").getConstructor(new Class[]{Handler.class, AudioRendererEventListener.class, AudioSink.class}).newInstance(new Object[]{r18, r19, r17}));     // Catch: Exception -> L41 ClassNotFoundException -> L43
        Log.m46i(TAG, "Loaded LibflacAudioRenderer.");     // Catch: Exception -> L41 ClassNotFoundException -> L43
    L77:
        int r110 = r12 + 1;
        r20.add(r12, (Renderer) Class.forName("androidx.media3.decoder.ffmpeg.FfmpegAudioRenderer").getConstructor(new Class[]{Handler.class, AudioRendererEventListener.class, AudioSink.class}).newInstance(new Object[]{r18, r19, r17}));     // Catch: Exception -> L54 ClassNotFoundException -> L56
        Log.m46i(TAG, "Loaded FfmpegAudioRenderer.");     // Catch: Exception -> L54 ClassNotFoundException -> L56
    L95:
        int r6 = r110 + 1;
        r20.add(r110, (Renderer) Class.forName("androidx.media3.decoder.iamf.LibiamfAudioRenderer").getConstructor(new Class[]{Context.class, Handler.class, AudioRendererEventListener.class, AudioSink.class}).newInstance(new Object[]{r13, r18, r19, r17}));     // Catch: Exception -> L67 ClassNotFoundException -> L69
        Log.m46i(TAG, "Loaded LibiamfAudioRenderer.");     // Catch: Exception -> L67 ClassNotFoundException -> L69
        goto L70
    L97:
        return;
    L67:
        e = e;
    L73:
        throw new IllegalStateException("Error instantiating IAMF extension", e);
    L70:
        r110 = r6;
        goto L76
    L71:
        e = e;
    L76:
        return;
    L54:
        e = e;
    L60:
        throw new IllegalStateException("Error instantiating FFmpeg extension", e);
    L57:
        r12 = r110;
        goto L62
    L58:
        e = e;
    L62:
        r110 = r12;
        goto L95
    L41:
        e = e;
    L47:
        throw new IllegalStateException("Error instantiating FLAC extension", e);
    L44:
        r11 = r12;
        goto L49
    L45:
        e = e;
    L49:
        r12 = r11;
        goto L77
    L28:
        e = e;
    L34:
        throw new IllegalStateException("Error instantiating Opus extension", e);
    L31:
        r10 = r11;
        goto L36
    L32:
        e = e;
    L36:
        r11 = r10;
    L14:
        e = e;
    L20:
        throw new IllegalStateException("Error instantiating MIDI extension", e);
    L17:
        r5 = r10;
        goto L22
    L18:
        e = e;
    L22:
        r10 = r5;
        goto L81
    L8:
        r5 = r0;
        goto L89
    }

    protected AudioSink buildAudioSink(Context r2, boolean r3, boolean r4) {
        return new DefaultAudioSink.Builder(r2).setEnableFloatOutput(r3).setEnableAudioTrackPlaybackParams(r4).build();
    }

    protected void buildCameraMotionRenderers(Context r2, int r3, ArrayList<Renderer> r4) {
        r4.add(new CameraMotionRenderer());
    }

    protected void buildImageRenderers(ArrayList<Renderer> r4) {
        r4.add(new ImageRenderer(getImageDecoderFactory(), null));
    }

    protected void buildMetadataRenderers(Context r2, MetadataOutput r3, Looper r4, int r5, ArrayList<Renderer> r6) {
        r6.add(new MetadataRenderer(r3, r4));
    }

    protected void buildMiscellaneousRenderers(Context r1, Handler r2, int r3, ArrayList<Renderer> r4) {
    }

    protected void buildTextRenderers(Context r2, TextOutput r3, Looper r4, int r5, ArrayList<Renderer> r6) {
        r6.add(new TextRenderer(r3, r4));
    }

    protected void buildVideoRenderers(Context r17, int r18, MediaCodecSelector r19, boolean r20, Handler r21, VideoRendererEventListener r22, long r23, ArrayList<Renderer> r25) {
        r25.add(new MediaCodecVideoRenderer(r17, getCodecAdapterFactory(), r19, r23, r20, r21, r22, 50));
        if (r18 != 0) goto L5;
        return;
    L5:
        int r0 = r25.size();
        if (r18 != 2) goto L8;
        int r6 = r0 - 1;
    L58:
        int r14 = r6 + 1;
        r25.add(r6, (Renderer) Class.forName("androidx.media3.decoder.vp9.LibvpxVideoRenderer").getConstructor(new Class[]{Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE}).newInstance(new Object[]{Long.valueOf(r23), r21, r22, 50}));     // Catch: Exception -> L14 ClassNotFoundException -> L16
        Log.m46i(TAG, "Loaded LibvpxVideoRenderer.");     // Catch: Exception -> L14 ClassNotFoundException -> L16
    L56:
        int r13 = r14 + 1;
        r25.add(r14, (Renderer) Class.forName("androidx.media3.decoder.av1.Libgav1VideoRenderer").getConstructor(new Class[]{Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE}).newInstance(new Object[]{Long.valueOf(r23), r21, r22, 50}));     // Catch: Exception -> L27 ClassNotFoundException -> L29
        Log.m46i(TAG, "Loaded Libgav1VideoRenderer.");     // Catch: Exception -> L27 ClassNotFoundException -> L29
    L50:
        int r7 = r13 + 1;
        r25.add(r13, (Renderer) Class.forName("androidx.media3.decoder.ffmpeg.ExperimentalFfmpegVideoRenderer").getConstructor(new Class[]{Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE}).newInstance(new Object[]{Long.valueOf(r23), r21, r22, 50}));     // Catch: Exception -> L40 ClassNotFoundException -> L42
        Log.m46i(TAG, "Loaded FfmpegVideoRenderer.");     // Catch: Exception -> L40 ClassNotFoundException -> L42
        goto L43
    L62:
        return;
    L40:
        e = e;
    L46:
        throw new IllegalStateException("Error instantiating FFmpeg extension", e);
    L43:
        r13 = r7;
        goto L49
    L44:
        e = e;
    L49:
        return;
    L27:
        e = e;
    L33:
        throw new IllegalStateException("Error instantiating AV1 extension", e);
    L30:
        r14 = r13;
        goto L35
    L31:
        e = e;
    L35:
        r13 = r14;
    L14:
        e = e;
    L20:
        throw new IllegalStateException("Error instantiating VP9 extension", e);
    L17:
        r6 = r14;
        goto L22
    L18:
        e = e;
    L22:
        r14 = r6;
        goto L56
    L8:
        r6 = r0;
        goto L58
    }

    @Override // androidx.media3.exoplayer.RenderersFactory
    public Renderer[] createRenderers(Handler r11, VideoRendererEventListener r12, AudioRendererEventListener r13, TextOutput r14, MetadataOutput r15) {
        ArrayList<Renderer> r5 = new ArrayList();
        buildVideoRenderers(this.context, this.extensionRendererMode, this.mediaCodecSelector, this.enableDecoderFallback, r11, r12, this.allowedVideoJoiningTimeMs, r5);
        ArrayList<Renderer> r6 = r5;
        AudioSink r1 = buildAudioSink(this.context, this.enableFloatOutput, this.enableAudioTrackPlaybackParams);
        if (r1 == null) goto L6;
        buildAudioRenderers(this.context, this.extensionRendererMode, this.mediaCodecSelector, this.enableDecoderFallback, r1, r11, r13, r6);
        r6 = r6;
    L6:
        buildTextRenderers(this.context, r14, r11.getLooper(), this.extensionRendererMode, r6);
        buildMetadataRenderers(this.context, r15, r11.getLooper(), this.extensionRendererMode, r6);
        buildCameraMotionRenderers(this.context, this.extensionRendererMode, r6);
        buildImageRenderers(r6);
        buildMiscellaneousRenderers(this.context, r11, this.extensionRendererMode, r6);
        return (Renderer[]) r6.toArray(new Renderer[0]);
    }

    public final DefaultRenderersFactory experimentalSetMediaCodecAsyncCryptoFlagEnabled(boolean r2) {
        this.codecAdapterFactory.experimentalSetAsyncCryptoFlagEnabled(r2);
        return this;
    }

    public final DefaultRenderersFactory forceDisableMediaCodecAsynchronousQueueing() {
        this.codecAdapterFactory.forceDisableAsynchronous();
        return this;
    }

    public final DefaultRenderersFactory forceEnableMediaCodecAsynchronousQueueing() {
        this.codecAdapterFactory.forceEnableAsynchronous();
        return this;
    }

    protected MediaCodecAdapter.Factory getCodecAdapterFactory() {
        return this.codecAdapterFactory;
    }

    protected ImageDecoder.Factory getImageDecoderFactory() {
        return ImageDecoder.Factory.DEFAULT;
    }

    public final DefaultRenderersFactory setAllowedVideoJoiningTimeMs(long r1) {
        this.allowedVideoJoiningTimeMs = r1;
        return this;
    }

    public final DefaultRenderersFactory setEnableAudioFloatOutput(boolean r1) {
        this.enableFloatOutput = r1;
        return this;
    }

    public final DefaultRenderersFactory setEnableAudioTrackPlaybackParams(boolean r1) {
        this.enableAudioTrackPlaybackParams = r1;
        return this;
    }

    public final DefaultRenderersFactory setEnableDecoderFallback(boolean r1) {
        this.enableDecoderFallback = r1;
        return this;
    }

    public final DefaultRenderersFactory setExtensionRendererMode(int r1) {
        this.extensionRendererMode = r1;
        return this;
    }

    public final DefaultRenderersFactory setMediaCodecSelector(MediaCodecSelector r1) {
        this.mediaCodecSelector = r1;
        return this;
    }
}
