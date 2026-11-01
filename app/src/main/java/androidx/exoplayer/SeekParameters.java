package androidx.media3.exoplayer;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class SeekParameters {
    public static final SeekParameters CLOSEST_SYNC = null;
    public static final SeekParameters DEFAULT = null;
    public static final SeekParameters EXACT = null;
    public static final SeekParameters NEXT_SYNC = null;
    public static final SeekParameters PREVIOUS_SYNC = null;
    public final long toleranceAfterUs;
    public final long toleranceBeforeUs;

    static {
        EXACT = new SeekParameters(0, 0);
        CLOSEST_SYNC = new SeekParameters(Long.MAX_VALUE, Long.MAX_VALUE);
        PREVIOUS_SYNC = new SeekParameters(Long.MAX_VALUE, 0);
        NEXT_SYNC = new SeekParameters(0, Long.MAX_VALUE);
        DEFAULT = EXACT;
    }

    public SeekParameters(long r6, long r8) {
        boolean r3 = true;
        if (r6 < 0) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkArgument(r2);
        if (r8 >= 0) goto L10;
        r3 = false;
    L10:
        Assertions.checkArgument(r3);
        this.toleranceBeforeUs = r6;
        this.toleranceAfterUs = r8;
        return;
    L5:
        r2 = false;
        goto L6
    }

    public boolean equals(Object r8) {
        if (this != r8) goto L6;
        return true;
    L6:
        if (r8 != null) goto L8;
    L17:
        return false;
    L8:
        if (getClass() != r8.getClass()) goto L17;
        SeekParameters r2 = (SeekParameters) r8;
        if (this.toleranceBeforeUs != r2.toleranceBeforeUs) goto L16;
        if (this.toleranceAfterUs != r2.toleranceAfterUs) goto L16;
        return true;
    L16:
        return false;
    }

    public int hashCode() {
        return (((int) this.toleranceBeforeUs) * 31) + ((int) this.toleranceAfterUs);
    }

    public long resolveSeekPositionUs(long r12, long r14, long r16) {
        if (this.toleranceBeforeUs == 0) goto L5;
    L7:
        long r3 = Util.subtractWithOverflowDefault(r12, this.toleranceBeforeUs, Long.MIN_VALUE);
        long r0 = Util.addWithOverflowDefault(r12, this.toleranceAfterUs, Long.MAX_VALUE);
        boolean r5 = true;
        if (r3 <= r14) goto L10;
    L12:
        boolean r2 = false;
    L14:
        if (r3 <= r16) goto L16;
    L18:
        r5 = false;
    L19:
        if (r2 == false) goto L25;
        if (r5 == false) goto L25;
        if (Math.abs(r14 - r12) > Math.abs(r16 - r12)) goto L24;
        return r14;
    L24:
        return r16;
    L25:
        if (r2 == false) goto L27;
        return r14;
    L27:
        if (r5 == false) goto L29;
        return r16;
    L29:
        return r3;
    L16:
        if (r16 > r0) goto L18;
    L10:
        if (r14 > r0) goto L12;
        r2 = true;
        goto L14
    L5:
        if (this.toleranceAfterUs != 0) goto L7;
        return r12;
    }
}
