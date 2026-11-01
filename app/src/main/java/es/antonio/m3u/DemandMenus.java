package es.antonio.m3u;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: loaded from: classes7.dex */
final class DemandMenus {

    static final class Item implements Serializable {
        private static final long serialVersionUID = 1;
        String detail;
        Entry entry;
        int level;
        String target;
        String title;

        Item() {
        }
    }

    static final class Menu implements Serializable {
        private static final long serialVersionUID = 1;
        List<String> choices;
        String kind;
        long modified;
        String provider;
        List<Item> rows;
        String section;
        long size;

        Menu() {
            this.rows = new ArrayList();
        }
    }

    DemandMenus() {
    }

    static boolean eligible(MainActivity.CatalogRequest r2) {
        if ("Películas".equals(r2.kind) == true) goto L7;
        if ("Series".equals(r2.kind) == true) goto L7;
        return false;
    L7:
        if (r2.series == null) goto L9;
        return false;
    L9:
        if (r2.favorites == false) goto L11;
        return false;
    L11:
        if (r2.filter.isEmpty() == true) goto L13;
        return false;
    L13:
        if (r2.section != null) goto L15;
        return true;
    L15:
        if ("Todas las secciones".equals(r2.section) == false) goto L24;
        return true;
    L24:
        return false;
    }

