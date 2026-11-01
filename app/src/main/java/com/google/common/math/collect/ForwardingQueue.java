package com.google.common.collect;

import java.util.Collection;
import java.util.Queue;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingQueue<E> extends ForwardingCollection<E> implements Queue<E> {
    protected ForwardingQueue() {
    }

    @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Collection delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected abstract Queue<E> delegate();

    @Override // java.util.Queue
    @ParametricNullness
    public E element() {
        return delegate().element();
    }

    public boolean offer(@ParametricNullness E r2) {
        return delegate().offer(r2);
    }

    @Override // java.util.Queue
    @CheckForNull
    public E peek() {
        return delegate().peek();
    }

    @Override // java.util.Queue
    @CheckForNull
    public E poll() {
        return delegate().poll();
    }

    @Override // java.util.Queue
    @ParametricNullness
    public E remove() {
        return delegate().remove();
    }

    protected boolean standardOffer(@ParametricNullness E r3) {
        return add(r3);
    L5:
        return false;
    }

    @CheckForNull
    protected E standardPeek() {
        return element();
    L5:
        return null;
    }

    @CheckForNull
    protected E standardPoll() {
        return remove();
    L5:
        return null;
    }
}
