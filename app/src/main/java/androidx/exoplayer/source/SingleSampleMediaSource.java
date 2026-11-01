package androidx.media3.exoplayer.source;

import android.net.Uri;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.DefaultLoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class SingleSampleMediaSource extends BaseMediaSource {
    private final DataSource.Factory dataSourceFactory;
    private final DataSpec dataSpec;
    private final long durationUs;
    private final Format format;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private final MediaItem mediaItem;
    private final Timeline timeline;
    private TransferListener transferListener;
    private final boolean treatLoadErrorsAsEndOfStream;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.SingleSampleMediaSource$1 */
    static /* synthetic */ class C03501 {
    }

    public static final class Factory {
        private final DataSource.Factory dataSourceFactory;
        private LoadErrorHandlingPolicy loadErrorHandlingPolicy;
        private Object tag;
        private String trackId;
        private boolean treatLoadErrorsAsEndOfStream;

        public Factory(DataSource.Factory r2) {
            this.dataSourceFactory = (DataSource.Factory) Assertions.checkNotNull(r2);
            this.loadErrorHandlingPolicy = new DefaultLoadErrorHandlingPolicy();
            this.treatLoadErrorsAsEndOfStream = true;
        }

        public SingleSampleMediaSource createMediaSource(MediaItem.SubtitleConfiguration r11, long r12) {
            return new SingleSampleMediaSource(this.trackId, r11, this.dataSourceFactory, r12, this.loadErrorHandlingPolicy, this.treatLoadErrorsAsEndOfStream, this.tag, null);
        }

        public Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r2) {
            if (r2 == null) goto L4;
            LoadErrorHandlingPolicy r0 = r2;
        L5:
            this.loadErrorHandlingPolicy = r0;
            return this;
        L4:
            r0 = new DefaultLoadErrorHandlingPolicy();
            goto L5
        }

        public Factory setTag(Object r1) {
            this.tag = r1;
            return this;
        }

        @Deprecated
        public Factory setTrackId(String r1) {
            this.trackId = r1;
            return this;
        }

        public Factory setTreatLoadErrorsAsEndOfStream(boolean r1) {
            this.treatLoadErrorsAsEndOfStream = r1;
            return this;
        }
    }

    private SingleSampleMediaSource(String r12, MediaItem.SubtitleConfiguration r13, DataSource.Factory r14, long r15, LoadErrorHandlingPolicy r17, boolean r18, Object r19) {
        this.dataSourceFactory = r14;
        this.durationUs = r15;
        this.loadErrorHandlingPolicy = r17;
        this.treatLoadErrorsAsEndOfStream = r18;
        this.mediaItem = new MediaItem.Builder().setUri(Uri.EMPTY).setMediaId(r13.uri.toString()).setSubtitleConfigurations(ImmutableList.m213of(r13)).setTag(r19).build();
        Format.Builder r0 = new Format.Builder().setSampleMimeType((String) MoreObjects.firstNonNull(r13.mimeType, MimeTypes.TEXT_UNKNOWN)).setLanguage(r13.language).setSelectionFlags(r13.selectionFlags).setRoleFlags(r13.roleFlags).setLabel(r13.label);
        if (r13.f28id == null) goto L5;
        String r3 = r13.f28id;
    L6:
        this.format = r0.setId(r3).build();
        this.dataSpec = new DataSpec.Builder().setUri(r13.uri).setFlags(1).build();
        this.timeline = new SinglePeriodTimeline(r15, true, false, false, null, this.mediaItem);
        return;
    L5:
        r3 = r12;
        goto L6
    }

    /* synthetic */ SingleSampleMediaSource(String r1, MediaItem.SubtitleConfiguration r2, DataSource.Factory r3, long r4, LoadErrorHandlingPolicy r6, boolean r7, Object r8, C03501 r9) {
        this(r1, r2, r3, r4, r6, r7, r8);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r11, Allocator r12, long r13) {
        return new SingleSampleMediaPeriod(this.dataSpec, this.dataSourceFactory, this.transferListener, this.format, this.durationUs, this.loadErrorHandlingPolicy, createEventDispatcher(r11), this.treatLoadErrorsAsEndOfStream);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaItem getMediaItem() {
        return this.mediaItem;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() {
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void prepareSourceInternal(TransferListener r2) {
        this.transferListener = r2;
        refreshSourceInfo(this.timeline);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r2) {
        ((SingleSampleMediaPeriod) r2).release();
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
    }
}
