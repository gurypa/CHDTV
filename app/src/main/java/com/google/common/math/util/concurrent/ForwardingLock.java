package com.google.common.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class ForwardingLock implements Lock {
    ForwardingLock() {
    }

    abstract Lock delegate();

    @Override // java.util.concurrent.locks.Lock
    public void lock() {
        delegate().lock();
    }

    @Override // java.util.concurrent.locks.Lock
    public void lockInterruptibly() throws InterruptedException {
        delegate().lockInterruptibly();
    }

    @Override // java.util.concurrent.locks.Lock
    public Condition newCondition() {
        return delegate().newCondition();
    }

    @Override // java.util.concurrent.locks.Lock
    public boolean tryLock() {
        return delegate().tryLock();
    }

    @Override // java.util.concurrent.locks.Lock
    public boolean tryLock(long r2, TimeUnit r4) throws InterruptedException {
        return delegate().tryLock(r2, r4);
    }

    @Override // java.util.concurrent.locks.Lock
    public void unlock() {
        delegate().unlock();
    }
}
