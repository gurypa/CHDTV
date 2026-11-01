package androidx.media3.exoplayer.source.ads;

import androidx.media3.common.AdPlaybackState;
import androidx.media3.common.C0180C;
import androidx.media3.common.Player;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.source.MediaSource;

/* JADX INFO: loaded from: classes.dex */
public final class ServerSideAdInsertionUtil {
    private ServerSideAdInsertionUtil() {
    }

    public static AdPlaybackState addAdGroupToAdPlaybackState(AdPlaybackState r9, long r10, long r12, long... r14) {
        long r0 = getMediaPeriodPositionUsForContent(r10, -1, r9);
        int r4 = r9.removedAdGroupCount;
    L4:
        if (r4 >= r9.adGroupCount) goto L10;
        if (r9.getAdGroup(r4).timeUs == Long.MIN_VALUE) goto L10;
        if (r9.getAdGroup(r4).timeUs > r0) goto L10;
        r4 = r4 + 1;
    L10:
        AdPlaybackState r11 = r9.withNewAdGroup(r4, r0).withIsServerSideInserted(r4, true).withAdCount(r4, r14.length).withAdDurationsUs(r4, r14).withContentResumeOffsetUs(r4, r12);
        int r2 = 0;
        AdPlaybackState r3 = r11;
    L12:
        if (r2 >= r14.length) goto L17;
        if (r14[r2] != 0) goto L17;
        r3 = r3.withSkippedAd(r4, r2);
        r2 = r2 + 1;
    L17:
        return correctFollowingAdGroupTimes(r3, r4, Util.sum(r14), r12);
    }

    private static AdPlaybackState correctFollowingAdGroupTimes(AdPlaybackState r7, int r8, long r9, long r11) {
        long r0 = (-r9) + r11;
        int r2 = r8 + 1;
    L4:
        if (r2 >= r7.adGroupCount) goto L9;
        long r3 = r7.getAdGroup(r2).timeUs;
        if (r3 == Long.MIN_VALUE) goto L8;
        r7 = r7.withAdGroupTimeUs(r2, r3 + r0);
    L8:
        r2 = r2 + 1;
        goto L4
    L9:
        return r7;
    }

    public static int getAdCountInGroup(AdPlaybackState r3, int r4) {
        AdPlaybackState.AdGroup r0 = r3.getAdGroup(r4);
        if (r0.count != (-1)) goto L6;
        return 0;
    L6:
        return r0.count;
    }

    public static long getMediaPeriodPositionUs(long r2, MediaSource.MediaPeriodId r4, AdPlaybackState r5) {
        if (r4.isAd() == false) goto L6;
        return getMediaPeriodPositionUsForAd(r2, r4.adGroupIndex, r4.adIndexInAdGroup, r5);
    L6:
        return getMediaPeriodPositionUsForContent(r2, r4.nextAdGroupIndex, r5);
    }

    public static long getMediaPeriodPositionUsForAd(long r7, int r9, int r10, AdPlaybackState r11) {
        AdPlaybackState.AdGroup r0 = r11.getAdGroup(r9);
        long r8 = r7 - r0.timeUs;
        int r1 = r11.removedAdGroupCount;
    L3:
        if (r1 >= r9) goto L10;
        AdPlaybackState.AdGroup r2 = r11.getAdGroup(r1);
        int r3 = 0;
    L6:
        if (r3 >= getAdCountInGroup(r11, r1)) goto L8;
        r8 = r8 - r2.durationsUs[r3];
        r3 = r3 + 1;
        goto L6
    L8:
        r8 = r8 + r2.contentResumeOffsetUs;
        r1 = r1 + 1;
        goto L3
    L10:
        if (r10 >= getAdCountInGroup(r11, r9)) goto L14;
        int r4 = 0;
    L12:
        if (r4 >= r10) goto L14;
        r8 = r8 - r0.durationsUs[r4];
        r4 = r4 + 1;
    L14:
        return r8;
    }

