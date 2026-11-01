package androidx.media3.exoplayer.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Pair;
import android.view.Surface;
import androidx.media3.common.C0180C;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.DebugViewProvider;
import androidx.media3.common.Effect;
import androidx.media3.common.Format;
import androidx.media3.common.FrameInfo;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.PreviewingVideoGraph;
import androidx.media3.common.SurfaceInfo;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.VideoFrameProcessor;
import androidx.media3.common.VideoGraph;
import androidx.media3.common.VideoSize;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.HandlerWrapper;
import androidx.media3.common.util.Size;
import androidx.media3.common.util.TimestampIterator;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.ExoPlaybackException;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;

/* JADX INFO: loaded from: classes.dex */
public final class PlaybackVideoGraphWrapper implements VideoSinkProvider, VideoGraph.Listener {
    private static final Executor NO_OP_EXECUTOR = null;
    private static final int STATE_CREATED = 0;
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_RELEASED = 2;
    private long bufferTimestampAdjustmentUs;
    private final Clock clock;
    private final List<Effect> compositionEffects;
    private final Context context;
    private Pair<Surface, Size> currentSurfaceAndSize;
    private final VideoSink defaultVideoSink;
    private HandlerWrapper handler;
    private final InputVideoSink inputVideoSink;
    private final CopyOnWriteArraySet<Listener> listeners;
    private Format outputFormat;
    private int pendingFlushCount;
    private final PreviewingVideoGraph.Factory previewingVideoGraphFactory;
    private int state;
    private VideoFrameMetadataListener videoFrameMetadataListener;
    private final VideoFrameReleaseControl videoFrameReleaseControl;
    private final VideoFrameRenderControl videoFrameRenderControl;
    private PreviewingVideoGraph videoGraph;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.video.PlaybackVideoGraphWrapper$1 */
    static /* synthetic */ class C03841 {
    }

    public static final class Builder {
        private boolean built;
        private Clock clock;
        private List<Effect> compositionEffects;
        private final Context context;
        private PreviewingVideoGraph.Factory previewingVideoGraphFactory;
        private VideoFrameProcessor.Factory videoFrameProcessorFactory;
        private final VideoFrameReleaseControl videoFrameReleaseControl;

        public Builder(Context r2, VideoFrameReleaseControl r3) {
            this.context = r2.getApplicationContext();
            this.videoFrameReleaseControl = r3;
            this.compositionEffects = ImmutableList.m212of();
            this.clock = Clock.DEFAULT;
        }

        static /* synthetic */ Context access$200(Builder r1) {
            return r1.context;
        }

        static /* synthetic */ Clock access$300(Builder r1) {
            return r1.clock;
        }

        static /* synthetic */ VideoFrameReleaseControl access$400(Builder r1) {
            return r1.videoFrameReleaseControl;
        }

        static /* synthetic */ PreviewingVideoGraph.Factory access$600(Builder r1) {
            return r1.previewingVideoGraphFactory;
        }

        static /* synthetic */ List access$700(Builder r1) {
            return r1.compositionEffects;
        }

        public PlaybackVideoGraphWrapper build() {
            Assertions.checkState(!this.built);
            C03841 r2 = null;
            if (this.previewingVideoGraphFactory == null) goto L5;
        L8:
            PlaybackVideoGraphWrapper r0 = new PlaybackVideoGraphWrapper(this, r2);
            this.built = true;
            return r0;
        L5:
            if (this.videoFrameProcessorFactory != null) goto L7;
            this.videoFrameProcessorFactory = new ReflectiveDefaultVideoFrameProcessorFactory(r2);
        L7:
            this.previewingVideoGraphFactory = new ReflectivePreviewingSingleInputVideoGraphFactory(this.videoFrameProcessorFactory);
            goto L8
        }

        public Builder setClock(Clock r1) {
            this.clock = r1;
            return this;
        }

        public Builder setCompositionEffects(List<Effect> r1) {
            this.compositionEffects = r1;
            return this;
        }

        public Builder setPreviewingVideoGraphFactory(PreviewingVideoGraph.Factory r1) {
            this.previewingVideoGraphFactory = r1;
            return this;
        }

        public Builder setVideoFrameProcessorFactory(VideoFrameProcessor.Factory r1) {
            this.videoFrameProcessorFactory = r1;
            return this;
        }
    }

    private final class FrameRendererImpl implements VideoFrameRenderControl.FrameRenderer {
        final /* synthetic */ PlaybackVideoGraphWrapper this$0;

        private FrameRendererImpl(PlaybackVideoGraphWrapper r1) {
            this.this$0 = r1;
        }

        /* synthetic */ FrameRendererImpl(PlaybackVideoGraphWrapper r1, C03841 r2) {
            this(r1);
        }

