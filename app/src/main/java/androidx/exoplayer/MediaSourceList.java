package androidx.media3.exoplayer;

import android.util.Pair;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.HandlerWrapper;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.analytics.AnalyticsCollector;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MaskingMediaPeriod;
import androidx.media3.exoplayer.source.MaskingMediaSource;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.MediaSourceEventListener;
import androidx.media3.exoplayer.source.ShuffleOrder;
import androidx.media3.exoplayer.upstream.Allocator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
final class MediaSourceList {
    private static final String TAG = "MediaSourceList";
    private final HashMap<MediaSourceHolder, MediaSourceAndListener> childSources;
    private final Set<MediaSourceHolder> enabledMediaSourceHolders;
    private final HandlerWrapper eventHandler;
    private final AnalyticsCollector eventListener;
    private boolean isPrepared;
    private final IdentityHashMap<MediaPeriod, MediaSourceHolder> mediaSourceByMediaPeriod;
    private final Map<Object, MediaSourceHolder> mediaSourceByUid;
    private final List<MediaSourceHolder> mediaSourceHolders;
    private final MediaSourceListInfoRefreshListener mediaSourceListInfoListener;
    private TransferListener mediaTransferListener;
    private final PlayerId playerId;
    private ShuffleOrder shuffleOrder;

    private final class ForwardingEventListener implements MediaSourceEventListener, DrmSessionEventListener {

        /* JADX INFO: renamed from: id */
        private final MediaSourceHolder f36id;
        final /* synthetic */ MediaSourceList this$0;

        public ForwardingEventListener(MediaSourceList r1, MediaSourceHolder r2) {
            this.this$0 = r1;
            this.f36id = r2;
        }

        private Pair<Integer, MediaSource.MediaPeriodId> getEventParameters(int r4, MediaSource.MediaPeriodId r5) {
            MediaSource.MediaPeriodId r0 = null;
            if (r5 == null) goto L9;
            r0 = MediaSourceList.access$100(this.f36id, r5);
            if (r0 != null) goto L9;
            return null;
        L9:
            return Pair.create(Integer.valueOf(MediaSourceList.access$200(this.f36id, r4)), r0);
        }

        /* JADX INFO: renamed from: lambda$onDownstreamFormatChanged$5$androidx-media3-exoplayer-MediaSourceList$ForwardingEventListener */
        /* synthetic */ void m66xf34172ec(Pair r4, MediaLoadData r5) {
            MediaSourceList.access$300(this.this$0).onDownstreamFormatChanged(((Integer) r4.first).intValue(), (MediaSource.MediaPeriodId) r4.second, r5);
        }

        /* JADX INFO: renamed from: lambda$onDrmKeysLoaded$7$androidx-media3-exoplayer-MediaSourceList$ForwardingEventListener */
        /* synthetic */ void m67x3f5587cb(Pair r4) {
            MediaSourceList.access$300(this.this$0).onDrmKeysLoaded(((Integer) r4.first).intValue(), (MediaSource.MediaPeriodId) r4.second);
        }

        /* JADX INFO: renamed from: lambda$onDrmKeysRemoved$10$androidx-media3-exoplayer-MediaSourceList$ForwardingEventListener */
        /* synthetic */ void m68x32cca0d6(Pair r4) {
            MediaSourceList.access$300(this.this$0).onDrmKeysRemoved(((Integer) r4.first).intValue(), (MediaSource.MediaPeriodId) r4.second);
        }

        /* JADX INFO: renamed from: lambda$onDrmKeysRestored$9$androidx-media3-exoplayer-MediaSourceList$ForwardingEventListener */
        /* synthetic */ void m69x356ec9e(Pair r4) {
            MediaSourceList.access$300(this.this$0).onDrmKeysRestored(((Integer) r4.first).intValue(), (MediaSource.MediaPeriodId) r4.second);
        }

