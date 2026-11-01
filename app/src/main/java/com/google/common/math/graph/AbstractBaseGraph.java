package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.math.IntMath;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractBaseGraph<N> implements BaseGraph<N> {

    /* JADX INFO: renamed from: com.google.common.graph.AbstractBaseGraph$2 */
    class C07912 extends IncidentEdgeSet<N> {
        final /* synthetic */ AbstractBaseGraph this$0;

        C07912(AbstractBaseGraph r1, BaseGraph r2, Object r3) {
            this.this$0 = r1;
            super(r2, r3);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public UnmodifiableIterator<EndpointPair<N>> iterator() {
            if (this.graph.isDirected() == false) goto L7;
            return Iterators.unmodifiableIterator(Iterators.concat(Iterators.transform(this.graph.predecessors(this.node).iterator(), new AbstractBaseGraph$2$$ExternalSyntheticLambda0(this)), Iterators.transform(Sets.difference(this.graph.successors(this.node), ImmutableSet.m260of(this.node)).iterator(), new AbstractBaseGraph$2$$ExternalSyntheticLambda1(this))));
        L7:
            return Iterators.unmodifiableIterator(Iterators.transform(this.graph.adjacentNodes(this.node).iterator(), new AbstractBaseGraph$2$$ExternalSyntheticLambda2(this)));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return iterator();
        }

        /* JADX INFO: renamed from: lambda$iterator$0$com-google-common-graph-AbstractBaseGraph$2, reason: not valid java name */
        /* synthetic */ EndpointPair m502lambda$iterator$0$comgooglecommongraphAbstractBaseGraph$2(Object r2) {
            return EndpointPair.ordered(r2, this.node);
        }

        /* JADX INFO: renamed from: lambda$iterator$1$com-google-common-graph-AbstractBaseGraph$2, reason: not valid java name */
        /* synthetic */ EndpointPair m503lambda$iterator$1$comgooglecommongraphAbstractBaseGraph$2(Object r2) {
            return EndpointPair.ordered(this.node, r2);
        }

        /* JADX INFO: renamed from: lambda$iterator$2$com-google-common-graph-AbstractBaseGraph$2, reason: not valid java name */
        /* synthetic */ EndpointPair m504lambda$iterator$2$comgooglecommongraphAbstractBaseGraph$2(Object r2) {
            return EndpointPair.unordered(this.node, r2);
        }
    }

    AbstractBaseGraph() {
    }

    static /* synthetic */ String lambda$nodeInvalidatableSet$1(Object r2) {
        return String.format("Node %s that was used to generate this set is no longer in the graph.", new Object[]{r2});
    }

    static /* synthetic */ String lambda$nodePairInvalidatableSet$3(Object r2, Object r3) {
        return String.format("Node %s or node %s that were used to generate this set are no longer in the graph.", new Object[]{r2, r3});
    }

    @Override // com.google.common.graph.BaseGraph
    public int degree(N r4) {
        if (isDirected() == true) goto L5;
        Set<N> r0 = adjacentNodes(r4);
        if (allowsSelfLoops() == true) goto L9;
    L11:
        int r1 = 0;
    L13:
        return IntMath.saturatedAdd(r0.size(), r1);
    L9:
        if (r0.contains(r4) == false) goto L11;
        r1 = 1;
        goto L13
    L5:
        return IntMath.saturatedAdd(predecessors(r4).size(), successors(r4).size());
    }

    protected long edgeCount() {
        long r0 = 0;
        Iterator<N> r2 = nodes().iterator();
    L4:
        if (r2.hasNext() == false) goto L7;
        r0 = r0 + ((long) degree(r2.next()));
        goto L4
    L7:
        if ((1 & r0) != 0) goto L9;
        boolean r3 = true;
    L10:
        Preconditions.checkState(r3);
        return r0 >>> 1;
    L9:
        r3 = false;
        goto L10
    }

    @Override // com.google.common.graph.BaseGraph
    public Set<EndpointPair<N>> edges() {
        return new C07901(this);
    }

    @Override // com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(EndpointPair<N> r5) {
        Preconditions.checkNotNull(r5);
        if (isOrderingCompatible(r5) == true) goto L5;
        return false;
    L5:
        N r0 = r5.nodeU();
        N r2 = r5.nodeV();
        if (nodes().contains(r0) == true) goto L8;
        return false;
    L8:
        if (successors(r0).contains(r2) == false) goto L12;
        return true;
    L12:
        return false;
    }

    @Override // com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(N r2, N r3) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        if (nodes().contains(r2) == true) goto L5;
    L7:
        return false;
    L5:
        if (successors(r2).contains(r3) == false) goto L7;
        return true;
    }

    @Override // com.google.common.graph.BaseGraph
    public int inDegree(N r2) {
        if (isDirected() == false) goto L6;
        return predecessors(r2).size();
    L6:
        return degree(r2);
    }

    @Override // com.google.common.graph.BaseGraph
    public ElementOrder<N> incidentEdgeOrder() {
        return ElementOrder.unordered();
    }

    @Override // com.google.common.graph.BaseGraph
    public Set<EndpointPair<N>> incidentEdges(N r3) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkArgument(nodes().contains(r3), "Node %s is not an element of this graph.", r3);
        return (Set<EndpointPair<N>>) nodeInvalidatableSet(new C07912(this, this, r3), r3);
    }

    protected final boolean isOrderingCompatible(EndpointPair<?> r3) {
        if (r3.isOrdered() != isDirected()) goto L5;
        return true;
    L5:
        return false;
    }

    /* JADX INFO: renamed from: lambda$nodeInvalidatableSet$0$com-google-common-graph-AbstractBaseGraph */
    /* synthetic */ Boolean m324xbbd68347(Object r2) {
        return Boolean.valueOf(nodes().contains(r2));
    }

    /* JADX INFO: renamed from: lambda$nodePairInvalidatableSet$2$com-google-common-graph-AbstractBaseGraph */
    /* synthetic */ Boolean m325xe4abd43(Object r2, Object r3) {
        if (nodes().contains(r2) == true) goto L5;
    L7:
        boolean r0 = false;
    L9:
        return Boolean.valueOf(r0);
    L5:
        if (nodes().contains(r3) == false) goto L7;
        r0 = true;
        goto L9
    }

    protected final <T> Set<T> nodeInvalidatableSet(Set<T> r3, final N r4) {
        return InvalidatableSet.m336of(r3, new AbstractBaseGraph$$ExternalSyntheticLambda2(this, r4), new AbstractBaseGraph$$ExternalSyntheticLambda3(r4));
    }

    protected final <T> Set<T> nodePairInvalidatableSet(Set<T> r3, final N r4, final N r5) {
        return InvalidatableSet.m336of(r3, new AbstractBaseGraph$$ExternalSyntheticLambda0(this, r4, r5), new AbstractBaseGraph$$ExternalSyntheticLambda1(r4, r5));
    }

    @Override // com.google.common.graph.BaseGraph
    public int outDegree(N r2) {
        if (isDirected() == false) goto L6;
        return successors(r2).size();
    L6:
        return degree(r2);
    }

    @Override // com.google.common.graph.PredecessorsFunction
    public /* bridge */ /* synthetic */ Iterable predecessors(Object r1) {
        return BaseGraph.CC.$default$predecessors(this, r1);
    }

    @Override // com.google.common.graph.SuccessorsFunction
    public /* bridge */ /* synthetic */ Iterable successors(Object r1) {
        return BaseGraph.CC.$default$successors(this, r1);
    }

    protected final void validateEndpoints(EndpointPair<?> r3) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkArgument(isOrderingCompatible(r3), "Mismatch: endpoints' ordering is not compatible with directionality of the graph");
    }
}
