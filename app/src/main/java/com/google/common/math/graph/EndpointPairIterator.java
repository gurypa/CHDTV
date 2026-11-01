package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class EndpointPairIterator<N> extends AbstractIterator<EndpointPair<N>> {
    private final BaseGraph<N> graph;

    @CheckForNull
    N node;
    private final Iterator<N> nodeIterator;
    Iterator<N> successorIterator;

    /* JADX INFO: renamed from: com.google.common.graph.EndpointPairIterator$1 */
    static /* synthetic */ class C08031 {
    }

    private static final class Directed<N> extends EndpointPairIterator<N> {
        private Directed(BaseGraph<N> r2) {
            super(r2, null);
        }

        /* synthetic */ Directed(BaseGraph r1, C08031 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.AbstractIterator
        @CheckForNull
        protected EndpointPair<N> computeNext() {
        L3:
            if (this.successorIterator.hasNext() == true) goto L5;
            if (advance() == true) goto L3;
            return endOfData();
        L5:
            return EndpointPair.ordered(Objects.requireNonNull(this.node), this.successorIterator.next());
        }

        @Override // com.google.common.collect.AbstractIterator
        @CheckForNull
        protected /* bridge */ /* synthetic */ Object computeNext() {
            return computeNext();
        }
    }

    private static final class Undirected<N> extends EndpointPairIterator<N> {

        @CheckForNull
        private Set<N> visitedNodes;

        private Undirected(BaseGraph<N> r2) {
            super(r2, null);
            this.visitedNodes = Sets.newHashSetWithExpectedSize(r2.nodes().size() + 1);
        }

        /* synthetic */ Undirected(BaseGraph r1, C08031 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.AbstractIterator
        @CheckForNull
        protected EndpointPair<N> computeNext() {
        L2:
            Objects.requireNonNull(this.visitedNodes);
        L4:
            if (this.successorIterator.hasNext() == false) goto L10;
            N r0 = this.successorIterator.next();
            if (this.visitedNodes.contains(r0) == true) goto L4;
            return EndpointPair.unordered(Objects.requireNonNull(this.node), r0);
        L10:
            this.visitedNodes.add(this.node);
            if (advance() == true) goto L2;
            this.visitedNodes = null;
            return endOfData();
        }

        @Override // com.google.common.collect.AbstractIterator
        @CheckForNull
        protected /* bridge */ /* synthetic */ Object computeNext() {
            return computeNext();
        }
    }

    private EndpointPairIterator(BaseGraph<N> r2) {
        this.node = null;
        this.successorIterator = ImmutableSet.m259of().iterator();
        this.graph = r2;
        this.nodeIterator = r2.nodes().iterator();
    }

    /* synthetic */ EndpointPairIterator(BaseGraph r1, C08031 r2) {
        this(r1);
    }

    /* JADX INFO: renamed from: of */
    static <N> EndpointPairIterator<N> m334of(BaseGraph<N> r2) {
        C08031 r1 = null;
        if (r2.isDirected() == false) goto L6;
        return new Directed(r2, r1);
    L6:
        return new Undirected(r2, r1);
    }

    final boolean advance() {
        Preconditions.checkState(!this.successorIterator.hasNext());
        if (this.nodeIterator.hasNext() == true) goto L6;
        return false;
    L6:
        this.node = this.nodeIterator.next();
        this.successorIterator = this.graph.successors(this.node).iterator();
        return true;
    }
}
