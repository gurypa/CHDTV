package com.google.common.graph;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import com.google.errorprone.annotations.Immutable;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@Immutable(containerOf = {"N"})
@ElementTypesAreNonnullByDefault
public abstract class EndpointPair<N> implements Iterable<N> {
    private final N nodeU;
    private final N nodeV;

    /* JADX INFO: renamed from: com.google.common.graph.EndpointPair$1 */
    static /* synthetic */ class C08021 {
    }

    private static final class Ordered<N> extends EndpointPair<N> {
        private Ordered(N r2, N r3) {
            super(r2, r3, null);
        }

        /* synthetic */ Ordered(Object r1, Object r2, C08021 r3) {
            this(r1, r2);
        }

        @Override // com.google.common.graph.EndpointPair
        public boolean equals(@CheckForNull Object r6) {
            if (r6 != this) goto L6;
            return true;
        L6:
            if ((r6 instanceof EndpointPair) == true) goto L8;
            return false;
        L8:
            EndpointPair r1 = (EndpointPair) r6;
            if (isOrdered() == r1.isOrdered()) goto L12;
            return false;
        L12:
            if (source().equals(r1.source()) == false) goto L17;
            if (target().equals(r1.target()) == false) goto L17;
            return true;
        L17:
            return false;
        }

        @Override // com.google.common.graph.EndpointPair
        public int hashCode() {
            return Objects.hashCode(new Object[]{source(), target()});
        }

        @Override // com.google.common.graph.EndpointPair
        public boolean isOrdered() {
            return true;
        }

        @Override // com.google.common.graph.EndpointPair, java.lang.Iterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        @Override // com.google.common.graph.EndpointPair
        public N source() {
            return nodeU();
        }

        @Override // com.google.common.graph.EndpointPair
        public N target() {
            return nodeV();
        }

        public String toString() {
            return "<" + source() + " -> " + target() + ">";
        }
    }

    private static final class Unordered<N> extends EndpointPair<N> {
        private Unordered(N r2, N r3) {
            super(r2, r3, null);
        }

        /* synthetic */ Unordered(Object r1, Object r2, C08021 r3) {
            this(r1, r2);
        }

        @Override // com.google.common.graph.EndpointPair
        public boolean equals(@CheckForNull Object r6) {
            if (r6 != this) goto L6;
            return true;
        L6:
            if ((r6 instanceof EndpointPair) == true) goto L8;
            return false;
        L8:
            EndpointPair r1 = (EndpointPair) r6;
            if (isOrdered() == r1.isOrdered()) goto L12;
            return false;
        L12:
            if (nodeU().equals(r1.nodeU()) == false) goto L16;
            return nodeV().equals(r1.nodeV());
        L16:
            if (nodeU().equals(r1.nodeV()) == false) goto L21;
            if (nodeV().equals(r1.nodeU()) == false) goto L21;
            return true;
        L21:
            return false;
        }

        @Override // com.google.common.graph.EndpointPair
        public int hashCode() {
            return nodeU().hashCode() + nodeV().hashCode();
        }

        @Override // com.google.common.graph.EndpointPair
        public boolean isOrdered() {
            return false;
        }

        @Override // com.google.common.graph.EndpointPair, java.lang.Iterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        @Override // com.google.common.graph.EndpointPair
        public N source() {
            throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
        }

        @Override // com.google.common.graph.EndpointPair
        public N target() {
            throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
        }

        public String toString() {
            return "[" + nodeU() + ", " + nodeV() + "]";
        }
    }

    private EndpointPair(N r2, N r3) {
        this.nodeU = (N) Preconditions.checkNotNull(r2);
        this.nodeV = (N) Preconditions.checkNotNull(r3);
    }

    /* synthetic */ EndpointPair(Object r1, Object r2, C08021 r3) {
        this(r1, r2);
    }

    /* JADX INFO: renamed from: of */
    static <N> EndpointPair<N> m332of(Graph<?> r1, N r2, N r3) {
        if (r1.isDirected() == false) goto L6;
        return ordered(r2, r3);
    L6:
        return unordered(r2, r3);
    }

    /* JADX INFO: renamed from: of */
    static <N> EndpointPair<N> m333of(Network<?, ?> r1, N r2, N r3) {
        if (r1.isDirected() == false) goto L6;
        return ordered(r2, r3);
    L6:
        return unordered(r2, r3);
    }

    public static <N> EndpointPair<N> ordered(N r2, N r3) {
        return new Ordered(r2, r3, null);
    }

    public static <N> EndpointPair<N> unordered(N r2, N r3) {
        return new Unordered(r3, r2, null);
    }

    public final N adjacentNode(N r4) {
        if (r4.equals(this.nodeU) == false) goto L7;
        return this.nodeV;
    L7:
        if (r4.equals(this.nodeV) == false) goto L11;
        return this.nodeU;
    L11:
        throw new IllegalArgumentException("EndpointPair " + this + " does not contain node " + r4);
    }

    public abstract boolean equals(@CheckForNull Object r1);

    public abstract int hashCode();

    public abstract boolean isOrdered();

    @Override // java.lang.Iterable
    public final UnmodifiableIterator<N> iterator() {
        return Iterators.forArray(new Object[]{this.nodeU, this.nodeV});
    }

    @Override // java.lang.Iterable
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return iterator();
    }

    public final N nodeU() {
        return this.nodeU;
    }

    public final N nodeV() {
        return this.nodeV;
    }

    public abstract N source();

    public abstract N target();
}
