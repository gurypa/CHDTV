package androidx.media3.exoplayer.analytics;

import android.media.metrics.LogSessionId;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class PlayerId {
    public static final PlayerId UNSET = null;
    private final Object equalityToken;
    private final LogSessionIdApi31 logSessionIdApi31;
    public final String name;

    private static final class LogSessionIdApi31 {
        public static final LogSessionIdApi31 UNSET = null;
        public final LogSessionId logSessionId;

        static {
            UNSET = new LogSessionIdApi31(LogSessionId.LOG_SESSION_ID_NONE);
        }

        public LogSessionIdApi31(LogSessionId r1) {
            this.logSessionId = r1;
        }
    }

    static {
        if (Util.SDK_INT >= 31) goto L5;
        PlayerId r0 = new PlayerId("");
    L6:
        UNSET = r0;
        return;
    L5:
        r0 = new PlayerId(LogSessionIdApi31.UNSET, "");
        goto L6
    }

    public PlayerId(LogSessionId r2, String r3) {
        this(new LogSessionIdApi31(r2), r3);
    }

    private PlayerId(LogSessionIdApi31 r2, String r3) {
        this.logSessionIdApi31 = r2;
        this.name = r3;
        this.equalityToken = new Object();
    }

    public PlayerId(String r3) {
        if (Util.SDK_INT >= 31) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        this.name = r3;
        this.logSessionIdApi31 = null;
        this.equalityToken = new Object();
        return;
    L5:
        r0 = false;
        goto L6
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof PlayerId) == true) goto L8;
        return false;
    L8:
        PlayerId r1 = (PlayerId) r6;
        if (Objects.equals(this.name, r1.name) == false) goto L16;
        if (Objects.equals(this.logSessionIdApi31, r1.logSessionIdApi31) == false) goto L16;
        if (Objects.equals(this.equalityToken, r1.equalityToken) == false) goto L16;
        return true;
    L16:
        return false;
    }

    public LogSessionId getLogSessionId() {
        return ((LogSessionIdApi31) Assertions.checkNotNull(this.logSessionIdApi31)).logSessionId;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.name, this.logSessionIdApi31, this.equalityToken});
    }
}
