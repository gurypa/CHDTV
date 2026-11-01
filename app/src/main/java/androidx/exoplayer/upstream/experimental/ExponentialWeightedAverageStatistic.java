package androidx.media3.exoplayer.upstream.experimental;

/* JADX INFO: loaded from: classes.dex */
public class ExponentialWeightedAverageStatistic implements BandwidthStatistic {
    public static final double DEFAULT_SMOOTHING_FACTOR = 0.9999d;
    private long bitrateEstimate;
    private final double smoothingFactor;

    public ExponentialWeightedAverageStatistic() {
        this(0.9999d);
    }

    public ExponentialWeightedAverageStatistic(double r3) {
        this.smoothingFactor = r3;
        this.bitrateEstimate = Long.MIN_VALUE;
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthStatistic
    public void addSample(long r11, long r13) {
        long r0 = (8000000 * r11) / r13;
        if (this.bitrateEstimate != Long.MIN_VALUE) goto L6;
        this.bitrateEstimate = r0;
        return;
    L6:
        double r2 = Math.pow(this.smoothingFactor, Math.sqrt(r11));
        this.bitrateEstimate = (long) ((this.bitrateEstimate * r2) + ((1.0d - r2) * r0));
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthStatistic
    public long getBandwidthEstimate() {
        return this.bitrateEstimate;
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthStatistic
    public void reset() {
        this.bitrateEstimate = Long.MIN_VALUE;
    }
}
