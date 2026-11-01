package androidx.media3.exoplayer.source.preload;

import android.os.Handler;
import android.os.Looper;
import androidx.media3.common.C0180C;
import androidx.media3.common.FlagSet;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.ListenerSet;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.source.MediaSource;
import com.google.common.base.Supplier;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

/* JADX INFO: loaded from: classes.dex */
public abstract class BasePreloadManager<T> {
    private final Handler applicationHandler;
    private final ListenerSet<Listener> listeners;
    private final Object lock;
    private final Map<MediaItem, BasePreloadManager<T>.MediaSourceHolder> mediaItemMediaSourceHolderMap;
    private final MediaSource.Factory mediaSourceFactory;
    protected final Comparator<T> rankingDataComparator;
    private final PriorityQueue<BasePreloadManager<T>.MediaSourceHolder> sourceHolderPriorityQueue;
    private final TargetPreloadStatusControl<T> targetPreloadStatusControl;
    private TargetPreloadStatusControl.PreloadStatus targetPreloadStatusOfCurrentPreloadingSource;

    protected static abstract class BuilderBase<T> {
        protected Supplier<MediaSource.Factory> mediaSourceFactorySupplier;
        protected final Comparator<T> rankingDataComparator;
        protected final TargetPreloadStatusControl<T> targetPreloadStatusControl;

        public BuilderBase(Comparator<T> r1, TargetPreloadStatusControl<T> r2, Supplier<MediaSource.Factory> r3) {
            this.rankingDataComparator = r1;
            this.targetPreloadStatusControl = r2;
            this.mediaSourceFactorySupplier = r3;
        }

        public abstract BasePreloadManager<T> build();
    }

    public interface Listener {
        void onCompleted(MediaItem r1);

        void onError(PreloadException r1);
    }

    private final class MediaSourceHolder implements Comparable<BasePreloadManager<T>.MediaSourceHolder> {
        public final MediaSource mediaSource;
        public final T rankingData;
        public final long startPositionUs;
        final /* synthetic */ BasePreloadManager this$0;

        public MediaSourceHolder(BasePreloadManager r7, MediaSource r8, T r9) {
            this(r7, r8, r9, C0180C.TIME_UNSET);
        }

        public MediaSourceHolder(BasePreloadManager r1, MediaSource r2, T r3, long r4) {
            this.this$0 = r1;
            this.mediaSource = r2;
            this.rankingData = r3;
            this.startPositionUs = r4;
        }

        public int compareTo(BasePreloadManager<T>.MediaSourceHolder r4) {
            return this.this$0.rankingDataComparator.compare(this.rankingData, r4.rankingData);
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Object r1) {
            return compareTo((MediaSourceHolder) r1);
        }
    }

    protected BasePreloadManager(Comparator<T> r5, TargetPreloadStatusControl<T> r6, MediaSource.Factory r7) {
        this.lock = new Object();
        this.applicationHandler = Util.createHandlerForCurrentOrMainLooper();
        this.rankingDataComparator = r5;
        this.targetPreloadStatusControl = r6;
        this.mediaSourceFactory = r7;
        this.listeners = new ListenerSet(this.applicationHandler.getLooper(), Clock.DEFAULT, new BasePreloadManager$$ExternalSyntheticLambda5());
        this.mediaItemMediaSourceHolderMap = new HashMap();
        this.sourceHolderPriorityQueue = new PriorityQueue();
    }

    private boolean isPreloading(MediaSource r2) {
        if (this.sourceHolderPriorityQueue.isEmpty() == false) goto L5;
    L7:
        return false;
    L5:
        if (((MediaSourceHolder) Assertions.checkNotNull(this.sourceHolderPriorityQueue.peek())).mediaSource != r2) goto L7;
        return true;
    }

    static /* synthetic */ void lambda$new$0(Listener r0, FlagSet r1) {
    }

    static /* synthetic */ void lambda$onPreloadCompleted$1(MediaSource r1, Listener r2) {
        r2.onCompleted(r1.getMediaItem());
    }

    static /* synthetic */ void lambda$onPreloadError$3(PreloadException r0, Listener r1) {
        r1.onError(r0);
    }

