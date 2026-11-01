package com.google.common.graph;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class ValueGraphBuilder<N, V> extends AbstractGraphBuilder<N> {
    private ValueGraphBuilder(boolean r1) {
        super(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <N1 extends N, V1 extends V> ValueGraphBuilder<N1, V1> cast() {
        return this;
    }

    public static ValueGraphBuilder<Object, Object> directed() {
        return new ValueGraphBuilder(true);
    }

    public static <N, V> ValueGraphBuilder<N, V> from(ValueGraph<N, V> r2) {
        return new ValueGraphBuilder(r2.isDirected()).allowsSelfLoops(r2.allowsSelfLoops()).nodeOrder(r2.nodeOrder()).incidentEdgeOrder(r2.incidentEdgeOrder());
    }

    public static ValueGraphBuilder<Object, Object> undirected() {
        return new ValueGraphBuilder(false);
    }

    public ValueGraphBuilder<N, V> allowsSelfLoops(boolean r1) {
        this.allowsSelfLoops = r1;
        return this;
    }

    public <N1 extends N, V1 extends V> MutableValueGraph<N1, V1> build() {
        return new StandardMutableValueGraph(this);
    }

    ValueGraphBuilder<N, V> copy() {
        ValueGraphBuilder<N, V> r0 = new ValueGraphBuilder(this.directed);
        r0.allowsSelfLoops = this.allowsSelfLoops;
        r0.nodeOrder = this.nodeOrder;
        r0.expectedNodeCount = this.expectedNodeCount;
        r0.incidentEdgeOrder = this.incidentEdgeOrder;
        return r0;
    }

    public ValueGraphBuilder<N, V> expectedNodeCount(int r2) {
        this.expectedNodeCount = Optional.m185of(Integer.valueOf(Graphs.checkNonNegative(r2)));
        return this;
    }

    public <N1 extends N, V1 extends V> ImmutableValueGraph.Builder<N1, V1> immutable() {
        return new ImmutableValueGraph.Builder(cast());
    }

    public <N1 extends N> ValueGraphBuilder<N1, V> incidentEdgeOrder(ElementOrder<N1> r3) {
        if (r3.type() != ElementOrder.Type.UNORDERED) goto L5;
    L8:
        boolean r0 = true;
    L9:
        Preconditions.checkArgument(r0, "The given elementOrder (%s) is unsupported. incidentEdgeOrder() only supports ElementOrder.unordered() and ElementOrder.stable().", r3);
        ValueGraphBuilder<N1, V> r1 = (ValueGraphBuilder<N1, V>) cast();
        r1.incidentEdgeOrder = (ElementOrder) Preconditions.checkNotNull(r3);
        return r1;
    L5:
        if (r3.type() == ElementOrder.Type.STABLE) goto L8;
        r0 = false;
        goto L9
    }

    public <N1 extends N> ValueGraphBuilder<N1, V> nodeOrder(ElementOrder<N1> r3) {
        ValueGraphBuilder<N1, V> r0 = (ValueGraphBuilder<N1, V>) cast();
        r0.nodeOrder = (ElementOrder) Preconditions.checkNotNull(r3);
        return r0;
    }
}
