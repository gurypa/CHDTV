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
final class DirectedMultiNetworkConnections<N, E> extends AbstractDirectedNetworkConnections<N, E> {

    @CheckForNull
    @LazyInit
    private transient Reference<Multiset<N>> predecessorsReference;

    @CheckForNull
    @LazyInit
    private transient Reference<Multiset<N>> successorsReference;

    private DirectedMultiNetworkConnections(Map<E, N> r1, Map<E, N> r2, int r3) {
        super(r1, r2, r3);
    }

    static /* synthetic */ Multiset access$000(DirectedMultiNetworkConnections r1) {
        return r1.successorsMultiset();
    }

    @CheckForNull
    private static <T> T getReference(@CheckForNull Reference<T> r1) {
        if (r1 != null) goto L5;
        return null;
    L5:
        return r1.get();
    }

    /* JADX INFO: renamed from: of */
    static <N, E> DirectedMultiNetworkConnections<N, E> m330of() {
        return new DirectedMultiNetworkConnections(new HashMap(2, 1.0f), new HashMap(2, 1.0f), 0);
    }

    static <N, E> DirectedMultiNetworkConnections<N, E> ofImmutable(Map<E, N> r3, Map<E, N> r4, int r5) {
        return new DirectedMultiNetworkConnections(ImmutableMap.copyOf(r3), ImmutableMap.copyOf(r4), r5);
    }

    private Multiset<N> predecessorsMultiset() {
        Multiset<N> r0 = (Multiset) getReference(this.predecessorsReference);
        if (r0 != null) goto L6;
        HashMultiset r1 = HashMultiset.create(this.inEdgeMap.values());
        this.predecessorsReference = new SoftReference(r1);
        return r1;
    L6:
        return r0;
    }

    private Multiset<N> successorsMultiset() {
        Multiset<N> r0 = (Multiset) getReference(this.successorsReference);
        if (r0 != null) goto L6;
        HashMultiset r1 = HashMultiset.create(this.outEdgeMap.values());
        this.successorsReference = new SoftReference(r1);
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.graph.AbstractDirectedNetworkConnections, com.google.common.graph.NetworkConnections
    public void addInEdge(E r3, N r4, boolean r5) {
        super.addInEdge(r3, r4, r5);
        Multiset r0 = (Multiset) getReference(this.predecessorsReference);
        if (r0 == null) goto L6;
        Preconditions.checkState(r0.add(r4));
        return;
    }

    @Override // com.google.common.graph.AbstractDirectedNetworkConnections, com.google.common.graph.NetworkConnections
    public void addOutEdge(E r3, N r4) {
        super.addOutEdge(r3, r4);
        Multiset r0 = (Multiset) getReference(this.successorsReference);
        if (r0 == null) goto L6;
        Preconditions.checkState(r0.add(r4));
        return;
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<E> edgesConnecting(final N r3) {
        final Map<E, N> r1 = this.outEdgeMap;
        return new C08011(this, r1, r3, r3);
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<N> predecessors() {
        return Collections.unmodifiableSet(predecessorsMultiset().elementSet());
    }

    @Override // com.google.common.graph.AbstractDirectedNetworkConnections, com.google.common.graph.NetworkConnections
    public N removeInEdge(E r4, boolean r5) {
        N r0 = (N) super.removeInEdge(r4, r5);
        Multiset r1 = (Multiset) getReference(this.predecessorsReference);
        if (r1 == null) goto L5;
        Preconditions.checkState(r1.remove(r0));
    L5:
        return r0;
    }

    @Override // com.google.common.graph.AbstractDirectedNetworkConnections, com.google.common.graph.NetworkConnections
    public N removeOutEdge(E r4) {
        N r0 = (N) super.removeOutEdge(r4);
        Multiset r1 = (Multiset) getReference(this.successorsReference);
        if (r1 == null) goto L5;
        Preconditions.checkState(r1.remove(r0));
    L5:
        return r0;
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<N> successors() {
        return Collections.unmodifiableSet(successorsMultiset().elementSet());
    }
}
