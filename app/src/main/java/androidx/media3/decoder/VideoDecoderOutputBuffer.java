package androidx.media3.decoder;

import androidx.media3.common.Format;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class VideoDecoderOutputBuffer extends DecoderOutputBuffer {
    public static final int COLORSPACE_BT2020 = 3;
    public static final int COLORSPACE_BT601 = 1;
    public static final int COLORSPACE_BT709 = 2;
    public static final int COLORSPACE_UNKNOWN = 0;
    public int colorspace;
    public ByteBuffer data;
    public int decoderPrivate;
    public Format format;
    public int height;
    public int mode;
    private final DecoderOutputBuffer.Owner<VideoDecoderOutputBuffer> owner;
    public ByteBuffer supplementalData;
    public int width;
    public ByteBuffer[] yuvPlanes;
    public int[] yuvStrides;

    public VideoDecoderOutputBuffer(DecoderOutputBuffer.Owner<VideoDecoderOutputBuffer> r1) {
        this.owner = r1;
    }

    private static boolean isSafeToMultiply(int r1, int r2) {
        if (r1 < 0) goto L8;
        if (r2 < 0) goto L8;
        if (r2 > 0) goto L6;
    L7:
        return true;
    L6:
        if (r1 < (Integer.MAX_VALUE / r2)) goto L7;
    L8:
        return false;
    }

    public void init(long r3, int r5, ByteBuffer r6) {
        this.timeUs = r3;
        this.mode = r5;
        if (r6 != null) goto L5;
    L14:
        this.supplementalData = null;
        return;
    L5:
        if (r6.hasRemaining() == false) goto L14;
        addFlag(268435456);
        int r0 = r6.limit();
        if (this.supplementalData != null) goto L9;
    L12:
        this.supplementalData = ByteBuffer.allocate(r0);
    L13:
        this.supplementalData.put(r6);
        this.supplementalData.flip();
        r6.position(0);
        return;
    L9:
        if (this.supplementalData.capacity() < r0) goto L12;
        this.supplementalData.clear();
        goto L13
    }

    public void initForPrivateFrame(int r1, int r2) {
        this.width = r1;
        this.height = r2;
    }

    public boolean initForYuvFrame(int r18, int r19, int r20, int r21, int r22) {
        this.width = r18;
        this.height = r19;
        this.colorspace = r22;
        int r6 = (int) ((((long) r19) + 1) / 2);
        if (isSafeToMultiply(r20, r19) == true) goto L5;
    L27:
        return false;
    L5:
        if (isSafeToMultiply(r21, r6) == false) goto L27;
        int r7 = r20 * r19;
        int r9 = r21 * r6;
        int r10 = (r9 * 2) + r7;
        if (isSafeToMultiply(r9, 2) == false) goto L26;
        if (r10 < r7) goto L26;
        if (this.data != null) goto L14;
    L17:
        this.data = ByteBuffer.allocateDirect(r10);
    L19:
        if (this.yuvPlanes != null) goto L21;
        this.yuvPlanes = new ByteBuffer[3];
    L21:
        ByteBuffer r12 = this.data;
        ByteBuffer[] r14 = this.yuvPlanes;
        r14[0] = r12.slice();
        r14[0].limit(r7);
        r12.position(r7);
        r14[1] = r12.slice();
        r14[1].limit(r9);
        r12.position(r7 + r9);
        r14[2] = r12.slice();
        r14[2].limit(r9);
        if (this.yuvStrides != null) goto L24;
        this.yuvStrides = new int[3];
    L24:
        this.yuvStrides[0] = r20;
        this.yuvStrides[1] = r21;
        this.yuvStrides[2] = r21;
        return true;
    L14:
        if (this.data.capacity() < r10) goto L17;
        this.data.position(0);
        this.data.limit(r10);
    L26:
        return false;
    }

    @Override // androidx.media3.decoder.DecoderOutputBuffer
    public void release() {
        this.owner.releaseOutputBuffer(this);
    }
}
