package com.google.common.graph;

import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface ValueGraph<N, V> extends BaseGraph<N> {

    /* JADX INFO: renamed from: com.google.common.graph.ValueGraph$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ Iterable $default$predecessors(ValueGraph r0, Object r1) {
            return r0.predecessors(r1);
        }

        public static /* bridge */ /* synthetic */ Iterable $default$successors(ValueGraph r0, Object r1) {
            return r0.successors(r1);
        }
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    Set<N> adjacentNodes(N r1);

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    boolean allowsSelfLoops();

    Graph<N> asGraph();

    @Override // com.google.common.graph.BaseGraph
    int degree(N r1);

    @CheckForNull
    V edgeValueOrDefault(EndpointPair<N> r1, @CheckForNull V r2);

    @CheckForNull
    V edgeValueOrDefault(N r1, N r2, @CheckForNull V r3);

    @Override // com.google.common.graph.BaseGraph
    Set<EndpointPair<N>> edges();

    boolean equals(@CheckForNull Object r1);

    @Override // com.google.common.graph.BaseGraph
    boolean hasEdgeConnecting(EndpointPair<N> r1);

    @Override // com.google.common.graph.BaseGraph
    boolean hasEdgeConnecting(N r1, N r2);

    int hashCode();

    @Override // com.google.common.graph.BaseGraph
    int inDegree(N r1);

    @Override // com.google.common.graph.BaseGraph
    ElementOrder<N> incidentEdgeOrder();

    @Override // com.google.common.graph.BaseGraph
    Set<EndpointPair<N>> incidentEdges(N r1);

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    boolean isDirected();

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    ElementOrder<N> nodeOrder();

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    Set<N> nodes();

    @Override // com.google.common.graph.BaseGraph
    int outDegree(N r1);

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.PredecessorsFunction
    Set<N> predecessors(N r1);

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.SuccessorsFunction
    Set<N> successors(N r1);
}
