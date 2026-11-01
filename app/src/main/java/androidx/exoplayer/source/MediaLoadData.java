package androidx.media3.exoplayer.source;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;

/* JADX INFO: loaded from: classes.dex */
public final class MediaLoadData {
    public final int dataType;
    public final long mediaEndTimeMs;
    public final long mediaStartTimeMs;
    public final Format trackFormat;
    public final Object trackSelectionData;
    public final int trackSelectionReason;
    public final int trackType;

    public MediaLoadData(int r11) {
        this(r11, -1, null, 0, null, C0180C.TIME_UNSET, C0180C.TIME_UNSET);
    }

    public MediaLoadData(int r1, int r2, Format r3, int r4, Object r5, long r6, long r8) {
        this.dataType = r1;
        this.trackType = r2;
        this.trackFormat = r3;
        this.trackSelectionReason = r4;
        this.trackSelectionData = r5;
        this.mediaStartTimeMs = r6;
        this.mediaEndTimeMs = r8;
    }
}
