package es.antonio.m3u;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes3.dex */
final class TmdbTitles {
    private TmdbTitles() {
    }

    static String key(Entry r3) {
        return type(r3) + "|" + normalize(query(r3)) + "|" + year(r3);
    }

    static String normalize(String r3) {
        return Normalizer.normalize(r3, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT).replaceAll("[^\\p{L}\\p{N}]", "");
    }

    static String query(Entry r4) {
        if ("Series".equals(r4.kind) == false) goto L5;
        String r0 = CatalogCounts.series(r4);
    L7:
        if (r0 != null) goto L9;
        String r2 = "";
    L11:
        return r2.replaceAll("(?i)\\s*[·|\\-]?\\s*S\\d+\\s*E\\d+.*$", "").replaceAll("(?i)\\s*[\\[(]?(?:4k|uhd|fhd|hd|sd|1080p|720p)[\\])]?$", "").trim().replaceAll("\\s*\\((?:19|20)\\d{2}\\)\\s*$", "").trim();
    L9:
        r2 = r0;
        goto L11
    L5:
        r0 = r4.title;
        goto L7
    }

    static String type(Entry r2) {
        if ("Series".equals(r2.kind) == false) goto L5;
        return "tv";
    L5:
        return "movie";
    }

    static String year(Entry r4) {
        Pattern r0 = Pattern.compile("\\((?:19|20)\\d{2}\\)");
        if (r4.title != null) goto L5;
        String r1 = "";
    L6:
        Matcher r2 = r0.matcher(r1);
        if (r2.find() == true) goto L9;
        return "";
    L9:
        return r2.group().substring(1, 5);
    L5:
        r1 = r4.title;
        goto L6
    }
}
