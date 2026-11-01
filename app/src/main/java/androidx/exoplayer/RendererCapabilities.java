package androidx.media3.exoplayer;

import androidx.media3.common.Format;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public interface RendererCapabilities {
    public static final int ADAPTIVE_NOT_SEAMLESS = 8;
    public static final int ADAPTIVE_NOT_SUPPORTED = 0;
    public static final int ADAPTIVE_SEAMLESS = 16;
    public static final int ADAPTIVE_SUPPORT_MASK = 24;
    public static final int AUDIO_OFFLOAD_GAPLESS_SUPPORTED = 1024;
    public static final int AUDIO_OFFLOAD_NOT_SUPPORTED = 0;
    public static final int AUDIO_OFFLOAD_SPEED_CHANGE_SUPPORTED = 2048;
    public static final int AUDIO_OFFLOAD_SUPPORTED = 512;
    public static final int AUDIO_OFFLOAD_SUPPORT_MASK = 3584;
    public static final int DECODER_SUPPORT_FALLBACK = 0;
    public static final int DECODER_SUPPORT_FALLBACK_MIMETYPE = 256;
    public static final int DECODER_SUPPORT_MASK = 384;
    public static final int DECODER_SUPPORT_PRIMARY = 128;
    public static final int FORMAT_SUPPORT_MASK = 7;
    public static final int HARDWARE_ACCELERATION_NOT_SUPPORTED = 0;
    public static final int HARDWARE_ACCELERATION_SUPPORTED = 64;
    public static final int HARDWARE_ACCELERATION_SUPPORT_MASK = 64;
    public static final int TUNNELING_NOT_SUPPORTED = 0;
    public static final int TUNNELING_SUPPORTED = 32;
    public static final int TUNNELING_SUPPORT_MASK = 32;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.RendererCapabilities$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$clearListener(RendererCapabilities r0) {
        }

        public static void $default$setListener(RendererCapabilities r0, Listener r1) {
        }

        public static int create(int r1) {
            return create(r1, 0, 0, 0);
        }

        public static int create(int r6, int r7, int r8) {
            return create(r6, r7, r8, 0, 128, 0);
        }

        public static int create(int r6, int r7, int r8, int r9) {
            return create(r6, r7, r8, 0, 128, r9);
        }

        public static int create(int r6, int r7, int r8, int r9, int r10) {
            return create(r6, r7, r8, r9, r10, 0);
        }

        public static int create(int r1, int r2, int r3, int r4, int r5, int r6) {
            return ((((r1 | r2) | r3) | r4) | r5) | r6;
        }

        public static int getAdaptiveSupport(int r1) {
            return r1 & 24;
        }

        public static int getAudioOffloadSupport(int r1) {
            return r1 & RendererCapabilities.AUDIO_OFFLOAD_SUPPORT_MASK;
        }

        public static int getDecoderSupport(int r1) {
            return r1 & RendererCapabilities.DECODER_SUPPORT_MASK;
        }

        public static int getFormatSupport(int r1) {
            return r1 & 7;
        }

        public static int getHardwareAccelerationSupport(int r1) {
            return r1 & 64;
        }

        public static int getTunnelingSupport(int r1) {
            return r1 & 32;
        }

        public static boolean isFormatSupported(int r2, boolean r3) {
            int r0 = getFormatSupport(r2);
            if (r0 == 4) goto L9;
            if (r3 == true) goto L6;
        L8:
            return false;
        L6:
            if (r0 != 3) goto L8;
        L9:
            return true;
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface AdaptiveSupport {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface AudioOffloadSupport {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Capabilities {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface DecoderSupport {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface HardwareAccelerationSupport {
    }

    public interface Listener {
        void onRendererCapabilitiesChanged(Renderer r1);
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface TunnelingSupport {
    }

    void clearListener();

    String getName();

    int getTrackType();

    void setListener(Listener r1);

    int supportsFormat(Format r1) throws ExoPlaybackException;

    int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException;
}
