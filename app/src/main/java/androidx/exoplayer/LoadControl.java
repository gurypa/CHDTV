package androidx.media3.exoplayer;

import androidx.media3.common.Timeline;
import androidx.media3.common.util.Log;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.Allocator;

/* JADX INFO: loaded from: classes.dex */
public interface LoadControl {

    @Deprecated
    public static final MediaSource.MediaPeriodId EMPTY_MEDIA_PERIOD_ID = null;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.LoadControl$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        @Deprecated
        public static long $default$getBackBufferDurationUs(LoadControl r2) {
            throw new IllegalStateException("getBackBufferDurationUs not implemented");
        }

        public static long $default$getBackBufferDurationUs(LoadControl r2, PlayerId r3) {
            return r2.getBackBufferDurationUs();
        }

        @Deprecated
        public static void $default$onPrepared(LoadControl r2) {
            throw new IllegalStateException("onPrepared not implemented");
        }

        public static void $default$onPrepared(LoadControl r0, PlayerId r1) {
            r0.onPrepared();
        }

        @Deprecated
        public static void $default$onReleased(LoadControl r2) {
            throw new IllegalStateException("onReleased not implemented");
        }

        public static void $default$onReleased(LoadControl r0, PlayerId r1) {
            r0.onReleased();
        }

        @Deprecated
        public static void $default$onStopped(LoadControl r2) {
            throw new IllegalStateException("onStopped not implemented");
        }

        public static void $default$onStopped(LoadControl r0, PlayerId r1) {
            r0.onStopped();
        }

        @Deprecated
        public static void $default$onTracksSelected(LoadControl r0, Timeline r1, MediaSource.MediaPeriodId r2, Renderer[] r3, TrackGroupArray r4, ExoTrackSelection[] r5) {
            r0.onTracksSelected(r3, r4, r5);
        }

        public static void $default$onTracksSelected(LoadControl r2, Parameters r3, TrackGroupArray r4, ExoTrackSelection[] r5) {
            throw new IllegalStateException("onTracksSelected not implemented");
        }

        @Deprecated
        public static void $default$onTracksSelected(LoadControl r6, PlayerId r7, Timeline r8, MediaSource.MediaPeriodId r9, Renderer[] r10, TrackGroupArray r11, ExoTrackSelection[] r12) {
            r6.onTracksSelected(r8, r9, r10, r11, r12);
        }

        @Deprecated
        public static void $default$onTracksSelected(LoadControl r2, Renderer[] r3, TrackGroupArray r4, ExoTrackSelection[] r5) {
            throw new IllegalStateException("onTracksSelected not implemented");
        }

        @Deprecated
        public static boolean $default$retainBackBufferFromKeyframe(LoadControl r2) {
            throw new IllegalStateException("retainBackBufferFromKeyframe not implemented");
        }

        public static boolean $default$retainBackBufferFromKeyframe(LoadControl r1, PlayerId r2) {
            return r1.retainBackBufferFromKeyframe();
        }

        @Deprecated
        public static boolean $default$shouldContinueLoading(LoadControl r2, long r3, long r5, float r7) {
            throw new IllegalStateException("shouldContinueLoading not implemented");
        }

        public static boolean $default$shouldContinueLoading(LoadControl r6, Parameters r7) {
            return r6.shouldContinueLoading(r7.playbackPositionUs, r7.bufferedDurationUs, r7.playbackSpeed);
        }

        public static boolean $default$shouldContinuePreloading(LoadControl r2, Timeline r3, MediaSource.MediaPeriodId r4, long r5) {
            Log.m48w("LoadControl", "shouldContinuePreloading needs to be implemented when playlist preloading is enabled");
            return false;
        }

        @Deprecated
        public static boolean $default$shouldStartPlayback(LoadControl r2, long r3, float r5, boolean r6, long r7) {
            throw new IllegalStateException("shouldStartPlayback not implemented");
        }

        @Deprecated
        public static boolean $default$shouldStartPlayback(LoadControl r7, Timeline r8, MediaSource.MediaPeriodId r9, long r10, float r12, boolean r13, long r14) {
            return r7.shouldStartPlayback(r10, r12, r13, r14);
        }

        public static boolean $default$shouldStartPlayback(LoadControl r9, Parameters r10) {
            return r9.shouldStartPlayback(r10.timeline, r10.mediaPeriodId, r10.bufferedDurationUs, r10.playbackSpeed, r10.rebuffering, r10.targetLiveOffsetUs);
        }
    }

    public static final class Parameters {
        public final long bufferedDurationUs;
        public final MediaSource.MediaPeriodId mediaPeriodId;
        public final boolean playWhenReady;
        public final long playbackPositionUs;
        public final float playbackSpeed;
        public final PlayerId playerId;
        public final boolean rebuffering;
        public final long targetLiveOffsetUs;
        public final Timeline timeline;

        public Parameters(PlayerId r1, Timeline r2, MediaSource.MediaPeriodId r3, long r4, long r6, float r8, boolean r9, boolean r10, long r11) {
            this.playerId = r1;
            this.timeline = r2;
            this.mediaPeriodId = r3;
            this.playbackPositionUs = r4;
            this.bufferedDurationUs = r6;
            this.playbackSpeed = r8;
            this.playWhenReady = r9;
            this.rebuffering = r10;
            this.targetLiveOffsetUs = r11;
        }
    }

    static {
        EMPTY_MEDIA_PERIOD_ID = new MediaSource.MediaPeriodId(new Object());
    }

    Allocator getAllocator();

    @Deprecated
    long getBackBufferDurationUs();

    long getBackBufferDurationUs(PlayerId r1);

    @Deprecated
    void onPrepared();

    void onPrepared(PlayerId r1);

    @Deprecated
    void onReleased();

    void onReleased(PlayerId r1);

    @Deprecated
    void onStopped();

    void onStopped(PlayerId r1);

    @Deprecated
    void onTracksSelected(Timeline r1, MediaSource.MediaPeriodId r2, Renderer[] r3, TrackGroupArray r4, ExoTrackSelection[] r5);

    void onTracksSelected(Parameters r1, TrackGroupArray r2, ExoTrackSelection[] r3);

    @Deprecated
    void onTracksSelected(PlayerId r1, Timeline r2, MediaSource.MediaPeriodId r3, Renderer[] r4, TrackGroupArray r5, ExoTrackSelection[] r6);

    @Deprecated
    void onTracksSelected(Renderer[] r1, TrackGroupArray r2, ExoTrackSelection[] r3);

    @Deprecated
    boolean retainBackBufferFromKeyframe();

    boolean retainBackBufferFromKeyframe(PlayerId r1);

    @Deprecated
    boolean shouldContinueLoading(long r1, long r3, float r5);

    boolean shouldContinueLoading(Parameters r1);

    boolean shouldContinuePreloading(Timeline r1, MediaSource.MediaPeriodId r2, long r3);

    @Deprecated
    boolean shouldStartPlayback(long r1, float r3, boolean r4, long r5);

    @Deprecated
    boolean shouldStartPlayback(Timeline r1, MediaSource.MediaPeriodId r2, long r3, float r5, boolean r6, long r7);

    boolean shouldStartPlayback(Parameters r1);
}
