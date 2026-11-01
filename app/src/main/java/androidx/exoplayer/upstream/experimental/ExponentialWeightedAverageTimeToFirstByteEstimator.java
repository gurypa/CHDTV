package androidx.media3.exoplayer.upstream.experimental;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSpec;
import androidx.media3.exoplayer.upstream.TimeToFirstByteEstimator;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class ExponentialWeightedAverageTimeToFirstByteEstimator implements TimeToFirstByteEstimator {
    public static final double DEFAULT_SMOOTHING_FACTOR = 0.85d;
    private static final int MAX_DATA_SPECS = 10;
    private final Clock clock;
    private long estimateUs;
    private final LinkedHashMap<DataSpec, Long> initializedDataSpecs;
    private final double smoothingFactor;

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

    public ExponentialWeightedAverageTimeToFirstByteEstimator() {
        this(0.85d, Clock.DEFAULT);
    }

    public ExponentialWeightedAverageTimeToFirstByteEstimator(double r2) {
        this(r2, Clock.DEFAULT);
    }

    ExponentialWeightedAverageTimeToFirstByteEstimator(double r3, Clock r5) {
        this.smoothingFactor = r3;
        this.clock = r5;
        this.initializedDataSpecs = new FixedSizeLinkedHashMap(10);
        this.estimateUs = C0180C.TIME_UNSET;
    }

    @Override // androidx.media3.exoplayer.upstream.TimeToFirstByteEstimator
    public long getTimeToFirstByteEstimateUs() {
        return this.estimateUs;
    }

    @Override // androidx.media3.exoplayer.upstream.TimeToFirstByteEstimator
    public void onTransferInitializing(DataSpec r4) {
        this.initializedDataSpecs.remove(r4);
        this.initializedDataSpecs.put(r4, Long.valueOf(Util.msToUs(this.clock.elapsedRealtime())));
    }

    @Override // androidx.media3.exoplayer.upstream.TimeToFirstByteEstimator
    public void onTransferStart(DataSpec r10) {
        Long r0 = this.initializedDataSpecs.remove(r10);
        if (r0 != null) goto L5;
        return;
    L5:
        long r1 = Util.msToUs(this.clock.elapsedRealtime()) - r0.longValue();
        if (this.estimateUs != C0180C.TIME_UNSET) goto L8;
        this.estimateUs = r1;
        return;
    L8:
        this.estimateUs = (long) ((this.smoothingFactor * this.estimateUs) + ((1.0d - this.smoothingFactor) * r1));
    }

    @Override // androidx.media3.exoplayer.upstream.TimeToFirstByteEstimator
    public void reset() {
        this.estimateUs = C0180C.TIME_UNSET;
    }
}
