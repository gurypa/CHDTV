package com.google.common.graph;

import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class GraphsBridgeMethods {
    GraphsBridgeMethods() {
    }

    public static <N> Set<N> reachableNodes(Graph<N> r1, N r2) {
        return Graphs.reachableNodes(r1, r2);
    }

    public static <N> Graph<N> transitiveClosure(Graph<N> r1) {
        return Graphs.transitiveClosure(r1);
    }
}
