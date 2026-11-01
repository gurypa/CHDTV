package com.google.common.graph;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class StandardValueGraph<N, V> extends AbstractValueGraph<N, V> {
    private final boolean allowsSelfLoops;
    long edgeCount;
    private final boolean isDirected;
    final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
    private final ElementOrder<N> nodeOrder;

    StandardValueGraph(AbstractGraphBuilder<? super N> r4) {
        this(r4, r4.nodeOrder.createMap(r4.expectedNodeCount.mo179or(10).intValue()), 0);
    }

    StandardValueGraph(AbstractGraphBuilder<? super N> r3, Map<N, GraphConnections<N, V>> r4, long r5) {
        this.isDirected = r3.directed;
        this.allowsSelfLoops = r3.allowsSelfLoops;
        this.nodeOrder = (ElementOrder<N>) r3.nodeOrder.cast();
        if ((r4 instanceof TreeMap) == false) goto L5;
        MapIteratorCache<N, GraphConnections<N, V>> r0 = new MapRetrievalCache(r4);
    L6:
        this.nodeConnections = r0;
        this.edgeCount = Graphs.checkNonNegative(r5);
        return;
    L5:
        r0 = new MapIteratorCache(r4);
        goto L6
    }

    private final GraphConnections<N, V> checkedConnections(N r5) {
        GraphConnections<N, V> r0 = this.nodeConnections.get(r5);
        if (r0 == null) goto L5;
        return r0;
    L5:
        Preconditions.checkNotNull(r5);
        throw new IllegalArgumentException("Node " + r5 + " is not an element of this graph.");
    }

    @CheckForNull
    private final V edgeValueOrDefaultInternal(N r3, N r4, @CheckForNull V r5) {
        GraphConnections<N, V> r0 = this.nodeConnections.get(r3);
        if (r0 != null) goto L5;
        V r1 = null;
    L6:
        if (r1 != null) goto L8;
        return r5;
    L8:
        return r1;
    L5:
        r1 = r0.value(r4);
        goto L6
    }

    private final boolean hasEdgeConnectingInternal(N r3, N r4) {
        GraphConnections<N, V> r0 = this.nodeConnections.get(r3);
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (r0.successors().contains(r4) == false) goto L7;
        return true;
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public Set<N> adjacentNodes(N r2) {
        return (Set<N>) nodeInvalidatableSet(checkedConnections(r2).adjacentNodes(), r2);
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }

    final boolean containsNode(@CheckForNull N r2) {
        return this.nodeConnections.containsKey(r2);
    }

    @Override // com.google.common.graph.AbstractBaseGraph
    protected long edgeCount() {
        return this.edgeCount;
    }

    @CheckForNull
    public V edgeValueOrDefault(EndpointPair<N> r3, @CheckForNull V r4) {
        validateEndpoints(r3);
        return edgeValueOrDefaultInternal(r3.nodeU(), r3.nodeV(), r4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @CheckForNull
    public V edgeValueOrDefault(N r3, N r4, @CheckForNull V r5) {
        return (V) edgeValueOrDefaultInternal(Preconditions.checkNotNull(r3), Preconditions.checkNotNull(r4), r5);
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(EndpointPair<N> r3) {
        Preconditions.checkNotNull(r3);
        if (isOrderingCompatible(r3) == true) goto L5;
    L7:
        return false;
    L5:
        if (hasEdgeConnectingInternal(r3.nodeU(), r3.nodeV()) == false) goto L7;
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(N r3, N r4) {
        return hasEdgeConnectingInternal(Preconditions.checkNotNull(r3), Preconditions.checkNotNull(r4));
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public Set<EndpointPair<N>> incidentEdges(final N r4) {
        final GraphConnections<N, V> r0 = checkedConnections(r4);
        return (Set<EndpointPair<N>>) nodeInvalidatableSet(new C08071(this, this, r4, r0), r4);
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public boolean isDirected() {
        return this.isDirected;
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
    public /* bridge */ /* synthetic */ Iterable predecessors(Object r1) {
        return predecessors(r1);
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
    public Set<N> predecessors(N r2) {
        return (Set<N>) nodeInvalidatableSet(checkedConnections(r2).predecessors(), r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
    public /* bridge */ /* synthetic */ Iterable successors(Object r1) {
        return successors(r1);
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
    public Set<N> successors(N r2) {
        return (Set<N>) nodeInvalidatableSet(checkedConnections(r2).successors(), r2);
    }
}
