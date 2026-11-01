package androidx.media3.extractor.mkv;

import androidx.media3.extractor.ExtractorInput;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
final class VarintReader {
    private static final int STATE_BEGIN_READING = 0;
    private static final int STATE_READ_CONTENTS = 1;
    private static final long[] VARINT_LENGTH_MASKS = null;
    private int length;
    private final byte[] scratch;
    private int state;

    static {
        VARINT_LENGTH_MASKS = new long[]{128, 64, 32, 16, 8, 4, 2, 1};
    }

    public VarintReader() {
        this.scratch = new byte[8];
    }

    public static long assembleVarint(byte[] r9, int r10, boolean r11) {
        long r0 = ((long) r9[0]) & 255;
        if (r11 == false) goto L5;
        r0 = r0 & (~VARINT_LENGTH_MASKS[r10 - 1]);
    L5:
        int r4 = 1;
    L6:
        if (r4 >= r10) goto L8;
        r0 = (r0 << 8) | (((long) r9[r4]) & 255);
        r4 = r4 + 1;
        goto L6
    L8:
        return r0;
    }

    public static int parseUnsignedVarintLength(int r7) {
        int r1 = 0;
    L4:
        if (r1 >= VARINT_LENGTH_MASKS.length) goto L12;
        if ((VARINT_LENGTH_MASKS[r1] & ((long) r7)) != 0) goto L9;
        r1 = r1 + 1;
        goto L4
    L9:
        return r1 + 1;
    L12:
        return -1;
    }

    public int getLastLength() {
        return this.length;
    }

    public long readUnsignedVarint(ExtractorInput r6, boolean r7, boolean r8, int r9) throws IOException {
        if (this.state != 0) goto L14;
        if (r6.readFully(this.scratch, 0, 1, r7) == true) goto L8;
        return -1;
    L8:
        this.length = parseUnsignedVarintLength(this.scratch[0] & 255);
        if (this.length == (-1)) goto L12;
        this.state = 1;
        goto L14
    L12:
        throw new IllegalStateException("No valid varint length mask found");
    L14:
        if (this.length <= r9) goto L18;
        this.state = 0;
        return -2;
    L18:
        if (this.length == 1) goto L20;
        r6.readFully(this.scratch, 1, this.length - 1);
    L20:
        this.state = 0;
        return assembleVarint(this.scratch, this.length, r8);
    }

    public void reset() {
        this.state = 0;
        this.length = 0;
    }
}
