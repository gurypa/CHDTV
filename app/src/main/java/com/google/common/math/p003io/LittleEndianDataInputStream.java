package com.google.common.p003io;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class LittleEndianDataInputStream extends FilterInputStream implements DataInput {
    public LittleEndianDataInputStream(InputStream r2) {
        super((InputStream) Preconditions.checkNotNull(r2));
    }

    private byte readAndCheckByte() throws IOException, EOFException {
        int r0 = this.in.read();
        if ((-1) == r0) goto L7;
        return (byte) r0;
    L7:
        throw new EOFException();
    }

    @Override // java.io.DataInput
    public boolean readBoolean() throws IOException {
        if (readUnsignedByte() == 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.io.DataInput
    public byte readByte() throws IOException {
        return (byte) readUnsignedByte();
    }

    @Override // java.io.DataInput
    public char readChar() throws IOException {
        return (char) readUnsignedShort();
    }

    @Override // java.io.DataInput
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override // java.io.DataInput
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override // java.io.DataInput
    public void readFully(byte[] r1) throws IOException {
        ByteStreams.readFully(this, r1);
    }

    @Override // java.io.DataInput
    public void readFully(byte[] r1, int r2, int r3) throws IOException {
        ByteStreams.readFully(this, r1, r2, r3);
    }

    @Override // java.io.DataInput
    public int readInt() throws IOException {
        byte r0 = readAndCheckByte();
        byte r1 = readAndCheckByte();
        byte r2 = readAndCheckByte();
        return Ints.fromBytes(readAndCheckByte(), r2, r1, r0);
    }

    @Override // java.io.DataInput
    public String readLine() {
        throw new UnsupportedOperationException("readLine is not supported");
    }

    @Override // java.io.DataInput
    public long readLong() throws IOException {
        byte r7 = readAndCheckByte();
        byte r6 = readAndCheckByte();
        byte r5 = readAndCheckByte();
        byte r4 = readAndCheckByte();
        byte r3 = readAndCheckByte();
        byte r2 = readAndCheckByte();
        byte r1 = readAndCheckByte();
        return Longs.fromBytes(readAndCheckByte(), r1, r2, r3, r4, r5, r6, r7);
    }

    @Override // java.io.DataInput
    public short readShort() throws IOException {
        return (short) readUnsignedShort();
    }

    @Override // java.io.DataInput
    public String readUTF() throws IOException {
        return new DataInputStream(this.in).readUTF();
    }

    @Override // java.io.DataInput
    public int readUnsignedByte() throws IOException {
        int r0 = this.in.read();
        if (r0 < 0) goto L6;
        return r0;
    L6:
        throw new EOFException();
    }

    @Override // java.io.DataInput
    public int readUnsignedShort() throws IOException {
        byte r0 = readAndCheckByte();
        return Ints.fromBytes((byte) 0, (byte) 0, readAndCheckByte(), r0);
    }

    @Override // java.io.DataInput
    public int skipBytes(int r4) throws IOException {
        return (int) this.in.skip(r4);
    }
}
