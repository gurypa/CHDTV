package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface BiMap<K, V> extends Map<K, V> {

    /* JADX INFO: renamed from: com.google.common.collect.BiMap$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ Collection $default$values(BiMap r1) {
            return r1.values();
        }
    }

    @CheckForNull
    V forcePut(@ParametricNullness K r1, @ParametricNullness V r2);

    BiMap<V, K> inverse();

    @CheckForNull
    V put(@ParametricNullness K r1, @ParametricNullness V r2);

    void putAll(Map<? extends K, ? extends V> r1);

    @Override // java.util.Map
    Set<V> values();
}
