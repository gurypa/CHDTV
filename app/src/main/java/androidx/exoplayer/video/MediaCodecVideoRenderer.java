package androidx.media3.exoplayer.video;

import android.content.Context;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.Display;
import android.view.Surface;
import androidx.media3.common.C0180C;
import androidx.media3.common.Effect;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Timeline;
import androidx.media3.common.VideoSize;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.MediaFormatUtil;
import androidx.media3.common.util.Size;
import androidx.media3.common.util.TraceUtil;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.DecoderReuseEvaluation;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.exoplayer.mediacodec.MediaCodecAdapter;
import androidx.media3.exoplayer.mediacodec.MediaCodecDecoderException;
import androidx.media3.exoplayer.mediacodec.MediaCodecInfo;
import androidx.media3.exoplayer.mediacodec.MediaCodecRenderer;
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector;
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil;
import androidx.media3.exoplayer.source.MediaSource;
import com.arthenica.ffmpegkit.StreamInformation;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.SignedBytes;
import com.google.common.util.concurrent.MoreExecutors;
import java.nio.ByteBuffer;
import java.util.List;
import kotlin.p005io.encoding.Base64;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public class MediaCodecVideoRenderer extends MediaCodecRenderer implements VideoFrameReleaseControl.FrameTimingEvaluator {
    private static final int HEVC_MAX_INPUT_SIZE_THRESHOLD = 2097152;
    private static final float INITIAL_FORMAT_MAX_INPUT_SIZE_SCALE_FACTOR = 1.5f;
    private static final String KEY_CROP_BOTTOM = "crop-bottom";
    private static final String KEY_CROP_LEFT = "crop-left";
    private static final String KEY_CROP_RIGHT = "crop-right";
    private static final String KEY_CROP_TOP = "crop-top";
    private static final long MIN_EARLY_US_LATE_THRESHOLD = -30000;
    private static final long MIN_EARLY_US_VERY_LATE_THRESHOLD = -500000;
    private static final long OFFSET_FROM_PERIOD_END_TO_TREAT_AS_LAST_US = 100000;
    private static final int[] STANDARD_LONG_EDGE_VIDEO_PX = null;
    private static final String TAG = "MediaCodecVideoRenderer";
    private static final long TUNNELING_EOS_PRESENTATION_TIME_US = Long.MAX_VALUE;
    private static boolean deviceNeedsSetOutputSurfaceWorkaround;
    private static boolean evaluatedDeviceNeedsSetOutputSurfaceWorkaround;
    private int buffersInCodecCount;
    private int changeFrameRateStrategy;
    private boolean codecHandlesHdr10PlusOutOfBandMetadata;
    private CodecMaxValues codecMaxValues;
    private boolean codecNeedsSetOutputSurfaceWorkaround;
    private int consecutiveDroppedFrameCount;
    private final Context context;
    private VideoSize decodedVideoSize;
    private final boolean deviceNeedsNoPostProcessWorkaround;
    private Surface displaySurface;
    private long droppedFrameAccumulationStartTimeMs;
    private int droppedFrames;
    private final VideoRendererEventListener.EventDispatcher eventDispatcher;
    private VideoFrameMetadataListener frameMetadataListener;
    private boolean hasSetVideoSink;
    private boolean haveReportedFirstFrameRenderedForCurrentSurface;
    private long lastFrameReleaseTimeNs;
    private final int maxDroppedFramesToNotify;
    private Size outputResolution;
    private final boolean ownsVideoSink;
    private boolean pendingVideoSinkInputStreamChange;
    private long periodDurationUs;
    private PlaceholderSurface placeholderSurface;
    private int rendererPriority;
    private VideoSize reportedVideoSize;
    private int scalingMode;
    private long startPositionUs;
    private long totalVideoFrameProcessingOffsetUs;
    private boolean tunneling;
    private int tunnelingAudioSessionId;
    OnFrameRenderedListenerV23 tunnelingOnFrameRenderedListener;
    private List<Effect> videoEffects;
    private int videoFrameProcessingOffsetCount;
    private final VideoFrameReleaseControl videoFrameReleaseControl;
    private final VideoFrameReleaseControl.FrameReleaseInfo videoFrameReleaseInfo;
    private VideoSink videoSink;

    private static final class Api26 {
        private Api26() {
        }

        public static boolean doesDisplaySupportDolbyVision(Context r8) {
            DisplayManager r1 = (DisplayManager) r8.getSystemService("display");
            int r2 = 0;
            if (r1 == null) goto L5;
            Display r3 = r1.getDisplay(0);
        L6:
            if (r3 != null) goto L8;
            return false;
        L8:
            if (r3.isHdr() == false) goto L18;
            int[] r4 = r3.getHdrCapabilities().getSupportedHdrTypes();
            int r5 = r4.length;
        L10:
            if (r2 >= r5) goto L20;
            if (r4[r2] == 1) goto L13;
            r2 = r2 + 1;
            goto L10
        L13:
            return true;
        L20:
            return false;
        L18:
            return false;
        L5:
            r3 = null;
            goto L6
        }
    }

    protected static final class CodecMaxValues {
        public final int height;
        public final int inputSize;
        public final int width;

        public CodecMaxValues(int r1, int r2, int r3) {
            this.width = r1;
            this.height = r2;
            this.inputSize = r3;
        }
    }

    private final class OnFrameRenderedListenerV23 implements MediaCodecAdapter.OnFrameRenderedListener, Handler.Callback {
        private static final int HANDLE_FRAME_RENDERED = 0;
        private final Handler handler;
        final /* synthetic */ MediaCodecVideoRenderer this$0;

        public OnFrameRenderedListenerV23(MediaCodecVideoRenderer r1, MediaCodecAdapter r2) {
            this.this$0 = r1;
            this.handler = Util.createHandlerForCurrentLooper(this);
            r2.setOnFrameRenderedListener(this, this.handler);
        }

        private void handleFrameRendered(long r3) {
            if (this == this.this$0.tunnelingOnFrameRenderedListener) goto L5;
            return;
        L5:
            if (MediaCodecVideoRenderer.access$500(this.this$0) != null) goto L8;
            return;
        L8:
            if (r3 != Long.MAX_VALUE) goto L16;
            MediaCodecVideoRenderer.access$600(this.this$0);
            return;
        L16:
            this.this$0.onProcessedTunneledBuffer(r3);     // Catch: ExoPlaybackException -> L12
            return;
        L12:
            e = move-exception;
            MediaCodecVideoRenderer.access$700(this.this$0, e);
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message r3) {
            switch(r3.what) {
                case 0: goto L6;
                default: goto L4;
            };
        L4:
            return false;
        L6:
            handleFrameRendered(Util.toLong(r3.arg1, r3.arg2));
            return true;
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter.OnFrameRenderedListener
        public void onFrameRendered(MediaCodecAdapter r5, long r6, long r8) {
            if (Util.SDK_INT >= 30) goto L5;
            Message r0 = Message.obtain(this.handler, 0, (int) (r6 >> 32), (int) r6);
            this.handler.sendMessageAtFrontOfQueue(r0);
            return;
        L5:
            handleFrameRendered(r6);
        }
    }

    static {
        STANDARD_LONG_EDGE_VIDEO_PX = new int[]{1920, 1600, 1440, 1280, 960, 854, 640, 540, 480};
    }

    public MediaCodecVideoRenderer(Context r12, MediaCodecAdapter.Factory r13, MediaCodecSelector r14, long r15, boolean r17, Handler r18, VideoRendererEventListener r19, int r20) {
        this(r12, r13, r14, r15, r17, r18, r19, r20, 30.0f);
    }

    public MediaCodecVideoRenderer(Context r13, MediaCodecAdapter.Factory r14, MediaCodecSelector r15, long r16, boolean r18, Handler r19, VideoRendererEventListener r20, int r21, float r22) {
        VideoSink r0 = (VideoSink) null;
        this(r13, r14, r15, r16, r18, r19, r20, r21, r22, null);
    }

    public MediaCodecVideoRenderer(Context r13, MediaCodecAdapter.Factory r14, MediaCodecSelector r15, long r16, boolean r18, Handler r19, VideoRendererEventListener r20, int r21, float r22, VideoSink r23) {
        super(2, r14, r15, r18, r22);
        this.context = r13.getApplicationContext();
        this.maxDroppedFramesToNotify = r21;
        this.videoSink = r23;
        this.eventDispatcher = new VideoRendererEventListener.EventDispatcher(r19, r20);
        if (r23 != null) goto L5;
        boolean r7 = true;
    L6:
        this.ownsVideoSink = r7;
        this.videoFrameReleaseControl = new VideoFrameReleaseControl(this.context, this, r16);
        this.videoFrameReleaseInfo = new VideoFrameReleaseControl.FrameReleaseInfo();
        this.deviceNeedsNoPostProcessWorkaround = deviceNeedsNoPostProcessWorkaround();
        this.outputResolution = Size.UNKNOWN;
        this.scalingMode = 1;
        this.changeFrameRateStrategy = 0;
        this.decodedVideoSize = VideoSize.UNKNOWN;
        this.tunnelingAudioSessionId = 0;
        this.reportedVideoSize = null;
        this.rendererPriority = -1000;
        this.startPositionUs = C0180C.TIME_UNSET;
        this.periodDurationUs = C0180C.TIME_UNSET;
        return;
    L5:
        r7 = false;
        goto L6
    }

    @Deprecated
    public MediaCodecVideoRenderer(Context r14, MediaCodecAdapter.Factory r15, MediaCodecSelector r16, long r17, boolean r19, Handler r20, VideoRendererEventListener r21, int r22, float r23, VideoSinkProvider r24) {
        if (r24 != null) goto L4;
        VideoSink r0 = null;
    L5:
        this(r14, r15, r16, r17, r19, r20, r21, r22, r23, r0);
        return;
    L4:
        r0 = r24.getSink();
        goto L5
    }

    public MediaCodecVideoRenderer(Context r3, MediaCodecSelector r4) {
        this(r3, r4, 0);
    }

    public MediaCodecVideoRenderer(Context r9, MediaCodecSelector r10, long r11) {
        this(r9, r10, r11, null, null, 0);
    }

    public MediaCodecVideoRenderer(Context r12, MediaCodecSelector r13, long r14, Handler r16, VideoRendererEventListener r17, int r18) {
        this(r12, MediaCodecAdapter.Factory.CC.getDefault(r12), r13, r14, false, r16, r17, r18, 30.0f);
    }

    public MediaCodecVideoRenderer(Context r12, MediaCodecSelector r13, long r14, boolean r16, Handler r17, VideoRendererEventListener r18, int r19) {
        this(r12, MediaCodecAdapter.Factory.CC.getDefault(r12), r13, r14, r16, r17, r18, r19, 30.0f);
    }

    static /* synthetic */ Surface access$000(MediaCodecVideoRenderer r1) {
        return r1.displaySurface;
    }

    static /* synthetic */ void access$100(MediaCodecVideoRenderer r0) {
        r0.notifyRenderedFirstFrame();
    }

    static /* synthetic */ ExoPlaybackException access$200(MediaCodecVideoRenderer r1, Throwable r2, Format r3, int r4) {
        return r1.createRendererException(r2, r3, r4);
    }

    static /* synthetic */ void access$300(MediaCodecVideoRenderer r0, ExoPlaybackException r1) {
        r0.setPendingPlaybackException(r1);
    }

    static /* synthetic */ void access$400(MediaCodecVideoRenderer r0, MediaCodecAdapter r1, int r2, long r3, long r5) {
        r0.renderOutputBuffer(r1, r2, r3, r5);
    }

    static /* synthetic */ MediaCodecAdapter access$500(MediaCodecVideoRenderer r1) {
        return r1.getCodec();
    }

    static /* synthetic */ void access$600(MediaCodecVideoRenderer r0) {
        r0.onProcessedTunneledEndOfStream();
    }

    static /* synthetic */ void access$700(MediaCodecVideoRenderer r0, ExoPlaybackException r1) {
        r0.setPendingPlaybackException(r1);
    }

    private static boolean deviceNeedsNoPostProcessWorkaround() {
        return "NVIDIA".equals(Util.MANUFACTURER);
    }

    private static boolean evaluateDeviceNeedsSetOutputSurfaceWorkaround() {
        byte r1 = 5;
        byte r7 = -1;
        if (Util.SDK_INT > 28) goto L36;
        String r0 = Util.DEVICE;
        switch(r0.hashCode()) {
            case -1339091551: goto L29;
            case -1220081023: goto L26;
            case -1220066608: goto L23;
            case -1012436106: goto L20;
            case -760312546: goto L17;
            case -64886864: goto L14;
            case 3415681: goto L11;
            case 825323514: goto L8;
            default: goto L31;
        };
    L31:
        byte r2 = -1;
    L32:
        switch(r2) {
            case 0: goto L34;
            case 1: goto L34;
            case 2: goto L34;
            case 3: goto L34;
            case 4: goto L34;
            case 5: goto L34;
            case 6: goto L34;
            case 7: goto L34;
            default: goto L36;
        };
    L34:
        return true;
    L8:
        if (r0.equals("machuca") == false) goto L511;
        r2 = 5;
        goto L32
    L511:
        r2 = -1;
        goto L32
    L11:
        if (r0.equals("once") == false) goto L512;
        r2 = 6;
        goto L32
    L512:
        r2 = -1;
        goto L32
    L14:
        if (r0.equals("magnolia") == false) goto L513;
        r2 = 4;
        goto L32
    L513:
        r2 = -1;
        goto L32
    L17:
        if (r0.equals("aquaman") == false) goto L514;
        r2 = 0;
        goto L32
    L514:
        r2 = -1;
        goto L32
    L20:
        if (r0.equals("oneday") == false) goto L515;
        r2 = 7;
        goto L32
    L515:
        r2 = -1;
        goto L32
    L23:
        if (r0.equals("dangalUHD") == false) goto L516;
        r2 = 2;
        goto L32
    L516:
        r2 = -1;
        goto L32
    L26:
        if (r0.equals("dangalFHD") == false) goto L517;
        r2 = 3;
        goto L32
    L517:
        r2 = -1;
        goto L32
    L29:
        if (r0.equals("dangal") == false) goto L518;
        r2 = 1;
        goto L32
    L518:
        r2 = -1;
    L36:
        if (Util.SDK_INT <= 27) goto L38;
    L40:
        String r3 = Util.MODEL;
        switch(r3.hashCode()) {
            case -349662828: goto L68;
            case -321033677: goto L65;
            case 2006354: goto L62;
            case 2006367: goto L59;
            case 2006371: goto L56;
            case 1785421873: goto L53;
            case 1785421876: goto L50;
            case 1798172390: goto L47;
            case 2119412532: goto L44;
            default: goto L70;
        };
    L70:
        byte r4 = -1;
    L71:
        switch(r4) {
            case 0: goto L77;
            case 1: goto L77;
            case 2: goto L77;
            case 3: goto L77;
            case 4: goto L77;
            case 5: goto L77;
            case 6: goto L77;
            case 7: goto L77;
            case 8: goto L77;
            default: goto L73;
        };
    L77:
        return true;
    L73:
        if (Util.SDK_INT > 26) goto L510;
        String r5 = Util.DEVICE;
        switch(r5.hashCode()) {
            case -2144781245: goto L496;
            case -2144781185: goto L493;
            case -2144781160: goto L490;
            case -2097309513: goto L487;
            case -2022874474: goto L484;
            case -1978993182: goto L481;
            case -1978990237: goto L478;
            case -1936688988: goto L475;
            case -1936688066: goto L472;
            case -1936688065: goto L469;
            case -1931988508: goto L466;
            case -1885099851: goto L463;
            case -1696512866: goto L460;
            case -1680025915: goto L457;
            case -1615810839: goto L454;
            case -1600724499: goto L451;
            case -1554255044: goto L448;
            case -1481772737: goto L445;
            case -1481772730: goto L442;
            case -1481772729: goto L439;
            case -1320080169: goto L436;
            case -1217592143: goto L433;
            case -1180384755: goto L430;
            case -1139198265: goto L427;
            case -1052835013: goto L424;
            case -993250464: goto L421;
            case -993250458: goto L418;
            case -965403638: goto L415;
            case -958336948: goto L412;
            case -879245230: goto L409;
            case -842500323: goto L406;
            case -821392978: goto L403;
            case -797483286: goto L400;
            case -794946968: goto L397;
            case -788334647: goto L394;
            case -782144577: goto L391;
            case -575125681: goto L388;
            case -521118391: goto L385;
            case -430914369: goto L382;
            case -290434366: goto L379;
            case -282781963: goto L376;
            case -277133239: goto L373;
            case -173639913: goto L370;
            case -56598463: goto L367;
            case 2126: goto L364;
            case 2564: goto L361;
            case 2715: goto L358;
            case 2719: goto L355;
            case 3091: goto L352;
            case 3483: goto L349;
            case 73405: goto L346;
            case 75537: goto L343;
            case 75739: goto L340;
            case 76779: goto L337;
            case 78669: goto L334;
            case 79305: goto L331;
            case 80618: goto L328;
            case 88274: goto L325;
            case 98846: goto L322;
            case 98848: goto L319;
            case 99329: goto L316;
            case 101481: goto L313;
            case 1513190: goto L310;
            case 1514184: goto L307;
            case 1514185: goto L304;
            case 2133089: goto L301;
            case 2133091: goto L298;
            case 2133120: goto L295;
            case 2133151: goto L292;
            case 2133182: goto L289;
            case 2133184: goto L286;
            case 2436959: goto L283;
            case 2463773: goto L280;
            case 2464648: goto L277;
            case 2689555: goto L274;
            case 3154429: goto L271;
            case 3284551: goto L268;
            case 3351335: goto L265;
            case 3386211: goto L262;
            case 41325051: goto L259;
            case 51349633: goto L256;
            case 51350594: goto L253;
            case 55178625: goto L250;
            case 61542055: goto L247;
            case 65355429: goto L244;
            case 66214468: goto L241;
            case 66214470: goto L238;
            case 66214473: goto L235;
            case 66215429: goto L232;
            case 66215431: goto L229;
            case 66215433: goto L226;
            case 66216390: goto L223;
            case 76402249: goto L220;
            case 76404105: goto L217;
            case 76404911: goto L214;
            case 80963634: goto L211;
            case 82882791: goto L208;
            case 98715550: goto L205;
            case 101370885: goto L202;
            case 102844228: goto L199;
            case 165221241: goto L196;
            case 182191441: goto L193;
            case 245388979: goto L190;
            case 287431619: goto L187;
            case 307593612: goto L184;
            case 308517133: goto L181;
            case 316215098: goto L178;
            case 316215116: goto L175;
            case 316246811: goto L172;
            case 316246818: goto L169;
            case 407160593: goto L166;
            case 507412548: goto L163;
            case 793982701: goto L160;
            case 794038622: goto L157;
            case 794040393: goto L154;
            case 835649806: goto L151;
            case 917340916: goto L148;
            case 958008161: goto L145;
            case 1060579533: goto L142;
            case 1150207623: goto L139;
            case 1176899427: goto L136;
            case 1280332038: goto L133;
            case 1306947716: goto L130;
            case 1349174697: goto L127;
            case 1522194893: goto L124;
            case 1691543273: goto L121;
            case 1691544261: goto L118;
            case 1709443163: goto L115;
            case 1865889110: goto L112;
            case 1906253259: goto L109;
            case 1977196784: goto L106;
            case 2006372676: goto L103;
            case 2019281702: goto L100;
            case 2029784656: goto L97;
            case 2030379515: goto L94;
            case 2033393791: goto L91;
            case 2047190025: goto L88;
            case 2047252157: goto L85;
            case 2048319463: goto L82;
            case 2048855701: goto L79;
            default: goto L498;
        };
    L498:
        r1 = -1;
    L499:
        switch(r1) {
            case 0: goto L503;
            case 1: goto L503;
            case 2: goto L503;
            case 3: goto L503;
            case 4: goto L503;
            case 5: goto L503;
            case 6: goto L503;
            case 7: goto L503;
            case 8: goto L503;
            case 9: goto L503;
            case 10: goto L503;
            case 11: goto L503;
            case 12: goto L503;
            case 13: goto L503;
            case 14: goto L503;
            case 15: goto L503;
            case 16: goto L503;
            case 17: goto L503;
            case 18: goto L503;
            case 19: goto L503;
            case 20: goto L503;
            case 21: goto L503;
            case 22: goto L503;
            case 23: goto L503;
            case 24: goto L503;
            case 25: goto L503;
            case 26: goto L503;
            case 27: goto L503;
            case 28: goto L503;
            case 29: goto L503;
            case 30: goto L503;
            case 31: goto L503;
            case 32: goto L503;
            case 33: goto L503;
            case 34: goto L503;
            case 35: goto L503;
            case 36: goto L503;
            case 37: goto L503;
            case 38: goto L503;
            case 39: goto L503;
            case 40: goto L503;
            case 41: goto L503;
            case 42: goto L503;
            case 43: goto L503;
            case 44: goto L503;
            case 45: goto L503;
            case 46: goto L503;
            case 47: goto L503;
            case 48: goto L503;
            case 49: goto L503;
            case 50: goto L503;
            case 51: goto L503;
            case 52: goto L503;
            case 53: goto L503;
            case 54: goto L503;
            case 55: goto L503;
            case 56: goto L503;
            case 57: goto L503;
            case 58: goto L503;
            case 59: goto L503;
            case 60: goto L503;
            case 61: goto L503;
            case 62: goto L503;
            case 63: goto L503;
            case 64: goto L503;
            case 65: goto L503;
            case 66: goto L503;
            case 67: goto L503;
            case 68: goto L503;
            case 69: goto L503;
            case 70: goto L503;
            case 71: goto L503;
            case 72: goto L503;
            case 73: goto L503;
            case 74: goto L503;
            case 75: goto L503;
            case 76: goto L503;
            case 77: goto L503;
            case 78: goto L503;
            case 79: goto L503;
            case 80: goto L503;
            case 81: goto L503;
            case 82: goto L503;
            case 83: goto L503;
            case 84: goto L503;
            case 85: goto L503;
            case 86: goto L503;
            case 87: goto L503;
            case 88: goto L503;
            case 89: goto L503;
            case 90: goto L503;
            case 91: goto L503;
            case 92: goto L503;
            case 93: goto L503;
            case 94: goto L503;
            case 95: goto L503;
            case 96: goto L503;
            case 97: goto L503;
            case 98: goto L503;
            case 99: goto L503;
            case 100: goto L503;
            case 101: goto L503;
            case 102: goto L503;
            case 103: goto L503;
            case 104: goto L503;
            case 105: goto L503;
            case 106: goto L503;
            case 107: goto L503;
            case 108: goto L503;
            case 109: goto L503;
            case 110: goto L503;
            case 111: goto L503;
            case 112: goto L503;
            case 113: goto L503;
            case 114: goto L503;
            case 115: goto L503;
            case 116: goto L503;
            case 117: goto L503;
            case 118: goto L503;
            case 119: goto L503;
            case 120: goto L503;
            case 121: goto L503;
            case 122: goto L503;
            case 123: goto L503;
            case 124: goto L503;
            case 125: goto L503;
            case 126: goto L503;
            case 127: goto L503;
            case 128: goto L503;
            case 129: goto L503;
            case 130: goto L503;
            case 131: goto L503;
            case 132: goto L503;
            case 133: goto L503;
            case 134: goto L503;
            case 135: goto L503;
            case 136: goto L503;
            case 137: goto L503;
            case 138: goto L503;
            case 139: goto L503;
            default: goto L500;
        };
    L500:
        String r6 = Util.MODEL;
        switch(r6.hashCode()) {
            case -594534941: goto L505;
            default: goto L507;
        };
    L507:
        switch(r7) {
            case 0: goto L509;
            default: goto L510;
        };
    L509:
        return true;
    L505:
        if (r6.equals("JSN-L21") == false) goto L507;
        r7 = 0;
        goto L507
    L503:
        return true;
    L79:
        if (r5.equals("HWWAS-H") == false) goto L519;
        r1 = 66;
        goto L499
    L519:
        r1 = -1;
        goto L499
    L82:
        if (r5.equals("HWVNS-H") == false) goto L520;
        r1 = 65;
        goto L499
    L520:
        r1 = -1;
        goto L499
    L85:
        if (r5.equals("ELUGA_Prim") == false) goto L521;
        r1 = 33;
        goto L499
    L521:
        r1 = -1;
        goto L499
    L88:
        if (r5.equals("ELUGA_Note") == false) goto L522;
        r1 = 32;
        goto L499
    L522:
        r1 = -1;
        goto L499
    L91:
        if (r5.equals("ASUS_X00AD_2") == false) goto L523;
        r1 = Ascii.f89SO;
        goto L499
    L523:
        r1 = -1;
        goto L499
    L94:
        if (r5.equals("HWCAM-H") == false) goto L524;
        r1 = SignedBytes.MAX_POWER_OF_TWO;
        goto L499
    L524:
        r1 = -1;
        goto L499
    L97:
        if (r5.equals("HWBLN-H") == false) goto L525;
        r1 = 63;
        goto L499
    L525:
        r1 = -1;
        goto L499
    L100:
        if (r5.equals("DM-01K") == false) goto L526;
        r1 = Ascii.f83GS;
        goto L499
    L526:
        r1 = -1;
        goto L499
    L103:
        if (r5.equals("BRAVIA_ATV3_4K") == false) goto L527;
        r1 = 19;
        goto L499
    L527:
        r1 = -1;
        goto L499
    L106:
        if (r5.equals("Infinix-X572") == false) goto L528;
        r1 = 69;
        goto L499
    L528:
        r1 = -1;
        goto L499
    L109:
        if (r5.equals("PB2-670M") == false) goto L529;
        r1 = 100;
        goto L499
    L529:
        r1 = -1;
        goto L499
    L112:
        if (r5.equals("santoni") == false) goto L530;
        r1 = 117;
        goto L499
    L530:
        r1 = -1;
        goto L499
    L115:
        if (r5.equals("iball8735_9806") == false) goto L531;
        r1 = 68;
        goto L499
    L531:
        r1 = -1;
        goto L499
    L118:
        if (r5.equals("CPH1715") == false) goto L532;
        r1 = Ascii.CAN;
        goto L499
    L532:
        r1 = -1;
        goto L499
    L121:
        if (r5.equals("CPH1609") == false) goto L533;
        r1 = Ascii.ETB;
        goto L499
    L533:
        r1 = -1;
        goto L499
    L124:
        if (r5.equals("woods_f") == false) goto L534;
        r1 = 133;
        goto L499
    L534:
        r1 = -1;
        goto L499
    L127:
        if (r5.equals("htc_e56ml_dtul") == false) goto L535;
        r1 = Base64.padSymbol;
        goto L499
    L535:
        r1 = -1;
        goto L499
    L130:
        if (r5.equals("EverStar_S") == false) goto L536;
        r1 = 35;
        goto L499
    L536:
        r1 = -1;
        goto L499
    L133:
        if (r5.equals("hwALE-H") == false) goto L537;
        r1 = 62;
        goto L499
    L537:
        r1 = -1;
        goto L499
    L136:
        if (r5.equals("itel_S41") == false) goto L538;
        r1 = 71;
        goto L499
    L538:
        r1 = -1;
        goto L499
    L139:
        if (r5.equals("LS-5017") == false) goto L539;
        r1 = 78;
        goto L499
    L539:
        r1 = -1;
        goto L499
    L142:
        if (r5.equals("panell_d") == false) goto L540;
        r1 = 96;
        goto L499
    L540:
        r1 = -1;
        goto L499
    L145:
        if (r5.equals("j2xlteins") == false) goto L541;
        r1 = 72;
        goto L499
    L541:
        r1 = -1;
        goto L499
    L148:
        if (r5.equals("A7000plus") == false) goto L542;
        r1 = 10;
        goto L499
    L542:
        r1 = -1;
        goto L499
    L151:
        if (r5.equals("manning") == false) goto L543;
        r1 = 81;
        goto L499
    L543:
        r1 = -1;
        goto L499
    L154:
        if (r5.equals("GIONEE_WBL7519") == false) goto L544;
        r1 = 59;
        goto L499
    L544:
        r1 = -1;
        goto L499
    L157:
        if (r5.equals("GIONEE_WBL7365") == false) goto L545;
        r1 = 58;
        goto L499
    L545:
        r1 = -1;
        goto L499
    L160:
        if (r5.equals("GIONEE_WBL5708") == false) goto L546;
        r1 = 57;
        goto L499
    L546:
        r1 = -1;
        goto L499
    L163:
        if (r5.equals("QM16XE_U") == false) goto L547;
        r1 = 114;
        goto L499
    L547:
        r1 = -1;
        goto L499
    L166:
        if (r5.equals("Pixi5-10_4G") == false) goto L548;
        r1 = 106;
        goto L499
    L548:
        r1 = -1;
        goto L499
    L169:
        if (r5.equals("TB3-850M") == false) goto L549;
        r1 = 125;
        goto L499
    L549:
        r1 = -1;
        goto L499
    L172:
        if (r5.equals("TB3-850F") == false) goto L550;
        r1 = 124;
        goto L499
    L550:
        r1 = -1;
        goto L499
    L175:
        if (r5.equals("TB3-730X") == false) goto L551;
        r1 = 123;
        goto L499
    L551:
        r1 = -1;
        goto L499
    L178:
        if (r5.equals("TB3-730F") == false) goto L552;
        r1 = 122;
        goto L499
    L552:
        r1 = -1;
        goto L499
    L181:
        if (r5.equals("A7020a48") == false) goto L553;
        r1 = Ascii.f81FF;
        goto L499
    L553:
        r1 = -1;
        goto L499
    L184:
        if (r5.equals("A7010a48") == false) goto L554;
        r1 = Ascii.f92VT;
        goto L499
    L554:
        r1 = -1;
        goto L499
    L187:
        if (r5.equals("griffin") == false) goto L555;
        r1 = 60;
        goto L499
    L555:
        r1 = -1;
        goto L499
    L190:
        if (r5.equals("marino_f") == false) goto L556;
        r1 = 82;
        goto L499
    L556:
        r1 = -1;
        goto L499
    L193:
        if (r5.equals("CPY83_I00") == false) goto L557;
        r1 = Ascii.f80EM;
        goto L499
    L557:
        r1 = -1;
        goto L499
    L196:
        if (r5.equals("A2016a40") == false) goto L558;
        r1 = 8;
        goto L499
    L558:
        r1 = -1;
        goto L499
    L199:
        if (r5.equals("le_x6") == false) goto L559;
        r1 = 77;
        goto L499
    L559:
        r1 = -1;
        goto L499
    L202:
        if (r5.equals("l5460") == false) goto L560;
        r1 = 76;
        goto L499
    L560:
        r1 = -1;
        goto L499
    L205:
        if (r5.equals("i9031") == false) goto L561;
        r1 = 67;
        goto L499
    L561:
        r1 = -1;
        goto L499
    L208:
        if (r5.equals("X3_HK") == false) goto L562;
        r1 = 135;
        goto L499
    L562:
        r1 = -1;
        goto L499
    L211:
        if (r5.equals("V23GB") == false) goto L563;
        r1 = 128;
        goto L499
    L563:
        r1 = -1;
        goto L499
    L214:
        if (r5.equals("Q4310") == false) goto L564;
        r1 = 112;
        goto L499
    L564:
        r1 = -1;
        goto L499
    L217:
        if (r5.equals("Q4260") == false) goto L565;
        r1 = 110;
        goto L499
    L565:
        r1 = -1;
        goto L499
    L220:
        if (r5.equals("PRO7S") == false) goto L566;
        r1 = 108;
        goto L499
    L566:
        r1 = -1;
        goto L499
    L223:
        if (r5.equals("F3311") == false) goto L567;
        r1 = 48;
        goto L499
    L567:
        r1 = -1;
        goto L499
    L226:
        if (r5.equals("F3215") == false) goto L568;
        r1 = 47;
        goto L499
    L568:
        r1 = -1;
        goto L499
    L229:
        if (r5.equals("F3213") == false) goto L569;
        r1 = 46;
        goto L499
    L569:
        r1 = -1;
        goto L499
    L232:
        if (r5.equals("F3211") == false) goto L570;
        r1 = 45;
        goto L499
    L570:
        r1 = -1;
        goto L499
    L235:
        if (r5.equals("F3116") == false) goto L571;
        r1 = 44;
        goto L499
    L571:
        r1 = -1;
        goto L499
    L238:
        if (r5.equals("F3113") == false) goto L572;
        r1 = 43;
        goto L499
    L572:
        r1 = -1;
        goto L499
    L241:
        if (r5.equals("F3111") == false) goto L573;
        r1 = 42;
        goto L499
    L573:
        r1 = -1;
        goto L499
    L244:
        if (r5.equals("E5643") == false) goto L574;
        r1 = Ascii.f87RS;
        goto L499
    L574:
        r1 = -1;
        goto L499
    L247:
        if (r5.equals("A1601") == false) goto L575;
        r1 = 7;
        goto L499
    L575:
        r1 = -1;
        goto L499
    L250:
        if (r5.equals("Aura_Note_2") == false) goto L576;
        r1 = Ascii.f88SI;
        goto L499
    L576:
        r1 = -1;
        goto L499
    L253:
        if (r5.equals("602LV") == false) goto L577;
        r1 = 4;
        goto L499
    L577:
        r1 = -1;
        goto L499
    L256:
        if (r5.equals("601LV") == false) goto L578;
        r1 = 3;
        goto L499
    L578:
        r1 = -1;
        goto L499
    L259:
        if (r5.equals("MEIZU_M5") == false) goto L579;
        r1 = 83;
        goto L499
    L579:
        r1 = -1;
        goto L499
    L262:
        if (r5.equals("p212") == false) goto L580;
        r1 = 92;
        goto L499
    L580:
        r1 = -1;
        goto L499
    L265:
        if (r5.equals("mido") == false) goto L581;
        r1 = 85;
        goto L499
    L581:
        r1 = -1;
        goto L499
    L268:
        if (r5.equals("kate") == false) goto L582;
        r1 = 75;
        goto L499
    L582:
        r1 = -1;
        goto L499
    L271:
        if (r5.equals("fugu") == false) goto L583;
        r1 = 50;
        goto L499
    L583:
        r1 = -1;
        goto L499
    L274:
        if (r5.equals("XE2X") == false) goto L584;
        r1 = 136;
        goto L499
    L584:
        r1 = -1;
        goto L499
    L277:
        if (r5.equals("Q427") == false) goto L585;
        r1 = 111;
        goto L499
    L585:
        r1 = -1;
        goto L499
    L280:
        if (r5.equals("Q350") == false) goto L586;
        r1 = 109;
        goto L499
    L586:
        r1 = -1;
        goto L499
    L283:
        if (r5.equals("P681") == false) goto L587;
        r1 = 93;
        goto L499
    L587:
        r1 = -1;
        goto L499
    L286:
        if (r5.equals("F04J") == false) goto L588;
        r1 = 41;
        goto L499
    L588:
        r1 = -1;
        goto L499
    L289:
        if (r5.equals("F04H") == false) goto L589;
        r1 = 40;
        goto L499
    L589:
        r1 = -1;
        goto L499
    L292:
        if (r5.equals("F03H") == false) goto L590;
        r1 = 39;
        goto L499
    L590:
        r1 = -1;
        goto L499
    L295:
        if (r5.equals("F02H") == false) goto L591;
        r1 = 38;
        goto L499
    L591:
        r1 = -1;
        goto L499
    L298:
        if (r5.equals("F01J") == false) goto L592;
        r1 = 37;
        goto L499
    L592:
        r1 = -1;
        goto L499
    L301:
        if (r5.equals("F01H") == false) goto L593;
        r1 = 36;
        goto L499
    L593:
        r1 = -1;
        goto L499
    L304:
        if (r5.equals("1714") == false) goto L594;
        r1 = 2;
        goto L499
    L594:
        r1 = -1;
        goto L499
    L307:
        if (r5.equals("1713") == false) goto L595;
        r1 = 1;
        goto L499
    L595:
        r1 = -1;
        goto L499
    L310:
        if (r5.equals("1601") == false) goto L596;
        r1 = 0;
        goto L499
    L596:
        r1 = -1;
        goto L499
    L313:
        if (r5.equals("flo") == false) goto L597;
        r1 = 49;
        goto L499
    L597:
        r1 = -1;
        goto L499
    L316:
        if (r5.equals("deb") == false) goto L598;
        r1 = 28;
        goto L499
    L598:
        r1 = -1;
        goto L499
    L319:
        if (r5.equals("cv3") == false) goto L599;
        r1 = 27;
        goto L499
    L599:
        r1 = -1;
        goto L499
    L322:
        if (r5.equals("cv1") == false) goto L600;
        r1 = 26;
        goto L499
    L600:
        r1 = -1;
        goto L499
    L325:
        if (r5.equals("Z80") == false) goto L601;
        r1 = 139;
        goto L499
    L601:
        r1 = -1;
        goto L499
    L328:
        if (r5.equals("QX1") == false) goto L602;
        r1 = 115;
        goto L499
    L602:
        r1 = -1;
        goto L499
    L331:
        if (r5.equals("PLE") == false) goto L603;
        r1 = 107;
        goto L499
    L603:
        r1 = -1;
        goto L499
    L334:
        if (r5.equals("P85") == false) goto L604;
        r1 = 94;
        goto L499
    L604:
        r1 = -1;
        goto L499
    L337:
        if (r5.equals("MX6") == false) goto L605;
        r1 = 86;
        goto L499
    L605:
        r1 = -1;
        goto L499
    L340:
        if (r5.equals("M5c") == false) goto L606;
        r1 = 80;
        goto L499
    L606:
        r1 = -1;
        goto L499
    L343:
        if (r5.equals("M04") == false) goto L607;
        r1 = 79;
        goto L499
    L607:
        r1 = -1;
        goto L499
    L346:
        if (r5.equals("JGZ") == false) goto L608;
        r1 = 73;
        goto L499
    L608:
        r1 = -1;
        goto L499
    L349:
        if (r5.equals("mh") == false) goto L609;
        r1 = 84;
        goto L499
    L609:
        r1 = -1;
        goto L499
    L352:
        if (r5.equals("b5") == false) goto L610;
        r1 = Ascii.DLE;
        goto L499
    L610:
        r1 = -1;
        goto L499
    L355:
        if (r5.equals("V5") == false) goto L611;
        r1 = 129;
        goto L499
    L611:
        r1 = -1;
        goto L499
    L358:
        if (r5.equals("V1") == false) goto L612;
        r1 = 127;
        goto L499
    L612:
        r1 = -1;
        goto L499
    L361:
        if (r5.equals("Q5") == false) goto L613;
        r1 = 113;
        goto L499
    L613:
        r1 = -1;
        goto L499
    L364:
        if (r5.equals("C1") == false) goto L614;
        r1 = Ascii.DC4;
        goto L499
    L614:
        r1 = -1;
        goto L499
    L367:
        if (r5.equals("woods_fn") == false) goto L615;
        r1 = 134;
        goto L499
    L615:
        r1 = -1;
        goto L499
    L370:
        if (r5.equals("ELUGA_A3_Pro") == false) goto L616;
        r1 = Ascii.f91US;
        goto L499
    L616:
        r1 = -1;
        goto L499
    L373:
        if (r5.equals("Z12_PRO") == false) goto L617;
        r1 = 138;
        goto L499
    L617:
        r1 = -1;
        goto L499
    L376:
        if (r5.equals("BLACK-1X") == false) goto L618;
        r1 = 17;
        goto L499
    L618:
        r1 = -1;
        goto L499
    L379:
        if (r5.equals("taido_row") == false) goto L619;
        r1 = 121;
        goto L499
    L619:
        r1 = -1;
        goto L499
    L382:
        if (r5.equals("Pixi4-7_3G") == false) goto L620;
        r1 = 105;
        goto L499
    L620:
        r1 = -1;
        goto L499
    L385:
        if (r5.equals("GIONEE_GBL7360") == false) goto L621;
        r1 = 53;
        goto L499
    L621:
        r1 = -1;
        goto L499
    L388:
        if (r5.equals("GiONEE_CBL7513") == false) goto L622;
        r1 = 51;
        goto L499
    L622:
        r1 = -1;
        goto L499
    L391:
        if (r5.equals("OnePlus5T") == false) goto L623;
        r1 = 91;
        goto L499
    L623:
        r1 = -1;
        goto L499
    L394:
        if (r5.equals("whyred") == false) goto L624;
        r1 = 132;
        goto L499
    L624:
        r1 = -1;
        goto L499
    L397:
        if (r5.equals("watson") == false) goto L625;
        r1 = 131;
        goto L499
    L625:
        r1 = -1;
        goto L499
    L400:
        if (r5.equals("SVP-DTV15") == false) goto L626;
        r1 = 119;
        goto L499
    L626:
        r1 = -1;
        goto L499
    L403:
        if (r5.equals("A7000-a") == false) goto L627;
        r1 = 9;
        goto L499
    L627:
        r1 = -1;
        goto L499
    L406:
        if (r5.equals("nicklaus_f") == false) goto L628;
        r1 = 88;
        goto L499
    L628:
        r1 = -1;
        goto L499
    L409:
        if (r5.equals("tcl_eu") == false) goto L629;
        r1 = 126;
        goto L499
    L629:
        r1 = -1;
        goto L499
    L412:
        if (r5.equals("ELUGA_Ray_X") == false) goto L630;
        r1 = 34;
        goto L499
    L630:
        r1 = -1;
        goto L499
    L415:
        if (r5.equals("s905x018") == false) goto L631;
        r1 = 120;
        goto L499
    L631:
        r1 = -1;
        goto L499
    L418:
        if (r5.equals("A10-70L") == false) goto L632;
        r1 = 6;
        goto L499
    L632:
        r1 = -1;
        goto L499
    L421:
        if (r5.equals("A10-70F") == true) goto L499;
        r1 = -1;
        goto L499
    L424:
        if (r5.equals("namath") == false) goto L634;
        r1 = 87;
        goto L499
    L634:
        r1 = -1;
        goto L499
    L427:
        if (r5.equals("Slate_Pro") == false) goto L635;
        r1 = 118;
        goto L499
    L635:
        r1 = -1;
        goto L499
    L430:
        if (r5.equals("iris60") == false) goto L636;
        r1 = 70;
        goto L499
    L636:
        r1 = -1;
        goto L499
    L433:
        if (r5.equals("BRAVIA_ATV2") == false) goto L637;
        r1 = Ascii.DC2;
        goto L499
    L637:
        r1 = -1;
        goto L499
    L436:
        if (r5.equals("GiONEE_GBL7319") == false) goto L638;
        r1 = 52;
        goto L499
    L638:
        r1 = -1;
        goto L499
    L439:
        if (r5.equals("panell_dt") == false) goto L639;
        r1 = 99;
        goto L499
    L639:
        r1 = -1;
        goto L499
    L442:
        if (r5.equals("panell_ds") == false) goto L640;
        r1 = 98;
        goto L499
    L640:
        r1 = -1;
        goto L499
    L445:
        if (r5.equals("panell_dl") == false) goto L641;
        r1 = 97;
        goto L499
    L641:
        r1 = -1;
        goto L499
    L448:
        if (r5.equals("vernee_M5") == false) goto L642;
        r1 = 130;
        goto L499
    L642:
        r1 = -1;
        goto L499
    L451:
        if (r5.equals("pacificrim") == false) goto L643;
        r1 = 95;
        goto L499
    L643:
        r1 = -1;
        goto L499
    L454:
        if (r5.equals("Phantom6") == false) goto L644;
        r1 = 104;
        goto L499
    L644:
        r1 = -1;
        goto L499
    L457:
        if (r5.equals("ComioS1") == false) goto L645;
        r1 = Ascii.NAK;
        goto L499
    L645:
        r1 = -1;
        goto L499
    L460:
        if (r5.equals("XT1663") == false) goto L646;
        r1 = 137;
        goto L499
    L646:
        r1 = -1;
        goto L499
    L463:
        if (r5.equals("RAIJIN") == false) goto L647;
        r1 = 116;
        goto L499
    L647:
        r1 = -1;
        goto L499
    L466:
        if (r5.equals("AquaPowerM") == false) goto L648;
        r1 = Ascii.f79CR;
        goto L499
    L648:
        r1 = -1;
        goto L499
    L469:
        if (r5.equals("PGN611") == false) goto L649;
        r1 = 103;
        goto L499
    L649:
        r1 = -1;
        goto L499
    L472:
        if (r5.equals("PGN610") == false) goto L650;
        r1 = 102;
        goto L499
    L650:
        r1 = -1;
        goto L499
    L475:
        if (r5.equals("PGN528") == false) goto L651;
        r1 = 101;
        goto L499
    L651:
        r1 = -1;
        goto L499
    L478:
        if (r5.equals("NX573J") == false) goto L652;
        r1 = 90;
        goto L499
    L652:
        r1 = -1;
        goto L499
    L481:
        if (r5.equals("NX541J") == false) goto L653;
        r1 = 89;
        goto L499
    L653:
        r1 = -1;
        goto L499
    L484:
        if (r5.equals("CP8676_I02") == false) goto L654;
        r1 = Ascii.SYN;
        goto L499
    L654:
        r1 = -1;
        goto L499
    L487:
        if (r5.equals("K50a40") == false) goto L655;
        r1 = 74;
        goto L499
    L655:
        r1 = -1;
        goto L499
    L490:
        if (r5.equals("GIONEE_SWW1631") == false) goto L656;
        r1 = 56;
        goto L499
    L656:
        r1 = -1;
        goto L499
    L493:
        if (r5.equals("GIONEE_SWW1627") == false) goto L657;
        r1 = 55;
        goto L499
    L657:
        r1 = -1;
        goto L499
    L496:
        if (r5.equals("GIONEE_SWW1609") == false) goto L658;
        r1 = 54;
        goto L499
    L658:
        r1 = -1;
    L510:
        return false;
    L44:
        if (r3.equals("AFTEUFF014") == false) goto L659;
        r4 = 5;
        goto L71
    L659:
        r4 = -1;
        goto L71
    L47:
        if (r3.equals("AFTSO001") == false) goto L660;
        r4 = 8;
        goto L71
    L660:
        r4 = -1;
        goto L71
    L50:
        if (r3.equals("AFTEU014") == false) goto L661;
        r4 = 4;
        goto L71
    L661:
        r4 = -1;
        goto L71
    L53:
        if (r3.equals("AFTEU011") == false) goto L662;
        r4 = 3;
        goto L71
    L662:
        r4 = -1;
        goto L71
    L56:
        if (r3.equals("AFTR") == false) goto L663;
        r4 = 2;
        goto L71
    L663:
        r4 = -1;
        goto L71
    L59:
        if (r3.equals("AFTN") == false) goto L664;
        r4 = 1;
        goto L71
    L664:
        r4 = -1;
        goto L71
    L62:
        if (r3.equals("AFTA") == false) goto L665;
        r4 = 0;
        goto L71
    L665:
        r4 = -1;
        goto L71
    L65:
        if (r3.equals("AFTKMST12") == false) goto L666;
        r4 = 7;
        goto L71
    L666:
        r4 = -1;
        goto L71
    L68:
        if (r3.equals("AFTJMST12") == false) goto L667;
        r4 = 6;
        goto L71
    L667:
        r4 = -1;
        goto L71
    L38:
        if ("HWEML".equals(Util.DEVICE) == false) goto L40;
        return true;
    }

    public static int getCodecMaxInputSize(MediaCodecInfo r9, Format r10) {
        int r0 = r10.width;
        int r1 = r10.height;
        if (r0 == (-1)) goto L62;
        if (r1 == (-1)) goto L62;
        String r3 = (String) Assertions.checkNotNull(r10.sampleMimeType);
        byte r5 = 1;
        if (MimeTypes.VIDEO_DOLBY_VISION.equals(r3) == false) goto L16;
        r3 = MimeTypes.VIDEO_H265;
        Pair<Integer, Integer> r4 = MediaCodecUtil.getCodecProfileAndLevel(r10);
        if (r4 == null) goto L16;
        int r7 = ((Integer) r4.first).intValue();
        if (r7 == 512) goto L14;
        if (r7 == 1) goto L14;
        if (r7 != 2) goto L16;
    L14:
        r3 = MimeTypes.VIDEO_H264;
    L16:
        switch(r3.hashCode()) {
            case -1664118616: goto L37;
            case -1662735862: goto L34;
            case -1662541442: goto L31;
            case 1187890754: goto L28;
            case 1331836730: goto L25;
            case 1599127256: goto L22;
            case 1599127257: goto L19;
            default: goto L39;
        };
    L39:
        r5 = -1;
    L40:
        switch(r5) {
            case 0: goto L61;
            case 1: goto L61;
            case 2: goto L61;
            case 3: goto L61;
            case 4: goto L59;
            case 5: goto L45;
            case 6: goto L43;
            default: goto L41;
        };
    L41:
        return -1;
    L43:
        return getMaxSampleSize(r0 * r1, 4);
    L45:
        if ("BRAVIA 4K 2015".equals(Util.MODEL) == false) goto L47;
    L57:
        return -1;
    L47:
        if ("Amazon".equals(Util.MANUFACTURER) == false) goto L56;
        if ("KFSOWI".equals(Util.MODEL) == true) goto L57;
        if ("AFTS".equals(Util.MODEL) == false) goto L56;
        if (r9.secure == true) goto L57;
    L56:
        return getMaxSampleSize(((Util.ceilDivide(r0, 16) * Util.ceilDivide(r1, 16)) * 16) * 16, 2);
    L59:
        return Math.max(2097152, getMaxSampleSize(r0 * r1, 2));
    L61:
        return getMaxSampleSize(r0 * r1, 2);
    L19:
        if (r3.equals(MimeTypes.VIDEO_VP9) == false) goto L63;
        r5 = 6;
        goto L40
    L63:
        r5 = -1;
        goto L40
    L22:
        if (r3.equals(MimeTypes.VIDEO_VP8) == false) goto L64;
        r5 = 3;
        goto L40
    L64:
        r5 = -1;
        goto L40
    L25:
        if (r3.equals(MimeTypes.VIDEO_H264) == false) goto L65;
        r5 = 5;
        goto L40
    L65:
        r5 = -1;
        goto L40
    L28:
        if (r3.equals(MimeTypes.VIDEO_MP4V) == true) goto L40;
        r5 = -1;
        goto L40
    L31:
        if (r3.equals(MimeTypes.VIDEO_H265) == false) goto L67;
        r5 = 4;
        goto L40
    L67:
        r5 = -1;
        goto L40
    L34:
        if (r3.equals(MimeTypes.VIDEO_AV1) == false) goto L68;
        r5 = 2;
        goto L40
    L68:
        r5 = -1;
        goto L40
    L37:
        if (r3.equals(MimeTypes.VIDEO_H263) == false) goto L69;
        r5 = 0;
        goto L40
    L69:
        r5 = -1;
    L62:
        return -1;
    }

    private static Point getCodecMaxSize(MediaCodecInfo r16, Format r17) {
        Format r1 = r17;
        int r4 = 0;
        if (r1.height <= r1.width) goto L5;
        boolean r2 = true;
    L6:
        if (r2 == false) goto L8;
        int r3 = r1.height;
    L9:
        if (r2 == false) goto L11;
        int r5 = r1.width;
    L12:
        float r6 = r5 / r3;
        int[] r7 = STANDARD_LONG_EDGE_VIDEO_PX;
        int r8 = r7.length;
    L14:
        if (r4 >= r8) goto L35;
        int r10 = r7[r4];
        int r11 = (int) (r10 * r6);
        if (r10 <= r3) goto L34;
        if (r11 <= r5) goto L34;
        if (r2 == false) goto L22;
        int r9 = r11;
    L23:
        if (r2 == false) goto L25;
        int r12 = r10;
    L26:
        Point r13 = r16.alignVideoSizeV21(r9, r12);
        float r14 = r1.frameRate;
        if (r13 == null) goto L32;
        if (r16.isVideoSizeAndRateSupportedV21(r13.x, r13.y, r14) == false) goto L32;
        return r13;
    L32:
        r4 = r4 + 1;
        r1 = r17;
        r2 = r2;
        goto L14
    L25:
        r12 = r11;
        goto L26
    L22:
        r9 = r10;
    L34:
        return null;
    L35:
        return null;
    L11:
        r5 = r1.height;
        goto L12
    L8:
        r3 = r1.width;
        goto L9
    L5:
        r2 = false;
        goto L6
    }

    private static List<MediaCodecInfo> getDecoderInfos(Context r2, MediaCodecSelector r3, Format r4, boolean r5, boolean r6) throws MediaCodecUtil.DecoderQueryException {
        if (r4.sampleMimeType != null) goto L7;
        return ImmutableList.m212of();
    L7:
        if (Util.SDK_INT < 26) goto L16;
        if (MimeTypes.VIDEO_DOLBY_VISION.equals(r4.sampleMimeType) == false) goto L16;
        if (Api26.doesDisplaySupportDolbyVision(r2) == true) goto L16;
        List<MediaCodecInfo> r0 = MediaCodecUtil.getAlternativeDecoderInfos(r3, r4, r5, r6);
        if (r0.isEmpty() == true) goto L16;
        return r0;
    L16:
        return MediaCodecUtil.getDecoderInfosSoftMatch(r3, r4, r5, r6);
    }

    protected static int getMaxInputSize(MediaCodecInfo r4, Format r5) {
        if (r5.maxInputSize == (-1)) goto L10;
        int r0 = 0;
        int r1 = r5.initializationData.size();
        int r2 = 0;
    L5:
        if (r2 >= r1) goto L8;
        r0 = r0 + r5.initializationData.get(r2).length;
        r2 = r2 + 1;
        goto L5
    L8:
        return r5.maxInputSize + r0;
    L10:
        return getCodecMaxInputSize(r4, r5);
    }

    private static int getMaxSampleSize(int r2, int r3) {
        return (r2 * 3) / (r3 * 2);
    }

    private Surface getSurfaceForCodec(MediaCodecInfo r3) {
        if (this.videoSink == null) goto L7;
        return this.videoSink.getInputSurface();
    L7:
        if (this.displaySurface == null) goto L11;
        return this.displaySurface;
    L11:
        if (shouldUseDetachedSurface(r3) == false) goto L14;
        return null;
    L14:
        Assertions.checkState(shouldUsePlaceholderSurface(r3));
        if (this.placeholderSurface == null) goto L20;
        if (this.placeholderSurface.secure == r3.secure) goto L20;
        releasePlaceholderSurface();
    L20:
        if (this.placeholderSurface != null) goto L23;
        this.placeholderSurface = PlaceholderSurface.newInstance(this.context, r3.secure);
    L23:
        return this.placeholderSurface;
    }

    private boolean hasSurfaceForCodec(MediaCodecInfo r2) {
        if (this.displaySurface == null) goto L7;
        if (this.displaySurface.isValid() == false) goto L7;
    L12:
        return true;
    L7:
        if (shouldUseDetachedSurface(r2) == true) goto L12;
        if (shouldUsePlaceholderSurface(r2) == true) goto L12;
        return false;
    }

    private boolean isBufferBeforeStartTime(DecoderInputBuffer r5) {
        if (r5.timeUs >= getLastResetPositionUs()) goto L5;
        return true;
    L5:
        return false;
    }

    private boolean isBufferProbablyLastSample(DecoderInputBuffer r9) {
        if (hasReadStreamToEnd() == false) goto L5;
    L15:
        return true;
    L5:
        if (r9.isLastSample() == true) goto L15;
        if (this.periodDurationUs != C0180C.TIME_UNSET) goto L10;
        return true;
    L10:
        long r2 = r9.timeUs - getOutputStreamOffsetUs();
        if ((this.periodDurationUs - r2) <= 100000) goto L16;
        return false;
    L16:
        return true;
    }

    private void maybeNotifyDroppedFrames() {
        if (this.droppedFrames <= 0) goto L6;
        long r0 = getClock().elapsedRealtime();
        long r2 = r0 - this.droppedFrameAccumulationStartTimeMs;
        this.eventDispatcher.droppedFrames(this.droppedFrames, r2);
        this.droppedFrames = 0;
        this.droppedFrameAccumulationStartTimeMs = r0;
        return;
    }

    private void maybeNotifyRenderedFirstFrame() {
        if (this.videoFrameReleaseControl.onFrameReleasedIsFirstFrame() == true) goto L5;
        return;
    L5:
        if (this.displaySurface == null) goto L9;
        notifyRenderedFirstFrame();
        return;
    }

    private void maybeNotifyVideoFrameProcessingOffset() {
        if (this.videoFrameProcessingOffsetCount == 0) goto L6;
        this.eventDispatcher.reportVideoFrameProcessingOffset(this.totalVideoFrameProcessingOffsetUs, this.videoFrameProcessingOffsetCount);
        this.totalVideoFrameProcessingOffsetUs = 0;
        this.videoFrameProcessingOffsetCount = 0;
        return;
    }

    private void maybeNotifyVideoSizeChanged(VideoSize r3) {
        if (r3.equals(VideoSize.UNKNOWN) == false) goto L5;
        return;
    L5:
        if (r3.equals(this.reportedVideoSize) == true) goto L9;
        this.reportedVideoSize = r3;
        this.eventDispatcher.videoSizeChanged(this.reportedVideoSize);
        return;
    }

    private void maybeRenotifyRenderedFirstFrame() {
        if (this.displaySurface != null) goto L5;
        return;
    L5:
        if (this.haveReportedFirstFrameRenderedForCurrentSurface == false) goto L9;
        this.eventDispatcher.renderedFirstFrame(this.displaySurface);
        return;
    }

    private void maybeRenotifyVideoSizeChanged() {
        if (this.reportedVideoSize == null) goto L6;
        this.eventDispatcher.videoSizeChanged(this.reportedVideoSize);
        return;
    }

    private void maybeSetKeyAllowFrameDrop(MediaFormat r3) {
        if (this.videoSink != null) goto L5;
        return;
    L5:
        if (Util.isFrameDropAllowedOnSurfaceInput(this.context) == true) goto L9;
        r3.setInteger("allow-frame-drop", 0);
        return;
    }

    private void maybeSetupTunnelingForFirstFrame() {
        if (this.tunneling == true) goto L5;
        return;
    L5:
        if (Util.SDK_INT < 23) goto L16;
        MediaCodecAdapter r0 = getCodec();
        if (r0 != null) goto L10;
        return;
    L10:
        this.tunnelingOnFrameRenderedListener = new OnFrameRenderedListenerV23(this, r0);
        if (Util.SDK_INT < 33) goto L15;
        Bundle r1 = new Bundle();
        r1.putInt("tunnel-peek", 1);
        r0.setParameters(r1);
        return;
    L15:
        return;
    }

    private void notifyFrameMetadataListener(long r9, long r11, Format r13) {
        if (this.frameMetadataListener == null) goto L6;
        this.frameMetadataListener.onVideoFrameAboutToBeRendered(r9, r11, r13, getCodecOutputMediaFormat());
        return;
    }

    @RequiresNonNull({"displaySurface"})
    private void notifyRenderedFirstFrame() {
        this.eventDispatcher.renderedFirstFrame(this.displaySurface);
        this.haveReportedFirstFrameRenderedForCurrentSurface = true;
    }

    private void onProcessedTunneledEndOfStream() {
        setPendingOutputEndOfStream();
    }

    private void releaseFrame(MediaCodecAdapter r11, int r12, long r13, Format r15) {
        long r4 = this.videoFrameReleaseInfo.getReleaseTimeNs();
        long r8 = this.videoFrameReleaseInfo.getEarlyUs();
        if (shouldSkipBuffersWithIdenticalReleaseTime() == true) goto L5;
    L7:
        MediaCodecVideoRenderer r1 = this;
        r1.notifyFrameMetadataListener(r13, r4, r15);
        long r6 = r4;
        r1.renderOutputBufferV21(r11, r12, r13, r6);
    L8:
        updateVideoFrameProcessingOffsetCounters(r8);
        r1.lastFrameReleaseTimeNs = r6;
        return;
    L5:
        if (r4 != this.lastFrameReleaseTimeNs) goto L7;
        skipOutputBuffer(r11, r12, r13);
        r1 = this;
        r6 = r4;
        goto L8
    }

    private void releasePlaceholderSurface() {
        if (this.placeholderSurface == null) goto L6;
        this.placeholderSurface.release();
        this.placeholderSurface = null;
        return;
    }

    private void renderOutputBuffer(MediaCodecAdapter r1, int r2, long r3, long r5) {
        renderOutputBufferV21(r1, r2, r3, r5);
    }

    private static void setHdr10PlusInfoV29(MediaCodecAdapter r2, byte[] r3) {
        Bundle r0 = new Bundle();
        r0.putByteArray("hdr10-plus-info", r3);
        r2.setParameters(r0);
    }

    private void setOutput(Object r9) throws ExoPlaybackException {
        if ((r9 instanceof Surface) == false) goto L5;
        Surface r0 = (Surface) r9;
    L7:
        if (this.displaySurface == r0) goto L33;
        this.displaySurface = r0;
        if (this.videoSink != null) goto L11;
        this.videoFrameReleaseControl.setOutputSurface(r0);
    L11:
        this.haveReportedFirstFrameRenderedForCurrentSurface = false;
        int r2 = getState();
        MediaCodecAdapter r3 = getCodec();
        if (r3 != null) goto L14;
    L22:
        if (r0 == null) goto L29;
        maybeRenotifyVideoSizeChanged();
        if (r2 == 2) goto L26;
    L32:
        maybeSetupTunnelingForFirstFrame();
        return;
    L26:
        if (this.videoSink == null) goto L28;
        this.videoSink.join(true);
        goto L32
    L28:
        this.videoFrameReleaseControl.join(true);
        goto L32
    L29:
        this.reportedVideoSize = null;
        if (this.videoSink == null) goto L32;
        this.videoSink.clearOutputSurfaceInfo();
        goto L32
    L14:
        if (this.videoSink != null) goto L22;
        MediaCodecInfo r4 = (MediaCodecInfo) Assertions.checkNotNull(getCodecInfo());
        boolean r5 = hasSurfaceForCodec(r4);
        if (Util.SDK_INT < 23) goto L21;
        if (r5 == false) goto L21;
        if (this.codecNeedsSetOutputSurfaceWorkaround == true) goto L21;
        setOutputSurface(r3, getSurfaceForCodec(r4));
    L21:
        releaseCodec();
        maybeInitCodecOrBypass();
        goto L22
    L33:
        if (r0 == null) goto L38;
        maybeRenotifyVideoSizeChanged();
        maybeRenotifyRenderedFirstFrame();
        return;
    L38:
        return;
    L5:
        r0 = null;
        goto L7
    }

    private void setOutputSurface(MediaCodecAdapter r3, Surface r4) {
        if (Util.SDK_INT < 23) goto L7;
        if (r4 == null) goto L7;
        setOutputSurfaceV23(r3, r4);
        return;
    L7:
        if (Util.SDK_INT < 35) goto L11;
        detachOutputSurfaceV35(r3);
        return;
    L11:
        throw new IllegalStateException();
    }

    private boolean shouldUsePlaceholderSurface(MediaCodecInfo r3) {
        if (Util.SDK_INT >= 23) goto L5;
    L13:
        return false;
    L5:
        if (this.tunneling == true) goto L13;
        if (codecNeedsSetOutputSurfaceWorkaround(r3.name) == true) goto L13;
        if (r3.secure == true) goto L11;
    L12:
        return true;
    L11:
        if (PlaceholderSurface.isSecureSupported(this.context) == false) goto L13;
        goto L12
    }

    public static int supportsFormat(Context r1, MediaCodecSelector r2, Format r3) throws MediaCodecUtil.DecoderQueryException {
        return supportsFormatInternal(r1, r2, r3);
    }

    private static int supportsFormatInternal(Context r17, MediaCodecSelector r18, Format r19) throws MediaCodecUtil.DecoderQueryException {
        if (MimeTypes.isVideo(r19.sampleMimeType) == true) goto L7;
        return RendererCapabilities.CC.create(0);
    L7:
        if (r19.drmInitData == null) goto L9;
        boolean r7 = true;
    L10:
        List<MediaCodecInfo> r8 = getDecoderInfos(r17, r18, r19, r7, false);
        if (r7 == false) goto L16;
        if (r8.isEmpty() == false) goto L16;
        r8 = getDecoderInfos(r17, r18, r19, false, false);
    L16:
        if (r8.isEmpty() == false) goto L20;
        return RendererCapabilities.CC.create(1);
    L20:
        if (supportsFormatDrm(r19) == false) goto L22;
        MediaCodecInfo r9 = r8.get(0);
        boolean r10 = r9.isFormatSupported(r19);
        boolean r11 = true;
        if (r10 == true) goto L32;
        int r12 = 1;
    L27:
        if (r12 >= r8.size()) goto L32;
        MediaCodecInfo r13 = r8.get(r12);
        if (r13.isFormatSupported(r19) == true) goto L30;
        r12 = r12 + 1;
        goto L27
    L30:
        r9 = r13;
        r10 = true;
        r11 = false;
    L32:
        if (r10 == false) goto L34;
        int r14 = 4;
    L36:
        if (r9.isSeamlessAdaptationSupported(r19) == false) goto L38;
        int r15 = 16;
    L40:
        if (r9.hardwareAccelerated == false) goto L42;
        int r16 = 64;
    L44:
        if (r11 == false) goto L46;
        int r110 = 128;
    L48:
        if (Util.SDK_INT >= 26) goto L50;
    L54:
        int r5 = 0;
        if (r10 == false) goto L64;
        List<MediaCodecInfo> r6 = getDecoderInfos(r17, r18, r19, r7, true);
        if (r6.isEmpty() == true) goto L64;
        MediaCodecInfo r0 = MediaCodecUtil.getDecoderInfosSortedByFormatSupport(r6, r19).get(0);
        if (r0.isFormatSupported(r19) == false) goto L64;
        if (r0.isSeamlessAdaptationSupported(r19) == false) goto L64;
        r5 = 32;
    L64:
        return RendererCapabilities.CC.create(r14, r15, r5, r16, r110);
    L50:
        if (MimeTypes.VIDEO_DOLBY_VISION.equals(r19.sampleMimeType) == false) goto L54;
        if (Api26.doesDisplaySupportDolbyVision(r17) == true) goto L54;
        r110 = 256;
        goto L54
    L46:
        r110 = 0;
        goto L48
    L42:
        r16 = 0;
        goto L44
    L38:
        r15 = 8;
        goto L40
    L34:
        r14 = 3;
        goto L36
    L22:
        return RendererCapabilities.CC.create(2);
    L9:
        r7 = false;
        goto L10
    }

    private void updateCodecImportance() {
        MediaCodecAdapter r0 = getCodec();
        if (r0 != null) goto L6;
        return;
    L6:
        if (Util.SDK_INT < 35) goto L9;
        Bundle r1 = new Bundle();
        r1.putInt("importance", Math.max(0, -this.rendererPriority));
        r0.setParameters(r1);
        return;
    }

    private void updatePeriodDurationUs(MediaSource.MediaPeriodId r4) {
        Timeline r0 = getTimeline();
        if (r0.isEmpty() == false) goto L6;
        this.periodDurationUs = C0180C.TIME_UNSET;
        return;
    L6:
        this.periodDurationUs = r0.getPeriodByUid(((MediaSource.MediaPeriodId) Assertions.checkNotNull(r4)).periodUid, new Timeline.Period()).getDurationUs();
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected DecoderReuseEvaluation canReuseCodec(MediaCodecInfo r10, Format r11, Format r12) {
        DecoderReuseEvaluation r0 = r10.canReuseCodec(r11, r12);
        int r1 = r0.discardReasons;
        CodecMaxValues r2 = (CodecMaxValues) Assertions.checkNotNull(this.codecMaxValues);
        if (r12.width <= r2.width) goto L5;
    L6:
        r1 = r1 | 256;
    L8:
        if (getMaxInputSize(r10, r12) <= r2.inputSize) goto L10;
        int r8 = r1 | 64;
    L11:
        String r4 = r10.name;
        if (r8 == 0) goto L14;
        int r3 = 0;
    L16:
        return new DecoderReuseEvaluation(r4, r11, r12, r3, r8);
    L14:
        r3 = r0.result;
        goto L16
    L10:
        r8 = r1;
        goto L11
    L5:
        if (r12.height <= r2.height) goto L8;
        goto L6
    }

    protected boolean codecNeedsSetOutputSurfaceWorkaround(String r3) {
        if (r3.startsWith("OMX.google") == false) goto L7;
        return false;
    L7:
        monitor-enter(MediaCodecVideoRenderer.class);
    L14:
        th = move-exception;
        throw th;
    L9:
        if (evaluatedDeviceNeedsSetOutputSurfaceWorkaround == true) goto L11;
        deviceNeedsSetOutputSurfaceWorkaround = evaluateDeviceNeedsSetOutputSurfaceWorkaround();     // Catch: Throwable -> L14
        evaluatedDeviceNeedsSetOutputSurfaceWorkaround = true;     // Catch: Throwable -> L14
    L11:
        monitor-exit(MediaCodecVideoRenderer.class);     // Catch: Throwable -> L14
        return deviceNeedsSetOutputSurfaceWorkaround;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected MediaCodecDecoderException createDecoderException(Throwable r3, MediaCodecInfo r4) {
        return new MediaCodecVideoDecoderException(r3, r4, this.displaySurface);
    }

    protected void detachOutputSurfaceV35(MediaCodecAdapter r1) {
        r1.detachOutputSurface();
    }

    protected void dropOutputBuffer(MediaCodecAdapter r3, int r4, long r5) {
        TraceUtil.beginSection("dropVideoBuffer");
        r3.releaseOutputBuffer(r4, false);
        TraceUtil.endSection();
        updateDroppedBufferCounters(0, 1);
    }

    @Override // androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.Renderer
    public void enableMayRenderStartOfStream() {
        if (this.videoSink == null) goto L5;
        this.videoSink.enableMayRenderStartOfStream();
        return;
    L5:
        this.videoFrameReleaseControl.allowReleaseFirstFrameBeforeStarted();
    }

    protected long getBufferTimestampAdjustmentUs() {
        return -this.startPositionUs;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected int getCodecBufferFlags(DecoderInputBuffer r3) {
        if (Util.SDK_INT >= 34) goto L5;
        return 0;
    L5:
        if (this.tunneling == true) goto L7;
        return 0;
    L7:
        if (isBufferBeforeStartTime(r3) == false) goto L13;
        return 32;
    L13:
        return 0;
    }

    protected CodecMaxValues getCodecMaxValues(MediaCodecInfo r13, Format r14, Format[] r15) {
        int r0 = r14.width;
        int r1 = r14.height;
        int r2 = getMaxInputSize(r13, r14);
        if (r15.length != 1) goto L10;
        if (r2 == (-1)) goto L9;
        int r3 = getCodecMaxInputSize(r13, r14);
        if (r3 == (-1)) goto L9;
        r2 = Math.min((int) (r2 * INITIAL_FORMAT_MAX_INPUT_SIZE_SCALE_FACTOR), r3);
    L9:
        return new CodecMaxValues(r0, r1, r2);
    L10:
        boolean r4 = false;
        int r6 = r15.length;
        int r8 = 0;
    L11:
        if (r8 >= r6) goto L28;
        Format r9 = r15[r8];
        if (r14.colorInfo == null) goto L18;
        if (r9.colorInfo != null) goto L18;
        r9 = r9.buildUpon().setColorInfo(r14.colorInfo).build();
    L18:
        if (r13.canReuseCodec(r14, r9).result == 0) goto L27;
        if (r9.width != (-1)) goto L22;
    L25:
        boolean r10 = true;
    L26:
        r4 = r4 | r10;
        r0 = Math.max(r0, r9.width);
        r1 = Math.max(r1, r9.height);
        r2 = Math.max(r2, getMaxInputSize(r13, r9));
        goto L27
    L22:
        if (r9.height == (-1)) goto L25;
        r10 = false;
    L27:
        r8 = r8 + 1;
        goto L11
    L28:
        if (r4 == false) goto L33;
        Log.m48w(TAG, "Resolutions unknown. Codec max resolution: " + r0 + "x" + r1);
        Point r5 = getCodecMaxSize(r13, r14);
        if (r5 == null) goto L33;
        r0 = Math.max(r0, r5.x);
        r1 = Math.max(r1, r5.y);
        r2 = Math.max(r2, getCodecMaxInputSize(r13, r14.buildUpon().setWidth(r0).setHeight(r1).build()));
        Log.m48w(TAG, "Codec max resolution adjusted to: " + r0 + "x" + r1);
    L33:
        return new CodecMaxValues(r0, r1, r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected boolean getCodecNeedsEosPropagation() {
        if (this.tunneling == true) goto L5;
    L7:
        return false;
    L5:
        if (Util.SDK_INT >= 23) goto L7;
        return true;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected float getCodecOperatingRateV23(float r7, Format r8, Format[] r9) {
        float r0 = -1.0f;
        int r1 = r9.length;
        int r2 = 0;
    L4:
        if (r2 >= r1) goto L10;
        float r5 = r9[r2].frameRate;
        if (r5 == (-1.0f)) goto L8;
        r0 = Math.max(r0, r5);
    L8:
        r2 = r2 + 1;
        goto L4
    L10:
        if (r0 != (-1.0f)) goto L13;
        return -1.0f;
    L13:
        return r0 * r7;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected List<MediaCodecInfo> getDecoderInfos(MediaCodecSelector r3, Format r4, boolean r5) throws MediaCodecUtil.DecoderQueryException {
        return MediaCodecUtil.getDecoderInfosSortedByFormatSupport(getDecoderInfos(this.context, r3, r4, r5, this.tunneling), r4);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected MediaCodecAdapter.Configuration getMediaCodecConfiguration(MediaCodecInfo r8, Format r9, MediaCrypto r10, float r11) {
        String r2 = r8.codecMimeType;
        this.codecMaxValues = getCodecMaxValues(r8, r9, getStreamFormats());
        CodecMaxValues r3 = this.codecMaxValues;
        boolean r5 = this.deviceNeedsNoPostProcessWorkaround;
        if (this.tunneling == false) goto L5;
        int r0 = this.tunnelingAudioSessionId;
    L6:
        MediaFormat r12 = getMediaFormat(r9, r2, r3, r11, r5, r0);
        Surface r13 = getSurfaceForCodec(r8);
        maybeSetKeyAllowFrameDrop(r12);
        return MediaCodecAdapter.Configuration.createForVideoDecoding(r8, r12, r9, r13, r10);
    L5:
        r0 = 0;
        goto L6
    }

    protected MediaFormat getMediaFormat(Format r5, String r6, CodecMaxValues r7, float r8, boolean r9, int r10) {
        MediaFormat r0 = new MediaFormat();
        r0.setString("mime", r6);
        r0.setInteger(StreamInformation.KEY_WIDTH, r5.width);
        r0.setInteger(StreamInformation.KEY_HEIGHT, r5.height);
        MediaFormatUtil.setCsdBuffers(r0, r5.initializationData);
        MediaFormatUtil.maybeSetFloat(r0, "frame-rate", r5.frameRate);
        MediaFormatUtil.maybeSetInteger(r0, "rotation-degrees", r5.rotationDegrees);
        MediaFormatUtil.maybeSetColorInfo(r0, r5.colorInfo);
        if (MimeTypes.VIDEO_DOLBY_VISION.equals(r5.sampleMimeType) == false) goto L7;
        Pair<Integer, Integer> r1 = MediaCodecUtil.getCodecProfileAndLevel(r5);
        if (r1 == null) goto L7;
        MediaFormatUtil.maybeSetInteger(r0, "profile", ((Integer) r1.first).intValue());
    L7:
        r0.setInteger("max-width", r7.width);
        r0.setInteger("max-height", r7.height);
        MediaFormatUtil.maybeSetInteger(r0, "max-input-size", r7.inputSize);
        if (Util.SDK_INT < 23) goto L13;
        r0.setInteger("priority", 0);
        if (r8 == (-1.0f)) goto L13;
        r0.setFloat("operating-rate", r8);
    L13:
        if (r9 == false) goto L15;
        r0.setInteger("no-post-process", 1);
        r0.setInteger("auto-frc", 0);
    L15:
        if (r10 == 0) goto L18;
        r0.setFeatureEnabled("tunneled-playback", true);
        r0.setInteger("audio-session-id", r10);
    L18:
        if (Util.SDK_INT < 35) goto L20;
        r0.setInteger("importance", Math.max(0, -this.rendererPriority));
    L20:
        return r0;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public String getName() {
        return TAG;
    }

    protected Surface getSurface() {
        return this.displaySurface;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void handleInputBufferSupplementalData(DecoderInputBuffer r10) throws ExoPlaybackException {
        if (this.codecHandlesHdr10PlusOutOfBandMetadata == true) goto L5;
        return;
    L5:
        ByteBuffer r0 = (ByteBuffer) Assertions.checkNotNull(r10.supplementalData);
        if (r0.remaining() < 7) goto L19;
        byte r1 = r0.get();
        short r2 = r0.getShort();
        short r3 = r0.getShort();
        byte r4 = r0.get();
        byte r5 = r0.get();
        r0.position(0);
        if (r1 == (-75)) goto L10;
        return;
    L10:
        if (r2 == 60) goto L12;
        return;
    L12:
        if (r3 == 1) goto L14;
        return;
    L14:
        if (r4 != 4) goto L23;
        if (r5 == 0) goto L17;
        if (r5 == 1) goto L17;
        return;
    L17:
        byte[] r7 = new byte[r0.remaining()];
        r0.get(r7);
        r0.position(0);
        setHdr10PlusInfoV29((MediaCodecAdapter) Assertions.checkNotNull(getCodec()), r7);
        return;
    L23:
        return;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.PlayerMessage.Target
    public void handleMessage(int r4, Object r5) throws ExoPlaybackException {
        switch(r4) {
            case 1: goto L28;
            case 4: goto L25;
            case 5: goto L21;
            case 7: goto L18;
            case 10: goto L13;
            case 13: goto L12;
            case 14: goto L5;
            case 16: goto L4;
            default: goto L3;
        };
    L3:
        super.handleMessage(r4, r5);
        return;
    L4:
        this.rendererPriority = ((Integer) Assertions.checkNotNull(r5)).intValue();
        updateCodecImportance();
        return;
    L5:
        Size r0 = (Size) Assertions.checkNotNull(r5);
        if (r0.getWidth() != 0) goto L8;
        return;
    L8:
        if (r0.getHeight() == 0) goto L33;
        this.outputResolution = r0;
        if (this.videoSink == null) goto L34;
        this.videoSink.setOutputSurfaceInfo((Surface) Assertions.checkStateNotNull(this.displaySurface), r0);
        return;
    L34:
        return;
    L33:
        return;
    L12:
        setVideoEffects((List) Assertions.checkNotNull(r5));
        return;
    L13:
        int r1 = ((Integer) Assertions.checkNotNull(r5)).intValue();
        if (this.tunnelingAudioSessionId == r1) goto L37;
        this.tunnelingAudioSessionId = r1;
        if (this.tunneling == false) goto L38;
        releaseCodec();
        return;
    L38:
        return;
    L37:
        return;
    L18:
        this.frameMetadataListener = (VideoFrameMetadataListener) Assertions.checkNotNull(r5);
        if (this.videoSink == null) goto L40;
        this.videoSink.setVideoFrameMetadataListener(this.frameMetadataListener);
        return;
    L40:
        return;
    L21:
        this.changeFrameRateStrategy = ((Integer) Assertions.checkNotNull(r5)).intValue();
        if (this.videoSink == null) goto L24;
        this.videoSink.setChangeFrameRateStrategy(this.changeFrameRateStrategy);
        return;
    L24:
        this.videoFrameReleaseControl.setChangeFrameRateStrategy(this.changeFrameRateStrategy);
        return;
    L25:
        this.scalingMode = ((Integer) Assertions.checkNotNull(r5)).intValue();
        MediaCodecAdapter r2 = getCodec();
        if (r2 == null) goto L44;
        r2.setVideoScalingMode(this.scalingMode);
        return;
    L44:
        return;
    L28:
        setOutput(r5);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.Renderer
    public boolean isEnded() {
        if (super.isEnded() == true) goto L5;
    L9:
        return false;
    L5:
        if (this.videoSink != null) goto L7;
    L8:
        return true;
    L7:
        if (this.videoSink.isEnded() == false) goto L9;
        goto L8
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.Renderer
    public boolean isReady() {
        boolean r0 = super.isReady();
        if (this.videoSink != null) goto L5;
        if (r0 == false) goto L16;
        if (getCodec() != null) goto L10;
        return true;
    L10:
        if (this.displaySurface != null) goto L12;
        return true;
    L12:
        if (this.tunneling == false) goto L16;
        return true;
    L16:
        return this.videoFrameReleaseControl.isReady(r0);
    L5:
        return this.videoSink.isReady(r0);
    }

    protected boolean maybeDropBuffersToKeyframe(long r7, boolean r9) throws ExoPlaybackException {
        int r0 = skipSource(r7);
        if (r0 != 0) goto L6;
        return false;
    L6:
        if (r9 == false) goto L8;
        this.decoderCounters.skippedInputBufferCount += r0;
        this.decoderCounters.skippedOutputBufferCount += this.buffersInCodecCount;
    L9:
        flushOrReinitializeCodec();
        if (this.videoSink == null) goto L12;
        this.videoSink.flush(false);
    L12:
        return true;
    L8:
        this.decoderCounters.droppedToKeyframeCount++;
        updateDroppedBufferCounters(r0, this.buffersInCodecCount);
        goto L9
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onCodecError(Exception r3) {
        Log.m45e(TAG, "Video codec error", r3);
        this.eventDispatcher.videoCodecError(r3);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onCodecInitialized(String r7, MediaCodecAdapter.Configuration r8, long r9, long r11) {
        this.eventDispatcher.decoderInitialized(r7, r9, r11);
        this.codecNeedsSetOutputSurfaceWorkaround = codecNeedsSetOutputSurfaceWorkaround(r7);
        this.codecHandlesHdr10PlusOutOfBandMetadata = ((MediaCodecInfo) Assertions.checkNotNull(getCodecInfo())).isHdr10PlusOutOfBandMetadataSupported();
        maybeSetupTunnelingForFirstFrame();
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onCodecReleased(String r2) {
        this.eventDispatcher.decoderReleased(r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer
    protected void onDisabled() {
        this.reportedVideoSize = null;
        this.periodDurationUs = C0180C.TIME_UNSET;
        if (this.videoSink == null) goto L5;
        this.videoSink.onRendererDisabled();
    L6:
        maybeSetupTunnelingForFirstFrame();
        this.haveReportedFirstFrameRenderedForCurrentSurface = false;
        this.tunnelingOnFrameRenderedListener = null;
        super.onDisabled();     // Catch: Throwable -> L10
        this.eventDispatcher.disabled(this.decoderCounters);
        this.eventDispatcher.videoSizeChanged(VideoSize.UNKNOWN);
        return;
    L10:
        th = move-exception;
        this.eventDispatcher.disabled(this.decoderCounters);
        this.eventDispatcher.videoSizeChanged(VideoSize.UNKNOWN);
        throw th;
    L5:
        this.videoFrameReleaseControl.onDisabled();
        goto L6
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer
    protected void onEnabled(boolean r6, boolean r7) throws ExoPlaybackException {
        super.onEnabled(r6, r7);
        boolean r0 = getConfiguration().tunneling;
        if (r0 == true) goto L5;
    L8:
        boolean r2 = true;
    L9:
        Assertions.checkState(r2);
        if (this.tunneling == r0) goto L12;
        this.tunneling = r0;
        releaseCodec();
    L12:
        this.eventDispatcher.enabled(this.decoderCounters);
        if (this.hasSetVideoSink == true) goto L21;
        if (this.videoEffects != null) goto L17;
    L19:
        this.hasSetVideoSink = true;
        goto L21
    L17:
        if (this.videoSink != null) goto L19;
        this.videoSink = new PlaybackVideoGraphWrapper.Builder(this.context, this.videoFrameReleaseControl).setClock(getClock()).build().getSink();
    L21:
        if (this.videoSink == null) goto L34;
        this.videoSink.setListener(new C03811(this), MoreExecutors.directExecutor());
        if (this.frameMetadataListener == null) goto L26;
        this.videoSink.setVideoFrameMetadataListener(this.frameMetadataListener);
    L26:
        if (this.displaySurface != null) goto L28;
    L30:
        this.videoSink.setChangeFrameRateStrategy(this.changeFrameRateStrategy);
        this.videoSink.setPlaybackSpeed(getPlaybackSpeed());
        if (this.videoEffects == null) goto L33;
        this.videoSink.setVideoEffects(this.videoEffects);
    L33:
        this.videoSink.onRendererEnabled(r7);
        return;
    L28:
        if (this.outputResolution.equals(Size.UNKNOWN) == true) goto L30;
        this.videoSink.setOutputSurfaceInfo(this.displaySurface, this.outputResolution);
        goto L30
    L34:
        this.videoFrameReleaseControl.setClock(getClock());
        this.videoFrameReleaseControl.onEnabled(r7);
        return;
    L5:
        if (this.tunnelingAudioSessionId != 0) goto L8;
        r2 = false;
        goto L9
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onInit() {
        super.onInit();
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected DecoderReuseEvaluation onInputFormatChanged(FormatHolder r4) throws ExoPlaybackException {
        DecoderReuseEvaluation r0 = super.onInputFormatChanged(r4);
        this.eventDispatcher.inputFormatChanged((Format) Assertions.checkNotNull(r4.format), r0);
        return r0;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onOutputFormatChanged(Format r9, MediaFormat r10) {
        MediaCodecAdapter r0 = getCodec();
        if (r0 == null) goto L6;
        r0.setVideoScalingMode(this.scalingMode);
    L6:
        if (this.tunneling == false) goto L8;
        int r1 = r9.width;
        int r4 = r9.height;
    L26:
        float r5 = r9.pixelWidthHeightRatio;
        if (r9.rotationDegrees != 90) goto L29;
    L30:
        int r6 = r1;
        r1 = r4;
        r4 = r6;
        r5 = 1.0f / r5;
    L31:
        this.decodedVideoSize = new VideoSize(r1, r4, r5);
        if (this.videoSink != null) goto L34;
    L36:
        this.videoFrameReleaseControl.setFrameRate(r9.frameRate);
    L37:
        this.pendingVideoSinkInputStreamChange = false;
        return;
    L34:
        if (this.pendingVideoSinkInputStreamChange == false) goto L36;
        onReadyToChangeVideoSinkInputStream();
        this.videoSink.onInputStreamChanged(1, r9.buildUpon().setWidth(r1).setHeight(r4).setPixelWidthHeightRatio(r5).build());
        goto L37
    L29:
        if (r9.rotationDegrees != 270) goto L31;
    L8:
        Assertions.checkNotNull(r10);
        if (r10.containsKey(KEY_CROP_RIGHT) == true) goto L11;
    L17:
        boolean r7 = false;
    L18:
        if (r7 == false) goto L20;
        r1 = (r10.getInteger(KEY_CROP_RIGHT) - r10.getInteger(KEY_CROP_LEFT)) + 1;
    L22:
        if (r7 == false) goto L24;
        int r8 = (r10.getInteger(KEY_CROP_BOTTOM) - r10.getInteger(KEY_CROP_TOP)) + 1;
    L25:
        r4 = r8;
        goto L26
    L24:
        r8 = r10.getInteger(StreamInformation.KEY_HEIGHT);
        goto L25
    L20:
        r1 = r10.getInteger(StreamInformation.KEY_WIDTH);
        goto L22
    L11:
        if (r10.containsKey(KEY_CROP_LEFT) == false) goto L17;
        if (r10.containsKey(KEY_CROP_BOTTOM) == false) goto L17;
        if (r10.containsKey(KEY_CROP_TOP) == false) goto L17;
        r7 = true;
        goto L18
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer
    protected void onPositionReset(long r12, boolean r14) throws ExoPlaybackException {
        if (this.videoSink == null) goto L5;
        this.videoSink.flush(true);
        this.videoSink.setStreamTimestampInfo(getOutputStreamStartPositionUs(), getOutputStreamOffsetUs(), getBufferTimestampAdjustmentUs(), getLastResetPositionUs());
        this.pendingVideoSinkInputStreamChange = true;
    L5:
        super.onPositionReset(r12, r14);
        if (this.videoSink != null) goto L9;
        this.videoFrameReleaseControl.reset();
    L9:
        if (r14 == true) goto L11;
    L14:
        maybeSetupTunnelingForFirstFrame();
        this.consecutiveDroppedFrameCount = 0;
        return;
    L11:
        if (this.videoSink == null) goto L13;
        this.videoSink.join(false);
        goto L14
    L13:
        this.videoFrameReleaseControl.join(false);
        goto L14
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onProcessedOutputBuffer(long r2) {
        super.onProcessedOutputBuffer(r2);
        if (this.tunneling == true) goto L6;
        this.buffersInCodecCount--;
        return;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onProcessedStreamChange() {
        super.onProcessedStreamChange();
        if (this.videoSink == null) goto L5;
        this.videoSink.setStreamTimestampInfo(getOutputStreamStartPositionUs(), getOutputStreamOffsetUs(), getBufferTimestampAdjustmentUs(), getLastResetPositionUs());
    L6:
        this.pendingVideoSinkInputStreamChange = true;
        maybeSetupTunnelingForFirstFrame();
        return;
    L5:
        this.videoFrameReleaseControl.onProcessedStreamChange();
        goto L6
    }

    protected void onProcessedTunneledBuffer(long r3) throws ExoPlaybackException {
        updateOutputFormatForTime(r3);
        maybeNotifyVideoSizeChanged(this.decodedVideoSize);
        this.decoderCounters.renderedOutputBufferCount++;
        maybeNotifyRenderedFirstFrame();
        onProcessedOutputBuffer(r3);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onQueueInputBuffer(DecoderInputBuffer r3) throws ExoPlaybackException {
        if (this.tunneling == true) goto L6;
        this.buffersInCodecCount++;
    L6:
        if (Util.SDK_INT < 23) goto L8;
        return;
    L8:
        if (this.tunneling == false) goto L12;
        onProcessedTunneledBuffer(r3.timeUs);
        return;
    }

    protected void onReadyToChangeVideoSinkInputStream() {
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onReadyToInitializeCodec(Format r3) throws ExoPlaybackException {
        if (this.videoSink != null) goto L5;
        return;
    L5:
        if (this.videoSink.isInitialized() == true) goto L14;
        this.videoSink.initialize(r3);     // Catch: VideoSink.VideoSinkException -> L8
        return;
    L8:
        e = move-exception;
        throw createRendererException(e, r3, 7000);
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onRelease() {
        super.onRelease();
        if (this.videoSink != null) goto L5;
        return;
    L5:
        if (this.ownsVideoSink == false) goto L9;
        this.videoSink.release();
        return;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer
    protected void onReset() {
        super.onReset();     // Catch: Throwable -> L6
        this.hasSetVideoSink = false;
        this.startPositionUs = C0180C.TIME_UNSET;
        releasePlaceholderSurface();
        return;
    L6:
        th = move-exception;
        this.hasSetVideoSink = false;
        this.startPositionUs = C0180C.TIME_UNSET;
        releasePlaceholderSurface();
        throw th;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer
    protected void onStarted() {
        super.onStarted();
        this.droppedFrames = 0;
        this.droppedFrameAccumulationStartTimeMs = getClock().elapsedRealtime();
        this.totalVideoFrameProcessingOffsetUs = 0;
        this.videoFrameProcessingOffsetCount = 0;
        if (this.videoSink == null) goto L5;
        this.videoSink.onRendererStarted();
        return;
    L5:
        this.videoFrameReleaseControl.onStarted();
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer
    protected void onStopped() {
        maybeNotifyDroppedFrames();
        maybeNotifyVideoFrameProcessingOffset();
        if (this.videoSink == null) goto L5;
        this.videoSink.onRendererStopped();
    L6:
        super.onStopped();
        return;
    L5:
        this.videoFrameReleaseControl.onStopped();
        goto L6
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer
    protected void onStreamChanged(Format[] r6, long r7, long r9, MediaSource.MediaPeriodId r11) throws ExoPlaybackException {
        super.onStreamChanged(r6, r7, r9, r11);
        if (this.startPositionUs != C0180C.TIME_UNSET) goto L5;
        this.startPositionUs = r7;
    L5:
        updatePeriodDurationUs(r11);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected boolean processOutputBuffer(long r27, long r29, final MediaCodecAdapter r31, ByteBuffer r32, final int r33, int r34, int r35, long r36, boolean r38, boolean r39, Format r40) throws ExoPlaybackException {
        Assertions.checkNotNull(r31);
        final long r4 = r36 - getOutputStreamOffsetUs();
        if (this.videoSink == null) goto L19;
        long r11 = r36 + getBufferTimestampAdjustmentUs();
        return this.videoSink.handleInputFrame(r11, r39, r27, r29, new C03822(this, r31, r33, r4));
    L11:
        e = e;
    L18:
        throw createRendererException(e, e.format, PlaybackException.ERROR_CODE_VIDEO_FRAME_PROCESSING_FAILED);
    L13:
        e = e;
    L15:
        e = e;
        goto L18
    L19:
        int r0 = this.videoFrameReleaseControl.getFrameReleaseAction(r36, r27, r29, getOutputStreamStartPositionUs(), r39, this.videoFrameReleaseInfo);
        if (r0 != 4) goto L23;
        return false;
    L23:
        if (r38 == false) goto L28;
        if (r39 == true) goto L28;
        skipOutputBuffer(r31, r33, r4);
        return true;
    L28:
        if (this.displaySurface == null) goto L30;
        switch(r0) {
            case 0: goto L44;
            case 1: goto L42;
            case 2: goto L40;
            case 3: goto L38;
            case 4: goto L36;
            case 5: goto L37;
            default: goto L36;
        };
    L37:
        return false;
    L38:
        skipOutputBuffer(r31, r33, r4);
        updateVideoFrameProcessingOffsetCounters(this.videoFrameReleaseInfo.getEarlyUs());
        return true;
    L40:
        dropOutputBuffer(r31, r33, r4);
        updateVideoFrameProcessingOffsetCounters(this.videoFrameReleaseInfo.getEarlyUs());
        return true;
    L42:
        releaseFrame((MediaCodecAdapter) Assertions.checkStateNotNull(r31), r33, r4, r40);
        return true;
    L44:
        long r1 = getClock().nanoTime();
        notifyFrameMetadataListener(r4, r1, r40);
        renderOutputBuffer(r31, r33, r4, r1);
        updateVideoFrameProcessingOffsetCounters(this.videoFrameReleaseInfo.getEarlyUs());
        return true;
    L36:
        throw new IllegalStateException(String.valueOf(r0));
    L30:
        if (this.videoFrameReleaseInfo.getEarlyUs() >= 30000) goto L33;
        skipOutputBuffer(r31, r33, r4);
        updateVideoFrameProcessingOffsetCounters(this.videoFrameReleaseInfo.getEarlyUs());
        return true;
    L33:
        return false;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.Renderer
    public void render(long r4, long r6) throws ExoPlaybackException {
        super.render(r4, r6);
        if (this.videoSink != null) goto L10;
        return;
    L10:
        this.videoSink.render(r4, r6);     // Catch: VideoSink.VideoSinkException -> L6
        return;
    L6:
        e = move-exception;
        throw createRendererException(e, e.format, PlaybackException.ERROR_CODE_VIDEO_FRAME_PROCESSING_FAILED);
    }

    @Deprecated
    protected void renderOutputBuffer(MediaCodecAdapter r4, int r5, long r6) {
        TraceUtil.beginSection("releaseOutputBuffer");
        r4.releaseOutputBuffer(r5, true);
        TraceUtil.endSection();
        this.decoderCounters.renderedOutputBufferCount++;
        this.consecutiveDroppedFrameCount = 0;
        if (this.videoSink != null) goto L6;
        maybeNotifyVideoSizeChanged(this.decodedVideoSize);
        maybeNotifyRenderedFirstFrame();
        return;
    }

    protected void renderOutputBufferV21(MediaCodecAdapter r3, int r4, long r5, long r7) {
        TraceUtil.beginSection("releaseOutputBuffer");
        r3.releaseOutputBuffer(r4, r7);
        TraceUtil.endSection();
        this.decoderCounters.renderedOutputBufferCount++;
        this.consecutiveDroppedFrameCount = 0;
        if (this.videoSink != null) goto L6;
        maybeNotifyVideoSizeChanged(this.decodedVideoSize);
        maybeNotifyRenderedFirstFrame();
        return;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void resetCodecStateForFlush() {
        super.resetCodecStateForFlush();
        this.buffersInCodecCount = 0;
    }

    protected void setOutputSurfaceV23(MediaCodecAdapter r1, Surface r2) {
        r1.setOutputSurface(r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.Renderer
    public void setPlaybackSpeed(float r2, float r3) throws ExoPlaybackException {
        super.setPlaybackSpeed(r2, r3);
        if (this.videoSink == null) goto L5;
        this.videoSink.setPlaybackSpeed(r2);
        return;
    L5:
        this.videoFrameReleaseControl.setPlaybackSpeed(r2);
    }

    public void setVideoEffects(List<Effect> r2) {
        this.videoEffects = r2;
        if (this.videoSink == null) goto L6;
        this.videoSink.setVideoEffects(r2);
        return;
    }

    protected boolean shouldDropBuffersToKeyframe(long r3, long r5, boolean r7) {
        if (r3 >= MIN_EARLY_US_VERY_LATE_THRESHOLD) goto L6;
        if (r7 == true) goto L6;
        return true;
    L6:
        return false;
    }

    @Override // androidx.media3.exoplayer.video.VideoFrameReleaseControl.FrameTimingEvaluator
    public boolean shouldDropFrame(long r2, long r4, boolean r6) {
        return shouldDropOutputBuffer(r2, r4, r6);
    }

    protected boolean shouldDropOutputBuffer(long r3, long r5, boolean r7) {
        if (r3 >= MIN_EARLY_US_LATE_THRESHOLD) goto L6;
        if (r7 == true) goto L6;
        return true;
    L6:
        return false;
    }

    @Override // androidx.media3.exoplayer.video.VideoFrameReleaseControl.FrameTimingEvaluator
    public boolean shouldForceReleaseFrame(long r2, long r4) {
        return shouldForceRenderOutputBuffer(r2, r4);
    }

    protected boolean shouldForceRenderOutputBuffer(long r3, long r5) {
        if (r3 < MIN_EARLY_US_LATE_THRESHOLD) goto L5;
    L7:
        return false;
    L5:
        if (r5 <= 100000) goto L7;
        return true;
    }

    @Override // androidx.media3.exoplayer.video.VideoFrameReleaseControl.FrameTimingEvaluator
    public boolean shouldIgnoreFrame(long r7, long r9, long r11, boolean r13, boolean r14) throws ExoPlaybackException {
        if (shouldDropBuffersToKeyframe(r7, r11, r13) == true) goto L5;
    L7:
        return false;
    L5:
        if (maybeDropBuffersToKeyframe(r9, r14) == false) goto L7;
        return true;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected boolean shouldInitCodec(MediaCodecInfo r2) {
        return hasSurfaceForCodec(r2);
    }

    protected boolean shouldSkipBuffersWithIdenticalReleaseTime() {
        return true;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected boolean shouldSkipDecoderInputBuffer(DecoderInputBuffer r3) {
        if (r3.notDependedOn() == true) goto L6;
        return false;
    L6:
        if (isBufferProbablyLastSample(r3) == false) goto L9;
        return false;
    L9:
        if (r3.isEncrypted() == false) goto L12;
        return false;
    L12:
        return isBufferBeforeStartTime(r3);
    }

    protected boolean shouldUseDetachedSurface(MediaCodecInfo r3) {
        if (Util.SDK_INT >= 35) goto L5;
    L7:
        return false;
    L5:
        if (r3.detachedSurfaceSupported == false) goto L7;
        return true;
    }

    protected void skipOutputBuffer(MediaCodecAdapter r3, int r4, long r5) {
        TraceUtil.beginSection("skipVideoBuffer");
        r3.releaseOutputBuffer(r4, false);
        TraceUtil.endSection();
        this.decoderCounters.skippedOutputBufferCount++;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected int supportsFormat(MediaCodecSelector r2, Format r3) throws MediaCodecUtil.DecoderQueryException {
        return supportsFormatInternal(this.context, r2, r3);
    }

    protected void updateDroppedBufferCounters(int r5, int r6) {
        this.decoderCounters.droppedInputBufferCount += r5;
        int r0 = r5 + r6;
        this.decoderCounters.droppedBufferCount += r0;
        this.droppedFrames += r0;
        this.consecutiveDroppedFrameCount += r0;
        this.decoderCounters.maxConsecutiveDroppedBufferCount = Math.max(this.consecutiveDroppedFrameCount, this.decoderCounters.maxConsecutiveDroppedBufferCount);
        if (this.maxDroppedFramesToNotify > 0) goto L5;
        return;
    L5:
        if (this.droppedFrames < this.maxDroppedFramesToNotify) goto L9;
        maybeNotifyDroppedFrames();
        return;
    }

    protected void updateVideoFrameProcessingOffsetCounters(long r3) {
        this.decoderCounters.addVideoFrameProcessingOffset(r3);
        this.totalVideoFrameProcessingOffsetUs += r3;
        this.videoFrameProcessingOffsetCount++;
    }
}
