package com.google.common.collect;

import com.google.errorprone.annotations.DoNotMock;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use ImmutableMultimap, HashMultimap, or another implementation")
@ElementTypesAreNonnullByDefault
public interface Multimap<K, V> {
    Map<K, Collection<V>> asMap();

    void clear();

    boolean containsEntry(@CheckForNull Object r1, @CheckForNull Object r2);

    boolean containsKey(@CheckForNull Object r1);

    boolean containsValue(@CheckForNull Object r1);

    Collection<Map.Entry<K, V>> entries();

    boolean equals(@CheckForNull Object r1);

    Collection<V> get(@ParametricNullness K r1);

    int hashCode();

    boolean isEmpty();

    Set<K> keySet();

    Multiset<K> keys();

    boolean put(@ParametricNullness K r1, @ParametricNullness V r2);

    boolean putAll(Multimap<? extends K, ? extends V> r1);

    boolean putAll(@ParametricNullness K r1, Iterable<? extends V> r2);

    boolean remove(@CheckForNull Object r1, @CheckForNull Object r2);

    Collection<V> removeAll(@CheckForNull Object r1);

    Collection<V> replaceValues(@ParametricNullness K r1, Iterable<? extends V> r2);

    int size();

    Collection<V> values();
}