    public static long getMediaPeriodPositionUsForContent(long r10, int r12, AdPlaybackState r13) {
        long r0 = 0;
        if (r12 != (-1)) goto L5;
        r12 = r13.adGroupCount;
    L5:
        int r2 = r13.removedAdGroupCount;
    L6:
        if (r2 >= r12) goto L22;
        AdPlaybackState.AdGroup r3 = r13.getAdGroup(r2);
        if (r3.timeUs == Long.MIN_VALUE) goto L22;
        if (r3.timeUs > (r10 - r0)) goto L22;
        int r4 = 0;
    L14:
        if (r4 >= getAdCountInGroup(r13, r2)) goto L16;
        r0 = r0 + r3.durationsUs[r4];
        r4 = r4 + 1;
        goto L14
    L16:
        r0 = r0 - r3.contentResumeOffsetUs;
        if ((r3.timeUs + r3.contentResumeOffsetUs) > (r10 - r0)) goto L19;
        r2 = r2 + 1;
        goto L6
    L19:
        return Math.max(r3.timeUs, r10 - r0);
    L22:
        return r10 - r0;
    }

    public static long getStreamPositionUs(long r2, MediaSource.MediaPeriodId r4, AdPlaybackState r5) {
        if (r4.isAd() == false) goto L6;
        return getStreamPositionUsForAd(r2, r4.adGroupIndex, r4.adIndexInAdGroup, r5);
    L6:
        return getStreamPositionUsForContent(r2, r4.nextAdGroupIndex, r5);
    }

    public static long getStreamPositionUs(Player r8, AdPlaybackState r9) {
        Timeline r0 = r8.getCurrentTimeline();
        if (r0.isEmpty() == false) goto L5;
        return C0180C.TIME_UNSET;
    L5:
        Timeline.Period r1 = r0.getPeriod(r8.getCurrentPeriodIndex(), new Timeline.Period());
        if (Util.areEqual(r1.getAdsId(), r9.adsId) == true) goto L9;
        return C0180C.TIME_UNSET;
    L9:
        if (r8.isPlayingAd() == false) goto L13;
        int r2 = r8.getCurrentAdGroupIndex();
        int r3 = r8.getCurrentAdIndexInAdGroup();
        return getStreamPositionUsForAd(Util.msToUs(r8.getCurrentPosition()), r2, r3, r9);
    L13:
        return getStreamPositionUsForContent(Util.msToUs(r8.getCurrentPosition()) - r1.getPositionInWindowUs(), -1, r9);
    }

    public static long getStreamPositionUsForAd(long r7, int r9, int r10, AdPlaybackState r11) {
        AdPlaybackState.AdGroup r0 = r11.getAdGroup(r9);
        long r8 = r7 + r0.timeUs;
        int r1 = r11.removedAdGroupCount;
    L3:
        if (r1 >= r9) goto L10;
        AdPlaybackState.AdGroup r2 = r11.getAdGroup(r1);
        int r3 = 0;
    L6:
        if (r3 >= getAdCountInGroup(r11, r1)) goto L8;
        r8 = r8 + r2.durationsUs[r3];
        r3 = r3 + 1;
        goto L6
    L8:
        r8 = r8 - r2.contentResumeOffsetUs;
        r1 = r1 + 1;
        goto L3
    L10:
        if (r10 >= getAdCountInGroup(r11, r9)) goto L14;
        int r4 = 0;
    L12:
        if (r4 >= r10) goto L14;
        r8 = r8 + r0.durationsUs[r4];
        r4 = r4 + 1;
    L14:
        return r8;
    }

    public static long getStreamPositionUsForContent(long r10, int r12, AdPlaybackState r13) {
        long r0 = 0;
        if (r12 != (-1)) goto L5;
        r12 = r13.adGroupCount;
    L5:
        int r2 = r13.removedAdGroupCount;
    L6:
        if (r2 >= r12) goto L22;
        AdPlaybackState.AdGroup r3 = r13.getAdGroup(r2);
        if (r3.timeUs == Long.MIN_VALUE) goto L22;
        if (r3.timeUs > r10) goto L22;
        long r4 = r3.timeUs + r0;
        int r6 = 0;
    L14:
        if (r6 >= getAdCountInGroup(r13, r2)) goto L16;
        r0 = r0 + r3.durationsUs[r6];
        r6 = r6 + 1;
        goto L14
    L16:
        r0 = r0 - r3.contentResumeOffsetUs;
        if ((r3.timeUs + r3.contentResumeOffsetUs) > r10) goto L19;
        r2 = r2 + 1;
        goto L6
    L19:
        return Math.max(r4, r10 + r0);
    L22:
        return r10 + r0;
    }
}
