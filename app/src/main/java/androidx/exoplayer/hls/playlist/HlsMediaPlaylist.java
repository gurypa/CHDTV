package androidx.media3.exoplayer.hls.playlist;

import android.net.Uri;
import androidx.media3.common.C0180C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.StreamKey;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class HlsMediaPlaylist extends HlsPlaylist {
    public static final int PLAYLIST_TYPE_EVENT = 2;
    public static final int PLAYLIST_TYPE_UNKNOWN = 0;
    public static final int PLAYLIST_TYPE_VOD = 1;
    public final int discontinuitySequence;
    public final long durationUs;
    public final boolean hasDiscontinuitySequence;
    public final boolean hasEndTag;
    public final boolean hasPositiveStartOffset;
    public final boolean hasProgramDateTime;
    public final long mediaSequence;
    public final long partTargetDurationUs;
    public final int playlistType;
    public final boolean preciseStart;
    public final DrmInitData protectionSchemes;
    public final Map<Uri, RenditionReport> renditionReports;
    public final List<Segment> segments;
    public final ServerControl serverControl;
    public final long startOffsetUs;
    public final long startTimeUs;
    public final long targetDurationUs;
    public final List<Part> trailingParts;
    public final int version;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.hls.playlist.HlsMediaPlaylist$1 */
    static /* synthetic */ class C03001 {
    }

    public static final class Part extends SegmentBase {
        public final boolean isIndependent;
        public final boolean isPreload;

        public Part(String r18, Segment r19, long r20, int r22, long r23, DrmInitData r25, String r26, String r27, long r28, long r30, boolean r32, boolean r33, boolean r34) {
            super(r18, r19, r20, r22, r23, r25, r26, r27, r28, r30, r32, null);
            this.isIndependent = r33;
            this.isPreload = r34;
        }

        public Part copyWith(long r20, int r22) {
            return new Part(this.url, this.initializationSegment, this.durationUs, r22, r20, this.drmInitData, this.fullSegmentEncryptionKeyUri, this.encryptionIV, this.byteRangeOffset, this.byteRangeLength, this.hasGapTag, this.isIndependent, this.isPreload);
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlaylistType {
    }

    public static final class RenditionReport {
        public final long lastMediaSequence;
        public final int lastPartIndex;
        public final Uri playlistUri;

        public RenditionReport(Uri r1, long r2, int r4) {
            this.playlistUri = r1;
            this.lastMediaSequence = r2;
            this.lastPartIndex = r4;
        }
    }

    public static final class Segment extends SegmentBase {
        public final List<Part> parts;
        public final String title;

        public Segment(String r19, long r20, long r22, String r24, String r25) {
            this(r19, null, "", 0, -1, C0180C.TIME_UNSET, null, r24, r25, r20, r22, false, ImmutableList.m212of());
        }

        public Segment(String r18, Segment r19, String r20, long r21, int r23, long r24, DrmInitData r26, String r27, String r28, long r29, long r31, boolean r33, List<Part> r34) {
            super(r18, r19, r21, r23, r24, r26, r27, r28, r29, r31, r33, null);
            this.title = r20;
            this.parts = ImmutableList.copyOf(r34);
        }

        public Segment copyWith(long r23, int r25) {
            ArrayList r1 = new ArrayList();
            long r2 = r23;
            int r4 = 0;
        L4:
            if (r4 >= this.parts.size()) goto L7;
            Part r5 = this.parts.get(r4);
            r1.add(r5.copyWith(r2, r25));
            r2 = r2 + r5.durationUs;
            r4 = r4 + 1;
            goto L4
        L7:
            return new Segment(this.url, this.initializationSegment, this.title, this.durationUs, r25, r23, this.drmInitData, this.fullSegmentEncryptionKeyUri, this.encryptionIV, this.byteRangeOffset, this.byteRangeLength, this.hasGapTag, r1);
        }
    }

    public static class SegmentBase implements Comparable<Long> {
        public final long byteRangeLength;
        public final long byteRangeOffset;
        public final DrmInitData drmInitData;
        public final long durationUs;
        public final String encryptionIV;
        public final String fullSegmentEncryptionKeyUri;
        public final boolean hasGapTag;
        public final Segment initializationSegment;
        public final int relativeDiscontinuitySequence;
        public final long relativeStartTimeUs;
        public final String url;

        private SegmentBase(String r17, Segment r18, long r19, int r21, long r22, DrmInitData r24, String r25, String r26, long r27, long r29, boolean r31) {
            this.url = r17;
            this.initializationSegment = r18;
            this.durationUs = r19;
            this.relativeDiscontinuitySequence = r21;
            this.relativeStartTimeUs = r22;
            this.drmInitData = r24;
            this.fullSegmentEncryptionKeyUri = r25;
            this.encryptionIV = r26;
            this.byteRangeOffset = r27;
            this.byteRangeLength = r29;
            this.hasGapTag = r31;
        }

        /* synthetic */ SegmentBase(String r1, Segment r2, long r3, int r5, long r6, DrmInitData r8, String r9, String r10, long r11, long r13, boolean r15, C03001 r16) {
            this(r1, r2, r3, r5, r6, r8, r9, r10, r11, r13, r15);
        }

        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(Long r5) {
            if (this.relativeStartTimeUs <= r5.longValue()) goto L6;
            return 1;
        L6:
            if (this.relativeStartTimeUs >= r5.longValue()) goto L8;
            return -1;
        L8:
            return 0;
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Long r1) {
            return compareTo2(r1);
        }
    }

    public static final class ServerControl {
        public final boolean canBlockReload;
        public final boolean canSkipDateRanges;
        public final long holdBackUs;
        public final long partHoldBackUs;
        public final long skipUntilUs;

        public ServerControl(long r1, boolean r3, long r4, long r6, boolean r8) {
            this.skipUntilUs = r1;
            this.canSkipDateRanges = r3;
            this.holdBackUs = r4;
            this.partHoldBackUs = r6;
            this.canBlockReload = r8;
        }
    }

    public HlsMediaPlaylist(int r21, String r22, List<String> r23, long r24, boolean r26, long r27, boolean r29, int r30, long r31, int r33, long r34, long r36, boolean r38, boolean r39, boolean r40, DrmInitData r41, List<Segment> r42, List<Part> r43, ServerControl r44, Map<Uri, RenditionReport> r45) {
        super(r22, r23, r38);
        this.playlistType = r21;
        this.startTimeUs = r27;
        this.preciseStart = r26;
        this.hasDiscontinuitySequence = r29;
        this.discontinuitySequence = r30;
        this.mediaSequence = r31;
        this.version = r33;
        this.targetDurationUs = r34;
        this.partTargetDurationUs = r36;
        this.hasEndTag = r39;
        this.hasProgramDateTime = r40;
        this.protectionSchemes = r41;
        this.segments = ImmutableList.copyOf(r42);
        this.trailingParts = ImmutableList.copyOf(r43);
        this.renditionReports = ImmutableMap.copyOf(r45);
        if (r43.isEmpty() == true) goto L6;
        Part r3 = (Part) Iterables.getLast(r43);
        this.durationUs = r3.relativeStartTimeUs + r3.durationUs;
        long r4 = 0;
    L10:
        if (r24 != C0180C.TIME_UNSET) goto L13;
        long r1 = r4;
        long r5 = -9223372036854775807L;
    L16:
        this.startOffsetUs = r5;
        if (r24 < r1) goto L19;
        boolean r2 = true;
    L20:
        this.hasPositiveStartOffset = r2;
        this.serverControl = r44;
        return;
    L19:
        r2 = false;
        goto L20
    L13:
        if (r24 < r4) goto L15;
        r5 = Math.min(this.durationUs, r24);
        r1 = 0;
        goto L16
    L15:
        r1 = 0;
        r5 = Math.max(0, this.durationUs + r24);
        goto L16
    L6:
        if (r42.isEmpty() == true) goto L8;
        Segment r6 = (Segment) Iterables.getLast(r42);
        this.durationUs = r6.relativeStartTimeUs + r6.durationUs;
        r4 = 0;
        goto L10
    L8:
        r4 = 0;
        this.durationUs = 0;
        goto L10
    }

    @Override // androidx.media3.exoplayer.offline.FilterableManifest
    public HlsPlaylist copy(List<StreamKey> r1) {
        return this;
    }

    @Override // androidx.media3.exoplayer.offline.FilterableManifest
    /* JADX INFO: renamed from: copy, reason: avoid collision after fix types in other method */
    public /* bridge */ /* synthetic */ HlsPlaylist copy2(List r1) {
        return copy(r1);
    }

    public HlsMediaPlaylist copyWith(long r28, int r30) {
        return new HlsMediaPlaylist(this.playlistType, this.baseUri, this.tags, this.startOffsetUs, this.preciseStart, r28, true, r30, this.mediaSequence, this.version, this.targetDurationUs, this.partTargetDurationUs, this.hasIndependentSegments, this.hasEndTag, this.hasProgramDateTime, this.protectionSchemes, this.segments, this.trailingParts, this.serverControl, this.renditionReports);
    }

    public HlsMediaPlaylist copyWithEndTag() {
        if (this.hasEndTag == false) goto L6;
        return this;
    L6:
        return new HlsMediaPlaylist(this.playlistType, this.baseUri, this.tags, this.startOffsetUs, this.preciseStart, this.startTimeUs, this.hasDiscontinuitySequence, this.discontinuitySequence, this.mediaSequence, this.version, this.targetDurationUs, this.partTargetDurationUs, this.hasIndependentSegments, true, this.hasProgramDateTime, this.protectionSchemes, this.segments, this.trailingParts, this.serverControl, this.renditionReports);
    }

    public long getEndTimeUs() {
        return this.startTimeUs + this.durationUs;
    }

    public boolean isNewerThan(HlsMediaPlaylist r7) {
        if (r7 != null) goto L5;
    L26:
        return true;
    L5:
        if (this.mediaSequence > r7.mediaSequence) goto L26;
        if (this.mediaSequence >= r7.mediaSequence) goto L10;
        return false;
    L10:
        int r1 = this.segments.size() - r7.segments.size();
        if (r1 == 0) goto L16;
        if (r1 <= 0) goto L15;
        return true;
    L15:
        return false;
    L16:
        int r3 = this.trailingParts.size();
        int r4 = r7.trailingParts.size();
        if (r3 > r4) goto L28;
        if (r3 != r4) goto L25;
        if (this.hasEndTag == false) goto L25;
        if (r7.hasEndTag == true) goto L25;
        return true;
    L25:
        return false;
    L28:
        return true;
    }
}
