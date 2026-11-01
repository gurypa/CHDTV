package com.google.common.p003io;

import java.io.DataOutput;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface ByteArrayDataOutput extends DataOutput {
    byte[] toByteArray();

    @Override // java.io.DataOutput
    void write(int r1);

    @Override // java.io.DataOutput
    void write(byte[] r1);

    @Override // java.io.DataOutput
    void write(byte[] r1, int r2, int r3);

    @Override // java.io.DataOutput
    void writeBoolean(boolean r1);

    @Override // java.io.DataOutput
    void writeByte(int r1);

    @Override // java.io.DataOutput
    @Deprecated
    void writeBytes(String r1);

    @Override // java.io.DataOutput
    void writeChar(int r1);

    @Override // java.io.DataOutput
    void writeChars(String r1);

    @Override // java.io.DataOutput
    void writeDouble(double r1);

    @Override // java.io.DataOutput
    void writeFloat(float r1);

    @Override // java.io.DataOutput
    void writeInt(int r1);

    @Override // java.io.DataOutput
    void writeLong(long r1);

    @Override // java.io.DataOutput
    void writeShort(int r1);

    @Override // java.io.DataOutput
    void writeUTF(String r1);
}
