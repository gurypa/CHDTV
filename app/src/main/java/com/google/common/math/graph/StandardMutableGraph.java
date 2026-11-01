package com.google.common.graph;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class StandardMutableGraph<N> extends ForwardingGraph<N> implements MutableGraph<N> {
    private final MutableValueGraph<N, GraphConstants.Presence> backingValueGraph;

    StandardMutableGraph(AbstractGraphBuilder<? super N> r2) {
        this.backingValueGraph = new StandardMutableValueGraph(r2);
    }

    @Override // com.google.common.graph.MutableGraph
    public boolean addNode(N r2) {
        return this.backingValueGraph.addNode(r2);
    }

    @Override // com.google.common.graph.ForwardingGraph
    BaseGraph<N> delegate() {
        return this.backingValueGraph;
    }

    @Override // com.google.common.graph.MutableGraph
    public boolean putEdge(EndpointPair<N> r3) {
        validateEndpoints(r3);
        return putEdge(r3.nodeU(), r3.nodeV());
    }

    @Override // com.google.common.graph.MutableGraph
    public boolean putEdge(N r3, N r4) {
        if (this.backingValueGraph.putEdgeValue(r3, r4, GraphConstants.Presence.EDGE_EXISTS) != null) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // com.google.common.graph.MutableGraph
    public boolean removeEdge(EndpointPair<N> r3) {
        validateEndpoints(r3);
        return removeEdge(r3.nodeU(), r3.nodeV());
    }

    @Override // com.google.common.graph.MutableGraph
    public boolean removeEdge(N r2, N r3) {
        if (this.backingValueGraph.removeEdge(r2, r3) == null) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // com.google.common.graph.MutableGraph
    public boolean removeNode(N r2) {
        return this.backingValueGraph.removeNode(r2);
    }
}
