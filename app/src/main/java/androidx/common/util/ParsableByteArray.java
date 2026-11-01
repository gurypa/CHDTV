package androidx.media3.common.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedBytes;
import com.google.errorprone.annotations.CheckReturnValue;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
@CheckReturnValue
public final class ParsableByteArray {
    private static final char[] CR_AND_LF = null;

    /* JADX INFO: renamed from: LF */
    private static final char[] f32LF = null;
    private static final ImmutableSet<Charset> SUPPORTED_CHARSETS_FOR_READLINE = null;
    private byte[] data;
    private int limit;
    private int position;

    static {
        CR_AND_LF = new char[]{'\r', '\n'};
        f32LF = new char[]{'\n'};
        SUPPORTED_CHARSETS_FOR_READLINE = ImmutableSet.m264of(StandardCharsets.US_ASCII, StandardCharsets.UTF_8, StandardCharsets.UTF_16, StandardCharsets.UTF_16BE, StandardCharsets.UTF_16LE);
    }

    public ParsableByteArray() {
        this.data = Util.EMPTY_BYTE_ARRAY;
    }

    public ParsableByteArray(int r2) {
        this.data = new byte[r2];
        this.limit = r2;
    }

    public ParsableByteArray(byte[] r2) {
        this.data = r2;
        this.limit = r2.length;
    }

    public ParsableByteArray(byte[] r1, int r2) {
        this.data = r1;
        this.limit = r2;
    }

    private int findNextLineTerminator(Charset r5) {
        if (r5.equals(StandardCharsets.UTF_8) == false) goto L5;
    L17:
        int r0 = 1;
    L18:
        int r1 = this.position;
    L20:
        if (r1 >= (this.limit - (r0 - 1))) goto L46;
        if (r5.equals(StandardCharsets.UTF_8) == true) goto L26;
        if (r5.equals(StandardCharsets.US_ASCII) == true) goto L26;
    L29:
        if (r5.equals(StandardCharsets.UTF_16) == true) goto L33;
        if (r5.equals(StandardCharsets.UTF_16BE) == true) goto L33;
    L38:
        if (r5.equals(StandardCharsets.UTF_16LE) == false) goto L44;
        if (this.data[r1 + 1] != 0) goto L44;
        if (Util.isLinebreak(this.data[r1]) == false) goto L44;
        return r1;
    L44:
        r1 = r1 + r0;
    L33:
        if (this.data[r1] != 0) goto L38;
        if (Util.isLinebreak(this.data[r1 + 1]) == false) goto L38;
        return r1;
    L26:
        if (Util.isLinebreak(this.data[r1]) == false) goto L29;
        return r1;
    L46:
        return this.limit;
    L5:
        if (r5.equals(StandardCharsets.US_ASCII) == true) goto L17;
        if (r5.equals(StandardCharsets.UTF_16) == false) goto L10;
    L16:
        r0 = 2;
        goto L18
    L10:
        if (r5.equals(StandardCharsets.UTF_16LE) == true) goto L16;
        if (r5.equals(StandardCharsets.UTF_16BE) == true) goto L16;
        throw new IllegalArgumentException("Unsupported charset: " + r5);
    }

    private int peekCharacterAndSize(Charset r5) {
        if (r5.equals(StandardCharsets.UTF_8) == true) goto L7;
        if (r5.equals(StandardCharsets.US_ASCII) == true) goto L7;
    L10:
        if (r5.equals(StandardCharsets.UTF_16) == true) goto L14;
        if (r5.equals(StandardCharsets.UTF_16BE) == true) goto L14;
    L17:
        if (r5.equals(StandardCharsets.UTF_16LE) == true) goto L19;
        return 0;
    L19:
        if (bytesLeft() < 2) goto L25;
        byte r0 = (byte) Chars.fromBytes(this.data[this.position + 1], this.data[this.position]);
        int r1 = 2;
    L22:
        return (Chars.checkedCast(r0) << 16) + r1;
    L25:
        return 0;
    L14:
        if (bytesLeft() < 2) goto L17;
        r0 = (byte) Chars.fromBytes(this.data[this.position], this.data[this.position + 1]);
        r1 = 2;
    L7:
        if (bytesLeft() < 1) goto L10;
        r0 = (byte) Chars.checkedCast(UnsignedBytes.toInt(this.data[this.position]));
        r1 = 1;
        goto L22
    }

