package es.antonio.m3u;

import android.content.Context;
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;

/* JADX INFO: loaded from: classes7.dex */
final class EpgChannelStore {
    static final long HOUR = 3600000;
    static final long LIMIT = 67108864;
    static final int MAGIC = 1128812054;
    static final int MAX = 250000;
    static final long WINDOW = 86400000;

    EpgChannelStore() {
    }

    static int bound(int r0, int r1) throws IOException {
        if (r0 < 0) goto L6;
        if (r0 > r1) goto L6;
        return r0;
    L6:
        throw new IOException("EPG projection bounds");
    }

    static EpgData compact(EpgData r12, long r13) {
        EpgData r0 = new EpgData();
        r0.loadedAt = r12.loadedAt;
        Iterator<Map.Entry<String, List<String>>> r1 = r12.names.entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        Map.Entry<String, List<String>> r2 = r1.next();
        r0.names.put(r2.getKey(), new ArrayList(r2.getValue()));
        goto L4
    L6:
        Iterator<Map.Entry<String, List<EpgData.Programme>>> r3 = r12.programmes.entrySet().iterator();
    L8:
        if (r3.hasNext() == false) goto L36;
        Map.Entry<String, List<EpgData.Programme>> r4 = r3.next();
        ArrayList r5 = new ArrayList();
        Iterator<EpgData.Programme> r6 = r4.getValue().iterator();
    L11:
        if (r6.hasNext() == false) goto L22;
        EpgData.Programme r7 = r6.next();
        if (r7.stop <= r7.start) goto L15;
        long r8 = r7.stop;
    L17:
        if (r8 <= (r13 - HOUR)) goto L11;
        if (r7.start >= (WINDOW + r13)) goto L11;
        EpgData.Programme r9 = new EpgData.Programme();
        r9.channel = r7.channel;
        r9.title = r7.title;
        r9.description = r7.description;
        r9.start = r7.start;
        r9.stop = r7.stop;
        r5.add(r9);
        goto L11
    L15:
        r8 = 7200000 + r7.start;
        goto L17
    L22:
        if (r5.isEmpty() == true) goto L8;
        Collections.sort(r5, new EpgChannelStore$$ExternalSyntheticLambda0());
        int r10 = 0;
    L25:
        if (r10 >= r5.size()) goto L34;
        EpgData.Programme r11 = (EpgData.Programme) r5.get(r10);
        if (r11.stop > r11.start) goto L33;
        int r14 = r10 + 1;
        if (r14 >= r5.size()) goto L31;
        long r15 = ((EpgData.Programme) r5.get(r14)).start;
    L32:
        r11.stop = r15;
        goto L33
    L31:
        r15 = r11.start + 7200000;
    L33:
        r10 = r10 + 1;
        goto L25
    L34:
        r0.programmes.put(r4.getKey(), r5);
        goto L8
    L36:
        r12.epg217CopyNames(r0);
        return r0;
    }

    static File file(Context r2) {
        return new File(r2.getFilesDir(), "epg-channels-v216.bin");
    }

    static /* synthetic */ int lambda$compact$0(EpgData.Programme r2, EpgData.Programme r3) {
        return Long.compare(r2.start, r3.start);
    }

