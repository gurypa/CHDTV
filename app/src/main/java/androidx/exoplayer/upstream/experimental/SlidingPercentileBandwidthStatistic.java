package androidx.media3.exoplayer.upstream.experimental;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes.dex */
public class SlidingPercentileBandwidthStatistic implements BandwidthStatistic {
    public static final int DEFAULT_MAX_SAMPLES_COUNT = 10;
    public static final double DEFAULT_PERCENTILE = 0.5d;
    private long bitrateEstimate;
    private final int maxSampleCount;
    private final double percentile;
    private final ArrayDeque<Sample> samples;
    private final TreeSet<Sample> sortedSamples;
    private double weightSum;

    private static class Sample implements Comparable<Sample> {
        private final long bitrate;
        private final double weight;

        public Sample(long r1, double r3) {
            this.bitrate = r1;
            this.weight = r3;
        }

        static /* synthetic */ double access$000(Sample r2) {
            return r2.weight;
        }

        static /* synthetic */ long access$100(Sample r2) {
            return r2.bitrate;
        }

        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(Sample r5) {
            return Util.compareLong(this.bitrate, r5.bitrate);
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Sample r1) {
            return compareTo2(r1);
        }
    }

    public SlidingPercentileBandwidthStatistic() {
        this(10, 0.5d);
    }

    public SlidingPercentileBandwidthStatistic(int r3, double r4) {
        if (r4 >= 0.0d) goto L5;
    L7:
        boolean r0 = false;
    L8:
        Assertions.checkArgument(r0);
        this.maxSampleCount = r3;
        this.percentile = r4;
        this.samples = new ArrayDeque();
        this.sortedSamples = new TreeSet();
        this.bitrateEstimate = Long.MIN_VALUE;
        return;
    L5:
        if (r4 > 1.0d) goto L7;
        r0 = true;
        goto L8
    }

    private long calculateBitrateEstimate() {
        if (this.samples.isEmpty() == false) goto L6;
        return Long.MIN_VALUE;
    L6:
        double r0 = this.weightSum * this.percentile;
        double r2 = 0.0d;
        long r4 = 0;
        double r6 = 0.0d;
        Iterator<Sample> r8 = this.sortedSamples.iterator();
    L8:
        if (r8.hasNext() == false) goto L18;
        Sample r9 = r8.next();
        double r7 = r6 + (Sample.access$000(r9) / 2.0d);
        if (r7 >= r0) goto L12;
        r4 = Sample.access$100(r9);
        r2 = r7;
        r6 = r7 + (Sample.access$000(r9) / 2.0d);
        goto L8
    L12:
        if (r4 != 0) goto L16;
        return Sample.access$100(r9);
    L16:
        return ((long) (((Sample.access$100(r9) - r4) * (r0 - r2)) / (r7 - r2))) + r4;
    L18:
        return r4;
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthStatistic
    public void addSample(long r8, long r10) {
    L3:
        if (this.samples.size() < this.maxSampleCount) goto L5;
        Sample r0 = this.samples.remove();
        this.sortedSamples.remove(r0);
        this.weightSum -= Sample.access$000(r0);
        goto L3
    L5:
        double r1 = Math.sqrt(r8);
        Sample r4 = new Sample((8000000 * r8) / r10, r1);
        this.samples.add(r4);
        this.sortedSamples.add(r4);
        this.weightSum += r1;
        this.bitrateEstimate = calculateBitrateEstimate();
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthStatistic
    public long getBandwidthEstimate() {
        return this.bitrateEstimate;
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthStatistic
    public void reset() {
        this.samples.clear();
        this.sortedSamples.clear();
        this.weightSum = 0.0d;
        this.bitrateEstimate = Long.MIN_VALUE;
    }
}
