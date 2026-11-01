package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class CollectCollectors {
    private static final Collector<Object, ?, ImmutableList<Object>> TO_IMMUTABLE_LIST = null;
    private static final Collector<Range<Comparable<?>>, ?, ImmutableRangeSet<Comparable<?>>> TO_IMMUTABLE_RANGE_SET = null;
    private static final Collector<Object, ?, ImmutableSet<Object>> TO_IMMUTABLE_SET = null;

    /* JADX INFO: renamed from: com.google.common.collect.CollectCollectors$1 */
    static /* synthetic */ class C06041 {
    }

    private static class EnumMapAccumulator<K extends Enum<K>, V> {

        @CheckForNull
        private EnumMap<K, V> map;
        private final BinaryOperator<V> mergeFunction;

        EnumMapAccumulator(BinaryOperator<V> r2) {
            this.map = null;
            this.mergeFunction = r2;
        }

        EnumMapAccumulator<K, V> combine(EnumMapAccumulator<K, V> r3) {
            if (this.map != null) goto L6;
            return r3;
        L6:
            if (r3.map != null) goto L8;
            return this;
        L8:
            r3.map.forEach(new CollectCollectors$EnumMapAccumulator$$ExternalSyntheticLambda0(this));
            return this;
        }

        void put(K r3, V r4) {
            if (this.map != null) goto L5;
            this.map = new EnumMap(Collections.singletonMap(r3, r4));
            return;
        L5:
            this.map.merge(r3, r4, this.mergeFunction);
        }

        ImmutableMap<K, V> toImmutableMap() {
            if (this.map != null) goto L6;
            return ImmutableMap.m231of();
        L6:
            return ImmutableEnumMap.asImmutable(this.map);
        }
    }

    private static final class EnumSetAccumulator<E extends Enum<E>> {
        static final Collector<Enum<?>, ?, ImmutableSet<? extends Enum<?>>> TO_IMMUTABLE_ENUM_SET = null;

        @CheckForNull
        private EnumSet<E> set;

        static {
            TO_IMMUTABLE_ENUM_SET = CollectCollectors.access$000();
        }

        private EnumSetAccumulator() {
        }

        /* synthetic */ EnumSetAccumulator(C06041 r1) {
            this();
        }

        void add(E r2) {
            if (this.set != null) goto L5;
            this.set = EnumSet.of(r2);
            return;
        L5:
            this.set.add(r2);
        }

        EnumSetAccumulator<E> combine(EnumSetAccumulator<E> r3) {
            if (this.set != null) goto L6;
            return r3;
        L6:
            if (r3.set != null) goto L8;
            return this;
        L8:
            this.set.addAll(r3.set);
            return this;
        }

        ImmutableSet<E> toImmutableSet() {
            if (this.set == null) goto L5;
            ImmutableSet<E> r0 = ImmutableEnumSet.asImmutable(this.set);
            this.set = null;
            return r0;
        L5:
            return ImmutableSet.m259of();
        }
    }

    static {
        TO_IMMUTABLE_LIST = Collector.of(new CollectCollectors$$ExternalSyntheticLambda8(), new CollectCollectors$$ExternalSyntheticLambda12(), new CollectCollectors$$ExternalSyntheticLambda13(), new CollectCollectors$$ExternalSyntheticLambda14(), new Collector.Characteristics[0]);
        TO_IMMUTABLE_SET = Collector.of(new CollectCollectors$$ExternalSyntheticLambda15(), new CollectCollectors$$ExternalSyntheticLambda16(), new CollectCollectors$$ExternalSyntheticLambda17(), new CollectCollectors$$ExternalSyntheticLambda18(), new Collector.Characteristics[0]);
        TO_IMMUTABLE_RANGE_SET = Collector.of(new CollectCollectors$$ExternalSyntheticLambda19(), new CollectCollectors$$ExternalSyntheticLambda20(), new CollectCollectors$$ExternalSyntheticLambda9(), new CollectCollectors$$ExternalSyntheticLambda10(), new Collector.Characteristics[0]);
    }

    private CollectCollectors() {
    }

    static /* synthetic */ Collector access$000() {
        return toImmutableEnumSetGeneric();
    }

    static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(final Function<? super T, ? extends K> r4, final Function<? super T, ? extends Stream<? extends V>> r5) {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        Function r0 = new CollectCollectors$$ExternalSyntheticLambda24(r4);
        Function r1 = new CollectCollectors$$ExternalSyntheticLambda25(r5);
        final MultimapBuilder.ListMultimapBuilder<Object, Object> r2 = MultimapBuilder.linkedHashKeys().arrayListValues();
        Objects.requireNonNull(r2);
        return Collectors.collectingAndThen(flatteningToMultimap(r0, r1, new CollectCollectors$$ExternalSyntheticLambda26(r2)), new CollectCollectors$$ExternalSyntheticLambda27());
    }

    static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(final Function<? super T, ? extends K> r4, final Function<? super T, ? extends Stream<? extends V>> r5) {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        Function r0 = new CollectCollectors$$ExternalSyntheticLambda32(r4);
        Function r1 = new CollectCollectors$$ExternalSyntheticLambda34(r5);
        final MultimapBuilder.SetMultimapBuilder<Object, Object> r2 = MultimapBuilder.linkedHashKeys().linkedHashSetValues();
        Objects.requireNonNull(r2);
        return Collectors.collectingAndThen(flatteningToMultimap(r0, r1, new CollectCollectors$$ExternalSyntheticLambda35(r2)), new CollectCollectors$$ExternalSyntheticLambda36());
    }

    static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> flatteningToMultimap(final Function<? super T, ? extends K> r3, final Function<? super T, ? extends Stream<? extends V>> r4, Supplier<M> r5) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        return Collector.of(r5, new CollectCollectors$$ExternalSyntheticLambda21(r3, r4), new CollectCollectors$$ExternalSyntheticLambda23(), new Collector.Characteristics[0]);
    }

    static /* synthetic */ Object lambda$flatteningToImmutableListMultimap$19(Function r1, Object r2) {
        return Preconditions.checkNotNull(r1.apply(r2));
    }

    static /* synthetic */ Stream lambda$flatteningToImmutableListMultimap$20(Function r2, Object r3) {
        return ((Stream) r2.apply(r3)).peek(new CollectCollectors$$ExternalSyntheticLambda39());
    }

    static /* synthetic */ Object lambda$flatteningToImmutableSetMultimap$22(Function r1, Object r2) {
        return Preconditions.checkNotNull(r1.apply(r2));
    }

    static /* synthetic */ Stream lambda$flatteningToImmutableSetMultimap$23(Function r2, Object r3) {
        return ((Stream) r2.apply(r3)).peek(new CollectCollectors$$ExternalSyntheticLambda39());
    }

    static /* synthetic */ void lambda$flatteningToMultimap$26(Function r4, Function r5, Multimap r6, Object r7) {
        final Collection r1 = r6.get(r4.apply(r7));
        Stream r2 = (Stream) r5.apply(r7);
        Objects.requireNonNull(r1);
        r2.forEachOrdered(new CollectCollectors$$ExternalSyntheticLambda64(r1));
    }

    static /* synthetic */ Multimap lambda$flatteningToMultimap$27(Multimap r0, Multimap r1) {
        r0.putAll(r1);
        return r0;
    }

    static /* synthetic */ void lambda$toImmutableBiMap$11(Function r2, Function r3, ImmutableBiMap.Builder r4, Object r5) {
        r4.put(r2.apply(r5), r3.apply(r5));
    }

    static /* synthetic */ Object lambda$toImmutableEnumMap$12(Object r3, Object r4) {
        throw new IllegalArgumentException("Multiple values for key: " + r3 + ", " + r4);
    }

    static /* synthetic */ EnumMapAccumulator lambda$toImmutableEnumMap$13() {
        return new EnumMapAccumulator(new CollectCollectors$$ExternalSyntheticLambda0());
    }

    static /* synthetic */ void lambda$toImmutableEnumMap$14(Function r4, Function r5, EnumMapAccumulator r6, Object r7) {
        Enum r0 = (Enum) r4.apply(r7);
        Object r1 = r5.apply(r7);
        r6.put((Enum) Preconditions.checkNotNull(r0, "Null key for input %s", r7), Preconditions.checkNotNull(r1, "Null value for input %s", r7));
    }

    static /* synthetic */ EnumMapAccumulator lambda$toImmutableEnumMap$15(BinaryOperator r1) {
        return new EnumMapAccumulator(r1);
    }

    static /* synthetic */ void lambda$toImmutableEnumMap$16(Function r4, Function r5, EnumMapAccumulator r6, Object r7) {
        Enum r0 = (Enum) r4.apply(r7);
        Object r1 = r5.apply(r7);
        r6.put((Enum) Preconditions.checkNotNull(r0, "Null key for input %s", r7), Preconditions.checkNotNull(r1, "Null value for input %s", r7));
    }

    static /* synthetic */ EnumSetAccumulator lambda$toImmutableEnumSetGeneric$1() {
        return new EnumSetAccumulator(null);
    }

    static /* synthetic */ void lambda$toImmutableListMultimap$18(Function r2, Function r3, ImmutableListMultimap.Builder r4, Object r5) {
        r4.put(r2.apply(r5), r3.apply(r5));
    }

    static /* synthetic */ void lambda$toImmutableMap$7(Function r2, Function r3, ImmutableMap.Builder r4, Object r5) {
        r4.put(r2.apply(r5), r3.apply(r5));
    }

    static /* synthetic */ void lambda$toImmutableMultiset$2(Function r2, ToIntFunction r3, Multiset r4, Object r5) {
        r4.add(Preconditions.checkNotNull(r2.apply(r5)), r3.applyAsInt(r5));
    }

    static /* synthetic */ Multiset lambda$toImmutableMultiset$3(Multiset r0, Multiset r1) {
        r0.addAll(r1);
        return r0;
    }

    static /* synthetic */ ImmutableMultiset lambda$toImmutableMultiset$4(Multiset r1) {
        return ImmutableMultiset.copyFromEntries(r1.entrySet());
    }

    static /* synthetic */ void lambda$toImmutableRangeMap$17(Function r2, Function r3, ImmutableRangeMap.Builder r4, Object r5) {
        r4.put((Range) r2.apply(r5), r3.apply(r5));
    }

    static /* synthetic */ void lambda$toImmutableSetMultimap$21(Function r2, Function r3, ImmutableSetMultimap.Builder r4, Object r5) {
        r4.put(r2.apply(r5), r3.apply(r5));
    }

    static /* synthetic */ TreeMap lambda$toImmutableSortedMap$10(Comparator r1) {
        return new TreeMap(r1);
    }

    static /* synthetic */ ImmutableSortedMap.Builder lambda$toImmutableSortedMap$8(Comparator r1) {
        return new ImmutableSortedMap.Builder(r1);
    }

    static /* synthetic */ void lambda$toImmutableSortedMap$9(Function r2, Function r3, ImmutableSortedMap.Builder r4, Object r5) {
        r4.put(r2.apply(r5), r3.apply(r5));
    }

    static /* synthetic */ ImmutableSortedSet.Builder lambda$toImmutableSortedSet$0(Comparator r1) {
        return new ImmutableSortedSet.Builder(r1);
    }

    static /* synthetic */ void lambda$toMultimap$24(Function r2, Function r3, Multimap r4, Object r5) {
        r4.put(r2.apply(r5), r3.apply(r5));
    }

    static /* synthetic */ Multimap lambda$toMultimap$25(Multimap r0, Multimap r1) {
        r0.putAll(r1);
        return r0;
    }

    static /* synthetic */ void lambda$toMultiset$5(Function r2, ToIntFunction r3, Multiset r4, Object r5) {
        r4.add(r2.apply(r5), r3.applyAsInt(r5));
    }

    static /* synthetic */ Multiset lambda$toMultiset$6(Multiset r0, Multiset r1) {
        r0.addAll(r1);
        return r0;
    }

    static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(final Function<? super T, ? extends K> r5, final Function<? super T, ? extends V> r6) {
        Preconditions.checkNotNull(r5);
        Preconditions.checkNotNull(r6);
        return Collector.of(new CollectCollectors$$ExternalSyntheticLambda74(), new CollectCollectors$$ExternalSyntheticLambda1(r5, r6), new CollectCollectors$$ExternalSyntheticLambda2(), new CollectCollectors$$ExternalSyntheticLambda3(), new Collector.Characteristics[0]);
    }

    static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(final Function<? super T, ? extends K> r7, final Function<? super T, ? extends V> r8) {
        Preconditions.checkNotNull(r7);
        Preconditions.checkNotNull(r8);
        return Collector.of(new CollectCollectors$$ExternalSyntheticLambda40(), new CollectCollectors$$ExternalSyntheticLambda41(r7, r8), new CollectCollectors$$ExternalSyntheticLambda42(), new CollectCollectors$$ExternalSyntheticLambda43(), new Collector.Characteristics[]{Collector.Characteristics.UNORDERED});
    }

    static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(final Function<? super T, ? extends K> r5, final Function<? super T, ? extends V> r6, final BinaryOperator<V> r7) {
        Preconditions.checkNotNull(r5);
        Preconditions.checkNotNull(r6);
        Preconditions.checkNotNull(r7);
        return Collector.of(new CollectCollectors$$ExternalSyntheticLambda68(r7), new CollectCollectors$$ExternalSyntheticLambda69(r5, r6), new CollectCollectors$$ExternalSyntheticLambda42(), new CollectCollectors$$ExternalSyntheticLambda43(), new Collector.Characteristics[0]);
    }

    static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
        return (Collector<E, ?, ImmutableSet<E>>) EnumSetAccumulator.TO_IMMUTABLE_ENUM_SET;
    }

    private static <E extends Enum<E>> Collector<E, EnumSetAccumulator<E>, ImmutableSet<E>> toImmutableEnumSetGeneric() {
        return Collector.of(new CollectCollectors$$ExternalSyntheticLambda4(), new CollectCollectors$$ExternalSyntheticLambda5(), new CollectCollectors$$ExternalSyntheticLambda6(), new CollectCollectors$$ExternalSyntheticLambda7(), new Collector.Characteristics[]{Collector.Characteristics.UNORDERED});
    }

    static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
        return (Collector<E, ?, ImmutableList<E>>) TO_IMMUTABLE_LIST;
    }

    static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(final Function<? super T, ? extends K> r5, final Function<? super T, ? extends V> r6) {
        Preconditions.checkNotNull(r5, "keyFunction");
        Preconditions.checkNotNull(r6, "valueFunction");
        return Collector.of(new CollectCollectors$$ExternalSyntheticLambda53(), new CollectCollectors$$ExternalSyntheticLambda54(r5, r6), new CollectCollectors$$ExternalSyntheticLambda56(), new CollectCollectors$$ExternalSyntheticLambda57(), new Collector.Characteristics[0]);
    }

    static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> r5, final Function<? super T, ? extends V> r6) {
        Preconditions.checkNotNull(r5);
        Preconditions.checkNotNull(r6);
        return Collector.of(new CollectCollectors$$ExternalSyntheticLambda45(), new CollectCollectors$$ExternalSyntheticLambda46(r5, r6), new CollectCollectors$$ExternalSyntheticLambda47(), new CollectCollectors$$ExternalSyntheticLambda48(), new Collector.Characteristics[0]);
    }

    static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> r2, Function<? super T, ? extends V> r3, BinaryOperator<V> r4) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        return Collectors.collectingAndThen(Collectors.toMap(r2, r3, r4, new CollectCollectors$$ExternalSyntheticLambda62()), new CollectCollectors$$ExternalSyntheticLambda63());
    }

    static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(final Function<? super T, ? extends E> r5, final ToIntFunction<? super T> r6) {
        Preconditions.checkNotNull(r5);
        Preconditions.checkNotNull(r6);
        return Collector.of(new CollectCollectors$$ExternalSyntheticLambda28(), new CollectCollectors$$ExternalSyntheticLambda29(r5, r6), new CollectCollectors$$ExternalSyntheticLambda30(), new CollectCollectors$$ExternalSyntheticLambda31(), new Collector.Characteristics[0]);
    }

    static <T, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(final Function<? super T, Range<K>> r5, final Function<? super T, ? extends V> r6) {
        Preconditions.checkNotNull(r5);
        Preconditions.checkNotNull(r6);
        return Collector.of(new CollectCollectors$$ExternalSyntheticLambda11(), new CollectCollectors$$ExternalSyntheticLambda22(r5, r6), new CollectCollectors$$ExternalSyntheticLambda33(), new CollectCollectors$$ExternalSyntheticLambda44(), new Collector.Characteristics[0]);
    }

    static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
        return (Collector<Range<E>, ?, ImmutableRangeSet<E>>) TO_IMMUTABLE_RANGE_SET;
    }

    static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
        return (Collector<E, ?, ImmutableSet<E>>) TO_IMMUTABLE_SET;
    }

    static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(final Function<? super T, ? extends K> r5, final Function<? super T, ? extends V> r6) {
        Preconditions.checkNotNull(r5, "keyFunction");
        Preconditions.checkNotNull(r6, "valueFunction");
        return Collector.of(new CollectCollectors$$ExternalSyntheticLambda58(), new CollectCollectors$$ExternalSyntheticLambda59(r5, r6), new CollectCollectors$$ExternalSyntheticLambda60(), new CollectCollectors$$ExternalSyntheticLambda61(), new Collector.Characteristics[0]);
    }

    static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(final Comparator<? super K> r7, final Function<? super T, ? extends K> r8, final Function<? super T, ? extends V> r9) {
        Preconditions.checkNotNull(r7);
        Preconditions.checkNotNull(r8);
        Preconditions.checkNotNull(r9);
        return Collector.of(new CollectCollectors$$ExternalSyntheticLambda55(r7), new CollectCollectors$$ExternalSyntheticLambda66(r8, r9), new CollectCollectors$$ExternalSyntheticLambda72(), new CollectCollectors$$ExternalSyntheticLambda73(), new Collector.Characteristics[]{Collector.Characteristics.UNORDERED});
    }

    static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(final Comparator<? super K> r2, Function<? super T, ? extends K> r3, Function<? super T, ? extends V> r4, BinaryOperator<V> r5) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        return Collectors.collectingAndThen(Collectors.toMap(r3, r4, r5, new CollectCollectors$$ExternalSyntheticLambda37(r2)), new CollectCollectors$$ExternalSyntheticLambda38());
    }

    static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(final Comparator<? super E> r5) {
        Preconditions.checkNotNull(r5);
        return Collector.of(new CollectCollectors$$ExternalSyntheticLambda49(r5), new CollectCollectors$$ExternalSyntheticLambda50(), new CollectCollectors$$ExternalSyntheticLambda51(), new CollectCollectors$$ExternalSyntheticLambda52(), new Collector.Characteristics[0]);
    }

    static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> toMultimap(final Function<? super T, ? extends K> r3, final Function<? super T, ? extends V> r4, Supplier<M> r5) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        return Collector.of(r5, new CollectCollectors$$ExternalSyntheticLambda70(r3, r4), new CollectCollectors$$ExternalSyntheticLambda71(), new Collector.Characteristics[0]);
    }

    static <T, E, M extends Multiset<E>> Collector<T, ?, M> toMultiset(final Function<? super T, E> r3, final ToIntFunction<? super T> r4, Supplier<M> r5) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        return Collector.of(r5, new CollectCollectors$$ExternalSyntheticLambda65(r3, r4), new CollectCollectors$$ExternalSyntheticLambda67(), new Collector.Characteristics[0]);
    }
}
