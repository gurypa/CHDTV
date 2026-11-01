package androidx.media3.extractor.text.webvtt;

import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.text.Subtitle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class WebvttSubtitle implements Subtitle {
    private final List<WebvttCueInfo> cueInfos;
    private final long[] cueTimesUs;
    private final long[] sortedCueTimesUs;

    public WebvttSubtitle(List<WebvttCueInfo> r8) {
        this.cueInfos = Collections.unmodifiableList(new ArrayList(r8));
        this.cueTimesUs = new long[r8.size() * 2];
        int r0 = 0;
    L4:
        if (r0 >= r8.size()) goto L6;
        WebvttCueInfo r1 = r8.get(r0);
        int r2 = r0 * 2;
        this.cueTimesUs[r2] = r1.startTimeUs;
        this.cueTimesUs[r2 + 1] = r1.endTimeUs;
        r0 = r0 + 1;
        goto L4
    L6:
        this.sortedCueTimesUs = Arrays.copyOf(this.cueTimesUs, this.cueTimesUs.length);
        Arrays.sort(this.sortedCueTimesUs);
    }

    static /* synthetic */ int lambda$getCues$0(WebvttCueInfo r4, WebvttCueInfo r5) {
        return Long.compare(r4.startTimeUs, r5.startTimeUs);
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public List<Cue> getCues(long r8) {
        ArrayList r0 = new ArrayList();
        ArrayList r1 = new ArrayList();
        int r2 = 0;
    L4:
        if (r2 >= this.cueInfos.size()) goto L14;
        if (this.cueTimesUs[r2 * 2] > r8) goto L13;
        if (r8 >= this.cueTimesUs[(r2 * 2) + 1]) goto L13;
        WebvttCueInfo r3 = this.cueInfos.get(r2);
        if (r3.cue.line != (-3.4028235E38f)) goto L12;
        r1.add(r3);
        goto L13
    L12:
        r0.add(r3.cue);
    L13:
        r2 = r2 + 1;
        goto L4
    L14:
        Collections.sort(r1, new WebvttSubtitle$$ExternalSyntheticLambda0());
        int r4 = 0;
    L16:
        if (r4 >= r1.size()) goto L18;
        r0.add(((WebvttCueInfo) r1.get(r4)).cue.buildUpon().setLine((-1) - r4, 1).build());
        r4 = r4 + 1;
        goto L16
    L18:
        return r0;
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public long getEventTime(int r4) {
        boolean r0 = true;
        if (r4 < 0) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkArgument(r2);
        if (r4 < this.sortedCueTimesUs.length) goto L10;
        r0 = false;
    L10:
        Assertions.checkArgument(r0);
        return this.sortedCueTimesUs[r4];
    L5:
        r2 = false;
        goto L6
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public int getEventTimeCount() {
        return this.sortedCueTimesUs.length;
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public int getNextEventTimeIndex(long r3) {
        int r0 = Util.binarySearchCeil(this.sortedCueTimesUs, r3, false, false);
        if (r0 < this.sortedCueTimesUs.length) goto L7;
        return -1;
    L7:
        return r0;
    }
}
