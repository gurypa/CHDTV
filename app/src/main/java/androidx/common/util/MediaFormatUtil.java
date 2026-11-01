package androidx.media3.common.util;

import android.media.MediaFormat;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import com.arthenica.ffmpegkit.StreamInformation;
import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class MediaFormatUtil {
    public static final String KEY_MAX_BIT_RATE = "max-bitrate";
    public static final String KEY_PCM_ENCODING_EXTENDED = "exo-pcm-encoding-int";
    public static final String KEY_PIXEL_WIDTH_HEIGHT_RATIO_FLOAT = "exo-pixel-width-height-ratio-float";
    private static final int MAX_POWER_OF_TWO_INT = 1073741824;

    private MediaFormatUtil() {
    }

    public static Format createFormatFromMediaFormat(MediaFormat r5) {
        Format.Builder r0 = new Format.Builder().setSampleMimeType(r5.getString("mime")).setLanguage(r5.getString("language")).setPeakBitrate(getInteger(r5, KEY_MAX_BIT_RATE, -1)).setAverageBitrate(getInteger(r5, "bitrate", -1)).setCodecs(getCodecString(r5)).setFrameRate(getFrameRate(r5, -1.0f)).setWidth(getInteger(r5, StreamInformation.KEY_WIDTH, -1)).setHeight(getInteger(r5, StreamInformation.KEY_HEIGHT, -1)).setPixelWidthHeightRatio(getPixelWidthHeightRatio(r5, 1.0f)).setMaxInputSize(getInteger(r5, "max-input-size", -1)).setRotationDegrees(getInteger(r5, "rotation-degrees", 0)).setColorInfo(getColorInfo(r5)).setSampleRate(getInteger(r5, "sample-rate", -1)).setChannelCount(getInteger(r5, "channel-count", -1)).setPcmEncoding(getInteger(r5, "pcm-encoding", -1));
        ImmutableList.Builder r1 = new ImmutableList.Builder();
        int r2 = 0;
    L3:
        ByteBuffer r3 = r5.getByteBuffer("csd-" + r2);
        if (r3 == null) goto L5;
        byte[] r4 = new byte[r3.remaining()];
        r3.get(r4);
        r3.rewind();
        r1.add(r4);
        r2 = r2 + 1;
        goto L3
    L5:
        r0.setInitializationData(r1.build());
        return r0.build();
    }

    public static MediaFormat createMediaFormatFromFormat(Format r4) {
        MediaFormat r0 = new MediaFormat();
        maybeSetInteger(r0, "bitrate", r4.bitrate);
        maybeSetInteger(r0, KEY_MAX_BIT_RATE, r4.peakBitrate);
        maybeSetInteger(r0, "channel-count", r4.channelCount);
        maybeSetColorInfo(r0, r4.colorInfo);
        maybeSetString(r0, "mime", r4.sampleMimeType);
        maybeSetString(r0, "codecs-string", r4.codecs);
        maybeSetFloat(r0, "frame-rate", r4.frameRate);
        maybeSetInteger(r0, StreamInformation.KEY_WIDTH, r4.width);
        maybeSetInteger(r0, StreamInformation.KEY_HEIGHT, r4.height);
        setCsdBuffers(r0, r4.initializationData);
        maybeSetPcmEncoding(r0, r4.pcmEncoding);
        maybeSetString(r0, "language", r4.language);
        maybeSetInteger(r0, "max-input-size", r4.maxInputSize);
        maybeSetInteger(r0, "sample-rate", r4.sampleRate);
        maybeSetInteger(r0, "caption-service-number", r4.accessibilityChannel);
        r0.setInteger("rotation-degrees", r4.rotationDegrees);
        int r1 = r4.selectionFlags;
        setBooleanAsInt(r0, "is-autoselect", r1 & 4);
        setBooleanAsInt(r0, "is-default", r1 & 1);
        setBooleanAsInt(r0, "is-forced-subtitle", r1 & 2);
        r0.setInteger("encoder-delay", r4.encoderDelay);
        r0.setInteger("encoder-padding", r4.encoderPadding);
        maybeSetPixelAspectRatio(r0, r4.pixelWidthHeightRatio);
        return r0;
    }

    public static byte[] getArray(ByteBuffer r1) {
        byte[] r0 = new byte[r1.remaining()];
        r1.get(r0);
        return r0;
    }

    private static String getCodecString(MediaFormat r3) {
        if (Objects.equals(r3.getString("mime"), MimeTypes.VIDEO_H263) == false) goto L11;
        if (r3.containsKey("profile") == false) goto L11;
        if (r3.containsKey("level") == false) goto L11;
        return CodecSpecificDataUtil.buildH263CodecString(r3.getInteger("profile"), r3.getInteger("level"));
    L11:
        return getString(r3, "codecs-string", null);
    }

    public static ColorInfo getColorInfo(MediaFormat r8) {
        if (Util.SDK_INT >= 24) goto L5;
        return null;
    L5:
        int r0 = getInteger(r8, "color-standard", -1);
        int r3 = getInteger(r8, "color-range", -1);
        int r4 = getInteger(r8, "color-transfer", -1);
        ByteBuffer r5 = r8.getByteBuffer("hdr-static-info");
        if (r5 == null) goto L8;
        byte[] r6 = getArray(r5);
    L10:
        if (isValidColorSpace(r0) == true) goto L13;
        r0 = -1;
    L13:
        if (isValidColorRange(r3) == true) goto L16;
        r3 = -1;
    L16:
        if (isValidColorTransfer(r4) == true) goto L18;
        r4 = -1;
    L18:
        if (r0 != (-1)) goto L25;
        if (r3 != (-1)) goto L25;
        if (r4 != (-1)) goto L25;
        if (r6 != null) goto L25;
        return null;
    L25:
        return new ColorInfo.Builder().setColorSpace(r0).setColorRange(r3).setColorTransfer(r4).setHdrStaticInfo(r6).build();
    L8:
        r6 = null;
        goto L10
    }

    public static float getFloat(MediaFormat r1, String r2, float r3) {
        if (r1.containsKey(r2) == false) goto L6;
        return r1.getFloat(r2);
    L6:
        return r3;
    }

    private static float getFrameRate(MediaFormat r3, float r4) {
        if (r3.containsKey("frame-rate") == false) goto L11;
        return r3.getFloat("frame-rate");
    L8:
        return r3.getInteger("frame-rate");
    L11:
        return r4;
    }

    public static int getInteger(MediaFormat r1, String r2, int r3) {
        if (r1.containsKey(r2) == false) goto L6;
        return r1.getInteger(r2);
    L6:
        return r3;
    }

    private static float getPixelWidthHeightRatio(MediaFormat r3, float r4) {
        if (r3.containsKey("sar-width") == true) goto L5;
    L8:
        return r4;
    L5:
        if (r3.containsKey("sar-height") == false) goto L8;
        return r3.getInteger("sar-width") / r3.getInteger("sar-height");
    }

    public static String getString(MediaFormat r1, String r2, String r3) {
        if (r1.containsKey(r2) == false) goto L6;
        return r1.getString(r2);
    L6:
        return r3;
    }

    public static Integer getTimeLapseFrameRate(MediaFormat r2) {
        if (r2.containsKey("time-lapse-enable") == true) goto L5;
        return null;
    L5:
        if (r2.getInteger("time-lapse-enable") > 0) goto L7;
        return null;
    L7:
        if (r2.containsKey("time-lapse-fps") == true) goto L9;
        return null;
    L9:
        return Integer.valueOf(r2.getInteger("time-lapse-fps"));
    }

    public static boolean isAudioFormat(MediaFormat r1) {
        return MimeTypes.isAudio(r1.getString("mime"));
    }

    private static boolean isValidColorRange(int r2) {
        if (r2 == 2) goto L10;
        if (r2 != 1) goto L6;
        return true;
    L6:
        if (r2 == (-1)) goto L12;
        return false;
    L12:
        return true;
    L10:
        return true;
    }

    private static boolean isValidColorSpace(int r2) {
        if (r2 == 2) goto L12;
        if (r2 != 1) goto L6;
        return true;
    L6:
        if (r2 != 6) goto L8;
        return true;
    L8:
        if (r2 == (-1)) goto L15;
        return false;
    L15:
        return true;
    L12:
        return true;
    }

    private static boolean isValidColorTransfer(int r2) {
        if (r2 != 1) goto L5;
        return true;
    L5:
        if (r2 != 3) goto L7;
        return true;
    L7:
        if (r2 != 6) goto L9;
        return true;
    L9:
        if (r2 != 7) goto L11;
        return true;
    L11:
        if (r2 == (-1)) goto L19;
        return false;
    L19:
        return true;
    }

    public static boolean isVideoFormat(MediaFormat r1) {
        return MimeTypes.isVideo(r1.getString("mime"));
    }

    public static void maybeSetByteBuffer(MediaFormat r1, String r2, byte[] r3) {
        if (r3 == null) goto L5;
        r1.setByteBuffer(r2, ByteBuffer.wrap(r3));
        return;
    }

    public static void maybeSetColorInfo(MediaFormat r2, ColorInfo r3) {
        if (r3 == null) goto L5;
        maybeSetInteger(r2, "color-transfer", r3.colorTransfer);
        maybeSetInteger(r2, "color-standard", r3.colorSpace);
        maybeSetInteger(r2, "color-range", r3.colorRange);
        maybeSetByteBuffer(r2, "hdr-static-info", r3.hdrStaticInfo);
        return;
    }

    public static void maybeSetFloat(MediaFormat r1, String r2, float r3) {
        if (r3 == (-1.0f)) goto L6;
        r1.setFloat(r2, r3);
        return;
    }

    public static void maybeSetInteger(MediaFormat r1, String r2, int r3) {
        if (r3 == (-1)) goto L6;
        r1.setInteger(r2, r3);
        return;
    }

    private static void maybeSetPcmEncoding(MediaFormat r2, int r3) {
        if (r3 != (-1)) goto L5;
        return;
    L5:
        maybeSetInteger(r2, KEY_PCM_ENCODING_EXTENDED, r3);
        switch(r3) {
            case 0: goto L13;
            case 2: goto L12;
            case 3: goto L11;
            case 4: goto L10;
            case 21: goto L9;
            case 22: goto L8;
            default: goto L7;
        };
    L7:
        return;
    L8:
        int r0 = 22;
    L14:
        r2.setInteger("pcm-encoding", r0);
        return;
    L9:
        r0 = 21;
        goto L14
    L10:
        r0 = 4;
        goto L14
    L11:
        r0 = 3;
        goto L14
    L12:
        r0 = 2;
        goto L14
    L13:
        r0 = 0;
        goto L14
    }

    private static void maybeSetPixelAspectRatio(MediaFormat r4, float r5) {
        r4.setFloat(KEY_PIXEL_WIDTH_HEIGHT_RATIO_FLOAT, r5);
        int r0 = 1;
        int r1 = 1;
        if (r5 >= 1.0f) goto L6;
        r1 = 1073741824;
        r0 = (int) (1073741824 * r5);
    L8:
        r4.setInteger("sar-width", r0);
        r4.setInteger("sar-height", r1);
        return;
    L6:
        if (r5 <= 1.0f) goto L8;
        r0 = 1073741824;
        r1 = (int) (1073741824 / r5);
        goto L8
    }

    public static void maybeSetString(MediaFormat r0, String r1, String r2) {
        if (r2 == null) goto L5;
        r0.setString(r1, r2);
        return;
    }

    private static void setBooleanAsInt(MediaFormat r1, String r2, int r3) {
        if (r3 == 0) goto L4;
        int r0 = 1;
    L5:
        r1.setInteger(r2, r0);
        return;
    L4:
        r0 = 0;
        goto L5
    }

    public static void setCsdBuffers(MediaFormat r3, List<byte[]> r4) {
        int r0 = 0;
    L4:
        if (r0 >= r4.size()) goto L6;
        r3.setByteBuffer("csd-" + r0, ByteBuffer.wrap(r4.get(r0)));
        r0 = r0 + 1;
        goto L4
    }
}
