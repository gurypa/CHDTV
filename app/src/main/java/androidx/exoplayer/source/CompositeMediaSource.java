package androidx.media3.exoplayer.source;

import android.os.Handler;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public abstract class CompositeMediaSource<T> extends BaseMediaSource {
    private final HashMap<T, MediaSourceAndListener<T>> childSources;
    private Handler eventHandler;
    private TransferListener mediaTransferListener;

    private final class ForwardingEventListener implements MediaSourceEventListener, DrmSessionEventListener {
        private DrmSessionEventListener.EventDispatcher drmEventDispatcher;

        /* JADX INFO: renamed from: id */
        private final T f43id;
        private MediaSourceEventListener.EventDispatcher mediaSourceEventDispatcher;
        final /* synthetic */ CompositeMediaSource this$0;

        public ForwardingEventListener(CompositeMediaSource r3, T r4) {
            this.this$0 = r3;
            this.mediaSourceEventDispatcher = r3.createEventDispatcher(null);
            this.drmEventDispatcher = r3.createDrmEventDispatcher(null);
            this.f43id = r4;
        }

        private boolean maybeUpdateEventDispatcher(int r4, MediaSource.MediaPeriodId r5) {
            MediaSource.MediaPeriodId r0 = null;
            if (r5 == null) goto L8;
            r0 = this.this$0.getMediaPeriodIdForChildMediaPeriodId(this.f43id, r5);
            if (r0 != null) goto L8;
            return false;
        L8:
            int r1 = this.this$0.getWindowIndexForChildWindowIndex(this.f43id, r4);
            if (this.mediaSourceEventDispatcher.windowIndex == r1) goto L11;
        L12:
            this.mediaSourceEventDispatcher = this.this$0.createEventDispatcher(r1, r0);
        L14:
            if (this.drmEventDispatcher.windowIndex == r1) goto L16;
        L17:
            this.drmEventDispatcher = this.this$0.createDrmEventDispatcher(r1, r0);
            return true;
        L16:
            if (Util.areEqual(this.drmEventDispatcher.mediaPeriodId, r0) == false) goto L17;
            return true;
        L11:
            if (Util.areEqual(this.mediaSourceEventDispatcher.mediaPeriodId, r0) == true) goto L14;
            goto L12
        }

        private MediaLoadData maybeUpdateMediaLoadData(MediaLoadData r18, MediaSource.MediaPeriodId r19) {
            long r13 = this.this$0.getMediaTimeForChildMediaTime(this.f43id, r18.mediaStartTimeMs, r19);
            long r15 = this.this$0.getMediaTimeForChildMediaTime(this.f43id, r18.mediaEndTimeMs, r19);
            if (r13 != r18.mediaStartTimeMs) goto L8;
            if (r15 != r18.mediaEndTimeMs) goto L8;
            return r18;
        L8:
            return new MediaLoadData(r18.dataType, r18.trackType, r18.trackFormat, r18.trackSelectionReason, r18.trackSelectionData, r13, r15);
        }

        @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
        public void onDownstreamFormatChanged(int r3, MediaSource.MediaPeriodId r4, MediaLoadData r5) {
            if (maybeUpdateEventDispatcher(r3, r4) == false) goto L6;
            this.mediaSourceEventDispatcher.downstreamFormatChanged(maybeUpdateMediaLoadData(r5, r4));
            return;
        }

        @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
        public void onDrmKeysLoaded(int r2, MediaSource.MediaPeriodId r3) {
            if (maybeUpdateEventDispatcher(r2, r3) == false) goto L6;
            this.drmEventDispatcher.drmKeysLoaded();
            return;
        }

        @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
        public void onDrmKeysRemoved(int r2, MediaSource.MediaPeriodId r3) {
            if (maybeUpdateEventDispatcher(r2, r3) == false) goto L6;
            this.drmEventDispatcher.drmKeysRemoved();
            return;
        }

        @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
        public void onDrmKeysRestored(int r2, MediaSource.MediaPeriodId r3) {
            if (maybeUpdateEventDispatcher(r2, r3) == false) goto L6;
            this.drmEventDispatcher.drmKeysRestored();
            return;
        }

        @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
        public void onDrmSessionAcquired(int r2, MediaSource.MediaPeriodId r3, int r4) {
            if (maybeUpdateEventDispatcher(r2, r3) == false) goto L6;
            this.drmEventDispatcher.drmSessionAcquired(r4);
            return;
        }

        @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
        public void onDrmSessionManagerError(int r2, MediaSource.MediaPeriodId r3, Exception r4) {
            if (maybeUpdateEventDispatcher(r2, r3) == false) goto L6;
            this.drmEventDispatcher.drmSessionManagerError(r4);
            return;
        }

        @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
        public void onDrmSessionReleased(int r2, MediaSource.MediaPeriodId r3) {
            if (maybeUpdateEventDispatcher(r2, r3) == false) goto L6;
            this.drmEventDispatcher.drmSessionReleased();
            return;
        }

        @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
        public void onLoadCanceled(int r3, MediaSource.MediaPeriodId r4, LoadEventInfo r5, MediaLoadData r6) {
            if (maybeUpdateEventDispatcher(r3, r4) == false) goto L6;
            this.mediaSourceEventDispatcher.loadCanceled(r5, maybeUpdateMediaLoadData(r6, r4));
            return;
        }

        @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
        public void onLoadCompleted(int r3, MediaSource.MediaPeriodId r4, LoadEventInfo r5, MediaLoadData r6) {
            if (maybeUpdateEventDispatcher(r3, r4) == false) goto L6;
            this.mediaSourceEventDispatcher.loadCompleted(r5, maybeUpdateMediaLoadData(r6, r4));
            return;
        }

        @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
        public void onLoadError(int r3, MediaSource.MediaPeriodId r4, LoadEventInfo r5, MediaLoadData r6, IOException r7, boolean r8) {
            if (maybeUpdateEventDispatcher(r3, r4) == false) goto L6;
            this.mediaSourceEventDispatcher.loadError(r5, maybeUpdateMediaLoadData(r6, r4), r7, r8);
            return;
        }

        @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
        public void onLoadStarted(int r3, MediaSource.MediaPeriodId r4, LoadEventInfo r5, MediaLoadData r6) {
            if (maybeUpdateEventDispatcher(r3, r4) == false) goto L6;
            this.mediaSourceEventDispatcher.loadStarted(r5, maybeUpdateMediaLoadData(r6, r4));
            return;
        }

        @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
        public void onUpstreamDiscarded(int r3, MediaSource.MediaPeriodId r4, MediaLoadData r5) {
            if (maybeUpdateEventDispatcher(r3, r4) == false) goto L6;
            this.mediaSourceEventDispatcher.upstreamDiscarded(maybeUpdateMediaLoadData(r5, r4));
            return;
        }
    }

    private static final class MediaSourceAndListener<T> {
        public final MediaSource.MediaSourceCaller caller;
        public final CompositeMediaSource<T>.ForwardingEventListener eventListener;
        public final MediaSource mediaSource;

        public MediaSourceAndListener(MediaSource r1, MediaSource.MediaSourceCaller r2, CompositeMediaSource<T>.ForwardingEventListener r3) {
            this.mediaSource = r1;
            this.caller = r2;
            this.eventListener = r3;
        }
    }

    protected CompositeMediaSource() {
        this.childSources = new HashMap();
    }

    protected final void disableChildSource(T r4) {
        MediaSourceAndListener r0 = (MediaSourceAndListener) Assertions.checkNotNull(this.childSources.get(r4));
        r0.mediaSource.disable(r0.caller);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void disableInternal() {
        Iterator<MediaSourceAndListener<T>> r0 = this.childSources.values().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        MediaSourceAndListener<T> r1 = r0.next();
        r1.mediaSource.disable(r1.caller);
        goto L4
    }

    protected final void enableChildSource(T r4) {
        MediaSourceAndListener r0 = (MediaSourceAndListener) Assertions.checkNotNull(this.childSources.get(r4));
        r0.mediaSource.enable(r0.caller);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void enableInternal() {
        Iterator<MediaSourceAndListener<T>> r0 = this.childSources.values().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        MediaSourceAndListener<T> r1 = r0.next();
        r1.mediaSource.enable(r1.caller);
        goto L4
    }

    protected MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(T r1, MediaSource.MediaPeriodId r2) {
        return r2;
    }

    protected long getMediaTimeForChildMediaTime(T r1, long r2, MediaSource.MediaPeriodId r4) {
        return r2;
    }

    protected int getWindowIndexForChildWindowIndex(T r1, int r2) {
        return r2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: renamed from: lambda$prepareChildSource$0$androidx-media3-exoplayer-source-CompositeMediaSource */
    /* synthetic */ void m119x28f9175(Object r1, MediaSource r2, Timeline r3) {
        onChildSourceInfoRefreshed(r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        Iterator<MediaSourceAndListener<T>> r0 = this.childSources.values().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().mediaSource.maybeThrowSourceInfoRefreshError();
        goto L4
    }

    protected abstract void onChildSourceInfoRefreshed(T r1, MediaSource r2, Timeline r3);

    protected final void prepareChildSource(final T r5, MediaSource r6) {
        Assertions.checkArgument(!this.childSources.containsKey(r5));
        MediaSource.MediaSourceCaller r0 = new CompositeMediaSource$$ExternalSyntheticLambda0(this, r5);
        ForwardingEventListener r1 = new ForwardingEventListener(this, r5);
        this.childSources.put(r5, new MediaSourceAndListener(r6, r0, r1));
        r6.addEventListener((Handler) Assertions.checkNotNull(this.eventHandler), r1);
        r6.addDrmEventListener((Handler) Assertions.checkNotNull(this.eventHandler), r1);
        r6.prepareSource(r0, this.mediaTransferListener, getPlayerId());
        if (isEnabled() == true) goto L6;
        r6.disable(r0);
        return;
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void prepareSourceInternal(TransferListener r2) {
        this.mediaTransferListener = r2;
        this.eventHandler = Util.createHandlerForCurrentLooper();
    }

    protected final void releaseChildSource(T r4) {
        MediaSourceAndListener r0 = (MediaSourceAndListener) Assertions.checkNotNull(this.childSources.remove(r4));
        r0.mediaSource.releaseSource(r0.caller);
        r0.mediaSource.removeEventListener(r0.eventListener);
        r0.mediaSource.removeDrmEventListener(r0.eventListener);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
        Iterator<MediaSourceAndListener<T>> r0 = this.childSources.values().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        MediaSourceAndListener<T> r1 = r0.next();
        r1.mediaSource.releaseSource(r1.caller);
        r1.mediaSource.removeEventListener(r1.eventListener);
        r1.mediaSource.removeDrmEventListener(r1.eventListener);
        goto L4
    L6:
        this.childSources.clear();
    }
}
