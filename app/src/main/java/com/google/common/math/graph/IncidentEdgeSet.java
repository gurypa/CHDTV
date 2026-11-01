package com.google.common.graph;

import java.util.AbstractSet;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class IncidentEdgeSet<N> extends AbstractSet<EndpointPair<N>> {
    final BaseGraph<N> graph;
    final N node;

    IncidentEdgeSet(BaseGraph<N> r1, N r2) {
        this.graph = r1;
        this.node = r2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@CheckForNull Object r8) {
        if ((r8 instanceof EndpointPair) == true) goto L5;
        return false;
    L5:
        EndpointPair r0 = (EndpointPair) r8;
        if (this.graph.isDirected() == false) goto L21;
        if (r0.isOrdered() == true) goto L10;
        return false;
    L10:
        Object r2 = r0.source();
        Object r4 = r0.target();
        if (this.node.equals(r2) == false) goto L15;
        if (this.graph.successors(this.node).contains(r4) == false) goto L15;
    L19:
        return true;
    L15:
        if (this.node.equals(r4) == true) goto L17;
        return false;
    L17:
        if (this.graph.predecessors(this.node).contains(r2) == true) goto L19;
        return false;
    L21:
        if (r0.isOrdered() == false) goto L23;
        return false;
    L23:
        Set<N> r3 = this.graph.adjacentNodes(this.node);
        Object r5 = r0.nodeU();
        Object r6 = r0.nodeV();
        if (this.node.equals(r6) == false) goto L28;
        if (r3.contains(r5) == false) goto L28;
    L32:
        return true;
    L28:
        if (this.node.equals(r5) == true) goto L30;
        return false;
    L30:
        if (r3.contains(r6) == true) goto L32;
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(@CheckForNull Object r2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        if (this.graph.isDirected() == false) goto L7;
        return (this.graph.inDegree(this.node) + this.graph.outDegree(this.node)) - (this.graph.successors(this.node).contains(this.node) ? 1 : 0);
    L7:
        return this.graph.adjacentNodes(this.node).size();
    }
}
