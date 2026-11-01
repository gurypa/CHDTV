package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class EdgesConnecting<E> extends AbstractSet<E> {
    private final Map<?, E> nodeToOutEdge;
    private final Object targetNode;

    EdgesConnecting(Map<?, E> r2, Object r3) {
        this.nodeToOutEdge = (Map) Preconditions.checkNotNull(r2);
        this.targetNode = Preconditions.checkNotNull(r3);
    }

    @CheckForNull
    private E getConnectingEdge() {
        return this.nodeToOutEdge.get(this.targetNode);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@CheckForNull Object r3) {
        E r0 = getConnectingEdge();
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (r0.equals(r3) == false) goto L7;
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public UnmodifiableIterator<E> iterator() {
        E r0 = getConnectingEdge();
        if (r0 != null) goto L6;
        return ImmutableSet.m259of().iterator();
    L6:
        return Iterators.singletonIterator(r0);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        if (getConnectingEdge() != null) goto L5;
        return 0;
    L5:
        return 1;
    }
}