    private void maybeAdvanceToNextSource(MediaSource r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L14:
        th = move-exception;
        throw th;
    L5:
        if (isPreloading(r3) == true) goto L8;
        monitor-exit(r0);     // Catch: Throwable -> L14
        return;
    L8:
        this.sourceHolderPriorityQueue.poll();     // Catch: Throwable -> L14
        if (this.sourceHolderPriorityQueue.isEmpty() == true) goto L12;
        if (maybeStartPreloadNextSource() == false) goto L8;
    L12:
        monitor-exit(r0);     // Catch: Throwable -> L14
    }

    private boolean maybeStartPreloadNextSource() {
        if (shouldStartPreloadingNextSource() == false) goto L11;
        MediaSourceHolder r0 = (MediaSourceHolder) Assertions.checkNotNull(this.sourceHolderPriorityQueue.peek());
        this.targetPreloadStatusOfCurrentPreloadingSource = this.targetPreloadStatusControl.getTargetPreloadStatus(r0.rankingData);
        if (this.targetPreloadStatusOfCurrentPreloadingSource == null) goto L8;
        preloadSourceInternal(r0.mediaSource, r0.startPositionUs);
        return true;
    L8:
        clearSourceInternal(r0.mediaSource);
        return false;
    L11:
        return false;
    }

    private void verifyApplicationThread() {
        if (Looper.myLooper() != this.applicationHandler.getLooper()) goto L6;
        return;
    L6:
        throw new IllegalStateException("Preload manager is accessed on the wrong thread.");
    }

    public final void add(MediaItem r2, T r3) {
        add(this.mediaSourceFactory.createMediaSource(r2), r3);
    }

    public final void add(MediaSource r5, T r6) {
        MediaSource r0 = createMediaSourceForPreloading(r5);
        BasePreloadManager<T>.MediaSourceHolder r1 = new MediaSourceHolder(this, r0, r6);
        this.mediaItemMediaSourceHolderMap.put(r0.getMediaItem(), r1);
    }

    public void addListener(Listener r2) {
        this.listeners.add(r2);
    }

    public void clearListeners() {
        verifyApplicationThread();
        this.listeners.clear();
    }

    protected abstract void clearSourceInternal(MediaSource r1);

    protected MediaSource createMediaSourceForPreloading(MediaSource r1) {
        return r1;
    }

    public final MediaSource getMediaSource(MediaItem r2) {
        if (this.mediaItemMediaSourceHolderMap.containsKey(r2) == true) goto L7;
        return null;
    L7:
        return this.mediaItemMediaSourceHolderMap.get(r2).mediaSource;
    }

    public final int getSourceCount() {
        return this.mediaItemMediaSourceHolderMap.size();
    }

    protected final TargetPreloadStatusControl.PreloadStatus getTargetPreloadStatus(MediaSource r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L12:
        th = move-exception;
        throw th;
    L5:
        if (isPreloading(r3) == true) goto L9;
        monitor-exit(r0);     // Catch: Throwable -> L12
        return null;
    L9:
        TargetPreloadStatusControl.PreloadStatus r1 = this.targetPreloadStatusOfCurrentPreloadingSource;     // Catch: Throwable -> L12
        monitor-exit(r0);     // Catch: Throwable -> L12
        return r1;
    }

    public final void invalidate() {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.sourceHolderPriorityQueue.clear();     // Catch: Throwable -> L12
        this.sourceHolderPriorityQueue.addAll(this.mediaItemMediaSourceHolderMap.values());     // Catch: Throwable -> L12
    L6:
        if (this.sourceHolderPriorityQueue.isEmpty() == true) goto L10;
        if (maybeStartPreloadNextSource() == true) goto L10;
        this.sourceHolderPriorityQueue.poll();     // Catch: Throwable -> L12
    L10:
        monitor-exit(r0);     // Catch: Throwable -> L12
        return;
    L12:
        th = move-exception;
        throw th;
    }

