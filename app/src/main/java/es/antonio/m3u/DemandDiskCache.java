package es.antonio.m3u;

import com.google.common.base.Ascii;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
final class DemandDiskCache {
    private static final Map<String, State> states = null;

    static final class State {
        long bytes;
        int count;
        boolean known;

        State() {
        }
    }

    interface Writer {
        void write(OutputStream r1) throws IOException;
    }

    static {
        states = new HashMap();
    }

    DemandDiskCache() {
    }

    static void clear(File r6) {
        State r0 = state(r6);
        monitor-enter(r0);
        File[] r7 = r6.listFiles();     // Catch: Throwable -> L16
        if (r7 == null) goto L13;
        int r2 = r7.length;     // Catch: Throwable -> L16
        int r3 = 0;
    L7:
        if (r3 >= r2) goto L13;
        File r4 = r7[r3];     // Catch: Throwable -> L16
        if (r4.isFile() == false) goto L12;
        r4.delete();     // Catch: Throwable -> L16
    L12:
        r3 = r3 + 1;     // Catch: Throwable -> L16
    L13:
        r0.known = false;     // Catch: Throwable -> L16
        monitor-exit(r0);     // Catch: Throwable -> L16
        return;
    L16:
        th = move-exception;
        throw th;
    }

    static File[] files(File r1) {
        File[] r2 = r1.listFiles(new DemandDiskCache$$ExternalSyntheticLambda0());
        if (r2 == null) goto L5;
        return r2;
    L5:
        return new File[0];
    }

