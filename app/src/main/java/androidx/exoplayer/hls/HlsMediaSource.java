package androidx.media3.exoplayer.hls;

import android.os.Looper;
import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.StreamKey;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.drm.DefaultDrmSessionManagerProvider;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider;
import androidx.media3.exoplayer.hls.playlist.DefaultHlsPlaylistParserFactory;
import androidx.media3.exoplayer.hls.playlist.DefaultHlsPlaylistTracker;
import androidx.media3.exoplayer.hls.playlist.FilteringHlsPlaylistParserFactory;
import androidx.media3.exoplayer.hls.playlist.HlsMediaPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsMultivariantPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory;
import androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker;
import androidx.media3.exoplayer.source.BaseMediaSource;
import androidx.media3.exoplayer.source.CompositeSequenceableLoaderFactory;
import androidx.media3.exoplayer.source.DefaultCompositeSequenceableLoaderFactory;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.MediaSourceEventListener;
import androidx.media3.exoplayer.source.MediaSourceFactory;
import androidx.media3.exoplayer.source.SinglePeriodTimeline;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.DefaultLoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.extractor.text.SubtitleParser;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class HlsMediaSource extends BaseMediaSource implements HlsPlaylistTracker.PrimaryPlaylistListener {
    public static final int METADATA_TYPE_EMSG = 3;
    public static final int METADATA_TYPE_ID3 = 1;
    private final boolean allowChunklessPreparation;
    private final CmcdConfiguration cmcdConfiguration;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final HlsDataSourceFactory dataSourceFactory;
    private final DrmSessionManager drmSessionManager;
    private final long elapsedRealTimeOffsetMs;
    private final HlsExtractorFactory extractorFactory;
    private MediaItem.LiveConfiguration liveConfiguration;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private MediaItem mediaItem;
    private TransferListener mediaTransferListener;
    private final int metadataType;
    private final HlsPlaylistTracker playlistTracker;
    private final long timestampAdjusterInitializationTimeoutMs;
    private final boolean useSessionKeys;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.hls.HlsMediaSource$1 */
    static /* synthetic */ class C02921 {
    }

    public static final class Factory implements MediaSourceFactory {
        private boolean allowChunklessPreparation;
        private CmcdConfiguration.Factory cmcdConfigurationFactory;
        private CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
        private DrmSessionManagerProvider drmSessionManagerProvider;
        private long elapsedRealTimeOffsetMs;
        private HlsExtractorFactory extractorFactory;
        private final HlsDataSourceFactory hlsDataSourceFactory;
        private LoadErrorHandlingPolicy loadErrorHandlingPolicy;
        private int metadataType;
        private boolean parseSubtitlesDuringExtraction;
        private HlsPlaylistParserFactory playlistParserFactory;
        private HlsPlaylistTracker.Factory playlistTrackerFactory;
        private SubtitleParser.Factory subtitleParserFactoryOverride;
        private long timestampAdjusterInitializationTimeoutMs;
        private boolean useSessionKeys;

        public Factory(DataSource.Factory r2) {
            this(new DefaultHlsDataSourceFactory(r2));
        }

        public Factory(HlsDataSourceFactory r4) {
            this.hlsDataSourceFactory = (HlsDataSourceFactory) Assertions.checkNotNull(r4);
            this.drmSessionManagerProvider = new DefaultDrmSessionManagerProvider();
            this.playlistParserFactory = new DefaultHlsPlaylistParserFactory();
            this.playlistTrackerFactory = DefaultHlsPlaylistTracker.FACTORY;
            this.loadErrorHandlingPolicy = new DefaultLoadErrorHandlingPolicy();
            this.compositeSequenceableLoaderFactory = new DefaultCompositeSequenceableLoaderFactory();
            this.metadataType = 1;
            this.elapsedRealTimeOffsetMs = C0180C.TIME_UNSET;
            this.allowChunklessPreparation = true;
            experimentalParseSubtitlesDuringExtraction(true);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public HlsMediaSource createMediaSource(MediaItem r23) {
            Assertions.checkNotNull(r23.localConfiguration);
            if (this.extractorFactory != null) goto L6;
            this.extractorFactory = new DefaultHlsExtractorFactory();
        L6:
            if (this.subtitleParserFactoryOverride == null) goto L8;
            this.extractorFactory.setSubtitleParserFactory(this.subtitleParserFactoryOverride);
        L8:
            this.extractorFactory.experimentalParseSubtitlesDuringExtraction(this.parseSubtitlesDuringExtraction);
            HlsExtractorFactory r4 = this.extractorFactory;
            HlsPlaylistParserFactory r1 = this.playlistParserFactory;
            List<StreamKey> r3 = r23.localConfiguration.streamKeys;
            if (r3.isEmpty() == true) goto L12;
            r1 = new FilteringHlsPlaylistParserFactory(r1, r3);
        L12:
            if (this.cmcdConfigurationFactory != null) goto L14;
            CmcdConfiguration r6 = null;
        L16:
            return new HlsMediaSource(r23, this.hlsDataSourceFactory, r4, this.compositeSequenceableLoaderFactory, r6, this.drmSessionManagerProvider.get(r23), this.loadErrorHandlingPolicy, this.playlistTrackerFactory.createTracker(this.hlsDataSourceFactory, this.loadErrorHandlingPolicy, r1), this.elapsedRealTimeOffsetMs, this.allowChunklessPreparation, this.metadataType, this.useSessionKeys, this.timestampAdjusterInitializationTimeoutMs, null);
        L14:
            r6 = this.cmcdConfigurationFactory.createCmcdConfiguration(r23);
            goto L16
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource createMediaSource(MediaItem r1) {
            return createMediaSource(r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        @Deprecated
        public Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
            this.parseSubtitlesDuringExtraction = r1;
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        @Deprecated
        public /* bridge */ /* synthetic */ MediaSource.Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
            return experimentalParseSubtitlesDuringExtraction(r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public int[] getSupportedTypes() {
            return new int[]{2};
        }

        public Factory setAllowChunklessPreparation(boolean r1) {
            this.allowChunklessPreparation = r1;
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public Factory setCmcdConfigurationFactory(CmcdConfiguration.Factory r2) {
            this.cmcdConfigurationFactory = (CmcdConfiguration.Factory) Assertions.checkNotNull(r2);
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource.Factory setCmcdConfigurationFactory(CmcdConfiguration.Factory r1) {
            return setCmcdConfigurationFactory(r1);
        }

        public Factory setCompositeSequenceableLoaderFactory(CompositeSequenceableLoaderFactory r2) {
            this.compositeSequenceableLoaderFactory = (CompositeSequenceableLoaderFactory) Assertions.checkNotNull(r2, "HlsMediaSource.Factory#setCompositeSequenceableLoaderFactory no longer handles null by instantiating a new DefaultCompositeSequenceableLoaderFactory. Explicitly construct and pass an instance in order to retain the old behavior.");
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public Factory setDrmSessionManagerProvider(DrmSessionManagerProvider r2) {
            this.drmSessionManagerProvider = (DrmSessionManagerProvider) Assertions.checkNotNull(r2, "MediaSource.Factory#setDrmSessionManagerProvider no longer handles null by instantiating a new DefaultDrmSessionManagerProvider. Explicitly construct and pass an instance in order to retain the old behavior.");
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource.Factory setDrmSessionManagerProvider(DrmSessionManagerProvider r1) {
            return setDrmSessionManagerProvider(r1);
        }

        Factory setElapsedRealTimeOffsetMs(long r1) {
            this.elapsedRealTimeOffsetMs = r1;
            return this;
        }

        public Factory setExtractorFactory(HlsExtractorFactory r1) {
            this.extractorFactory = r1;
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r2) {
            this.loadErrorHandlingPolicy = (LoadErrorHandlingPolicy) Assertions.checkNotNull(r2, "MediaSource.Factory#setLoadErrorHandlingPolicy no longer handles null by instantiating a new DefaultLoadErrorHandlingPolicy. Explicitly construct and pass an instance in order to retain the old behavior.");
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource.Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r1) {
            return setLoadErrorHandlingPolicy(r1);
        }

        public Factory setMetadataType(int r1) {
            this.metadataType = r1;
            return this;
        }

        public Factory setPlaylistParserFactory(HlsPlaylistParserFactory r2) {
            this.playlistParserFactory = (HlsPlaylistParserFactory) Assertions.checkNotNull(r2, "HlsMediaSource.Factory#setPlaylistParserFactory no longer handles null by instantiating a new DefaultHlsPlaylistParserFactory. Explicitly construct and pass an instance in order to retain the old behavior.");
            return this;
        }

        public Factory setPlaylistTrackerFactory(HlsPlaylistTracker.Factory r2) {
            this.playlistTrackerFactory = (HlsPlaylistTracker.Factory) Assertions.checkNotNull(r2, "HlsMediaSource.Factory#setPlaylistTrackerFactory no longer handles null by defaulting to DefaultHlsPlaylistTracker.FACTORY. Explicitly pass a reference to this instance in order to retain the old behavior.");
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public Factory setSubtitleParserFactory(SubtitleParser.Factory r1) {
            this.subtitleParserFactoryOverride = r1;
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource.Factory setSubtitleParserFactory(SubtitleParser.Factory r1) {
            return setSubtitleParserFactory(r1);
        }

        public Factory setTimestampAdjusterInitializationTimeoutMs(long r1) {
            this.timestampAdjusterInitializationTimeoutMs = r1;
            return this;
        }

        public Factory setUseSessionKeys(boolean r1) {
            this.useSessionKeys = r1;
            return this;
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface MetadataType {
    }

    static {
        MediaLibraryInfo.registerModule("media3.exoplayer.hls");
    }

    private HlsMediaSource(MediaItem r17, HlsDataSourceFactory r18, HlsExtractorFactory r19, CompositeSequenceableLoaderFactory r20, CmcdConfiguration r21, DrmSessionManager r22, LoadErrorHandlingPolicy r23, HlsPlaylistTracker r24, long r25, boolean r27, int r28, boolean r29, long r30) {
        this.mediaItem = r17;
        this.liveConfiguration = r17.liveConfiguration;
        this.dataSourceFactory = r18;
        this.extractorFactory = r19;
        this.compositeSequenceableLoaderFactory = r20;
        this.cmcdConfiguration = r21;
        this.drmSessionManager = r22;
        this.loadErrorHandlingPolicy = r23;
        this.playlistTracker = r24;
        this.elapsedRealTimeOffsetMs = r25;
        this.allowChunklessPreparation = r27;
        this.metadataType = r28;
        this.useSessionKeys = r29;
        this.timestampAdjusterInitializationTimeoutMs = r30;
    }

    /* synthetic */ HlsMediaSource(MediaItem r1, HlsDataSourceFactory r2, HlsExtractorFactory r3, CompositeSequenceableLoaderFactory r4, CmcdConfiguration r5, DrmSessionManager r6, LoadErrorHandlingPolicy r7, HlsPlaylistTracker r8, long r9, boolean r11, int r12, boolean r13, long r14, C02921 r16) {
        this(r1, r2, r3, r4, r5, r6, r7, r8, r9, r11, r12, r13, r14);
    }

    private SinglePeriodTimeline createTimelineForLive(HlsMediaPlaylist r28, long r29, long r31, HlsManifest r33) {
        long r17 = r28.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
        if (r28.hasEndTag == false) goto L5;
        long r13 = r17 + r28.durationUs;
    L6:
        long r7 = getLiveEdgeOffsetUs(r28);
        if (this.liveConfiguration.targetOffsetMs == C0180C.TIME_UNSET) goto L9;
        long r5 = Util.msToUs(this.liveConfiguration.targetOffsetMs);
    L10:
        updateLiveConfiguration(r28, Util.constrainValue(r5, r7, r28.durationUs + r7));
        long r19 = getLiveWindowDefaultStartPositionUs(r28, r7);
        if (r28.playlistType == 2) goto L13;
    L15:
        boolean r23 = false;
    L17:
        return new SinglePeriodTimeline(r29, r31, C0180C.TIME_UNSET, r13, r28.durationUs, r17, r19, true, !r28.hasEndTag, r23, r33, getMediaItem(), this.liveConfiguration);
    L13:
        if (r28.hasPositiveStartOffset == false) goto L15;
        r23 = true;
        goto L17
    L9:
        r5 = getTargetLiveOffsetUs(r28, r7);
        goto L10
    L5:
        r13 = -9223372036854775807L;
        goto L6
    }

    private SinglePeriodTimeline createTimelineForOnDemand(HlsMediaPlaylist r25, long r26, long r28, HlsManifest r30) {
        if (r25.startOffsetUs != C0180C.TIME_UNSET) goto L5;
    L14:
        long r16 = 0;
    L16:
        return new SinglePeriodTimeline(r26, r28, C0180C.TIME_UNSET, r25.durationUs, r25.durationUs, 0, r16, true, false, true, r30, getMediaItem(), null);
    L5:
        if (r25.segments.isEmpty() == true) goto L14;
        if (r25.preciseStart == false) goto L10;
    L13:
        r16 = r25.startOffsetUs;
        goto L16
    L10:
        if (r25.startOffsetUs == r25.durationUs) goto L13;
        r16 = findClosestPrecedingSegment(r25.segments, r25.startOffsetUs).relativeStartTimeUs;
        goto L16
    }

    private static HlsMediaPlaylist.Part findClosestPrecedingIndependentPart(List<HlsMediaPlaylist.Part> r5, long r6) {
        HlsMediaPlaylist.Part r0 = null;
        int r1 = 0;
    L4:
        if (r1 >= r5.size()) goto L14;
        HlsMediaPlaylist.Part r2 = r5.get(r1);
        if (r2.relativeStartTimeUs > r6) goto L11;
        if (r2.isIndependent == false) goto L11;
        r0 = r2;
    L13:
        r1 = r1 + 1;
    L11:
        if (r2.relativeStartTimeUs <= r6) goto L13;
    L14:
        return r0;
    }

    private static HlsMediaPlaylist.Segment findClosestPrecedingSegment(List<HlsMediaPlaylist.Segment> r2, long r3) {
        return r2.get(Util.binarySearchFloor(r2, Long.valueOf(r3), true, true));
    }

    private long getLiveEdgeOffsetUs(HlsMediaPlaylist r5) {
        if (r5.hasProgramDateTime == true) goto L7;
        return 0;
    L7:
        return Util.msToUs(Util.getNowUnixTimeMs(this.elapsedRealTimeOffsetMs)) - r5.getEndTimeUs();
    }

    private long getLiveWindowDefaultStartPositionUs(HlsMediaPlaylist r7, long r8) {
        if (r7.startOffsetUs == C0180C.TIME_UNSET) goto L5;
        long r0 = r7.startOffsetUs;
    L7:
        if (r7.preciseStart == false) goto L9;
        return r0;
    L9:
        HlsMediaPlaylist.Part r2 = findClosestPrecedingIndependentPart(r7.trailingParts, r0);
        if (r2 == null) goto L14;
        return r2.relativeStartTimeUs;
    L14:
        if (r7.segments.isEmpty() == false) goto L17;
        return 0;
    L17:
        HlsMediaPlaylist.Segment r3 = findClosestPrecedingSegment(r7.segments, r0);
        HlsMediaPlaylist.Part r4 = findClosestPrecedingIndependentPart(r3.parts, r0);
        if (r4 == null) goto L22;
        return r4.relativeStartTimeUs;
    L22:
        return r3.relativeStartTimeUs;
    L5:
        r0 = (r7.durationUs + r8) - Util.msToUs(this.liveConfiguration.targetOffsetMs);
        goto L7
    }

    private static long getTargetLiveOffsetUs(HlsMediaPlaylist r5, long r6) {
        HlsMediaPlaylist.ServerControl r0 = r5.serverControl;
        if (r5.startOffsetUs == C0180C.TIME_UNSET) goto L6;
        long r1 = r5.durationUs - r5.startOffsetUs;
    L15:
        return r1 + r6;
    L6:
        if (r0.partHoldBackUs == C0180C.TIME_UNSET) goto L11;
        if (r5.partTargetDurationUs == C0180C.TIME_UNSET) goto L11;
        r1 = r0.partHoldBackUs;
    L11:
        if (r0.holdBackUs == C0180C.TIME_UNSET) goto L13;
        r1 = r0.holdBackUs;
        goto L15
    L13:
        r1 = 3 * r5.targetDurationUs;
        goto L15
    }

    private void updateLiveConfiguration(HlsMediaPlaylist r6, long r7) {
        MediaItem.LiveConfiguration r0 = getMediaItem().liveConfiguration;
        if (r0.minPlaybackSpeed == (-3.4028235E38f)) goto L5;
    L11:
        boolean r1 = false;
    L12:
        MediaItem.LiveConfiguration.Builder r2 = new MediaItem.LiveConfiguration.Builder().setTargetOffsetMs(Util.usToMs(r7));
        float r3 = 1.0f;
        if (r1 == false) goto L15;
        float r4 = 1.0f;
    L16:
        MediaItem.LiveConfiguration.Builder r5 = r2.setMinPlaybackSpeed(r4);
        if (r1 == true) goto L20;
        r3 = this.liveConfiguration.maxPlaybackSpeed;
    L20:
        this.liveConfiguration = r5.setMaxPlaybackSpeed(r3).build();
        return;
    L15:
        r4 = this.liveConfiguration.minPlaybackSpeed;
        goto L16
    L5:
        if (r0.maxPlaybackSpeed != (-3.4028235E38f)) goto L11;
        if (r6.serverControl.holdBackUs != C0180C.TIME_UNSET) goto L11;
        if (r6.serverControl.partHoldBackUs != C0180C.TIME_UNSET) goto L11;
        r1 = true;
        goto L12
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean canUpdateMediaItem(MediaItem r6) {
        MediaItem r0 = getMediaItem();
        MediaItem.LocalConfiguration r1 = (MediaItem.LocalConfiguration) Assertions.checkNotNull(r0.localConfiguration);
        MediaItem.LocalConfiguration r2 = r6.localConfiguration;
        if (r2 != null) goto L5;
    L13:
        return false;
    L5:
        if (r2.uri.equals(r1.uri) == false) goto L13;
        if (r2.streamKeys.equals(r1.streamKeys) == false) goto L13;
        if (Util.areEqual(r2.drmConfiguration, r1.drmConfiguration) == false) goto L13;
        if (r0.liveConfiguration.equals(r6.liveConfiguration) == false) goto L13;
        return true;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r22, Allocator r23, long r24) {
        MediaSourceEventListener.EventDispatcher r10 = createEventDispatcher(r22);
        DrmSessionEventListener.EventDispatcher r8 = createDrmEventDispatcher(r22);
        return new HlsMediaPeriod(this.extractorFactory, this.playlistTracker, this.dataSourceFactory, this.mediaTransferListener, this.cmcdConfiguration, this.drmSessionManager, r8, this.loadErrorHandlingPolicy, r10, r23, this.compositeSequenceableLoaderFactory, this.allowChunklessPreparation, this.metadataType, this.useSessionKeys, getPlayerId(), this.timestampAdjusterInitializationTimeoutMs);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public synchronized MediaItem getMediaItem() {
        monitor-enter(this);
        MediaItem r0 = this.mediaItem;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.playlistTracker.maybeThrowPrimaryPlaylistRefreshError();
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker.PrimaryPlaylistListener
    public void onPrimaryPlaylistRefreshed(HlsMediaPlaylist r13) {
        if (r13.hasProgramDateTime == false) goto L5;
        long r3 = Util.usToMs(r13.startTimeUs);
    L6:
        long r9 = r3;
        if (r13.playlistType != 2) goto L9;
    L12:
        long r7 = r9;
    L13:
        HlsManifest r11 = new HlsManifest((HlsMultivariantPlaylist) Assertions.checkNotNull(this.playlistTracker.getMultivariantPlaylist()), r13);
        if (this.playlistTracker.isLive() == false) goto L16;
        SinglePeriodTimeline r14 = createTimelineForLive(r13, r7, r9, r11);
    L17:
        refreshSourceInfo(r14);
        return;
    L16:
        r14 = createTimelineForOnDemand(r13, r7, r9, r11);
        goto L17
    L9:
        if (r13.playlistType == 1) goto L12;
        r7 = -9223372036854775807L;
        goto L13
    L5:
        r3 = -9223372036854775807L;
        goto L6
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void prepareSourceInternal(TransferListener r4) {
        this.mediaTransferListener = r4;
        this.drmSessionManager.setPlayer((Looper) Assertions.checkNotNull(Looper.myLooper()), getPlayerId());
        this.drmSessionManager.prepare();
        MediaSourceEventListener.EventDispatcher r0 = createEventDispatcher(null);
        this.playlistTracker.start(((MediaItem.LocalConfiguration) Assertions.checkNotNull(getMediaItem().localConfiguration)).uri, r0, this);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r2) {
        ((HlsMediaPeriod) r2).release();
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
        this.playlistTracker.stop();
        this.drmSessionManager.release();
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public synchronized void updateMediaItem(MediaItem r1) {
        monitor-enter(this);
        this.mediaItem = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }
}
