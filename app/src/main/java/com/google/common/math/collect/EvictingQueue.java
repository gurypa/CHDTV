package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class EvictingQueue<E> extends ForwardingQueue<E> implements Serializable {
    private static final long serialVersionUID = 0;
    private final Queue<E> delegate;
    final int maxSize;

    private EvictingQueue(int r3) {
        if (r3 < 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "maxSize (%s) must >= 0", r3);
        this.delegate = new ArrayDeque(r3);
        this.maxSize = r3;
        return;
    L5:
        r0 = false;
        goto L6
    }

    public static <E> EvictingQueue<E> create(int r1) {
        return new EvictingQueue(r1);
    }

    @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Queue
    public boolean add(E r4) {
        Preconditions.checkNotNull(r4);
        if (this.maxSize != 0) goto L6;
        return true;
    L6:
        if (size() != this.maxSize) goto L8;
        this.delegate.remove();
    L8:
        this.delegate.add(r4);
        return true;
    }

    @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> r3) {
        int r0 = r3.size();
        if (r0 < this.maxSize) goto L7;
        clear();
        return Iterables.addAll(this, Iterables.skip(r3, r0 - this.maxSize));
    L7:
        return standardAddAll(r3);
    }

    @Override // com.google.common.collect.ForwardingQueue, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingQueue, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Collection delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingQueue, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected Queue<E> delegate() {
        return this.delegate;
    }

    @Override // com.google.common.collect.ForwardingQueue, java.util.Queue
    public boolean offer(E r2) {
        return add(r2);
    }

    public int remainingCapacity() {
        return this.maxSize - size();
    }

    @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
    public Object[] toArray() {
        return super.toArray();
    }
}
