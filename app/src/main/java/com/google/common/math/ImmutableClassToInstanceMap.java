package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Primitives;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@Immutable(containerOf = {"B"})
@ElementTypesAreNonnullByDefault
public final class ImmutableClassToInstanceMap<B> extends ForwardingMap<Class<? extends B>, B> implements ClassToInstanceMap<B>, Serializable {
    private static final ImmutableClassToInstanceMap<Object> EMPTY = null;
    private final ImmutableMap<Class<? extends B>, B> delegate;

    /* JADX INFO: renamed from: com.google.common.collect.ImmutableClassToInstanceMap$1 */
    static /* synthetic */ class C06271 {
    }

    public static final class Builder<B> {
        private final ImmutableMap.Builder<Class<? extends B>, B> mapBuilder;

        public Builder() {
            this.mapBuilder = ImmutableMap.builder();
        }

        private static <T> T cast(Class<T> r1, Object r2) {
            return (T) Primitives.wrap(r1).cast(r2);
        }

        public ImmutableClassToInstanceMap<B> build() {
            ImmutableMap<Class<? extends B>, B> r0 = this.mapBuilder.buildOrThrow();
            if (r0.isEmpty() == false) goto L7;
            return ImmutableClassToInstanceMap.m210of();
        L7:
            return new ImmutableClassToInstanceMap(r0, null);
        }

        public <T extends B> Builder<B> put(Class<T> r2, T r3) {
            this.mapBuilder.put(r2, r3);
            return this;
        }

        public <T extends B> Builder<B> putAll(Map<? extends Class<? extends T>, ? extends T> r7) {
            Iterator<Map.Entry<? extends Class<? extends T>, ? extends T>> r0 = r7.entrySet().iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            Map.Entry<? extends Class<? extends T>, ? extends T> r1 = r0.next();
            Class r2 = r1.getKey();
            T r3 = r1.getValue();
            ImmutableMap.Builder<Class<? extends B>, B> r4 = this.mapBuilder;
            Object r5 = cast(r2, r3);
            r4.put((Class<? extends B>) r2, (B) r5);
            goto L4
        L6:
            return this;
        }
    }

    static {
        EMPTY = new ImmutableClassToInstanceMap(ImmutableMap.m231of());
    }

    private ImmutableClassToInstanceMap(ImmutableMap<Class<? extends B>, B> r1) {
        this.delegate = r1;
    }

    /* synthetic */ ImmutableClassToInstanceMap(ImmutableMap r1, C06271 r2) {
        this(r1);
    }

    public static <B> Builder<B> builder() {
        return new Builder();
    }

    public static <B, S extends B> ImmutableClassToInstanceMap<B> copyOf(Map<? extends Class<? extends S>, ? extends S> r2) {
        if ((r2 instanceof ImmutableClassToInstanceMap) == false) goto L7;
        return (ImmutableClassToInstanceMap) r2;
    L7:
        return new Builder().putAll(r2).build();
    }

    /* JADX INFO: renamed from: of */
    public static <B> ImmutableClassToInstanceMap<B> m210of() {
        return (ImmutableClassToInstanceMap<B>) EMPTY;
    }

    /* JADX INFO: renamed from: of */
    public static <B, T extends B> ImmutableClassToInstanceMap<B> m211of(Class<T> r2, T r3) {
        return new ImmutableClassToInstanceMap(ImmutableMap.m232of(r2, r3));
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected Map<Class<? extends B>, B> delegate() {
        return this.delegate;
    }

    @Override // com.google.common.collect.ClassToInstanceMap
    @CheckForNull
    public <T extends B> T getInstance(Class<T> r3) {
        return this.delegate.get(Preconditions.checkNotNull(r3));
    }

    @Override // com.google.common.collect.ClassToInstanceMap
    @CheckForNull
    @Deprecated
    public <T extends B> T putInstance(Class<T> r2, T r3) {
        throw new UnsupportedOperationException();
    }

    Object readResolve() {
        if (isEmpty() == false) goto L6;
        return m210of();
    L6:
        return this;
    }
}
