package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.primitives.Ints;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import javax.annotation.CheckForNull;
import kotlin.text.Typography;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Iterators {

    private static final class ArrayItr<T> extends AbstractIndexedListIterator<T> {
        static final UnmodifiableListIterator<Object> EMPTY = null;
        private final T[] array;

        static {
            EMPTY = new ArrayItr(new Object[0], 0);
        }

        ArrayItr(T[] r2, int r3) {
            super(r2.length, r3);
            this.array = r2;
        }

        @Override // com.google.common.collect.AbstractIndexedListIterator
        @ParametricNullness
        protected T get(int r2) {
            return this.array[r2];
        }
    }

    private static class ConcatenatedIterator<T> implements Iterator<T> {
        private Iterator<? extends T> iterator;

        @CheckForNull
        private Deque<Iterator<? extends Iterator<? extends T>>> metaIterators;

        @CheckForNull
        private Iterator<? extends T> toRemove;

        @CheckForNull
        private Iterator<? extends Iterator<? extends T>> topMetaIterator;

        ConcatenatedIterator(Iterator<? extends Iterator<? extends T>> r2) {
            this.iterator = Iterators.emptyIterator();
            this.topMetaIterator = (Iterator) Preconditions.checkNotNull(r2);
        }

        @CheckForNull
        private Iterator<? extends Iterator<? extends T>> getTopMetaIterator() {
        L3:
            if (this.topMetaIterator == null) goto L10;
            if (this.topMetaIterator.hasNext() == false) goto L10;
            return this.topMetaIterator;
        L10:
            if (this.metaIterators == null) goto L14;
            if (this.metaIterators.isEmpty() == true) goto L19;
            this.topMetaIterator = this.metaIterators.removeFirst();
            goto L3
        L19:
            return null;
        L14:
            return null;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
        L3:
            if (((Iterator) Preconditions.checkNotNull(this.iterator)).hasNext() == true) goto L19;
            this.topMetaIterator = getTopMetaIterator();
            if (this.topMetaIterator == null) goto L6;
            this.iterator = this.topMetaIterator.next();
            if ((this.iterator instanceof ConcatenatedIterator) == false) goto L3;
            ConcatenatedIterator r0 = (ConcatenatedIterator) this.iterator;
            this.iterator = r0.iterator;
            if (this.metaIterators != null) goto L13;
            this.metaIterators = new ArrayDeque();
        L13:
            this.metaIterators.addFirst(this.topMetaIterator);
            if (r0.metaIterators != null) goto L16;
        L18:
            this.topMetaIterator = r0.topMetaIterator;
        L16:
            if (r0.metaIterators.isEmpty() == true) goto L18;
            this.metaIterators.addFirst(r0.metaIterators.removeLast());
            goto L16
        L6:
            return false;
        L19:
            return true;
        }

        @Override // java.util.Iterator
        @ParametricNullness
        public T next() {
            if (hasNext() == false) goto L7;
            this.toRemove = this.iterator;
            return this.iterator.next();
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.toRemove == null) goto L7;
            this.toRemove.remove();
            this.toRemove = null;
            return;
        L7:
            throw new IllegalStateException("no calls to next() since the last call to remove()");
        }
    }

    private static final enum EmptyModifiableIterator extends Enum<EmptyModifiableIterator> implements Iterator<Object> {
        private static final /* synthetic */ EmptyModifiableIterator[] $VALUES = null;
        public static final EmptyModifiableIterator INSTANCE = null;

        private static /* synthetic */ EmptyModifiableIterator[] $values() {
            return new EmptyModifiableIterator[]{INSTANCE};
        }

        static {
            INSTANCE = new EmptyModifiableIterator("INSTANCE", 0);
            $VALUES = $values();
        }

        private EmptyModifiableIterator(String r1, int r2) {
            super(r1, r2);
        }

        public static EmptyModifiableIterator valueOf(String r1) {
            return (EmptyModifiableIterator) Enum.valueOf(EmptyModifiableIterator.class, r1);
        }

        public static EmptyModifiableIterator[] values() {
            return (EmptyModifiableIterator[]) $VALUES.clone();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return false;
        }

        @Override // java.util.Iterator
        public Object next() {
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            CollectPreconditions.checkRemove(false);
        }
    }

    private static class MergingIterator<T> extends UnmodifiableIterator<T> {
        final Queue<PeekingIterator<T>> queue;

        public MergingIterator(Iterable<? extends Iterator<? extends T>> r6, final Comparator<? super T> r7) {
            this.queue = new PriorityQueue(2, new Iterators$MergingIterator$$ExternalSyntheticLambda0(r7));
            Iterator<? extends Iterator<? extends T>> r1 = r6.iterator();
        L4:
            if (r1.hasNext() == false) goto L9;
            Iterator<? extends T> r2 = r1.next();
            if (r2.hasNext() == false) goto L4;
            this.queue.add(Iterators.peekingIterator(r2));
            goto L4
        }

        /* JADX WARN: Multi-variable type inference failed */
        static /* synthetic */ int lambda$new$0(Comparator r2, PeekingIterator r3, PeekingIterator r4) {
            return r2.compare(r3.peek(), r4.peek());
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }

        @Override // java.util.Iterator
        @ParametricNullness
        public T next() {
            PeekingIterator<T> r0 = this.queue.remove();
            T r1 = r0.next();
            if (r0.hasNext() == false) goto L5;
            this.queue.add(r0);
        L5:
            return r1;
        }
    }

    private static class PeekingImpl<E> implements PeekingIterator<E> {
        private boolean hasPeeked;
        private final Iterator<? extends E> iterator;

        @CheckForNull
        private E peekedElement;

        public PeekingImpl(Iterator<? extends E> r2) {
            this.iterator = (Iterator) Preconditions.checkNotNull(r2);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.hasPeeked == false) goto L5;
        L8:
            return true;
        L5:
            if (this.iterator.hasNext() == true) goto L8;
            return false;
        }

        @Override // com.google.common.collect.PeekingIterator, java.util.Iterator
        @ParametricNullness
        public E next() {
            if (this.hasPeeked == false) goto L5;
            E r0 = (E) NullnessCasts.uncheckedCastNullableTToT(this.peekedElement);
            this.hasPeeked = false;
            this.peekedElement = null;
            return r0;
        L5:
            return this.iterator.next();
        }

        @Override // com.google.common.collect.PeekingIterator
        @ParametricNullness
        public E peek() {
            if (this.hasPeeked == true) goto L6;
            this.peekedElement = this.iterator.next();
            this.hasPeeked = true;
        L6:
            return (E) NullnessCasts.uncheckedCastNullableTToT(this.peekedElement);
        }

        @Override // com.google.common.collect.PeekingIterator, java.util.Iterator
        public void remove() {
            Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
            this.iterator.remove();
        }
    }

    private static final class SingletonIterator<T> extends UnmodifiableIterator<T> {
        private boolean done;
        private final T value;

        SingletonIterator(T r1) {
            this.value = r1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !this.done;
        }

        @Override // java.util.Iterator
        @ParametricNullness
        public T next() {
            if (this.done == true) goto L7;
            this.done = true;
            return this.value;
        L7:
            throw new NoSuchElementException();
        }
    }

    private Iterators() {
    }

    public static <T> boolean addAll(Collection<T> r2, Iterator<? extends T> r3) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        boolean r0 = false;
    L4:
        if (r3.hasNext() == false) goto L6;
        r0 = r0 | r2.add(r3.next());
        goto L4
    L6:
        return r0;
    }

    public static int advance(Iterator<?> r2, int r3) {
        Preconditions.checkNotNull(r2);
        if (r3 < 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "numberToAdvance must be nonnegative");
        int r1 = 0;
    L7:
        if (r1 >= r3) goto L11;
        if (r2.hasNext() == false) goto L11;
        r2.next();
        r1 = r1 + 1;
    L11:
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    public static <T> boolean all(Iterator<T> r2, Predicate<? super T> r3) {
        Preconditions.checkNotNull(r3);
    L4:
        if (r2.hasNext() == false) goto L10;
        if (r3.apply(r2.next()) == true) goto L4;
        return false;
    L10:
        return true;
    }

    public static <T> boolean any(Iterator<T> r2, Predicate<? super T> r3) {
        if (indexOf(r2, r3) == (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    public static <T> Enumeration<T> asEnumeration(final Iterator<T> r1) {
        Preconditions.checkNotNull(r1);
        return new C065510(r1);
    }

    static void checkNonnegative(int r3) {
        if (r3 < 0) goto L5;
        return;
    L5:
        throw new IndexOutOfBoundsException("position (" + r3 + ") must not be negative");
    }

    static void clear(Iterator<?> r1) {
        Preconditions.checkNotNull(r1);
    L4:
        if (r1.hasNext() == false) goto L6;
        r1.next();
        r1.remove();
        goto L4
    }

    public static <T> Iterator<T> concat(Iterator<? extends Iterator<? extends T>> r1) {
        return new ConcatenatedIterator(r1);
    }

    public static <T> Iterator<T> concat(Iterator<? extends T> r2, Iterator<? extends T> r3) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        return concat(consumingForArray(new Iterator[]{r2, r3}));
    }

    public static <T> Iterator<T> concat(Iterator<? extends T> r2, Iterator<? extends T> r3, Iterator<? extends T> r4) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        return concat(consumingForArray(new Iterator[]{r2, r3, r4}));
    }

    public static <T> Iterator<T> concat(Iterator<? extends T> r2, Iterator<? extends T> r3, Iterator<? extends T> r4, Iterator<? extends T> r5) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        return concat(consumingForArray(new Iterator[]{r2, r3, r4, r5}));
    }

    @SafeVarargs
    public static <T> Iterator<T> concat(Iterator<? extends T>... r1) {
        return concatNoDefensiveCopy((Iterator[]) Arrays.copyOf(r1, r1.length));
    }

    static <T> Iterator<T> concatNoDefensiveCopy(Iterator<? extends T>... r4) {
        Iterator[] r0 = (Iterator[]) Preconditions.checkNotNull(r4);
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L6;
        Preconditions.checkNotNull(r0[r2]);
        r2 = r2 + 1;
        goto L3
    L6:
        return concat(consumingForArray(r4));
    }

    private static <I extends Iterator<?>> Iterator<I> consumingForArray(final I... r1) {
        return new C06573(r1);
    }

    public static <T> Iterator<T> consumingIterator(final Iterator<T> r1) {
        Preconditions.checkNotNull(r1);
        return new C06628(r1);
    }

    public static boolean contains(Iterator<?> r2, @CheckForNull Object r3) {
        if (r3 != null) goto L10;
    L5:
        if (r2.hasNext() == false) goto L14;
        if (r2.next() != null) goto L5;
        return true;
    L14:
        return false;
    L10:
        if (r2.hasNext() == false) goto L22;
        if (r3.equals(r2.next()) == false) goto L10;
        return true;
    L22:
        return false;
    }

    public static <T> Iterator<T> cycle(final Iterable<T> r1) {
        Preconditions.checkNotNull(r1);
        return new C06562(r1);
    }

    @SafeVarargs
    public static <T> Iterator<T> cycle(T... r1) {
        return cycle(Lists.newArrayList(r1));
    }

    public static boolean elementsEqual(Iterator<?> r4, Iterator<?> r5) {
    L3:
        if (r4.hasNext() == false) goto L12;
        if (r5.hasNext() == false) goto L6;
        if (Objects.equal(r4.next(), r5.next()) == true) goto L3;
        return false;
    L6:
        return false;
    L12:
        return !r5.hasNext();
    }

    static <T> UnmodifiableIterator<T> emptyIterator() {
        return emptyListIterator();
    }

    static <T> UnmodifiableListIterator<T> emptyListIterator() {
        return (UnmodifiableListIterator<T>) ArrayItr.EMPTY;
    }

    static <T> Iterator<T> emptyModifiableIterator() {
        return EmptyModifiableIterator.INSTANCE;
    }

    public static <T> UnmodifiableIterator<T> filter(final Iterator<T> r1, final Predicate<? super T> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new C06595(r1, r2);
    }

    public static <T> UnmodifiableIterator<T> filter(Iterator<?> r1, Class<T> r2) {
        return filter(r1, Predicates.instanceOf(r2));
    }

    @ParametricNullness
    public static <T> T find(Iterator<T> r2, Predicate<? super T> r3) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
    L4:
        if (r2.hasNext() == false) goto L10;
        T r0 = r2.next();
        if (r3.apply(r0) == false) goto L4;
        return r0;
    L10:
        throw new NoSuchElementException();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [T, java.lang.Object] */
    @CheckForNull
    public static <T> T find(Iterator<? extends T> r2, Predicate<? super T> r3, @CheckForNull T r4) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
    L4:
        if (r2.hasNext() == false) goto L9;
        T r0 = r2.next();
        if (r3.apply(r0) == false) goto L4;
        return r0;
    L9:
        return r4;
    }

    @SafeVarargs
    public static <T> UnmodifiableIterator<T> forArray(T... r1) {
        return forArrayWithPosition(r1, 0);
    }

    static <T> UnmodifiableListIterator<T> forArrayWithPosition(T[] r1, int r2) {
        if (r1.length != 0) goto L7;
        Preconditions.checkPositionIndex(r2, r1.length);
        return emptyListIterator();
    L7:
        return new ArrayItr(r1, r2);
    }

    public static <T> UnmodifiableIterator<T> forEnumeration(final Enumeration<T> r1) {
        Preconditions.checkNotNull(r1);
        return new C06639(r1);
    }

    public static int frequency(Iterator<?> r2, @CheckForNull Object r3) {
        int r0 = 0;
    L4:
        if (contains(r2, r3) == false) goto L6;
        r0 = r0 + 1;
        goto L4
    L6:
        return r0;
    }

    @ParametricNullness
    public static <T> T get(Iterator<T> r4, int r5) {
        checkNonnegative(r5);
        int r0 = advance(r4, r5);
        if (r4.hasNext() == false) goto L7;
        return r4.next();
    L7:
        throw new IndexOutOfBoundsException("position (" + r5 + ") must be less than the number of elements that remained (" + r0 + ")");
    }

    @ParametricNullness
    public static <T> T get(Iterator<? extends T> r1, int r2, @ParametricNullness T r3) {
        checkNonnegative(r2);
        advance(r1, r2);
        return (T) getNext(r1, r3);
    }

    @ParametricNullness
    public static <T> T getLast(Iterator<T> r2) {
    L2:
        T r0 = r2.next();
        if (r2.hasNext() == true) goto L2;
        return r0;
    }

    @ParametricNullness
    public static <T> T getLast(Iterator<? extends T> r1, @ParametricNullness T r2) {
        if (r1.hasNext() == false) goto L6;
        return (T) getLast(r1);
    L6:
        return r2;
    }

    @ParametricNullness
    public static <T> T getNext(Iterator<? extends T> r1, @ParametricNullness T r2) {
        if (r1.hasNext() == false) goto L6;
        return r1.next();
    L6:
        return r2;
    }

    @ParametricNullness
    public static <T> T getOnlyElement(Iterator<T> r5) {
        T r0 = r5.next();
        if (r5.hasNext() == true) goto L5;
        return r0;
    L5:
        StringBuilder r1 = new StringBuilder().append("expected one element but was: <").append(r0);
        int r2 = 0;
    L7:
        if (r2 >= 4) goto L12;
        if (r5.hasNext() == false) goto L12;
        r1.append(", ").append(r5.next());
        r2 = r2 + 1;
    L12:
        if (r5.hasNext() == false) goto L14;
        r1.append(", ...");
    L14:
        r1.append(Typography.greater);
        throw new IllegalArgumentException(r1.toString());
    }

    @ParametricNullness
    public static <T> T getOnlyElement(Iterator<? extends T> r1, @ParametricNullness T r2) {
        if (r1.hasNext() == false) goto L6;
        return (T) getOnlyElement(r1);
    L6:
        return r2;
    }

    public static <T> int indexOf(Iterator<T> r3, Predicate<? super T> r4) {
        Preconditions.checkNotNull(r4, "predicate");
        int r0 = 0;
    L4:
        if (r3.hasNext() == false) goto L9;
        if (r4.apply(r3.next()) == true) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r0;
    L9:
        return -1;
    }

    public static <T> Iterator<T> limit(final Iterator<T> r2, final int r3) {
        Preconditions.checkNotNull(r2);
        if (r3 < 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "limit is negative");
        return new C06617(r3, r2);
    L5:
        r0 = false;
        goto L6
    }

    public static <T> UnmodifiableIterator<T> mergeSorted(Iterable<? extends Iterator<? extends T>> r1, Comparator<? super T> r2) {
        Preconditions.checkNotNull(r1, "iterators");
        Preconditions.checkNotNull(r2, "comparator");
        return new MergingIterator(r1, r2);
    }

    public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> r1, int r2) {
        return partitionImpl(r1, r2, true);
    }

    public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> r1, int r2) {
        return partitionImpl(r1, r2, false);
    }

    private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> r1, final int r2, final boolean r3) {
        Preconditions.checkNotNull(r1);
        if (r2 <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        return new C06584(r1, r2, r3);
    L5:
        r0 = false;
        goto L6
    }

    @Deprecated
    public static <T> PeekingIterator<T> peekingIterator(PeekingIterator<T> r1) {
        return (PeekingIterator) Preconditions.checkNotNull(r1);
    }

    public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> r1) {
        if ((r1 instanceof PeekingImpl) == false) goto L7;
        return (PeekingImpl) r1;
    L7:
        return new PeekingImpl(r1);
    }

    @CheckForNull
    static <T> T pollNext(Iterator<T> r1) {
        if (r1.hasNext() == false) goto L6;
        T r0 = r1.next();
        r1.remove();
        return r0;
    L6:
        return null;
    }

    public static boolean removeAll(Iterator<?> r2, Collection<?> r3) {
        Preconditions.checkNotNull(r3);
        boolean r0 = false;
    L4:
        if (r2.hasNext() == false) goto L8;
        if (r3.contains(r2.next()) == false) goto L4;
        r2.remove();
        r0 = true;
        goto L4
    L8:
        return r0;
    }

    public static <T> boolean removeIf(Iterator<T> r2, Predicate<? super T> r3) {
        Preconditions.checkNotNull(r3);
        boolean r0 = false;
    L4:
        if (r2.hasNext() == false) goto L8;
        if (r3.apply(r2.next()) == false) goto L4;
        r2.remove();
        r0 = true;
        goto L4
    L8:
        return r0;
    }

    public static boolean retainAll(Iterator<?> r2, Collection<?> r3) {
        Preconditions.checkNotNull(r3);
        boolean r0 = false;
    L4:
        if (r2.hasNext() == false) goto L8;
        if (r3.contains(r2.next()) == true) goto L4;
        r2.remove();
        r0 = true;
        goto L4
    L8:
        return r0;
    }

    public static <T> UnmodifiableIterator<T> singletonIterator(@ParametricNullness T r1) {
        return new SingletonIterator(r1);
    }

    public static int size(Iterator<?> r4) {
        long r0 = 0;
    L4:
        if (r4.hasNext() == false) goto L7;
        r4.next();
        r0 = r0 + 1;
        goto L4
    L7:
        return Ints.saturatedCast(r0);
    }

    public static <T> T[] toArray(Iterator<? extends T> r2, Class<T> r3) {
        return (T[]) Iterables.toArray(Lists.newArrayList(r2), r3);
    }

    public static String toString(Iterator<?> r3) {
        StringBuilder r0 = new StringBuilder().append('[');
        boolean r1 = true;
    L4:
        if (r3.hasNext() == false) goto L9;
        if (r1 == true) goto L7;
        r0.append(", ");
    L7:
        r1 = false;
        r0.append(r3.next());
        goto L4
    L9:
        return r0.append(']').toString();
    }

    public static <F, T> Iterator<T> transform(final Iterator<F> r1, final Function<? super F, ? extends T> r2) {
        Preconditions.checkNotNull(r2);
        return new C06606(r1, r2);
    }

    public static <T> Optional<T> tryFind(Iterator<T> r2, Predicate<? super T> r3) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
    L4:
        if (r2.hasNext() == false) goto L11;
        T r0 = r2.next();
        if (r3.apply(r0) == false) goto L4;
        return Optional.m185of(r0);
    L11:
        return Optional.absent();
    }

    @Deprecated
    public static <T> UnmodifiableIterator<T> unmodifiableIterator(UnmodifiableIterator<T> r1) {
        return (UnmodifiableIterator) Preconditions.checkNotNull(r1);
    }

    public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<? extends T> r1) {
        Preconditions.checkNotNull(r1);
        if ((r1 instanceof UnmodifiableIterator) == false) goto L7;
        return (UnmodifiableIterator) r1;
    L7:
        return new C06541(r1);
    }
}
