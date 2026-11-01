package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class FluentIterable<E> implements Iterable<E> {
    private final Optional<Iterable<E>> iterableDelegate;

    private static class FromIterableFunction<E> implements Function<Iterable<E>, FluentIterable<E>> {
        private FromIterableFunction() {
        }

        public FluentIterable<E> apply(Iterable<E> r2) {
            return FluentIterable.from(r2);
        }

        @Override // com.google.common.base.Function
        public /* bridge */ /* synthetic */ Object apply(Object r1) {
            return apply((Iterable) r1);
        }
    }

    protected FluentIterable() {
        this.iterableDelegate = Optional.absent();
    }

    FluentIterable(Iterable<E> r2) {
        this.iterableDelegate = Optional.m185of(r2);
    }

    public static <T> FluentIterable<T> concat(final Iterable<? extends Iterable<? extends T>> r1) {
        Preconditions.checkNotNull(r1);
        return new C06232(r1);
    }

    public static <T> FluentIterable<T> concat(Iterable<? extends T> r2, Iterable<? extends T> r3) {
        return concatNoDefensiveCopy(new Iterable[]{r2, r3});
    }

    public static <T> FluentIterable<T> concat(Iterable<? extends T> r2, Iterable<? extends T> r3, Iterable<? extends T> r4) {
        return concatNoDefensiveCopy(new Iterable[]{r2, r3, r4});
    }

    public static <T> FluentIterable<T> concat(Iterable<? extends T> r2, Iterable<? extends T> r3, Iterable<? extends T> r4, Iterable<? extends T> r5) {
        return concatNoDefensiveCopy(new Iterable[]{r2, r3, r4, r5});
    }

    @SafeVarargs
    public static <T> FluentIterable<T> concat(Iterable<? extends T>... r1) {
        return concatNoDefensiveCopy((Iterable[]) Arrays.copyOf(r1, r1.length));
    }

    private static <T> FluentIterable<T> concatNoDefensiveCopy(final Iterable<? extends T>... r3) {
        int r0 = r3.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L6;
        Preconditions.checkNotNull(r3[r1]);
        r1 = r1 + 1;
        goto L3
    L6:
        return new C06243(r3);
    }

    @Deprecated
    public static <E> FluentIterable<E> from(FluentIterable<E> r1) {
        return (FluentIterable) Preconditions.checkNotNull(r1);
    }

    public static <E> FluentIterable<E> from(final Iterable<E> r1) {
        if ((r1 instanceof FluentIterable) == false) goto L6;
        return (FluentIterable) r1;
    L6:
        return new C06221(r1, r1);
    }

    public static <E> FluentIterable<E> from(E[] r1) {
        return from(Arrays.asList(r1));
    }

    private Iterable<E> getDelegate() {
        return this.iterableDelegate.mo179or(this);
    }

    /* JADX INFO: renamed from: of */
    public static <E> FluentIterable<E> m197of() {
        return from(Collections.emptyList());
    }

    /* JADX INFO: renamed from: of */
    public static <E> FluentIterable<E> m198of(@ParametricNullness E r1, E... r2) {
        return from(Lists.asList(r1, r2));
    }

    public final boolean allMatch(Predicate<? super E> r2) {
        return Iterables.all(getDelegate(), r2);
    }

    public final boolean anyMatch(Predicate<? super E> r2) {
        return Iterables.any(getDelegate(), r2);
    }

    public final FluentIterable<E> append(Iterable<? extends E> r2) {
        return concat(getDelegate(), r2);
    }

    public final FluentIterable<E> append(E... r3) {
        return concat(getDelegate(), Arrays.asList(r3));
    }

    public final boolean contains(@CheckForNull Object r2) {
        return Iterables.contains(getDelegate(), r2);
    }

    public final <C extends Collection<? super E>> C copyInto(C r4) {
        Preconditions.checkNotNull(r4);
        Iterable<E> r0 = getDelegate();
        if ((r0 instanceof Collection) == false) goto L5;
        r4.addAll((Collection) r0);
    L9:
        return r4;
    L5:
        Iterator<E> r1 = r0.iterator();
    L7:
        if (r1.hasNext() == false) goto L9;
        r4.add(r1.next());
        goto L7
    }

    public final FluentIterable<E> cycle() {
        return from(Iterables.cycle(getDelegate()));
    }

    public final FluentIterable<E> filter(Predicate<? super E> r2) {
        return from(Iterables.filter(getDelegate(), r2));
    }

    public final <T> FluentIterable<T> filter(Class<T> r2) {
        return from(Iterables.filter(getDelegate(), r2));
    }

    public final Optional<E> first() {
        Iterator<E> r0 = getDelegate().iterator();
        if (r0.hasNext() == false) goto L6;
        return Optional.m185of(r0.next());
    L6:
        return Optional.absent();
    }

    public final Optional<E> firstMatch(Predicate<? super E> r2) {
        return Iterables.tryFind(getDelegate(), r2);
    }

    @ParametricNullness
    public final E get(int r2) {
        return (E) Iterables.get(getDelegate(), r2);
    }

    public final <K> ImmutableListMultimap<K, E> index(Function<? super E, K> r2) {
        return Multimaps.index(getDelegate(), r2);
    }

    public final boolean isEmpty() {
        return !getDelegate().iterator().hasNext();
    }

    public final String join(Joiner r2) {
        return r2.join(this);
    }

    public final Optional<E> last() {
        Iterable<E> r0 = getDelegate();
        if ((r0 instanceof List) == false) goto L10;
        List r1 = (List) r0;
        if (r1.isEmpty() == false) goto L9;
        return Optional.absent();
    L9:
        return Optional.m185of(r1.get(r1.size() - 1));
    L10:
        Iterator<E> r2 = r0.iterator();
        if (r2.hasNext() == true) goto L15;
        return Optional.absent();
    L15:
        if ((r0 instanceof SortedSet) == true) goto L17;
    L18:
        E r3 = r2.next();
        if (r2.hasNext() == true) goto L18;
        return Optional.m185of(r3);
    L17:
        return Optional.m185of(((SortedSet) r0).last());
    }

    public final FluentIterable<E> limit(int r2) {
        return from(Iterables.limit(getDelegate(), r2));
    }

    public final int size() {
        return Iterables.size(getDelegate());
    }

    public final FluentIterable<E> skip(int r2) {
        return from(Iterables.skip(getDelegate(), r2));
    }

    public final E[] toArray(Class<E> r2) {
        return (E[]) Iterables.toArray(getDelegate(), r2);
    }

    public final ImmutableList<E> toList() {
        return ImmutableList.copyOf(getDelegate());
    }

    public final <V> ImmutableMap<E, V> toMap(Function<? super E, V> r2) {
        return Maps.toMap(getDelegate(), r2);
    }

    public final ImmutableMultiset<E> toMultiset() {
        return ImmutableMultiset.copyOf(getDelegate());
    }

    public final ImmutableSet<E> toSet() {
        return ImmutableSet.copyOf(getDelegate());
    }

    public final ImmutableList<E> toSortedList(Comparator<? super E> r3) {
        return Ordering.from(r3).immutableSortedCopy(getDelegate());
    }

    public final ImmutableSortedSet<E> toSortedSet(Comparator<? super E> r2) {
        return ImmutableSortedSet.copyOf(r2, getDelegate());
    }

    public String toString() {
        return Iterables.toString(getDelegate());
    }

    public final <T> FluentIterable<T> transform(Function<? super E, T> r2) {
        return from(Iterables.transform(getDelegate(), r2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> FluentIterable<T> transformAndConcat(Function<? super E, ? extends Iterable<? extends T>> r2) {
        return concat(transform(r2));
    }

    public final <K> ImmutableMap<K, E> uniqueIndex(Function<? super E, K> r2) {
        return Maps.uniqueIndex(getDelegate(), r2);
    }
}
