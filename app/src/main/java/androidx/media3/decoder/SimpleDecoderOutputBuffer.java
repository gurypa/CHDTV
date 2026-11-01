package androidx.media3.decoder;

import androidx.media3.common.util.Assertions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
public class SimpleDecoderOutputBuffer extends DecoderOutputBuffer {
    public ByteBuffer data;
    private final DecoderOutputBuffer.Owner<SimpleDecoderOutputBuffer> owner;

    public SimpleDecoderOutputBuffer(DecoderOutputBuffer.Owner<SimpleDecoderOutputBuffer> r1) {
        this.owner = r1;
    }

    @Override // androidx.media3.decoder.DecoderOutputBuffer, androidx.media3.decoder.Buffer
    public void clear() {
        super.clear();
        if (this.data == null) goto L6;
        this.data.clear();
        return;
    }

    public ByteBuffer grow(int r5) {
        ByteBuffer r0 = (ByteBuffer) Assertions.checkNotNull(this.data);
        if (r5 < r0.limit()) goto L5;
        boolean r1 = true;
    L6:
        Assertions.checkArgument(r1);
        ByteBuffer r2 = ByteBuffer.allocateDirect(r5).order(ByteOrder.nativeOrder());
        int r3 = r0.position();
        r0.position(0);
        r2.put(r0);
        r2.position(r3);
        r2.limit(r5);
        this.data = r2;
        return r2;
    L5:
        r1 = false;
        goto L6
    }

    public ByteBuffer init(long r3, int r5) {
        this.timeUs = r3;
        if (this.data != null) goto L5;
    L6:
        this.data = ByteBuffer.allocateDirect(r5).order(ByteOrder.nativeOrder());
    L7:
        this.data.position(0);
        this.data.limit(r5);
        return this.data;
    L5:
        if (this.data.capacity() >= r5) goto L7;
        goto L6
    }

    @Override // androidx.media3.decoder.DecoderOutputBuffer
    public void release() {
        this.owner.releaseOutputBuffer(this);
    }
}