        /* JADX INFO: renamed from: lambda$onDrmSessionAcquired$6$androidx-media3-exoplayer-MediaSourceList$ForwardingEventListener */
        /* synthetic */ void m70xe036324f(Pair r4, int r5) {
            MediaSourceList.access$300(this.this$0).onDrmSessionAcquired(((Integer) r4.first).intValue(), (MediaSource.MediaPeriodId) r4.second, r5);
        }

        /* JADX INFO: renamed from: lambda$onDrmSessionManagerError$8$androidx-media3-exoplayer-MediaSourceList$ForwardingEventListener */
        /* synthetic */ void m71x6070cdde(Pair r4, Exception r5) {
            MediaSourceList.access$300(this.this$0).onDrmSessionManagerError(((Integer) r4.first).intValue(), (MediaSource.MediaPeriodId) r4.second, r5);
        }

        /* JADX INFO: renamed from: lambda$onDrmSessionReleased$11$androidx-media3-exoplayer-MediaSourceList$ForwardingEventListener */
        /* synthetic */ void m72xbda1950(Pair r4) {
            MediaSourceList.access$300(this.this$0).onDrmSessionReleased(((Integer) r4.first).intValue(), (MediaSource.MediaPeriodId) r4.second);
        }

        /* JADX INFO: renamed from: lambda$onLoadCanceled$2$androidx-media3-exoplayer-MediaSourceList$ForwardingEventListener */
        /* synthetic */ void m73x36d40f85(Pair r4, LoadEventInfo r5, MediaLoadData r6) {
            MediaSourceList.access$300(this.this$0).onLoadCanceled(((Integer) r4.first).intValue(), (MediaSource.MediaPeriodId) r4.second, r5, r6);
        }

        /* JADX INFO: renamed from: lambda$onLoadCompleted$1$androidx-media3-exoplayer-MediaSourceList$ForwardingEventListener */
        /* synthetic */ void m74x562f44b2(Pair r4, LoadEventInfo r5, MediaLoadData r6) {
            MediaSourceList.access$300(this.this$0).onLoadCompleted(((Integer) r4.first).intValue(), (MediaSource.MediaPeriodId) r4.second, r5, r6);
        }

        /* JADX INFO: renamed from: lambda$onLoadError$3$androidx-media3-exoplayer-MediaSourceList$ForwardingEventListener */
        /* synthetic */ void m75x67e35871(Pair r9, LoadEventInfo r10, MediaLoadData r11, IOException r12, boolean r13) {
            MediaSourceList.access$300(this.this$0).onLoadError(((Integer) r9.first).intValue(), (MediaSource.MediaPeriodId) r9.second, r10, r11, r12, r13);
        }

        /* JADX INFO: renamed from: lambda$onLoadStarted$0$androidx-media3-exoplayer-MediaSourceList$ForwardingEventListener */
        /* synthetic */ void m76x11454fa7(Pair r4, LoadEventInfo r5, MediaLoadData r6) {
            MediaSourceList.access$300(this.this$0).onLoadStarted(((Integer) r4.first).intValue(), (MediaSource.MediaPeriodId) r4.second, r5, r6);
        }

        /* JADX INFO: renamed from: lambda$onUpstreamDiscarded$4$androidx-media3-exoplayer-MediaSourceList$ForwardingEventListener */
        /* synthetic */ void m77xcc5b5192(Pair r4, MediaLoadData r5) {
            MediaSourceList.access$300(this.this$0).onUpstreamDiscarded(((Integer) r4.first).intValue(), (MediaSource.MediaPeriodId) Assertions.checkNotNull((MediaSource.MediaPeriodId) r4.second), r5);
        }

        @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
        public void onDownstreamFormatChanged(int r4, MediaSource.MediaPeriodId r5, final MediaLoadData r6) {
            final Pair<Integer, MediaSource.MediaPeriodId> r0 = getEventParameters(r4, r5);
            if (r0 == null) goto L6;
            MediaSourceList.access$000(this.this$0).post(new RunnableC0245x327d8040(this, r0, r6));
            return;
        }

