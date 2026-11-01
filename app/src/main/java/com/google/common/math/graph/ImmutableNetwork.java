package com.google.common.graph;

import androidx.exifinterface.media.ExifInterface;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.Immutable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@Immutable(containerOf = {"N", ExifInterface.LONGITUDE_EAST})
@ElementTypesAreNonnullByDefault
public final class ImmutableNetwork<N, E> extends StandardNetwork<N, E> {

    public static class Builder<N, E> {
        private final MutableNetwork<N, E> mutableNetwork;

        Builder(NetworkBuilder<N, E> r2) {
            this.mutableNetwork = (MutableNetwork<N, E>) r2.build();
        }

        public Builder<N, E> addEdge(EndpointPair<N> r2, E r3) {
            this.mutableNetwork.addEdge(r2, r3);
            return this;
        }

        public Builder<N, E> addEdge(N r2, N r3, E r4) {
            this.mutableNetwork.addEdge(r2, r3, r4);
            return this;
        }

        public Builder<N, E> addNode(N r2) {
            this.mutableNetwork.addNode(r2);
            return this;
        }

        public ImmutableNetwork<N, E> build() {
            return ImmutableNetwork.copyOf(this.mutableNetwork);
        }
    }

    private ImmutableNetwork(Network<N, E> r4) {
        super(NetworkBuilder.from(r4), getNodeConnections(r4), getEdgeToReferenceNode(r4));
    }

    private static <N, E> Function<E, N> adjacentNodeFn(final Network<N, E> r1, final N r2) {
        return new ImmutableNetwork$$ExternalSyntheticLambda1(r1, r2);
    }

    private static <N, E> NetworkConnections<N, E> connectionsOf(Network<N, E> r4, N r5) {
        if (r4.isDirected() == false) goto L9;
        Map r0 = Maps.asMap(r4.inEdges(r5), sourceNodeFn(r4));
        Map r1 = Maps.asMap(r4.outEdges(r5), targetNodeFn(r4));
        int r2 = r4.edgesConnecting(r5, r5).size();
        if (r4.allowsParallelEdges() == false) goto L8;
        return DirectedMultiNetworkConnections.ofImmutable(r0, r1, r2);
    L8:
        return DirectedNetworkConnections.ofImmutable(r0, r1, r2);
    L9:
        Map r3 = Maps.asMap(r4.incidentEdges(r5), adjacentNodeFn(r4, r5));
        if (r4.allowsParallelEdges() == false) goto L13;
        return UndirectedMultiNetworkConnections.ofImmutable(r3);
    L13:
        return UndirectedNetworkConnections.ofImmutable(r3);
    }

    @Deprecated
    public static <N, E> ImmutableNetwork<N, E> copyOf(ImmutableNetwork<N, E> r1) {
        return (ImmutableNetwork) Preconditions.checkNotNull(r1);
    }

    public static <N, E> ImmutableNetwork<N, E> copyOf(Network<N, E> r1) {
        if ((r1 instanceof ImmutableNetwork) == false) goto L6;
        return (ImmutableNetwork) r1;
    L6:
        return new ImmutableNetwork(r1);
    }

    private static <N, E> Map<E, N> getEdgeToReferenceNode(Network<N, E> r4) {
        ImmutableMap.Builder r0 = ImmutableMap.builder();
        Iterator<E> r1 = r4.edges().iterator();
    L4:
        if (r1.hasNext() == false) goto L7;
        E r2 = r1.next();
        r0.put(r2, r4.incidentNodes(r2).nodeU());
        goto L4
    L7:
        return r0.buildOrThrow();
    }

    private static <N, E> Map<N, NetworkConnections<N, E>> getNodeConnections(Network<N, E> r4) {
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

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ Object lambda$adjacentNodeFn$2(Network r1, Object r2, Object r3) {
        return r1.incidentNodes(r3).adjacentNode(r2);
    }

    static /* synthetic */ Object lambda$sourceNodeFn$0(Network r1, Object r2) {
        return r1.incidentNodes(r2).source();
    }

    static /* synthetic */ Object lambda$targetNodeFn$1(Network r1, Object r2) {
        return r1.incidentNodes(r2).target();
    }

    private static <N, E> Function<E, N> sourceNodeFn(final Network<N, E> r1) {
        return new ImmutableNetwork$$ExternalSyntheticLambda0(r1);
    }

    private static <N, E> Function<E, N> targetNodeFn(final Network<N, E> r1) {
        return new ImmutableNetwork$$ExternalSyntheticLambda2(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set adjacentNodes(Object r1) {
        return super.adjacentNodes(r1);
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ boolean allowsParallelEdges() {
        return super.allowsParallelEdges();
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ boolean allowsSelfLoops() {
        return super.allowsSelfLoops();
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Graph asGraph() {
        return asGraph();
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public ImmutableGraph<N> asGraph() {
        return new ImmutableGraph(super.asGraph());
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ ElementOrder edgeOrder() {
        return super.edgeOrder();
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set edges() {
        return super.edges();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set edgesConnecting(Object r1, Object r2) {
        return super.edgesConnecting(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set inEdges(Object r1) {
        return super.inEdges(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set incidentEdges(Object r1) {
        return super.incidentEdges(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ EndpointPair incidentNodes(Object r1) {
        return super.incidentNodes(r1);
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ boolean isDirected() {
        return super.isDirected();
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ ElementOrder nodeOrder() {
        return super.nodeOrder();
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set nodes() {
        return super.nodes();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set outEdges(Object r1) {
        return super.outEdges(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.PredecessorsFunction
    public /* bridge */ /* synthetic */ Set predecessors(Object r1) {
        return super.predecessors(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.SuccessorsFunction
    public /* bridge */ /* synthetic */ Set successors(Object r1) {
        return super.successors(r1);
    }
}
