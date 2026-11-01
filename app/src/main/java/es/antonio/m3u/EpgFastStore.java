package es.antonio.m3u;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;

/* JADX INFO: loaded from: classes7.dex */
final class EpgFastStore {
    static final long LIMIT = 268435456;
    static final int MAGIC = 1128809749;
    static final int MAX = 100000;

    private EpgFastStore() {
    }

    static int bound(int r0, int r1) throws IOException {
        if (r0 < 0) goto L6;
        if (r0 > r1) goto L6;
        return r0;
    L6:
        throw new IOException("EPG cache bounds");
    }

    static File file(File r2) {
        return new File(r2, "epg-fast-v215.bin");
    }

    static EpgData read(File r12, File r13) throws IOException {
        File r14 = file(r12);
        if (r13.isFile() == false) goto L51;
        if (r14.length() > LIMIT) goto L51;
        CRC32 r0 = new CRC32();
        DataInputStream r1 = new DataInputStream(new CheckedInputStream(new BufferedInputStream(new FileInputStream(r14), 65536), r0));
    L44:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L47
    L49:
        throw th;
    L47:
        th = move-exception;
        th.addSuppressed(th);
        goto L49
    L8:
        if (r1.readInt() != MAGIC) goto L61;
        if (r1.readLong() != r13.lastModified()) goto L61;
        if (r1.readLong() != r13.length()) goto L61;
        EpgData r15 = new EpgData();     // Catch: Throwable -> L44
        r15.loadedAt = r1.readLong();     // Catch: Throwable -> L44
        int r16 = bound(r1.readInt(), AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH);     // Catch: Throwable -> L44
        int r4 = 0;
    L14:
        if (r4 >= r16) goto L23;
        String r5 = r1.readUTF();     // Catch: Throwable -> L44
        int r6 = bound(r1.readInt(), 256);     // Catch: Throwable -> L44
        ArrayList r7 = new ArrayList(r6);     // Catch: Throwable -> L44
        int r8 = 0;
    L16:
        if (r8 >= r6) goto L19;
        r7.add(text(r1));     // Catch: Throwable -> L44
        r8 = r8 + 1;     // Catch: Throwable -> L44
        goto L16
    L19:
        if (r15.names.put(r5, r7) != null) goto L22;
        r4 = r4 + 1;     // Catch: Throwable -> L44
        goto L14
    L22:
        throw new IOException("Duplicate EPG name");     // Catch: Throwable -> L44
    L23:
        int r17 = bound(r1.readInt(), AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH);     // Catch: Throwable -> L44
        int r2 = 0;
        int r9 = 0;
    L24:
        if (r2 >= r17) goto L34;
        String r10 = r1.readUTF();     // Catch: Throwable -> L44
        int r11 = bound(r1.readInt(), 100000 - r9);     // Catch: Throwable -> L44
        r9 = r9 + r11;     // Catch: Throwable -> L44
        ArrayList r18 = new ArrayList(r11);     // Catch: Throwable -> L44
        int r19 = 0;
    L26:
        if (r19 >= r11) goto L29;
        EpgData.Programme r20 = new EpgData.Programme();     // Catch: Throwable -> L44
        r20.channel = text(r1);     // Catch: Throwable -> L44
        r20.start = r1.readLong();     // Catch: Throwable -> L44
        r20.stop = r1.readLong();     // Catch: Throwable -> L44
        r20.title = text(r1);     // Catch: Throwable -> L44
        r20.description = text(r1);     // Catch: Throwable -> L44
        r18.add(r20);     // Catch: Throwable -> L44
        r19 = r19 + 1;     // Catch: Throwable -> L44
        goto L26
    L29:
        if (r15.programmes.put(r10, r18) != null) goto L32;
        r2 = r2 + 1;     // Catch: Throwable -> L44
        goto L24
    L32:
        throw new IOException("Duplicate EPG channel");     // Catch: Throwable -> L44
    L34:
        if (r1.readLong() != r0.getValue()) goto L41;
        if (r1.read() != (-1)) goto L41;
        r1.close();
        return r15;
    L41:
        throw new IOException("EPG cache checksum");     // Catch: Throwable -> L44
    L61:
        throw new IOException("EPG cache fingerprint");     // Catch: Throwable -> L44
    L51:
        throw new IOException("EPG source/cache missing");
    }

    static void remove(File r2) {
        file(r2).delete();
        new File(file(r2).getPath() + ".tmp").delete();
    }

