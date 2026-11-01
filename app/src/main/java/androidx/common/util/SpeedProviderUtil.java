package androidx.media3.common.util;

import androidx.media3.common.C0180C;
import androidx.media3.common.audio.SpeedProvider;

/* JADX INFO: loaded from: classes.dex */
public class SpeedProviderUtil {
    private SpeedProviderUtil() {
    }

    public static long getDurationAfterSpeedProviderApplied(SpeedProvider r10, long r11) {
        long r0 = 0;
        double r2 = 0.0d;
    L4:
        if (r0 >= r11) goto L10;
        long r4 = r10.getNextSpeedChangeTimeUs(r0);
        if (r4 != C0180C.TIME_UNSET) goto L8;
        r4 = Long.MAX_VALUE;
    L8:
        r2 = r2 + ((Math.min(r4, r11) - r0) / ((double) r10.getSpeed(r0)));
        r0 = r4;
        goto L4
    L10:
        return Math.round(r2);
    }
}