    static String hash(String r6) {
        byte[] r7 = MessageDigest.getInstance("SHA-256").digest(r6.getBytes(StandardCharsets.UTF_8));     // Catch: Exception -> L8
        StringBuilder r0 = new StringBuilder();     // Catch: Exception -> L8
        int r1 = r7.length;     // Catch: Exception -> L8
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L4;
        byte r3 = r7[r2];     // Catch: Exception -> L8
        r0.append(Character.forDigit((r3 >>> 4) & 15, 16));     // Catch: Exception -> L8
        r0.append(Character.forDigit(r3 & Ascii.f88SI, 16));     // Catch: Exception -> L8
        r2 = r2 + 1;
        goto L3
    L4:
        return r0.toString();
    L8:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    static void inventory(File r7, State r8) {
        r8.bytes = 0;
        File[] r9 = files(r7);
        r8.count = r9.length;
        int r0 = r9.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L4;
        r8.bytes += r9[r1].length();
        r1 = r1 + 1;
        goto L3
    L4:
        r8.known = true;
    }

    static /* synthetic */ boolean lambda$0(File r0, String r1) {
        if (r1.endsWith(".json") == false) goto L5;
    L7:
        return true;
    L5:
        if (r1.endsWith(".png") == true) goto L7;
        return false;
    }

    static boolean put(File r16, File r17, Writer r18, long r19, int r21) {
        State r3 = state(r16);
        monitor-enter(r3);
    L64:
        th = move-exception;
        throw th;
    L5:
        if (r16.isDirectory() == true) goto L11;
        if (r16.mkdirs() == true) goto L11;
        monitor-exit(r3);     // Catch: Throwable -> L64
        return false;
    L11:
        if (r3.known == true) goto L13;
        inventory(r16, r3);     // Catch: Throwable -> L64
    L13:
        boolean r4 = r17.isFile();     // Catch: Throwable -> L64
        long r6 = r17.length();     // Catch: Throwable -> L64
        File r8 = new File(String.valueOf(r17.getPath()) + ".tmp");     // Catch: Throwable -> L64
        Throwable r9 = null;
        BufferedOutputStream r10 = new BufferedOutputStream(new FileOutputStream(r8));     // Catch: Throwable -> L54
        r18.write(r10);     // Catch: Throwable -> L50
        r10.close();     // Catch: Throwable -> L54
        if (r8.length() > r19) goto L74;
        if (r8.renameTo(r17) == false) goto L47;
        r17.setLastModified(System.currentTimeMillis());     // Catch: IOException -> L60 Throwable -> L64
        r3.bytes += r17.length() - r6;     // Catch: IOException -> L60 Throwable -> L64
        if (r4 == true) goto L27;
        r3.count++;     // Catch: IOException -> L60 Throwable -> L64
    L27:
        if (r3.bytes <= r19) goto L29;
    L30:
        File[] r5 = files(r16);     // Catch: IOException -> L60 Throwable -> L64
        Arrays.sort(r5, Comparator.comparingLong(new DemandDiskCache$$ExternalSyntheticLambda1()));     // Catch: IOException -> L60 Throwable -> L64
        inventory(r16, r3);     // Catch: IOException -> L60 Throwable -> L64
        int r0 = r5.length;     // Catch: IOException -> L60 Throwable -> L64
        int r7 = 0;
    L31:
        if (r7 >= r0) goto L37;
        File r11 = r5[r7];     // Catch: IOException -> L60 Throwable -> L64
        if (r3.bytes > ((9 * r19) / 10)) goto L40;
        if (r3.count <= Math.max(1, (r21 * 9) / 10)) goto L37;
    L40:
        if (r11.equals(r17) == true) goto L45;
        long r12 = r11.length();     // Catch: IOException -> L60 Throwable -> L64
        if (r11.delete() == false) goto L45;
        r3.bytes -= r12;     // Catch: IOException -> L60 Throwable -> L64
        r3.count--;     // Catch: IOException -> L60 Throwable -> L64
    L45:
        r7 = r7 + 1;     // Catch: IOException -> L60 Throwable -> L64
    L37:
        monitor-exit(r3);     // Catch: Throwable -> L64
        return true;
    L29:
        if (r3.count <= r21) goto L37;
    L47:
        throw new IOException("Cache rename");     // Catch: IOException -> L60 Throwable -> L64
    L74:
        throw new IOException("Cache entry over budget");     // Catch: IOException -> L60 Throwable -> L64
    L50:
        th = move-exception;
        r10.close();     // Catch: Throwable -> L54
        throw th;     // Catch: Throwable -> L54
    L54:
        th = move-exception;
        if (0 == 0) goto L59;
        if (null == th) goto L75;
        r9.addSuppressed(th);     // Catch: IOException -> L60 Throwable -> L64
        throw null;     // Catch: IOException -> L60 Throwable -> L64
    L75:
        throw null;     // Catch: IOException -> L60 Throwable -> L64
    L59:
        throw th;     // Catch: IOException -> L60 Throwable -> L64
    L61:
        r8.delete();     // Catch: Throwable -> L64
        return false;
    }

    static byte[] read(File r7, int r8) throws IOException {
        long r0 = r7.length();
        if (r0 < 1) goto L40;
        if (r0 > r8) goto L40;
        Throwable r2 = null;
        BufferedInputStream r4 = new BufferedInputStream(new FileInputStream(r7));     // Catch: Throwable -> L33
        ByteArrayOutputStream r5 = new ByteArrayOutputStream((int) r0);     // Catch: Throwable -> L26
        byte[] r1 = new byte[8192];     // Catch: Throwable -> L22
    L11:
        int r3 = r4.read(r1);     // Catch: Throwable -> L22
        if (r3 == (-1)) goto L13;
        if ((r5.size() + r3) > r8) goto L21;
        r5.write(r1, 0, r3);     // Catch: Throwable -> L22
        goto L11
    L21:
        throw new IOException("Cache entry size");     // Catch: Throwable -> L22
    L13:
        r7.setLastModified(System.currentTimeMillis());     // Catch: Throwable -> L22
        byte[] r9 = r5.toByteArray();     // Catch: Throwable -> L22
        r5.close();     // Catch: Throwable -> L26
        r4.close();     // Catch: Throwable -> L33
        return r9;
    L22:
        th = move-exception;
        r5.close();     // Catch: Throwable -> L26
        throw th;     // Catch: Throwable -> L26
    L26:
        th = move-exception;
        if (0 == 0) goto L30;
        if (null == th) goto L31;
        r2.addSuppressed(th);     // Catch: Throwable -> L33
    L31:
        r4.close();     // Catch: Throwable -> L33
        throw r2;     // Catch: Throwable -> L33
    L30:
        r2 = th;
    L33:
        th = move-exception;
        if (0 == 0) goto L46;
        if (null == th) goto L38;
        r2.addSuppressed(th);
    L38:
        throw null;
    L46:
        throw th;
    L40:
        throw new IOException("Cache entry size");
    }

    static synchronized State state(File r3) {
        monitor-enter(DemandDiskCache.class);
        String r4 = r3.getAbsolutePath();     // Catch: Throwable -> L9
        State r1 = states.get(r4);     // Catch: Throwable -> L9
        if (r1 != null) goto L7;
        r1 = new State();     // Catch: Throwable -> L9
        states.put(r4, r1);     // Catch: Throwable -> L9
    L7:
        monitor-exit(DemandDiskCache.class);
        return r1;
    L9:
        th = move-exception;
        throw th;
    }
}
