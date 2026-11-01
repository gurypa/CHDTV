package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class DirectedGraphConnections<N, V> implements GraphConnections<N, V> {
    private static final Object PRED = null;
    private final Map<N, Object> adjacentNodeValues;

    @CheckForNull
    private final List<NodeConnection<N>> orderedNodeConnections;
    private int predecessorCount;
    private int successorCount;

    /* JADX INFO: renamed from: com.google.common.graph.DirectedGraphConnections$5 */
    static /* synthetic */ class C08005 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$graph$ElementOrder$Type = null;

        static {
            $SwitchMap$com$google$common$graph$ElementOrder$Type = new int[ElementOrder.Type.values().length];
            $SwitchMap$com$google$common$graph$ElementOrder$Type[ElementOrder.Type.UNORDERED.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L12:
            $SwitchMap$com$google$common$graph$ElementOrder$Type[ElementOrder.Type.STABLE.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
            return;
        }
    }

    private static abstract class NodeConnection<N> {
        final N node;

        static final class Pred<N> extends NodeConnection<N> {
            Pred(N r1) {
                super(r1);
            }

            public boolean equals(@CheckForNull Object r3) {
                if ((r3 instanceof Pred) == true) goto L5;
                return false;
            L5:
                return this.node.equals(((Pred) r3).node);
            }

            public int hashCode() {
                return Pred.class.hashCode() + this.node.hashCode();
            }
        }

        static final class Succ<N> extends NodeConnection<N> {
            Succ(N r1) {
                super(r1);
            }

            public boolean equals(@CheckForNull Object r3) {
                if ((r3 instanceof Succ) == true) goto L5;
                return false;
            L5:
                return this.node.equals(((Succ) r3).node);
            }

            public int hashCode() {
                return Succ.class.hashCode() + this.node.hashCode();
            }
        }

        NodeConnection(N r2) {
            this.node = (N) Preconditions.checkNotNull(r2);
        }
    }

    private static final class PredAndSucc {
        private final Object successorValue;

        PredAndSucc(Object r1) {
            this.successorValue = r1;
        }

        static /* synthetic */ Object access$600(PredAndSucc r1) {
            return r1.successorValue;
        }
    }

    static {
        PRED = new Object();
    }

    private DirectedGraphConnections(Map<N, Object> r2, @CheckForNull List<NodeConnection<N>> r3, int r4, int r5) {
        this.adjacentNodeValues = (Map) Preconditions.checkNotNull(r2);
        this.orderedNodeConnections = r3;
        this.predecessorCount = Graphs.checkNonNegative(r4);
        this.successorCount = Graphs.checkNonNegative(r5);
        if (r4 <= r2.size()) goto L5;
    L7:
        boolean r0 = false;
    L8:
        Preconditions.checkState(r0);
        return;
    L5:
        if (r5 > r2.size()) goto L7;
        r0 = true;
        goto L8
    }

    static /* synthetic */ List access$000(DirectedGraphConnections r1) {
        return r1.orderedNodeConnections;
    }

    static /* synthetic */ Map access$100(DirectedGraphConnections r1) {
        return r1.adjacentNodeValues;
    }

    static /* synthetic */ boolean access$200(Object r1) {
        return isPredecessor(r1);
    }

    static /* synthetic */ int access$300(DirectedGraphConnections r1) {
        return r1.predecessorCount;
    }

    static /* synthetic */ boolean access$400(Object r1) {
        return isSuccessor(r1);
    }

    static /* synthetic */ int access$500(DirectedGraphConnections r1) {
        return r1.successorCount;
    }

    private static boolean isPredecessor(@CheckForNull Object r1) {
        if (r1 != PRED) goto L5;
    L8:
        return true;
    L5:
        if ((r1 instanceof PredAndSucc) == true) goto L8;
        return false;
    }

    private static boolean isSuccessor(@CheckForNull Object r1) {
        if (r1 == PRED) goto L6;
        if (r1 == null) goto L6;
        return true;
    L6:
        return false;
    }

    static /* synthetic */ EndpointPair lambda$incidentEdgeIterator$0(Object r1, Object r2) {
        return EndpointPair.ordered(r2, r1);
    }

    static /* synthetic */ EndpointPair lambda$incidentEdgeIterator$1(Object r1, Object r2) {
        return EndpointPair.ordered(r1, r2);
    }

    static /* synthetic */ EndpointPair lambda$incidentEdgeIterator$2(Object r1, NodeConnection r2) {
        if ((r2 instanceof NodeConnection.Succ) == false) goto L7;
        return EndpointPair.ordered(r1, r2.node);
    L7:
        return EndpointPair.ordered(r2.node, r1);
    }

    /* JADX INFO: renamed from: of */
    static <N, V> DirectedGraphConnections<N, V> m329of(ElementOrder<N> r5) {
        switch(C08005.$SwitchMap$com$google$common$graph$ElementOrder$Type[r5.type().ordinal()]) {
            case 1: goto L7;
            case 2: goto L6;
            default: goto L5;
        };
    L6:
        ArrayList r1 = new ArrayList();
    L9:
        return new DirectedGraphConnections(new HashMap(4, 1.0f), r1, 0, 0);
    L7:
        r1 = null;
        goto L9
    L5:
        throw new AssertionError(r5.type());
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <N, V> DirectedGraphConnections<N, V> ofImmutable(N r10, Iterable<EndpointPair<N>> r11, Function<N, V> r12) {
        Preconditions.checkNotNull(r10);
        Preconditions.checkNotNull(r12);
        HashMap r0 = new HashMap();
        ImmutableList.Builder r1 = ImmutableList.builder();
        int r2 = 0;
        int r3 = 0;
        Iterator<EndpointPair<N>> r4 = r11.iterator();
    L4:
        if (r4.hasNext() == false) goto L26;
        EndpointPair<N> r5 = r4.next();
        if (r5.nodeU().equals(r10) == false) goto L11;
        if (r5.nodeV().equals(r10) == false) goto L11;
        r0.put(r10, new PredAndSucc(r12.apply(r10)));
        r1.add(new NodeConnection.Pred(r10));
        r1.add(new NodeConnection.Succ(r10));
        r2 = r2 + 1;
        r3 = r3 + 1;
    L11:
        if (r5.nodeV().equals(r10) == true) goto L12;
        Preconditions.checkArgument(r5.nodeU().equals(r10));
        N r6 = r5.nodeV();
        V r7 = r12.apply(r6);
        Object r8 = r0.put(r6, r7);
        if (r8 != null) goto L19;
    L23:
        r1.add(new NodeConnection.Succ(r6));
        r3 = r3 + 1;
        goto L4
    L19:
        if (r8 != PRED) goto L21;
        boolean r9 = true;
    L22:
        Preconditions.checkArgument(r9);
        r0.put(r6, new PredAndSucc(r7));
        goto L23
    L21:
        r9 = false;
        goto L22
    L12:
        N r13 = r5.nodeU();
        Object r14 = r0.put(r13, PRED);
        if (r14 == null) goto L15;
        r0.put(r13, new PredAndSucc(r14));
    L15:
        r1.add(new NodeConnection.Pred(r13));
        r2 = r2 + 1;
        goto L4
    L26:
        return new DirectedGraphConnections(r0, r1.build(), r2, r3);
    }

    @Override // com.google.common.graph.GraphConnections
    public void addPredecessor(N r5, V r6) {
        Object r0 = this.adjacentNodeValues.put(r5, PRED);
        if (r0 != null) goto L6;
        boolean r1 = true;
    L12:
        if (r1 == false) goto L17;
        int r2 = this.predecessorCount + 1;
        this.predecessorCount = r2;
        Graphs.checkPositive(r2);
        if (this.orderedNodeConnections == null) goto L18;
        this.orderedNodeConnections.add(new NodeConnection.Pred(r5));
        return;
    L18:
        return;
    L17:
        return;
    L6:
        if ((r0 instanceof PredAndSucc) == false) goto L9;
        this.adjacentNodeValues.put(r5, r0);
        r1 = false;
        goto L12
    L9:
        if (r0 == PRED) goto L11;
        this.adjacentNodeValues.put(r5, new PredAndSucc(r0));
        r1 = true;
        goto L12
    L11:
        r1 = false;
        goto L12
    }

    @Override // com.google.common.graph.GraphConnections
    @CheckForNull
    public V addSuccessor(N r5, V r6) {
        Object r0 = this.adjacentNodeValues.put(r5, r6);
        if (r0 != null) goto L6;
        Object r1 = null;
    L12:
        if (r1 != null) goto L16;
        int r2 = this.successorCount + 1;
        this.successorCount = r2;
        Graphs.checkPositive(r2);
        if (this.orderedNodeConnections == null) goto L16;
        this.orderedNodeConnections.add(new NodeConnection.Succ(r5));
    L16:
        if (r1 != null) goto L19;
        return null;
    L19:
        return (V) r1;
    L6:
        if ((r0 instanceof PredAndSucc) == false) goto L9;
        this.adjacentNodeValues.put(r5, new PredAndSucc(r6));
        r1 = PredAndSucc.access$600((PredAndSucc) r0);
        goto L12
    L9:
        if (r0 != PRED) goto L11;
        this.adjacentNodeValues.put(r5, new PredAndSucc(r6));
        r1 = null;
        goto L12
    L11:
        r1 = r0;
        goto L12
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> adjacentNodes() {
        if (this.orderedNodeConnections != null) goto L7;
        return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
    L7:
        return new C07961(this);
    }

    @Override // com.google.common.graph.GraphConnections
    public Iterator<EndpointPair<N>> incidentEdgeIterator(final N r4) {
        Preconditions.checkNotNull(r4);
        if (this.orderedNodeConnections != null) goto L5;
        final Iterator r0 = Iterators.concat(Iterators.transform(predecessors().iterator(), new DirectedGraphConnections$$ExternalSyntheticLambda0(r4)), Iterators.transform(successors().iterator(), new DirectedGraphConnections$$ExternalSyntheticLambda1(r4)));
    L6:
        final AtomicBoolean r1 = new AtomicBoolean(false);
        return new C07994(this, r0, r1);
    L5:
        r0 = Iterators.transform(this.orderedNodeConnections.iterator(), new DirectedGraphConnections$$ExternalSyntheticLambda2(r4));
        goto L6
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> predecessors() {
        return new C07972(this);
    }

    @Override // com.google.common.graph.GraphConnections
    public void removePredecessor(N r5) {
        Preconditions.checkNotNull(r5);
        Object r0 = this.adjacentNodeValues.get(r5);
        if (r0 != PRED) goto L6;
        this.adjacentNodeValues.remove(r5);
        boolean r1 = true;
    L9:
        if (r1 == false) goto L14;
        int r2 = this.predecessorCount - 1;
        this.predecessorCount = r2;
        Graphs.checkNonNegative(r2);
        if (this.orderedNodeConnections == null) goto L15;
        this.orderedNodeConnections.remove(new NodeConnection.Pred(r5));
        return;
    L15:
        return;
    L14:
        return;
    L6:
        if ((r0 instanceof PredAndSucc) == false) goto L8;
        this.adjacentNodeValues.put(r5, PredAndSucc.access$600((PredAndSucc) r0));
        r1 = true;
        goto L9
    L8:
        r1 = false;
        goto L9
    }

    @Override // com.google.common.graph.GraphConnections
    @CheckForNull
    public V removeSuccessor(Object r5) {
        Preconditions.checkNotNull(r5);
        Object r0 = this.adjacentNodeValues.get(r5);
        if (r0 != null) goto L5;
    L11:
        Object r1 = null;
    L12:
        if (r1 == null) goto L16;
        int r2 = this.successorCount - 1;
        this.successorCount = r2;
        Graphs.checkNonNegative(r2);
        if (this.orderedNodeConnections == null) goto L16;
        this.orderedNodeConnections.remove(new NodeConnection.Succ(r5));
    L16:
        if (r1 != null) goto L19;
        return null;
    L19:
        return (V) r1;
    L5:
        if (r0 == PRED) goto L11;
        if ((r0 instanceof PredAndSucc) == false) goto L10;
        this.adjacentNodeValues.put(r5, PRED);
        r1 = PredAndSucc.access$600((PredAndSucc) r0);
        goto L12
    L10:
        this.adjacentNodeValues.remove(r5);
        r1 = r0;
        goto L12
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> successors() {
        return new C07983(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.GraphConnections
    @CheckForNull
    public V value(N r3) {
        Preconditions.checkNotNull(r3);
        V r0 = (V) this.adjacentNodeValues.get(r3);
        if (r0 != PRED) goto L7;
        return null;
    L7:
        if ((r0 instanceof PredAndSucc) == true) goto L9;
        return r0;
    L9:
        return (V) PredAndSucc.access$600((PredAndSucc) r0);
    }
}
