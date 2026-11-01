package com.google.common.reflect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class MutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B> implements TypeToInstanceMap<B> {
    private final Map<TypeToken<? extends B>, B> backingMap;

    private static final class UnmodifiableEntry<K, V> extends ForwardingMapEntry<K, V> {
        private final Map.Entry<K, V> delegate;

        /* JADX INFO: renamed from: $r8$lambda$PXRRP_NYxQ7IKAyO13H8YX2p-q0, reason: not valid java name */
        public static /* synthetic */ UnmodifiableEntry m506$r8$lambda$PXRRP_NYxQ7IKAyO13H8YX2pq0(Map.Entry r1) {
            return new UnmodifiableEntry(r1);
        }

        private UnmodifiableEntry(Map.Entry<K, V> r2) {
            this.delegate = (Map.Entry) Preconditions.checkNotNull(r2);
        }

        static /* synthetic */ Iterator access$000(Iterator r1) {
            return transformEntries(r1);
        }

        private static <K, V> Iterator<Map.Entry<K, V>> transformEntries(Iterator<Map.Entry<K, V>> r1) {
            return Iterators.transform(r1, new C0874xa2538d6c());
        }

        static <K, V> Set<Map.Entry<K, V>> transformEntries(final Set<Map.Entry<K, V>> r1) {
            return new C08751(r1);
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
        @ParametricNullness
        public V setValue(@ParametricNullness V r2) {
            throw new UnsupportedOperationException();
        }
    }

    public MutableTypeToInstanceMap() {
        this.backingMap = Maps.newHashMap();
    }

    @CheckForNull
    private <T extends B> T trustedGet(TypeToken<T> r2) {
        return this.backingMap.get(r2);
    }

    @CheckForNull
    private <T extends B> T trustedPut(TypeToken<T> r2, @ParametricNullness T r3) {
        return this.backingMap.put(r2, r3);
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected Map<TypeToken<? extends B>, B> delegate() {
        return this.backingMap;
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public Set<Map.Entry<TypeToken<? extends B>, B>> entrySet() {
        return UnmodifiableEntry.transformEntries(super.entrySet());
    }

    @Override // com.google.common.reflect.TypeToInstanceMap
    @CheckForNull
    public <T extends B> T getInstance(TypeToken<T> r2) {
        return (T) trustedGet(r2.rejectTypeVariables());
    }

    @Override // com.google.common.reflect.TypeToInstanceMap
    @CheckForNull
    public <T extends B> T getInstance(Class<T> r2) {
        return (T) trustedGet(TypeToken.m375of(r2));
    }

    @CheckForNull
    @Deprecated
    public B put(TypeToken<? extends B> r3, @ParametricNullness B r4) {
        throw new UnsupportedOperationException("Please use putInstance() instead.");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    @CheckForNull
    @Deprecated
    public /* bridge */ /* synthetic */ Object put(Object r1, @ParametricNullness Object r2) {
        return put((TypeToken) r1, r2);
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    @Deprecated
    public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> r3) {
        throw new UnsupportedOperationException("Please use putInstance() instead.");
    }

    @Override // com.google.common.reflect.TypeToInstanceMap
    @CheckForNull
    public <T extends B> T putInstance(TypeToken<T> r2, @ParametricNullness T r3) {
        return (T) trustedPut(r2.rejectTypeVariables(), r3);
    }

    @Override // com.google.common.reflect.TypeToInstanceMap
    @CheckForNull
    public <T extends B> T putInstance(Class<T> r2, @ParametricNullness T r3) {
        return (T) trustedPut(TypeToken.m375of(r2), r3);
    }
}
