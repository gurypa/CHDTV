package es.antonio.m3u;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/* JADX INFO: loaded from: classes3.dex */
final class FullBackupArchive {
    static final String DATA = "datos.chdbak";
    static final long MAX = 1342177280;

    FullBackupArchive() {
    }

    static void add(ZipOutputStream r3, String r4, File r5) throws Exception {
        r3.putNextEntry(new ZipEntry(r4));
        FileInputStream r0 = new FileInputStream(r5);
        BackupArchive.copy(r0, r3, 1073741824);     // Catch: Throwable -> L6
        r0.close();
        r3.closeEntry();
        return;
    L6:
        th = move-exception;
        r0.close();     // Catch: Throwable -> L9
    L11:
        throw th;
    L9:
        th = move-exception;
        th.addSuppressed(th);
        goto L11
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r15v0 */
    /* JADX WARN: Type inference failed for: r15v1, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r15v10 */
    /* JADX WARN: Type inference failed for: r15v11, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r15v12 */
    /* JADX WARN: Type inference failed for: r15v13, types: [java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r15v14 */
    /* JADX WARN: Type inference failed for: r15v15 */
    /* JADX WARN: Type inference failed for: r15v2 */
    /* JADX WARN: Type inference failed for: r15v3 */
    /* JADX WARN: Type inference failed for: r15v4 */
    /* JADX WARN: Type inference failed for: r15v5 */
    /* JADX WARN: Type inference failed for: r15v7 */
    /* JADX WARN: Type inference failed for: r15v9 */
    static void restore(Context r17, InputStream r18, String r19) throws Exception {
        PushbackInputStream r3 = new PushbackInputStream(r18, 4);
        byte[] r6 = new byte[4];
        int r7 = 0;
    L3:
        if (r7 >= 4) goto L7;
        int r8 = r3.read(r6, r7, 4 - r7);
        if (r8 <= 0) goto L7;
        r7 = r7 + r8;
    L7:
        r3.unread(r6, 0, r7);
        if (r7 >= 4) goto L10;
    L105:
        BackupArchive.restore(r17, r3, r19);
        return;
    L10:
        if (r6[0] != 80) goto L105;
        if (r6[1] != 75) goto L105;
        File r4 = File.createTempFile("full-restore-", ".zip", r17.getCacheDir());
        File r9 = File.createTempFile("restore-data-", ".chdbak", r17.getCacheDir());
        FileOutputStream r10 = new FileOutputStream(r4);     // Catch: Throwable -> L101
        BackupArchive.copy(r3, r10, MAX);     // Catch: Throwable -> L92
        r10.close();     // Catch: Throwable -> L101
        ZipFile r11 = new ZipFile(r4);     // Catch: Throwable -> L101
        HashSet r12 = new HashSet();     // Catch: Throwable -> L85
        Enumeration<? extends ZipEntry> r13 = r11.entries();     // Catch: Throwable -> L85
        ZipEntry r14 = null;
    L22:
        if (r13.hasMoreElements() == false) goto L127;
        ZipEntry r15 = r13.nextElement();     // Catch: Throwable -> L38
        ?? r16 = r15.getName();     // Catch: Throwable -> L38
        if (r12.add(r16) == false) goto L37;
        if (r16.equals(DATA) == true) goto L34;
        if (r16.equals("CHDTV.apk") == true) goto L34;
        if (r16.equals("LEEME.txt") == true) goto L34;
        if (r16.matches("splits/parte-[0-9]+\\.apk") == false) goto L37;
    L34:
        if (r16.equals(DATA) == false) goto L22;
        r14 = r15;
    L37:
        throw new IOException("ZIP incompatible");     // Catch: Throwable -> L38
    L38:
        th = move-exception;
        ?? r110 = r4;
        Throwable r5 = th;
    L109:
        r11.close();     // Catch: Throwable -> L89
    L144:
        throw r5;     // Catch: Throwable -> L99
    L89:
        th = move-exception;
        r5.addSuppressed(th);     // Catch: Throwable -> L99
        throw r5;     // Catch: Throwable -> L99
    L127:
        if (r14 == null) goto L82;
        if (r12.contains("CHDTV.apk") == false) goto L82;
        InputStream r0 = r11.getInputStream(r14);     // Catch: Throwable -> L85
        FileOutputStream r1 = new FileOutputStream(r9);     // Catch: Throwable -> L73
        r110 = r4;
        BackupArchive.copy(r0, r1, 1073741824);     // Catch: Throwable -> L66
        r1.close();     // Catch: Throwable -> L64
        if (r0 == null) goto L51;
        r0.close();     // Catch: Throwable -> L83
    L51:
        r11.close();     // Catch: Throwable -> L99
        FileInputStream r2 = new FileInputStream(r9);     // Catch: Throwable -> L99
        BackupArchive.restore(r17, r2, r19);     // Catch: Throwable -> L57
        r2.close();     // Catch: Throwable -> L99
        r110.delete();
        r9.delete();
        return;
    L57:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L61
    L140:
        throw th;     // Catch: Throwable -> L99
    L61:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L99
        throw th;     // Catch: Throwable -> L99
    L64:
        th = move-exception;
        Throwable r20 = th;
        r16 = r110;
    L75:
        if (r0 == null) goto L142;
        r0.close();     // Catch: Throwable -> L78
    L143:
        throw r20;     // Catch: Throwable -> L83
    L78:
        th = move-exception;
        r20.addSuppressed(th);     // Catch: Throwable -> L83
        throw r20;     // Catch: Throwable -> L83
    L142:
        throw r20;     // Catch: Throwable -> L83
    L66:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L70
    L141:
        throw th;     // Catch: Throwable -> L64
    L70:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L64
        throw th;     // Catch: Throwable -> L64
    L73:
        th = move-exception;
        r16 = r4;
        r20 = th;
    L82:
        throw new IOException("Falta copia completa");     // Catch: Throwable -> L83
    L83:
        th = move-exception;
        r5 = th;
        r110 = r16;
    L85:
        th = move-exception;
        r110 = r4;
        r5 = th;
        goto L109
    L92:
        th = move-exception;
        r110 = r4;
        r10.close();     // Catch: Throwable -> L96
    L145:
        throw th;     // Catch: Throwable -> L99
    L96:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L99
        throw th;     // Catch: Throwable -> L99
    L99:
        th = th;
    L103:
        r110.delete();
        r9.delete();
        throw th;
    L101:
        th = th;
        r110 = r4;
        goto L103
    }

    static void write(Context r7, OutputStream r8, String r9) throws Exception {
        File r0 = File.createTempFile("backup-", ".chdbak", r7.getCacheDir());
        FileOutputStream r1 = new FileOutputStream(r0);     // Catch: Throwable -> L29
        BackupArchive.write(r7, r1, r9);     // Catch: Throwable -> L23
        r1.close();     // Catch: Throwable -> L29
        ZipOutputStream r2 = new ZipOutputStream(r8);     // Catch: Throwable -> L29
        r2.setLevel(0);     // Catch: Throwable -> L17
        add(r2, "CHDTV.apk", new File(r7.getApplicationInfo().sourceDir));     // Catch: Throwable -> L17
        String[] r3 = r7.getApplicationInfo().splitSourceDirs;     // Catch: Throwable -> L17
        if (r3 == null) goto L13;
        int r4 = 0;
    L11:
        if (r4 >= r3.length) goto L13;
        add(r2, "splits/parte-" + r4 + ".apk", new File(r3[r4]));     // Catch: Throwable -> L17
        r4 = r4 + 1;     // Catch: Throwable -> L17
    L13:
        add(r2, DATA, r0);     // Catch: Throwable -> L17
        r2.putNextEntry(new ZipEntry("LEEME.txt"));     // Catch: Throwable -> L17
        r2.write("CHDTV: copia completa\n1. Extrae e instala CHDTV.apk. Si hay splits, instala todas las partes con un instalador compatible.\n2. Abre Ajustes > Copia de seguridad > Restaurar copia y elige este ZIP. Introduce la contraseña.\nIncluye listas, EPG, Plex, favoritos, PIN, ajustes y progreso. Los datos están cifrados; el APK no. No incluye vídeos, caché de imágenes ni credenciales protegidas de Movistar/DAZN. Vuelve a iniciar esas cuentas y concede permisos de archivos en el nuevo dispositivo.\n".getBytes("UTF-8"));     // Catch: Throwable -> L17
        r2.closeEntry();     // Catch: Throwable -> L17
        r2.close();     // Catch: Throwable -> L29
        r0.delete();
        return;
    L17:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L20
    L22:
        throw th;     // Catch: Throwable -> L29
    L20:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L29
        goto L22
    L23:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L26
    L28:
        throw th;     // Catch: Throwable -> L29
    L26:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L29
    L29:
        th = move-exception;
        r0.delete();
        throw th;
    }
}
