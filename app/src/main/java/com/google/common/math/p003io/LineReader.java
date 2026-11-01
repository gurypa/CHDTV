package com.google.common.p003io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class LineReader {
    private final char[] buf;
    private final CharBuffer cbuf;
    private final LineBuffer lineBuf;
    private final Queue<String> lines;
    private final Readable readable;

    @CheckForNull
    private final Reader reader;

    public LineReader(Readable r2) {
        this.cbuf = CharStreams.createBuffer();
        this.buf = this.cbuf.array();
        this.lines = new ArrayDeque();
        this.lineBuf = new C08541(this);
        this.readable = (Readable) Preconditions.checkNotNull(r2);
        if ((r2 instanceof Reader) == false) goto L5;
        Reader r0 = (Reader) r2;
    L6:
        this.reader = r0;
        return;
    L5:
        r0 = null;
        goto L6
    }

    static /* synthetic */ Queue access$000(LineReader r1) {
        return r1.lines;
    }

    @CheckForNull
    public String readLine() throws IOException {
    L3:
        if (this.lines.peek() != null) goto L13;
        Java8Compatibility.clear(this.cbuf);
        if (this.reader == null) goto L7;
        int r0 = this.reader.read(this.buf, 0, this.buf.length);
    L9:
        if (r0 == (-1)) goto L10;
        this.lineBuf.add(this.buf, 0, r0);
        goto L3
    L10:
        this.lineBuf.finish();
        goto L13
    L7:
        r0 = this.readable.read(this.cbuf);
    L13:
        return this.lines.poll();
    }
}
