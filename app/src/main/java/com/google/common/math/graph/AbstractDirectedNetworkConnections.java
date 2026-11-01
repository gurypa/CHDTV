package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractDirectedNetworkConnections<N, E> implements NetworkConnections<N, E> {
    final Map<E, N> inEdgeMap;
    final Map<E, N> outEdgeMap;
    private int selfLoopCount;

    AbstractDirectedNetworkConnections(Map<E, N> r2, Map<E, N> r3, int r4) {
        this.inEdgeMap = (Map) Preconditions.checkNotNull(r2);
        this.outEdgeMap = (Map) Preconditions.checkNotNull(r3);
        this.selfLoopCount = Graphs.checkNonNegative(r4);
        if (r4 <= r2.size()) goto L5;
    L7:
        boolean r0 = false;
    L8:
        Preconditions.checkState(r0);
        return;
    L5:
        if (r4 > r3.size()) goto L7;
        r0 = true;
        goto L8
    }

    static /* synthetic */ int access$000(AbstractDirectedNetworkConnections r1) {
        return r1.selfLoopCount;
    }

    @Override // com.google.common.graph.NetworkConnections
    public void addInEdge(E r3, N r4, boolean r5) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        boolean r0 = true;
        if (r5 == false) goto L6;
        int r1 = this.selfLoopCount + 1;
        this.selfLoopCount = r1;
        Graphs.checkPositive(r1);
    L6:
        if (this.inEdgeMap.put(r3, r4) == null) goto L9;
        r0 = false;
    L9:
        Preconditions.checkState(r0);
    }

    @Override // com.google.common.graph.NetworkConnections
    public void addOutEdge(E r3, N r4) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        if (this.outEdgeMap.put(r3, r4) != null) goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkState(r1);
        return;
    L5:
        r1 = false;
        goto L6
    }

    @Override // com.google.common.graph.NetworkConnections
    public N adjacentNode(E r2) {
        return (N) Objects.requireNonNull(this.outEdgeMap.get(r2));
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<N> adjacentNodes() {
        return Sets.union(predecessors(), successors());
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<E> inEdges() {
        return Collections.unmodifiableSet(this.inEdgeMap.keySet());
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<E> incidentEdges() {
        return new C07921(this);
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<E> outEdges() {
        return Collections.unmodifiableSet(this.outEdgeMap.keySet());
    }

    @Override // com.google.common.graph.NetworkConnections
    public N removeInEdge(E r3, boolean r4) {
        if (r4 == false) goto L5;
        int r0 = this.selfLoopCount - 1;
        this.selfLoopCount = r0;
        Graphs.checkNonNegative(r0);
    L5:
        return (N) Objects.requireNonNull(this.inEdgeMap.remove(r3));
    }

    @Override // com.google.common.graph.NetworkConnections
    public N removeOutEdge(E r3) {
        return (N) Objects.requireNonNull(this.outEdgeMap.remove(r3));
    }
}
