package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
    private static final long serialVersionUID = 0;
    private transient Map<K, V> delegate;

    @CheckForNull
    @LazyInit
    private transient Set<Map.Entry<K, V>> entrySet;
    transient AbstractBiMap<V, K> inverse;

    @CheckForNull
    @LazyInit
    private transient Set<K> keySet;

    @CheckForNull
    @LazyInit
    private transient Set<V> valueSet;

    class BiMapEntry extends ForwardingMapEntry<K, V> {
        private final Map.Entry<K, V> delegate;
        final /* synthetic */ AbstractBiMap this$0;

        BiMapEntry(AbstractBiMap r1, Map.Entry<K, V> r2) {
            this.this$0 = r1;
            this.delegate = r2;
        }

        @Override // com.google.common.collect.ForwardingMapEntry, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingMapEntry, com.google.common.collect.ForwardingObject
        protected Map.Entry<K, V> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingMapEntry, java.util.Map.Entry
        public V setValue(V r6) {
            this.this$0.checkValue(r6);
            Preconditions.checkState(this.this$0.entrySet().contains(this), "entry no longer in map");
            if (Objects.equal(r6, getValue()) == false) goto L5;
            return r6;
        L5:
            Preconditions.checkArgument(!this.this$0.containsValue(r6), "value already present: %s", r6);
            V r0 = this.delegate.setValue(r6);
            Preconditions.checkState(Objects.equal(r6, this.this$0.get(getKey())), "entry no longer in map");
            AbstractBiMap.access$500(this.this$0, getKey(), true, r0, r6);
            return r0;
        }
    }

    private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
        final Set<Map.Entry<K, V>> esDelegate;
        final /* synthetic */ AbstractBiMap this$0;

        private EntrySet(AbstractBiMap r1) {
            this.this$0 = r1;
            this.esDelegate = AbstractBiMap.access$100(this.this$0).entrySet();
        }

        /* synthetic */ EntrySet(AbstractBiMap r1, C05901 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.this$0.clear();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r2) {
            return Maps.containsEntryImpl(delegate(), r2);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> r2) {
            return standardContainsAll(r2);
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Collection delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected Set<Map.Entry<K, V>> delegate() {
            return this.esDelegate;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return this.this$0.entrySetIterator();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r4) {
            if (this.esDelegate.contains(r4) == true) goto L5;
            return false;
        L5:
            if ((r4 instanceof Map.Entry) == false) goto L11;
            Map.Entry r0 = (Map.Entry) r4;
            AbstractBiMap.access$100(this.this$0.inverse).remove(r0.getValue());
            this.esDelegate.remove(r0);
            return true;
        L11:
            return false;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> r2) {
            return standardRemoveAll(r2);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> r2) {
            return standardRetainAll(r2);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return standardToArray();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] r2) {
            return (T[]) standardToArray(r2);
        }
    }

    static class Inverse<K, V> extends AbstractBiMap<K, V> {
        private static final long serialVersionUID = 0;

        Inverse(Map<K, V> r2, AbstractBiMap<V, K> r3) {
            super(r2, r3, null);
        }

        private void readObject(ObjectInputStream r2) throws IOException, ClassNotFoundException {
            r2.defaultReadObject();
            setInverse((AbstractBiMap) java.util.Objects.requireNonNull(r2.readObject()));
        }

        private void writeObject(ObjectOutputStream r2) throws IOException {
            r2.defaultWriteObject();
            r2.writeObject(inverse());
        }

        @Override // com.google.common.collect.AbstractBiMap
        @ParametricNullness
        K checkKey(@ParametricNullness K r2) {
            return this.inverse.checkValue(r2);
        }

        @Override // com.google.common.collect.AbstractBiMap
        @ParametricNullness
        V checkValue(@ParametricNullness V r2) {
            return this.inverse.checkKey(r2);
        }

        @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return super.delegate();
        }

        Object readResolve() {
            return inverse().inverse();
        }

        @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, java.util.Map
        public /* bridge */ /* synthetic */ Collection values() {
            return super.values();
        }
    }

    private class KeySet extends ForwardingSet<K> {
        final /* synthetic */ AbstractBiMap this$0;

        private KeySet(AbstractBiMap r1) {
            this.this$0 = r1;
        }

        /* synthetic */ KeySet(AbstractBiMap r1, C05901 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.this$0.clear();
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Collection delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected Set<K> delegate() {
            return AbstractBiMap.access$100(this.this$0).keySet();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return Maps.keyIterator(this.this$0.entrySet().iterator());
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r2) {
            if (contains(r2) == true) goto L6;
            return false;
        L6:
            AbstractBiMap.access$200(this.this$0, r2);
            return true;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> r2) {
            return standardRemoveAll(r2);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> r2) {
            return standardRetainAll(r2);
        }
    }

    private class ValueSet extends ForwardingSet<V> {
        final /* synthetic */ AbstractBiMap this$0;
        final Set<V> valuesDelegate;

        private ValueSet(AbstractBiMap r1) {
            this.this$0 = r1;
            this.valuesDelegate = this.this$0.inverse.keySet();
        }

        /* synthetic */ ValueSet(AbstractBiMap r1, C05901 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Collection delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected Set<V> delegate() {
            return this.valuesDelegate;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<V> iterator() {
            return Maps.valueIterator(this.this$0.entrySet().iterator());
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return standardToArray();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] r2) {
            return (T[]) standardToArray(r2);
        }

        @Override // com.google.common.collect.ForwardingObject
        public String toString() {
            return standardToString();
        }
    }

    private AbstractBiMap(Map<K, V> r1, AbstractBiMap<V, K> r2) {
        this.delegate = r1;
        this.inverse = r2;
    }

    /* synthetic */ AbstractBiMap(Map r1, AbstractBiMap r2, C05901 r3) {
        this(r1, r2);
    }

    AbstractBiMap(Map<K, V> r1, Map<V, K> r2) {
        setDelegates(r1, r2);
    }

    static /* synthetic */ Map access$100(AbstractBiMap r1) {
        return r1.delegate;
    }

    static /* synthetic */ Object access$200(AbstractBiMap r1, Object r2) {
        return r1.removeFromBothMaps(r2);
    }

    static /* synthetic */ void access$500(AbstractBiMap r0, Object r1, boolean r2, Object r3, Object r4) {
        r0.updateInverseMap(r1, r2, r3, r4);
    }

    static /* synthetic */ void access$600(AbstractBiMap r0, Object r1) {
        r0.removeFromInverseMap(r1);
    }

    @CheckForNull
    private V putInBothMaps(@ParametricNullness K r4, @ParametricNullness V r5, boolean r6) {
        checkKey(r4);
        checkValue(r5);
        boolean r0 = containsKey(r4);
        if (r0 == true) goto L5;
    L7:
        if (r6 == false) goto L9;
        inverse().remove(r5);
    L10:
        V r1 = this.delegate.put(r4, r5);
        updateInverseMap(r4, r0, r1, r5);
        return r1;
    L9:
        Preconditions.checkArgument(!containsValue(r5), "value already present: %s", r5);
        goto L10
    L5:
        if (Objects.equal(r5, get(r4)) == false) goto L7;
        return r5;
    }

    @ParametricNullness
    private V removeFromBothMaps(@CheckForNull Object r2) {
        V r0 = (V) NullnessCasts.uncheckedCastNullableTToT(this.delegate.remove(r2));
        removeFromInverseMap(r0);
        return r0;
    }

    private void removeFromInverseMap(@ParametricNullness V r2) {
        this.inverse.delegate.remove(r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateInverseMap(@ParametricNullness K r2, boolean r3, @CheckForNull V r4, @ParametricNullness V r5) {
        if (r3 == false) goto L4;
        removeFromInverseMap(NullnessCasts.uncheckedCastNullableTToT(r4));
    L4:
        this.inverse.delegate.put(r5, r2);
    }

    @ParametricNullness
    K checkKey(@ParametricNullness K r1) {
        return r1;
    }

    @ParametricNullness
    V checkValue(@ParametricNullness V r1) {
        return r1;
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public void clear() {
        this.delegate.clear();
        this.inverse.delegate.clear();
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public boolean containsValue(@CheckForNull Object r2) {
        return this.inverse.containsKey(r2);
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected Map<K, V> delegate() {
        return this.delegate;
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> r0 = this.entrySet;
        if (r0 != null) goto L6;
        EntrySet r1 = new EntrySet(this, null);
        this.entrySet = r1;
        return r1;
    L6:
        return r0;
    }

    Iterator<Map.Entry<K, V>> entrySetIterator() {
        final Iterator<Map.Entry<K, V>> r0 = this.delegate.entrySet().iterator();
        return new C05901(this, r0);
    }

    @Override // com.google.common.collect.BiMap
    @CheckForNull
    public V forcePut(@ParametricNullness K r2, @ParametricNullness V r3) {
        return putInBothMaps(r2, r3, true);
    }

    @Override // com.google.common.collect.BiMap
    public BiMap<V, K> inverse() {
        return this.inverse;
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public Set<K> keySet() {
        Set<K> r0 = this.keySet;
        if (r0 != null) goto L6;
        KeySet r1 = new KeySet(this, null);
        this.keySet = r1;
        return r1;
    L6:
        return r0;
    }

    AbstractBiMap<V, K> makeInverse(Map<V, K> r2) {
        return new Inverse(r2, this);
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    @CheckForNull
    public V put(@ParametricNullness K r2, @ParametricNullness V r3) {
        return putInBothMaps(r2, r3, false);
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    public void putAll(Map<? extends K, ? extends V> r5) {
        Iterator<Map.Entry<? extends K, ? extends V>> r0 = r5.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Map.Entry<? extends K, ? extends V> r1 = r0.next();
        put(r1.getKey(), r1.getValue());
        goto L4
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    @CheckForNull
    public V remove(@CheckForNull Object r2) {
        if (containsKey(r2) == true) goto L7;
        return null;
    L7:
        return removeFromBothMaps(r2);
    }

    void setDelegates(Map<K, V> r4, Map<V, K> r5) {
        boolean r1 = true;
        if (this.delegate != null) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        if (this.inverse != null) goto L9;
        boolean r2 = true;
    L10:
        Preconditions.checkState(r2);
        Preconditions.checkArgument(r4.isEmpty());
        Preconditions.checkArgument(r5.isEmpty());
        if (r4 != r5) goto L14;
        r1 = false;
    L14:
        Preconditions.checkArgument(r1);
        this.delegate = r4;
        this.inverse = makeInverse(r5);
        return;
    L9:
        r2 = false;
        goto L10
    L5:
        r0 = false;
        goto L6
    }

    void setInverse(AbstractBiMap<V, K> r1) {
        this.inverse = r1;
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public /* bridge */ /* synthetic */ Collection values() {
        return values();
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public Set<V> values() {
        Set<V> r0 = this.valueSet;
        if (r0 != null) goto L6;
        ValueSet r1 = new ValueSet(this, null);
        this.valueSet = r1;
        return r1;
    L6:
        return r0;
    }
}
