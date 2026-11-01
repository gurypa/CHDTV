package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class StandardNetwork<N, E> extends AbstractNetwork<N, E> {
    private final boolean allowsParallelEdges;
    private final boolean allowsSelfLoops;
    private final ElementOrder<E> edgeOrder;
    final MapIteratorCache<E, N> edgeToReferenceNode;
    private final boolean isDirected;
    final MapIteratorCache<N, NetworkConnections<N, E>> nodeConnections;
    private final ElementOrder<N> nodeOrder;

    StandardNetwork(NetworkBuilder<? super N, ? super E> r5) {
        this(r5, r5.nodeOrder.createMap(r5.expectedNodeCount.mo179or(10).intValue()), r5.edgeOrder.createMap(r5.expectedEdgeCount.mo179or(20).intValue()));
    }

    StandardNetwork(NetworkBuilder<? super N, ? super E> r2, Map<N, NetworkConnections<N, E>> r3, Map<E, N> r4) {
        this.isDirected = r2.directed;
        this.allowsParallelEdges = r2.allowsParallelEdges;
        this.allowsSelfLoops = r2.allowsSelfLoops;
        this.nodeOrder = (ElementOrder<N>) r2.nodeOrder.cast();
        this.edgeOrder = (ElementOrder<E>) r2.edgeOrder.cast();
        if ((r3 instanceof TreeMap) == false) goto L5;
        MapIteratorCache<N, NetworkConnections<N, E>> r0 = new MapRetrievalCache(r3);
    L6:
        this.nodeConnections = r0;
        this.edgeToReferenceNode = new MapIteratorCache(r4);
        return;
    L5:
        r0 = new MapIteratorCache(r3);
        goto L6
    }

    @Override // com.google.common.graph.Network
    public Set<N> adjacentNodes(N r2) {
        return (Set<N>) nodeInvalidatableSet(checkedConnections(r2).adjacentNodes(), r2);
    }

    @Override // com.google.common.graph.Network
    public boolean allowsParallelEdges() {
        return this.allowsParallelEdges;
    }

    @Override // com.google.common.graph.Network
    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }

    final NetworkConnections<N, E> checkedConnections(N r5) {
        NetworkConnections<N, E> r0 = this.nodeConnections.get(r5);
        if (r0 == null) goto L5;
        return r0;
    L5:
        Preconditions.checkNotNull(r5);
        throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", new Object[]{r5}));
    }

    final N checkedReferenceNode(E r5) {
        N r0 = this.edgeToReferenceNode.get(r5);
        if (r0 == null) goto L5;
        return r0;
    L5:
        Preconditions.checkNotNull(r5);
        throw new IllegalArgumentException(String.format("Edge %s is not an element of this graph.", new Object[]{r5}));
    }

    final boolean containsEdge(E r2) {
        return this.edgeToReferenceNode.containsKey(r2);
    }

    final boolean containsNode(N r2) {
        return this.nodeConnections.containsKey(r2);
    }

    @Override // com.google.common.graph.Network
    public ElementOrder<E> edgeOrder() {
        return this.edgeOrder;
    }

    @Override // com.google.common.graph.Network
    public Set<E> edges() {
        return this.edgeToReferenceNode.unmodifiableKeySet();
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public Set<E> edgesConnecting(N r4, N r5) {
        NetworkConnections<N, E> r0 = checkedConnections(r4);
        if (this.allowsSelfLoops == true) goto L7;
        if (r4 != r5) goto L7;
        return ImmutableSet.m259of();
    L7:
        Preconditions.checkArgument(containsNode(r5), "Node %s is not an element of this graph.", r5);
        return (Set<E>) nodePairInvalidatableSet(r0.edgesConnecting(r5), r4, r5);
    }

    @Override // com.google.common.graph.Network
    public Set<E> inEdges(N r2) {
        return (Set<E>) nodeInvalidatableSet(checkedConnections(r2).inEdges(), r2);
    }

    @Override // com.google.common.graph.Network
    public Set<E> incidentEdges(N r2) {
        return (Set<E>) nodeInvalidatableSet(checkedConnections(r2).incidentEdges(), r2);
    }

    @Override // com.google.common.graph.Network
    public EndpointPair<N> incidentNodes(E r4) {
        N r0 = checkedReferenceNode(r4);
        return EndpointPair.m333of(this, r0, ((NetworkConnections) Objects.requireNonNull(this.nodeConnections.get(r0))).adjacentNode(r4));
    }

    @Override // com.google.common.graph.Network
    public boolean isDirected() {
        return this.isDirected;
    }

    @Override // com.google.common.graph.Network
    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }

    @Override // com.google.common.graph.Network
    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }

    @Override // com.google.common.graph.Network
    public Set<E> outEdges(N r2) {
        return (Set<E>) nodeInvalidatableSet(checkedConnections(r2).outEdges(), r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.PredecessorsFunction
    public /* bridge */ /* synthetic */ Iterable predecessors(Object r1) {
        return predecessors(r1);
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.PredecessorsFunction
    public Set<N> predecessors(N r2) {
        return (Set<N>) nodeInvalidatableSet(checkedConnections(r2).predecessors(), r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.SuccessorsFunction
    public /* bridge */ /* synthetic */ Iterable successors(Object r1) {
        return successors(r1);
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.SuccessorsFunction
    public Set<N> successors(N r2) {
        return (Set<N>) nodeInvalidatableSet(checkedConnections(r2).successors(), r2);
    }
}
