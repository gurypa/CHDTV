package com.google.common.graph;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class DirectedNetworkConnections<N, E> extends AbstractDirectedNetworkConnections<N, E> {
    DirectedNetworkConnections(Map<E, N> r1, Map<E, N> r2, int r3) {
        super(r1, r2, r3);
    }

    /* JADX INFO: renamed from: of */
    static <N, E> DirectedNetworkConnections<N, E> m331of() {
        return new DirectedNetworkConnections(HashBiMap.create(2), HashBiMap.create(2), 0);
    }

    static <N, E> DirectedNetworkConnections<N, E> ofImmutable(Map<E, N> r3, Map<E, N> r4, int r5) {
        return new DirectedNetworkConnections(ImmutableBiMap.copyOf(r3), ImmutableBiMap.copyOf(r4), r5);
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<E> edgesConnecting(N r3) {
        return new EdgesConnecting(((BiMap) this.outEdgeMap).inverse(), r3);
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<N> predecessors() {
        return Collections.unmodifiableSet(((BiMap) this.inEdgeMap).values());
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<N> successors() {
        return Collections.unmodifiableSet(((BiMap) this.outEdgeMap).values());
    }
}
