package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Iterables {

    private static final class UnmodifiableIterable<T> extends FluentIterable<T> {
        private final Iterable<? extends T> iterable;

        private UnmodifiableIterable(Iterable<? extends T> r1) {
            this.iterable = r1;
        }

        /* synthetic */ UnmodifiableIterable(Iterable r1, C06451 r2) {
            this(r1);
        }

        @Override // java.lang.Iterable
        public Iterator<T> iterator() {
            return Iterators.unmodifiableIterator(this.iterable.iterator());
        }

        @Override // com.google.common.collect.FluentIterable
        public String toString() {
            return this.iterable.toString();
        }
    }

    private Iterables() {
    }

    public static <T> boolean addAll(Collection<T> r2, Iterable<? extends T> r3) {
        if ((r3 instanceof Collection) == false) goto L7;
        return r2.addAll((Collection) r3);
    L7:
        return Iterators.addAll(r2, ((Iterable) Preconditions.checkNotNull(r3)).iterator());
    }

    public static <T> boolean all(Iterable<T> r1, Predicate<? super T> r2) {
        return Iterators.all(r1.iterator(), r2);
    }

    public static <T> boolean any(Iterable<T> r1, Predicate<? super T> r2) {
        return Iterators.any(r1.iterator(), r2);
    }

    private static <E> Collection<E> castOrCopyToCollection(Iterable<E> r1) {
        if ((r1 instanceof Collection) == false) goto L6;
        return (Collection) r1;
    L6:
        return Lists.newArrayList(r1.iterator());
    }

    public static <T> Iterable<T> concat(Iterable<? extends Iterable<? extends T>> r1) {
        return FluentIterable.concat(r1);
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> r1, Iterable<? extends T> r2) {
        return FluentIterable.concat(r1, r2);
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> r1, Iterable<? extends T> r2, Iterable<? extends T> r3) {
        return FluentIterable.concat(r1, r2, r3);
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> r1, Iterable<? extends T> r2, Iterable<? extends T> r3, Iterable<? extends T> r4) {
        return FluentIterable.concat(r1, r2, r3, r4);
    }

    @SafeVarargs
    public static <T> Iterable<T> concat(Iterable<? extends T>... r1) {
        return FluentIterable.concat(r1);
    }

    public static <T> Iterable<T> consumingIterable(final Iterable<T> r1) {
        Preconditions.checkNotNull(r1);
        return new C06528(r1);
    }

    public static boolean contains(Iterable<? extends Object> r2, @CheckForNull Object r3) {
        if ((r2 instanceof Collection) == false) goto L7;
        return Collections2.safeContains((Collection) r2, r3);
    L7:
        return Iterators.contains(r2.iterator(), r3);
    }

    public static <T> Iterable<T> cycle(final Iterable<T> r1) {
        Preconditions.checkNotNull(r1);
        return new C06451(r1);
    }

    @SafeVarargs
    public static <T> Iterable<T> cycle(T... r1) {
        return cycle(Lists.newArrayList(r1));
    }

    public static boolean elementsEqual(Iterable<?> r4, Iterable<?> r5) {
        if ((r4 instanceof Collection) == false) goto L11;
        if ((r5 instanceof Collection) == false) goto L11;
        if (((Collection) r4).size() == ((Collection) r5).size()) goto L11;
        return false;
    L11:
        return Iterators.elementsEqual(r4.iterator(), r5.iterator());
    }

    public static <T> Iterable<T> filter(final Iterable<T> r1, final Predicate<? super T> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new C06484(r1, r2);
    }

    public static <T> Iterable<T> filter(Iterable<?> r1, Class<T> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return filter(r1, Predicates.instanceOf(r2));
    }

    @ParametricNullness
    public static <T> T find(Iterable<T> r1, Predicate<? super T> r2) {
        return (T) Iterators.find(r1.iterator(), r2);
    }

    @CheckForNull
    public static <T> T find(Iterable<? extends T> r1, Predicate<? super T> r2, @CheckForNull T r3) {
        return (T) Iterators.find(r1.iterator(), r2, r3);
    }

    public static int frequency(Iterable<?> r1, @CheckForNull Object r2) {
        if ((r1 instanceof Multiset) == false) goto L7;
        return ((Multiset) r1).count(r2);
    L7:
        if ((r1 instanceof Set) == false) goto L11;
        return ((Set) r1).contains(r2) ? 1 : 0;
    L11:
        return Iterators.frequency(r1.iterator(), r2);
    }

    @ParametricNullness
    public static <T> T get(Iterable<T> r1, int r2) {
        Preconditions.checkNotNull(r1);
        if ((r1 instanceof List) == false) goto L6;
        return (T) ((List) r1).get(r2);
    L6:
        return (T) Iterators.get(r1.iterator(), r2);
    }

    @ParametricNullness
    public static <T> T get(Iterable<? extends T> r2, int r3, @ParametricNullness T r4) {
        Preconditions.checkNotNull(r2);
        Iterators.checkNonnegative(r3);
        if ((r2 instanceof List) == false) goto L9;
        List r0 = Lists.cast(r2);
        if (r3 >= r0.size()) goto L8;
        return (T) r0.get(r3);
    L8:
        return r4;
    L9:
        Iterator<? extends T> r1 = r2.iterator();
        Iterators.advance(r1, r3);
        return (T) Iterators.getNext(r1, r4);
    }

    @ParametricNullness
    public static <T> T getFirst(Iterable<? extends T> r1, @ParametricNullness T r2) {
        return (T) Iterators.getNext(r1.iterator(), r2);
    }

    @ParametricNullness
    public static <T> T getLast(Iterable<T> r2) {
        if ((r2 instanceof List) == false) goto L11;
        List r0 = (List) r2;
        if (r0.isEmpty() == true) goto L9;
        return (T) getLastInNonemptyList(r0);
    L9:
        throw new NoSuchElementException();
    L11:
        return (T) Iterators.getLast(r2.iterator());
    }

    @ParametricNullness
    public static <T> T getLast(Iterable<? extends T> r2, @ParametricNullness T r3) {
        if ((r2 instanceof Collection) == false) goto L12;
        if (((Collection) r2).isEmpty() == false) goto L8;
        return r3;
    L8:
        if ((r2 instanceof List) == false) goto L12;
        return (T) getLastInNonemptyList(Lists.cast(r2));
    L12:
        return (T) Iterators.getLast(r2.iterator(), r3);
    }

    @ParametricNullness
    private static <T> T getLastInNonemptyList(List<T> r1) {
        return r1.get(r1.size() - 1);
    }

    @ParametricNullness
    public static <T> T getOnlyElement(Iterable<T> r1) {
        return (T) Iterators.getOnlyElement(r1.iterator());
    }

    @ParametricNullness
    public static <T> T getOnlyElement(Iterable<? extends T> r1, @ParametricNullness T r2) {
        return (T) Iterators.getOnlyElement(r1.iterator(), r2);
    }

    public static <T> int indexOf(Iterable<T> r1, Predicate<? super T> r2) {
        return Iterators.indexOf(r1.iterator(), r2);
    }

    public static boolean isEmpty(Iterable<?> r1) {
        if ((r1 instanceof Collection) == false) goto L7;
        return ((Collection) r1).isEmpty();
    L7:
        return !r1.iterator().hasNext();
    }

    public static <T> Iterable<T> limit(final Iterable<T> r2, final int r3) {
        Preconditions.checkNotNull(r2);
        if (r3 < 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "limit is negative");
        return new C06517(r2, r3);
    L5:
        r0 = false;
        goto L6
    }

    public static <T> Iterable<T> mergeSorted(final Iterable<? extends Iterable<? extends T>> r3, final Comparator<? super T> r4) {
        Preconditions.checkNotNull(r3, "iterables");
        Preconditions.checkNotNull(r4, "comparator");
        return new UnmodifiableIterable(new C06539(r3, r4), null);
    }

    public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> r1, final int r2) {
        Preconditions.checkNotNull(r1);
        if (r2 <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        return new C06473(r1, r2);
    L5:
        r0 = false;
        goto L6
    }

    public static <T> Iterable<List<T>> partition(final Iterable<T> r1, final int r2) {
        Preconditions.checkNotNull(r1);
        if (r2 <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        return new C06462(r1, r2);
    L5:
        r0 = false;
        goto L6
    }

    public static boolean removeAll(Iterable<?> r2, Collection<?> r3) {
        if ((r2 instanceof Collection) == false) goto L6;
        return ((Collection) r2).removeAll((Collection) Preconditions.checkNotNull(r3));
    L6:
        return Iterators.removeAll(r2.iterator(), r3);
    }

    @CheckForNull
    static <T> T removeFirstMatching(Iterable<T> r3, Predicate<? super T> r4) {
        Preconditions.checkNotNull(r4);
        Iterator<T> r0 = r3.iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        T r1 = r0.next();
        if (r4.apply(r1) == false) goto L4;
        r0.remove();
        return r1;
    L10:
        return null;
    }

    public static <T> boolean removeIf(Iterable<T> r2, Predicate<? super T> r3) {
        if ((r2 instanceof RandomAccess) == false) goto L9;
        if ((r2 instanceof List) == false) goto L9;
        return removeIfFromRandomAccessList((List) r2, (Predicate) Preconditions.checkNotNull(r3));
    L9:
        return Iterators.removeIf(r2.iterator(), r3);
    }

    private static <T> boolean removeIfFromRandomAccessList(List<T> r5, Predicate<? super T> r6) {
        int r0 = 0;
        int r1 = 0;
    L4:
        if (r0 >= r5.size()) goto L18;
        T r2 = r5.get(r0);
        if (r6.apply(r2) == true) goto L17;
        if (r0 > r1) goto L23;
    L16:
        r1 = r1 + 1;
        goto L17
    L23:
        r5.set(r1, r2);     // Catch: IllegalArgumentException -> L10 UnsupportedOperationException -> L13
    L11:
        slowRemoveIfForRemainingElements(r5, r6, r1, r0);
        return true;
    L14:
        slowRemoveIfForRemainingElements(r5, r6, r1, r0);
        return true;
    L17:
        r0 = r0 + 1;
        goto L4
    L18:
        r5.subList(r1, r5.size()).clear();
        if (r0 != r1) goto L28;
        return false;
    L28:
        return true;
    }

    public static boolean retainAll(Iterable<?> r2, Collection<?> r3) {
        if ((r2 instanceof Collection) == false) goto L6;
        return ((Collection) r2).retainAll((Collection) Preconditions.checkNotNull(r3));
    L6:
        return Iterators.retainAll(r2.iterator(), r3);
    }

    public static int size(Iterable<?> r1) {
        if ((r1 instanceof Collection) == false) goto L6;
        return ((Collection) r1).size();
    L6:
        return Iterators.size(r1.iterator());
    }

    public static <T> Iterable<T> skip(final Iterable<T> r2, final int r3) {
        Preconditions.checkNotNull(r2);
        if (r3 < 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "number to skip cannot be negative");
        return new C06506(r2, r3);
    L5:
        r0 = false;
        goto L6
    }

    private static <T> void slowRemoveIfForRemainingElements(List<T> r2, Predicate<? super T> r3, int r4, int r5) {
        int r0 = r2.size() - 1;
    L3:
        if (r0 <= r5) goto L8;
        if (r3.apply(r2.get(r0)) == false) goto L7;
        r2.remove(r0);
    L7:
        r0 = r0 - 1;
        goto L3
    L8:
        int r1 = r5 - 1;
    L9:
        if (r1 < r4) goto L11;
        r2.remove(r1);
        r1 = r1 - 1;
        goto L9
    }

    static Object[] toArray(Iterable<?> r1) {
        return castOrCopyToCollection(r1).toArray();
    }

    public static <T> T[] toArray(Iterable<? extends T> r1, Class<T> r2) {
        return (T[]) toArray(r1, ObjectArrays.newArray(r2, 0));
    }

    static <T> T[] toArray(Iterable<? extends T> r2, T[] r3) {
        return (T[]) castOrCopyToCollection(r2).toArray(r3);
    }

    public static String toString(Iterable<?> r1) {
        return Iterators.toString(r1.iterator());
    }

    public static <F, T> Iterable<T> transform(final Iterable<F> r1, final Function<? super F, ? extends T> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new C06495(r1, r2);
    }

    public static <T> Optional<T> tryFind(Iterable<T> r1, Predicate<? super T> r2) {
        return Iterators.tryFind(r1.iterator(), r2);
    }

    @Deprecated
    public static <E> Iterable<E> unmodifiableIterable(ImmutableCollection<E> r1) {
        return (Iterable) Preconditions.checkNotNull(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> Iterable<T> unmodifiableIterable(Iterable<? extends T> r2) {
        Preconditions.checkNotNull(r2);
        if ((r2 instanceof UnmodifiableIterable) == true) goto L10;
        if ((r2 instanceof ImmutableCollection) == true) goto L10;
        return new UnmodifiableIterable(r2, null);
    L10:
        return r2;
    }
}
