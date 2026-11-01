package com.google.common.util.concurrent;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class ForwardingCondition implements Condition {
    ForwardingCondition() {
    }

    @Override // java.util.concurrent.locks.Condition
    public void await() throws InterruptedException {
        delegate().await();
    }

    @Override // java.util.concurrent.locks.Condition
    public boolean await(long r2, TimeUnit r4) throws InterruptedException {
        return delegate().await(r2, r4);
    }

    @Override // java.util.concurrent.locks.Condition
    public long awaitNanos(long r3) throws InterruptedException {
        return delegate().awaitNanos(r3);
    }

    @Override // java.util.concurrent.locks.Condition
    public void awaitUninterruptibly() {
        delegate().awaitUninterruptibly();
    }

    @Override // java.util.concurrent.locks.Condition
    public boolean awaitUntil(Date r2) throws InterruptedException {
        return delegate().awaitUntil(r2);
    }

    abstract Condition delegate();

    @Override // java.util.concurrent.locks.Condition
    public void signal() {
        delegate().signal();
    }

    @Override // java.util.concurrent.locks.Condition
    public void signalAll() {
        delegate().signalAll();
    }
}
