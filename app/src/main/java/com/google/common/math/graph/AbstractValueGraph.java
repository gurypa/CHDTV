package com.google.common.graph;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class AbstractValueGraph<N, V> extends AbstractBaseGraph<N> implements ValueGraph<N, V> {
    public AbstractValueGraph() {
    }

    private static <N, V> Map<EndpointPair<N>, V> edgeValueMap(final ValueGraph<N, V> r2) {
        return Maps.asMap(r2.edges(), new AbstractValueGraph$$ExternalSyntheticLambda0(r2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ Object lambda$edgeValueMap$0(ValueGraph r3, EndpointPair r4) {
        return Objects.requireNonNull(r3.edgeValueOrDefault(r4.nodeU(), r4.nodeV(), null));
    }

    @Override // com.google.common.graph.ValueGraph
    public Graph<N> asGraph() {
        return new C07951(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ int degree(Object r1) {
        return super.degree(r1);
    }

    @Override // com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ Set edges() {
        return super.edges();
    }

    @Override // com.google.common.graph.ValueGraph
    public final boolean equals(@CheckForNull Object r6) {
        if (r6 != this) goto L6;
        return true;
    L6:
        if ((r6 instanceof ValueGraph) == true) goto L8;
        return false;
    L8:
        ValueGraph r1 = (ValueGraph) r6;
        if (isDirected() != r1.isDirected()) goto L16;
        if (nodes().equals(r1.nodes()) == false) goto L16;
        if (edgeValueMap(this).equals(edgeValueMap(r1)) == false) goto L16;
        return true;
    L16:
        return false;
    }

    @Override // com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ boolean hasEdgeConnecting(EndpointPair r1) {
        return super.hasEdgeConnecting(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ boolean hasEdgeConnecting(Object r1, Object r2) {
        return super.hasEdgeConnecting(r1, r2);
    }

    @Override // com.google.common.graph.ValueGraph
    public final int hashCode() {
        return edgeValueMap(this).hashCode();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ int inDegree(Object r1) {
        return super.inDegree(r1);
    }

    @Override // com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ ElementOrder incidentEdgeOrder() {
        return super.incidentEdgeOrder();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ Set incidentEdges(Object r1) {
        return super.incidentEdges(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ int outDegree(Object r1) {
        return super.outDegree(r1);
    }

    @Override // com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
    public /* bridge */ /* synthetic */ Iterable predecessors(Object r1) {
        return ValueGraph.CC.$default$predecessors(this, r1);
    }

    @Override // com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
    public /* bridge */ /* synthetic */ Iterable successors(Object r1) {
        return ValueGraph.CC.$default$successors(this, r1);
    }

    public String toString() {
        return "isDirected: " + isDirected() + ", allowsSelfLoops: " + allowsSelfLoops() + ", nodes: " + nodes() + ", edges: " + edgeValueMap(this);
    }
}
