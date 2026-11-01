package com.google.common.graph;

import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class ForwardingNetwork<N, E> extends AbstractNetwork<N, E> {
    ForwardingNetwork() {
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public Set<E> adjacentEdges(E r2) {
        return delegate().adjacentEdges(r2);
    }

    @Override // com.google.common.graph.Network
    public Set<N> adjacentNodes(N r2) {
        return delegate().adjacentNodes(r2);
    }

    @Override // com.google.common.graph.Network
    public boolean allowsParallelEdges() {
        return delegate().allowsParallelEdges();
    }

    @Override // com.google.common.graph.Network
    public boolean allowsSelfLoops() {
        return delegate().allowsSelfLoops();
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public int degree(N r2) {
        return delegate().degree(r2);
    }

    abstract Network<N, E> delegate();

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    @CheckForNull
    public E edgeConnectingOrNull(EndpointPair<N> r2) {
        return delegate().edgeConnectingOrNull(r2);
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    @CheckForNull
    public E edgeConnectingOrNull(N r2, N r3) {
        return delegate().edgeConnectingOrNull(r2, r3);
    }

    @Override // com.google.common.graph.Network
    public ElementOrder<E> edgeOrder() {
        return delegate().edgeOrder();
    }

    @Override // com.google.common.graph.Network
    public Set<E> edges() {
        return delegate().edges();
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public Set<E> edgesConnecting(EndpointPair<N> r2) {
        return delegate().edgesConnecting(r2);
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public Set<E> edgesConnecting(N r2, N r3) {
        return delegate().edgesConnecting(r2, r3);
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public boolean hasEdgeConnecting(EndpointPair<N> r2) {
        return delegate().hasEdgeConnecting(r2);
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public boolean hasEdgeConnecting(N r2, N r3) {
        return delegate().hasEdgeConnecting(r2, r3);
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public int inDegree(N r2) {
        return delegate().inDegree(r2);
    }

    @Override // com.google.common.graph.Network
    public Set<E> inEdges(N r2) {
        return delegate().inEdges(r2);
    }

    @Override // com.google.common.graph.Network
    public Set<E> incidentEdges(N r2) {
        return delegate().incidentEdges(r2);
    }

    @Override // com.google.common.graph.Network
    public EndpointPair<N> incidentNodes(E r2) {
        return delegate().incidentNodes(r2);
    }

    @Override // com.google.common.graph.Network
    public boolean isDirected() {
        return delegate().isDirected();
    }

    @Override // com.google.common.graph.Network
    public ElementOrder<N> nodeOrder() {
        return delegate().nodeOrder();
    }

    @Override // com.google.common.graph.Network
    public Set<N> nodes() {
        return delegate().nodes();
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public int outDegree(N r2) {
        return delegate().outDegree(r2);
    }

    @Override // com.google.common.graph.Network
    public Set<E> outEdges(N r2) {
        return delegate().outEdges(r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.PredecessorsFunction
    public /* bridge */ /* synthetic */ Iterable predecessors(Object r1) {
        return predecessors(r1);
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.PredecessorsFunction
    public Set<N> predecessors(N r2) {
        return delegate().predecessors(r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.SuccessorsFunction
    public /* bridge */ /* synthetic */ Iterable successors(Object r1) {
        return successors(r1);
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.SuccessorsFunction
    public Set<N> successors(N r2) {
        return delegate().successors(r2);
    }
}
