package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class UndirectedGraphConnections<N, V> implements GraphConnections<N, V> {
    private final Map<N, V> adjacentNodeValues;

    /* JADX INFO: renamed from: com.google.common.graph.UndirectedGraphConnections$1 */
    static /* synthetic */ class C08191 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$graph$ElementOrder$Type = null;

        static {
            $SwitchMap$com$google$common$graph$ElementOrder$Type = new int[ElementOrder.Type.values().length];
            $SwitchMap$com$google$common$graph$ElementOrder$Type[ElementOrder.Type.UNORDERED.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L12:
            $SwitchMap$com$google$common$graph$ElementOrder$Type[ElementOrder.Type.STABLE.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
            return;
        }
    }

    private UndirectedGraphConnections(Map<N, V> r2) {
        this.adjacentNodeValues = (Map) Preconditions.checkNotNull(r2);
    }

    static /* synthetic */ EndpointPair lambda$incidentEdgeIterator$0(Object r1, Object r2) {
        return EndpointPair.unordered(r1, r2);
    }

    /* JADX INFO: renamed from: of */
    static <N, V> UndirectedGraphConnections<N, V> m337of(ElementOrder<N> r4) {
        switch(C08191.$SwitchMap$com$google$common$graph$ElementOrder$Type[r4.type().ordinal()]) {
            case 1: goto L9;
            case 2: goto L7;
            default: goto L5;
        };
    L5:
        throw new AssertionError(r4.type());
    L7:
        return new UndirectedGraphConnections(new LinkedHashMap(2, 1.0f));
    L9:
        return new UndirectedGraphConnections(new HashMap(2, 1.0f));
    }

    static <N, V> UndirectedGraphConnections<N, V> ofImmutable(Map<N, V> r2) {
        return new UndirectedGraphConnections(ImmutableMap.copyOf(r2));
    }

    @Override // com.google.common.graph.GraphConnections
    public void addPredecessor(N r2, V r3) {
        addSuccessor(r2, r3);
    }

    @Override // com.google.common.graph.GraphConnections
    @CheckForNull
    public V addSuccessor(N r2, V r3) {
        return this.adjacentNodeValues.put(r2, r3);
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> adjacentNodes() {
        return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
    }

    @Override // com.google.common.graph.GraphConnections
    public Iterator<EndpointPair<N>> incidentEdgeIterator(final N r3) {
        return Iterators.transform(this.adjacentNodeValues.keySet().iterator(), new UndirectedGraphConnections$$ExternalSyntheticLambda0(r3));
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> predecessors() {
        return adjacentNodes();
    }

    @Override // com.google.common.graph.GraphConnections
    public void removePredecessor(N r2) {
        removeSuccessor(r2);
    }

    @Override // com.google.common.graph.GraphConnections
    @CheckForNull
    public V removeSuccessor(N r2) {
        return this.adjacentNodeValues.remove(r2);
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> successors() {
        return adjacentNodes();
    }

    @Override // com.google.common.graph.GraphConnections
    @CheckForNull
    public V value(N r2) {
        return this.adjacentNodeValues.get(r2);
    }
}
