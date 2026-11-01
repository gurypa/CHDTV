package androidx.media3.exoplayer.source;

import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.extractor.text.SubtitleParser;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class ExternallyLoadedMediaSource extends BaseMediaSource {
    private final ExternalLoader externalLoader;
    private MediaItem mediaItem;
    private final long timelineDurationUs;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.ExternallyLoadedMediaSource$1 */
    static /* synthetic */ class C03341 {
    }

    public static final class Factory implements MediaSource.Factory {
        private final ExternalLoader externalLoader;
        private final long timelineDurationUs;

        public Factory(long r1, ExternalLoader r3) {
            this.timelineDurationUs = r1;
            this.externalLoader = r3;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public ExternallyLoadedMediaSource createMediaSource(MediaItem r7) {
            return new ExternallyLoadedMediaSource(r7, this.timelineDurationUs, this.externalLoader, null);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource createMediaSource(MediaItem r1) {
            return createMediaSource(r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* synthetic */ MediaSource.Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
            return MediaSource.Factory.CC.$default$experimentalParseSubtitlesDuringExtraction(this, r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public int[] getSupportedTypes() {
            return new int[]{4};
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* synthetic */ MediaSource.Factory setCmcdConfigurationFactory(CmcdConfiguration.Factory r1) {
            return MediaSource.Factory.CC.$default$setCmcdConfigurationFactory(this, r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public MediaSource.Factory setDrmSessionManagerProvider(DrmSessionManagerProvider r1) {
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public MediaSource.Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r1) {
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* synthetic */ MediaSource.Factory setSubtitleParserFactory(SubtitleParser.Factory r1) {
            return MediaSource.Factory.CC.$default$setSubtitleParserFactory(this, r1);
        }
    }

    private ExternallyLoadedMediaSource(MediaItem r1, long r2, ExternalLoader r4) {
        this.mediaItem = r1;
        this.timelineDurationUs = r2;
        this.externalLoader = r4;
    }

    /* synthetic */ ExternallyLoadedMediaSource(MediaItem r1, long r2, ExternalLoader r4, C03341 r5) {
        this(r1, r2, r4);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean canUpdateMediaItem(MediaItem r7) {
        MediaItem.LocalConfiguration r0 = r7.localConfiguration;
        MediaItem.LocalConfiguration r1 = (MediaItem.LocalConfiguration) Assertions.checkNotNull(getMediaItem().localConfiguration);
        if (r0 != null) goto L5;
    L13:
        return false;
    L5:
        if (r0.uri.equals(r1.uri) == false) goto L13;
        if (Objects.equals(r0.mimeType, r1.mimeType) == false) goto L13;
        if (r0.imageDurationMs != C0180C.TIME_UNSET) goto L11;
    L12:
        return true;
    L11:
        if (Util.msToUs(r0.imageDurationMs) != this.timelineDurationUs) goto L13;
        goto L12
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r6, Allocator r7, long r8) {
        MediaItem r0 = getMediaItem();
        Assertions.checkNotNull(r0.localConfiguration);
        Assertions.checkNotNull(r0.localConfiguration.mimeType, "Externally loaded mediaItems require a MIME type.");
        return new ExternallyLoadedMediaPeriod(r0.localConfiguration.uri, r0.localConfiguration.mimeType, this.externalLoader);
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
    public void maybeThrowSourceInfoRefreshError() {
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void prepareSourceInternal(TransferListener r9) {
        refreshSourceInfo(new SinglePeriodTimeline(this.timelineDurationUs, true, false, false, null, getMediaItem()));
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r2) {
        ((ExternallyLoadedMediaPeriod) r2).releasePeriod();
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
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
