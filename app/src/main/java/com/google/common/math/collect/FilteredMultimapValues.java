package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class FilteredMultimapValues<K, V> extends AbstractCollection<V> {
    private final FilteredMultimap<K, V> multimap;

    FilteredMultimapValues(FilteredMultimap<K, V> r2) {
        this.multimap = (FilteredMultimap) Preconditions.checkNotNull(r2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public void clear() {
        this.multimap.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean contains(@CheckForNull Object r2) {
        return this.multimap.containsValue(r2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<V> iterator() {
        return Maps.valueIterator(this.multimap.entries().iterator());
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean remove(@CheckForNull Object r5) {
        Predicate<? super Map.Entry<K, V>> r0 = this.multimap.entryPredicate();
        Iterator<Map.Entry<K, V>> r1 = this.multimap.unfiltered().entries().iterator();
    L4:
        if (r1.hasNext() == false) goto L12;
        Map.Entry<K, V> r2 = r1.next();
        if (r0.apply(r2) == false) goto L4;
        if (Objects.equal(r2.getValue(), r5) == false) goto L4;
        r1.remove();
        return true;
    L12:
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> r4) {
        return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.m186in(r4))));
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection<?> r4) {
        return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.not(Predicates.m186in(r4)))));
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return this.multimap.size();
    }
}
