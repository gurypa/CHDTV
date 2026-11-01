package com.google.common.graph;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface MutableGraph<N> extends Graph<N> {
    boolean addNode(N r1);

    boolean putEdge(EndpointPair<N> r1);

    boolean putEdge(N r1, N r2);

    boolean removeEdge(EndpointPair<N> r1);

    boolean removeEdge(N r1, N r2);

    boolean removeNode(N r1);
}
