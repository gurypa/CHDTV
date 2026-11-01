package androidx.media3.exoplayer.video;

import androidx.media3.common.C0180C;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
final class FixedFrameRateEstimator {
    public static final int CONSECUTIVE_MATCHING_FRAME_DURATIONS_FOR_SYNC = 15;
    static final long MAX_MATCHING_FRAME_DIFFERENCE_NS = 1000000;
    private Matcher candidateMatcher;
    private boolean candidateMatcherActive;
    private Matcher currentMatcher;
    private int framesWithoutSyncCount;
    private long lastFramePresentationTimeNs;
    private boolean switchToCandidateMatcherWhenSynced;

    private static final class Matcher {
        private long firstFrameDurationNs;
        private long firstFramePresentationTimeNs;
        private long frameCount;
        private long lastFramePresentationTimeNs;
        private long matchingFrameCount;
        private long matchingFrameDurationSumNs;
        private int recentFrameOutlierCount;
        private final boolean[] recentFrameOutlierFlags;

        public Matcher() {
            this.recentFrameOutlierFlags = new boolean[15];
        }

        private static int getRecentFrameOutlierIndex(long r2) {
            return (int) (r2 % 15);
        }

        public long getFrameDurationNs() {
            if (this.matchingFrameCount != 0) goto L6;
            return 0;
        L6:
            return this.matchingFrameDurationSumNs / this.matchingFrameCount;
        }

        public long getMatchingFrameDurationSumNs() {
            return this.matchingFrameDurationSumNs;
        }

        public boolean isLastFrameOutlier() {
            if (this.frameCount != 0) goto L7;
            return false;
        L7:
            return this.recentFrameOutlierFlags[getRecentFrameOutlierIndex(this.frameCount - 1)];
        }

        public boolean isSynced() {
            if (this.frameCount > 15) goto L5;
        L7:
            return false;
        L5:
            if (this.recentFrameOutlierCount != 0) goto L7;
            return true;
        }

        public void onNextFrame(long r10) {
            if (this.frameCount != 0) goto L6;
            this.firstFramePresentationTimeNs = r10;
        L16:
            this.frameCount++;
            this.lastFramePresentationTimeNs = r10;
            return;
        L6:
            if (this.frameCount != 1) goto L8;
            this.firstFrameDurationNs = r10 - this.firstFramePresentationTimeNs;
            this.matchingFrameDurationSumNs = this.firstFrameDurationNs;
            this.matchingFrameCount = 1;
            goto L16
        L8:
            long r3 = r10 - this.lastFramePresentationTimeNs;
            int r0 = getRecentFrameOutlierIndex(this.frameCount);
            if (Math.abs(r3 - this.firstFrameDurationNs) > 1000000) goto L14;
            this.matchingFrameCount++;
            this.matchingFrameDurationSumNs += r3;
            if (this.recentFrameOutlierFlags[r0] == false) goto L16;
            this.recentFrameOutlierFlags[r0] = false;
            this.recentFrameOutlierCount--;
            goto L16
        L14:
            if (this.recentFrameOutlierFlags[r0] == true) goto L16;
            this.recentFrameOutlierFlags[r0] = true;
            this.recentFrameOutlierCount++;
            goto L16
        }

        public void reset() {
            this.frameCount = 0;
            this.matchingFrameCount = 0;
            this.matchingFrameDurationSumNs = 0;
            this.recentFrameOutlierCount = 0;
            Arrays.fill(this.recentFrameOutlierFlags, false);
        }
    }

    public FixedFrameRateEstimator() {
        this.currentMatcher = new Matcher();
        this.candidateMatcher = new Matcher();
        this.lastFramePresentationTimeNs = C0180C.TIME_UNSET;
    }

    public long getFrameDurationNs() {
        if (isSynced() == true) goto L7;
        return C0180C.TIME_UNSET;
    L7:
        return this.currentMatcher.getFrameDurationNs();
    }

    public float getFrameRate() {
        if (isSynced() == true) goto L7;
        return -1.0f;
    L7:
        return (float) (1.0E9d / this.currentMatcher.getFrameDurationNs());
    }

    public int getFramesWithoutSyncCount() {
        return this.framesWithoutSyncCount;
    }

    public long getMatchingFrameDurationSumNs() {
        if (isSynced() == true) goto L7;
        return C0180C.TIME_UNSET;
    L7:
        return this.currentMatcher.getMatchingFrameDurationSumNs();
    }

    public boolean isSynced() {
        return this.currentMatcher.isSynced();
    }

    public void onNextFrame(long r8) {
        this.currentMatcher.onNextFrame(r8);
        int r2 = 0;
        if (this.currentMatcher.isSynced() == false) goto L8;
        if (this.switchToCandidateMatcherWhenSynced == true) goto L8;
        this.candidateMatcherActive = false;
    L16:
        if (this.candidateMatcherActive == true) goto L18;
    L20:
        this.lastFramePresentationTimeNs = r8;
        if (this.currentMatcher.isSynced() == true) goto L24;
        r2 = this.framesWithoutSyncCount + 1;
    L24:
        this.framesWithoutSyncCount = r2;
        return;
    L18:
        if (this.candidateMatcher.isSynced() == false) goto L20;
        Matcher r0 = this.currentMatcher;
        this.currentMatcher = this.candidateMatcher;
        this.candidateMatcher = r0;
        this.candidateMatcherActive = false;
        this.switchToCandidateMatcherWhenSynced = false;
    L8:
        if (this.lastFramePresentationTimeNs == C0180C.TIME_UNSET) goto L16;
        if (this.candidateMatcherActive == true) goto L12;
    L13:
        this.candidateMatcher.reset();
        this.candidateMatcher.onNextFrame(this.lastFramePresentationTimeNs);
    L14:
        this.candidateMatcherActive = true;
        this.candidateMatcher.onNextFrame(r8);
        goto L16
    L12:
        if (this.candidateMatcher.isLastFrameOutlier() == false) goto L14;
        goto L13
    }

    public void reset() {
        this.currentMatcher.reset();
        this.candidateMatcher.reset();
        this.candidateMatcherActive = false;
        this.lastFramePresentationTimeNs = C0180C.TIME_UNSET;
        this.framesWithoutSyncCount = 0;
    }
}
