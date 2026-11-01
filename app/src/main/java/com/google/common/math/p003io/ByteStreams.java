package com.google.common.p003io;

import android.support.v4.media.session.PlaybackStateCompat;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class ByteStreams {
    private static final int BUFFER_SIZE = 8192;
    private static final int MAX_ARRAY_LEN = 2147483639;
    private static final OutputStream NULL_OUTPUT_STREAM = null;
    private static final int TO_BYTE_ARRAY_DEQUE_SIZE = 20;
    private static final int ZERO_COPY_CHUNK_SIZE = 524288;

    private static class ByteArrayDataInputStream implements ByteArrayDataInput {
        final DataInput input;

        ByteArrayDataInputStream(ByteArrayInputStream r2) {
            this.input = new DataInputStream(r2);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public boolean readBoolean() {
            return this.input.readBoolean();
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public byte readByte() {
            return this.input.readByte();
        L7:
            e = move-exception;
            throw new IllegalStateException(e);
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public char readChar() {
            return this.input.readChar();
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public double readDouble() {
            return this.input.readDouble();
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public float readFloat() {
            return this.input.readFloat();
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public void readFully(byte[] r3) {
            this.input.readFully(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public void readFully(byte[] r3, int r4, int r5) {
            this.input.readFully(r3, r4, r5);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public int readInt() {
            return this.input.readInt();
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        @CheckForNull
        public String readLine() {
            return this.input.readLine();
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public long readLong() {
            return this.input.readLong();
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public short readShort() {
            return this.input.readShort();
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public String readUTF() {
            return this.input.readUTF();
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public int readUnsignedByte() {
            return this.input.readUnsignedByte();
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public int readUnsignedShort() {
            return this.input.readUnsignedShort();
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataInput, java.io.DataInput
        public int skipBytes(int r3) {
            return this.input.skipBytes(r3);
        L4:
            e = move-exception;
            throw new IllegalStateException(e);
        }
    }

    private static class ByteArrayDataOutputStream implements ByteArrayDataOutput {
        final ByteArrayOutputStream byteArrayOutputStream;
        final DataOutput output;

        ByteArrayDataOutputStream(ByteArrayOutputStream r2) {
            this.byteArrayOutputStream = r2;
            this.output = new DataOutputStream(r2);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput
        public byte[] toByteArray() {
            return this.byteArrayOutputStream.toByteArray();
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void write(int r3) {
            this.output.write(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void write(byte[] r3) {
            this.output.write(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void write(byte[] r3, int r4, int r5) {
            this.output.write(r3, r4, r5);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void writeBoolean(boolean r3) {
            this.output.writeBoolean(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void writeByte(int r3) {
            this.output.writeByte(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void writeBytes(String r3) {
            this.output.writeBytes(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void writeChar(int r3) {
            this.output.writeChar(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void writeChars(String r3) {
            this.output.writeChars(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void writeDouble(double r3) {
            this.output.writeDouble(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void writeFloat(float r3) {
            this.output.writeFloat(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void writeInt(int r3) {
            this.output.writeInt(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void writeLong(long r3) {
            this.output.writeLong(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void writeShort(int r3) {
            this.output.writeShort(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }

        @Override // com.google.common.p003io.ByteArrayDataOutput, java.io.DataOutput
        public void writeUTF(String r3) {
            this.output.writeUTF(r3);     // Catch: IOException -> L4
            return;
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }
    }

    private static final class LimitedInputStream extends FilterInputStream {
        private long left;
        private long mark;

        LimitedInputStream(InputStream r3, long r4) {
            super(r3);
            this.mark = -1;
            Preconditions.checkNotNull(r3);
            if (r4 < 0) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "limit must be non-negative");
            this.left = r4;
            return;
        L5:
            r0 = false;
            goto L6
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int available() throws IOException {
            return (int) Math.min(this.in.available(), this.left);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public synchronized void mark(int r3) {
            monitor-enter(this);
            this.in.mark(r3);     // Catch: Throwable -> L6
            this.mark = this.left;     // Catch: Throwable -> L6
            monitor-exit(this);
            return;
        L6:
            th = move-exception;
            throw th;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            if (this.left != 0) goto L5;
            return -1;
        L5:
            int r0 = this.in.read();
            if (r0 == (-1)) goto L8;
            this.left--;
        L8:
            return r0;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] r7, int r8, int r9) throws IOException {
            if (this.left != 0) goto L5;
            return -1;
        L5:
            int r0 = this.in.read(r7, r8, (int) Math.min(r9, this.left));
            if (r0 == (-1)) goto L8;
            this.left -= (long) r0;
        L8:
            return r0;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public synchronized void reset() throws IOException {
            monitor-enter(this);
        L14:
            th = move-exception;
            throw th;
        L4:
            if (this.in.markSupported() == false) goto L13;
            if (this.mark == (-1)) goto L11;
            this.in.reset();     // Catch: Throwable -> L14
            this.left = this.mark;     // Catch: Throwable -> L14
            monitor-exit(this);
            return;
        L11:
            throw new IOException("Mark not set");     // Catch: Throwable -> L14
        L13:
            throw new IOException("Mark not supported");     // Catch: Throwable -> L14
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public long skip(long r5) throws IOException {
            long r0 = this.in.skip(Math.min(r5, this.left));
            this.left -= r0;
            return r0;
        }
    }

    static {
        NULL_OUTPUT_STREAM = new C08461();
    }

    private ByteStreams() {
    }

    private static byte[] combineBuffers(Queue<byte[]> r6, int r7) {
        if (r6.isEmpty() == true) goto L5;
        byte[] r0 = r6.remove();
        if (r0.length != r7) goto L9;
        return r0;
    L9:
        int r2 = r7 - r0.length;
        byte[] r1 = Arrays.copyOf(r0, r7);
    L10:
        if (r2 <= 0) goto L12;
        byte[] r3 = r6.remove();
        int r4 = Math.min(r2, r3.length);
        System.arraycopy(r3, 0, r1, r7 - r2, r4);
        r2 = r2 - r4;
        goto L10
    L12:
        return r1;
    L5:
        return new byte[0];
    }

    public static long copy(InputStream r6, OutputStream r7) throws IOException {
        Preconditions.checkNotNull(r6);
        Preconditions.checkNotNull(r7);
        byte[] r0 = createBuffer();
        long r1 = 0;
    L3:
        int r3 = r6.read(r0);
        if (r3 == (-1)) goto L5;
        r7.write(r0, 0, r3);
        r1 = r1 + ((long) r3);
        goto L3
    L5:
        return r1;
    }

    public static long copy(ReadableByteChannel r11, WritableByteChannel r12) throws IOException {
        Preconditions.checkNotNull(r11);
        Preconditions.checkNotNull(r12);
        if ((r11 instanceof FileChannel) == false) goto L12;
        FileChannel r1 = (FileChannel) r11;
        long r7 = r1.position();
        long r2 = r7;
    L5:
        WritableByteChannel r6 = r12;
        long r4 = r1.transferTo(r2, PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED, r6);
        r2 = r2 + r4;
        r1.position(r2);
        if (r4 > 0) goto L11;
        if (r2 < r1.size()) goto L11;
        return r2 - r7;
    L11:
        r12 = r6;
        goto L5
    L12:
        ByteBuffer r13 = ByteBuffer.wrap(createBuffer());
        long r0 = 0;
    L14:
        if (r11.read(r13) == (-1)) goto L20;
        Java8Compatibility.flip(r13);
    L17:
        if (r13.hasRemaining() == false) goto L19;
        r0 = r0 + ((long) r12.write(r13));
        goto L17
    L19:
        Java8Compatibility.clear(r13);
        goto L14
    L20:
        return r0;
    }

    static byte[] createBuffer() {
        return new byte[8192];
    }

    public static long exhaust(InputStream r9) throws IOException {
        long r0 = 0;
        byte[] r2 = createBuffer();
    L3:
        long r3 = r9.read(r2);
        if (r3 == (-1)) goto L6;
        r0 = r0 + r3;
        goto L3
    L6:
        return r0;
    }

    public static InputStream limit(InputStream r1, long r2) {
        return new LimitedInputStream(r1, r2);
    }

    public static ByteArrayDataInput newDataInput(ByteArrayInputStream r2) {
        return new ByteArrayDataInputStream((ByteArrayInputStream) Preconditions.checkNotNull(r2));
    }

    public static ByteArrayDataInput newDataInput(byte[] r1) {
        return newDataInput(new ByteArrayInputStream(r1));
    }

    public static ByteArrayDataInput newDataInput(byte[] r2, int r3) {
        Preconditions.checkPositionIndex(r3, r2.length);
        return newDataInput(new ByteArrayInputStream(r2, r3, r2.length - r3));
    }

    public static ByteArrayDataOutput newDataOutput() {
        return newDataOutput(new ByteArrayOutputStream());
    }

    public static ByteArrayDataOutput newDataOutput(int r4) {
        if (r4 < 0) goto L6;
        return newDataOutput(new ByteArrayOutputStream(r4));
    L6:
        throw new IllegalArgumentException(String.format("Invalid size: %s", new Object[]{Integer.valueOf(r4)}));
    }

    public static ByteArrayDataOutput newDataOutput(ByteArrayOutputStream r2) {
        return new ByteArrayDataOutputStream((ByteArrayOutputStream) Preconditions.checkNotNull(r2));
    }

    public static OutputStream nullOutputStream() {
        return NULL_OUTPUT_STREAM;
    }

    public static int read(InputStream r4, byte[] r5, int r6, int r7) throws IOException {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        if (r7 < 0) goto L12;
        Preconditions.checkPositionIndexes(r6, r6 + r7, r5.length);
        int r0 = 0;
    L5:
        if (r0 >= r7) goto L10;
        int r1 = r4.read(r5, r6 + r0, r7 - r0);
        if (r1 == (-1)) goto L10;
        r0 = r0 + r1;
    L10:
        return r0;
    L12:
        throw new IndexOutOfBoundsException(String.format("len (%s) cannot be negative", new Object[]{Integer.valueOf(r7)}));
    }

    @ParametricNullness
    public static <T> T readBytes(InputStream r3, ByteProcessor<T> r4) throws IOException {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        byte[] r0 = createBuffer();
    L3:
        int r1 = r3.read(r0);
        if (r1 == (-1)) goto L8;
        if (r4.processBytes(r0, 0, r1) == true) goto L3;
    L8:
        return r4.getResult();
    }

    public static void readFully(InputStream r2, byte[] r3) throws IOException {
        readFully(r2, r3, 0, r3.length);
    }

    public static void readFully(InputStream r4, byte[] r5, int r6, int r7) throws IOException {
        int r0 = read(r4, r5, r6, r7);
        if (r0 != r7) goto L6;
        return;
    L6:
        throw new EOFException("reached end of stream after reading " + r0 + " bytes; " + r7 + " bytes expected");
    }

    public static void skipFully(InputStream r5, long r6) throws IOException {
        long r0 = skipUpTo(r5, r6);
        if (r0 < r6) goto L6;
        return;
    L6:
        throw new EOFException("reached end of stream after skipping " + r0 + " bytes; " + r6 + " bytes expected");
    }

    private static long skipSafely(InputStream r3, long r4) throws IOException {
        int r0 = r3.available();
        if (r0 != 0) goto L6;
        return 0;
    L6:
        return r3.skip(Math.min(r0, r4));
    }

    static long skipUpTo(InputStream r12, long r13) throws IOException {
        long r0 = 0;
        byte[] r2 = null;
    L4:
        if (r0 >= r13) goto L14;
        long r3 = r13 - r0;
        long r5 = skipSafely(r12, r3);
        if (r5 != 0) goto L13;
        int r7 = (int) Math.min(r3, PlaybackStateCompat.ACTION_PLAY_FROM_URI);
        if (r2 != null) goto L10;
        r2 = new byte[r7];
    L10:
        long r8 = r12.read(r2, 0, r7);
        r5 = r8;
        if (r8 == (-1)) goto L14;
    L13:
        r0 = r0 + r5;
    L14:
        return r0;
    }

    public static byte[] toByteArray(InputStream r2) throws IOException {
        Preconditions.checkNotNull(r2);
        return toByteArrayInternal(r2, new ArrayDeque(20), 0);
    }

    static byte[] toByteArray(InputStream r8, long r9) throws IOException {
        if (r9 < 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "expectedSize (%s) must be non-negative", r9);
        if (r9 > 2147483639) goto L22;
        byte[] r1 = new byte[(int) r9];
        int r3 = (int) r9;
    L10:
        if (r3 <= 0) goto L16;
        int r5 = ((int) r9) - r3;
        int r6 = r8.read(r1, r5, r3);
        if (r6 == (-1)) goto L14;
        r3 = r3 - r6;
        goto L10
    L14:
        return Arrays.copyOf(r1, r5);
    L16:
        int r7 = r8.read();
        if (r7 != (-1)) goto L19;
        return r1;
    L19:
        ArrayDeque r4 = new ArrayDeque(22);
        r4.add(r1);
        r4.add(new byte[]{(byte) r7});
        return toByteArrayInternal(r8, r4, r1.length + 1);
    L22:
        throw new OutOfMemoryError(r9 + " bytes is too large to fit in a byte array");
    L5:
        r0 = false;
        goto L6
    }

    private static byte[] toByteArrayInternal(InputStream r7, Queue<byte[]> r8, int r9) throws IOException {
        int r2 = Math.min(8192, Math.max(128, Integer.highestOneBit(r9) * 2));
    L4:
        if (r9 >= MAX_ARRAY_LEN) goto L19;
        byte[] r4 = new byte[Math.min(r2, MAX_ARRAY_LEN - r9)];
        r8.add(r4);
        int r5 = 0;
    L7:
        if (r5 >= r4.length) goto L14;
        int r6 = r7.read(r4, r5, r4.length - r5);
        if (r6 == (-1)) goto L11;
        r5 = r5 + r6;
        r9 = r9 + r6;
        goto L7
    L11:
        return combineBuffers(r8, r9);
    L14:
        if (r2 >= 4096) goto L16;
        int r3 = 4;
    L17:
        r2 = IntMath.saturatedMultiply(r2, r3);
        goto L4
    L16:
        r3 = 2;
        goto L17
    L19:
        if (r7.read() != (-1)) goto L23;
        return combineBuffers(r8, MAX_ARRAY_LEN);
    L23:
        throw new OutOfMemoryError("input is too large to fit in a byte array");
    }
}