    static void save(File r12, File r13, EpgData r14) {
        if (r13.isFile() == true) goto L5;
        return;
    L5:
        if (r14.count() > 100000) goto L68;
        File r0 = file(r12);
        File r1 = new File(r0.getPath() + ".tmp");
        long r2 = r13.lastModified();
        long r4 = r13.length();
        r12.mkdirs();     // Catch: Throwable -> L50 IOException -> L52
        CRC32 r15 = new CRC32();     // Catch: Throwable -> L50 IOException -> L52
        BufferedOutputStream r6 = new BufferedOutputStream(new FileOutputStream(r1), 65536);     // Catch: Throwable -> L50 IOException -> L52
        DataOutputStream r7 = new DataOutputStream(new CheckedOutputStream(r6, r15));     // Catch: Throwable -> L44
        r7.writeInt(MAGIC);     // Catch: Throwable -> L44
        r7.writeLong(r2);     // Catch: Throwable -> L44
        r7.writeLong(r4);     // Catch: Throwable -> L44
        r7.writeLong(r14.loadedAt);     // Catch: Throwable -> L44
        r7.writeInt(r14.names.size());     // Catch: Throwable -> L44
        if (r14.names.size() > 20000) goto L65;
        if (r14.programmes.size() > 20000) goto L65;
        Iterator<Map.Entry<String, List<String>>> r8 = r14.names.entrySet().iterator();     // Catch: Throwable -> L44
    L15:
        if (r8.hasNext() == false) goto L24;
        Map.Entry<String, List<String>> r9 = r8.next();     // Catch: Throwable -> L44
        r7.writeUTF(r9.getKey());     // Catch: Throwable -> L44
        if (r9.getValue().size() > 256) goto L23;
        r7.writeInt(r9.getValue().size());     // Catch: Throwable -> L44
        Iterator<String> r10 = r9.getValue().iterator();     // Catch: Throwable -> L44
    L20:
        if (r10.hasNext() == false) goto L15;
        text(r7, r10.next());     // Catch: Throwable -> L44
        goto L20
    L23:
        throw new IOException("EPG aliases limit");     // Catch: Throwable -> L44
    L24:
        r7.writeInt(r14.programmes.size());     // Catch: Throwable -> L44
        Iterator<Map.Entry<String, List<EpgData.Programme>>> r16 = r14.programmes.entrySet().iterator();     // Catch: Throwable -> L44
    L26:
        if (r16.hasNext() == false) goto L31;
        Map.Entry<String, List<EpgData.Programme>> r11 = r16.next();     // Catch: Throwable -> L44
        r7.writeUTF(r11.getKey());     // Catch: Throwable -> L44
        r7.writeInt(r11.getValue().size());     // Catch: Throwable -> L44
        Iterator<EpgData.Programme> r17 = r11.getValue().iterator();     // Catch: Throwable -> L44
    L29:
        if (r17.hasNext() == false) goto L26;
        EpgData.Programme r18 = r17.next();     // Catch: Throwable -> L44
        text(r7, r18.channel);     // Catch: Throwable -> L44
        r7.writeLong(r18.start);     // Catch: Throwable -> L44
        r7.writeLong(r18.stop);     // Catch: Throwable -> L44
        text(r7, r18.title);     // Catch: Throwable -> L44
        text(r7, r18.description);     // Catch: Throwable -> L44
        goto L29
    L31:
        r7.flush();     // Catch: Throwable -> L44
        new DataOutputStream(r6).writeLong(r15.getValue());     // Catch: Throwable -> L44
        r6.flush();     // Catch: Throwable -> L44
        r6.close();     // Catch: Throwable -> L50 IOException -> L52
        if (r13.lastModified() == r2) goto L35;
    L40:
        r1.delete();     // Catch: Throwable -> L50 IOException -> L52
        return;
    L35:
        if (r13.length() != r4) goto L40;
        if (r1.length() > LIMIT) goto L40;
        if (r1.renameTo(r0) == false) goto L40;
        return;
    L65:
        throw new IOException("EPG channel limit");     // Catch: Throwable -> L44
    L44:
        th = move-exception;
        r6.close();     // Catch: Throwable -> L47
    L49:
        throw th;     // Catch: Throwable -> L50 IOException -> L52
    L47:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L50 IOException -> L52
    L53:
        r1.delete();
        return;
    }

    static String text(DataInputStream r1) throws IOException {
        if (r1.readBoolean() == true) goto L7;
        return null;
    L7:
        return r1.readUTF();
    }

    static void text(DataOutputStream r1, String r2) throws IOException {
        if (r2 == null) goto L4;
        boolean r0 = true;
    L5:
        r1.writeBoolean(r0);
        if (r2 == null) goto L9;
        r1.writeUTF(r2);
        return;
    L9:
        return;
    L4:
        r0 = false;
        goto L5
    }
}
