package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ImmutableSortedMultiset<E> extends ImmutableMultiset<E> implements SortedMultiset<E> {
    private static final long serialVersionUID = 912559;

    @CheckForNull
    @LazyInit
    transient ImmutableSortedMultiset<E> descendingMultiset;

    public static class Builder<E> extends ImmutableMultiset.Builder<E> {
        private final Comparator<? super E> comparator;
        private int[] counts;
        E[] elements;
        private boolean forceCopyElements;
        private int length;

        public Builder(Comparator<? super E> r3) {
            super(true);
            this.comparator = (Comparator) Preconditions.checkNotNull(r3);
            this.elements = (E[]) new Object[4];
            this.counts = new int[4];
        }

        private void dedupAndCoalesce(boolean r8) {
            if (this.length != 0) goto L5;
            return;
        L5:
            Object[] r0 = (E[]) Arrays.copyOf(this.elements, this.length);
            Arrays.sort(r0, this.comparator);
            int r1 = 1;
            int r2 = 1;
        L7:
            if (r2 >= r0.length) goto L12;
            if (this.comparator.compare((Object) r0[r1 - 1], (Object) r0[r2]) >= 0) goto L11;
            r0[r1] = r0[r2];
            r1 = r1 + 1;
        L11:
            r2 = r2 + 1;
            goto L7
        L12:
            Arrays.fill(r0, r1, this.length, null);
            if (r8 == true) goto L15;
        L17:
            int[] r3 = new int[r0.length];
            int r4 = 0;
        L19:
            if (r4 >= this.length) goto L25;
            int r5 = Arrays.binarySearch(r0, 0, r1, this.elements[r4], this.comparator);
            if (this.counts[r4] < 0) goto L23;
            r3[r5] = r3[r5] + this.counts[r4];
        L24:
            r4 = r4 + 1;
            goto L19
        L23:
            r3[r5] = ~this.counts[r4];
            goto L24
        L25:
            this.elements = (E[]) r0;
            this.counts = r3;
            this.length = r1;
            return;
        L15:
            if ((r1 * 4) <= (this.length * 3)) goto L17;
            r0 = (E[]) Arrays.copyOf(r0, IntMath.saturatedAdd(this.length, (this.length / 2) + 1));
            goto L17
        }

        private void dedupAndCoalesceAndDeleteEmpty() {
            dedupAndCoalesce(false);
            int r1 = 0;
            int r2 = 0;
        L4:
            if (r2 >= this.length) goto L9;
            if (this.counts[r2] <= 0) goto L8;
            this.elements[r1] = this.elements[r2];
            this.counts[r1] = this.counts[r2];
            r1 = r1 + 1;
        L8:
            r2 = r2 + 1;
            goto L4
        L9:
            Arrays.fill(this.elements, r1, this.length, null);
            Arrays.fill(this.counts, r1, this.length, 0);
            this.length = r1;
        }

        private void maintenance() {
            if (this.length != this.elements.length) goto L6;
            dedupAndCoalesce(true);
        L8:
            this.forceCopyElements = false;
            return;
        L6:
            if (this.forceCopyElements == false) goto L8;
            this.elements = (E[]) Arrays.copyOf(this.elements, this.elements.length);
            goto L8
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder add(Object r1) {
            return add(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder add(Object[] r1) {
            return add(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableMultiset.Builder add(Object r1) {
            return add(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableMultiset.Builder add(Object[] r1) {
            return add(r1);
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E r2) {
            return addCopies(r2, 1);
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E... r4) {
            int r0 = r4.length;
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L5;
            add(r4[r1]);
            r1 = r1 + 1;
            goto L3
        L5:
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder addAll(Iterable r1) {
            return addAll(r1);
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder addAll(Iterator r1) {
            return addAll(r1);
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableMultiset.Builder addAll(Iterable r1) {
            return addAll(r1);
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableMultiset.Builder addAll(Iterator r1) {
            return addAll(r1);
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterable<? extends E> r5) {
            if ((r5 instanceof Multiset) == false) goto L9;
            Iterator<Multiset.Entry<E>> r0 = ((Multiset) r5).entrySet().iterator();
        L6:
            if (r0.hasNext() == false) goto L13;
            Multiset.Entry<E> r1 = r0.next();
            addCopies(r1.getElement(), r1.getCount());
        L13:
            return this;
        L9:
            Iterator<? extends E> r2 = r5.iterator();
        L11:
            if (r2.hasNext() == false) goto L13;
            add(r2.next());
            goto L11
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterator<? extends E> r2) {
        L3:
            if (r2.hasNext() == false) goto L5;
            add(r2.next());
            goto L3
        L5:
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultiset.Builder
        public /* bridge */ /* synthetic */ ImmutableMultiset.Builder addCopies(Object r1, int r2) {
            return addCopies(r1, r2);
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder
        public Builder<E> addCopies(E r3, int r4) {
            Preconditions.checkNotNull(r3);
            CollectPreconditions.checkNonnegative(r4, "occurrences");
            if (r4 != 0) goto L5;
            return this;
        L5:
            maintenance();
            this.elements[this.length] = r3;
            this.counts[this.length] = r4;
            this.length++;
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection build() {
            return build();
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableMultiset build() {
            return build();
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public ImmutableSortedMultiset<E> build() {
            dedupAndCoalesceAndDeleteEmpty();
            if (this.length == 0) goto L5;
            RegularImmutableSortedSet r0 = (RegularImmutableSortedSet) ImmutableSortedSet.construct(this.comparator, this.length, this.elements);
            long[] r1 = new long[this.length + 1];
            int r3 = 0;
        L8:
            if (r3 >= this.length) goto L10;
            r1[r3 + 1] = r1[r3] + ((long) this.counts[r3]);
            r3 = r3 + 1;
            goto L8
        L10:
            this.forceCopyElements = true;
            return new RegularImmutableSortedMultiset(r0, r1, 0, this.length);
        L5:
            return ImmutableSortedMultiset.emptyMultiset(this.comparator);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultiset.Builder
        public /* bridge */ /* synthetic */ ImmutableMultiset.Builder setCount(Object r1, int r2) {
            return setCount(r1, r2);
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder
        public Builder<E> setCount(E r4, int r5) {
            Preconditions.checkNotNull(r4);
            CollectPreconditions.checkNonnegative(r5, "count");
            maintenance();
            this.elements[this.length] = r4;
            this.counts[this.length] = ~r5;
            this.length++;
            return this;
        }
    }

    private static final class SerializedForm<E> implements Serializable {
        final Comparator<? super E> comparator;
        final int[] counts;
        final E[] elements;

        SerializedForm(SortedMultiset<E> r7) {
            this.comparator = r7.comparator();
            int r0 = r7.entrySet().size();
            this.elements = (E[]) new Object[r0];
            this.counts = new int[r0];
            int r1 = 0;
            Iterator<Multiset.Entry<E>> r2 = r7.entrySet().iterator();
        L4:
            if (r2.hasNext() == false) goto L6;
            Multiset.Entry<E> r3 = r2.next();
            this.elements[r1] = r3.getElement();
            this.counts[r1] = r3.getCount();
            r1 = r1 + 1;
            goto L4
        }

        Object readResolve() {
            int r0 = this.elements.length;
            Builder r1 = new Builder(this.comparator);
            int r2 = 0;
        L3:
            if (r2 >= r0) goto L6;
            r1.addCopies(this.elements[r2], this.counts[r2]);
            r2 = r2 + 1;
            goto L3
        L6:
            return r1.build();
        }
    }

    ImmutableSortedMultiset() {
    }

    @Deprecated
    public static <E> Builder<E> builder() {
        throw new UnsupportedOperationException();
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Iterable<? extends E> r2) {
        return copyOf(Ordering.natural(), r2);
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> r2, Iterable<? extends E> r3) {
        if ((r3 instanceof ImmutableSortedMultiset) == false) goto L12;
        ImmutableSortedMultiset<E> r0 = (ImmutableSortedMultiset) r3;
        if (r2.equals(r0.comparator()) == false) goto L12;
        if (r0.isPartialView() == true) goto L9;
        return r0;
    L9:
        return copyOfSortedEntries(r2, r0.entrySet().asList());
    L12:
        return new Builder(r2).addAll(r3).build();
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> r1, Iterator<? extends E> r2) {
        Preconditions.checkNotNull(r1);
        return new Builder(r1).addAll(r2).build();
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Iterator<? extends E> r2) {
        return copyOf(Ordering.natural(), r2);
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>([TE;)Lcom/google/common/collect/ImmutableSortedMultiset<TE;>; */
    public static ImmutableSortedMultiset copyOf(Comparable[] r2) {
        return copyOf(Ordering.natural(), Arrays.asList(r2));
    }

    @Deprecated
    public static <Z> ImmutableSortedMultiset<Z> copyOf(Z[] r1) {
        throw new UnsupportedOperationException();
    }

    public static <E> ImmutableSortedMultiset<E> copyOfSorted(SortedMultiset<E> r2) {
        return copyOfSortedEntries(r2.comparator(), Lists.newArrayList(r2.entrySet()));
    }

    private static <E> ImmutableSortedMultiset<E> copyOfSortedEntries(Comparator<? super E> r10, Collection<Multiset.Entry<E>> r11) {
        if (r11.isEmpty() == true) goto L5;
        ImmutableList.Builder r0 = new ImmutableList.Builder(r11.size());
        long[] r1 = new long[r11.size() + 1];
        int r2 = 0;
        Iterator<Multiset.Entry<E>> r3 = r11.iterator();
    L8:
        if (r3.hasNext() == false) goto L11;
        Multiset.Entry<E> r4 = r3.next();
        r0.add(r4.getElement());
        r1[r2 + 1] = r1[r2] + ((long) r4.getCount());
        r2 = r2 + 1;
        goto L8
    L11:
        return new RegularImmutableSortedMultiset(new RegularImmutableSortedSet(r0.build(), r10), r1, 0, r11.size());
    L5:
        return emptyMultiset(r10);
    }

    static <E> ImmutableSortedMultiset<E> emptyMultiset(Comparator<? super E> r1) {
        if (Ordering.natural().equals(r1) == false) goto L7;
        return (ImmutableSortedMultiset<E>) RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
    L7:
        return new RegularImmutableSortedMultiset(r1);
    }

    static /* synthetic */ int lambda$toImmutableSortedMultiset$0(Object r1) {
        return 1;
    }

    static /* synthetic */ Multiset lambda$toImmutableSortedMultiset$1(Comparator r1) {
        return TreeMultiset.create(r1);
    }

    static /* synthetic */ void lambda$toImmutableSortedMultiset$2(Function r0, ToIntFunction r1, Multiset r2, Object r3) {
        mapAndAdd(r3, r2, r0, r1);
    }

    static /* synthetic */ Multiset lambda$toImmutableSortedMultiset$3(Multiset r0, Multiset r1) {
        r0.addAll(r1);
        return r0;
    }

    static /* synthetic */ ImmutableSortedMultiset lambda$toImmutableSortedMultiset$4(Comparator r1, Multiset r2) {
        return copyOfSortedEntries(r1, r2.entrySet());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T, E> void mapAndAdd(T r2, Multiset<E> r3, Function<? super T, ? extends E> r4, ToIntFunction<? super T> r5) {
        r3.add(Preconditions.checkNotNull(r4.apply(r2)), r5.applyAsInt(r2));
    }

    public static <E extends Comparable<?>> Builder<E> naturalOrder() {
        return new Builder(Ordering.natural());
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedMultiset<E> m294of() {
        return (ImmutableSortedMultiset<E>) RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;)Lcom/google/common/collect/ImmutableSortedMultiset<TE;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMultiset m295of(Comparable r5) {
        return new RegularImmutableSortedMultiset((RegularImmutableSortedSet) ImmutableSortedSet.m308of(r5), new long[]{0, 1}, 0, 1);
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;)Lcom/google/common/collect/ImmutableSortedMultiset<TE;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMultiset m296of(Comparable r3, Comparable r4) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{r3, r4}));
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;TE;)Lcom/google/common/collect/ImmutableSortedMultiset<TE;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMultiset m297of(Comparable r3, Comparable r4, Comparable r5) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{r3, r4, r5}));
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;TE;TE;)Lcom/google/common/collect/ImmutableSortedMultiset<TE;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMultiset m298of(Comparable r3, Comparable r4, Comparable r5, Comparable r6) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{r3, r4, r5, r6}));
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;TE;TE;TE;)Lcom/google/common/collect/ImmutableSortedMultiset<TE;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMultiset m299of(Comparable r3, Comparable r4, Comparable r5, Comparable r6, Comparable r7) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{r3, r4, r5, r6, r7}));
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;TE;TE;TE;TE;[TE;)Lcom/google/common/collect/ImmutableSortedMultiset<TE;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedMultiset m300of(Comparable r4, Comparable r5, Comparable r6, Comparable r7, Comparable r8, Comparable r9, Comparable... r10) {
        ArrayList r2 = Lists.newArrayListWithCapacity(r10.length + 6);
        Collections.addAll(r2, new Comparable[]{r4, r5, r6, r7, r8, r9});
        Collections.addAll(r2, r10);
        return copyOf(Ordering.natural(), r2);
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedMultiset<E> m301of(E r1) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedMultiset<E> m302of(E r1, E r2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedMultiset<E> m303of(E r1, E r2, E r3) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedMultiset<E> m304of(E r1, E r2, E r3, E r4) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedMultiset<E> m305of(E r1, E r2, E r3, E r4, E r5) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedMultiset<E> m306of(E r1, E r2, E r3, E r4, E r5, E r6, E... r7) {
        throw new UnsupportedOperationException();
    }

    public static <E> Builder<E> orderedBy(Comparator<E> r1) {
        return new Builder(r1);
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    public static <E extends Comparable<?>> Builder<E> reverseOrder() {
        return new Builder(Ordering.natural().reverse());
    }

    @Deprecated
    public static <E> Collector<E, ?, ImmutableMultiset<E>> toImmutableMultiset() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(Function<? super T, ? extends E> r1, ToIntFunction<? super T> r2) {
        throw new UnsupportedOperationException();
    }

    public static <E> Collector<E, ?, ImmutableSortedMultiset<E>> toImmutableSortedMultiset(Comparator<? super E> r2) {
        return toImmutableSortedMultiset(r2, Function.identity(), new ImmutableSortedMultiset$$ExternalSyntheticLambda4());
    }

    public static <T, E> Collector<T, ?, ImmutableSortedMultiset<E>> toImmutableSortedMultiset(final Comparator<? super E> r5, final Function<? super T, ? extends E> r6, final ToIntFunction<? super T> r7) {
        Preconditions.checkNotNull(r5);
        Preconditions.checkNotNull(r6);
        Preconditions.checkNotNull(r7);
        return Collector.of(new ImmutableSortedMultiset$$ExternalSyntheticLambda0(r5), new ImmutableSortedMultiset$$ExternalSyntheticLambda1(r6, r7), new ImmutableSortedMultiset$$ExternalSyntheticLambda2(), new ImmutableSortedMultiset$$ExternalSyntheticLambda3(r5), new Collector.Characteristics[0]);
    }

    @Override // com.google.common.collect.SortedMultiset, com.google.common.collect.SortedIterable
    public final Comparator<? super E> comparator() {
        return elementSet().comparator();
    }

    public ImmutableSortedMultiset<E> descendingMultiset() {
        ImmutableSortedMultiset<E> r0 = this.descendingMultiset;
        if (r0 == null) goto L5;
        return r0;
    L5:
        if (isEmpty() == false) goto L7;
        ImmutableSortedMultiset<E> r1 = emptyMultiset(Ordering.from(comparator()).reverse());
    L8:
        this.descendingMultiset = r1;
        return r1;
    L7:
        r1 = new DescendingImmutableSortedMultiset(this);
        goto L8
    }

    public /* bridge */ /* synthetic */ SortedMultiset descendingMultiset() {
        return descendingMultiset();
    }

    @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ ImmutableSet elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
    public abstract ImmutableSortedSet<E> elementSet();

    @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ NavigableSet elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ SortedSet elementSet() {
        return elementSet();
    }

    public abstract ImmutableSortedMultiset<E> headMultiset(E r1, BoundType r2);

    /* JADX WARN: Multi-variable type inference failed */
    public /* bridge */ /* synthetic */ SortedMultiset headMultiset(Object r1, BoundType r2) {
        return headMultiset(r1, r2);
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    @Deprecated
    public final Multiset.Entry<E> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    @Deprecated
    public final Multiset.Entry<E> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.SortedMultiset
    public ImmutableSortedMultiset<E> subMultiset(E r3, BoundType r4, E r5, BoundType r6) {
        if (comparator().compare(r3, r5) > 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "Expected lowerBound <= upperBound but %s > %s", r3, r5);
        return tailMultiset(r3, r4).headMultiset(r5, r6);
    L5:
        r0 = false;
        goto L6
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.SortedMultiset
    public /* bridge */ /* synthetic */ SortedMultiset subMultiset(Object r1, BoundType r2, Object r3, BoundType r4) {
        return subMultiset(r1, r2, r3, r4);
    }

    public abstract ImmutableSortedMultiset<E> tailMultiset(E r1, BoundType r2);

    /* JADX WARN: Multi-variable type inference failed */
    public /* bridge */ /* synthetic */ SortedMultiset tailMultiset(Object r1, BoundType r2) {
        return tailMultiset(r1, r2);
    }

    @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(this);
    }
}
