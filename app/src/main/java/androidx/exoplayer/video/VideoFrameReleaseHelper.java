package androidx.media3.exoplayer.video;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Choreographer;
import android.view.Display;
import android.view.Surface;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class VideoFrameReleaseHelper {
    private static final long MAX_ALLOWED_ADJUSTMENT_NS = 20000000;
    private static final int MINIMUM_FRAMES_WITHOUT_SYNC_TO_CLEAR_SURFACE_FRAME_RATE = 30;
    private static final long MINIMUM_MATCHING_FRAME_DURATION_FOR_HIGH_CONFIDENCE_NS = 5000000000L;
    private static final float MINIMUM_MEDIA_FRAME_RATE_CHANGE_FOR_UPDATE_HIGH_CONFIDENCE = 0.02f;
    private static final float MINIMUM_MEDIA_FRAME_RATE_CHANGE_FOR_UPDATE_LOW_CONFIDENCE = 1.0f;
    private static final String TAG = "VideoFrameReleaseHelper";
    private static final long VSYNC_OFFSET_PERCENTAGE = 80;
    private static final long VSYNC_SAMPLE_UPDATE_PERIOD_MS = 500;
    private int changeFrameRateStrategy;
    private final DisplayHelper displayHelper;
    private float formatFrameRate;
    private long frameIndex;
    private final FixedFrameRateEstimator frameRateEstimator;
    private long lastAdjustedFrameIndex;
    private long lastAdjustedReleaseTimeNs;
    private long pendingLastAdjustedFrameIndex;
    private long pendingLastAdjustedReleaseTimeNs;
    private float playbackSpeed;
    private boolean started;
    private Surface surface;
    private float surfaceMediaFrameRate;
    private float surfacePlaybackFrameRate;
    private long vsyncDurationNs;
    private long vsyncOffsetNs;
    private final VSyncSampler vsyncSampler;

    private static final class Api30 {
        private Api30() {
        }

        public static void setSurfaceFrameRate(Surface r4, float r5) {
            if (r5 != 0.0f) goto L5;
            int r0 = 0;
        L12:
            r4.setFrameRate(r5, r0);     // Catch: IllegalStateException -> L9
            return;
        L9:
            e = move-exception;
            Log.m45e(VideoFrameReleaseHelper.TAG, "Failed to call Surface.setFrameRate", e);
            return;
        L5:
            r0 = 1;
            goto L12
        }
    }

    private final class DisplayHelper implements DisplayManager.DisplayListener {
        private final DisplayManager displayManager;
        final /* synthetic */ VideoFrameReleaseHelper this$0;

        public DisplayHelper(VideoFrameReleaseHelper r1, DisplayManager r2) {
            this.this$0 = r1;
            this.displayManager = r2;
        }

        private Display getDefaultDisplay() {
            return this.displayManager.getDisplay(0);
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int r1) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int r3) {
            if (r3 != 0) goto L5;
            VideoFrameReleaseHelper.access$000(this.this$0, getDefaultDisplay());
            return;
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int r1) {
        }

        public void register() {
            this.displayManager.registerDisplayListener(this, Util.createHandlerForCurrentLooper());
            VideoFrameReleaseHelper.access$000(this.this$0, getDefaultDisplay());
        }

        public void unregister() {
            this.displayManager.unregisterDisplayListener(this);
        }
    }

    private static final class VSyncSampler implements Choreographer.FrameCallback, Handler.Callback {
        private static final int CREATE_CHOREOGRAPHER = 1;
        private static final VSyncSampler INSTANCE = null;
        private static final int MSG_ADD_OBSERVER = 2;
        private static final int MSG_REMOVE_OBSERVER = 3;
        private Choreographer choreographer;
        private final HandlerThread choreographerOwnerThread;
        private final Handler handler;
        private int observerCount;
        public volatile long sampledVsyncTimeNs;

        static {
            INSTANCE = new VSyncSampler();
        }

        private VSyncSampler() {
            this.sampledVsyncTimeNs = C0180C.TIME_UNSET;
            this.choreographerOwnerThread = new HandlerThread("ExoPlayer:FrameReleaseChoreographer");
            this.choreographerOwnerThread.start();
            this.handler = Util.createHandler(this.choreographerOwnerThread.getLooper(), this);
            this.handler.sendEmptyMessage(1);
        }

        private void addObserverInternal() {
            if (this.choreographer == null) goto L8;
            this.observerCount++;
            if (this.observerCount != 1) goto L9;
            this.choreographer.postFrameCallback(this);
            return;
        L9:
            return;
        }

        private void createChoreographerInstanceInternal() {
            this.choreographer = Choreographer.getInstance();     // Catch: RuntimeException -> L4
            return;
        L4:
            e = move-exception;
            Log.m49w(VideoFrameReleaseHelper.TAG, "Vsync sampling disabled due to platform error", e);
        }

        public static VSyncSampler getInstance() {
            return INSTANCE;
        }

        private void removeObserverInternal() {
            if (this.choreographer == null) goto L8;
            this.observerCount--;
            if (this.observerCount != 0) goto L9;
            this.choreographer.removeFrameCallback(this);
            this.sampledVsyncTimeNs = C0180C.TIME_UNSET;
            return;
        L9:
            return;
        }

        public void addObserver() {
            this.handler.sendEmptyMessage(2);
        }

        @Override // android.view.Choreographer.FrameCallback
        public void doFrame(long r4) {
            this.sampledVsyncTimeNs = r4;
            ((Choreographer) Assertions.checkNotNull(this.choreographer)).postFrameCallbackDelayed(this, 500);
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message r3) {
            switch(r3.what) {
                case 1: goto L10;
                case 2: goto L8;
                case 3: goto L6;
                default: goto L4;
            };
        L4:
            return false;
        L6:
            removeObserverInternal();
            return true;
        L8:
            addObserverInternal();
            return true;
        L10:
            createChoreographerInstanceInternal();
            return true;
        }

        public void removeObserver() {
            this.handler.sendEmptyMessage(3);
        }
    }

    public VideoFrameReleaseHelper(Context r3) {
        this.frameRateEstimator = new FixedFrameRateEstimator();
        this.displayHelper = maybeBuildDisplayHelper(r3);
        if (this.displayHelper == null) goto L5;
        VSyncSampler r0 = VSyncSampler.getInstance();
    L6:
        this.vsyncSampler = r0;
        this.vsyncDurationNs = C0180C.TIME_UNSET;
        this.vsyncOffsetNs = C0180C.TIME_UNSET;
        this.formatFrameRate = -1.0f;
        this.playbackSpeed = 1.0f;
        this.changeFrameRateStrategy = 0;
        return;
    L5:
        r0 = null;
        goto L6
    }

    static /* synthetic */ void access$000(VideoFrameReleaseHelper r0, Display r1) {
        r0.updateDefaultDisplayRefreshRateParams(r1);
    }

    private static boolean adjustmentAllowed(long r4, long r6) {
        if (Math.abs(r4 - r6) > MAX_ALLOWED_ADJUSTMENT_NS) goto L5;
        return true;
    L5:
        return false;
    }

    private void clearSurfaceFrameRate() {
        if (Util.SDK_INT >= 30) goto L5;
        return;
    L5:
        if (this.surface != null) goto L7;
        return;
    L7:
        if (this.changeFrameRateStrategy != Integer.MIN_VALUE) goto L9;
        return;
    L9:
        if (this.surfacePlaybackFrameRate == 0.0f) goto L16;
        this.surfacePlaybackFrameRate = 0.0f;
        Api30.setSurfaceFrameRate(this.surface, 0.0f);
        return;
    }

    private static long closestVsync(long r14, long r16, long r18) {
        long r2 = r16 + (r18 * ((r14 - r16) / r18));
        if (r14 > r2) goto L5;
        long r4 = r2 - r18;
        long r6 = r2;
    L7:
        if ((r6 - r14) >= (r14 - r4)) goto L10;
        return r6;
    L10:
        return r4;
    L5:
        r4 = r2;
        r6 = r2 + r18;
        goto L7
    }

    private DisplayHelper maybeBuildDisplayHelper(Context r3) {
        if (r3 != null) goto L5;
        return null;
    L5:
        DisplayManager r1 = (DisplayManager) r3.getSystemService("display");
        if (r1 != null) goto L8;
        return null;
    L8:
        return new DisplayHelper(this, r1);
    }

    private void resetAdjustment() {
        this.frameIndex = 0;
        this.lastAdjustedFrameIndex = -1;
        this.pendingLastAdjustedFrameIndex = -1;
    }

    private void updateDefaultDisplayRefreshRateParams(Display r7) {
        if (r7 == null) goto L4;
        this.vsyncDurationNs = (long) (1.0E9d / ((double) r7.getRefreshRate()));
        this.vsyncOffsetNs = (this.vsyncDurationNs * VSYNC_OFFSET_PERCENTAGE) / 100;
        return;
    L4:
        Log.m48w(TAG, "Unable to query display refresh rate");
        this.vsyncDurationNs = C0180C.TIME_UNSET;
        this.vsyncOffsetNs = C0180C.TIME_UNSET;
    }

    private void updateSurfaceMediaFrameRate() {
        if (Util.SDK_INT >= 30) goto L5;
        return;
    L5:
        if (this.surface != null) goto L8;
        return;
    L8:
        if (this.frameRateEstimator.isSynced() == false) goto L10;
        float r0 = this.frameRateEstimator.getFrameRate();
    L12:
        if (r0 != this.surfaceMediaFrameRate) goto L14;
        return;
    L14:
        boolean r4 = true;
        if (r0 == (-1.0f)) goto L33;
        if (this.surfaceMediaFrameRate == (-1.0f)) goto L33;
        if (this.frameRateEstimator.isSynced() == true) goto L21;
    L23:
        boolean r1 = false;
    L24:
        if (r1 == false) goto L26;
        float r2 = MINIMUM_MEDIA_FRAME_RATE_CHANGE_FOR_UPDATE_HIGH_CONFIDENCE;
    L28:
        if (Math.abs(r0 - this.surfaceMediaFrameRate) >= r2) goto L39;
        r4 = false;
    L39:
        if (r4 == false) goto L43;
        this.surfaceMediaFrameRate = r0;
        updateSurfacePlaybackFrameRate(false);
        return;
    L43:
        return;
    L26:
        r2 = 1.0f;
        goto L28
    L21:
        if (this.frameRateEstimator.getMatchingFrameDurationSumNs() < MINIMUM_MATCHING_FRAME_DURATION_FOR_HIGH_CONFIDENCE_NS) goto L23;
        r1 = true;
    L33:
        if (r0 == (-1.0f)) goto L36;
        r4 = true;
        goto L39
    L36:
        if (this.frameRateEstimator.getFramesWithoutSyncCount() >= 30) goto L39;
        r4 = false;
        goto L39
    L10:
        r0 = this.formatFrameRate;
        goto L12
    }

    private void updateSurfacePlaybackFrameRate(boolean r4) {
        if (Util.SDK_INT >= 30) goto L5;
        return;
    L5:
        if (this.surface != null) goto L7;
        return;
    L7:
        if (this.changeFrameRateStrategy == Integer.MIN_VALUE) goto L22;
        float r0 = 0.0f;
        if (this.started == true) goto L12;
    L14:
        if (r4 == false) goto L16;
    L18:
        this.surfacePlaybackFrameRate = r0;
        Api30.setSurfaceFrameRate(this.surface, r0);
        return;
    L16:
        if (this.surfacePlaybackFrameRate != r0) goto L18;
        return;
    L12:
        if (this.surfaceMediaFrameRate == (-1.0f)) goto L14;
        r0 = this.surfaceMediaFrameRate * this.playbackSpeed;
        goto L14
    }

    public long adjustReleaseTime(long r13) {
        if (this.lastAdjustedFrameIndex != (-1)) goto L5;
    L10:
        long r6 = r13;
    L11:
        this.pendingLastAdjustedFrameIndex = this.frameIndex;
        this.pendingLastAdjustedReleaseTimeNs = r6;
        if (this.vsyncSampler != null) goto L14;
    L21:
        return r6;
    L14:
        if (this.vsyncDurationNs == C0180C.TIME_UNSET) goto L21;
        long r8 = this.vsyncSampler.sampledVsyncTimeNs;
        if (r8 != C0180C.TIME_UNSET) goto L20;
        return r6;
    L20:
        return closestVsync(r6, r8, this.vsyncDurationNs) - this.vsyncOffsetNs;
    L5:
        if (this.frameRateEstimator.isSynced() == false) goto L10;
        long r4 = this.lastAdjustedReleaseTimeNs + ((long) (((this.frameIndex - this.lastAdjustedFrameIndex) * this.frameRateEstimator.getFrameDurationNs()) / this.playbackSpeed));
        if (adjustmentAllowed(r13, r4) == false) goto L9;
        r6 = r4;
        goto L11
    L9:
        resetAdjustment();
        goto L10
    }

    public void onFormatChanged(float r2) {
        this.formatFrameRate = r2;
        this.frameRateEstimator.reset();
        updateSurfaceMediaFrameRate();
    }

    public void onNextFrame(long r5) {
        if (this.pendingLastAdjustedFrameIndex == (-1)) goto L5;
        this.lastAdjustedFrameIndex = this.pendingLastAdjustedFrameIndex;
        this.lastAdjustedReleaseTimeNs = this.pendingLastAdjustedReleaseTimeNs;
    L5:
        this.frameIndex++;
        this.frameRateEstimator.onNextFrame(1000 * r5);
        updateSurfaceMediaFrameRate();
    }

    public void onPlaybackSpeed(float r2) {
        this.playbackSpeed = r2;
        resetAdjustment();
        updateSurfacePlaybackFrameRate(false);
    }

    public void onPositionReset() {
        resetAdjustment();
    }

    public void onStarted() {
        this.started = true;
        resetAdjustment();
        if (this.displayHelper == null) goto L5;
        ((VSyncSampler) Assertions.checkNotNull(this.vsyncSampler)).addObserver();
        this.displayHelper.register();
    L5:
        updateSurfacePlaybackFrameRate(false);
    }

    public void onStopped() {
        this.started = false;
        if (this.displayHelper == null) goto L5;
        this.displayHelper.unregister();
        ((VSyncSampler) Assertions.checkNotNull(this.vsyncSampler)).removeObserver();
    L5:
        clearSurfaceFrameRate();
    }

    public void onSurfaceChanged(Surface r2) {
        if (this.surface != r2) goto L5;
        return;
    L5:
        clearSurfaceFrameRate();
        this.surface = r2;
        updateSurfacePlaybackFrameRate(true);
    }

    public void setChangeFrameRateStrategy(int r2) {
        if (this.changeFrameRateStrategy != r2) goto L5;
        return;
    L5:
        this.changeFrameRateStrategy = r2;
        updateSurfacePlaybackFrameRate(true);
    }
}
