package androidx.media3.exoplayer.hls;

import android.util.SparseArray;
import androidx.media3.common.util.TimestampAdjuster;

/* JADX INFO: loaded from: classes.dex */
public final class TimestampAdjusterProvider {
    private final SparseArray<TimestampAdjuster> timestampAdjusters;

    public TimestampAdjusterProvider() {
        this.timestampAdjusters = new SparseArray();
    }

    public TimestampAdjuster getAdjuster(int r5) {
        TimestampAdjuster r0 = this.timestampAdjusters.get(r5);
        if (r0 != null) goto L6;
        TimestampAdjuster r1 = new TimestampAdjuster(TimestampAdjuster.MODE_SHARED);
        this.timestampAdjusters.put(r5, r1);
        return r1;
    L6:
        return r0;
    }

    public void reset() {
        this.timestampAdjusters.clear();
    }
}
