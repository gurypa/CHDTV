package androidx.media3.exoplayer.audio;

import androidx.media3.common.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
public final class PcmAudioUtil {
    private PcmAudioUtil() {
    }

    public static ByteBuffer rampUpVolume(ByteBuffer r9, int r10, int r11, int r12, int r13) {
        ByteBuffer r0 = ByteBuffer.allocateDirect(r9.remaining()).order(ByteOrder.nativeOrder());
        int r1 = r12;
        int r2 = r9.position();
    L4:
        if (r9.hasRemaining() == false) goto L10;
        if (r1 >= r13) goto L10;
        write32BitIntPcm(r0, (int) ((r1 * ((long) readAs32BitIntPcm(r9, r10))) / ((long) r13)), r10);
        if (r9.position() != (r2 + r11)) goto L4;
        r1 = r1 + 1;
        r2 = r9.position();
    L10:
        r0.put(r9);
        r0.flip();
        return r0;
    }

    public static int readAs32BitIntPcm(ByteBuffer r3, int r4) {
        switch(r4) {
            case 2: goto L24;
            case 3: goto L22;
            case 4: goto L15;
            case 21: goto L14;
            case 22: goto L12;
            case 268435456: goto L10;
            case 1342177280: goto L8;
            case 1610612736: goto L6;
            default: goto L4;
        };
    L15:
        float r0 = Util.constrainValue(r3.getFloat(), -1.0f, 1.0f);
        if (r0 >= 0.0f) goto L20;
        return (int) ((-r0) * (-2.14748365E9f));
    L20:
        return (int) (2.14748365E9f * r0);
    L4:
        throw new IllegalStateException();
    L6:
        return ((((r3.get() & 255) << 24) | ((r3.get() & 255) << 16)) | ((r3.get() & 255) << 8)) | (r3.get() & 255);
    L8:
        return (((r3.get() & 255) << 24) | ((r3.get() & 255) << 16)) | ((r3.get() & 255) << 8);
    L10:
        return ((r3.get() & 255) << 24) | ((r3.get() & 255) << 16);
    L12:
        return (((r3.get() & 255) | ((r3.get() & 255) << 8)) | ((r3.get() & 255) << 16)) | ((r3.get() & 255) << 24);
    L14:
        return (((r3.get() & 255) << 8) | ((r3.get() & 255) << 16)) | ((r3.get() & 255) << 24);
    L22:
        return (r3.get() & 255) << 24;
    L24:
        return ((r3.get() & 255) << 16) | ((r3.get() & 255) << 24);
    }

    public static void write32BitIntPcm(ByteBuffer r2, int r3, int r4) {
        switch(r4) {
            case 2: goto L21;
            case 3: goto L19;
            case 4: goto L15;
            case 21: goto L13;
            case 22: goto L11;
            case 268435456: goto L9;
            case 1342177280: goto L7;
            case 1610612736: goto L5;
            default: goto L4;
        };
    L5:
        r2.put((byte) (r3 >> 24));
        r2.put((byte) (r3 >> 16));
        r2.put((byte) (r3 >> 8));
        r2.put((byte) r3);
        return;
    L7:
        r2.put((byte) (r3 >> 24));
        r2.put((byte) (r3 >> 16));
        r2.put((byte) (r3 >> 8));
        return;
    L9:
        r2.put((byte) (r3 >> 24));
        r2.put((byte) (r3 >> 16));
        return;
    L11:
        r2.put((byte) r3);
        r2.put((byte) (r3 >> 8));
        r2.put((byte) (r3 >> 16));
        r2.put((byte) (r3 >> 24));
        return;
    L13:
        r2.put((byte) (r3 >> 8));
        r2.put((byte) (r3 >> 16));
        r2.put((byte) (r3 >> 24));
        return;
    L15:
        if (r3 >= 0) goto L17;
        r2.putFloat((-r3) / (-2.14748365E9f));
        return;
    L17:
        r2.putFloat(r3 / 2.14748365E9f);
        return;
    L19:
        r2.put((byte) (r3 >> 24));
        return;
    L21:
        r2.put((byte) (r3 >> 16));
        r2.put((byte) (r3 >> 24));
        return;
    L4:
        throw new IllegalStateException();
    }
}
