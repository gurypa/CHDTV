package androidx.media3.exoplayer.source.ads;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.media3.common.AdPlaybackState;
import androidx.media3.common.AdViewProvider;
import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.source.CompositeMediaSource;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MaskingMediaPeriod;
import androidx.media3.exoplayer.source.MaskingMediaSource;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.MediaSourceEventListener;
import androidx.media3.exoplayer.upstream.Allocator;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class AdsMediaSource extends CompositeMediaSource<MediaSource.MediaPeriodId> {
    private static final MediaSource.MediaPeriodId CHILD_SOURCE_MEDIA_PERIOD_ID = null;
    private final MediaSource.Factory adMediaSourceFactory;
    private AdMediaSourceHolder[][] adMediaSourceHolders;
    private AdPlaybackState adPlaybackState;
    private final DataSpec adTagDataSpec;
    private final AdViewProvider adViewProvider;
    private final Object adsId;
    private final AdsLoader adsLoader;
    private ComponentListener componentListener;
    final MediaItem.DrmConfiguration contentDrmConfiguration;
    private final MaskingMediaSource contentMediaSource;
    private Timeline contentTimeline;
    private final Handler mainHandler;
    private final Timeline.Period period;

    public static final class AdLoadException extends IOException {
        public static final int TYPE_AD = 0;
        public static final int TYPE_AD_GROUP = 1;
        public static final int TYPE_ALL_ADS = 2;
        public static final int TYPE_UNEXPECTED = 3;
        public final int type;

        @Target({ElementType.TYPE_USE})
        @Documented
        @Retention(RetentionPolicy.SOURCE)
        public @interface Type {
        }

        private AdLoadException(int r1, Exception r2) {
            super(r2);
            this.type = r1;
        }

        public static AdLoadException createForAd(Exception r2) {
            return new AdLoadException(0, r2);
        }

        public static AdLoadException createForAdGroup(Exception r4, int r5) {
            return new AdLoadException(1, new IOException("Failed to load ad group " + r5, r4));
        }

        public static AdLoadException createForAllAds(Exception r2) {
            return new AdLoadException(2, r2);
        }

        public static AdLoadException createForUnexpected(RuntimeException r2) {
            return new AdLoadException(3, r2);
        }

        public RuntimeException getRuntimeExceptionForUnexpected() {
            if (this.type != 3) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkState(r0);
            return (RuntimeException) Assertions.checkNotNull(getCause());
        L5:
            r0 = false;
            goto L6
        }
    }

    private final class AdMediaSourceHolder {
        private final List<MaskingMediaPeriod> activeMediaPeriods;
        private MediaItem adMediaItem;
        private MediaSource adMediaSource;

        /* JADX INFO: renamed from: id */
        private final MediaSource.MediaPeriodId f46id;
        final /* synthetic */ AdsMediaSource this$0;
        private Timeline timeline;

        public AdMediaSourceHolder(AdsMediaSource r1, MediaSource.MediaPeriodId r2) {
            this.this$0 = r1;
            this.f46id = r2;
            this.activeMediaPeriods = new ArrayList();
        }

        public MediaPeriod createMediaPeriod(MediaSource.MediaPeriodId r6, Allocator r7, long r8) {
            MaskingMediaPeriod r0 = new MaskingMediaPeriod(r6, r7, r8);
            this.activeMediaPeriods.add(r0);
            if (this.adMediaSource == null) goto L6;
            r0.setMediaSource(this.adMediaSource);
            r0.setPrepareListener(new AdPrepareListener(this.this$0, (MediaItem) Assertions.checkNotNull(this.adMediaItem)));
        L6:
            if (this.timeline == null) goto L8;
            r0.createPeriod(new MediaSource.MediaPeriodId(this.timeline.getUidOfPeriod(0), r6.windowSequenceNumber));
        L8:
            return r0;
        }

        public long getDurationUs() {
            if (this.timeline != null) goto L6;
            return C0180C.TIME_UNSET;
        L6:
            return this.timeline.getPeriod(0, AdsMediaSource.access$600(this.this$0)).getDurationUs();
        }

        public void handleSourceInfoRefresh(Timeline r7) {
            boolean r2 = true;
            if (r7.getPeriodCount() == 1) goto L6;
            r2 = false;
        L6:
            Assertions.checkArgument(r2);
            if (this.timeline != null) goto L12;
            Object r0 = r7.getUidOfPeriod(0);
            int r1 = 0;
        L10:
            if (r1 >= this.activeMediaPeriods.size()) goto L12;
            MaskingMediaPeriod r3 = this.activeMediaPeriods.get(r1);
            r3.createPeriod(new MediaSource.MediaPeriodId(r0, r3.f44id.windowSequenceNumber));
            r1 = r1 + 1;
        L12:
            this.timeline = r7;
        }

        public boolean hasMediaSource() {
            if (this.adMediaSource == null) goto L5;
            return true;
        L5:
            return false;
        }

        public void initializeWithMediaSource(MediaSource r5, MediaItem r6) {
            this.adMediaSource = r5;
            this.adMediaItem = r6;
            int r0 = 0;
        L4:
            if (r0 >= this.activeMediaPeriods.size()) goto L6;
            MaskingMediaPeriod r1 = this.activeMediaPeriods.get(r0);
            r1.setMediaSource(r5);
            r1.setPrepareListener(new AdPrepareListener(this.this$0, r6));
            r0 = r0 + 1;
            goto L4
        L6:
            AdsMediaSource.access$500(this.this$0, this.f46id, r5);
        }

        public boolean isInactive() {
            return this.activeMediaPeriods.isEmpty();
        }

        public void release() {
            if (hasMediaSource() == false) goto L6;
            AdsMediaSource.access$700(this.this$0, this.f46id);
            return;
        }

        public void releaseMediaPeriod(MaskingMediaPeriod r2) {
            this.activeMediaPeriods.remove(r2);
            r2.releasePeriod();
        }
    }

    private final class AdPrepareListener implements MaskingMediaPeriod.PrepareListener {
        private final MediaItem adMediaItem;
        final /* synthetic */ AdsMediaSource this$0;

        public AdPrepareListener(AdsMediaSource r1, MediaItem r2) {
            this.this$0 = r1;
            this.adMediaItem = r2;
        }

        /* JADX INFO: renamed from: lambda$onPrepareComplete$0$androidx-media3-exoplayer-source-ads-AdsMediaSource$AdPrepareListener */
        /* synthetic */ void m133x672fc1f4(MediaSource.MediaPeriodId r5) {
            AdsMediaSource.access$400(this.this$0).handlePrepareComplete(this.this$0, r5.adGroupIndex, r5.adIndexInAdGroup);
        }

        /* JADX INFO: renamed from: lambda$onPrepareError$1$androidx-media3-exoplayer-source-ads-AdsMediaSource$AdPrepareListener */
        /* synthetic */ void m134xa9898f8e(MediaSource.MediaPeriodId r5, IOException r6) {
            AdsMediaSource.access$400(this.this$0).handlePrepareError(this.this$0, r5.adGroupIndex, r5.adIndexInAdGroup, r6);
        }

        @Override // androidx.media3.exoplayer.source.MaskingMediaPeriod.PrepareListener
        public void onPrepareComplete(final MediaSource.MediaPeriodId r3) {
            AdsMediaSource.access$200(this.this$0).post(new AdsMediaSource$AdPrepareListener$$ExternalSyntheticLambda1(this, r3));
        }

        @Override // androidx.media3.exoplayer.source.MaskingMediaPeriod.PrepareListener
        public void onPrepareError(final MediaSource.MediaPeriodId r8, final IOException r9) {
            AdsMediaSource.access$300(this.this$0, r8).loadError(new LoadEventInfo(LoadEventInfo.getNewId(), new DataSpec(((MediaItem.LocalConfiguration) Assertions.checkNotNull(this.adMediaItem.localConfiguration)).uri), SystemClock.elapsedRealtime()), 6, AdLoadException.createForAd(r9), true);
            AdsMediaSource.access$200(this.this$0).post(new AdsMediaSource$AdPrepareListener$$ExternalSyntheticLambda0(this, r8, r9));
        }
    }

    private final class ComponentListener implements AdsLoader.EventListener {
        private final Handler playerHandler;
        private volatile boolean stopped;
        final /* synthetic */ AdsMediaSource this$0;

        public ComponentListener(AdsMediaSource r1) {
            this.this$0 = r1;
            this.playerHandler = Util.createHandlerForCurrentLooper();
        }

        /* JADX INFO: renamed from: lambda$onAdPlaybackState$0$androidx-media3-exoplayer-source-ads-AdsMediaSource$ComponentListener */
        /* synthetic */ void m135x6396e000(AdPlaybackState r2) {
            if (this.stopped == false) goto L5;
            return;
        L5:
            AdsMediaSource.access$100(this.this$0, r2);
        }

        @Override // androidx.media3.exoplayer.source.ads.AdsLoader.EventListener
        public /* synthetic */ void onAdClicked() {
            AdsLoader.EventListener.CC.$default$onAdClicked(this);
        }

        @Override // androidx.media3.exoplayer.source.ads.AdsLoader.EventListener
        public void onAdLoadError(AdLoadException r8, DataSpec r9) {
            if (this.stopped == false) goto L5;
            return;
        L5:
            AdsMediaSource.access$000(this.this$0, null).loadError(new LoadEventInfo(LoadEventInfo.getNewId(), r9, SystemClock.elapsedRealtime()), 6, r8, true);
        }

        @Override // androidx.media3.exoplayer.source.ads.AdsLoader.EventListener
        public void onAdPlaybackState(final AdPlaybackState r3) {
            if (this.stopped == false) goto L5;
            return;
        L5:
            this.playerHandler.post(new AdsMediaSource$ComponentListener$$ExternalSyntheticLambda0(this, r3));
        }

        @Override // androidx.media3.exoplayer.source.ads.AdsLoader.EventListener
        public /* synthetic */ void onAdTapped() {
            AdsLoader.EventListener.CC.$default$onAdTapped(this);
        }

        public void stop() {
            this.stopped = true;
            this.playerHandler.removeCallbacksAndMessages(null);
        }
    }

    static {
        CHILD_SOURCE_MEDIA_PERIOD_ID = new MediaSource.MediaPeriodId(new Object());
    }

    public AdsMediaSource(MediaSource r3, DataSpec r4, Object r5, MediaSource.Factory r6, AdsLoader r7, AdViewProvider r8) {
        this.contentMediaSource = new MaskingMediaSource(r3, true);
        this.contentDrmConfiguration = ((MediaItem.LocalConfiguration) Assertions.checkNotNull(r3.getMediaItem().localConfiguration)).drmConfiguration;
        this.adMediaSourceFactory = r6;
        this.adsLoader = r7;
        this.adViewProvider = r8;
        this.adTagDataSpec = r4;
        this.adsId = r5;
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.period = new Timeline.Period();
        this.adMediaSourceHolders = new AdMediaSourceHolder[0][];
        r7.setSupportedContentTypes(r6.getSupportedTypes());
    }

    static /* synthetic */ MediaSourceEventListener.EventDispatcher access$000(AdsMediaSource r1, MediaSource.MediaPeriodId r2) {
        return r1.createEventDispatcher(r2);
    }

    static /* synthetic */ void access$100(AdsMediaSource r0, AdPlaybackState r1) {
        r0.onAdPlaybackState(r1);
    }

    static /* synthetic */ Handler access$200(AdsMediaSource r1) {
        return r1.mainHandler;
    }

    static /* synthetic */ MediaSourceEventListener.EventDispatcher access$300(AdsMediaSource r1, MediaSource.MediaPeriodId r2) {
        return r1.createEventDispatcher(r2);
    }

    static /* synthetic */ AdsLoader access$400(AdsMediaSource r1) {
        return r1.adsLoader;
    }

    static /* synthetic */ void access$500(AdsMediaSource r0, Object r1, MediaSource r2) {
        r0.prepareChildSource(r1, r2);
    }

    static /* synthetic */ Timeline.Period access$600(AdsMediaSource r1) {
        return r1.period;
    }

    static /* synthetic */ void access$700(AdsMediaSource r0, Object r1) {
        r0.releaseChildSource(r1);
    }

    private long[][] getAdDurationsUs() {
        long[][] r0 = new long[this.adMediaSourceHolders.length][];
        int r1 = 0;
    L4:
        if (r1 >= this.adMediaSourceHolders.length) goto L14;
        r0[r1] = new long[this.adMediaSourceHolders[r1].length];
        int r2 = 0;
    L7:
        if (r2 >= this.adMediaSourceHolders[r1].length) goto L13;
        AdMediaSourceHolder r3 = this.adMediaSourceHolders[r1][r2];
        long[] r4 = r0[r1];
        if (r3 != null) goto L11;
        long r5 = C0180C.TIME_UNSET;
    L12:
        r4[r2] = r5;
        r2 = r2 + 1;
        goto L7
    L11:
        r5 = r3.getDurationUs();
        goto L12
    L13:
        r1 = r1 + 1;
        goto L4
    L14:
        return r0;
    }

    private static MediaItem.AdsConfiguration getAdsConfiguration(MediaItem r1) {
        if (r1.localConfiguration != null) goto L6;
        return null;
    L6:
        return r1.localConfiguration.adsConfiguration;
    }

    private void maybeUpdateAdMediaSources() {
        AdPlaybackState r0 = this.adPlaybackState;
        if (r0 != null) goto L5;
        return;
    L5:
        int r1 = 0;
    L7:
        if (r1 >= this.adMediaSourceHolders.length) goto L25;
        int r2 = 0;
    L10:
        if (r2 >= this.adMediaSourceHolders[r1].length) goto L24;
        AdMediaSourceHolder r3 = this.adMediaSourceHolders[r1][r2];
        AdPlaybackState.AdGroup r4 = r0.getAdGroup(r1);
        if (r3 == null) goto L23;
        if (r3.hasMediaSource() == true) goto L23;
        if (r2 >= r4.mediaItems.length) goto L23;
        MediaItem r5 = r4.mediaItems[r2];
        if (r5 == null) goto L23;
        if (this.contentDrmConfiguration == null) goto L22;
        r5 = r5.buildUpon().setDrmConfiguration(this.contentDrmConfiguration).build();
    L22:
        r3.initializeWithMediaSource(this.adMediaSourceFactory.createMediaSource(r5), r5);
    L23:
        r2 = r2 + 1;
        goto L10
    L24:
        r1 = r1 + 1;
        goto L7
    }

    private void maybeUpdateSourceInfo() {
        Timeline r0 = this.contentTimeline;
        if (this.adPlaybackState == null) goto L10;
        if (r0 != null) goto L6;
        return;
    L6:
        if (this.adPlaybackState.adGroupCount != 0) goto L8;
        refreshSourceInfo(r0);
        return;
    L8:
        this.adPlaybackState = this.adPlaybackState.withAdDurationsUs(getAdDurationsUs());
        refreshSourceInfo(new SinglePeriodAdTimeline(r0, this.adPlaybackState));
        return;
    }

    private void onAdPlaybackState(AdPlaybackState r4) {
        boolean r1 = false;
        if (this.adPlaybackState != null) goto L6;
        this.adMediaSourceHolders = new AdMediaSourceHolder[r4.adGroupCount][];
        Arrays.fill(this.adMediaSourceHolders, new AdMediaSourceHolder[0]);
    L9:
        this.adPlaybackState = r4;
        maybeUpdateAdMediaSources();
        maybeUpdateSourceInfo();
        return;
    L6:
        if (r4.adGroupCount != this.adPlaybackState.adGroupCount) goto L8;
        r1 = true;
    L8:
        Assertions.checkState(r1);
        goto L9
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean canUpdateMediaItem(MediaItem r3) {
        if (Util.areEqual(getAdsConfiguration(getMediaItem()), getAdsConfiguration(r3)) == true) goto L5;
    L7:
        return false;
    L5:
        if (this.contentMediaSource.canUpdateMediaItem(r3) == false) goto L7;
        return true;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r7, Allocator r8, long r9) {
        if (((AdPlaybackState) Assertions.checkNotNull(this.adPlaybackState)).adGroupCount > 0) goto L5;
    L14:
        MaskingMediaPeriod r1 = new MaskingMediaPeriod(r7, r8, r9);
        r1.setMediaSource(this.contentMediaSource);
        r1.createPeriod(r7);
        return r1;
    L5:
        if (r7.isAd() == false) goto L14;
        int r2 = r7.adGroupIndex;
        int r3 = r7.adIndexInAdGroup;
        if (this.adMediaSourceHolders[r2].length > r3) goto L9;
        AdMediaSourceHolder[][] r4 = this.adMediaSourceHolders;
        AdMediaSourceHolder[] r5 = this.adMediaSourceHolders[r2];
        r4[r2] = (AdMediaSourceHolder[]) Arrays.copyOf(r5, r3 + 1);
    L9:
        AdMediaSourceHolder r6 = this.adMediaSourceHolders[r2][r3];
        if (r6 != null) goto L13;
        r6 = new AdMediaSourceHolder(this, r7);
        this.adMediaSourceHolders[r2][r3] = r6;
        maybeUpdateAdMediaSources();
    L13:
        return r6.createMediaPeriod(r7, r8, r9);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaItem getMediaItem() {
        return this.contentMediaSource.getMediaItem();
    }

    /* JADX INFO: renamed from: getMediaPeriodIdForChildMediaPeriodId, reason: avoid collision after fix types in other method */
    protected MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId2(MediaSource.MediaPeriodId r2, MediaSource.MediaPeriodId r3) {
        if (r2.isAd() == false) goto L6;
        return r2;
    L6:
        return r3;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(MediaSource.MediaPeriodId r1, MediaSource.MediaPeriodId r2) {
        return getMediaPeriodIdForChildMediaPeriodId2(r1, r2);
    }

    /* JADX INFO: renamed from: lambda$prepareSourceInternal$0$androidx-media3-exoplayer-source-ads-AdsMediaSource */
    /* synthetic */ void m131x9f9466de(ComponentListener r7) {
        this.adsLoader.start(this, this.adTagDataSpec, this.adsId, this.adViewProvider, r7);
    }

    /* JADX INFO: renamed from: lambda$releaseSourceInternal$1$androidx-media3-exoplayer-source-ads-AdsMediaSource */
    /* synthetic */ void m132x4d6cb35f(ComponentListener r2) {
        this.adsLoader.stop(this, r2);
    }

    /* JADX INFO: renamed from: onChildSourceInfoRefreshed, reason: avoid collision after fix types in other method */
    protected void onChildSourceInfoRefreshed2(MediaSource.MediaPeriodId r4, MediaSource r5, Timeline r6) {
        if (r4.isAd() == false) goto L5;
        int r0 = r4.adGroupIndex;
        int r1 = r4.adIndexInAdGroup;
        ((AdMediaSourceHolder) Assertions.checkNotNull(this.adMediaSourceHolders[r0][r1])).handleSourceInfoRefresh(r6);
    L10:
        maybeUpdateSourceInfo();
        return;
    L5:
        boolean r2 = true;
        if (r6.getPeriodCount() == 1) goto L9;
        r2 = false;
    L9:
        Assertions.checkArgument(r2);
        this.contentTimeline = r6;
        goto L10
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ void onChildSourceInfoRefreshed(MediaSource.MediaPeriodId r1, MediaSource r2, Timeline r3) {
        onChildSourceInfoRefreshed2(r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected void prepareSourceInternal(TransferListener r4) {
        super.prepareSourceInternal(r4);
        final ComponentListener r0 = new ComponentListener(this);
        this.componentListener = r0;
        this.contentTimeline = this.contentMediaSource.getTimeline();
        prepareChildSource(CHILD_SOURCE_MEDIA_PERIOD_ID, this.contentMediaSource);
        this.mainHandler.post(new AdsMediaSource$$ExternalSyntheticLambda0(this, r0));
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r7) {
        MaskingMediaPeriod r0 = (MaskingMediaPeriod) r7;
        MediaSource.MediaPeriodId r1 = r0.f44id;
        if (r1.isAd() == false) goto L8;
        AdMediaSourceHolder r2 = (AdMediaSourceHolder) Assertions.checkNotNull(this.adMediaSourceHolders[r1.adGroupIndex][r1.adIndexInAdGroup]);
        r2.releaseMediaPeriod(r0);
        if (r2.isInactive() == false) goto L11;
        r2.release();
        this.adMediaSourceHolders[r1.adGroupIndex][r1.adIndexInAdGroup] = null;
        return;
    L11:
        return;
    L8:
        r0.releasePeriod();
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
        super.releaseSourceInternal();
        final ComponentListener r0 = (ComponentListener) Assertions.checkNotNull(this.componentListener);
        this.componentListener = null;
        r0.stop();
        this.contentTimeline = null;
        this.adPlaybackState = null;
        this.adMediaSourceHolders = new AdMediaSourceHolder[0][];
        this.mainHandler.post(new AdsMediaSource$$ExternalSyntheticLambda1(this, r0));
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public void updateMediaItem(MediaItem r2) {
        this.contentMediaSource.updateMediaItem(r2);
    }
}
