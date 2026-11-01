package androidx.media3.extractor.mp3;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.IndexSeekMap;
import androidx.media3.extractor.SeekMap;
import java.math.RoundingMode;

/* JADX INFO: loaded from: classes.dex */
final class IndexSeeker implements Seeker {
    static final long MIN_TIME_BETWEEN_POINTS_US = 100000;
    private final int averageBitrate;
    private final long dataEndPosition;
    private final IndexSeekMap indexSeekMap;

    public IndexSeeker(long r13, long r15, long r17) {
        this.indexSeekMap = new IndexSeekMap(new long[]{r15}, new long[]{0}, r13);
        this.dataEndPosition = r17;
        int r11 = C0180C.RATE_UNSET_INT;
        if (r13 == C0180C.TIME_UNSET) goto L10;
        long r0 = Util.scaleLargeValue(r15 - r17, 8, r13, RoundingMode.HALF_UP);
        if (r0 > 0) goto L7;
    L9:
        this.averageBitrate = r11;
        return;
    L7:
        if (r0 > 2147483647L) goto L9;
        r11 = (int) r0;
        goto L9
    L10:
        this.averageBitrate = C0180C.RATE_UNSET_INT;
    }

    @Override // androidx.media3.extractor.mp3.Seeker
    public int getAverageBitrate() {
        return this.averageBitrate;
    }

    @Override // androidx.media3.extractor.mp3.Seeker
    public long getDataEndPosition() {
        return this.dataEndPosition;
    }

    @Override // androidx.media3.extractor.SeekMap
    public long getDurationUs() {
        return this.indexSeekMap.getDurationUs();
    }

    @Override // androidx.media3.extractor.SeekMap
    public SeekMap.SeekPoints getSeekPoints(long r2) {
        return this.indexSeekMap.getSeekPoints(r2);
    }

    @Override // androidx.media3.extractor.mp3.Seeker
    public long getTimeUs(long r3) {
        return this.indexSeekMap.getTimeUs(r3);
    }

    @Override // androidx.media3.extractor.SeekMap
    public boolean isSeekable() {
        return this.indexSeekMap.isSeekable();
    }

    public boolean isTimeUsInIndex(long r4) {
        return this.indexSeekMap.isTimeUsInIndex(r4, 100000);
    }

    public void maybeAddSeekPoint(long r2, long r4) {
        if (isTimeUsInIndex(r2) == false) goto L5;
        return;
    L5:
        this.indexSeekMap.addSeekPoint(r2, r4);
    }

    void setDurationUs(long r2) {
        this.indexSeekMap.setDurationUs(r2);
    }
}
