package androidx.media3.exoplayer.mediacodec;

import android.graphics.Point;
import android.util.Pair;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.DecoderReuseEvaluation;

/* JADX INFO: loaded from: classes.dex */
public final class MediaCodecInfo {
    public static final int MAX_SUPPORTED_INSTANCES_UNKNOWN = -1;
    public static final String TAG = "MediaCodecInfo";
    public final boolean adaptive;
    public final android.media.MediaCodecInfo.CodecCapabilities capabilities;
    public final String codecMimeType;
    public final boolean detachedSurfaceSupported;
    public final boolean hardwareAccelerated;
    private final boolean isVideo;
    public final String mimeType;
    public final String name;
    public final boolean secure;
    public final boolean softwareOnly;
    public final boolean tunneling;
    public final boolean vendor;

    MediaCodecInfo(String r2, String r3, String r4, android.media.MediaCodecInfo.CodecCapabilities r5, boolean r6, boolean r7, boolean r8, boolean r9, boolean r10, boolean r11, boolean r12) {
        this.name = (String) Assertions.checkNotNull(r2);
        this.mimeType = r3;
        this.codecMimeType = r4;
        this.capabilities = r5;
        this.hardwareAccelerated = r6;
        this.softwareOnly = r7;
        this.vendor = r8;
        this.adaptive = r9;
        this.tunneling = r10;
        this.secure = r11;
        this.detachedSurfaceSupported = r12;
        this.isVideo = MimeTypes.isVideo(r3);
    }

    private static int adjustMaxInputChannelCount(String r3, String r4, int r5) {
        if (r5 <= 1) goto L5;
    L41:
        return r5;
    L5:
        if (Util.SDK_INT < 26) goto L9;
        if (r5 > 0) goto L41;
    L9:
        if (MimeTypes.AUDIO_MPEG.equals(r4) == false) goto L11;
    L40:
        return r5;
    L11:
        if (MimeTypes.AUDIO_AMR_NB.equals(r4) == true) goto L40;
        if (MimeTypes.AUDIO_AMR_WB.equals(r4) == true) goto L40;
        if (MimeTypes.AUDIO_AAC.equals(r4) == true) goto L40;
        if (MimeTypes.AUDIO_VORBIS.equals(r4) == true) goto L40;
        if (MimeTypes.AUDIO_OPUS.equals(r4) == true) goto L40;
        if (MimeTypes.AUDIO_RAW.equals(r4) == true) goto L40;
        if (MimeTypes.AUDIO_FLAC.equals(r4) == true) goto L40;
        if (MimeTypes.AUDIO_ALAW.equals(r4) == true) goto L40;
        if (MimeTypes.AUDIO_MLAW.equals(r4) == true) goto L40;
        if (MimeTypes.AUDIO_MSGSM.equals(r4) == true) goto L40;
        if (MimeTypes.AUDIO_AC3.equals(r4) == false) goto L35;
        int r0 = 6;
    L38:
        Log.m48w(TAG, "AssumedMaxChannelAdjustment: " + r3 + ", [" + r5 + " to " + r0 + "]");
        return r0;
    L35:
        if (MimeTypes.AUDIO_E_AC3.equals(r4) == false) goto L37;
        r0 = 16;
        goto L38
    L37:
        r0 = 30;
        goto L38
    }

    private static Point alignVideoSize(android.media.MediaCodecInfo.VideoCapabilities r5, int r6, int r7) {
        int r0 = r5.getWidthAlignment();
        int r1 = r5.getHeightAlignment();
        return new Point(Util.ceilDivide(r6, r0) * r0, Util.ceilDivide(r7, r1) * r1);
    }

    private static boolean areSizeAndRateSupported(android.media.MediaCodecInfo.VideoCapabilities r4, int r5, int r6, double r7) {
        Point r0 = alignVideoSize(r4, r5, r6);
        int r8 = r0.x;
        int r9 = r0.y;
        if (r7 == (-1.0d)) goto L10;
        if (r7 < 1.0d) goto L10;
        return r4.areSizeAndRateSupported(r8, r9, Math.floor(r7));
    L10:
        return r4.isSizeSupported(r8, r9);
    }

