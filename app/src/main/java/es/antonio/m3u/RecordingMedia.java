package es.antonio.m3u;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.ReturnCode;
import com.arthenica.ffmpegkit.StreamInformation;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/* JADX INFO: loaded from: classes7.dex */
final class RecordingMedia {
    private RecordingMedia() {
    }

    static synchronized void ensureSeekable(File r7) throws IOException {
        monitor-enter(RecordingMedia.class);
    L46:
        th = move-exception;
        throw th;
    L5:
        if (fragmented(r7) == true) goto L8;
    L6:
        monitor-exit(RecordingMedia.class);
        return;
    L8:
        long r1 = r7.length();     // Catch: Throwable -> L46
        if (r7.getParentFile().getUsableSpace() < (67108864 + r1)) goto L55;
        File r3 = new File(r7.getParentFile(), String.valueOf(r7.getName()) + ".indexing.mp4");     // Catch: Throwable -> L46
        BlackBox.event("RECORDING_INDEX_START", "bytes=" + r1);     // Catch: Throwable -> L46
    L28:
        th = move-exception;
    L39:
        th = move-exception;
        if (r3.exists() == false) goto L54;
        r3.delete();     // Catch: Throwable -> L46
        throw th;     // Catch: Throwable -> L46
    L54:
        throw th;     // Catch: Throwable -> L46
    L30:
        if (RecordingNative.fatal(th) == true) goto L53;
        if ((th instanceof IOException) == true) goto L34;
        RecordingNative.failure(StreamInformation.KEY_INDEX, th);     // Catch: Throwable -> L39
        throw new IOException("No se pudo crear el índice de la grabación", th);     // Catch: Throwable -> L39
    L34:
        throw ((IOException) th);     // Catch: Throwable -> L39
    L53:
        throw ((Error) th);     // Catch: Throwable -> L39
    L12:
        if (ReturnCode.isSuccess(FFmpegKit.executeWithArguments(indexArguments(r7, r3)).getReturnCode()) == false) goto L52;
        if (r3.length() == 0) goto L52;
        if (fragmented(r3) == true) goto L52;
        if (r3.renameTo(r7) == false) goto L25;
        BlackBox.event("RECORDING_INDEX_DONE", "bytes=" + r7.length());     // Catch: Throwable -> L28
        if (r3.exists() == false) goto L6;
        r3.delete();     // Catch: Throwable -> L46
        goto L6
    L25:
        throw new IOException("No se pudo guardar el índice de la grabación; se conserva el archivo original");     // Catch: Throwable -> L28
    L52:
        throw new IOException("No se pudo crear el índice de la grabación; se conserva el archivo original");     // Catch: Throwable -> L28
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r14v2, types: [java.lang.Throwable] */
    static boolean fragmented(File r14) throws IOException {
        ?? r0 = 0;
        r0 = 0;
        RandomAccessFile r1 = new RandomAccessFile(r14, "r");     // Catch: Throwable -> L37
        long r2 = r1.length();     // Catch: Throwable -> L33
        long r6 = 0;
    L6:
        long r8 = 8;
        if ((r6 + 8) > r2) goto L8;
        r1.seek(r6);     // Catch: Throwable -> L33
        long r10 = ((long) r1.readInt()) & 4294967295L;     // Catch: Throwable -> L33
        int r15 = r1.readInt();     // Catch: Throwable -> L33
        if (r10 != 1) goto L19;
        r8 = 16;
        if ((r6 + 16) > r2) goto L16;
        r10 = r1.readLong();     // Catch: Throwable -> L33
        goto L19
    L16:
        r0 = "Cabecera MP4 incompleta";
        throw new IOException("Cabecera MP4 incompleta");     // Catch: Throwable -> L33
    L19:
        if (r10 != 0) goto L22;
        r10 = r2 - r6;
    L22:
        if (r10 < r8) goto L51;
        if (r10 > (r2 - r6)) goto L51;
        if (r15 == 1836019558) goto L27;
        r6 = r6 + r10;
        goto L6
    L27:
        r1.close();     // Catch: Throwable -> L37
        return true;
    L51:
        throw new IOException("Archivo MP4 incompleto");     // Catch: Throwable -> L33
    L8:
        r1.close();     // Catch: Throwable -> L37
        return false;
    L33:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L37
        throw th;     // Catch: Throwable -> L37
    L37:
        th = move-exception;
        if (r0 == 0) goto L52;
        if (r0 == th) goto L42;
        r0.addSuppressed(th);
    L42:
        throw r0;
    L52:
        throw th;
    }

    static String[] indexArguments(File r19, File r20) {
        return new String[]{"-hide_banner", "-loglevel", "error", "-nostdin", "-y", "-xerror", "-i", r19.getAbsolutePath(), "-map", "0:v?", "-map", "0:a?", "-c", "copy", "-movflags", "+faststart", "-f", "mp4", r20.getAbsolutePath()};
    }
}
