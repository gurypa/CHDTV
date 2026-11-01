package com.google.common.graph;

import java.util.Iterator;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
interface GraphConnections<N, V> {
    void addPredecessor(N r1, V r2);

    @CheckForNull
    V addSuccessor(N r1, V r2);

    Set<N> adjacentNodes();

    Iterator<EndpointPair<N>> incidentEdgeIterator(N r1);

    Set<N> predecessors();

    void removePredecessor(N r1);

    @CheckForNull
    V removeSuccessor(N r1);

    Set<N> successors();

    @CheckForNull
    V value(N r1);
}
