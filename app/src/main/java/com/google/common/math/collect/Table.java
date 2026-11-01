package com.google.common.collect;

import com.google.errorprone.annotations.DoNotMock;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use ImmutableTable, HashBasedTable, or another implementation")
@ElementTypesAreNonnullByDefault
public interface Table<R, C, V> {

    public interface Cell<R, C, V> {
        boolean equals(@CheckForNull Object r1);

        @ParametricNullness
        C getColumnKey();

        @ParametricNullness
        R getRowKey();

        @ParametricNullness
        V getValue();

        int hashCode();
    }

    Set<Cell<R, C, V>> cellSet();

    void clear();

    Map<R, V> column(@ParametricNullness C r1);

    Set<C> columnKeySet();

    Map<C, Map<R, V>> columnMap();

    boolean contains(@CheckForNull Object r1, @CheckForNull Object r2);

    boolean containsColumn(@CheckForNull Object r1);

    boolean containsRow(@CheckForNull Object r1);

    boolean containsValue(@CheckForNull Object r1);

    boolean equals(@CheckForNull Object r1);

    @CheckForNull
    V get(@CheckForNull Object r1, @CheckForNull Object r2);

    int hashCode();

    boolean isEmpty();

    @CheckForNull
    V put(@ParametricNullness R r1, @ParametricNullness C r2, @ParametricNullness V r3);

    void putAll(Table<? extends R, ? extends C, ? extends V> r1);

    @CheckForNull
    V remove(@CheckForNull Object r1, @CheckForNull Object r2);

    Map<C, V> row(@ParametricNullness R r1);

    Set<R> rowKeySet();

    Map<R, Map<C, V>> rowMap();

    int size();

    Collection<V> values();
}
