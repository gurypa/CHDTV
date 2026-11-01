package com.google.common.collect;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ImmutableBiMap<K, V> extends ImmutableMap<K, V> implements BiMap<K, V> {
    private static final long serialVersionUID = 912559;

    public static final class Builder<K, V> extends ImmutableMap.Builder<K, V> {
        public Builder() {
        }

        Builder(int r1) {
            super(r1);
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public ImmutableBiMap<K, V> build() {
            return buildOrThrow();
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public /* bridge */ /* synthetic */ ImmutableMap build() {
            return build();
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        @Deprecated
        public ImmutableBiMap<K, V> buildKeepingLast() {
            throw new UnsupportedOperationException("Not supported for bimaps");
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        @Deprecated
        public /* bridge */ /* synthetic */ ImmutableMap buildKeepingLast() {
            return buildKeepingLast();
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public ImmutableBiMap<K, V> buildOrThrow() {
            if (this.size != 0) goto L7;
            return ImmutableBiMap.m199of();
        L7:
            if (this.valueComparator != null) goto L9;
        L12:
            this.entriesUsed = true;
            return new RegularImmutableBiMap(this.alternatingKeysAndValues, this.size);
        L9:
            if (this.entriesUsed == false) goto L11;
            this.alternatingKeysAndValues = Arrays.copyOf(this.alternatingKeysAndValues, this.size * 2);
        L11:
            sortEntries(this.alternatingKeysAndValues, this.size, this.valueComparator);
            goto L12
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public /* bridge */ /* synthetic */ ImmutableMap buildOrThrow() {
            return buildOrThrow();
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        Builder<K, V> combine(ImmutableMap.Builder<K, V> r1) {
            super.combine(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        /* bridge */ /* synthetic */ ImmutableMap.Builder combine(ImmutableMap.Builder r1) {
            return combine(r1);
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public Builder<K, V> orderEntriesByValue(Comparator<? super V> r1) {
            super.orderEntriesByValue(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public /* bridge */ /* synthetic */ ImmutableMap.Builder orderEntriesByValue(Comparator r1) {
            return orderEntriesByValue(r1);
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public Builder<K, V> put(K r1, V r2) {
            super.put(r1, r2);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> r1) {
            super.put(r1);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMap.Builder
        public /* bridge */ /* synthetic */ ImmutableMap.Builder put(Object r1, Object r2) {
            return put(r1, r2);
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public /* bridge */ /* synthetic */ ImmutableMap.Builder put(Map.Entry r1) {
            return put(r1);
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> r1) {
            super.putAll(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public Builder<K, V> putAll(Map<? extends K, ? extends V> r1) {
            super.putAll(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public /* bridge */ /* synthetic */ ImmutableMap.Builder putAll(Iterable r1) {
            return putAll(r1);
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public /* bridge */ /* synthetic */ ImmutableMap.Builder putAll(Map r1) {
            return putAll(r1);
        }
    }

    private static class SerializedForm<K, V> extends ImmutableMap.SerializedForm<K, V> {
        private static final long serialVersionUID = 0;

        SerializedForm(ImmutableBiMap<K, V> r1) {
            super(r1);
        }

        @Override // com.google.common.collect.ImmutableMap.SerializedForm
        Builder<K, V> makeBuilder(int r2) {
            return new Builder(r2);
        }

        @Override // com.google.common.collect.ImmutableMap.SerializedForm
        /* bridge */ /* synthetic */ ImmutableMap.Builder makeBuilder(int r1) {
            return makeBuilder(r1);
        }
    }

    ImmutableBiMap() {
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> Builder<K, V> builderWithExpectedSize(int r1) {
        CollectPreconditions.checkNonnegative(r1, "expectedSize");
        return new Builder(r1);
    }

    public static <K, V> ImmutableBiMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> r2) {
        if ((r2 instanceof Collection) == false) goto L5;
        int r0 = ((Collection) r2).size();
    L7:
        return new Builder(r0).putAll(r2).build();
    L5:
        r0 = 4;
        goto L7
    }

    public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> r2) {
        if ((r2 instanceof ImmutableBiMap) == false) goto L8;
        ImmutableBiMap<K, V> r0 = (ImmutableBiMap) r2;
        if (r0.isPartialView() == true) goto L8;
        return r0;
    L8:
        return copyOf(r2.entrySet());
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m199of() {
        return RegularImmutableBiMap.EMPTY;
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m200of(K r3, V r4) {
        CollectPreconditions.checkEntryNotNull(r3, r4);
        return new RegularImmutableBiMap(new Object[]{r3, r4}, 1);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m201of(K r4, V r5, K r6, V r7) {
        CollectPreconditions.checkEntryNotNull(r4, r5);
        CollectPreconditions.checkEntryNotNull(r6, r7);
        return new RegularImmutableBiMap(new Object[]{r4, r5, r6, r7}, 2);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m202of(K r4, V r5, K r6, V r7, K r8, V r9) {
        CollectPreconditions.checkEntryNotNull(r4, r5);
        CollectPreconditions.checkEntryNotNull(r6, r7);
        CollectPreconditions.checkEntryNotNull(r8, r9);
        return new RegularImmutableBiMap(new Object[]{r4, r5, r6, r7, r8, r9}, 3);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m203of(K r4, V r5, K r6, V r7, K r8, V r9, K r10, V r11) {
        CollectPreconditions.checkEntryNotNull(r4, r5);
        CollectPreconditions.checkEntryNotNull(r6, r7);
        CollectPreconditions.checkEntryNotNull(r8, r9);
        CollectPreconditions.checkEntryNotNull(r10, r11);
        return new RegularImmutableBiMap(new Object[]{r4, r5, r6, r7, r8, r9, r10, r11}, 4);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m204of(K r4, V r5, K r6, V r7, K r8, V r9, K r10, V r11, K r12, V r13) {
        CollectPreconditions.checkEntryNotNull(r4, r5);
        CollectPreconditions.checkEntryNotNull(r6, r7);
        CollectPreconditions.checkEntryNotNull(r8, r9);
        CollectPreconditions.checkEntryNotNull(r10, r11);
        CollectPreconditions.checkEntryNotNull(r12, r13);
        return new RegularImmutableBiMap(new Object[]{r4, r5, r6, r7, r8, r9, r10, r11, r12, r13}, 5);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m205of(K r4, V r5, K r6, V r7, K r8, V r9, K r10, V r11, K r12, V r13, K r14, V r15) {
        CollectPreconditions.checkEntryNotNull(r4, r5);
        CollectPreconditions.checkEntryNotNull(r6, r7);
        CollectPreconditions.checkEntryNotNull(r8, r9);
        CollectPreconditions.checkEntryNotNull(r10, r11);
        CollectPreconditions.checkEntryNotNull(r12, r13);
        CollectPreconditions.checkEntryNotNull(r14, r15);
        return new RegularImmutableBiMap(new Object[]{r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15}, 6);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m206of(K r4, V r5, K r6, V r7, K r8, V r9, K r10, V r11, K r12, V r13, K r14, V r15, K r16, V r17) {
        CollectPreconditions.checkEntryNotNull(r4, r5);
        CollectPreconditions.checkEntryNotNull(r6, r7);
        CollectPreconditions.checkEntryNotNull(r8, r9);
        CollectPreconditions.checkEntryNotNull(r10, r11);
        CollectPreconditions.checkEntryNotNull(r12, r13);
        CollectPreconditions.checkEntryNotNull(r14, r15);
        CollectPreconditions.checkEntryNotNull(r16, r17);
        return new RegularImmutableBiMap(new Object[]{r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17}, 7);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m207of(K r4, V r5, K r6, V r7, K r8, V r9, K r10, V r11, K r12, V r13, K r14, V r15, K r16, V r17, K r18, V r19) {
        CollectPreconditions.checkEntryNotNull(r4, r5);
        CollectPreconditions.checkEntryNotNull(r6, r7);
        CollectPreconditions.checkEntryNotNull(r8, r9);
        CollectPreconditions.checkEntryNotNull(r10, r11);
        CollectPreconditions.checkEntryNotNull(r12, r13);
        CollectPreconditions.checkEntryNotNull(r14, r15);
        CollectPreconditions.checkEntryNotNull(r16, r17);
        CollectPreconditions.checkEntryNotNull(r18, r19);
        return new RegularImmutableBiMap(new Object[]{r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19}, 8);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m208of(K r4, V r5, K r6, V r7, K r8, V r9, K r10, V r11, K r12, V r13, K r14, V r15, K r16, V r17, K r18, V r19, K r20, V r21) {
        CollectPreconditions.checkEntryNotNull(r4, r5);
        CollectPreconditions.checkEntryNotNull(r6, r7);
        CollectPreconditions.checkEntryNotNull(r8, r9);
        CollectPreconditions.checkEntryNotNull(r10, r11);
        CollectPreconditions.checkEntryNotNull(r12, r13);
        CollectPreconditions.checkEntryNotNull(r14, r15);
        CollectPreconditions.checkEntryNotNull(r16, r17);
        CollectPreconditions.checkEntryNotNull(r18, r19);
        CollectPreconditions.checkEntryNotNull(r20, r21);
        return new RegularImmutableBiMap(new Object[]{r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21}, 9);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m209of(K r4, V r5, K r6, V r7, K r8, V r9, K r10, V r11, K r12, V r13, K r14, V r15, K r16, V r17, K r18, V r19, K r20, V r21, K r22, V r23) {
        CollectPreconditions.checkEntryNotNull(r4, r5);
        CollectPreconditions.checkEntryNotNull(r6, r7);
        CollectPreconditions.checkEntryNotNull(r8, r9);
        CollectPreconditions.checkEntryNotNull(r10, r11);
        CollectPreconditions.checkEntryNotNull(r12, r13);
        CollectPreconditions.checkEntryNotNull(r14, r15);
        CollectPreconditions.checkEntryNotNull(r16, r17);
        CollectPreconditions.checkEntryNotNull(r18, r19);
        CollectPreconditions.checkEntryNotNull(r20, r21);
        CollectPreconditions.checkEntryNotNull(r22, r23);
        return new RegularImmutableBiMap(new Object[]{r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23}, 10);
    }

    @SafeVarargs
    public static <K, V> ImmutableBiMap<K, V> ofEntries(Map.Entry<? extends K, ? extends V>... r2) {
        return copyOf(Arrays.asList(r2));
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    public static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(Function<? super T, ? extends K> r1, Function<? super T, ? extends V> r2) {
        return CollectCollectors.toImmutableBiMap(r1, r2);
    }

    @Deprecated
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> r1, Function<? super T, ? extends V> r2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> r1, Function<? super T, ? extends V> r2, BinaryOperator<V> r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.ImmutableMap
    /* bridge */ /* synthetic */ ImmutableCollection createValues() {
        return createValues();
    }

    @Override // com.google.common.collect.ImmutableMap
    final ImmutableSet<V> createValues() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.BiMap
    @CheckForNull
    @Deprecated
    public final V forcePut(K r2, V r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.BiMap
    public /* bridge */ /* synthetic */ BiMap inverse() {
        return inverse();
    }

    @Override // com.google.common.collect.BiMap
    public abstract ImmutableBiMap<V, K> inverse();

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public /* bridge */ /* synthetic */ ImmutableCollection values() {
        return values();
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableSet<V> values() {
        return inverse().keySet();
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public /* bridge */ /* synthetic */ Collection values() {
        return values();
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public /* bridge */ /* synthetic */ Set values() {
        return values();
    }

    @Override // com.google.common.collect.ImmutableMap
    Object writeReplace() {
        return new SerializedForm(this);
    }
}
