package androidx.media3.exoplayer.mediacodec;

import android.media.MediaCodecList;
import android.text.TextUtils;
import android.util.Pair;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.CodecSpecificDataUtil;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.container.NalUnitUtil;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class MediaCodecUtil {
    private static final String TAG = "MediaCodecUtil";
    private static final HashMap<CodecKey, List<MediaCodecInfo>> decoderInfosCache = null;
    private static int maxH264DecodableFrameSize;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.mediacodec.MediaCodecUtil$1 */
    static /* synthetic */ class C03101 {
    }

    private static final class CodecKey {
        public final String mimeType;
        public final boolean secure;
        public final boolean tunneling;

        public CodecKey(String r1, boolean r2, boolean r3) {
            this.mimeType = r1;
            this.secure = r2;
            this.tunneling = r3;
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if (r6 != null) goto L8;
        L19:
            return false;
        L8:
            if (r6.getClass() != CodecKey.class) goto L19;
            CodecKey r2 = (CodecKey) r6;
            if (TextUtils.equals(this.mimeType, r2.mimeType) == false) goto L18;
            if (this.secure != r2.secure) goto L18;
            if (this.tunneling != r2.tunneling) goto L18;
            return true;
        L18:
            return false;
        }

        public int hashCode() {
            int r1 = ((1 * 31) + this.mimeType.hashCode()) * 31;
            int r4 = 1231;
            if (this.secure == false) goto L5;
            int r3 = 1231;
        L6:
            int r2 = (r1 + r3) * 31;
            if (this.tunneling == true) goto L11;
            r4 = 1237;
        L11:
            return r2 + r4;
        L5:
            r3 = 1237;
            goto L6
        }
    }

    public static class DecoderQueryException extends Exception {
        private DecoderQueryException(Throwable r2) {
            super("Failed to query underlying media codecs", r2);
        }

        /* synthetic */ DecoderQueryException(Throwable r1, C03101 r2) {
            this(r1);
        }
    }

    private interface MediaCodecListCompat {
        int getCodecCount();

        android.media.MediaCodecInfo getCodecInfoAt(int r1);

        boolean isFeatureRequired(String r1, String r2, android.media.MediaCodecInfo.CodecCapabilities r3);

        boolean isFeatureSupported(String r1, String r2, android.media.MediaCodecInfo.CodecCapabilities r3);

        boolean secureDecodersExplicit();
    }

    private static final class MediaCodecListCompatV16 implements MediaCodecListCompat {
        private MediaCodecListCompatV16() {
        }

        /* synthetic */ MediaCodecListCompatV16(C03101 r1) {
            this();
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public int getCodecCount() {
            return MediaCodecList.getCodecCount();
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public android.media.MediaCodecInfo getCodecInfoAt(int r2) {
            return MediaCodecList.getCodecInfoAt(r2);
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public boolean isFeatureRequired(String r2, String r3, android.media.MediaCodecInfo.CodecCapabilities r4) {
            return false;
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public boolean isFeatureSupported(String r2, String r3, android.media.MediaCodecInfo.CodecCapabilities r4) {
            if ("secure-playback".equals(r2) == true) goto L5;
        L7:
            return false;
        L5:
            if (MimeTypes.VIDEO_H264.equals(r3) == false) goto L7;
            return true;
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public boolean secureDecodersExplicit() {
            return false;
        }
    }

    private static final class MediaCodecListCompatV21 implements MediaCodecListCompat {
        private final int codecKind;
        private android.media.MediaCodecInfo[] mediaCodecInfos;

        public MediaCodecListCompatV21(boolean r2, boolean r3) {
            if (r2 == true) goto L7;
            if (r3 == true) goto L7;
            int r0 = 0;
        L8:
            this.codecKind = r0;
            return;
        L7:
            r0 = 1;
            goto L8
        }

        @EnsuresNonNull({"mediaCodecInfos"})
        private void ensureMediaCodecInfosInitialized() {
            if (this.mediaCodecInfos != null) goto L6;
            this.mediaCodecInfos = new MediaCodecList(this.codecKind).getCodecInfos();
            return;
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public int getCodecCount() {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos.length;
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public android.media.MediaCodecInfo getCodecInfoAt(int r2) {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos[r2];
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public boolean isFeatureRequired(String r2, String r3, android.media.MediaCodecInfo.CodecCapabilities r4) {
            return r4.isFeatureRequired(r2);
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public boolean isFeatureSupported(String r2, String r3, android.media.MediaCodecInfo.CodecCapabilities r4) {
            return r4.isFeatureSupported(r2);
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public boolean secureDecodersExplicit() {
            return true;
        }
    }

    private interface ScoreProvider<T> {
        int getScore(T r1);
    }

    static {
        decoderInfosCache = new HashMap();
        maxH264DecodableFrameSize = -1;
    }

    private MediaCodecUtil() {
    }

    private static void applyWorkarounds(String r12, List<MediaCodecInfo> r13) {
        if (MimeTypes.AUDIO_RAW.equals(r12) == false) goto L15;
        if (Util.SDK_INT < 26) goto L7;
    L13:
        sortByScore(r13, new MediaCodecUtil$$ExternalSyntheticLambda1());
        goto L15
    L7:
        if (Util.DEVICE.equals("R9") == false) goto L13;
        if (r13.size() != 1) goto L13;
        if (r13.get(0).name.equals("OMX.MTK.AUDIO.DECODER.RAW") == false) goto L13;
        r13.add(MediaCodecInfo.newInstance("OMX.google.raw.decoder", MimeTypes.AUDIO_RAW, MimeTypes.AUDIO_RAW, null, false, true, false, false, false));
    L15:
        if (Util.SDK_INT < 32) goto L17;
        return;
    L17:
        if (r13.size() > 1) goto L19;
        return;
    L19:
        if ("OMX.qti.audio.decoder.flac".equals(r13.get(0).name) == false) goto L24;
        r13.add(r13.remove(0));
        return;
    }

    private static int avcLevelToMaxFrameSize(int r1) {
        switch(r1) {
            case 1: goto L25;
            case 2: goto L25;
            case 8: goto L23;
            case 16: goto L23;
            case 32: goto L23;
            case 64: goto L21;
            case 128: goto L19;
            case 256: goto L19;
            case 512: goto L17;
            case 1024: goto L15;
            case 2048: goto L13;
            case 4096: goto L13;
            case 8192: goto L11;
            case 16384: goto L9;
            case 32768: goto L7;
            case 65536: goto L7;
            case 131072: goto L5;
            case 262144: goto L5;
            case 524288: goto L5;
            default: goto L3;
        };
    L3:
        return -1;
    L5:
        return 35651584;
    L7:
        return 9437184;
    L9:
        return 5652480;
    L11:
        return 2228224;
    L13:
        return 2097152;
    L15:
        return 1310720;
    L17:
        return 921600;
    L19:
        return 414720;
    L21:
        return 202752;
    L23:
        return 101376;
    L25:
        return 25344;
    }

    public static synchronized void clearDecoderInfoCache() {
        monitor-enter(MediaCodecUtil.class);
        decoderInfosCache.clear();     // Catch: Throwable -> L7
        monitor-exit(MediaCodecUtil.class);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public static String getAlternativeCodecMimeType(Format r4) {
        if (MimeTypes.AUDIO_E_AC3_JOC.equals(r4.sampleMimeType) == false) goto L7;
        return MimeTypes.AUDIO_E_AC3;
    L7:
        if (MimeTypes.VIDEO_DOLBY_VISION.equals(r4.sampleMimeType) == false) goto L25;
        Pair<Integer, Integer> r0 = getCodecProfileAndLevel(r4);
        if (r0 == null) goto L25;
        int r2 = ((Integer) r0.first).intValue();
        if (r2 != 16) goto L13;
    L23:
        return MimeTypes.VIDEO_H265;
    L13:
        if (r2 == 256) goto L23;
        if (r2 != 512) goto L20;
        return MimeTypes.VIDEO_H264;
    L20:
        if (r2 != 1024) goto L25;
        return MimeTypes.VIDEO_AV1;
    L25:
        if (MimeTypes.VIDEO_MV_HEVC.equals(r4.sampleMimeType) == false) goto L27;
        return MimeTypes.VIDEO_H265;
    L27:
        return null;
    }

    public static List<MediaCodecInfo> getAlternativeDecoderInfos(MediaCodecSelector r2, Format r3, boolean r4, boolean r5) throws DecoderQueryException {
        String r0 = getAlternativeCodecMimeType(r3);
        if (r0 != null) goto L7;
        return ImmutableList.m212of();
    L7:
        return r2.getDecoderInfos(r0, r4, r5);
    }

    private static String getCodecMimeType(android.media.MediaCodecInfo r5, String r6, String r7) {
        String[] r0 = r5.getSupportedTypes();
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L9;
        String r3 = r0[r2];
        if (r3.equalsIgnoreCase(r7) == true) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        return r3;
    L9:
        if (r7.equals(MimeTypes.VIDEO_DOLBY_VISION) == false) goto L21;
        if ("OMX.MS.HEVCDV.Decoder".equals(r6) == false) goto L15;
        return "video/hevcdv";
    L15:
        if ("OMX.RTK.video.decoder".equals(r6) == false) goto L17;
        return "video/dv_hevc";
    L17:
        if ("OMX.realtek.video.decoder.tunneled".equals(r6) == true) goto L48;
        return null;
    L48:
        return "video/dv_hevc";
    L21:
        if (r7.equals(MimeTypes.VIDEO_MV_HEVC) == false) goto L27;
        if ("c2.qti.mvhevc.decoder".equals(r6) == false) goto L49;
        return "video/x-mvhevc";
    L49:
        return null;
    L27:
        if (r7.equals(MimeTypes.AUDIO_ALAC) == false) goto L33;
        if ("OMX.lge.alac.decoder".equals(r6) == false) goto L33;
        return "audio/x-lg-alac";
    L33:
        if (r7.equals(MimeTypes.AUDIO_FLAC) == false) goto L39;
        if ("OMX.lge.flac.decoder".equals(r6) == false) goto L39;
        return "audio/x-lg-flac";
    L39:
        if (r7.equals(MimeTypes.AUDIO_AC3) == true) goto L41;
        return null;
    L41:
        if ("OMX.lge.ac3.decoder".equals(r6) == false) goto L51;
        return "audio/lg-ac3";
    L51:
        return null;
    }

    @Deprecated
    public static Pair<Integer, Integer> getCodecProfileAndLevel(Format r1) {
        return CodecSpecificDataUtil.getCodecProfileAndLevel(r1);
    }

    public static MediaCodecInfo getDecoderInfo(String r2, boolean r3, boolean r4) throws DecoderQueryException {
        List<MediaCodecInfo> r0 = getDecoderInfos(r2, r3, r4);
        if (r0.isEmpty() == false) goto L6;
        return null;
    L6:
        return r0.get(0);
    }

    public static synchronized List<MediaCodecInfo> getDecoderInfos(String r8, boolean r9, boolean r10) throws DecoderQueryException {
        monitor-enter(MediaCodecUtil.class);
        CodecKey r1 = new CodecKey(r8, r9, r10);     // Catch: Throwable -> L20
        List<MediaCodecInfo> r2 = decoderInfosCache.get(r1);     // Catch: Throwable -> L20
        if (r2 == null) goto L8;
        monitor-exit(MediaCodecUtil.class);
        return r2;
    L8:
        ArrayList<MediaCodecInfo> r4 = getDecoderInfosInternal(r1, new MediaCodecListCompatV21(r9, r10));     // Catch: Throwable -> L20
        if (r9 == true) goto L11;
    L17:
        applyWorkarounds(r8, r4);     // Catch: Throwable -> L20
        ImmutableList r5 = ImmutableList.copyOf(r4);     // Catch: Throwable -> L20
        decoderInfosCache.put(r1, r5);     // Catch: Throwable -> L20
        monitor-exit(MediaCodecUtil.class);
        return r5;
    L11:
        if (r4.isEmpty() == false) goto L17;
        if (Util.SDK_INT > 23) goto L17;
        r4 = getDecoderInfosInternal(r1, new MediaCodecListCompatV16(null));     // Catch: Throwable -> L20
        if (r4.isEmpty() == true) goto L17;
        Log.m48w(TAG, "MediaCodecList API didn't list secure decoder for: " + r8 + ". Assuming: " + r4.get(0).name);     // Catch: Throwable -> L20
    L20:
        th = move-exception;
        throw th;
    }

    private static ArrayList<MediaCodecInfo> getDecoderInfosInternal(CodecKey r23, MediaCodecListCompat r24) throws DecoderQueryException {
        CodecKey r1 = r23;
        ArrayList<MediaCodecInfo> r0 = new ArrayList();     // Catch: Exception -> L74
        String r2 = r1.mimeType;     // Catch: Exception -> L74
        int r3 = r24.getCodecCount();     // Catch: Exception -> L74
        boolean r6 = r24.secureDecodersExplicit();     // Catch: Exception -> L74
        int r8 = 0;
    L4:
        if (r8 >= r3) goto L73;
        android.media.MediaCodecInfo r4 = r24.getCodecInfoAt(r8);     // Catch: Exception -> L74
        if (isAlias(r4) == false) goto L8;
        boolean r21 = r6;
        int r22 = r8;
    L70:
        r8 = r22 + 1;     // Catch: Exception -> L74
        r1 = r23;
        r6 = r21;
        goto L4
    L8:
        String r5 = r4.getName();     // Catch: Exception -> L74
        if (isCodecUsableDecoder(r4, r5, r6, r2) == true) goto L11;
        r21 = r6;
        r22 = r8;
        goto L70
    L11:
        String r7 = getCodecMimeType(r4, r5, r2);     // Catch: Exception -> L74
        if (r7 != null) goto L89;
        r21 = r6;
        r22 = r8;
        goto L70
    L89:
        android.media.MediaCodecInfo.CodecCapabilities r9 = r4.getCapabilitiesForType(r7);     // Catch: Exception -> L62
        boolean r12 = r24.isFeatureSupported("tunneled-playback", r7, r9);     // Catch: Exception -> L62
        boolean r13 = r24.isFeatureRequired("tunneled-playback", r7, r9);     // Catch: Exception -> L62
        if (r1.tunneling == true) goto L19;
        if (r13 == false) goto L19;
    L21:
        r21 = r6;
        r22 = r8;
    L19:
        if (r1.tunneling == false) goto L22;
        if (r12 == false) goto L21;
    L22:
        boolean r14 = r24.isFeatureSupported("secure-playback", r7, r9);     // Catch: Exception -> L62
        boolean r15 = r24.isFeatureRequired("secure-playback", r7, r9);     // Catch: Exception -> L62
        if (r1.secure == true) goto L26;
        if (r15 == false) goto L26;
    L28:
        r21 = r6;
        r22 = r8;
    L26:
        if (r1.secure == false) goto L29;
        if (r14 == false) goto L28;
    L29:
        boolean r16 = isHardwareAccelerated(r4, r2);     // Catch: Exception -> L62
        boolean r17 = isSoftwareOnly(r4, r2);     // Catch: Exception -> L62
        boolean r19 = isVendor(r4);     // Catch: Exception -> L62
        if (r6 == false) goto L37;
        android.media.MediaCodecInfo.CodecCapabilities r20 = r9;
        if (r1.secure == r14) goto L42;
    L38:
        if (r6 == false) goto L81;
    L49:
        r21 = r6;
        r22 = r8;
        String r10 = r7;
        android.media.MediaCodecInfo.CodecCapabilities r11 = r20;
        if (r21 == true) goto L70;
        if (r14 == false) goto L70;
        String r18 = r5;
        r0.add(MediaCodecInfo.newInstance(r5 + ".secure", r2, r10, r11, r16, r17, r19, false, true));     // Catch: Exception -> L56
        return r0;
    L56:
        e = e;
    L66:
        if (Util.SDK_INT > 23) goto L71;
        if (r0.isEmpty() == true) goto L71;
        Log.m44e(TAG, "Skipping codec " + r18 + " (failed to query capabilities)");     // Catch: Exception -> L74
    L71:
        Log.m44e(TAG, "Failed to query codec " + r18 + " (" + r10 + ")");     // Catch: Exception -> L74
        throw e;     // Catch: Exception -> L74
    L58:
        e = e;
        r18 = r5;
        goto L66
    L81:
        if (r1.secure == true) goto L49;
    L47:
        e = e;
        r21 = r6;
        r22 = r8;
        r10 = r7;
        r18 = r5;
    L42:
        r21 = r6;
        r22 = r8;
        r10 = r7;
        r0.add(MediaCodecInfo.newInstance(r5, r2, r10, r20, r16, r17, r19, false, false));     // Catch: Exception -> L45
    L45:
        e = e;
        r18 = r5;
    L35:
        e = e;
        r21 = r6;
        r22 = r8;
        r18 = r5;
        r10 = r7;
        goto L66
    L37:
        r20 = r9;
    L62:
        e = e;
        r21 = r6;
        r22 = r8;
        r18 = r5;
        r10 = r7;
        goto L66
    L73:
        return r0;
    L74:
        e = move-exception;
        throw new DecoderQueryException(e, null);
    }

    @RequiresNonNull({"#2.sampleMimeType"})
    public static List<MediaCodecInfo> getDecoderInfosSoftMatch(MediaCodecSelector r3, Format r4, boolean r5, boolean r6) throws DecoderQueryException {
        List<MediaCodecInfo> r0 = r3.getDecoderInfos(r4.sampleMimeType, r5, r6);
        List<MediaCodecInfo> r1 = getAlternativeDecoderInfos(r3, r4, r5, r6);
        return ImmutableList.builder().addAll(r0).addAll(r1).build();
    }

    public static List<MediaCodecInfo> getDecoderInfosSortedByFormatSupport(List<MediaCodecInfo> r1, final Format r2) {
        ArrayList r0 = new ArrayList(r1);
        sortByScore(r0, new MediaCodecUtil$$ExternalSyntheticLambda2(r2));
        return r0;
    }

    public static MediaCodecInfo getDecryptOnlyDecoderInfo() throws DecoderQueryException {
        return getDecoderInfo(MimeTypes.AUDIO_RAW, false, false);
    }

    public static Pair<Integer, Integer> getHevcBaseLayerCodecProfileAndLevel(Format r3) {
        String r0 = NalUnitUtil.getH265BaseLayerCodecsString(r3.initializationData);
        if (r0 != null) goto L7;
        return null;
    L7:
        return CodecSpecificDataUtil.getHevcProfileAndLevel(r0, Util.split(r0.trim(), "\\."), r3.colorInfo);
    }

    private static boolean isAlias(android.media.MediaCodecInfo r2) {
        if (Util.SDK_INT >= 29) goto L5;
    L7:
        return false;
    L5:
        if (isAliasV29(r2) == false) goto L7;
        return true;
    }

    private static boolean isAliasV29(android.media.MediaCodecInfo r1) {
        return r1.isAlias();
    }

    private static boolean isCodecUsableDecoder(android.media.MediaCodecInfo r3, String r4, boolean r5, String r6) {
        if (r3.isEncoder() == true) goto L42;
        if (r5 == true) goto L9;
        if (r4.endsWith(".secure") == true) goto L42;
    L9:
        if (Util.SDK_INT >= 24) goto L34;
        if ("OMX.SEC.aac.dec".equals(r4) == true) goto L15;
        if ("OMX.Exynos.AAC.Decoder".equals(r4) == false) goto L34;
    L15:
        if ("samsung".equals(Util.MANUFACTURER) == false) goto L34;
        if (Util.DEVICE.startsWith("zeroflte") == false) goto L19;
    L32:
        return false;
    L19:
        if (Util.DEVICE.startsWith("zerolte") == true) goto L32;
        if (Util.DEVICE.startsWith("zenlte") == true) goto L32;
        if ("SC-05G".equals(Util.DEVICE) == true) goto L32;
        if ("marinelteatt".equals(Util.DEVICE) == true) goto L32;
        if ("404SC".equals(Util.DEVICE) == true) goto L32;
        if ("SC-04G".equals(Util.DEVICE) == true) goto L32;
        if ("SCV31".equals(Util.DEVICE) == true) goto L32;
    L34:
        if (Util.SDK_INT <= 23) goto L36;
        return true;
    L36:
        if (MimeTypes.AUDIO_E_AC3_JOC.equals(r6) == true) goto L38;
        return true;
    L38:
        if ("OMX.MTK.AUDIO.DECODER.DSPAC3".equals(r4) == false) goto L44;
        return false;
    L44:
        return true;
    L42:
        return false;
    }

    private static boolean isHardwareAccelerated(android.media.MediaCodecInfo r2, String r3) {
        if (Util.SDK_INT < 29) goto L7;
        return isHardwareAcceleratedV29(r2);
    L7:
        return !isSoftwareOnly(r2, r3);
    }

    private static boolean isHardwareAcceleratedV29(android.media.MediaCodecInfo r1) {
        return r1.isHardwareAccelerated();
    }

    private static boolean isSoftwareOnly(android.media.MediaCodecInfo r4, String r5) {
        if (Util.SDK_INT < 29) goto L7;
        return isSoftwareOnlyV29(r4);
    L7:
        if (MimeTypes.isAudio(r5) == false) goto L9;
        return true;
    L9:
        String r0 = Ascii.toLowerCase(r4.getName());
        if (r0.startsWith("arc.") == false) goto L13;
        return false;
    L13:
        if (r0.startsWith("omx.google.") == false) goto L15;
        return true;
    L15:
        if (r0.startsWith("omx.ffmpeg.") == false) goto L17;
        return true;
    L17:
        if (r0.startsWith("omx.sec.") == false) goto L21;
        if (r0.contains(".sw.") == false) goto L21;
        return true;
    L21:
        if (r0.equals("omx.qcom.video.decoder.hevcswvdec") == false) goto L23;
        return true;
    L23:
        if (r0.startsWith("c2.android.") == false) goto L25;
        return true;
    L25:
        if (r0.startsWith("c2.google.") == false) goto L27;
        return true;
    L27:
        if (r0.startsWith("omx.") == true) goto L33;
        if (r0.startsWith("c2.") == true) goto L33;
        return true;
    L33:
        return false;
    }

    private static boolean isSoftwareOnlyV29(android.media.MediaCodecInfo r1) {
        return r1.isSoftwareOnly();
    }

    private static boolean isVendor(android.media.MediaCodecInfo r2) {
        if (Util.SDK_INT >= 29) goto L5;
        String r0 = Ascii.toLowerCase(r2.getName());
        if (r0.startsWith("omx.google.") == false) goto L9;
    L13:
        return false;
    L9:
        if (r0.startsWith("c2.android.") == true) goto L13;
        if (r0.startsWith("c2.google.") == true) goto L13;
        return true;
    L5:
        return isVendorV29(r2);
    }

    private static boolean isVendorV29(android.media.MediaCodecInfo r1) {
        return r1.isVendor();
    }

    static /* synthetic */ int lambda$applyWorkarounds$1(MediaCodecInfo r3) {
        String r0 = r3.name;
        if (r0.startsWith("OMX.google") == false) goto L5;
        return 1;
    L5:
        if (r0.startsWith("c2.android") == false) goto L8;
        return 1;
    L8:
        if (Util.SDK_INT < 26) goto L10;
        return 0;
    L10:
        if (r0.equals("OMX.MTK.AUDIO.DECODER.RAW") == false) goto L17;
        return -1;
    L17:
        return 0;
    }

    static /* synthetic */ int lambda$getDecoderInfosSortedByFormatSupport$0(Format r1, MediaCodecInfo r2) {
        return r2.isFormatFunctionallySupported(r1) ? 1 : 0;
    }

    static /* synthetic */ int lambda$sortByScore$2(ScoreProvider r2, Object r3, Object r4) {
        return r2.getScore(r4) - r2.getScore(r3);
    }

    public static int maxH264DecodableFrameSize() throws DecoderQueryException {
        if (maxH264DecodableFrameSize != (-1)) goto L12;
        int r0 = 0;
        int r2 = 0;
        MediaCodecInfo r1 = getDecoderInfo(MimeTypes.VIDEO_H264, false, false);
        if (r1 == null) goto L10;
        android.media.MediaCodecInfo.CodecProfileLevel[] r3 = r1.getProfileLevels();
        int r4 = r3.length;
    L7:
        if (r2 >= r4) goto L9;
        r0 = Math.max(avcLevelToMaxFrameSize(r3[r2].level), r0);
        r2 = r2 + 1;
        goto L7
    L9:
        r0 = Math.max(r0, 345600);
    L10:
        maxH264DecodableFrameSize = r0;
    L12:
        return maxH264DecodableFrameSize;
    }

    private static <T> void sortByScore(List<T> r1, final ScoreProvider<T> r2) {
        Collections.sort(r1, new MediaCodecUtil$$ExternalSyntheticLambda0(r2));
    }

    public static void warmDecoderInfoCache(String r3, boolean r4, boolean r5) {
        getDecoderInfos(r3, r4, r5);     // Catch: DecoderQueryException -> L4
        return;
    L4:
        e = move-exception;
        Log.m45e(TAG, "Codec warming failed", e);
    }
}
