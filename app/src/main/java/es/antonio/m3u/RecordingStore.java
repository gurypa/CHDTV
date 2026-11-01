package es.antonio.m3u;

import android.content.Context;
import android.os.Environment;
import android.util.AtomicFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes7.dex */
final class RecordingStore {
    RecordingStore() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r1v8, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v9 */
    /* JADX WARN: Type inference failed for: r6v4, types: [java.lang.Throwable] */
    static synchronized List<RecordingTask> all(Context r6) throws IOException {
        monitor-enter(RecordingStore.class);
    L39:
        th = move-exception;
        throw th;
    L5:
        if (index(r6).getBaseFile().exists() == true) goto L9;
        ArrayList r7 = new ArrayList();     // Catch: Throwable -> L39
        monitor-exit(RecordingStore.class);
        return r7;
    L9:
        ?? r1 = 0;
        r1 = 0;
        ObjectInputStream r2 = new ObjectInputStream(index(r6).openRead());     // Catch: Throwable -> L30
        Object r8 = r2.readObject();     // Catch: Throwable -> L26
        if ((r8 instanceof ArrayList) == false) goto L50;
        ArrayList r3 = new ArrayList();     // Catch: Throwable -> L26
        Iterator r9 = ((ArrayList) r8).iterator();     // Catch: Throwable -> L26
    L15:
        if (r9.hasNext() == false) goto L16;
        Object r4 = r9.next();     // Catch: Throwable -> L26
        if ((r4 instanceof RecordingTask) == false) goto L22;
        r3.add((RecordingTask) r4);     // Catch: Throwable -> L26
        goto L15
    L22:
        r1 = "Índice de grabaciones no válido";
        throw new IOException("Índice de grabaciones no válido");     // Catch: Throwable -> L26
    L16:
        r2.close();     // Catch: Throwable -> L30
        monitor-exit(RecordingStore.class);
        return r3;
    L50:
        throw new IOException("Índice de grabaciones no válido");     // Catch: Throwable -> L26
    L26:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L30
        throw th;     // Catch: Throwable -> L30
    L30:
        th = move-exception;
        if (r1 == 0) goto L51;
        if (r1 == th) goto L35;
        r1.addSuppressed(th);     // Catch: ClassNotFoundException -> L36 Throwable -> L39
    L36:
        e = move-exception;
        throw new IOException("No se pudo abrir el índice de grabaciones", e);     // Catch: Throwable -> L39
    L35:
        throw r1;     // Catch: ClassNotFoundException -> L36 Throwable -> L39
    L51:
        throw th;     // Catch: ClassNotFoundException -> L36 Throwable -> L39
    }

    static File file(Context r3, RecordingTask r4) throws IOException {
        if (r4.f188id.matches("[a-fA-F0-9-]{36}") == false) goto L11;
        if (r4.format.equals("mp4") == true) goto L9;
        if (r4.format.equals("mkv") == false) goto L11;
    L9:
        return new File(folder(r3), String.valueOf(r4.f188id) + "." + r4.format);
    L11:
        throw new IOException("Ruta de grabación no válida");
    }

    static File folder(Context r2) throws IOException {
        File r0 = r2.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        if (r0 != null) goto L5;
        r0 = r2.getFilesDir();
    L5:
        File r3 = new File(r0, "CHDTV-grabaciones");
        if (r3.isDirectory() == false) goto L8;
    L12:
        return r3;
    L8:
        if (r3.mkdirs() == true) goto L12;
        throw new IOException("No se pudo crear la carpeta de grabaciones");
    }

    static synchronized RecordingTask get(Context r3, String r4) throws IOException {
        monitor-enter(RecordingStore.class);
        Iterator<RecordingTask> r5 = all(r3).iterator();     // Catch: Throwable -> L14
    L6:
        if (r5.hasNext() == false) goto L7;
        RecordingTask r1 = r5.next();     // Catch: Throwable -> L14
        if (r1.f188id.equals(r4) == false) goto L6;
        monitor-exit(RecordingStore.class);
        return r1;
    L7:
        monitor-exit(RecordingStore.class);
        return null;
    L14:
        th = move-exception;
        throw th;
    }

    private static AtomicFile index(Context r3) {
        return new AtomicFile(new File(r3.getFilesDir(), "recordings-index.dat"));
    }

    static /* synthetic */ boolean lambda$0(String r0, RecordingTask r1) {
        return r1.f188id.equals(r0);
    }

    static synchronized void put(Context r7, RecordingTask r8) throws IOException {
        monitor-enter(RecordingStore.class);
    L24:
        th = move-exception;
        throw th;
    L6:
        if (RecordingControl.request(r7, r8.f188id) != 2) goto L9;
    L7:
        monitor-exit(RecordingStore.class);
        return;
    L9:
        List<RecordingTask> r1 = all(r7);     // Catch: Throwable -> L24
        int r3 = -1;
        int r4 = 0;
    L11:
        if (r4 >= r1.size()) goto L12;
        if (r1.get(r4).f188id.equals(r8.f188id) == false) goto L23;
        r3 = r4;
    L23:
        r4 = r4 + 1;
        goto L11
    L12:
        if (r3 < 0) goto L15;
        r1.set(r3, r8);     // Catch: Throwable -> L24
    L17:
        write(r7, r1);     // Catch: Throwable -> L24
        goto L7
    L15:
        if (r1.size() >= 500) goto L19;
        r1.add(0, r8);     // Catch: Throwable -> L24
        goto L17
    L19:
        throw new IOException("Hay 500 grabaciones: elimina las que ya no necesites");     // Catch: Throwable -> L24
    }

    static synchronized void remove(Context r5, final String r6) throws IOException {
        monitor-enter(RecordingStore.class);
        List<RecordingTask> r1 = all(r5);     // Catch: Throwable -> L21
        Iterator<RecordingTask> r2 = r1.iterator();     // Catch: Throwable -> L21
    L6:
        if (r2.hasNext() == false) goto L7;
        RecordingTask r3 = r2.next();     // Catch: Throwable -> L21
        if (r3.f188id.equals(r6) == false) goto L6;
        if (r3.active() == true) goto L17;
        if ("Programada".equals(r3.state) == false) goto L6;
    L17:
        if (RecordingControl.request(r5, r6) == 2) goto L6;
        throw new IOException("Detén o cancela la grabación antes de borrarla");     // Catch: Throwable -> L21
    L7:
        r1.removeIf(new RecordingStore$$ExternalSyntheticLambda0(r6));     // Catch: Throwable -> L21
        write(r5, r1);     // Catch: Throwable -> L21
        monitor-exit(RecordingStore.class);
        return;
    L21:
        th = move-exception;
        throw th;
    }

    static String size(long r3) {
        return String.format(Locale.getDefault(), "%.1f MB", new Object[]{Double.valueOf(r3 / 1048576.0d)});
    }

    private static void write(Context r3, List<RecordingTask> r4) throws IOException {
        AtomicFile r5 = index(r3);
        FileOutputStream r0 = r5.startWrite();
        ObjectOutputStream r1 = new ObjectOutputStream(r0);     // Catch: RuntimeException -> L5 Throwable -> L7
        r1.writeObject(new ArrayList(r4));     // Catch: RuntimeException -> L5 Throwable -> L7
        r1.flush();     // Catch: RuntimeException -> L5 Throwable -> L7
        r5.finishWrite(r0);     // Catch: RuntimeException -> L5 Throwable -> L7
        return;
    L7:
        e = move-exception;
        r5.failWrite(r0);
        throw e;
    }
}