        @Override // androidx.media3.exoplayer.video.VideoFrameRenderControl.FrameRenderer
        public void dropFrame() {
            Iterator r0 = PlaybackVideoGraphWrapper.access$2200(this.this$0).iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            ((Listener) r0.next()).onFrameDropped(this.this$0);
            goto L4
        L6:
            ((PreviewingVideoGraph) Assertions.checkStateNotNull(PlaybackVideoGraphWrapper.access$2600(this.this$0))).renderOutputFrame(-2);
        }

        @Override // androidx.media3.exoplayer.video.VideoFrameRenderControl.FrameRenderer
        public void onVideoSizeChanged(VideoSize r4) {
            PlaybackVideoGraphWrapper.access$2102(this.this$0, new Format.Builder().setWidth(r4.width).setHeight(r4.height).setSampleMimeType(MimeTypes.VIDEO_RAW).build());
            Iterator r0 = PlaybackVideoGraphWrapper.access$2200(this.this$0).iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            ((Listener) r0.next()).onVideoSizeChanged(this.this$0, r4);
            goto L4
        }

        @Override // androidx.media3.exoplayer.video.VideoFrameRenderControl.FrameRenderer
        public void renderFrame(long r9, long r11, long r13, boolean r15) {
            if (r15 == false) goto L10;
            if (PlaybackVideoGraphWrapper.access$2300(this.this$0) == null) goto L10;
            Iterator r0 = PlaybackVideoGraphWrapper.access$2200(this.this$0).iterator();
        L7:
            if (r0.hasNext() == false) goto L10;
            ((Listener) r0.next()).onFirstFrameRendered(this.this$0);
        L10:
            if (PlaybackVideoGraphWrapper.access$2400(this.this$0) == null) goto L17;
            if (PlaybackVideoGraphWrapper.access$2100(this.this$0) != null) goto L14;
            Format r1 = new Format.Builder().build();
        L15:
            Format r6 = r1;
            PlaybackVideoGraphWrapper.access$2400(this.this$0).onVideoFrameAboutToBeRendered(r11, PlaybackVideoGraphWrapper.access$2500(this.this$0).nanoTime(), r6, null);
            goto L17
        L14:
            r1 = PlaybackVideoGraphWrapper.access$2100(this.this$0);
        L17:
            ((PreviewingVideoGraph) Assertions.checkStateNotNull(PlaybackVideoGraphWrapper.access$2600(this.this$0))).renderOutputFrame(r9);
        }
    }

    private final class InputVideoSink implements VideoSink, Listener {
        private long finalBufferPresentationTimeUs;
        private final VideoFrameReleaseControl.FrameReleaseInfo frameReleaseInfo;
        private boolean hasRegisteredFirstInputStream;
        private long inputBufferTimestampAdjustmentUs;
        private Format inputFormat;
        private long inputStreamOffsetUs;
        private long inputStreamStartPositionUs;
        private int inputType;
        private boolean isInputStreamChangePending;
        private long lastBufferPresentationTimeUs;
        private long lastResetPositionUs;
        private VideoSink.Listener listener;
        private Executor listenerExecutor;
        private long pendingInputStreamBufferPresentationTimeUs;
        private boolean pendingInputStreamOffsetChange;
        final /* synthetic */ PlaybackVideoGraphWrapper this$0;
        private final ArrayList<Effect> videoEffects;
        private VideoFrameProcessor videoFrameProcessor;
        private final int videoFrameProcessorMaxPendingFrameCount;

        public InputVideoSink(PlaybackVideoGraphWrapper r3, Context r4) {
            this.this$0 = r3;
            this.videoFrameProcessorMaxPendingFrameCount = Util.getMaxPendingFramesCountForMediaCodecDecoders(r4);
            this.videoEffects = new ArrayList();
            this.frameReleaseInfo = new VideoFrameReleaseControl.FrameReleaseInfo();
            this.finalBufferPresentationTimeUs = C0180C.TIME_UNSET;
            this.lastBufferPresentationTimeUs = C0180C.TIME_UNSET;
            this.listener = VideoSink.Listener.NO_OP;
            this.listenerExecutor = PlaybackVideoGraphWrapper.access$800();
        }

        private void maybeRegisterInputStream() {
            if (this.inputFormat != null) goto L5;
            return;
        L5:
            ArrayList r0 = new ArrayList(this.videoEffects);
            Format r1 = (Format) Assertions.checkNotNull(this.inputFormat);
            ((VideoFrameProcessor) Assertions.checkStateNotNull(this.videoFrameProcessor)).registerInputStream(this.inputType, r0, new FrameInfo.Builder(PlaybackVideoGraphWrapper.access$2000(r1.colorInfo), r1.width, r1.height).setPixelWidthHeightRatio(r1.pixelWidthHeightRatio).build());
            this.finalBufferPresentationTimeUs = C0180C.TIME_UNSET;
        }

