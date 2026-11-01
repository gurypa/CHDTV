package es.antonio.m3u;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes7.dex */
final class TvProviders {
    static final Map<DemandCatalog, AtomicBoolean> warming = null;

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
        long modified;
        List<Item> rows;
        String section;
        long size;

        Menu() {
            this.rows = new ArrayList();
        }
    }

    static final class Warm implements Runnable {
        final DemandCatalog engine;

        Warm(DemandCatalog r1) {
            this.engine = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.engine.resolve();     // Catch: RuntimeException -> L4
            return;
        L4:
            e = move-exception;
            BlackBox.event("TV_DIRECTORY_WARM_ERROR", e.getClass().getSimpleName());
        }
    }

    static {
        warming = Collections.synchronizedMap(new WeakHashMap());
    }

    private TvProviders() {
    }

    static boolean eligible(MainActivity.CatalogRequest r2) {
        if ("Canales".equals(r2.kind) == true) goto L5;
        return false;
    L5:
        if (r2.provider == null) goto L7;
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
        if ("Todas las secciones".equals(r2.section) == false) goto L25;
        return true;
    L25:
        return false;
    }

    static File file(DemandCatalog r2) {
        return new File(r2.files, "tv-providers-v220.bin");
    }

    static MainActivity.CatalogPlan load(DemandCatalog r19, MainActivity.CatalogRequest r20, boolean r21) {
        if (eligible(r20) == true) goto L5;
        return null;
    L5:
        long r4 = System.nanoTime();
        File r2 = file(r19);
        if (r2.length() <= PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE) goto L62;
        return null;
    L62:
        ObjectInputStream r6 = new ObjectInputStream(new BufferedInputStream(new FileInputStream(r2)));     // Catch: Throwable -> L45
        Menu r3 = (Menu) r6.readObject();     // Catch: Throwable -> L39
        if (r3.modified == SourceStore.stamp(r19, "Canales")) goto L12;
    L37:
        r6.close();     // Catch: Throwable -> L45
        return null;
    L12:
        if (r3.size != SourceStore.weight(r19, "Canales")) goto L37;
        if (r3.rows == null) goto L37;
        if (r3.rows.size() > 1000) goto L37;
        ArrayList r7 = new ArrayList();     // Catch: Throwable -> L39
        Iterator<Item> r8 = r3.rows.iterator();     // Catch: Throwable -> L39
    L20:
        if (r8.hasNext() == false) goto L21;
        Item r9 = r8.next();     // Catch: Throwable -> L39
        if (r9.entry == null) goto L35;
        if ("Canales".equals(r9.entry.kind) == false) goto L35;
        if (r9.level != 3) goto L35;
        r19.remembered.put(r9.entry, r9.target);     // Catch: Throwable -> L39
        r7.add(new MainActivity.Row(r9.title, r9.detail, r9.target, r9.entry, r9.level));     // Catch: Throwable -> L39
    L35:
        r6.close();     // Catch: Throwable -> L45
        return null;
    L21:
        MainActivity.CatalogPlan r1 = new MainActivity.CatalogPlan(r7, r3.choices, r3.section);     // Catch: Throwable -> L39
        BlackBox.event("TV_PROVIDERS_CACHE", "rows=" + r7.size() + " duration_ms=" + ((System.nanoTime() - r4) / 1000000) + " catalog_payload_opened=false");     // Catch: Throwable -> L39
        if (r21 == false) goto L24;
        warmDirectory(r19);     // Catch: Throwable -> L39
    L24:
        r6.close();     // Catch: Throwable -> L45
        return r1;
    L39:
        th = move-exception;
        Throwable r5 = th;
        r6.close();     // Catch: Throwable -> L43
        throw r5;     // Catch: Throwable -> L43
    L43:
        th = th;
    L47:
        if (r5 == null) goto L51;
        if (r5 == th) goto L69;
        r5.addSuppressed(th);     // Catch: ClassCastException -> L52 ClassNotFoundException -> L54 Throwable -> L56
        throw r5;     // Catch: ClassCastException -> L52 ClassNotFoundException -> L54 Throwable -> L56
    L57:
        return null;
    L69:
        throw r5;     // Catch: ClassCastException -> L52 ClassNotFoundException -> L54 Throwable -> L56
    L51:
        throw th;     // Catch: ClassCastException -> L52 ClassNotFoundException -> L54 Throwable -> L56
    L45:
        th = th;
        r5 = null;
        goto L47
    }

    static void save(DemandCatalog r4, MainActivity.CatalogRequest r5, MainActivity.CatalogPlan r6) {
        if (eligible(r5) == true) goto L5;
        return;
    L5:
        Menu r7 = new Menu();
        r7.modified = SourceStore.stamp(r4, "Canales");
        r7.size = SourceStore.weight(r4, "Canales");
        r7.choices = new ArrayList(r6.choices);
        r7.section = r6.section;
        Iterator<MainActivity.Row> r8 = r6.rows.iterator();
    L7:
        if (r8.hasNext() == false) goto L8;
        MainActivity.Row r0 = r8.next();
        Item r1 = new Item();
        r1.title = r0.title;
        r1.detail = r0.detail;
        r1.target = r0.target;
        r1.entry = r0.entry;
        r1.level = r0.level;
        r7.rows.add(r1);
        goto L7
    L8:
        File r9 = new File(r4.files, "tv-providers-v173.tmp");
        Throwable r2 = null;
        FileOutputStream r3 = new FileOutputStream(r9);     // Catch: Throwable -> L28
        ObjectOutputStream r10 = new ObjectOutputStream(new BufferedOutputStream(r3));     // Catch: Throwable -> L21
        r10.writeObject(r7);     // Catch: Throwable -> L17
        r10.flush();     // Catch: Throwable -> L17
        r3.getFD().sync();     // Catch: Throwable -> L17
        if (r9.renameTo(file(r4)) == true) goto L14;
        r9.delete();     // Catch: Throwable -> L17
    L14:
        r10.close();     // Catch: Throwable -> L21
        r3.close();     // Catch: Throwable -> L28
        return;
    L17:
        th = move-exception;
        r10.close();     // Catch: Throwable -> L21
        throw th;     // Catch: Throwable -> L21
    L21:
        th = move-exception;
        if (0 == 0) goto L25;
        if (null == th) goto L26;
        r2.addSuppressed(th);     // Catch: Throwable -> L28
    L26:
        r3.close();     // Catch: Throwable -> L28
        throw r2;     // Catch: Throwable -> L28
    L25:
        r2 = th;
    L28:
        th = move-exception;
        if (0 == 0) goto L45;
        if (null == th) goto L33;
        r2.addSuppressed(th);     // Catch: IOException -> L34
    L35:
        r9.delete();
        return;
    L33:
        throw null;     // Catch: IOException -> L34
        goto L35
    }

    static void warmDirectory(DemandCatalog r3) {
        if (r3.resolved == null) goto L5;
        return;
    L5:
        Map<DemandCatalog, AtomicBoolean> r0 = warming;
        monitor-enter(r0);
        AtomicBoolean r1 = warming.get(r3);     // Catch: Throwable -> L16
        if (r1 != null) goto L10;
        r1 = new AtomicBoolean();     // Catch: Throwable -> L16
        warming.put(r3, r1);     // Catch: Throwable -> L16
    L10:
        monitor-exit(r0);     // Catch: Throwable -> L16
        if (r1.compareAndSet(false, true) == true) goto L14;
        return;
    L14:
        Thread r2 = new Thread(new Warm(r3), "CHDTVTvDirectory");
        r2.setPriority(1);
        r2.start();
        return;
    L16:
        th = move-exception;
        throw th;
    }
}
