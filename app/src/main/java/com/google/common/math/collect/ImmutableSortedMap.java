package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class ImmutableSortedMap<K, V> extends ImmutableMap<K, V> implements NavigableMap<K, V> {
    private static final ImmutableSortedMap<Comparable<?>, Object> NATURAL_EMPTY_MAP = null;
    private static final Comparator<?> NATURAL_ORDER = null;
    private static final long serialVersionUID = 0;

    @CheckForNull
    private transient ImmutableSortedMap<K, V> descendingMap;
    private final transient RegularImmutableSortedSet<K> keySet;
    private final transient ImmutableList<V> valueList;

    public static class Builder<K, V> extends ImmutableMap.Builder<K, V> {
        private final Comparator<? super K> comparator;
        private transient Object[] keys;
        private transient Object[] values;

        public Builder(Comparator<? super K> r2) {
            this(r2, 4);
        }

        private Builder(Comparator<? super K> r2, int r3) {
            this.comparator = (Comparator) Preconditions.checkNotNull(r2);
            this.keys = new Object[r3];
            this.values = new Object[r3];
        }

        private void ensureCapacity(int r3) {
            if (r3 <= this.keys.length) goto L6;
            int r0 = ImmutableCollection.Builder.expandedCapacity(this.keys.length, r3);
            this.keys = Arrays.copyOf(this.keys, r0);
            this.values = Arrays.copyOf(this.values, r0);
            return;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public /* bridge */ /* synthetic */ ImmutableMap build() {
            return build();
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public ImmutableSortedMap<K, V> build() {
            return buildOrThrow();
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        @Deprecated
        public /* bridge */ /* synthetic */ ImmutableMap buildKeepingLast() {
            return buildKeepingLast();
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        @Deprecated
        public final ImmutableSortedMap<K, V> buildKeepingLast() {
            throw new UnsupportedOperationException("ImmutableSortedMap.Builder does not yet implement buildKeepingLast()");
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public /* bridge */ /* synthetic */ ImmutableMap buildOrThrow() {
            return buildOrThrow();
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public ImmutableSortedMap<K, V> buildOrThrow() {
            switch(this.size) {
                case 0: goto L8;
                case 1: goto L6;
                default: goto L4;
            };
        L4:
            Object[] r0 = Arrays.copyOf(this.keys, this.size);
            Arrays.sort(r0, this.comparator);
            Object[] r2 = new Object[this.size];
            int r3 = 0;
        L10:
            if (r3 >= this.size) goto L19;
            if (r3 <= 0) goto L17;
            if (this.comparator.compare(r0[r3 - 1], r0[r3]) != 0) goto L17;
            throw new IllegalArgumentException("keys required to be distinct but compared as equal: " + r0[r3 - 1] + " and " + r0[r3]);
        L17:
            r2[Arrays.binarySearch(r0, Objects.requireNonNull(this.keys[r3]), this.comparator)] = Objects.requireNonNull(this.values[r3]);
            r3 = r3 + 1;
            goto L10
        L19:
            return new ImmutableSortedMap(new RegularImmutableSortedSet(ImmutableList.asImmutableList(r0), this.comparator), ImmutableList.asImmutableList(r2));
        L6:
            return ImmutableSortedMap.access$000(this.comparator, Objects.requireNonNull(this.keys[0]), Objects.requireNonNull(this.values[0]));
        L8:
            return ImmutableSortedMap.emptyMap(this.comparator);
        }

        Builder<K, V> combine(Builder<K, V> r6) {
            ensureCapacity(this.size + r6.size);
            System.arraycopy(r6.keys, 0, this.keys, this.size, r6.size);
            System.arraycopy(r6.values, 0, this.values, this.size, r6.size);
            this.size += r6.size;
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        @Deprecated
        public /* bridge */ /* synthetic */ ImmutableMap.Builder orderEntriesByValue(Comparator r1) {
            return orderEntriesByValue(r1);
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        @Deprecated
        public final Builder<K, V> orderEntriesByValue(Comparator<? super V> r3) {
            throw new UnsupportedOperationException("Not available on ImmutableSortedMap.Builder");
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
        public Builder<K, V> put(K r3, V r4) {
            ensureCapacity(this.size + 1);
            CollectPreconditions.checkEntryNotNull(r3, r4);
            this.keys[this.size] = r3;
            this.values[this.size] = r4;
            this.size++;
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> r1) {
            super.put(r1);
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
    }

    private static class SerializedForm<K, V> extends ImmutableMap.SerializedForm<K, V> {
        private static final long serialVersionUID = 0;
        private final Comparator<? super K> comparator;

        SerializedForm(ImmutableSortedMap<K, V> r2) {
            super(r2);
            this.comparator = r2.comparator();
        }

        @Override // com.google.common.collect.ImmutableMap.SerializedForm
        /* bridge */ /* synthetic */ ImmutableMap.Builder makeBuilder(int r1) {
            return makeBuilder(r1);
        }

        @Override // com.google.common.collect.ImmutableMap.SerializedForm
        Builder<K, V> makeBuilder(int r3) {
            return new Builder(this.comparator);
        }
    }

    static {
        NATURAL_ORDER = Ordering.natural();
        NATURAL_EMPTY_MAP = new ImmutableSortedMap(ImmutableSortedSet.emptySet(Ordering.natural()), ImmutableList.m212of());
    }

    ImmutableSortedMap(RegularImmutableSortedSet<K> r2, ImmutableList<V> r3) {
        this(r2, r3, null);
    }

    ImmutableSortedMap(RegularImmutableSortedSet<K> r1, ImmutableList<V> r2, @CheckForNull ImmutableSortedMap<K, V> r3) {
        this.keySet = r1;
        this.valueList = r2;
        this.descendingMap = r3;
    }

    static /* synthetic */ ImmutableSortedMap access$000(Comparator r1, Object r2, Object r3) {
        return m293of(r1, r2, r3);
    }

    static /* synthetic */ RegularImmutableSortedSet access$100(ImmutableSortedMap r1) {
        return r1.keySet;
    }

    static /* synthetic */ ImmutableList access$200(ImmutableSortedMap r1) {
        return r1.valueList;
    }

    @Deprecated
    public static <K, V> Builder<K, V> builder() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public static <K, V> Builder<K, V> builderWithExpectedSize(int r1) {
        throw new UnsupportedOperationException();
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> r2) {
        return copyOf(r2, (Ordering) NATURAL_ORDER);
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> r2, Comparator<? super K> r3) {
        return fromEntries((Comparator) Preconditions.checkNotNull(r3), false, r2);
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> r2) {
        return copyOfInternal(r2, (Ordering) NATURAL_ORDER);
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> r1, Comparator<? super K> r2) {
        return copyOfInternal(r1, (Comparator) Preconditions.checkNotNull(r2));
    }

    private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> r4, Comparator<? super K> r5) {
        boolean r0 = false;
        if ((r4 instanceof SortedMap) == false) goto L12;
        Comparator<? super K> r2 = ((SortedMap) r4).comparator();
        if (r2 == null) goto L7;
        boolean r3 = r5.equals(r2);
    L11:
        r0 = r3;
        goto L12
    L7:
        if (r5 != NATURAL_ORDER) goto L9;
        r3 = true;
        goto L11
    L9:
        r3 = false;
    L12:
        if (r0 == false) goto L19;
        if ((r4 instanceof ImmutableSortedMap) == false) goto L19;
        ImmutableSortedMap<K, V> r1 = (ImmutableSortedMap) r4;
        if (r1.isPartialView() == true) goto L19;
        return r1;
    L19:
        return fromEntries(r5, r0, r4.entrySet());
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> r3) {
        Comparator r0 = r3.comparator();
        if (r0 != null) goto L6;
        r0 = NATURAL_ORDER;
    L6:
        if ((r3 instanceof ImmutableSortedMap) == false) goto L11;
        ImmutableSortedMap<K, V> r1 = (ImmutableSortedMap) r3;
        if (r1.isPartialView() == true) goto L11;
        return r1;
    L11:
        return fromEntries(r0, true, r3.entrySet());
    }

    static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> r3) {
        if (Ordering.natural().equals(r3) == false) goto L7;
        return m272of();
    L7:
        return new ImmutableSortedMap(ImmutableSortedSet.emptySet(r3), ImmutableList.m212of());
    }

    private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> r2, boolean r3, Iterable<? extends Map.Entry<? extends K, ? extends V>> r4) {
        Map.Entry[] r0 = (Map.Entry[]) Iterables.toArray(r4, EMPTY_ENTRY_ARRAY);
        return fromEntries(r2, r3, r0, r0.length);
    }

    private static <K, V> ImmutableSortedMap<K, V> fromEntries(final Comparator<? super K> r12, boolean r13, Map.Entry<K, V>[] r14, int r15) {
        switch(r15) {
            case 0: goto L10;
            case 1: goto L7;
            default: goto L4;
        };
    L4:
        Object[] r1 = new Object[r15];
        Object[] r2 = new Object[r15];
        if (r13 == false) goto L14;
        int r0 = 0;
    L11:
        if (r0 >= r15) goto L22;
        Map.Entry r3 = (Map.Entry) Objects.requireNonNull(r14[r0]);
        Object r4 = r3.getKey();
        Object r5 = r3.getValue();
        CollectPreconditions.checkEntryNotNull(r4, r5);
        r1[r0] = r4;
        r2[r0] = r5;
        r0 = r0 + 1;
    L22:
        return new ImmutableSortedMap(new RegularImmutableSortedSet(ImmutableList.asImmutableList(r1), r12), ImmutableList.asImmutableList(r2));
    L14:
        Arrays.sort(r14, 0, r15, new ImmutableSortedMap$$ExternalSyntheticLambda0(r12));
        Map.Entry r6 = (Map.Entry) Objects.requireNonNull(r14[0]);
        Object r7 = r6.getKey();
        r1[0] = r7;
        r2[0] = r6.getValue();
        CollectPreconditions.checkEntryNotNull(r1[0], r2[0]);
        int r8 = 1;
    L15:
        if (r8 >= r15) goto L22;
        Map.Entry r9 = (Map.Entry) Objects.requireNonNull(r14[r8 - 1]);
        Map.Entry r10 = (Map.Entry) Objects.requireNonNull(r14[r8]);
        Object r11 = r10.getKey();
        Object r16 = r10.getValue();
        CollectPreconditions.checkEntryNotNull(r11, r16);
        r1[r8] = r11;
        r2[r8] = r16;
        if (r12.compare(r7, r11) == 0) goto L19;
        boolean r17 = true;
    L20:
        checkNoConflict(r17, "key", r9, r10);
        r7 = r11;
        r8 = r8 + 1;
        goto L15
    L19:
        r17 = false;
        goto L20
    L7:
        Map.Entry r18 = (Map.Entry) Objects.requireNonNull(r14[0]);
        return m293of(r12, r18.getKey(), r18.getValue());
    L10:
        return emptyMap(r12);
    }

    private static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> fromEntries(Map.Entry<K, V>... r3) {
        return fromEntries(Ordering.natural(), false, r3, r3.length);
    }

    private ImmutableSortedMap<K, V> getSubMap(int r4, int r5) {
        if (r4 == 0) goto L4;
    L6:
        if (r4 != r5) goto L10;
        return emptyMap(comparator());
    L10:
        return new ImmutableSortedMap(this.keySet.getSubSet(r4, r5), this.valueList.subList(r4, r5));
    L4:
        if (r5 != size()) goto L6;
        return this;
    }

    static /* synthetic */ int lambda$fromEntries$0(Comparator r2, Map.Entry r3, Map.Entry r4) {
        Objects.requireNonNull(r3);
        Objects.requireNonNull(r4);
        return r2.compare(r3.getKey(), r4.getKey());
    }

    public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
        return new Builder(Ordering.natural());
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSortedMap<K, V> m272of() {
        return (ImmutableSortedMap<K, V>) NATURAL_EMPTY_MAP;
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMap m273of(Comparable r1, Object r2) {
        return m293of(Ordering.natural(), r1, r2);
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMap m274of(Comparable r4, Object r5, Comparable r6, Object r7) {
        return fromEntries(new Map.Entry[]{entryOf(r4, r5), entryOf(r6, r7)});
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMap m275of(Comparable r5, Object r6, Comparable r7, Object r8, Comparable r9, Object r10) {
        return fromEntries(new Map.Entry[]{entryOf(r5, r6), entryOf(r7, r8), entryOf(r9, r10)});
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMap m276of(Comparable r6, Object r7, Comparable r8, Object r9, Comparable r10, Object r11, Comparable r12, Object r13) {
        return fromEntries(new Map.Entry[]{entryOf(r6, r7), entryOf(r8, r9), entryOf(r10, r11), entryOf(r12, r13)});
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMap m277of(Comparable r7, Object r8, Comparable r9, Object r10, Comparable r11, Object r12, Comparable r13, Object r14, Comparable r15, Object r16) {
        return fromEntries(new Map.Entry[]{entryOf(r7, r8), entryOf(r9, r10), entryOf(r11, r12), entryOf(r13, r14), entryOf(r15, r16)});
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMap m278of(Comparable r8, Object r9, Comparable r10, Object r11, Comparable r12, Object r13, Comparable r14, Object r15, Comparable r16, Object r17, Comparable r18, Object r19) {
        return fromEntries(new Map.Entry[]{entryOf(r8, r9), entryOf(r10, r11), entryOf(r12, r13), entryOf(r14, r15), entryOf(r16, r17), entryOf(r18, r19)});
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMap m279of(Comparable r9, Object r10, Comparable r11, Object r12, Comparable r13, Object r14, Comparable r15, Object r16, Comparable r17, Object r18, Comparable r19, Object r20, Comparable r21, Object r22) {
        return fromEntries(new Map.Entry[]{entryOf(r9, r10), entryOf(r11, r12), entryOf(r13, r14), entryOf(r15, r16), entryOf(r17, r18), entryOf(r19, r20), entryOf(r21, r22)});
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMap m280of(Comparable r10, Object r11, Comparable r12, Object r13, Comparable r14, Object r15, Comparable r16, Object r17, Comparable r18, Object r19, Comparable r20, Object r21, Comparable r22, Object r23, Comparable r24, Object r25) {
        return fromEntries(new Map.Entry[]{entryOf(r10, r11), entryOf(r12, r13), entryOf(r14, r15), entryOf(r16, r17), entryOf(r18, r19), entryOf(r20, r21), entryOf(r22, r23), entryOf(r24, r25)});
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMap m281of(Comparable r11, Object r12, Comparable r13, Object r14, Comparable r15, Object r16, Comparable r17, Object r18, Comparable r19, Object r20, Comparable r21, Object r22, Comparable r23, Object r24, Comparable r25, Object r26, Comparable r27, Object r28) {
        return fromEntries(new Map.Entry[]{entryOf(r11, r12), entryOf(r13, r14), entryOf(r15, r16), entryOf(r17, r18), entryOf(r19, r20), entryOf(r21, r22), entryOf(r23, r24), entryOf(r25, r26), entryOf(r27, r28)});
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMap m282of(Comparable r12, Object r13, Comparable r14, Object r15, Comparable r16, Object r17, Comparable r18, Object r19, Comparable r20, Object r21, Comparable r22, Object r23, Comparable r24, Object r25, Comparable r26, Object r27, Comparable r28, Object r29, Comparable r30, Object r31) {
        return fromEntries(new Map.Entry[]{entryOf(r12, r13), entryOf(r14, r15), entryOf(r16, r17), entryOf(r18, r19), entryOf(r20, r21), entryOf(r22, r23), entryOf(r24, r25), entryOf(r26, r27), entryOf(r28, r29), entryOf(r30, r31)});
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSortedMap<K, V> m283of(K r1, V r2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSortedMap<K, V> m284of(K r1, V r2, K r3, V r4) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSortedMap<K, V> m285of(K r1, V r2, K r3, V r4, K r5, V r6) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSortedMap<K, V> m286of(K r1, V r2, K r3, V r4, K r5, V r6, K r7, V r8) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSortedMap<K, V> m287of(K r1, V r2, K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSortedMap<K, V> m288of(K r1, V r2, K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10, K r11, V r12) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSortedMap<K, V> m289of(K r1, V r2, K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10, K r11, V r12, K r13, V r14) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSortedMap<K, V> m290of(K r1, V r2, K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10, K r11, V r12, K r13, V r14, K r15, V r16) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSortedMap<K, V> m291of(K r1, V r2, K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10, K r11, V r12, K r13, V r14, K r15, V r16, K r17, V r18) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableSortedMap<K, V> m292of(K r1, V r2, K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10, K r11, V r12, K r13, V r14, K r15, V r16, K r17, V r18, K r19, V r20) {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: renamed from: of */
    private static <K, V> ImmutableSortedMap<K, V> m293of(Comparator<? super K> r4, K r5, V r6) {
        return new ImmutableSortedMap(new RegularImmutableSortedSet(ImmutableList.m213of(r5), (Comparator) Preconditions.checkNotNull(r4)), ImmutableList.m213of(r6));
    }

    @SafeVarargs
    @Deprecated
    public static <K, V> ImmutableSortedMap<K, V> ofEntries(Map.Entry<? extends K, ? extends V>... r1) {
        throw new UnsupportedOperationException();
    }

    public static <K, V> Builder<K, V> orderedBy(Comparator<K> r1) {
        return new Builder(r1);
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
        return new Builder(Ordering.natural().reverse());
    }

    @Deprecated
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> r1, Function<? super T, ? extends V> r2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> r1, Function<? super T, ? extends V> r2, BinaryOperator<V> r3) {
        throw new UnsupportedOperationException();
    }

    public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> r1, Function<? super T, ? extends K> r2, Function<? super T, ? extends V> r3) {
        return CollectCollectors.toImmutableSortedMap(r1, r2, r3);
    }

    public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> r1, Function<? super T, ? extends K> r2, Function<? super T, ? extends V> r3, BinaryOperator<V> r4) {
        return CollectCollectors.toImmutableSortedMap(r1, r2, r3, r4);
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> ceilingEntry(K r2) {
        return tailMap(r2, true).firstEntry();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public K ceilingKey(K r2) {
        return (K) Maps.keyOrNull(ceilingEntry(r2));
    }

    @Override // java.util.SortedMap
    public Comparator<? super K> comparator() {
        return keySet().comparator();
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        if (isEmpty() == false) goto L6;
        return ImmutableSet.m259of();
    L6:
        return new C1EntrySet(this);
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<K> createKeySet() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableCollection<V> createValues() {
        throw new AssertionError("should never be called");
    }

    @Override // java.util.NavigableMap
    public ImmutableSortedSet<K> descendingKeySet() {
        return this.keySet.descendingSet();
    }

    @Override // java.util.NavigableMap
    public /* bridge */ /* synthetic */ NavigableSet descendingKeySet() {
        return descendingKeySet();
    }

    @Override // java.util.NavigableMap
    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap<K, V> r0 = this.descendingMap;
        if (r0 == null) goto L5;
        return r0;
    L5:
        if (isEmpty() == false) goto L9;
        return emptyMap(Ordering.from(comparator()).reverse());
    L9:
        return new ImmutableSortedMap((RegularImmutableSortedSet) this.keySet.descendingSet(), this.valueList.reverse(), this);
    }

    @Override // java.util.NavigableMap
    public /* bridge */ /* synthetic */ NavigableMap descendingMap() {
        return descendingMap();
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public /* bridge */ /* synthetic */ Set entrySet() {
        return entrySet();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> firstEntry() {
        if (isEmpty() == false) goto L6;
        return null;
    L6:
        return entrySet().asList().get(0);
    }

    @Override // java.util.SortedMap
    public K firstKey() {
        return keySet().first();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> floorEntry(K r2) {
        return headMap(r2, true).lastEntry();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public K floorKey(K r2) {
        return (K) Maps.keyOrNull(floorEntry(r2));
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    @CheckForNull
    public V get(@CheckForNull Object r3) {
        int r0 = this.keySet.indexOf(r3);
        if (r0 != (-1)) goto L6;
        return null;
    L6:
        return this.valueList.get(r0);
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    public ImmutableSortedMap<K, V> headMap(K r2) {
        return headMap(r2, false);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // java.util.NavigableMap
    public ImmutableSortedMap<K, V> headMap(K r3, boolean r4) {
        return getSubMap(0, this.keySet.headIndex((K) Preconditions.checkNotNull(r3), r4));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    public /* bridge */ /* synthetic */ NavigableMap headMap(Object r1, boolean r2) {
        return headMap(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap, java.util.SortedMap
    public /* bridge */ /* synthetic */ SortedMap headMap(Object r1) {
        return headMap(r1);
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> higherEntry(K r2) {
        return tailMap(r2, false).firstEntry();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public K higherKey(K r2) {
        return (K) Maps.keyOrNull(higherEntry(r2));
    }

    @Override // com.google.common.collect.ImmutableMap
    boolean isPartialView() {
        if (this.keySet.isPartialView() == false) goto L5;
    L8:
        return true;
    L5:
        if (this.valueList.isPartialView() == true) goto L8;
        return false;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public /* bridge */ /* synthetic */ ImmutableSet keySet() {
        return keySet();
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public /* bridge */ /* synthetic */ Set keySet() {
        return keySet();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> lastEntry() {
        if (isEmpty() == false) goto L6;
        return null;
    L6:
        return entrySet().asList().get(size() - 1);
    }

    @Override // java.util.SortedMap
    public K lastKey() {
        return keySet().last();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public Map.Entry<K, V> lowerEntry(K r2) {
        return headMap(r2, false).lastEntry();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    public K lowerKey(K r2) {
        return (K) Maps.keyOrNull(lowerEntry(r2));
    }

    @Override // java.util.NavigableMap
    public ImmutableSortedSet<K> navigableKeySet() {
        return this.keySet;
    }

    @Override // java.util.NavigableMap
    public /* bridge */ /* synthetic */ NavigableSet navigableKeySet() {
        return navigableKeySet();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    @Deprecated
    public final Map.Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableMap
    @CheckForNull
    @Deprecated
    public final Map.Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public int size() {
        return this.valueList.size();
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    public ImmutableSortedMap<K, V> subMap(K r3, K r4) {
        return subMap(r3, true, r4, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    public ImmutableSortedMap<K, V> subMap(K r3, boolean r4, K r5, boolean r6) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r5);
        if (comparator().compare(r3, r5) > 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "expected fromKey <= toKey but %s > %s", r3, r5);
        return headMap(r5, r6).tailMap(r3, r4);
    L5:
        r0 = false;
        goto L6
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    public /* bridge */ /* synthetic */ NavigableMap subMap(Object r1, boolean r2, Object r3, boolean r4) {
        return subMap(r1, r2, r3, r4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap, java.util.SortedMap
    public /* bridge */ /* synthetic */ SortedMap subMap(Object r1, Object r2) {
        return subMap(r1, r2);
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    public ImmutableSortedMap<K, V> tailMap(K r2) {
        return tailMap(r2, true);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // java.util.NavigableMap
    public ImmutableSortedMap<K, V> tailMap(K r3, boolean r4) {
        return getSubMap(this.keySet.tailIndex((K) Preconditions.checkNotNull(r3), r4), size());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    public /* bridge */ /* synthetic */ NavigableMap tailMap(Object r1, boolean r2) {
        return tailMap(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap, java.util.SortedMap
    public /* bridge */ /* synthetic */ SortedMap tailMap(Object r1) {
        return tailMap(r1);
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableCollection<V> values() {
        return this.valueList;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public /* bridge */ /* synthetic */ Collection values() {
        return values();
    }

    @Override // com.google.common.collect.ImmutableMap
    Object writeReplace() {
        return new SerializedForm(this);
    }
}