        @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
        public void onDrmKeysLoaded(int r4, MediaSource.MediaPeriodId r5) {
            final Pair<Integer, MediaSource.MediaPeriodId> r0 = getEventParameters(r4, r5);
            if (r0 == null) goto L6;
            MediaSourceList.access$000(this.this$0).post(new RunnableC0237x327d803a(this, r0));
            return;
        }

        @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
        public void onDrmKeysRemoved(int r4, MediaSource.MediaPeriodId r5) {
            final Pair<Integer, MediaSource.MediaPeriodId> r0 = getEventParameters(r4, r5);
            if (r0 == null) goto L6;
            MediaSourceList.access$000(this.this$0).post(new RunnableC0242x327d803d(this, r0));
            return;
        }

        @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
        public void onDrmKeysRestored(int r4, MediaSource.MediaPeriodId r5) {
            final Pair<Integer, MediaSource.MediaPeriodId> r0 = getEventParameters(r4, r5);
            if (r0 == null) goto L6;
            MediaSourceList.access$000(this.this$0).post(new RunnableC0246x327d8041(this, r0));
            return;
        }

        @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
        public void onDrmSessionAcquired(int r4, MediaSource.MediaPeriodId r5, final int r6) {
            final Pair<Integer, MediaSource.MediaPeriodId> r0 = getEventParameters(r4, r5);
            if (r0 == null) goto L6;
            MediaSourceList.access$000(this.this$0).post(new RunnableC0239x1d328737(this, r0, r6));
            return;
        }

        @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
        public void onDrmSessionManagerError(int r4, MediaSource.MediaPeriodId r5, final Exception r6) {
            final Pair<Integer, MediaSource.MediaPeriodId> r0 = getEventParameters(r4, r5);
            if (r0 == null) goto L6;
            MediaSourceList.access$000(this.this$0).post(new RunnableC0243x327d803e(this, r0, r6));
            return;
        }

        @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
        public void onDrmSessionReleased(int r4, MediaSource.MediaPeriodId r5) {
            final Pair<Integer, MediaSource.MediaPeriodId> r0 = getEventParameters(r4, r5);
            if (r0 == null) goto L6;
            MediaSourceList.access$000(this.this$0).post(new RunnableC0247x327d8042(this, r0));
            return;
        }

        @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
        public void onLoadCanceled(int r4, MediaSource.MediaPeriodId r5, final LoadEventInfo r6, final MediaLoadData r7) {
            final Pair<Integer, MediaSource.MediaPeriodId> r0 = getEventParameters(r4, r5);
            if (r0 == null) goto L6;
            MediaSourceList.access$000(this.this$0).post(new RunnableC0241x327d803c(this, r0, r6, r7));
            return;
        }

        @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
        public void onLoadCompleted(int r4, MediaSource.MediaPeriodId r5, final LoadEventInfo r6, final MediaLoadData r7) {
            final Pair<Integer, MediaSource.MediaPeriodId> r0 = getEventParameters(r4, r5);
            if (r0 == null) goto L6;
            MediaSourceList.access$000(this.this$0).post(new RunnableC0238x1d328736(this, r0, r6, r7));
            return;
        }

        @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
        public void onLoadError(int r9, MediaSource.MediaPeriodId r10, final LoadEventInfo r11, final MediaLoadData r12, final IOException r13, final boolean r14) {
            final Pair<Integer, MediaSource.MediaPeriodId> r2 = getEventParameters(r9, r10);
            if (r2 == null) goto L6;
            MediaSourceList.access$000(this.this$0).post(new RunnableC0240x327d803b(this, r2, r11, r12, r13, r14));
            return;
        }

        @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
        public void onLoadStarted(int r4, MediaSource.MediaPeriodId r5, final LoadEventInfo r6, final MediaLoadData r7) {
            final Pair<Integer, MediaSource.MediaPeriodId> r0 = getEventParameters(r4, r5);
            if (r0 == null) goto L6;
            MediaSourceList.access$000(this.this$0).post(new RunnableC0244x327d803f(this, r0, r6, r7));
            return;
        }

