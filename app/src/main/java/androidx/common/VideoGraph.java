package androidx.media3.common;

/* JADX INFO: loaded from: classes.dex */
public interface VideoGraph {

    public interface Listener {

        /* JADX INFO: renamed from: androidx.media3.common.VideoGraph$Listener$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$onEnded(Listener r0, long r1) {
            }

            public static void $default$onError(Listener r0, VideoFrameProcessingException r1) {
            }

            public static void $default$onOutputFrameAvailableForRendering(Listener r0, long r1) {
            }

            public static void $default$onOutputSizeChanged(Listener r0, int r1, int r2) {
            }
        }

        void onEnded(long r1);

        void onError(VideoFrameProcessingException r1);

        void onOutputFrameAvailableForRendering(long r1);

        void onOutputSizeChanged(int r1, int r2);
    }

    VideoFrameProcessor getProcessor(int r1);

    boolean hasProducedFrameWithTimestampZero();

    void initialize() throws VideoFrameProcessingException;

    void registerInput(int r1) throws VideoFrameProcessingException;

    void release();

    void setOutputSurfaceInfo(SurfaceInfo r1);
}
