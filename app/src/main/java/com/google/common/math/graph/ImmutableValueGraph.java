package com.google.common.graph;

import androidx.exifinterface.media.ExifInterface;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.Immutable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@Immutable(containerOf = {"N", ExifInterface.GPS_MEASUREMENT_INTERRUPTED})
@ElementTypesAreNonnullByDefault
public final class ImmutableValueGraph<N, V> extends StandardValueGraph<N, V> {

    public static class Builder<N, V> {
        private final MutableValueGraph<N, V> mutableValueGraph;

        Builder(ValueGraphBuilder<N, V> r3) {
            this.mutableValueGraph = r3.copy().incidentEdgeOrder(ElementOrder.stable()).build();
        }

        public Builder<N, V> addNode(N r2) {
            this.mutableValueGraph.addNode(r2);
            return this;
        }

        public ImmutableValueGraph<N, V> build() {
            return ImmutableValueGraph.copyOf(this.mutableValueGraph);
        }

        public Builder<N, V> putEdgeValue(EndpointPair<N> r2, V r3) {
            this.mutableValueGraph.putEdgeValue(r2, r3);
            return this;
        }

        public Builder<N, V> putEdgeValue(N r2, N r3, V r4) {
            this.mutableValueGraph.putEdgeValue(r2, r3, r4);
            return this;
        }
    }

    private ImmutableValueGraph(ValueGraph<N, V> r5) {
        super(ValueGraphBuilder.from(r5), getNodeConnections(r5), r5.edges().size());
    }

    private static <N, V> GraphConnections<N, V> connectionsOf(final ValueGraph<N, V> r2, final N r3) {
        Function r0 = new ImmutableValueGraph$$ExternalSyntheticLambda0(r2, r3);
        if (r2.isDirected() == false) goto L6;
        return DirectedGraphConnections.ofImmutable(r3, r2.incidentEdges(r3), r0);
    L6:
        return UndirectedGraphConnections.ofImmutable(Maps.asMap(r2.adjacentNodes(r3), r0));
    }

    @Deprecated
    public static <N, V> ImmutableValueGraph<N, V> copyOf(ImmutableValueGraph<N, V> r1) {
        return (ImmutableValueGraph) Preconditions.checkNotNull(r1);
    }

    public static <N, V> ImmutableValueGraph<N, V> copyOf(ValueGraph<N, V> r1) {
        if ((r1 instanceof ImmutableValueGraph) == false) goto L6;
        return (ImmutableValueGraph) r1;
    L6:
        return new ImmutableValueGraph(r1);
    }

    private static <N, V> ImmutableMap<N, GraphConnections<N, V>> getNodeConnections(ValueGraph<N, V> r4) {
        ImmutableMap.Builder r0 = ImmutableMap.builder();
        Iterator<N> r1 = r4.nodes().iterator();
    L4:
        if (r1.hasNext() == false) goto L7;
        N r2 = r1.next();
        r0.put(r2, connectionsOf(r4, r2));
        goto L4
    L7:
        return r0.buildOrThrow();
    }

    static /* synthetic */ Object lambda$connectionsOf$0(ValueGraph r1, Object r2, Object r3) {
        return Objects.requireNonNull(r1.edgeValueOrDefault(r2, r3, null));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ Set adjacentNodes(Object r1) {
        return super.adjacentNodes(r1);
    }

    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ boolean allowsSelfLoops() {
        return super.allowsSelfLoops();
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.ValueGraph
    public /* bridge */ /* synthetic */ Graph asGraph() {
        return asGraph();
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.ValueGraph
    public ImmutableGraph<N> asGraph() {
        return new ImmutableGraph(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.ValueGraph
    @CheckForNull
    public /* bridge */ /* synthetic */ Object edgeValueOrDefault(EndpointPair r1, @CheckForNull Object r2) {
        return super.edgeValueOrDefault(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.ValueGraph
    @CheckForNull
    public /* bridge */ /* synthetic */ Object edgeValueOrDefault(Object r1, Object r2, @CheckForNull Object r3) {
        return super.edgeValueOrDefault(r1, r2, r3);
    }

    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ boolean hasEdgeConnecting(EndpointPair r1) {
        return super.hasEdgeConnecting(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ boolean hasEdgeConnecting(Object r1, Object r2) {
        return super.hasEdgeConnecting(r1, r2);
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public ElementOrder<N> incidentEdgeOrder() {
        return ElementOrder.stable();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ Set incidentEdges(Object r1) {
        return super.incidentEdges(r1);
    }

    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ boolean isDirected() {
        return super.isDirected();
    }

    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ ElementOrder nodeOrder() {
        return super.nodeOrder();
    }

    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ Set nodes() {
        return super.nodes();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
    public /* bridge */ /* synthetic */ Set predecessors(Object r1) {
        return super.predecessors(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
    public /* bridge */ /* synthetic */ Set successors(Object r1) {
        return super.successors(r1);
    }
}
