package com.google.common.collect;

import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class ImmutableListMultimap<K, V> extends ImmutableMultimap<K, V> implements ListMultimap<K, V> {
    private static final long serialVersionUID = 0;

    @CheckForNull
    @LazyInit
    private transient ImmutableListMultimap<V, K> inverse;

    public static final class Builder<K, V> extends ImmutableMultimap.Builder<K, V> {
        public Builder() {
        }

        Builder(int r1) {
            super(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public ImmutableListMultimap<K, V> build() {
            return (ImmutableListMultimap) super.build();
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap build() {
            return build();
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        Builder<K, V> combine(ImmutableMultimap.Builder<K, V> r1) {
            super.combine(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        /* bridge */ /* synthetic */ ImmutableMultimap.Builder combine(ImmutableMultimap.Builder r1) {
            return combine(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> expectedValuesPerKey(int r1) {
            super.expectedValuesPerKey(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder expectedValuesPerKey(int r1) {
            return expectedValuesPerKey(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> orderKeysBy(Comparator<? super K> r1) {
            super.orderKeysBy(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder orderKeysBy(Comparator r1) {
            return orderKeysBy(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> orderValuesBy(Comparator<? super V> r1) {
            super.orderValuesBy(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder orderValuesBy(Comparator r1) {
            return orderValuesBy(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> put(K r1, V r2) {
            super.put(r1, r2);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> r1) {
            super.put(r1);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder put(Object r1, Object r2) {
            return put(r1, r2);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder put(Map.Entry r1) {
            return put(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> putAll(Multimap<? extends K, ? extends V> r1) {
            super.putAll(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> r1) {
            super.putAll(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> putAll(K r1, Iterable<? extends V> r2) {
            super.putAll(r1, r2);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder<K, V> putAll(K r1, V... r2) {
            super.putAll(r1, r2);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder putAll(Multimap r1) {
            return putAll(r1);
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder putAll(Iterable r1) {
            return putAll(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder putAll(Object r1, Iterable r2) {
            return putAll(r1, r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public /* bridge */ /* synthetic */ ImmutableMultimap.Builder putAll(Object r1, Object[] r2) {
            return putAll(r1, r2);
        }
    }

    ImmutableListMultimap(ImmutableMap<K, ImmutableList<V>> r1, int r2) {
        super(r1, r2);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> Builder<K, V> builderWithExpectedKeys(int r1) {
        CollectPreconditions.checkNonnegative(r1, "expectedKeys");
        return new Builder(r1);
    }

    public static <K, V> ImmutableListMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> r2) {
        if (r2.isEmpty() == false) goto L7;
        return m225of();
    L7:
        if ((r2 instanceof ImmutableListMultimap) == false) goto L12;
        ImmutableListMultimap<K, V> r0 = (ImmutableListMultimap) r2;
        if (r0.isPartialView() == true) goto L12;
        return r0;
    L12:
        return fromMapEntries(r2.asMap().entrySet(), null);
    }

    public static <K, V> ImmutableListMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> r1) {
        return new Builder().putAll(r1).build();
    }

    public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(Function<? super T, ? extends K> r1, Function<? super T, ? extends Stream<? extends V>> r2) {
        return CollectCollectors.flatteningToImmutableListMultimap(r1, r2);
    }

    static <K, V> ImmutableListMultimap<K, V> fromMapBuilderEntries(Collection<? extends Map.Entry<K, ImmutableCollection.Builder<V>>> r8, @CheckForNull Comparator<? super V> r9) {
        if (r8.isEmpty() == true) goto L5;
        ImmutableMap.Builder r0 = new ImmutableMap.Builder(r8.size());
        int r1 = 0;
        Iterator<? extends Map.Entry<K, ImmutableCollection.Builder<V>>> r2 = r8.iterator();
    L8:
        if (r2.hasNext() == false) goto L15;
        Map.Entry<K, ImmutableCollection.Builder<V>> r3 = r2.next();
        K r4 = r3.getKey();
        ImmutableList.Builder r5 = (ImmutableList.Builder) r3.getValue();
        if (r9 != null) goto L12;
        ImmutableList r6 = r5.build();
    L13:
        r0.put(r4, r6);
        r1 = r1 + r6.size();
        goto L8
    L12:
        r6 = r5.buildSorted(r9);
        goto L13
    L15:
        return new ImmutableListMultimap(r0.buildOrThrow(), r1);
    L5:
        return m225of();
    }

    static <K, V> ImmutableListMultimap<K, V> fromMapEntries(Collection<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>> r8, @CheckForNull Comparator<? super V> r9) {
        if (r8.isEmpty() == true) goto L5;
        ImmutableMap.Builder r0 = new ImmutableMap.Builder(r8.size());
        int r1 = 0;
        Iterator<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>> r2 = r8.iterator();
    L8:
        if (r2.hasNext() == false) goto L18;
        Map.Entry<? extends K, ? extends Collection<? extends V>> r3 = r2.next();
        K r4 = r3.getKey();
        Collection<? extends V> r5 = r3.getValue();
        if (r9 != null) goto L12;
        ImmutableList r6 = ImmutableList.copyOf(r5);
    L14:
        if (r6.isEmpty() == true) goto L8;
        r0.put(r4, r6);
        r1 = r1 + r6.size();
        goto L8
    L12:
        r6 = ImmutableList.sortedCopyOf(r9, r5);
        goto L14
    L18:
        return new ImmutableListMultimap(r0.buildOrThrow(), r1);
    L5:
        return m225of();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ImmutableListMultimap<V, K> invert() {
        Builder r0 = builder();
        UnmodifiableIterator r1 = entries().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        Map.Entry r2 = (Map.Entry) r1.next();
        r0.put(r2.getValue(), r2.getKey());
        goto L4
    L6:
        ImmutableListMultimap<V, K> r3 = r0.build();
        r3.inverse = this;
        return r3;
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableListMultimap<K, V> m225of() {
        return EmptyImmutableListMultimap.INSTANCE;
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableListMultimap<K, V> m226of(K r2, V r3) {
        Builder r0 = builder();
        r0.put(r2, r3);
        return r0.build();
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableListMultimap<K, V> m227of(K r2, V r3, K r4, V r5) {
        Builder r0 = builder();
        r0.put(r2, r3);
        r0.put(r4, r5);
        return r0.build();
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableListMultimap<K, V> m228of(K r2, V r3, K r4, V r5, K r6, V r7) {
        Builder r0 = builder();
        r0.put(r2, r3);
        r0.put(r4, r5);
        r0.put(r6, r7);
        return r0.build();
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableListMultimap<K, V> m229of(K r2, V r3, K r4, V r5, K r6, V r7, K r8, V r9) {
        Builder r0 = builder();
        r0.put(r2, r3);
        r0.put(r4, r5);
        r0.put(r6, r7);
        r0.put(r8, r9);
        return r0.build();
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableListMultimap<K, V> m230of(K r2, V r3, K r4, V r5, K r6, V r7, K r8, V r9, K r10, V r11) {
        Builder r0 = builder();
        r0.put(r2, r3);
        r0.put(r4, r5);
        r0.put(r6, r7);
        r0.put(r8, r9);
        r0.put(r10, r11);
        return r0.build();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream r10) throws IOException, ClassNotFoundException {
        r10.defaultReadObject();
        int r0 = r10.readInt();
        if (r0 < 0) goto L21;
        ImmutableMap.Builder r1 = ImmutableMap.builder();
        int r2 = 0;
        int r3 = 0;
    L5:
        if (r3 >= r0) goto L22;
        Object r4 = Objects.requireNonNull(r10.readObject());
        int r5 = r10.readInt();
        if (r5 <= 0) goto L13;
        ImmutableList.Builder r6 = ImmutableList.builder();
        int r7 = 0;
    L9:
        if (r7 >= r5) goto L11;
        r6.add(Objects.requireNonNull(r10.readObject()));
        r7 = r7 + 1;
        goto L9
    L11:
        r1.put(r4, r6.build());
        r2 = r2 + r5;
        r3 = r3 + 1;
        goto L5
    L13:
        throw new InvalidObjectException("Invalid value count " + r5);
    L22:
        ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, r1.buildOrThrow());
        ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, r2);
        return;
    L17:
        e = move-exception;
        throw ((InvalidObjectException) new InvalidObjectException(e.getMessage()).initCause(e));
    L21:
        throw new InvalidObjectException("Invalid key count " + r0);
    }

    public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(Function<? super T, ? extends K> r1, Function<? super T, ? extends V> r2) {
        return CollectCollectors.toImmutableListMultimap(r1, r2);
    }

    private void writeObject(ObjectOutputStream r1) throws IOException {
        r1.defaultWriteObject();
        Serialization.writeMultimap(this, r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ ImmutableCollection get(Object r1) {
        return get(r1);
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    public ImmutableList<V> get(K r3) {
        ImmutableList<V> r0 = (ImmutableList) this.map.get(r3);
        if (r0 != null) goto L6;
        return ImmutableList.m212of();
    L6:
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection get(Object r1) {
        return get(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ List get(Object r1) {
        return get(r1);
    }

    @Override // com.google.common.collect.ImmutableMultimap
    public ImmutableListMultimap<V, K> inverse() {
        ImmutableListMultimap<V, K> r0 = this.inverse;
        if (r0 != null) goto L6;
        ImmutableListMultimap<V, K> r1 = invert();
        this.inverse = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.ImmutableMultimap
    public /* bridge */ /* synthetic */ ImmutableMultimap inverse() {
        return inverse();
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ ImmutableCollection removeAll(@CheckForNull Object r1) {
        return removeAll(r1);
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    @Deprecated
    public final ImmutableList<V> removeAll(@CheckForNull Object r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ Collection removeAll(@CheckForNull Object r1) {
        return removeAll(r1);
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ List removeAll(@CheckForNull Object r1) {
        return removeAll(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ ImmutableCollection replaceValues(Object r1, Iterable r2) {
        return replaceValues(r1, r2);
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public final ImmutableList<V> replaceValues(K r2, Iterable<? extends V> r3) {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ Collection replaceValues(Object r1, Iterable r2) {
        return replaceValues(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    @Deprecated
    public /* bridge */ /* synthetic */ List replaceValues(Object r1, Iterable r2) {
        return replaceValues(r1, r2);
    }
}
