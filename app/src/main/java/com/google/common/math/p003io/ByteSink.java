package com.google.common.p003io;

import com.google.common.base.Preconditions;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ByteSink {

    /* JADX INFO: renamed from: com.google.common.io.ByteSink$1 */
    static /* synthetic */ class C08451 {
    }

    private final class AsCharSink extends CharSink {
        private final Charset charset;
        final /* synthetic */ ByteSink this$0;

        private AsCharSink(ByteSink r1, Charset r2) {
            this.this$0 = r1;
            this.charset = (Charset) Preconditions.checkNotNull(r2);
        }

        /* synthetic */ AsCharSink(ByteSink r1, Charset r2, C08451 r3) {
            this(r1, r2);
        }

        @Override // com.google.common.p003io.CharSink
        public Writer openStream() throws IOException {
            return new OutputStreamWriter(this.this$0.openStream(), this.charset);
        }

        public String toString() {
            return this.this$0.toString() + ".asCharSink(" + this.charset + ")";
        }
    }

    protected ByteSink() {
    }

    public CharSink asCharSink(Charset r3) {
        return new AsCharSink(this, r3, null);
    }

    public OutputStream openBufferedStream() throws IOException {
        OutputStream r0 = openStream();
        if ((r0 instanceof BufferedOutputStream) == false) goto L6;
        return (BufferedOutputStream) r0;
    L6:
        return new BufferedOutputStream(r0);
    }

    public abstract OutputStream openStream() throws IOException;

    public void write(byte[] r4) throws IOException {
        Preconditions.checkNotNull(r4);
        Closer r0 = Closer.create();
        OutputStream r1 = (OutputStream) r0.register(openStream());     // Catch: Throwable -> L6
        r1.write(r4);     // Catch: Throwable -> L6
        r1.flush();     // Catch: Throwable -> L6
        r0.close();
        return;
    L6:
        th = move-exception;
    L9:
        th = move-exception;
        r0.close();
        throw th;
    L8:
        throw r0.rethrow(th);     // Catch: Throwable -> L9
    }

    public long writeFrom(InputStream r5) throws IOException {
        Preconditions.checkNotNull(r5);
        Closer r0 = Closer.create();
        OutputStream r1 = (OutputStream) r0.register(openStream());     // Catch: Throwable -> L6
        long r2 = ByteStreams.copy(r5, r1);     // Catch: Throwable -> L6
        r1.flush();     // Catch: Throwable -> L6
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