    private static android.media.MediaCodecInfo.CodecProfileLevel[] estimateLegacyVp9ProfileLevels(android.media.MediaCodecInfo.CodecCapabilities r5) {
        int r0 = 0;
        if (r5 == null) goto L8;
        android.media.MediaCodecInfo.VideoCapabilities r1 = r5.getVideoCapabilities();
        if (r1 == null) goto L8;
        r0 = ((Integer) r1.getBitrateRange().getUpper()).intValue();
    L8:
        if (r0 < 180000000) goto L11;
        int r2 = 1024;
    L38:
        android.media.MediaCodecInfo.CodecProfileLevel r3 = new android.media.MediaCodecInfo.CodecProfileLevel();
        r3.profile = 1;
        r3.level = r2;
        return new android.media.MediaCodecInfo.CodecProfileLevel[]{r3};
    L11:
        if (r0 < 120000000) goto L14;
        r2 = 512;
        goto L38
    L14:
        if (r0 < 60000000) goto L17;
        r2 = 256;
        goto L38
    L17:
        if (r0 < 30000000) goto L20;
        r2 = 128;
        goto L38
    L20:
        if (r0 < 18000000) goto L23;
        r2 = 64;
        goto L38
    L23:
        if (r0 < 12000000) goto L26;
        r2 = 32;
        goto L38
    L26:
        if (r0 < 7200000) goto L29;
        r2 = 16;
        goto L38
    L29:
        if (r0 < 3600000) goto L32;
        r2 = 8;
        goto L38
    L32:
        if (r0 < 1800000) goto L35;
        r2 = 4;
        goto L38
    L35:
        if (r0 < 800000) goto L37;
        r2 = 2;
        goto L38
    L37:
        r2 = 1;
        goto L38
    }

    private static int getMaxSupportedInstancesV23(android.media.MediaCodecInfo.CodecCapabilities r1) {
        return r1.getMaxSupportedInstances();
    }

    private static boolean isAdaptive(android.media.MediaCodecInfo.CodecCapabilities r1) {
        return r1.isFeatureSupported("adaptive-playback");
    }

    private boolean isCodecProfileAndLevelSupported(Format r11, boolean r12) {
        Pair<Integer, Integer> r0 = MediaCodecUtil.getCodecProfileAndLevel(r11);
        if (r11.sampleMimeType != null) goto L5;
    L12:
        if (r0 != null) goto L14;
        return true;
    L14:
        int r1 = ((Integer) r0.first).intValue();
        int r4 = ((Integer) r0.second).intValue();
        if (MimeTypes.VIDEO_DOLBY_VISION.equals(r11.sampleMimeType) == false) goto L23;
        if (MimeTypes.VIDEO_H264.equals(this.mimeType) == false) goto L20;
        r1 = 8;
        r4 = 0;
        goto L23
    L20:
        if (MimeTypes.VIDEO_H265.equals(this.mimeType) == false) goto L23;
        r1 = 2;
        r4 = 0;
    L23:
        if (this.isVideo == false) goto L25;
    L27:
        android.media.MediaCodecInfo.CodecProfileLevel[] r2 = getProfileLevels();
        if (Util.SDK_INT <= 23) goto L30;
    L34:
        int r5 = r2.length;
        int r7 = 0;
    L35:
        if (r7 >= r5) goto L45;
        android.media.MediaCodecInfo.CodecProfileLevel r8 = r2[r7];
        if (r8.profile != r1) goto L44;
        if (r8.level >= r4) goto L42;
        if (r12 == true) goto L44;
    L42:
        if (needsProfileExcludedWorkaround(this.mimeType, r1) == true) goto L44;
        return true;
    L44:
        r7 = r7 + 1;
        goto L35
    L45:
        logNoSupport("codec.profileLevel, " + r11.codecs + ", " + this.codecMimeType);
        return false;
    L30:
        if (MimeTypes.VIDEO_VP9.equals(this.mimeType) == false) goto L34;
        if (r2.length != 0) goto L34;
        r2 = estimateLegacyVp9ProfileLevels(this.capabilities);
        goto L34
    L25:
        if (r1 == 42) goto L27;
        return true;
    L5:
        if (r11.sampleMimeType.equals(MimeTypes.VIDEO_MV_HEVC) == false) goto L12;
        String r3 = MimeTypes.normalizeMimeType(this.codecMimeType);
        if (r3.equals(MimeTypes.VIDEO_MV_HEVC) == false) goto L10;
        return true;
    L10:
        if (r3.equals(MimeTypes.VIDEO_H265) == false) goto L12;
        r0 = MediaCodecUtil.getHevcBaseLayerCodecProfileAndLevel(r11);
        goto L12
    }

    private static boolean isDetachedSurfaceSupported(android.media.MediaCodecInfo.CodecCapabilities r2) {
        if (Util.SDK_INT < 35) goto L8;
        if (r2 == null) goto L8;
        if (r2.isFeatureSupported("detached-surface") == false) goto L8;
        return true;
    L8:
        return false;
    }

