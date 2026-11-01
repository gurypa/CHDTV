package es.antonio.m3u;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

/* JADX INFO: loaded from: classes3.dex */
public final class PlaylistInput {
    public static final long MAX_BYTES = Long.MAX_VALUE;

    private PlaylistInput() {
    }

    public static Reader reader(InputStream r17) throws IOException {
        BufferedInputStream r0 = new BufferedInputStream(r17);
        r0.mark(2);
        int r3 = r0.read();
        int r4 = r0.read();
        r0.reset();
        if (r3 == 31) goto L5;
    L7:
        InputStream r5 = r0;
    L8:
        PushbackInputStream r6 = new PushbackInputStream(r5, 3);
        byte[] r8 = new byte[3];
        int r9 = 0;
    L9:
        if (r9 >= 3) goto L14;
        int r10 = r6.read();
        if (r10 < 0) goto L14;
        r8[r9] = (byte) r10;
        r9 = r9 + 1;
    L14:
        String r11 = "UTF-8";
        int r12 = 0;
        if (r9 < 2) goto L21;
        char r16 = 0;
        if ((r8[0] & 255) == 255) goto L19;
    L22:
        if (r9 >= 2) goto L24;
    L28:
        if (r9 >= 3) goto L30;
    L36:
        if (r9 <= r12) goto L39;
        r6.unread(r8, r12, r9 - r12);
    L39:
        return new InputStreamReader(r6, r11);
    L30:
        if ((r8[r16] & 255) != 239) goto L36;
        if ((r8[1] & 255) != 187) goto L36;
        if ((r8[2] & 255) != 191) goto L36;
        r12 = 3;
        goto L36
    L24:
        if ((r8[r16] & 255) != 254) goto L28;
        if ((r8[1] & 255) != 255) goto L28;
        r11 = "UTF-16BE";
        r12 = 2;
        goto L36
    L19:
        if ((r8[1] & 255) != 254) goto L22;
        r11 = "UTF-16LE";
        r12 = 2;
        goto L36
    L21:
        r16 = 0;
        goto L22
    L5:
        if (r4 != 139) goto L7;
        r5 = new GZIPInputStream(r0);
        goto L8
    }
}
