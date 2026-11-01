package com.google.common.collect;

import java.util.Collection;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
@Deprecated
public abstract class ForwardingBlockingDeque<E> extends ForwardingDeque<E> implements BlockingDeque<E> {
    protected ForwardingBlockingDeque() {
    }

    @Override // com.google.common.collect.ForwardingDeque, com.google.common.collect.ForwardingQueue, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingDeque, com.google.common.collect.ForwardingQueue, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Collection delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingDeque, com.google.common.collect.ForwardingQueue, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Deque delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingDeque, com.google.common.collect.ForwardingQueue, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Queue delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingDeque, com.google.common.collect.ForwardingQueue, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected abstract BlockingDeque<E> delegate();

    @Override // java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> r2) {
        return delegate().drainTo(r2);
    }

    @Override // java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> r2, int r3) {
        return delegate().drainTo(r2, r3);
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.concurrent.BlockingQueue
    public boolean offer(E r2, long r3, TimeUnit r5) throws InterruptedException {
        return delegate().offer(r2, r3, r5);
    }

    @Override // java.util.concurrent.BlockingDeque
    public boolean offerFirst(E r2, long r3, TimeUnit r5) throws InterruptedException {
        return delegate().offerFirst(r2, r3, r5);
    }

    @Override // java.util.concurrent.BlockingDeque
    public boolean offerLast(E r2, long r3, TimeUnit r5) throws InterruptedException {
        return delegate().offerLast(r2, r3, r5);
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.concurrent.BlockingQueue
    @CheckForNull
    public E poll(long r2, TimeUnit r4) throws InterruptedException {
        return delegate().poll(r2, r4);
    }

    @Override // java.util.concurrent.BlockingDeque
    @CheckForNull
    public E pollFirst(long r2, TimeUnit r4) throws InterruptedException {
        return delegate().pollFirst(r2, r4);
    }

    @Override // java.util.concurrent.BlockingDeque
    @CheckForNull
    public E pollLast(long r2, TimeUnit r4) throws InterruptedException {
        return delegate().pollLast(r2, r4);
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.concurrent.BlockingQueue
    public void put(E r2) throws InterruptedException {
        delegate().put(r2);
    }

    @Override // java.util.concurrent.BlockingDeque
    public void putFirst(E r2) throws InterruptedException {
        delegate().putFirst(r2);
    }

    @Override // java.util.concurrent.BlockingDeque
    public void putLast(E r2) throws InterruptedException {
        delegate().putLast(r2);
    }

    @Override // java.util.concurrent.BlockingQueue
    public int remainingCapacity() {
        return delegate().remainingCapacity();
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.concurrent.BlockingQueue
    public E take() throws InterruptedException {
        return delegate().take();
    }

    @Override // java.util.concurrent.BlockingDeque
    public E takeFirst() throws InterruptedException {
        return delegate().takeFirst();
    }

    @Override // java.util.concurrent.BlockingDeque
    public E takeLast() throws InterruptedException {
        return delegate().takeLast();
    }
}
