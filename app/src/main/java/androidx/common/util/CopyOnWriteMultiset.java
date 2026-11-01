package androidx.media3.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class CopyOnWriteMultiset<E> implements Iterable<E> {
    private final Map<E, Integer> elementCounts;
    private Set<E> elementSet;
    private List<E> elements;
    private final Object lock;

    public CopyOnWriteMultiset() {
        this.lock = new Object();
        this.elementCounts = new HashMap();
        this.elementSet = Collections.emptySet();
        this.elements = Collections.emptyList();
    }

    public void add(E r7) {
        Object r0 = this.lock;
        monitor-enter(r0);
        ArrayList r1 = new ArrayList(this.elements);     // Catch: Throwable -> L13
        r1.add(r7);     // Catch: Throwable -> L13
        this.elements = Collections.unmodifiableList(r1);     // Catch: Throwable -> L13
        Integer r2 = this.elementCounts.get(r7);     // Catch: Throwable -> L13
        if (r2 != null) goto L7;
        HashSet r3 = new HashSet(this.elementSet);     // Catch: Throwable -> L13
        r3.add(r7);     // Catch: Throwable -> L13
        this.elementSet = Collections.unmodifiableSet(r3);     // Catch: Throwable -> L13
    L7:
        Map<E, Integer> r4 = this.elementCounts;     // Catch: Throwable -> L13
        int r5 = 1;
        if (r2 == null) goto L10;
        r5 = 1 + r2.intValue();     // Catch: Throwable -> L13
    L10:
        r4.put(r7, Integer.valueOf(r5));     // Catch: Throwable -> L13
        monitor-exit(r0);     // Catch: Throwable -> L13
        return;
    L13:
        th = move-exception;
        throw th;
    }

    public int count(E r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (this.elementCounts.containsKey(r3) == false) goto L7;
        int r1 = this.elementCounts.get(r3).intValue();     // Catch: Throwable -> L10
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L10
        return r1;
    L7:
        r1 = 0;
        goto L8
    }

    public Set<E> elementSet() {
        Object r0 = this.lock;
        monitor-enter(r0);
        Set<E> r1 = this.elementSet;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // java.lang.Iterable
    public Iterator<E> iterator() {
        Object r0 = this.lock;
        monitor-enter(r0);
        Iterator<E> r1 = this.elements.iterator();     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    public void remove(E r7) {
        Object r0 = this.lock;
        monitor-enter(r0);
        Integer r1 = this.elementCounts.get(r7);     // Catch: Throwable -> L14
        if (r1 != null) goto L8;
        monitor-exit(r0);     // Catch: Throwable -> L14
        return;
    L8:
        ArrayList r2 = new ArrayList(this.elements);     // Catch: Throwable -> L14
        r2.remove(r7);     // Catch: Throwable -> L14
        this.elements = Collections.unmodifiableList(r2);     // Catch: Throwable -> L14
        if (r1.intValue() != 1) goto L11;
        this.elementCounts.remove(r7);     // Catch: Throwable -> L14
        HashSet r3 = new HashSet(this.elementSet);     // Catch: Throwable -> L14
        r3.remove(r7);     // Catch: Throwable -> L14
        this.elementSet = Collections.unmodifiableSet(r3);     // Catch: Throwable -> L14
    L12:
        monitor-exit(r0);     // Catch: Throwable -> L14
        return;
    L11:
        this.elementCounts.put(r7, Integer.valueOf(r1.intValue() - 1));     // Catch: Throwable -> L14
    L14:
        th = move-exception;
        throw th;
    }
}
