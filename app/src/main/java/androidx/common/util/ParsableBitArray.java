package androidx.media3.common.util;

import androidx.core.view.MotionEventCompat;
import com.google.errorprone.annotations.CheckReturnValue;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: classes.dex */
@CheckReturnValue
public final class ParsableBitArray {
    private int bitOffset;
    private int byteLimit;
    private int byteOffset;
    public byte[] data;

    public ParsableBitArray() {
        this.data = Util.EMPTY_BYTE_ARRAY;
    }

    public ParsableBitArray(byte[] r2) {
        this(r2, r2.length);
    }

    public ParsableBitArray(byte[] r1, int r2) {
        this.data = r1;
        this.byteLimit = r2;
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

    public void byteAlign() {
        if (this.bitOffset != 0) goto L5;
        return;
    L5:
        this.bitOffset = 0;
        this.byteOffset++;
        assertValidOffset();
    }

    public int getBytePosition() {
        if (this.bitOffset != 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        return this.byteOffset;
    L5:
        r0 = false;
        goto L6
    }

    public int getPosition() {
        return (this.byteOffset * 8) + this.bitOffset;
    }

    public void putInt(int r12, int r13) {
        if (r13 >= 32) goto L5;
        r12 = r12 & ((1 << r13) - 1);
    L5:
        int r1 = Math.min(8 - this.bitOffset, r13);
        int r4 = (8 - this.bitOffset) - r1;
        this.data[this.byteOffset] = (byte) (this.data[this.byteOffset] & ((MotionEventCompat.ACTION_POINTER_INDEX_MASK >> this.bitOffset) | ((1 << r4) - 1)));
        this.data[this.byteOffset] = (byte) (this.data[this.byteOffset] | ((r12 >>> (r13 - r1)) << r4));
        int r0 = r13 - r1;
        int r7 = this.byteOffset + 1;
    L6:
        if (r0 <= 8) goto L8;
        this.data[r7] = (byte) (r12 >>> (r0 - 8));
        r0 = r0 - 8;
        r7 = r7 + 1;
        goto L6
    L8:
        int r3 = 8 - r0;
        this.data[r7] = (byte) (this.data[r7] & ((1 << r3) - 1));
        this.data[r7] = (byte) (this.data[r7] | ((r12 & ((1 << r0) - 1)) << r3));
        skipBits(r13);
        assertValidOffset();
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

    public int readBits(int r6) {
        if (r6 != 0) goto L5;
        return 0;
    L5:
        int r1 = 0;
        this.bitOffset += r6;
    L7:
        if (this.bitOffset <= 8) goto L9;
        this.bitOffset -= 8;
        byte[] r2 = this.data;
        int r3 = this.byteOffset;
        this.byteOffset = r3 + 1;
        r1 = r1 | ((r2[r3] & 255) << this.bitOffset);
        goto L7
    L9:
        int r4 = (r1 | ((this.data[this.byteOffset] & 255) >> (8 - this.bitOffset))) & ((-1) >>> (32 - r6));
        if (this.bitOffset != 8) goto L12;
        this.bitOffset = 0;
        this.byteOffset++;
    L12:
        assertValidOffset();
        return r4;
    }

    public void readBits(byte[] r9, int r10, int r11) {
        int r0 = (r11 >> 3) + r10;
        int r1 = r10;
    L4:
        if (r1 >= r0) goto L6;
        byte[] r4 = this.data;
        int r5 = this.byteOffset;
        this.byteOffset = r5 + 1;
        r9[r1] = (byte) (r4[r5] << this.bitOffset);
        r9[r1] = (byte) (((255 & this.data[this.byteOffset]) >> (8 - this.bitOffset)) | r9[r1]);
        r1 = r1 + 1;
        goto L4
    L6:
        int r2 = r11 & 7;
        if (r2 != 0) goto L9;
        return;
    L9:
        r9[r0] = (byte) (r9[r0] & (255 >> r2));
        if ((this.bitOffset + r2) <= 8) goto L12;
        byte r6 = r9[r0];
        byte[] r7 = this.data;
        int r8 = this.byteOffset;
        this.byteOffset = r8 + 1;
        r9[r0] = (byte) (r6 | ((r7[r8] & 255) << this.bitOffset));
        this.bitOffset -= 8;
    L12:
        this.bitOffset += r2;
        r9[r0] = (byte) (r9[r0] | ((byte) (((255 & this.data[this.byteOffset]) >> (8 - this.bitOffset)) << (8 - r2))));
        if (this.bitOffset != 8) goto L15;
        this.bitOffset = 0;
        this.byteOffset++;
    L15:
        assertValidOffset();
    }

    public long readBitsToLong(int r3) {
        if (r3 > 32) goto L7;
        return Util.toUnsignedLong(readBits(r3));
    L7:
        return Util.toLong(readBits(r3 - 32), readBits(32));
    }

    public void readBytes(byte[] r3, int r4, int r5) {
        if (this.bitOffset != 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        System.arraycopy(this.data, this.byteOffset, r3, r4, r5);
        this.byteOffset += r5;
        assertValidOffset();
        return;
    L5:
        r0 = false;
        goto L6
    }

    public String readBytesAsString(int r2) {
        return readBytesAsString(r2, StandardCharsets.UTF_8);
    }

    public String readBytesAsString(int r3, Charset r4) {
        byte[] r0 = new byte[r3];
        readBytes(r0, 0, r3);
        return new String(r0, r4);
    }

    public void reset(ParsableByteArray r3) {
        reset(r3.getData(), r3.limit());
        setPosition(r3.getPosition() * 8);
    }

    public void reset(byte[] r2) {
        reset(r2, r2.length);
    }

    public void reset(byte[] r2, int r3) {
        this.data = r2;
        this.byteOffset = 0;
        this.bitOffset = 0;
        this.byteLimit = r3;
    }

    public void setPosition(int r2) {
        this.byteOffset = r2 / 8;
        this.bitOffset = r2 - (this.byteOffset * 8);
        assertValidOffset();
    }

    public void skipBit() {
        int r0 = this.bitOffset + 1;
        this.bitOffset = r0;
        if (r0 != 8) goto L5;
        this.bitOffset = 0;
        this.byteOffset++;
    L5:
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

    public void skipBytes(int r2) {
        if (this.bitOffset != 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        this.byteOffset += r2;
        assertValidOffset();
        return;
    L5:
        r0 = false;
        goto L6
    }
}
