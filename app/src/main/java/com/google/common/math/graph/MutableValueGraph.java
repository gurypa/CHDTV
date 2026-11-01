package com.google.common.graph;

import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface MutableValueGraph<N, V> extends ValueGraph<N, V> {
    boolean addNode(N r1);

    @CheckForNull
    V putEdgeValue(EndpointPair<N> r1, V r2);

    @CheckForNull
    V putEdgeValue(N r1, N r2, V r3);

    @CheckForNull
    V removeEdge(EndpointPair<N> r1);

    @CheckForNull
    V removeEdge(N r1, N r2);

    boolean removeNode(N r1);
}
