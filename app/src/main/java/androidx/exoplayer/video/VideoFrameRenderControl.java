package androidx.media3.exoplayer.video;

import androidx.media3.common.C0180C;
import androidx.media3.common.VideoSize;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.LongArrayQueue;
import androidx.media3.common.util.TimedValueQueue;
import androidx.media3.exoplayer.ExoPlaybackException;

/* JADX INFO: loaded from: classes.dex */
final class VideoFrameRenderControl {
    private final FrameRenderer frameRenderer;
    private long lastPresentationTimeUs;
    private long outputStreamOffsetUs;
    private VideoSize pendingOutputVideoSize;
    private final LongArrayQueue presentationTimestampsUs;
    private VideoSize reportedVideoSize;
    private final TimedValueQueue<Long> streamOffsets;
    private final VideoFrameReleaseControl videoFrameReleaseControl;
    private final VideoFrameReleaseControl.FrameReleaseInfo videoFrameReleaseInfo;
    private final TimedValueQueue<VideoSize> videoSizeChanges;

    interface FrameRenderer {
        void dropFrame();

        void onVideoSizeChanged(VideoSize r1);

        void renderFrame(long r1, long r3, long r5, boolean r7);
    }

    public VideoFrameRenderControl(FrameRenderer r3, VideoFrameReleaseControl r4) {
        this.frameRenderer = r3;
        this.videoFrameReleaseControl = r4;
        this.videoFrameReleaseInfo = new VideoFrameReleaseControl.FrameReleaseInfo();
        this.videoSizeChanges = new TimedValueQueue();
        this.streamOffsets = new TimedValueQueue();
        this.presentationTimestampsUs = new LongArrayQueue();
        this.reportedVideoSize = VideoSize.UNKNOWN;
        this.lastPresentationTimeUs = C0180C.TIME_UNSET;
    }

    private void dropFrame() {
        Assertions.checkStateNotNull(Long.valueOf(this.presentationTimestampsUs.remove()));
        this.frameRenderer.dropFrame();
    }

    private static <T> T getLastAndClear(TimedValueQueue<T> r2) {
        if (r2.size() <= 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
    L8:
        if (r2.size() <= 1) goto L11;
        r2.pollFirst();
        goto L8
    L11:
        return (T) Assertions.checkNotNull(r2.pollFirst());
    L5:
        r0 = false;
        goto L6
    }

    private boolean maybeUpdateOutputStreamOffset(long r6) {
        Long r0 = this.streamOffsets.pollFloor(r6);
        if (r0 != null) goto L5;
        return false;
    L5:
        if (r0.longValue() == this.outputStreamOffsetUs) goto L10;
        this.outputStreamOffsetUs = r0.longValue();
        return true;
    L10:
        return false;
    }

    private boolean maybeUpdateVideoSize(long r4) {
        VideoSize r0 = this.videoSizeChanges.pollFloor(r4);
        if (r0 != null) goto L6;
        return false;
    L6:
        if (r0.equals(VideoSize.UNKNOWN) == false) goto L8;
    L11:
        return false;
    L8:
        if (r0.equals(this.reportedVideoSize) == true) goto L11;
        this.reportedVideoSize = r0;
        return true;
    }

    private void renderFrame(boolean r10) {
        long r4 = ((Long) Assertions.checkStateNotNull(Long.valueOf(this.presentationTimestampsUs.remove()))).longValue();
        if (maybeUpdateVideoSize(r4) == false) goto L5;
        this.frameRenderer.onVideoSizeChanged(this.reportedVideoSize);
    L5:
        if (r10 == false) goto L7;
        long r2 = -1;
    L8:
        this.frameRenderer.renderFrame(r2, r4, this.outputStreamOffsetUs, this.videoFrameReleaseControl.onFrameReleasedIsFirstFrame());
        return;
    L7:
        r2 = this.videoFrameReleaseInfo.getReleaseTimeNs();
        goto L8
    }

    public void flush() {
        this.presentationTimestampsUs.clear();
        this.lastPresentationTimeUs = C0180C.TIME_UNSET;
        if (this.streamOffsets.size() <= 0) goto L6;
        long r0 = ((Long) getLastAndClear(this.streamOffsets)).longValue();
        this.streamOffsets.add(0, Long.valueOf(r0));
    L6:
        if (this.pendingOutputVideoSize == null) goto L8;
        this.videoSizeChanges.clear();
        return;
    L8:
        if (this.videoSizeChanges.size() <= 0) goto L12;
        this.pendingOutputVideoSize = (VideoSize) getLastAndClear(this.videoSizeChanges);
        return;
    }

    public boolean hasReleasedFrame(long r5) {
        if (this.lastPresentationTimeUs != C0180C.TIME_UNSET) goto L5;
    L7:
        return false;
    L5:
        if (this.lastPresentationTimeUs < r5) goto L7;
        return true;
    }

    public void onOutputFrameAvailableForRendering(long r3) {
        if (this.pendingOutputVideoSize == null) goto L5;
        this.videoSizeChanges.add(r3, this.pendingOutputVideoSize);
        this.pendingOutputVideoSize = null;
    L5:
        this.presentationTimestampsUs.add(r3);
    }

    public void onOutputSizeChanged(int r2, int r3) {
        this.pendingOutputVideoSize = new VideoSize(r2, r3);
    }

    public void onStreamOffsetChange(long r3, long r5) {
        this.streamOffsets.add(r3, Long.valueOf(r5));
    }

    public void render(long r13, long r15) throws ExoPlaybackException {
    L3:
        if (this.presentationTimestampsUs.isEmpty() == true) goto L19;
        long r2 = this.presentationTimestampsUs.element();
        if (maybeUpdateOutputStreamOffset(r2) == false) goto L7;
        this.videoFrameReleaseControl.onProcessedStreamChange();
    L7:
        int r0 = this.videoFrameReleaseControl.getFrameReleaseAction(r2, r13, r15, this.outputStreamOffsetUs, false, this.videoFrameReleaseInfo);
        switch(r0) {
            case 0: goto L13;
            case 1: goto L13;
            case 2: goto L12;
            case 3: goto L12;
            case 4: goto L12;
            case 5: goto L11;
            default: goto L10;
        };
    L10:
        throw new IllegalStateException(String.valueOf(r0));
    L11:
        return;
    L13:
        this.lastPresentationTimeUs = r2;
        if (r0 != 0) goto L16;
        boolean r1 = true;
    L17:
        renderFrame(r1);
        goto L3
    L16:
        r1 = false;
        goto L17
    L12:
        this.lastPresentationTimeUs = r2;
        dropFrame();
        goto L3
    }
}
