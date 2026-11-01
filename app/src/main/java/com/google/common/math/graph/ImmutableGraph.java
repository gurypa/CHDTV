package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.Immutable;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@Immutable(containerOf = {"N"})
@ElementTypesAreNonnullByDefault
public class ImmutableGraph<N> extends ForwardingGraph<N> {
    private final BaseGraph<N> backingGraph;

    public static class Builder<N> {
        private final MutableGraph<N> mutableGraph;

        Builder(GraphBuilder<N> r3) {
            this.mutableGraph = r3.copy().incidentEdgeOrder(ElementOrder.stable()).build();
        }

        public Builder<N> addNode(N r2) {
            this.mutableGraph.addNode(r2);
            return this;
        }

        public ImmutableGraph<N> build() {
            return ImmutableGraph.copyOf(this.mutableGraph);
        }

        public Builder<N> putEdge(EndpointPair<N> r2) {
            this.mutableGraph.putEdge(r2);
            return this;
        }

        public Builder<N> putEdge(N r2, N r3) {
            this.mutableGraph.putEdge(r2, r3);
            return this;
        }
    }

    ImmutableGraph(BaseGraph<N> r1) {
        this.backingGraph = r1;
    }

    private static <N> GraphConnections<N, GraphConstants.Presence> connectionsOf(Graph<N> r2, N r3) {
        Function r0 = Functions.constant(GraphConstants.Presence.EDGE_EXISTS);
        if (r2.isDirected() == false) goto L6;
        return DirectedGraphConnections.ofImmutable(r3, r2.incidentEdges(r3), r0);
    L6:
        return UndirectedGraphConnections.ofImmutable(Maps.asMap(r2.adjacentNodes(r3), r0));
    }

    public static <N> ImmutableGraph<N> copyOf(Graph<N> r6) {
        if ((r6 instanceof ImmutableGraph) == false) goto L6;
        return (ImmutableGraph) r6;
    L6:
        return new ImmutableGraph(new StandardValueGraph(GraphBuilder.from(r6), getNodeConnections(r6), r6.edges().size()));
    }

    @Deprecated
    public static <N> ImmutableGraph<N> copyOf(ImmutableGraph<N> r1) {
        return (ImmutableGraph) Preconditions.checkNotNull(r1);
    }

    private static <N> ImmutableMap<N, GraphConnections<N, GraphConstants.Presence>> getNodeConnections(Graph<N> r4) {
        ImmutableMap.Builder r0 = ImmutableMap.builder();
        Iterator<N> r1 = r4.nodes().iterator();
    L4:
        if (r1.hasNext() == false) goto L7;
        N r2 = r1.next();
        r0.put(r2, connectionsOf(r4, r2));
        goto L4
    L7:
        return r0.buildOrThrow();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ Set adjacentNodes(Object r1) {
        return super.adjacentNodes(r1);
    }

    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ boolean allowsSelfLoops() {
        return super.allowsSelfLoops();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ int degree(Object r1) {
        return super.degree(r1);
    }

    @Override // com.google.common.graph.ForwardingGraph
    BaseGraph<N> delegate() {
        return this.backingGraph;
    }

    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ boolean hasEdgeConnecting(EndpointPair r1) {
        return super.hasEdgeConnecting(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ boolean hasEdgeConnecting(Object r1, Object r2) {
        return super.hasEdgeConnecting(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ int inDegree(Object r1) {
        return super.inDegree(r1);
    }

    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public ElementOrder<N> incidentEdgeOrder() {
        return ElementOrder.stable();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ Set incidentEdges(Object r1) {
        return super.incidentEdges(r1);
    }

    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ boolean isDirected() {
        return super.isDirected();
    }

    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ ElementOrder nodeOrder() {
        return super.nodeOrder();
    }

    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ Set nodes() {
        return super.nodes();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ int outDegree(Object r1) {
        return super.outDegree(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
    public /* bridge */ /* synthetic */ Set predecessors(Object r1) {
        return super.predecessors(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
    public /* bridge */ /* synthetic */ Set successors(Object r1) {
        return super.successors(r1);
    }
}
