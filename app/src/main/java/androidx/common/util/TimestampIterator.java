package androidx.media3.common.util;

import androidx.media3.common.C0180C;

/* JADX INFO: loaded from: classes.dex */
public interface TimestampIterator {

    /* JADX INFO: renamed from: androidx.media3.common.util.TimestampIterator$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static long $default$getLastTimestampUs(TimestampIterator r2) {
            return C0180C.TIME_UNSET;
        }
    }

    TimestampIterator copyOf();

    long getLastTimestampUs();

    boolean hasNext();

    long next();
}
