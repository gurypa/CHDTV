package es.antonio.m3u;

import android.content.Context;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes3.dex */
final class TimedBackupArchive {
    static final byte[] MAGIC = null;
    static final long MAX_DURATION = 315360000000L;
    static final long YEAR = 31536000000L;

    static {
        MAGIC = new byte[]{67, 72, 68, 84, 73, 77, 69, 1};
    }

    TimedBackupArchive() {
    }

    static byte[] codeKey(String r4) throws IOException {
        if (r4 == null) goto L12;
    L13:
        e = move-exception;
        throw new IOException("Código no válido", e);
    L5:
        if (r4.matches("CHD1-[A-Za-z0-9_-]{43}") == false) goto L12;
        byte[] r1 = Base64.decode(r4.substring(5), 11);     // Catch: IllegalArgumentException -> L13
        if (r1.length != 32) goto L10;
        return r1;
    L10:
        throw new IOException("Código no válido");     // Catch: IllegalArgumentException -> L13
    L12:
        throw new IOException("Código no válido");     // Catch: IllegalArgumentException -> L13
    }

    static byte[] digest(File r6) throws Exception {
        MessageDigest r0 = MessageDigest.getInstance("SHA-256");
        FileInputStream r1 = new FileInputStream(r6);
        byte[] r2 = new byte[65536];     // Catch: Throwable -> L10
    L4:
        int r3 = r1.read(r2);     // Catch: Throwable -> L10
        if (r3 == (-1)) goto L8;
        r0.update(r2, 0, r3);     // Catch: Throwable -> L10
        goto L4
    L8:
        r1.close();
        return r0.digest();
    L10:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L13
    L15:
        throw th;
    L13:
        th = move-exception;
        th.addSuppressed(th);
        goto L15
    }

    static String newCode() {
        byte[] r0 = new byte[32];
        new SecureRandom().nextBytes(r0);
        return "CHD1-" + Base64.encodeToString(r0, 11);
    }

    static long restore(Context r16, InputStream r17, String r18) throws Exception {
        codeKey(r18);
        DataInputStream r0 = new DataInputStream(r17);
        byte[] r4 = new byte[MAGIC.length];
        byte[] r10 = new byte[32];
        byte[] r11 = new byte[32];
        r0.readFully(r4);
        if (Arrays.equals(r4, MAGIC) == false) goto L57;
        long r6 = r0.readLong();
        long r8 = r0.readLong();
        r0.readFully(r10);
        r0.readFully(r11);
        if (r6 <= 0) goto L55;
        if (r8 <= r6) goto L55;
        if ((r8 - r6) > MAX_DURATION) goto L55;
        if (MessageDigest.isEqual(r11, tag(r18, r6, r8, r10)) == false) goto L55;
        if (TimedAccess.now(r16) >= r8) goto L53;
        File r12 = File.createTempFile("timed-import-", ".zip", r16.getCacheDir());
        FileOutputStream r1 = new FileOutputStream(r12);     // Catch: Throwable -> L48
        BackupArchive.copy(r0, r1, 1342177280);     // Catch: Throwable -> L39
        r1.close();     // Catch: Throwable -> L48
        if (MessageDigest.isEqual(r10, digest(r12)) == false) goto L69;
        if (TimedAccess.now(r16) >= r8) goto L36;
        FileInputStream r2 = new FileInputStream(r12);     // Catch: Throwable -> L48
        FullBackupArchive.restore(r16, r2, r18);     // Catch: Throwable -> L28
        r2.close();     // Catch: Throwable -> L46
        TimedAccess.activate(r16, r8);     // Catch: Throwable -> L46
        r12.delete();
        return r8;
    L28:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L32
    L68:
        throw th;     // Catch: Throwable -> L46
    L32:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L46
        throw th;     // Catch: Throwable -> L46
    L36:
        throw new IOException("El código ha caducado");     // Catch: Throwable -> L46
    L69:
        throw new IOException("Copia dañada");     // Catch: Throwable -> L46
    L46:
        th = th;
    L50:
        r12.delete();
        throw th;
    L39:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L43
    L70:
        throw th;     // Catch: Throwable -> L46
    L43:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L46
        throw th;     // Catch: Throwable -> L46
    L48:
        th = th;
        goto L50
    L53:
        throw new IOException("El código ha caducado");
    L55:
        throw new IOException("Código incorrecto o copia modificada");
    L57:
        throw new IOException("No es una copia temporal");
    }

    static byte[] tag(String r4, long r5, long r7, byte[] r9) throws Exception {
        Mac r1 = Mac.getInstance("HmacSHA256");
        r1.init(new SecretKeySpec(codeKey(r4), "HmacSHA256"));
        r1.update(MAGIC);
        ByteArrayOutputStream r0 = new ByteArrayOutputStream();
        DataOutputStream r2 = new DataOutputStream(r0);
        r2.writeLong(r5);
        r2.writeLong(r7);
        r2.write(r9);
        return r1.doFinal(r0.toByteArray());
    }

    static long write(Context r16, OutputStream r17, String r18, long r19) throws Exception {
        if (TimedAccess.limited(r16) == true) goto L46;
        codeKey(r18);
        if (r19 < 60000) goto L44;
        if (r19 > MAX_DURATION) goto L44;
        long r4 = TimedAccess.now(r16);
        long r6 = TimedBackupArchive$$ExternalSyntheticBackport0.m410m(r4, r19);
        File r9 = File.createTempFile("timed-full-", ".zip", r16.getCacheDir());
        FileOutputStream r0 = new FileOutputStream(r9);     // Catch: Throwable -> L38
        FullBackupArchive.write(r16, r0, r18);     // Catch: Throwable -> L29
        r0.close();     // Catch: Throwable -> L27
        byte[] r1 = digest(r9);     // Catch: Throwable -> L27
        DataOutputStream r2 = new DataOutputStream(r17);     // Catch: Throwable -> L36
        r2.write(MAGIC);     // Catch: Throwable -> L36
        r2.writeLong(r4);     // Catch: Throwable -> L36
        r2.writeLong(r6);     // Catch: Throwable -> L36
        r2.write(r1);     // Catch: Throwable -> L36
        r2.write(tag(r18, r4, r6, r1));     // Catch: Throwable -> L36
        FileInputStream r3 = new FileInputStream(r9);     // Catch: Throwable -> L36
        BackupArchive.copy(r3, r2, 1342177280);     // Catch: Throwable -> L20
        r3.close();     // Catch: Throwable -> L36
        r2.flush();     // Catch: Throwable -> L36
        r9.delete();
        return r6;
    L20:
        th = move-exception;
        r3.close();     // Catch: Throwable -> L24
    L59:
        throw th;     // Catch: Throwable -> L36
    L24:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L36
        throw th;     // Catch: Throwable -> L36
    L27:
        th = th;
    L41:
        r9.delete();
        throw th;
    L36:
        th = th;
    L29:
        th = move-exception;
        r0.close();     // Catch: Throwable -> L33
    L60:
        throw th;     // Catch: Throwable -> L36
    L33:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L36
        throw th;     // Catch: Throwable -> L36
    L38:
        th = th;
    L44:
        throw new IOException("Duración entre 1 minuto y 10 años");
    L46:
        throw new IOException("Una instalación con acceso temporal no puede generar otra cesión");
    }
}
