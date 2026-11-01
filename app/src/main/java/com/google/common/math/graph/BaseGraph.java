package com.google.common.graph;

import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
interface BaseGraph<N> extends SuccessorsFunction<N>, PredecessorsFunction<N> {

    /* JADX INFO: renamed from: com.google.common.graph.BaseGraph$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ Iterable $default$predecessors(BaseGraph r0, Object r1) {
            return r0.predecessors(r1);
        }

        public static /* bridge */ /* synthetic */ Iterable $default$successors(BaseGraph r0, Object r1) {
            return r0.successors(r1);
        }
    }

    Set<N> adjacentNodes(N r1);

    boolean allowsSelfLoops();

    int degree(N r1);

    Set<EndpointPair<N>> edges();

    boolean hasEdgeConnecting(EndpointPair<N> r1);

    boolean hasEdgeConnecting(N r1, N r2);

    int inDegree(N r1);

    ElementOrder<N> incidentEdgeOrder();

    Set<EndpointPair<N>> incidentEdges(N r1);

    boolean isDirected();

    ElementOrder<N> nodeOrder();

    Set<N> nodes();

    int outDegree(N r1);

    @Override // com.google.common.graph.PredecessorsFunction
    Set<N> predecessors(N r1);

    @Override // com.google.common.graph.SuccessorsFunction
    Set<N> successors(N r1);
}
