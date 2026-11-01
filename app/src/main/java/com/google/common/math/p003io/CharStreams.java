package com.google.common.p003io;

import com.google.common.base.Preconditions;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class CharStreams {
    private static final int DEFAULT_BUF_SIZE = 2048;

    private static final class NullWriter extends Writer {
        private static final NullWriter INSTANCE = null;

        static {
            INSTANCE = new NullWriter();
        }

        private NullWriter() {
        }

        static /* synthetic */ NullWriter access$000() {
            return INSTANCE;
        }

        @Override // java.io.Writer, java.lang.Appendable
        public Writer append(char r1) {
            return this;
        }

        @Override // java.io.Writer, java.lang.Appendable
        public Writer append(@CheckForNull CharSequence r1) {
            return this;
        }

        @Override // java.io.Writer, java.lang.Appendable
        public Writer append(@CheckForNull CharSequence r2, int r3, int r4) {
            if (r2 != null) goto L4;
            int r0 = "null".length();
        L5:
            Preconditions.checkPositionIndexes(r3, r4, r0);
            return this;
        L4:
            r0 = r2.length();
            goto L5
        }

        @Override // java.io.Writer, java.lang.Appendable
        public /* bridge */ /* synthetic */ Appendable append(char r1) throws IOException {
            return append(r1);
        }

        @Override // java.io.Writer, java.lang.Appendable
        public /* bridge */ /* synthetic */ Appendable append(@CheckForNull CharSequence r1) throws IOException {
            return append(r1);
        }

        @Override // java.io.Writer, java.lang.Appendable
        public /* bridge */ /* synthetic */ Appendable append(@CheckForNull CharSequence r1, int r2, int r3) throws IOException {
            return append(r1, r2, r3);
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
        }

        public String toString() {
            return "CharStreams.nullWriter()";
        }

        @Override // java.io.Writer
        public void write(int r1) {
        }

        @Override // java.io.Writer
        public void write(String r1) {
            Preconditions.checkNotNull(r1);
        }

        @Override // java.io.Writer
        public void write(String r3, int r4, int r5) {
            Preconditions.checkPositionIndexes(r4, r4 + r5, r3.length());
        }

        @Override // java.io.Writer
        public void write(char[] r1) {
            Preconditions.checkNotNull(r1);
        }

        @Override // java.io.Writer
        public void write(char[] r3, int r4, int r5) {
            Preconditions.checkPositionIndexes(r4, r4 + r5, r3.length);
        }
    }

    private CharStreams() {
    }

    public static Writer asWriter(Appendable r1) {
        if ((r1 instanceof Writer) == false) goto L7;
        return (Writer) r1;
    L7:
        return new AppendableWriter(r1);
    }

    public static long copy(Readable r5, Appendable r6) throws IOException {
        if ((r5 instanceof Reader) == true) goto L5;
        Preconditions.checkNotNull(r5);
        Preconditions.checkNotNull(r6);
        long r0 = 0;
        CharBuffer r2 = createBuffer();
    L12:
        if (r5.read(r2) == (-1)) goto L14;
        Java8Compatibility.flip(r2);
        r6.append(r2);
        r0 = r0 + ((long) r2.remaining());
        Java8Compatibility.clear(r2);
        goto L12
    L14:
        return r0;
    L5:
        if ((r6 instanceof StringBuilder) == false) goto L9;
        return copyReaderToBuilder((Reader) r5, (StringBuilder) r6);
    L9:
        return copyReaderToWriter((Reader) r5, asWriter(r6));
    }

    static long copyReaderToBuilder(Reader r7, StringBuilder r8) throws IOException {
        Preconditions.checkNotNull(r7);
        Preconditions.checkNotNull(r8);
        char[] r0 = new char[2048];
        long r1 = 0;
    L3:
        int r3 = r7.read(r0);
        if (r3 == (-1)) goto L6;
        r8.append(r0, 0, r3);
        r1 = r1 + ((long) r3);
        goto L3
    L6:
        return r1;
    }

    static long copyReaderToWriter(Reader r7, Writer r8) throws IOException {
        Preconditions.checkNotNull(r7);
        Preconditions.checkNotNull(r8);
        char[] r0 = new char[2048];
        long r1 = 0;
    L3:
        int r3 = r7.read(r0);
        if (r3 == (-1)) goto L6;
        r8.write(r0, 0, r3);
        r1 = r1 + ((long) r3);
        goto L3
    L6:
        return r1;
    }

    static CharBuffer createBuffer() {
        return CharBuffer.allocate(2048);
    }

    public static long exhaust(Readable r9) throws IOException {
        long r0 = 0;
        CharBuffer r2 = createBuffer();
    L3:
        long r3 = r9.read(r2);
        if (r3 == (-1)) goto L6;
        r0 = r0 + r3;
        Java8Compatibility.clear(r2);
        goto L3
    L6:
        return r0;
    }

    public static Writer nullWriter() {
        return NullWriter.access$000();
    }

    @ParametricNullness
    public static <T> T readLines(Readable r3, LineProcessor<T> r4) throws IOException {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        LineReader r0 = new LineReader(r3);
    L3:
        String r1 = r0.readLine();
        if (r1 == null) goto L8;
        if (r4.processLine(r1) == true) goto L3;
    L8:
        return r4.getResult();
    }

    public static List<String> readLines(Readable r4) throws IOException {
        ArrayList r0 = new ArrayList();
        LineReader r1 = new LineReader(r4);
    L3:
        String r2 = r1.readLine();
        if (r2 == null) goto L6;
        r0.add(r2);
        goto L3
    L6:
        return r0;
    }

    public static void skipFully(Reader r4, long r5) throws IOException {
        Preconditions.checkNotNull(r4);
    L4:
        if (r5 <= 0) goto L10;
        long r2 = r4.skip(r5);
        if (r2 == 0) goto L9;
        r5 = r5 - r2;
        goto L4
    L9:
        throw new EOFException();
    }

    public static String toString(Readable r1) throws IOException {
        return toStringBuilder(r1).toString();
    }

    private static StringBuilder toStringBuilder(Readable r2) throws IOException {
        StringBuilder r0 = new StringBuilder();
        if ((r2 instanceof Reader) == false) goto L5;
        copyReaderToBuilder((Reader) r2, r0);
    L6:
        return r0;
    L5:
        copy(r2, r0);
        goto L6
    }
}
