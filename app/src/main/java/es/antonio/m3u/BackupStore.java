package es.antonio.m3u;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/* JADX INFO: loaded from: classes3.dex */
final class BackupStore {
    static final String PREF = "backup-history-local";

    interface Writer {
        void write(OutputStream r1) throws Exception;
    }

    BackupStore() {
    }

    static String code(Context r3, File r4) {
        return r3.getSharedPreferences(PREF, 0).getString(r4.getName() + ".code", "");
    }

    static File create(Context r7, String r8, String r9, Writer r10) throws Exception {
        File r1 = new File(directory(r7), "CHDTV-" + r8 + "-" + new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.ROOT).format(new Date()) + "-" + UUID.randomUUID().toString().substring(0, 8) + r9);
        File r2 = new File(r1.getPath() + ".partial");
        FileOutputStream r3 = new FileOutputStream(r2);     // Catch: Throwable -> L19
        r10.write(new BackupOutput(r3));     // Catch: Throwable -> L13
        r3.getFD().sync();     // Catch: Throwable -> L13
        r3.close();     // Catch: Throwable -> L19
        if (r2.length() == 0) goto L12;
        if (r2.renameTo(r1) == false) goto L12;
        r2.delete();
        return r1;
    L12:
        throw new IOException("No se pudo confirmar la copia guardada");     // Catch: Throwable -> L19
    L13:
        th = move-exception;
        r3.close();     // Catch: Throwable -> L16
    L18:
        throw th;     // Catch: Throwable -> L19
    L16:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L19
    L19:
        th = move-exception;
        r2.delete();
        throw th;
    }

    static synchronized void delete(Context r8, File r9) throws IOException {
        monitor-enter(BackupStore.class);
        File r1 = directory(r8).getCanonicalFile();     // Catch: Throwable -> L38
        File r2 = r9.getCanonicalFile();     // Catch: Throwable -> L38
        if (r1.equals(r2.getParentFile()) == false) goto L37;
        if (r2.getName().endsWith(".zip") == false) goto L9;
    L12:
        String r3 = code(r8, r2);     // Catch: Throwable -> L38
        if (r2.exists() == false) goto L22;
        if (r2.isFile() == false) goto L20;
        if (r2.delete() == true) goto L22;
    L20:
        throw new IOException("No se pudo borrar la copia");     // Catch: Throwable -> L38
    L22:
        if (r8.getSharedPreferences(PREF, 0).edit().remove(r2.getName() + ".code").remove(r2.getName() + ".expires").commit() == false) goto L35;
        SharedPreferences r4 = r8.getSharedPreferences("timed-issued-codes", 0);     // Catch: Throwable -> L38
        if (r3.isEmpty() == false) goto L26;
    L32:
        monitor-exit(BackupStore.class);
        return;
    L26:
        if (r3.equals(r4.getString("last", "")) == false) goto L32;
        if (r4.edit().remove("last").commit() == true) goto L32;
        throw new IOException("Archivo borrado, pero no se pudo limpiar el último código");     // Catch: Throwable -> L38
    L35:
        throw new IOException("Archivo borrado, pero no se pudo limpiar su código del historial");     // Catch: Throwable -> L38
    L9:
        if (r2.getName().endsWith(".chdbak") == true) goto L12;
        if (r2.getName().endsWith(".chdtime") == true) goto L12;
    L37:
        throw new IOException("La copia no pertenece al historial local");     // Catch: Throwable -> L38
    L38:
        th = move-exception;
        throw th;
    }

    static File directory(Context r3) throws IOException {
        File r0 = new File(r3.getFilesDir(), "backups");
        if (r0.isDirectory() == false) goto L5;
    L9:
        return r0;
    L5:
        if (r0.mkdirs() == true) goto L9;
        throw new IOException("No se pudo crear la carpeta de copias");
    }

    static long expires(Context r4, File r5) {
        return r4.getSharedPreferences(PREF, 0).getLong(r5.getName() + ".expires", 0);
    }

    static /* synthetic */ boolean lambda$list$0(File r2) {
        if (r2.isFile() == true) goto L5;
    L11:
        return false;
    L5:
        if (r2.getName().endsWith(".zip") == false) goto L7;
    L10:
        return true;
    L7:
        if (r2.getName().endsWith(".chdbak") == true) goto L10;
        if (r2.getName().endsWith(".chdtime") == false) goto L11;
        goto L10
    }

    static /* synthetic */ int lambda$list$1(File r4, File r5) {
        return Long.compare(r5.lastModified(), r4.lastModified());
    }

    static String lastCode(Context r4) {
        Iterator<File> r0 = list(r4).iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        String r2 = code(r4, r0.next());
        if (r2.isEmpty() == true) goto L4;
        return r2;
    L10:
        return r4.getSharedPreferences("timed-issued-codes", 0).getString("last", "");
    }

    static List<File> list(Context r3) {
        File[] r0 = new File(r3.getFilesDir(), "backups").listFiles(new BackupStore$$ExternalSyntheticLambda0());
        ArrayList r1 = new ArrayList();
        if (r0 == null) goto L5;
        Collections.addAll(r1, r0);
    L5:
        r1.sort(new BackupStore$$ExternalSyntheticLambda1());
        return r1;
    }

    static String mime(File r2) {
        if (r2.getName().endsWith(".zip") == false) goto L5;
        return "application/zip";
    L5:
        return "application/octet-stream";
    }

    static void record(Context r3, File r4, String r5, long r6) throws IOException {
        if (r3.getSharedPreferences(PREF, 0).edit().putString(r4.getName() + ".code", r5).putLong(r4.getName() + ".expires", r6).commit() == false) goto L6;
        return;
    L6:
        throw new IOException("Copia guardada, pero no se pudo guardar su código");
    }

    static Intent share(Context r5, File r6) {
        Uri r0 = FileProvider.getUriForFile(r5, r5.getPackageName() + ".backups", r6);
        Intent r1 = new Intent("android.intent.action.SEND").setType(mime(r6)).putExtra("android.intent.extra.STREAM", r0).addFlags(1);
        r1.setClipData(ClipData.newRawUri("Copia CHDTV", r0));
        String r2 = code(r5, r6);
        if (r2.isEmpty() == true) goto L5;
        r1.putExtra("android.intent.extra.TEXT", "Código CHDTV: " + r2);
    L5:
        return r1;
    }
}
