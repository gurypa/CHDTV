package androidx.media3.exoplayer.video;

import android.graphics.Bitmap;
import android.view.Surface;
import androidx.media3.common.Effect;
import androidx.media3.common.Format;
import androidx.media3.common.VideoSize;
import androidx.media3.common.util.Size;
import androidx.media3.common.util.TimestampIterator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public interface VideoSink {
    public static final int INPUT_TYPE_BITMAP = 2;
    public static final int INPUT_TYPE_SURFACE = 1;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface InputType {
    }

    public interface Listener {
        public static final Listener NO_OP = null;

        static {
            NO_OP = new C04001();
        }

        void onError(VideoSink r1, VideoSinkException r2);

        void onFirstFrameRendered(VideoSink r1);

        void onFrameDropped(VideoSink r1);

        void onVideoSizeChanged(VideoSink r1, VideoSize r2);
    }

    public interface VideoFrameHandler {
        void render(long r1);

        void skip();
    }

    public static final class VideoSinkException extends Exception {
        public final Format format;

        public VideoSinkException(Throwable r1, Format r2) {
            super(r1);
            this.format = r2;
        }
    }

    void clearOutputSurfaceInfo();

    void enableMayRenderStartOfStream();

    void flush(boolean r1);

    Surface getInputSurface();

    boolean handleInputBitmap(Bitmap r1, TimestampIterator r2);

    boolean handleInputFrame(long r1, boolean r3, long r4, long r6, VideoFrameHandler r8) throws VideoSinkException;

    void initialize(Format r1) throws VideoSinkException;

    boolean isEnded();

    boolean isInitialized();

    boolean isReady(boolean r1);

    void join(boolean r1);

    void onInputStreamChanged(int r1, Format r2);

    void onRendererDisabled();

    void onRendererEnabled(boolean r1);

    void onRendererStarted();

    void onRendererStopped();

    void release();

    void render(long r1, long r3) throws VideoSinkException;

    void setChangeFrameRateStrategy(int r1);

    void setListener(Listener r1, Executor r2);

    void setOutputSurfaceInfo(Surface r1, Size r2);

    void setPendingVideoEffects(List<Effect> r1);

    void setPlaybackSpeed(float r1);

    void setStreamTimestampInfo(long r1, long r3, long r5, long r7);

    void setVideoEffects(List<Effect> r1);

    void setVideoFrameMetadataListener(VideoFrameMetadataListener r1);
}
