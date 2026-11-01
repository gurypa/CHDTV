package com.google.common.p003io;

import com.google.common.base.Ascii;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ByteSource {

    class AsCharSource extends CharSource {
        final Charset charset;
        final /* synthetic */ ByteSource this$0;

        AsCharSource(ByteSource r2, Charset r3) {
            this.this$0 = r2;
            this.charset = (Charset) Preconditions.checkNotNull(r3);
        }

        @Override // com.google.common.p003io.CharSource
        public ByteSource asByteSource(Charset r2) {
            if (r2.equals(this.charset) == false) goto L7;
            return this.this$0;
        L7:
            return super.asByteSource(r2);
        }

        @Override // com.google.common.p003io.CharSource
        public Reader openStream() throws IOException {
            return new InputStreamReader(this.this$0.openStream(), this.charset);
        }

        @Override // com.google.common.p003io.CharSource
        public String read() throws IOException {
            return new String(this.this$0.read(), this.charset);
        }

        public String toString() {
            return this.this$0.toString() + ".asCharSource(" + this.charset + ")";
        }
    }

    private static class ByteArrayByteSource extends ByteSource {
        final byte[] bytes;
        final int length;
        final int offset;

        ByteArrayByteSource(byte[] r3) {
            this(r3, 0, r3.length);
        }

        ByteArrayByteSource(byte[] r1, int r2, int r3) {
            this.bytes = r1;
            this.offset = r2;
            this.length = r3;
        }

        @Override // com.google.common.p003io.ByteSource
        public long copyTo(OutputStream r4) throws IOException {
            r4.write(this.bytes, this.offset, this.length);
            return this.length;
        }

        @Override // com.google.common.p003io.ByteSource
        public HashCode hash(HashFunction r4) throws IOException {
            return r4.hashBytes(this.bytes, this.offset, this.length);
        }

        @Override // com.google.common.p003io.ByteSource
        public boolean isEmpty() {
            if (this.length != 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.p003io.ByteSource
        public InputStream openBufferedStream() {
            return openStream();
        }

        @Override // com.google.common.p003io.ByteSource
        public InputStream openStream() {
            return new ByteArrayInputStream(this.bytes, this.offset, this.length);
        }

        @Override // com.google.common.p003io.ByteSource
        @ParametricNullness
        public <T> T read(ByteProcessor<T> r4) throws IOException {
            r4.processBytes(this.bytes, this.offset, this.length);
            return r4.getResult();
        }

        @Override // com.google.common.p003io.ByteSource
        public byte[] read() {
            return Arrays.copyOfRange(this.bytes, this.offset, this.offset + this.length);
        }

        @Override // com.google.common.p003io.ByteSource
        public long size() {
            return this.length;
        }

        @Override // com.google.common.p003io.ByteSource
        public Optional<Long> sizeIfKnown() {
            return Optional.m185of(Long.valueOf(this.length));
        }

        @Override // com.google.common.p003io.ByteSource
        public ByteSource slice(long r7, long r9) {
            boolean r3 = true;
            if (r7 < 0) goto L5;
            boolean r2 = true;
        L6:
            Preconditions.checkArgument(r2, "offset (%s) may not be negative", r7);
            if (r9 >= 0) goto L10;
            r3 = false;
        L10:
            Preconditions.checkArgument(r3, "length (%s) may not be negative", r9);
            long r8 = Math.min(r7, this.length);
            long r10 = Math.min(r9, ((long) this.length) - r8);
            return new ByteArrayByteSource(this.bytes, this.offset + ((int) r8), (int) r10);
        L5:
            r2 = false;
            goto L6
        }

        public String toString() {
            return "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(this.bytes, this.offset, this.length), 30, "...") + ")";
        }
    }

    private static final class ConcatenatedByteSource extends ByteSource {
        final Iterable<? extends ByteSource> sources;

        ConcatenatedByteSource(Iterable<? extends ByteSource> r2) {
            this.sources = (Iterable) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.p003io.ByteSource
        public boolean isEmpty() throws IOException {
            Iterator<? extends ByteSource> r0 = this.sources.iterator();
        L4:
            if (r0.hasNext() == false) goto L10;
            if (r0.next().isEmpty() == true) goto L4;
            return false;
        L10:
            return true;
        }

        @Override // com.google.common.p003io.ByteSource
        public InputStream openStream() throws IOException {
            return new MultiInputStream(this.sources.iterator());
        }

        @Override // com.google.common.p003io.ByteSource
        public long size() throws IOException {
            long r0 = 0;
            Iterator<? extends ByteSource> r2 = this.sources.iterator();
        L4:
            if (r2.hasNext() == false) goto L10;
            r0 = r0 + r2.next().size();
            if (r0 >= 0) goto L4;
            return Long.MAX_VALUE;
        L10:
            return r0;
        }

        @Override // com.google.common.p003io.ByteSource
        public Optional<Long> sizeIfKnown() {
            if ((this.sources instanceof Collection) == false) goto L5;
            long r0 = 0;
            Iterator<? extends ByteSource> r2 = this.sources.iterator();
        L8:
            if (r2.hasNext() == false) goto L19;
            Optional<Long> r4 = r2.next().sizeIfKnown();
            if (r4.isPresent() == false) goto L12;
            r0 = r0 + r4.get().longValue();
            if (r0 >= 0) goto L8;
            return Optional.m185of(Long.MAX_VALUE);
        L12:
            return Optional.absent();
        L19:
            return Optional.m185of(Long.valueOf(r0));
        L5:
            return Optional.absent();
        }

        public String toString() {
            return "ByteSource.concat(" + this.sources + ")";
        }
    }

    private static final class EmptyByteSource extends ByteArrayByteSource {
        static final EmptyByteSource INSTANCE = null;

        static {
            INSTANCE = new EmptyByteSource();
        }

        EmptyByteSource() {
            super(new byte[0]);
        }

        @Override // com.google.common.p003io.ByteSource
        public CharSource asCharSource(Charset r2) {
            Preconditions.checkNotNull(r2);
            return CharSource.empty();
        }

        @Override // com.google.common.io.ByteSource.ByteArrayByteSource, com.google.common.p003io.ByteSource
        public byte[] read() {
            return this.bytes;
        }

        @Override // com.google.common.io.ByteSource.ByteArrayByteSource
        public String toString() {
            return "ByteSource.empty()";
        }
    }

    private final class SlicedByteSource extends ByteSource {
        final long length;
        final long offset;
        final /* synthetic */ ByteSource this$0;

        SlicedByteSource(ByteSource r6, long r7, long r9) {
            this.this$0 = r6;
            boolean r2 = true;
            if (r7 < 0) goto L5;
            boolean r8 = true;
        L6:
            Preconditions.checkArgument(r8, "offset (%s) may not be negative", r7);
            if (r9 >= 0) goto L10;
            r2 = false;
        L10:
            Preconditions.checkArgument(r2, "length (%s) may not be negative", r9);
            this.offset = r7;
            this.length = r9;
            return;
        L5:
            r8 = false;
            goto L6
        }

        private InputStream sliceStream(InputStream r5) throws IOException {
            if (this.offset <= 0) goto L17;
            if (ByteStreams.skipUpTo(r5, this.offset) >= this.offset) goto L17;
            r5.close();
            return new ByteArrayInputStream(new byte[0]);
        L9:
            th = move-exception;
            Closer r1 = Closer.create();
            r1.register(r5);
        L13:
            th = move-exception;
            r1.close();
            throw th;
        L12:
            throw r1.rethrow(th);     // Catch: Throwable -> L13
        L17:
            return ByteStreams.limit(r5, this.length);
        }

        @Override // com.google.common.p003io.ByteSource
        public boolean isEmpty() throws IOException {
            if (this.length != 0) goto L5;
        L8:
            return true;
        L5:
            if (super.isEmpty() == true) goto L8;
            return false;
        }

        @Override // com.google.common.p003io.ByteSource
        public InputStream openBufferedStream() throws IOException {
            return sliceStream(this.this$0.openBufferedStream());
        }

        @Override // com.google.common.p003io.ByteSource
        public InputStream openStream() throws IOException {
            return sliceStream(this.this$0.openStream());
        }

        @Override // com.google.common.p003io.ByteSource
        public Optional<Long> sizeIfKnown() {
            Optional<Long> r0 = this.this$0.sizeIfKnown();
            if (r0.isPresent() == false) goto L7;
            long r1 = r0.get().longValue();
            long r3 = Math.min(this.offset, r1);
            return Optional.m185of(Long.valueOf(Math.min(this.length, r1 - r3)));
        L7:
            return Optional.absent();
        }

        @Override // com.google.common.p003io.ByteSource
        public ByteSource slice(long r9, long r11) {
            boolean r3 = true;
            if (r9 < 0) goto L5;
            boolean r2 = true;
        L6:
            Preconditions.checkArgument(r2, "offset (%s) may not be negative", r9);
            if (r11 >= 0) goto L10;
            r3 = false;
        L10:
            Preconditions.checkArgument(r3, "length (%s) may not be negative", r11);
            long r4 = this.length - r9;
            if (r4 > 0) goto L14;
            return ByteSource.empty();
        L14:
            return this.this$0.slice(this.offset + r9, Math.min(r11, r4));
        L5:
            r2 = false;
            goto L6
        }

        public String toString() {
            return this.this$0.toString() + ".slice(" + this.offset + ", " + this.length + ")";
        }
    }

    protected ByteSource() {
    }

    public static ByteSource concat(Iterable<? extends ByteSource> r1) {
        return new ConcatenatedByteSource(r1);
    }

    public static ByteSource concat(Iterator<? extends ByteSource> r1) {
        return concat(ImmutableList.copyOf(r1));
    }

    public static ByteSource concat(ByteSource... r1) {
        return concat(ImmutableList.copyOf(r1));
    }

    private long countBySkipping(InputStream r9) throws IOException {
        long r0 = 0;
    L3:
        long r2 = ByteStreams.skipUpTo(r9, 2147483647L);
        if (r2 <= 0) goto L6;
        r0 = r0 + r2;
        goto L3
    L6:
        return r0;
    }

    public static ByteSource empty() {
        return EmptyByteSource.INSTANCE;
    }

    public static ByteSource wrap(byte[] r1) {
        return new ByteArrayByteSource(r1);
    }

    public CharSource asCharSource(Charset r2) {
        return new AsCharSource(this, r2);
    }

    public boolean contentEquals(ByteSource r10) throws IOException {
        Preconditions.checkNotNull(r10);
        byte[] r0 = ByteStreams.createBuffer();
        byte[] r1 = ByteStreams.createBuffer();
        Closer r2 = Closer.create();
        InputStream r3 = (InputStream) r2.register(openStream());     // Catch: Throwable -> L16
        InputStream r4 = (InputStream) r2.register(r10.openStream());     // Catch: Throwable -> L16
    L4:
        int r5 = ByteStreams.read(r3, r0, 0, r0.length);     // Catch: Throwable -> L16
        if (r5 != ByteStreams.read(r4, r1, 0, r1.length)) goto L14;
        if (Arrays.equals(r0, r1) == false) goto L14;
        if (r5 == r0.length) goto L4;
        r2.close();
        return true;
    L14:
        r2.close();
        return false;
    L16:
        th = move-exception;
    L19:
        th = move-exception;
        r2.close();
        throw th;
    L18:
        throw r2.rethrow(th);     // Catch: Throwable -> L19
    }

    public long copyTo(ByteSink r6) throws IOException {
        Preconditions.checkNotNull(r6);
        Closer r0 = Closer.create();
        long r3 = ByteStreams.copy((InputStream) r0.register(openStream()), (OutputStream) r0.register(r6.openStream()));     // Catch: Throwable -> L6
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

    public long copyTo(OutputStream r5) throws IOException {
        Preconditions.checkNotNull(r5);
        Closer r0 = Closer.create();
        long r2 = ByteStreams.copy((InputStream) r0.register(openStream()), r5);     // Catch: Throwable -> L6
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

    public HashCode hash(HashFunction r3) throws IOException {
        Hasher r0 = r3.newHasher();
        copyTo(Funnels.asOutputStream(r0));
        return r0.hash();
    }

    public boolean isEmpty() throws IOException {
        Optional<Long> r0 = sizeIfKnown();
        boolean r2 = true;
        if (r0.isPresent() == true) goto L5;
        Closer r1 = Closer.create();
        if (((InputStream) r1.register(openStream())).read() == (-1)) goto L15;
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

    public InputStream openBufferedStream() throws IOException {
        InputStream r0 = openStream();
        if ((r0 instanceof BufferedInputStream) == false) goto L6;
        return (BufferedInputStream) r0;
    L6:
        return new BufferedInputStream(r0);
    }

    public abstract InputStream openStream() throws IOException;

    @ParametricNullness
    public <T> T read(ByteProcessor<T> r4) throws IOException {
        Preconditions.checkNotNull(r4);
        Closer r0 = Closer.create();
        T r2 = (T) ByteStreams.readBytes((InputStream) r0.register(openStream()), r4);
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

    public byte[] read() throws IOException {
        Closer r0 = Closer.create();
        InputStream r1 = (InputStream) r0.register(openStream());     // Catch: Throwable -> L9
        Optional<Long> r2 = sizeIfKnown();     // Catch: Throwable -> L9
        if (r2.isPresent() == false) goto L6;
        byte[] r3 = ByteStreams.toByteArray(r1, r2.get().longValue());     // Catch: Throwable -> L9
    L7:
        r0.close();
        return r3;
    L6:
        r3 = ByteStreams.toByteArray(r1);     // Catch: Throwable -> L9
    L9:
        th = move-exception;
    L12:
        th = move-exception;
        r0.close();
        throw th;
    L11:
        throw r0.rethrow(th);     // Catch: Throwable -> L12
    }

    public long size() throws IOException {
        Optional<Long> r0 = sizeIfKnown();
        if (r0.isPresent() == true) goto L5;
        Closer r1 = Closer.create();
        long r3 = countBySkipping((InputStream) r1.register(openStream()));     // Catch: Throwable -> L10 IOException -> L13
        r1.close();
        return r3;
    L10:
        th = move-exception;
        r1.close();
        throw th;
    L14:
        r1.close();
        Closer r2 = Closer.create();
        long r4 = ByteStreams.exhaust((InputStream) r2.register(openStream()));     // Catch: Throwable -> L18
        r2.close();
        return r4;
    L18:
        th = move-exception;
    L21:
        th = move-exception;
        r2.close();
        throw th;
    L20:
        throw r2.rethrow(th);     // Catch: Throwable -> L21
    L5:
        return r0.get().longValue();
    }

    public Optional<Long> sizeIfKnown() {
        return Optional.absent();
    }

    public ByteSource slice(long r7, long r9) {
        return new SlicedByteSource(this, r7, r9);
    }
}
