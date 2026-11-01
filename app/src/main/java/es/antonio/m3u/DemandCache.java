package es.antonio.m3u;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
final class DemandCache {

    static final class Dictionary {
        final LinkedHashMap<Map<String, String>, Integer> maps;
        final LinkedHashMap<String, Integer> text;

        Dictionary() {
            this.text = new LinkedHashMap();
            this.maps = new LinkedHashMap();
        }

        int headers(Map<String, String> r4) {
            if (r4 != null) goto L5;
            return -1;
        L5:
            Integer r0 = this.maps.get(r4);
            if (r0 != null) goto L13;
            Iterator<Map.Entry<String, String>> r1 = r4.entrySet().iterator();
        L9:
            if (r1.hasNext() == false) goto L10;
            Map.Entry<String, String> r2 = r1.next();
            m396id(r2.getKey());
            m396id(r2.getValue());
            goto L9
        L10:
            r0 = Integer.valueOf(this.maps.size());
            this.maps.put(new LinkedHashMap(r4), r0);
        L13:
            return r0.intValue();
        }

        /* JADX INFO: renamed from: id */
        int m396id(String r3) {
            if (r3 != null) goto L5;
            return -1;
        L5:
            Integer r0 = this.text.get(r3);
            if (r0 != null) goto L9;
            r0 = Integer.valueOf(this.text.size());
            this.text.put(r3, r0);
        L9:
            return r0.intValue();
        }
    }

    static final class Source {
        int count;
        final int[] fields;

        Source(int[] r1) {
            this.fields = r1;
        }
    }

    DemandCache() {
    }

    static String[] fields(Entry r13) {
        return new String[]{r13.title, r13.group, r13.logo, r13.url, r13.kind, r13.series, r13.manifest, r13.licenseType, r13.license, r13.plexBase, r13.plexKey, r13.tvgId, r13.tvgName};
    }

    static void record(DataOutputStream r4, Dictionary r5, Entry r6, String r7) throws IOException {
        Entry r8 = Entry.details(r6);
        String[] r0 = fields(r8);
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L4;
        r4.writeInt(r5.m396id(r0[r2]));
        r2 = r2 + 1;
        goto L3
    L4:
        r4.writeInt(r8.season);
        r4.writeInt(r8.episode);
        r4.writeInt(r5.headers(r8.headers));
        r4.writeInt(r5.headers(r8.licenseHeaders));
        r4.writeInt(r5.m396id(r7));
    }

    static boolean valid(File r7, File r8) {
        Throwable r1 = null;
        DataInputStream r2 = new DataInputStream(new FileInputStream(r7));     // Catch: Throwable -> L22
    L19:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L22
        throw th;     // Catch: Throwable -> L22
    L5:
        if (r2.readInt() == 1128809523) goto L7;
    L16:
        boolean r9 = false;
    L17:
        r2.close();     // Catch: Throwable -> L22
        return r9;
    L7:
        if (r2.readInt() != 6) goto L16;
        if (r2.readBoolean() == false) goto L16;
        if (r2.readLong() != r8.lastModified()) goto L16;
    L14:
        if (r2.readLong() != r8.length()) goto L16;
        r9 = true;
    L22:
        th = move-exception;
        if (0 == 0) goto L35;
        if (null == th) goto L27;
        r1.addSuppressed(th);     // Catch: IOException -> L28
    L29:
        return false;
    L27:
        throw null;     // Catch: IOException -> L28
        goto L29
    L35:
        throw th;     // Catch: IOException -> L28
    }

