package com.google.common.reflect;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class ImmutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B> implements TypeToInstanceMap<B> {
    private final ImmutableMap<TypeToken<? extends B>, B> delegate;

    /* JADX INFO: renamed from: com.google.common.reflect.ImmutableTypeToInstanceMap$1 */
    static /* synthetic */ class C08731 {
    }

    public static final class Builder<B> {
        private final ImmutableMap.Builder<TypeToken<? extends B>, B> mapBuilder;

        private Builder() {
            this.mapBuilder = ImmutableMap.builder();
        }

        /* synthetic */ Builder(C08731 r1) {
            this();
        }

        public ImmutableTypeToInstanceMap<B> build() {
            return new ImmutableTypeToInstanceMap(this.mapBuilder.buildOrThrow(), null);
        }

        public <T extends B> Builder<B> put(TypeToken<T> r3, T r4) {
            this.mapBuilder.put(r3.rejectTypeVariables(), r4);
            return this;
        }

        public <T extends B> Builder<B> put(Class<T> r3, T r4) {
            this.mapBuilder.put(TypeToken.m375of(r3), r4);
            return this;
        }
    }

    private ImmutableTypeToInstanceMap(ImmutableMap<TypeToken<? extends B>, B> r1) {
        this.delegate = r1;
    }

    /* synthetic */ ImmutableTypeToInstanceMap(ImmutableMap r1, C08731 r2) {
        this(r1);
    }

    public static <B> Builder<B> builder() {
        return new Builder(null);
    }

    /* JADX INFO: renamed from: of */
    public static <B> ImmutableTypeToInstanceMap<B> m373of() {
        return new ImmutableTypeToInstanceMap(ImmutableMap.m231of());
    }

    @CheckForNull
    private <T extends B> T trustedGet(TypeToken<T> r2) {
        return this.delegate.get(r2);
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected Map<TypeToken<? extends B>, B> delegate() {
        return this.delegate;
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
    public B put(TypeToken<? extends B> r2, B r3) {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    @CheckForNull
    @Deprecated
    public /* bridge */ /* synthetic */ Object put(Object r1, Object r2) {
        return put((TypeToken) r1, r2);
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    @Deprecated
    public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.reflect.TypeToInstanceMap
    @CheckForNull
    @Deprecated
    public <T extends B> T putInstance(TypeToken<T> r2, T r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.reflect.TypeToInstanceMap
    @CheckForNull
    @Deprecated
    public <T extends B> T putInstance(Class<T> r2, T r3) {
        throw new UnsupportedOperationException();
    }
}
