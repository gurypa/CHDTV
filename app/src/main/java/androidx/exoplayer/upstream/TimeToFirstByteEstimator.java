package androidx.media3.exoplayer.upstream;

import androidx.media3.datasource.DataSpec;

/* JADX INFO: loaded from: classes.dex */
public interface TimeToFirstByteEstimator {
    long getTimeToFirstByteEstimateUs();

    void onTransferInitializing(DataSpec r1);

    void onTransferStart(DataSpec r1);

    void reset();
}
