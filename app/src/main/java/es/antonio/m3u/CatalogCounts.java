package es.antonio.m3u;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public final class CatalogCounts {
    private CatalogCounts() {
    }

    public static String detail(List<Entry> r4, boolean r5) {
        HashSet r0 = new HashSet();
        Iterator<Entry> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0.add(Integer.valueOf(r1.next().season));
        goto L4
    L6:
        StringBuilder r2 = new StringBuilder();
        if (r5 == false) goto L9;
        String r3 = r0.size() + " temporadas · ";
    L11:
        return r2.append(r3).append(episodes(r4)).append(" episodios").toString();
    L9:
        r3 = "";
        goto L11
    }

    public static String episodeKey(Entry r3) {
        if (r3.season < 0) goto L8;
        if (r3.episode < 0) goto L8;
        return series(r3) + "\n" + r3.season + "\n" + r3.episode;
    L8:
        return r3.identity();
    }

    public static int episodes(List<Entry> r4) {
        HashSet r0 = new HashSet();
        Iterator<Entry> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L7;
        r0.add(episodeKey(r1.next()));
        goto L4
    L7:
        return r0.size();
    }

    public static String provider(List<Entry> r4, String r5) {
        HashSet r0 = new HashSet();
        Iterator<Entry> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L10;
        Entry r2 = r1.next();
        if ("Series".equals(r5) == false) goto L8;
        String r3 = series(r2);
    L9:
        r0.add(r3);
        goto L4
    L8:
        r3 = r2.identity();
        goto L9
    L10:
        StringBuilder r6 = new StringBuilder().append(r0.size());
        if ("Series".equals(r5) == false) goto L14;
        String r7 = " series";
    L18:
        return r6.append(r7).toString();
    L14:
        if ("Canales".equals(r5) == false) goto L16;
        r7 = " canales";
        goto L18
    L16:
        r7 = " películas";
        goto L18
    }

    public static String series(Entry r1) {
        if (r1.series == null) goto L9;
        if (r1.series.isEmpty() == true) goto L9;
        return r1.series;
    L9:
        return r1.group;
    }
}