    private boolean isSampleMimeTypeSupported(Format r3) {
        if (this.mimeType.equals(r3.sampleMimeType) == false) goto L5;
    L8:
        return true;
    L5:
        if (this.mimeType.equals(MediaCodecUtil.getAlternativeCodecMimeType(r3)) == true) goto L8;
        return false;
    }

    private static boolean isSecure(android.media.MediaCodecInfo.CodecCapabilities r1) {
        return r1.isFeatureSupported("secure-playback");
    }

    private static boolean isTunneling(android.media.MediaCodecInfo.CodecCapabilities r1) {
        return r1.isFeatureSupported("tunneled-playback");
    }

    private void logAssumedSupport(String r4) {
        Log.m42d(TAG, "AssumedSupport [" + r4 + "] [" + this.name + ", " + this.mimeType + "] [" + Util.DEVICE_DEBUG_INFO + "]");
    }

    private void logNoSupport(String r4) {
        Log.m42d(TAG, "NoSupport [" + r4 + "] [" + this.name + ", " + this.mimeType + "] [" + Util.DEVICE_DEBUG_INFO + "]");
    }

    private static boolean needsAdaptationFlushWorkaround(String r1) {
        return MimeTypes.AUDIO_OPUS.equals(r1);
    }

    private static boolean needsAdaptationReconfigureWorkaround(String r2) {
        if (Util.MODEL.startsWith("SM-T230") == true) goto L5;
    L7:
        return false;
    L5:
        if ("OMX.MARVELL.VIDEO.HW.CODA7542DECODER".equals(r2) == false) goto L7;
        return true;
    }

    private static boolean needsDisableAdaptationWorkaround(String r2) {
        if (Util.SDK_INT <= 22) goto L5;
    L13:
        return false;
    L5:
        if ("ODROID-XU3".equals(Util.MODEL) == true) goto L9;
        if ("Nexus 10".equals(Util.MODEL) == false) goto L13;
    L9:
        if ("OMX.Exynos.AVC.Decoder".equals(r2) == false) goto L11;
    L12:
        return true;
    L11:
        if ("OMX.Exynos.AVC.Decoder.secure".equals(r2) == false) goto L13;
        goto L12
    }

    private static boolean needsProfileExcludedWorkaround(String r2, int r3) {
        if (MimeTypes.VIDEO_H265.equals(r2) == true) goto L5;
    L11:
        return false;
    L5:
        if (2 != r3) goto L11;
        if ("sailfish".equals(Util.DEVICE) == false) goto L9;
    L10:
        return true;
    L9:
        if ("marlin".equals(Util.DEVICE) == false) goto L11;
        goto L10
    }

    private static boolean needsRotatedVerticalResolutionWorkaround(String r2) {
        if ("OMX.MTK.VIDEO.DECODER.HEVC".equals(r2) == true) goto L5;
        return true;
    L5:
        if ("mcv5a".equals(Util.DEVICE) == false) goto L10;
        return false;
    L10:
        return true;
    }

    public static MediaCodecInfo newInstance(String r12, String r13, String r14, android.media.MediaCodecInfo.CodecCapabilities r15, boolean r16, boolean r17, boolean r18, boolean r19, boolean r20) {
        if (r19 == true) goto L10;
        if (r15 == null) goto L10;
        if (isAdaptive(r15) == false) goto L10;
        if (needsDisableAdaptationWorkaround(r12) == true) goto L10;
        boolean r8 = true;
    L11:
        if (r15 != null) goto L13;
    L15:
        boolean r9 = false;
    L16:
        if (r20 == true) goto L22;
        if (r15 != null) goto L19;
    L21:
        boolean r10 = false;
    L24:
        return new MediaCodecInfo(r12, r13, r14, r15, r16, r17, r18, r8, r9, r10, isDetachedSurfaceSupported(r15));
    L19:
        if (isSecure(r15) == false) goto L21;
    L22:
        r10 = true;
        goto L24
    L13:
        if (isTunneling(r15) == false) goto L15;
        r9 = true;
    L10:
        r8 = false;
        goto L11
    }

    public Point alignVideoSizeV21(int r3, int r4) {
        if (this.capabilities != null) goto L5;
        return null;
    L5:
        android.media.MediaCodecInfo.VideoCapabilities r0 = this.capabilities.getVideoCapabilities();
        if (r0 != null) goto L9;
        return null;
    L9:
        return alignVideoSize(r0, r3, r4);
    }