        @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
        public void onUpstreamDiscarded(int r4, MediaSource.MediaPeriodId r5, final MediaLoadData r6) {
            final Pair<Integer, MediaSource.MediaPeriodId> r0 = getEventParameters(r4, r5);
            if (r0 == null) goto L6;
            MediaSourceList.access$000(this.this$0).post(new RunnableC0236x327d8039(this, r0, r6));
            return;
        }
    }

    private static final class MediaSourceAndListener {
        public final MediaSource.MediaSourceCaller caller;
        public final ForwardingEventListener eventListener;
        public final MediaSource mediaSource;

        public MediaSourceAndListener(MediaSource r1, MediaSource.MediaSourceCaller r2, ForwardingEventListener r3) {
            this.mediaSource = r1;
            this.caller = r2;
            this.eventListener = r3;
        }
    }

    static final class MediaSourceHolder implements MediaSourceInfoHolder {
        public final List<MediaSource.MediaPeriodId> activeMediaPeriodIds;
        public int firstWindowIndexInChild;
        public boolean isRemoved;
        public final MaskingMediaSource mediaSource;
        public final Object uid;

        public MediaSourceHolder(MediaSource r2, boolean r3) {
            this.mediaSource = new MaskingMediaSource(r2, r3);
            this.activeMediaPeriodIds = new ArrayList();
            this.uid = new Object();
        }

        @Override // androidx.media3.exoplayer.MediaSourceInfoHolder
        public Timeline getTimeline() {
            return this.mediaSource.getTimeline();
        }

        @Override // androidx.media3.exoplayer.MediaSourceInfoHolder
        public Object getUid() {
            return this.uid;
        }

        public void reset(int r2) {
            this.firstWindowIndexInChild = r2;
            this.isRemoved = false;
            this.activeMediaPeriodIds.clear();
        }
    }

    public interface MediaSourceListInfoRefreshListener {
        void onPlaylistUpdateRequested();
    }

    public MediaSourceList(MediaSourceListInfoRefreshListener r3, AnalyticsCollector r4, HandlerWrapper r5, PlayerId r6) {
        this.playerId = r6;
        this.mediaSourceListInfoListener = r3;
        this.shuffleOrder = new ShuffleOrder.DefaultShuffleOrder(0);
        this.mediaSourceByMediaPeriod = new IdentityHashMap();
        this.mediaSourceByUid = new HashMap();
        this.mediaSourceHolders = new ArrayList();
        this.eventListener = r4;
        this.eventHandler = r5;
        this.childSources = new HashMap();
        this.enabledMediaSourceHolders = new HashSet();
    }

    static /* synthetic */ HandlerWrapper access$000(MediaSourceList r1) {
        return r1.eventHandler;
    }

    static /* synthetic */ MediaSource.MediaPeriodId access$100(MediaSourceHolder r1, MediaSource.MediaPeriodId r2) {
        return getMediaPeriodIdForChildMediaPeriodId(r1, r2);
    }

    static /* synthetic */ int access$200(MediaSourceHolder r1, int r2) {
        return getWindowIndexForChildWindowIndex(r1, r2);
    }

    static /* synthetic */ AnalyticsCollector access$300(MediaSourceList r1) {
        return r1.eventListener;
    }

    private void correctOffsets(int r4, int r5) {
        int r0 = r4;
    L4:
        if (r0 >= this.mediaSourceHolders.size()) goto L6;
        this.mediaSourceHolders.get(r0).firstWindowIndexInChild += r5;
        r0 = r0 + 1;
        goto L4
    }

    private void disableChildSource(MediaSourceHolder r4) {
        MediaSourceAndListener r0 = this.childSources.get(r4);
        if (r0 == null) goto L6;
        r0.mediaSource.disable(r0.caller);
        return;
    }

    private void disableUnusedMediaSources() {
        Iterator<MediaSourceHolder> r0 = this.enabledMediaSourceHolders.iterator();
    L4:
        if (r0.hasNext() == false) goto L9;
        MediaSourceHolder r1 = r0.next();
        if (r1.activeMediaPeriodIds.isEmpty() == false) goto L4;
        disableChildSource(r1);
        r0.remove();
        goto L4
    }

