package es.antonio.m3u;

import java.util.Iterator;
import java.util.LinkedHashMap;

/* JADX INFO: loaded from: classes7.dex */
final class CatalogPlanCache {
    private static final int MAX_PLANS = 8;
    private static final int MAX_ROWS = 30000;
    private final LinkedHashMap<Object, Item> items;
    private int rows;

    private static final class Item {
        final int rows;
        final Object value;

        Item(Object r1, int r2) {
            this.value = r1;
            this.rows = r2;
        }
    }

    CatalogPlanCache() {
        this.items = new LinkedHashMap(4, 0.75f, true);
    }

    synchronized Object get(Object r2) {
        monitor-enter(this);
        Item r3 = this.items.get(r2);     // Catch: Throwable -> L9
        if (r3 != null) goto L6;
        Object r4 = null;
    L7:
        monitor-exit(this);
        return r4;
    L6:
        r4 = r3.value;     // Catch: Throwable -> L9
    L9:
        th = move-exception;
        throw th;
    }

    synchronized void put(Object r4, Object r5, int r6) {
        monitor-enter(this);
        Item r0 = this.items.remove(r4);     // Catch: Throwable -> L21
        if (r0 == null) goto L6;
        this.rows -= r0.rows;     // Catch: Throwable -> L21
    L6:
        if (r6 >= 0) goto L8;
    L17:
        monitor-exit(this);
        return;
    L8:
        if (r6 > MAX_ROWS) goto L17;
        this.items.put(r4, new Item(r5, r6));     // Catch: Throwable -> L21
        this.rows += r6;     // Catch: Throwable -> L21
        Iterator<Item> r7 = this.items.values().iterator();     // Catch: Throwable -> L21
    L11:
        if (this.items.size() > 8) goto L16;
        if (this.rows <= MAX_ROWS) goto L17;
    L16:
        if (r7.hasNext() == false) goto L17;
        this.rows -= r7.next().rows;     // Catch: Throwable -> L21
        r7.remove();     // Catch: Throwable -> L21
    L21:
        th = move-exception;
        throw th;
    }
}