    public DecoderReuseEvaluation canReuseCodec(Format r14, Format r15) {
        int r0 = 0;
        if (Util.areEqual(r14.sampleMimeType, r15.sampleMimeType) == true) goto L6;
        r0 = 0 | 8;
    L6:
        if (this.isVideo == true) goto L8;
        Format r9 = r14;
        Format r10 = r15;
        if (r9.channelCount == r10.channelCount) goto L41;
        r0 = r0 | 4096;
    L41:
        if (r9.sampleRate == r10.sampleRate) goto L44;
        r0 = r0 | 8192;
    L44:
        if (r9.pcmEncoding == r10.pcmEncoding) goto L46;
        r0 = r0 | 16384;
    L46:
        if (r0 != 0) goto L58;
        if (MimeTypes.AUDIO_AAC.equals(this.mimeType) == false) goto L58;
        Pair<Integer, Integer> r16 = MediaCodecUtil.getCodecProfileAndLevel(r9);
        Pair<Integer, Integer> r17 = MediaCodecUtil.getCodecProfileAndLevel(r10);
        if (r16 == null) goto L58;
        if (r17 == null) goto L58;
        int r1 = ((Integer) r16.first).intValue();
        int r2 = ((Integer) r17.first).intValue();
        if (r1 != 42) goto L58;
        if (r2 != 42) goto L58;
        return new DecoderReuseEvaluation(this.name, r9, r10, 3, 0);
    L58:
        if (r9.initializationDataEquals(r10) == true) goto L61;
        r0 = r0 | 32;
    L61:
        if (needsAdaptationFlushWorkaround(this.mimeType) == false) goto L63;
        r0 = r0 | 2;
    L63:
        if (r0 == 0) goto L65;
        int r12 = r0;
    L68:
        return new DecoderReuseEvaluation(this.name, r9, r10, 0, r12);
    L65:
        return new DecoderReuseEvaluation(this.name, r9, r10, 1, 0);
    L8:
        if (r14.rotationDegrees == r15.rotationDegrees) goto L11;
        r0 = r0 | 1024;
    L11:
        if (this.adaptive == true) goto L18;
        if (r14.width == r15.width) goto L15;
    L16:
        r0 = r0 | 512;
        goto L18
    L15:
        if (r14.height != r15.height) goto L16;
    L18:
        if (ColorInfo.isEquivalentToAssumedSdrDefault(r14.colorInfo) == false) goto L22;
        if (ColorInfo.isEquivalentToAssumedSdrDefault(r15.colorInfo) == false) goto L22;
    L25:
        if (needsAdaptationReconfigureWorkaround(this.name) == true) goto L27;
    L29:
        if (r0 != 0) goto L36;
        String r3 = this.name;
        if (r14.initializationDataEquals(r15) == false) goto L33;
        int r5 = 3;
    L35:
        return new DecoderReuseEvaluation(r3, r14, r15, r5, 0);
    L33:
        r5 = 2;
        goto L35
    L36:
        r9 = r14;
        r10 = r15;
        r12 = r0;
        goto L68
    L27:
        if (r14.initializationDataEquals(r15) == true) goto L29;
        r0 = r0 | 2;
    L22:
        if (Util.areEqual(r14.colorInfo, r15.colorInfo) == true) goto L25;
        r0 = r0 | 2048;
        goto L25
    }

    public int getMaxSupportedInstances() {
        if (Util.SDK_INT >= 23) goto L5;
        return -1;
    L5:
        if (this.capabilities != null) goto L8;
        return -1;
    L8:
        return getMaxSupportedInstancesV23(this.capabilities);
    }

    public android.media.MediaCodecInfo.CodecProfileLevel[] getProfileLevels() {
        if (this.capabilities == null) goto L9;
        if (this.capabilities.profileLevels == null) goto L9;
        return this.capabilities.profileLevels;
    L9:
        return new android.media.MediaCodecInfo.CodecProfileLevel[0];
    }

    public boolean isAudioChannelCountSupportedV21(int r6) {
        if (this.capabilities != null) goto L6;
        logNoSupport("channelCount.caps");
        return false;
    L6:
        android.media.MediaCodecInfo.AudioCapabilities r0 = this.capabilities.getAudioCapabilities();
        if (r0 != null) goto L11;
        logNoSupport("channelCount.aCaps");
        return false;
    L11:
        if (adjustMaxInputChannelCount(this.name, this.mimeType, r0.getMaxInputChannelCount()) >= r6) goto L14;
        logNoSupport("channelCount.support, " + r6);
        return false;
    L14:
        return true;
    }

