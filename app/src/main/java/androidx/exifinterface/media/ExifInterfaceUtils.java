package androidx.exifinterface.media;

import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
class ExifInterfaceUtils {
    private static final String TAG = "ExifInterfaceUtils";

    static class Api21Impl {
        private Api21Impl() {
        }

        static void close(FileDescriptor r0) throws ErrnoException {
            Os.close(r0);
        }

        static FileDescriptor dup(FileDescriptor r1) throws ErrnoException {
            return Os.dup(r1);
        }

        static long lseek(FileDescriptor r2, long r3, int r5) throws ErrnoException {
            return Os.lseek(r2, r3, r5);
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static void setDataSource(MediaMetadataRetriever r0, MediaDataSource r1) {
            r0.setDataSource(r1);
        }
    }

    private ExifInterfaceUtils() {
    }

    static String byteArrayToHexString(byte[] r5) {
        StringBuilder r0 = new StringBuilder(r5.length * 2);
        int r1 = 0;
    L4:
        if (r1 >= r5.length) goto L7;
        r0.append(String.format("%02x", new Object[]{Byte.valueOf(r5[r1])}));
        r1 = r1 + 1;
        goto L4
    L7:
        return r0.toString();
    }

    static void closeFileDescriptor(FileDescriptor r3) {
        Api21Impl.close(r3);     // Catch: Exception -> L4
        return;
    L5:
        Log.e(TAG, "Error closing fd.");
    }

    static void closeQuietly(Closeable r1) {
        if (r1 != null) goto L10;
        return;
    L10:
        r1.close();     // Catch: Exception -> L5 RuntimeException -> L7
        goto L13
    L12:
        return;
    L7:
        e = move-exception;
        throw e;
    }

    static long[] convertToLongArray(Object r5) {
        if ((r5 instanceof int[]) == false) goto L10;
        int[] r0 = (int[]) r5;
        long[] r1 = new long[r0.length];
        int r2 = 0;
    L6:
        if (r2 >= r0.length) goto L8;
        r1[r2] = r0[r2];
        r2 = r2 + 1;
        goto L6
    L8:
        return r1;
    L10:
        if ((r5 instanceof long[]) == true) goto L12;
        return null;
    L12:
        return (long[]) r5;
    }

    static int copy(InputStream r5, OutputStream r6) throws IOException {
        int r0 = 0;
        byte[] r1 = new byte[8192];
    L3:
        int r2 = r5.read(r1);
        if (r2 == (-1)) goto L6;
        r0 = r0 + r2;
        r6.write(r1, 0, r2);
        goto L3
    L6:
        return r0;
    }

    static void copy(InputStream r6, OutputStream r7, int r8) throws IOException {
        int r0 = r8;
        byte[] r2 = new byte[8192];
    L3:
        if (r0 <= 0) goto L9;
        int r3 = Math.min(r0, 8192);
        int r5 = r6.read(r2, 0, r3);
        if (r5 != r3) goto L8;
        r0 = r0 - r5;
        r7.write(r2, 0, r5);
        goto L3
    L8:
        throw new IOException("Failed to copy the given amount of bytes from the inputstream to the output stream.");
    }

    static long parseSubSeconds(String r7) {
        int r0 = Math.min(r7.length(), 3);     // Catch: NumberFormatException -> L7
        long r2 = Long.parseLong(r7.substring(0, r0));     // Catch: NumberFormatException -> L7
        int r4 = r0;
    L4:
        if (r4 >= 3) goto L6;
        r2 = r2 * 10;
        r4 = r4 + 1;
        goto L4
    L6:
        return r2;
    L8:
        return 0;
    }

    static boolean startsWith(byte[] r4, byte[] r5) {
        if (r4 == null) goto L18;
        if (r5 == null) goto L18;
        if (r4.length >= r5.length) goto L9;
        return false;
    L9:
        int r1 = 0;
    L11:
        if (r1 >= r5.length) goto L16;
        if (r4[r1] != r5[r1]) goto L14;
        r1 = r1 + 1;
        goto L11
    L14:
        return false;
    L16:
        return true;
    L18:
        return false;
    }
}
