package androidx.media3.common.audio;

import androidx.media3.common.util.Util;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public final class ToInt16PcmAudioProcessor extends BaseAudioProcessor {
    public ToInt16PcmAudioProcessor() {
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    public AudioProcessor.AudioFormat onConfigure(AudioProcessor.AudioFormat r6) throws AudioProcessor.UnhandledAudioFormatException {
        int r0 = r6.encoding;
        if (r0 == 3) goto L20;
        if (r0 == 2) goto L20;
        if (r0 == 268435456) goto L20;
        if (r0 == 21) goto L20;
        if (r0 == 1342177280) goto L20;
        if (r0 == 22) goto L20;
        if (r0 == 1610612736) goto L20;
        if (r0 == 4) goto L20;
        throw new AudioProcessor.UnhandledAudioFormatException(r6);
    L20:
        if (r0 == 2) goto L23;
        return new AudioProcessor.AudioFormat(r6.sampleRate, r6.channelCount, 2);
    L23:
        return AudioProcessor.AudioFormat.NOT_SET;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public void queueInput(ByteBuffer r10) {
        int r0 = r10.position();
        int r1 = r10.limit();
        int r2 = r1 - r0;
        switch(this.inputAudioFormat.encoding) {
            case 3: goto L9;
            case 4: goto L8;
            case 21: goto L7;
            case 22: goto L8;
            case 268435456: goto L6;
            case 1342177280: goto L7;
            case 1610612736: goto L8;
            default: goto L5;
        };
    L6:
        int r3 = r2;
    L10:
        ByteBuffer r4 = replaceOutputBuffer(r3);
        switch(this.inputAudioFormat.encoding) {
            case 3: goto L38;
            case 4: goto L34;
            case 21: goto L30;
            case 22: goto L26;
            case 268435456: goto L22;
            case 1342177280: goto L18;
            case 1610612736: goto L14;
            default: goto L13;
        };
    L14:
        int r5 = r0;
    L15:
        if (r5 >= r1) goto L42;
        r4.put(r10.get(r5 + 1));
        r4.put(r10.get(r5));
        r5 = r5 + 4;
    L42:
        r10.position(r10.limit());
        r4.flip();
        return;
    L18:
        int r6 = r0;
    L19:
        if (r6 >= r1) goto L42;
        r4.put(r10.get(r6 + 1));
        r4.put(r10.get(r6));
        r6 = r6 + 3;
        goto L19
    L22:
        int r7 = r0;
    L23:
        if (r7 >= r1) goto L42;
        r4.put(r10.get(r7 + 1));
        r4.put(r10.get(r7));
        r7 = r7 + 2;
        goto L23
    L26:
        int r8 = r0;
    L27:
        if (r8 >= r1) goto L42;
        r4.put(r10.get(r8 + 2));
        r4.put(r10.get(r8 + 3));
        r8 = r8 + 4;
        goto L27
    L30:
        int r9 = r0;
    L31:
        if (r9 >= r1) goto L42;
        r4.put(r10.get(r9 + 1));
        r4.put(r10.get(r9 + 2));
        r9 = r9 + 3;
        goto L31
    L34:
        int r11 = r0;
    L35:
        if (r11 >= r1) goto L42;
        short r12 = (short) (32767.0f * Util.constrainValue(r10.getFloat(r11), -1.0f, 1.0f));
        r4.put((byte) (r12 & 255));
        r4.put((byte) ((r12 >> 8) & 255));
        r11 = r11 + 4;
        goto L35
    L38:
        int r13 = r0;
    L39:
        if (r13 >= r1) goto L42;
        r4.put((byte) 0);
        r4.put((byte) ((r10.get(r13) & 255) - 128));
        r13 = r13 + 1;
        goto L39
    L13:
        throw new IllegalStateException();
    L7:
        r3 = (r2 / 3) * 2;
        goto L10
    L8:
        r3 = r2 / 2;
        goto L10
    L9:
        r3 = r2 * 2;
        goto L10
    L5:
        throw new IllegalStateException();
    }
}
