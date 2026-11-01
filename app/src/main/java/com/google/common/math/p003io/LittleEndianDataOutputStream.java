package com.google.common.p003io;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Longs;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class LittleEndianDataOutputStream extends FilterOutputStream implements DataOutput {
    public LittleEndianDataOutputStream(OutputStream r3) {
        super(new DataOutputStream((OutputStream) Preconditions.checkNotNull(r3)));
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.out.close();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.DataOutput
    public void write(byte[] r2, int r3, int r4) throws IOException {
        this.out.write(r2, r3, r4);
    }

    @Override // java.io.DataOutput
    public void writeBoolean(boolean r2) throws IOException {
        ((DataOutputStream) this.out).writeBoolean(r2);
    }

    @Override // java.io.DataOutput
    public void writeByte(int r2) throws IOException {
        ((DataOutputStream) this.out).writeByte(r2);
    }

    @Override // java.io.DataOutput
    @Deprecated
    public void writeBytes(String r2) throws IOException {
        ((DataOutputStream) this.out).writeBytes(r2);
    }

    @Override // java.io.DataOutput
    public void writeChar(int r1) throws IOException {
        writeShort(r1);
    }

    @Override // java.io.DataOutput
    public void writeChars(String r3) throws IOException {
        int r0 = 0;
    L4:
        if (r0 >= r3.length()) goto L6;
        writeChar(r3.charAt(r0));
        r0 = r0 + 1;
        goto L4
    }

    @Override // java.io.DataOutput
    public void writeDouble(double r3) throws IOException {
        writeLong(Double.doubleToLongBits(r3));
    }

    @Override // java.io.DataOutput
    public void writeFloat(float r2) throws IOException {
        writeInt(Float.floatToIntBits(r2));
    }

    @Override // java.io.DataOutput
    public void writeInt(int r3) throws IOException {
        this.out.write(r3 & 255);
        this.out.write((r3 >> 8) & 255);
        this.out.write((r3 >> 16) & 255);
        this.out.write((r3 >> 24) & 255);
    }

    @Override // java.io.DataOutput
    public void writeLong(long r4) throws IOException {
        byte[] r0 = Longs.toByteArray(Long.reverseBytes(r4));
        write(r0, 0, r0.length);
    }

    @Override // java.io.DataOutput
    public void writeShort(int r3) throws IOException {
        this.out.write(r3 & 255);
        this.out.write((r3 >> 8) & 255);
    }

    @Override // java.io.DataOutput
    public void writeUTF(String r2) throws IOException {
        ((DataOutputStream) this.out).writeUTF(r2);
    }
}
