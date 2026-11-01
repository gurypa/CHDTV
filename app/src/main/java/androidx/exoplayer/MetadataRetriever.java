package androidx.media3.exoplayer;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.HandlerWrapper;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.SequenceableLoader;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.DefaultAllocator;
import androidx.media3.extractor.DefaultExtractorsFactory;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public final class MetadataRetriever {
    public static final int DEFAULT_MAXIMUM_PARALLEL_RETRIEVALS = 5;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.MetadataRetriever$1 */
    static /* synthetic */ class C02481 {
    }

    private static final class MetadataRetrieverInternal {
        private static final int MESSAGE_CHECK_FOR_FAILURE = 2;
        private static final int MESSAGE_CONTINUE_LOADING = 3;
        private static final int MESSAGE_PREPARE_SOURCE = 1;
        private static final int MESSAGE_RELEASE = 4;
        private static final SharedWorkerThread SHARED_WORKER_THREAD = null;
        private final MediaItem mediaItem;
        private final MediaSource.Factory mediaSourceFactory;
        private final HandlerWrapper mediaSourceHandler;
        private final SettableFuture<TrackGroupArray> trackGroupsFuture;

        private final class MediaSourceHandlerCallback implements Handler.Callback {
            private static final int ERROR_POLL_INTERVAL_MS = 100;
            private MediaPeriod mediaPeriod;
            private MediaSource mediaSource;
            private final MediaSourceCaller mediaSourceCaller;
            final /* synthetic */ MetadataRetrieverInternal this$0;

            private final class MediaSourceCaller implements MediaSource.MediaSourceCaller {
                private final Allocator allocator;
                private final MediaPeriodCallback mediaPeriodCallback;
                private boolean mediaPeriodCreated;
                final /* synthetic */ MediaSourceHandlerCallback this$1;

                private final class MediaPeriodCallback implements MediaPeriod.Callback {
                    final /* synthetic */ MediaSourceCaller this$2;

                    private MediaPeriodCallback(MediaSourceCaller r1) {
                        this.this$2 = r1;
                    }

                    /* synthetic */ MediaPeriodCallback(MediaSourceCaller r1, C02481 r2) {
                        this(r1);
                    }

                    public void onContinueLoadingRequested(MediaPeriod r3) {
                        MetadataRetrieverInternal.access$100(this.this$2.this$1.this$0).obtainMessage(3).sendToTarget();
                    }

                    @Override // androidx.media3.exoplayer.source.SequenceableLoader.Callback
                    public /* bridge */ /* synthetic */ void onContinueLoadingRequested(SequenceableLoader r1) {
                        onContinueLoadingRequested((MediaPeriod) r1);
                    }

                    @Override // androidx.media3.exoplayer.source.MediaPeriod.Callback
                    public void onPrepared(MediaPeriod r3) {
                        MetadataRetrieverInternal.access$200(this.this$2.this$1.this$0).set(r3.getTrackGroups());
                        MetadataRetrieverInternal.access$100(this.this$2.this$1.this$0).obtainMessage(4).sendToTarget();
                    }
                }

                public MediaSourceCaller(MediaSourceHandlerCallback r3) {
                    this.this$1 = r3;
                    this.mediaPeriodCallback = new MediaPeriodCallback(this, null);
                    this.allocator = new DefaultAllocator(true, 65536);
                }

                @Override // androidx.media3.exoplayer.source.MediaSource.MediaSourceCaller
                public void onSourceInfoRefreshed(MediaSource r6, Timeline r7) {
                    if (this.mediaPeriodCreated == false) goto L5;
                    return;
                L5:
                    this.mediaPeriodCreated = true;
                    MediaSourceHandlerCallback.access$502(this.this$1, r6.createPeriod(new MediaSource.MediaPeriodId(r7.getUidOfPeriod(0)), this.allocator, 0));
                    MediaSourceHandlerCallback.access$500(this.this$1).prepare(this.mediaPeriodCallback, 0);
                }
            }

            public MediaSourceHandlerCallback(MetadataRetrieverInternal r1) {
                this.this$0 = r1;
                this.mediaSourceCaller = new MediaSourceCaller(this);
            }

            static /* synthetic */ MediaPeriod access$500(MediaSourceHandlerCallback r1) {
                return r1.mediaPeriod;
            }

            static /* synthetic */ MediaPeriod access$502(MediaSourceHandlerCallback r0, MediaPeriod r1) {
                r0.mediaPeriod = r1;
                return r1;
            }

            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message r8) {
                switch(r8.what) {
                    case 1: goto L22;
                    case 2: goto L24;
                    case 3: goto L11;
                    case 4: goto L7;
                    default: goto L4;
                };
            L4:
                return false;
            L11:
                ((MediaPeriod) Assertions.checkNotNull(this.mediaPeriod)).continueLoading(new LoadingInfo.Builder().setPlaybackPositionUs(0).build());
                return true;
            L22:
                MediaItem r0 = (MediaItem) r8.obj;
                this.mediaSource = MetadataRetrieverInternal.access$000(this.this$0).createMediaSource(r0);
                this.mediaSource.prepareSource(this.mediaSourceCaller, null, PlayerId.UNSET);
                MetadataRetrieverInternal.access$100(this.this$0).sendEmptyMessage(2);
                return true;
            L24:
            L19:
                e = move-exception;
                MetadataRetrieverInternal.access$200(this.this$0).setException(e);
                MetadataRetrieverInternal.access$100(this.this$0).obtainMessage(4).sendToTarget();
            L21:
                return true;
            L14:
                if (this.mediaPeriod != null) goto L16;
                ((MediaSource) Assertions.checkNotNull(this.mediaSource)).maybeThrowSourceInfoRefreshError();     // Catch: Exception -> L19
            L17:
                MetadataRetrieverInternal.access$100(this.this$0).sendEmptyMessageDelayed(2, 100);     // Catch: Exception -> L19
                goto L21
            L16:
                this.mediaPeriod.maybeThrowPrepareError();     // Catch: Exception -> L19
                goto L17
            L7:
                if (this.mediaPeriod == null) goto L9;
                ((MediaSource) Assertions.checkNotNull(this.mediaSource)).releasePeriod(this.mediaPeriod);
            L9:
                ((MediaSource) Assertions.checkNotNull(this.mediaSource)).releaseSource(this.mediaSourceCaller);
                MetadataRetrieverInternal.access$100(this.this$0).removeCallbacksAndMessages(null);
                MetadataRetrieverInternal.access$300().removeWorker();
                return true;
            }
        }

        static {
            SHARED_WORKER_THREAD = new SharedWorkerThread();
        }

        public MetadataRetrieverInternal(MediaSource.Factory r3, MediaItem r4, Clock r5) {
            this.mediaSourceFactory = r3;
            this.mediaItem = r4;
            this.mediaSourceHandler = r5.createHandler(SHARED_WORKER_THREAD.addWorker(), new MediaSourceHandlerCallback(this));
            this.trackGroupsFuture = SettableFuture.create();
        }

        static /* synthetic */ MediaSource.Factory access$000(MetadataRetrieverInternal r1) {
            return r1.mediaSourceFactory;
        }

        static /* synthetic */ HandlerWrapper access$100(MetadataRetrieverInternal r1) {
            return r1.mediaSourceHandler;
        }

        static /* synthetic */ SettableFuture access$200(MetadataRetrieverInternal r1) {
            return r1.trackGroupsFuture;
        }

        static /* synthetic */ SharedWorkerThread access$300() {
            return SHARED_WORKER_THREAD;
        }

        public ListenableFuture<TrackGroupArray> retrieveMetadata() {
            SHARED_WORKER_THREAD.startRetrieval(this);
            return this.trackGroupsFuture;
        }

        public void start() {
            this.mediaSourceHandler.obtainMessage(1, this.mediaItem).sendToTarget();
        }
    }

    private static final class SharedWorkerThread {
        public static final AtomicInteger MAX_PARALLEL_RETRIEVALS = null;
        private HandlerThread mediaSourceThread;
        private final Deque<MetadataRetrieverInternal> pendingRetrievals;
        private int referenceCount;

        static {
            MAX_PARALLEL_RETRIEVALS = new AtomicInteger(5);
        }

        public SharedWorkerThread() {
            this.pendingRetrievals = new ArrayDeque();
        }

        private void maybeStartNewRetrieval() {
            if (this.pendingRetrievals.isEmpty() == false) goto L6;
            return;
        L6:
            if ((this.referenceCount - this.pendingRetrievals.size()) >= MAX_PARALLEL_RETRIEVALS.get()) goto L9;
            this.pendingRetrievals.removeFirst().start();
            return;
        }

        public synchronized Looper addWorker() {
            monitor-enter(this);
        L13:
            th = move-exception;
            throw th;
        L4:
            if (this.mediaSourceThread == null) goto L6;
        L10:
            this.referenceCount++;     // Catch: Throwable -> L13
            Looper r0 = this.mediaSourceThread.getLooper();     // Catch: Throwable -> L13
            monitor-exit(this);
            return r0;
        L6:
            if (this.referenceCount != 0) goto L8;
            boolean r1 = true;
        L9:
            Assertions.checkState(r1);     // Catch: Throwable -> L13
            this.mediaSourceThread = new HandlerThread("ExoPlayer:MetadataRetriever");     // Catch: Throwable -> L13
            this.mediaSourceThread.start();     // Catch: Throwable -> L13
            goto L10
        L8:
            r1 = false;
            goto L9
        }

        public synchronized void removeWorker() {
            monitor-enter(this);
            int r0 = this.referenceCount - 1;     // Catch: Throwable -> L9
            this.referenceCount = r0;     // Catch: Throwable -> L9
            if (r0 != 0) goto L6;
            ((HandlerThread) Assertions.checkNotNull(this.mediaSourceThread)).quit();     // Catch: Throwable -> L9
            this.mediaSourceThread = null;     // Catch: Throwable -> L9
        L7:
            monitor-exit(this);
            return;
        L6:
            maybeStartNewRetrieval();     // Catch: Throwable -> L9
        L9:
            th = move-exception;
            throw th;
        }

        public synchronized void startRetrieval(MetadataRetrieverInternal r2) {
            monitor-enter(this);
            this.pendingRetrievals.addLast(r2);     // Catch: Throwable -> L6
            maybeStartNewRetrieval();     // Catch: Throwable -> L6
            monitor-exit(this);
            return;
        L6:
            th = move-exception;
            throw th;
        }
    }

    private MetadataRetriever() {
    }

    public static ListenableFuture<TrackGroupArray> retrieveMetadata(Context r1, MediaItem r2) {
        return retrieveMetadata(r1, r2, Clock.DEFAULT);
    }

    static ListenableFuture<TrackGroupArray> retrieveMetadata(Context r3, MediaItem r4, Clock r5) {
        return retrieveMetadata(new DefaultMediaSourceFactory(r3, new DefaultExtractorsFactory().setMp4ExtractorFlags(6)), r4, r5);
    }

    public static ListenableFuture<TrackGroupArray> retrieveMetadata(MediaSource.Factory r1, MediaItem r2) {
        return retrieveMetadata(r1, r2, Clock.DEFAULT);
    }

    private static ListenableFuture<TrackGroupArray> retrieveMetadata(MediaSource.Factory r1, MediaItem r2, Clock r3) {
        return new MetadataRetrieverInternal(r1, r2, r3).retrieveMetadata();
    }

    public static void setMaximumParallelRetrievals(int r1) {
        boolean r0 = true;
        if (r1 >= 1) goto L6;
        r0 = false;
    L6:
        Assertions.checkArgument(r0);
        SharedWorkerThread.MAX_PARALLEL_RETRIEVALS.set(r1);
    }
}
