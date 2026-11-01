package es.antonio.m3u;

import android.content.Context;
import android.content.SharedPreferences;
import com.arthenica.ffmpegkit.MediaInformation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class WatchHistory {
    static final int MAX = 200;
    static final String PREFS = "watch_history";

    static final class Item {
        final long duration;
        final Entry entry;
        final long position;
        final long updated;

        Item(Entry r1, long r2, long r4, long r6) {
            this.entry = r1;
            this.position = r2;
            this.duration = r4;
            this.updated = r6;
        }

        String label() {
            StringBuilder r0 = new StringBuilder().append(WatchHistory.clock(this.position));
            if (this.duration <= 0) goto L5;
            String r1 = " / " + WatchHistory.clock(this.duration);
        L7:
            return r0.append(r1).toString();
        L5:
            r1 = "";
            goto L7
        }

        int percent() {
            if (this.duration > 0) goto L7;
            return 0;
        L7:
            return (int) Math.min(100.0d, (this.position * 100.0d) / this.duration);
        }
    }

    WatchHistory() {
    }

    static List<Item> all(Context r10) {
        ArrayList r0 = new ArrayList();
        Iterator<?> r1 = prefs(r10).getAll().values().iterator();
    L4:
        if (r1.hasNext() == false) goto L17;
        Object r2 = r1.next();
        if ((r2 instanceof String) == false) goto L4;
        Entry r4 = decode(new JSONObject((String) r2).getJSONObject("entry"));     // Catch: Exception -> L15
        Item r5 = get(r10, r4);     // Catch: Exception -> L15
        if (r5 == null) goto L4;
        if (vod(r4) == false) goto L4;
        if (r5.position <= 0) goto L4;
        r0.add(r5);     // Catch: Exception -> L15
        goto L4
    L17:
        Collections.sort(r0, new WatchHistory$$ExternalSyntheticLambda0());
        return r0;
    }

    static String clock(long r12) {
        long r0 = Math.max(0, r12 / 1000);
        Locale r2 = Locale.ROOT;
        if (r0 < 3600) goto L5;
        String r5 = "%d:%02d:%02d";
    L7:
        if (r0 < 3600) goto L9;
        Object[] r6 = {Long.valueOf(r0 / 3600), Long.valueOf((r0 / 60) % 60), Long.valueOf(r0 % 60)};
    L11:
        return String.format(r2, r5, r6);
    L9:
        r6 = new Object[]{Long.valueOf(r0 / 60), Long.valueOf(r0 % 60)};
        goto L11
    L5:
        r5 = "%d:%02d";
        goto L7
    }

    static Entry decode(JSONObject r6) throws JSONException {
        Entry r0 = new Entry();
        r0.title = r6.getString("title");
        r0.group = r6.getString("group");
        r0.logo = r6.optString("logo");
        Entry r1 = Entry.details(r0);
        r1.url = r6.getString("url");
        r0.kind = r6.getString("kind");
        r0.series = r6.optString("series");
        r0.season = r6.optInt("season", -1);
        r0.episode = r6.optInt("episode", -1);
        r0.manifest = r6.optString("manifest");
        r0.licenseType = r6.optString("licenseType");
        Entry r2 = Entry.details(r0);
        r2.license = r6.optString("license");
        r0.plexBase = r6.optString("plexBase");
        r0.plexKey = r6.optString("plexKey");
        JSONObject r3 = r6.optJSONObject("headers");
        if (r3 == null) goto L8;
        Iterator<String> r4 = r3.keys();
    L6:
        if (r4.hasNext() == false) goto L8;
        String r5 = r4.next();
        r0.headers.put(r5, r3.getString(r5));
    L8:
        return r0;
    }

    static JSONObject entry(Entry r4) throws JSONException {
        JSONObject r0 = new JSONObject().put("title", r4.title).put("group", r4.group).put("logo", r4.logo).put("url", Entry.details(r4).url).put("kind", r4.kind).put("series", r4.series).put("season", r4.season).put("episode", r4.episode).put("manifest", r4.manifest).put("licenseType", r4.licenseType).put("license", Entry.details(r4).license);
        String r2 = "";
        if (r4.plexBase != null) goto L5;
        String r1 = "";
    L6:
        JSONObject r3 = r0.put("plexBase", r1);
        if (r4.plexKey == null) goto L11;
        r2 = r4.plexKey;
    L11:
        return r3.put("plexKey", r2).put("headers", new JSONObject(r4.headers));
    L5:
        r1 = r4.plexBase;
        goto L6
    }

    static boolean finished(long r3, long r5) {
        if (r5 > 0) goto L5;
    L9:
        return false;
    L5:
        if (r3 <= 0) goto L9;
        if (r3 < (r5 - 1000)) goto L9;
        return true;
    }

    static Item get(Context r11, Entry r12) {
        String r0 = prefs(r11).getString(m418id(r12), null);     // Catch: Exception -> L23
        if (r0 == null) goto L26;
    L17:
        JSONObject r2 = new JSONObject(r0);     // Catch: Exception -> L23
        return new Item(r12, r2.getLong("position"), r2.optLong(MediaInformation.KEY_DURATION), r2.optLong("updated"));
    L26:
        r0 = prefs(r11).getString(oldId(r12), null);     // Catch: Exception -> L15
        if (r0 != null) goto L8;
        return null;
    L8:
        String r3 = new JSONObject(r0).getJSONObject("entry").optString("plexBase");     // Catch: Exception -> L15
        if (r12.plexBase != null) goto L11;
        String r4 = "";
    L12:
        if (Objects.equals(r3, r4) == true) goto L17;
        return null;
    L11:
        r4 = r12.plexBase;     // Catch: Exception -> L15
    L25:
        return null;
    }

    /* JADX INFO: renamed from: id */
    static String m418id(Entry r6) {
        StringBuilder r0 = new StringBuilder().append(oldId(r6));
        String r2 = "";
        if (r6.plexBase == null) goto L9;
        if (r6.plexBase.isEmpty() == true) goto L9;
        r2 = "\n" + r6.plexBase.replaceAll("/+$", "") + "\n" + r6.plexKey;
    L9:
        return r0.append(r2).toString();
    }

    static List<Item> inCatalog(Context r4, List<Entry> r5, String r6) {
        ArrayList r0 = new ArrayList();
        Iterator<Item> r1 = select(r4, r5, r6).iterator();
    L4:
        if (r1.hasNext() == false) goto L10;
        Item r2 = r1.next();
        if (r2.entry.plexBase == null) goto L9;
        if (r2.entry.plexBase.isEmpty() == false) goto L4;
    L9:
        r0.add(r2);
        goto L4
    L10:
        return r0;
    }

    static /* synthetic */ int lambda$all$0(Item r4, Item r5) {
        return Long.compare(r5.updated, r4.updated);
    }

    static /* synthetic */ int lambda$plex$1(Item r4, Item r5) {
        return Long.compare(r5.updated, r4.updated);
    }

    static SharedPreferences legacy(Context r2) {
        return r2.getSharedPreferences("PlayerActivity", 0);
    }

    static String oldId(Entry r3) {
        return r3.identity() + "\n" + r3.series + "\n" + r3.season + "\n" + r3.episode;
    }

    static List<Item> plex(Context r3) {
        ArrayList r0 = new ArrayList();
        r0.addAll(select(r3, Collections.emptyList(), "Películas"));
        r0.addAll(select(r3, Collections.emptyList(), "Series"));
        Collections.sort(r0, new WatchHistory$$ExternalSyntheticLambda1());
        return r0;
    }

    static List<Item> plex(Context r7, String r8) {
        ArrayList r0 = new ArrayList();
        if (r8 != null) goto L5;
    L15:
        return r0;
    L5:
        if (r8.isEmpty() == true) goto L15;
        String r3 = r8.replaceAll("/+$", "");
        Iterator<Item> r4 = plex(r7).iterator();
    L9:
        if (r4.hasNext() == false) goto L15;
        Item r5 = r4.next();
        if (r5.entry.plexBase == null) goto L9;
        if (r3.equals(r5.entry.plexBase.replaceAll("/+$", "")) == false) goto L9;
        r0.add(r5);
        goto L9
    }

    static SharedPreferences prefs(Context r2) {
        return r2.getSharedPreferences(PREFS, 0);
    }

    static void remove(Context r2, Entry r3) {
        prefs(r2).edit().remove(m418id(r3)).remove(oldId(r3)).apply();
        legacy(r2).edit().remove(r3.identity()).apply();
    }

    static long resume(Context r5, Entry r6) {
        if (vod(r6) == true) goto L5;
        return 0;
    L5:
        Item r0 = get(r5, r6);
        if (r0 != null) goto L9;
        return Math.max(0, legacy(r5).getLong(r6.identity(), 0));
    L9:
        return r0.position;
    }

    static void save(Context r6, Entry r7, long r8, long r10, boolean r12) {
        if (vod(r7) == true) goto L5;
        return;
    L5:
        if (r12 == false) goto L7;
    L27:
        remove(r6, r7);
        return;
    L7:
        if (finished(r8, r10) == true) goto L27;
        if (r8 >= 1000) goto L29;
        return;
    L29:
        SharedPreferences.Editor r0 = prefs(r6).edit();     // Catch: JSONException -> L25
        if (m418id(r7).equals(oldId(r7)) == true) goto L15;
        r0.remove(oldId(r7));     // Catch: JSONException -> L25
    L15:
        r0.putString(m418id(r7), new JSONObject().put("entry", entry(r7)).put("position", r8).put(MediaInformation.KEY_DURATION, Math.max(0, r10)).put("updated", System.currentTimeMillis()).toString());     // Catch: JSONException -> L25
        r0.apply();     // Catch: JSONException -> L25
        legacy(r6).edit().remove(r7.identity()).apply();     // Catch: JSONException -> L25
        if (prefs(r6).getAll().size() <= 200) goto L32;
        List<Item> r1 = all(r6);     // Catch: JSONException -> L25
        if (r1.size() <= 200) goto L33;
        SharedPreferences.Editor r2 = prefs(r6).edit();     // Catch: JSONException -> L25
        int r3 = 200;
    L21:
        if (r3 >= r1.size()) goto L23;
        r2.remove(m418id(r1.get(r3).entry));     // Catch: JSONException -> L25
        legacy(r6).edit().remove(r1.get(r3).entry.identity()).apply();     // Catch: JSONException -> L25
        r3 = r3 + 1;     // Catch: JSONException -> L25
        goto L21
    L23:
        r2.apply();     // Catch: JSONException -> L25
        return;
    L33:
        return;
    L32:
        return;
    }

    static List<Item> select(Context r30, List<Entry> r31, String r32) {
        String r0 = r32;
        List<Item> r1 = all(r30);
        Map<String, ?> r2 = legacy(r30).getAll();
        HashSet r3 = new HashSet();
        boolean r4 = false;
        Iterator<Map.Entry<String, ?>> r5 = r2.entrySet().iterator();
    L3:
        long r7 = 0;
        if (r5.hasNext() == false) goto L19;
        Map.Entry<String, ?> r6 = r5.next();
        if ((r6.getValue() instanceof Long) == false) goto L3;
        if (((Long) r6.getValue()).longValue() <= 0) goto L3;
        String r8 = r6.getKey();
        int r9 = r8.indexOf(10);
        if (r9 >= 0) goto L12;
        int r10 = -1;
    L13:
        if (r10 < 0) goto L3;
        if (r8.indexOf(10, r10 + 1) >= 0) goto L16;
        r3.add(r8.substring(r10 + 1));
        goto L3
    L16:
        r4 = true;
        goto L3
    L12:
        r10 = r8.indexOf(10, r9 + 1);
        goto L13
    L19:
        if (r1.isEmpty() == true) goto L21;
    L25:
        HashSet r11 = new HashSet();
        HashSet r12 = new HashSet();
        Iterator<Item> r13 = r1.iterator();
    L27:
        if (r13.hasNext() == false) goto L29;
        Item r14 = r13.next();
        r11.add(m418id(r14.entry));
        r12.add(r14.entry.title);
        goto L27
    L29:
        HashMap r15 = new HashMap();
        if (r12.isEmpty() == true) goto L39;
        Iterator<Entry> r16 = r31.iterator();
    L33:
        if (r16.hasNext() == false) goto L39;
        Entry r17 = r16.next();
        if (r12.contains(r17.title) == false) goto L33;
        String r18 = m418id(r17);
        if (r11.contains(r18) == false) goto L33;
        r15.put(r18, r17);
    L39:
        ArrayList r19 = new ArrayList();
        HashSet r110 = new HashSet();
        HashSet r111 = new HashSet();
        Iterator<Item> r112 = r1.iterator();
    L40:
        r7 = r7;
        if (r112.hasNext() == false) goto L61;
        Item r20 = r112.next();
        String r113 = m418id(r20.entry);
        if (r15.containsKey(r113) == false) goto L45;
        Entry r21 = (Entry) r15.get(r113);
    L46:
        r4 = r4;
        if (r21.kind.equals(r0) == false) goto L40;
        if (r15.containsKey(r113) == true) goto L53;
        HashSet r28 = r11;
        if ("Plex".equals(r21.group) == false) goto L52;
    L55:
        if (r0.equals("Series") == false) goto L59;
        if (r111.add(r21.group + "\n" + r21.plexBase + "\n" + r21.series) == false) goto L52;
    L59:
        r110.add(r113);
        r19.add(new Item(r21, r20.position, r20.duration, r20.updated));
        r11 = r28;
    L52:
        r11 = r28;
        goto L40
    L53:
        r28 = r11;
        goto L55
    L45:
        r21 = r20.entry;
        goto L46
    L61:
        boolean r27 = r4;
        if (r3.isEmpty() == false) goto L64;
        if (r27 == true) goto L64;
    L92:
        return r19;
    L64:
        Map<String, ?> r22 = prefs(r30).getAll();
        Iterator<Entry> r23 = r31.iterator();
    L66:
        if (r23.hasNext() == false) goto L92;
        Entry r24 = r23.next();
        if (r27 == true) goto L72;
        if (r3.contains(r24.title) == false) goto L66;
    L72:
        String r25 = m418id(r24);
        Object r114 = r2.get(r24.identity());
        if (r24.kind.equals(r0) == false) goto L91;
        if (r110.contains(r25) == true) goto L91;
        if (r22.containsKey(r25) == true) goto L91;
        if (r22.containsKey(oldId(r24)) == true) goto L91;
        if ((r114 instanceof Long) == false) goto L91;
        if (((Long) r114).longValue() <= r7) goto L66;
        if (r0.equals("Series") == false) goto L90;
        if (r111.add(r24.group + "\n" + r24.plexBase + "\n" + r24.series) == true) goto L90;
        r0 = r32;
    L90:
        r19.add(new Item(r24, ((Long) r114).longValue(), 0, 0));
        r110.add(r25);
        r0 = r32;
    L91:
        r0 = r32;
        goto L66
    L21:
        if (r3.isEmpty() == false) goto L25;
        if (r4 == true) goto L25;
        return new ArrayList();
    }

    static boolean vod(Entry r2) {
        if (r2 != null) goto L4;
    L8:
        return false;
    L4:
        if (r2.kind.equals("Películas") == false) goto L6;
    L7:
        return true;
    L6:
        if (r2.kind.equals("Series") == false) goto L8;
        goto L7
    }
}
