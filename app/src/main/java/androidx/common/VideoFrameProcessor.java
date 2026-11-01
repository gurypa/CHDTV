package androidx.media3.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Surface;
import androidx.media3.common.util.TimestampIterator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public interface VideoFrameProcessor {
    public static final long DROP_OUTPUT_FRAME = -2;
    public static final int INPUT_TYPE_BITMAP = 2;
    public static final int INPUT_TYPE_SURFACE = 1;
    public static final int INPUT_TYPE_SURFACE_AUTOMATIC_FRAME_REGISTRATION = 4;
    public static final int INPUT_TYPE_TEXTURE_ID = 3;
    public static final long RENDER_OUTPUT_FRAME_IMMEDIATELY = -1;
    public static final long RENDER_OUTPUT_FRAME_WITH_PRESENTATION_TIME = -3;

    public interface Factory {
        VideoFrameProcessor create(Context r1, DebugViewProvider r2, ColorInfo r3, boolean r4, Executor r5, Listener r6) throws VideoFrameProcessingException;
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface InputType {
    }

    public interface Listener {

        /* JADX INFO: renamed from: androidx.media3.common.VideoFrameProcessor$Listener$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$onEnded(Listener r0) {
            }

            public static void $default$onError(Listener r0, VideoFrameProcessingException r1) {
            }

            public static void $default$onInputStreamRegistered(Listener r0, int r1, List r2, FrameInfo r3) {
            }

            public static void $default$onOutputFrameAvailableForRendering(Listener r0, long r1) {
            }

            public static void $default$onOutputSizeChanged(Listener r0, int r1, int r2) {
            }
        }

        void onEnded();

        void onError(VideoFrameProcessingException r1);

        void onInputStreamRegistered(int r1, List<Effect> r2, FrameInfo r3);

        void onOutputFrameAvailableForRendering(long r1);

        void onOutputSizeChanged(int r1, int r2);
    }

    void flush();

    Surface getInputSurface();

    int getPendingInputFrameCount();

    boolean queueInputBitmap(Bitmap r1, TimestampIterator r2);

    boolean queueInputTexture(int r1, long r2);

    boolean registerInputFrame();

    void registerInputStream(int r1, List<Effect> r2, FrameInfo r3);

    void release();

    void renderOutputFrame(long r1);

    void setOnInputFrameProcessedListener(OnInputFrameProcessedListener r1);

    void setOnInputSurfaceReadyListener(Runnable r1);

    void setOutputSurfaceInfo(SurfaceInfo r1);

    void signalEndOfInput();
}
