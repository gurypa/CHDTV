package es.antonio.m3u;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes7.dex */
final class DemandSeries {

    static final class Group {
        final Set<String> episodes;
        DemandNavigation.Ref first;
        final Set<Integer> seasons;

        Group() {
            this.episodes = new HashSet();
            this.seasons = new HashSet();
        }
    }

    DemandSeries() {
    }

    static MainActivity.CatalogPlan prepare(DemandCatalog r43, MainActivity.CatalogRequest r44) {
        r43 = r43;
        MainActivity.CatalogRequest r2 = r44;
        String r3 = " ";
        r43.resolve();
        if (r43.directory != null) goto L5;
        return null;
    L5:
        long r5 = System.nanoTime();
        int r7 = r43.created();
        DemandCatalog.Directory r8 = r43.directory;
        TreeSet r9 = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        DemandSeriesIndex.Index r10 = DemandSeriesIndex.get(r43, r2.provider);
        if (r10 == null) goto L13;
        r9.addAll(r10.choices.subList(1, r10.choices.size()));
        if (r2.series == null) goto L13;
        Map<DemandCatalog.Rows, int[]> r0 = DemandSeriesIndex.select(r43, r10, r2.series);     // Catch: IOException -> L11
    L14:
        String r12 = "r";
        String r13 = "Mixta";
        String r15 = "Series";
        if (r10 != null) goto L65;
        Iterator<Catalog.Source> r11 = r8.catalog.sources.iterator();
    L18:
        if (r11.hasNext() == false) goto L19;
        Catalog.Source r14 = r11.next();
        if (r14.kind != null) goto L23;
    L27:
        DemandCatalog.Rows r4 = (DemandCatalog.Rows) r14.entries;
        DemandFile r16 = new DemandFile(r4.directory.file, r12);     // Catch: Throwable -> L55
        int r17 = 0;
    L30:
        Iterator<Catalog.Source> r20 = r11;
        if (r17 >= r4.count) goto L33;
        String r21 = r3;
        int r18 = Math.min(1024, r4.count - r17);     // Catch: Throwable -> L49
        byte[] r6 = new byte[r18 * 72];     // Catch: Throwable -> L49
        String r22 = r12;
        String r23 = r13;
        long r24 = r5;
        r16.seek(r4.records + (((long) r17) * 72));     // Catch: Throwable -> L49
        r16.readFully(r6);     // Catch: Throwable -> L49
        IntBuffer r19 = ByteBuffer.wrap(r6).asIntBuffer();     // Catch: Throwable -> L49
        int r25 = 0;
    L36:
        if (r25 >= r18) goto L37;
        int r26 = r25 * 18;     // Catch: Throwable -> L49
        if ("Series".equals(r4.directory.value(r16, r19.get(r26 + 4))) == true) goto L42;
        int r27 = r18;
    L48:
        r25 = r25 + 1;
        r18 = r27;
        goto L36
    L42:
        if (r2.provider == null) goto L46;
        r27 = r18;
        if (r2.provider.equals(r4.directory.value(r16, r19.get(r26 + 17))) == false) goto L48;
    L47:
        r9.add(r4.directory.value(r16, r19.get(r26 + 1)));     // Catch: Throwable -> L49
        goto L48
    L46:
        r27 = r18;
        goto L47
    L37:
        r17 = r17 + 1024;     // Catch: Throwable -> L49
        r11 = r20;
        r3 = r21;
        r12 = r22;
        r13 = r23;
        r5 = r24;
        goto L30
    L33:
        r16.close();     // Catch: Throwable -> L55
        r11 = r20;
    L49:
        th = move-exception;
        Throwable r28 = th;
        r16.close();     // Catch: Throwable -> L53
        throw r28;     // Catch: Throwable -> L53
    L53:
        th = th;
    L57:
        if (r28 == null) goto L61;
        if (r28 == th) goto L292;
        r28.addSuppressed(th);     // Catch: IOException -> L62
        throw r28;     // Catch: IOException -> L62
    L62:
        e = move-exception;
        throw new IllegalStateException(e);
    L292:
        throw r28;     // Catch: IOException -> L62
    L61:
        throw th;     // Catch: IOException -> L62
    L55:
        th = th;
        r28 = null;
        goto L57
    L23:
        if (r13.equals(r14.kind) == true) goto L27;
        if ("Series".equals(r14.kind) == true) goto L27;
    L19:
        String r29 = r3;
        long r210 = r5;
        String r211 = r12;
        String r212 = r13;
    L66:
        ArrayList r30 = new ArrayList();
        String r31 = "Todas las secciones";
        r30.add("Todas las secciones");
        r30.addAll(r9);
        if (r30.contains(r2.section) == false) goto L69;
        String r32 = r2.section;
    L70:
        TreeMap r33 = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        ArrayList r34 = new ArrayList();
        HashSet r110 = new HashSet();
        String r111 = r2.filter.toLowerCase(Locale.ROOT);
        if (r2.provider != null) goto L73;
        boolean r112 = true;
    L75:
        if (r2.series != null) goto L77;
    L79:
        boolean r113 = false;
    L81:
        if (r2.series != null) goto L83;
    L85:
        boolean r114 = false;
    L86:
        Iterator<Catalog.Source> r35 = r8.catalog.sources.iterator();
    L88:
        if (r35.hasNext() == false) goto L89;
        Catalog.Source r1 = r35.next();
        r30 = r30;
        if (r1.kind == null) goto L128;
        int r213 = r7;
        String r36 = r212;
        if (r36.equals(r1.kind) == false) goto L126;
    L129:
        DemandCatalog.Rows r37 = (DemandCatalog.Rows) r1.entries;
        if (r0 != null) goto L132;
        int[] r38 = null;
    L133:
        if (r0 == null) goto L136;
        if (r38 != null) goto L136;
    L127:
        r212 = r36;
        r7 = r213;
    L136:
        if (r38 != null) goto L138;
        int r39 = r37.count;
    L139:
        String r214 = r36;
        Iterator<Catalog.Source> r215 = r35;
        TreeMap r310 = r33;
        String r40 = r211;
        DemandFile r41 = new DemandFile(r37.directory.file, r40);     // Catch: Throwable -> L239
        int r42 = 0;
    L141:
        if (r42 >= r39) goto L142;
        String r216 = r40;
        int r311 = r39;
        int r45 = Math.min(1024, r39 - r42);     // Catch: Throwable -> L233
        IntBuffer r46 = ByteBuffer.wrap(DemandSeriesIndex.block(r37, r41, r42, r45, r38)).asIntBuffer();     // Catch: Throwable -> L233
        boolean r115 = r113;
        int r116 = 0;
    L146:
        if (r116 >= r45) goto L147;
        int r312 = r45;
        int r47 = r116 * 18;     // Catch: Throwable -> L233
        boolean r313 = r112;
        ArrayList r314 = r34;
        if (r15.equals(r37.directory.value(r41, r46.get(r47 + 4))) == true) goto L153;
    L150:
        r46 = r46;
        r38 = r38;
        String r315 = r31;
        String r316 = r32;
        int r317 = r42;
        int r48 = r116;
        String r318 = r15;
    L151:
        String r49 = r29;
    L152:
        TreeMap r50 = r310;
        ArrayList r51 = r314;
    L232:
        r116 = r48 + 1;
        r29 = r49;
        r34 = r51;
        r310 = r50;
        r45 = r312;
        r112 = r313;
        r15 = r318;
        r31 = r315;
        r32 = r316;
        r42 = r317;
        r38 = r38;
        r46 = r46;
        r2 = r44;
        goto L146
    L153:
        String r52 = r37.directory.value(r41, r46.get(r47 + 17));     // Catch: Throwable -> L233
        if (r2.provider != null) goto L156;
    L158:
        r318 = r15;
        String r117 = r37.directory.value(r41, r46.get(r47 + 1));     // Catch: Throwable -> L233
        if (r31.equals(r32) == false) goto L161;
    L164:
        String r118 = r37.series(r41, r46, r47);     // Catch: Throwable -> L233
        r315 = r31;
        if (r2.series != null) goto L167;
    L169:
        int r53 = r46.get(r47 + 13);     // Catch: Throwable -> L233
        r316 = r32;
        int r54 = r46.get(r47 + 14);     // Catch: Throwable -> L233
        r317 = r42;
        if (r2.season == null) goto L174;
        r48 = r116;
        if (Math.max(0, r53) == r2.season.intValue()) goto L176;
        r46 = r46;
        r38 = r38;
    L176:
        r38 = r38;
        if (r2.favorites == false) goto L179;
    L184:
        String r55 = r37.directory.value(r41, r46.get(r47));     // Catch: Throwable -> L233
        if (r2.favorites == false) goto L189;
        r46 = r46;
        if (Boolean.TRUE.equals(r2.prefs.get("Series\n" + r117 + "\n" + r55)) == false) goto L151;
    L191:
        if (r111.isEmpty() == true) goto L195;
        r49 = r29;
        if (new StringBuilder(String.valueOf(r55)).append(r49).append(r117).append(r49).append(r52).toString().toLowerCase(Locale.ROOT).contains(r111) == false) goto L152;
    L196:
        if (r53 < 0) goto L199;
        if (r54 < 0) goto L199;
        StringBuilder r56 = new StringBuilder(String.valueOf(r118)).append("\n").append(r53).append("\n").append(r54);     // Catch: Throwable -> L233
    L200:
        String r57 = r56.toString();     // Catch: Throwable -> L233
        if (r114 == true) goto L203;
        r51 = r314;
        if (r313 == true) goto L220;
        if (r115 == false) goto L219;
        StringBuilder r58 = new StringBuilder("Temporada ");     // Catch: Throwable -> L233
        if (Math.max(0, r53) >= 10) goto L217;
        String r59 = "0";
    L218:
        r52 = r58.append(r59).append(Math.max(0, r53)).toString();     // Catch: Throwable -> L233
        goto L220
    L217:
        r59 = "";
        goto L218
    L219:
        r52 = r118;
    L220:
        r50 = r310;
        Group r60 = (Group) r50.get(r52);     // Catch: Throwable -> L233
        if (r60 != null) goto L227;
        r60 = new Group();     // Catch: Throwable -> L233
        if (r38 != null) goto L225;
        int r119 = r317 + r48;     // Catch: Throwable -> L233
    L226:
        r60.first = new DemandNavigation.Ref(r37, r119);     // Catch: Throwable -> L233
        r50.put(r52, r60);     // Catch: Throwable -> L233
        goto L227
    L225:
        r119 = r38[r317 + r48];     // Catch: Throwable -> L233
    L227:
        Set<String> r61 = r60.episodes;     // Catch: Throwable -> L233
        if (r313 == true) goto L231;
        r118 = r57;
    L231:
        r61.add(r118);     // Catch: Throwable -> L233
        r60.seasons.add(Integer.valueOf(r53));     // Catch: Throwable -> L233
        goto L232
    L203:
        if (r110.add(r57) == false) goto L208;
        if (r38 != null) goto L206;
        int r62 = r317 + r48;     // Catch: Throwable -> L233
    L207:
        r51 = r314;
        r51.add(r37.get(r62));     // Catch: Throwable -> L233
    L209:
        r50 = r310;
        goto L232
    L206:
        r62 = r38[r317 + r48];     // Catch: Throwable -> L233
        goto L207
    L208:
        r51 = r314;
    L199:
        r56 = new StringBuilder("Series\n").append(r117).append("\n").append(r55);     // Catch: Throwable -> L233
        goto L200
    L195:
        r49 = r29;
        goto L196
    L189:
        r46 = r46;
        goto L191
    L179:
        if (r111.isEmpty() == false) goto L184;
        if (r53 < 0) goto L184;
        if (r54 < 0) goto L184;
        r46 = r46;
        r49 = r29;
        r55 = null;
        goto L196
    L174:
        r48 = r116;
        goto L176
    L167:
        if (r2.series.equals(r118) == true) goto L169;
    L163:
        r316 = r32;
        r317 = r42;
        r48 = r116;
        goto L151
    L161:
        if (r32.equals(r117) == true) goto L164;
        r315 = r31;
        goto L163
    L156:
        if (r2.provider.equals(r52) == true) goto L158;
    L147:
        r42 = r42 + 1024;     // Catch: Throwable -> L233
        r113 = r115;
        r40 = r216;
        r39 = r311;
    L233:
        th = move-exception;
        Throwable r63 = th;
        r41.close();     // Catch: Throwable -> L237
        throw r63;     // Catch: Throwable -> L237
    L237:
        th = th;
    L241:
        if (r63 == null) goto L245;
        if (r63 == th) goto L294;
        r63.addSuppressed(th);     // Catch: IOException -> L246
        throw r63;     // Catch: IOException -> L246
    L246:
        e = move-exception;
        throw new IllegalStateException(e);
    L294:
        throw r63;     // Catch: IOException -> L246
    L245:
        throw th;     // Catch: IOException -> L246
    L142:
        r41.close();     // Catch: Throwable -> L239
        r211 = r40;
        r0 = r0;
        r7 = r213;
        r212 = r214;
        r35 = r215;
        r33 = r310;
    L239:
        th = th;
        r63 = null;
        goto L241
    L138:
        r39 = r38.length;
        goto L139
    L132:
        r38 = r0.get(r37);
        goto L133
    L126:
        if (r15.equals(r1.kind) == true) goto L129;
    L128:
        r213 = r7;
        r36 = r212;
        goto L129
    L89:
        ArrayList r64 = new ArrayList();
        if (r114 == false) goto L96;
        SectionNavigation.sortSelected(r34);
        Iterator r65 = r34.iterator();
    L93:
        if (r65.hasNext() == false) goto L99;
        Entry r66 = (Entry) r65.next();
        r64.add(new MainActivity.Row(r66.title, r66.group, null, r66, 0));
    L99:
        MainActivity.CatalogPlan r67 = new MainActivity.CatalogPlan(r64, r30, r32);
        DemandMenus.save(r43, r2, r67);
        BlackBox.event("SERIES_VIEW_READY", "rows=" + r64.size() + " new_entries=" + (r43.created() - r7) + " season_menu=" + r113 + " episode_menu=" + r114 + " metadata_only=true duration_ms=" + ((System.nanoTime() - r210) / 1000000));
        return r67;
    L96:
        Iterator r217 = r33.entrySet().iterator();
    L98:
        if (r217.hasNext() == false) goto L99;
        Map.Entry r68 = (Map.Entry) r217.next();
        Group r69 = (Group) r68.getValue();
        Entry r319 = r69.first.entry();
        String r70 = (String) r68.getKey();
        if (r112 == false) goto L109;
        if (Providers.base(r70).equals(r70) == false) goto L106;
        r70 = String.valueOf(r70) + " · " + StreamFormat.label(r319);
    L106:
        StringBuilder r71 = new StringBuilder(String.valueOf(r69.episodes.size())).append(" series");
    L107:
        String r72 = r71.toString();
        String r218 = r70;
        String r320 = (String) r68.getKey();
        if (r112 == false) goto L117;
        int r73 = 3;
    L116:
        int r321 = r73;
    L120:
        r64.add(new MainActivity.Row(r218, r72, r320, r319, r321));
        goto L98
    L117:
        if (r113 == false) goto L119;
        r73 = 2;
        goto L116
    L119:
        r321 = 1;
        goto L120
    L109:
        if (r113 == false) goto L111;
        String r120 = "";
    L112:
        r71 = new StringBuilder(String.valueOf(r120)).append(r69.episodes.size()).append(" episodios");
        goto L107
    L111:
        r120 = String.valueOf(r69.seasons.size()) + " temporadas · ";
        goto L112
    L83:
        if (r2.season == null) goto L85;
        r114 = true;
        goto L86
    L77:
        if (r2.season != null) goto L79;
        r113 = true;
        goto L81
    L73:
        r112 = false;
        goto L75
    L69:
        r32 = (String) r30.get(0);
        goto L70
    L65:
        r29 = " ";
        r210 = r5;
        r211 = "r";
        r212 = "Mixta";
    L12:
        r10.file.delete();
        r9.clear();
        r0 = null;
        r10 = null;
    L13:
        r0 = null;
        goto L14
    }
}
