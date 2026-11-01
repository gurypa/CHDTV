package es.antonio.m3u;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes7.dex */
final class RelatedTitles {
    private final Map<Integer, List<Entry>> ids;
    private final Map<String, List<Entry>> titles;
    private final String type;
    private final Map<String, String> years;

    static final class Result {
        final List<Entry> entries;
        final JSONObject metadata;

        Result(JSONObject r1, List<Entry> r2) {
            this.metadata = r1;
            this.entries = r2;
        }
    }

    RelatedTitles(List<Entry> r9, Entry r10, TmdbService r11) {
        this.titles = new HashMap();
        this.ids = new HashMap();
        this.years = new HashMap();
        if (r10 != null) goto L5;
        String r0 = "movie";
    L6:
        this.type = r0;
        if (r10 != null) goto L9;
        String r1 = "";
    L10:
        HashMap r2 = new HashMap();
        HashMap r3 = new HashMap();
        Iterator<Entry> r12 = SectionStore.related(r9, r10).iterator();
    L12:
        if (r12.hasNext() == false) goto L13;
        Entry r13 = r12.next();
        if ("Canales".equals(r13.kind) == true) goto L12;
        if (this.type.equals(TmdbTitles.type(r13)) == false) goto L12;
        if ("Series".equals(r13.kind) == false) goto L22;
        String r4 = CatalogCounts.series(r13);
    L23:
        String r5 = (String) r2.get(r4);
        if (r5 != null) goto L27;
        r5 = TmdbTitles.key(r13);
        r2.put(r4, r5);
    L27:
        if (r5.equals(r1) == true) goto L12;
        int r6 = -1;
        String r7 = r5.split("\\|", -1)[1];
        this.titles.computeIfAbsent(r7, new RelatedTitles$$ExternalSyntheticLambda0()).add(r13);
        this.years.put(r13.identity(), TmdbTitles.year(r13));
        Integer r8 = (Integer) r3.get(r5);
        if (r8 != null) goto L39;
        if (r11 != null) goto L33;
        JSONObject r14 = null;
    L34:
        if (r14 == null) goto L37;
        r6 = r14.optInt("id", -1);
    L37:
        r8 = Integer.valueOf(r6);
        r3.put(r5, r8);
        goto L39
    L33:
        r14 = r11.cached(r13);
    L39:
        if (r8.intValue() <= 0) goto L12;
        this.ids.computeIfAbsent(r8, new RelatedTitles$$ExternalSyntheticLambda1()).add(r13);
        goto L12
    L22:
        r4 = r13.title;
        goto L23
    L13:
        return;
    L9:
        r1 = TmdbTitles.key(r10);
        goto L10
    L5:
        r0 = TmdbTitles.type(r10);
        goto L6
    }

    static /* synthetic */ List lambda$0(String r0) {
        return new ArrayList();
    }

    static /* synthetic */ List lambda$1(Integer r0) {
        return new ArrayList();
    }

    List<Entry> match(JSONObject r10) {
        LinkedHashMap r0 = new LinkedHashMap();
        List<Entry> r1 = this.ids.get(Integer.valueOf(r10.optInt("id", -1)));
        if (r1 == null) goto L10;
        Iterator<Entry> r2 = r1.iterator();
    L6:
        if (r2.hasNext() == false) goto L10;
        Entry r3 = r2.next();
        r0.put(r3.identity(), r3);
    L10:
        if (this.type.equals("tv") == false) goto L12;
        String r4 = "first_air_date";
    L13:
        String r5 = r10.optString(r4);
        int r6 = 0;
        if (r5.length() < 4) goto L16;
        String r7 = r5.substring(0, 4);
    L17:
        String[] r8 = {"title", "name", "original_title", "original_name"};
    L18:
        if (r6 >= 4) goto L20;
        String r9 = TmdbTitles.normalize(r10.optString(r8[r6]));
        List<Entry> r11 = this.titles.get(r9);
        if (r11 == null) goto L36;
        Iterator<Entry> r12 = r11.iterator();
    L25:
        if (r12.hasNext() == false) goto L36;
        Entry r13 = r12.next();
        String r14 = this.years.get(r13.identity());
        if (r14 == null) goto L35;
        if (r14.isEmpty() == true) goto L35;
        if (r7.isEmpty() == true) goto L35;
        if (r7.equals(r14) == false) goto L25;
    L35:
        r0.put(r13.identity(), r13);
    L36:
        r6 = r6 + 1;
        goto L18
    L20:
        return new ArrayList(r0.values());
    L16:
        r7 = "";
        goto L17
    L12:
        r4 = "release_date";
        goto L13
    }

    List<Result> resolve(JSONArray r8) {
        ArrayList r0 = new ArrayList();
        HashSet r1 = new HashSet();
        if (r8 == null) goto L19;
        int r3 = 0;
    L6:
        if (r3 >= Math.min(40, r8.length())) goto L19;
        if (r0.size() >= 12) goto L19;
        JSONObject r4 = r8.optJSONObject(r3);
        if (r4 == null) goto L18;
        List<Entry> r5 = match(r4);
        if (r5.isEmpty() == true) goto L18;
        if (r1.add(TmdbTitles.key(r5.get(0))) == false) goto L18;
        r0.add(new Result(r4, r5));
    L18:
        r3 = r3 + 1;
    L19:
        return r0;
    }
}