    static EpgData read(Context r11, long r12) throws IOException {
        File r0 = new File(r11.getFilesDir(), "epg.dat");
        File r13 = file(r11);
        if (r0.isFile() == false) goto L59;
        if (r13.length() > LIMIT) goto L59;
        CRC32 r1 = new CRC32();
        DataInputStream r2 = new DataInputStream(new CheckedInputStream(new BufferedInputStream(new FileInputStream(r13), 65536), r1));
    L52:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L55
    L57:
        throw th;
    L55:
        th = move-exception;
        th.addSuppressed(th);
        goto L57
    L8:
        if (r2.readInt() != MAGIC) goto L51;
        if (r2.readLong() != r0.lastModified()) goto L51;
        if (r2.readLong() != r0.length()) goto L51;
        long r3 = r2.readLong();     // Catch: Throwable -> L52
        if (r12 < (r3 - HOUR)) goto L49;
        if (r12 >= (r3 + 43200000)) goto L49;
        EpgData r14 = new EpgData();     // Catch: Throwable -> L52
        r14.loadedAt = r2.readLong();     // Catch: Throwable -> L52
        int r15 = bound(r2.readInt(), AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH);     // Catch: Throwable -> L52
        int r4 = 0;
    L18:
        if (r4 >= r15) goto L28;
        String r5 = text(r2);     // Catch: Throwable -> L52
        int r6 = bound(r2.readInt(), 256);     // Catch: Throwable -> L52
        ArrayList r7 = new ArrayList(r6);     // Catch: Throwable -> L52
        int r8 = 0;
    L20:
        if (r8 >= r6) goto L22;
        r7.add(text(r2));     // Catch: Throwable -> L52
        r8 = r8 + 1;     // Catch: Throwable -> L52
        goto L20
    L22:
        if (r5 == null) goto L27;
        if (r14.names.put(r5, r7) != null) goto L27;
        r4 = r4 + 1;     // Catch: Throwable -> L52
    L27:
        throw new IOException("EPG aliases");     // Catch: Throwable -> L52
    L28:
        int r16 = bound(r2.readInt(), AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH);     // Catch: Throwable -> L52
        int r17 = 0;
        int r9 = 0;
    L29:
        if (r17 >= r16) goto L40;
        String r10 = text(r2);     // Catch: Throwable -> L52
        int r18 = bound(r2.readInt(), MAX - r9);     // Catch: Throwable -> L52
        r9 = r9 + r18;     // Catch: Throwable -> L52
        ArrayList r19 = new ArrayList(r18);     // Catch: Throwable -> L52
        int r20 = 0;
    L31:
        if (r20 >= r18) goto L33;
        EpgData.Programme r21 = new EpgData.Programme();     // Catch: Throwable -> L52
        r21.channel = text(r2);     // Catch: Throwable -> L52
        r21.title = text(r2);     // Catch: Throwable -> L52
        r21.description = text(r2);     // Catch: Throwable -> L52
        r21.start = r2.readLong();     // Catch: Throwable -> L52
        r21.stop = r2.readLong();     // Catch: Throwable -> L52
        r19.add(r21);     // Catch: Throwable -> L52
        r20 = r20 + 1;     // Catch: Throwable -> L52
        goto L31
    L33:
        if (r10 == null) goto L38;
        if (r14.programmes.put(r10, r19) != null) goto L38;
        r17 = r17 + 1;     // Catch: Throwable -> L52
    L38:
        throw new IOException("EPG channels");     // Catch: Throwable -> L52
    L40:
        if (r2.readLong() != r1.getValue()) goto L47;
        if (r2.read() != (-1)) goto L47;
        r14.ensureIndexed();     // Catch: Throwable -> L52
        r2.close();
        return r14;
    L47:
        throw new IOException("EPG projection checksum");     // Catch: Throwable -> L52
    L49:
        throw new IOException("EPG projection expired");     // Catch: Throwable -> L52
    L51:
        throw new IOException("EPG projection changed");     // Catch: Throwable -> L52
    L59:
        throw new IOException("EPG projection absent");
    }

