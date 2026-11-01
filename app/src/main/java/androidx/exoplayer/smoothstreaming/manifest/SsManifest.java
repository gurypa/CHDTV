package androidx.media3.exoplayer.smoothstreaming.manifest;

import android.net.Uri;
import androidx.media3.common.Format;
import androidx.media3.common.StreamKey;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UriUtil;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.offline.FilterableManifest;
import androidx.media3.extractor.mp4.TrackEncryptionBox;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class SsManifest implements FilterableManifest<SsManifest> {
    public static final int UNSET_LOOKAHEAD = -1;
    public final long durationUs;
    public final long dvrWindowLengthUs;
    public final boolean isLive;
    public final int lookAheadCount;
    public final int majorVersion;
    public final int minorVersion;
    public final ProtectionElement protectionElement;
    public final StreamElement[] streamElements;

    public static class ProtectionElement {
        public final byte[] data;
        public final TrackEncryptionBox[] trackEncryptionBoxes;
        public final UUID uuid;

        public ProtectionElement(UUID r1, byte[] r2, TrackEncryptionBox[] r3) {
            this.uuid = r1;
            this.data = r2;
            this.trackEncryptionBoxes = r3;
        }
    }

    public static class StreamElement {
        private static final String URL_PLACEHOLDER_BITRATE_1 = "{bitrate}";
        private static final String URL_PLACEHOLDER_BITRATE_2 = "{Bitrate}";
        private static final String URL_PLACEHOLDER_START_TIME_1 = "{start time}";
        private static final String URL_PLACEHOLDER_START_TIME_2 = "{start_time}";
        private final String baseUri;
        public final int chunkCount;
        private final List<Long> chunkStartTimes;
        private final long[] chunkStartTimesUs;
        private final String chunkTemplate;
        public final int displayHeight;
        public final int displayWidth;
        public final Format[] formats;
        public final String language;
        private final long lastChunkDurationUs;
        public final int maxHeight;
        public final int maxWidth;
        public final String name;
        public final String subType;
        public final long timescale;
        public final int type;

        public StreamElement(String r21, String r22, int r23, String r24, long r25, String r27, int r28, int r29, int r30, int r31, String r32, Format[] r33, List<Long> r34, long r35) {
            this(r21, r22, r23, r24, r25, r27, r28, r29, r30, r31, r32, r33, r34, Util.scaleLargeTimestamps(r34, 1000000, r25), Util.scaleLargeTimestamp(r35, 1000000, r25));
        }

        private StreamElement(String r17, String r18, int r19, String r20, long r21, String r23, int r24, int r25, int r26, int r27, String r28, Format[] r29, List<Long> r30, long[] r31, long r32) {
            this.baseUri = r17;
            this.chunkTemplate = r18;
            this.type = r19;
            this.subType = r20;
            this.timescale = r21;
            this.name = r23;
            this.maxWidth = r24;
            this.maxHeight = r25;
            this.displayWidth = r26;
            this.displayHeight = r27;
            this.language = r28;
            this.formats = r29;
            this.chunkStartTimes = r30;
            this.chunkStartTimesUs = r31;
            this.lastChunkDurationUs = r32;
            this.chunkCount = r30.size();
        }

        public Uri buildRequestUri(int r5, int r6) {
            boolean r1 = true;
            if (this.formats == null) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkState(r0);
            if (this.chunkStartTimes == null) goto L9;
            boolean r2 = true;
        L10:
            Assertions.checkState(r2);
            if (r6 < this.chunkStartTimes.size()) goto L14;
            r1 = false;
        L14:
            Assertions.checkState(r1);
            String r3 = Integer.toString(this.formats[r5].bitrate);
            String r4 = this.chunkStartTimes.get(r6).toString();
            return UriUtil.resolveToUri(this.baseUri, this.chunkTemplate.replace(URL_PLACEHOLDER_BITRATE_1, r3).replace(URL_PLACEHOLDER_BITRATE_2, r3).replace(URL_PLACEHOLDER_START_TIME_1, r4).replace(URL_PLACEHOLDER_START_TIME_2, r4));
        L9:
            r2 = false;
            goto L10
        L5:
            r0 = false;
            goto L6
        }

        public StreamElement copy(Format[] r22) {
            return new StreamElement(this.baseUri, this.chunkTemplate, this.type, this.subType, this.timescale, this.name, this.maxWidth, this.maxHeight, this.displayWidth, this.displayHeight, this.language, r22, this.chunkStartTimes, this.chunkStartTimesUs, this.lastChunkDurationUs);
        }

        public long getChunkDurationUs(int r6) {
            if (r6 != (this.chunkCount - 1)) goto L6;
            return this.lastChunkDurationUs;
        L6:
            return this.chunkStartTimesUs[r6 + 1] - this.chunkStartTimesUs[r6];
        }

        public int getChunkIndex(long r3) {
            return Util.binarySearchFloor(this.chunkStartTimesUs, r3, true, true);
        }

        public long getStartTimeUs(int r4) {
            return this.chunkStartTimesUs[r4];
        }
    }

    private SsManifest(int r1, int r2, long r3, long r5, int r7, boolean r8, ProtectionElement r9, StreamElement[] r10) {
        this.majorVersion = r1;
        this.minorVersion = r2;
        this.durationUs = r3;
        this.dvrWindowLengthUs = r5;
        this.lookAheadCount = r7;
        this.isLive = r8;
        this.protectionElement = r9;
        this.streamElements = r10;
    }

    public SsManifest(int r21, int r22, long r23, long r25, long r27, int r29, boolean r30, ProtectionElement r31, StreamElement[] r32) {
        if (r25 != 0) goto L5;
        long r7 = -9223372036854775807L;
    L7:
        if (r27 != 0) goto L9;
        long r14 = -9223372036854775807L;
    L10:
        this(r21, r22, r7, r14, r29, r30, r31, r32);
        return;
    L9:
        r14 = Util.scaleLargeTimestamp(r27, 1000000, r23);
        goto L10
    L5:
        r7 = Util.scaleLargeTimestamp(r25, 1000000, r23);
        goto L7
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.media3.exoplayer.offline.FilterableManifest
    public final SsManifest copy(List<StreamKey> r19) {
        ArrayList r1 = new ArrayList(r19);
        Collections.sort(r1);
        StreamElement r3 = null;
        ArrayList r4 = new ArrayList();
        ArrayList r5 = new ArrayList();
        int r6 = 0;
    L4:
        if (r6 >= r1.size()) goto L10;
        StreamKey r7 = (StreamKey) r1.get(r6);
        StreamElement r9 = this.streamElements[r7.groupIndex];
        if (r9 == r3) goto L9;
        if (r3 == null) goto L9;
        r4.add(r3.copy((Format[]) r5.toArray(new Format[0])));
        r5.clear();
    L9:
        r3 = r9;
        r5.add(r9.formats[r7.streamIndex]);
        r6 = r6 + 1;
        goto L4
    L10:
        if (r3 == null) goto L12;
        r4.add(r3.copy((Format[]) r5.toArray(new Format[0])));
    L12:
        StreamElement[] r17 = (StreamElement[]) r4.toArray(new StreamElement[0]);
        return new SsManifest(this.majorVersion, this.minorVersion, this.durationUs, this.dvrWindowLengthUs, this.lookAheadCount, this.isLive, this.protectionElement, r17);
    }

    @Override // androidx.media3.exoplayer.offline.FilterableManifest
    public /* bridge */ /* synthetic */ SsManifest copy(List r1) {
        return copy(r1);
    }
}
