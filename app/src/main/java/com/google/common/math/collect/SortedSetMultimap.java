package com.google.common.collect;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface SortedSetMultimap<K, V> extends SetMultimap<K, V> {

    /* JADX INFO: renamed from: com.google.common.collect.SortedSetMultimap$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ Collection $default$get(SortedSetMultimap r0, @ParametricNullness Object r1) {
            return r0.get(r1);
        }

        /* JADX INFO: renamed from: $default$get, reason: collision with other method in class */
        public static /* bridge */ /* synthetic */ Set m498$default$get(SortedSetMultimap r0, @ParametricNullness Object r1) {
            return r0.get(r1);
        }

        public static /* bridge */ /* synthetic */ Collection $default$removeAll(SortedSetMultimap r0, @CheckForNull Object r1) {
            return r0.removeAll(r1);
        }

        /* JADX INFO: renamed from: $default$removeAll, reason: collision with other method in class */
        public static /* bridge */ /* synthetic */ Set m499$default$removeAll(SortedSetMultimap r0, @CheckForNull Object r1) {
            return r0.removeAll(r1);
        }

        public static /* bridge */ /* synthetic */ Collection $default$replaceValues(SortedSetMultimap r0, @ParametricNullness Object r1, Iterable r2) {
            return r0.replaceValues(r1, r2);
        }

        /* JADX INFO: renamed from: $default$replaceValues, reason: collision with other method in class */
        public static /* bridge */ /* synthetic */ Set m500$default$replaceValues(SortedSetMultimap r0, @ParametricNullness Object r1, Iterable r2) {
            return r0.replaceValues(r1, r2);
        }
    }

    @Override // com.google.common.collect.SetMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    Map<K, Collection<V>> asMap();

    @Override // com.google.common.collect.SetMultimap, com.google.common.collect.Multimap
    SortedSet<V> get(@ParametricNullness K r1);

    @Override // com.google.common.collect.SetMultimap, com.google.common.collect.Multimap
    SortedSet<V> removeAll(@CheckForNull Object r1);

    @Override // com.google.common.collect.SetMultimap, com.google.common.collect.Multimap
    SortedSet<V> replaceValues(@ParametricNullness K r1, Iterable<? extends V> r2);

    @CheckForNull
    Comparator<? super V> valueComparator();
}
