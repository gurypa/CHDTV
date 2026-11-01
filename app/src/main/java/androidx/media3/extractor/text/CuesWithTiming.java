package androidx.media3.extractor.text;

import androidx.media3.common.C0180C;
import androidx.media3.common.text.Cue;
import com.google.common.collect.ImmutableList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class CuesWithTiming {
    public final ImmutableList<Cue> cues;
    public final long durationUs;
    public final long endTimeUs;
    public final long startTimeUs;

    public CuesWithTiming(List<Cue> r4, long r5, long r7) {
        this.cues = ImmutableList.copyOf(r4);
        this.startTimeUs = r5;
        this.durationUs = r7;
        long r0 = C0180C.TIME_UNSET;
        if (r5 != C0180C.TIME_UNSET) goto L5;
    L9:
        this.endTimeUs = r0;
        return;
    L5:
        if (r7 == C0180C.TIME_UNSET) goto L9;
        r0 = r5 + r7;
        goto L9
    }
}
