package androidx.media3.exoplayer.video;

import android.os.Handler;
import android.os.SystemClock;
import androidx.media3.common.Format;
import androidx.media3.common.VideoSize;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.DecoderReuseEvaluation;

/* JADX INFO: loaded from: classes.dex */
public interface VideoRendererEventListener {

    /* JADX INFO: renamed from: androidx.media3.exoplayer.video.VideoRendererEventListener$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onDroppedFrames(VideoRendererEventListener r0, int r1, long r2) {
        }

        public static void $default$onRenderedFirstFrame(VideoRendererEventListener r0, Object r1, long r2) {
        }

        public static void $default$onVideoCodecError(VideoRendererEventListener r0, Exception r1) {
        }

        public static void $default$onVideoDecoderInitialized(VideoRendererEventListener r0, String r1, long r2, long r4) {
        }

        public static void $default$onVideoDecoderReleased(VideoRendererEventListener r0, String r1) {
        }

        public static void $default$onVideoDisabled(VideoRendererEventListener r0, DecoderCounters r1) {
        }

        public static void $default$onVideoEnabled(VideoRendererEventListener r0, DecoderCounters r1) {
        }

        public static void $default$onVideoFrameProcessingOffset(VideoRendererEventListener r0, long r1, int r3) {
        }

        public static void $default$onVideoInputFormatChanged(VideoRendererEventListener r0, Format r1, DecoderReuseEvaluation r2) {
        }

        public static void $default$onVideoSizeChanged(VideoRendererEventListener r0, VideoSize r1) {
        }
    }

    public static final class EventDispatcher {
        private final Handler handler;
        private final VideoRendererEventListener listener;

        public EventDispatcher(Handler r2, VideoRendererEventListener r3) {
            if (r3 == null) goto L5;
            Handler r0 = (Handler) Assertions.checkNotNull(r2);
        L6:
            this.handler = r0;
            this.listener = r3;
            return;
        L5:
            r0 = null;
            goto L6
        }

        public void decoderInitialized(final String r9, final long r10, final long r12) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0390x6446ce7c(this, r9, r10, r12));
            return;
        }

        public void decoderReleased(final String r3) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0399x6446ce85(this, r3));
            return;
        }

        public void disabled(final DecoderCounters r3) {
            r3.ensureUpdated();
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0398x6446ce84(this, r3));
            return;
        }

        public void droppedFrames(final int r3, final long r4) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0392x6446ce7e(this, r3, r4));
            return;
        }

        public void enabled(final DecoderCounters r3) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0396x6446ce82(this, r3));
            return;
        }

        public void inputFormatChanged(final Format r3, final DecoderReuseEvaluation r4) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0397x6446ce83(this, r3, r4));
            return;
        }

        /* JADX INFO: renamed from: lambda$decoderInitialized$1$androidx-media3-exoplayer-video-VideoRendererEventListener$EventDispatcher */
        /* synthetic */ void m154xffa420d3(String r8, long r9, long r11) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onVideoDecoderInitialized(r8, r9, r11);
        }

        /* JADX INFO: renamed from: lambda$decoderReleased$7$androidx-media3-exoplayer-video-VideoRendererEventListener$EventDispatcher */
        /* synthetic */ void m155x45853f96(String r2) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onVideoDecoderReleased(r2);
        }

        /* JADX INFO: renamed from: lambda$disabled$8$androidx-media3-exoplayer-video-VideoRendererEventListener$EventDispatcher */
        /* synthetic */ void m156x166f1720(DecoderCounters r2) {
            r2.ensureUpdated();
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onVideoDisabled(r2);
        }

        /* JADX INFO: renamed from: lambda$droppedFrames$3$androidx-media3-exoplayer-video-VideoRendererEventListener$EventDispatcher */
        /* synthetic */ void m157x9a4cf695(int r2, long r3) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onDroppedFrames(r2, r3);
        }

        /* JADX INFO: renamed from: lambda$enabled$0$androidx-media3-exoplayer-video-VideoRendererEventListener$EventDispatcher */
        /* synthetic */ void m158x7180d5d(DecoderCounters r2) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onVideoEnabled(r2);
        }

        /* JADX INFO: renamed from: lambda$inputFormatChanged$2$androidx-media3-exoplayer-video-VideoRendererEventListener$EventDispatcher */
        /* synthetic */ void m159xd00d27ef(Format r2, DecoderReuseEvaluation r3) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onVideoInputFormatChanged(r2, r3);
        }

        /* JADX INFO: renamed from: lambda$renderedFirstFrame$6$androidx-media3-exoplayer-video-VideoRendererEventListener$EventDispatcher */
        /* synthetic */ void m160xb1e96bac(Object r2, long r3) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onRenderedFirstFrame(r2, r3);
        }

        /* JADX INFO: renamed from: lambda$reportVideoFrameProcessingOffset$4$androidx-media3-exoplayer-video-VideoRendererEventListener$EventDispatcher */
        /* synthetic */ void m161xc5ffb974(long r2, int r4) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onVideoFrameProcessingOffset(r2, r4);
        }

        /* JADX INFO: renamed from: lambda$videoCodecError$9$androidx-media3-exoplayer-video-VideoRendererEventListener$EventDispatcher */
        /* synthetic */ void m162x90ab4908(Exception r2) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onVideoCodecError(r2);
        }

        /* JADX INFO: renamed from: lambda$videoSizeChanged$5$androidx-media3-exoplayer-video-VideoRendererEventListener$EventDispatcher */
        /* synthetic */ void m163xad971007(VideoSize r2) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onVideoSizeChanged(r2);
        }

        public void renderedFirstFrame(final Object r5) {
            if (this.handler == null) goto L6;
            final long r0 = SystemClock.elapsedRealtime();
            this.handler.post(new RunnableC0393x6446ce7f(this, r5, r0));
            return;
        }

        public void reportVideoFrameProcessingOffset(final long r3, final int r5) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0394x6446ce80(this, r3, r5));
            return;
        }

        public void videoCodecError(final Exception r3) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0395x6446ce81(this, r3));
            return;
        }

        public void videoSizeChanged(final VideoSize r3) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0391x6446ce7d(this, r3));
            return;
        }
    }

    void onDroppedFrames(int r1, long r2);

    void onRenderedFirstFrame(Object r1, long r2);

    void onVideoCodecError(Exception r1);

    void onVideoDecoderInitialized(String r1, long r2, long r4);

    void onVideoDecoderReleased(String r1);

    void onVideoDisabled(DecoderCounters r1);

    void onVideoEnabled(DecoderCounters r1);

    void onVideoFrameProcessingOffset(long r1, int r3);

    void onVideoInputFormatChanged(Format r1, DecoderReuseEvaluation r2);

    void onVideoSizeChanged(VideoSize r1);
}
