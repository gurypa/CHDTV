package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingTable<R, C, V> extends ForwardingObject implements Table<R, C, V> {
    protected ForwardingTable() {
    }

    @Override // com.google.common.collect.Table
    public Set<Table.Cell<R, C, V>> cellSet() {
        return delegate().cellSet();
    }

    @Override // com.google.common.collect.Table
    public void clear() {
        delegate().clear();
    }

    @Override // com.google.common.collect.Table
    public Map<R, V> column(@ParametricNullness C r2) {
        return delegate().column(r2);
    }

    @Override // com.google.common.collect.Table
    public Set<C> columnKeySet() {
        return delegate().columnKeySet();
    }

    @Override // com.google.common.collect.Table
    public Map<C, Map<R, V>> columnMap() {
        return delegate().columnMap();
    }

    @Override // com.google.common.collect.Table
    public boolean contains(@CheckForNull Object r2, @CheckForNull Object r3) {
        return delegate().contains(r2, r3);
    }

    @Override // com.google.common.collect.Table
    public boolean containsColumn(@CheckForNull Object r2) {
        return delegate().containsColumn(r2);
    }

    @Override // com.google.common.collect.Table
    public boolean containsRow(@CheckForNull Object r2) {
        return delegate().containsRow(r2);
    }

    @Override // com.google.common.collect.Table
    public boolean containsValue(@CheckForNull Object r2) {
        return delegate().containsValue(r2);
    }

    @Override // com.google.common.collect.ForwardingObject
    protected abstract Table<R, C, V> delegate();

    @Override // com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.Table
    public boolean equals(@CheckForNull Object r2) {
        if (r2 != this) goto L4;
    L7:
        return true;
    L4:
        if (delegate().equals(r2) == true) goto L7;
        return false;
    }

    @Override // com.google.common.collect.Table
    @CheckForNull
    public V get(@CheckForNull Object r2, @CheckForNull Object r3) {
        return delegate().get(r2, r3);
    }

    @Override // com.google.common.collect.Table
    public int hashCode() {
        return delegate().hashCode();
    }

    @Override // com.google.common.collect.Table
    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    @Override // com.google.common.collect.Table
    @CheckForNull
    public V put(@ParametricNullness R r2, @ParametricNullness C r3, @ParametricNullness V r4) {
        return delegate().put(r2, r3, r4);
    }

    @Override // com.google.common.collect.Table
    public void putAll(Table<? extends R, ? extends C, ? extends V> r2) {
        delegate().putAll(r2);
    }

    @Override // com.google.common.collect.Table
    @CheckForNull
    public V remove(@CheckForNull Object r2, @CheckForNull Object r3) {
        return delegate().remove(r2, r3);
    }

    @Override // com.google.common.collect.Table
    public Map<C, V> row(@ParametricNullness R r2) {
        return delegate().row(r2);
    }

    @Override // com.google.common.collect.Table
    public Set<R> rowKeySet() {
        return delegate().rowKeySet();
    }

    @Override // com.google.common.collect.Table
    public Map<R, Map<C, V>> rowMap() {
        return delegate().rowMap();
    }

    @Override // com.google.common.collect.Table
    public int size() {
        return delegate().size();
    }

    @Override // com.google.common.collect.Table
    public Collection<V> values() {
        return delegate().values();
    }
}
