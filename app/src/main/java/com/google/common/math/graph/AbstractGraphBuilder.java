package com.google.common.graph;

import com.google.common.base.Optional;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractGraphBuilder<N> {
    boolean allowsSelfLoops;
    final boolean directed;
    Optional<Integer> expectedNodeCount;
    ElementOrder<N> incidentEdgeOrder;
    ElementOrder<N> nodeOrder;

    AbstractGraphBuilder(boolean r2) {
        this.allowsSelfLoops = false;
        this.nodeOrder = ElementOrder.insertion();
        this.incidentEdgeOrder = ElementOrder.unordered();
        this.expectedNodeCount = Optional.absent();
        this.directed = r2;
    }
}
