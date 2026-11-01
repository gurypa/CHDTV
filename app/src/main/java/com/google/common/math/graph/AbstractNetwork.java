package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.math.IntMath;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class AbstractNetwork<N, E> implements Network<N, E> {

    /* JADX INFO: renamed from: com.google.common.graph.AbstractNetwork$1 */
    class C07931 extends AbstractGraph<N> {
        final /* synthetic */ AbstractNetwork this$0;

        /* JADX INFO: renamed from: com.google.common.graph.AbstractNetwork$1$1, reason: invalid class name */
        class AnonymousClass1 extends AbstractSet<EndpointPair<N>> {
            final /* synthetic */ C07931 this$1;

            AnonymousClass1(C07931 r1) {
                this.this$1 = r1;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@CheckForNull Object r5) {
                if ((r5 instanceof EndpointPair) == true) goto L5;
                return false;
            L5:
                EndpointPair<?> r0 = (EndpointPair) r5;
                if (this.this$1.isOrderingCompatible(r0) == true) goto L8;
                return false;
            L8:
                if (this.this$1.nodes().contains(r0.nodeU()) == true) goto L10;
                return false;
            L10:
                if (this.this$1.successors(r0.nodeU()).contains(r0.nodeV()) == false) goto L16;
                return true;
            L16:
                return false;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<EndpointPair<N>> iterator() {
                return Iterators.transform(this.this$1.this$0.edges().iterator(), new AbstractNetwork$1$1$$ExternalSyntheticLambda0(this));
            }

            /* JADX INFO: renamed from: lambda$iterator$0$com-google-common-graph-AbstractNetwork$1$1, reason: not valid java name */
            /* synthetic */ EndpointPair m505lambda$iterator$0$comgooglecommongraphAbstractNetwork$1$1(Object r2) {
                return this.this$1.this$0.incidentNodes(r2);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return this.this$1.this$0.edges().size();
            }
        }

        C07931(AbstractNetwork r1) {
            this.this$0 = r1;
        }

        @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
        public Set<N> adjacentNodes(N r2) {
            return this.this$0.adjacentNodes(r2);
        }

        @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
        public boolean allowsSelfLoops() {
            return this.this$0.allowsSelfLoops();
        }

        @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public Set<EndpointPair<N>> edges() {
            if (this.this$0.allowsParallelEdges() == false) goto L7;
            return super.edges();
        L7:
            return new AnonymousClass1(this);
        }

        @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public ElementOrder<N> incidentEdgeOrder() {
            return ElementOrder.unordered();
        }

        @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
        public boolean isDirected() {
            return this.this$0.isDirected();
        }

        @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
        public ElementOrder<N> nodeOrder() {
            return this.this$0.nodeOrder();
        }

        @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
        public Set<N> nodes() {
            return this.this$0.nodes();
        }

        @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
        public /* bridge */ /* synthetic */ Iterable predecessors(Object r1) {
            return predecessors(r1);
        }

        @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
        public Set<N> predecessors(N r2) {
            return this.this$0.predecessors(r2);
        }

        @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
        public /* bridge */ /* synthetic */ Iterable successors(Object r1) {
            return successors(r1);
        }

        @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
        public Set<N> successors(N r2) {
            return this.this$0.successors(r2);
        }
    }

    public AbstractNetwork() {
    }

    private Predicate<E> connectedPredicate(final N r2, final N r3) {
        return new C07942(this, r2, r3);
    }

    private static <N, E> Map<E, EndpointPair<N>> edgeIncidentNodesMap(final Network<N, E> r2) {
        Set<E> r0 = r2.edges();
        Objects.requireNonNull(r2);
        return Maps.asMap(r0, new AbstractNetwork$$ExternalSyntheticLambda6(r2));
    }

    static /* synthetic */ String lambda$edgeInvalidatableSet$1(Object r2) {
        return String.format("Edge %s that was used to generate this set is no longer in the graph.", new Object[]{r2});
    }

    static /* synthetic */ String lambda$nodeInvalidatableSet$3(Object r2) {
        return String.format("Node %s that was used to generate this set is no longer in the graph.", new Object[]{r2});
    }

    static /* synthetic */ String lambda$nodePairInvalidatableSet$5(Object r2, Object r3) {
        return String.format("Node %s or node %s that were used to generate this set are no longer in the graph.", new Object[]{r2, r3});
    }

    @Override // com.google.common.graph.Network
    public Set<E> adjacentEdges(E r4) {
        EndpointPair<N> r0 = incidentNodes(r4);
        return (Set<E>) edgeInvalidatableSet(Sets.difference(Sets.union(incidentEdges(r0.nodeU()), incidentEdges(r0.nodeV())), ImmutableSet.m260of(r4)), r4);
    }

    @Override // com.google.common.graph.Network
    public Graph<N> asGraph() {
        return new C07931(this);
    }

    @Override // com.google.common.graph.Network
    public int degree(N r3) {
        if (isDirected() == false) goto L7;
        return IntMath.saturatedAdd(inEdges(r3).size(), outEdges(r3).size());
    L7:
        return IntMath.saturatedAdd(incidentEdges(r3).size(), edgesConnecting(r3, r3).size());
    }

    @Override // com.google.common.graph.Network
    @CheckForNull
    public E edgeConnectingOrNull(EndpointPair<N> r3) {
        validateEndpoints(r3);
        return edgeConnectingOrNull(r3.nodeU(), r3.nodeV());
    }

    @Override // com.google.common.graph.Network
    @CheckForNull
    public E edgeConnectingOrNull(N r5, N r6) {
        Set<E> r0 = edgesConnecting(r5, r6);
        switch(r0.size()) {
            case 0: goto L8;
            case 1: goto L7;
            default: goto L5;
        };
    L8:
        return null;
    L5:
        throw new IllegalArgumentException(String.format("Cannot call edgeConnecting() when parallel edges exist between %s and %s. Consider calling edgesConnecting() instead.", new Object[]{r5, r6}));
    L7:
        return r0.iterator().next();
    }

    protected final <T> Set<T> edgeInvalidatableSet(Set<T> r3, final E r4) {
        return InvalidatableSet.m336of(r3, new AbstractNetwork$$ExternalSyntheticLambda4(this, r4), new AbstractNetwork$$ExternalSyntheticLambda5(r4));
    }

    @Override // com.google.common.graph.Network
    public Set<E> edgesConnecting(EndpointPair<N> r3) {
        validateEndpoints(r3);
        return edgesConnecting(r3.nodeU(), r3.nodeV());
    }

    @Override // com.google.common.graph.Network
    public Set<E> edgesConnecting(N r5, N r6) {
        Set<E> r0 = outEdges(r5);
        Set<E> r1 = inEdges(r6);
        if (r0.size() > r1.size()) goto L5;
        Set r2 = Collections.unmodifiableSet(Sets.filter(r0, connectedPredicate(r5, r6)));
    L7:
        return (Set<E>) nodePairInvalidatableSet(r2, r5, r6);
    L5:
        r2 = Collections.unmodifiableSet(Sets.filter(r1, connectedPredicate(r6, r5)));
        goto L7
    }

    @Override // com.google.common.graph.Network
    public final boolean equals(@CheckForNull Object r6) {
        if (r6 != this) goto L6;
        return true;
    L6:
        if ((r6 instanceof Network) == true) goto L8;
        return false;
    L8:
        Network r1 = (Network) r6;
        if (isDirected() != r1.isDirected()) goto L16;
        if (nodes().equals(r1.nodes()) == false) goto L16;
        if (edgeIncidentNodesMap(this).equals(edgeIncidentNodesMap(r1)) == false) goto L16;
        return true;
    L16:
        return false;
    }

    @Override // com.google.common.graph.Network
    public boolean hasEdgeConnecting(EndpointPair<N> r3) {
        Preconditions.checkNotNull(r3);
        if (isOrderingCompatible(r3) == true) goto L7;
        return false;
    L7:
        return hasEdgeConnecting(r3.nodeU(), r3.nodeV());
    }

    @Override // com.google.common.graph.Network
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

    @Override // com.google.common.graph.Network
    public final int hashCode() {
        return edgeIncidentNodesMap(this).hashCode();
    }

    @Override // com.google.common.graph.Network
    public int inDegree(N r2) {
        if (isDirected() == false) goto L6;
        return inEdges(r2).size();
    L6:
        return degree(r2);
    }

    protected final boolean isOrderingCompatible(EndpointPair<?> r3) {
        if (r3.isOrdered() != isDirected()) goto L5;
        return true;
    L5:
        return false;
    }

    /* JADX INFO: renamed from: lambda$edgeInvalidatableSet$0$com-google-common-graph-AbstractNetwork */
    /* synthetic */ Boolean m326xa0c212f3(Object r2) {
        return Boolean.valueOf(edges().contains(r2));
    }

    /* JADX INFO: renamed from: lambda$nodeInvalidatableSet$2$com-google-common-graph-AbstractNetwork */
    /* synthetic */ Boolean m327xe136d8fa(Object r2) {
        return Boolean.valueOf(nodes().contains(r2));
    }

    /* JADX INFO: renamed from: lambda$nodePairInvalidatableSet$4$com-google-common-graph-AbstractNetwork */
    /* synthetic */ Boolean m328xa62961f6(Object r2, Object r3) {
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
        return InvalidatableSet.m336of(r3, new AbstractNetwork$$ExternalSyntheticLambda0(this, r4), new AbstractNetwork$$ExternalSyntheticLambda1(r4));
    }

    protected final <T> Set<T> nodePairInvalidatableSet(Set<T> r3, final N r4, final N r5) {
        return InvalidatableSet.m336of(r3, new AbstractNetwork$$ExternalSyntheticLambda2(this, r4, r5), new AbstractNetwork$$ExternalSyntheticLambda3(r4, r5));
    }

    @Override // com.google.common.graph.Network
    public int outDegree(N r2) {
        if (isDirected() == false) goto L6;
        return outEdges(r2).size();
    L6:
        return degree(r2);
    }

    @Override // com.google.common.graph.PredecessorsFunction
    public /* bridge */ /* synthetic */ Iterable predecessors(Object r1) {
        return Network.CC.$default$predecessors(this, r1);
    }

    @Override // com.google.common.graph.SuccessorsFunction
    public /* bridge */ /* synthetic */ Iterable successors(Object r1) {
        return Network.CC.$default$successors(this, r1);
    }

    public String toString() {
        return "isDirected: " + isDirected() + ", allowsParallelEdges: " + allowsParallelEdges() + ", allowsSelfLoops: " + allowsSelfLoops() + ", nodes: " + nodes() + ", edges: " + edgeIncidentNodesMap(this);
    }

    protected final void validateEndpoints(EndpointPair<?> r3) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkArgument(isOrderingCompatible(r3), "Mismatch: endpoints' ordering is not compatible with directionality of the graph");
    }
}
