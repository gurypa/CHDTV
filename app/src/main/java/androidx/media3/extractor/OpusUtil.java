package androidx.media3.extractor;

import androidx.media3.common.C0180C;
import androidx.media3.exoplayer.DefaultLoadControl;
import com.google.common.base.Ascii;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class OpusUtil {
    private static final int DEFAULT_SEEK_PRE_ROLL_SAMPLES = 3840;
    private static final int FULL_CODEC_INITIALIZATION_DATA_BUFFER_COUNT = 3;
    public static final int MAX_BYTES_PER_SECOND = 63750;
    public static final int SAMPLE_RATE = 48000;

    private OpusUtil() {
    }

    public static List<byte[]> buildInitializationData(byte[] r7) {
        long r1 = sampleCountToNanoseconds(getPreSkipSamples(r7));
        long r3 = sampleCountToNanoseconds(3840);
        ArrayList r5 = new ArrayList(3);
        r5.add(r7);
        r5.add(buildNativeOrderByteArray(r1));
        r5.add(buildNativeOrderByteArray(r3));
        return r5;
    }

    private static byte[] buildNativeOrderByteArray(long r2) {
        return ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(r2).array();
    }

    public static int getChannelCount(byte[] r1) {
        return r1[9] & 255;
    }

    private static long getPacketDurationUs(byte r9, byte r10) {
        int r0 = r9 & 255;
        switch((r0 & 3)) {
            case 0: goto L6;
            case 1: goto L5;
            case 2: goto L5;
            default: goto L4;
        };
    L4:
        int r1 = r10 & 63;
    L7:
        int r2 = r0 >> 3;
        int r3 = r2 & 3;
        if (r2 < 16) goto L11;
        int r4 = DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS << r3;
    L18:
        return ((long) r1) * ((long) r4);
    L11:
        if (r2 < 12) goto L14;
        r4 = 10000 << (r3 & 1);
        goto L18
    L14:
        if (r3 != 3) goto L16;
        r4 = 60000;
        goto L18
    L16:
        r4 = 10000 << r3;
        goto L18
    L5:
        r1 = 2;
        goto L7
    L6:
        r1 = 1;
        goto L7
    }

    public static long getPacketDurationUs(byte[] r4) {
        byte r0 = 0;
        byte r1 = r4[0];
        if (r4.length <= 1) goto L6;
        r0 = r4[1];
    L6:
        return getPacketDurationUs(r1, r0);
    }

    public static int getPreSkipSamples(byte[] r2) {
        return ((r2[11] & 255) << 8) | (r2[10] & 255);
    }

    public static boolean needToDecodeOpusFrame(long r6, long r8) {
        if ((r6 - r8) > (sampleCountToNanoseconds(3840) / 1000)) goto L5;
        return true;
    L5:
        return false;
    }

    public static int parseOggPacketAudioSampleCount(ByteBuffer r9) {
        int r0 = parseOggPacketForPreAudioSampleByteCount(r9);
        int r2 = (r9.get(r0 + 26) + Ascii.ESC) + r0;
        byte r3 = r9.get(r2);
        if ((r9.limit() - r2) <= 1) goto L5;
        byte r4 = r9.get(r2 + 1);
    L7:
        return (int) ((48000 * getPacketDurationUs(r3, r4)) / 1000000);
    L5:
        r4 = 0;
        goto L7
    }

    public static int parseOggPacketForPreAudioSampleByteCount(ByteBuffer r6) {
        if ((r6.get(5) & 2) != 0) goto L6;
        return 0;
    L6:
        int r0 = 28;
        byte r1 = r6.get(26);
        int r2 = 0;
    L7:
        if (r2 >= r1) goto L9;
        r0 = r0 + r6.get(r2 + 27);
        r2 = r2 + 1;
        goto L7
    L9:
        int r3 = 28;
        byte r4 = r6.get(r0 + 26);
        int r5 = 0;
    L10:
        if (r5 >= r4) goto L13;
        r3 = r3 + r6.get((r0 + 27) + r5);
        r5 = r5 + 1;
        goto L10
    L13:
        return r0 + r3;
    }

    public static int parsePacketAudioSampleCount(ByteBuffer r6) {
        byte r0 = 0;
        byte r1 = r6.get(0);
        if (r6.limit() <= 1) goto L6;
        r0 = r6.get(1);
    L6:
        return (int) ((48000 * getPacketDurationUs(r1, r0)) / 1000000);
    }

    private static long sampleCountToNanoseconds(long r4) {
        return (C0180C.NANOS_PER_SECOND * r4) / 48000;
    }
}
