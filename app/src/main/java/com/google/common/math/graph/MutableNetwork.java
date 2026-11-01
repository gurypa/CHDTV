package com.google.common.graph;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface MutableNetwork<N, E> extends Network<N, E> {
    boolean addEdge(EndpointPair<N> r1, E r2);

    boolean addEdge(N r1, N r2, E r3);

    boolean addNode(N r1);

    boolean removeEdge(E r1);

    boolean removeNode(N r1);
}
