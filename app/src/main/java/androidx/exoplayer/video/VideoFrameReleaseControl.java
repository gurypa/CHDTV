package androidx.media3.exoplayer.video;

import android.content.Context;
import android.view.Surface;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.ExoPlaybackException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public final class VideoFrameReleaseControl {
    public static final int FRAME_RELEASE_DROP = 2;
    public static final int FRAME_RELEASE_IGNORE = 4;
    public static final int FRAME_RELEASE_IMMEDIATELY = 0;
    public static final int FRAME_RELEASE_SCHEDULED = 1;
    public static final int FRAME_RELEASE_SKIP = 3;
    public static final int FRAME_RELEASE_TRY_AGAIN_LATER = 5;
    private static final long MAX_EARLY_US_THRESHOLD = 50000;
    private final long allowedJoiningTimeMs;
    private Clock clock;
    private int firstFrameState;
    private final VideoFrameReleaseHelper frameReleaseHelper;
    private final FrameTimingEvaluator frameTimingEvaluator;
    private long initialPositionUs;
    private long joiningDeadlineMs;
    private boolean joiningRenderNextFrameImmediately;
    private long lastPresentationTimeUs;
    private long lastReleaseRealtimeUs;
    private float playbackSpeed;
    private boolean started;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface FrameReleaseAction {
    }

    public static class FrameReleaseInfo {
        private long earlyUs;
        private long releaseTimeNs;

        public FrameReleaseInfo() {
            this.earlyUs = C0180C.TIME_UNSET;
            this.releaseTimeNs = C0180C.TIME_UNSET;
        }

        static /* synthetic */ void access$000(FrameReleaseInfo r0) {
            r0.reset();
        }

        static /* synthetic */ long access$100(FrameReleaseInfo r2) {
            return r2.earlyUs;
        }

        static /* synthetic */ long access$102(FrameReleaseInfo r0, long r1) {
            r0.earlyUs = r1;
            return r1;
        }

        static /* synthetic */ long access$200(FrameReleaseInfo r2) {
            return r2.releaseTimeNs;
        }

        static /* synthetic */ long access$202(FrameReleaseInfo r0, long r1) {
            r0.releaseTimeNs = r1;
            return r1;
        }

        private void reset() {
            this.earlyUs = C0180C.TIME_UNSET;
            this.releaseTimeNs = C0180C.TIME_UNSET;
        }

        public long getEarlyUs() {
            return this.earlyUs;
        }

        public long getReleaseTimeNs() {
            return this.releaseTimeNs;
        }
    }

    public interface FrameTimingEvaluator {
        boolean shouldDropFrame(long r1, long r3, boolean r5);

        boolean shouldForceReleaseFrame(long r1, long r3);

        boolean shouldIgnoreFrame(long r1, long r3, long r5, boolean r7, boolean r8) throws ExoPlaybackException;
    }

    public VideoFrameReleaseControl(Context r3, FrameTimingEvaluator r4, long r5) {
        this.frameTimingEvaluator = r4;
        this.allowedJoiningTimeMs = r5;
        this.frameReleaseHelper = new VideoFrameReleaseHelper(r3);
        this.firstFrameState = 0;
        this.initialPositionUs = C0180C.TIME_UNSET;
        this.lastPresentationTimeUs = C0180C.TIME_UNSET;
        this.joiningDeadlineMs = C0180C.TIME_UNSET;
        this.playbackSpeed = 1.0f;
        this.clock = Clock.DEFAULT;
    }

    private long calculateEarlyTimeUs(long r5, long r7, long r9) {
        long r0 = (long) ((r9 - r5) / ((double) this.playbackSpeed));
        if (this.started == true) goto L5;
        return r0;
    L5:
        return r0 - (Util.msToUs(this.clock.elapsedRealtime()) - r7);
    }

    private void lowerFirstFrameState(int r2) {
        this.firstFrameState = Math.min(this.firstFrameState, r2);
    }

    private boolean shouldForceRelease(long r8, long r10, long r12) {
        if (this.joiningDeadlineMs == C0180C.TIME_UNSET) goto L8;
        if (this.joiningRenderNextFrameImmediately == true) goto L8;
        return false;
    L8:
        switch(this.firstFrameState) {
            case 0: goto L24;
            case 1: goto L22;
            case 2: goto L19;
            case 3: goto L11;
            default: goto L10;
        };
    L11:
        long r3 = Util.msToUs(this.clock.elapsedRealtime()) - this.lastReleaseRealtimeUs;
        if (this.started == true) goto L14;
        return false;
    L14:
        if (this.frameTimingEvaluator.shouldForceReleaseFrame(r10, r3) == true) goto L17;
        return false;
    L17:
        return true;
    L22:
        return true;
    L10:
        throw new IllegalStateException();
    L19:
        if (r8 >= r12) goto L21;
        return false;
    L21:
        return true;
    L24:
        return this.started;
    }

    public void allowReleaseFirstFrameBeforeStarted() {
        if (this.firstFrameState != 0) goto L6;
        this.firstFrameState = 1;
        return;
    }

    public int getFrameReleaseAction(long r16, long r18, long r20, long r22, boolean r24, FrameReleaseInfo r25) throws ExoPlaybackException {
        FrameReleaseInfo.access$000(r25);
        if (this.initialPositionUs != C0180C.TIME_UNSET) goto L6;
        this.initialPositionUs = r18;
    L6:
        if (this.lastPresentationTimeUs == r16) goto L8;
        this.frameReleaseHelper.onNextFrame(r16);
        this.lastPresentationTimeUs = r16;
    L8:
        FrameReleaseInfo.access$102(r25, calculateEarlyTimeUs(r18, r20, r16));
        if (shouldForceRelease(r18, FrameReleaseInfo.access$100(r25), r22) == false) goto L12;
        return 0;
    L12:
        if (this.started == true) goto L14;
    L36:
        return 5;
    L14:
        if (r18 == this.initialPositionUs) goto L36;
        long r12 = this.clock.nanoTime();
        FrameReleaseInfo.access$202(r25, this.frameReleaseHelper.adjustReleaseTime((FrameReleaseInfo.access$100(r25) * 1000) + r12));
        FrameReleaseInfo.access$102(r25, (FrameReleaseInfo.access$200(r25) - r12) / 1000);
        if (this.joiningDeadlineMs != C0180C.TIME_UNSET) goto L19;
    L21:
        boolean r8 = false;
    L23:
        if (this.frameTimingEvaluator.shouldIgnoreFrame(FrameReleaseInfo.access$100(r25), r18, r20, r24, r8) == false) goto L27;
        return 4;
    L27:
        if (this.frameTimingEvaluator.shouldDropFrame(FrameReleaseInfo.access$100(r25), r20, r24) == false) goto L33;
        if (r8 == false) goto L30;
        return 3;
    L30:
        return 2;
    L33:
        if (FrameReleaseInfo.access$100(r25) <= MAX_EARLY_US_THRESHOLD) goto L35;
        return 5;
    L35:
        return 1;
    L19:
        if (this.joiningRenderNextFrameImmediately == true) goto L21;
        r8 = true;
        goto L23
    }

    public boolean isReady(boolean r10) {
        if (r10 == false) goto L9;
        if (this.firstFrameState != 3) goto L9;
        this.joiningDeadlineMs = C0180C.TIME_UNSET;
        return true;
    L9:
        if (this.joiningDeadlineMs != C0180C.TIME_UNSET) goto L12;
        return false;
    L12:
        if (this.clock.elapsedRealtime() >= this.joiningDeadlineMs) goto L14;
        return true;
    L14:
        this.joiningDeadlineMs = C0180C.TIME_UNSET;
        return false;
    }

    public void join(boolean r5) {
        this.joiningRenderNextFrameImmediately = r5;
        if (this.allowedJoiningTimeMs <= 0) goto L5;
        long r0 = this.clock.elapsedRealtime() + this.allowedJoiningTimeMs;
    L6:
        this.joiningDeadlineMs = r0;
        return;
    L5:
        r0 = C0180C.TIME_UNSET;
        goto L6
    }

    public void onDisabled() {
        lowerFirstFrameState(0);
    }

    public void onEnabled(boolean r2) {
        if (r2 == false) goto L4;
        int r0 = 1;
    L5:
        this.firstFrameState = r0;
        return;
    L4:
        r0 = 0;
        goto L5
    }

    public boolean onFrameReleasedIsFirstFrame() {
        if (this.firstFrameState == 3) goto L5;
        boolean r0 = true;
    L6:
        this.firstFrameState = 3;
        this.lastReleaseRealtimeUs = Util.msToUs(this.clock.elapsedRealtime());
        return r0;
    L5:
        r0 = false;
        goto L6
    }

    public void onProcessedStreamChange() {
        lowerFirstFrameState(2);
    }

    public void onStarted() {
        this.started = true;
        this.lastReleaseRealtimeUs = Util.msToUs(this.clock.elapsedRealtime());
        this.frameReleaseHelper.onStarted();
    }

    public void onStopped() {
        this.started = false;
        this.joiningDeadlineMs = C0180C.TIME_UNSET;
        this.frameReleaseHelper.onStopped();
    }

    public void reset() {
        this.frameReleaseHelper.onPositionReset();
        this.lastPresentationTimeUs = C0180C.TIME_UNSET;
        this.initialPositionUs = C0180C.TIME_UNSET;
        lowerFirstFrameState(1);
        this.joiningDeadlineMs = C0180C.TIME_UNSET;
    }

    public void setChangeFrameRateStrategy(int r2) {
        this.frameReleaseHelper.setChangeFrameRateStrategy(r2);
    }

    public void setClock(Clock r1) {
        this.clock = r1;
    }

    public void setFrameRate(float r2) {
        this.frameReleaseHelper.onFormatChanged(r2);
    }

    public void setOutputSurface(Surface r2) {
        this.frameReleaseHelper.onSurfaceChanged(r2);
        lowerFirstFrameState(1);
    }

    public void setPlaybackSpeed(float r2) {
        if (r2 <= 0.0f) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        if (r2 != this.playbackSpeed) goto L9;
        return;
    L9:
        this.playbackSpeed = r2;
        this.frameReleaseHelper.onPlaybackSpeed(r2);
        return;
    L5:
        r0 = false;
        goto L6
    }
}
