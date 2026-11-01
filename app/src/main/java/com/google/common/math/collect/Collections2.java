package com.google.common.collect;

import com.arthenica.ffmpegkit.MediaInformation;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.math.IntMath;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Collections2 {

    static class FilteredCollection<E> extends AbstractCollection<E> {
        final Predicate<? super E> predicate;
        final Collection<E> unfiltered;

        FilteredCollection(Collection<E> r1, Predicate<? super E> r2) {
            this.unfiltered = r1;
            this.predicate = r2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean add(@ParametricNullness E r2) {
            Preconditions.checkArgument(this.predicate.apply(r2));
            return this.unfiltered.add(r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> r4) {
            Iterator<? extends E> r0 = r4.iterator();
        L4:
            if (r0.hasNext() == false) goto L7;
            E r1 = r0.next();
            Preconditions.checkArgument(this.predicate.apply(r1));
            goto L4
        L7:
            return this.unfiltered.addAll(r4);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            Iterables.removeIf(this.unfiltered, this.predicate);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(@CheckForNull Object r3) {
            if (Collections2.safeContains(this.unfiltered, r3) == true) goto L5;
            return false;
        L5:
            return this.predicate.apply(r3);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> r2) {
            return Collections2.containsAllImpl(this, r2);
        }

        FilteredCollection<E> createCombined(Predicate<? super E> r4) {
            return new FilteredCollection(this.unfiltered, Predicates.and(this.predicate, r4));
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return !Iterables.any(this.unfiltered, this.predicate);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<E> iterator() {
            return Iterators.filter(this.unfiltered.iterator(), this.predicate);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(@CheckForNull Object r2) {
            if (contains(r2) == true) goto L5;
        L7:
            return false;
        L5:
            if (this.unfiltered.remove(r2) == false) goto L7;
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> r5) {
            boolean r0 = false;
            Iterator<E> r1 = this.unfiltered.iterator();
        L4:
            if (r1.hasNext() == false) goto L11;
            E r2 = r1.next();
            if (this.predicate.apply(r2) == false) goto L4;
            if (r5.contains(r2) == false) goto L4;
            r1.remove();
            r0 = true;
            goto L4
        L11:
            return r0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> r5) {
            boolean r0 = false;
            Iterator<E> r1 = this.unfiltered.iterator();
        L4:
            if (r1.hasNext() == false) goto L11;
            E r2 = r1.next();
            if (this.predicate.apply(r2) == false) goto L4;
            if (r5.contains(r2) == true) goto L4;
            r1.remove();
            r0 = true;
            goto L4
        L11:
            return r0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            int r0 = 0;
            Iterator<E> r1 = this.unfiltered.iterator();
        L4:
            if (r1.hasNext() == false) goto L9;
            E r2 = r1.next();
            if (this.predicate.apply(r2) == false) goto L4;
            r0 = r0 + 1;
            goto L4
        L9:
            return r0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public Object[] toArray() {
            return Lists.newArrayList(iterator()).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] r2) {
            return (T[]) Lists.newArrayList(iterator()).toArray(r2);
        }
    }

    private static final class OrderedPermutationCollection<E> extends AbstractCollection<List<E>> {
        final Comparator<? super E> comparator;
        final ImmutableList<E> inputList;
        final int size;

        OrderedPermutationCollection(Iterable<E> r2, Comparator<? super E> r3) {
            this.inputList = ImmutableList.sortedCopyOf(r3, r2);
            this.comparator = r3;
            this.size = calculateSize(this.inputList, r3);
        }

        private static <E> int calculateSize(List<E> r5, Comparator<? super E> r6) {
            int r0 = 1;
            int r1 = 1;
            int r2 = 1;
        L4:
            if (r1 >= r5.size()) goto L12;
            if (r6.compare(r5.get(r1 - 1), r5.get(r1)) >= 0) goto L10;
            r0 = IntMath.saturatedMultiply(r0, IntMath.binomial(r1, r2));
            r2 = 0;
            if (r0 != Integer.MAX_VALUE) goto L10;
            return Integer.MAX_VALUE;
        L10:
            r1 = r1 + 1;
            r2 = r2 + 1;
            goto L4
        L12:
            return IntMath.saturatedMultiply(r0, IntMath.binomial(r1, r2));
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(@CheckForNull Object r3) {
            if ((r3 instanceof List) == false) goto L6;
            ImmutableList<E> r1 = this.inputList;
            return Collections2.access$000(r1, (List) r3);
        L6:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<List<E>> iterator() {
            return new OrderedPermutationIterator(this.inputList, this.comparator);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.size;
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            return "orderedPermutationCollection(" + this.inputList + ")";
        }
    }

    private static final class OrderedPermutationIterator<E> extends AbstractIterator<List<E>> {
        final Comparator<? super E> comparator;

        @CheckForNull
        List<E> nextPermutation;

        OrderedPermutationIterator(List<E> r2, Comparator<? super E> r3) {
            this.nextPermutation = Lists.newArrayList(r2);
            this.comparator = r3;
        }

        void calculateNextPermutation() {
            int r0 = findNextJ();
            if (r0 != (-1)) goto L6;
            this.nextPermutation = null;
            return;
        L6:
            Objects.requireNonNull(this.nextPermutation);
            int r1 = findNextL(r0);
            Collections.swap(this.nextPermutation, r0, r1);
            int r2 = this.nextPermutation.size();
            Collections.reverse(this.nextPermutation.subList(r0 + 1, r2));
        }

        @Override // com.google.common.collect.AbstractIterator
        @CheckForNull
        protected /* bridge */ /* synthetic */ Object computeNext() {
            return computeNext();
        }

        @Override // com.google.common.collect.AbstractIterator
        @CheckForNull
        protected List<E> computeNext() {
            if (this.nextPermutation == null) goto L5;
            ImmutableList r0 = ImmutableList.copyOf(this.nextPermutation);
            calculateNextPermutation();
            return r0;
        L5:
            return endOfData();
        }

        int findNextJ() {
            Objects.requireNonNull(this.nextPermutation);
            int r0 = this.nextPermutation.size() - 2;
        L3:
            if (r0 < 0) goto L8;
            if (this.comparator.compare(this.nextPermutation.get(r0), this.nextPermutation.get(r0 + 1)) < 0) goto L6;
            r0 = r0 - 1;
            goto L3
        L6:
            return r0;
        L8:
            return -1;
        }

        int findNextL(int r5) {
            Objects.requireNonNull(this.nextPermutation);
            E r0 = this.nextPermutation.get(r5);
            int r1 = this.nextPermutation.size() - 1;
        L3:
            if (r1 <= r5) goto L9;
            if (this.comparator.compare(r0, this.nextPermutation.get(r1)) < 0) goto L6;
            r1 = r1 - 1;
            goto L3
        L6:
            return r1;
        L9:
            throw new AssertionError("this statement should be unreachable");
        }
    }

    private static final class PermutationCollection<E> extends AbstractCollection<List<E>> {
        final ImmutableList<E> inputList;

        PermutationCollection(ImmutableList<E> r1) {
            this.inputList = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(@CheckForNull Object r3) {
            if ((r3 instanceof List) == false) goto L6;
            ImmutableList<E> r1 = this.inputList;
            return Collections2.access$000(r1, (List) r3);
        L6:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<List<E>> iterator() {
            return new PermutationIterator(this.inputList);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return IntMath.factorial(this.inputList.size());
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            return "permutations(" + this.inputList + ")";
        }
    }

    private static class PermutationIterator<E> extends AbstractIterator<List<E>> {

        /* JADX INFO: renamed from: c */
        final int[] f113c;

        /* JADX INFO: renamed from: j */
        int f114j;
        final List<E> list;

        /* JADX INFO: renamed from: o */
        final int[] f115o;

        PermutationIterator(List<E> r4) {
            this.list = new ArrayList(r4);
            int r0 = r4.size();
            this.f113c = new int[r0];
            this.f115o = new int[r0];
            Arrays.fill(this.f113c, 0);
            Arrays.fill(this.f115o, 1);
            this.f114j = Integer.MAX_VALUE;
        }

        void calculateNextPermutation() {
            this.f114j = this.list.size() - 1;
            int r0 = 0;
            if (this.f114j != (-1)) goto L5;
            return;
        L5:
            int r1 = this.f113c[this.f114j] + this.f115o[this.f114j];
            if (r1 < 0) goto L7;
            if (r1 != (this.f114j + 1)) goto L14;
            if (this.f114j == 0) goto L23;
            r0 = r0 + 1;
            switchDirection();
            goto L5
        L23:
            return;
        L14:
            Collections.swap(this.list, (this.f114j - this.f113c[this.f114j]) + r0, (this.f114j - r1) + r0);
            this.f113c[this.f114j] = r1;
            return;
        L7:
            switchDirection();
            goto L5
        }

        @Override // com.google.common.collect.AbstractIterator
        @CheckForNull
        protected /* bridge */ /* synthetic */ Object computeNext() {
            return computeNext();
        }

        @Override // com.google.common.collect.AbstractIterator
        @CheckForNull
        protected List<E> computeNext() {
            if (this.f114j <= 0) goto L5;
            ImmutableList r0 = ImmutableList.copyOf(this.list);
            calculateNextPermutation();
            return r0;
        L5:
            return endOfData();
        }

        void switchDirection() {
            this.f115o[this.f114j] = -this.f115o[this.f114j];
            this.f114j--;
        }
    }

    static class TransformedCollection<F, T> extends AbstractCollection<T> {
        final Collection<F> fromCollection;
        final Function<? super F, ? extends T> function;

        TransformedCollection(Collection<F> r2, Function<? super F, ? extends T> r3) {
            this.fromCollection = (Collection) Preconditions.checkNotNull(r2);
            this.function = (Function) Preconditions.checkNotNull(r3);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            this.fromCollection.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.fromCollection.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<T> iterator() {
            return Iterators.transform(this.fromCollection.iterator(), this.function);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.fromCollection.size();
        }
    }

    private Collections2() {
    }

    static /* synthetic */ boolean access$000(List r1, List r2) {
        return isPermutation(r1, r2);
    }

    static boolean containsAllImpl(Collection<?> r3, Collection<?> r4) {
        Iterator<?> r0 = r4.iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (r3.contains(r0.next()) == true) goto L4;
        return false;
    L10:
        return true;
    }

    private static <E> ObjectCountHashMap<E> counts(Collection<E> r4) {
        ObjectCountHashMap<E> r0 = new ObjectCountHashMap();
        Iterator<E> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        E r2 = r1.next();
        r0.put(r2, r0.get(r2) + 1);
        goto L4
    L6:
        return r0;
    }

    public static <E> Collection<E> filter(Collection<E> r3, Predicate<? super E> r4) {
        if ((r3 instanceof FilteredCollection) == false) goto L7;
        return ((FilteredCollection) r3).createCombined(r4);
    L7:
        return new FilteredCollection((Collection) Preconditions.checkNotNull(r3), (Predicate) Preconditions.checkNotNull(r4));
    }

    private static boolean isPermutation(List<?> r6, List<?> r7) {
        if (r6.size() == r7.size()) goto L5;
        return false;
    L5:
        ObjectCountHashMap r0 = counts(r6);
        ObjectCountHashMap r1 = counts(r7);
        if (r6.size() == r7.size()) goto L8;
        return false;
    L8:
        int r3 = 0;
    L10:
        if (r3 >= r6.size()) goto L15;
        if (r0.getValue(r3) != r1.get(r0.getKey(r3))) goto L13;
        r3 = r3 + 1;
        goto L10
    L13:
        return false;
    L15:
        return true;
    }

    static StringBuilder newStringBuilderForCollection(int r5) {
        CollectPreconditions.checkNonnegative(r5, MediaInformation.KEY_SIZE);
        return new StringBuilder((int) Math.min(((long) r5) * 8, 1073741824));
    }

    public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(Iterable<E> r1) {
        return orderedPermutations(r1, Ordering.natural());
    }

    public static <E> Collection<List<E>> orderedPermutations(Iterable<E> r1, Comparator<? super E> r2) {
        return new OrderedPermutationCollection(r1, r2);
    }

    public static <E> Collection<List<E>> permutations(Collection<E> r2) {
        return new PermutationCollection(ImmutableList.copyOf(r2));
    }

    static boolean safeContains(Collection<?> r2, @CheckForNull Object r3) {
        Preconditions.checkNotNull(r2);
        return r2.contains(r3);
    L8:
        return false;
    }

    static boolean safeRemove(Collection<?> r2, @CheckForNull Object r3) {
        Preconditions.checkNotNull(r2);
        return r2.remove(r3);
    L8:
        return false;
    }

    static String toStringImpl(Collection<?> r5) {
        StringBuilder r0 = newStringBuilderForCollection(r5.size()).append('[');
        boolean r1 = true;
        Iterator<?> r2 = r5.iterator();
    L4:
        if (r2.hasNext() == false) goto L14;
        Object r3 = r2.next();
        if (r1 == true) goto L8;
        r0.append(", ");
    L8:
        r1 = false;
        if (r3 == r5) goto L10;
        r0.append(r3);
        goto L4
    L10:
        r0.append("(this Collection)");
        goto L4
    L14:
        return r0.append(']').toString();
    }

    public static <F, T> Collection<T> transform(Collection<F> r1, Function<? super F, T> r2) {
        return new TransformedCollection(r1, r2);
    }
}
