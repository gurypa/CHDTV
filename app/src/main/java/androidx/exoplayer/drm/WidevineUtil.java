package androidx.media3.exoplayer.drm;

import android.util.Pair;
import androidx.media3.common.C0180C;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class WidevineUtil {
    public static final String PROPERTY_LICENSE_DURATION_REMAINING = "LicenseDurationRemaining";
    public static final String PROPERTY_PLAYBACK_DURATION_REMAINING = "PlaybackDurationRemaining";

    private WidevineUtil() {
    }

    private static long getDurationRemainingSec(Map<String, String> r3, String r4) {
        if (r3 == null) goto L13;
        String r0 = r3.get(r4);     // Catch: NumberFormatException -> L8
        if (r0 == null) goto L14;
        return Long.parseLong(r0);
    L14:
        return C0180C.TIME_UNSET;
    L9:
        return C0180C.TIME_UNSET;
    L13:
        return C0180C.TIME_UNSET;
    }

    public static Pair<Long, Long> getLicenseDurationRemainingSec(DrmSession r5) {
        Map<String, String> r0 = r5.queryKeyStatus();
        if (r0 != null) goto L7;
        return null;
    L7:
        return new Pair(Long.valueOf(getDurationRemainingSec(r0, PROPERTY_LICENSE_DURATION_REMAINING)), Long.valueOf(getDurationRemainingSec(r0, PROPERTY_PLAYBACK_DURATION_REMAINING)));
    }
}