    private void enableMediaSource(MediaSourceHolder r4) {
        this.enabledMediaSourceHolders.add(r4);
        MediaSourceAndListener r0 = this.childSources.get(r4);
        if (r0 == null) goto L6;
        r0.mediaSource.enable(r0.caller);
        return;
    }

    private static Object getChildPeriodUid(Object r1) {
        return PlaylistTimeline.getChildPeriodUidFromConcatenatedUid(r1);
    }

    private static MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(MediaSourceHolder r5, MediaSource.MediaPeriodId r6) {
        int r0 = 0;
    L4:
        if (r0 >= r5.activeMediaPeriodIds.size()) goto L10;
        if (r5.activeMediaPeriodIds.get(r0).windowSequenceNumber == r6.windowSequenceNumber) goto L8;
        r0 = r0 + 1;
        goto L4
    L8:
        return r6.copyWithPeriodUid(getPeriodUid(r5, r6.periodUid));
    L10:
        return null;
    }

    private static Object getMediaSourceHolderUid(Object r1) {
        return PlaylistTimeline.getChildTimelineUidFromConcatenatedUid(r1);
    }

    private static Object getPeriodUid(MediaSourceHolder r1, Object r2) {
        return PlaylistTimeline.getConcatenatedUid(r1.uid, r2);
    }

    private static int getWindowIndexForChildWindowIndex(MediaSourceHolder r1, int r2) {
        return r1.firstWindowIndexInChild + r2;
    }

    private void maybeReleaseChildSource(MediaSourceHolder r4) {
        if (r4.isRemoved == true) goto L5;
        return;
    L5:
        if (r4.activeMediaPeriodIds.isEmpty() == false) goto L9;
        MediaSourceAndListener r0 = (MediaSourceAndListener) Assertions.checkNotNull(this.childSources.remove(r4));
        r0.mediaSource.releaseSource(r0.caller);
        r0.mediaSource.removeEventListener(r0.eventListener);
        r0.mediaSource.removeDrmEventListener(r0.eventListener);
        this.enabledMediaSourceHolders.remove(r4);
        return;
    }

    private void prepareChildSource(MediaSourceHolder r6) {
        MaskingMediaSource r0 = r6.mediaSource;
        MediaSource.MediaSourceCaller r1 = new MediaSourceList$$ExternalSyntheticLambda0(this);
        ForwardingEventListener r2 = new ForwardingEventListener(this, r6);
        this.childSources.put(r6, new MediaSourceAndListener(r0, r1, r2));
        r0.addEventListener(Util.createHandlerForCurrentOrMainLooper(), r2);
        r0.addDrmEventListener(Util.createHandlerForCurrentOrMainLooper(), r2);
        r0.prepareSource(r1, this.mediaTransferListener, this.playerId);
    }

    private void removeMediaSourcesInternal(int r5, int r6) {
        int r0 = r6 - 1;
    L3:
        if (r0 < r5) goto L8;
        MediaSourceHolder r1 = this.mediaSourceHolders.remove(r0);
        this.mediaSourceByUid.remove(r1.uid);
        correctOffsets(r0, -r1.mediaSource.getTimeline().getWindowCount());
        r1.isRemoved = true;
        if (this.isPrepared == false) goto L7;
        maybeReleaseChildSource(r1);
    L7:
        r0 = r0 - 1;
        goto L3
    }

