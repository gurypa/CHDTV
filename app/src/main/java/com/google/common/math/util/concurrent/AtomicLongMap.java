package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class AtomicLongMap<K> implements Serializable {

    @CheckForNull
    @LazyInit
    private transient Map<K, Long> asMap;
    private final ConcurrentHashMap<K, AtomicLong> map;

    private AtomicLongMap(ConcurrentHashMap<K, AtomicLong> r2) {
        this.map = (ConcurrentHashMap) Preconditions.checkNotNull(r2);
    }

    public static <K> AtomicLongMap<K> create() {
        return new AtomicLongMap(new ConcurrentHashMap());
    }

    public static <K> AtomicLongMap<K> create(Map<? extends K, ? extends Long> r1) {
        AtomicLongMap<K> r0 = create();
        r0.putAll(r1);
        return r0;
    }

    private Map<K, Long> createAsMap() {
        return Collections.unmodifiableMap(Maps.transformValues(this.map, new C09181(this)));
    }

    public long addAndGet(K r7, long r8) {
    L2:
        AtomicLong r0 = this.map.get(r7);
        if (r0 != null) goto L7;
        r0 = this.map.putIfAbsent(r7, new AtomicLong(r8));
        if (r0 != null) goto L7;
        return r8;
    L7:
        long r1 = r0.get();
        if (r1 == 0) goto L10;
        long r3 = r1 + r8;
        if (r0.compareAndSet(r1, r3) == false) goto L7;
        return r3;
    L10:
        if (this.map.replace(r7, r0, new AtomicLong(r8)) == false) goto L2;
        return r8;
    }

    public Map<K, Long> asMap() {
        Map<K, Long> r0 = this.asMap;
        if (r0 != null) goto L6;
        Map<K, Long> r1 = createAsMap();
        this.asMap = r1;
        return r1;
    L6:
        return r0;
    }

    public void clear() {
        this.map.clear();
    }

    public boolean containsKey(Object r2) {
        return this.map.containsKey(r2);
    }

    public long decrementAndGet(K r3) {
        return addAndGet(r3, -1);
    }

    public long get(K r4) {
        AtomicLong r0 = this.map.get(r4);
        if (r0 != null) goto L6;
        return 0;
    L6:
        return r0.get();
    }

    public long getAndAdd(K r9, long r10) {
    L2:
        AtomicLong r0 = this.map.get(r9);
        if (r0 != null) goto L7;
        r0 = this.map.putIfAbsent(r9, new AtomicLong(r10));
        if (r0 != null) goto L7;
        return 0;
    L7:
        long r3 = r0.get();
        if (r3 == 0) goto L10;
        if (r0.compareAndSet(r3, r3 + r10) == false) goto L7;
        return r3;
    L10:
        if (this.map.replace(r9, r0, new AtomicLong(r10)) == false) goto L2;
        return 0;
    }

    public long getAndDecrement(K r3) {
        return getAndAdd(r3, -1);
    }

    public long getAndIncrement(K r3) {
        return getAndAdd(r3, 1);
    }

    public long incrementAndGet(K r3) {
        return addAndGet(r3, 1);
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public long put(K r8, long r9) {
    L2:
        AtomicLong r0 = this.map.get(r8);
        if (r0 != null) goto L7;
        r0 = this.map.putIfAbsent(r8, new AtomicLong(r9));
        if (r0 != null) goto L7;
        return 0;
    L7:
        long r3 = r0.get();
        if (r3 == 0) goto L10;
        if (r0.compareAndSet(r3, r9) == false) goto L7;
        return r3;
    L10:
        if (this.map.replace(r8, r0, new AtomicLong(r9)) == false) goto L2;
        return 0;
    }

    public void putAll(Map<? extends K, ? extends Long> r6) {
        Iterator<Map.Entry<? extends K, ? extends Long>> r0 = r6.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Map.Entry<? extends K, ? extends Long> r1 = r0.next();
        put(r1.getKey(), r1.getValue().longValue());
        goto L4
    }

    long putIfAbsent(K r8, long r9) {
    L2:
        AtomicLong r0 = this.map.get(r8);
        if (r0 != null) goto L7;
        r0 = this.map.putIfAbsent(r8, new AtomicLong(r9));
        if (r0 != null) goto L7;
        return 0;
    L7:
        long r3 = r0.get();
        if (r3 != 0) goto L12;
        if (this.map.replace(r8, r0, new AtomicLong(r9)) == false) goto L2;
        return 0;
    L12:
        return r3;
    }

    public long remove(K r7) {
        AtomicLong r0 = this.map.get(r7);
        if (r0 != null) goto L5;
        return 0;
    L5:
        long r3 = r0.get();
        if (r3 == 0) goto L11;
        if (r0.compareAndSet(r3, 0) == false) goto L5;
    L11:
        this.map.remove(r7, r0);
        return r3;
    }

    boolean remove(K r8, long r9) {
        AtomicLong r0 = this.map.get(r8);
        if (r0 != null) goto L5;
        return false;
    L5:
        long r2 = r0.get();
        if (r2 == r9) goto L9;
        return false;
    L9:
        if (r2 != 0) goto L11;
    L14:
        this.map.remove(r8, r0);
        return true;
    L11:
        if (r0.compareAndSet(r2, 0) == true) goto L14;
        return false;
    }

    public void removeAllZeros() {
        Iterator<Map.Entry<K, AtomicLong>> r0 = this.map.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L11;
        AtomicLong r2 = r0.next().getValue();
        if (r2 == null) goto L4;
        if (r2.get() != 0) goto L4;
        r0.remove();
        goto L4
    }

    public boolean removeIfZero(K r3) {
        return remove(r3, 0);
    }

    boolean replace(K r7, long r8, long r10) {
        if (r8 == 0) goto L5;
        AtomicLong r0 = this.map.get(r7);
        if (r0 != null) goto L12;
        return false;
    L12:
        return r0.compareAndSet(r8, r10);
    L5:
        if (putIfAbsent(r7, r10) != 0) goto L13;
        return true;
    L13:
        return false;
    }

    public int size() {
        return this.map.size();
    }

    public long sum() {
        long r0 = 0;
        Iterator<AtomicLong> r2 = this.map.values().iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        r0 = r0 + r2.next().get();
        goto L4
    L6:
        return r0;
    }

    public String toString() {
        return this.map.toString();
    }
}
