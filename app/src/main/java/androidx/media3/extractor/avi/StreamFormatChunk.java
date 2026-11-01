package androidx.media3.extractor.avi;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import com.google.common.collect.ImmutableList;

/* JADX INFO: loaded from: classes.dex */
final class StreamFormatChunk implements AviChunk {
    private static final String TAG = "StreamFormatChunk";
    public final Format format;

    public StreamFormatChunk(Format r1) {
        this.format = r1;
    }

    private static String getMimeTypeFromCompression(int r1) {
        switch(r1) {
            case 808802372: goto L13;
            case 826496577: goto L11;
            case 828601953: goto L11;
            case 842289229: goto L9;
            case 859066445: goto L7;
            case 875967048: goto L11;
            case 877677894: goto L13;
            case 1145656883: goto L13;
            case 1145656920: goto L13;
            case 1196444237: goto L5;
            case 1482049860: goto L13;
            case 1684633208: goto L13;
            case 1735420525: goto L5;
            case 2021026148: goto L13;
            default: goto L3;
        };
    L3:
        return null;
    L5:
        return MimeTypes.VIDEO_MJPEG;
    L7:
        return MimeTypes.VIDEO_MP43;
    L9:
        return MimeTypes.VIDEO_MP42;
    L11:
        return MimeTypes.VIDEO_H264;
    L13:
        return MimeTypes.VIDEO_MP4V;
    }

    private static String getMimeTypeFromTag(int r1) {
        switch(r1) {
            case 1: goto L13;
            case 85: goto L11;
            case 255: goto L9;
            case 8192: goto L7;
            case 8193: goto L5;
            default: goto L3;
        };
    L3:
        return null;
    L5:
        return MimeTypes.AUDIO_DTS;
    L7:
        return MimeTypes.AUDIO_AC3;
    L9:
        return MimeTypes.AUDIO_AAC;
    L11:
        return MimeTypes.AUDIO_MPEG;
    L13:
        return MimeTypes.AUDIO_RAW;
    }

    private static AviChunk parseBitmapInfoHeader(ParsableByteArray r7) {
        r7.skipBytes(4);
        int r1 = r7.readLittleEndianInt();
        int r2 = r7.readLittleEndianInt();
        r7.skipBytes(4);
        int r0 = r7.readLittleEndianInt();
        String r3 = getMimeTypeFromCompression(r0);
        if (r3 != null) goto L6;
        Log.m48w(TAG, "Ignoring track with unsupported compression " + r0);
        return null;
    L6:
        Format.Builder r4 = new Format.Builder();
        r4.setWidth(r1).setHeight(r2).setSampleMimeType(r3);
        return new StreamFormatChunk(r4.build());
    }

    public static AviChunk parseFrom(int r2, ParsableByteArray r3) {
        if (r2 != 2) goto L7;
        return parseBitmapInfoHeader(r3);
    L7:
        if (r2 == 1) goto L9;
        Log.m48w(TAG, "Ignoring strf box for unsupported track type: " + Util.getTrackTypeString(r2));
        return null;
    L9:
        return parseWaveFormatEx(r3);
    }

    private static AviChunk parseWaveFormatEx(ParsableByteArray r11) {
        int r0 = r11.readLittleEndianUnsignedShort();
        String r1 = getMimeTypeFromTag(r0);
        if (r1 != null) goto L6;
        Log.m48w(TAG, "Ignoring track with unsupported format tag " + r0);
        return null;
    L6:
        int r2 = r11.readLittleEndianUnsignedShort();
        int r3 = r11.readLittleEndianInt();
        r11.skipBytes(6);
        int r5 = Util.getPcmEncoding(r11.readLittleEndianUnsignedShort());
        if (r11.bytesLeft() <= 0) goto L9;
        int r6 = r11.readLittleEndianUnsignedShort();
    L10:
        byte[] r8 = new byte[r6];
        r11.readBytes(r8, 0, r8.length);
        Format.Builder r7 = new Format.Builder();
        r7.setSampleMimeType(r1).setChannelCount(r2).setSampleRate(r3);
        if (MimeTypes.AUDIO_RAW.equals(r1) == false) goto L15;
        if (r5 == 0) goto L15;
        r7.setPcmEncoding(r5);
    L15:
        if (MimeTypes.AUDIO_AAC.equals(r1) == false) goto L20;
        if (r8.length <= 0) goto L20;
        r7.setInitializationData(ImmutableList.m213of(r8));
    L20:
        return new StreamFormatChunk(r7.build());
    L9:
        r6 = 0;
        goto L10
    }

    @Override // androidx.media3.extractor.avi.AviChunk
    public int getType() {
        return AviExtractor.FOURCC_strf;
    }
}
