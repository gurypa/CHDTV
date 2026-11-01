package androidx.media3.extractor;

import androidx.media3.common.util.Assertions;

/* JADX INFO: loaded from: classes.dex */
public final class VorbisBitArray {
    private int bitOffset;
    private final int byteLimit;
    private int byteOffset;
    private final byte[] data;

    public VorbisBitArray(byte[] r2) {
        this.data = r2;
        this.byteLimit = r2.length;
    }

    private void assertValidOffset() {
        if (this.byteOffset >= 0) goto L5;
    L11:
        boolean r0 = false;
    L12:
        Assertions.checkState(r0);
        return;
    L5:
        if (this.byteOffset >= this.byteLimit) goto L7;
    L10:
        r0 = true;
        goto L12
    L7:
        if (this.byteOffset != this.byteLimit) goto L11;
        if (this.bitOffset != 0) goto L11;
        goto L10
    }

    public int bitsLeft() {
        return ((this.byteLimit - this.byteOffset) * 8) - this.bitOffset;
    }

    public int getPosition() {
        return (this.byteOffset * 8) + this.bitOffset;
    }

    public boolean readBit() {
        if ((((this.data[this.byteOffset] & 255) >> this.bitOffset) & 1) != 1) goto L5;
        boolean r0 = true;
    L6:
        skipBits(1);
        return r0;
    L5:
        r0 = false;
        goto L6
    }

    public int readBits(int r7) {
        int r0 = this.byteOffset;
        int r1 = Math.min(r7, 8 - this.bitOffset);
        int r3 = r0 + 1;
        int r2 = ((this.data[r0] & 255) >> this.bitOffset) & (255 >> (8 - r1));
    L3:
        if (r1 >= r7) goto L5;
        int r5 = r3 + 1;
        r2 = r2 | ((this.data[r3] & 255) << r1);
        r1 = r1 + 8;
        r3 = r5;
        goto L3
    L5:
        int r4 = r2 & ((-1) >>> (32 - r7));
        skipBits(r7);
        return r4;
    }

    public void reset() {
        this.byteOffset = 0;
        this.bitOffset = 0;
    }

    public void setPosition(int r2) {
        this.byteOffset = r2 / 8;
        this.bitOffset = r2 - (this.byteOffset * 8);
        assertValidOffset();
    }

    public void skipBits(int r4) {
        int r0 = r4 / 8;
        this.byteOffset += r0;
        this.bitOffset += r4 - (r0 * 8);
        if (this.bitOffset <= 7) goto L5;
        this.byteOffset++;
        this.bitOffset -= 8;
    L5:
        assertValidOffset();
    }
}