    static void write(File r30, File r31, Catalog r32, Map<Entry, String> r33, Map<String, Integer> r34, String r35) throws IOException {
        long r0 = System.nanoTime();
        long r2 = r31.lastModified();
        long r4 = r31.length();
        File r6 = r30.getParentFile();
        if (r6.isDirectory() == false) goto L5;
    L9:
        File r7 = File.createTempFile("catalog-records-", ".tmp", r6);
        File r8 = File.createTempFile("catalog-directory-", ".tmp", r6);
        Dictionary r9 = new Dictionary();
        ArrayList r10 = new ArrayList();
        int r15 = 65536;
        DataOutputStream r12 = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(r7), 65536));     // Catch: Throwable -> L181
        Iterator<Catalog.Source> r13 = r32.sources.iterator();     // Catch: Throwable -> L174
        int r14 = 0;
    L13:
        if (r13.hasNext() == false) goto L15;
        long r18 = r0;
        long r20 = r2;
        long r22 = r4;
        File r17 = r7;
        int r5 = r15;
        Catalog.Source r1 = r13.next();     // Catch: Throwable -> L172
        String[] r3 = {r1.f177id, r1.name, r1.origin, r1.kind, r1.originalFilename, r1.provider};     // Catch: Throwable -> L172
        int[] r11 = new int[6];     // Catch: Throwable -> L172
        int r16 = 0;
    L145:
        if (r16 >= 6) goto L146;
        r11[r16] = r9.m396id(r3[r16]);     // Catch: Throwable -> L172
        r16 = r16 + 1;
        goto L145
    L146:
        Source r19 = new Source(r11);     // Catch: Throwable -> L172
        r10.add(r19);     // Catch: Throwable -> L172
        if ((r1.entries instanceof SectionStore.DiskEntries) == false) goto L161;
        SectionStore.DiskEntries r21 = (SectionStore.DiskEntries) r1.entries;     // Catch: Throwable -> L172
        Iterator<SectionStore.Part> r23 = r21.source.parts.iterator();     // Catch: Throwable -> L172
    L150:
        if (r23.hasNext() == false) goto L165;
        SectionStore.Part r24 = r23.next();     // Catch: Throwable -> L172
        List<Entry> r25 = r21.state.read(r24.offset, r24.length);     // Catch: Throwable -> L172
        if (r25.size() != r24.count) goto L160;
        Iterator<Entry> r26 = r25.iterator();     // Catch: Throwable -> L172
    L156:
        if (r26.hasNext() == false) goto L150;
        record(r12, r9, r26.next(), r24.provider);     // Catch: Throwable -> L172
        r19.count++;     // Catch: Throwable -> L172
        goto L156
    L160:
        throw new IOException("Section count mismatch");     // Catch: Throwable -> L172
    L165:
        if (r19.count != r1.entries.size()) goto L168;
        r14 = r14 + r19.count;     // Catch: Throwable -> L172
        r15 = r5;
        r7 = r17;
        r0 = r18;
        r2 = r20;
        r4 = r22;
        goto L13
    L168:
        throw new IOException("Source count mismatch");     // Catch: Throwable -> L172
    L161:
        Iterator<Entry> r27 = r1.entries.iterator();     // Catch: Throwable -> L172
    L163:
        if (r27.hasNext() == false) goto L165;
        Entry r28 = r27.next();     // Catch: Throwable -> L172
        record(r12, r9, r28, r33.get(r28));     // Catch: Throwable -> L172
        r19.count++;     // Catch: Throwable -> L172
    L172:
        th = th;
    L176:
        Throwable r110 = th;
        r12.close();     // Catch: Throwable -> L179
        throw r110;     // Catch: Throwable -> L179
    L179:
        th = th;
    L183:
        if (r110 == null) goto L187;
        if (r110 == th) goto L249;
        r110.addSuppressed(th);     // Catch: Throwable -> L188
        throw r110;     // Catch: Throwable -> L188
    L249:
        throw r110;     // Catch: Throwable -> L188
    L187:
        throw th;     // Catch: Throwable -> L188
    L15:
        r12.close();     // Catch: Throwable -> L181
        Iterator<String> r111 = r34.keySet().iterator();     // Catch: Throwable -> L141
    L17:
        if (r111.hasNext() == false) goto L224;
        long r112 = r0;
        long r29 = r2;
        long r210 = r4;
        File r113 = r7;
        int r36 = r15;
        r9.m396id(r111.next());     // Catch: Throwable -> L188
        r15 = r36;
        r7 = r113;
        r0 = r112;
        r2 = r29;
        r4 = r210;
        goto L17
    L224:
        FileOutputStream r114 = new FileOutputStream(r8);     // Catch: Throwable -> L131
        DataOutputStream r115 = new DataOutputStream(new BufferedOutputStream(r114, r15));     // Catch: Throwable -> L120
        BufferedInputStream r116 = new BufferedInputStream(new FileInputStream(r7), r15);     // Catch: Throwable -> L109
        r115.writeInt(1128809523);     // Catch: Throwable -> L102
        r115.writeInt(6);     // Catch: Throwable -> L102
        r115.writeBoolean(true);     // Catch: Throwable -> L102
        r115.writeLong(r2);     // Catch: Throwable -> L102
        r115.writeLong(r4);     // Catch: Throwable -> L102
        r115.writeInt(r9.text.size());     // Catch: Throwable -> L102
        ArrayList r117 = new ArrayList(r9.text.keySet());     // Catch: Throwable -> L102
        int r118 = 0;
    L25:
        File r119 = r7;
    L100:
        th = th;
    L104:
        Throwable r120 = th;
        r116.close();     // Catch: Throwable -> L107
        throw r120;     // Catch: Throwable -> L107
    L107:
        th = th;
    L112:
        if (r120 == null) goto L117;
        if (r120 != th) goto L226;
    L118:
        r115.close();     // Catch: Throwable -> L115
        throw r120;     // Catch: Throwable -> L115
    L115:
        th = th;
    L123:
        if (r120 == null) goto L128;
        if (r120 != th) goto L215;
    L129:
        r114.close();     // Catch: Throwable -> L126
        throw r120;     // Catch: Throwable -> L126
    L126:
        th = th;
    L134:
        if (r120 == null) goto L138;
        if (r120 == th) goto L247;
        r120.addSuppressed(th);     // Catch: Throwable -> L188
        throw r120;     // Catch: Throwable -> L188
    L247:
        throw r120;     // Catch: Throwable -> L188
    L138:
        throw th;     // Catch: Throwable -> L188
    L215:
        r120.addSuppressed(th);     // Catch: Throwable -> L126
        goto L129
    L128:
        r120 = th;
        goto L129
    L226:
        r120.addSuppressed(th);     // Catch: Throwable -> L115
        goto L118
    L117:
        r120 = th;
        goto L118
    L27:
        if (r118 >= r117.size()) goto L28;
        long r121 = r0;
        long r211 = r2;
        long r212 = r4;
        int r37 = Math.min(1024, r117.size() - r118);     // Catch: Throwable -> L100
        byte[][] r38 = new byte[r37][];     // Catch: Throwable -> L100
        int r39 = 0;
        int r40 = 0;
    L90:
        if (r39 >= r37) goto L91;
        r38[r39] = ((String) r117.get(r118 + r39)).getBytes(StandardCharsets.UTF_8);     // Catch: Throwable -> L100
        r40 = r40 + r38[r39].length;
        r39 = r39 + 1;
        goto L90
    L91:
        r115.writeInt(r40);     // Catch: Throwable -> L100
        int r41 = 0;
    L92:
        if (r41 >= r37) goto L93;
        r115.writeInt(r38[r41].length);     // Catch: Throwable -> L100
        r41 = r41 + 1;     // Catch: Throwable -> L100
        goto L92
    L93:
        int r42 = 0;
    L94:
        if (r42 >= r37) goto L95;
        r115.write(r38[r42]);     // Catch: Throwable -> L100
        r42 = r42 + 1;     // Catch: Throwable -> L100
        goto L94
    L95:
        r118 = r118 + 1024;     // Catch: Throwable -> L100
        r7 = r119;
        r0 = r121;
        r2 = r211;
        r4 = r212;
        goto L25
    L28:
        r115.writeInt(r9.maps.size());     // Catch: Throwable -> L100
        Iterator<Map<String, String>> r43 = r9.maps.keySet().iterator();     // Catch: Throwable -> L100
    L30:
        if (r43.hasNext() == false) goto L31;
        long r122 = r0;
        long r213 = r2;
        long r214 = r4;
        Map<String, String> r44 = r43.next();     // Catch: Throwable -> L100
        r115.writeInt(r44.size());     // Catch: Throwable -> L100
        Iterator<Map.Entry<String, String>> r45 = r44.entrySet().iterator();     // Catch: Throwable -> L100
    L86:
        if (r45.hasNext() == false) goto L87;
        Map.Entry<String, String> r46 = r45.next();     // Catch: Throwable -> L100
        r115.writeInt(r9.m396id(r46.getKey()));     // Catch: Throwable -> L100
        r115.writeInt(r9.m396id(r46.getValue()));     // Catch: Throwable -> L100
        goto L86
    L87:
        r0 = r122;
        r2 = r213;
        r4 = r214;
        goto L30
    L31:
        r115.writeInt(r10.size());     // Catch: Throwable -> L100
        byte[] r123 = new byte[65536];     // Catch: Throwable -> L100
        Iterator r47 = r10.iterator();     // Catch: Throwable -> L100
    L33:
        if (r47.hasNext() == false) goto L35;
        long r124 = r0;
        Source r48 = (Source) r47.next();     // Catch: Throwable -> L100
        int[] r49 = r48.fields;     // Catch: Throwable -> L100
        long r215 = r2;
        int r50 = r49.length;     // Catch: Throwable -> L100
        int r51 = 0;
    L70:
        if (r51 >= r50) goto L211;
        r115.writeInt(r49[r51]);     // Catch: Throwable -> L100
        r51 = r51 + 1;     // Catch: Throwable -> L100
        r4 = r4;
        goto L70
    L211:
        r115.writeInt(r48.count);     // Catch: Throwable -> L81
        long r52 = ((long) r48.count) * 72;     // Catch: Throwable -> L81
    L73:
        if (r52 <= 0) goto L74;
        long r216 = r4;
        int r53 = r116.read(r123, 0, (int) Math.min(65536, r52));     // Catch: Throwable -> L100
        if (r53 < 0) goto L246;
        r115.write(r123, 0, r53);     // Catch: Throwable -> L100
        r52 = r52 - ((long) r53);     // Catch: Throwable -> L100
        r4 = r216;
        goto L73
    L246:
        throw new EOFException("Directory records");     // Catch: Throwable -> L100
    L74:
        r0 = r124;
        r2 = r215;
    L81:
        th = th;
        goto L104
    L35:
        if (r116.read() != (-1)) goto L67;
        r115.writeInt(r34.size());     // Catch: Throwable -> L100
        Iterator<Map.Entry<String, Integer>> r54 = r34.entrySet().iterator();     // Catch: Throwable -> L100
    L38:
        if (r54.hasNext() == false) goto L39;
        Map.Entry<String, Integer> r125 = r54.next();     // Catch: Throwable -> L100
        r115.writeInt(r9.m396id(r125.getKey()));     // Catch: Throwable -> L100
        r115.writeInt(r125.getValue().intValue());     // Catch: Throwable -> L100
        r0 = r0;
        goto L38
    L39:
        r115.flush();     // Catch: Throwable -> L100
        r114.getFD().sync();     // Catch: Throwable -> L100
        r116.close();     // Catch: Throwable -> L62
        r115.close();     // Catch: Throwable -> L60
        r114.close();     // Catch: Throwable -> L58
        if (r31.lastModified() != r2) goto L245;
        if (r31.length() != r4) goto L245;
        if (r8.renameTo(r30) == false) goto L55;
        BlackBox.event("CATALOG_DIRECTORY_SAVED", "reason=" + r35 + " entries=" + r14 + " duration_ms=" + ((System.nanoTime() - r0) / 1000000) + " bytes=" + r30.length());     // Catch: Throwable -> L52
        r119.delete();
        return;
    L52:
        th = th;
        boolean r126 = true;
    L190:
        r119.delete();
        if (r126 == true) goto L193;
        r8.delete();
    L193:
        throw th;
    L55:
        throw new IOException("Cannot commit directory cache");     // Catch: Throwable -> L188
    L245:
        throw new IOException("Catalog changed during cache build");     // Catch: Throwable -> L188
    L58:
        th = th;
    L133:
        r120 = null;
    L60:
        th = th;
    L122:
        r120 = null;
    L62:
        th = th;
    L111:
        r120 = null;
        goto L112
    L67:
        throw new IOException("Directory records trailing");     // Catch: Throwable -> L100
    L102:
        th = th;
    L109:
        th = th;
    L120:
        th = th;
    L131:
        th = th;
    L141:
        th = th;
        r119 = r7;
    L189:
        r126 = false;
    L174:
        th = th;
    L188:
        th = th;
    L181:
        th = th;
        r110 = null;
        goto L183
    L5:
        if (r6.mkdirs() == true) goto L9;
        throw new IOException("Directory cache parent");
    }
}
