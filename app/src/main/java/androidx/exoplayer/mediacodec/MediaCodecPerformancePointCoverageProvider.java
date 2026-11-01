package androidx.media3.exoplayer.mediacodec;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Util;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class MediaCodecPerformancePointCoverageProvider {
    static final int COVERAGE_RESULT_NO = 1;
    static final int COVERAGE_RESULT_NO_PERFORMANCE_POINTS_UNSUPPORTED = 0;
    static final int COVERAGE_RESULT_YES = 2;
    private static Boolean shouldIgnorePerformancePoints;

    private static final class Api29 {
        private Api29() {
        }

        public static int areResolutionAndFrameRateCovered(android.media.MediaCodecInfo.VideoCapabilities r5, int r6, int r7, double r8) {
            List<android.media.MediaCodecInfo.VideoCapabilities.PerformancePoint> r0 = r5.getSupportedPerformancePoints();
            if (r0 != null) goto L5;
        L15:
            return 0;
        L5:
            if (r0.isEmpty() == true) goto L15;
            int r3 = evaluatePerformancePointCoverage(r0, new android.media.MediaCodecInfo.VideoCapabilities.PerformancePoint(r6, r7, (int) r8));
            if (r3 == 1) goto L10;
        L14:
            return r3;
        L10:
            if (MediaCodecPerformancePointCoverageProvider.access$000() != null) goto L14;
            MediaCodecPerformancePointCoverageProvider.access$002(Boolean.valueOf(shouldIgnorePerformancePoints()));
            if (MediaCodecPerformancePointCoverageProvider.access$000().booleanValue() == false) goto L14;
            return 0;
        }

        private static int evaluateH264RequiredSupport(boolean r9) {
            Format r1 = new Format.Builder().setSampleMimeType(MimeTypes.VIDEO_H264).build();     // Catch: MediaCodecUtil.DecoderQueryException -> L20
            if (r1.sampleMimeType == null) goto L19;
            List<MediaCodecInfo> r2 = MediaCodecUtil.getDecoderInfosSoftMatch(MediaCodecSelector.DEFAULT, r1, r9, false);     // Catch: MediaCodecUtil.DecoderQueryException -> L20
            int r3 = 0;
        L7:
            if (r3 >= r2.size()) goto L19;
            if (r2.get(r3).capabilities == null) goto L18;
            if (r2.get(r3).capabilities.getVideoCapabilities() == null) goto L18;
            List<android.media.MediaCodecInfo.VideoCapabilities.PerformancePoint> r4 = r2.get(r3).capabilities.getVideoCapabilities().getSupportedPerformancePoints();     // Catch: MediaCodecUtil.DecoderQueryException -> L20
            if (r4 == null) goto L18;
            if (r4.isEmpty() == true) goto L18;
            return evaluatePerformancePointCoverage(r4, new android.media.MediaCodecInfo.VideoCapabilities.PerformancePoint(1280, 720, 60));
        L18:
            r3 = r3 + 1;
        L19:
            return 0;
        L21:
            return 0;
        }

        private static int evaluatePerformancePointCoverage(List<android.media.MediaCodecInfo.VideoCapabilities.PerformancePoint> r2, android.media.MediaCodecInfo.VideoCapabilities.PerformancePoint r3) {
            int r0 = 0;
        L4:
            if (r0 >= r2.size()) goto L10;
            if (r2.get(r0).covers(r3) == true) goto L7;
            r0 = r0 + 1;
            goto L4
        L7:
            return 2;
        L10:
            return 1;
        }

        private static boolean shouldIgnorePerformancePoints() {
            if (Util.SDK_INT < 35) goto L5;
            return false;
        L5:
            int r0 = evaluateH264RequiredSupport(false);
            int r3 = evaluateH264RequiredSupport(true);
            if (r0 != 0) goto L9;
            return true;
        L9:
            if (r3 != 0) goto L13;
            if (r0 != 2) goto L12;
            return false;
        L12:
            return true;
        L13:
            if (r0 != 2) goto L16;
            if (r3 != 2) goto L16;
            return false;
        L16:
            return true;
        }
    }

    private MediaCodecPerformancePointCoverageProvider() {
    }

    static /* synthetic */ Boolean access$000() {
        return shouldIgnorePerformancePoints;
    }

    static /* synthetic */ Boolean access$002(Boolean r0) {
        shouldIgnorePerformancePoints = r0;
        return r0;
    }

    public static int areResolutionAndFrameRateCovered(android.media.MediaCodecInfo.VideoCapabilities r2, int r3, int r4, double r5) {
        if (Util.SDK_INT >= 29) goto L5;
        return 0;
    L5:
        if (shouldIgnorePerformancePoints == null) goto L10;
        if (shouldIgnorePerformancePoints.booleanValue() == false) goto L10;
        return 0;
    L10:
        return Api29.areResolutionAndFrameRateCovered(r2, r3, r4, r5);
    }
}
