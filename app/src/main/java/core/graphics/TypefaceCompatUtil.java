package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.StrictMode;
import android.util.Log;
import androidx.core.provider.FontsContractCompat;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    static class Api19Impl {
        private Api19Impl() {
        }

        static ParcelFileDescriptor openFileDescriptor(ContentResolver r1, Uri r2, String r3, CancellationSignal r4) throws FileNotFoundException {
            return r1.openFileDescriptor(r2, r3, r4);
        }
    }

    private TypefaceCompatUtil() {
    }

    public static void closeQuietly(Closeable r1) {
        if (r1 == null) goto L9;
        r1.close();     // Catch: IOException -> L5
        return;
    L6:
        return;
    }

    public static ByteBuffer copyToDirectBuffer(Context r3, Resources r4, int r5) {
        File r0 = getTempFile(r3);
        if (r0 != null) goto L15;
        return null;
    L15:
    L12:
        th = move-exception;
        r0.delete();
        throw th;
    L6:
        if (copyToFile(r0, r4, r5) == true) goto L9;
        r0.delete();
        return null;
    L9:
        ByteBuffer r1 = mmap(r0);     // Catch: Throwable -> L12
        r0.delete();
        return r1;
    }

    public static boolean copyToFile(File r2, Resources r3, int r4) {
        InputStream r0 = null;
        r0 = r3.openRawResource(r4);     // Catch: Throwable -> L6
        boolean r1 = copyToFile(r2, r0);     // Catch: Throwable -> L6
        closeQuietly(r0);
        return r1;
    L6:
        th = move-exception;
        closeQuietly(r0);
        throw th;
    }

    public static boolean copyToFile(File r7, InputStream r8) {
        FileOutputStream r0 = null;
        StrictMode.ThreadPolicy r1 = StrictMode.allowThreadDiskWrites();
        r0 = new FileOutputStream(r7, false);     // Catch: Throwable -> L10 IOException -> L12
        byte[] r3 = new byte[1024];     // Catch: Throwable -> L10 IOException -> L12
    L4:
        int r4 = r8.read(r3);     // Catch: Throwable -> L10 IOException -> L12
        if (r4 == (-1)) goto L8;
        r0.write(r3, 0, r4);     // Catch: Throwable -> L10 IOException -> L12
    L8:
        closeQuietly(r0);
        StrictMode.setThreadPolicy(r1);
        return true;
    L12:
        e = move-exception;
        Log.e(TAG, "Error copying resource contents to temp file: " + e.getMessage());     // Catch: Throwable -> L10
        closeQuietly(r0);
        StrictMode.setThreadPolicy(r1);
        return false;
    L10:
        th = move-exception;
        closeQuietly(r0);
        StrictMode.setThreadPolicy(r1);
        throw th;
    }

    public static File getTempFile(Context r6) {
        File r0 = r6.getCacheDir();
        if (r0 != null) goto L5;
        return null;
    L5:
        String r2 = CACHE_FILE_PREFIX + Process.myPid() + "-" + Process.myTid() + "-";
        int r3 = 0;
    L7:
        if (r3 >= 100) goto L15;
        File r4 = new File(r0, r2 + r3);
        if (r4.createNewFile() == false) goto L14;
        return r4;
    L14:
        r3 = r3 + 1;
        goto L7
    L15:
        return null;
    }

    public static ByteBuffer mmap(Context r11, CancellationSignal r12, Uri r13) {
        ParcelFileDescriptor r0 = Api19Impl.openFileDescriptor(r11.getContentResolver(), r13, "r", r12);     // Catch: IOException -> L30
        if (r0 != null) goto L32;
        if (r0 == null) goto L7;
        r0.close();     // Catch: IOException -> L30
    L7:
        return null;
    L32:
        FileInputStream r1 = new FileInputStream(r0.getFileDescriptor());     // Catch: Throwable -> L22
        FileChannel r2 = r1.getChannel();     // Catch: Throwable -> L15
        long r9 = r2.size();     // Catch: Throwable -> L15
        MappedByteBuffer r3 = r2.map(FileChannel.MapMode.READ_ONLY, 0, r9);     // Catch: Throwable -> L15
        r1.close();     // Catch: Throwable -> L22
        if (r0 == null) goto L14;
        r0.close();     // Catch: IOException -> L30
    L14:
        return r3;
    L15:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L19
        goto L31
    L40:
        throw th;     // Catch: Throwable -> L22
    L19:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L22
        throw th;     // Catch: Throwable -> L22
    L22:
        th = move-exception;
        if (r0 == null) goto L41;
        r0.close();     // Catch: Throwable -> L27
        goto L31
    L42:
        throw th;     // Catch: IOException -> L30
    L27:
        th = move-exception;
        th.addSuppressed(th);     // Catch: IOException -> L30
        throw th;     // Catch: IOException -> L30
    L41:
        throw th;     // Catch: IOException -> L30
    L31:
        return null;
    }

    private static ByteBuffer mmap(File r8) {
        FileInputStream r0 = new FileInputStream(r8);     // Catch: IOException -> L14
        FileChannel r1 = r0.getChannel();     // Catch: Throwable -> L7
        long r6 = r1.size();     // Catch: Throwable -> L7
        MappedByteBuffer r2 = r1.map(FileChannel.MapMode.READ_ONLY, 0, r6);     // Catch: Throwable -> L7
        r0.close();     // Catch: IOException -> L14
        return r2;
    L7:
        th = move-exception;
        r0.close();     // Catch: Throwable -> L11
        goto L15
    L22:
        throw th;     // Catch: IOException -> L14
    L11:
        th = move-exception;
        th.addSuppressed(th);     // Catch: IOException -> L14
        throw th;     // Catch: IOException -> L14
    L15:
        return null;
    }

    public static Map<Uri, ByteBuffer> readFontInfoIntoByteBuffer(Context r6, FontsContractCompat.FontInfo[] r7, CancellationSignal r8) {
        HashMap r0 = new HashMap();
        int r1 = r7.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L13;
        FontsContractCompat.FontInfo r3 = r7[r2];
        if (r3.getResultCode() != 0) goto L11;
        Uri r4 = r3.getUri();
        if (r0.containsKey(r4) == true) goto L11;
        r0.put(r4, mmap(r6, r8, r4));
    L11:
        r2 = r2 + 1;
        goto L3
    L13:
        return Collections.unmodifiableMap(r0);
    }
}
