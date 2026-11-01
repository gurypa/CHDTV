package com.google.common.p003io;

import java.io.DataInput;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface ByteArrayDataInput extends DataInput {
    @Override // java.io.DataInput
    boolean readBoolean();

    @Override // java.io.DataInput
    byte readByte();

    @Override // java.io.DataInput
    char readChar();

    @Override // java.io.DataInput
    double readDouble();

    @Override // java.io.DataInput
    float readFloat();

    @Override // java.io.DataInput
    void readFully(byte[] r1);

    @Override // java.io.DataInput
    void readFully(byte[] r1, int r2, int r3);

    @Override // java.io.DataInput
    int readInt();

    @Override // java.io.DataInput
    @CheckForNull
    String readLine();

    @Override // java.io.DataInput
    long readLong();

    @Override // java.io.DataInput
    short readShort();

    @Override // java.io.DataInput
    String readUTF();

    @Override // java.io.DataInput
    int readUnsignedByte();

    @Override // java.io.DataInput
    int readUnsignedShort();

    @Override // java.io.DataInput
    int skipBytes(int r1);
}
