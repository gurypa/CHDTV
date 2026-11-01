package androidx.media3.common.audio;

import androidx.media3.common.util.Util;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public final class AudioMixingUtil {
    private static final float FLOAT_PCM_MAX_VALUE = 1.0f;
    private static final float FLOAT_PCM_MIN_VALUE = -1.0f;

    private AudioMixingUtil() {
    }

    public static boolean canMix(AudioProcessor.AudioFormat r3) {
        if (r3.sampleRate != (-1)) goto L6;
        return false;
    L6:
        if (r3.channelCount != (-1)) goto L9;
        return false;
    L9:
        if (r3.encoding != 2) goto L11;
    L12:
        return true;
    L11:
        if (r3.encoding == 4) goto L12;
        return false;
    }

    public static boolean canMix(AudioProcessor.AudioFormat r3, AudioProcessor.AudioFormat r4) {
        if (r3.sampleRate == r4.sampleRate) goto L6;
        return false;
    L6:
        if (canMix(r3) == true) goto L9;
        return false;
    L9:
        if (canMix(r4) == true) goto L11;
        return false;
    L11:
        return true;
    }

    private static float floatSampleToInt16Pcm(float r3) {
        if (r3 >= 0.0f) goto L5;
        int r0 = 32768;
    L7:
        return Util.constrainValue(r0 * r3, -32768.0f, 32767.0f);
    L5:
        r0 = 32767;
        goto L7
    }

    private static float getPcmSample(ByteBuffer r1, boolean r2, boolean r3) {
        if (r3 == false) goto L7;
        if (r2 == false) goto L6;
        return r1.getShort();
    L6:
        return floatSampleToInt16Pcm(r1.getFloat());
    L7:
        if (r2 == false) goto L10;
        return int16SampleToFloatPcm(r1.getShort());
    L10:
        return r1.getFloat();
    }

    private static float int16SampleToFloatPcm(short r2) {
        float r0 = r2;
        if (r2 >= 0) goto L5;
        int r1 = 32768;
    L7:
        return r0 / r1;
    L5:
        r1 = 32767;
        goto L7
    }

    public static ByteBuffer mix(ByteBuffer r17, AudioProcessor.AudioFormat r18, ByteBuffer r19, AudioProcessor.AudioFormat r20, ChannelMixingMatrix r21, int r22, boolean r23, boolean r24) {
        boolean r3 = false;
        if (r18.encoding != 2) goto L5;
        boolean r2 = true;
    L7:
        if (r20.encoding != 2) goto L9;
        r3 = true;
    L9:
        int r4 = r21.getInputChannelCount();
        int r5 = r21.getOutputChannelCount();
        float[] r7 = new float[r4];
        float[] r8 = new float[r5];
        int r9 = 0;
    L11:
        if (r9 >= r22) goto L36;
        if (r23 == false) goto L17;
        int r11 = r19.position();
        int r12 = 0;
    L14:
        if (r12 >= r5) goto L16;
        r8[r12] = getPcmSample(r19, r3, r3);
        r12 = r12 + 1;
        goto L14
    L16:
        r19.position(r11);
    L17:
        int r13 = 0;
    L18:
        if (r13 >= r4) goto L20;
        r7[r13] = getPcmSample(r17, r2, r3);
        r13 = r13 + 1;
        goto L18
    L20:
        int r14 = 0;
    L21:
        if (r14 >= r5) goto L34;
        int r15 = 0;
    L23:
        if (r15 >= r4) goto L26;
        r8[r14] = r8[r14] + (r7[r15] * r21.getMixingCoefficient(r15, r14));
        r15 = r15 + 1;
        goto L23
    L26:
        if (r3 == false) goto L29;
        r19.putShort((short) Util.constrainValue(r8[r14], -32768.0f, 32767.0f));
    L33:
        r8[r14] = 0.0f;
        r14 = r14 + 1;
        goto L21
    L29:
        if (r24 == false) goto L31;
        float r16 = Util.constrainValue(r8[r14], FLOAT_PCM_MIN_VALUE, 1.0f);
    L32:
        r19.putFloat(r16);
        goto L33
    L31:
        r16 = r8[r14];
        goto L32
    L34:
        r9 = r9 + 1;
        goto L11
    L36:
        return r19;
    L5:
        r2 = false;
        goto L7
    }
}
