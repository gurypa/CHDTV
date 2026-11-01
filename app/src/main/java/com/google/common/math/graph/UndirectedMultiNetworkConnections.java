package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multiset;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class UndirectedMultiNetworkConnections<N, E> extends AbstractUndirectedNetworkConnections<N, E> {

    @CheckForNull
    @LazyInit
    private transient Reference<Multiset<N>> adjacentNodesReference;

    private UndirectedMultiNetworkConnections(Map<E, N> r1) {
        super(r1);
    }

    static /* synthetic */ Multiset access$000(UndirectedMultiNetworkConnections r1) {
        return r1.adjacentNodesMultiset();
    }

    private Multiset<N> adjacentNodesMultiset() {
        Multiset<N> r0 = (Multiset) getReference(this.adjacentNodesReference);
        if (r0 != null) goto L6;
        HashMultiset r1 = HashMultiset.create(this.incidentEdgeMap.values());
        this.adjacentNodesReference = new SoftReference(r1);
        return r1;
    L6:
        return r0;
    }

    @CheckForNull
    private static <T> T getReference(@CheckForNull Reference<T> r1) {
        if (r1 != null) goto L5;
        return null;
    L5:
        return r1.get();
    }

    /* JADX INFO: renamed from: of */
    static <N, E> UndirectedMultiNetworkConnections<N, E> m338of() {
        return new UndirectedMultiNetworkConnections(new HashMap(2, 1.0f));
    }

    static <N, E> UndirectedMultiNetworkConnections<N, E> ofImmutable(Map<E, N> r2) {
        return new UndirectedMultiNetworkConnections(ImmutableMap.copyOf(r2));
    }

    @Override // com.google.common.graph.AbstractUndirectedNetworkConnections, com.google.common.graph.NetworkConnections
    public void addInEdge(E r1, N r2, boolean r3) {
        if (r3 == true) goto L5;
        addOutEdge(r1, r2);
        return;
    }

    @Override // com.google.common.graph.AbstractUndirectedNetworkConnections, com.google.common.graph.NetworkConnections
    public void addOutEdge(E r3, N r4) {
        super.addOutEdge(r3, r4);
        Multiset r0 = (Multiset) getReference(this.adjacentNodesReference);
        if (r0 == null) goto L6;
        Preconditions.checkState(r0.add(r4));
        return;
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<N> adjacentNodes() {
        return Collections.unmodifiableSet(adjacentNodesMultiset().elementSet());
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<E> edgesConnecting(final N r3) {
        final Map<E, N> r1 = this.incidentEdgeMap;
        return new C08201(this, r1, r3, r3);
    }

    @Override // com.google.common.graph.AbstractUndirectedNetworkConnections, com.google.common.graph.NetworkConnections
    @CheckForNull
    public N removeInEdge(E r2, boolean r3) {
        if (r3 == false) goto L4;
        return null;
    L4:
        return removeOutEdge(r2);
    }

    @Override // com.google.common.graph.AbstractUndirectedNetworkConnections, com.google.common.graph.NetworkConnections
    public N removeOutEdge(E r4) {
        N r0 = (N) super.removeOutEdge(r4);
        Multiset r1 = (Multiset) getReference(this.adjacentNodesReference);
        if (r1 == null) goto L5;
        Preconditions.checkState(r1.remove(r0));
    L5:
        return r0;
    }
}
