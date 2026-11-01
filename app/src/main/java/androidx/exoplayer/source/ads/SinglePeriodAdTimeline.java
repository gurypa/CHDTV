package androidx.media3.exoplayer.source.ads;

import androidx.media3.common.AdPlaybackState;
import androidx.media3.common.C0180C;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.exoplayer.source.ForwardingTimeline;

/* JADX INFO: loaded from: classes.dex */
public final class SinglePeriodAdTimeline extends ForwardingTimeline {
    private final AdPlaybackState adPlaybackState;

    public SinglePeriodAdTimeline(Timeline r4, AdPlaybackState r5) {
        super(r4);
        boolean r1 = false;
        if (r4.getPeriodCount() != 1) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        if (r4.getWindowCount() != 1) goto L9;
        r1 = true;
    L9:
        Assertions.checkState(r1);
        this.adPlaybackState = r5;
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
    public Timeline.Period getPeriod(int r13, Timeline.Period r14, boolean r15) {
        this.timeline.getPeriod(r13, r14, r15);
        if (r14.durationUs != C0180C.TIME_UNSET) goto L5;
        long r0 = this.adPlaybackState.contentDurationUs;
    L6:
        Object r3 = r14.f30id;
        Object r4 = r14.uid;
        int r5 = r14.windowIndex;
        r14.set(r3, r4, r5, r0, r14.getPositionInWindowUs(), this.adPlaybackState, r14.isPlaceholder);
        return r14;
    L5:
        r0 = r14.durationUs;
        goto L6
    }
}