    static synchronized void remove(Context r3) {
        monitor-enter(EpgChannelStore.class);
        file(r3).delete();     // Catch: Throwable -> L7
        new File(file(r3) + ".tmp").delete();     // Catch: Throwable -> L7
        monitor-exit(EpgChannelStore.class);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    static EpgData save(Context r0, File r1, EpgData r2, long r3) {
        return saveReady(r0, r1, compact(r2, r3), r3);
    }

    static synchronized EpgData saveReady(Context r16, File r17, EpgData r18, long r19) {
        monitor-enter(EpgChannelStore.class);
        File r0 = file(r16);     // Catch: Throwable -> L60
        File r3 = new File(r0 + ".tmp");     // Catch: Throwable -> L60
        long r4 = r17.lastModified();     // Catch: Throwable -> L60
        long r6 = r17.length();     // Catch: Throwable -> L60
        if (r17.isFile() == true) goto L7;
    L58:
        monitor-exit(EpgChannelStore.class);
        return r18;
    L7:
        if (r18.count() > MAX) goto L58;
        if (r18.names.size() > 20000) goto L58;
        if (r18.programmes.size() > 20000) goto L58;
        CRC32 r8 = new CRC32();     // Catch: Throwable -> L52 IOException -> L54 Throwable -> L60
        BufferedOutputStream r9 = new BufferedOutputStream(new FileOutputStream(r3), 65536);     // Catch: Throwable -> L52 IOException -> L54 Throwable -> L60
        DataOutputStream r10 = new DataOutputStream(new CheckedOutputStream(r9, r8));     // Catch: Throwable -> L44
        r10.writeInt(MAGIC);     // Catch: Throwable -> L44
        r10.writeLong(r4);     // Catch: Throwable -> L44
        r10.writeLong(r6);     // Catch: Throwable -> L44
        r10.writeLong(r19);     // Catch: Throwable -> L44
        r10.writeLong(r18.loadedAt);     // Catch: Throwable -> L44
        r10.writeInt(r18.names.size());     // Catch: Throwable -> L44
        Iterator<Map.Entry<String, List<String>>> r11 = r18.names.entrySet().iterator();     // Catch: Throwable -> L44
    L16:
        if (r11.hasNext() == false) goto L25;
        Map.Entry<String, List<String>> r12 = r11.next();     // Catch: Throwable -> L44
        text(r10, r12.getKey());     // Catch: Throwable -> L44
        if (r12.getValue().size() > 256) goto L24;
        r10.writeInt(r12.getValue().size());     // Catch: Throwable -> L44
        Iterator<String> r13 = r12.getValue().iterator();     // Catch: Throwable -> L44
    L21:
        if (r13.hasNext() == false) goto L16;
        text(r10, r13.next());     // Catch: Throwable -> L44
        goto L21
    L24:
        throw new IOException("EPG aliases limit");     // Catch: Throwable -> L44
    L25:
        r10.writeInt(r18.programmes.size());     // Catch: Throwable -> L44
        Iterator<Map.Entry<String, List<EpgData.Programme>>> r14 = r18.programmes.entrySet().iterator();     // Catch: Throwable -> L44
    L27:
        if (r14.hasNext() == false) goto L32;
        Map.Entry<String, List<EpgData.Programme>> r15 = r14.next();     // Catch: Throwable -> L44
        text(r10, r15.getKey());     // Catch: Throwable -> L44
        r10.writeInt(r15.getValue().size());     // Catch: Throwable -> L44
        Iterator<EpgData.Programme> r110 = r15.getValue().iterator();     // Catch: Throwable -> L44
    L30:
        if (r110.hasNext() == false) goto L27;
        EpgData.Programme r111 = r110.next();     // Catch: Throwable -> L44
        text(r10, r111.channel);     // Catch: Throwable -> L44
        text(r10, r111.title);     // Catch: Throwable -> L44
        text(r10, r111.description);     // Catch: Throwable -> L44
        r10.writeLong(r111.start);     // Catch: Throwable -> L44
        r10.writeLong(r111.stop);     // Catch: Throwable -> L44
        goto L30
    L32:
        r10.flush();     // Catch: Throwable -> L44
        new DataOutputStream(r9).writeLong(r8.getValue());     // Catch: Throwable -> L44
        r9.flush();     // Catch: Throwable -> L44
        r9.close();     // Catch: Throwable -> L52 IOException -> L54 Throwable -> L60
        if (r17.lastModified() != r4) goto L43;
        if (r17.length() != r6) goto L43;
        if (r3.length() > LIMIT) goto L43;
        if (r3.renameTo(r0) == false) goto L43;
    L56:
        monitor-exit(EpgChannelStore.class);
        return r18;
    L43:
        throw new IOException("EPG changed while saving");     // Catch: Throwable -> L52 IOException -> L54 Throwable -> L60
    L44:
        th = move-exception;
        r9.close();     // Catch: Throwable -> L48
        throw th;     // Catch: Throwable -> L52 IOException -> L54 Throwable -> L60
    L48:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L52 IOException -> L54 Throwable -> L60
        throw th;     // Catch: Throwable -> L52 IOException -> L54 Throwable -> L60
    L52:
        e = move-exception;
        r3.delete();     // Catch: Throwable -> L60
        BlackBox.event("EPG_CHANNEL_CACHE_WRITE_ERROR", e.getClass().getSimpleName());     // Catch: Throwable -> L60
    L60:
        th = move-exception;
        throw th;
    }

    static String text(DataInputStream r2) throws IOException {
        int r0 = r2.readInt();
        if (r0 != (-1)) goto L6;
        return null;
    L6:
        bound(r0, 2097152);
        byte[] r1 = new byte[r0];
        r2.readFully(r1);
        return new String(r1, "UTF-8");
    }

    static void text(DataOutputStream r2, String r3) throws IOException {
        if (r3 != null) goto L5;
        r2.writeInt(-1);
        return;
    L5:
        byte[] r4 = r3.getBytes("UTF-8");
        if (r4.length > 2097152) goto L10;
        r2.writeInt(r4.length);
        r2.write(r4);
        return;
    L10:
        throw new IOException("EPG text limit");
    }
}
