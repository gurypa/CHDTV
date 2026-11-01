package androidx.media3.exoplayer.video;

import android.graphics.Bitmap;
import android.view.Surface;
import androidx.media3.common.Effect;
import androidx.media3.common.Format;
import androidx.media3.common.util.Size;
import androidx.media3.common.util.TimestampIterator;
import java.util.List;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
final class DefaultVideoSink implements VideoSink {
    private Format inputFormat;
    private final VideoFrameReleaseControl videoFrameReleaseControl;
    private final VideoFrameRenderControl videoFrameRenderControl;

    public DefaultVideoSink(VideoFrameReleaseControl r2, VideoFrameRenderControl r3) {
        this.videoFrameReleaseControl = r2;
        this.videoFrameRenderControl = r3;
        this.inputFormat = new Format.Builder().build();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void clearOutputSurfaceInfo() {
        this.videoFrameReleaseControl.setOutputSurface(null);
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void enableMayRenderStartOfStream() {
        this.videoFrameReleaseControl.allowReleaseFirstFrameBeforeStarted();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void flush(boolean r2) {
        if (r2 == false) goto L4;
        this.videoFrameReleaseControl.reset();
    L4:
        this.videoFrameRenderControl.flush();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public Surface getInputSurface() {
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public boolean handleInputBitmap(Bitmap r2, TimestampIterator r3) {
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public boolean handleInputFrame(long r2, boolean r4, long r5, long r7, VideoSink.VideoFrameHandler r9) {
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void initialize(Format r1) {
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public boolean isEnded() {
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public boolean isInitialized() {
        return true;
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public boolean isReady(boolean r2) {
        return this.videoFrameReleaseControl.isReady(r2);
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void join(boolean r2) {
        this.videoFrameReleaseControl.join(r2);
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void onInputStreamChanged(int r4, Format r5) {
        if (r5.width == this.inputFormat.width) goto L5;
    L6:
        this.videoFrameRenderControl.onOutputSizeChanged(r5.width, r5.height);
    L7:
        this.inputFormat = r5;
        return;
    L5:
        if (r5.height == this.inputFormat.height) goto L7;
        goto L6
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void onRendererDisabled() {
        this.videoFrameReleaseControl.onDisabled();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void onRendererEnabled(boolean r2) {
        this.videoFrameReleaseControl.onEnabled(r2);
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void onRendererStarted() {
        this.videoFrameReleaseControl.onStarted();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void onRendererStopped() {
        this.videoFrameReleaseControl.onStopped();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void release() {
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void render(long r2, long r4) {
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void setChangeFrameRateStrategy(int r2) {
        this.videoFrameReleaseControl.setChangeFrameRateStrategy(r2);
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void setListener(VideoSink.Listener r2, Executor r3) {
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void setOutputSurfaceInfo(Surface r2, Size r3) {
        this.videoFrameReleaseControl.setOutputSurface(r2);
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void setPendingVideoEffects(List<Effect> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void setPlaybackSpeed(float r2) {
        this.videoFrameReleaseControl.setPlaybackSpeed(r2);
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void setStreamTimestampInfo(long r2, long r4, long r6, long r8) {
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void setVideoEffects(List<Effect> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.exoplayer.video.VideoSink
    public void setVideoFrameMetadataListener(VideoFrameMetadataListener r2) {
        throw new UnsupportedOperationException();
    }
}
