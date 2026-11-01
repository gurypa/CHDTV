package com.google.common.collect;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingMap<K, V> extends ForwardingObject implements Map<K, V> {

    protected abstract class StandardEntrySet extends Maps.EntrySet<K, V> {
        final /* synthetic */ ForwardingMap this$0;

        protected StandardEntrySet(ForwardingMap r1) {
            this.this$0 = r1;
        }

        @Override // com.google.common.collect.Maps.EntrySet
        Map<K, V> map() {
            return this.this$0;
        }
    }

    protected class StandardKeySet extends Maps.KeySet<K, V> {
        final /* synthetic */ ForwardingMap this$0;

        public StandardKeySet(ForwardingMap r1) {
            this.this$0 = r1;
            super(r1);
        }
    }

    protected class StandardValues extends Maps.Values<K, V> {
        final /* synthetic */ ForwardingMap this$0;

        public StandardValues(ForwardingMap r1) {
            this.this$0 = r1;
            super(r1);
        }
    }

    protected ForwardingMap() {
    }

    public void clear() {
        delegate().clear();
    }

    public boolean containsKey(@CheckForNull Object r2) {
        return delegate().containsKey(r2);
    }

    public boolean containsValue(@CheckForNull Object r2) {
        return delegate().containsValue(r2);
    }

    @Override // com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingObject
    protected abstract Map<K, V> delegate();

    public Set<Map.Entry<K, V>> entrySet() {
        return delegate().entrySet();
    }

    public boolean equals(@CheckForNull Object r2) {
        if (r2 != this) goto L4;
    L7:
        return true;
    L4:
        if (delegate().equals(r2) == true) goto L7;
        return false;
    }

    @CheckForNull
    public V get(@CheckForNull Object r2) {
        return delegate().get(r2);
    }

    public int hashCode() {
        return delegate().hashCode();
    }

    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    public Set<K> keySet() {
        return delegate().keySet();
    }

    @CheckForNull
    public V put(@ParametricNullness K r2, @ParametricNullness V r3) {
        return delegate().put(r2, r3);
    }

    public void putAll(Map<? extends K, ? extends V> r2) {
        delegate().putAll(r2);
    }

    @CheckForNull
    public V remove(@CheckForNull Object r2) {
        return delegate().remove(r2);
    }

    public int size() {
        return delegate().size();
    }

    protected void standardClear() {
        Iterators.clear(entrySet().iterator());
    }

    protected boolean standardContainsKey(@CheckForNull Object r2) {
        return Maps.containsKeyImpl(this, r2);
    }

    protected boolean standardContainsValue(@CheckForNull Object r2) {
        return Maps.containsValueImpl(this, r2);
    }

    protected boolean standardEquals(@CheckForNull Object r2) {
        return Maps.equalsImpl(this, r2);
    }

    protected int standardHashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    protected boolean standardIsEmpty() {
        return !entrySet().iterator().hasNext();
    }

    protected void standardPutAll(Map<? extends K, ? extends V> r1) {
        Maps.putAllImpl(this, r1);
    }

    @CheckForNull
    protected V standardRemove(@CheckForNull Object r4) {
        Iterator<Map.Entry<K, V>> r0 = entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        Map.Entry<K, V> r1 = r0.next();
        if (Objects.equal(r1.getKey(), r4) == false) goto L4;
        V r2 = r1.getValue();
        r0.remove();
        return r2;
    L10:
        return null;
    }

    protected String standardToString() {
        return Maps.toStringImpl(this);
    }

    public Collection<V> values() {
        return delegate().values();
    }
}
