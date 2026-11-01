package es.antonio.m3u;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;

/* JADX INFO: loaded from: classes7.dex */
final class DemandFile extends InputStream implements DataInput {
    static final int PAGE = 8192;
    static final int SLOTS = 8;
    boolean closed;
    final int[] lengths;
    int next;
    final byte[][] pages;
    long physicalReads;
    long physicalSeeks;
    long position;
    final RandomAccessFile raw;
    final long size;
    final long[] tags;

    DemandFile(File r3, String r4) throws IOException {
        this.pages = new byte[8][];
        this.tags = new long[8];
        this.lengths = new int[8];
        if ("r".equals(r4) == false) goto L7;
        this.raw = new RandomAccessFile(r3, "r");
        this.size = this.raw.length();
        Arrays.fill(this.tags, -1);
        return;
    L7:
        throw new IllegalArgumentException("Read-only catalog");
    }

    private int page() throws IOException {
        check();
        long r0 = this.position / PlaybackStateCompat.ACTION_PLAY_FROM_URI;
        int r4 = 0;
        int r5 = 0;
    L4:
        if (r5 >= 8) goto L5;
        if (this.tags[r5] == r0) goto L19;
        r5 = r5 + 1;
        goto L4
    L19:
        return r5;
    L5:
        int r7 = this.next;
        this.next = (this.next + 1) % 8;
        if (this.pages[r7] != null) goto L8;
        this.pages[r7] = new byte[8192];
    L8:
        this.tags[r7] = -1;
        RandomAccessFile r6 = this.raw;
        long r8 = r0 * PlaybackStateCompat.ACTION_PLAY_FROM_URI;
        r6.seek(r8);
        this.physicalSeeks++;
        int r9 = (int) Math.min(PlaybackStateCompat.ACTION_PLAY_FROM_URI, this.size - r8);
    L9:
        if (r4 >= r9) goto L14;
        int r2 = this.raw.read(this.pages[r7], r4, r9 - r4);
        this.physicalReads++;
        if (r2 <= 0) goto L14;
        r4 = r4 + r2;
    L14:
        this.lengths[r7] = r4;
        this.tags[r7] = r0;
        return r7;
    }

    void check() throws IOException {
        if (this.closed == true) goto L6;
        return;
    L6:
        throw new IOException("Closed catalog");
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed == true) goto L6;
        this.closed = true;
        Arrays.fill(this.pages, null);
        this.raw.close();
        return;
    }

    long getFilePointer() throws IOException {
        check();
        return this.position;
    }

    long length() throws IOException {
        check();
        return this.size;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        check();
        if (this.position < this.size) goto L5;
        return -1;
    L5:
        int r0 = page();
        int r2 = (int) (this.position % PlaybackStateCompat.ACTION_PLAY_FROM_URI);
        if (r2 < this.lengths[r0]) goto L8;
        return -1;
    L8:
        this.position++;
        return this.pages[r0][r2] & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] r3) throws IOException {
        return read(r3, 0, r3.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] r10, int r11, int r12) throws IOException {
        check();
        if (r10 == null) goto L29;
        if (r11 < 0) goto L27;
        if (r12 < 0) goto L27;
        if (r11 > (r10.length - r12)) goto L27;
        int r0 = 0;
        if (r12 != 0) goto L12;
        return 0;
    L12:
        if (this.position < this.size) goto L14;
        return -1;
    L14:
        if (r0 >= r12) goto L22;
        if (this.position >= this.size) goto L22;
        int r1 = page();
        int r3 = (int) (this.position % PlaybackStateCompat.ACTION_PLAY_FROM_URI);
        int r4 = Math.min(r12 - r0, this.lengths[r1] - r3);
        if (r4 <= 0) goto L22;
        System.arraycopy(this.pages[r1], r3, r10, r11 + r0, r4);
        this.position += (long) r4;
        r0 = r0 + r4;
    L22:
        if (r0 != 0) goto L25;
        return -1;
    L25:
        return r0;
    L27:
        throw new IndexOutOfBoundsException();
    L29:
        throw new NullPointerException();
    }

    @Override // java.io.DataInput
    public boolean readBoolean() throws IOException {
        if (readUnsignedByte() == 0) goto L6;
        return true;
    L6:
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
    public void readFully(byte[] r3) throws IOException {
        readFully(r3, 0, r3.length);
    }

    @Override // java.io.DataInput
    public void readFully(byte[] r4, int r5, int r6) throws IOException {
        if (r5 < 0) goto L15;
        if (r6 < 0) goto L15;
        if (r5 > (r4.length - r6)) goto L15;
        int r0 = 0;
    L7:
        if (r0 >= r6) goto L8;
        int r1 = read(r4, r5 + r0, r6 - r0);
        if (r1 < 0) goto L13;
        r0 = r0 + r1;
        goto L7
    L13:
        throw new EOFException();
    L8:
        return;
    L15:
        throw new IndexOutOfBoundsException();
    }

    @Override // java.io.DataInput
    public int readInt() throws IOException {
        return (((readUnsignedByte() << 24) | (readUnsignedByte() << 16)) | (readUnsignedByte() << 8)) | readUnsignedByte();
    }

    @Override // java.io.DataInput
    public String readLine() throws IOException {
        StringBuilder r0 = new StringBuilder();
    L3:
        int r1 = read();
        if (r1 < 0) goto L14;
        if (r1 == 10) goto L14;
        if (r1 == 13) goto L11;
        r0.append((char) r1);
        goto L3
    L11:
        long r3 = this.position;
        if (read() == 10) goto L14;
        seek(r3);
    L14:
        if (r1 >= 0) goto L19;
        if (r0.length() != 0) goto L19;
        return null;
    L19:
        return r0.toString();
    }

    @Override // java.io.DataInput
    public long readLong() throws IOException {
        return (((long) readInt()) << 32) | (((long) readInt()) & 4294967295L);
    }

    @Override // java.io.DataInput
    public short readShort() throws IOException {
        return (short) readUnsignedShort();
    }

    @Override // java.io.DataInput
    public String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }

    @Override // java.io.DataInput
    public int readUnsignedByte() throws IOException {
        int r0 = read();
        if (r0 < 0) goto L6;
        return r0;
    L6:
        throw new EOFException();
    }

    @Override // java.io.DataInput
    public int readUnsignedShort() throws IOException {
        return (readUnsignedByte() << 8) | readUnsignedByte();
    }

    void seek(long r3) throws IOException {
        check();
        if (r3 < 0) goto L7;
        this.position = r3;
        return;
    L7:
        throw new IOException("Negative seek");
    }

    @Override // java.io.DataInput
    public int skipBytes(int r7) throws IOException {
        check();
        if (r7 > 0) goto L5;
        return 0;
    L5:
        if (this.position >= this.size) goto L11;
        int r8 = (int) Math.min(r7, this.size - this.position);
        this.position += (long) r8;
        return r8;
    L11:
        return 0;
    }
}
