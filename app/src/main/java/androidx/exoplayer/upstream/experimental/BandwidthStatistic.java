package androidx.media3.exoplayer.upstream.experimental;

/* JADX INFO: loaded from: classes.dex */
public interface BandwidthStatistic {
    void addSample(long r1, long r3);

    long getBandwidthEstimate();

    void reset();
}
