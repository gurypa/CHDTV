package es.antonio.m3u;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes7.dex */
final class DemandImageInput {
    DemandImageInput() {
    }

    static byte[] read(InputStream r4, int r5) throws IOException {
        ByteArrayOutputStream r0 = new ByteArrayOutputStream();
        byte[] r1 = new byte[8192];
    L3:
        int r2 = r4.read(r1);
        if (r2 == (-1)) goto L6;
        if (r2 == 0) goto L3;
        if (r2 > (r5 - r0.size())) goto L13;
        r0.write(r1, 0, r2);
        goto L3
    L13:
        throw new IOException("Artwork exceeds encoded byte limit");
    L6:
        return r0.toByteArray();
    }
}
