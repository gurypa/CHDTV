package com.google.common.graph;

import com.google.errorprone.annotations.DoNotMock;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use NetworkBuilder to create a real instance")
@ElementTypesAreNonnullByDefault
public interface Network<N, E> extends SuccessorsFunction<N>, PredecessorsFunction<N> {

    /* JADX INFO: renamed from: com.google.common.graph.Network$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ Iterable $default$predecessors(Network r0, Object r1) {
            return r0.predecessors(r1);
        }

        public static /* bridge */ /* synthetic */ Iterable $default$successors(Network r0, Object r1) {
            return r0.successors(r1);
        }
    }

    Set<E> adjacentEdges(E r1);

    Set<N> adjacentNodes(N r1);

    boolean allowsParallelEdges();

    boolean allowsSelfLoops();

    Graph<N> asGraph();

    int degree(N r1);

    @CheckForNull
    E edgeConnectingOrNull(EndpointPair<N> r1);

    @CheckForNull
    E edgeConnectingOrNull(N r1, N r2);

    ElementOrder<E> edgeOrder();

    Set<E> edges();

    Set<E> edgesConnecting(EndpointPair<N> r1);

    Set<E> edgesConnecting(N r1, N r2);

    boolean equals(@CheckForNull Object r1);

    boolean hasEdgeConnecting(EndpointPair<N> r1);

    boolean hasEdgeConnecting(N r1, N r2);

    int hashCode();

    int inDegree(N r1);

    Set<E> inEdges(N r1);

    Set<E> incidentEdges(N r1);

    EndpointPair<N> incidentNodes(E r1);

    boolean isDirected();

    ElementOrder<N> nodeOrder();

    Set<N> nodes();

    int outDegree(N r1);

    Set<E> outEdges(N r1);

    @Override // com.google.common.graph.PredecessorsFunction
    Set<N> predecessors(N r1);

    @Override // com.google.common.graph.SuccessorsFunction
    Set<N> successors(N r1);
}
