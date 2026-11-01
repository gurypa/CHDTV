package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.math.RoundingMode;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Lists {

    private static class AbstractListWrapper<E> extends AbstractList<E> {
        final List<E> backingList;

        AbstractListWrapper(List<E> r2) {
            this.backingList = (List) Preconditions.checkNotNull(r2);
        }

        @Override // java.util.AbstractList, java.util.List
        public void add(int r2, @ParametricNullness E r3) {
            this.backingList.add(r2, r3);
        }

        @Override // java.util.AbstractList, java.util.List
        public boolean addAll(int r2, Collection<? extends E> r3) {
            return this.backingList.addAll(r2, r3);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean contains(@CheckForNull Object r2) {
            return this.backingList.contains(r2);
        }

        @Override // java.util.AbstractList, java.util.List
        @ParametricNullness
        public E get(int r2) {
            return this.backingList.get(r2);
        }

        @Override // java.util.AbstractList, java.util.List
        @ParametricNullness
        public E remove(int r2) {
            return this.backingList.remove(r2);
        }

        @Override // java.util.AbstractList, java.util.List
        @ParametricNullness
        public E set(int r2, @ParametricNullness E r3) {
            return this.backingList.set(r2, r3);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.backingList.size();
        }
    }

    private static final class CharSequenceAsList extends AbstractList<Character> {
        private final CharSequence sequence;

        CharSequenceAsList(CharSequence r1) {
            this.sequence = r1;
        }

        @Override // java.util.AbstractList, java.util.List
        public Character get(int r2) {
            Preconditions.checkElementIndex(r2, size());
            return Character.valueOf(this.sequence.charAt(r2));
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.sequence.length();
        }
    }

    private static class OnePlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {
        private static final long serialVersionUID = 0;

        @ParametricNullness
        final E first;
        final E[] rest;

        OnePlusArrayList(@ParametricNullness E r2, E[] r3) {
            this.first = r2;
            this.rest = (E[]) ((Object[]) Preconditions.checkNotNull(r3));
        }

        @Override // java.util.AbstractList, java.util.List
        @ParametricNullness
        public E get(int r3) {
            Preconditions.checkElementIndex(r3, size());
            if (r3 != 0) goto L6;
            return this.first;
        L6:
            return this.rest[r3 - 1];
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return IntMath.saturatedAdd(this.rest.length, 1);
        }
    }

    private static class Partition<T> extends AbstractList<List<T>> {
        final List<T> list;
        final int size;

        Partition(List<T> r1, int r2) {
            this.list = r1;
            this.size = r2;
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // java.util.AbstractList, java.util.List
        public List<T> get(int r4) {
            Preconditions.checkElementIndex(r4, size());
            int r0 = this.size * r4;
            int r1 = Math.min(this.size + r0, this.list.size());
            return this.list.subList(r0, r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean isEmpty() {
            return this.list.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return IntMath.divide(this.list.size(), this.size, RoundingMode.CEILING);
        }
    }

    private static class RandomAccessListWrapper<E> extends AbstractListWrapper<E> implements RandomAccess {
        RandomAccessListWrapper(List<E> r1) {
            super(r1);
        }
    }

    private static class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {
        RandomAccessPartition(List<T> r1, int r2) {
            super(r1, r2);
        }
    }

    private static class RandomAccessReverseList<T> extends ReverseList<T> implements RandomAccess {
        RandomAccessReverseList(List<T> r1) {
            super(r1);
        }
    }

    private static class ReverseList<T> extends AbstractList<T> {
        private final List<T> forwardList;

        ReverseList(List<T> r2) {
            this.forwardList = (List) Preconditions.checkNotNull(r2);
        }

        static /* synthetic */ int access$000(ReverseList r1, int r2) {
            return r1.reversePosition(r2);
        }

        private int reverseIndex(int r3) {
            int r0 = size();
            Preconditions.checkElementIndex(r3, r0);
            return (r0 - 1) - r3;
        }

        private int reversePosition(int r3) {
            int r0 = size();
            Preconditions.checkPositionIndex(r3, r0);
            return r0 - r3;
        }

        @Override // java.util.AbstractList, java.util.List
        public void add(int r3, @ParametricNullness T r4) {
            this.forwardList.add(reversePosition(r3), r4);
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.forwardList.clear();
        }

        @Override // java.util.AbstractList, java.util.List
        @ParametricNullness
        public T get(int r3) {
            return this.forwardList.get(reverseIndex(r3));
        }

        List<T> getForwardList() {
            return this.forwardList;
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<T> iterator() {
            return listIterator();
        }

        @Override // java.util.AbstractList, java.util.List
        public ListIterator<T> listIterator(int r4) {
            int r0 = reversePosition(r4);
            final ListIterator<T> r1 = this.forwardList.listIterator(r0);
            return new C06701(this, r1);
        }

        @Override // java.util.AbstractList, java.util.List
        @ParametricNullness
        public T remove(int r3) {
            return this.forwardList.remove(reverseIndex(r3));
        }

        @Override // java.util.AbstractList
        protected void removeRange(int r2, int r3) {
            subList(r2, r3).clear();
        }

        @Override // java.util.AbstractList, java.util.List
        @ParametricNullness
        public T set(int r3, @ParametricNullness T r4) {
            return this.forwardList.set(reverseIndex(r3), r4);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.forwardList.size();
        }

        @Override // java.util.AbstractList, java.util.List
        public List<T> subList(int r4, int r5) {
            Preconditions.checkPositionIndexes(r4, r5, size());
            return Lists.reverse(this.forwardList.subList(reversePosition(r5), reversePosition(r4)));
        }
    }

    private static final class StringAsImmutableList extends ImmutableList<Character> {
        private final String string;

        StringAsImmutableList(String r1) {
            this.string = r1;
        }

        @Override // java.util.List
        public Character get(int r2) {
            Preconditions.checkElementIndex(r2, size());
            return Character.valueOf(this.string.charAt(r2));
        }

        @Override // java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public int indexOf(@CheckForNull Object r3) {
            if ((r3 instanceof Character) == true) goto L7;
            return -1;
        L7:
            return this.string.indexOf(((Character) r3).charValue());
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return false;
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public int lastIndexOf(@CheckForNull Object r3) {
            if ((r3 instanceof Character) == true) goto L7;
            return -1;
        L7:
            return this.string.lastIndexOf(((Character) r3).charValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.string.length();
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public ImmutableList<Character> subList(int r2, int r3) {
            Preconditions.checkPositionIndexes(r2, r3, size());
            return Lists.charactersOf(this.string.substring(r2, r3));
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public /* bridge */ /* synthetic */ List subList(int r1, int r2) {
            return subList(r1, r2);
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    private static class TransformingRandomAccessList<F, T> extends AbstractList<T> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final List<F> fromList;
        final Function<? super F, ? extends T> function;

        TransformingRandomAccessList(List<F> r2, Function<? super F, ? extends T> r3) {
            this.fromList = (List) Preconditions.checkNotNull(r2);
            this.function = (Function) Preconditions.checkNotNull(r3);
        }

        @Override // java.util.AbstractList, java.util.List
        @ParametricNullness
        public T get(int r3) {
            return this.function.apply(this.fromList.get(r3));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean isEmpty() {
            return this.fromList.isEmpty();
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<T> iterator() {
            return listIterator();
        }

        @Override // java.util.AbstractList, java.util.List
        public ListIterator<T> listIterator(int r3) {
            final ListIterator<F> r1 = this.fromList.listIterator(r3);
            return new C06711(this, r1);
        }

        @Override // java.util.AbstractList, java.util.List
        public T remove(int r3) {
            return this.function.apply(this.fromList.remove(r3));
        }

        @Override // java.util.AbstractList
        protected void removeRange(int r2, int r3) {
            this.fromList.subList(r2, r3).clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.fromList.size();
        }
    }

    private static class TransformingSequentialList<F, T> extends AbstractSequentialList<T> implements Serializable {
        private static final long serialVersionUID = 0;
        final List<F> fromList;
        final Function<? super F, ? extends T> function;

        TransformingSequentialList(List<F> r2, Function<? super F, ? extends T> r3) {
            this.fromList = (List) Preconditions.checkNotNull(r2);
            this.function = (Function) Preconditions.checkNotNull(r3);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean isEmpty() {
            return this.fromList.isEmpty();
        }

        @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public ListIterator<T> listIterator(int r3) {
            final ListIterator<F> r1 = this.fromList.listIterator(r3);
            return new C06721(this, r1);
        }

        @Override // java.util.AbstractList
        protected void removeRange(int r2, int r3) {
            this.fromList.subList(r2, r3).clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.fromList.size();
        }
    }

    private static class TwoPlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {
        private static final long serialVersionUID = 0;

        @ParametricNullness
        final E first;
        final E[] rest;

        @ParametricNullness
        final E second;

        TwoPlusArrayList(@ParametricNullness E r2, @ParametricNullness E r3, E[] r4) {
            this.first = r2;
            this.second = r3;
            this.rest = (E[]) ((Object[]) Preconditions.checkNotNull(r4));
        }

        @Override // java.util.AbstractList, java.util.List
        @ParametricNullness
        public E get(int r3) {
            switch(r3) {
                case 0: goto L8;
                case 1: goto L6;
                default: goto L3;
            };
        L3:
            Preconditions.checkElementIndex(r3, size());
            return this.rest[r3 - 2];
        L6:
            return this.second;
        L8:
            return this.first;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return IntMath.saturatedAdd(this.rest.length, 2);
        }
    }

    private Lists() {
    }

    static <E> boolean addAllImpl(List<E> r4, int r5, Iterable<? extends E> r6) {
        boolean r0 = false;
        ListIterator<E> r1 = r4.listIterator(r5);
        Iterator<? extends E> r2 = r6.iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        r1.add(r2.next());
        r0 = true;
        goto L4
    L6:
        return r0;
    }

    public static <E> List<E> asList(@ParametricNullness E r1, @ParametricNullness E r2, E[] r3) {
        return new TwoPlusArrayList(r1, r2, r3);
    }

    public static <E> List<E> asList(@ParametricNullness E r1, E[] r2) {
        return new OnePlusArrayList(r1, r2);
    }

    public static <B> List<List<B>> cartesianProduct(List<? extends List<? extends B>> r1) {
        return CartesianList.create(r1);
    }

    @SafeVarargs
    public static <B> List<List<B>> cartesianProduct(List<? extends B>... r1) {
        return cartesianProduct(Arrays.asList(r1));
    }

    static <T> List<T> cast(Iterable<T> r1) {
        return (List) r1;
    }

    public static ImmutableList<Character> charactersOf(String r2) {
        return new StringAsImmutableList((String) Preconditions.checkNotNull(r2));
    }

    public static List<Character> charactersOf(CharSequence r2) {
        return new CharSequenceAsList((CharSequence) Preconditions.checkNotNull(r2));
    }

    static int computeArrayListCapacity(int r4) {
        CollectPreconditions.checkNonnegative(r4, "arraySize");
        return Ints.saturatedCast((((long) r4) + 5) + ((long) (r4 / 10)));
    }

    static boolean equalsImpl(List<?> r7, @CheckForNull Object r8) {
        if (r8 != Preconditions.checkNotNull(r7)) goto L6;
        return true;
    L6:
        if ((r8 instanceof List) == true) goto L8;
        return false;
    L8:
        List r0 = (List) r8;
        int r3 = r7.size();
        if (r3 == r0.size()) goto L12;
        return false;
    L12:
        if ((r7 instanceof RandomAccess) == false) goto L23;
        if ((r0 instanceof RandomAccess) == false) goto L23;
        int r4 = 0;
    L16:
        if (r4 >= r3) goto L21;
        if (Objects.equal(r7.get(r4), r0.get(r4)) == false) goto L19;
        r4 = r4 + 1;
        goto L16
    L19:
        return false;
    L21:
        return true;
    L23:
        return Iterators.elementsEqual(r7.iterator(), r0.iterator());
    }

    static int hashCodeImpl(List<?> r5) {
        int r0 = 1;
        Iterator<?> r1 = r5.iterator();
    L4:
        if (r1.hasNext() == false) goto L10;
        Object r2 = r1.next();
        int r3 = r0 * 31;
        if (r2 != null) goto L8;
        int r4 = 0;
    L9:
        r0 = ~(~(r3 + r4));
        goto L4
    L8:
        r4 = r2.hashCode();
        goto L9
    L10:
        return r0;
    }

    static int indexOfImpl(List<?> r2, @CheckForNull Object r3) {
        if ((r2 instanceof RandomAccess) == true) goto L5;
        ListIterator<?> r0 = r2.listIterator();
    L8:
        if (r0.hasNext() == false) goto L13;
        if (Objects.equal(r3, r0.next()) == false) goto L8;
        return r0.previousIndex();
    L13:
        return -1;
    L5:
        return indexOfRandomAccess(r2, r3);
    }

    private static int indexOfRandomAccess(List<?> r3, @CheckForNull Object r4) {
        int r0 = r3.size();
        if (r4 != null) goto L11;
        int r1 = 0;
    L5:
        if (r1 >= r0) goto L17;
        if (r3.get(r1) == null) goto L8;
        r1 = r1 + 1;
        goto L5
    L8:
        return r1;
    L17:
        return -1;
    L11:
        int r2 = 0;
    L12:
        if (r2 >= r0) goto L23;
        if (r4.equals(r3.get(r2)) == true) goto L15;
        r2 = r2 + 1;
        goto L12
    L15:
        return r2;
    L23:
        return -1;
    }

    static int lastIndexOfImpl(List<?> r2, @CheckForNull Object r3) {
        if ((r2 instanceof RandomAccess) == true) goto L5;
        ListIterator<?> r0 = r2.listIterator(r2.size());
    L8:
        if (r0.hasPrevious() == false) goto L13;
        if (Objects.equal(r3, r0.previous()) == false) goto L8;
        return r0.nextIndex();
    L13:
        return -1;
    L5:
        return lastIndexOfRandomAccess(r2, r3);
    }

    private static int lastIndexOfRandomAccess(List<?> r2, @CheckForNull Object r3) {
        if (r3 != null) goto L10;
        int r0 = r2.size() - 1;
    L4:
        if (r0 < 0) goto L16;
        if (r2.get(r0) == null) goto L7;
        r0 = r0 - 1;
        goto L4
    L7:
        return r0;
    L16:
        return -1;
    L10:
        int r1 = r2.size() - 1;
    L11:
        if (r1 < 0) goto L22;
        if (r3.equals(r2.get(r1)) == true) goto L14;
        r1 = r1 - 1;
        goto L11
    L14:
        return r1;
    L22:
        return -1;
    }

    static <E> ListIterator<E> listIteratorImpl(List<E> r1, int r2) {
        return new AbstractListWrapper(r1).listIterator(r2);
    }

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList();
    }

    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> r2) {
        Preconditions.checkNotNull(r2);
        if ((r2 instanceof Collection) == false) goto L6;
        return new ArrayList((Collection) r2);
    L6:
        return newArrayList(r2.iterator());
    }

    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> r1) {
        ArrayList<E> r0 = newArrayList();
        Iterators.addAll(r0, r1);
        return r0;
    }

    @SafeVarargs
    public static <E> ArrayList<E> newArrayList(E... r2) {
        Preconditions.checkNotNull(r2);
        ArrayList<E> r1 = new ArrayList(computeArrayListCapacity(r2.length));
        Collections.addAll(r1, r2);
        return r1;
    }

    public static <E> ArrayList<E> newArrayListWithCapacity(int r1) {
        CollectPreconditions.checkNonnegative(r1, "initialArraySize");
        return new ArrayList(r1);
    }

    public static <E> ArrayList<E> newArrayListWithExpectedSize(int r2) {
        return new ArrayList(computeArrayListCapacity(r2));
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList();
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<? extends E> r2) {
        if ((r2 instanceof Collection) == false) goto L5;
        Collection r0 = (Collection) r2;
    L7:
        return new CopyOnWriteArrayList(r0);
    L5:
        r0 = newArrayList(r2);
        goto L7
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList();
    }

    public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> r1) {
        LinkedList<E> r0 = newLinkedList();
        Iterables.addAll(r0, r1);
        return r0;
    }

    public static <T> List<List<T>> partition(List<T> r1, int r2) {
        Preconditions.checkNotNull(r1);
        if (r2 <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        if ((r1 instanceof RandomAccess) == false) goto L10;
        return new RandomAccessPartition(r1, r2);
    L10:
        return new Partition(r1, r2);
    L5:
        r0 = false;
        goto L6
    }

    public static <T> List<T> reverse(List<T> r2) {
        if ((r2 instanceof ImmutableList) == false) goto L7;
        return ((ImmutableList) r2).reverse();
    L7:
        if ((r2 instanceof ReverseList) == false) goto L11;
        return ((ReverseList) r2).getForwardList();
    L11:
        if ((r2 instanceof RandomAccess) == false) goto L15;
        return new RandomAccessReverseList(r2);
    L15:
        return new ReverseList(r2);
    }

    static <E> List<E> subListImpl(final List<E> r2, int r3, int r4) {
        if ((r2 instanceof RandomAccess) == false) goto L5;
        List r0 = new C06681(r2);
    L7:
        return r0.subList(r3, r4);
    L5:
        r0 = new C06692(r2);
        goto L7
    }

    public static <F, T> List<T> transform(List<F> r1, Function<? super F, ? extends T> r2) {
        if ((r1 instanceof RandomAccess) == false) goto L6;
        return new TransformingRandomAccessList(r1, r2);
    L6:
        return new TransformingSequentialList(r1, r2);
    }
}
