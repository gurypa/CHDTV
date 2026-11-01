package androidx.media3.exoplayer;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.source.MediaSource;

/* JADX INFO: loaded from: classes.dex */
final class MediaPeriodInfo {
    public final long durationUs;
    public final long endPositionUs;

    /* JADX INFO: renamed from: id */
    public final MediaSource.MediaPeriodId f35id;
    public final boolean isFinal;
    public final boolean isFollowedByTransitionToSameStream;
    public final boolean isLastInTimelinePeriod;
    public final boolean isLastInTimelineWindow;
    public final long requestedContentPositionUs;
    public final long startPositionUs;

    MediaPeriodInfo(MediaSource.MediaPeriodId r13, long r14, long r16, long r18, long r20, boolean r22, boolean r23, boolean r24, boolean r25) {
        boolean r4 = false;
        if (r25 == false) goto L7;
        if (r23 == true) goto L7;
        boolean r6 = false;
    L8:
        Assertions.checkArgument(r6);
        if (r24 == false) goto L13;
        if (r23 == true) goto L13;
        boolean r7 = false;
    L14:
        Assertions.checkArgument(r7);
        if (r22 == false) goto L19;
        if (r23 == true) goto L20;
        if (r24 == true) goto L20;
        if (r25 == false) goto L19;
    L20:
        Assertions.checkArgument(r4);
        this.f35id = r13;
        this.startPositionUs = r14;
        this.requestedContentPositionUs = r16;
        this.endPositionUs = r18;
        this.durationUs = r20;
        this.isFollowedByTransitionToSameStream = r22;
        this.isLastInTimelinePeriod = r23;
        this.isLastInTimelineWindow = r24;
        this.isFinal = r25;
        return;
    L19:
        r4 = true;
    L13:
        r7 = true;
    L7:
        r6 = true;
        goto L8
    }

    public MediaPeriodInfo copyWithRequestedContentPositionUs(long r17) {
        if (r17 != this.requestedContentPositionUs) goto L6;
        return this;
    L6:
        return new MediaPeriodInfo(this.f35id, this.startPositionUs, r17, this.endPositionUs, this.durationUs, this.isFollowedByTransitionToSameStream, this.isLastInTimelinePeriod, this.isLastInTimelineWindow, this.isFinal);
    }

    public MediaPeriodInfo copyWithStartPositionUs(long r17) {
        if (r17 != this.startPositionUs) goto L6;
        return this;
    L6:
        return new MediaPeriodInfo(this.f35id, r17, this.requestedContentPositionUs, this.endPositionUs, this.durationUs, this.isFollowedByTransitionToSameStream, this.isLastInTimelinePeriod, this.isLastInTimelineWindow, this.isFinal);
    }

    public boolean equals(Object r8) {
        if (this != r8) goto L6;
        return true;
    L6:
        if (r8 != null) goto L8;
    L31:
        return false;
    L8:
        if (getClass() != r8.getClass()) goto L31;
        MediaPeriodInfo r2 = (MediaPeriodInfo) r8;
        if (this.startPositionUs != r2.startPositionUs) goto L30;
        if (this.requestedContentPositionUs != r2.requestedContentPositionUs) goto L30;
        if (this.endPositionUs != r2.endPositionUs) goto L30;
        if (this.durationUs != r2.durationUs) goto L30;
        if (this.isFollowedByTransitionToSameStream != r2.isFollowedByTransitionToSameStream) goto L30;
        if (this.isLastInTimelinePeriod != r2.isLastInTimelinePeriod) goto L30;
        if (this.isLastInTimelineWindow != r2.isLastInTimelineWindow) goto L30;
        if (this.isFinal != r2.isFinal) goto L30;
        if (Util.areEqual(this.f35id, r2.f35id) == false) goto L30;
        return true;
    L30:
        return false;
    }

    public int hashCode() {
        return (((((((((((((((((17 * 31) + this.f35id.hashCode()) * 31) + ((int) this.startPositionUs)) * 31) + ((int) this.requestedContentPositionUs)) * 31) + ((int) this.endPositionUs)) * 31) + ((int) this.durationUs)) * 31) + (this.isFollowedByTransitionToSameStream ? 1 : 0)) * 31) + (this.isLastInTimelinePeriod ? 1 : 0)) * 31) + (this.isLastInTimelineWindow ? 1 : 0)) * 31) + (this.isFinal ? 1 : 0);
    }
}
