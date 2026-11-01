package androidx.media3.exoplayer.upstream.experimental;

import android.os.Handler;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.datasource.DataSource;
import androidx.media3.exoplayer.upstream.BandwidthMeter;

/* JADX INFO: loaded from: classes.dex */
public class SplitParallelSampleBandwidthEstimator implements BandwidthEstimator {
    private long bandwidthEstimate;
    private final BandwidthStatistic bandwidthStatistic;
    private final Clock clock;
    private final BandwidthMeter.EventListener.EventDispatcher eventDispatcher;
    private long lastReportedBandwidthEstimate;
    private final long minBytesTransferred;
    private final int minSamples;
    private long sampleBytesTransferred;
    private long sampleStartTimeMs;
    private int streamCount;
    private long totalBytesTransferred;
    private int totalSamplesAdded;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.upstream.experimental.SplitParallelSampleBandwidthEstimator$1 */
    static /* synthetic */ class C03771 {
    }

    public static class Builder {
        private BandwidthStatistic bandwidthStatistic;
        private Clock clock;
        private long minBytesTransferred;
        private int minSamples;

        public Builder() {
            this.bandwidthStatistic = new SlidingWeightedAverageBandwidthStatistic();
            this.clock = Clock.DEFAULT;
        }

        static /* synthetic */ BandwidthStatistic access$100(Builder r1) {
            return r1.bandwidthStatistic;
        }

        static /* synthetic */ int access$200(Builder r1) {
            return r1.minSamples;
        }

        static /* synthetic */ long access$300(Builder r2) {
            return r2.minBytesTransferred;
        }

        static /* synthetic */ Clock access$400(Builder r1) {
            return r1.clock;
        }

        public SplitParallelSampleBandwidthEstimator build() {
            return new SplitParallelSampleBandwidthEstimator(this, null);
        }

        public Builder setBandwidthStatistic(BandwidthStatistic r1) {
            Assertions.checkNotNull(r1);
            this.bandwidthStatistic = r1;
            return this;
        }

        Builder setClock(Clock r1) {
            this.clock = r1;
            return this;
        }

        public Builder setMinBytesTransferred(long r3) {
            if (r3 < 0) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            this.minBytesTransferred = r3;
            return this;
        L5:
            r0 = false;
            goto L6
        }

        public Builder setMinSamples(int r2) {
            if (r2 < 0) goto L4;
            boolean r0 = true;
        L5:
            Assertions.checkArgument(r0);
            this.minSamples = r2;
            return this;
        L4:
            r0 = false;
            goto L5
        }
    }

    private SplitParallelSampleBandwidthEstimator(Builder r3) {
        this.bandwidthStatistic = Builder.access$100(r3);
        this.minSamples = Builder.access$200(r3);
        this.minBytesTransferred = Builder.access$300(r3);
        this.clock = Builder.access$400(r3);
        this.eventDispatcher = new BandwidthMeter.EventListener.EventDispatcher();
        this.bandwidthEstimate = Long.MIN_VALUE;
        this.lastReportedBandwidthEstimate = Long.MIN_VALUE;
    }

    /* synthetic */ SplitParallelSampleBandwidthEstimator(Builder r1, C03771 r2) {
        this(r1);
    }

    private void maybeNotifyBandwidthSample(int r8, long r9, long r11) {
        if (r11 == Long.MIN_VALUE) goto L13;
        if (r8 == 0) goto L6;
    L10:
        this.lastReportedBandwidthEstimate = r11;
        this.eventDispatcher.bandwidthSample(r8, r9, r11);
        return;
    L6:
        if (r9 != 0) goto L10;
        if (r11 != this.lastReportedBandwidthEstimate) goto L10;
        return;
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthEstimator
    public void addEventListener(Handler r2, BandwidthMeter.EventListener r3) {
        this.eventDispatcher.addListener(r2, r3);
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthEstimator
    public long getBandwidthEstimate() {
        return this.bandwidthEstimate;
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthEstimator
    public void onBytesTransferred(DataSource r5, int r6) {
        this.sampleBytesTransferred += (long) r6;
        this.totalBytesTransferred += (long) r6;
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthEstimator
    public void onNetworkTypeChange(long r11) {
        long r0 = this.clock.elapsedRealtime();
        if (this.streamCount <= 0) goto L5;
        int r2 = (int) (r0 - this.sampleStartTimeMs);
    L6:
        maybeNotifyBandwidthSample(r2, this.sampleBytesTransferred, r11);
        this.bandwidthStatistic.reset();
        this.bandwidthEstimate = Long.MIN_VALUE;
        this.sampleStartTimeMs = r0;
        this.sampleBytesTransferred = 0;
        this.totalSamplesAdded = 0;
        this.totalBytesTransferred = 0;
        return;
    L5:
        r2 = 0;
        goto L6
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthEstimator
    public void onTransferEnd(DataSource r14) {
        if (this.streamCount <= 0) goto L5;
        boolean r1 = true;
    L6:
        Assertions.checkState(r1);
        long r7 = this.clock.elapsedRealtime();
        long r9 = (int) (r7 - this.sampleStartTimeMs);
        if (r9 <= 0) goto L14;
        this.bandwidthStatistic.addSample(this.sampleBytesTransferred, 1000 * r9);
        this.totalSamplesAdded++;
        if (this.totalSamplesAdded > this.minSamples) goto L11;
    L13:
        maybeNotifyBandwidthSample((int) r9, this.sampleBytesTransferred, this.bandwidthEstimate);
        this.sampleStartTimeMs = r7;
        this.sampleBytesTransferred = 0;
        goto L14
    L11:
        if (this.totalBytesTransferred <= this.minBytesTransferred) goto L13;
        this.bandwidthEstimate = this.bandwidthStatistic.getBandwidthEstimate();
    L14:
        this.streamCount--;
        return;
    L5:
        r1 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthEstimator
    public void onTransferInitializing(DataSource r1) {
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthEstimator
    public void onTransferStart(DataSource r3) {
        if (this.streamCount != 0) goto L5;
        this.sampleStartTimeMs = this.clock.elapsedRealtime();
    L5:
        this.streamCount++;
    }

    @Override // androidx.media3.exoplayer.upstream.experimental.BandwidthEstimator
    public void removeEventListener(BandwidthMeter.EventListener r2) {
        this.eventDispatcher.removeListener(r2);
    }
}
