package androidx.media3.exoplayer.upstream.experimental;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSpec;
import androidx.media3.exoplayer.upstream.SlidingPercentile;
import androidx.media3.exoplayer.upstream.TimeToFirstByteEstimator;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class PercentileTimeToFirstByteEstimator implements TimeToFirstByteEstimator {
    public static final int DEFAULT_MAX_SAMPLES_COUNT = 10;
    public static final float DEFAULT_PERCENTILE = 0.5f;
    private static final int MAX_DATA_SPECS = 10;
    private final Clock clock;
    private final LinkedHashMap<DataSpec, Long> initializedDataSpecs;
    private boolean isEmpty;
    private final float percentile;
    private final SlidingPercentile slidingPercentile;

    private static class FixedSizeLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        private final int maxSize;

        public FixedSizeLinkedHashMap(int r1) {
            this.maxSize = r1;
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<K, V> r3) {
            if (size() <= this.maxSize) goto L5;
            return true;
        L5:
            return false;
        }
    }

    public PercentileTimeToFirstByteEstimator() {
        this(10, 0.5f);
    }

    public PercentileTimeToFirstByteEstimator(int r2, float r3) {
        this(r2, r3, Clock.DEFAULT);
    }

    PercentileTimeToFirstByteEstimator(int r4, float r5, Clock r6) {
        if (r4 > 0) goto L5;
    L9:
        boolean r1 = false;
    L10:
        Assertions.checkArgument(r1);
        this.percentile = r5;
        this.clock = r6;
        this.initializedDataSpecs = new FixedSizeLinkedHashMap(10);
        this.slidingPercentile = new SlidingPercentile(r4);
        this.isEmpty = true;
        return;
    L5:
        if (r5 <= 0.0f) goto L9;
        if (r5 > 1.0f) goto L9;
        r1 = true;
        goto L10
    }

    @Override // androidx.media3.exoplayer.upstream.TimeToFirstByteEstimator
    public long getTimeToFirstByteEstimateUs() {
        if (this.isEmpty == false) goto L7;
        return C0180C.TIME_UNSET;
    L7:
        return (long) this.slidingPercentile.getPercentile(this.percentile);
    }

    @Override // androidx.media3.exoplayer.upstream.TimeToFirstByteEstimator
    public void onTransferInitializing(DataSpec r4) {
        this.initializedDataSpecs.remove(r4);
        this.initializedDataSpecs.put(r4, Long.valueOf(Util.msToUs(this.clock.elapsedRealtime())));
    }

    @Override // androidx.media3.exoplayer.upstream.TimeToFirstByteEstimator
    public void onTransferStart(DataSpec r7) {
        Long r0 = this.initializedDataSpecs.remove(r7);
        if (r0 != null) goto L5;
        return;
    L5:
        this.slidingPercentile.addSample(1, Util.msToUs(this.clock.elapsedRealtime()) - r0.longValue());
        this.isEmpty = false;
    }

    @Override // androidx.media3.exoplayer.upstream.TimeToFirstByteEstimator
    public void reset() {
        this.slidingPercentile.reset();
        this.isEmpty = true;
    }
}
