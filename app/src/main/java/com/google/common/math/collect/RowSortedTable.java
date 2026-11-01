package com.google.common.collect;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface RowSortedTable<R, C, V> extends Table<R, C, V> {

    /* JADX INFO: renamed from: com.google.common.collect.RowSortedTable$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ Set $default$rowKeySet(RowSortedTable r1) {
            return r1.rowKeySet();
        }

        public static /* bridge */ /* synthetic */ Map $default$rowMap(RowSortedTable r1) {
            return r1.rowMap();
        }
    }

    @Override // com.google.common.collect.Table
    SortedSet<R> rowKeySet();

    @Override // com.google.common.collect.Table
    SortedMap<R, Map<C, V>> rowMap();
}
