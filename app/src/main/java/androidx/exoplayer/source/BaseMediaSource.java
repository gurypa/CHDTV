package androidx.media3.exoplayer.source;

import android.os.Handler;
import android.os.Looper;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseMediaSource implements MediaSource {
    private final DrmSessionEventListener.EventDispatcher drmEventDispatcher;
    private final HashSet<MediaSource.MediaSourceCaller> enabledMediaSourceCallers;
    private final MediaSourceEventListener.EventDispatcher eventDispatcher;
    private Looper looper;
    private final ArrayList<MediaSource.MediaSourceCaller> mediaSourceCallers;
    private PlayerId playerId;
    private Timeline timeline;

    public BaseMediaSource() {
        this.mediaSourceCallers = new ArrayList(1);
        this.enabledMediaSourceCallers = new HashSet(1);
        this.eventDispatcher = new MediaSourceEventListener.EventDispatcher();
        this.drmEventDispatcher = new DrmSessionEventListener.EventDispatcher();
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public final void addDrmEventListener(Handler r2, DrmSessionEventListener r3) {
        Assertions.checkNotNull(r2);
        Assertions.checkNotNull(r3);
        this.drmEventDispatcher.addEventListener(r2, r3);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public final void addEventListener(Handler r2, MediaSourceEventListener r3) {
        Assertions.checkNotNull(r2);
        Assertions.checkNotNull(r3);
        this.eventDispatcher.addEventListener(r2, r3);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public /* synthetic */ boolean canUpdateMediaItem(MediaItem r1) {
        return MediaSource.CC.$default$canUpdateMediaItem(this, r1);
    }

    protected final DrmSessionEventListener.EventDispatcher createDrmEventDispatcher(int r2, MediaSource.MediaPeriodId r3) {
        return this.drmEventDispatcher.withParameters(r2, r3);
    }

    protected final DrmSessionEventListener.EventDispatcher createDrmEventDispatcher(MediaSource.MediaPeriodId r3) {
        return this.drmEventDispatcher.withParameters(0, r3);
    }

    protected final MediaSourceEventListener.EventDispatcher createEventDispatcher(int r2, MediaSource.MediaPeriodId r3) {
        return this.eventDispatcher.withParameters(r2, r3);
    }

    @Deprecated
    protected final MediaSourceEventListener.EventDispatcher createEventDispatcher(int r2, MediaSource.MediaPeriodId r3, long r4) {
        return this.eventDispatcher.withParameters(r2, r3);
    }

    protected final MediaSourceEventListener.EventDispatcher createEventDispatcher(MediaSource.MediaPeriodId r3) {
        return this.eventDispatcher.withParameters(0, r3);
    }

    @Deprecated
    protected final MediaSourceEventListener.EventDispatcher createEventDispatcher(MediaSource.MediaPeriodId r3, long r4) {
        Assertions.checkNotNull(r3);
        return this.eventDispatcher.withParameters(0, r3);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public final void disable(MediaSource.MediaSourceCaller r3) {
        boolean r0 = !this.enabledMediaSourceCallers.isEmpty();
        this.enabledMediaSourceCallers.remove(r3);
        if (r0 == true) goto L5;
        return;
    L5:
        if (this.enabledMediaSourceCallers.isEmpty() == false) goto L9;
        disableInternal();
        return;
    }

    protected void disableInternal() {
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public final void enable(MediaSource.MediaSourceCaller r3) {
        Assertions.checkNotNull(this.looper);
        boolean r0 = this.enabledMediaSourceCallers.isEmpty();
        this.enabledMediaSourceCallers.add(r3);
        if (r0 == false) goto L6;
        enableInternal();
        return;
    }

    protected void enableInternal() {
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public /* synthetic */ Timeline getInitialTimeline() {
        return MediaSource.CC.$default$getInitialTimeline(this);
    }

    protected final PlayerId getPlayerId() {
        return (PlayerId) Assertions.checkStateNotNull(this.playerId);
    }

    protected final boolean isEnabled() {
        return !this.enabledMediaSourceCallers.isEmpty();
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public /* synthetic */ boolean isSingleWindow() {
        return MediaSource.CC.$default$isSingleWindow(this);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public final void prepareSource(MediaSource.MediaSourceCaller r2, TransferListener r3) {
        prepareSource(r2, r3, PlayerId.UNSET);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public final void prepareSource(MediaSource.MediaSourceCaller r4, TransferListener r5, PlayerId r6) {
        Looper r0 = Looper.myLooper();
        if (this.looper != null) goto L5;
    L8:
        boolean r1 = true;
    L9:
        Assertions.checkArgument(r1);
        this.playerId = r6;
        Timeline r2 = this.timeline;
        this.mediaSourceCallers.add(r4);
        if (this.looper != null) goto L12;
        this.looper = r0;
        this.enabledMediaSourceCallers.add(r4);
        prepareSourceInternal(r5);
        return;
    L12:
        if (r2 == null) goto L16;
        enable(r4);
        r4.onSourceInfoRefreshed(this, r2);
        return;
    L16:
        return;
    L5:
        if (this.looper == r0) goto L8;
        r1 = false;
        goto L9
    }

    protected final boolean prepareSourceCalled() {
        return !this.mediaSourceCallers.isEmpty();
    }

    protected abstract void prepareSourceInternal(TransferListener r1);

    protected final void refreshSourceInfo(Timeline r3) {
        this.timeline = r3;
        Iterator<MediaSource.MediaSourceCaller> r0 = this.mediaSourceCallers.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().onSourceInfoRefreshed(this, r3);
        goto L4
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public final void releaseSource(MediaSource.MediaSourceCaller r2) {
        this.mediaSourceCallers.remove(r2);
        if (this.mediaSourceCallers.isEmpty() == false) goto L5;
        this.looper = null;
        this.timeline = null;
        this.playerId = null;
        this.enabledMediaSourceCallers.clear();
        releaseSourceInternal();
        return;
    L5:
        disable(r2);
    }

    protected abstract void releaseSourceInternal();

    @Override // androidx.media3.exoplayer.source.MediaSource
    public final void removeDrmEventListener(DrmSessionEventListener r2) {
        this.drmEventDispatcher.removeEventListener(r2);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public final void removeEventListener(MediaSourceEventListener r2) {
        this.eventDispatcher.removeEventListener(r2);
    }

    protected final void setPlayerId(PlayerId r1) {
        this.playerId = r1;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public /* synthetic */ void updateMediaItem(MediaItem r1) {
        MediaSource.CC.$default$updateMediaItem(this, r1);
    }
}