        private boolean maybeRegisterPendingInputStream() {
            if (this.isInputStreamChangePending == true) goto L6;
            return true;
        L6:
            if (this.pendingInputStreamBufferPresentationTimeUs != C0180C.TIME_UNSET) goto L8;
        L11:
            maybeRegisterInputStream();
            this.isInputStreamChangePending = false;
            this.pendingInputStreamBufferPresentationTimeUs = C0180C.TIME_UNSET;
            return true;
        L8:
            if (PlaybackVideoGraphWrapper.access$1300(this.this$0, this.pendingInputStreamBufferPresentationTimeUs) == true) goto L11;
            return false;
        }

        private void maybeSetStreamOffsetChange(long r9) {
            if (this.pendingInputStreamOffsetChange == false) goto L6;
            PlaybackVideoGraphWrapper.access$1900(this.this$0, this.inputBufferTimestampAdjustmentUs, r9, this.inputStreamOffsetUs);
            this.pendingInputStreamOffsetChange = false;
            return;
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void clearOutputSurfaceInfo() {
            this.this$0.clearOutputSurfaceInfo();
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void enableMayRenderStartOfStream() {
            PlaybackVideoGraphWrapper.access$900(this.this$0).enableMayRenderStartOfStream();
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void flush(boolean r4) {
            if (isInitialized() == false) goto L5;
            this.videoFrameProcessor.flush();
        L5:
            this.hasRegisteredFirstInputStream = false;
            this.finalBufferPresentationTimeUs = C0180C.TIME_UNSET;
            this.lastBufferPresentationTimeUs = C0180C.TIME_UNSET;
            PlaybackVideoGraphWrapper.access$1100(this.this$0, r4);
            this.pendingInputStreamBufferPresentationTimeUs = C0180C.TIME_UNSET;
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public Surface getInputSurface() {
            Assertions.checkState(isInitialized());
            return ((VideoFrameProcessor) Assertions.checkStateNotNull(this.videoFrameProcessor)).getInputSurface();
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public boolean handleInputBitmap(Bitmap r9, TimestampIterator r10) {
            Assertions.checkState(isInitialized());
            boolean r1 = false;
            if (maybeRegisterPendingInputStream() == true) goto L6;
            return false;
        L6:
            if (((VideoFrameProcessor) Assertions.checkStateNotNull(this.videoFrameProcessor)).queueInputBitmap(r9, r10) == true) goto L8;
            return false;
        L8:
            TimestampIterator r0 = r10.copyOf();
            long r2 = r0.next();
            long r4 = r0.getLastTimestampUs() - this.inputBufferTimestampAdjustmentUs;
            if (r4 == C0180C.TIME_UNSET) goto L11;
            r1 = true;
        L11:
            Assertions.checkState(r1);
            maybeSetStreamOffsetChange(r2);
            this.lastBufferPresentationTimeUs = r4;
            this.finalBufferPresentationTimeUs = r4;
            return true;
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public boolean handleInputFrame(long r15, boolean r17, long r18, long r20, VideoSink.VideoFrameHandler r22) throws VideoSink.VideoSinkException {
            Assertions.checkState(isInitialized());
            long r3 = r15 - this.inputBufferTimestampAdjustmentUs;
            if (PlaybackVideoGraphWrapper.access$1400(this.this$0).getFrameReleaseAction(r3, r18, r20, this.inputStreamStartPositionUs, r17, this.frameReleaseInfo) != 4) goto L10;
            return false;
        L10:
            if (r3 >= this.lastResetPositionUs) goto L14;
            if (r17 == true) goto L14;
            r22.skip();
            return true;
        L14:
            render(r18, r20);
            if (this.isInputStreamChangePending == false) goto L24;
            if (this.pendingInputStreamBufferPresentationTimeUs != C0180C.TIME_UNSET) goto L19;
        L22:
            maybeRegisterInputStream();
            this.isInputStreamChangePending = false;
            this.pendingInputStreamBufferPresentationTimeUs = C0180C.TIME_UNSET;
            goto L24
        L19:
            if (PlaybackVideoGraphWrapper.access$1300(this.this$0, this.pendingInputStreamBufferPresentationTimeUs) == true) goto L22;
            return false;
        L24:
            if (((VideoFrameProcessor) Assertions.checkStateNotNull(this.videoFrameProcessor)).getPendingInputFrameCount() < this.videoFrameProcessorMaxPendingFrameCount) goto L27;
            return false;
        L27:
            if (((VideoFrameProcessor) Assertions.checkStateNotNull(this.videoFrameProcessor)).registerInputFrame() == true) goto L29;
            return false;
        L29:
            maybeSetStreamOffsetChange(r3);
            this.lastBufferPresentationTimeUs = r3;
            if (r17 == false) goto L32;
            this.finalBufferPresentationTimeUs = r3;
        L32:
            r22.render(1000 * r15);
            return true;
        L34:
            e = e;
        L39:
            throw new VideoSink.VideoSinkException(e, (Format) Assertions.checkStateNotNull(this.inputFormat));
        L36:
            e = e;
            goto L39
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void initialize(Format r2) throws VideoSink.VideoSinkException {
            Assertions.checkState(!isInitialized());
            this.videoFrameProcessor = PlaybackVideoGraphWrapper.access$1000(this.this$0, r2);
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public boolean isEnded() {
            if (isInitialized() == true) goto L5;
        L9:
            return false;
        L5:
            if (this.finalBufferPresentationTimeUs == C0180C.TIME_UNSET) goto L9;
            if (PlaybackVideoGraphWrapper.access$1300(this.this$0, this.finalBufferPresentationTimeUs) == false) goto L9;
            return true;
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        @EnsuresNonNullIf(expression = {"videoFrameProcessor"}, result = true)
        public boolean isInitialized() {
            if (this.videoFrameProcessor == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public boolean isReady(boolean r3) {
            PlaybackVideoGraphWrapper r0 = this.this$0;
            if (r3 == true) goto L5;
        L7:
            boolean r1 = false;
        L9:
            return PlaybackVideoGraphWrapper.access$1200(r0, r1);
        L5:
            if (isInitialized() == false) goto L7;
            r1 = true;
            goto L9
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void join(boolean r2) {
            PlaybackVideoGraphWrapper.access$900(this.this$0).join(r2);
        }

        /* JADX INFO: renamed from: lambda$onError$3$androidx-media3-exoplayer-video-PlaybackVideoGraphWrapper$InputVideoSink */
        /* synthetic */ void m150x4b60f8c8(VideoSink.Listener r3, VideoFrameProcessingException r4) {
            r3.onError(this, new VideoSink.VideoSinkException(r4, (Format) Assertions.checkStateNotNull(this.inputFormat)));
        }

        /* JADX INFO: renamed from: lambda$onFirstFrameRendered$0$androidx-media3-exoplayer-video-PlaybackVideoGraphWrapper$InputVideoSink */
        /* synthetic */ void m151x2299ee49(VideoSink.Listener r1) {
            r1.onFirstFrameRendered(this);
        }

        /* JADX INFO: renamed from: lambda$onFrameDropped$1$androidx-media3-exoplayer-video-PlaybackVideoGraphWrapper$InputVideoSink */
        /* synthetic */ void m152x39948ea7(VideoSink.Listener r2) {
            r2.onFrameDropped((VideoSink) Assertions.checkStateNotNull(this));
        }

        /* JADX INFO: renamed from: lambda$onVideoSizeChanged$2$androidx-media3-exoplayer-video-PlaybackVideoGraphWrapper$InputVideoSink */
        /* synthetic */ void m153x47b4e41(VideoSink.Listener r1, VideoSize r2) {
            r1.onVideoSizeChanged(this, r2);
        }

        @Override // androidx.media3.exoplayer.video.PlaybackVideoGraphWrapper.Listener
        public void onError(PlaybackVideoGraphWrapper r4, final VideoFrameProcessingException r5) {
            final VideoSink.Listener r0 = this.listener;
            this.listenerExecutor.execute(new RunnableC0386xb9ef0033(this, r0, r5));
        }

        @Override // androidx.media3.exoplayer.video.PlaybackVideoGraphWrapper.Listener
        public void onFirstFrameRendered(PlaybackVideoGraphWrapper r4) {
            final VideoSink.Listener r0 = this.listener;
            this.listenerExecutor.execute(new RunnableC0387xb9ef0034(this, r0));
        }

        @Override // androidx.media3.exoplayer.video.PlaybackVideoGraphWrapper.Listener
        public void onFrameDropped(PlaybackVideoGraphWrapper r4) {
            final VideoSink.Listener r0 = this.listener;
            this.listenerExecutor.execute(new RunnableC0388xb9ef0035(this, r0));
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void onInputStreamChanged(int r8, Format r9) {
            Assertions.checkState(isInitialized());
            switch(r8) {
                case 1: goto L6;
                case 2: goto L6;
                default: goto L5;
            };
        L6:
            PlaybackVideoGraphWrapper.access$1400(this.this$0).setFrameRate(r9.frameRate);
            this.inputType = r8;
            this.inputFormat = r9;
            boolean r1 = false;
            if (this.hasRegisteredFirstInputStream == true) goto L10;
            maybeRegisterInputStream();
            this.hasRegisteredFirstInputStream = true;
            this.isInputStreamChangePending = false;
            this.pendingInputStreamBufferPresentationTimeUs = C0180C.TIME_UNSET;
            return;
        L10:
            if (this.lastBufferPresentationTimeUs == C0180C.TIME_UNSET) goto L12;
            r1 = true;
        L12:
            Assertions.checkState(r1);
            this.isInputStreamChangePending = true;
            this.pendingInputStreamBufferPresentationTimeUs = this.lastBufferPresentationTimeUs;
            return;
        L5:
            throw new UnsupportedOperationException("Unsupported input type " + r8);
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void onRendererDisabled() {
            PlaybackVideoGraphWrapper.access$900(this.this$0).onRendererDisabled();
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void onRendererEnabled(boolean r2) {
            PlaybackVideoGraphWrapper.access$900(this.this$0).onRendererEnabled(r2);
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void onRendererStarted() {
            PlaybackVideoGraphWrapper.access$900(this.this$0).onRendererStarted();
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void onRendererStopped() {
            PlaybackVideoGraphWrapper.access$900(this.this$0).onRendererStopped();
        }

        @Override // androidx.media3.exoplayer.video.PlaybackVideoGraphWrapper.Listener
        public void onVideoSizeChanged(PlaybackVideoGraphWrapper r4, final VideoSize r5) {
            final VideoSink.Listener r0 = this.listener;
            this.listenerExecutor.execute(new RunnableC0385xb9ef0032(this, r0, r5));
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void release() {
            this.this$0.release();
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void render(long r4, long r6) throws VideoSink.VideoSinkException {
            PlaybackVideoGraphWrapper.access$1800(this.this$0, r4, r6);     // Catch: ExoPlaybackException -> L4
            return;
        L4:
            e = move-exception;
            if (this.inputFormat == null) goto L8;
            Format r2 = this.inputFormat;
        L10:
            throw new VideoSink.VideoSinkException(e, r2);
        L8:
            r2 = new Format.Builder().build();
            goto L10
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void setChangeFrameRateStrategy(int r2) {
            PlaybackVideoGraphWrapper.access$900(this.this$0).setChangeFrameRateStrategy(r2);
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void setListener(VideoSink.Listener r1, Executor r2) {
            this.listener = r1;
            this.listenerExecutor = r2;
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void setOutputSurfaceInfo(Surface r2, Size r3) {
            this.this$0.setOutputSurfaceInfo(r2, r3);
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void setPendingVideoEffects(List<Effect> r3) {
            this.videoEffects.clear();
            this.videoEffects.addAll(r3);
            this.videoEffects.addAll(PlaybackVideoGraphWrapper.access$1700(this.this$0));
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void setPlaybackSpeed(float r2) {
            PlaybackVideoGraphWrapper.access$1600(this.this$0, r2);
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void setStreamTimestampInfo(long r4, long r6, long r8, long r10) {
            boolean r0 = this.pendingInputStreamOffsetChange;
            if (this.inputStreamOffsetUs == r6) goto L5;
        L8:
            boolean r1 = true;
        L9:
            this.pendingInputStreamOffsetChange = r0 | r1;
            this.inputStreamStartPositionUs = r4;
            this.inputStreamOffsetUs = r6;
            this.inputBufferTimestampAdjustmentUs = r8;
            this.lastResetPositionUs = r10;
            return;
        L5:
            if (this.inputBufferTimestampAdjustmentUs != r8) goto L8;
            r1 = false;
            goto L9
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void setVideoEffects(List<Effect> r2) {
            if (this.videoEffects.equals(r2) == false) goto L5;
            return;
        L5:
            setPendingVideoEffects(r2);
            maybeRegisterInputStream();
        }

        @Override // androidx.media3.exoplayer.video.VideoSink
        public void setVideoFrameMetadataListener(VideoFrameMetadataListener r2) {
            PlaybackVideoGraphWrapper.access$1500(this.this$0, r2);
        }
    }

    public interface Listener {
        void onError(PlaybackVideoGraphWrapper r1, VideoFrameProcessingException r2);

        void onFirstFrameRendered(PlaybackVideoGraphWrapper r1);

        void onFrameDropped(PlaybackVideoGraphWrapper r1);

        void onVideoSizeChanged(PlaybackVideoGraphWrapper r1, VideoSize r2);
    }

    private static final class ReflectiveDefaultVideoFrameProcessorFactory implements VideoFrameProcessor.Factory {
        private static final Supplier<VideoFrameProcessor.Factory> VIDEO_FRAME_PROCESSOR_FACTORY_SUPPLIER = null;

        static {
            VIDEO_FRAME_PROCESSOR_FACTORY_SUPPLIER = Suppliers.memoize(new C0389xe73bfd20());
        }

        private ReflectiveDefaultVideoFrameProcessorFactory() {
        }

        /* synthetic */ ReflectiveDefaultVideoFrameProcessorFactory(C03841 r1) {
            this();
        }

        static /* synthetic */ VideoFrameProcessor.Factory lambda$static$0() {
            Class<?> r0 = Class.forName("androidx.media3.effect.DefaultVideoFrameProcessor$Factory$Builder");     // Catch: Exception -> L4
            Object r2 = r0.getConstructor(new Class[0]).newInstance(new Object[0]);     // Catch: Exception -> L4
            return (VideoFrameProcessor.Factory) Assertions.checkNotNull(r0.getMethod("build", new Class[0]).invoke(r2, new Object[0]));
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // androidx.media3.common.VideoFrameProcessor.Factory
        public VideoFrameProcessor create(Context r9, DebugViewProvider r10, ColorInfo r11, boolean r12, Executor r13, VideoFrameProcessor.Listener r14) throws VideoFrameProcessingException {
            return VIDEO_FRAME_PROCESSOR_FACTORY_SUPPLIER.get().create(r9, r10, r11, r12, r13, r14);
        }
    }

    private static final class ReflectivePreviewingSingleInputVideoGraphFactory implements PreviewingVideoGraph.Factory {
        private final VideoFrameProcessor.Factory videoFrameProcessorFactory;

        public ReflectivePreviewingSingleInputVideoGraphFactory(VideoFrameProcessor.Factory r1) {
            this.videoFrameProcessorFactory = r1;
        }

        @Override // androidx.media3.common.PreviewingVideoGraph.Factory
        public PreviewingVideoGraph create(Context r12, ColorInfo r13, DebugViewProvider r14, VideoGraph.Listener r15, Executor r16, List<Effect> r17, long r18) throws VideoFrameProcessingException {
            return ((PreviewingVideoGraph.Factory) Class.forName("androidx.media3.effect.PreviewingSingleInputVideoGraph$Factory").getConstructor(new Class[]{VideoFrameProcessor.Factory.class}).newInstance(new Object[]{this.videoFrameProcessorFactory})).create(r12, r13, r14, r15, r16, r17, r18);
        L4:
            e = move-exception;
            throw VideoFrameProcessingException.from(e);
        }
    }

    static {
        NO_OP_EXECUTOR = new PlaybackVideoGraphWrapper$$ExternalSyntheticLambda1();
    }

    private PlaybackVideoGraphWrapper(Builder r4) {
        this.context = Builder.access$200(r4);
        this.inputVideoSink = new InputVideoSink(this, this.context);
        this.clock = Builder.access$300(r4);
        this.videoFrameReleaseControl = Builder.access$400(r4);
        this.videoFrameReleaseControl.setClock(this.clock);
        this.videoFrameRenderControl = new VideoFrameRenderControl(new FrameRendererImpl(this, null), this.videoFrameReleaseControl);
        this.previewingVideoGraphFactory = (PreviewingVideoGraph.Factory) Assertions.checkStateNotNull(Builder.access$600(r4));
        this.compositionEffects = Builder.access$700(r4);
        this.defaultVideoSink = new DefaultVideoSink(this.videoFrameReleaseControl, this.videoFrameRenderControl);
        this.listeners = new CopyOnWriteArraySet();
        this.state = 0;
        addListener(this.inputVideoSink);
    }

    /* synthetic */ PlaybackVideoGraphWrapper(Builder r1, C03841 r2) {
        this(r1);
    }

    static /* synthetic */ VideoFrameProcessor access$1000(PlaybackVideoGraphWrapper r1, Format r2) throws VideoSink.VideoSinkException {
        return r1.initialize(r2);
    }

    static /* synthetic */ void access$1100(PlaybackVideoGraphWrapper r0, boolean r1) {
        r0.flush(r1);
    }

    static /* synthetic */ boolean access$1200(PlaybackVideoGraphWrapper r1, boolean r2) {
        return r1.isReady(r2);
    }

    static /* synthetic */ boolean access$1300(PlaybackVideoGraphWrapper r1, long r2) {
        return r1.hasReleasedFrame(r2);
    }

    static /* synthetic */ VideoFrameReleaseControl access$1400(PlaybackVideoGraphWrapper r1) {
        return r1.videoFrameReleaseControl;
    }

    static /* synthetic */ void access$1500(PlaybackVideoGraphWrapper r0, VideoFrameMetadataListener r1) {
        r0.setVideoFrameMetadataListener(r1);
    }

    static /* synthetic */ void access$1600(PlaybackVideoGraphWrapper r0, float r1) {
        r0.setPlaybackSpeed(r1);
    }

    static /* synthetic */ List access$1700(PlaybackVideoGraphWrapper r1) {
        return r1.compositionEffects;
    }

    static /* synthetic */ void access$1800(PlaybackVideoGraphWrapper r0, long r1, long r3) throws ExoPlaybackException {
        r0.render(r1, r3);
    }

    static /* synthetic */ void access$1900(PlaybackVideoGraphWrapper r0, long r1, long r3, long r5) {
        r0.onStreamOffsetChange(r1, r3, r5);
    }

    static /* synthetic */ ColorInfo access$2000(ColorInfo r1) {
        return getAdjustedInputColorInfo(r1);
    }

    static /* synthetic */ Format access$2100(PlaybackVideoGraphWrapper r1) {
        return r1.outputFormat;
    }

    static /* synthetic */ Format access$2102(PlaybackVideoGraphWrapper r0, Format r1) {
        r0.outputFormat = r1;
        return r1;
    }

    static /* synthetic */ CopyOnWriteArraySet access$2200(PlaybackVideoGraphWrapper r1) {
        return r1.listeners;
    }

    static /* synthetic */ Pair access$2300(PlaybackVideoGraphWrapper r1) {
        return r1.currentSurfaceAndSize;
    }

    static /* synthetic */ VideoFrameMetadataListener access$2400(PlaybackVideoGraphWrapper r1) {
        return r1.videoFrameMetadataListener;
    }

    static /* synthetic */ Clock access$2500(PlaybackVideoGraphWrapper r1) {
        return r1.clock;
    }

    static /* synthetic */ PreviewingVideoGraph access$2600(PlaybackVideoGraphWrapper r1) {
        return r1.videoGraph;
    }

    static /* synthetic */ Executor access$800() {
        return NO_OP_EXECUTOR;
    }

    static /* synthetic */ VideoSink access$900(PlaybackVideoGraphWrapper r1) {
        return r1.defaultVideoSink;
    }

    private void flush(boolean r3) {
        if (isInitialized() == true) goto L5;
        return;
    L5:
        this.pendingFlushCount++;
        this.defaultVideoSink.flush(r3);
        ((HandlerWrapper) Assertions.checkStateNotNull(this.handler)).post(new PlaybackVideoGraphWrapper$$ExternalSyntheticLambda0(this));
    }

    private static ColorInfo getAdjustedInputColorInfo(ColorInfo r1) {
        if (r1 == null) goto L8;
        if (r1.isDataSpaceValid() == false) goto L8;
        return r1;
    L8:
        return ColorInfo.SDR_BT709_LIMITED;
    }

    private boolean hasReleasedFrame(long r2) {
        if (this.pendingFlushCount == 0) goto L5;
    L7:
        return false;
    L5:
        if (this.videoFrameRenderControl.hasReleasedFrame(r2) == false) goto L7;
        return true;
    }

    private VideoFrameProcessor initialize(Format r14) throws VideoSink.VideoSinkException {
        if (this.state != 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        ColorInfo r3 = getAdjustedInputColorInfo(r14.colorInfo);
        if (r3.colorTransfer == 7) goto L9;
    L11:
        ColorInfo r6 = r3;
    L12:
        this.handler = this.clock.createHandler((Looper) Assertions.checkStateNotNull(Looper.myLooper()), null);
        PreviewingVideoGraph.Factory r4 = this.previewingVideoGraphFactory;     // Catch: VideoFrameProcessingException -> L23
        Context r5 = this.context;     // Catch: VideoFrameProcessingException -> L23
        DebugViewProvider r7 = DebugViewProvider.NONE;     // Catch: VideoFrameProcessingException -> L23
        final HandlerWrapper r1 = this.handler;     // Catch: VideoFrameProcessingException -> L23
        Objects.requireNonNull(r1);     // Catch: VideoFrameProcessingException -> L23
        this.videoGraph = r4.create(r5, r6, r7, this, new PlaybackVideoGraphWrapper$$ExternalSyntheticLambda2(r1), ImmutableList.m212of(), 0);     // Catch: VideoFrameProcessingException -> L21
        if (this.currentSurfaceAndSize == null) goto L18;
        Surface r2 = (Surface) this.currentSurfaceAndSize.first;     // Catch: VideoFrameProcessingException -> L21
        Size r8 = (Size) this.currentSurfaceAndSize.second;     // Catch: VideoFrameProcessingException -> L21
        maybeSetOutputSurfaceInfo(r2, r8.getWidth(), r8.getHeight());     // Catch: VideoFrameProcessingException -> L21
    L18:
        this.videoGraph.registerInput(0);     // Catch: VideoFrameProcessingException -> L21
        this.defaultVideoSink.initialize(r14);
        this.state = 1;
        return this.videoGraph.getProcessor(0);
    L21:
        e = e;
    L26:
        throw new VideoSink.VideoSinkException(e, r14);
    L23:
        e = e;
        goto L26
    L9:
        if (Util.SDK_INT >= 34) goto L11;
        r6 = r3.buildUpon().setColorTransfer(6).build();
        goto L12
    L5:
        r0 = false;
        goto L6
    }

    private boolean isInitialized() {
        if (this.state == 1) goto L7;
        return false;
    L7:
        return true;
    }

    private boolean isReady(boolean r3) {
        VideoSink r0 = this.defaultVideoSink;
        if (r3 == true) goto L5;
    L7:
        boolean r1 = false;
    L9:
        return r0.isReady(r1);
    L5:
        if (this.pendingFlushCount != 0) goto L7;
        r1 = true;
        goto L9
    }

    static /* synthetic */ void lambda$static$0(Runnable r0) {
    }

    private void maybeSetOutputSurfaceInfo(Surface r3, int r4, int r5) {
        if (this.videoGraph != null) goto L5;
        return;
    L5:
        if (r3 == null) goto L7;
        this.videoGraph.setOutputSurfaceInfo(new SurfaceInfo(r3, r4, r5));
        this.defaultVideoSink.setOutputSurfaceInfo(r3, new Size(r4, r5));
        return;
    L7:
        this.videoGraph.setOutputSurfaceInfo(null);
        this.defaultVideoSink.clearOutputSurfaceInfo();
    }

    private void onStreamOffsetChange(long r2, long r4, long r6) {
        this.bufferTimestampAdjustmentUs = r2;
        this.videoFrameRenderControl.onStreamOffsetChange(r4, r6);
    }

    private void render(long r2, long r4) throws ExoPlaybackException {
        this.videoFrameRenderControl.render(r2, r4);
    }

    private void setPlaybackSpeed(float r2) {
        this.defaultVideoSink.setPlaybackSpeed(r2);
    }

    private void setVideoFrameMetadataListener(VideoFrameMetadataListener r1) {
        this.videoFrameMetadataListener = r1;
    }

    public void addListener(Listener r2) {
        this.listeners.add(r2);
    }

    @Override // androidx.media3.exoplayer.video.VideoSinkProvider
    public void clearOutputSurfaceInfo() {
        maybeSetOutputSurfaceInfo(null, Size.UNKNOWN.getWidth(), Size.UNKNOWN.getHeight());
        this.currentSurfaceAndSize = null;
    }

    @Override // androidx.media3.exoplayer.video.VideoSinkProvider
    public VideoSink getSink() {
        return this.inputVideoSink;
    }

    /* JADX INFO: renamed from: lambda$flush$1$androidx-media3-exoplayer-video-PlaybackVideoGraphWrapper */
    /* synthetic */ void m149x92e2e5d9() {
        this.pendingFlushCount--;
    }

    @Override // androidx.media3.common.VideoGraph.Listener
    public void onEnded(long r2) {
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.common.VideoGraph.Listener
    public void onError(VideoFrameProcessingException r3) {
        Iterator<Listener> r0 = this.listeners.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().onError(this, r3);
        goto L4
    }

    @Override // androidx.media3.common.VideoGraph.Listener
    public void onOutputFrameAvailableForRendering(long r4) {
        if (this.pendingFlushCount <= 0) goto L5;
        return;
    L5:
        this.videoFrameRenderControl.onOutputFrameAvailableForRendering(r4 - this.bufferTimestampAdjustmentUs);
    }

    @Override // androidx.media3.common.VideoGraph.Listener
    public void onOutputSizeChanged(int r4, int r5) {
        Format r0 = new Format.Builder().setWidth(r4).setHeight(r5).build();
        this.defaultVideoSink.onInputStreamChanged(1, r0);
    }

    @Override // androidx.media3.exoplayer.video.VideoSinkProvider
    public void release() {
        if (this.state != 2) goto L6;
        return;
    L6:
        if (this.handler == null) goto L9;
        this.handler.removeCallbacksAndMessages(null);
    L9:
        if (this.videoGraph == null) goto L11;
        this.videoGraph.release();
    L11:
        this.currentSurfaceAndSize = null;
        this.state = 2;
    }

    public void removeListener(Listener r2) {
        this.listeners.remove(r2);
    }

    @Override // androidx.media3.exoplayer.video.VideoSinkProvider
    public void setOutputSurfaceInfo(Surface r3, Size r4) {
        if (this.currentSurfaceAndSize != null) goto L5;
    L9:
        this.currentSurfaceAndSize = Pair.create(r3, r4);
        maybeSetOutputSurfaceInfo(r3, r4.getWidth(), r4.getHeight());
        return;
    L5:
        if (((Surface) this.currentSurfaceAndSize.first).equals(r3) == false) goto L9;
        if (((Size) this.currentSurfaceAndSize.second).equals(r4) == false) goto L9;
    }
}