    static File file(DemandCatalog r8, MainActivity.CatalogRequest r9) {
        MessageDigest r0 = MessageDigest.getInstance("SHA-256");     // Catch: Exception -> L12
        StringBuilder r1 = new StringBuilder(String.valueOf(r9.kind)).append("\n");     // Catch: Exception -> L12
        if (r9.provider != null) goto L5;
        String r10 = "";
    L6:
        byte[] r11 = r0.digest(r1.append(r10).toString().getBytes(StandardCharsets.UTF_8));     // Catch: Exception -> L12
        StringBuilder r2 = new StringBuilder();     // Catch: Exception -> L12
        int r3 = r11.length;     // Catch: Exception -> L12
        int r4 = 0;
    L7:
        if (r4 >= r3) goto L8;
        r2.append(String.format(Locale.ROOT, "%02x", new Object[]{Integer.valueOf(r11[r4] & 255)}));     // Catch: Exception -> L12
        r4 = r4 + 1;
        goto L7
    L8:
        return new File(r8.files, "vod-menu-v182-" + r2 + ".bin");
    L5:
        r10 = r9.provider;     // Catch: Exception -> L12
    L12:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    static MainActivity.CatalogPlan load(DemandCatalog r19, MainActivity.CatalogRequest r20) {
        if (eligible(r20) == true) goto L5;
        return null;
    L5:
        long r4 = System.nanoTime();
        File r2 = file(r19, r20);
        if (r2.length() <= 33554432) goto L72;
        return null;
    L72:
        ObjectInputStream r6 = new ObjectInputStream(new BufferedInputStream(new FileInputStream(r2)));     // Catch: Throwable -> L57
        Menu r3 = (Menu) r6.readObject();     // Catch: Throwable -> L51
        if (r3.modified == SourceStore.stamp(r19, r20.kind)) goto L12;
    L49:
        r6.close();     // Catch: Throwable -> L57
        return null;
    L12:
        if (r3.size != SourceStore.weight(r19, r20.kind)) goto L49;
        if (Objects.equals(r20.kind, r3.kind) == false) goto L49;
        if (Objects.equals(r20.provider, r3.provider) == false) goto L49;
        if (r3.rows == null) goto L49;
        if (r3.rows.size() > 25000) goto L49;
        ArrayList r7 = new ArrayList();     // Catch: Throwable -> L51
        Iterator<Item> r8 = r3.rows.iterator();     // Catch: Throwable -> L51
    L24:
        if (r8.hasNext() == false) goto L25;
        Item r9 = r8.next();     // Catch: Throwable -> L51
        if (r9.entry == null) goto L47;
        if (r20.kind.equals(r9.entry.kind) == false) goto L47;
        int r10 = r9.level;     // Catch: Throwable -> L51
        if (r20.provider != null) goto L36;
        int r11 = 3;
    L39:
        if (r10 != r11) goto L47;
        Map<Entry, String> r12 = r19.remembered;     // Catch: Throwable -> L51
        Entry r13 = r9.entry;     // Catch: Throwable -> L51
        if (r20.provider != null) goto L44;
        String r14 = r9.target;     // Catch: Throwable -> L51
    L45:
        r12.put(r13, r14);     // Catch: Throwable -> L51
        r7.add(new MainActivity.Row(r9.title, r9.detail, r9.target, r9.entry, r9.level));     // Catch: Throwable -> L51
        goto L24
    L44:
        r14 = r20.provider;     // Catch: Throwable -> L51
        goto L45
    L36:
        if ("Series".equals(r20.kind) == false) goto L38;
        r11 = 1;
        goto L39
    L38:
        r11 = 0;
    L47:
        r6.close();     // Catch: Throwable -> L57
        return null;
    L25:
        BlackBox.event("VOD_MENU_CACHE", "kind=" + r20.kind + " rows=" + r7.size() + " duration_ms=" + ((System.nanoTime() - r4) / 1000000) + " episode_payload_opened=false");     // Catch: Throwable -> L51
        MainActivity.CatalogPlan r0 = new MainActivity.CatalogPlan(r7, r3.choices, r3.section);     // Catch: Throwable -> L51
        r6.close();     // Catch: Throwable -> L57
        return r0;
    L51:
        th = move-exception;
        Throwable r1 = th;
        r6.close();     // Catch: Throwable -> L55
        throw r1;     // Catch: Throwable -> L55
    L55:
        th = th;
    L59:
        if (r1 == null) goto L63;
        if (r1 == th) goto L83;
        r1.addSuppressed(th);     // Catch: Throwable -> L64 ClassNotFoundException -> L66 IOException -> L68
        throw r1;     // Catch: Throwable -> L64 ClassNotFoundException -> L66 IOException -> L68
    L69:
        return null;
    L83:
        throw r1;     // Catch: Throwable -> L64 ClassNotFoundException -> L66 IOException -> L68
    L63:
        throw th;     // Catch: Throwable -> L64 ClassNotFoundException -> L66 IOException -> L68
    L57:
        th = th;
        r1 = null;
        goto L59
    }

    static void save(DemandCatalog r4, MainActivity.CatalogRequest r5, MainActivity.CatalogPlan r6) {
        if (eligible(r5) == true) goto L5;
        return;
    L5:
        if (r6.rows.size() > 25000) goto L50;
        Menu r0 = new Menu();
        r0.modified = SourceStore.stamp(r4, r5.kind);
        r0.size = SourceStore.weight(r4, r5.kind);
        r0.kind = r5.kind;
        r0.provider = r5.provider;
        r0.section = r6.section;
        r0.choices = new ArrayList(r6.choices);
        Iterator<MainActivity.Row> r7 = r6.rows.iterator();
    L9:
        if (r7.hasNext() == false) goto L10;
        MainActivity.Row r1 = r7.next();
        Item r2 = new Item();
        r2.title = r1.title;
        r2.detail = r1.detail;
        r2.target = r1.target;
        r2.entry = r1.entry;
        r2.level = r1.level;
        r0.rows.add(r2);
        goto L9
    L10:
        File r8 = file(r4, r5);
        File r9 = new File(String.valueOf(r8.getPath()) + ".tmp");
        Throwable r10 = null;
        FileOutputStream r3 = new FileOutputStream(r9);     // Catch: Throwable -> L30
        ObjectOutputStream r11 = new ObjectOutputStream(new BufferedOutputStream(r3));     // Catch: Throwable -> L23
        r11.writeObject(r0);     // Catch: Throwable -> L19
        r11.flush();     // Catch: Throwable -> L19
        r3.getFD().sync();     // Catch: Throwable -> L19
        if (r9.renameTo(r8) == true) goto L16;
        r9.delete();     // Catch: Throwable -> L19
    L16:
        r11.close();     // Catch: Throwable -> L23
        r3.close();     // Catch: Throwable -> L30
        return;
    L19:
        th = move-exception;
        r11.close();     // Catch: Throwable -> L23
        throw th;     // Catch: Throwable -> L23
    L23:
        th = move-exception;
        if (0 == 0) goto L27;
        if (null == th) goto L28;
        r10.addSuppressed(th);     // Catch: Throwable -> L30
    L28:
        r3.close();     // Catch: Throwable -> L30
        throw r10;     // Catch: Throwable -> L30
    L27:
        r10 = th;
    L30:
        th = move-exception;
        if (0 == 0) goto L48;
        if (null == th) goto L35;
        r10.addSuppressed(th);     // Catch: IOException -> L36
    L37:
        r9.delete();
        return;
    L35:
        throw null;     // Catch: IOException -> L36
        goto L37
    L48:
        throw th;     // Catch: IOException -> L36
    }
}
