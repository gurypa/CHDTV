package androidx.arch.core.internal;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class FastSafeIterableMap<K, V> extends SafeIterableMap<K, V> {
    private HashMap<K, SafeIterableMap.Entry<K, V>> mHashMap;

    public FastSafeIterableMap() {
        this.mHashMap = new HashMap();
    }

    public Map.Entry<K, V> ceil(K r2) {
        if (contains(r2) == true) goto L5;
        return null;
    L5:
        return this.mHashMap.get(r2).mPrevious;
    }

    public boolean contains(K r2) {
        return this.mHashMap.containsKey(r2);
    }

    @Override // androidx.arch.core.internal.SafeIterableMap
    protected SafeIterableMap.Entry<K, V> get(K r2) {
        return this.mHashMap.get(r2);
    }

    @Override // androidx.arch.core.internal.SafeIterableMap
    public V putIfAbsent(K r4, V r5) {
        SafeIterableMap.Entry<K, V> r0 = get(r4);
        if (r0 != null) goto L5;
        this.mHashMap.put(r4, put(r4, r5));
        return null;
    L5:
        return r0.mValue;
    }

    @Override // androidx.arch.core.internal.SafeIterableMap
    public V remove(K r3) {
        V r0 = (V) super.remove(r3);
        this.mHashMap.remove(r3);
        return r0;
    }
}
