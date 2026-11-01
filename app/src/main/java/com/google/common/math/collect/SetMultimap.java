package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface SetMultimap<K, V> extends Multimap<K, V> {

    /* JADX INFO: renamed from: com.google.common.collect.SetMultimap$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ Collection $default$entries(SetMultimap r1) {
            return r1.entries();
        }

        public static /* bridge */ /* synthetic */ Collection $default$get(SetMultimap r0, @ParametricNullness Object r1) {
            return r0.get(r1);
        }

        public static /* bridge */ /* synthetic */ Collection $default$removeAll(SetMultimap r0, @CheckForNull Object r1) {
            return r0.removeAll(r1);
        }

        public static /* bridge */ /* synthetic */ Collection $default$replaceValues(SetMultimap r0, @ParametricNullness Object r1, Iterable r2) {
            return r0.replaceValues(r1, r2);
        }
    }

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    Map<K, Collection<V>> asMap();

    @Override // com.google.common.collect.Multimap
    Set<Map.Entry<K, V>> entries();

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    boolean equals(@CheckForNull Object r1);

    @Override // com.google.common.collect.Multimap
    Set<V> get(@ParametricNullness K r1);

    @Override // com.google.common.collect.Multimap
    Set<V> removeAll(@CheckForNull Object r1);

    @Override // com.google.common.collect.Multimap
    Set<V> replaceValues(@ParametricNullness K r1, Iterable<? extends V> r2);
}
