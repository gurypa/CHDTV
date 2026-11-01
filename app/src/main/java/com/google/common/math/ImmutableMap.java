package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.DoNotMock;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use ImmutableMap.of or another implementation")
@ElementTypesAreNonnullByDefault
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
    static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY = null;
    private static final long serialVersionUID = 912559;

    @CheckForNull
    @LazyInit
    private transient ImmutableSet<Map.Entry<K, V>> entrySet;

    @CheckForNull
    @LazyInit
    private transient ImmutableSet<K> keySet;

    @CheckForNull
    @LazyInit
    private transient ImmutableSetMultimap<K, V> multimapView;

    @CheckForNull
    @LazyInit
    private transient ImmutableCollection<V> values;

    @DoNotMock
    public static class Builder<K, V> {
        Object[] alternatingKeysAndValues;
        DuplicateKey duplicateKey;
        boolean entriesUsed;
        int size;

        @CheckForNull
        Comparator<? super V> valueComparator;

        static final class DuplicateKey {
            private final Object key;
            private final Object value1;
            private final Object value2;

            DuplicateKey(Object r1, Object r2, Object r3) {
                this.key = r1;
                this.value1 = r2;
                this.value2 = r3;
            }

            IllegalArgumentException exception() {
                return new IllegalArgumentException("Multiple entries with same key: " + this.key + "=" + this.value1 + " and " + this.key + "=" + this.value2);
            }
        }

        public Builder() {
            this(4);
        }

        Builder(int r2) {
            this.alternatingKeysAndValues = new Object[r2 * 2];
            this.size = 0;
            this.entriesUsed = false;
        }

        private ImmutableMap<K, V> build(boolean r6) {
            if (r6 == true) goto L4;
        L8:
            int r0 = this.size;
            if (this.valueComparator != null) goto L12;
            Object[] r1 = this.alternatingKeysAndValues;
        L20:
            this.entriesUsed = true;
            RegularImmutableMap r2 = RegularImmutableMap.create(r0, r1, this);
            if (r6 == true) goto L23;
        L27:
            return r2;
        L23:
            if (this.duplicateKey == null) goto L27;
            throw this.duplicateKey.exception();
        L12:
            if (this.entriesUsed == false) goto L14;
            this.alternatingKeysAndValues = Arrays.copyOf(this.alternatingKeysAndValues, this.size * 2);
        L14:
            r1 = this.alternatingKeysAndValues;
            if (r6 == true) goto L19;
            r1 = lastEntryForEachKey(r1, this.size);
            if (r1.length >= this.alternatingKeysAndValues.length) goto L19;
            r0 = r1.length >>> 1;
        L19:
            sortEntries(r1, r0, this.valueComparator);
            goto L20
        L4:
            if (this.duplicateKey == null) goto L8;
            throw this.duplicateKey.exception();
        }

        private void ensureCapacity(int r4) {
            if ((r4 * 2) <= this.alternatingKeysAndValues.length) goto L6;
            this.alternatingKeysAndValues = Arrays.copyOf(this.alternatingKeysAndValues, ImmutableCollection.Builder.expandedCapacity(this.alternatingKeysAndValues.length, r4 * 2));
            this.entriesUsed = false;
            return;
        }

        private Object[] lastEntryForEachKey(Object[] r8, int r9) {
            HashSet r0 = new HashSet();
            BitSet r1 = new BitSet();
            int r2 = r9 - 1;
        L3:
            if (r2 < 0) goto L9;
            if (r0.add(Objects.requireNonNull(r8[r2 * 2])) == true) goto L7;
            r1.set(r2);
        L7:
            r2 = r2 - 1;
            goto L3
        L9:
            if (r1.isEmpty() == false) goto L11;
            return r8;
        L11:
            Object[] r3 = new Object[(r9 - r1.cardinality()) * 2];
            int r4 = 0;
            int r5 = 0;
        L13:
            if (r4 >= (r9 * 2)) goto L18;
            if (r1.get(r4 >>> 1) == true) goto L16;
            int r6 = r5 + 1;
            int r7 = r4 + 1;
            r3[r5] = Objects.requireNonNull(r8[r4]);
            r5 = r6 + 1;
            r4 = r7 + 1;
            r3[r6] = Objects.requireNonNull(r8[r7]);
            goto L13
        L16:
            r4 = r4 + 2;
            goto L13
        L18:
            return r3;
        }

        static <V> void sortEntries(Object[] r5, int r6, Comparator<? super V> r7) {
            Map.Entry[] r0 = new Map.Entry[r6];
            int r1 = 0;
        L3:
            if (r1 >= r6) goto L5;
            r0[r1] = new AbstractMap.SimpleImmutableEntry(Objects.requireNonNull(r5[r1 * 2]), Objects.requireNonNull(r5[(r1 * 2) + 1]));
            r1 = r1 + 1;
            goto L3
        L5:
            Arrays.sort(r0, 0, r6, Ordering.from(r7).onResultOf(Maps.valueFunction()));
            int r2 = 0;
        L6:
            if (r2 >= r6) goto L8;
            r5[r2 * 2] = r0[r2].getKey();
            r5[(r2 * 2) + 1] = r0[r2].getValue();
            r2 = r2 + 1;
            goto L6
        }

        public ImmutableMap<K, V> build() {
            return buildOrThrow();
        }

        public ImmutableMap<K, V> buildKeepingLast() {
            return build(false);
        }

        public ImmutableMap<K, V> buildOrThrow() {
            return build(true);
        }

        Builder<K, V> combine(Builder<K, V> r6) {
            Preconditions.checkNotNull(r6);
            ensureCapacity(this.size + r6.size);
            System.arraycopy(r6.alternatingKeysAndValues, 0, this.alternatingKeysAndValues, this.size * 2, r6.size * 2);
            this.size += r6.size;
            return this;
        }

        public Builder<K, V> orderEntriesByValue(Comparator<? super V> r3) {
            if (this.valueComparator != null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkState(r0, "valueComparator was already set");
            this.valueComparator = (Comparator) Preconditions.checkNotNull(r3, "valueComparator");
            return this;
        L5:
            r0 = false;
            goto L6
        }

        public Builder<K, V> put(K r3, V r4) {
            ensureCapacity(this.size + 1);
            CollectPreconditions.checkEntryNotNull(r3, r4);
            this.alternatingKeysAndValues[this.size * 2] = r3;
            this.alternatingKeysAndValues[(this.size * 2) + 1] = r4;
            this.size++;
            return this;
        }

        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> r3) {
            return put(r3.getKey(), r3.getValue());
        }

        public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> r3) {
            if ((r3 instanceof Collection) == false) goto L5;
            ensureCapacity(this.size + ((Collection) r3).size());
        L5:
            Iterator<? extends Map.Entry<? extends K, ? extends V>> r0 = r3.iterator();
        L7:
            if (r0.hasNext() == false) goto L9;
            put(r0.next());
            goto L7
        L9:
            return this;
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> r2) {
            return putAll(r2.entrySet());
        }
    }

    static abstract class IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V> {
        IteratorBasedImmutableMap() {
        }

        @Override // com.google.common.collect.ImmutableMap
        ImmutableSet<Map.Entry<K, V>> createEntrySet() {
            return new C1EntrySetImpl(this);
        }

        @Override // com.google.common.collect.ImmutableMap
        ImmutableSet<K> createKeySet() {
            return new ImmutableMapKeySet(this);
        }

        @Override // com.google.common.collect.ImmutableMap
        ImmutableCollection<V> createValues() {
            return new ImmutableMapValues(this);
        }

        abstract UnmodifiableIterator<Map.Entry<K, V>> entryIterator();

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public /* bridge */ /* synthetic */ Set entrySet() {
            return super.entrySet();
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public /* bridge */ /* synthetic */ Set keySet() {
            return super.keySet();
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public /* bridge */ /* synthetic */ Collection values() {
            return super.values();
        }

        @Override // com.google.common.collect.ImmutableMap
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    private final class MapViewOfValuesAsSingletonSets extends IteratorBasedImmutableMap<K, ImmutableSet<V>> {
        final /* synthetic */ ImmutableMap this$0;

        private MapViewOfValuesAsSingletonSets(ImmutableMap r1) {
            this.this$0 = r1;
        }

        /* synthetic */ MapViewOfValuesAsSingletonSets(ImmutableMap r1, C06301 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            return this.this$0.containsKey(r2);
        }

        @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap, com.google.common.collect.ImmutableMap
        ImmutableSet<K> createKeySet() {
            return this.this$0.keySet();
        }

        @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap
        UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>> entryIterator() {
            final UnmodifiableIterator<Map.Entry<K, V>> r0 = this.this$0.entrySet().iterator();
            return new C06311(this, r0);
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        @CheckForNull
        public ImmutableSet<V> get(@CheckForNull Object r3) {
            Object r0 = this.this$0.get(r3);
            if (r0 != null) goto L6;
            return null;
        L6:
            return ImmutableSet.m260of(r0);
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object get(@CheckForNull Object r1) {
            return get(r1);
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public int hashCode() {
            return this.this$0.hashCode();
        }

        @Override // com.google.common.collect.ImmutableMap
        boolean isHashCodeFast() {
            return this.this$0.isHashCodeFast();
        }

        @Override // com.google.common.collect.ImmutableMap
        boolean isPartialView() {
            return this.this$0.isPartialView();
        }

        @Override // java.util.Map
        public int size() {
            return this.this$0.size();
        }

        @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap, com.google.common.collect.ImmutableMap
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    static class SerializedForm<K, V> implements Serializable {
        private static final boolean USE_LEGACY_SERIALIZATION = true;
        private static final long serialVersionUID = 0;
        private final Object keys;
        private final Object values;

        SerializedForm(ImmutableMap<K, V> r7) {
            Object[] r0 = new Object[r7.size()];
            Object[] r1 = new Object[r7.size()];
            int r2 = 0;
            UnmodifiableIterator<Map.Entry<K, V>> r3 = r7.entrySet().iterator();
        L4:
            if (r3.hasNext() == false) goto L6;
            Map.Entry<K, V> r4 = r3.next();
            r0[r2] = r4.getKey();
            r1[r2] = r4.getValue();
            r2 = r2 + 1;
            goto L4
        L6:
            this.keys = r0;
            this.values = r1;
        }

        /* JADX WARN: Multi-variable type inference failed */
        final Object legacyReadResolve() {
            Object[] r0 = (Object[]) this.keys;
            Object[] r1 = (Object[]) this.values;
            Builder<K, V> r2 = makeBuilder(r0.length);
            int r3 = 0;
        L4:
            if (r3 >= r0.length) goto L7;
            r2.put(r0[r3], r1[r3]);
            r3 = r3 + 1;
            goto L4
        L7:
            return r2.buildOrThrow();
        }

        Builder<K, V> makeBuilder(int r2) {
            return new Builder(r2);
        }

        final Object readResolve() {
            if ((this.keys instanceof ImmutableSet) == false) goto L5;
            ImmutableSet r0 = (ImmutableSet) this.keys;
            ImmutableCollection r1 = (ImmutableCollection) this.values;
            Builder<K, V> r2 = makeBuilder(r0.size());
            UnmodifiableIterator r3 = r0.iterator();
            UnmodifiableIterator r4 = r1.iterator();
        L8:
            if (r3.hasNext() == false) goto L11;
            r2.put(r3.next(), r4.next());
            goto L8
        L11:
            return r2.buildOrThrow();
        L5:
            return legacyReadResolve();
        }
    }

    static {
        EMPTY_ENTRY_ARRAY = new Map.Entry[0];
    }

    ImmutableMap() {
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> Builder<K, V> builderWithExpectedSize(int r1) {
        CollectPreconditions.checkNonnegative(r1, "expectedSize");
        return new Builder(r1);
    }

    static void checkNoConflict(boolean r1, String r2, Object r3, Object r4) {
        if (r1 == false) goto L5;
        return;
    L5:
        throw conflictException(r2, r3, r4);
    }

    static IllegalArgumentException conflictException(String r3, Object r4, Object r5) {
        return new IllegalArgumentException("Multiple entries with same " + r3 + ": " + r4 + " and " + r5);
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> r3) {
        if ((r3 instanceof Collection) == false) goto L5;
        int r0 = ((Collection) r3).size();
    L6:
        Builder r1 = new Builder(r0);
        r1.putAll(r3);
        return r1.build();
    L5:
        r0 = 4;
        goto L6
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> r2) {
        if ((r2 instanceof ImmutableMap) == false) goto L10;
        if ((r2 instanceof SortedMap) == true) goto L10;
        ImmutableMap<K, V> r0 = (ImmutableMap) r2;
        if (r0.isPartialView() == true) goto L10;
        return r0;
    L10:
        return copyOf(r2.entrySet());
    }

    static <K, V> Map.Entry<K, V> entryOf(K r1, V r2) {
        CollectPreconditions.checkEntryNotNull(r1, r2);
        return new AbstractMap.SimpleImmutableEntry(r1, r2);
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMap<K, V> m231of() {
        return (ImmutableMap<K, V>) RegularImmutableMap.EMPTY;
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMap<K, V> m232of(K r2, V r3) {
        CollectPreconditions.checkEntryNotNull(r2, r3);
        return RegularImmutableMap.create(1, new Object[]{r2, r3});
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMap<K, V> m233of(K r3, V r4, K r5, V r6) {
        CollectPreconditions.checkEntryNotNull(r3, r4);
        CollectPreconditions.checkEntryNotNull(r5, r6);
        return RegularImmutableMap.create(2, new Object[]{r3, r4, r5, r6});
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMap<K, V> m234of(K r3, V r4, K r5, V r6, K r7, V r8) {
        CollectPreconditions.checkEntryNotNull(r3, r4);
        CollectPreconditions.checkEntryNotNull(r5, r6);
        CollectPreconditions.checkEntryNotNull(r7, r8);
        return RegularImmutableMap.create(3, new Object[]{r3, r4, r5, r6, r7, r8});
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMap<K, V> m235of(K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10) {
        CollectPreconditions.checkEntryNotNull(r3, r4);
        CollectPreconditions.checkEntryNotNull(r5, r6);
        CollectPreconditions.checkEntryNotNull(r7, r8);
        CollectPreconditions.checkEntryNotNull(r9, r10);
        return RegularImmutableMap.create(4, new Object[]{r3, r4, r5, r6, r7, r8, r9, r10});
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMap<K, V> m236of(K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10, K r11, V r12) {
        CollectPreconditions.checkEntryNotNull(r3, r4);
        CollectPreconditions.checkEntryNotNull(r5, r6);
        CollectPreconditions.checkEntryNotNull(r7, r8);
        CollectPreconditions.checkEntryNotNull(r9, r10);
        CollectPreconditions.checkEntryNotNull(r11, r12);
        return RegularImmutableMap.create(5, new Object[]{r3, r4, r5, r6, r7, r8, r9, r10, r11, r12});
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMap<K, V> m237of(K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10, K r11, V r12, K r13, V r14) {
        CollectPreconditions.checkEntryNotNull(r3, r4);
        CollectPreconditions.checkEntryNotNull(r5, r6);
        CollectPreconditions.checkEntryNotNull(r7, r8);
        CollectPreconditions.checkEntryNotNull(r9, r10);
        CollectPreconditions.checkEntryNotNull(r11, r12);
        CollectPreconditions.checkEntryNotNull(r13, r14);
        return RegularImmutableMap.create(6, new Object[]{r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14});
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMap<K, V> m238of(K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10, K r11, V r12, K r13, V r14, K r15, V r16) {
        CollectPreconditions.checkEntryNotNull(r3, r4);
        CollectPreconditions.checkEntryNotNull(r5, r6);
        CollectPreconditions.checkEntryNotNull(r7, r8);
        CollectPreconditions.checkEntryNotNull(r9, r10);
        CollectPreconditions.checkEntryNotNull(r11, r12);
        CollectPreconditions.checkEntryNotNull(r13, r14);
        CollectPreconditions.checkEntryNotNull(r15, r16);
        return RegularImmutableMap.create(7, new Object[]{r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16});
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMap<K, V> m239of(K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10, K r11, V r12, K r13, V r14, K r15, V r16, K r17, V r18) {
        CollectPreconditions.checkEntryNotNull(r3, r4);
        CollectPreconditions.checkEntryNotNull(r5, r6);
        CollectPreconditions.checkEntryNotNull(r7, r8);
        CollectPreconditions.checkEntryNotNull(r9, r10);
        CollectPreconditions.checkEntryNotNull(r11, r12);
        CollectPreconditions.checkEntryNotNull(r13, r14);
        CollectPreconditions.checkEntryNotNull(r15, r16);
        CollectPreconditions.checkEntryNotNull(r17, r18);
        return RegularImmutableMap.create(8, new Object[]{r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18});
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMap<K, V> m240of(K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10, K r11, V r12, K r13, V r14, K r15, V r16, K r17, V r18, K r19, V r20) {
        CollectPreconditions.checkEntryNotNull(r3, r4);
        CollectPreconditions.checkEntryNotNull(r5, r6);
        CollectPreconditions.checkEntryNotNull(r7, r8);
        CollectPreconditions.checkEntryNotNull(r9, r10);
        CollectPreconditions.checkEntryNotNull(r11, r12);
        CollectPreconditions.checkEntryNotNull(r13, r14);
        CollectPreconditions.checkEntryNotNull(r15, r16);
        CollectPreconditions.checkEntryNotNull(r17, r18);
        CollectPreconditions.checkEntryNotNull(r19, r20);
        return RegularImmutableMap.create(9, new Object[]{r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20});
    }

    /* JADX INFO: renamed from: of */
    public static <K, V> ImmutableMap<K, V> m241of(K r3, V r4, K r5, V r6, K r7, V r8, K r9, V r10, K r11, V r12, K r13, V r14, K r15, V r16, K r17, V r18, K r19, V r20, K r21, V r22) {
        CollectPreconditions.checkEntryNotNull(r3, r4);
        CollectPreconditions.checkEntryNotNull(r5, r6);
        CollectPreconditions.checkEntryNotNull(r7, r8);
        CollectPreconditions.checkEntryNotNull(r9, r10);
        CollectPreconditions.checkEntryNotNull(r11, r12);
        CollectPreconditions.checkEntryNotNull(r13, r14);
        CollectPreconditions.checkEntryNotNull(r15, r16);
        CollectPreconditions.checkEntryNotNull(r17, r18);
        CollectPreconditions.checkEntryNotNull(r19, r20);
        CollectPreconditions.checkEntryNotNull(r21, r22);
        return RegularImmutableMap.create(10, new Object[]{r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22});
    }

    @SafeVarargs
    public static <K, V> ImmutableMap<K, V> ofEntries(Map.Entry<? extends K, ? extends V>... r2) {
        return copyOf(Arrays.asList(r2));
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> r1, Function<? super T, ? extends V> r2) {
        return CollectCollectors.toImmutableMap(r1, r2);
    }

    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> r1, Function<? super T, ? extends V> r2, BinaryOperator<V> r3) {
        return CollectCollectors.toImmutableMap(r1, r2, r3);
    }

    public ImmutableSetMultimap<K, V> asMultimap() {
        if (isEmpty() == true) goto L5;
        ImmutableSetMultimap<K, V> r0 = this.multimapView;
        if (r0 != null) goto L10;
        ImmutableSetMultimap<K, V> r1 = new ImmutableSetMultimap(new MapViewOfValuesAsSingletonSets(this, null), size(), null);
        this.multimapView = r1;
        return r1;
    L10:
        return r0;
    L5:
        return ImmutableSetMultimap.m266of();
    }

    @Override // java.util.Map
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public boolean containsKey(@CheckForNull Object r2) {
        if (get(r2) == null) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.Map
    public boolean containsValue(@CheckForNull Object r2) {
        return values().contains(r2);
    }

    abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();

    abstract ImmutableSet<K> createKeySet();

    abstract ImmutableCollection<V> createValues();

    @Override // java.util.Map
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        ImmutableSet<Map.Entry<K, V>> r0 = this.entrySet;
        if (r0 != null) goto L6;
        ImmutableSet<Map.Entry<K, V>> r1 = createEntrySet();
        this.entrySet = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // java.util.Map
    public /* bridge */ /* synthetic */ Set entrySet() {
        return entrySet();
    }

    @Override // java.util.Map
    public boolean equals(@CheckForNull Object r2) {
        return Maps.equalsImpl(this, r2);
    }

    @Override // java.util.Map
    @CheckForNull
    public abstract V get(@CheckForNull Object r1);

    @Override // java.util.Map
    @CheckForNull
    public final V getOrDefault(@CheckForNull Object r2, @CheckForNull V r3) {
        V r0 = get(r2);
        if (r0 == null) goto L5;
        return r0;
    L5:
        return r3;
    }

    @Override // java.util.Map
    public int hashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        if (size() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    boolean isHashCodeFast() {
        return false;
    }

    abstract boolean isPartialView();

    UnmodifiableIterator<K> keyIterator() {
        final UnmodifiableIterator<Map.Entry<K, V>> r0 = entrySet().iterator();
        return new C06301(this, r0);
    }

    @Override // java.util.Map
    public ImmutableSet<K> keySet() {
        ImmutableSet<K> r0 = this.keySet;
        if (r0 != null) goto L6;
        ImmutableSet<K> r1 = createKeySet();
        this.keySet = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // java.util.Map
    public /* bridge */ /* synthetic */ Set keySet() {
        return keySet();
    }

    @Override // java.util.Map
    @CheckForNull
    @Deprecated
    public final V put(K r2, V r3) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @Deprecated
    public final void putAll(Map<? extends K, ? extends V> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @CheckForNull
    @Deprecated
    public final V remove(@CheckForNull Object r2) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return Maps.toStringImpl(this);
    }

    @Override // java.util.Map
    public ImmutableCollection<V> values() {
        ImmutableCollection<V> r0 = this.values;
        if (r0 != null) goto L6;
        ImmutableCollection<V> r1 = createValues();
        this.values = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // java.util.Map
    public /* bridge */ /* synthetic */ Collection values() {
        return values();
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
