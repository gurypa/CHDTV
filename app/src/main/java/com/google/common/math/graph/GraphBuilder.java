package com.google.common.graph;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.DoNotMock;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock
@ElementTypesAreNonnullByDefault
public final class GraphBuilder<N> extends AbstractGraphBuilder<N> {
    private GraphBuilder(boolean r1) {
        super(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <N1 extends N> GraphBuilder<N1> cast() {
        return this;
    }

    public static GraphBuilder<Object> directed() {
        return new GraphBuilder(true);
    }

    public static <N> GraphBuilder<N> from(Graph<N> r2) {
        return new GraphBuilder(r2.isDirected()).allowsSelfLoops(r2.allowsSelfLoops()).nodeOrder(r2.nodeOrder()).incidentEdgeOrder(r2.incidentEdgeOrder());
    }

    public static GraphBuilder<Object> undirected() {
        return new GraphBuilder(false);
    }

    public GraphBuilder<N> allowsSelfLoops(boolean r1) {
        this.allowsSelfLoops = r1;
        return this;
    }

    public <N1 extends N> MutableGraph<N1> build() {
        return new StandardMutableGraph(this);
    }

    GraphBuilder<N> copy() {
        GraphBuilder<N> r0 = new GraphBuilder(this.directed);
        r0.allowsSelfLoops = this.allowsSelfLoops;
        r0.nodeOrder = this.nodeOrder;
        r0.expectedNodeCount = this.expectedNodeCount;
        r0.incidentEdgeOrder = this.incidentEdgeOrder;
        return r0;
    }

    public GraphBuilder<N> expectedNodeCount(int r2) {
        this.expectedNodeCount = Optional.m185of(Integer.valueOf(Graphs.checkNonNegative(r2)));
        return this;
    }

    public <N1 extends N> ImmutableGraph.Builder<N1> immutable() {
        return new ImmutableGraph.Builder(cast());
    }

    public <N1 extends N> GraphBuilder<N1> incidentEdgeOrder(ElementOrder<N1> r3) {
        if (r3.type() != ElementOrder.Type.UNORDERED) goto L5;
    L8:
        boolean r0 = true;
    L9:
        Preconditions.checkArgument(r0, "The given elementOrder (%s) is unsupported. incidentEdgeOrder() only supports ElementOrder.unordered() and ElementOrder.stable().", r3);
        GraphBuilder<N1> r1 = cast();
        r1.incidentEdgeOrder = (ElementOrder) Preconditions.checkNotNull(r3);
        return r1;
    L5:
        if (r3.type() == ElementOrder.Type.STABLE) goto L8;
        r0 = false;
        goto L9
    }

    public <N1 extends N> GraphBuilder<N1> nodeOrder(ElementOrder<N1> r3) {
        GraphBuilder<N1> r0 = cast();
        r0.nodeOrder = (ElementOrder) Preconditions.checkNotNull(r3);
        return r0;
    }
}
