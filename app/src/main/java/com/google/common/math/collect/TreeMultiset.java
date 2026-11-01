package com.google.common.collect;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class TreeMultiset<E> extends AbstractSortedMultiset<E> implements Serializable {
    private static final long serialVersionUID = 1;
    private final transient AvlNode<E> header;
    private final transient GeneralRange<E> range;
    private final transient Reference<AvlNode<E>> rootReference;

    /* JADX INFO: renamed from: com.google.common.collect.TreeMultiset$4 */
    static /* synthetic */ class C07614 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$collect$BoundType = null;

        static {
            $SwitchMap$com$google$common$collect$BoundType = new int[BoundType.values().length];
            $SwitchMap$com$google$common$collect$BoundType[BoundType.OPEN.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L12:
            $SwitchMap$com$google$common$collect$BoundType[BoundType.CLOSED.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
            return;
        }
    }

    private static abstract enum Aggregate extends Enum<Aggregate> {
        private static final /* synthetic */ Aggregate[] $VALUES = null;
        public static final Aggregate DISTINCT = null;
        public static final Aggregate SIZE = null;

        private static /* synthetic */ Aggregate[] $values() {
            return new Aggregate[]{SIZE, DISTINCT};
        }

        static {
            final String r1 = "SIZE";
            final int r2 = 0;
            SIZE = new C07621(r1, r2);
            final String r3 = "DISTINCT";
            final int r4 = 1;
            DISTINCT = new C07632(r3, r4);
            $VALUES = $values();
        }

        private Aggregate(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ Aggregate(String r1, int r2, C07581 r3) {
            this(r1, r2);
        }

        public static Aggregate valueOf(String r1) {
            return (Aggregate) Enum.valueOf(Aggregate.class, r1);
        }

        public static Aggregate[] values() {
            return (Aggregate[]) $VALUES.clone();
        }

        abstract int nodeAggregate(AvlNode<?> r1);

        abstract long treeAggregate(@CheckForNull AvlNode<?> r1);
    }

    private static final class AvlNode<E> {
        private int distinctElements;

        @CheckForNull
        private final E elem;
        private int elemCount;
        private int height;

        @CheckForNull
        private AvlNode<E> left;

        @CheckForNull
        private AvlNode<E> pred;

        @CheckForNull
        private AvlNode<E> right;

        @CheckForNull
        private AvlNode<E> succ;
        private long totalCount;

        AvlNode() {
            this.elem = null;
            this.elemCount = 1;
        }

        AvlNode(@ParametricNullness E r4, int r5) {
            if (r5 <= 0) goto L5;
            boolean r1 = true;
        L6:
            Preconditions.checkArgument(r1);
            this.elem = r4;
            this.elemCount = r5;
            this.totalCount = r5;
            this.distinctElements = 1;
            this.height = 1;
            this.left = null;
            this.right = null;
            return;
        L5:
            r1 = false;
            goto L6
        }

        static /* synthetic */ AvlNode access$1000(AvlNode r1, Comparator r2, Object r3) {
            return r1.ceiling(r2, r3);
        }

        static /* synthetic */ AvlNode access$1100(AvlNode r1, Comparator r2, Object r3) {
            return r1.floor(r2, r3);
        }

        static /* synthetic */ AvlNode access$1200(AvlNode r1) {
            return r1.pred();
        }

        static /* synthetic */ int access$200(AvlNode r1) {
            return r1.elemCount;
        }

        static /* synthetic */ int access$202(AvlNode r0, int r1) {
            r0.elemCount = r1;
            return r1;
        }

        static /* synthetic */ long access$300(AvlNode r2) {
            return r2.totalCount;
        }

        static /* synthetic */ int access$400(AvlNode r1) {
            return r1.distinctElements;
        }

        static /* synthetic */ AvlNode access$500(AvlNode r1) {
            return r1.left;
        }

        static /* synthetic */ AvlNode access$502(AvlNode r0, AvlNode r1) {
            r0.left = r1;
            return r1;
        }

        static /* synthetic */ AvlNode access$600(AvlNode r1) {
            return r1.right;
        }

        static /* synthetic */ AvlNode access$602(AvlNode r0, AvlNode r1) {
            r0.right = r1;
            return r1;
        }

        static /* synthetic */ AvlNode access$700(AvlNode r1) {
            return r1.succ();
        }

        static /* synthetic */ AvlNode access$802(AvlNode r0, AvlNode r1) {
            r0.pred = r1;
            return r1;
        }

        static /* synthetic */ AvlNode access$902(AvlNode r0, AvlNode r1) {
            r0.succ = r1;
            return r1;
        }

        private AvlNode<E> addLeftChild(@ParametricNullness E r5, int r6) {
            this.left = new AvlNode(r5, r6);
            TreeMultiset.access$1800(pred(), this.left, this);
            this.height = Math.max(2, this.height);
            this.distinctElements++;
            this.totalCount += (long) r6;
            return this;
        }

        private AvlNode<E> addRightChild(@ParametricNullness E r5, int r6) {
            this.right = new AvlNode(r5, r6);
            TreeMultiset.access$1800(this, this.right, succ());
            this.height = Math.max(2, this.height);
            this.distinctElements++;
            this.totalCount += (long) r6;
            return this;
        }

        private int balanceFactor() {
            return height(this.left) - height(this.right);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @CheckForNull
        private AvlNode<E> ceiling(Comparator<? super E> r3, @ParametricNullness E r4) {
            int r0 = r3.compare(r4, getElement());
            if (r0 < 0) goto L5;
            if (r0 != 0) goto L12;
            return this;
        L12:
            if (this.right != null) goto L15;
            return null;
        L15:
            return this.right.ceiling(r3, r4);
        L5:
            if (this.left != null) goto L8;
            return this;
        L8:
            return (AvlNode) MoreObjects.firstNonNull(this.left.ceiling(r3, r4), this);
        }

        @CheckForNull
        private AvlNode<E> deleteMe() {
            int r0 = this.elemCount;
            this.elemCount = 0;
            TreeMultiset.access$1900(pred(), succ());
            if (this.left != null) goto L7;
            return this.right;
        L7:
            if (this.right != null) goto L11;
            return this.left;
        L11:
            if (this.left.height < this.right.height) goto L14;
            AvlNode<E> r1 = pred();
            r1.left = this.left.removeMax(r1);
            r1.right = this.right;
            r1.distinctElements = this.distinctElements - 1;
            r1.totalCount = this.totalCount - ((long) r0);
            return r1.rebalance();
        L14:
            AvlNode<E> r2 = succ();
            r2.right = this.right.removeMin(r2);
            r2.left = this.left;
            r2.distinctElements = this.distinctElements - 1;
            r2.totalCount = this.totalCount - ((long) r0);
            return r2.rebalance();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @CheckForNull
        private AvlNode<E> floor(Comparator<? super E> r3, @ParametricNullness E r4) {
            int r0 = r3.compare(r4, getElement());
            if (r0 > 0) goto L5;
            if (r0 != 0) goto L12;
            return this;
        L12:
            if (this.left != null) goto L15;
            return null;
        L15:
            return this.left.floor(r3, r4);
        L5:
            if (this.right != null) goto L8;
            return this;
        L8:
            return (AvlNode) MoreObjects.firstNonNull(this.right.floor(r3, r4), this);
        }

        private static int height(@CheckForNull AvlNode<?> r1) {
            if (r1 != null) goto L5;
            return 0;
        L5:
            return ((AvlNode) r1).height;
        }

        private AvlNode<E> pred() {
            return (AvlNode) Objects.requireNonNull(this.pred);
        }

        private AvlNode<E> rebalance() {
            switch(balanceFactor()) {
                case -2: goto L11;
                case 2: goto L6;
                default: goto L4;
            };
        L4:
            recomputeHeight();
            return this;
        L6:
            Objects.requireNonNull(this.left);
            if (this.left.balanceFactor() >= 0) goto L10;
            this.left = this.left.rotateLeft();
        L10:
            return rotateRight();
        L11:
            Objects.requireNonNull(this.right);
            if (this.right.balanceFactor() <= 0) goto L15;
            this.right = this.right.rotateRight();
        L15:
            return rotateLeft();
        }

        private void recompute() {
            recomputeMultiset();
            recomputeHeight();
        }

        private void recomputeHeight() {
            this.height = Math.max(height(this.left), height(this.right)) + 1;
        }

        private void recomputeMultiset() {
            this.distinctElements = (TreeMultiset.distinctElements(this.left) + 1) + TreeMultiset.distinctElements(this.right);
            this.totalCount = (((long) this.elemCount) + totalCount(this.left)) + totalCount(this.right);
        }

        @CheckForNull
        private AvlNode<E> removeMax(AvlNode<E> r5) {
            if (this.right == null) goto L5;
            this.right = this.right.removeMax(r5);
            this.distinctElements--;
            this.totalCount -= (long) r5.elemCount;
            return rebalance();
        L5:
            return this.left;
        }

        @CheckForNull
        private AvlNode<E> removeMin(AvlNode<E> r5) {
            if (this.left == null) goto L5;
            this.left = this.left.removeMin(r5);
            this.distinctElements--;
            this.totalCount -= (long) r5.elemCount;
            return rebalance();
        L5:
            return this.right;
        }

        private AvlNode<E> rotateLeft() {
            if (this.right == null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkState(r0);
            AvlNode<E> r1 = this.right;
            this.right = r1.left;
            r1.left = this;
            r1.totalCount = this.totalCount;
            r1.distinctElements = this.distinctElements;
            recompute();
            r1.recomputeHeight();
            return r1;
        L5:
            r0 = false;
            goto L6
        }

        private AvlNode<E> rotateRight() {
            if (this.left == null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkState(r0);
            AvlNode<E> r1 = this.left;
            this.left = r1.right;
            r1.right = this;
            r1.totalCount = this.totalCount;
            r1.distinctElements = this.distinctElements;
            recompute();
            r1.recomputeHeight();
            return r1;
        L5:
            r0 = false;
            goto L6
        }

        private AvlNode<E> succ() {
            return (AvlNode) Objects.requireNonNull(this.succ);
        }

        private static long totalCount(@CheckForNull AvlNode<?> r2) {
            if (r2 != null) goto L5;
            return 0;
        L5:
            return ((AvlNode) r2).totalCount;
        }

        /* JADX WARN: Multi-variable type inference failed */
        AvlNode<E> add(Comparator<? super E> r8, @ParametricNullness E r9, int r10, int[] r11) {
            int r0 = r8.compare(r9, getElement());
            boolean r1 = true;
            if (r0 >= 0) goto L16;
            AvlNode<E> r3 = this.left;
            if (r3 != null) goto L8;
            r11[0] = 0;
            return addLeftChild(r9, r10);
        L8:
            int r4 = r3.height;
            this.left = r3.add(r8, r9, r10, r11);
            if (r11[0] != 0) goto L11;
            this.distinctElements++;
        L11:
            this.totalCount += (long) r10;
            if (this.left.height != r4) goto L15;
            return this;
        L15:
            return rebalance();
        L16:
            if (r0 <= 0) goto L29;
            AvlNode<E> r5 = this.right;
            if (r5 != null) goto L21;
            r11[0] = 0;
            return addRightChild(r9, r10);
        L21:
            int r6 = r5.height;
            this.right = r5.add(r8, r9, r10, r11);
            if (r11[0] != 0) goto L24;
            this.distinctElements++;
        L24:
            this.totalCount += (long) r10;
            if (this.right.height != r6) goto L28;
            return this;
        L28:
            return rebalance();
        L29:
            r11[0] = this.elemCount;
            if ((((long) this.elemCount) + ((long) r10)) <= 2147483647L) goto L33;
            r1 = false;
        L33:
            Preconditions.checkArgument(r1);
            this.elemCount += r10;
            this.totalCount += (long) r10;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        int count(Comparator<? super E> r4, @ParametricNullness E r5) {
            int r0 = r4.compare(r5, getElement());
            if (r0 < 0) goto L5;
            if (r0 <= 0) goto L16;
            if (this.right != null) goto L14;
            return 0;
        L14:
            return this.right.count(r4, r5);
        L16:
            return this.elemCount;
        L5:
            if (this.left != null) goto L8;
            return 0;
        L8:
            return this.left.count(r4, r5);
        }

        int getCount() {
            return this.elemCount;
        }

        @ParametricNullness
        E getElement() {
            return (E) NullnessCasts.uncheckedCastNullableTToT(this.elem);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @CheckForNull
        AvlNode<E> remove(Comparator<? super E> r8, @ParametricNullness E r9, int r10, int[] r11) {
            int r0 = r8.compare(r9, getElement());
            if (r0 >= 0) goto L19;
            AvlNode<E> r2 = this.left;
            if (r2 != null) goto L8;
            r11[0] = 0;
            return this;
        L8:
            this.left = r2.remove(r8, r9, r10, r11);
            if (r11[0] <= 0) goto L15;
            if (r10 < r11[0]) goto L13;
            this.distinctElements--;
            this.totalCount -= (long) r11[0];
            goto L15
        L13:
            this.totalCount -= (long) r10;
        L15:
            if (r11[0] != 0) goto L18;
            return this;
        L18:
            return rebalance();
        L19:
            if (r0 <= 0) goto L32;
            AvlNode<E> r3 = this.right;
            if (r3 != null) goto L24;
            r11[0] = 0;
            return this;
        L24:
            this.right = r3.remove(r8, r9, r10, r11);
            if (r11[0] <= 0) goto L31;
            if (r10 < r11[0]) goto L29;
            this.distinctElements--;
            this.totalCount -= (long) r11[0];
            goto L31
        L29:
            this.totalCount -= (long) r10;
        L31:
            return rebalance();
        L32:
            r11[0] = this.elemCount;
            if (r10 >= this.elemCount) goto L35;
            this.elemCount -= r10;
            this.totalCount -= (long) r10;
            return this;
        L35:
            return deleteMe();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @CheckForNull
        AvlNode<E> setCount(Comparator<? super E> r10, @ParametricNullness E r11, int r12, int r13, int[] r14) {
            int r0 = r10.compare(r11, getElement());
            if (r0 >= 0) goto L26;
            AvlNode<E> r2 = this.left;
            if (r2 != null) goto L12;
            r14[0] = 0;
            if (r12 != 0) goto L11;
            if (r13 <= 0) goto L11;
            return addLeftChild(r11, r13);
        L11:
            return this;
        L12:
            this.left = r2.setCount(r10, r11, r12, r13, r14);
            if (r14[0] != r12) goto L24;
            if (r13 == 0) goto L16;
        L18:
            if (r13 > 0) goto L20;
        L22:
            this.totalCount += (long) (r13 - r14[0]);
            goto L24
        L20:
            if (r14[0] != 0) goto L22;
            this.distinctElements++;
            goto L22
        L16:
            if (r14[0] == 0) goto L18;
            this.distinctElements--;
        L24:
            return rebalance();
        L26:
            if (r0 <= 0) goto L48;
            AvlNode<E> r3 = this.right;
            if (r3 != null) goto L35;
            r14[0] = 0;
            if (r12 != 0) goto L34;
            if (r13 <= 0) goto L34;
            return addRightChild(r11, r13);
        L34:
            return this;
        L35:
            this.right = r3.setCount(r10, r11, r12, r13, r14);
            if (r14[0] != r12) goto L47;
            if (r13 == 0) goto L39;
        L41:
            if (r13 > 0) goto L43;
        L45:
            this.totalCount += (long) (r13 - r14[0]);
            goto L47
        L43:
            if (r14[0] != 0) goto L45;
            this.distinctElements++;
            goto L45
        L39:
            if (r14[0] == 0) goto L41;
            this.distinctElements--;
        L47:
            return rebalance();
        L48:
            r14[0] = this.elemCount;
            if (r12 != this.elemCount) goto L54;
            if (r13 == 0) goto L52;
            this.totalCount += (long) (r13 - this.elemCount);
            this.elemCount = r13;
            goto L54
        L52:
            return deleteMe();
        L54:
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @CheckForNull
        AvlNode<E> setCount(Comparator<? super E> r8, @ParametricNullness E r9, int r10, int[] r11) {
            int r0 = r8.compare(r9, getElement());
            if (r0 >= 0) goto L22;
            AvlNode<E> r2 = this.left;
            if (r2 != null) goto L11;
            r11[0] = 0;
            if (r10 <= 0) goto L10;
            return addLeftChild(r9, r10);
        L10:
            return this;
        L11:
            this.left = r2.setCount(r8, r9, r10, r11);
            if (r10 == 0) goto L14;
        L16:
            if (r10 > 0) goto L18;
        L20:
            this.totalCount += (long) (r10 - r11[0]);
            return rebalance();
        L18:
            if (r11[0] != 0) goto L20;
            this.distinctElements++;
            goto L20
        L14:
            if (r11[0] == 0) goto L16;
            this.distinctElements--;
            goto L20
        L22:
            if (r0 <= 0) goto L41;
            AvlNode<E> r3 = this.right;
            if (r3 != null) goto L30;
            r11[0] = 0;
            if (r10 <= 0) goto L29;
            return addRightChild(r9, r10);
        L29:
            return this;
        L30:
            this.right = r3.setCount(r8, r9, r10, r11);
            if (r10 == 0) goto L33;
        L35:
            if (r10 > 0) goto L37;
        L39:
            this.totalCount += (long) (r10 - r11[0]);
            return rebalance();
        L37:
            if (r11[0] != 0) goto L39;
            this.distinctElements++;
            goto L39
        L33:
            if (r11[0] == 0) goto L35;
            this.distinctElements--;
            goto L39
        L41:
            r11[0] = this.elemCount;
            if (r10 == 0) goto L44;
            this.totalCount += (long) (r10 - this.elemCount);
            this.elemCount = r10;
            return this;
        L44:
            return deleteMe();
        }

        public String toString() {
            return Multisets.immutableEntry(getElement(), getCount()).toString();
        }
    }

    private static final class Reference<T> {

        @CheckForNull
        private T value;

        private Reference() {
        }

        /* synthetic */ Reference(C07581 r1) {
            this();
        }

        public void checkAndSet(@CheckForNull T r2, @CheckForNull T r3) {
            if (this.value != r2) goto L7;
            this.value = r3;
            return;
        L7:
            throw new ConcurrentModificationException();
        }

        void clear() {
            this.value = null;
        }

        @CheckForNull
        public T get() {
            return this.value;
        }
    }

    TreeMultiset(Reference<AvlNode<E>> r2, GeneralRange<E> r3, AvlNode<E> r4) {
        super(r3.comparator());
        this.rootReference = r2;
        this.range = r3;
        this.header = r4;
    }

    TreeMultiset(Comparator<? super E> r3) {
        super(r3);
        this.range = GeneralRange.all(r3);
        this.header = new AvlNode();
        successor(this.header, this.header);
        this.rootReference = new Reference(null);
    }

    static /* synthetic */ AvlNode access$1300(TreeMultiset r1) {
        return r1.firstNode();
    }

    static /* synthetic */ GeneralRange access$1400(TreeMultiset r1) {
        return r1.range;
    }

    static /* synthetic */ Multiset.Entry access$1500(TreeMultiset r1, AvlNode r2) {
        return r1.wrapEntry(r2);
    }

    static /* synthetic */ AvlNode access$1600(TreeMultiset r1) {
        return r1.header;
    }

    static /* synthetic */ AvlNode access$1700(TreeMultiset r1) {
        return r1.lastNode();
    }

    static /* synthetic */ void access$1800(AvlNode r0, AvlNode r1, AvlNode r2) {
        successor(r0, r1, r2);
    }

    static /* synthetic */ void access$1900(AvlNode r0, AvlNode r1) {
        successor(r0, r1);
    }

    private long aggregateAboveRange(Aggregate r6, @CheckForNull AvlNode<E> r7) {
        if (r7 != null) goto L5;
        return 0;
    L5:
        int r0 = comparator().compare(NullnessCasts.uncheckedCastNullableTToT(this.range.getUpperEndpoint()), r7.getElement());
        if (r0 > 0) goto L8;
        if (r0 != 0) goto L19;
        switch(C07614.$SwitchMap$com$google$common$collect$BoundType[this.range.getUpperBoundType().ordinal()]) {
            case 1: goto L17;
            case 2: goto L15;
            default: goto L13;
        };
    L13:
        throw new AssertionError();
    L15:
        return r6.treeAggregate(AvlNode.access$600(r7));
    L17:
        return ((long) r6.nodeAggregate(r7)) + r6.treeAggregate(AvlNode.access$600(r7));
    L19:
        return (r6.treeAggregate(AvlNode.access$600(r7)) + ((long) r6.nodeAggregate(r7))) + aggregateAboveRange(r6, AvlNode.access$500(r7));
    L8:
        return aggregateAboveRange(r6, AvlNode.access$600(r7));
    }

    private long aggregateBelowRange(Aggregate r6, @CheckForNull AvlNode<E> r7) {
        if (r7 != null) goto L5;
        return 0;
    L5:
        int r0 = comparator().compare(NullnessCasts.uncheckedCastNullableTToT(this.range.getLowerEndpoint()), r7.getElement());
        if (r0 < 0) goto L8;
        if (r0 != 0) goto L19;
        switch(C07614.$SwitchMap$com$google$common$collect$BoundType[this.range.getLowerBoundType().ordinal()]) {
            case 1: goto L17;
            case 2: goto L15;
            default: goto L13;
        };
    L13:
        throw new AssertionError();
    L15:
        return r6.treeAggregate(AvlNode.access$500(r7));
    L17:
        return ((long) r6.nodeAggregate(r7)) + r6.treeAggregate(AvlNode.access$500(r7));
    L19:
        return (r6.treeAggregate(AvlNode.access$500(r7)) + ((long) r6.nodeAggregate(r7))) + aggregateBelowRange(r6, AvlNode.access$600(r7));
    L8:
        return aggregateBelowRange(r6, AvlNode.access$500(r7));
    }

    private long aggregateForEntries(Aggregate r6) {
        AvlNode<E> r0 = this.rootReference.get();
        long r1 = r6.treeAggregate(r0);
        if (this.range.hasLowerBound() == false) goto L6;
        r1 = r1 - aggregateBelowRange(r6, r0);
    L6:
        if (this.range.hasUpperBound() == true) goto L8;
        return r1;
    L8:
        return r1 - aggregateAboveRange(r6, r0);
    }

    public static <E extends Comparable> TreeMultiset<E> create() {
        return new TreeMultiset(Ordering.natural());
    }

    public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> r1) {
        TreeMultiset<E> r0 = create();
        Iterables.addAll(r0, r1);
        return r0;
    }

    public static <E> TreeMultiset<E> create(@CheckForNull Comparator<? super E> r2) {
        if (r2 != null) goto L5;
        return new TreeMultiset(Ordering.natural());
    L5:
        return new TreeMultiset(r2);
    }

    static int distinctElements(@CheckForNull AvlNode<?> r1) {
        if (r1 != null) goto L5;
        return 0;
    L5:
        return AvlNode.access$400(r1);
    }

    @CheckForNull
    private AvlNode<E> firstNode() {
        AvlNode<E> r0 = this.rootReference.get();
        if (r0 != null) goto L6;
        return null;
    L6:
        if (this.range.hasLowerBound() == false) goto L16;
        Object r2 = NullnessCasts.uncheckedCastNullableTToT(this.range.getLowerEndpoint());
        AvlNode<E> r3 = AvlNode.access$1000(r0, comparator(), r2);
        if (r3 != null) goto L11;
        return null;
    L11:
        if (this.range.getLowerBoundType() != BoundType.OPEN) goto L18;
        if (comparator().compare(r2, r3.getElement()) != 0) goto L18;
        r3 = AvlNode.access$700(r3);
    L18:
        if (r3 != this.header) goto L20;
        return null;
    L20:
        if (this.range.contains(r3.getElement()) == true) goto L23;
        return null;
    L23:
        return r3;
    L16:
        r3 = AvlNode.access$700(this.header);
        goto L18
    }

    @CheckForNull
    private AvlNode<E> lastNode() {
        AvlNode<E> r0 = this.rootReference.get();
        if (r0 != null) goto L6;
        return null;
    L6:
        if (this.range.hasUpperBound() == false) goto L16;
        Object r2 = NullnessCasts.uncheckedCastNullableTToT(this.range.getUpperEndpoint());
        AvlNode<E> r3 = AvlNode.access$1100(r0, comparator(), r2);
        if (r3 != null) goto L11;
        return null;
    L11:
        if (this.range.getUpperBoundType() != BoundType.OPEN) goto L18;
        if (comparator().compare(r2, r3.getElement()) != 0) goto L18;
        r3 = AvlNode.access$1200(r3);
    L18:
        if (r3 != this.header) goto L20;
        return null;
    L20:
        if (this.range.contains(r3.getElement()) == true) goto L23;
        return null;
    L23:
        return r3;
    L16:
        r3 = AvlNode.access$1200(this.header);
        goto L18
    }

    private void readObject(ObjectInputStream r6) throws IOException, ClassNotFoundException {
        r6.defaultReadObject();
        Comparator r0 = (Comparator) Objects.requireNonNull(r6.readObject());
        Serialization.getFieldSetter(AbstractSortedMultiset.class, "comparator").set(this, r0);
        Serialization.getFieldSetter(TreeMultiset.class, "range").set(this, GeneralRange.all(r0));
        Serialization.getFieldSetter(TreeMultiset.class, "rootReference").set(this, new Reference(null));
        AvlNode r1 = new AvlNode();
        Serialization.getFieldSetter(TreeMultiset.class, "header").set(this, r1);
        successor(r1, r1);
        Serialization.populateMultiset(this, r6);
    }

    private static <T> void successor(AvlNode<T> r0, AvlNode<T> r1) {
        AvlNode.access$902(r0, r1);
        AvlNode.access$802(r1, r0);
    }

    private static <T> void successor(AvlNode<T> r0, AvlNode<T> r1, AvlNode<T> r2) {
        successor(r0, r1);
        successor(r1, r2);
    }

    private Multiset.Entry<E> wrapEntry(final AvlNode<E> r2) {
        return new C07581(this, r2);
    }

    private void writeObject(ObjectOutputStream r2) throws IOException {
        r2.defaultWriteObject();
        r2.writeObject(elementSet().comparator());
        Serialization.writeMultiset(this, r2);
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int add(@ParametricNullness E r7, int r8) {
        CollectPreconditions.checkNonnegative(r8, "occurrences");
        if (r8 == 0) goto L5;
        Preconditions.checkArgument(this.range.contains(r7));
        AvlNode<E> r0 = this.rootReference.get();
        if (r0 != null) goto L10;
        comparator().compare(r7, r7);
        AvlNode<E> r3 = new AvlNode(r7, r8);
        successor(this.header, r3, this.header);
        this.rootReference.checkAndSet(r0, r3);
        return 0;
    L10:
        int[] r2 = new int[1];
        AvlNode<E> r4 = r0.add(comparator(), r7, r8, r2);
        this.rootReference.checkAndSet(r0, r4);
        return r2[0];
    L5:
        return count(r7);
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        if (this.range.hasLowerBound() == false) goto L5;
    L11:
        Iterators.clear(entryIterator());
        return;
    L5:
        if (this.range.hasUpperBound() == true) goto L11;
        AvlNode<E> r0 = AvlNode.access$700(this.header);
    L8:
        if (r0 == this.header) goto L10;
        AvlNode<E> r1 = AvlNode.access$700(r0);
        AvlNode.access$202(r0, 0);
        AvlNode.access$502(r0, null);
        AvlNode.access$602(r0, null);
        AvlNode.access$802(r0, null);
        AvlNode.access$902(r0, null);
        r0 = r1;
        goto L8
    L10:
        successor(this.header, this.header);
        this.rootReference.clear();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset, com.google.common.collect.SortedIterable
    public /* bridge */ /* synthetic */ Comparator comparator() {
        return super.comparator();
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ boolean contains(@CheckForNull Object r1) {
        return super.contains(r1);
    }

    @Override // com.google.common.collect.Multiset
    public int count(@CheckForNull Object r5) {
        AvlNode<E> r2 = this.rootReference.get();     // Catch: Throwable -> L10 ClassCastException -> L12
        if (this.range.contains(r5) == false) goto L9;
        if (r2 == null) goto L9;
        return r2.count(comparator(), r5);
    L9:
        return 0;
    L13:
        return 0;
    }

    @Override // com.google.common.collect.AbstractSortedMultiset
    Iterator<Multiset.Entry<E>> descendingEntryIterator() {
        return new C07603(this);
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    public /* bridge */ /* synthetic */ SortedMultiset descendingMultiset() {
        return super.descendingMultiset();
    }

    @Override // com.google.common.collect.AbstractMultiset
    int distinctElements() {
        return Ints.saturatedCast(aggregateForEntries(Aggregate.DISTINCT));
    }

    @Override // com.google.common.collect.AbstractMultiset
    Iterator<E> elementIterator() {
        return Multisets.elementIterator(entryIterator());
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ NavigableSet elementSet() {
        return super.elementSet();
    }

    @Override // com.google.common.collect.AbstractMultiset
    Iterator<Multiset.Entry<E>> entryIterator() {
        return new C07592(this);
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    @CheckForNull
    public /* bridge */ /* synthetic */ Multiset.Entry firstEntry() {
        return super.firstEntry();
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> headMultiset(@ParametricNullness E r5, BoundType r6) {
        return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.upTo(comparator(), r5, r6)), this.header);
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
    public Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    @CheckForNull
    public /* bridge */ /* synthetic */ Multiset.Entry lastEntry() {
        return super.lastEntry();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    @CheckForNull
    public /* bridge */ /* synthetic */ Multiset.Entry pollFirstEntry() {
        return super.pollFirstEntry();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    @CheckForNull
    public /* bridge */ /* synthetic */ Multiset.Entry pollLastEntry() {
        return super.pollLastEntry();
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int remove(@CheckForNull Object r6, int r7) {
        CollectPreconditions.checkNonnegative(r7, "occurrences");
        if (r7 == 0) goto L5;
        AvlNode<E> r0 = this.rootReference.get();
        int[] r1 = new int[1];
        if (this.range.contains(r6) == false) goto L14;
        if (r0 == null) goto L14;
        AvlNode<E> r4 = r0.remove(comparator(), r6, r7, r1);     // Catch: NullPointerException -> L15 Throwable -> L17
        this.rootReference.checkAndSet(r0, r4);
        return r1[0];
    L14:
        return 0;
    L18:
        return 0;
    L5:
        return count(r6);
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int setCount(@ParametricNullness E r6, int r7) {
        CollectPreconditions.checkNonnegative(r7, "count");
        boolean r1 = true;
        if (this.range.contains(r6) == true) goto L9;
        if (r7 == 0) goto L7;
        r1 = false;
    L7:
        Preconditions.checkArgument(r1);
        return 0;
    L9:
        AvlNode<E> r0 = this.rootReference.get();
        if (r0 != null) goto L14;
        if (r7 <= 0) goto L13;
        add(r6, r7);
    L13:
        return 0;
    L14:
        int[] r2 = new int[1];
        this.rootReference.checkAndSet(r0, r0.setCount(comparator(), r6, r7, r2));
        return r2[0];
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public boolean setCount(@ParametricNullness E r9, int r10, int r11) {
        CollectPreconditions.checkNonnegative(r11, "newCount");
        CollectPreconditions.checkNonnegative(r10, "oldCount");
        Preconditions.checkArgument(this.range.contains(r9));
        AvlNode<E> r1 = this.rootReference.get();
        if (r1 != null) goto L9;
        if (r10 != 0) goto L8;
        if (r11 <= 0) goto L7;
        add(r9, r11);
    L7:
        return true;
    L8:
        return false;
    L9:
        int[] r6 = new int[1];
        AvlNode<E> r12 = r1.setCount(comparator(), r9, r10, r11, r6);
        this.rootReference.checkAndSet(r1, r12);
        if (r6[0] == r10) goto L12;
        return false;
    L12:
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public int size() {
        return Ints.saturatedCast(aggregateForEntries(Aggregate.SIZE));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    public /* bridge */ /* synthetic */ SortedMultiset subMultiset(@ParametricNullness Object r1, BoundType r2, @ParametricNullness Object r3, BoundType r4) {
        return super.subMultiset(r1, r2, r3, r4);
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> tailMultiset(@ParametricNullness E r5, BoundType r6) {
        return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.downTo(comparator(), r5, r6)), this.header);
    }
}
