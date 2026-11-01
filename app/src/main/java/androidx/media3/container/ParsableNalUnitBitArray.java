package androidx.media3.container;

import androidx.media3.common.util.Assertions;

/* JADX INFO: loaded from: classes.dex */
public final class ParsableNalUnitBitArray {
    private int bitOffset;
    private int byteLimit;
    private int byteOffset;
    private byte[] data;

    public ParsableNalUnitBitArray(byte[] r1, int r2, int r3) {
        reset(r1, r2, r3);
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

    private int readExpGolombCodeNum() {
        int r0 = 0;
    L4:
        if (readBit() == true) goto L6;
        r0 = r0 + 1;
        goto L4
    L6:
        int r2 = (1 << r0) - 1;
        if (r0 <= 0) goto L9;
        int r1 = readBits(r0);
    L11:
        return r2 + r1;
    L9:
        r1 = 0;
        goto L11
    }

    private boolean shouldSkipByte(int r3) {
        if (2 <= r3) goto L5;
    L13:
        return false;
    L5:
        if (r3 >= this.byteLimit) goto L13;
        if (this.data[r3] != 3) goto L13;
        if (this.data[r3 - 2] != 0) goto L13;
        if (this.data[r3 - 1] != 0) goto L13;
        return true;
    }

    public void byteAlign() {
        if (this.bitOffset <= 0) goto L6;
        skipBits(8 - this.bitOffset);
        return;
    }

    public boolean canReadBits(int r8) {
        int r0 = this.byteOffset;
        int r1 = r8 / 8;
        int r2 = this.byteOffset + r1;
        int r3 = (this.bitOffset + r8) - (r1 * 8);
        if (r3 <= 7) goto L5;
        r2 = r2 + 1;
        r3 = r3 - 8;
    L5:
        int r4 = r0 + 1;
    L7:
        if (r4 > r2) goto L15;
        if (r2 >= this.byteLimit) goto L15;
        if (shouldSkipByte(r4) == false) goto L13;
        r2 = r2 + 1;
        r4 = r4 + 2;
    L13:
        r4 = r4 + 1;
    L15:
        if (r2 >= this.byteLimit) goto L17;
        return true;
    L17:
        if (r2 != this.byteLimit) goto L20;
        if (r3 != 0) goto L20;
        return true;
    L20:
        return false;
    }

    public boolean canReadExpGolombCodedNum() {
        int r0 = this.byteOffset;
        int r1 = this.bitOffset;
        int r2 = 0;
    L4:
        if (this.byteOffset >= this.byteLimit) goto L9;
        if (readBit() == true) goto L9;
        r2 = r2 + 1;
    L9:
        if (this.byteOffset != this.byteLimit) goto L11;
        boolean r3 = true;
    L12:
        this.byteOffset = r0;
        this.bitOffset = r1;
        if (r3 == false) goto L15;
        return false;
    L15:
        if (canReadBits((r2 * 2) + 1) == true) goto L17;
        return false;
    L17:
        return true;
    L11:
        r3 = false;
        goto L12
    }

    public boolean readBit() {
        if ((this.data[this.byteOffset] & (128 >> this.bitOffset)) == 0) goto L5;
        boolean r0 = true;
    L6:
        skipBit();
        return r0;
    L5:
        r0 = false;
        goto L6
    }

    public int readBits(int r7) {
        int r0 = 0;
        this.bitOffset += r7;
    L3:
        int r2 = 2;
        if (this.bitOffset <= 8) goto L10;
        this.bitOffset -= 8;
        r0 = r0 | ((this.data[this.byteOffset] & 255) << this.bitOffset);
        int r1 = this.byteOffset;
        if (shouldSkipByte(this.byteOffset + 1) == true) goto L9;
        r2 = 1;
    L9:
        this.byteOffset = r1 + r2;
        goto L3
    L10:
        int r3 = (r0 | ((this.data[this.byteOffset] & 255) >> (8 - this.bitOffset))) & ((-1) >>> (32 - r7));
        if (this.bitOffset != 8) goto L17;
        this.bitOffset = 0;
        int r4 = this.byteOffset;
        if (shouldSkipByte(this.byteOffset + 1) == true) goto L16;
        r2 = 1;
    L16:
        this.byteOffset = r4 + r2;
    L17:
        assertValidOffset();
        return r3;
    }

    public int readSignedExpGolombCodedInt() {
        int r0 = readExpGolombCodeNum();
        if ((r0 % 2) != 0) goto L5;
        int r1 = -1;
    L7:
        return r1 * ((r0 + 1) / 2);
    L5:
        r1 = 1;
        goto L7
    }

    public int readUnsignedExpGolombCodedInt() {
        return readExpGolombCodeNum();
    }

    public void reset(byte[] r2, int r3, int r4) {
        this.data = r2;
        this.byteOffset = r3;
        this.byteLimit = r4;
        this.bitOffset = 0;
        assertValidOffset();
    }

    public void skipBit() {
        int r1 = 1;
        int r0 = this.bitOffset + 1;
        this.bitOffset = r0;
        if (r0 != 8) goto L8;
        this.bitOffset = 0;
        int r2 = this.byteOffset;
        if (shouldSkipByte(this.byteOffset + 1) == false) goto L7;
        r1 = 2;
    L7:
        this.byteOffset = r2 + r1;
    L8:
        assertValidOffset();
    }

    public void skipBits(int r5) {
        int r0 = this.byteOffset;
        int r1 = r5 / 8;
        this.byteOffset += r1;
        this.bitOffset += r5 - (r1 * 8);
        if (this.bitOffset <= 7) goto L5;
        this.byteOffset++;
        this.bitOffset -= 8;
    L5:
        int r2 = r0 + 1;
    L7:
        if (r2 > this.byteOffset) goto L12;
        if (shouldSkipByte(r2) == false) goto L11;
        this.byteOffset++;
        r2 = r2 + 2;
    L11:
        r2 = r2 + 1;
        goto L7
    L12:
        assertValidOffset();
    }
}
