package com.google.common.p003io;

import com.google.common.base.Ascii;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class CharSource {

    private final class AsByteSource extends ByteSource {
        final Charset charset;
        final /* synthetic */ CharSource this$0;

        AsByteSource(CharSource r1, Charset r2) {
            this.this$0 = r1;
            this.charset = (Charset) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.p003io.ByteSource
        public CharSource asCharSource(Charset r2) {
            if (r2.equals(this.charset) == false) goto L7;
            return this.this$0;
        L7:
            return super.asCharSource(r2);
        }

        @Override // com.google.common.p003io.ByteSource
        public InputStream openStream() throws IOException {
            return new ReaderInputStream(this.this$0.openStream(), this.charset, 8192);
        }

        public String toString() {
            return this.this$0.toString() + ".asByteSource(" + this.charset + ")";
        }
    }

    private static class CharSequenceCharSource extends CharSource {
        private static final Splitter LINE_SPLITTER = null;
        protected final CharSequence seq;

        static {
            LINE_SPLITTER = Splitter.onPattern("\r\n|\n|\r");
        }

        protected CharSequenceCharSource(CharSequence r2) {
            this.seq = (CharSequence) Preconditions.checkNotNull(r2);
        }

        static /* synthetic */ Splitter access$100() {
            return LINE_SPLITTER;
        }

        private Iterator<String> linesIterator() {
            return new C08471(this);
        }

        @Override // com.google.common.p003io.CharSource
        public boolean isEmpty() {
            if (this.seq.length() != 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.p003io.CharSource
        public long length() {
            return this.seq.length();
        }

        @Override // com.google.common.p003io.CharSource
        public Optional<Long> lengthIfKnown() {
            return Optional.m185of(Long.valueOf(this.seq.length()));
        }

        @Override // com.google.common.p003io.CharSource
        public Reader openStream() {
            return new CharSequenceReader(this.seq);
        }

        @Override // com.google.common.p003io.CharSource
        public String read() {
            return this.seq.toString();
        }

        @Override // com.google.common.p003io.CharSource
        @CheckForNull
        public String readFirstLine() {
            Iterator<String> r0 = linesIterator();
            if (r0.hasNext() == true) goto L7;
            return null;
        L7:
            return r0.next();
        }

        @Override // com.google.common.p003io.CharSource
        public ImmutableList<String> readLines() {
            return ImmutableList.copyOf(linesIterator());
        }

        @Override // com.google.common.p003io.CharSource
        @ParametricNullness
        public <T> T readLines(LineProcessor<T> r3) throws IOException {
            Iterator<String> r0 = linesIterator();
        L4:
            if (r0.hasNext() == false) goto L8;
            if (r3.processLine(r0.next()) == true) goto L4;
        L8:
            return r3.getResult();
        }

        public String toString() {
            return "CharSource.wrap(" + Ascii.truncate(this.seq, 30, "...") + ")";
        }
    }

    private static final class ConcatenatedCharSource extends CharSource {
        private final Iterable<? extends CharSource> sources;

        ConcatenatedCharSource(Iterable<? extends CharSource> r2) {
            this.sources = (Iterable) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.p003io.CharSource
        public boolean isEmpty() throws IOException {
            Iterator<? extends CharSource> r0 = this.sources.iterator();
        L4:
            if (r0.hasNext() == false) goto L10;
            if (r0.next().isEmpty() == true) goto L4;
            return false;
        L10:
            return true;
        }

        @Override // com.google.common.p003io.CharSource
        public long length() throws IOException {
            long r0 = 0;
            Iterator<? extends CharSource> r2 = this.sources.iterator();
        L4:
            if (r2.hasNext() == false) goto L6;
            r0 = r0 + r2.next().length();
            goto L4
        L6:
            return r0;
        }

        @Override // com.google.common.p003io.CharSource
        public Optional<Long> lengthIfKnown() {
            long r0 = 0;
            Iterator<? extends CharSource> r2 = this.sources.iterator();
        L4:
            if (r2.hasNext() == false) goto L11;
            Optional<Long> r4 = r2.next().lengthIfKnown();
            if (r4.isPresent() == false) goto L8;
            r0 = r0 + r4.get().longValue();
            goto L4
        L8:
            return Optional.absent();
        L11:
            return Optional.m185of(Long.valueOf(r0));
        }

        @Override // com.google.common.p003io.CharSource
        public Reader openStream() throws IOException {
            return new MultiReader(this.sources.iterator());
        }

        public String toString() {
            return "CharSource.concat(" + this.sources + ")";
        }
    }

    private static final class EmptyCharSource extends StringCharSource {
        private static final EmptyCharSource INSTANCE = null;

        static {
            INSTANCE = new EmptyCharSource();
        }

        private EmptyCharSource() {
            super("");
        }

        static /* synthetic */ EmptyCharSource access$000() {
            return INSTANCE;
        }

        @Override // com.google.common.io.CharSource.CharSequenceCharSource
        public String toString() {
            return "CharSource.empty()";
        }
    }

    private static class StringCharSource extends CharSequenceCharSource {
        protected StringCharSource(String r1) {
            super(r1);
        }

        @Override // com.google.common.p003io.CharSource
        public long copyTo(CharSink r5) throws IOException {
            Preconditions.checkNotNull(r5);
            Closer r0 = Closer.create();
            ((Writer) r0.register(r5.openStream())).write((String) this.seq);     // Catch: Throwable -> L6
            long r2 = this.seq.length();     // Catch: Throwable -> L6
            r0.close();
            return r2;
        L6:
            th = move-exception;
        L9:
            th = move-exception;
            r0.close();
            throw th;
        L8:
            throw r0.rethrow(th);     // Catch: Throwable -> L9
        }

        @Override // com.google.common.p003io.CharSource
        public long copyTo(Appendable r3) throws IOException {
            r3.append(this.seq);
            return this.seq.length();
        }

        @Override // com.google.common.io.CharSource.CharSequenceCharSource, com.google.common.p003io.CharSource
        public Reader openStream() {
            return new StringReader((String) this.seq);
        }
    }

    protected CharSource() {
    }

    public static CharSource concat(Iterable<? extends CharSource> r1) {
        return new ConcatenatedCharSource(r1);
    }

    public static CharSource concat(Iterator<? extends CharSource> r1) {
        return concat(ImmutableList.copyOf(r1));
    }

    public static CharSource concat(CharSource... r1) {
        return concat(ImmutableList.copyOf(r1));
    }

    private long countBySkipping(Reader r9) throws IOException {
        long r0 = 0;
    L3:
        long r2 = r9.skip(Long.MAX_VALUE);
        if (r2 == 0) goto L6;
        r0 = r0 + r2;
        goto L3
    L6:
        return r0;
    }

    public static CharSource empty() {
        return EmptyCharSource.access$000();
    }

    public static CharSource wrap(CharSequence r2) {
        if ((r2 instanceof String) == false) goto L6;
        return new StringCharSource((String) r2);
    L6:
        return new CharSequenceCharSource(r2);
    }

    public ByteSource asByteSource(Charset r2) {
        return new AsByteSource(this, r2);
    }

    public long copyTo(CharSink r6) throws IOException {
        Preconditions.checkNotNull(r6);
        Closer r0 = Closer.create();
        long r3 = CharStreams.copy((Reader) r0.register(openStream()), (Writer) r0.register(r6.openStream()));     // Catch: Throwable -> L6
        r0.close();
        return r3;
    L6:
        th = move-exception;
    L9:
        th = move-exception;
        r0.close();
        throw th;
    L8:
        throw r0.rethrow(th);     // Catch: Throwable -> L9
    }

    public long copyTo(Appendable r5) throws IOException {
        Preconditions.checkNotNull(r5);
        Closer r0 = Closer.create();
        long r2 = CharStreams.copy((Reader) r0.register(openStream()), r5);     // Catch: Throwable -> L6
        r0.close();
        return r2;
    L6:
        th = move-exception;
    L9:
        th = move-exception;
        r0.close();
        throw th;
    L8:
        throw r0.rethrow(th);     // Catch: Throwable -> L9
    }

    public boolean isEmpty() throws IOException {
        Optional<Long> r0 = lengthIfKnown();
        boolean r2 = true;
        if (r0.isPresent() == true) goto L5;
        Closer r1 = Closer.create();
        if (((Reader) r1.register(openStream())).read() == (-1)) goto L15;
        r2 = false;
    L15:
        r1.close();
        return r2;
    L17:
        th = move-exception;
    L20:
        th = move-exception;
        r1.close();
        throw th;
    L19:
        throw r1.rethrow(th);     // Catch: Throwable -> L20
    L5:
        if (r0.get().longValue() != 0) goto L8;
        return true;
    L8:
        return false;
    }

    public long length() throws IOException {
        Optional<Long> r0 = lengthIfKnown();
        if (r0.isPresent() == true) goto L5;
        Closer r1 = Closer.create();
        long r3 = countBySkipping((Reader) r1.register(openStream()));     // Catch: Throwable -> L10
        r1.close();
        return r3;
    L10:
        th = move-exception;
    L13:
        th = move-exception;
        r1.close();
        throw th;
    L12:
        throw r1.rethrow(th);     // Catch: Throwable -> L13
    L5:
        return r0.get().longValue();
    }

    public Optional<Long> lengthIfKnown() {
        return Optional.absent();
    }

    public BufferedReader openBufferedStream() throws IOException {
        Reader r0 = openStream();
        if ((r0 instanceof BufferedReader) == false) goto L6;
        return (BufferedReader) r0;
    L6:
        return new BufferedReader(r0);
    }

    public abstract Reader openStream() throws IOException;

    public String read() throws IOException {
        Closer r0 = Closer.create();
        String r2 = CharStreams.toString((Reader) r0.register(openStream()));     // Catch: Throwable -> L6
        r0.close();
        return r2;
    L6:
        th = move-exception;
    L9:
        th = move-exception;
        r0.close();
        throw th;
    L8:
        throw r0.rethrow(th);     // Catch: Throwable -> L9
    }

    @CheckForNull
    public String readFirstLine() throws IOException {
        Closer r0 = Closer.create();
        String r2 = ((BufferedReader) r0.register(openBufferedStream())).readLine();     // Catch: Throwable -> L6
        r0.close();
        return r2;
    L6:
        th = move-exception;
    L9:
        th = move-exception;
        r0.close();
        throw th;
    L8:
        throw r0.rethrow(th);     // Catch: Throwable -> L9
    }

    public ImmutableList<String> readLines() throws IOException {
        Closer r0 = Closer.create();
        BufferedReader r1 = (BufferedReader) r0.register(openBufferedStream());     // Catch: Throwable -> L10
        ArrayList r2 = Lists.newArrayList();     // Catch: Throwable -> L10
    L4:
        String r3 = r1.readLine();     // Catch: Throwable -> L10
        if (r3 == null) goto L7;
        r2.add(r3);     // Catch: Throwable -> L10
        goto L4
    L7:
        ImmutableList<String> r4 = ImmutableList.copyOf(r2);     // Catch: Throwable -> L10
        r0.close();
        return r4;
    L10:
        th = move-exception;
    L13:
        th = move-exception;
        r0.close();
        throw th;
    L12:
        throw r0.rethrow(th);     // Catch: Throwable -> L13
    }

    @ParametricNullness
    public <T> T readLines(LineProcessor<T> r4) throws IOException {
        Preconditions.checkNotNull(r4);
        Closer r0 = Closer.create();
        T r2 = (T) CharStreams.readLines((Reader) r0.register(openStream()), r4);
        r0.close();
        return r2;
    L6:
        th = move-exception;
    L9:
        th = move-exception;
        r0.close();
        throw th;
    L8:
        throw r0.rethrow(th);     // Catch: Throwable -> L9
    }
}
