package es.antonio.m3u;

import java.util.Objects;
import java.util.SortedSet;

/* JADX INFO: loaded from: classes7.dex */
final class GuideSelection {
    GuideSelection() {
    }

    static String active(String r0, String r1, String r2) {
        if (Objects.equals(r1, r2) == true) goto L7;
        return "";
    L7:
        return r0;
    }

    static String firstProvider(SortedSet<String> r1, String r2) {
        if (r1.contains(r2) == false) goto L6;
        return r2;
    L6:
        if (r1.isEmpty() == false) goto L9;
        return "Todos los proveedores";
    L9:
        return r1.first();
    }
}
