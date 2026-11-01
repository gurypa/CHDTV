package es.antonio.m3u;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
final class DemandMatches {
    DemandMatches() {
    }

    static File file(DemandCatalog.Rows r6) {
        return new File(r6.directory.owner.files, "vod-history-v193-" + r6.directory.file.getName() + "-" + r6.records + "-" + r6.count + ".bin");
    }

    static int[] get(DemandCatalog.Rows r12, String r13) {
        monitor-enter(r12);
    L68:
        th = move-exception;
        throw th;
    L4:
        if (r12.historyMatchesLoaded == true) goto L65;
        r12.historyMatchesLoaded = true;     // Catch: Throwable -> L68
        File r0 = file(r12);     // Catch: Throwable -> L68
        if (r0.length() >= PlaybackStateCompat.ACTION_SET_PLAYBACK_SPEED) goto L65;
        Throwable r1 = null;
        DataInputStream r2 = new DataInputStream(new BufferedInputStream(new FileInputStream(r0)));     // Catch: Throwable -> L58
    L54:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L58
        throw th;     // Catch: Throwable -> L58
    L11:
        if (r2.readInt() != 1128809589) goto L85;
        if (r2.readLong() != r12.directory.owner.modified) goto L85;
        if (r2.readLong() != r12.directory.owner.size) goto L85;
        if (r2.readLong() != r12.directory.file.length()) goto L85;
        if (r2.readLong() != r12.directory.file.lastModified()) goto L85;
        if (r2.readLong() != r12.records) goto L85;
        if (r2.readInt() != r12.count) goto L85;
        int r3 = r2.readInt();     // Catch: Throwable -> L54
        if (r3 < 0) goto L84;
        if (r3 > 1024) goto L84;
        LinkedHashMap r4 = new LinkedHashMap();     // Catch: Throwable -> L54
        int r5 = 0;
    L29:
        if (r5 >= r3) goto L31;
        String r6 = r2.readUTF();     // Catch: Throwable -> L54
        int r7 = r2.readInt();     // Catch: Throwable -> L54
        if (r7 < 0) goto L83;
        if (r7 > 1000) goto L83;
        int[] r8 = new int[r7];     // Catch: Throwable -> L54
        int r9 = 0;
    L42:
        if (r9 >= r7) goto L43;
        r8[r9] = r2.readInt();     // Catch: Throwable -> L54
        if (r8[r9] < 0) goto L82;
        if (r8[r9] >= r12.count) goto L82;
        r9 = r9 + 1;     // Catch: Throwable -> L54
    L82:
        throw new IOException("History row");     // Catch: Throwable -> L54
    L43:
        r4.put(r6, r8);     // Catch: Throwable -> L54
        r5 = r5 + 1;     // Catch: Throwable -> L54
    L83:
        throw new IOException("History rows");     // Catch: Throwable -> L54
    L31:
        if (r2.read() != (-1)) goto L36;
        r12.historyMatches.putAll(r4);     // Catch: Throwable -> L54
        r2.close();     // Catch: Throwable -> L58
        goto L65
    L36:
        throw new IOException("History trailing");     // Catch: Throwable -> L54
    L84:
        throw new IOException("History count");     // Catch: Throwable -> L54
    L85:
        throw new IOException("History fingerprint");     // Catch: Throwable -> L54
    L58:
        th = move-exception;
        if (0 == 0) goto L86;
        if (null == th) goto L63;
        r1.addSuppressed(th);     // Catch: IOException -> L64 Throwable -> L68
    L63:
        throw null;     // Catch: IOException -> L64 Throwable -> L68
        goto L65
    L86:
        throw th;     // Catch: IOException -> L64 Throwable -> L68
    L65:
        int[] r14 = r12.historyMatches.get(r13);     // Catch: Throwable -> L68
        monitor-exit(r12);     // Catch: Throwable -> L68
        return r14;
    }

    static void put(DemandCatalog.Rows r2, String r3, int[] r4) {
        if (r4.length <= 1000) goto L5;
        return;
    L5:
        monitor-enter(r2);
        r2.historyMatches.put(r3, r4);     // Catch: Throwable -> L13
    L8:
        if (r2.historyMatches.size() <= 1024) goto L9;
        r2.historyMatches.remove(r2.historyMatches.keySet().iterator().next());     // Catch: Throwable -> L13
        goto L8
    L9:
        r2.historyMatchesDirty = true;     // Catch: Throwable -> L13
        monitor-exit(r2);     // Catch: Throwable -> L13
        return;
    L13:
        th = move-exception;
        throw th;
    }

    static void save(DemandCatalog.Rows r9) {
        monitor-enter(r9);
    L41:
        th = move-exception;
        throw th;
    L4:
        if (r9.historyMatchesDirty == true) goto L7;
        monitor-exit(r9);     // Catch: Throwable -> L41
        return;
    L7:
        File r0 = file(r9);     // Catch: Throwable -> L41
        File r1 = new File(String.valueOf(r0.getPath()) + ".tmp");     // Catch: Throwable -> L41
        Throwable r2 = null;
        DataOutputStream r3 = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(r1)));     // Catch: Throwable -> L31
        r3.writeInt(1128809589);     // Catch: Throwable -> L27
        r3.writeLong(r9.directory.owner.modified);     // Catch: Throwable -> L27
        r3.writeLong(r9.directory.owner.size);     // Catch: Throwable -> L27
        r3.writeLong(r9.directory.file.length());     // Catch: Throwable -> L27
        r3.writeLong(r9.directory.file.lastModified());     // Catch: Throwable -> L27
        r3.writeLong(r9.records);     // Catch: Throwable -> L27
        r3.writeInt(r9.count);     // Catch: Throwable -> L27
        r3.writeInt(r9.historyMatches.size());     // Catch: Throwable -> L27
        Iterator<Map.Entry<String, int[]>> r4 = r9.historyMatches.entrySet().iterator();     // Catch: Throwable -> L27
    L13:
        int r6 = 0;
        if (r4.hasNext() == false) goto L15;
        Map.Entry<String, int[]> r5 = r4.next();     // Catch: Throwable -> L27
        r3.writeUTF(r5.getKey());     // Catch: Throwable -> L27
        r3.writeInt(r5.getValue().length);     // Catch: Throwable -> L27
        int[] r7 = r5.getValue();     // Catch: Throwable -> L27
        int r8 = r7.length;     // Catch: Throwable -> L27
    L23:
        if (r6 >= r8) goto L13;
        r3.writeInt(r7[r6]);     // Catch: Throwable -> L27
        r6 = r6 + 1;
        goto L23
    L15:
        r3.close();     // Catch: Throwable -> L31
        if (r1.renameTo(r0) == false) goto L19;
        r9.historyMatchesDirty = false;     // Catch: Throwable -> L41
    L20:
        monitor-exit(r9);     // Catch: Throwable -> L41
        return;
    L19:
        r1.delete();     // Catch: Throwable -> L41
    L27:
        th = move-exception;
        r3.close();     // Catch: Throwable -> L31
        throw th;     // Catch: Throwable -> L31
    L31:
        th = move-exception;
        if (0 == 0) goto L52;
        if (null == th) goto L36;
        r2.addSuppressed(th);     // Catch: IOException -> L37 Throwable -> L41
    L38:
        r1.delete();     // Catch: Throwable -> L41
        return;
    L36:
        throw null;     // Catch: IOException -> L37 Throwable -> L41
        goto L38
    }
}
