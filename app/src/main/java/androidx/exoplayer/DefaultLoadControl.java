package androidx.media3.exoplayer;

import androidx.media3.common.C0180C;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.DefaultAllocator;
import java.util.HashMap;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class DefaultLoadControl implements LoadControl {
    public static final int DEFAULT_AUDIO_BUFFER_SIZE = 13107200;
    public static final int DEFAULT_BACK_BUFFER_DURATION_MS = 0;
    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 5000;
    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_MS = 2500;
    public static final int DEFAULT_CAMERA_MOTION_BUFFER_SIZE = 131072;
    public static final int DEFAULT_IMAGE_BUFFER_SIZE = 131072;
    public static final int DEFAULT_MAX_BUFFER_MS = 50000;
    public static final int DEFAULT_METADATA_BUFFER_SIZE = 131072;
    public static final int DEFAULT_MIN_BUFFER_MS = 50000;
    public static final int DEFAULT_MIN_BUFFER_SIZE = 13107200;
    public static final int DEFAULT_MUXED_BUFFER_SIZE = 144310272;
    public static final boolean DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS = false;
    public static final boolean DEFAULT_RETAIN_BACK_BUFFER_FROM_KEYFRAME = false;
    public static final int DEFAULT_TARGET_BUFFER_BYTES = -1;
    public static final int DEFAULT_TEXT_BUFFER_SIZE = 131072;
    public static final int DEFAULT_VIDEO_BUFFER_SIZE = 131072000;
    private final DefaultAllocator allocator;
    private final long backBufferDurationUs;
    private final long bufferForPlaybackAfterRebufferUs;
    private final long bufferForPlaybackUs;
    private final HashMap<PlayerId, PlayerLoadingState> loadingStates;
    private final long maxBufferUs;
    private final long minBufferUs;
    private final boolean prioritizeTimeOverSizeThresholds;
    private final boolean retainBackBufferFromKeyframe;
    private final int targetBufferBytesOverwrite;
    private long threadId;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.DefaultLoadControl$1 */
    static /* synthetic */ class C02231 {
    }

    public static final class Builder {
        private DefaultAllocator allocator;
        private int backBufferDurationMs;
        private int bufferForPlaybackAfterRebufferMs;
        private int bufferForPlaybackMs;
        private boolean buildCalled;
        private int maxBufferMs;
        private int minBufferMs;
        private boolean prioritizeTimeOverSizeThresholds;
        private boolean retainBackBufferFromKeyframe;
        private int targetBufferBytes;

        public Builder() {
            this.minBufferMs = 50000;
            this.maxBufferMs = 50000;
            this.bufferForPlaybackMs = DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS;
            this.bufferForPlaybackAfterRebufferMs = 5000;
            this.targetBufferBytes = -1;
            this.prioritizeTimeOverSizeThresholds = false;
            this.backBufferDurationMs = 0;
            this.retainBackBufferFromKeyframe = false;
        }

        public DefaultLoadControl build() {
            Assertions.checkState(!this.buildCalled);
            this.buildCalled = true;
            if (this.allocator != null) goto L6;
            this.allocator = new DefaultAllocator(true, 65536);
        L6:
            return new DefaultLoadControl(this.allocator, this.minBufferMs, this.maxBufferMs, this.bufferForPlaybackMs, this.bufferForPlaybackAfterRebufferMs, this.targetBufferBytes, this.prioritizeTimeOverSizeThresholds, this.backBufferDurationMs, this.retainBackBufferFromKeyframe);
        }

        public Builder setAllocator(DefaultAllocator r2) {
            Assertions.checkState(!this.buildCalled);
            this.allocator = r2;
            return this;
        }

        public Builder setBackBuffer(int r4, boolean r5) {
            Assertions.checkState(!this.buildCalled);
            DefaultLoadControl.access$000(r4, 0, "backBufferDurationMs", "0");
            this.backBufferDurationMs = r4;
            this.retainBackBufferFromKeyframe = r5;
            return this;
        }

        public Builder setBufferDurationsMs(int r5, int r6, int r7, int r8) {
            Assertions.checkState(!this.buildCalled);
            DefaultLoadControl.access$000(r7, 0, "bufferForPlaybackMs", "0");
            DefaultLoadControl.access$000(r8, 0, "bufferForPlaybackAfterRebufferMs", "0");
            DefaultLoadControl.access$000(r5, r7, "minBufferMs", "bufferForPlaybackMs");
            DefaultLoadControl.access$000(r5, r8, "minBufferMs", "bufferForPlaybackAfterRebufferMs");
            DefaultLoadControl.access$000(r6, r5, "maxBufferMs", "minBufferMs");
            this.minBufferMs = r5;
            this.maxBufferMs = r6;
            this.bufferForPlaybackMs = r7;
            this.bufferForPlaybackAfterRebufferMs = r8;
            return this;
        }

        public Builder setPrioritizeTimeOverSizeThresholds(boolean r2) {
            Assertions.checkState(!this.buildCalled);
            this.prioritizeTimeOverSizeThresholds = r2;
            return this;
        }

        public Builder setTargetBufferBytes(int r2) {
            Assertions.checkState(!this.buildCalled);
            this.targetBufferBytes = r2;
            return this;
        }
    }

    private static class PlayerLoadingState {
        public boolean isLoading;
        public int targetBufferBytes;

        private PlayerLoadingState() {
        }

        /* synthetic */ PlayerLoadingState(C02231 r1) {
            this();
        }
    }

    public DefaultLoadControl() {
        this(new DefaultAllocator(true, 65536), 50000, 50000, DEFAULT_BUFFER_FOR_PLAYBACK_MS, 5000, -1, false, 0, false);
    }

    protected DefaultLoadControl(DefaultAllocator r6, int r7, int r8, int r9, int r10, int r11, boolean r12, int r13, boolean r14) {
        assertGreaterOrEqual(r9, 0, "bufferForPlaybackMs", "0");
        assertGreaterOrEqual(r10, 0, "bufferForPlaybackAfterRebufferMs", "0");
        assertGreaterOrEqual(r7, r9, "minBufferMs", "bufferForPlaybackMs");
        assertGreaterOrEqual(r7, r10, "minBufferMs", "bufferForPlaybackAfterRebufferMs");
        assertGreaterOrEqual(r8, r7, "maxBufferMs", "minBufferMs");
        assertGreaterOrEqual(r13, 0, "backBufferDurationMs", "0");
        this.allocator = r6;
        this.minBufferUs = Util.msToUs(r7);
        this.maxBufferUs = Util.msToUs(r8);
        this.bufferForPlaybackUs = Util.msToUs(r9);
        this.bufferForPlaybackAfterRebufferUs = Util.msToUs(r10);
        this.targetBufferBytesOverwrite = r11;
        this.prioritizeTimeOverSizeThresholds = r12;
        this.backBufferDurationUs = Util.msToUs(r13);
        this.retainBackBufferFromKeyframe = r14;
        this.loadingStates = new HashMap();
        this.threadId = -1;
    }

    static /* synthetic */ void access$000(int r0, int r1, String r2, String r3) {
        assertGreaterOrEqual(r0, r1, r2, r3);
    }

    private static void assertGreaterOrEqual(int r3, int r4, String r5, String r6) {
        if (r3 < r4) goto L4;
        boolean r0 = true;
    L5:
        Assertions.checkArgument(r0, r5 + " cannot be less than " + r6);
        return;
    L4:
        r0 = false;
        goto L5
    }

    private static int getDefaultBufferSize(int r2) {
        switch(r2) {
            case -2: goto L16;
            case -1: goto L15;
            case 0: goto L13;
            case 1: goto L12;
            case 2: goto L10;
            case 3: goto L9;
            case 4: goto L8;
            case 5: goto L7;
            case 6: goto L6;
            default: goto L5;
        };
    L6:
        return 131072;
    L7:
        return 131072;
    L8:
        return 131072;
    L9:
        return 131072;
    L10:
        return DEFAULT_VIDEO_BUFFER_SIZE;
    L12:
        return 13107200;
    L13:
        return DEFAULT_MUXED_BUFFER_SIZE;
    L15:
        return 13107200;
    L16:
        return 0;
    L5:
        throw new IllegalArgumentException();
    }

    private void removePlayer(PlayerId r2) {
        if (this.loadingStates.remove(r2) == null) goto L6;
        updateAllocator();
        return;
    }

    private void resetPlayerLoadingState(PlayerId r4) {
        PlayerLoadingState r0 = (PlayerLoadingState) Assertions.checkNotNull(this.loadingStates.get(r4));
        if (this.targetBufferBytesOverwrite != (-1)) goto L5;
        int r1 = 13107200;
    L6:
        r0.targetBufferBytes = r1;
        r0.isLoading = false;
        return;
    L5:
        r1 = this.targetBufferBytesOverwrite;
        goto L6
    }

    private void updateAllocator() {
        if (this.loadingStates.isEmpty() == false) goto L5;
        this.allocator.reset();
        return;
    L5:
        this.allocator.setTargetBufferSize(calculateTotalTargetBufferBytes());
    }

    @Deprecated
    protected final int calculateTargetBufferBytes(Renderer[] r2, ExoTrackSelection[] r3) {
        return calculateTargetBufferBytes(r3);
    }

    protected int calculateTargetBufferBytes(ExoTrackSelection[] r6) {
        int r0 = 0;
        int r1 = r6.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L9;
        ExoTrackSelection r3 = r6[r2];
        if (r3 == null) goto L7;
        r0 = r0 + getDefaultBufferSize(r3.getTrackGroup().type);
    L7:
        r2 = r2 + 1;
        goto L3
    L9:
        return Math.max(13107200, r0);
    }

    int calculateTotalTargetBufferBytes() {
        int r0 = 0;
        Iterator<PlayerLoadingState> r1 = this.loadingStates.values().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0 = r0 + r1.next().targetBufferBytes;
        goto L4
    L6:
        return r0;
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public Allocator getAllocator() {
        return this.allocator;
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public /* synthetic */ long getBackBufferDurationUs() {
        return LoadControl.CC.$default$getBackBufferDurationUs(this);
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public long getBackBufferDurationUs(PlayerId r3) {
        return this.backBufferDurationUs;
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public /* synthetic */ void onPrepared() {
        LoadControl.CC.$default$onPrepared(this);
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public void onPrepared(PlayerId r7) {
        long r0 = Thread.currentThread().getId();
        if (this.threadId != (-1)) goto L5;
    L8:
        boolean r2 = true;
    L9:
        Assertions.checkState(r2, "Players that share the same LoadControl must share the same playback thread. See ExoPlayer.Builder.setPlaybackLooper(Looper).");
        this.threadId = r0;
        if (this.loadingStates.containsKey(r7) == true) goto L12;
        this.loadingStates.put(r7, new PlayerLoadingState(null));
    L12:
        resetPlayerLoadingState(r7);
        return;
    L5:
        if (this.threadId == r0) goto L8;
        r2 = false;
        goto L9
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public /* synthetic */ void onReleased() {
        LoadControl.CC.$default$onReleased(this);
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public void onReleased(PlayerId r3) {
        removePlayer(r3);
        if (this.loadingStates.isEmpty() == false) goto L6;
        this.threadId = -1;
        return;
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public /* synthetic */ void onStopped() {
        LoadControl.CC.$default$onStopped(this);
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public void onStopped(PlayerId r1) {
        removePlayer(r1);
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public /* synthetic */ void onTracksSelected(Timeline r1, MediaSource.MediaPeriodId r2, Renderer[] r3, TrackGroupArray r4, ExoTrackSelection[] r5) {
        LoadControl.CC.$default$onTracksSelected(this, r1, r2, r3, r4, r5);
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public void onTracksSelected(LoadControl.Parameters r4, TrackGroupArray r5, ExoTrackSelection[] r6) {
        PlayerLoadingState r0 = (PlayerLoadingState) Assertions.checkNotNull(this.loadingStates.get(r4.playerId));
        if (this.targetBufferBytesOverwrite != (-1)) goto L5;
        int r1 = calculateTargetBufferBytes(r6);
    L6:
        r0.targetBufferBytes = r1;
        updateAllocator();
        return;
    L5:
        r1 = this.targetBufferBytesOverwrite;
        goto L6
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public /* synthetic */ void onTracksSelected(PlayerId r1, Timeline r2, MediaSource.MediaPeriodId r3, Renderer[] r4, TrackGroupArray r5, ExoTrackSelection[] r6) {
        LoadControl.CC.$default$onTracksSelected(this, r1, r2, r3, r4, r5, r6);
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public /* synthetic */ void onTracksSelected(Renderer[] r1, TrackGroupArray r2, ExoTrackSelection[] r3) {
        LoadControl.CC.$default$onTracksSelected(this, r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public /* synthetic */ boolean retainBackBufferFromKeyframe() {
        return LoadControl.CC.$default$retainBackBufferFromKeyframe(this);
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public boolean retainBackBufferFromKeyframe(PlayerId r2) {
        return this.retainBackBufferFromKeyframe;
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public /* synthetic */ boolean shouldContinueLoading(long r1, long r3, float r5) {
        return LoadControl.CC.$default$shouldContinueLoading(this, r1, r3, r5);
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public boolean shouldContinueLoading(LoadControl.Parameters r12) {
        PlayerLoadingState r0 = (PlayerLoadingState) Assertions.checkNotNull(this.loadingStates.get(r12.playerId));
        boolean r3 = true;
        if (this.allocator.getTotalBytesAllocated() < calculateTotalTargetBufferBytes()) goto L5;
        boolean r1 = true;
    L6:
        long r5 = this.minBufferUs;
        if (r12.playbackSpeed <= 1.0f) goto L10;
        r5 = Math.min(Util.getMediaDurationForPlayoutDuration(r5, r12.playbackSpeed), this.maxBufferUs);
    L10:
        if (r12.bufferedDurationUs >= Math.max(r5, 500000)) goto L22;
        if (this.prioritizeTimeOverSizeThresholds == true) goto L16;
        if (r1 == false) goto L16;
        r3 = false;
    L16:
        r0.isLoading = r3;
        if (r0.isLoading == true) goto L26;
        if (r12.bufferedDurationUs >= 500000) goto L26;
        Log.m48w("DefaultLoadControl", "Target buffer size reached with less than 500ms of buffered media data.");
    L26:
        return r0.isLoading;
    L22:
        if (r12.bufferedDurationUs >= this.maxBufferUs) goto L24;
        if (r1 == false) goto L26;
    L24:
        r0.isLoading = false;
        goto L26
    L5:
        r1 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public boolean shouldContinuePreloading(Timeline r4, MediaSource.MediaPeriodId r5, long r6) {
        Iterator<PlayerLoadingState> r0 = this.loadingStates.values().iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (r0.next().isLoading == false) goto L4;
        return false;
    L10:
        return true;
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public /* synthetic */ boolean shouldStartPlayback(long r1, float r3, boolean r4, long r5) {
        return LoadControl.CC.$default$shouldStartPlayback(this, r1, r3, r4, r5);
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public /* synthetic */ boolean shouldStartPlayback(Timeline r1, MediaSource.MediaPeriodId r2, long r3, float r5, boolean r6, long r7) {
        return LoadControl.CC.$default$shouldStartPlayback(this, r1, r2, r3, r5, r6, r7);
    }

    @Override // androidx.media3.exoplayer.LoadControl
    public boolean shouldStartPlayback(LoadControl.Parameters r9) {
        long r0 = Util.getPlayoutDurationForMediaDuration(r9.bufferedDurationUs, r9.playbackSpeed);
        if (r9.rebuffering == false) goto L5;
        long r2 = this.bufferForPlaybackAfterRebufferUs;
    L7:
        if (r9.targetLiveOffsetUs == C0180C.TIME_UNSET) goto L10;
        r2 = Math.min(r9.targetLiveOffsetUs / 2, r2);
    L10:
        if (r2 > 0) goto L12;
    L19:
        return true;
    L12:
        if (r0 >= r2) goto L19;
        if (this.prioritizeTimeOverSizeThresholds == false) goto L16;
    L18:
        return false;
    L16:
        if (this.allocator.getTotalBytesAllocated() < calculateTotalTargetBufferBytes()) goto L18;
    L5:
        r2 = this.bufferForPlaybackUs;
        goto L7
    }
}