    private char readCharacterIfInList(Charset r4, char[] r5) {
        int r0 = peekCharacterAndSize(r4);
        if (r0 != 0) goto L5;
        return 0;
    L5:
        if (Chars.contains(r5, (char) (r0 >> 16)) == false) goto L10;
        this.position += 65535 & r0;
        return (char) (r0 >> 16);
    L10:
        return 0;
    }

    private void skipLineTerminator(Charset r3) {
        if (readCharacterIfInList(r3, CR_AND_LF) != '\r') goto L6;
        readCharacterIfInList(r3, f32LF);
        return;
    }

    public int bytesLeft() {
        return this.limit - this.position;
    }

    public int capacity() {
        return this.data.length;
    }

    public void ensureCapacity(int r2) {
        if (r2 <= capacity()) goto L6;
        this.data = Arrays.copyOf(this.data, r2);
        return;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getPosition() {
        return this.position;
    }

    public int limit() {
        return this.limit;
    }

    public char peekChar() {
        return (char) (((this.data[this.position] & 255) << 8) | (this.data[this.position + 1] & 255));
    }

    public char peekChar(Charset r4) {
        Assertions.checkArgument(SUPPORTED_CHARSETS_FOR_READLINE.contains(r4), "Unsupported charset: " + r4);
        return (char) (peekCharacterAndSize(r4) >> 16);
    }

    public int peekUnsignedByte() {
        return this.data[this.position] & 255;
    }

    public void readBytes(ParsableBitArray r3, int r4) {
        readBytes(r3.data, 0, r4);
        r3.setPosition(0);
    }

    public void readBytes(ByteBuffer r3, int r4) {
        r3.put(this.data, this.position, r4);
        this.position += r4;
    }

    public void readBytes(byte[] r3, int r4, int r5) {
        System.arraycopy(this.data, this.position, r3, r4, r5);
        this.position += r5;
    }

    public String readDelimiterTerminatedString(char r5) {
        if (bytesLeft() != 0) goto L6;
        return null;
    L6:
        int r0 = this.position;
    L8:
        if (r0 >= this.limit) goto L12;
        if (this.data[r0] == r5) goto L12;
        r0 = r0 + 1;
    L12:
        String r1 = Util.fromUtf8Bytes(this.data, this.position, r0 - this.position);
        this.position = r0;
        if (this.position >= this.limit) goto L15;
        this.position++;
    L15:
        return r1;
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public int readInt() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        int r2 = (r0[r1] & 255) << 24;
        byte[] r3 = this.data;
        int r4 = this.position;
        this.position = r4 + 1;
        int r5 = r2 | ((r3[r4] & 255) << 16);
        byte[] r6 = this.data;
        int r7 = this.position;
        this.position = r7 + 1;
        int r8 = r5 | ((r6[r7] & 255) << 8);
        byte[] r9 = this.data;
        int r10 = this.position;
        this.position = r10 + 1;
        return r8 | (r9[r10] & 255);
    }

    public int readInt24() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        int r2 = ((r0[r1] & 255) << 24) >> 8;
        byte[] r3 = this.data;
        int r4 = this.position;
        this.position = r4 + 1;
        int r5 = r2 | ((r3[r4] & 255) << 8);
        byte[] r6 = this.data;
        int r7 = this.position;
        this.position = r7 + 1;
        return r5 | (r6[r7] & 255);
    }

    public String readLine() {
        return readLine(StandardCharsets.UTF_8);
    }

    public String readLine(Charset r5) {
        Assertions.checkArgument(SUPPORTED_CHARSETS_FOR_READLINE.contains(r5), "Unsupported charset: " + r5);
        if (bytesLeft() != 0) goto L7;
        return null;
    L7:
        if (r5.equals(StandardCharsets.US_ASCII) == true) goto L9;
        readUtfCharsetFromBom();
    L9:
        String r1 = readString(findNextLineTerminator(r5) - this.position, r5);
        if (this.position != this.limit) goto L12;
        return r1;
    L12:
        skipLineTerminator(r5);
        return r1;
    }

