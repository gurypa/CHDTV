package androidx.media3.exoplayer.audio;

import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.util.Util;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
final class ToFloatPcmAudioProcessor extends androidx.media3.common.audio.BaseAudioProcessor {
    private static final int FLOAT_NAN_AS_INT = 0;
    private static final double PCM_32_BIT_INT_TO_PCM_32_BIT_FLOAT_FACTOR = 4.656612875245797E-10d;

    static {
        FLOAT_NAN_AS_INT = Float.floatToIntBits(Float.NaN);
    }

    ToFloatPcmAudioProcessor() {
    }

    private static void writePcm32BitFloat(int r4, ByteBuffer r5) {
        int r1 = Float.floatToIntBits((float) (((double) r4) * PCM_32_BIT_INT_TO_PCM_32_BIT_FLOAT_FACTOR));
        if (r1 != FLOAT_NAN_AS_INT) goto L5;
        r1 = Float.floatToIntBits(0.0f);
    L5:
        r5.putInt(r1);
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    public AudioProcessor.AudioFormat onConfigure(AudioProcessor.AudioFormat r6) throws AudioProcessor.UnhandledAudioFormatException {
        int r0 = r6.encoding;
        if (Util.isEncodingHighResolutionPcm(r0) == false) goto L10;
        if (r0 == 4) goto L8;
        return new AudioProcessor.AudioFormat(r6.sampleRate, r6.channelCount, 4);
    L8:
        return AudioProcessor.AudioFormat.NOT_SET;
    L10:
        throw new AudioProcessor.UnhandledAudioFormatException(r6);
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public void queueInput(ByteBuffer r8) {
        int r0 = r8.position();
        int r1 = r8.limit();
        int r2 = r1 - r0;
        switch(this.inputAudioFormat.encoding) {
            case 21: goto L18;
            case 22: goto L14;
            case 1342177280: goto L10;
            case 1610612736: goto L6;
            default: goto L5;
        };
    L6:
        ByteBuffer r3 = replaceOutputBuffer(r2);
        int r4 = r0;
    L7:
        if (r4 >= r1) goto L22;
        writePcm32BitFloat((((r8.get(r4 + 3) & 255) | ((r8.get(r4 + 2) & 255) << 8)) | ((r8.get(r4 + 1) & 255) << 16)) | ((r8.get(r4) & 255) << 24), r3);
        r4 = r4 + 4;
    L22:
        r8.position(r8.limit());
        r3.flip();
        return;
    L10:
        r3 = replaceOutputBuffer((r2 / 3) * 4);
        int r5 = r0;
    L11:
        if (r5 >= r1) goto L22;
        writePcm32BitFloat((((r8.get(r5 + 2) & 255) << 8) | ((r8.get(r5 + 1) & 255) << 16)) | ((r8.get(r5) & 255) << 24), r3);
        r5 = r5 + 3;
        goto L11
    L14:
        r3 = replaceOutputBuffer(r2);
        int r6 = r0;
    L15:
        if (r6 >= r1) goto L22;
        writePcm32BitFloat((((r8.get(r6) & 255) | ((r8.get(r6 + 1) & 255) << 8)) | ((r8.get(r6 + 2) & 255) << 16)) | ((r8.get(r6 + 3) & 255) << 24), r3);
        r6 = r6 + 4;
        goto L15
    L18:
        r3 = replaceOutputBuffer((r2 / 3) * 4);
        int r7 = r0;
    L19:
        if (r7 >= r1) goto L22;
        writePcm32BitFloat((((r8.get(r7) & 255) << 8) | ((r8.get(r7 + 1) & 255) << 16)) | ((r8.get(r7 + 2) & 255) << 24), r3);
        r7 = r7 + 3;
        goto L19
    L5:
        throw new IllegalStateException();
    }
}
