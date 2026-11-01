package com.google.common.collect;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface ListMultimap<K, V> extends Multimap<K, V> {

    /* JADX INFO: renamed from: com.google.common.collect.ListMultimap$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ Collection $default$get(ListMultimap r0, @ParametricNullness Object r1) {
            return r0.get(r1);
        }

        public static /* bridge */ /* synthetic */ Collection $default$removeAll(ListMultimap r0, @CheckForNull Object r1) {
            return r0.removeAll(r1);
        }

        public static /* bridge */ /* synthetic */ Collection $default$replaceValues(ListMultimap r0, @ParametricNullness Object r1, Iterable r2) {
            return r0.replaceValues(r1, r2);
        }
    }

    Map<K, Collection<V>> asMap();

    boolean equals(@CheckForNull Object r1);

    @Override // com.google.common.collect.Multimap
    List<V> get(@ParametricNullness K r1);

    @Override // com.google.common.collect.Multimap
    List<V> removeAll(@CheckForNull Object r1);

    @Override // com.google.common.collect.Multimap
    List<V> replaceValues(@ParametricNullness K r1, Iterable<? extends V> r2);
}
