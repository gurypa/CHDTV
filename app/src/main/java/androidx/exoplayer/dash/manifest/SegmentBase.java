package androidx.media3.exoplayer.dash.manifest;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Util;
import com.google.common.math.BigIntegerMath;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class SegmentBase {
    final RangedUri initialization;
    final long presentationTimeOffset;
    final long timescale;

    public static abstract class MultiSegmentBase extends SegmentBase {
        final long availabilityTimeOffsetUs;
        final long duration;
        private final long periodStartUnixTimeUs;
        final List<SegmentTimelineElement> segmentTimeline;
        final long startNumber;
        private final long timeShiftBufferDepthUs;

        public MultiSegmentBase(RangedUri r12, long r13, long r15, long r17, long r19, List<SegmentTimelineElement> r21, long r22, long r24, long r26) {
            super(r12, r13, r15);
            this.startNumber = r17;
            this.duration = r19;
            this.segmentTimeline = r21;
            this.availabilityTimeOffsetUs = r22;
            this.timeShiftBufferDepthUs = r24;
            this.periodStartUnixTimeUs = r26;
        }

        public long getAvailableSegmentCount(long r15, long r17) {
            long r0 = getSegmentCount(r15);
            if (r0 == (-1)) goto L6;
            return r0;
        L6:
            return (int) (getSegmentNum(this.availabilityTimeOffsetUs + (r17 - this.periodStartUnixTimeUs), r15) - getFirstAvailableSegmentNum(r15, r17));
        }

        public long getFirstAvailableSegmentNum(long r11, long r13) {
            if (getSegmentCount(r11) != (-1)) goto L10;
            if (this.timeShiftBufferDepthUs == C0180C.TIME_UNSET) goto L10;
            long r6 = getSegmentNum((r13 - this.periodStartUnixTimeUs) - this.timeShiftBufferDepthUs, r11);
            return Math.max(getFirstSegmentNum(), r6);
        L10:
            return getFirstSegmentNum();
        }

        public long getFirstSegmentNum() {
            return this.startNumber;
        }

        public long getNextSegmentAvailableTimeUs(long r7, long r9) {
            if (this.segmentTimeline == null) goto L6;
            return C0180C.TIME_UNSET;
        L6:
            long r0 = getFirstAvailableSegmentNum(r7, r9) + getAvailableSegmentCount(r7, r9);
            return (getSegmentTimeUs(r0) + getSegmentDurationUs(r0, r7)) - this.availabilityTimeOffsetUs;
        }

        public abstract long getSegmentCount(long r1);

        public final long getSegmentDurationUs(long r10, long r12) {
            if (this.segmentTimeline != null) goto L5;
            long r3 = getSegmentCount(r12);
            if (r3 == (-1)) goto L12;
            if (r10 != ((getFirstSegmentNum() + r3) - 1)) goto L12;
            return r12 - getSegmentTimeUs(r10);
        L12:
            return (this.duration * 1000000) / this.timescale;
        L5:
            return (1000000 * this.segmentTimeline.get((int) (r10 - this.startNumber)).duration) / this.timescale;
        }

        public long getSegmentNum(long r19, long r21) {
            long r1 = getFirstSegmentNum();
            long r5 = getSegmentCount(r21);
            if (r5 != 0) goto L6;
            return r1;
        L6:
            if (this.segmentTimeline != null) goto L15;
            long r10 = (this.duration * 1000000) / this.timescale;
            long r12 = this.startNumber + (r19 / r10);
            if (r12 >= r1) goto L11;
            return r1;
        L11:
            if (r5 != (-1)) goto L14;
            return r12;
        L14:
            return Math.min(r12, (r1 + r5) - 1);
        L15:
            long r11 = r1;
            long r13 = (r1 + r5) - 1;
        L17:
            if (r11 > r13) goto L27;
            long r14 = ((r13 - r11) / 2) + r11;
            long r16 = getSegmentTimeUs(r14);
            if (r16 < r19) goto L20;
            if (r16 <= r19) goto L25;
            r13 = r14 - 1;
            goto L17
        L25:
            return r14;
        L20:
            r11 = r14 + 1;
            goto L17
        L27:
            if (r11 != r1) goto L30;
            return r11;
        L30:
            return r13;
        }

        public final long getSegmentTimeUs(long r9) {
            if (this.segmentTimeline == null) goto L5;
            long r2 = this.segmentTimeline.get((int) (r9 - this.startNumber)).startTime - this.presentationTimeOffset;
        L7:
            return Util.scaleLargeTimestamp(r2, 1000000, this.timescale);
        L5:
            r2 = (r9 - this.startNumber) * this.duration;
            goto L7
        }

        public abstract RangedUri getSegmentUrl(Representation r1, long r2);

        public boolean isExplicit() {
            if (this.segmentTimeline == null) goto L5;
            return true;
        L5:
            return false;
        }
    }

    public static final class SegmentList extends MultiSegmentBase {
        final List<RangedUri> mediaSegments;

        public SegmentList(RangedUri r18, long r19, long r21, long r23, long r25, List<SegmentTimelineElement> r27, long r28, List<RangedUri> r30, long r31, long r33) {
            super(r18, r19, r21, r23, r25, r27, r28, r31, r33);
            this.mediaSegments = r30;
        }

        @Override // androidx.media3.exoplayer.dash.manifest.SegmentBase.MultiSegmentBase
        public long getSegmentCount(long r3) {
            return this.mediaSegments.size();
        }

        @Override // androidx.media3.exoplayer.dash.manifest.SegmentBase.MultiSegmentBase
        public RangedUri getSegmentUrl(Representation r4, long r5) {
            return this.mediaSegments.get((int) (r5 - this.startNumber));
        }

        @Override // androidx.media3.exoplayer.dash.manifest.SegmentBase.MultiSegmentBase
        public boolean isExplicit() {
            return true;
        }
    }

    public static final class SegmentTemplate extends MultiSegmentBase {
        final long endNumber;
        final UrlTemplate initializationTemplate;
        final UrlTemplate mediaTemplate;

        public SegmentTemplate(RangedUri r18, long r19, long r21, long r23, long r25, long r27, List<SegmentTimelineElement> r29, long r30, UrlTemplate r32, UrlTemplate r33, long r34, long r36) {
            super(r18, r19, r21, r23, r27, r29, r30, r34, r36);
            this.initializationTemplate = r32;
            this.mediaTemplate = r33;
            this.endNumber = r25;
        }

        @Override // androidx.media3.exoplayer.dash.manifest.SegmentBase
        public RangedUri getInitialization(Representation r15) {
            if (this.initializationTemplate == null) goto L7;
            return new RangedUri(this.initializationTemplate.buildUri(r15.format.f26id, 0, r15.format.bitrate, 0), 0, -1);
        L7:
            return super.getInitialization(r15);
        }

        @Override // androidx.media3.exoplayer.dash.manifest.SegmentBase.MultiSegmentBase
        public long getSegmentCount(long r5) {
            if (this.segmentTimeline == null) goto L7;
            return this.segmentTimeline.size();
        L7:
            if (this.endNumber == (-1)) goto L11;
            return (this.endNumber - this.startNumber) + 1;
        L11:
            if (r5 != C0180C.TIME_UNSET) goto L13;
            return -1;
        L13:
            return BigIntegerMath.divide(BigInteger.valueOf(r5).multiply(BigInteger.valueOf(this.timescale)), BigInteger.valueOf(this.duration).multiply(BigInteger.valueOf(1000000)), RoundingMode.CEILING).longValue();
        }

        @Override // androidx.media3.exoplayer.dash.manifest.SegmentBase.MultiSegmentBase
        public RangedUri getSegmentUrl(Representation r18, long r19) {
            if (this.segmentTimeline == null) goto L5;
            long r9 = this.segmentTimeline.get((int) (r19 - this.startNumber)).startTime;
        L7:
            return new RangedUri(this.mediaTemplate.buildUri(r18.format.f26id, r19, r18.format.bitrate, r9), 0, -1);
        L5:
            r9 = (r19 - this.startNumber) * this.duration;
            goto L7
        }
    }

    public static final class SegmentTimelineElement {
        final long duration;
        final long startTime;

        public SegmentTimelineElement(long r1, long r3) {
            this.startTime = r1;
            this.duration = r3;
        }

        public boolean equals(Object r8) {
            if (this != r8) goto L6;
            return true;
        L6:
            if (r8 != null) goto L8;
        L17:
            return false;
        L8:
            if (getClass() != r8.getClass()) goto L17;
            SegmentTimelineElement r2 = (SegmentTimelineElement) r8;
            if (this.startTime != r2.startTime) goto L16;
            if (this.duration != r2.duration) goto L16;
            return true;
        L16:
            return false;
        }

        public int hashCode() {
            return (((int) this.startTime) * 31) + ((int) this.duration);
        }
    }

    public static class SingleSegmentBase extends SegmentBase {
        final long indexLength;
        final long indexStart;

        public SingleSegmentBase() {
            this(null, 1, 0, 0, 0);
        }

        public SingleSegmentBase(RangedUri r3, long r4, long r6, long r8, long r10) {
            super(r3, r4, r6);
            this.indexStart = r8;
            this.indexLength = r10;
        }

        public RangedUri getIndex() {
            if (this.indexLength > 0) goto L6;
            return null;
        L6:
            return new RangedUri(null, this.indexStart, this.indexLength);
        }
    }

    public SegmentBase(RangedUri r1, long r2, long r4) {
        this.initialization = r1;
        this.timescale = r2;
        this.presentationTimeOffset = r4;
    }

    public RangedUri getInitialization(Representation r2) {
        return this.initialization;
    }

    public long getPresentationTimeOffsetUs() {
        return Util.scaleLargeTimestamp(this.presentationTimeOffset, 1000000, this.timescale);
    }
}
