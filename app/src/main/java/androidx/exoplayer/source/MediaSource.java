package androidx.media3.exoplayer.source;

import android.os.Handler;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.extractor.text.SubtitleParser;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public interface MediaSource {

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.MediaSource$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static boolean $default$canUpdateMediaItem(MediaSource r1, MediaItem r2) {
            return false;
        }

        public static Timeline $default$getInitialTimeline(MediaSource r1) {
            return null;
        }

        public static boolean $default$isSingleWindow(MediaSource r1) {
            return true;
        }

        @Deprecated
        public static void $default$prepareSource(MediaSource r1, MediaSourceCaller r2, TransferListener r3) {
            r1.prepareSource(r2, r3, PlayerId.UNSET);
        }

        public static void $default$updateMediaItem(MediaSource r0, MediaItem r1) {
        }
    }

    public interface Factory {
        public static final Factory UNSUPPORTED = null;

        /* JADX INFO: renamed from: androidx.media3.exoplayer.source.MediaSource$Factory$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            @Deprecated
            public static Factory $default$experimentalParseSubtitlesDuringExtraction(Factory r0, boolean r1) {
                return r0;
            }

            public static Factory $default$setCmcdConfigurationFactory(Factory r0, CmcdConfiguration.Factory r1) {
                return r0;
            }

            public static Factory $default$setSubtitleParserFactory(Factory r0, SubtitleParser.Factory r1) {
                return r0;
            }
        }

        static {
            UNSUPPORTED = MediaSourceFactory.UNSUPPORTED;
        }

        MediaSource createMediaSource(MediaItem r1);

        @Deprecated
        Factory experimentalParseSubtitlesDuringExtraction(boolean r1);

        int[] getSupportedTypes();

        Factory setCmcdConfigurationFactory(CmcdConfiguration.Factory r1);

        Factory setDrmSessionManagerProvider(DrmSessionManagerProvider r1);

        Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r1);

        Factory setSubtitleParserFactory(SubtitleParser.Factory r1);
    }

    public static final class MediaPeriodId {
        public final int adGroupIndex;
        public final int adIndexInAdGroup;
        public final int nextAdGroupIndex;
        public final Object periodUid;
        public final long windowSequenceNumber;

        public MediaPeriodId(Object r3) {
            this(r3, -1);
        }

        public MediaPeriodId(Object r8, int r9, int r10, long r11) {
            this(r8, r9, r10, r11, -1);
        }

        private MediaPeriodId(Object r1, int r2, int r3, long r4, int r6) {
            this.periodUid = r1;
            this.adGroupIndex = r2;
            this.adIndexInAdGroup = r3;
            this.windowSequenceNumber = r4;
            this.nextAdGroupIndex = r6;
        }

        public MediaPeriodId(Object r8, long r9) {
            this(r8, -1, -1, r9, -1);
        }

        public MediaPeriodId(Object r8, long r9, int r11) {
            this(r8, -1, -1, r9, r11);
        }

        public MediaPeriodId copyWithPeriodUid(Object r9) {
            if (this.periodUid.equals(r9) == false) goto L6;
            return this;
        L6:
            return new MediaPeriodId(r9, this.adGroupIndex, this.adIndexInAdGroup, this.windowSequenceNumber, this.nextAdGroupIndex);
        }

        public MediaPeriodId copyWithWindowSequenceNumber(long r9) {
            if (this.windowSequenceNumber != r9) goto L6;
            return this;
        L6:
            return new MediaPeriodId(this.periodUid, this.adGroupIndex, this.adIndexInAdGroup, r9, this.nextAdGroupIndex);
        }

        public boolean equals(Object r8) {
            if (this != r8) goto L6;
            return true;
        L6:
            if ((r8 instanceof MediaPeriodId) == true) goto L8;
            return false;
        L8:
            MediaPeriodId r1 = (MediaPeriodId) r8;
            if (this.periodUid.equals(r1.periodUid) == false) goto L20;
            if (this.adGroupIndex != r1.adGroupIndex) goto L20;
            if (this.adIndexInAdGroup != r1.adIndexInAdGroup) goto L20;
            if (this.windowSequenceNumber != r1.windowSequenceNumber) goto L20;
            if (this.nextAdGroupIndex != r1.nextAdGroupIndex) goto L20;
            return true;
        L20:
            return false;
        }

        public int hashCode() {
            return (((((((((17 * 31) + this.periodUid.hashCode()) * 31) + this.adGroupIndex) * 31) + this.adIndexInAdGroup) * 31) + ((int) this.windowSequenceNumber)) * 31) + this.nextAdGroupIndex;
        }

        public boolean isAd() {
            if (this.adGroupIndex == (-1)) goto L5;
            return true;
        L5:
            return false;
        }
    }

    public interface MediaSourceCaller {
        void onSourceInfoRefreshed(MediaSource r1, Timeline r2);
    }

    void addDrmEventListener(Handler r1, DrmSessionEventListener r2);

    void addEventListener(Handler r1, MediaSourceEventListener r2);

    boolean canUpdateMediaItem(MediaItem r1);

    MediaPeriod createPeriod(MediaPeriodId r1, Allocator r2, long r3);

    void disable(MediaSourceCaller r1);

    void enable(MediaSourceCaller r1);

    Timeline getInitialTimeline();

    MediaItem getMediaItem();

    boolean isSingleWindow();

    void maybeThrowSourceInfoRefreshError() throws IOException;

    @Deprecated
    void prepareSource(MediaSourceCaller r1, TransferListener r2);

    void prepareSource(MediaSourceCaller r1, TransferListener r2, PlayerId r3);

    void releasePeriod(MediaPeriod r1);

    void releaseSource(MediaSourceCaller r1);

    void removeDrmEventListener(DrmSessionEventListener r1);

    void removeEventListener(MediaSourceEventListener r1);

    void updateMediaItem(MediaItem r1);
}
