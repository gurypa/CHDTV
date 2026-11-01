package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import com.google.errorprone.annotations.DoNotMock;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Call forGraph or forTree, passing a lambda or a Graph with the desired edges (built with GraphBuilder)")
@ElementTypesAreNonnullByDefault
public abstract class Traverser<N> {
    private final SuccessorsFunction<N> successorFunction;

    private static abstract enum InsertionOrder extends Enum<InsertionOrder> {
        private static final /* synthetic */ InsertionOrder[] $VALUES = null;
        public static final InsertionOrder BACK = null;
        public static final InsertionOrder FRONT = null;

        private static /* synthetic */ InsertionOrder[] $values() {
            return new InsertionOrder[]{FRONT, BACK};
        }

        static {
            final String r1 = "FRONT";
            final int r2 = 0;
            FRONT = new C08131(r1, r2);
            final String r3 = "BACK";
            final int r4 = 1;
            BACK = new C08142(r3, r4);
            $VALUES = $values();
        }

        private InsertionOrder(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ InsertionOrder(String r1, int r2, C08081 r3) {
            this(r1, r2);
        }

        public static InsertionOrder valueOf(String r1) {
            return (InsertionOrder) Enum.valueOf(InsertionOrder.class, r1);
        }

        public static InsertionOrder[] values() {
            return (InsertionOrder[]) $VALUES.clone();
        }

        abstract <T> void insertInto(Deque<T> r1, T r2);
    }

    private static abstract class Traversal<N> {
        final SuccessorsFunction<N> successorFunction;

        Traversal(SuccessorsFunction<N> r1) {
            this.successorFunction = r1;
        }

        static <N> Traversal<N> inGraph(final SuccessorsFunction<N> r2) {
            final HashSet r0 = new HashSet();
            return new C08151(r2, r0);
        }

        static <N> Traversal<N> inTree(final SuccessorsFunction<N> r1) {
            return new C08162(r1);
        }

        private Iterator<N> topDown(Iterator<? extends N> r3, final InsertionOrder r4) {
            final ArrayDeque r0 = new ArrayDeque();
            r0.add(r3);
            return new C08173(this, r0, r4);
        }

        final Iterator<N> breadthFirst(Iterator<? extends N> r2) {
            return topDown(r2, InsertionOrder.BACK);
        }

        final Iterator<N> postOrder(Iterator<? extends N> r4) {
            final ArrayDeque r0 = new ArrayDeque();
            final ArrayDeque r1 = new ArrayDeque();
            r1.add(r4);
            return new C08184(this, r1, r0);
        }

        final Iterator<N> preOrder(Iterator<? extends N> r2) {
            return topDown(r2, InsertionOrder.FRONT);
        }

        @CheckForNull
        abstract N visitNext(Deque<Iterator<? extends N>> r1);
    }

    private Traverser(SuccessorsFunction<N> r2) {
        this.successorFunction = (SuccessorsFunction) Preconditions.checkNotNull(r2);
    }

    /* synthetic */ Traverser(SuccessorsFunction r1, C08081 r2) {
        this(r1);
    }

    public static <N> Traverser<N> forGraph(final SuccessorsFunction<N> r1) {
        return new C08081(r1, r1);
    }

    public static <N> Traverser<N> forTree(final SuccessorsFunction<N> r2) {
        if ((r2 instanceof BaseGraph) == false) goto L6;
        Preconditions.checkArgument(((BaseGraph) r2).isDirected(), "Undirected graphs can never be trees.");
    L6:
        if ((r2 instanceof Network) == false) goto L9;
        Preconditions.checkArgument(((Network) r2).isDirected(), "Undirected networks can never be trees.");
    L9:
        return new C08092(r2, r2);
    }

    private ImmutableSet<N> validate(Iterable<? extends N> r5) {
        ImmutableSet<N> r0 = ImmutableSet.copyOf(r5);
        UnmodifiableIterator<N> r1 = r0.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        N r2 = r1.next();
        this.successorFunction.successors(r2);
        goto L4
    L6:
        return r0;
    }

    public final Iterable<N> breadthFirst(Iterable<? extends N> r3) {
        final ImmutableSet<N> r0 = validate(r3);
        return new C08103(this, r0);
    }

    public final Iterable<N> breadthFirst(N r2) {
        return breadthFirst(ImmutableSet.m260of(r2));
    }

    public final Iterable<N> depthFirstPostOrder(Iterable<? extends N> r3) {
        final ImmutableSet<N> r0 = validate(r3);
        return new C08125(this, r0);
    }

    public final Iterable<N> depthFirstPostOrder(N r2) {
        return depthFirstPostOrder(ImmutableSet.m260of(r2));
    }

    public final Iterable<N> depthFirstPreOrder(Iterable<? extends N> r3) {
        final ImmutableSet<N> r0 = validate(r3);
        return new C08114(this, r0);
    }

    public final Iterable<N> depthFirstPreOrder(N r2) {
        return depthFirstPreOrder(ImmutableSet.m260of(r2));
    }

    abstract Traversal<N> newTraversal();
}
