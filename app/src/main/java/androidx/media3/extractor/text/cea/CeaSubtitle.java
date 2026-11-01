package androidx.media3.extractor.text.cea;

import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import androidx.media3.extractor.text.Subtitle;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class CeaSubtitle implements Subtitle {
    private final List<Cue> cues;

    public CeaSubtitle(List<Cue> r1) {
        this.cues = r1;
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public List<Cue> getCues(long r3) {
        if (r3 < 0) goto L6;
        return this.cues;
    L6:
        return Collections.emptyList();
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public long getEventTime(int r3) {
        if (r3 != 0) goto L4;
        boolean r0 = true;
    L5:
        Assertions.checkArgument(r0);
        return 0;
    L4:
        r0 = false;
        goto L5
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public int getEventTimeCount() {
        return 1;
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public int getNextEventTimeIndex(long r3) {
        if (r3 >= 0) goto L5;
        return 0;
    L5:
        return -1;
    }
}
