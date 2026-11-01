package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class MinMaxPriorityQueue<E> extends AbstractQueue<E> {
    private static final int DEFAULT_CAPACITY = 11;
    private static final int EVEN_POWERS_OF_TWO = 1431655765;
    private static final int ODD_POWERS_OF_TWO = -1431655766;
    private final MinMaxPriorityQueue<E>.Heap maxHeap;
    final int maximumSize;
    private final MinMaxPriorityQueue<E>.Heap minHeap;
    private int modCount;
    private Object[] queue;
    private int size;

    /* JADX INFO: renamed from: com.google.common.collect.MinMaxPriorityQueue$1 */
    static /* synthetic */ class C06951 {
    }

    public static final class Builder<B> {
        private static final int UNSET_EXPECTED_SIZE = -1;
        private final Comparator<B> comparator;
        private int expectedSize;
        private int maximumSize;

        private Builder(Comparator<B> r2) {
            this.expectedSize = -1;
            this.maximumSize = Integer.MAX_VALUE;
            this.comparator = (Comparator) Preconditions.checkNotNull(r2);
        }

        /* synthetic */ Builder(Comparator r1, C06951 r2) {
            this(r1);
        }

        static /* synthetic */ Ordering access$200(Builder r1) {
            return r1.ordering();
        }

        static /* synthetic */ int access$300(Builder r1) {
            return r1.maximumSize;
        }

        private <T extends B> Ordering<T> ordering() {
            return Ordering.from(this.comparator);
        }

        public <T extends B> MinMaxPriorityQueue<T> create() {
            return create(Collections.emptySet());
        }

        public <T extends B> MinMaxPriorityQueue<T> create(Iterable<? extends T> r4) {
            MinMaxPriorityQueue<T> r0 = new MinMaxPriorityQueue(this, MinMaxPriorityQueue.initialQueueSize(this.expectedSize, this.maximumSize, r4), null);
            Iterator<? extends T> r1 = r4.iterator();
        L4:
            if (r1.hasNext() == false) goto L6;
            r0.offer(r1.next());
            goto L4
        L6:
            return r0;
        }

        public Builder<B> expectedSize(int r2) {
            if (r2 < 0) goto L4;
            boolean r0 = true;
        L5:
            Preconditions.checkArgument(r0);
            this.expectedSize = r2;
            return this;
        L4:
            r0 = false;
            goto L5
        }

        public Builder<B> maximumSize(int r2) {
            if (r2 <= 0) goto L4;
            boolean r0 = true;
        L5:
            Preconditions.checkArgument(r0);
            this.maximumSize = r2;
            return this;
        L4:
            r0 = false;
            goto L5
        }
    }

    class Heap {
        final Ordering<E> ordering;
        MinMaxPriorityQueue<E>.Heap otherHeap;
        final /* synthetic */ MinMaxPriorityQueue this$0;

        Heap(MinMaxPriorityQueue r1, Ordering<E> r2) {
            this.this$0 = r1;
            this.ordering = r2;
        }

        static /* synthetic */ boolean access$400(Heap r1, int r2) {
            return r1.verifyIndex(r2);
        }

        private int getGrandparentIndex(int r2) {
            return getParentIndex(getParentIndex(r2));
        }

        private int getLeftChildIndex(int r2) {
            return (r2 * 2) + 1;
        }

        private int getParentIndex(int r2) {
            return (r2 - 1) / 2;
        }

        private int getRightChildIndex(int r2) {
            return (r2 * 2) + 2;
        }

        private boolean verifyIndex(int r4) {
            if (getLeftChildIndex(r4) >= MinMaxPriorityQueue.access$600(this.this$0)) goto L8;
            if (compareElements(r4, getLeftChildIndex(r4)) <= 0) goto L8;
            return false;
        L8:
            if (getRightChildIndex(r4) < MinMaxPriorityQueue.access$600(this.this$0)) goto L10;
        L12:
            if (r4 <= 0) goto L17;
            if (compareElements(r4, getParentIndex(r4)) <= 0) goto L17;
            return false;
        L17:
            if (r4 > 2) goto L19;
            return true;
        L19:
            if (compareElements(getGrandparentIndex(r4), r4) <= 0) goto L23;
            return false;
        L23:
            return true;
        L10:
            if (compareElements(r4, getRightChildIndex(r4)) <= 0) goto L12;
            return false;
        }

        void bubbleUp(int r3, E r4) {
            int r0 = crossOverUp(r3, r4);
            if (r0 != r3) goto L5;
            Heap r1 = this;
        L6:
            r1.bubbleUpAlternatingLevels(r3, r4);
            return;
        L5:
            r3 = r0;
            r1 = this.otherHeap;
            goto L6
        }

        int bubbleUpAlternatingLevels(int r4, E r5) {
        L3:
            if (r4 <= 2) goto L8;
            int r0 = getGrandparentIndex(r4);
            Object r1 = this.this$0.elementData(r0);
            if (this.ordering.compare((E) r1, r5) <= 0) goto L8;
            MinMaxPriorityQueue.access$500(this.this$0)[r4] = r1;
            r4 = r0;
        L8:
            MinMaxPriorityQueue.access$500(this.this$0)[r4] = r5;
            return r4;
        }

        int compareElements(int r4, int r5) {
            Ordering<E> r0 = this.ordering;
            Object r1 = this.this$0.elementData(r4);
            return r0.compare((E) r1, (E) this.this$0.elementData(r5));
        }

        int crossOver(int r4, E r5) {
            int r0 = findMinChild(r4);
            if (r0 <= 0) goto L9;
            if (this.ordering.compare((E) this.this$0.elementData(r0), r5) >= 0) goto L9;
            MinMaxPriorityQueue.access$500(this.this$0)[r4] = this.this$0.elementData(r0);
            MinMaxPriorityQueue.access$500(this.this$0)[r0] = r5;
            return r0;
        L9:
            return crossOverUp(r4, r5);
        }

        int crossOverUp(int r7, E r8) {
            if (r7 != 0) goto L5;
            MinMaxPriorityQueue.access$500(this.this$0)[0] = r8;
            return 0;
        L5:
            int r0 = getParentIndex(r7);
            Object r1 = this.this$0.elementData(r0);
            if (r0 == 0) goto L15;
            int r3 = getRightChildIndex(getParentIndex(r0));
            if (r3 == r0) goto L15;
            if (getLeftChildIndex(r3) < MinMaxPriorityQueue.access$600(this.this$0)) goto L15;
            Object r4 = this.this$0.elementData(r3);
            if (this.ordering.compare((E) r4, (E) r1) >= 0) goto L15;
            r0 = r3;
            r1 = r4;
        L15:
            if (this.ordering.compare((E) r1, r8) >= 0) goto L18;
            MinMaxPriorityQueue.access$500(this.this$0)[r7] = r1;
            MinMaxPriorityQueue.access$500(this.this$0)[r0] = r8;
            return r0;
        L18:
            MinMaxPriorityQueue.access$500(this.this$0)[r7] = r8;
            return r7;
        }

        int fillHoleAt(int r4) {
        L2:
            int r0 = findMinGrandChild(r4);
            if (r0 <= 0) goto L5;
            MinMaxPriorityQueue.access$500(this.this$0)[r4] = this.this$0.elementData(r0);
            r4 = r0;
            goto L2
        L5:
            return r4;
        }

        int findMin(int r5, int r6) {
            if (r5 < MinMaxPriorityQueue.access$600(this.this$0)) goto L6;
            return -1;
        L6:
            if (r5 <= 0) goto L8;
            boolean r0 = true;
        L9:
            Preconditions.checkState(r0);
            int r1 = Math.min(r5, MinMaxPriorityQueue.access$600(this.this$0) - r6) + r6;
            int r2 = r5;
            int r3 = r5 + 1;
        L10:
            if (r3 >= r1) goto L15;
            if (compareElements(r3, r2) >= 0) goto L14;
            r2 = r3;
        L14:
            r3 = r3 + 1;
            goto L10
        L15:
            return r2;
        L8:
            r0 = false;
            goto L9
        }

        int findMinChild(int r3) {
            return findMin(getLeftChildIndex(r3), 2);
        }

        int findMinGrandChild(int r4) {
            int r0 = getLeftChildIndex(r4);
            if (r0 >= 0) goto L7;
            return -1;
        L7:
            return findMin(getLeftChildIndex(r0), 4);
        }

        int swapWithConceptuallyLastElement(E r7) {
            int r0 = getParentIndex(MinMaxPriorityQueue.access$600(this.this$0));
            if (r0 == 0) goto L13;
            int r2 = getRightChildIndex(getParentIndex(r0));
            if (r2 == r0) goto L13;
            if (getLeftChildIndex(r2) < MinMaxPriorityQueue.access$600(this.this$0)) goto L13;
            Object r3 = this.this$0.elementData(r2);
            if (this.ordering.compare((E) r3, r7) >= 0) goto L13;
            MinMaxPriorityQueue.access$500(this.this$0)[r2] = r7;
            MinMaxPriorityQueue.access$500(this.this$0)[MinMaxPriorityQueue.access$600(this.this$0)] = r3;
            return r2;
        L13:
            return MinMaxPriorityQueue.access$600(this.this$0);
        }

        @CheckForNull
        MoveDesc<E> tryCrossOverAndBubbleUp(int r5, int r6, E r7) {
            int r0 = crossOver(r6, r7);
            if (r0 != r6) goto L5;
            return null;
        L5:
            if (r0 >= r5) goto L7;
            Object r2 = this.this$0.elementData(r5);
        L9:
            if (this.otherHeap.bubbleUpAlternatingLevels(r0, r7) < r5) goto L11;
            return null;
        L11:
            return new MoveDesc(r7, r2);
        L7:
            r2 = this.this$0.elementData(getParentIndex(r5));
            goto L9
        }
    }

    static class MoveDesc<E> {
        final E replaced;
        final E toTrickle;

        MoveDesc(E r1, E r2) {
            this.toTrickle = r1;
            this.replaced = r2;
        }
    }

    private class QueueIterator implements Iterator<E> {
        private boolean canRemove;
        private int cursor;
        private int expectedModCount;

        @CheckForNull
        private Queue<E> forgetMeNot;

        @CheckForNull
        private E lastFromForgetMeNot;
        private int nextCursor;

        @CheckForNull
        private List<E> skipMe;
        final /* synthetic */ MinMaxPriorityQueue this$0;

        private QueueIterator(MinMaxPriorityQueue r1) {
            this.this$0 = r1;
            this.cursor = -1;
            this.nextCursor = -1;
            this.expectedModCount = MinMaxPriorityQueue.access$700(this.this$0);
        }

        /* synthetic */ QueueIterator(MinMaxPriorityQueue r1, C06951 r2) {
            this(r1);
        }

        private void checkModCount() {
            if (MinMaxPriorityQueue.access$700(this.this$0) != this.expectedModCount) goto L6;
            return;
        L6:
            throw new ConcurrentModificationException();
        }

        private boolean foundAndRemovedExactReference(Iterable<E> r4, E r5) {
            Iterator<E> r0 = r4.iterator();
        L4:
            if (r0.hasNext() == false) goto L10;
            if (r0.next() != r5) goto L4;
            r0.remove();
            return true;
        L10:
            return false;
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void nextNotInSkipMe(int r3) {
            if (this.nextCursor < r3) goto L5;
            return;
        L5:
            if (this.skipMe != null) goto L7;
        L11:
            this.nextCursor = r3;
            return;
        L7:
            if (r3 >= this.this$0.size()) goto L11;
            if (foundAndRemovedExactReference(this.skipMe, this.this$0.elementData(r3)) == false) goto L11;
            r3 = r3 + 1;
            goto L7
        }

        private boolean removeExact(Object r3) {
            int r0 = 0;
        L4:
            if (r0 >= MinMaxPriorityQueue.access$600(this.this$0)) goto L10;
            if (MinMaxPriorityQueue.access$500(this.this$0)[r0] == r3) goto L7;
            r0 = r0 + 1;
            goto L4
        L7:
            this.this$0.removeAt(r0);
            return true;
        L10:
            return false;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            checkModCount();
            nextNotInSkipMe(this.cursor + 1);
            if (this.nextCursor >= this.this$0.size()) goto L5;
            return true;
        L5:
            if (this.forgetMeNot != null) goto L7;
        L9:
            return false;
        L7:
            if (this.forgetMeNot.isEmpty() == true) goto L9;
            return true;
        }

        @Override // java.util.Iterator
        public E next() {
            checkModCount();
            nextNotInSkipMe(this.cursor + 1);
            if (this.nextCursor >= this.this$0.size()) goto L7;
            this.cursor = this.nextCursor;
            this.canRemove = true;
            return (E) this.this$0.elementData(this.cursor);
        L7:
            if (this.forgetMeNot == null) goto L13;
            this.cursor = this.this$0.size();
            this.lastFromForgetMeNot = this.forgetMeNot.poll();
            if (this.lastFromForgetMeNot == null) goto L13;
            this.canRemove = true;
            return this.lastFromForgetMeNot;
        L13:
            throw new NoSuchElementException("iterator moved past last element in queue.");
        }

        @Override // java.util.Iterator
        public void remove() {
            CollectPreconditions.checkRemove(this.canRemove);
            checkModCount();
            this.canRemove = false;
            this.expectedModCount++;
            if (this.cursor >= this.this$0.size()) goto L18;
            MoveDesc<E> r0 = this.this$0.removeAt(this.cursor);
            if (r0 != null) goto L7;
        L17:
            this.cursor--;
            this.nextCursor--;
            return;
        L7:
            if (this.forgetMeNot != null) goto L9;
        L10:
            this.forgetMeNot = new ArrayDeque();
            this.skipMe = new ArrayList(3);
        L12:
            if (foundAndRemovedExactReference(this.skipMe, r0.toTrickle) == true) goto L15;
            this.forgetMeNot.add(r0.toTrickle);
        L15:
            if (foundAndRemovedExactReference(this.forgetMeNot, r0.replaced) == true) goto L17;
            this.skipMe.add(r0.replaced);
            goto L17
        L9:
            if (this.skipMe != null) goto L12;
        L18:
            Preconditions.checkState(removeExact(Objects.requireNonNull(this.lastFromForgetMeNot)));
            this.lastFromForgetMeNot = null;
        }
    }

    private MinMaxPriorityQueue(Builder<? super E> r4, int r5) {
        Ordering r0 = Builder.access$200(r4);
        this.minHeap = new Heap(this, r0);
        this.maxHeap = new Heap(this, r0.reverse());
        MinMaxPriorityQueue<E>.Heap r1 = this.minHeap;
        r1.otherHeap = this.maxHeap;
        MinMaxPriorityQueue<E>.Heap r2 = this.maxHeap;
        r2.otherHeap = this.minHeap;
        this.maximumSize = Builder.access$300(r4);
        this.queue = new Object[r5];
    }

    /* synthetic */ MinMaxPriorityQueue(Builder r1, int r2, C06951 r3) {
        this(r1, r2);
    }

    static /* synthetic */ Object[] access$500(MinMaxPriorityQueue r1) {
        return r1.queue;
    }

    static /* synthetic */ int access$600(MinMaxPriorityQueue r1) {
        return r1.size;
    }

    static /* synthetic */ int access$700(MinMaxPriorityQueue r1) {
        return r1.modCount;
    }

    private int calculateNewCapacity() {
        int r0 = this.queue.length;
        if (r0 >= 64) goto L5;
        int r1 = (r0 + 1) * 2;
    L7:
        return capAtMaximumSize(r1, this.maximumSize);
    L5:
        r1 = IntMath.checkedMultiply(r0 / 2, 3);
        goto L7
    }

    private static int capAtMaximumSize(int r1, int r2) {
        return Math.min(r1 - 1, r2) + 1;
    }

    public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create() {
        return new Builder(Ordering.natural(), null).create();
    }

    public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create(Iterable<? extends E> r3) {
        return new Builder(Ordering.natural(), null).create(r3);
    }

    public static Builder<Comparable> expectedSize(int r3) {
        return new Builder(Ordering.natural(), null).expectedSize(r3);
    }

    @CheckForNull
    private MoveDesc<E> fillHole(int r6, E r7) {
        MinMaxPriorityQueue<E>.Heap r0 = heapForIndex(r6);
        int r1 = r0.fillHoleAt(r6);
        int r2 = r0.bubbleUpAlternatingLevels(r1, r7);
        if (r2 == r1) goto L5;
        if (r2 < r6) goto L10;
        return null;
    L10:
        return new MoveDesc(r7, elementData(r6));
    L5:
        return r0.tryCrossOverAndBubbleUp(r6, r1, r7);
    }

    private int getMaxElementIndex() {
        switch(this.size) {
            case 1: goto L8;
            case 2: goto L7;
            default: goto L5;
        };
    L7:
        return 1;
    L8:
        return 0;
    L5:
        if (this.maxHeap.compareElements(1, 2) > 0) goto L11;
        return 1;
    L11:
        return 2;
    }

    private void growIfNeeded() {
        if (this.size <= this.queue.length) goto L6;
        Object[] r1 = new Object[calculateNewCapacity()];
        System.arraycopy(this.queue, 0, r1, 0, this.queue.length);
        this.queue = r1;
        return;
    }

    private MinMaxPriorityQueue<E>.Heap heapForIndex(int r2) {
        if (isEvenLevel(r2) == false) goto L6;
        return this.minHeap;
    L6:
        return this.maxHeap;
    }

    static int initialQueueSize(int r2, int r3, Iterable<?> r4) {
        if (r2 != (-1)) goto L5;
        int r0 = 11;
    L7:
        if ((r4 instanceof Collection) == false) goto L10;
        r0 = Math.max(r0, ((Collection) r4).size());
    L10:
        return capAtMaximumSize(r0, r3);
    L5:
        r0 = r2;
        goto L7
    }

    static boolean isEvenLevel(int r5) {
        int r0 = ~(~(r5 + 1));
        if (r0 <= 0) goto L5;
        boolean r3 = true;
    L6:
        Preconditions.checkState(r3, "negative index");
        if ((EVEN_POWERS_OF_TWO & r0) > (ODD_POWERS_OF_TWO & r0)) goto L9;
        return false;
    L9:
        return true;
    L5:
        r3 = false;
        goto L6
    }

    public static Builder<Comparable> maximumSize(int r3) {
        return new Builder(Ordering.natural(), null).maximumSize(r3);
    }

    public static <B> Builder<B> orderedBy(Comparator<B> r2) {
        return new Builder(r2, null);
    }

    private E removeAndGet(int r2) {
        E r0 = elementData(r2);
        removeAt(r2);
        return r0;
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.Queue
    public boolean add(E r2) {
        offer(r2);
        return true;
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> r4) {
        boolean r0 = false;
        Iterator<? extends E> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        offer(r1.next());
        r0 = true;
        goto L4
    L6:
        return r0;
    }

    int capacity() {
        return this.queue.length;
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        int r0 = 0;
    L4:
        if (r0 >= this.size) goto L6;
        this.queue[r0] = null;
        r0 = r0 + 1;
        goto L4
    L6:
        this.size = 0;
    }

    public Comparator<? super E> comparator() {
        return this.minHeap.ordering;
    }

    E elementData(int r2) {
        return (E) Objects.requireNonNull(this.queue[r2]);
    }

    boolean isIntact() {
        int r0 = 1;
    L4:
        if (r0 >= this.size) goto L10;
        if (Heap.access$400(heapForIndex(r0), r0) == false) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return false;
    L10:
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return new QueueIterator(this, null);
    }

    @Override // java.util.Queue
    public boolean offer(E r5) {
        Preconditions.checkNotNull(r5);
        this.modCount++;
        int r0 = this.size;
        this.size = r0 + 1;
        growIfNeeded();
        heapForIndex(r0).bubbleUp(r0, r5);
        if (this.size > this.maximumSize) goto L5;
        return true;
    L5:
        if (pollLast() != r5) goto L10;
        return false;
    L10:
        return true;
    }

    @Override // java.util.Queue
    @CheckForNull
    public E peek() {
        if (isEmpty() == false) goto L6;
        return null;
    L6:
        return elementData(0);
    }

    @CheckForNull
    public E peekFirst() {
        return peek();
    }

    @CheckForNull
    public E peekLast() {
        if (isEmpty() == false) goto L6;
        return null;
    L6:
        return elementData(getMaxElementIndex());
    }

    @Override // java.util.Queue
    @CheckForNull
    public E poll() {
        if (isEmpty() == false) goto L6;
        return null;
    L6:
        return removeAndGet(0);
    }

    @CheckForNull
    public E pollFirst() {
        return poll();
    }

    @CheckForNull
    public E pollLast() {
        if (isEmpty() == false) goto L6;
        return null;
    L6:
        return removeAndGet(getMaxElementIndex());
    }

    @CheckForNull
    MoveDesc<E> removeAt(int r7) {
        Preconditions.checkPositionIndex(r7, this.size);
        this.modCount++;
        this.size--;
        if (this.size != r7) goto L6;
        this.queue[this.size] = null;
        return null;
    L6:
        E r0 = elementData(this.size);
        int r2 = heapForIndex(this.size).swapWithConceptuallyLastElement(r0);
        if (r2 != r7) goto L10;
        this.queue[this.size] = null;
        return null;
    L10:
        E r3 = elementData(this.size);
        this.queue[this.size] = null;
        MoveDesc<E> r1 = fillHole(r7, r3);
        if (r2 >= r7) goto L17;
        if (r1 != null) goto L16;
        return new MoveDesc(r0, r3);
    L16:
        return new MoveDesc(r0, r1.replaced);
    L17:
        return r1;
    }

    public E removeFirst() {
        return remove();
    }

    public E removeLast() {
        if (isEmpty() == true) goto L7;
        return removeAndGet(getMaxElementIndex());
    L7:
        throw new NoSuchElementException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public Object[] toArray() {
        Object[] r0 = new Object[this.size];
        System.arraycopy(this.queue, 0, r0, 0, this.size);
        return r0;
    }
}