    public int readLittleEndianInt() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        int r2 = r0[r1] & 255;
        byte[] r3 = this.data;
        int r4 = this.position;
        this.position = r4 + 1;
        int r5 = r2 | ((r3[r4] & 255) << 8);
        byte[] r6 = this.data;
        int r7 = this.position;
        this.position = r7 + 1;
        int r8 = r5 | ((r6[r7] & 255) << 16);
        byte[] r9 = this.data;
        int r10 = this.position;
        this.position = r10 + 1;
        return r8 | ((r9[r10] & 255) << 24);
    }

    public int readLittleEndianInt24() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        int r2 = r0[r1] & 255;
        byte[] r3 = this.data;
        int r4 = this.position;
        this.position = r4 + 1;
        int r5 = r2 | ((r3[r4] & 255) << 8);
        byte[] r6 = this.data;
        int r7 = this.position;
        this.position = r7 + 1;
        return r5 | ((r6[r7] & 255) << 16);
    }

    public long readLittleEndianLong() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        long r2 = ((long) r0[r1]) & 255;
        byte[] r4 = this.data;
        int r5 = this.position;
        this.position = r5 + 1;
        long r3 = r2 | ((((long) r4[r5]) & 255) << 8);
        byte[] r6 = this.data;
        int r7 = this.position;
        this.position = r7 + 1;
        long r8 = r3 | ((((long) r6[r7]) & 255) << 16);
        byte[] r9 = this.data;
        int r10 = this.position;
        this.position = r10 + 1;
        long r11 = r8 | ((((long) r9[r10]) & 255) << 24);
        byte[] r12 = this.data;
        int r13 = this.position;
        this.position = r13 + 1;
        long r14 = r11 | ((((long) r12[r13]) & 255) << 32);
        byte[] r15 = this.data;
        int r16 = this.position;
        this.position = r16 + 1;
        long r17 = r14 | ((((long) r15[r16]) & 255) << 40);
        byte[] r18 = this.data;
        int r19 = this.position;
        this.position = r19 + 1;
        long r20 = r17 | ((((long) r18[r19]) & 255) << 48);
        byte[] r21 = this.data;
        int r22 = this.position;
        this.position = r22 + 1;
        return r20 | ((255 & ((long) r21[r22])) << 56);
    }

    public short readLittleEndianShort() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        int r2 = r0[r1] & 255;
        byte[] r3 = this.data;
        int r4 = this.position;
        this.position = r4 + 1;
        return (short) (r2 | ((r3[r4] & 255) << 8));
    }

    public long readLittleEndianUnsignedInt() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        long r2 = ((long) r0[r1]) & 255;
        byte[] r4 = this.data;
        int r5 = this.position;
        this.position = r5 + 1;
        long r3 = r2 | ((((long) r4[r5]) & 255) << 8);
        byte[] r6 = this.data;
        int r7 = this.position;
        this.position = r7 + 1;
        long r8 = r3 | ((((long) r6[r7]) & 255) << 16);
        byte[] r9 = this.data;
        int r10 = this.position;
        this.position = r10 + 1;
        return r8 | ((255 & ((long) r9[r10])) << 24);
    }

    public int readLittleEndianUnsignedInt24() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        int r2 = r0[r1] & 255;
        byte[] r3 = this.data;
        int r4 = this.position;
        this.position = r4 + 1;
        int r5 = r2 | ((r3[r4] & 255) << 8);
        byte[] r6 = this.data;
        int r7 = this.position;
        this.position = r7 + 1;
        return r5 | ((r6[r7] & 255) << 16);
    }

    public int readLittleEndianUnsignedIntToInt() {
        int r0 = readLittleEndianInt();
        if (r0 < 0) goto L6;
        return r0;
    L6:
        throw new IllegalStateException("Top bit not zero: " + r0);
    }

    public int readLittleEndianUnsignedShort() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        int r2 = r0[r1] & 255;
        byte[] r3 = this.data;
        int r4 = this.position;
        this.position = r4 + 1;
        return r2 | ((r3[r4] & 255) << 8);
    }

    public long readLong() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        long r2 = (((long) r0[r1]) & 255) << 56;
        byte[] r4 = this.data;
        int r5 = this.position;
        this.position = r5 + 1;
        long r3 = r2 | ((((long) r4[r5]) & 255) << 48);
        byte[] r6 = this.data;
        int r7 = this.position;
        this.position = r7 + 1;
        long r8 = r3 | ((((long) r6[r7]) & 255) << 40);
        byte[] r9 = this.data;
        int r10 = this.position;
        this.position = r10 + 1;
        long r11 = r8 | ((((long) r9[r10]) & 255) << 32);
        byte[] r12 = this.data;
        int r13 = this.position;
        this.position = r13 + 1;
        long r14 = r11 | ((((long) r12[r13]) & 255) << 24);
        byte[] r15 = this.data;
        int r16 = this.position;
        this.position = r16 + 1;
        long r17 = r14 | ((((long) r15[r16]) & 255) << 16);
        byte[] r18 = this.data;
        int r19 = this.position;
        this.position = r19 + 1;
        long r20 = r17 | ((((long) r18[r19]) & 255) << 8);
        byte[] r21 = this.data;
        int r22 = this.position;
        this.position = r22 + 1;
        return r20 | (255 & ((long) r21[r22]));
    }

    public String readNullTerminatedString() {
        return readDelimiterTerminatedString(0);
    }

    public String readNullTerminatedString(int r5) {
        if (r5 != 0) goto L5;
        return "";
    L5:
        int r0 = r5;
        int r1 = (this.position + r5) - 1;
        if (r1 < this.limit) goto L8;
    L10:
        String r2 = Util.fromUtf8Bytes(this.data, this.position, r0);
        this.position += r5;
        return r2;
    L8:
        if (this.data[r1] != 0) goto L10;
        r0 = r0 - 1;
        goto L10
    }

    public short readShort() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        int r2 = (r0[r1] & 255) << 8;
        byte[] r3 = this.data;
        int r4 = this.position;
        this.position = r4 + 1;
        return (short) (r2 | (r3[r4] & 255));
    }

    public String readString(int r2) {
        return readString(r2, StandardCharsets.UTF_8);
    }

    public String readString(int r4, Charset r5) {
        String r0 = new String(this.data, this.position, r4, r5);
        this.position += r4;
        return r0;
    }

    public int readSynchSafeInt() {
        int r0 = readUnsignedByte();
        int r1 = readUnsignedByte();
        int r2 = readUnsignedByte();
        int r4 = ((r0 << 21) | (r1 << 14)) | (r2 << 7);
        return r4 | readUnsignedByte();
    }

    public int readUnsignedByte() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        return r0[r1] & 255;
    }

    public int readUnsignedFixedPoint1616() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        int r2 = (r0[r1] & 255) << 8;
        byte[] r3 = this.data;
        int r4 = this.position;
        this.position = r4 + 1;
        int r5 = r2 | (r3[r4] & 255);
        this.position += 2;
        return r5;
    }

    public long readUnsignedInt() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        long r2 = (((long) r0[r1]) & 255) << 24;
        byte[] r4 = this.data;
        int r5 = this.position;
        this.position = r5 + 1;
        long r3 = r2 | ((((long) r4[r5]) & 255) << 16);
        byte[] r6 = this.data;
        int r7 = this.position;
        this.position = r7 + 1;
        long r8 = r3 | ((((long) r6[r7]) & 255) << 8);
        byte[] r9 = this.data;
        int r10 = this.position;
        this.position = r10 + 1;
        return r8 | (255 & ((long) r9[r10]));
    }

    public int readUnsignedInt24() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        int r2 = (r0[r1] & 255) << 16;
        byte[] r3 = this.data;
        int r4 = this.position;
        this.position = r4 + 1;
        int r5 = r2 | ((r3[r4] & 255) << 8);
        byte[] r6 = this.data;
        int r7 = this.position;
        this.position = r7 + 1;
        return r5 | (r6[r7] & 255);
    }

    public int readUnsignedIntToInt() {
        int r0 = readInt();
        if (r0 < 0) goto L6;
        return r0;
    L6:
        throw new IllegalStateException("Top bit not zero: " + r0);
    }

    public int readUnsignedLeb128ToInt() {
        return Ints.checkedCast(readUnsignedLeb128ToLong());
    }

    public long readUnsignedLeb128ToLong() {
        long r0 = 0;
        int r2 = 0;
    L4:
        if (r2 >= 9) goto L13;
        if (this.position == this.limit) goto L12;
        long r3 = readUnsignedByte();
        r0 = r0 | ((127 & r3) << (r2 * 7));
        if ((128 & r3) == 0) goto L13;
        r2 = r2 + 1;
        goto L4
    L12:
        throw new IllegalStateException("Attempting to read a byte over the limit.");
    L13:
        return r0;
    }

    public long readUnsignedLongToLong() {
        long r0 = readLong();
        if (r0 < 0) goto L6;
        return r0;
    L6:
        throw new IllegalStateException("Top bit not zero: " + r0);
    }

    public int readUnsignedShort() {
        byte[] r0 = this.data;
        int r1 = this.position;
        this.position = r1 + 1;
        int r2 = (r0[r1] & 255) << 8;
        byte[] r3 = this.data;
        int r4 = this.position;
        this.position = r4 + 1;
        return r2 | (r3[r4] & 255);
    }

    public long readUtf8EncodedLong() {
        int r0 = 0;
        long r1 = this.data[this.position];
        int r3 = 7;
    L4:
        if (r3 < 0) goto L13;
        if ((((long) (1 << r3)) & r1) == 0) goto L7;
        r3 = r3 - 1;
        goto L4
    L7:
        if (r3 >= 6) goto L10;
        r1 = r1 & ((long) ((1 << r3) - 1));
        r0 = 7 - r3;
        goto L13
    L10:
        if (r3 != 7) goto L13;
        r0 = 1;
    L13:
        if (r0 == 0) goto L24;
        int r4 = 1;
    L15:
        if (r4 >= r0) goto L21;
        byte r5 = this.data[this.position + r4];
        if ((r5 & 192) != 128) goto L20;
        r1 = (r1 << 6) | ((long) (r5 & 63));
        r4 = r4 + 1;
        goto L15
    L20:
        throw new NumberFormatException("Invalid UTF-8 sequence continuation byte: " + r1);
    L21:
        this.position += r0;
        return r1;
    L24:
        throw new NumberFormatException("Invalid UTF-8 sequence first byte: " + r1);
    }

    public Charset readUtfCharsetFromBom() {
        if (bytesLeft() < 3) goto L13;
        if (this.data[this.position] != (-17)) goto L13;
        if (this.data[this.position + 1] != (-69)) goto L13;
        if (this.data[this.position + 2] != (-65)) goto L13;
        this.position += 3;
        return StandardCharsets.UTF_8;
    L13:
        if (bytesLeft() >= 2) goto L15;
        return null;
    L15:
        if (this.data[this.position] != (-2)) goto L21;
        if (this.data[this.position + 1] != (-1)) goto L21;
        this.position += 2;
        return StandardCharsets.UTF_16BE;
    L21:
        if (this.data[this.position] == (-1)) goto L23;
        return null;
    L23:
        if (this.data[this.position + 1] != (-2)) goto L29;
        this.position += 2;
        return StandardCharsets.UTF_16LE;
    L29:
        return null;
    }

    public void reset(int r2) {
        if (capacity() >= r2) goto L5;
        byte[] r0 = new byte[r2];
    L6:
        reset(r0, r2);
        return;
    L5:
        r0 = this.data;
        goto L6
    }

    public void reset(byte[] r2) {
        reset(r2, r2.length);
    }

    public void reset(byte[] r2, int r3) {
        this.data = r2;
        this.limit = r3;
        this.position = 0;
    }

    public void setLimit(int r2) {
        if (r2 >= 0) goto L4;
    L6:
        boolean r0 = false;
    L7:
        Assertions.checkArgument(r0);
        this.limit = r2;
        return;
    L4:
        if (r2 > this.data.length) goto L6;
        r0 = true;
        goto L7
    }

    public void setPosition(int r2) {
        if (r2 >= 0) goto L4;
    L6:
        boolean r0 = false;
    L7:
        Assertions.checkArgument(r0);
        this.position = r2;
        return;
    L4:
        if (r2 > this.limit) goto L6;
        r0 = true;
        goto L7
    }

    public void skipBytes(int r2) {
        setPosition(this.position + r2);
    }
}
