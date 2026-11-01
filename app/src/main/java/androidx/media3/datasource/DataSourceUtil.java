package androidx.media3.datasource;

import java.io.IOException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class DataSourceUtil {
    private DataSourceUtil() {
    }

    public static void closeQuietly(DataSource r1) {
        if (r1 == null) goto L11;
        r1.close();     // Catch: IOException -> L5
        return;
    L8:
        return;
    }

    public static byte[] readExactly(DataSource r6, int r7) throws IOException {
        byte[] r0 = new byte[r7];
        int r1 = 0;
    L3:
        if (r1 >= r7) goto L9;
        int r2 = r6.read(r0, r1, r0.length - r1);
        if (r2 == (-1)) goto L8;
        r1 = r1 + r2;
        goto L3
    L8:
        throw new IllegalStateException("Not enough data could be read: " + r1 + " < " + r7);
    L9:
        return r0;
    }

    public static byte[] readToEnd(DataSource r5) throws IOException {
        byte[] r0 = new byte[1024];
        int r1 = 0;
        int r2 = 0;
    L4:
        if (r2 == (-1)) goto L12;
        if (r1 != r0.length) goto L8;
        r0 = Arrays.copyOf(r0, r0.length * 2);
    L8:
        r2 = r5.read(r0, r1, r0.length - r1);
        if (r2 == (-1)) goto L4;
        r1 = r1 + r2;
        goto L4
    L12:
        return Arrays.copyOf(r0, r1);
    }
}
