package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Objects;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class StandardMutableValueGraph<N, V> extends StandardValueGraph<N, V> implements MutableValueGraph<N, V> {
    private final ElementOrder<N> incidentEdgeOrder;

    StandardMutableValueGraph(AbstractGraphBuilder<? super N> r2) {
        super(r2);
        this.incidentEdgeOrder = (ElementOrder<N>) r2.incidentEdgeOrder.cast();
    }

    private GraphConnections<N, V> addNodeInternal(N r3) {
        GraphConnections<N, V> r0 = newConnections();
        if (this.nodeConnections.put(r3, r0) != null) goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkState(r1);
        return r0;
    L5:
        r1 = false;
        goto L6
    }

    private GraphConnections<N, V> newConnections() {
        if (isDirected() == false) goto L6;
        return DirectedGraphConnections.m329of(this.incidentEdgeOrder);
    L6:
        return UndirectedGraphConnections.m337of(this.incidentEdgeOrder);
    }

    @Override // com.google.common.graph.MutableValueGraph
    public boolean addNode(N r2) {
        Preconditions.checkNotNull(r2, "node");
        if (containsNode(r2) == false) goto L6;
        return false;
    L6:
        addNodeInternal(r2);
        return true;
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public ElementOrder<N> incidentEdgeOrder() {
        return this.incidentEdgeOrder;
    }

    @Override // com.google.common.graph.MutableValueGraph
    @CheckForNull
    public V putEdgeValue(EndpointPair<N> r3, V r4) {
        validateEndpoints(r3);
        return putEdgeValue(r3.nodeU(), r3.nodeV(), r4);
    }

    @Override // com.google.common.graph.MutableValueGraph
    @CheckForNull
    public V putEdgeValue(N r8, N r9, V r10) {
        Preconditions.checkNotNull(r8, "nodeU");
        Preconditions.checkNotNull(r9, "nodeV");
        Preconditions.checkNotNull(r10, "value");
        if (allowsSelfLoops() == true) goto L5;
        Preconditions.checkArgument(!r8.equals(r9), "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", r8);
    L5:
        GraphConnections<N, V> r0 = this.nodeConnections.get(r8);
        if (r0 != null) goto L8;
        r0 = addNodeInternal(r8);
    L8:
        V r1 = r0.addSuccessor(r9, r10);
        GraphConnections<N, V> r2 = this.nodeConnections.get(r9);
        if (r2 != null) goto L11;
        r2 = addNodeInternal(r9);
    L11:
        r2.addPredecessor(r8, r10);
        if (r1 != null) goto L14;
        long r3 = this.edgeCount + 1;
        this.edgeCount = r3;
        Graphs.checkPositive(r3);
    L14:
        return r1;
    }

    @Override // com.google.common.graph.MutableValueGraph
    @CheckForNull
    public V removeEdge(EndpointPair<N> r3) {
        validateEndpoints(r3);
        return removeEdge(r3.nodeU(), r3.nodeV());
    }

    @Override // com.google.common.graph.MutableValueGraph
    @CheckForNull
    public V removeEdge(N r8, N r9) {
        Preconditions.checkNotNull(r8, "nodeU");
        Preconditions.checkNotNull(r9, "nodeV");
        GraphConnections<N, V> r0 = this.nodeConnections.get(r8);
        GraphConnections<N, V> r1 = this.nodeConnections.get(r9);
        if (r0 == null) goto L10;
        if (r1 == null) goto L12;
        V r2 = r0.removeSuccessor(r9);
        if (r2 == null) goto L9;
        r1.removePredecessor(r8);
        long r3 = this.edgeCount - 1;
        this.edgeCount = r3;
        Graphs.checkNonNegative(r3);
    L9:
        return r2;
    L12:
        return null;
    L10:
        return null;
    }

    @Override // com.google.common.graph.MutableValueGraph
    public boolean removeNode(N r10) {
        Preconditions.checkNotNull(r10, "node");
        GraphConnections r0 = (GraphConnections<N, V>) this.nodeConnections.get(r10);
        if (r0 != null) goto L6;
        return false;
    L6:
        if (allowsSelfLoops() == true) goto L8;
    L10:
        UnmodifiableIterator r2 = ImmutableList.copyOf(r0.successors()).iterator();
    L12:
        if (r2.hasNext() == false) goto L15;
        E r5 = r2.next();
        ((GraphConnections) Objects.requireNonNull(this.nodeConnections.getWithoutCaching(r5))).removePredecessor(r10);
        Objects.requireNonNull(r0.removeSuccessor(r5));
        this.edgeCount--;
        goto L12
    L15:
        if (isDirected() == false) goto L24;
        UnmodifiableIterator r3 = ImmutableList.copyOf(r0.predecessors()).iterator();
    L18:
        if (r3.hasNext() == false) goto L24;
        E r6 = r3.next();
        if (((GraphConnections) Objects.requireNonNull(this.nodeConnections.getWithoutCaching(r6))).removeSuccessor(r10) == null) goto L22;
        boolean r7 = true;
    L23:
        Preconditions.checkState(r7);
        r0.removePredecessor(r6);
        this.edgeCount--;
        goto L18
    L22:
        r7 = false;
    L24:
        this.nodeConnections.remove(r10);
        Graphs.checkNonNegative(this.edgeCount);
        return true;
    L8:
        if (r0.removeSuccessor(r10) == null) goto L10;
        r0.removePredecessor(r10);
        this.edgeCount--;
        goto L10
    }
}