    public boolean isAudioSampleRateSupportedV21(int r5) {
        if (this.capabilities != null) goto L6;
        logNoSupport("sampleRate.caps");
        return false;
    L6:
        android.media.MediaCodecInfo.AudioCapabilities r0 = this.capabilities.getAudioCapabilities();
        if (r0 != null) goto L11;
        logNoSupport("sampleRate.aCaps");
        return false;
    L11:
        if (r0.isSampleRateSupported(r5) == true) goto L14;
        logNoSupport("sampleRate.support, " + r5);
        return false;
    L14:
        return true;
    }

    public boolean isFormatFunctionallySupported(Format r3) {
        if (isSampleMimeTypeSupported(r3) == true) goto L5;
        return false;
    L5:
        if (isCodecProfileAndLevelSupported(r3, false) == false) goto L10;
        return true;
    L10:
        return false;
    }

    public boolean isFormatSupported(Format r5) throws MediaCodecUtil.DecoderQueryException {
        if (isSampleMimeTypeSupported(r5) == true) goto L6;
        return false;
    L6:
        if (isCodecProfileAndLevelSupported(r5, true) == true) goto L9;
        return false;
    L9:
        if (this.isVideo == false) goto L19;
        if (r5.width > 0) goto L13;
    L17:
        return true;
    L13:
        if (r5.height <= 0) goto L17;
        return isVideoSizeAndRateSupportedV21(r5.width, r5.height, r5.frameRate);
    L19:
        if (r5.sampleRate == (-1)) goto L23;
        if (isAudioSampleRateSupportedV21(r5.sampleRate) == true) goto L23;
        return false;
    L23:
        if (r5.channelCount == (-1)) goto L29;
        if (isAudioChannelCountSupportedV21(r5.channelCount) == true) goto L29;
        return false;
    L29:
        return true;
    }

    public boolean isHdr10PlusOutOfBandMetadataSupported() {
        if (Util.SDK_INT >= 29) goto L5;
    L13:
        return false;
    L5:
        if (MimeTypes.VIDEO_VP9.equals(this.mimeType) == false) goto L13;
        android.media.MediaCodecInfo.CodecProfileLevel[] r0 = getProfileLevels();
        int r1 = r0.length;
        int r3 = 0;
    L7:
        if (r3 >= r1) goto L13;
        if (r0[r3].profile == 16384) goto L10;
        r3 = r3 + 1;
        goto L7
    L10:
        return true;
    }

    public boolean isSeamlessAdaptationSupported(Format r4) {
        if (this.isVideo == true) goto L5;
        Pair<Integer, Integer> r0 = MediaCodecUtil.getCodecProfileAndLevel(r4);
        if (r0 != null) goto L9;
    L11:
        return false;
    L9:
        if (((Integer) r0.first).intValue() != 42) goto L11;
        return true;
    L5:
        return this.adaptive;
    }

    public boolean isVideoSizeAndRateSupportedV21(int r8, int r9, double r10) {
        if (this.capabilities != null) goto L6;
        logNoSupport("sizeAndRate.caps");
        return false;
    L6:
        android.media.MediaCodecInfo.VideoCapabilities r0 = this.capabilities.getVideoCapabilities();
        if (r0 != null) goto L11;
        logNoSupport("sizeAndRate.vCaps");
        return false;
    L11:
        if (Util.SDK_INT < 29) goto L19;
        int r2 = MediaCodecPerformancePointCoverageProvider.areResolutionAndFrameRateCovered(r0, r8, r9, r10);
        if (r2 != 2) goto L15;
        return true;
    L15:
        if (r2 != 1) goto L19;
        logNoSupport("sizeAndRate.cover, " + r8 + "x" + r9 + "@" + r10);
        return false;
    L19:
        if (areSizeAndRateSupported(r0, r8, r9, r10) == true) goto L29;
        if (r8 < r9) goto L22;
    L27:
        logNoSupport("sizeAndRate.support, " + r8 + "x" + r9 + "@" + r10);
        return false;
    L22:
        if (needsRotatedVerticalResolutionWorkaround(this.name) == false) goto L27;
        if (areSizeAndRateSupported(r0, r9, r8, r10) == false) goto L27;
        logAssumedSupport("sizeAndRate.rotated, " + r8 + "x" + r9 + "@" + r10);
    L29:
        return true;
    }

    public String toString() {
        return this.name;
    }
}
