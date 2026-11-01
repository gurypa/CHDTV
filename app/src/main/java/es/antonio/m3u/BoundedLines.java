package es.antonio.m3u;

import java.io.IOException;
import java.io.Reader;

/* JADX INFO: loaded from: classes3.dex */
final class BoundedLines {
    private final char[] buffer;
    private int end;
    private final int limit;
    private int position;
    private final Reader reader;

    BoundedLines(Reader r2, int r3) {
        this.buffer = new char[16384];
        this.reader = r2;
        this.limit = r3;
    }

    String next() throws IOException {
        StringBuilder r0 = new StringBuilder(256);
        boolean r1 = false;
        boolean r2 = false;
    L4:
        if (this.position < this.end) goto L16;
        this.end = this.reader.read(this.buffer);
        this.position = 0;
        if (this.end < 0) goto L7;
        if (this.end != 0) goto L16;
    L7:
        if (r1 == true) goto L41;
        if (r2 == true) goto L40;
        return null;
    L40:
        return r0.toString();
    L41:
        return "\u0000";
    L16:
        char[] r3 = this.buffer;
        int r4 = this.position;
        this.position = r4 + 1;
        char r5 = r3[r4];
        if (r5 == '\n') goto L18;
        r2 = true;
        if (r5 == '\r') goto L4;
        if (r0.length() < this.limit) goto L26;
        r1 = true;
        goto L4
    L26:
        r0.append(r5);
        goto L4
    L18:
        if (r1 == false) goto L21;
        return "\u0000";
    L21:
        return r0.toString();
    }
}
