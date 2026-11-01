package es.antonio.m3u;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import java.util.Locale;

/* JADX INFO: loaded from: classes7.dex */
final class PlaylistNames {
    private PlaylistNames() {
    }

    static String cached(Context r2, String r3) {
        if (r3 != null) goto L19;
        return "";
    L19:
        String r4 = Uri.parse(r3).getPath();     // Catch: Exception -> L17
        if (r4 == null) goto L18;
        String r5 = r4.substring(r4.lastIndexOf(47) + 1);     // Catch: Exception -> L17
        String r0 = r5.toLowerCase(Locale.ROOT);     // Catch: Exception -> L17
        if (r0.endsWith(".m3u") == false) goto L10;
    L15:
        return clean(r5);
    L10:
        if (r0.endsWith(".m3u8") == true) goto L15;
        if (r0.endsWith(".json") == true) goto L15;
        if (r0.endsWith(".txt") == true) goto L15;
    L18:
        return "";
    }

    private static String clean(String r2) {
        return r2.replace('\r', ' ').replace('\n', ' ').trim();
    }

    static String label(Catalog.Source r3, String r4) {
        String r0 = title(r3, r4);
        if (r4 != null) goto L5;
    L10:
        String r5 = "";
    L12:
        return String.valueOf(r0) + r5 + "\n" + r3.kind + " · " + r3.entries.size() + " entradas";
    L5:
        if (r4.isEmpty() == true) goto L10;
        if (r4.equals(r0) == true) goto L10;
        r5 = "\nArchivo: " + r4;
        goto L12
    }

    static String resolve(Context r9, String r10) {
        if (r10 != null) goto L5;
    L55:
        return "";
    L5:
        if (r10.trim().isEmpty() == true) goto L55;
        Uri r11 = Uri.parse(r10);     // Catch: Exception -> L53
        if ("content".equals(r11.getScheme()) == false) goto L40;
        Throwable r2 = null;
        Cursor r12 = r9.getContentResolver().query(r11, new String[]{"_display_name"}, null, null, null);     // Catch: Throwable -> L33
        if (r12 != null) goto L61;
    L30:
        if (r12 == null) goto L40;
        r12.close();     // Catch: Throwable -> L33
        goto L40
    L61:
    L25:
        th = move-exception;
        if (r12 == null) goto L63;
        r12.close();     // Catch: Throwable -> L33
        throw th;     // Catch: Throwable -> L33
    L63:
        throw th;     // Catch: Throwable -> L33
    L13:
        if (r12.moveToFirst() == false) goto L30;
        int r0 = r12.getColumnIndex("_display_name");     // Catch: Throwable -> L25
        if (r0 >= 0) goto L17;
        String r1 = null;
    L18:
        if (r1 == null) goto L30;
        if (r1.trim().isEmpty() == true) goto L30;
        String r3 = clean(r1);     // Catch: Throwable -> L25
        if (r12 == null) goto L24;
        r12.close();     // Catch: Throwable -> L33
    L24:
        return r3;
    L17:
        r1 = r12.getString(r0);     // Catch: Throwable -> L25
    L33:
        th = move-exception;
        if (0 == 0) goto L64;
        if (null == th) goto L38;
        r2.addSuppressed(th);     // Catch: Exception -> L39
    L38:
        throw null;     // Catch: Exception -> L39
        goto L40
    L64:
        throw th;     // Catch: Exception -> L39
    L40:
        String r13 = r11.getPath();     // Catch: Exception -> L53
        if (r13 != null) goto L43;
        return "";
    L43:
        String r14 = r13.substring(r13.lastIndexOf(47) + 1);     // Catch: Exception -> L53
        String r15 = r14.toLowerCase(Locale.ROOT);     // Catch: Exception -> L53
        if (r15.endsWith(".m3u") == false) goto L46;
    L51:
        return clean(r14);
    L46:
        if (r15.endsWith(".m3u8") == true) goto L51;
        if (r15.endsWith(".json") == true) goto L51;
        if (r15.endsWith(".txt") == true) goto L51;
    L54:
        return "";
    }

    static String title(Catalog.Source r4, String r5) {
        if (r4.name != null) goto L5;
        String r0 = "";
    L7:
        if (r0.isEmpty() == false) goto L9;
    L13:
        boolean r1 = true;
    L14:
        if (r1 == false) goto L20;
        if (r5 == null) goto L20;
        if (r5.isEmpty() == true) goto L20;
        return r5;
    L20:
        if (r0.isEmpty() == false) goto L23;
        return "Lista " + r4.kind;
    L23:
        return r0;
    L9:
        if (r0.equals("Lista importada") == true) goto L13;
        if (r0.equals("Lista " + r4.kind) == true) goto L13;
        r1 = false;
        goto L14
    L5:
        r0 = r4.name.trim();
        goto L7
    }
}
