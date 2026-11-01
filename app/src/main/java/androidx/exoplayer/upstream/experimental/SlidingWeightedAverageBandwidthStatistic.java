package androidx.media3.exoplayer.upstream.experimental;

import androidx.media3.common.util.Clock;
import androidx.media3.common.util.Util;
import java.util.ArrayDeque;
import java.util.Deque;

/* JADX INFO: loaded from: classes.dex */
public class SlidingWeightedAverageBandwidthStatistic implements BandwidthStatistic {
    public static final int DEFAULT_MAX_SAMPLES_COUNT = 10;
    private double bitrateWeightProductSum;
    private final Clock clock;
    private final SampleEvictionFunction sampleEvictionFunction;
    private final ArrayDeque<Sample> samples;
    private double weightSum;

    public static class Sample {
        public final long bitrate;
        public final long timeAddedMs;
        public final double weight;

        public Sample(long r1, double r3, long r5) {
            this.bitrate = r1;
            this.weight = r3;
            this.timeAddedMs = r5;
        }
    }

    public interface SampleEvictionFunction {
        boolean shouldEvictSample(Deque<Sample> r1);
    }

    public SlidingWeightedAverageBandwidthStatistic() {
        this(getMaxCountEvictionFunction(10));
    }

    public SlidingWeightedAverageBandwidthStatistic(SampleEvictionFunction r2) {
        this(r2, Clock.DEFAULT);
    }

    SlidingWeightedAverageBandwidthStatistic(SampleEvictionFunction r2, Clock r3) {
        this.samples = new ArrayDeque();
        this.sampleEvictionFunction = r2;
        this.clock = r3;
    }

    public static SampleEvictionFunction getAgeBasedEvictionFunction(long r1) {
        return getAgeBasedEvictionFunction(r1, Clock.DEFAULT);
    }

    static SampleEvictionFunction getAgeBasedEvictionFunction(final long r1, final Clock r3) {
        return new C0375xa865cce6(r1, r3);
    }

    public static SampleEvictionFunction getMaxCountEvictionFunction(final long r1) {
        return new C0376xa865cce7(r1);
    }

    static /* synthetic */ boolean lambda$getAgeBasedEvictionFunction$1(long r6, Clock r8, Deque r9) {
        if (r9.isEmpty() == false) goto L6;
        return false;
    L6:
        if ((((Sample) Util.castNonNull((Sample) r9.peek())).timeAddedMs + r6) >= r8.elapsedRealtime()) goto L9;
        return true;
    L9:
        return false;
    }

    static /* synthetic */ boolean lambda$getMaxCountEvictionFunction$0(long r2, Deque r4) {
        if (r4.size() < r2) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthStatistic
    public void addSample(long r12, long r14) {
    L3:
        if (this.sampleEvictionFunction.shouldEvictSample(this.samples) == false) goto L5;
        Sample r0 = this.samples.remove();
        this.bitrateWeightProductSum -= r0.bitrate * r0.weight;
        this.weightSum -= r0.weight;
        goto L3
    L5:
        Sample r2 = new Sample((8000000 * r12) / r14, Math.sqrt(r12), this.clock.elapsedRealtime());
        this.samples.add(r2);
        this.bitrateWeightProductSum += r2.bitrate * r2.weight;
        this.weightSum += r2.weight;
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthStatistic
    public long getBandwidthEstimate() {
        if (this.samples.isEmpty() == false) goto L7;
        return Long.MIN_VALUE;
    L7:
        return (long) (this.bitrateWeightProductSum / this.weightSum);
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthStatistic
    public void reset() {
        this.samples.clear();
        this.bitrateWeightProductSum = 0.0d;
        this.weightSum = 0.0d;
    }
}
