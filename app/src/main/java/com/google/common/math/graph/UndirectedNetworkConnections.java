package com.google.common.graph;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class UndirectedNetworkConnections<N, E> extends AbstractUndirectedNetworkConnections<N, E> {
    UndirectedNetworkConnections(Map<E, N> r1) {
        super(r1);
    }

    /* JADX INFO: renamed from: of */
    static <N, E> UndirectedNetworkConnections<N, E> m339of() {
        return new UndirectedNetworkConnections(HashBiMap.create(2));
    }

    static <N, E> UndirectedNetworkConnections<N, E> ofImmutable(Map<E, N> r2) {
        return new UndirectedNetworkConnections(ImmutableBiMap.copyOf(r2));
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<N> adjacentNodes() {
        return Collections.unmodifiableSet(((BiMap) this.incidentEdgeMap).values());
    }

    @Override // com.google.common.graph.NetworkConnections
    public Set<E> edgesConnecting(N r3) {
        return new EdgesConnecting(((BiMap) this.incidentEdgeMap).inverse(), r3);
    }
}
