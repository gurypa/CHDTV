package com.google.common.collect;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V> {

    protected class StandardDescendingMap extends Maps.DescendingMap<K, V> {
        final /* synthetic */ ForwardingNavigableMap this$0;

        public StandardDescendingMap(ForwardingNavigableMap r1) {
            this.this$0 = r1;
        }

        @Override // com.google.common.collect.Maps.DescendingMap
        protected Iterator<Map.Entry<K, V>> entryIterator() {
            return new C06251(this);
        }

        @Override // com.google.common.collect.Maps.DescendingMap
        NavigableMap<K, V> forward() {
            return this.this$0;
        }
    }

    protected class StandardNavigableKeySet extends Maps.NavigableKeySet<K, V> {
        final /* synthetic */ ForwardingNavigableMap this$0;

        public StandardNavigableKeySet(ForwardingNavigableMap r1) {
            this.this$0 = r1;
            super(r1);
        }
    }

    protected ForwardingNavigableMap() {
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> ceilingEntry(@ParametricNullness K r2) {
        return delegate().ceilingEntry(r2);
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public K ceilingKey(@ParametricNullness K r2) {
        return delegate().ceilingKey(r2);
    }

    @Override // com.google.common.collect.ForwardingSortedMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingSortedMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Map delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingSortedMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected abstract NavigableMap<K, V> delegate();

    @Override // com.google.common.collect.ForwardingSortedMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ SortedMap delegate() {
        return delegate();
    }

    @Override // java.util.NavigableMap
    public NavigableSet<K> descendingKeySet() {
        return delegate().descendingKeySet();
    }

    @Override // java.util.NavigableMap
    public NavigableMap<K, V> descendingMap() {
        return delegate().descendingMap();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> firstEntry() {
        return delegate().firstEntry();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> floorEntry(@ParametricNullness K r2) {
        return delegate().floorEntry(r2);
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public K floorKey(@ParametricNullness K r2) {
        return delegate().floorKey(r2);
    }

    @Override // java.util.NavigableMap
    public NavigableMap<K, V> headMap(@ParametricNullness K r2, boolean r3) {
        return delegate().headMap(r2, r3);
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> higherEntry(@ParametricNullness K r2) {
        return delegate().higherEntry(r2);
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public K higherKey(@ParametricNullness K r2) {
        return delegate().higherKey(r2);
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> lastEntry() {
        return delegate().lastEntry();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> lowerEntry(@ParametricNullness K r2) {
        return delegate().lowerEntry(r2);
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public K lowerKey(@ParametricNullness K r2) {
        return delegate().lowerKey(r2);
    }

    @Override // java.util.NavigableMap
    public NavigableSet<K> navigableKeySet() {
        return delegate().navigableKeySet();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> pollFirstEntry() {
        return delegate().pollFirstEntry();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> pollLastEntry() {
        return delegate().pollLastEntry();
    }

    @CheckForNull
    protected Map.Entry<K, V> standardCeilingEntry(@ParametricNullness K r2) {
        return tailMap(r2, true).firstEntry();
    }

    @CheckForNull
    protected K standardCeilingKey(@ParametricNullness K r2) {
        return (K) Maps.keyOrNull(ceilingEntry(r2));
    }

    protected NavigableSet<K> standardDescendingKeySet() {
        return descendingMap().navigableKeySet();
    }

    @CheckForNull
    protected Map.Entry<K, V> standardFirstEntry() {
        return (Map.Entry) Iterables.getFirst(entrySet(), null);
    }

    protected K standardFirstKey() {
        Map.Entry<K, V> r0 = firstEntry();
        if (r0 == null) goto L7;
        return r0.getKey();
    L7:
        throw new NoSuchElementException();
    }

    @CheckForNull
    protected Map.Entry<K, V> standardFloorEntry(@ParametricNullness K r2) {
        return headMap(r2, true).lastEntry();
    }

    @CheckForNull
    protected K standardFloorKey(@ParametricNullness K r2) {
        return (K) Maps.keyOrNull(floorEntry(r2));
    }

    protected SortedMap<K, V> standardHeadMap(@ParametricNullness K r2) {
        return headMap(r2, false);
    }

    @CheckForNull
    protected Map.Entry<K, V> standardHigherEntry(@ParametricNullness K r2) {
        return tailMap(r2, false).firstEntry();
    }

    @CheckForNull
    protected K standardHigherKey(@ParametricNullness K r2) {
        return (K) Maps.keyOrNull(higherEntry(r2));
    }

    @CheckForNull
    protected Map.Entry<K, V> standardLastEntry() {
        return (Map.Entry) Iterables.getFirst(descendingMap().entrySet(), null);
    }

    protected K standardLastKey() {
        Map.Entry<K, V> r0 = lastEntry();
        if (r0 == null) goto L7;
        return r0.getKey();
    L7:
        throw new NoSuchElementException();
    }

    @CheckForNull
    protected Map.Entry<K, V> standardLowerEntry(@ParametricNullness K r2) {
        return headMap(r2, false).lastEntry();
    }

    @CheckForNull
    protected K standardLowerKey(@ParametricNullness K r2) {
        return (K) Maps.keyOrNull(lowerEntry(r2));
    }

    @CheckForNull
    protected Map.Entry<K, V> standardPollFirstEntry() {
        return (Map.Entry) Iterators.pollNext(entrySet().iterator());
    }

    @CheckForNull
    protected Map.Entry<K, V> standardPollLastEntry() {
        return (Map.Entry) Iterators.pollNext(descendingMap().entrySet().iterator());
    }

    @Override // com.google.common.collect.ForwardingSortedMap
    protected SortedMap<K, V> standardSubMap(@ParametricNullness K r3, @ParametricNullness K r4) {
        return subMap(r3, true, r4, false);
    }

    protected SortedMap<K, V> standardTailMap(@ParametricNullness K r2) {
        return tailMap(r2, true);
    }

    @Override // java.util.NavigableMap
    public NavigableMap<K, V> subMap(@ParametricNullness K r2, boolean r3, @ParametricNullness K r4, boolean r5) {
        return delegate().subMap(r2, r3, r4, r5);
    }

    @Override // java.util.NavigableMap
    public NavigableMap<K, V> tailMap(@ParametricNullness K r2, boolean r3) {
        return delegate().tailMap(r2, r3);
    }
}
