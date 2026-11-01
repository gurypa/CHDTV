package androidx.media3.extractor;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public abstract class BinarySearchSeeker {
    private static final long MAX_SKIP_BYTES = 262144;
    private final int minimumSearchRange;
    protected final BinarySearchSeekMap seekMap;
    protected SeekOperationParams seekOperationParams;
    protected final TimestampSeeker timestampSeeker;

    public static class BinarySearchSeekMap implements SeekMap {
        private final long approxBytesPerFrame;
        private final long ceilingBytePosition;
        private final long ceilingTimePosition;
        private final long durationUs;
        private final long floorBytePosition;
        private final long floorTimePosition;
        private final SeekTimestampConverter seekTimestampConverter;

        public BinarySearchSeekMap(SeekTimestampConverter r1, long r2, long r4, long r6, long r8, long r10, long r12) {
            this.seekTimestampConverter = r1;
            this.durationUs = r2;
            this.floorTimePosition = r4;
            this.ceilingTimePosition = r6;
            this.floorBytePosition = r8;
            this.ceilingBytePosition = r10;
            this.approxBytesPerFrame = r12;
        }

        static /* synthetic */ long access$1000(BinarySearchSeekMap r2) {
            return r2.floorTimePosition;
        }

        static /* synthetic */ long access$1100(BinarySearchSeekMap r2) {
            return r2.ceilingTimePosition;
        }

        static /* synthetic */ long access$1200(BinarySearchSeekMap r2) {
            return r2.floorBytePosition;
        }

        static /* synthetic */ long access$1300(BinarySearchSeekMap r2) {
            return r2.ceilingBytePosition;
        }

        static /* synthetic */ long access$1400(BinarySearchSeekMap r2) {
            return r2.approxBytesPerFrame;
        }

        @Override // androidx.media3.extractor.SeekMap
        public long getDurationUs() {
            return this.durationUs;
        }

        @Override // androidx.media3.extractor.SeekMap
        public SeekMap.SeekPoints getSeekPoints(long r14) {
            return new SeekMap.SeekPoints(new SeekPoint(r14, SeekOperationParams.calculateNextSearchBytePosition(this.seekTimestampConverter.timeUsToTargetTime(r14), this.floorTimePosition, this.ceilingTimePosition, this.floorBytePosition, this.ceilingBytePosition, this.approxBytesPerFrame)));
        }

        @Override // androidx.media3.extractor.SeekMap
        public boolean isSeekable() {
            return true;
        }

        public long timeUsToTargetTime(long r3) {
            return this.seekTimestampConverter.timeUsToTargetTime(r3);
        }
    }

    public static final class DefaultSeekTimestampConverter implements SeekTimestampConverter {
        public DefaultSeekTimestampConverter() {
        }

        @Override // androidx.media3.extractor.BinarySearchSeeker.SeekTimestampConverter
        public long timeUsToTargetTime(long r1) {
            return r1;
        }
    }

    protected static class SeekOperationParams {
        private final long approxBytesPerFrame;
        private long ceilingBytePosition;
        private long ceilingTimePosition;
        private long floorBytePosition;
        private long floorTimePosition;
        private long nextSearchBytePosition;
        private final long seekTimeUs;
        private final long targetTimePosition;

        protected SeekOperationParams(long r15, long r17, long r19, long r21, long r23, long r25, long r27) {
            this.seekTimeUs = r15;
            this.targetTimePosition = r17;
            this.floorTimePosition = r19;
            this.ceilingTimePosition = r21;
            this.floorBytePosition = r23;
            this.ceilingBytePosition = r25;
            this.approxBytesPerFrame = r27;
            this.nextSearchBytePosition = calculateNextSearchBytePosition(r17, r19, r21, r23, r25, r27);
        }

        static /* synthetic */ long access$000(SeekOperationParams r2) {
            return r2.getSeekTimeUs();
        }

        static /* synthetic */ long access$100(SeekOperationParams r2) {
            return r2.getFloorBytePosition();
        }

        static /* synthetic */ long access$200(SeekOperationParams r2) {
            return r2.getCeilingBytePosition();
        }

        static /* synthetic */ long access$300(SeekOperationParams r2) {
            return r2.getNextSearchBytePosition();
        }

        static /* synthetic */ long access$400(SeekOperationParams r2) {
            return r2.getTargetTimePosition();
        }

        static /* synthetic */ void access$800(SeekOperationParams r0, long r1, long r3) {
            r0.updateSeekCeiling(r1, r3);
        }

        static /* synthetic */ void access$900(SeekOperationParams r0, long r1, long r3) {
            r0.updateSeekFloor(r1, r3);
        }

        protected static long calculateNextSearchBytePosition(long r17, long r19, long r21, long r23, long r25, long r27) {
            if ((r23 + 1) < r25) goto L5;
        L9:
            return r23;
        L5:
            if ((r19 + 1) >= r21) goto L9;
            long r5 = (long) ((r17 - r19) * ((r25 - r23) / (r21 - r19)));
            return Util.constrainValue(((r23 + r5) - r27) - (r5 / 20), r23, r25 - 1);
        }

        private long getCeilingBytePosition() {
            return this.ceilingBytePosition;
        }

        private long getFloorBytePosition() {
            return this.floorBytePosition;
        }

        private long getNextSearchBytePosition() {
            return this.nextSearchBytePosition;
        }

        private long getSeekTimeUs() {
            return this.seekTimeUs;
        }

        private long getTargetTimePosition() {
            return this.targetTimePosition;
        }

        private void updateNextSearchBytePosition() {
            this.nextSearchBytePosition = calculateNextSearchBytePosition(this.targetTimePosition, this.floorTimePosition, this.ceilingTimePosition, this.floorBytePosition, this.ceilingBytePosition, this.approxBytesPerFrame);
        }

        private void updateSeekCeiling(long r1, long r3) {
            this.ceilingTimePosition = r1;
            this.ceilingBytePosition = r3;
            updateNextSearchBytePosition();
        }

        private void updateSeekFloor(long r1, long r3) {
            this.floorTimePosition = r1;
            this.floorBytePosition = r3;
            updateNextSearchBytePosition();
        }
    }

    protected interface SeekTimestampConverter {
        long timeUsToTargetTime(long r1);
    }

    public static final class TimestampSearchResult {
        public static final TimestampSearchResult NO_TIMESTAMP_IN_RANGE_RESULT = null;
        public static final int TYPE_NO_TIMESTAMP = -3;
        public static final int TYPE_POSITION_OVERESTIMATED = -1;
        public static final int TYPE_POSITION_UNDERESTIMATED = -2;
        public static final int TYPE_TARGET_TIMESTAMP_FOUND = 0;
        private final long bytePositionToUpdate;
        private final long timestampToUpdate;
        private final int type;

        static {
            NO_TIMESTAMP_IN_RANGE_RESULT = new TimestampSearchResult(-3, C0180C.TIME_UNSET, -1);
        }

        private TimestampSearchResult(int r1, long r2, long r4) {
            this.type = r1;
            this.timestampToUpdate = r2;
            this.bytePositionToUpdate = r4;
        }

        static /* synthetic */ int access$500(TimestampSearchResult r1) {
            return r1.type;
        }

        static /* synthetic */ long access$600(TimestampSearchResult r2) {
            return r2.timestampToUpdate;
        }

        static /* synthetic */ long access$700(TimestampSearchResult r2) {
            return r2.bytePositionToUpdate;
        }

        public static TimestampSearchResult overestimatedResult(long r6, long r8) {
            return new TimestampSearchResult(-1, r6, r8);
        }

        public static TimestampSearchResult targetFoundResult(long r6) {
            return new TimestampSearchResult(0, C0180C.TIME_UNSET, r6);
        }

        public static TimestampSearchResult underestimatedResult(long r6, long r8) {
            return new TimestampSearchResult(-2, r6, r8);
        }
    }

    protected interface TimestampSeeker {

        /* JADX INFO: renamed from: androidx.media3.extractor.BinarySearchSeeker$TimestampSeeker$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$onSeekFinished(TimestampSeeker r0) {
            }
        }

        void onSeekFinished();

        TimestampSearchResult searchForTimestamp(ExtractorInput r1, long r2) throws IOException;
    }

    protected BinarySearchSeeker(SeekTimestampConverter r18, TimestampSeeker r19, long r20, long r22, long r24, long r26, long r28, long r30, int r32) {
        this.timestampSeeker = r19;
        this.minimumSearchRange = r32;
        this.seekMap = new BinarySearchSeekMap(r18, r20, r22, r24, r26, r28, r30);
    }

    protected SeekOperationParams createSeekParamsForTargetTimeUs(long r18) {
        return new SeekOperationParams(r18, this.seekMap.timeUsToTargetTime(r18), BinarySearchSeekMap.access$1000(this.seekMap), BinarySearchSeekMap.access$1100(this.seekMap), BinarySearchSeekMap.access$1200(this.seekMap), BinarySearchSeekMap.access$1300(this.seekMap), BinarySearchSeekMap.access$1400(this.seekMap));
    }

    public final SeekMap getSeekMap() {
        return this.seekMap;
    }

    public int handlePendingSeek(ExtractorInput r13, PositionHolder r14) throws IOException {
    L2:
        SeekOperationParams r0 = (SeekOperationParams) Assertions.checkStateNotNull(this.seekOperationParams);
        long r1 = SeekOperationParams.access$100(r0);
        long r3 = SeekOperationParams.access$200(r0);
        long r5 = SeekOperationParams.access$300(r0);
        if ((r3 - r1) <= this.minimumSearchRange) goto L4;
        if (skipInputUntilPosition(r13, r5) == false) goto L9;
        r13.resetPeekPosition();
        TimestampSearchResult r7 = this.timestampSeeker.searchForTimestamp(r13, SeekOperationParams.access$400(r0));
        switch(TimestampSearchResult.access$500(r7)) {
            case -3: goto L19;
            case -2: goto L17;
            case -1: goto L16;
            case 0: goto L14;
            default: goto L13;
        };
    L13:
        throw new IllegalStateException("Invalid case");
    L14:
        skipInputUntilPosition(r13, TimestampSearchResult.access$700(r7));
        markSeekOperationFinished(true, TimestampSearchResult.access$700(r7));
        return seekToPosition(r13, TimestampSearchResult.access$700(r7), r14);
    L17:
        SeekOperationParams.access$900(r0, TimestampSearchResult.access$600(r7), TimestampSearchResult.access$700(r7));
        goto L2
    L19:
        markSeekOperationFinished(false, r5);
        return seekToPosition(r13, r5, r14);
    L16:
        SeekOperationParams.access$800(r0, TimestampSearchResult.access$600(r7), TimestampSearchResult.access$700(r7));
        goto L2
    L9:
        return seekToPosition(r13, r5, r14);
    L4:
        markSeekOperationFinished(false, r1);
        return seekToPosition(r13, r1, r14);
    }

    public final boolean isSeeking() {
        if (this.seekOperationParams == null) goto L5;
        return true;
    L5:
        return false;
    }

    protected final void markSeekOperationFinished(boolean r2, long r3) {
        this.seekOperationParams = null;
        this.timestampSeeker.onSeekFinished();
        onSeekOperationFinished(r2, r3);
    }

    protected void onSeekOperationFinished(boolean r1, long r2) {
    }

    protected final int seekToPosition(ExtractorInput r3, long r4, PositionHolder r6) {
        if (r4 != r3.getPosition()) goto L6;
        return 0;
    L6:
        r6.position = r4;
        return 1;
    }

    public final void setSeekTargetUs(long r3) {
        if (this.seekOperationParams != null) goto L5;
    L7:
        this.seekOperationParams = createSeekParamsForTargetTimeUs(r3);
        return;
    L5:
        if (SeekOperationParams.access$000(this.seekOperationParams) != r3) goto L7;
    }

    protected final boolean skipInputUntilPosition(ExtractorInput r5, long r6) throws IOException {
        long r0 = r6 - r5.getPosition();
        if (r0 >= 0) goto L5;
        return false;
    L5:
        if (r0 > 262144) goto L10;
        r5.skipFully((int) r0);
        return true;
    L10:
        return false;
    }
}
