package com.google.common.collect;

import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
    static final RegularImmutableBiMap<Object, Object> EMPTY = null;
    final transient Object[] alternatingKeysAndValues;
    private final transient RegularImmutableBiMap<V, K> inverse;

    @CheckForNull
    private final transient Object keyHashTable;
    private final transient int keyOffset;
    private final transient int size;

    static {
        EMPTY = new RegularImmutableBiMap();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private RegularImmutableBiMap() {
        this.keyHashTable = null;
        this.alternatingKeysAndValues = new Object[0];
        this.keyOffset = 0;
        this.size = 0;
        this.inverse = this;
    }

    private RegularImmutableBiMap(@CheckForNull Object r2, Object[] r3, int r4, RegularImmutableBiMap<V, K> r5) {
        this.keyHashTable = r2;
        this.alternatingKeysAndValues = r3;
        this.keyOffset = 1;
        this.size = r4;
        this.inverse = r5;
    }

    RegularImmutableBiMap(Object[] r4, int r5) {
        this.alternatingKeysAndValues = r4;
        this.size = r5;
        this.keyOffset = 0;
        if (r5 < 2) goto L5;
        int r1 = ImmutableSet.chooseTableSize(r5);
    L6:
        this.keyHashTable = RegularImmutableMap.createHashTableOrThrow(r4, r5, r1, 0);
        this.inverse = new RegularImmutableBiMap(RegularImmutableMap.createHashTableOrThrow(r4, r5, r1, 1), r4, r5, this);
        return;
    L5:
        r1 = 0;
        goto L6
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new RegularImmutableMap.EntrySet(this, this.alternatingKeysAndValues, this.keyOffset, this.size);
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<K> createKeySet() {
        return new RegularImmutableMap.KeySet(this, new RegularImmutableMap.KeysOrValuesAsList(this.alternatingKeysAndValues, this.keyOffset, this.size));
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    @CheckForNull
    public V get(@CheckForNull Object r5) {
        V r0 = (V) RegularImmutableMap.get(this.keyHashTable, this.alternatingKeysAndValues, this.size, this.keyOffset, r5);
        if (r0 != null) goto L6;
        return null;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.ImmutableBiMap, com.google.common.collect.BiMap
    public /* bridge */ /* synthetic */ BiMap inverse() {
        return inverse();
    }

    @Override // com.google.common.collect.ImmutableBiMap, com.google.common.collect.BiMap
    public ImmutableBiMap<V, K> inverse() {
        return this.inverse;
    }

    @Override // com.google.common.collect.ImmutableMap
    boolean isPartialView() {
        return false;
    }

    @Override // java.util.Map
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.ImmutableBiMap, com.google.common.collect.ImmutableMap
    Object writeReplace() {
        return super.writeReplace();
    }
}
