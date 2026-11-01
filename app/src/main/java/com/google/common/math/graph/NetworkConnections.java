package com.google.common.graph;

import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
interface NetworkConnections<N, E> {
    void addInEdge(E r1, N r2, boolean r3);

    void addOutEdge(E r1, N r2);

    N adjacentNode(E r1);

    Set<N> adjacentNodes();

    Set<E> edgesConnecting(N r1);

    Set<E> inEdges();

    Set<E> incidentEdges();

    Set<E> outEdges();

    Set<N> predecessors();

    @CheckForNull
    N removeInEdge(E r1, boolean r2);

    N removeOutEdge(E r1);

    Set<N> successors();
}
