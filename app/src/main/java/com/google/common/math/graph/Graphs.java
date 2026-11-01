package com.google.common.graph;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Graphs extends GraphsBridgeMethods {

    private static final class NodeAndRemainingSuccessors<N> {
        final N node;

        @CheckForNull
        Queue<N> remainingSuccessors;

        NodeAndRemainingSuccessors(N r1) {
            this.node = r1;
        }
    }

    private static final enum NodeVisitState extends Enum<NodeVisitState> {
        private static final /* synthetic */ NodeVisitState[] $VALUES = null;
        public static final NodeVisitState COMPLETE = null;
        public static final NodeVisitState PENDING = null;

        private static /* synthetic */ NodeVisitState[] $values() {
            return new NodeVisitState[]{PENDING, COMPLETE};
        }

        static {
            PENDING = new NodeVisitState("PENDING", 0);
            COMPLETE = new NodeVisitState("COMPLETE", 1);
            $VALUES = $values();
        }

        private NodeVisitState(String r1, int r2) {
            super(r1, r2);
        }

        public static NodeVisitState valueOf(String r1) {
            return (NodeVisitState) Enum.valueOf(NodeVisitState.class, r1);
        }

        public static NodeVisitState[] values() {
            return (NodeVisitState[]) $VALUES.clone();
        }
    }

    private static class TransposedGraph<N> extends ForwardingGraph<N> {
        private final Graph<N> graph;

        /* JADX INFO: renamed from: com.google.common.graph.Graphs$TransposedGraph$1 */
        class C08041 extends IncidentEdgeSet<N> {
            final /* synthetic */ TransposedGraph this$0;

            C08041(TransposedGraph r1, BaseGraph r2, Object r3) {
                this.this$0 = r1;
                super(r2, r3);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<EndpointPair<N>> iterator() {
                return Iterators.transform(this.this$0.delegate().incidentEdges(this.node).iterator(), new Graphs$TransposedGraph$1$$ExternalSyntheticLambda0(this));
            }

            /* JADX INFO: renamed from: lambda$iterator$0$com-google-common-graph-Graphs$TransposedGraph$1 */
            /* synthetic */ EndpointPair m335x5749e417(EndpointPair r4) {
                return EndpointPair.m332of(this.this$0.delegate(), r4.nodeV(), r4.nodeU());
            }
        }

        TransposedGraph(Graph<N> r1) {
            this.graph = r1;
        }

        static /* synthetic */ Graph access$000(TransposedGraph r1) {
            return r1.graph;
        }

        @Override // com.google.common.graph.ForwardingGraph
        /* bridge */ /* synthetic */ BaseGraph delegate() {
            return delegate();
        }

        @Override // com.google.common.graph.ForwardingGraph
        Graph<N> delegate() {
            return this.graph;
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public boolean hasEdgeConnecting(EndpointPair<N> r3) {
            return delegate().hasEdgeConnecting(Graphs.transpose(r3));
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public boolean hasEdgeConnecting(N r2, N r3) {
            return delegate().hasEdgeConnecting(r3, r2);
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public int inDegree(N r2) {
            return delegate().outDegree(r2);
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public Set<EndpointPair<N>> incidentEdges(N r2) {
            return new C08041(this, this, r2);
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public int outDegree(N r2) {
            return delegate().inDegree(r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
        public /* bridge */ /* synthetic */ Iterable predecessors(Object r1) {
            return predecessors(r1);
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
        public Set<N> predecessors(N r2) {
            return delegate().successors(r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
        public /* bridge */ /* synthetic */ Iterable successors(Object r1) {
            return successors(r1);
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
        public Set<N> successors(N r2) {
            return delegate().predecessors(r2);
        }
    }

    private static class TransposedNetwork<N, E> extends ForwardingNetwork<N, E> {
        private final Network<N, E> network;

        TransposedNetwork(Network<N, E> r1) {
            this.network = r1;
        }

        static /* synthetic */ Network access$200(TransposedNetwork r1) {
            return r1.network;
        }

        @Override // com.google.common.graph.ForwardingNetwork
        Network<N, E> delegate() {
            return this.network;
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        @CheckForNull
        public E edgeConnectingOrNull(EndpointPair<N> r3) {
            return delegate().edgeConnectingOrNull(Graphs.transpose(r3));
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        @CheckForNull
        public E edgeConnectingOrNull(N r2, N r3) {
            return delegate().edgeConnectingOrNull(r3, r2);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public Set<E> edgesConnecting(EndpointPair<N> r3) {
            return delegate().edgesConnecting(Graphs.transpose(r3));
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public Set<E> edgesConnecting(N r2, N r3) {
            return delegate().edgesConnecting(r3, r2);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public boolean hasEdgeConnecting(EndpointPair<N> r3) {
            return delegate().hasEdgeConnecting(Graphs.transpose(r3));
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public boolean hasEdgeConnecting(N r2, N r3) {
            return delegate().hasEdgeConnecting(r3, r2);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public int inDegree(N r2) {
            return delegate().outDegree(r2);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.Network
        public Set<E> inEdges(N r2) {
            return delegate().outEdges(r2);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.Network
        public EndpointPair<N> incidentNodes(E r5) {
            EndpointPair<N> r0 = delegate().incidentNodes(r5);
            return EndpointPair.m333of(this.network, r0.nodeV(), r0.nodeU());
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public int outDegree(N r2) {
            return delegate().inDegree(r2);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.Network
        public Set<E> outEdges(N r2) {
            return delegate().inEdges(r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.PredecessorsFunction
        public /* bridge */ /* synthetic */ Iterable predecessors(Object r1) {
            return predecessors(r1);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.PredecessorsFunction
        public Set<N> predecessors(N r2) {
            return delegate().successors(r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.SuccessorsFunction
        public /* bridge */ /* synthetic */ Iterable successors(Object r1) {
            return successors(r1);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.SuccessorsFunction
        public Set<N> successors(N r2) {
            return delegate().predecessors(r2);
        }
    }

    private static class TransposedValueGraph<N, V> extends ForwardingValueGraph<N, V> {
        private final ValueGraph<N, V> graph;

        TransposedValueGraph(ValueGraph<N, V> r1) {
            this.graph = r1;
        }

        static /* synthetic */ ValueGraph access$100(TransposedValueGraph r1) {
            return r1.graph;
        }

        @Override // com.google.common.graph.ForwardingValueGraph
        ValueGraph<N, V> delegate() {
            return this.graph;
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.ValueGraph
        @CheckForNull
        public V edgeValueOrDefault(EndpointPair<N> r3, @CheckForNull V r4) {
            return delegate().edgeValueOrDefault(Graphs.transpose(r3), r4);
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.ValueGraph
        @CheckForNull
        public V edgeValueOrDefault(N r2, N r3, @CheckForNull V r4) {
            return delegate().edgeValueOrDefault(r3, r2, r4);
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public boolean hasEdgeConnecting(EndpointPair<N> r3) {
            return delegate().hasEdgeConnecting(Graphs.transpose(r3));
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public boolean hasEdgeConnecting(N r2, N r3) {
            return delegate().hasEdgeConnecting(r3, r2);
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public int inDegree(N r2) {
            return delegate().outDegree(r2);
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public int outDegree(N r2) {
            return delegate().inDegree(r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
        public /* bridge */ /* synthetic */ Iterable predecessors(Object r1) {
            return predecessors(r1);
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
        public Set<N> predecessors(N r2) {
            return delegate().successors(r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
        public /* bridge */ /* synthetic */ Iterable successors(Object r1) {
            return successors(r1);
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
        public Set<N> successors(N r2) {
            return delegate().predecessors(r2);
        }
    }

    private Graphs() {
    }

    private static boolean canTraverseWithoutReusingEdge(Graph<?> r1, Object r2, @CheckForNull Object r3) {
        if (r1.isDirected() == false) goto L5;
        return true;
    L5:
        if (Objects.equal(r3, r2) == false) goto L11;
        return false;
    L11:
        return true;
    }

    static int checkNonNegative(int r2) {
        if (r2 < 0) goto L4;
        boolean r0 = true;
    L5:
        Preconditions.checkArgument(r0, "Not true that %s is non-negative.", r2);
        return r2;
    L4:
        r0 = false;
        goto L5
    }

    static long checkNonNegative(long r2) {
        if (r2 < 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "Not true that %s is non-negative.", r2);
        return r2;
    L5:
        r0 = false;
        goto L6
    }

    static int checkPositive(int r2) {
        if (r2 <= 0) goto L4;
        boolean r0 = true;
    L5:
        Preconditions.checkArgument(r0, "Not true that %s is positive.", r2);
        return r2;
    L4:
        r0 = false;
        goto L5
    }

    static long checkPositive(long r2) {
        if (r2 <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "Not true that %s is positive.", r2);
        return r2;
    L5:
        r0 = false;
        goto L6
    }

    public static <N> MutableGraph<N> copyOf(Graph<N> r5) {
        MutableGraph<N> r0 = (MutableGraph<N>) GraphBuilder.from(r5).expectedNodeCount(r5.nodes().size()).build();
        Iterator<N> r1 = r5.nodes().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0.addNode(r1.next());
        goto L4
    L6:
        Iterator<EndpointPair<N>> r2 = r5.edges().iterator();
    L8:
        if (r2.hasNext() == false) goto L10;
        EndpointPair<N> r3 = r2.next();
        r0.putEdge(r3.nodeU(), r3.nodeV());
        goto L8
    L10:
        return r0;
    }

    public static <N, E> MutableNetwork<N, E> copyOf(Network<N, E> r6) {
        MutableNetwork<N, E> r0 = (MutableNetwork<N, E>) NetworkBuilder.from(r6).expectedNodeCount(r6.nodes().size()).expectedEdgeCount(r6.edges().size()).build();
        Iterator<N> r1 = r6.nodes().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0.addNode(r1.next());
        goto L4
    L6:
        Iterator<E> r2 = r6.edges().iterator();
    L8:
        if (r2.hasNext() == false) goto L10;
        E r3 = r2.next();
        EndpointPair<N> r4 = r6.incidentNodes(r3);
        r0.addEdge(r4.nodeU(), r4.nodeV(), r3);
        goto L8
    L10:
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <N, V> MutableValueGraph<N, V> copyOf(ValueGraph<N, V> r8) {
        StandardMutableValueGraph r0 = (MutableValueGraph<N, V>) ValueGraphBuilder.from(r8).expectedNodeCount(r8.nodes().size()).build();
        Iterator<N> r1 = r8.nodes().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0.addNode(r1.next());
        goto L4
    L6:
        Iterator<EndpointPair<N>> r2 = r8.edges().iterator();
    L8:
        if (r2.hasNext() == false) goto L10;
        EndpointPair<N> r3 = r2.next();
        r0.putEdgeValue(r3.nodeU(), r3.nodeV(), java.util.Objects.requireNonNull(r8.edgeValueOrDefault(r3.nodeU(), r3.nodeV(), null)));
        goto L8
    L10:
        return r0;
    }

    public static <N> boolean hasCycle(Graph<N> r7) {
        int r0 = r7.edges().size();
        if (r0 != 0) goto L6;
        return false;
    L6:
        if (r7.isDirected() == false) goto L8;
    L10:
        HashMap r2 = Maps.newHashMapWithExpectedSize(r7.nodes().size());
        Iterator<N> r4 = r7.nodes().iterator();
    L12:
        if (r4.hasNext() == false) goto L17;
        if (subgraphHasCycle(r7, r2, r4.next()) == false) goto L12;
        return true;
    L17:
        return false;
    L8:
        if (r0 < r7.nodes().size()) goto L10;
        return true;
    }

    public static boolean hasCycle(Network<?, ?> r2) {
        if (r2.isDirected() == true) goto L11;
        if (r2.allowsParallelEdges() == false) goto L11;
        if (r2.edges().size() <= r2.asGraph().edges().size()) goto L11;
        return true;
    L11:
        return hasCycle(r2.asGraph());
    }

    public static <N> MutableGraph<N> inducedSubgraph(Graph<N> r6, Iterable<? extends N> r7) {
        if ((r7 instanceof Collection) == false) goto L5;
        StandardMutableGraph r0 = (MutableGraph<N>) GraphBuilder.from(r6).expectedNodeCount(((Collection) r7).size()).build();
    L6:
        Iterator<? extends N> r1 = r7.iterator();
    L8:
        if (r1.hasNext() == false) goto L10;
        StandardMutableGraph r2 = r0;
        r2.addNode(r1.next());
        goto L8
    L10:
        Iterator<N> r3 = r0.nodes().iterator();
    L12:
        if (r3.hasNext() == false) goto L21;
        N r4 = r3.next();
        Iterator<N> r5 = r6.successors(r4).iterator();
    L15:
        if (r5.hasNext() == false) goto L12;
        N r8 = r5.next();
        if (r0.nodes().contains(r8) == false) goto L15;
        r0.putEdge(r4, r8);
        goto L15
    L21:
        return r0;
    L5:
        r0 = (MutableGraph<N>) GraphBuilder.from(r6).build();
        goto L6
    }

    public static <N, E> MutableNetwork<N, E> inducedSubgraph(Network<N, E> r7, Iterable<? extends N> r8) {
        if ((r8 instanceof Collection) == false) goto L5;
        StandardMutableNetwork r0 = (MutableNetwork<N, E>) NetworkBuilder.from(r7).expectedNodeCount(((Collection) r8).size()).build();
    L6:
        Iterator<? extends N> r1 = r8.iterator();
    L8:
        if (r1.hasNext() == false) goto L10;
        StandardMutableNetwork r2 = r0;
        r2.addNode(r1.next());
        goto L8
    L10:
        Iterator<E> r3 = r0.nodes().iterator();
    L12:
        if (r3.hasNext() == false) goto L21;
        E r4 = r3.next();
        Iterator<E> r5 = r7.outEdges(r4).iterator();
    L15:
        if (r5.hasNext() == false) goto L12;
        E r6 = r5.next();
        N r9 = r7.incidentNodes(r6).adjacentNode(r4);
        if (r0.nodes().contains(r9) == false) goto L15;
        r0.addEdge(r4, r9, r6);
        goto L15
    L21:
        return r0;
    L5:
        r0 = (MutableNetwork<N, E>) NetworkBuilder.from(r7).build();
        goto L6
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <N, V> MutableValueGraph<N, V> inducedSubgraph(ValueGraph<N, V> r6, Iterable<? extends N> r7) {
        if ((r7 instanceof Collection) == false) goto L5;
        MutableValueGraph r0 = (MutableValueGraph<N, V>) ValueGraphBuilder.from(r6).expectedNodeCount(((Collection) r7).size()).build();
    L6:
        Iterator<? extends N> r1 = r7.iterator();
    L8:
        if (r1.hasNext() == false) goto L10;
        MutableValueGraph r2 = r0;
        r2.addNode(r1.next());
        goto L8
    L10:
        Iterator<N> r3 = r0.nodes().iterator();
    L12:
        if (r3.hasNext() == false) goto L21;
        N r4 = r3.next();
        Iterator<N> r5 = r6.successors(r4).iterator();
    L15:
        if (r5.hasNext() == false) goto L12;
        N r8 = r5.next();
        if (r0.nodes().contains(r8) == false) goto L15;
        MutableValueGraph r9 = r0;
        r9.putEdgeValue(r4, r8, java.util.Objects.requireNonNull(r6.edgeValueOrDefault(r4, r8, null)));
        goto L15
    L21:
        return (MutableValueGraph<N, V>) r0;
    L5:
        r0 = (MutableValueGraph<N, V>) ValueGraphBuilder.from(r6).build();
        goto L6
    }

    public static <N> ImmutableSet<N> reachableNodes(Graph<N> r2, N r3) {
        Preconditions.checkArgument(r2.nodes().contains(r3), "Node %s is not an element of this graph.", r3);
        return ImmutableSet.copyOf(Traverser.forGraph(r2).breadthFirst(r3));
    }

    private static <N> boolean subgraphHasCycle(Graph<N> r8, Map<Object, NodeVisitState> r9, N r10) {
        ArrayDeque r0 = new ArrayDeque();
        r0.addLast(new NodeAndRemainingSuccessors(r10));
    L4:
        if (r0.isEmpty() == true) goto L25;
        NodeAndRemainingSuccessors r1 = (NodeAndRemainingSuccessors) r0.removeLast();
        NodeAndRemainingSuccessors r2 = (NodeAndRemainingSuccessors) r0.peekLast();
        r0.addLast(r1);
        N r3 = r1.node;
        if (r2 != null) goto L8;
        N r4 = null;
    L10:
        if (r1.remainingSuccessors != null) goto L20;
        NodeVisitState r5 = r9.get(r3);
        if (r5 == NodeVisitState.COMPLETE) goto L13;
        if (r5 == NodeVisitState.PENDING) goto L16;
        r9.put(r3, NodeVisitState.PENDING);
        r1.remainingSuccessors = new ArrayDeque(r8.successors(r3));
        goto L20
    L16:
        return true;
    L13:
        r0.removeLast();
    L20:
        if (r1.remainingSuccessors.isEmpty() == true) goto L24;
        N r6 = r1.remainingSuccessors.remove();
        if (canTraverseWithoutReusingEdge(r8, r6, r4) == false) goto L24;
        r0.addLast(new NodeAndRemainingSuccessors(r6));
    L24:
        r0.removeLast();
        r9.put(r3, NodeVisitState.COMPLETE);
        goto L4
    L8:
        r4 = r2.node;
        goto L10
    L25:
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <N> ImmutableGraph<N> transitiveClosure(Graph<N> r10) {
        ImmutableGraph.Builder<N1> r0 = GraphBuilder.from(r10).allowsSelfLoops(true).immutable();
        if (r10.isDirected() == false) goto L13;
        Iterator<N> r1 = r10.nodes().iterator();
    L6:
        if (r1.hasNext() == false) goto L28;
        N r2 = r1.next();
        UnmodifiableIterator r3 = reachableNodes(r10, r2).iterator();
    L9:
        if (r3.hasNext() == false) goto L6;
        r0.putEdge(r2, r3.next());
    L28:
        return r0.build();
    L13:
        HashSet r4 = new HashSet();
        Iterator<N> r5 = r10.nodes().iterator();
    L15:
        if (r5.hasNext() == false) goto L28;
        N r6 = r5.next();
        if (r4.contains(r6) == true) goto L15;
        ImmutableSet r7 = reachableNodes(r10, r6);
        r4.addAll(r7);
        int r8 = 1;
        Iterator<E> r9 = r7.iterator();
    L20:
        if (r9.hasNext() == false) goto L15;
        Object r11 = r9.next();
        int r12 = r8 + 1;
        Iterator r13 = Iterables.limit(r7, r8).iterator();
    L23:
        if (r13.hasNext() == false) goto L25;
        r0.putEdge(r11, r13.next());
        goto L23
    L25:
        r8 = r12;
        goto L20
    }

    static <N> EndpointPair<N> transpose(EndpointPair<N> r2) {
        if (r2.isOrdered() == true) goto L5;
        return r2;
    L5:
        return EndpointPair.ordered(r2.target(), r2.source());
    }

    public static <N> Graph<N> transpose(Graph<N> r1) {
        if (r1.isDirected() == true) goto L6;
        return r1;
    L6:
        if ((r1 instanceof TransposedGraph) == false) goto L10;
        return TransposedGraph.access$000((TransposedGraph) r1);
    L10:
        return new TransposedGraph(r1);
    }

    public static <N, E> Network<N, E> transpose(Network<N, E> r1) {
        if (r1.isDirected() == true) goto L6;
        return r1;
    L6:
        if ((r1 instanceof TransposedNetwork) == false) goto L10;
        return TransposedNetwork.access$200((TransposedNetwork) r1);
    L10:
        return new TransposedNetwork(r1);
    }

    public static <N, V> ValueGraph<N, V> transpose(ValueGraph<N, V> r1) {
        if (r1.isDirected() == true) goto L6;
        return r1;
    L6:
        if ((r1 instanceof TransposedValueGraph) == false) goto L10;
        return TransposedValueGraph.access$100((TransposedValueGraph) r1);
    L10:
        return new TransposedValueGraph(r1);
    }
}
