package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class Ordering<T> implements Comparator<T> {
    static final int LEFT_IS_GREATER = 1;
    static final int RIGHT_IS_GREATER = -1;

    static class ArbitraryOrdering extends Ordering<Object> {
        private final AtomicInteger counter;
        private final ConcurrentMap<Object, Integer> uids;

        ArbitraryOrdering() {
            this.counter = new AtomicInteger(0);
            this.uids = Platform.tryWeakKeys(new MapMaker()).makeMap();
        }

        private Integer getUid(Object r3) {
            Integer r0 = this.uids.get(r3);
            if (r0 != null) goto L8;
            Integer r1 = Integer.valueOf(this.counter.getAndIncrement());
            Integer r2 = this.uids.putIfAbsent(r3, r1);
            if (r2 != null) goto L7;
            return r1;
        L7:
            return r2;
        L8:
            return r0;
        }

        @Override // com.google.common.collect.Ordering, java.util.Comparator
        public int compare(@CheckForNull Object r5, @CheckForNull Object r6) {
            if (r5 != r6) goto L6;
            return 0;
        L6:
            if (r5 != null) goto L9;
            return -1;
        L9:
            if (r6 != null) goto L11;
            return 1;
        L11:
            int r2 = identityHashCode(r5);
            int r3 = identityHashCode(r6);
            if (r2 == r3) goto L17;
            if (r2 >= r3) goto L16;
            return -1;
        L16:
            return 1;
        L17:
            int r0 = getUid(r5).compareTo(getUid(r6));
            if (r0 == 0) goto L21;
            return r0;
        L21:
            throw new AssertionError();
        }

        int identityHashCode(Object r2) {
            return System.identityHashCode(r2);
        }

        public String toString() {
            return "Ordering.arbitrary()";
        }
    }

    private static class ArbitraryOrderingHolder {
        static final Ordering<Object> ARBITRARY_ORDERING = null;

        static {
            ARBITRARY_ORDERING = new ArbitraryOrdering();
        }

        private ArbitraryOrderingHolder() {
        }
    }

    static class IncomparableValueException extends ClassCastException {
        private static final long serialVersionUID = 0;
        final Object value;

        IncomparableValueException(Object r3) {
            super("Cannot compare value: " + r3);
            this.value = r3;
        }
    }

    protected Ordering() {
    }

    public static Ordering<Object> allEqual() {
        return AllEqualOrdering.INSTANCE;
    }

    public static Ordering<Object> arbitrary() {
        return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
    }

    public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> r1) {
        return new CompoundOrdering(r1);
    }

    public static <T> Ordering<T> explicit(T r1, T... r2) {
        return explicit(Lists.asList(r1, r2));
    }

    public static <T> Ordering<T> explicit(List<T> r1) {
        return new ExplicitOrdering(r1);
    }

    @Deprecated
    public static <T> Ordering<T> from(Ordering<T> r1) {
        return (Ordering) Preconditions.checkNotNull(r1);
    }

    public static <T> Ordering<T> from(Comparator<T> r1) {
        if ((r1 instanceof Ordering) == false) goto L6;
        return (Ordering) r1;
    L6:
        return new ComparatorOrdering(r1);
    }

    public static <C extends Comparable> Ordering<C> natural() {
        return NaturalOrdering.INSTANCE;
    }

    public static Ordering<Object> usingToString() {
        return UsingToStringOrdering.INSTANCE;
    }

    @Deprecated
    public int binarySearch(List<? extends T> r2, @ParametricNullness T r3) {
        return Collections.binarySearch(r2, r3, this);
    }

    @Override // java.util.Comparator
    public abstract int compare(@ParametricNullness T r1, @ParametricNullness T r2);

    public <U extends T> Ordering<U> compound(Comparator<? super U> r3) {
        return new CompoundOrdering(this, (Comparator) Preconditions.checkNotNull(r3));
    }

    public <E extends T> List<E> greatestOf(Iterable<E> r2, int r3) {
        return reverse().leastOf(r2, r3);
    }

    public <E extends T> List<E> greatestOf(Iterator<E> r2, int r3) {
        return reverse().leastOf(r2, r3);
    }

    public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> r2) {
        return ImmutableList.sortedCopyOf(this, r2);
    }

    public boolean isOrdered(Iterable<? extends T> r5) {
        Iterator<? extends T> r0 = r5.iterator();
        if (r0.hasNext() == false) goto L12;
        T r1 = r0.next();
    L6:
        if (r0.hasNext() == false) goto L16;
        T r2 = r0.next();
        if (compare(r1, r2) > 0) goto L9;
        r1 = r2;
        goto L6
    L9:
        return false;
    L16:
        return true;
    L12:
        return true;
    }

    public boolean isStrictlyOrdered(Iterable<? extends T> r5) {
        Iterator<? extends T> r0 = r5.iterator();
        if (r0.hasNext() == false) goto L12;
        T r1 = r0.next();
    L6:
        if (r0.hasNext() == false) goto L16;
        T r2 = r0.next();
        if (compare(r1, r2) >= 0) goto L9;
        r1 = r2;
        goto L6
    L9:
        return false;
    L16:
        return true;
    L12:
        return true;
    }

    public <E extends T> List<E> leastOf(Iterable<E> r8, int r9) {
        if ((r8 instanceof Collection) == false) goto L12;
        Collection r0 = (Collection) r8;
        if (r0.size() > (((long) r9) * 2)) goto L12;
        Object[] r1 = r0.toArray();
        Arrays.sort(r1, this);
        if (r1.length <= r9) goto L10;
        r1 = Arrays.copyOf(r1, r9);
    L10:
        return Collections.unmodifiableList(Arrays.asList(r1));
    L12:
        return leastOf(r8.iterator(), r9);
    }

    public <E extends T> List<E> leastOf(Iterator<E> r3, int r4) {
        Preconditions.checkNotNull(r3);
        CollectPreconditions.checkNonnegative(r4, "k");
        if (r4 == 0) goto L17;
        if (r3.hasNext() == false) goto L17;
        if (r4 < 1073741823) goto L14;
        ArrayList r0 = Lists.newArrayList(r3);
        Collections.sort(r0, this);
        if (r0.size() <= r4) goto L12;
        r0.subList(r4, r0.size()).clear();
    L12:
        r0.trimToSize();
        return Collections.unmodifiableList(r0);
    L14:
        TopKSelector r1 = TopKSelector.least(r4, this);
        r1.offerAll(r3);
        return r1.topK();
    L17:
        return Collections.emptyList();
    }

    public <S extends T> Ordering<Iterable<S>> lexicographical() {
        return new LexicographicalOrdering(this);
    }

    @ParametricNullness
    public <E extends T> E max(Iterable<E> r2) {
        return (E) max(r2.iterator());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @ParametricNullness
    public <E extends T> E max(@ParametricNullness E r2, @ParametricNullness E r3) {
        if (compare(r2, r3) < 0) goto L6;
        return r2;
    L6:
        return r3;
    }

    @ParametricNullness
    public <E extends T> E max(@ParametricNullness E r5, @ParametricNullness E r6, @ParametricNullness E r7, E... r8) {
        E r0 = (E) max(max(r5, r6), r7);
        int r1 = r8.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        E r3 = r0;
        r0 = (E) max(r3, r8[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    @ParametricNullness
    public <E extends T> E max(Iterator<E> r3) {
        E r0 = r3.next();
    L4:
        if (r3.hasNext() == false) goto L6;
        E r1 = r0;
        r0 = (E) max(r1, r3.next());
        goto L4
    L6:
        return r0;
    }

    @ParametricNullness
    public <E extends T> E min(Iterable<E> r2) {
        return (E) min(r2.iterator());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @ParametricNullness
    public <E extends T> E min(@ParametricNullness E r2, @ParametricNullness E r3) {
        if (compare(r2, r3) > 0) goto L6;
        return r2;
    L6:
        return r3;
    }

    @ParametricNullness
    public <E extends T> E min(@ParametricNullness E r5, @ParametricNullness E r6, @ParametricNullness E r7, E... r8) {
        E r0 = (E) min(min(r5, r6), r7);
        int r1 = r8.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        E r3 = r0;
        r0 = (E) min(r3, r8[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    @ParametricNullness
    public <E extends T> E min(Iterator<E> r3) {
        E r0 = r3.next();
    L4:
        if (r3.hasNext() == false) goto L6;
        E r1 = r0;
        r0 = (E) min(r1, r3.next());
        goto L4
    L6:
        return r0;
    }

    public <S extends T> Ordering<S> nullsFirst() {
        return new NullsFirstOrdering(this);
    }

    public <S extends T> Ordering<S> nullsLast() {
        return new NullsLastOrdering(this);
    }

    <T2 extends T> Ordering<Map.Entry<T2, ?>> onKeys() {
        return (Ordering<Map.Entry<T2, ?>>) onResultOf(Maps.keyFunction());
    }

    public <F> Ordering<F> onResultOf(Function<F, ? extends T> r2) {
        return new ByFunctionOrdering(r2, this);
    }

    public <S extends T> Ordering<S> reverse() {
        return new ReverseOrdering(this);
    }

    public <E extends T> List<E> sortedCopy(Iterable<E> r3) {
        Object[] r0 = Iterables.toArray(r3);
        Arrays.sort(r0, this);
        return Lists.newArrayList(Arrays.asList(r0));
    }
}