    public Timeline addMediaSources(int r7, List<MediaSourceHolder> r8, ShuffleOrder r9) {
        if (r8.isEmpty() == true) goto L19;
        this.shuffleOrder = r9;
        int r0 = r7;
    L6:
        if (r0 >= (r8.size() + r7)) goto L19;
        MediaSourceHolder r1 = r8.get(r0 - r7);
        if (r0 <= 0) goto L10;
        MediaSourceHolder r2 = this.mediaSourceHolders.get(r0 - 1);
        r1.reset(r2.firstWindowIndexInChild + r2.mediaSource.getTimeline().getWindowCount());
    L11:
        correctOffsets(r0, r1.mediaSource.getTimeline().getWindowCount());
        this.mediaSourceHolders.add(r0, r1);
        this.mediaSourceByUid.put(r1.uid, r1);
        if (this.isPrepared == false) goto L17;
        prepareChildSource(r1);
        if (this.mediaSourceByMediaPeriod.isEmpty() == false) goto L16;
        this.enabledMediaSourceHolders.add(r1);
        goto L17
    L16:
        disableChildSource(r1);
    L17:
        r0 = r0 + 1;
        goto L6
    L10:
        r1.reset(0);
    L19:
        return createTimeline();
    }

    public Timeline clear(ShuffleOrder r3) {
        if (r3 == null) goto L4;
        ShuffleOrder r0 = r3;
    L5:
        this.shuffleOrder = r0;
        removeMediaSourcesInternal(0, getSize());
        return createTimeline();
    L4:
        r0 = this.shuffleOrder.cloneAndClear();
        goto L5
    }

    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r6, Allocator r7, long r8) {
        Object r0 = getMediaSourceHolderUid(r6.periodUid);
        MediaSource.MediaPeriodId r1 = r6.copyWithPeriodUid(getChildPeriodUid(r6.periodUid));
        MediaSourceHolder r2 = (MediaSourceHolder) Assertions.checkNotNull(this.mediaSourceByUid.get(r0));
        enableMediaSource(r2);
        r2.activeMediaPeriodIds.add(r1);
        MaskingMediaPeriod r3 = r2.mediaSource.createPeriod(r1, r7, r8);
        this.mediaSourceByMediaPeriod.put(r3, r2);
        disableUnusedMediaSources();
        return r3;
    }

    public Timeline createTimeline() {
        if (this.mediaSourceHolders.isEmpty() == true) goto L5;
        int r0 = 0;
        int r1 = 0;
    L8:
        if (r1 >= this.mediaSourceHolders.size()) goto L11;
        MediaSourceHolder r2 = this.mediaSourceHolders.get(r1);
        r2.firstWindowIndexInChild = r0;
        r0 = r0 + r2.mediaSource.getTimeline().getWindowCount();
        r1 = r1 + 1;
        goto L8
    L11:
        return new PlaylistTimeline(this.mediaSourceHolders, this.shuffleOrder);
    L5:
        return Timeline.EMPTY;
    }

    public ShuffleOrder getShuffleOrder() {
        return this.shuffleOrder;
    }

    public int getSize() {
        return this.mediaSourceHolders.size();
    }

    public boolean isPrepared() {
        return this.isPrepared;
    }

    /* JADX INFO: renamed from: lambda$prepareChildSource$0$androidx-media3-exoplayer-MediaSourceList */
    /* synthetic */ void m65x10419188(MediaSource r2, Timeline r3) {
        this.mediaSourceListInfoListener.onPlaylistUpdateRequested();
    }

    public Timeline moveMediaSource(int r2, int r3, ShuffleOrder r4) {
        return moveMediaSourceRange(r2, r2 + 1, r3, r4);
    }

    public Timeline moveMediaSourceRange(int r8, int r9, int r10, ShuffleOrder r11) {
        if (r8 < 0) goto L9;
        if (r8 > r9) goto L9;
        if (r9 > getSize()) goto L9;
        if (r10 < 0) goto L9;
        boolean r1 = true;
    L10:
        Assertions.checkArgument(r1);
        this.shuffleOrder = r11;
        if (r8 == r9) goto L20;
        if (r8 == r10) goto L20;
        int r2 = Math.min(r8, r10);
        int r0 = Math.max(((r9 - r8) + r10) - 1, r9 - 1);
        int r3 = this.mediaSourceHolders.get(r2).firstWindowIndexInChild;
        Util.moveItems(this.mediaSourceHolders, r8, r9, r10);
        int r4 = r2;
    L15:
        if (r4 > r0) goto L18;
        MediaSourceHolder r5 = this.mediaSourceHolders.get(r4);
        r5.firstWindowIndexInChild = r3;
        r3 = r3 + r5.mediaSource.getTimeline().getWindowCount();
        r4 = r4 + 1;
        goto L15
    L18:
        return createTimeline();
    L20:
        return createTimeline();
    L9:
        r1 = false;
        goto L10
    }

    public void prepare(TransferListener r5) {
        Assertions.checkState(!this.isPrepared);
        this.mediaTransferListener = r5;
        int r0 = 0;
    L4:
        if (r0 >= this.mediaSourceHolders.size()) goto L6;
        MediaSourceHolder r2 = this.mediaSourceHolders.get(r0);
        prepareChildSource(r2);
        this.enabledMediaSourceHolders.add(r2);
        r0 = r0 + 1;
        goto L4
    L6:
        this.isPrepared = true;
    }

    public void release() {
        Iterator<MediaSourceAndListener> r0 = this.childSources.values().iterator();
    L4:
        if (r0.hasNext() == false) goto L11;
        MediaSourceAndListener r1 = r0.next();
        r1.mediaSource.releaseSource(r1.caller);     // Catch: RuntimeException -> L8
    L10:
        r1.mediaSource.removeEventListener(r1.eventListener);
        r1.mediaSource.removeDrmEventListener(r1.eventListener);
    L8:
        e = move-exception;
        Log.m45e(TAG, "Failed to release child source.", e);
        goto L10
    L11:
        this.childSources.clear();
        this.enabledMediaSourceHolders.clear();
        this.isPrepared = false;
    }

    public void releasePeriod(MediaPeriod r4) {
        MediaSourceHolder r0 = (MediaSourceHolder) Assertions.checkNotNull(this.mediaSourceByMediaPeriod.remove(r4));
        r0.mediaSource.releasePeriod(r4);
        r0.activeMediaPeriodIds.remove(((MaskingMediaPeriod) r4).f44id);
        if (this.mediaSourceByMediaPeriod.isEmpty() == true) goto L5;
        disableUnusedMediaSources();
    L5:
        maybeReleaseChildSource(r0);
    }

    public Timeline removeMediaSourceRange(int r2, int r3, ShuffleOrder r4) {
        if (r2 < 0) goto L7;
        if (r2 > r3) goto L7;
        if (r3 > getSize()) goto L7;
        boolean r0 = true;
    L8:
        Assertions.checkArgument(r0);
        this.shuffleOrder = r4;
        removeMediaSourcesInternal(r2, r3);
        return createTimeline();
    L7:
        r0 = false;
        goto L8
    }

    public Timeline setMediaSources(List<MediaSourceHolder> r3, ShuffleOrder r4) {
        removeMediaSourcesInternal(0, this.mediaSourceHolders.size());
        return addMediaSources(this.mediaSourceHolders.size(), r3, r4);
    }

    public Timeline setShuffleOrder(ShuffleOrder r4) {
        int r0 = getSize();
        if (r4.getLength() == r0) goto L5;
        r4 = r4.cloneAndClear().cloneAndInsert(0, r0);
    L5:
        this.shuffleOrder = r4;
        return createTimeline();
    }

    public Timeline updateMediaSourcesWithMediaItems(int r5, int r6, List<MediaItem> r7) {
        boolean r0 = false;
        if (r5 < 0) goto L8;
        if (r5 > r6) goto L8;
        if (r6 > getSize()) goto L8;
        boolean r2 = true;
    L9:
        Assertions.checkArgument(r2);
        if (r7.size() != (r6 - r5)) goto L12;
        r0 = true;
    L12:
        Assertions.checkArgument(r0);
        int r1 = r5;
    L13:
        if (r1 >= r6) goto L16;
        this.mediaSourceHolders.get(r1).mediaSource.updateMediaItem(r7.get(r1 - r5));
        r1 = r1 + 1;
        goto L13
    L16:
        return createTimeline();
    L8:
        r2 = false;
        goto L9
    }
}