    /* JADX INFO: renamed from: lambda$onPreloadCompleted$2$androidx-media3-exoplayer-source-preload-BasePreloadManager */
    /* synthetic */ void m137xf9589f29(final MediaSource r4) {
        this.listeners.sendEvent(-1, new BasePreloadManager$$ExternalSyntheticLambda0(r4));
        maybeAdvanceToNextSource(r4);
    }

    /* JADX INFO: renamed from: lambda$onPreloadError$4$androidx-media3-exoplayer-source-preload-BasePreloadManager */
    /* synthetic */ void m138x773570a8(final PreloadException r4, MediaSource r5) {
        this.listeners.sendEvent(-1, new BasePreloadManager$$ExternalSyntheticLambda1(r4));
        maybeAdvanceToNextSource(r5);
    }

    /* JADX INFO: renamed from: lambda$onPreloadSkipped$5$androidx-media3-exoplayer-source-preload-BasePreloadManager */
    /* synthetic */ void m139x7039cf91(MediaSource r1) {
        maybeAdvanceToNextSource(r1);
    }

    protected final void onPreloadCompleted(final MediaSource r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L11:
        th = move-exception;
        throw th;
    L5:
        if (isPreloading(r3) == true) goto L8;
        monitor-exit(r0);     // Catch: Throwable -> L11
        return;
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L11
        this.applicationHandler.post(new BasePreloadManager$$ExternalSyntheticLambda2(this, r3));
    }

    protected final void onPreloadError(final PreloadException r3, final MediaSource r4) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L11:
        th = move-exception;
        throw th;
    L5:
        if (isPreloading(r4) == true) goto L8;
        monitor-exit(r0);     // Catch: Throwable -> L11
        return;
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L11
        this.applicationHandler.post(new BasePreloadManager$$ExternalSyntheticLambda4(this, r3, r4));
    }

    protected final void onPreloadSkipped(final MediaSource r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L11:
        th = move-exception;
        throw th;
    L5:
        if (isPreloading(r3) == true) goto L8;
        monitor-exit(r0);     // Catch: Throwable -> L11
        return;
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L11
        this.applicationHandler.post(new BasePreloadManager$$ExternalSyntheticLambda3(this, r3));
    }

    protected abstract void preloadSourceInternal(MediaSource r1, long r2);

    public final void release() {
        reset();
        releaseInternal();
        clearListeners();
    }

    protected void releaseInternal() {
    }

    protected abstract void releaseSourceInternal(MediaSource r1);

    public final boolean remove(MediaItem r3) {
        if (this.mediaItemMediaSourceHolderMap.containsKey(r3) == false) goto L6;
        MediaSource r0 = this.mediaItemMediaSourceHolderMap.get(r3).mediaSource;
        this.mediaItemMediaSourceHolderMap.remove(r3);
        releaseSourceInternal(r0);
        return true;
    L6:
        return false;
    }

    public final boolean remove(MediaSource r4) {
        MediaItem r0 = r4.getMediaItem();
        if (this.mediaItemMediaSourceHolderMap.containsKey(r0) == true) goto L5;
        return false;
    L5:
        if (r4 != this.mediaItemMediaSourceHolderMap.get(r0).mediaSource) goto L10;
        this.mediaItemMediaSourceHolderMap.remove(r0);
        releaseSourceInternal(r4);
        return true;
    L10:
        return false;
    }

    public void removeListener(Listener r2) {
        verifyApplicationThread();
        this.listeners.remove(r2);
    }

    public final void reset() {
        Iterator<BasePreloadManager<T>.MediaSourceHolder> r0 = this.mediaItemMediaSourceHolderMap.values().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        releaseSourceInternal(r0.next().mediaSource);
        goto L4
    L6:
        this.mediaItemMediaSourceHolderMap.clear();
        Object r1 = this.lock;
        monitor-enter(r1);
        this.sourceHolderPriorityQueue.clear();     // Catch: Throwable -> L11
        this.targetPreloadStatusOfCurrentPreloadingSource = null;     // Catch: Throwable -> L11
        monitor-exit(r1);     // Catch: Throwable -> L11
        return;
    L11:
        th = move-exception;
        throw th;
    }

    protected boolean shouldStartPreloadingNextSource() {
        return true;
    }
}
