package androidx.media3.exoplayer;

import androidx.media3.common.Format;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Clock;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.SampleStream;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public interface Renderer extends PlayerMessage.Target {
    public static final long DEFAULT_DURATION_TO_PROGRESS_US = 10000;
    public static final int MSG_CUSTOM_BASE = 10000;
    public static final int MSG_SET_AUDIO_ATTRIBUTES = 3;
    public static final int MSG_SET_AUDIO_SESSION_ID = 10;
    public static final int MSG_SET_AUX_EFFECT_INFO = 6;
    public static final int MSG_SET_CAMERA_MOTION_LISTENER = 8;
    public static final int MSG_SET_CHANGE_FRAME_RATE_STRATEGY = 5;
    public static final int MSG_SET_IMAGE_OUTPUT = 15;
    public static final int MSG_SET_PREFERRED_AUDIO_DEVICE = 12;
    public static final int MSG_SET_PRIORITY = 16;
    public static final int MSG_SET_SCALING_MODE = 4;
    public static final int MSG_SET_SKIP_SILENCE_ENABLED = 9;
    public static final int MSG_SET_VIDEO_EFFECTS = 13;
    public static final int MSG_SET_VIDEO_FRAME_METADATA_LISTENER = 7;
    public static final int MSG_SET_VIDEO_OUTPUT = 1;
    public static final int MSG_SET_VIDEO_OUTPUT_RESOLUTION = 14;
    public static final int MSG_SET_VOLUME = 2;
    public static final int MSG_SET_WAKEUP_LISTENER = 11;
    public static final int STATE_DISABLED = 0;
    public static final int STATE_ENABLED = 1;
    public static final int STATE_STARTED = 2;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.Renderer$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$enableMayRenderStartOfStream(Renderer r0) {
        }

        public static long $default$getDurationToProgressUs(Renderer r2, long r3, long r5) {
            return Renderer.DEFAULT_DURATION_TO_PROGRESS_US;
        }

        public static void $default$release(Renderer r0) {
        }

        public static void $default$setPlaybackSpeed(Renderer r0, float r1, float r2) throws ExoPlaybackException {
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageType {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    public interface WakeupListener {
        void onSleep();

        void onWakeup();
    }

    void disable();

    void enable(RendererConfiguration r1, Format[] r2, SampleStream r3, long r4, boolean r6, boolean r7, long r8, long r10, MediaSource.MediaPeriodId r12) throws ExoPlaybackException;

    void enableMayRenderStartOfStream();

    RendererCapabilities getCapabilities();

    long getDurationToProgressUs(long r1, long r3);

    MediaClock getMediaClock();

    String getName();

    long getReadingPositionUs();

    int getState();

    SampleStream getStream();

    int getTrackType();

    boolean hasReadStreamToEnd();

    void init(int r1, PlayerId r2, Clock r3);

    boolean isCurrentStreamFinal();

    boolean isEnded();

    boolean isReady();

    void maybeThrowStreamError() throws IOException;

    void release();

    void render(long r1, long r3) throws ExoPlaybackException;

    void replaceStream(Format[] r1, SampleStream r2, long r3, long r5, MediaSource.MediaPeriodId r7) throws ExoPlaybackException;

    void reset();

    void resetPosition(long r1) throws ExoPlaybackException;

    void setCurrentStreamFinal();

    void setPlaybackSpeed(float r1, float r2) throws ExoPlaybackException;

    void setTimeline(Timeline r1);

    void start() throws ExoPlaybackException;

    void stop();
}
