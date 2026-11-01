package androidx.media3.exoplayer.upstream.experimental;

import android.os.Handler;
import androidx.media3.datasource.DataSource;
import androidx.media3.exoplayer.upstream.BandwidthMeter;

/* JADX INFO: loaded from: classes.dex */
public interface BandwidthEstimator {
    public static final long ESTIMATE_NOT_AVAILABLE = Long.MIN_VALUE;

    void addEventListener(Handler r1, BandwidthMeter.EventListener r2);

    long getBandwidthEstimate();

    void onBytesTransferred(DataSource r1, int r2);

    void onNetworkTypeChange(long r1);

    void onTransferEnd(DataSource r1);

    void onTransferInitializing(DataSource r1);

    void onTransferStart(DataSource r1);

    void removeEventListener(BandwidthMeter.EventListener r1);
}
